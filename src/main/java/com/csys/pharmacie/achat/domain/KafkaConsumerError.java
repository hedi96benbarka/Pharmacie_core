/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.EnumCrudMethod;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "kafka_consumer_error_pharmacie")
public class KafkaConsumerError implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @Size(min = 1, max = 100)
    @NotNull
    @Column(name = "topic")
    private String topic;
    @Basic(optional = false)
    @NotNull
    @Column(name = "partition")
    private Integer partition;
    @NotNull
    @Column(name = "offset")
    private Integer offset;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "group_id")
    private String groupId;
    @Column(name = "try_count")
    private Integer tryCount;
    @Column(name = "handled")
    private Boolean handled;
    @NotNull
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @NotNull
    @Size(max = 2147483647)
    @Column(name = "exception_details")
    private String exceptionDetails;
    @Column(name = "code_site")
    private Integer codeSite;
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "record_key")
    private String recordKey;
    @NotNull
    @Size(max = 2147483647)
    @Column(name = "record")
    private String record;
    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    private EnumCrudMethod action;

    public KafkaConsumerError() {
    }

    public KafkaConsumerError(Integer code) {
        this.code = code;
    }

    public KafkaConsumerError(Integer code, String topic, Integer partition, Integer offset, String groupId, LocalDateTime createTime, String recordKey) {
        this.code = code;
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.groupId = groupId;
        this.createTime = createTime;
        this.recordKey = recordKey;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    public Boolean getHandled() {
        return handled;
    }

    public void setHandled(Boolean handled) {
        this.handled = handled;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getExceptionDetails() {
        return exceptionDetails;
    }

    public void setExceptionDetails(String exceptionDetails) {
        this.exceptionDetails = exceptionDetails;
    }

    public Integer getCodeSite() {
        return codeSite;
    }

    public void setCodeSite(Integer codeSite) {
        this.codeSite = codeSite;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public EnumCrudMethod getAction() {
        return action;
    }

    public void setAction(EnumCrudMethod action) {
        this.action = action;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KafkaConsumerError)) {
            return false;
        }
        KafkaConsumerError other = (KafkaConsumerError) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "KafkaConsumerError{" + "code=" + code + ", topic=" + topic + ", partition=" + partition + ", offset=" + offset + ", groupId=" + groupId + ", tryCount=" + tryCount + ", handled=" + handled + ", createTime=" + createTime + ", exceptionDetails=" + exceptionDetails + ", codeSite=" + codeSite + ", recordKey=" + recordKey + ", record=" + record + ", action=" + action + '}';
    }

    

}
