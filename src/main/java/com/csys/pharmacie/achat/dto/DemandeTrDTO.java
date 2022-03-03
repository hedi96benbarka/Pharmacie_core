/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.prelevement.dto.DetailDemandePrTrDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemandeTrDTO {

    @Id
    private Integer code;

    @Size(min = 1, max = 30)
    private String numeroDemande;

    private Date dateCreate;

    @Size(min = 1, max = 50)
    private String userCreate;

    private BigDecimal montantHT;

    private Integer delai;

    @Size(min = 1, max = 50)
    private String validePar;

    @Size(min = 1, max = 50)
    private String observation;

    private Date dateValidation;

    private String categorieDepot;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateArchive;

    private Boolean accepted;

    @OneToMany(fetch = FetchType.EAGER)
    private List<DetailDemandePrTrDTO> detailsDemande;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNumeroDemande() {
        return numeroDemande;
    }

    public void setNumeroDemande(String numeroDemande) {
        this.numeroDemande = numeroDemande;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public BigDecimal getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(BigDecimal montantHT) {
        this.montantHT = montantHT;
    }

    public Integer getDelai() {
        return delai;
    }

    public void setDelai(Integer delai) {
        this.delai = delai;
    }

    public String getValidePar() {
        return validePar;
    }

    public void setValidePar(String validePar) {
        this.validePar = validePar;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getCategorieDepot() {
        return categorieDepot;
    }

    public void setCategorieDepot(String categorieDepot) {
        this.categorieDepot = categorieDepot;
    }

    public List<DetailDemandePrTrDTO> getDetailsDemande() {
        return detailsDemande;
    }

    public void setDetailsDemande(List<DetailDemandePrTrDTO> detailsDemande) {
        this.detailsDemande = detailsDemande;
    }

    public Date getDateArchive() {
        return dateArchive;
    }

    public void setDateArchive(Date dateArchive) {
        this.dateArchive = dateArchive;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
