package com.csys.pharmacie.achat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DetailTransfertCompanyBranchDTO {

    private Integer code;

    @NotNull
    private Integer codeArticle;

    @Size(min = 0, max = 17)
    private String lotInter;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate datePeremption;

    @Size(min = 0, max = 10)
    private String categDepot;

    @NotNull
    @Size(min = 1, max = 50)
    private String codeSaisi;

    @NotNull
    private Integer codeUnite;

    @Size(min = 0, max = 255)
    private String designationSec;

    @Size(min = 0, max = 255)
    private String designation;

    private BigDecimal quantite;

    @NotNull
    private BigDecimal prixUnitaire;

    private BigDecimal montantHt;

    private Integer codeTva;

    private BigDecimal tauxTva;

    private BigDecimal baseTva;

    private BigDecimal remise;
    @NotNull
    private String numBon;

    private Boolean isCapitalize;

    private BigDecimal prixVente;

    private String codeArticleFournisseur;

    private BigDecimal pmpPrecedent;

    // c est la quantite avant la reception 
    private BigDecimal quantitePrecedante;

    private BigDecimal quantiteRestante;

    private Boolean isReferencePrice;

    private BigDecimal ancienPrixAchat;

    private String designationUnite;

    private Boolean free;

    private Integer codeEmplacement;

    private String designationEmplacement;
    // en cas IMMO retourner le code et designation du departement auquel appartient l'emplacement
    private Integer codeDepartementEmplacement;

    private String desigationDepartementEmplacement;

    public DetailTransfertCompanyBranchDTO() {
    }

    public DetailTransfertCompanyBranchDTO(BigDecimal quantiteRestante) {
        this.quantiteRestante = quantiteRestante;
    }

    public BigDecimal getAncienPrixAchat() {
        return ancienPrixAchat;
    }

    public void setAncienPrixAchat(BigDecimal ancienPrixAchat) {
        this.ancienPrixAchat = ancienPrixAchat;
    }

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public LocalDate getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(LocalDate datePeremption) {
        this.datePeremption = datePeremption;
    }

    public String getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(String categDepot) {
        this.categDepot = categDepot;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationSec() {
        return designationSec;
    }

    public void setDesignationSec(String designationSec) {
        this.designationSec = designationSec;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public BigDecimal getMontantHt() {
        return montantHt;
    }

    public void setMontantHt(BigDecimal montantHt) {
        this.montantHt = montantHt;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public BigDecimal getBaseTva() {
        return baseTva;
    }

    public void setBaseTva(BigDecimal baseTva) {
        this.baseTva = baseTva;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public String getCodeArticleFournisseur() {
        return codeArticleFournisseur;
    }

    public void setCodeArticleFournisseur(String codeArticleFournisseur) {
        this.codeArticleFournisseur = codeArticleFournisseur;
    }

    public BigDecimal getPmpPrecedent() {
        return pmpPrecedent;
    }

    public void setPmpPrecedent(BigDecimal pmpPrecedent) {
        this.pmpPrecedent = pmpPrecedent;
    }

    public BigDecimal getQuantitePrecedante() {
        return quantitePrecedante;
    }

    public void setQuantitePrecedante(BigDecimal quantitePrecedante) {
        this.quantitePrecedante = quantitePrecedante;
    }

    public BigDecimal getQuantiteRestante() {
        return quantiteRestante;
    }

    public void setQuantiteRestante(BigDecimal quantiteRestante) {
        this.quantiteRestante = quantiteRestante;
    }

    public Boolean getIsReferencePrice() {
        return isReferencePrice;
    }

    public void setIsReferencePrice(Boolean isReferencePrice) {
        this.isReferencePrice = isReferencePrice;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public Integer getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(Integer codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
    }

    public String getDesignationEmplacement() {
        return designationEmplacement;
    }

    public void setDesignationEmplacement(String designationEmplacement) {
        this.designationEmplacement = designationEmplacement;
    }

    public Integer getCodeDepartementEmplacement() {
        return codeDepartementEmplacement;
    }

    public void setCodeDepartementEmplacement(Integer codeDepartementEmplacement) {
        this.codeDepartementEmplacement = codeDepartementEmplacement;
    }

    public String getDesigationDepartementEmplacement() {
        return desigationDepartementEmplacement;
    }

    public void setDesigationDepartementEmplacement(String desigationDepartementEmplacement) {
        this.desigationDepartementEmplacement = desigationDepartementEmplacement;
    }

}
