/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Farouk
 */
public class Mouvement {

    private String id;
    private Date date;
    private String codeSaisi;
    private String designation;
    private String operation;
    private String numbon;
    private String typbon;
    private String numaffiche;
    private String libelle;
    private Integer coddep;
    private String designationDepot;
    private List<EntreSortie> list;
    
    private BigDecimal entree;
    private BigDecimal sortie;
    private BigDecimal solde = BigDecimal.ZERO;
    private Date datPer;
    private String lotinter;
    private Integer codeUnite;
    private String designationUnite;
    private BigDecimal prix;
    private BigDecimal valeur;

    public Mouvement(Date date, String codeSaisi, String designation, String operation, String numbon, BigDecimal prix, String libelle, List<EntreSortie> list) {
        this.date = date;
        this.codeSaisi = codeSaisi;
        this.designation = designation;
        this.operation = operation;
        this.numbon = numbon;
        this.libelle = libelle;
        this.list = list;
    }

    public Mouvement() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<EntreSortie> getList() {
        return list;
    }

    public void setList(List<EntreSortie> list) {
        this.list = list;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public String getTypbon() {
        return typbon;
    }

    public void setTypbon(String typbon) {
        this.typbon = typbon;
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
        return "Mouvement{" + "id=" + id + ", date=" + date + ", codeSaisi=" + codeSaisi + ", designation=" + designation + ", operation=" + operation + ", numbon=" + numbon + ", typbon=" + typbon + ", numaffiche=" + numaffiche + ", libelle=" + libelle + ", coddep=" + coddep + ", designationDepot=" + designationDepot + ", entree=" + entree + ", sortie=" + sortie + ", solde=" + solde + ", datPer=" + datPer + ", lotinter=" + lotinter + ", codeUnite=" + codeUnite + ", designationUnite=" + designationUnite + ", prix=" + prix + ", valeur=" + valeur + '}';
    }

    
    
   

}
