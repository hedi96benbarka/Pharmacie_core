/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.TopicPartitionOffset;
import com.csys.pharmacie.achat.domain.TopicPartitionOffsetPK;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Administrateur
 */
@Repository
public interface TopicPartitionOffsetRepository extends JpaRepository<TopicPartitionOffset, TopicPartitionOffsetPK> {
    Set<TopicPartitionOffset> findByPkTopicIn (List<String> topics);
    Set<TopicPartitionOffset> findByPkTopicInAndPkGroupId (List<String> topics, String groupId);
}
