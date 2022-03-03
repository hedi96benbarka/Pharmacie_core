/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.EnumCrudMethod;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
public class KafkaConsumerErrorDTO {

    private Integer code;

    @NotNull
    @Size(
            min = 1,
            max = 100
    )
    private String topic;

    @NotNull
    private Integer partition;

    @NotNull
    private Integer offset;

    @NotNull
    @Size(
            min = 1,
            max = 100
    )
    private String groupId;

    private Integer tryCount;

    private Boolean handled;

    @NotNull
    private LocalDateTime createTime;

    @Size(
            min = 0,
            max = 2147483647
    )
    private String exceptionDetails;

    private Integer codeSite;

    @NotNull
    @Size(
            min = 1,
            max = 20
    )
    private String recordKey;

    @Size(
            min = 0,
            max = 2147483647
    )
    private String record;

    private EnumCrudMethod action;

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

    public Integer getPartition() {
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
    public String toString() {
        return "KafkaConsumerErrorDTO{" + "code=" + code + ", topic=" + topic + ", partition=" + partition + ", offset=" + offset + ", groupId=" + groupId + ", tryCount=" + tryCount + ", handled=" + handled + ", createTime=" + createTime + ", exceptionDetails=" + exceptionDetails + ", codeSite=" + codeSite + ", recordKey=" + recordKey + ", record=" + record + ", action=" + action + '}';
    }

}
