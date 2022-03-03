/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import com.csys.pharmacie.helper.MvtSto;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author siryne
 */
@Entity
@Table(name = "detail_retour_prelevement")

public class DetailRetourPrelevement extends MvtSto implements Serializable {

    @JoinColumn(name = "numbon", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    private RetourPrelevement retourPrelevement;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detailRetourPrelevement")
    private List<TraceDetailRetourPr> traceDetailRetourPr;

    public RetourPrelevement getRetourPrelevement() {
        return retourPrelevement;
    }

    public void setRetourPrelevement(RetourPrelevement retourPrelevement) {
        this.retourPrelevement = retourPrelevement;
    }

    public List<TraceDetailRetourPr> getTraceDetailRetourPr() {
        return traceDetailRetourPr;
    }

    public void setTraceDetailRetourPr(List<TraceDetailRetourPr> traceDetailRetourPr) {
        this.traceDetailRetourPr = traceDetailRetourPr;
    }

    @Override
    public String toString() {
        return "DetailRetourPrelevement{ retourPrelevement=" + retourPrelevement + "trace = " + this.traceDetailRetourPr  +  " desart = " + this.getDesart() + " code_saisi = " + this.getCodeSaisi()+ " quantite = " + this.getQuantite() + '}';
    }

}
