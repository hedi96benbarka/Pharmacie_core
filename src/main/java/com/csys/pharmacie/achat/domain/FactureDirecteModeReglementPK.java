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
 * @author Administrateur
 */
@Embeddable
public class FactureDirecteModeReglementPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "num_bon")
    private String numBon;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_reg")
    private Integer codeReg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_motif_paiement")
    private Integer codeMotifPaiement;

    public FactureDirecteModeReglementPK() {
    }

    public FactureDirecteModeReglementPK(String numBon, Integer codeReg, Integer codeMotifPaiement) {
        this.numBon = numBon;
        this.codeReg = codeReg;
        this.codeMotifPaiement = codeMotifPaiement;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public Integer getCodeReg() {
        return codeReg;
    }

    public void setCodeReg(Integer codeReg) {
        this.codeReg = codeReg;
    }

    public Integer getCodeMotifPaiement() {
        return codeMotifPaiement;
    }

    public void setCodeMotifPaiement(Integer codeMotifPaiement) {
        this.codeMotifPaiement = codeMotifPaiement;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numBon != null ? numBon.hashCode() : 0);
        hash += (Integer) codeReg;
        hash += (Integer) codeMotifPaiement;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactureDirecteModeReglementPK)) {
            return false;
        }
        FactureDirecteModeReglementPK other = (FactureDirecteModeReglementPK) object;
        if ((this.numBon == null && other.numBon != null) || (this.numBon != null && !this.numBon.equals(other.numBon))) {
            return false;
        }
        if (this.codeReg != other.codeReg) {
            return false;
        }
        if (this.codeMotifPaiement != other.codeMotifPaiement) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.FactureDirecteModeReglementPK[ numBon=" + numBon + ", codeReg=" + codeReg + ", codeMotifPaiement=" + codeMotifPaiement + " ]";
    }
    
}
