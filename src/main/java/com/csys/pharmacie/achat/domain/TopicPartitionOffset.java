/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "topic_partition_offset_pharmacie")
public class TopicPartitionOffset implements Serializable {

    @EmbeddedId
    private TopicPartitionOffsetPK pk;
    
    @Column(name = "offset")
    private Long offset;

    public TopicPartitionOffset() {
    }
    
    public TopicPartitionOffset(String topic, int partition, long offset, String groupId) {
        this.offset = offset;
        this.pk = new TopicPartitionOffsetPK(topic, partition,groupId);
    }
    public TopicPartitionOffset(String topic, int partition, long offset) {
        this.offset = offset;
        this.pk = new TopicPartitionOffsetPK(topic, partition);
    }

    public TopicPartitionOffset(String topic, int partition) {

        this.pk = new TopicPartitionOffsetPK(topic, partition);
    }

    public TopicPartitionOffsetPK getPk() {
        return pk;
    }

    public void setPk(TopicPartitionOffsetPK pk) {
        this.pk = pk;
    }

    public String getTopic() {
        return this.pk.getTopic();
    }

    public int getPartition() {
        return this.pk.getPartition();
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public String getGroupId() {
        return this.pk.getGroupId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.pk);
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
        final TopicPartitionOffset other = (TopicPartitionOffset) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TopicPartitionOffset{" + "pk=" + pk + ", offset=" + offset + '}';
    }

  

}
