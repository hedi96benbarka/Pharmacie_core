/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Embeddable
public class ValeurStockPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "datesys")
    private LocalDate datesys;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_depsto")
    private long codeDepsto;

    public ValeurStockPK() {
    }

    public ValeurStockPK(LocalDate datesys, long codeDepsto) {
        this.datesys = datesys;
        this.codeDepsto = codeDepsto;
    }

    public LocalDate getDatesys() {
        return datesys;
    }

    public void setDatesys(LocalDate datesys) {
        this.datesys = datesys;
    }

    public long getCodeDepsto() {
        return codeDepsto;
    }

    public void setCodeDepsto(long codeDepsto) {
        this.codeDepsto = codeDepsto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datesys != null ? datesys.hashCode() : 0);
        hash += (int) codeDepsto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValeurStockPK)) {
            return false;
        }
        ValeurStockPK other = (ValeurStockPK) object;
        if ((this.datesys == null && other.datesys != null) || (this.datesys != null && !this.datesys.equals(other.datesys))) {
            return false;
        }
        if (this.codeDepsto != other.codeDepsto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.stock.domain.ValeurStockPK[ datesys=" + datesys + ", codeDepsto=" + codeDepsto + " ]";
    }

}
