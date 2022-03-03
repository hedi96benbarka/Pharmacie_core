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
public class FactureDirecteCostCenterPK implements Serializable {

    @Size(min = 1, max = 20)
    @Column(name = "numero_facture_directe")
    private String numeroFactureDirecte;
    @NotNull
    @Column(name = "code_cost_center")
    private Integer codeCostCenter;

    public FactureDirecteCostCenterPK() {
    }

    public FactureDirecteCostCenterPK(String numeroFactureDirect, Integer codeCostCenter) {
        this.numeroFactureDirecte = numeroFactureDirect;
        this.codeCostCenter = codeCostCenter;
    }

    public String getNumeroFactureDirecte() {
        return numeroFactureDirecte;
    }

    public void setNumeroFactureDirecte(String numeroFactureDirecte) {
        this.numeroFactureDirecte = numeroFactureDirecte;
    }

    public Integer getCodeCostCenter() {
        return codeCostCenter;
    }

    public void setCodeCostCenter(Integer codeCostCenter) {
        this.codeCostCenter = codeCostCenter;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numeroFactureDirecte != null ? numeroFactureDirecte.hashCode() : 0);
        hash += (int) codeCostCenter;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactureDirecteCostCenterPK)) {
            return false;
        }
        FactureDirecteCostCenterPK other = (FactureDirecteCostCenterPK) object;
        if ((this.numeroFactureDirecte == null && other.numeroFactureDirecte != null) || (this.numeroFactureDirecte != null && !this.numeroFactureDirecte.equals(other.numeroFactureDirecte))) {
            return false;
        }
        if (this.codeCostCenter != other.codeCostCenter) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.FactureDirectCostCenterPK[ numeroFactureDirect=" + numeroFactureDirecte + ", codeCostCenter=" + codeCostCenter + " ]";
    }

}
