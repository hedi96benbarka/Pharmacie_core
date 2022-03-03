/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "ajustement_retour_fournisseur")
public class AjustementRetourFournisseur implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AjustementRetourFournisseurPK ajustementRetourFournisseurPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_depot")
    private long codeDepot;
    @Basic(optional = false)
    @NotNull
    @Column(name = "diff_mnt_ht")
    private BigDecimal diffMntHt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "diff_mnt_ttc")
    private BigDecimal diffMntTtc;
    
    @JoinColumn(name = "num_retour", referencedColumnName = "numbon", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FactureBA factureBA;
    
    public AjustementRetourFournisseur() {
    }

    public AjustementRetourFournisseur(AjustementRetourFournisseurPK ajustementRetourFournisseurPK) {
        this.ajustementRetourFournisseurPK = ajustementRetourFournisseurPK;
    }

    public AjustementRetourFournisseur(AjustementRetourFournisseurPK ajustementRetourFournisseurPK, long codeDepot, BigDecimal diffMntHt, BigDecimal diffMntTtc) {
        this.ajustementRetourFournisseurPK = ajustementRetourFournisseurPK;
        this.codeDepot = codeDepot;
        this.diffMntHt = diffMntHt;
        this.diffMntTtc = diffMntTtc;
    }

    public FactureBA getFactureBA() {
        return factureBA;
    }

    public void setFactureBA(FactureBA factureBA) {
        this.factureBA = factureBA;
    }

    public AjustementRetourFournisseurPK getAjustementRetourFournisseurPK() {
        return ajustementRetourFournisseurPK;
    }

    public void setAjustementRetourFournisseurPK(AjustementRetourFournisseurPK ajustementRetourFournisseurPK) {
        this.ajustementRetourFournisseurPK = ajustementRetourFournisseurPK;
    }

    public long getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(long codeDepot) {
        this.codeDepot = codeDepot;
    }

    public BigDecimal getDiffMntHt() {
        return diffMntHt;
    }

    public void setDiffMntHt(BigDecimal diffMntHt) {
        this.diffMntHt = diffMntHt;
    }

    public BigDecimal getDiffMntTtc() {
        return diffMntTtc;
    }

    public void setDiffMntTtc(BigDecimal diffMntTtc) {
        this.diffMntTtc = diffMntTtc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ajustementRetourFournisseurPK != null ? ajustementRetourFournisseurPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AjustementRetourFournisseur)) {
            return false;
        }
        AjustementRetourFournisseur other = (AjustementRetourFournisseur) object;
        if ((this.ajustementRetourFournisseurPK == null && other.ajustementRetourFournisseurPK != null) || (this.ajustementRetourFournisseurPK != null && !this.ajustementRetourFournisseurPK.equals(other.ajustementRetourFournisseurPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AjustementRetourFournisseur{" + "ajustementRetourFournisseurPK=" + ajustementRetourFournisseurPK + ", codeDepot=" + codeDepot + ", diffMntHt=" + diffMntHt + ", diffMntTtc=" + diffMntTtc + '}';
    }


    
}
