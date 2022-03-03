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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "base_tva_facture_retour_perime")
public class BaseTvaFactureRetourPerime implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Long code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_tva")
    private Integer codeTva;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux_tva")
    private BigDecimal tauxTva;
    @Column(name = "base_tva")
    private BigDecimal baseTva;
    @Column(name = "montant_tva")
    private BigDecimal montantTva;
    @ManyToOne(optional = false)
    @JoinColumn(name = "facture_retour_perime", referencedColumnName = "numbon")
    private FactureRetourPerime factureRetourPerime;

    public BaseTvaFactureRetourPerime() {
    }

    public BaseTvaFactureRetourPerime(Long code) {
        this.code = code;
    }

    public BaseTvaFactureRetourPerime(Long code, Integer codeTva) {
        this.code = code;
        this.codeTva = codeTva;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public BigDecimal getBaseTva() {
        return baseTva;
    }

    public void setBaseTva(BigDecimal baseTva) {
        this.baseTva = baseTva;
    }

    public BigDecimal getMontantTva() {
        return montantTva;
    }

    public void setMontantTva(BigDecimal montantTva) {
        this.montantTva = montantTva;
    }

    public FactureRetourPerime getFactureRetourPerime() {
        return factureRetourPerime;
    }

    public void setFactureRetourPerime(FactureRetourPerime factureRetourPerime) {
        this.factureRetourPerime = factureRetourPerime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BaseTvaFactureRetourPerime)) {
            return false;
        }
        BaseTvaFactureRetourPerime other = (BaseTvaFactureRetourPerime) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.BaseTvaFactureRetourPerime[ code=" + code + " ]";
    }
    
}
