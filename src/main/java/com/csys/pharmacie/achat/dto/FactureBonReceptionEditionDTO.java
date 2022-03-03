/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.BaseBonDTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
public class FactureBonReceptionEditionDTO  extends BaseBonDTO   {

    @NotNull
    @Size(min = 0, max = 50)
    private String codeFournisseur;

    @NotNull

    private Date dateFournisseur;

    @NotNull
    @Size(min = 1, max = 50)
    private String referenceFournisseur;

//    private String codvend;

//    private Date datesys;

//    private LocalTime heuresys;

//    private String numaffiche;
//
//    private String numbon;

//    private String numbon;

    private BigDecimal montant;

    private Date datbon;
//
//    private TypeBonEnum typbon;

    private CategorieDepotEnum categDepot;

    private BigDecimal remiseExep;
    
    private FournisseurDTO fournisseur;

    private List<BonRecepDTO> BonReceptionCollection;
    private List<MvtstoBADTO> detailReceptionCollection;
    private List<BaseTvaFactureBonReceptionDTO> baseTva;
    
    
    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }



    public String getReferenceFournisseur() {
        return referenceFournisseur;
    }

    public void setReferenceFournisseur(String referenceFournisseur) {
        this.referenceFournisseur = referenceFournisseur;
    }

//    public String getCodvend() {
//        return codvend;
//    }
//
//    public void setCodvend(String codvend) {
//        this.codvend = codvend;
//    }

  

//    public String getNumaffiche() {
//        return numaffiche;
//    }
//
//    public void setNumaffiche(String numaffiche) {
//        this.numaffiche = numaffiche;
//    }

//    public String getNumbon() {
//        return numbon;
//    }
//
//    public void setNumbon(String numbon) {
//        this.numbon = numbon;
//    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Date getDateFournisseur() {
        return dateFournisseur;
    }

    public void setDateFournisseur(Date dateFournisseur) {
        this.dateFournisseur = dateFournisseur;
    }

 

    public void setDatbon(Date datbon) {
        this.datbon = datbon;
    }




//
//    public void setTypbon(TypeBonEnum typbon) {
//        this.typbon = typbon;
//    }

//    public CategorieDepotEnum getCategDepot() {
//        return categDepot;
//    }
//
//    public void setCategDepot(CategorieDepotEnum categDepot) {
//        this.categDepot = categDepot;
//    }

    public BigDecimal getRemiseExep() {
        return remiseExep;
    }

    public void setRemiseExep(BigDecimal remiseExep) {
        this.remiseExep = remiseExep;
    }

    public List<BonRecepDTO> getBonReceptionCollection() {
        return BonReceptionCollection;
    }

    public void setBonReceptionCollection(List<BonRecepDTO> BonReceptionCollection) {
        this.BonReceptionCollection = BonReceptionCollection;
    }

    public List<MvtstoBADTO> getDetailReceptionCollection() {
        return detailReceptionCollection;
    }

    public void setDetailReceptionCollection(List<MvtstoBADTO> detailReceptionCollection) {
        this.detailReceptionCollection = detailReceptionCollection;
    }

    public List<BaseTvaFactureBonReceptionDTO> getBaseTva() {
        return baseTva;
    }

    public void setBaseTva(List<BaseTvaFactureBonReceptionDTO> baseTva) {
        this.baseTva = baseTva;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

}
