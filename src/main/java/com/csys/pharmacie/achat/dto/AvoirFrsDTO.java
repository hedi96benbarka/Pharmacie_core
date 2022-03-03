/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.BaseBonDTO;
import java.math.BigDecimal;

/**
 *
 * @author Farouk
 */
public class AvoirFrsDTO  {

    private String numAfficheFacRecep;
    private Integer codDep;
    private String memop;
    private BigDecimal valrem;
    private String codfrs;

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public BigDecimal getValrem() {
        return valrem;
    }

    public void setValrem(BigDecimal valrem) {
        this.valrem = valrem;
    }

    public String getCodfrs() {
        return codfrs;
    }

    public void setCodfrs(String codfrs) {
        this.codfrs = codfrs;
    }

    public AvoirFrsDTO() {
    }

    public Integer getCodDep() {
        return codDep;
    }

    public void setCodDep(Integer codDep) {
        this.codDep = codDep;
    }

    public String getNumAfficheFacRecep() {
        return numAfficheFacRecep;
    }

    public void setNumAfficheFacRecep(String numAfficheFacRecep) {
        this.numAfficheFacRecep = numAfficheFacRecep;
    }

}
