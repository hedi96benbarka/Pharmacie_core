/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author bassatine
 */
public class FactureBEEditionDTO {

    private BigDecimal mntbon;

    private String memop;

    private Integer coddep;

    private String designationDepot;

    private Integer codeMotifRedressement;
    
    private String designationMotifRedressement;

    private List<MvtStoBEEditionDTO> details;

    private String numbon;

    private String codvend;

    private Date datbon;

    private TypeBonEnum typbon;

    private String numaffiche;

    private CategorieDepotEnum categDepot;

    private Integer codeDemande;

    private String numeroDemande;

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

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }



    public List<MvtStoBEEditionDTO> getDetails() {
        return details;
    }

    public void setDetails(List<MvtStoBEEditionDTO> details) {
        this.details = details;
    }

    public Integer getCodeDemande() {
        return codeDemande;
    }

    public void setCodeDemande(Integer codeDemande) {
        this.codeDemande = codeDemande;
    }

    public String getNumeroDemande() {
        return numeroDemande;
    }

    public void setNumeroDemande(String numeroDemande) {
        this.numeroDemande = numeroDemande;
    }



    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public Date getDatbon() {
        return datbon;
    }

    public void setDatbon(Date datbon) {
        this.datbon = datbon;
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

    public FactureBEEditionDTO() {
    }

    public Integer getCodeMotifRedressement() {
        return codeMotifRedressement;
    }

    public void setCodeMotifRedressement(Integer codeMotifRedressement) {
        this.codeMotifRedressement = codeMotifRedressement;
    }

    public String getDesignationMotifRedressement() {
        return designationMotifRedressement;
    }

    public void setDesignationMotifRedressement(String designationMotifRedressement) {
        this.designationMotifRedressement = designationMotifRedressement;
    }

    @Override
    public String toString() {
        return "FactureBEEditionDTO{" + "mntbon=" + mntbon + ", memop=" + memop + ", coddep=" + coddep + ", designationDepot=" + designationDepot + ", motifRedressement=" + codeMotifRedressement + ", numbon=" + numbon + ", codvend=" + codvend + ", datbon=" + datbon + ", typbon=" + typbon + ", numaffiche=" + numaffiche + ", categDepot=" + categDepot + ", codeDemande=" + codeDemande + ", numeroDemande=" + numeroDemande + '}';
    }

}
