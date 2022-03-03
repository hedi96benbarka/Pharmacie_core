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
public class PMPArticleDTO {
    private BigDecimal PMP;
    private Integer articleID;

    public PMPArticleDTO() {
    }

    public PMPArticleDTO(BigDecimal PMP, Integer articleID) {
        this.PMP = PMP;
        this.articleID = articleID;
    }

    public BigDecimal getPMP() {
        return PMP;
    }

    public void setPMP(BigDecimal PMP) {
        this.PMP = PMP;
    }

  

    public Integer getArticleID() {
        return articleID;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }
    
    
}
