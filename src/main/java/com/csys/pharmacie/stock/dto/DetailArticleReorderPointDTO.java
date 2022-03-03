/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import java.math.BigDecimal;


/**
 *
 * @author Administrateur
 */
public class DetailArticleReorderPointDTO {
    
    private Integer code;
 
    private Integer codeArticle;
 
    private Integer codeUnite;
  
    private Integer safetyStockPerDay;
  
    private Integer leadTimeProcurement;
    
    private BigDecimal realConsumming;
  
    private BigDecimal consumingPerDay;
   
    private BigDecimal leadTime;

    private BigDecimal safetyStock;
   
    private BigDecimal rop;
   
    private BigDecimal safetyStockPerDaysConsumming;
    
    private BigDecimal maximumStock;
    
    private BigDecimal currentStock;
    
    private Integer codeArticleReorder;
    
    private String codeSaisiArticle;

    private String designationArticle;

    private String designationUnite;
    
    private String designationCategArticle;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public Integer getSafetyStockPerDay() {
        return safetyStockPerDay;
    }

    public void setSafetyStockPerDay(Integer safetyStockPerDay) {
        this.safetyStockPerDay = safetyStockPerDay;
    }

    public Integer getLeadTimeProcurement() {
        return leadTimeProcurement;
    }

    public void setLeadTimeProcurement(Integer leadTimeProcurement) {
        this.leadTimeProcurement = leadTimeProcurement;
    }

    public Integer getCodeArticleReorder() {
        return codeArticleReorder;
    }

    public void setCodeArticleReorder(Integer codeArticleReorder) {
        this.codeArticleReorder = codeArticleReorder;
    }

    public BigDecimal getRealConsumming() {
        return realConsumming;
    }

    public void setRealConsumming(BigDecimal realConsumming) {
        this.realConsumming = realConsumming;
    }

    public BigDecimal getConsumingPerDay() {
        return consumingPerDay;
    }

    public void setConsumingPerDay(BigDecimal consumingPerDay) {
        this.consumingPerDay = consumingPerDay;
    }

    public BigDecimal getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(BigDecimal leadTime) {
        this.leadTime = leadTime;
    }

    public BigDecimal getSafetyStock() {
        return safetyStock;
    }

    public void setSafetyStock(BigDecimal safetyStock) {
        this.safetyStock = safetyStock;
    }

    public BigDecimal getRop() {
        return rop;
    }

    public void setRop(BigDecimal rop) {
        this.rop = rop;
    }

    public BigDecimal getSafetyStockPerDaysConsumming() {
        return safetyStockPerDaysConsumming;
    }

    public void setSafetyStockPerDaysConsumming(BigDecimal safetyStockPerDaysConsumming) {
        this.safetyStockPerDaysConsumming = safetyStockPerDaysConsumming;
    }

    public BigDecimal getMaximumStock() {
        return maximumStock;
    }

    public void setMaximumStock(BigDecimal maximumStock) {
        this.maximumStock = maximumStock;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public String getCodeSaisiArticle() {
        return codeSaisiArticle;
    }

    public void setCodeSaisiArticle(String codeSaisiArticle) {
        this.codeSaisiArticle = codeSaisiArticle;
    }

    public String getDesignationArticle() {
        return designationArticle;
    }

    public void setDesignationArticle(String designationArticle) {
        this.designationArticle = designationArticle;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public String getDesignationCategArticle() {
        return designationCategArticle;
    }

    public void setDesignationCategArticle(String designationCategArticle) {
        this.designationCategArticle = designationCategArticle;
    }
    
    
}
