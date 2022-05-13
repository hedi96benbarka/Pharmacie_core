/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csys.pharmacie.achat.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author USER
 */
public class KafkaProducerErrorGecDTO {
    
    private Integer code;

    @NotNull
    @Size(
            min = 1,
            max = 100
    )
    private String topic;

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



    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "KAfkaProducerErrorGecDTO{" + "code=" + code + ", topic=" + topic + ", tryCount=" + tryCount + ", handled=" + handled + ", createTime=" + createTime + ", exceptionDetails=" + exceptionDetails + ", codeSite=" + codeSite + ", recordKey=" + recordKey + ", record=" + record + '}';
    }

    
}
