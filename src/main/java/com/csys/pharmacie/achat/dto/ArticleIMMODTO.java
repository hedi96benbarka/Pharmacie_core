/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;

/**
 *
 * @author DELL
 */
public class ArticleIMMODTO extends ArticleDTO {

    private Boolean avecNumeroSerie;
    private Boolean etiquettable;
    private Boolean corporel;
    private Boolean detaille;
    private Boolean genererImmobilisation;
    private BigDecimal tauxAmortissement;
    private BigDecimal tauxIfrs;
    private BigDecimal tauxAmortFiscale1;
    private BigDecimal tauxAmortFiscale2;

    public Boolean getAvecNumeroSerie() {
        return avecNumeroSerie;
    }

    public void setAvecNumeroSerie(Boolean avecNumeroSerie) {
        this.avecNumeroSerie = avecNumeroSerie;
    }

    public Boolean getEtiquettable() {
        return etiquettable;
    }

    public void setEtiquettable(Boolean etiquettable) {
        this.etiquettable = etiquettable;
    }

    public Boolean getCorporel() {
        return corporel;
    }

    public void setCorporel(Boolean corporel) {
        this.corporel = corporel;
    }

    public Boolean getDetaille() {
        return detaille;
    }

    public void setDetaille(Boolean detaille) {
        this.detaille = detaille;
    }

    public Boolean getGenererImmobilisation() {
        return genererImmobilisation;
    }

    public void setGenererImmobilisation(Boolean genererImmobilisation) {
        this.genererImmobilisation = genererImmobilisation;
    }

    public BigDecimal getTauxAmortissement() {
        return tauxAmortissement;
    }

    public void setTauxAmortissement(BigDecimal tauxAmortissement) {
        this.tauxAmortissement = tauxAmortissement;
    }

    public BigDecimal getTauxIfrs() {
        return tauxIfrs;
    }

    public void setTauxIfrs(BigDecimal tauxIfrs) {
        this.tauxIfrs = tauxIfrs;
    }

    public BigDecimal getTauxAmortFiscale1() {
        return tauxAmortFiscale1;
    }

    public void setTauxAmortFiscale1(BigDecimal tauxAmortFiscale1) {
        this.tauxAmortFiscale1 = tauxAmortFiscale1;
    }

    public BigDecimal getTauxAmortFiscale2() {
        return tauxAmortFiscale2;
    }

    public void setTauxAmortFiscale2(BigDecimal tauxAmortFiscale2) {
        this.tauxAmortFiscale2 = tauxAmortFiscale2;
    }

    @Override
    public String toString() {
        return "ArticleIMMODTO{" + "avecNumeroSerie=" + avecNumeroSerie + ", detaille=" + detaille + '}';
    }

   
    
    

}
