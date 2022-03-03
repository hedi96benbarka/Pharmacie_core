/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
public class BonRetourDTO extends FactureBADTO {

    @NotNull
    private String receptionID;

    private LocalDateTime DateReception;

    private BigDecimal mntBonReception;

    private String numAfficheReception;
     
    private String numbonRetourTransfertCompanyBranch;
    
    public LocalDateTime getDateReception() {
        return DateReception;
    }

    public void setDateReception(LocalDateTime DateReception) {
        this.DateReception = DateReception;
    }

    public BigDecimal getMntBonReception() {
        return mntBonReception;
    }

    public void setMntBonReception(BigDecimal mntBonReception) {
        this.mntBonReception = mntBonReception;
    }

    public String getNumAfficheReception() {
        return numAfficheReception;
    }

    public void setNumAfficheReception(String numAfficheReception) {
        this.numAfficheReception = numAfficheReception;
    }

    public String getReceptionID() {
        return receptionID;
    }

    public void setReceptionID(String receptionID) {
        this.receptionID = receptionID;
    }

    public String getNumbonRetourTransfertCompanyBranch() {
        return numbonRetourTransfertCompanyBranch;
    }

    public void setNumbonRetourTransfertCompanyBranch(String numbonRetourTransfertCompanyBranch) {
        this.numbonRetourTransfertCompanyBranch = numbonRetourTransfertCompanyBranch;
    }
 
   
    
}
