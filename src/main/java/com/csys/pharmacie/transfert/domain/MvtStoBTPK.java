/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Farouk
 */
@Embeddable
public class MvtStoBTPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "codart")
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "numbon")
    private String numbon;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "numordre")
    private String numordre;

    public MvtStoBTPK() {
    }

    public MvtStoBTPK(Integer codart, String numbon, String numordre) {
        this.codart = codart;
        this.numbon = numbon;
        this.numordre = numordre;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codart != null ? codart.hashCode() : 0);
        hash += (numbon != null ? numbon.hashCode() : 0);
        hash += (numordre != null ? numordre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MvtStoBTPK)) {
            return false;
        }
        MvtStoBTPK other = (MvtStoBTPK) object;
        if ((this.codart == null && other.codart != null) || (this.codart != null && !this.codart.equals(other.codart))) {
            return false;
        }
        if ((this.numbon == null && other.numbon != null) || (this.numbon != null && !this.numbon.equals(other.numbon))) {
            return false;
        }
        if ((this.numordre == null && other.numordre != null) || (this.numordre != null && !this.numordre.equals(other.numordre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.transfert.entity.MvtStoBTPK[ codart=" + codart + ", numbon=" + numbon + ", numordre=" + numordre + " ]";
    }

}
