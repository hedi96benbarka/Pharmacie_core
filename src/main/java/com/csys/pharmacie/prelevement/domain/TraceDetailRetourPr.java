/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "trace_detail_retour_pr")

public class TraceDetailRetourPr implements Serializable {

    private static final Long serialVersionUID = 1L;
    @EmbeddedId
    protected TraceDetailRetourPrPK traceDetailRetourPrPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_mvtstopr")
    private Integer codeMvtstopr;

    @JoinColumn(name = "code_detail_retour", referencedColumnName = "code")
    @MapsId("codeDetailRetour")
    @ManyToOne
    private DetailRetourPrelevement detailRetourPrelevement;

    @JoinColumn(name = "code_detail_mvtstopr", referencedColumnName = "code")
    @MapsId("codeDetailMvtstopr")
    @ManyToOne
    private DetailMvtStoPR detailMvtStoPR;

    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite")
    private BigDecimal quantite;

    public TraceDetailRetourPr() {
    }

    public TraceDetailRetourPr(TraceDetailRetourPrPK traceDetailRetourPrPK) {
        this.traceDetailRetourPrPK = traceDetailRetourPrPK;
    }

    public TraceDetailRetourPr(TraceDetailRetourPrPK traceDetailRetourPrPK, Integer codeMvtstopr, BigDecimal quantite) {
        this.traceDetailRetourPrPK = traceDetailRetourPrPK;
        this.codeMvtstopr = codeMvtstopr;
        this.quantite = quantite;
    }

    public TraceDetailRetourPr(Integer codeMvtstopr, DetailRetourPrelevement detailRetourPrelevement, DetailMvtStoPR detailMvtStoPR, BigDecimal quantite) {
        this.traceDetailRetourPrPK = new TraceDetailRetourPrPK();
        this.codeMvtstopr = codeMvtstopr;
        this.detailRetourPrelevement = detailRetourPrelevement;
        this.detailMvtStoPR = detailMvtStoPR;
        this.quantite = quantite;
    }

    public TraceDetailRetourPrPK getTraceDetailRetourPrPK() {
        return traceDetailRetourPrPK;
    }

    public void setTraceDetailRetourPrPK(TraceDetailRetourPrPK traceDetailRetourPrPK) {
        this.traceDetailRetourPrPK = traceDetailRetourPrPK;
    }

    public Integer getCodeMvtstopr() {
        return codeMvtstopr;
    }

    public void setCodeMvtstopr(Integer codeMvtstopr) {
        this.codeMvtstopr = codeMvtstopr;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public DetailRetourPrelevement getDetailRetourPrelevement() {
        return detailRetourPrelevement;
    }

    public void setDetailRetourPrelevement(DetailRetourPrelevement detailRetourPrelevement) {
        this.detailRetourPrelevement = detailRetourPrelevement;
    }

    public DetailMvtStoPR getDetailMvtStoPR() {
        return detailMvtStoPR;
    }

    public void setDetailMvtStoPR(DetailMvtStoPR detailMvtStoPR) {
        this.detailMvtStoPR = detailMvtStoPR;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (traceDetailRetourPrPK != null ? traceDetailRetourPrPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TraceDetailRetourPr)) {
            return false;
        }
        TraceDetailRetourPr other = (TraceDetailRetourPr) object;
        if ((this.traceDetailRetourPrPK == null && other.traceDetailRetourPrPK != null) || (this.traceDetailRetourPrPK != null && !this.traceDetailRetourPrPK.equals(other.traceDetailRetourPrPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TraceDetailRetourPr{" + "traceDetailRetourPrPK=" + traceDetailRetourPrPK + ", codeMvtstopr=" + codeMvtstopr + ",  quantite=" + quantite + '}';
    }

    

}
