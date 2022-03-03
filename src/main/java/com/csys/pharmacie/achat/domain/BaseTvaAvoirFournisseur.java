/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.BaseTva;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "base_tva_avoir_fournisseur")
public class BaseTvaAvoirFournisseur extends BaseTva implements Serializable {





    @Column(name = "mnt_tva_grtauite")
    private BigDecimal mntTvaGrtauite;
    @Column(name = "base_tva_gratuite")
    private BigDecimal baseTvaGratuite;
    @JoinColumn(name = "avoir_fournisseur", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    private AvoirFournisseur avoirFournisseur;

    public BaseTvaAvoirFournisseur() {
    }

    public BigDecimal getMntTvaGrtauite() {
        return mntTvaGrtauite;
    }

    public void setMntTvaGrtauite(BigDecimal mntTvaGrtauite) {
        this.mntTvaGrtauite = mntTvaGrtauite;
    }

    public BigDecimal getBaseTvaGratuite() {
        return baseTvaGratuite;
    }

    public void setBaseTvaGratuite(BigDecimal baseTvaGratuite) {
        this.baseTvaGratuite = baseTvaGratuite;
    }

    public AvoirFournisseur getAvoirFournisseur() {
        return avoirFournisseur;
    }

    public void setAvoirFournisseur(AvoirFournisseur avoirFournisseur) {
        this.avoirFournisseur = avoirFournisseur;
    }

  

  
    
}
