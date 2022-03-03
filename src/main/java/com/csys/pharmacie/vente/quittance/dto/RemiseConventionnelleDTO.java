/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import java.math.BigDecimal;

/**
 *
 * @author Administrateur
 */
public class RemiseConventionnelleDTO {

    private Integer codeArticle;
    private BigDecimal taux;
    private BigDecimal tauxCouverture;
    private String natureException;

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public BigDecimal getTaux() {
        return taux;
    }

    public void setTaux(BigDecimal taux) {
        this.taux = taux;
    }

    public String getNatureException() {
        return natureException;
    }

    public void setNatureException(String natureException) {
        this.natureException = natureException;
    }

    public BigDecimal getTauxCouverture() {
        return tauxCouverture;
    }

    public void setTauxCouverture(BigDecimal tauxCouverture) {
        this.tauxCouverture = tauxCouverture;
    }
}
