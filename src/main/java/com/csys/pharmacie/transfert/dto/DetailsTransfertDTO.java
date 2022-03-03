/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DELL
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailsTransfertDTO {

    private Integer codeDepotSource;

    private Integer codeDepotDest;

    private String designationDepotSource;

    private String designationDepotDestination;

    private Integer codeArticle;

    private String numbon;

    private String numordre;

    private String lotInter;

    private BigDecimal prixUnitaire;

    private Date datePeremption;

    private Integer codeUnite;

    private String designationUnite;

    private CategorieDepotEnum categDepot;

    private String designation;

    private String designationSecondaire;

    private String codeSaisi;

    private BigDecimal quantite;
    
    private BigDecimal quantiteRecue;
    
    private BigDecimal quantiteDefectueuse;
    
    private String typeMvt;

    private boolean interdepot;

    private boolean avoirTransfert;
    /////////////
//    private BigDecimal qteben;

//    private BigDecimal mntHT;
    private BigDecimal mntTTC;

    private Integer codeTvaA;

    private BigDecimal tauxTvaA;

    private Date dateCreate;

    private String numAffiche;

    private Integer codeCategorieArticle;

    private String designationCategorieArticle;

    private BigDecimal valeur;

    private String numAfficheRetourTransfert;
    
    private Boolean valide;

    private Boolean conforme;

    private String userValidate;

    private Date dateValidate;
    
    

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Date getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(Date datePeremption) {
        this.datePeremption = datePeremption;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignationSecondaire() {
        return designationSecondaire;
    }

    public void setDesignationSecondaire(String designationSecondaire) {
        this.designationSecondaire = designationSecondaire;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public String getSourceDesignation() {
        return designationDepotSource;
    }

    public void setSourceDesignation(String sourceDesignation) {
        this.designationDepotSource = sourceDesignation;
    }

    public String getDesignationDepotDestination() {
        return designationDepotDestination;
    }

    public void setDesignationDepotDestination(String designationDepotDestination) {
        this.designationDepotDestination = designationDepotDestination;
    }
//
//    public BigDecimal getMntHT() {
//        return mntHT;
//    }
//
//    public void setMntHT(BigDecimal mntHT) {
//        this.mntHT = mntHT;
//    }
//

    public BigDecimal getMntTTC() {
        return mntTTC;
    }

    public void setMntTTC(BigDecimal mntTTC) {
        this.mntTTC = mntTTC;
    }

    public Integer getCodeTvaA() {
        return codeTvaA;
    }

    public void setCodeTvaA(Integer codeTvaA) {
        this.codeTvaA = codeTvaA;
    }

    public BigDecimal getTauxTvaA() {
        return tauxTvaA;
    }

    public void setTauxTvaA(BigDecimal TauxTvaA) {
        this.tauxTvaA = TauxTvaA;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getNumAffiche() {
        return numAffiche;
    }

    public void setNumAffiche(String numAffiche) {
        this.numAffiche = numAffiche;
    }

    public Integer getCodeDepotSource() {
        return codeDepotSource;
    }

    public void setCodeDepotSource(Integer codeDepotSource) {
        this.codeDepotSource = codeDepotSource;
    }

    public Integer getCodeDepotDest() {
        return codeDepotDest;
    }

    public void setCodeDepotDest(Integer CodeDepotDest) {
        this.codeDepotDest = CodeDepotDest;
    }

    public String getDesignationDepotSource() {
        return designationDepotSource;
    }

    public void setDesignationDepotSource(String designationDepotSource) {
        this.designationDepotSource = designationDepotSource;
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

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public String getTypeMvt() {
        return typeMvt;
    }

    public void setTypeMvt(String typeMvt) {
        this.typeMvt = typeMvt;
    }

    public boolean isInterdepot() {
        return interdepot;
    }

    public void setInterdepot(boolean interdepot) {
        this.interdepot = interdepot;
    }

    public boolean isAvoirTransfert() {
        return avoirTransfert;
    }

    public void setAvoirTransfert(boolean avoirTransfert) {
        this.avoirTransfert = avoirTransfert;
    }

    public BigDecimal getQuantiteRecue() {
        return quantiteRecue;
    }

    public void setQuantiteRecue(BigDecimal quantiteRecue) {
        this.quantiteRecue = quantiteRecue;
    }

    public BigDecimal getQuantiteDefectueuse() {
        return quantiteDefectueuse;
    }

    public void setQuantiteDefectueuse(BigDecimal quantiteDefectueuse) {
        this.quantiteDefectueuse = quantiteDefectueuse;
    }

    public String getNumAfficheRetourTransfert() {
        return numAfficheRetourTransfert;
    }

    public void setNumAfficheRetourTransfert(String numAfficheRetourTransfert) {
        this.numAfficheRetourTransfert = numAfficheRetourTransfert;
    }

    public Boolean getValide() {
        return valide;
    }

    public void setValide(Boolean valide) {
        this.valide = valide;
    }

    public Boolean getConforme() {
        return conforme;
    }

    public void setConforme(Boolean conforme) {
        this.conforme = conforme;
    }

    public String getUserValidate() {
        return userValidate;
    }

    public void setUserValidate(String userValidate) {
        this.userValidate = userValidate;
    }

    public Date getDateValidate() {
        return dateValidate;
    }

    public void setDateValidate(Date dateValidate) {
        this.dateValidate = dateValidate;
    }

}
