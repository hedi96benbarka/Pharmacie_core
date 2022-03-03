/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author farouk
 */
@Entity
public class ArticleInDep implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    protected int num;
    private String codArt;
    private String desArt;
    private BigDecimal qte;
    private String lot;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datePer;
    private BigDecimal priUni;

    public BigDecimal getPriUni() {
        return priUni;
    }

    public void setPriUni(BigDecimal priUni) {
        this.priUni = priUni;
    }
    public String getCodArt() {
        return codArt;
    }

    public void setCodArt(String codDep) {
        this.codArt = codDep;
    }

    public String getDesArt() {
        return desArt;
    }

    public void setDesArt(String desArt) {
        this.desArt = desArt;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public Date getDatePer() {
        return datePer;
    }

    public void setDatePer(Date datePer) {
        this.datePer = datePer;
    }
   
 }
