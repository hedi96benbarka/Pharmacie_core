/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author farouk
 */
@Embeddable
public class TraceBAPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NumBon")
    private String numBon;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "CodArt")
    private Integer codArt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "NumBc")
    private String numBc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateSys")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSys;

    public TraceBAPK() {
    }

    public TraceBAPK(String numBon, Integer codArt, String numBc, Date dateSys) {
        this.numBon = numBon;
        this.codArt = codArt;
        this.numBc = numBc;
        this.dateSys = dateSys;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public Integer getCodArt() {
        return codArt;
    }

    public void setCodArt(Integer codArt) {
        this.codArt = codArt;
    }

    public String getNumBc() {
        return numBc;
    }

    public void setNumBc(String numBc) {
        this.numBc = numBc;
    }

    public Date getDateSys() {
        return dateSys;
    }

    public void setDateSys(Date dateSys) {
        this.dateSys = dateSys;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numBon != null ? numBon.hashCode() : 0);
        hash += (codArt != null ? codArt.hashCode() : 0);
        hash += (numBc != null ? numBc.hashCode() : 0);
        hash += (dateSys != null ? dateSys.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TraceBAPK)) {
            return false;
        }
        TraceBAPK other = (TraceBAPK) object;
        if ((this.numBon == null && other.numBon != null) || (this.numBon != null && !this.numBon.equals(other.numBon))) {
            return false;
        }
        if ((this.codArt == null && other.codArt != null) || (this.codArt != null && !this.codArt.equals(other.codArt))) {
            return false;
        }
        if ((this.numBc == null && other.numBc != null) || (this.numBc != null && !this.numBc.equals(other.numBc))) {
            return false;
        }
        if ((this.dateSys == null && other.dateSys != null) || (this.dateSys != null && !this.dateSys.equals(other.dateSys))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.achat.model.pharmacie.TraceBAPK[ numBon=" + numBon + ", codArt=" + codArt + ", numBc=" + numBc + ", dateSys=" + dateSys + " ]";
    }

}
