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
//import javax.persistence.Entity;
//import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "ajustement_avoir_fournisseur")
public class AjustementAvoirFournisseur implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AjustementAvoirFournisseurPK ajustementAvoirFournisseurPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_depot")
    private long codeDepot;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    private AvoirFournisseur avoirFournisseur;
    
    public AjustementAvoirFournisseur() {
    }

    public AjustementAvoirFournisseur(AjustementAvoirFournisseurPK ajustementAvoirFournisseurPK) {
        this.ajustementAvoirFournisseurPK = ajustementAvoirFournisseurPK;
    }

    public AjustementAvoirFournisseur(AjustementAvoirFournisseurPK ajustementAvoirFournisseurPK, long codeDepot, BigDecimal diffMntHt, BigDecimal diffMntTtc) {
        this.ajustementAvoirFournisseurPK = ajustementAvoirFournisseurPK;
        this.codeDepot = codeDepot;
        this.diffMntHt = diffMntHt;
        this.diffMntTtc = diffMntTtc;
    }

    public AjustementAvoirFournisseur(String numbon, long codart, long codeUnite, String numRetour) {
        this.ajustementAvoirFournisseurPK = new AjustementAvoirFournisseurPK(numbon, codart, codeUnite, numRetour);
    }

    public AjustementAvoirFournisseurPK getAjustementAvoirFournisseurPK() {
        return ajustementAvoirFournisseurPK;
    }

    public void setAjustementAvoirFournisseurPK(AjustementAvoirFournisseurPK ajustementAvoirFournisseurPK) {
        this.ajustementAvoirFournisseurPK = ajustementAvoirFournisseurPK;
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

    public AvoirFournisseur getAvoirFournisseur() {
        return avoirFournisseur;
    }

    public void setAvoirFournisseur(AvoirFournisseur avoirFournisseur) {
        this.avoirFournisseur = avoirFournisseur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ajustementAvoirFournisseurPK != null ? ajustementAvoirFournisseurPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AjustementAvoirFournisseur)) {
            return false;
        }
        AjustementAvoirFournisseur other = (AjustementAvoirFournisseur) object;
        if ((this.ajustementAvoirFournisseurPK == null && other.ajustementAvoirFournisseurPK != null) || (this.ajustementAvoirFournisseurPK != null && !this.ajustementAvoirFournisseurPK.equals(other.ajustementAvoirFournisseurPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.AjustementAvoirFournisseur[ ajustementAvoirFournisseurPK=" + ajustementAvoirFournisseurPK + " ]";
    }
    
}
