package com.csys.pharmacie.vente.quittance.dto;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DetailsAdmissionDTO {

    private Long code;

    private Integer codePrestation;

    @Size(min = 0, max = 50)
    private String numQuittance;

    @NotNull
    @Size(min = 1, max = 200)
    private String designationPrestationAr;

    @NotNull
    @Size(min = 1, max = 200)
    private String designationPrestationEn;

    private BigDecimal montant;

    private BigDecimal montantPatient;

    private BigDecimal montantPec;

    private BigDecimal montantTva;

    private BigDecimal montantTvaPatient;

    private BigDecimal montantTvaPec;

    private BigDecimal montantMasterPriceList;

    private BigDecimal quantite;

    private BigDecimal prixUnitaire;

    private BigDecimal prixUnitaireMasterPriceList;

    private Integer etatPaiement;

    @Size(min = 1, max = 20)
    private String userCreate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreate;

    private Integer codeMedecin;

    private Long codeDemande;

    private BigDecimal montantQuittanceTTC;

    private Integer codeInfirmier;

    private BigDecimal montantRemise;

    private BigDecimal montantRemisePec;

    private String designationFamilleFacturationAr;

    private String designationFamilleFacturationEn;

    private Integer codeFamilleFacturation;

    private Integer costCenter;

    private Integer codeService;
    private String codeAdmission;

    private String codeFactureAdmission;

    private BigDecimal montantRejected;

    private Integer codeOperation;

    private BigDecimal pourcentageMajoration;

    private BigDecimal pourcentageRemise;

    private String nomInfirmier;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Integer getCodePrestation() {
        return codePrestation;
    }

    public void setCodePrestation(Integer codePrestation) {
        this.codePrestation = codePrestation;
    }

    public String getNumQuittance() {
        return numQuittance;
    }

    public void setNumQuittance(String numQuittance) {
        this.numQuittance = numQuittance;
    }

    public String getDesignationPrestationAr() {
        return designationPrestationAr;
    }

    public void setDesignationPrestationAr(String designationPrestationAr) {
        this.designationPrestationAr = designationPrestationAr;
    }

    public String getDesignationPrestationEn() {
        return designationPrestationEn;
    }

    public void setDesignationPrestationEn(String designationPrestationEn) {
        this.designationPrestationEn = designationPrestationEn;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getMontantPatient() {
        return montantPatient;
    }

    public void setMontantPatient(BigDecimal montantPatient) {
        this.montantPatient = montantPatient;
    }

    public BigDecimal getMontantPec() {
        return montantPec;
    }

    public void setMontantPec(BigDecimal montantPec) {
        this.montantPec = montantPec;
    }

    public BigDecimal getMontantTva() {
        return montantTva;
    }

    public void setMontantTva(BigDecimal montantTva) {
        this.montantTva = montantTva;
    }

    public BigDecimal getMontantTvaPatient() {
        return montantTvaPatient;
    }

    public void setMontantTvaPatient(BigDecimal montantTvaPatient) {
        this.montantTvaPatient = montantTvaPatient;
    }

    public BigDecimal getMontantTvaPec() {
        return montantTvaPec;
    }

    public void setMontantTvaPec(BigDecimal montantTvaPec) {
        this.montantTvaPec = montantTvaPec;
    }

    public BigDecimal getMontantMasterPriceList() {
        return montantMasterPriceList;
    }

    public void setMontantMasterPriceList(BigDecimal montantMasterPriceList) {
        this.montantMasterPriceList = montantMasterPriceList;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getPrixUnitaireMasterPriceList() {
        return prixUnitaireMasterPriceList;
    }

    public void setPrixUnitaireMasterPriceList(BigDecimal prixUnitaireMasterPriceList) {
        this.prixUnitaireMasterPriceList = prixUnitaireMasterPriceList;
    }

    public Integer getEtatPaiement() {
        return etatPaiement;
    }

    public void setEtatPaiement(Integer etatPaiement) {
        this.etatPaiement = etatPaiement;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Integer getCodeMedecin() {
        return codeMedecin;
    }

    public void setCodeMedecin(Integer codeMedecin) {
        this.codeMedecin = codeMedecin;
    }

    public Long getCodeDemande() {
        return codeDemande;
    }

    public void setCodeDemande(Long codeDemande) {
        this.codeDemande = codeDemande;
    }

    public BigDecimal getMontantQuittanceTTC() {
        return montantQuittanceTTC;
    }

    public void setMontantQuittanceTTC(BigDecimal montantQuittanceTTC) {
        this.montantQuittanceTTC = montantQuittanceTTC;
    }

    public Integer getCodeInfirmier() {
        return codeInfirmier;
    }

    public void setCodeInfirmier(Integer codeInfirmier) {
        this.codeInfirmier = codeInfirmier;
    }

    public BigDecimal getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(BigDecimal montantRemise) {
        this.montantRemise = montantRemise;
    }

    public BigDecimal getMontantRemisePec() {
        return montantRemisePec;
    }

    public void setMontantRemisePec(BigDecimal montantRemisePec) {
        this.montantRemisePec = montantRemisePec;
    }

    public String getDesignationFamilleFacturationAr() {
        return designationFamilleFacturationAr;
    }

    public void setDesignationFamilleFacturationAr(String designationFamilleFacturationAr) {
        this.designationFamilleFacturationAr = designationFamilleFacturationAr;
    }

    public String getDesignationFamilleFacturationEn() {
        return designationFamilleFacturationEn;
    }

    public void setDesignationFamilleFacturationEn(String designationFamilleFacturationEn) {
        this.designationFamilleFacturationEn = designationFamilleFacturationEn;
    }

    public Integer getCodeFamilleFacturation() {
        return codeFamilleFacturation;
    }

    public void setCodeFamilleFacturation(Integer codeFamilleFacturation) {
        this.codeFamilleFacturation = codeFamilleFacturation;
    }

    public Integer getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(Integer costCenter) {
        this.costCenter = costCenter;
    }

    public Integer getCodeService() {
        return codeService;
    }

    public void setCodeService(Integer codeService) {
        this.codeService = codeService;
    }

    public String getCodeAdmission() {
        return codeAdmission;
    }

    public void setCodeAdmission(String codeAdmission) {
        this.codeAdmission = codeAdmission;
    }

    public String getCodeFactureAdmission() {
        return codeFactureAdmission;
    }

    public void setCodeFactureAdmission(String codeFactureAdmission) {
        this.codeFactureAdmission = codeFactureAdmission;
    }

    public BigDecimal getMontantRejected() {
        return montantRejected;
    }

    public void setMontantRejected(BigDecimal montantRejected) {
        this.montantRejected = montantRejected;
    }

    public Integer getCodeOperation() {
        return codeOperation;
    }

    public void setCodeOperation(Integer codeOperation) {
        this.codeOperation = codeOperation;
    }

    public BigDecimal getPourcentageMajoration() {
        return pourcentageMajoration;
    }

    public void setPourcentageMajoration(BigDecimal pourcentageMajoration) {
        this.pourcentageMajoration = pourcentageMajoration;
    }

    public BigDecimal getPourcentageRemise() {
        return pourcentageRemise;
    }

    public void setPourcentageRemise(BigDecimal pourcentageRemise) {
        this.pourcentageRemise = pourcentageRemise;
    }

    public String getNomInfirmier() {
        return nomInfirmier;
    }

    public void setNomInfirmier(String nomInfirmier) {
        this.nomInfirmier = nomInfirmier;
    }

}
