package com.csys.pharmacie.vente.quittance.dto;

import com.csys.pharmacie.helper.SatisfactionEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ListDemandeRecuperationDto {

    private String numBon;
    private String numAffiche;
    private LocalDateTime date;
    private String numDoss;
    private String numChambre;
    private String nomPatient;
    private Boolean valide;
    private List<String> numQuittance;
    private LitDTO lit;
    private LocalDate dateNaissance;
    private String sexe;
    private String user;
    private LocalDateTime dateSup;
    private String userCreate;
    private String designationDepotSec;
    
    private SatisfactionEnum satisfaction;

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public ListDemandeRecuperationDto(String numbon, String numaffiche, LocalDateTime datbon, String numdoss, String numCha, String raisoc, boolean valide, LocalDate dateNaissance, List<String> numQuittance, String sexe, String userCreate) {
        super();
        this.numBon = numbon;
        this.numAffiche = numaffiche;
        this.date = datbon;
        this.numDoss = numdoss;
        this.numChambre = numCha;
        this.nomPatient = raisoc;
        this.valide = valide;
        this.numQuittance = numQuittance;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.userCreate = userCreate;

    }

    public ListDemandeRecuperationDto(String numbon, String numaffiche, LocalDateTime datbon, String numdoss, String numCha, String raisoc, boolean valide, LocalDate dateNaissance, List<String> numQuittance, String sexe) {
        super();
        this.numBon = numbon;
        this.numAffiche = numaffiche;
        this.date = datbon;
        this.numDoss = numdoss;
        this.numChambre = numCha;
        this.nomPatient = raisoc;
        this.valide = valide;
        this.numQuittance = numQuittance;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;

    }

    public ListDemandeRecuperationDto(String numbon, String numaffiche, LocalDateTime datbon, String numdoss, String numCha, String raisoc, boolean valide, LocalDate dateNaissance, String sexe, String userSup, LocalDateTime dateSup) {
        super();
        this.numBon = numbon;
        this.numAffiche = numaffiche;
        this.date = datbon;
        this.numDoss = numdoss;
        this.numChambre = numCha;
        this.nomPatient = raisoc;
        this.valide = valide;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.setUser(userSup);
        this.setDateSup(dateSup);

    }

    public ListDemandeRecuperationDto() {
    }

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getNumDoss() {
        return numDoss;
    }

    public void setNumDoss(String numDoss) {
        this.numDoss = numDoss;
    }

    public String getNumChambre() {
        return numChambre;
    }

    public void setNumChambre(String numChambre) {
        this.numChambre = numChambre;
    }

    public String getNomPatient() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    public Boolean getValide() {
        return valide;
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

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public LocalDateTime getDateSup() {
        return dateSup;
    }

    public void setDateSup(LocalDateTime dateSup) {
        this.dateSup = dateSup;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LitDTO getLit() {
        return lit;
    }

    public void setLit(LitDTO lit) {
        this.lit = lit;
    }

    public SatisfactionEnum getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(SatisfactionEnum satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getDesignationDepotSec() {
        return designationDepotSec;
    }

    public void setDesignationDepotSec(String designationDepotSec) {
        this.designationDepotSec = designationDepotSec;
    }

}
