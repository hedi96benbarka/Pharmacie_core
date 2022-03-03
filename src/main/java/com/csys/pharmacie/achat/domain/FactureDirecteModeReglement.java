/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "facture_directe_mode_reglement")
public class FactureDirecteModeReglement implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FactureDirecteModeReglementPK factureDirecteModeReglementPK;
    
    @Column(name = "delai_paiement")
    private Integer delaiPaiement;
    @Column(name = "delai_valeur_paiement")
    private Integer delaiValeurPaiement;
    @Column(name = "pourcentage")
    private Integer pourcentage;
    
    @JoinColumn(name = "num_bon", referencedColumnName = "numbon", insertable = false, updatable = false)
    @MapsId("numBon")
    @ManyToOne(optional = false)
    private FactureDirecte factureDirecte;

    public FactureDirecteModeReglement() {
    }

    public FactureDirecteModeReglement(FactureDirecteModeReglementPK factureDirecteModeReglementPK) {
        this.factureDirecteModeReglementPK = factureDirecteModeReglementPK;
    }

    public FactureDirecteModeReglement(String numBon, Integer codeReg, Integer codeMotifPaiement) {
        this.factureDirecteModeReglementPK = new FactureDirecteModeReglementPK(numBon, codeReg, codeMotifPaiement);
    }

    public FactureDirecteModeReglementPK getFactureDirecteModeReglementPK() {
        return factureDirecteModeReglementPK;
    }

    public void setFactureDirecteModeReglementPK(FactureDirecteModeReglementPK factureDirecteModeReglementPK) {
        this.factureDirecteModeReglementPK = factureDirecteModeReglementPK;
    }

    public Integer getDelaiPaiement() {
        return delaiPaiement;
    }

    public void setDelaiPaiement(Integer delaiPaiement) {
        this.delaiPaiement = delaiPaiement;
    }

    public Integer getDelaiValeurPaiement() {
        return delaiValeurPaiement;
    }

    public void setDelaiValeurPaiement(Integer delaiValeurPaiement) {
        this.delaiValeurPaiement = delaiValeurPaiement;
    }

    public Integer getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Integer pourcentage) {
        this.pourcentage = pourcentage;
    }

    public FactureDirecte getFactureDirecte() {
        return factureDirecte;
    }

    public void setFactureDirecte(FactureDirecte factureDirecte) {
        this.factureDirecte = factureDirecte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (factureDirecteModeReglementPK != null ? factureDirecteModeReglementPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactureDirecteModeReglement)) {
            return false;
        }
        FactureDirecteModeReglement other = (FactureDirecteModeReglement) object;
        if ((this.factureDirecteModeReglementPK == null && other.factureDirecteModeReglementPK != null) || (this.factureDirecteModeReglementPK != null && !this.factureDirecteModeReglementPK.equals(other.factureDirecteModeReglementPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.FactureDirecteModeReglement[ factureDirecteModeReglementPK=" + factureDirecteModeReglementPK + " ]";
    }
    
}
