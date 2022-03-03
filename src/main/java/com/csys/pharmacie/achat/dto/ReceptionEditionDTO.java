package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ReceptionEditionDTO {

    private String codvend;

    private String numbon;

    private Date datbon;

    private String numaffiche;

    private BigDecimal mntBon;

    private Date dateFrs;

    private String refFrs;

    private String codeFournisseu;

    private String typbon;

    private Boolean annule;

    private BigDecimal baseAvecTvaZero;
    private BigDecimal baseAvecTvaDifferenteZERO;
    private BigDecimal montantTva;
    
    private Boolean automatique;

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public Date getDatbon() {
        return datbon;
    }

    public void setDatbon(Date datbon) {
        this.datbon = datbon;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public BigDecimal getMntBon() {
        return mntBon;
    }

    public void setMntBon(BigDecimal mntBon) {
        this.mntBon = mntBon;
    }

    public Date getDateFrs() {
        return dateFrs;
    }

    public void setDateFrs(Date dateFrs) {
        this.dateFrs = dateFrs;
    }

    public String getRefFrs() {
        return refFrs;
    }

    public void setRefFrs(String refFrs) {
        this.refFrs = refFrs;
    }

    public String getCodeFournisseu() {
        return codeFournisseu;
    }

    public void setCodeFournisseu(String codeFournisseu) {
        this.codeFournisseu = codeFournisseu;
    }

    public String getTypbon() {
        return typbon;
    }

    public void setTypbon(String typbon) {
        this.typbon = typbon;
    }

    public Boolean getAnnule() {
        return annule;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public BigDecimal getBaseAvecTvaZero() {
        return baseAvecTvaZero;
    }

    public void setBaseAvecTvaZero(BigDecimal BaseAvecTvaZero) {
        this.baseAvecTvaZero = BaseAvecTvaZero;
    }

    public BigDecimal getBaseAvecTvaDifferenteZERO() {
        return baseAvecTvaDifferenteZERO;
    }

    public void setBaseAvecTvaDifferenteZERO(BigDecimal BaseAvecTvaDifferenteZERO) {
        this.baseAvecTvaDifferenteZERO = BaseAvecTvaDifferenteZERO;
    }

    public BigDecimal getMontantTva() {
        return montantTva;
    }

    public void setMontantTva(BigDecimal montantTva) {
        this.montantTva = montantTva;
    }

    public Boolean getAutomatique() {
        return automatique;
    }

    public void setAutomatique(Boolean automatique) {
        this.automatique = automatique;
    }

}
