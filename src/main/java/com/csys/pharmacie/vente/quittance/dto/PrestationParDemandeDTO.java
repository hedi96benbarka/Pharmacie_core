/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

/**
 *
 * @author DELL
 */
public class PrestationParDemandeDTO {

    private Integer codePrestation;
    private Long codeDemande;

    public Integer getCodePrestation() {
        return codePrestation;
    }

    public void setCodePrestation(Integer codePrestation) {
        this.codePrestation = codePrestation;
    }

    public Long getCodeDemande() {
        return codeDemande;
    }

    public void setCodeDemande(Long codeDemande) {
        this.codeDemande = codeDemande;
    }

}
