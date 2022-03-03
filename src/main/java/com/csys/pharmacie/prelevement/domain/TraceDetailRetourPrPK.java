/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author DELL
 */
@Embeddable
public class TraceDetailRetourPrPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_detail_retour")
    private Integer codeDetailRetour;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_detail_mvtstopr")
    private Integer codeDetailMvtstopr;

    public TraceDetailRetourPrPK() {
    }

    public TraceDetailRetourPrPK(Integer codeDetailRetour, Integer codeDetailMvtstopr) {

        this.codeDetailRetour = codeDetailRetour;
        this.codeDetailMvtstopr = codeDetailMvtstopr;
    }

    public Integer getCodeDetailRetour() {
        return codeDetailRetour;
    }

    public void setCodeDetailRetour(Integer codeDetailRetour) {
        this.codeDetailRetour = codeDetailRetour;
    }

    public long getCodeDetailMvtstopr() {
        return codeDetailMvtstopr;
    }

    public void setCodeDetailMvtstopr(Integer codeDetailMvtstopr) {
        this.codeDetailMvtstopr = codeDetailMvtstopr;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.codeDetailRetour);
        hash = 17 * hash + Objects.hashCode(this.codeDetailMvtstopr);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TraceDetailRetourPrPK other = (TraceDetailRetourPrPK) obj;
        if (!Objects.equals(this.codeDetailRetour, other.codeDetailRetour)) {
            return false;
        }
        if (!Objects.equals(this.codeDetailMvtstopr, other.codeDetailMvtstopr)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.prelevement.domain.TraceDetailRetourPrPK[   codeDetailRetour=" + codeDetailRetour + ", codeDetailMvtstopr=" + codeDetailMvtstopr + " ]";
    }

}
