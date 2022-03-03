/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author DELL
 */
public class QuittanceAndQuittanceDepotFrs {

    @Valid
    private QuittanceDTO quittance;

    @Valid
    private QuittanceDTO quittanceDepotFrs;

    @NotNull
    private Integer codePrestation;

    @NotNull
    private BigDecimal quantitePrestation;

    private List<ReglementDTO> reglementDTOs;
    
    private Integer codeOperation;

    public QuittanceAndQuittanceDepotFrs() {
    }

    public QuittanceAndQuittanceDepotFrs(QuittanceDTO quittance, QuittanceDTO quittanceDepotFrs) {
        this.quittance = quittance;
        this.quittanceDepotFrs = quittanceDepotFrs;
    }

    public QuittanceDTO getQuittance() {
        return quittance;
    }

    public void setQuittance(QuittanceDTO quittance) {
        this.quittance = quittance;
    }

    public QuittanceDTO getQuittanceDepotFrs() {
        return quittanceDepotFrs;
    }

    public void setQuittanceDepotFrs(QuittanceDTO quittanceDepotFrs) {
        this.quittanceDepotFrs = quittanceDepotFrs;
    }

    public Integer getCodePrestation() {
        return codePrestation;
    }

    public void setCodePrestation(Integer codePrestation) {
        this.codePrestation = codePrestation;
    }

    public List<ReglementDTO> getReglementDTOs() {
        return reglementDTOs;
    }

    public void setReglementDTOs(List<ReglementDTO> reglementDTOs) {
        this.reglementDTOs = reglementDTOs;
    }

    public BigDecimal getQuantitePrestation() {
        return quantitePrestation;
    }

    public void setQuantitePrestation(BigDecimal quantitePrestation) {
        this.quantitePrestation = quantitePrestation;
    }

    public Integer getCodeOperation() {
        return codeOperation;
    }

    public void setCodeOperation(Integer codeOperation) {
        this.codeOperation = codeOperation;
    }

}
