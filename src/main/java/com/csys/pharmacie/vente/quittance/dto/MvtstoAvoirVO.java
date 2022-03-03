/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import java.math.BigDecimal;

/**
 *
 * @author Farouk
 */
public class MvtstoAvoirVO {

    private Integer codart;
    private BigDecimal priuni;
    private String desart;
    private BigDecimal qte;
    private Integer codTVA;
    private BigDecimal tauTVA;

    public MvtstoAvoirVO(Integer codart, String desart, Integer codTVA, BigDecimal tauTVA, BigDecimal priuni, BigDecimal qte) {
        super();
        this.codart = codart;
        this.desart = desart;
        this.qte = qte;
        this.codTVA = codTVA;
        this.tauTVA = tauTVA;
        this.priuni = priuni;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public Integer getCodTVA() {
        return codTVA;
    }

    public void setCodTVA(Integer codTVA) {
        this.codTVA = codTVA;
    }

    public BigDecimal getTauTVA() {
        return tauTVA;
    }

    public void setTauTVA(BigDecimal tauTVA) {
        this.tauTVA = tauTVA;
    }

   

}
