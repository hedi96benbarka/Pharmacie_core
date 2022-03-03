package com.csys.pharmacie.vente.quittance.dto;

import com.csys.pharmacie.helper.SatisfactionEnum;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class DemandeRecuperationDto {

    private String numBon;
    private Boolean valide;
    private List<String> numQuittance;
    private PatientDto patient;
    private String validePar;
    private LocalDateTime dateValidation;
    private Boolean ouverte;
    private String ouvertePar;
    private Date dateOuverture;
    private String adresseMac;
    private SatisfactionEnum satisfaction;
      private Integer coddep;
         private String designationDepot;
         private String designationDepotSec;

    private List<DemandeArticlesDto> demandes;

    public Boolean getValide() {
        return valide;
    }

    public Boolean getOuverte() {
        return ouverte;
    }

    public void setOuverte(Boolean ouverte) {
        this.ouverte = ouverte;
    }

    public String getOuvertePar() {
        return ouvertePar;
    }

    public void setOuvertePar(String ouvertePar) {
        this.ouvertePar = ouvertePar;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public List<String> getNumQuittance() {
        return numQuittance;
    }

    public void setNumQuittance(List<String> numQuittance) {
        this.numQuittance = numQuittance;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public PatientDto getPatient() {
        return patient;
    }

    public void setPatient(PatientDto patient) {
        this.patient = patient;
    }

    public List<DemandeArticlesDto> getDemandes() {
        return demandes;
    }

    public void setDemandes(List<DemandeArticlesDto> demandes) {
        this.demandes = demandes;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getValidePar() {
        return validePar;
    }

    public void setValidePar(String validePar) {
        this.validePar = validePar;
    }

    public Date getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Date dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public String getAdresseMac() {
        return adresseMac;
    }

    public void setAdresseMac(String adresseMac) {
        this.adresseMac = adresseMac;
    }

    public SatisfactionEnum getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(SatisfactionEnum satisfaction) {
        this.satisfaction = satisfaction;
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

    public String getDesignationDepotSec() {
        return designationDepotSec;
    }

    public void setDesignationDepotSec(String designationDepotSec) {
        this.designationDepotSec = designationDepotSec;
    }

}
