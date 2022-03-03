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
public class BtbtDTO {

    private String numBTReturn;

    private FactureBTDTO factureBTReturn;

    public BtbtDTO() {
    }

    public BtbtDTO(String numFE) {
        this.numBTReturn = numFE;
    }

    public String getNumBTReturn() {
        return numBTReturn;
    }

    public void setNumBTReturn(String numBTReturn) {
        this.numBTReturn = numBTReturn;
    }

    public FactureBTDTO getFactureBTReturn() {
        return factureBTReturn;
    }

    public void setFactureBTReturn(FactureBTDTO factureBTReturn) {
        this.factureBTReturn = factureBTReturn;
    }

    

}
