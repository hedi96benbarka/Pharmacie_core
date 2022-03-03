/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.SatisfactionEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
public class FactureDRDetailDTO {

    @NotNull
    private String numbon;
    private String codvend;
    private TypeBonEnum typbon;
    private String numaffiche;
    private CategorieDepotEnum categDepot;
    private String numpiece;
    private LocalDateTime datepiece;
    private SatisfactionEnum satisf;
    private BigDecimal mntbon;
    private String memop;
    private String numdoss;
    private String codAnnul;
    private LocalDateTime datAnnul;
    private String numfacbl;
    private Boolean etatbon;
    private String raisoc;
    private String numCha;
    private Integer coddep;
    @NotNull
    private boolean imprimer;
    private String reffrs;
    private LocalDateTime datreffrs;
    private String sexe;
    private LocalDate dateNaissance;

    @Valid
    private List<MvtstoDRDTO> detailFactureDTOCollection;

    public String getNumbon() {
        return numbon;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumpiece() {
        return numpiece;
    }

    public void setNumpiece(String numpiece) {
        this.numpiece = numpiece;
    }

    public SatisfactionEnum getSatisf() {
        return satisf;
    }

    public void setSatisf(SatisfactionEnum satisf) {
        this.satisf = satisf;
    }

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public String getNumdoss() {
        return numdoss;
    }

    public void setNumdoss(String numdoss) {
        this.numdoss = numdoss;
    }

    public String getCodAnnul() {
        return codAnnul;
    }

    public void setCodAnnul(String codAnnul) {
        this.codAnnul = codAnnul;
    }

    public LocalDateTime getDatAnnul() {
        return datAnnul;
    }

    public void setDatAnnul(LocalDateTime datAnnul) {
        this.datAnnul = datAnnul;
    }

    public String getNumfacbl() {
        return numfacbl;
    }

    public void setNumfacbl(String numfacbl) {
        this.numfacbl = numfacbl;
    }

    public Boolean getEtatbon() {
        return etatbon;
    }

    public void setEtatbon(Boolean etatbon) {
        this.etatbon = etatbon;
    }

    public String getRaisoc() {
        return raisoc;
    }

    public void setRaisoc(String raisoc) {
        this.raisoc = raisoc;
    }

    public String getNumCha() {
        return numCha;
    }

    public void setNumCha(String numCha) {
        this.numCha = numCha;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public boolean isImprimer() {
        return imprimer;
    }

    public void setImprimer(boolean imprimer) {
        this.imprimer = imprimer;
    }

    public String getReffrs() {
        return reffrs;
    }

    public void setReffrs(String reffrs) {
        this.reffrs = reffrs;
    }

    public LocalDateTime getDatreffrs() {
        return datreffrs;
    }

    public void setDatreffrs(LocalDateTime datreffrs) {
        this.datreffrs = datreffrs;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public List<MvtstoDRDTO> getDetailFactureDTOCollection() {
        return detailFactureDTOCollection;
    }

    public void setDetailFactureDTOCollection(List<MvtstoDRDTO> detailFactureDTOCollection) {
        this.detailFactureDTOCollection = detailFactureDTOCollection;
    }

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public LocalDateTime getDatepiece() {
        return datepiece;
    }

    public void setDatepiece(LocalDateTime datepiece) {
        this.datepiece = datepiece;
    }

}
