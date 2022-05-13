/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.config;

import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Administrateur
 */

@Configuration
public class KafkaConfigurationConsumer {
        private Map<String, Object> consumerConfig;
   @Value("${kafka.servers.bootstrap}")
    private String bootstrapServers;
    
    @Value("${kafka.group_id}") 
    private String groupId;// ="pharmacie-core";";

    
    public Map<String, Object> getConsumerConfig() {
        consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerConfig.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
//        consumerConfig.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 100_000);
//        consumerConfig.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 5000);
        consumerConfig.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 20000);
        consumerConfig.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1_000_000_000);
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
       return consumerConfig;
    }
    
}
