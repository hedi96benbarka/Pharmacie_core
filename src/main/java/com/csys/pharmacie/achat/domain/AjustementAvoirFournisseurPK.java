/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
@Embeddable
public class AjustementAvoirFournisseurPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbon")
    private String numbon;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codart")
    private long codart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_unite")
    private long codeUnite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "num_retour")
    private String numRetour;

    public AjustementAvoirFournisseurPK() {
    }

    public AjustementAvoirFournisseurPK(String numbon, long codart, long codeUnite, String numRetour) {
        this.numbon = numbon;
        this.codart = codart;
        this.codeUnite = codeUnite;
        this.numRetour = numRetour;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public long getCodart() {
        return codart;
    }

    public void setCodart(long codart) {
        this.codart = codart;
    }

    public long getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(long codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getNumRetour() {
        return numRetour;
    }

    public void setNumRetour(String numRetour) {
        this.numRetour = numRetour;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numbon != null ? numbon.hashCode() : 0);
        hash += (int) codart;
        hash += (int) codeUnite;
        hash += (numRetour != null ? numRetour.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AjustementAvoirFournisseurPK)) {
            return false;
        }
        AjustementAvoirFournisseurPK other = (AjustementAvoirFournisseurPK) object;
        if ((this.numbon == null && other.numbon != null) || (this.numbon != null && !this.numbon.equals(other.numbon))) {
            return false;
        }
        if (this.codart != other.codart) {
            return false;
        }
        if (this.codeUnite != other.codeUnite) {
            return false;
        }
        if ((this.numRetour == null && other.numRetour != null) || (this.numRetour != null && !this.numRetour.equals(other.numRetour))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.AjustementAvoirFournisseurPK[ numbon=" + numbon + ", codart=" + codart + ", codeUnite=" + codeUnite + ", numRetour=" + numRetour + " ]";
    }
    
}
