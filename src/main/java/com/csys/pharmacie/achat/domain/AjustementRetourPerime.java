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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "ajustement_retour_perime")
public class AjustementRetourPerime implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AjustementRetourPerimePK ajustementRetourPerimePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_depot")
    private Integer codeDepot;
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
    private RetourPerime retourPerime;

    public AjustementRetourPerime() {
    }

    public AjustementRetourPerime(AjustementRetourPerimePK ajustementRetourPerimePK) {
        this.ajustementRetourPerimePK = ajustementRetourPerimePK;
    }

    public AjustementRetourPerime(AjustementRetourPerimePK ajustementRetourPerimePK, Integer codeDepot, BigDecimal diffMntHt, BigDecimal diffMntTtc) {
        this.ajustementRetourPerimePK = ajustementRetourPerimePK;
        this.codeDepot = codeDepot;
        this.diffMntHt = diffMntHt;
        this.diffMntTtc = diffMntTtc;
    }

    public AjustementRetourPerime(String numbon, Integer codart, Integer codeUnite, String numRetour) {
        this.ajustementRetourPerimePK = new AjustementRetourPerimePK(numbon, codart, codeUnite, numRetour);
    }

    public AjustementRetourPerimePK getAjustementRetourPerimePK() {
        return ajustementRetourPerimePK;
    }

    public AjustementRetourPerime(BigDecimal diffMntHt, BigDecimal diffMntTtc) {
        this.diffMntHt = diffMntHt;
        this.diffMntTtc = diffMntTtc;
    }

    public void setAjustementRetourPerimePK(AjustementRetourPerimePK ajustementRetourPerimePK) {
        this.ajustementRetourPerimePK = ajustementRetourPerimePK;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
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

    public RetourPerime getRetourPerime() {
        return retourPerime;
    }

    public void setRetourPerime(RetourPerime retourPerime) {
        this.retourPerime = retourPerime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ajustementRetourPerimePK != null ? ajustementRetourPerimePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AjustementRetourPerime)) {
            return false;
        }
        AjustementRetourPerime other = (AjustementRetourPerime) object;
        if ((this.ajustementRetourPerimePK == null && other.ajustementRetourPerimePK != null) || (this.ajustementRetourPerimePK != null && !this.ajustementRetourPerimePK.equals(other.ajustementRetourPerimePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AjustementRetourPerime{" + "ajustementRetourPerimePK=" + ajustementRetourPerimePK + ", codeDepot=" + codeDepot + ", diffMntHt=" + diffMntHt + ", diffMntTtc=" + diffMntTtc + ", retourPerime=" + retourPerime + '}';
    }

 
    
}
