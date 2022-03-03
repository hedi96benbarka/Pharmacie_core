package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FcptfrsPHdto {

    private Long numOpr;
    @NotNull
    private Boolean declar;
    @Size(max = 20)
    private String numBon;
    private LocalDateTime dateOpr;
    @Size(max = 160)
    private String libOpr;
    @Size(max = 10)
    private String codFrs;
    @Size(max = 10)
    private String numReg;

    private String numRegAff;

    private String numAffiche;
    @Size(max = 5)
    private String typBon;

    private BigDecimal reste;

    private BigDecimal solde;
    @Size(max = 1)
    private String etat;
    @NotNull
    @Size(min = 1, max = 1)
    private String codsup;
    @NotNull
    @Size(min = 1, max = 50)
    private String retenu;
    @NotNull
    private String numfacture;
    @NotNull
    private String numFac;
    @NotNull
    private BigDecimal mntOP;
    @NotNull
    private BigDecimal retenuOP;
    @NotNull
    private BigDecimal montantHT;
    @NotNull
    private BigDecimal montantTVA;

    private BigDecimal mntttc;
    @Size(max = 1)
    private String sens;
    @NotNull
    private BigDecimal debit;
    @NotNull
    private BigDecimal credit;

    public Long getNumOpr() {
        return numOpr;
    }

    public void setNumOpr(Long numOpr) {
        this.numOpr = numOpr;
    }

    public boolean isDeclar() {
        return declar;
    }

    public void setDeclar(boolean declar) {
        this.declar = declar;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public LocalDateTime getDateOpr() {
        return dateOpr;
    }

    public void setDateOpr(LocalDateTime dateOpr) {
        this.dateOpr = dateOpr;
    }

    public String getLibOpr() {
        return libOpr;
    }

    public void setLibOpr(String libOpr) {
        this.libOpr = libOpr;
    }

    public String getCodFrs() {
        return codFrs;
    }

    public void setCodFrs(String codFrs) {
        this.codFrs = codFrs;
    }

    public String getNumReg() {
        return numReg;
    }

    public void setNumReg(String numReg) {
        this.numReg = numReg;
    }

    public String getNumRegAff() {
        return numRegAff;
    }

    public void setNumRegAff(String numRegAff) {
        this.numRegAff = numRegAff;
    }

    public String getNumAffiche() {
        return numAffiche;
    }

    public void setNumAffiche(String numAffiche) {
        this.numAffiche = numAffiche;
    }

    public String getTypBon() {
        return typBon;
    }

    public void setTypBon(String typBon) {
        this.typBon = typBon;
    }

    public BigDecimal getReste() {
        return reste;
    }

    public void setReste(BigDecimal reste) {
        this.reste = reste;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getCodsup() {
        return codsup;
    }

    public void setCodsup(String codsup) {
        this.codsup = codsup;
    }

    public String getRetenu() {
        return retenu;
    }

    public void setRetenu(String retenu) {
        this.retenu = retenu;
    }

    public String getNumfacture() {
        return numfacture;
    }

    public void setNumfacture(String numfacture) {
        this.numfacture = numfacture;
    }

    public String getNumFac() {
        return numFac;
    }

    public void setNumFac(String numFac) {
        this.numFac = numFac;
    }

    public BigDecimal getMntOP() {
        return mntOP;
    }

    public void setMntOP(BigDecimal mntOP) {
        this.mntOP = mntOP;
    }

    public BigDecimal getRetenuOP() {
        return retenuOP;
    }

    public void setRetenuOP(BigDecimal retenuOP) {
        this.retenuOP = retenuOP;
    }

    public BigDecimal getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(BigDecimal montantHT) {
        this.montantHT = montantHT;
    }

    public BigDecimal getMontantTVA() {
        return montantTVA;
    }

    public void setMontantTVA(BigDecimal montantTVA) {
        this.montantTVA = montantTVA;
    }

    public BigDecimal getMntttc() {
        return mntttc;
    }

    public void setMntttc(BigDecimal mntttc) {
        this.mntttc = mntttc;
    }

    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public Boolean getDeclar() {
        return declar;
    }

    public void setDeclar(Boolean declar) {
        this.declar = declar;
    }

}
