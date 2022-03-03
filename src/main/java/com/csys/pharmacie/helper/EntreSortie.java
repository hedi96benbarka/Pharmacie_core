/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Farouk
 */
public class EntreSortie {

    private String numbon;
    private BigDecimal entree;
    private BigDecimal sortie;
    private BigDecimal solde = BigDecimal.ZERO;
    private Date datPer;
    private String lotinter;
    private Integer codeUnite;
    private String designationUnite;
    private BigDecimal prix;
    private BigDecimal valeur;

    public EntreSortie(String numbon, BigDecimal entree, BigDecimal sortie, Integer codeUnite, String designationUnite) {
        this.numbon = numbon;
        this.entree = entree;
        this.sortie = sortie;
        this.codeUnite = codeUnite;
        this.designationUnite = designationUnite;
    }

    public EntreSortie() {

    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public BigDecimal getEntree() {
        return entree;
    }

    public void setEntree(BigDecimal entree) {
        this.entree = entree;
    }

    public BigDecimal getSortie() {
        return sortie;
    }

    public void setSortie(BigDecimal sortie) {
        this.sortie = sortie;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public Date getDatPer() {
        return datPer;
    }

    public void setDatPer(Date datPer) {
        this.datPer = datPer;
    }

    public String getLotinter() {
        return lotinter;
    }

    public void setLotinter(String lotinter) {
        this.lotinter = lotinter;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return "EntreSortie{" + "numbon=" + numbon + ", entree=" + entree + ", sortie=" + sortie + ", solde=" + solde + ", codeUnite=" + codeUnite + ", designationUnite=" + designationUnite + '}';
    }

}
