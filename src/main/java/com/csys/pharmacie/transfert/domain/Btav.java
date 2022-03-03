/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Audited
@Table(name = "BTAV")
@AuditTable("BTAV_AUD")
public class Btav implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Numero")
    private Integer numero;

    @JoinColumn(name = "NumBT", referencedColumnName = "numbon")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FactureBT factureBT;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NumAV",unique=true)
    private String numAvoir;

      public Btav() {
    }

    public Btav(Integer numero) {
        this.numero = numero;
    }
        public Btav(FactureBT factureBT, String numAvoir) {
        this.factureBT = factureBT;
        this.numAvoir = numAvoir;
    }
    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public FactureBT getFactureBT() {
        return factureBT;
    }

    public void setFactureBT(FactureBT factureBT) {
        this.factureBT = factureBT;
    }

    public String getNumAvoir() {
        return numAvoir;
    }

    public void setNumAvoir(String numAvoir) {
        this.numAvoir = numAvoir;
    }
    
        @Override
    public int hashCode() {
        int hash = 0;
        hash += (numero != null ? numero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Btfe)) {
            return false;
        }
        Btav other = (Btav) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Btfe{" + "numero=" + numero + ", factureBT=" + factureBT + ", numFE=" + numAvoir + '}';
    }
    
}