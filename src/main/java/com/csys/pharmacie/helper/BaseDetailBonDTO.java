/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Farouk
 */
public abstract class BaseDetailBonDTO {

    private String numbon;
    private String numAffiche;
    @NotNull
    private Integer refArt;
    @NotNull
    private String codeSaisi;
    @NotNull
    private String desart;
    @NotNull
    private String desartSec;

    @NotNull
    private BigDecimal priuni;
    @NotNull
    private BigDecimal quantite;
    @NotNull
    private BigDecimal tauTVA;
    private BigDecimal montht;
    private BigDecimal remise;

    private Integer codTVA;

    private Integer codeUnite;

    private String designationunite;

    public BaseDetailBonDTO() {
    }

    public BaseDetailBonDTO(BigDecimal priuni) {
        this.priuni = priuni;
    }

    
    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public Integer getRefArt() {
        return refArt;
    }

    public void setRefArt(Integer refArt) {
        this.refArt = refArt;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public String getDesartSec() {
        return desartSec;
    }

    public void setDesartSec(String desartSec) {
        this.desartSec = desartSec;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

 

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getTauTVA() {
        return tauTVA;
    }

    public void setTauTVA(BigDecimal tauTVA) {
        this.tauTVA = tauTVA;
    }

    public BigDecimal getMontht() {
        return montht;
    }

    public void setMontht(BigDecimal montht) {
        this.montht = montht;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public Integer getCodTVA() {
        return codTVA;
    }

    public void setCodTVA(Integer codTVA) {
        this.codTVA = codTVA;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumAffiche() {
        return numAffiche;
    }

    public void setNumAffiche(String numAffiche) {
        this.numAffiche = numAffiche;
    }

    
    
    public String getDesignationunite() {
        return designationunite;
    }

    public void setDesignationunite(String designationunite) {
        this.designationunite = designationunite;
    }

    @Override
    public String toString() {
        return "BaseDetailBonDTO{" + "numbon=" + numbon + ", numAffiche=" + numAffiche + ", refArt=" + refArt + ", codeSaisi=" + codeSaisi + ", desart=" + desart + ", desartSec=" + desartSec  + ", priuni=" + priuni + ", quantite=" + quantite + ", tauTVA=" + tauTVA + ", montht=" + montht + ", remise=" + remise + ", codTVA=" + codTVA + ", codeUnite=" + codeUnite + ", designationunite=" + designationunite + '}';
    }

}
