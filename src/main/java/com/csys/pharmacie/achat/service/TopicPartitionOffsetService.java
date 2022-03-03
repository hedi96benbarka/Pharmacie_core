/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.TopicPartitionOffset;
import com.csys.pharmacie.achat.domain.TopicPartitionOffsetPK;
import com.csys.pharmacie.achat.repository.TopicPartitionOffsetRepository;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Administrateur
 */
@Service
public class TopicPartitionOffsetService {

    private final Logger log = LoggerFactory.getLogger(TopicPartitionOffsetService.class);
    @Autowired
    private TopicPartitionOffsetRepository topicPartitionOffsetRepository;

    public void commitOffsetToDB(ConsumerRecord rec) {
        topicPartitionOffsetRepository.save(new TopicPartitionOffset(rec.topic(), rec.partition(), rec.offset() + 1));
    }
    public void commitOffsetByGroupIdToDB(ConsumerRecord rec, String groupId) {
        log.debug("commitOffsetByGroupIdToDB : {}", rec.offset() + 1);
        topicPartitionOffsetRepository.save(new TopicPartitionOffset(rec.topic(), rec.partition(), rec.offset() + 1, groupId));
    }

    public Map<TopicPartition, Long> findCommittedOffsets() {
        return topicPartitionOffsetRepository.findAll().stream().collect(Collectors.toMap(item -> new TopicPartition(item.getTopic(), item.getPartition()), TopicPartitionOffset::getOffset));
    }

    public Long findCommittedOffset(TopicPartition partition) {
        return topicPartitionOffsetRepository.findOne(new TopicPartitionOffsetPK(partition.topic(), partition.partition())).getOffset();
    }

    public Map<TopicPartition, Long> findCommittedOffsets(Collection<TopicPartition> topicPartition) {
        Set<TopicPartitionOffset> topicsPartionsOffset = topicPartitionOffsetRepository.findByPkTopicIn(topicPartition.stream().map(TopicPartition::topic).collect(toList()));
        return topicsPartionsOffset.stream()
                .filter(tpo -> topicPartition.stream().anyMatch(item -> tpo.getTopic().equals(item.topic()) && tpo.getPartition() == item.partition()))
                .collect(Collectors.toMap(item -> new TopicPartition(item.getTopic(), item.getPartition()), TopicPartitionOffset::getOffset));

    }
     public Map<TopicPartition, OffsetAndMetadata> findCommittedOffsetsAndMetaData(Collection<TopicPartition> topicPartition) {
        Set<TopicPartitionOffset> topicsPartionsOffset = topicPartitionOffsetRepository.findByPkTopicIn(topicPartition.stream().map(TopicPartition::topic).collect(toList()));
        return topicsPartionsOffset.stream()
                .filter(tpo -> topicPartition.stream().anyMatch(item -> tpo.getTopic().equals(item.topic()) && tpo.getPartition() == item.partition()))
                .collect(Collectors.toMap(item -> new TopicPartition(item.getTopic(), item.getPartition()),item2-> new OffsetAndMetadata(item2.getOffset())));

    }
    public Map<TopicPartition, Long> findCommittedOffsetsByGroupId(Collection<TopicPartition> topicPartition, String groupId) {
        Set<TopicPartitionOffset> topicsPartionsOffset = topicPartitionOffsetRepository.findByPkTopicInAndPkGroupId(topicPartition.stream().map(TopicPartition::topic).collect(toList()), groupId);
      log.debug("result topicPartitionOffsetRepository : {}", topicsPartionsOffset.toString());
        return topicsPartionsOffset.stream()
                .filter(tpo -> topicPartition.stream().anyMatch(item -> tpo.getTopic().equals(item.topic()) && tpo.getPartition() == item.partition()))
                .collect(Collectors.toMap(item -> new TopicPartition(item.getTopic(), item.getPartition()), TopicPartitionOffset::getOffset));
    }
    
    public Map<TopicPartition, OffsetAndMetadata> findCommittedOffsetsAndMetaDataByGroupId(Collection<TopicPartition> topicPartition, String groupId) {
        Set<TopicPartitionOffset> topicsPartionsOffset = topicPartitionOffsetRepository.findByPkTopicInAndPkGroupId(topicPartition.stream().map(TopicPartition::topic).collect(toList()), groupId);
        return topicsPartionsOffset.stream()
                .filter(tpo -> topicPartition.stream().anyMatch(item -> tpo.getTopic().equals(item.topic()) && tpo.getPartition() == item.partition()))
                .collect(Collectors.toMap(item -> new TopicPartition(item.getTopic(), item.getPartition()),item2-> new OffsetAndMetadata(item2.getOffset())));

    }
}
