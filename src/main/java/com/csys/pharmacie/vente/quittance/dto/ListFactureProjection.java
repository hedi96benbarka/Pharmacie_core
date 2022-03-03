package com.csys.pharmacie.vente.quittance.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class ListFactureProjection {

    private String numbon;
    private String numaffiche;
    private LocalDateTime datbon;
    private String numCha;
    private String raisoc;
    private BigDecimal mntbon;
    private Boolean imprimer;
    private String numdoss;
    private String codvend;

    public ListFactureProjection(String numbon, String numaffiche, LocalDateTime datbon, String numCha, String raisoc,
            BigDecimal mntbon, Boolean imprimer, String numdoss, String memop, String codvend) {
        super();
        this.numbon = numbon;
        this.numaffiche = numaffiche;
        this.datbon = datbon;
        this.numCha = numCha;
        this.raisoc = raisoc;
        this.mntbon = mntbon;
        this.imprimer = imprimer;
        this.numdoss = numdoss;
        this.memop = memop;
        this.codvend = codvend;
    }

    private String memop;

    public ListFactureProjection() {
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public LocalDateTime getDatbon() {
        return datbon;
    }

    public void setDatbon(LocalDateTime datbon) {
        this.datbon = datbon;
    }

    public String getNumCha() {
        return numCha;
    }

    public void setNumCha(String numCha) {
        this.numCha = numCha;
    }

    public String getRaisoc() {
        return raisoc;
    }

    public void setRaisoc(String raisoc) {
        this.raisoc = raisoc;
    }

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public Boolean getImprimer() {
        return imprimer;
    }

    public void setImprimer(Boolean imprimer) {
        this.imprimer = imprimer;
    }

    public String getNumdoss() {
        return numdoss;
    }

    public void setNumdoss(String numdoss) {
        this.numdoss = numdoss;
    }

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

}
