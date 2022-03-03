/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.config;

import com.csys.pharmacie.achat.service.TopicPartitionOffsetService;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Administrateur
 */
public abstract class AbstractNativeConsumer {

    private String topic;
    
    protected final Logger log = LoggerFactory.getLogger(AbstractNativeConsumer.class);

    protected final AtomicBoolean closed = new AtomicBoolean(false);

    private final KafkaConfigurationConsumer kafkaConfiguration;

    protected KafkaConsumer kafkaConsumer;

    private final TopicPartitionOffsetService topicPartitionOffsetService;

    public AbstractNativeConsumer(KafkaConfigurationConsumer kafkaConfiguration, TopicPartitionOffsetService topicPartitionOffsetService) {
        this.kafkaConfiguration = kafkaConfiguration;
        this.topicPartitionOffsetService = topicPartitionOffsetService;
    }

    /**
     * Implement your consumer loop here.
     *
     * One consumer per thread is the rule.
     *
     * This is a link to a must follow example
     * {@link http://172.16.10.34:9999/Farouk.Ellouze/template}
     */
    abstract public void start();

    /**
     * Invoke this methode to break the consuming loop. See this example of
     * usage {@link http://172.16.10.34:9999/Farouk.Ellouze/template}
     */
    public void shutdown() {
        log.info("Shutdown Kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }

    public KafkaConfigurationConsumer getKafkaConfiguration() {
        return kafkaConfiguration;
    }

    public TopicPartitionOffsetService getTopicPartitionOffsetService() {
        return topicPartitionOffsetService;
    }

    public String getTopic() {
        return topic;
    }

   

}
