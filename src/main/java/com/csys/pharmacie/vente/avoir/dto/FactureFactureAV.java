/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.dto;

import com.csys.pharmacie.vente.quittance.dto.FactureDTO;

/**
 *
 * @author DELL
 */
public class FactureFactureAV {

    private String numBonFactureAV;

    private String numBonFacture;
    
    private FactureDTO factureAV;
    
    private FactureDTO facture;

    public FactureFactureAV(String numBonFactureAV, String numBonFacture) {
        this.numBonFactureAV = numBonFactureAV;
        this.numBonFacture = numBonFacture;
    }

    
    public String getNumBonFactureAV() {
        return numBonFactureAV;
    }

    public void setNumBonFactureAV(String numBonFactureAV) {
        this.numBonFactureAV = numBonFactureAV;
    }

    public String getNumBonFacture() {
        return numBonFacture;
    }

    public void setNumBonFacture(String numBonFacture) {
        this.numBonFacture = numBonFacture;
    }

    public FactureDTO getFactureAV() {
        return factureAV;
    }

    public void setFactureAV(FactureDTO factureAV) {
        this.factureAV = factureAV;
    }

    public FactureDTO getFacture() {
        return facture;
    }

    public void setFacture(FactureDTO facture) {
        this.facture = facture;
    }
    
    
}
