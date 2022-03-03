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
public class BaseTVADTO {

    @NotNull
    private Integer codeTva;
    @NotNull
    private BigDecimal tauxTva;
    @NotNull
    private BigDecimal baseTva;
    @NotNull
    private BigDecimal montantTva;
    
    private String numbon;

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public BigDecimal getBaseTva() {
        return baseTva;
    }

    public void setBaseTva(BigDecimal baseTva) {
        this.baseTva = baseTva;
    }

    public BigDecimal getMontantTva() {
        return montantTva;
    }

    public void setMontantTva(BigDecimal montantTva) {
        this.montantTva = montantTva;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }
    
    
}
