/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ERRAYHAN
 */
@Embeddable
public class MvtstoPK implements Serializable {

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

    public MvtstoPK() {
    }

    public MvtstoPK(Integer codart, String numbon, String numordre) {
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
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.codart);
        hash = 97 * hash + Objects.hashCode(this.numbon);
        hash = 97 * hash + Objects.hashCode(this.numordre);
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
        final MvtstoPK other = (MvtstoPK) obj;
        if (!Objects.equals(this.numbon, other.numbon)) {
            return false;
        }
        if (!Objects.equals(this.numordre, other.numordre)) {
            return false;
        }
        if (!Objects.equals(this.codart, other.codart)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MvtstoPK{" + "codart=" + codart + ", numbon=" + numbon + ", numordre=" + numordre + '}';
    }
}
