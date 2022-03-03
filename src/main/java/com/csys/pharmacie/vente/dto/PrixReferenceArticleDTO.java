/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.dto;

import java.math.BigDecimal;

/**
 *
 * @author Farouk
 */
public class PrixReferenceArticleDTO {
    private BigDecimal referencePrice;
    private Integer articleID;

    public PrixReferenceArticleDTO(BigDecimal referencePrice, Integer articleID) {
        this.referencePrice = referencePrice;
        this.articleID = articleID;
    }

    public PrixReferenceArticleDTO() {
    }

    public BigDecimal getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(BigDecimal referencePrice) {
        this.referencePrice = referencePrice;
    }

    public Integer getArticleID() {
        return articleID;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }
    
    
}
