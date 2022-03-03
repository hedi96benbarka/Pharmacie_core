/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
public class DemandeRedressementDTO {
   private Integer code;

    @Size(min = 1, max = 30)
    private String numeroDemande;

    private Date dateCreate;

    private String userCreate;

    private Integer codeDepartement;

    private Integer codeDepot;

    @NotNull
    private String categorieDepot;

    private Date dateValidate;

    @Size(min = 0, max = 50)
    private String userValidate;
    
    private Date dateAnnulation;

    @Size(min = 0, max = 50)
    private String userAnnulation;

    private BigDecimal montantHt;

    @Size(min = 0, max = 50)
    private String observation;

    @NotNull
    private Integer motifDemandeRedressement;
    
    private Integer codeValidationDemadeRedressement;
    
    private String designationValidationDemadeRedressement;

    private String codeSaisiDepot;
    
    private String designationDepot;
    
    private String descriptionMotif;   

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

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Integer getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(Integer codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public String getCategorieDepot() {
        return categorieDepot;
    }

    public void setCategorieDepot(String categorieDepot) {
        this.categorieDepot = categorieDepot;
    }

    public Date getDateValidate() {
        return dateValidate;
    }

    public void setDateValidate(Date dateValidate) {
        this.dateValidate = dateValidate;
    }

    public String getUserValidate() {
        return userValidate;
    }

    public void setUserValidate(String userValidate) {
        this.userValidate = userValidate;
    }

    public Date getDateAnnulation() {
        return dateAnnulation;
    }

    public void setDateAnnulation(Date dateAnnulation) {
        this.dateAnnulation = dateAnnulation;
    }

    public String getUserAnnulation() {
        return userAnnulation;
    }

    public void setUserAnnulation(String userAnnulation) {
        this.userAnnulation = userAnnulation;
    }

    public BigDecimal getMontantHt() {
        return montantHt;
    }

    public void setMontantHt(BigDecimal montantHt) {
        this.montantHt = montantHt;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Integer getMotifDemandeRedressement() {
        return motifDemandeRedressement;
    }

    public void setMotifDemandeRedressement(Integer motifDemandeRedressement) {
        this.motifDemandeRedressement = motifDemandeRedressement;
    }

    public Integer getCodeValidationDemadeRedressement() {
        return codeValidationDemadeRedressement;
    }

    public void setCodeValidationDemadeRedressement(Integer codeValidationDemadeRedressement) {
        this.codeValidationDemadeRedressement = codeValidationDemadeRedressement;
    }

    public String getDesignationValidationDemadeRedressement() {
        return designationValidationDemadeRedressement;
    }

    public void setDesignationValidationDemadeRedressement(String designationValidationDemadeRedressement) {
        this.designationValidationDemadeRedressement = designationValidationDemadeRedressement;
    }

    public String getCodeSaisiDepot() {
        return codeSaisiDepot;
    }

    public void setCodeSaisiDepot(String codeSaisiDepot) {
        this.codeSaisiDepot = codeSaisiDepot;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public String getDescriptionMotif() {
        return descriptionMotif;
    }

    public void setDescriptionMotif(String descriptionMotif) {
        this.descriptionMotif = descriptionMotif;
    }

}


