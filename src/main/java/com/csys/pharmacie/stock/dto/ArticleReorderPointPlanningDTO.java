package com.csys.pharmacie.stock.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ArticleReorderPointPlanningDTO {

    private Integer code;

    @NotNull
    private CategorieDepotEnum categDepot;

    @NotNull
    private LocalDateTime dateDuReference;
    @NotNull
    private LocalDateTime dateAuReference;
    @NotNull
    @Min(value = 1)
    @Max(value = 360)
    private BigDecimal consummingDays;

    private LocalDateTime dateCreate;
    private String userCreate;
    private Boolean planned;
    private Boolean dataPrepared;
    private Boolean ltpExecuted;
    private Boolean stdExecuted;
    private Boolean executed;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public LocalDateTime getDateDuReference() {
        return dateDuReference;
    }

    public void setDateDuReference(LocalDateTime dateDuReference) {
        this.dateDuReference = dateDuReference;
    }

    public LocalDateTime getDateAuReference() {
        return dateAuReference;
    }

    public void setDateAuReference(LocalDateTime dateAuReference) {
        this.dateAuReference = dateAuReference;
    }

    public BigDecimal getConsummingDays() {
        return consummingDays;
    }

    public void setConsummingDays(BigDecimal consummingDays) {
        this.consummingDays = consummingDays;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Boolean isPlanned() {
        return planned;
    }

    public void setPlanned(Boolean planned) {
        this.planned = planned;
    }

    public Boolean isDataPrepared() {
        return dataPrepared;
    }

    public void setDataPrepared(Boolean dataPrepared) {
        this.dataPrepared = dataPrepared;
    }

    public Boolean getLtpExecuted() {
        return ltpExecuted;
    }

    public void setLtpExecuted(Boolean ltpExecuted) {
        this.ltpExecuted = ltpExecuted;
    }

    public Boolean getStdExecuted() {
        return stdExecuted;
    }

    public void setStdExecuted(Boolean stdExecuted) {
        this.stdExecuted = stdExecuted;
    }

    public Boolean isExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
    }

    @Override
    public String toString() {
        return "ArticleReorderPointPlanningDTO{" + "code=" + code + ", categDepot=" + categDepot + '}';
    }
    
    
}
