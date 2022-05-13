/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.config;

import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.FactureDirecteDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import java.util.HashMap;
import java.util.Map;

import com.csys.pharmacie.transfert.dto.FactureBEDTO;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 *
 * @author Administrateur
 */
@Configuration
public class KakfaConfigurationProducer {

    static String topicTransferBranchToCompany;
    @Value("${kafka.topic.transfer-branch-to-company-management}")
    public void setTopicTransferToCompany(String topic) {
        topicTransferBranchToCompany = topic;
    }

  
    static String topicTransferCompanyToBranch;
    @Value("${kafka.topic.transfer-branch-to-company-management}")
    public void setTopicTransferBranchToCompany(String topic) {
        topicTransferCompanyToBranch = topic;
    }
    
    @Value("${kafka.servers.bootstrap}")
    private String bootstrapServers;


    static String topicBonReceptionOnShelfManagement;

    @Value("${kafka.topic.transfert-bon-reception-onShelf-management}")
    public void setTopicBonReceptionOnShelfManagement(String db) {
        topicBonReceptionOnShelfManagement = db;
    }
    
    static String topicDirectBillManagementForAccounting;
    @Value("${kafka.topic.direct-bill-management-for-accounting}")
    public void setTopicDirectBillManagementForAccounting(String topic) {
        topicDirectBillManagementForAccounting = topic;
    }

    static String topicRedressementBillManagementForAccounting;
    @Value("${kafka.topic.redressement-bill-management-for-accounting}")
    public void setTopicRedressementBillManagementForAccounting(String topicRedressementBillManagementForAccounting) {
        KakfaConfigurationProducer.topicRedressementBillManagementForAccounting = topicRedressementBillManagementForAccounting;
    }



    

    @Bean
    public ProducerFactory producerFactory() { // config par producer non par topic
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 2000000);//taille buffer
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip"); //"none"
        config.put(ProducerConfig.RETRIES_CONFIG, 1);
        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 2000);
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);// apres quel time on va faire retry
        config.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, 5000);
        config.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1000);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);//deduplication ==> envoie avec message sequence id => renvoie meme sq id
        //=> brocker prend message si seq id different donc write message qu'une seule fois => exactly-once
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_DOC, true);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);//pour conserver l'ordre => on doit avoir un seul batch
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);//contient pls msg avec un max 16384
        config.put(ProducerConfig.LINGER_MS_CONFIG, 10);//temps l'attente pour envouyer un batch
        config.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 1048576);

        return new DefaultKafkaProducerFactory<>(config);
    }

    /*definit brocker qui va faire administration */
    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaTemplate<String, TransfertCompanyBranchDTO> kafkaTemplateTransfert() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTemplate<String, BonRecepDTO> kafkaTemplateBonReceptionOnShelf() {
        return new KafkaTemplate<>(producerFactory());
    }
 @Bean
    public KafkaTemplate<String, FactureDirecteDTO> kafkaTemplateFactureDirecte() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTemplate<String, FactureBEDTO> kafkaTemplateFactureBE() {
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    public NewTopic topicTransfertCompanyToBranchManagement() {
        Map<String, String> config = new HashMap<>();
        config.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2");
        return new NewTopic(topicTransferCompanyToBranch, 3, (short) 3).configs(config);
        //nbre de partition
        //(short) nbre replicat par partitions //on a 3 brokers donc on peut se permettre d avoir 3 replicats parpartitions (sorte de backup)

    }

    @Bean
    public NewTopic topicBonReceptionOnShelfManagement() {
        Map<String, String> config = new HashMap<>();
        config.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2");
        return new NewTopic(topicBonReceptionOnShelfManagement, 3, (short) 3).configs(config);
        //nbre de partition
        //(short) nbre replicat par partitions //on a 3 brokers donc on peut se permettre d avoir 3 replicats parpartitions (sorte de backup)
    }

    @Bean
    public NewTopic topicTransferBranchToCompanyManagement() {
        Map<String, String> config = new HashMap<>();
        config.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2");
        return new NewTopic(topicTransferBranchToCompany, 3, (short) 3).configs(config);

    }
    
    
    @Bean
    public NewTopic topicDirectBillManagementForAccounting() {
        Map<String, String> config = new HashMap<>();
        config.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2");
        return new NewTopic(topicDirectBillManagementForAccounting, 3, (short) 3).configs(config);

    }

    @Bean
    public NewTopic topicRedressementBillManagementForAccounting() {
        Map<String, String> config = new HashMap<>();
        config.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "2");
        return new NewTopic(topicRedressementBillManagementForAccounting, 3, (short) 3).configs(config);

    }
    
    
}
