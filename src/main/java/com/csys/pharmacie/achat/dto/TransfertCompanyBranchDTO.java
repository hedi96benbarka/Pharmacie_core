package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.EnumCrudMethod;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TransfertCompanyBranchDTO {

    @Size(min = 1, max = 20)
    private String numBon;

    @NotNull
    private TypeBonEnum typeBon;

    @NotNull
    private CategorieDepotEnum categDepot;

    @Size(min = 1, max = 14)
    private String numAffiche;

    @Size(min = 0, max = 20)
    private String userCreate;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateCreateReception;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateCreate;

    @Size(min = 1, max = 10)
    private String codeFournisseur;

    private BigDecimal montantTTC;

    @Size(min = 0, max = 20)
    private String numbonReception;

    private Integer codeDepot;

    private Collection<DetailTransfertCompanyBranchDTO> detailTransfertCompanyBranchDTOs;

    private EnumCrudMethod action;

    private Boolean replicated;

    //numbon transfert sur lequel on a fait un retour 
    private String numBonTransfertRelatif;
    private String designationFournisseur;
    private String designationSite;

    private Integer codeSite;
    private Boolean onShelf;
    private Boolean returnedToSupplier;

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public TypeBonEnum getTypeBon() {
        return typeBon;
    }

    public void setTypeBon(TypeBonEnum typeBon) {
        this.typeBon = typeBon;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getNumAffiche() {
        return numAffiche;
    }

    public void setNumAffiche(String numAffiche) {
        this.numAffiche = numAffiche;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public String getNumbonReception() {
        return numbonReception;
    }

    public void setNumbonReception(String numbonReception) {
        this.numbonReception = numbonReception;
    }

    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public Collection<DetailTransfertCompanyBranchDTO> getDetailTransfertCompanyBranchDTOs() {
        return detailTransfertCompanyBranchDTOs;
    }

    public void setDetailTransfertCompanyBranchDTOs(Collection<DetailTransfertCompanyBranchDTO> detailTransfertCompanyBranchDTOs) {
        this.detailTransfertCompanyBranchDTOs = detailTransfertCompanyBranchDTOs;
    }

    public EnumCrudMethod getAction() {
        return action;
    }

    public void setAction(EnumCrudMethod action) {
        this.action = action;
    }

    public Boolean getReplicated() {
        return replicated;
    }

    public void setReplicated(Boolean replicated) {
        this.replicated = replicated;
    }

    public String getNumBonTransfertRelatif() {
        return numBonTransfertRelatif;
    }

    public void setNumBonTransfertRelatif(String numBonTransfertRelatif) {
        this.numBonTransfertRelatif = numBonTransfertRelatif;
    }

    public String getDesignationFournisseur() {
        return designationFournisseur;
    }

    public void setDesignationFournisseur(String designationFournisseur) {
        this.designationFournisseur = designationFournisseur;
    }

    public LocalDateTime getDateCreateReception() {
        return dateCreateReception;
    }

    public void setDateCreateReception(LocalDateTime dateCreateReception) {
        this.dateCreateReception = dateCreateReception;
    }

    public Integer getCodeSite() {
        return codeSite;
    }

    public void setCodeSite(Integer codeSite) {
        this.codeSite = codeSite;
    }

    public String getDesignationSite() {
        return designationSite;
    }

    public void setDesignationSite(String designationSite) {
        this.designationSite = designationSite;
    }

    public Boolean getOnShelf() {
        return onShelf;
    }

    public void setOnShelf(Boolean onShelf) {
        this.onShelf = onShelf;
    }


    public Boolean getReturnedToSupplier() {
        return returnedToSupplier;
    }

    public void setReturnedToSupplier(Boolean returnedToSupplier) {
        this.returnedToSupplier = returnedToSupplier;
    }

    @Override
    public String toString() {
        return "TransfertCompanyBranchDTO{" + "numBon=" + numBon + ", typeBon=" + typeBon + ", categDepot=" + categDepot + ", numAffiche=" + numAffiche + ", userCreate=" + userCreate + ", dateCreate=" + dateCreate + ", codeFournisseur=" + codeFournisseur + ", montantTTC=" + montantTTC + ", numbonReception=" + numbonReception + ", codeDepot=" + codeDepot + ", detailTransfertCompanyBranchDTOs=" + detailTransfertCompanyBranchDTOs + ", action=" + action + ", replicated=" + replicated + '}';
    }

}
