/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.math.BigDecimal;

/**
 *
 * @author Administrateur
 */
public class TotalMouvement {

    private Integer codart;
    private Integer codeUnite;
    private BigDecimal quantite;
    private BigDecimal valeur;
    private Integer coddep;
    private String classificationArticle;

    public TotalMouvement() {
    }

    
    public TotalMouvement(Integer codart, Integer codeUnite, BigDecimal quantite, Integer coddep) {
        this.codart = codart;
        this.codeUnite = codeUnite;
        this.quantite = quantite;
        this.coddep = coddep;
    }

    public TotalMouvement(Integer codart, Integer codeUnite, BigDecimal quantite) {
        this.codart = codart;
        this.codeUnite = codeUnite;
        this.quantite = quantite;
    }

    public TotalMouvement(Integer codart, BigDecimal quantite) {
        this.codart = codart;
        this.quantite = quantite;
    }

    public TotalMouvement(Integer codart, Integer codeUnite, BigDecimal quantite, BigDecimal valeur) {
        this.codart = codart;
        this.codeUnite = codeUnite;
        this.quantite = quantite;
        this.valeur = valeur;
    }
    
    public TotalMouvement(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public TotalMouvement(BigDecimal quantite, BigDecimal valeur) {
        this.quantite = quantite;
        this.valeur = valeur;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public String getClassificationArticle() {
        return classificationArticle;
    }

    public void setClassificationArticle(String classificationArticle) {
        this.classificationArticle = classificationArticle;
    }

//    @Override
//    public String toString() {
//        return "TotalMouvement{" + "codart=" + codart + ", codeUnite=" + codeUnite + ", quantite=" + quantite + ", coddep=" + coddep + '}';
//    }

    @Override
    public String toString() {
        return "TotalMouvement{" + "codart=" + codart + ", codeUnite=" + codeUnite + ", quantite=" + quantite + '}';
    }

}
