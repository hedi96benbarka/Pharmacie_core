/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "base_tva_facture_directe")
@NamedQueries({
    @NamedQuery(name = "BaseTvaFactureDirecte.findAll", query = "SELECT b FROM BaseTvaFactureDirecte b")})
public class BaseTvaFactureDirecte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
   
    @Column(name = "code_tva")
    private Integer codeTva;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux_tva")
    private BigDecimal tauxTva;
    @Column(name = "base_tva")
    private BigDecimal baseTva;
    @Column(name = "montant_tva")
    private BigDecimal montantTva;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
 
    @Column(name = "code")
    
    private Long code;

    
    @JoinColumn(name = "facture_directe", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    private FactureDirecte factureDirecte;

    public BaseTvaFactureDirecte() {
    }

    public BaseTvaFactureDirecte(Long code) {
        this.code = code;
    }

    public BaseTvaFactureDirecte(Long code, int codeTva) {
        this.code = code;
        this.codeTva = codeTva;
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

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
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
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BaseTvaFactureDirecte)) {
            return false;
        }
        BaseTvaFactureDirecte other = (BaseTvaFactureDirecte) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.BaseTvaFactureDirecte[ code=" + code + " ]";
    }
    
}
