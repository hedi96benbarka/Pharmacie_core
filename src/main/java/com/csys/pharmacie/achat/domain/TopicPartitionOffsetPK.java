/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Administrateur
 */
@Embeddable
public class TopicPartitionOffsetPK implements Serializable {

    private String topic;
    private Integer partition;
    @Column(name = "group_id")
    private String groupId;

    public TopicPartitionOffsetPK(String topic, Integer partition, String groupId) {
        this.topic = topic;
        this.partition = partition;
        this.groupId = groupId;
    }


    public TopicPartitionOffsetPK(String topic, Integer partition ) {
        this.topic = topic;
        this.partition = partition;
     
    }

    public TopicPartitionOffsetPK() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.topic);
        hash = 59 * hash + Objects.hashCode(this.partition);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TopicPartitionOffsetPK other = (TopicPartitionOffsetPK) obj;
        if (!Objects.equals(this.topic, other.topic)) {
            return false;
        }
        if (!Objects.equals(this.partition, other.partition)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TopicPartitionOffsetPK{" + "topic=" + topic + ", partition=" + partition + ", groupId=" + groupId + '}';
    }

   

     
}
