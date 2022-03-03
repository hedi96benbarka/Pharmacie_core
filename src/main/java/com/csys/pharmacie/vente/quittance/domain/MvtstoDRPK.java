/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Embeddable
public class MvtstoDRPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "codart", nullable = false)
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbon", nullable = false, length = 20)
    private String numbon;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "numordre", nullable = false, length = 6)
    private String numordre;

    public MvtstoDRPK() {
    }

    public MvtstoDRPK(Integer codart, String numbon, String numordre) {
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
        hash += (int) codart;
        hash += (numbon != null ? numbon.hashCode() : 0);
        hash += (numordre != null ? numordre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MvtstoDRPK)) {
            return false;
        }
        MvtstoDRPK other = (MvtstoDRPK) object;
        if (this.codart != other.codart) {
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
        return "com.csys.pharmacie.vente.quittance.domain.MvtstoDRPK[ codart=" + codart + ", numbon=" + numbon + ", numordre=" + numordre + " ]";
    }
    
}
