/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.SatisfactionFactureEnum;

/**
 *
 * @author Administrateur
 */
public class DemandeTransfRecupereeDTO {

    private Integer demandeTRID;
    private static final SatisfactionFactureEnum STATE = SatisfactionFactureEnum.RECOVRED;

    public DemandeTransfRecupereeDTO(Integer demandeTRID) {
        this.demandeTRID = demandeTRID;
    }

    public Integer getDemandeTRID() {
        return demandeTRID;
    }

    public void setDemandeTRID(Integer demandeTRID) {
        this.demandeTRID = demandeTRID;
    }

}
