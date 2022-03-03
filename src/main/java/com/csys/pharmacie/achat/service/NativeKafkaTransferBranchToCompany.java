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
public class NativeKafkaTransferBranchToCompany extends AbstractNativeConsumer {

    @Value("${kafka.group_id}")
    private String groupId;

    static String topicTransferBranchToCompany;

    @Value("${kafka.topic.transfer-branch-to-company-management}")
    public void setTopicTransferToCompany(String topic) {
        topicTransferBranchToCompany = topic;
    }

    public NativeKafkaTransferBranchToCompany(TransfertCompanyFacadeService transfertCompanyService, KafkaConfigurationConsumer kafkaConfiguration, TopicPartitionOffsetService topicPartitionOffsetService) {
        super(kafkaConfiguration, topicPartitionOffsetService);
        this.transfertCompanyService = transfertCompanyService;
    }

    private final TransfertCompanyFacadeService transfertCompanyService;

    @Override
    public void start() {
        log.info("Kafka consumer starting Transfer branch to  company  ...");
        this.kafkaConsumer = new KafkaConsumer(getKafkaConfiguration().getConsumerConfig(), new StringDeserializer(), new JsonDeserializer(TransfertCompanyBranchDTO.class));
        /* When the application is shutingdown the shutdown method will be called in order to close 
        * the consumer before exiting. 
        * Closing the consumer will informe the group coordinator about the death of this consumer   
         */
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        //Starting the consumer loop in a new thread. One consumer per thread is the rule
        Thread consumerThread = new Thread(() -> {
            try {
                kafkaConsumer.subscribe(Collections.singletonList(topicTransferBranchToCompany), new ConsumerRebalanceListener() {

                    /* will be invoked whenever a partition is revoked from this consumer due to its death.
                     * Use it to clean things up before revoking th partition (commiting offset, save to database) 
                     */
                    @Override
                    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                        log.info("***************partition  Transfert  branch to  company  {} is beeing revoked****************", partitions.stream().map(TopicPartition::toString).collect(Collectors.toList()));
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
                        log.info("*************partition Transfert branch to company {} is beeing assigned************", partitions.stream().map(TopicPartition::toString).collect(Collectors.toList()));
                        if (!partitions.isEmpty()) {
                            Map<TopicPartition, Long> topicPartionsOffset = getTopicPartitionOffsetService().findCommittedOffsetsByGroupId(partitions, groupId);
                            topicPartionsOffset.forEach((topicPartition, offset) -> kafkaConsumer.seek(topicPartition, offset));
                        }
                    }
                });
                log.info("Kafka Transfert branch to company  consumer started");
                while (!closed.get()) {
                    log.info("*********start polling Transfert branch to company***********");

                    ConsumerRecords<String, TransfertCompanyBranchDTO> records = kafkaConsumer.poll(Duration.ofMinutes(4));

                    log.debug("finished polling Transfert branch to company");
//                    log.debug("polling Transfert to company  from offset {}", records.iterator().next().offset());
                    log.debug("Got {} messages Transfert branch to company ", records.count());
                    try {
                        SecurityContextHolder.getContext()
                                .setAuthentication(new UsernamePasswordAuthenticationToken(
                                        "AUTO",
                                        "AUTO",
                                        Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))));

                        records.forEach(record -> {
                            log.debug("Consuming message branch Transfert branch to company value {} in offset {}and partition {}", record.value(), record.offset(), record.partition());

                            EnumCrudMethod action = record.value().getAction();

//                            switch (action) {
//                                case CREATE: {
                            log.debug("** consumer ** create ***** save record  Transfert branch to company with code ", record.value().getNumBon());
                            TransfertCompanyBranchDTO transfertCompanyBranchDTO = transfertCompanyService.saveRecordAfterReturnTransfer(record, groupId);
                            log.debug(" ****************just saved Record transfertCompanyBranchDTO branch to company  **************** ");//                                    break;
//                                }
////                                case UPDATE: {
////                                    log.debug("** consumer ** update ***** update article immo record with code ", record.value().getCode());
//////                                   ArticleECDTO fournisseurRecordDTO=new FournisseurRecordDTO(record.partition(), rec, groupId);
////                                    BonRecepDTO bonRecepDto = factureBAService.updateRecord(record, groupId);
////                                    log.debug(" just updated Record immo : {}", BonRecepDTO.toString());
////                                    break;
////                                }
//                            }
                        });
                        kafkaConsumer.commitSync();
                    } catch (Exception e) {

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
                        log.error("************ {}***************", errorMessage);
//                        log.debug("error message Transfert reception in {}", records.partitions());
                        Map<TopicPartition, Long> topicPartionsOffset = getTopicPartitionOffsetService().findCommittedOffsetsByGroupId(records.partitions(), groupId);
                        topicPartionsOffset.forEach((topicPartition, offset) -> kafkaConsumer.seek(topicPartition, offset));
                    }
                }

            } catch (WakeupException e) {
                // Ignore exception if closing
                log.error("***********  Transfert  to company ***********second catch partWakeupException *********");
                if (!closed.get()) {
                    throw e;
                }
            } catch (Exception e) {
                log.error("***********  transfert branchhhh to companyyyy  ***********third catch part/ before closing kafka consumer *********");
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
            } //                 
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
        return topicTransferBranchToCompany;
    }

}
