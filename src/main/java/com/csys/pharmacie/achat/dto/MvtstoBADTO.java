/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.BaseDetailBonDTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Farouk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MvtstoBADTO extends BaseDetailBonDTO {

    private LocalDate datPer;
    private String lotInter;
    private String numOrdre;
    private String codeArtFrs;
    private Boolean isPrixRef;

    private BigDecimal sellingPrice;
    private Boolean free;
    private BigDecimal qteReturned;

    private BigDecimal baseTva;

//   meme variable que datPer mais de Type Date pour les editions 
    @JsonIgnore
    private Date dateEdition;
    @JsonIgnore
    private Date datePerEdition;

    private BigDecimal oldTauTva;

    private Integer oldCodTva;
    private String unitDesignation;

    private Integer codeEmplacement;

    private String designationEmplacement;
    // en cas IMMO retourner le code et designation du departement auquel appartient l'emplacement
    private Integer codeDepartementEmplacement;

    private String desigationDepartementEmplacement;

    private Integer codeCategorieArticle;

    private String designationCategorieArticle;

    private Integer codeCommande;

    private Boolean quantiteGratuiteFromCommande;

//    @JsonIgnore
//    private FactureBA factureBA;
    @JsonIgnore
    private String typeMouvement;
//    private BigDecimal qteReturned;
    private Boolean isCapitalize;

//    private Boolean free;
//    private String unitDesignation;
    private BigDecimal qteCom;

    private BigDecimal montantTvaGratuite;

    private BigDecimal ancienPrixAchat;

    private String typbon;

    private CategorieDepotEnum categDepot;

    public MvtstoBADTO() {
    }

    public MvtstoBADTO(BigDecimal priuni) {
        super(priuni);
    }

    public String getCodeArtFrs() {
        return codeArtFrs;
    }

    public void setCodeArtFrs(String codeArtFrs) {
        this.codeArtFrs = codeArtFrs;
    }

    public String getNumOrdre() {
        return numOrdre;
    }

    public void setNumOrdre(String numOrdre) {
        this.numOrdre = numOrdre;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public Boolean getIsPrixRef() {
        return isPrixRef;
    }

    public void setIsPrixRef(Boolean isPrixRef) {
        this.isPrixRef = isPrixRef;
    }

//    public BigDecimal getQteReturned() {
//        return qteReturned;
//    }
//
//    public void setQteReturned(BigDecimal qteReturned) {
//        this.qteReturned = qteReturned;
//    }
    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Date getDatePerEdition() {
        return datePerEdition;
    }

    public void setDatePerEdition(Date datePerEdition) {
        this.datePerEdition = datePerEdition;
    }

    public BigDecimal getBaseTva() {
        return baseTva;
    }

    public void setBaseTva(BigDecimal baseTva) {
        this.baseTva = baseTva;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public BigDecimal getOldTauTva() {
        return oldTauTva;
    }

    public void setOldTauTva(BigDecimal oldTauTva) {
        this.oldTauTva = oldTauTva;
    }

    public Integer getOldCodTva() {
        return oldCodTva;
    }

    public void setOldCodTva(Integer oldCodTva) {
        this.oldCodTva = oldCodTva;
    }

    public String getUnitDesignation() {
        return unitDesignation;
    }

    public void setUnitDesignation(String unitDesignation) {
        this.unitDesignation = unitDesignation;
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

    public Integer getCodeCommande() {
        return codeCommande;
    }

    public void setCodeCommande(Integer codeCommande) {
        this.codeCommande = codeCommande;
    }

    public Date getDateEdition() {
        return dateEdition;
    }

    public void setDateEdition(Date dateEdition) {
        this.dateEdition = dateEdition;
    }

    public String getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(String typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
    }

    public BigDecimal getQteReturned() {
        return qteReturned;
    }

    public void setQteReturned(BigDecimal qteReturned) {
        this.qteReturned = qteReturned;
    }

    public Integer getCodeCategorieArticle() {
        return codeCategorieArticle;
    }

    public void setCodeCategorieArticle(Integer codeCategorieArticle) {
        this.codeCategorieArticle = codeCategorieArticle;
    }

    public String getDesignationCategorieArticle() {
        return designationCategorieArticle;
    }

    public void setDesignationCategorieArticle(String designationCategorieArticle) {
        this.designationCategorieArticle = designationCategorieArticle;
    }

    public BigDecimal getQteCom() {
        return qteCom;
    }

    public void setQteCom(BigDecimal qteCom) {
        this.qteCom = qteCom;
    }

    public BigDecimal getMontantTvaGratuite() {
        return montantTvaGratuite;
    }

    public void setMontantTvaGratuite(BigDecimal montantTvaGratuite) {
        this.montantTvaGratuite = montantTvaGratuite;
    }

    public BigDecimal getAncienPrixAchat() {
        return ancienPrixAchat;
    }

    public void setAncienPrixAchat(BigDecimal ancienPrixAchat) {
        this.ancienPrixAchat = ancienPrixAchat;
    }

    public Boolean getQuantiteGratuiteFromCommande() {
        return quantiteGratuiteFromCommande;
    }

    public void setQuantiteGratuiteFromCommande(Boolean quantiteGratuiteFromCommande) {
        this.quantiteGratuiteFromCommande = quantiteGratuiteFromCommande;
    }

    public String getTypbon() {
        return typbon;
    }

    public void setTypbon(String typbon) {
        this.typbon = typbon;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

}
