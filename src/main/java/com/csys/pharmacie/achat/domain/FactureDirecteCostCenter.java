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
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "facture_directe_cost_center")
 
public class FactureDirecteCostCenter implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FactureDirecteCostCenterPK pk;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "montant_ttc")
    private BigDecimal montantTTC;

    @NotNull
    @MapsId(value = "numeroFactureDirecte")
    @JoinColumn(name = "numero_facture_directe", referencedColumnName = "numbon")
    @ManyToOne
    private FactureDirecte factureDirecte;

    public FactureDirecteCostCenter() {
    }

    public FactureDirecteCostCenter(FactureDirecteCostCenterPK factureDirectCostCenterPK) {
        this.pk = factureDirectCostCenterPK;
    }

    public FactureDirecteCostCenter(FactureDirecteCostCenterPK factureDirectCostCenterPK, BigDecimal montantTva) {
        this.pk = factureDirectCostCenterPK;
        this.montantTTC = montantTva;
    }

    public FactureDirecteCostCenter(String numeroFactureDirect, Integer codeCostCenter) {
        this.pk = new FactureDirecteCostCenterPK(numeroFactureDirect, codeCostCenter);
    }

    public FactureDirecteCostCenterPK getPk() {
        return pk;
    }

    public void setPk(FactureDirecteCostCenterPK pk) {
        this.pk = pk;
    }

    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
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
        hash += (pk != null ? pk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactureDirecteCostCenter)) {
            return false;
        }
        FactureDirecteCostCenter other = (FactureDirecteCostCenter) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.FactureDirectCostCenter[ factureDirectCostCenterPK=" + pk + " ]";
    }

}
