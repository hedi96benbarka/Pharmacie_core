/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import java.io.Serializable;
import javax.annotation.Generated;
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
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Audited
@Table(name = "demande_recup")
@AuditTable("demande_recup_AUD")
public class DemandeRecup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Numero", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_demandeTr", nullable = false)
    private Integer codedemandeTr;
    @JoinColumn(name = "NumBT", referencedColumnName = "numbon")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FactureBT factureBT;

    public DemandeRecup() {
    }

    public DemandeRecup(Integer numero) {
        this.numero = numero;
    }

    public DemandeRecup(Integer codedemandeTr, FactureBT factureBT) {
        this.codedemandeTr = codedemandeTr;
        this.factureBT = factureBT;
    }

    public DemandeRecup(Integer numero, Integer codedemandeTr) {
        this.numero = numero;
        this.codedemandeTr = codedemandeTr;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCodedemandeTr() {
        return codedemandeTr;
    }

    public void setCodedemandeTr(Integer codedemandeTr) {
        this.codedemandeTr = codedemandeTr;
    }

    public FactureBT getFactureBT() {
        return factureBT;
    }

    public void setFactureBT(FactureBT factureBT) {
        this.factureBT = factureBT;
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
        if (!(object instanceof DemandeRecup)) {
            return false;
        }
        DemandeRecup other = (DemandeRecup) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.vente.avoir.dto.DemandeRecup[ numero=" + numero + " ]";
    }

}
