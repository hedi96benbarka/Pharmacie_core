/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;

/**
 *
 * @author Farouk
 */
public class DetailCommandeEditionDTO {

    private Integer codeCommandeAchat;
    private Integer codart;
    private String codeSaisi;
    private String designation;
    private BigDecimal quantite;
    private BigDecimal prixUnitaire;

    public Integer getCodeCommandeAchat() {
        return codeCommandeAchat;
    }

    public void setCodeCommandeAchat(Integer codeCommandeAchat) {
        this.codeCommandeAchat = codeCommandeAchat;
    }

    
    
    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    @Override
    public String toString() {
        return "DetailCommandeEditionDTO{" + "codeCommandeAchat=" + codeCommandeAchat + ", codart=" + codart + ", codeSaisi=" + codeSaisi + ", designation=" + designation + ", quantite=" + quantite + ", prixUnitaire=" + prixUnitaire + '}';
    }

}
