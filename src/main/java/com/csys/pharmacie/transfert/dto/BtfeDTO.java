/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.vente.quittance.dto.FactureDTO;

/**
 *
 * @author Administrateur
 */
public class BtfeDTO {

    private String numFE;
    
    
    private FactureDTO quittance;

    public BtfeDTO() {
    }

    public BtfeDTO(String numFE) {
        this.numFE = numFE;
    }

    public String getNumFE() {
        return numFE;
    }

    public void setNumFE(String numFE) {
        this.numFE = numFE;
    }

    public FactureDTO getQuittance() {
        return quittance;
    }

    public void setQuittance(FactureDTO quittance) {
        this.quittance = quittance;
    }

}
