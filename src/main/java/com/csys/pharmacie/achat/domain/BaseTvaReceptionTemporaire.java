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
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity
@Audited
@Table(name = "base_tva_reception_temporaire")
@AuditTable("base_tva_reception_temporaire_AUD")
public class BaseTvaReceptionTemporaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_tva")
    private int codeTva;
    @Column(name = "taux_tva")
    private BigDecimal tauxTva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "base_tva")
    private BigDecimal baseTva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "montant_tva")
    private BigDecimal montantTva;

    @Column(name = "mnt_tva_grtauite")
    private BigDecimal mntTvaGrtauite;
    @Column(name = "base_tva_gratuite")
    private BigDecimal baseTvaGratuite;
    @JoinColumn(name = "reception", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    private ReceptionTemporaire reception;

    public BaseTvaReceptionTemporaire() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public int getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(int codeTva) {
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

    public ReceptionTemporaire getReception() {
        return reception;
    }

    public void setReception(ReceptionTemporaire reception) {
        this.reception = reception;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

   

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.BaseTvaReceptionTemporaire[ code=" + code + " ]";
    }



}
