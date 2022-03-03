/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class FactureDREditionDTO {

    private String numBon;

    private String numAffiche;

    private String numChambre;

    private String numDoss;

    private String depotDesignation;

    private String codePatient;

    private Date date;

    private String nomPatient;
    
    private List<DemandeArticlesDto> demandes;
    
    private Boolean imprimer;
    
    private Integer depotCode ;

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public String getNumAffiche() {
        return numAffiche;
    }

    public void setNumAffiche(String numAffiche) {
        this.numAffiche = numAffiche;
    }

    public String getNumChambre() {
        return numChambre;
    }

    public void setNumChambre(String numChambre) {
        this.numChambre = numChambre;
    }

    public String getNumDoss() {
        return numDoss;
    }

    public void setNumDoss(String numDoss) {
        this.numDoss = numDoss;
    }

    public String getDepotDesignation() {
        return depotDesignation;
    }

    public void setDepotDesignation(String depotDesignation) {
        this.depotDesignation = depotDesignation;
    }



    public String getCodePatient() {
        return codePatient;
    }

    public void setCodePatient(String codePatient) {
        this.codePatient = codePatient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNomPatient() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    public List<DemandeArticlesDto> getDemandes() {
        return demandes;
    }

    public void setDemandes(List<DemandeArticlesDto> demandes) {
        this.demandes = demandes;
    }

    public Boolean getImprimer() {
        return imprimer;
    }

    public void setImprimer(Boolean imprimer) {
        this.imprimer = imprimer;
    }

    public Integer getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(Integer depotCode) {
        this.depotCode = depotCode;
    }

    
}
