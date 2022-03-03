package com.csys.pharmacie.achat.dto;

import java.lang.Integer;
import java.lang.Long;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class BaseTvaFactureDirecteDTO {

    @NotNull
    private Integer codeTva;

    private BigDecimal tauxTva;

    private BigDecimal baseTva;

    private BigDecimal montantTva;

    @NotNull
    private Long code;

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

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

   
}
