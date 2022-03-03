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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author DELL
 */
@Entity
@Audited
@Table(name = "Btbt")
@AuditTable("Btbt_AUD")
@NamedEntityGraph(name = "Btbt.numBT",
                  attributeNodes = {
                      @NamedAttributeNode("numBT") 
                  })
public class Btbt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Numero")
    private Integer numero;

    @JoinColumn(name = "NumBT", referencedColumnName = "numbon", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FactureBT numBT;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NumBTReturn")
    private String numBTReturn;

    public Btbt() {
    }

    public Btbt(Integer numero) {
        this.numero = numero;
    }

    public Btbt(FactureBT factureBT, String numBTReturn) {
        this.numBT = factureBT;
        this.numBTReturn = numBTReturn;
    }

    public Btbt(Integer numero, FactureBT factureBT, String numBTReturn) {
        this.numero = numero;
        this.numBT = factureBT;
        this.numBTReturn = numBTReturn;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public FactureBT getNumBT() {
        return numBT;
    }

    public void setNumBT(FactureBT numBT) {
        this.numBT = numBT;
    }

    public String getNumBTReturn() {
        return numBTReturn;
    }

    public void setNumBTReturn(String numBTReturn) {
        this.numBTReturn = numBTReturn;
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
        if (!(object instanceof Btbt)) {
            return false;
        }
        Btbt other = (Btbt) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Btbt{" + "numero=" + numero + ", numBTReturn=" + numBTReturn + '}';
    }

  
}
