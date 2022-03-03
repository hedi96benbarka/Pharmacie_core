package com.csys.pharmacie.vente.quittance.dto;

import javax.persistence.Basic;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class OrdonnanceDTO {

    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @NotNull
    private String codMed;

    @NotNull
    @Size(max = 12)
    private String numDoss;

    @NotNull
    private Integer etat;

    @NotNull
    private String type;

    @NotNull
    private int satisf;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    private String userCreate;

    private List<DetailsOrdonnanceDTO> detailsOrdonnanceList;

    private String nomCompletAr;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date  dateNaissance;
    
    private String codepatient;

    private boolean isFacturee;
    
    public String getNomCompletAr() {
        return nomCompletAr;
    }

    public void setNomCompletAr(String nomCompletAr) {
        this.nomCompletAr = nomCompletAr;
    }
    
    public int getSatisf() {
        return satisf;
    }

    public void setSatisf(int satisf) {
        this.satisf = satisf;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCodMed() {
        return codMed;
    }

    public void setCodMed(String codMed) {
        this.codMed = codMed;
    }

    public String getNumDoss() {
        return numDoss;
    }

    public void setNumDoss(String numDoss) {
        this.numDoss = numDoss;
    }

    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DetailsOrdonnanceDTO> getDetailsOrdonnanceList() {
        return detailsOrdonnanceList;
    }

    public void setDetailsOrdonnanceList(List<DetailsOrdonnanceDTO> detailsOrdonnanceList) {
        this.detailsOrdonnanceList = detailsOrdonnanceList;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getCodepatient() {
        return codepatient;
    }

    public void setCodepatient(String codepatient) {
        this.codepatient = codepatient;
    }

    public boolean isIsFacturee() {
        return isFacturee;
    }

    public void setIsFacturee(boolean isFacturee) {
        this.isFacturee = isFacturee;
    }

}
