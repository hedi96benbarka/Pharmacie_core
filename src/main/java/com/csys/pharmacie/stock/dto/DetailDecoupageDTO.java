package com.csys.pharmacie.stock.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DetailDecoupageDTO {

    @NotNull
    private Integer articleID;

    private String parentID;

    @NotNull
    private Integer originUnit;

    @NotNull
    private Integer finalUnit;

    private String originUnitDesignation;

    private String finalUnitDesignation;

    @NotNull
    private BigDecimal transformedQuantity;

    private BigDecimal obtainedQuantity;

    @NotNull
    @Size(min = 0, max = 255)
    private String desart;

    @NotNull
    @Size(min = 0, max = 255)
    private String desArtSec;

    @NotNull
    @Size(min = 0, max = 50)
    private String codeSaisi;

    private LocalDate expirationDate;

    private String lot;

    public Integer getArticleID() {
        return articleID;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public Integer getOriginUnit() {
        return originUnit;
    }

    public void setOriginUnit(Integer originUnit) {
        this.originUnit = originUnit;
    }

    public Integer getFinalUnit() {
        return finalUnit;
    }

    public void setFinalUnit(Integer finalUnit) {
        this.finalUnit = finalUnit;
    }

    public BigDecimal getTransformedQuantity() {
        return transformedQuantity;
    }

    public void setTransformedQuantity(BigDecimal transformedQuantity) {
        this.transformedQuantity = transformedQuantity;
    }

    public BigDecimal getObtainedQuantity() {
        return obtainedQuantity;
    }

    public void setObtainedQuantity(BigDecimal obtainedQuantity) {
        this.obtainedQuantity = obtainedQuantity;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesArtSec() {
        return desArtSec;
    }

    public void setDesArtSec(String desArtSec) {
        this.desArtSec = desArtSec;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getOriginUnitDesignation() {
        return originUnitDesignation;
    }

    public void setOriginUnitDesignation(String originUnitDesignation) {
        this.originUnitDesignation = originUnitDesignation;
    }

    public String getFinalUnitDesignation() {
        return finalUnitDesignation;
    }

    public void setFinalUnitDesignation(String finalUnitDesignation) {
        this.finalUnitDesignation = finalUnitDesignation;
    }

}
