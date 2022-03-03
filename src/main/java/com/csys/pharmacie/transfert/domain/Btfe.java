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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.FetchType;
import javax.persistence.MapsId;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Farouk
 */
@Entity
@Audited
@Table(name = "BTFE")
@AuditTable("BTFE_AUD")
public class Btfe implements Serializable {

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
    @Column(name = "NumFE",unique=true)
    private String numFE;

    public Btfe() {
    }

    public Btfe(Integer numero) {
        this.numero = numero;
    }

    public FactureBT getFactureBT() {
        return factureBT;
    }

    public void setFactureBT(FactureBT factureBT) {
        this.factureBT = factureBT;
    }

    public Btfe(Integer numero, FactureBT factureBT, String numFE) {
        this.numero = numero;
        this.factureBT = factureBT;
        this.numFE = numFE;
    }

    public Btfe(FactureBT factureBT, String numFE) {
        this.factureBT = factureBT;
        this.numFE = numFE;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNumFE() {
        return numFE;
    }

    public void setNumFE(String numFE) {
        this.numFE = numFE;
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
        Btfe other = (Btfe) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Btfe{" + "numero=" + numero + ", factureBT=" + factureBT + ", numFE=" + numFE + '}';
    }

}
