/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;

/**
 *
 * @author DELL
 */
public class PriceVarianceDTO {

    private Integer codeArticle;
    private String desArticle;
    private String desSecArticle;
    private String codeSaisi;
    private BigDecimal prixMoyenActuel;
    private BigDecimal prixMoyenPrecedant;
    private BigDecimal prixMoyenActuelTTC;
    private BigDecimal prixMoyenPrecedantTTC;
    private BigDecimal prixDernierAchat;
    private String designationUnite;
    private BigDecimal tauxTvaActuel;
    private String listeCategorieArticlesParents; 
    private BigDecimal quantiteTotale;

    public PriceVarianceDTO() {
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getDesArticle() {
        return desArticle;
    }

    public void setDesArticle(String desArticle) {
        this.desArticle = desArticle;
    }

    public String getDesSecArticle() {
        return desSecArticle;
    }

    public void setDesSecArticle(String desSecArticle) {
        this.desSecArticle = desSecArticle;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public BigDecimal getPrixMoyenActuel() {
        return prixMoyenActuel;
    }

    public void setPrixMoyenActuel(BigDecimal prixMoyenActuel) {
        this.prixMoyenActuel = prixMoyenActuel;
    }

    public BigDecimal getPrixMoyenPrecedant() {
        return prixMoyenPrecedant;
    }

    public void setPrixMoyenPrecedant(BigDecimal prixMoyenPrecedant) {
        this.prixMoyenPrecedant = prixMoyenPrecedant;
    }

    public BigDecimal getPrixDernierAchat() {
        return prixDernierAchat;
    }

    public void setPrixDernierAchat(BigDecimal prixDernierAchat) {
        this.prixDernierAchat = prixDernierAchat;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public BigDecimal getPrixMoyenActuelTTC() {
        return prixMoyenActuelTTC;
    }

    public void setPrixMoyenActuelTTC(BigDecimal prixMoyenActuelTTC) {
        this.prixMoyenActuelTTC = prixMoyenActuelTTC;
    }

    public BigDecimal getPrixMoyenPrecedantTTC() {
        return prixMoyenPrecedantTTC;
    }

    public void setPrixMoyenPrecedantTTC(BigDecimal prixMoyenPrecedantTTC) {
        this.prixMoyenPrecedantTTC = prixMoyenPrecedantTTC;
    }

    public BigDecimal getTauxTvaActuel() {
        return tauxTvaActuel;
    }

    public void setTauxTvaActuel(BigDecimal tauxTvaActuel) {
        this.tauxTvaActuel = tauxTvaActuel;
    }

    public String getListeCategorieArticlesParents() {
        return listeCategorieArticlesParents;
    }

    public void setListeCategorieArticlesParents(String listeCategorieArticlesParents) {
        this.listeCategorieArticlesParents = listeCategorieArticlesParents;
    }

    public BigDecimal getQuantiteTotale() {
        return quantiteTotale;
    }

    public void setQuantiteTotale(BigDecimal quantiteTotale) {
        this.quantiteTotale = quantiteTotale;
    }
    
    

}
