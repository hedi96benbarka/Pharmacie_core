/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.config.AbstractNativeConsumer;
import com.csys.pharmacie.config.KafkaConfigurationConsumer;
import com.csys.pharmacie.helper.EnumCrudMethod;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Administrateur
 */
@Service
public class NativeKafkaTransferCompanyToBranch extends AbstractNativeConsumer {

    @Value("${kafka.group_id}")
    private String groupId;

    @Value("${kafka.topic.transfer-company-to-branch-management}")
    private String topic;

    public NativeKafkaTransferCompanyToBranch(@Lazy TransfertBranchFacadeService transfertBranchService, KafkaConfigurationConsumer kafkaConfiguration, TopicPartitionOffsetService topicPartitionOffsetService) {
        super(kafkaConfiguration, topicPartitionOffsetService);
        this.transfertBranchService = transfertBranchService;
    }

    private final TransfertBranchFacadeService transfertBranchService;

    @Override
    public void start() {
        log.info("Kafka consumer starting Transfert company to branch  ...");
        this.kafkaConsumer = new KafkaConsumer(getKafkaConfiguration().getConsumerConfig(), new StringDeserializer(), new JsonDeserializer(TransfertCompanyBranchDTO.class));
        /* When the application is shutingdown the shutdown method will be called in order to close 
        * the consumer before exiting. 
        * Closing the consumer will informe the group coordinator about the death of this consumer   
         */
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        //Starting the consumer loop in a new thread. One consumer per thread is the rule
        Thread consumerThread = new Thread(() -> {
            
            try {
                kafkaConsumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {

                    /* will be invoked whenever a partition is revoked from this consumer due to its death.
                     * Use it to clean things up before revoking th partition (commiting offset, save to database) 
                     */
                    @Override
                    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                        log.info("***************partition  Transfert company to branch  {} is beeing revoked****************", partitions.stream().map(TopicPartition::toString).collect(Collectors.toList()));
                        if (!partitions.isEmpty()) {
                            Map<TopicPartition, OffsetAndMetadata> topicPartionsOffset = getTopicPartitionOffsetService().findCommittedOffsetsAndMetaDataByGroupId(partitions, groupId);
                            kafkaConsumer.commitSync(topicPartionsOffset);
                        }
                    }

                    /* Use it to force the consumer to start polling from your latest consumed offset which is stored in your database. This enforcement is done 
                     * with the seek() method
                     */
                    @Override
                    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                        log.info("*************partition Transfert company to branch   {} is beeing assigned************", partitions.stream().map(TopicPartition::toString).collect(Collectors.toList()));
                        if (!partitions.isEmpty()) {
                            Map<TopicPartition, Long> topicPartionsOffset = getTopicPartitionOffsetService().findCommittedOffsetsByGroupId(partitions, groupId);
                            topicPartionsOffset.forEach((topicPartition, offset) -> kafkaConsumer.seek(topicPartition, offset));
                        }
                    }
                });
                log.info("Kafka Transfert company to branch started");
                while (!closed.get()) {
//                    log.trace("verifY trace is not shown in  this class ");
                    log.info("*********start polling Transfert company to branch  ***********");
                    ConsumerRecords<String, TransfertCompanyBranchDTO> records = kafkaConsumer.poll(Duration.ofMinutes(60));
                    log.debug("records  {}", records);
//                    log.trace("  records.iterator next {}", records.iterator().next() );             
                    log.debug("finished polling Transfert company to branch  ");
//                    log.debug("polling Transfert company to branch   from offset {}", records.iterator().next().offset());
                    log.debug("Got {} messages Transfert company to branch  ", records.count());
                    try {
                        SecurityContextHolder.getContext()
                                .setAuthentication(new UsernamePasswordAuthenticationToken(     "AUTO",  "AUTO",  Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))));
                        records.forEach(record -> {
                            log.debug("Consuming message branch Transfert company to branch  value {} in offset {}and partition {}", record.value(), record.offset(), record.partition());
                            log.debug("** consumer ** create ***** save record  Transfert company to branch  with code ", record.value().getNumBon());
                            transfertBranchService.saveRecord(record, groupId);
                            log.debug(" ****************just saved Record transfertCompanyBranchDTO  company to branch**************** ");
                        });
                        kafkaConsumer.commitSync();
                    } catch (Exception e) {
                        log.error("***********  transfert company to branch  ***********first catch part/ seeek partition  again *********");
                        String errorMessage = String.valueOf(" ********** my  exception  ********** :");
                        if (e.getCause() != null) {
                            errorMessage = errorMessage.concat(" **********exception cuase*********").concat(e.getCause().toString());    
                            if (e.getCause().getCause() != null) {
                            errorMessage = errorMessage.concat("************** exception cause  cause  est: ").concat(e.getCause().getCause().toString());
                        }
                        }
                    
                        if (e.getMessage() != null) {
                            errorMessage = errorMessage.concat("************** message exception est ********* : ")
                                    .concat(e.getMessage());
                        } else if (e.getStackTrace() != null) {
                            errorMessage = errorMessage.concat("*************** message stack  est : ").concat(Arrays.toString(e.getStackTrace()));    }
                        log.error("************ {}***************", errorMessage);
//                        log.debug("error message Transfert reception in {}", records.partitions());
                        Map<TopicPartition, Long> topicPartionsOffset = getTopicPartitionOffsetService().findCommittedOffsetsByGroupId(records.partitions(), groupId);
                        topicPartionsOffset.forEach((topicPartition, offset) -> kafkaConsumer.seek(topicPartition, offset)); } }
            } catch (WakeupException e) {
                // Ignore exception if closing
                log.error("***********  transfert company to branch  ***********second catch partWakeupException *********");
                if (!closed.get()) {
                    throw e;
                }
            } catch (Exception e) {
                log.error("***********  transfert company to branch  ***********third catch part/ before closing kafka consumer *********");
                String errorMessage = String.valueOf(" ********** my  exception  ********** :");
                if (e.getCause() != null) {
                    errorMessage = errorMessage.concat(" **********exception cuase*********").concat(e.getCause().toString());
                    if (e.getCause().getCause() != null) {
                        errorMessage = errorMessage.concat("************** exception cause  cause  est: ").concat(e.getCause().getCause().toString());
                    }
                }
                if (e.getMessage() != null) {
                    errorMessage = errorMessage.concat("************** message exception est ********* : ")
                            .concat(e.getMessage());
                } else if (e.getStackTrace() != null) {
                    errorMessage = errorMessage.concat("*************** message stack  est : ").concat(Arrays.toString(e.getStackTrace()));
                }
                log.error("*****final Exeception before closing kafka consumer est*************** ****** {}***************", errorMessage);
            }
            finally {
                kafkaConsumer.close();
            }
        });
        consumerThread.start();
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void namee() {
        log.info("{}", this.getClass().getName());
    }

    public String getTopic() {
        return topic;
    }

}
