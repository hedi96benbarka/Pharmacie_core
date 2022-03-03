/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Hamdi
 */
@Entity
@Table(name = "Prelevement_DetailDPR")
@NamedQueries({
    @NamedQuery(name = "PrelevementDetailDPR.findAll", query = "SELECT p FROM PrelevementDetailDPR p")})
public class PrelevementDetailDPR implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PrelevementDetailDPRPK pk;

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_DPR")
    private Integer codeDPR;

    @Column(name = "quantite_prelevee")
    private BigDecimal quantite_prelevee;
    @MapsId("reception")

    @JoinColumn(name = " code_Prelevement", referencedColumnName = "numbon")
    @ManyToOne(fetch = FetchType.LAZY)
    private FacturePR reception;

    public PrelevementDetailDPR() {
    }

    public PrelevementDetailDPR(PrelevementDetailDPRPK prelevementDetailDPRPK) {
        this.pk = prelevementDetailDPRPK;
    }

    public Integer getCodeDPR() {
        return codeDPR;
    }

    public void setCodeDPR(Integer codeDPR) {
        this.codeDPR = codeDPR;
    }

    public PrelevementDetailDPRPK getPk() {
        return pk;
    }

    public PrelevementDetailDPR(PrelevementDetailDPRPK prelevementDetailDPRPK, BigDecimal quantite_prelevee) {
        this.pk = prelevementDetailDPRPK;
        this.quantite_prelevee = quantite_prelevee;
    }

    public void setPk(PrelevementDetailDPRPK pk) {
        this.pk = pk;
    }

    public BigDecimal getQuantite_prelevee() {
        return quantite_prelevee;
    }

    public void setQuantite_prelevee(BigDecimal quantite_prelevee) {
        this.quantite_prelevee = quantite_prelevee;
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
        if (!(object instanceof PrelevementDetailDPR)) {
            return false;
        }
        PrelevementDetailDPR other = (PrelevementDetailDPR) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    public PrelevementDetailDPR(PrelevementDetailDPRPK pk, Integer codeDPR, BigDecimal quantite_prelevee, FacturePR reception) {
        this.pk = pk;
        this.codeDPR = codeDPR;
        this.quantite_prelevee = quantite_prelevee;
        this.reception = reception;
    }

    public FacturePR getReception() {
        return reception;
    }

    public void setReception(FacturePR reception) {
        this.reception = reception;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.prelevement.domain.PrelevementDetailDPR[ prelevementDetailDPRPK=" + pk + " ]";
    }

}
