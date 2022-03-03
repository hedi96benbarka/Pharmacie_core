/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.BaseTva;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "base_tva_reception")
@NamedQueries({
    @NamedQuery(name = "BaseTvaReception.findAll", query = "SELECT b FROM BaseTvaReception b")})
public class BaseTvaReception extends BaseTva implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "reception", referencedColumnName = "numbon")
    private FactureBA factureBA;

    @Column(name = "mnt_tva_grtauite")
    private BigDecimal montantTvaGratuite;
    
  @Column(name = "base_tva_gratuite")
    private BigDecimal baseTvaGratuite;
  
    public FactureBA getFactureBA() {
        return factureBA;
    }

    public void setFactureBA(FactureBA factureBA) {
        this.factureBA = factureBA;
    }

    public BigDecimal getMontantTvaGratuite() {
        return montantTvaGratuite;
    }

    public void setMontantTvaGratuite(BigDecimal montantTvaGratuite) {
        this.montantTvaGratuite = montantTvaGratuite;
    }

    public BigDecimal getBaseTvaGratuite() {
        return baseTvaGratuite;
    }

    public void setBaseTvaGratuite(BigDecimal baseTvaGratuite) {
        this.baseTvaGratuite = baseTvaGratuite;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.getCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BaseTva other = (BaseTva) obj;
        if (!Objects.equals(this.getCode(), other.getCode())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BaseTva{" + "code=" + getCode() + ", numbon=" + factureBA.getNumbon() + ", codeTva=" + getCodeTva() + '}';
    }

}
