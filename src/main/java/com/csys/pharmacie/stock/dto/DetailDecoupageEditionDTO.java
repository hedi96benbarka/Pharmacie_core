/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author bassatine
 */
public class DetailDecoupageEditionDTO {

    private Integer articleID;

    private String parentID;

    private Integer originUnit;

  
    private Integer finalUnit; 

    private String originUnitDesignation;

    private String finalUnitDesignation;

  
    private BigDecimal transformedQuantity;

    private BigDecimal obtainedQuantity;

   
   
    private String desart;

  
  
    private String desArtSec;

    private String codeSaisi;

    private Date expirationDate;

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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public DetailDecoupageEditionDTO() {
    }

    public DetailDecoupageEditionDTO(Integer articleID, String parentID, Integer originUnit, Integer finalUnit, String originUnitDesignation, String finalUnitDesignation, BigDecimal transformedQuantity, BigDecimal obtainedQuantity, String desart, String desArtSec, String codeSaisi, Date expirationDate, String lot) {
        this.articleID = articleID;
        this.parentID = parentID;
        this.originUnit = originUnit;
        this.finalUnit = finalUnit;
        this.originUnitDesignation = originUnitDesignation;
        this.finalUnitDesignation = finalUnitDesignation;
        this.transformedQuantity = transformedQuantity;
        this.obtainedQuantity = obtainedQuantity;
        this.desart = desart;
        this.desArtSec = desArtSec;
        this.codeSaisi = codeSaisi;
        this.expirationDate = expirationDate;
        this.lot = lot;
    }

    
}
