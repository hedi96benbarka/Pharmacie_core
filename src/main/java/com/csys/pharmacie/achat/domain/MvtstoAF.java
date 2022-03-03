/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.MvtSto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "mvtstoAF")

public class MvtstoAF extends MvtSto implements Serializable {

    @Column(name = "montht")
    private BigDecimal montht;
    @Column(name = "tautva")
    private BigDecimal tautva;

    @NotNull
    @Column(name = "codtva")
    private Integer codtva;

    @Column(name = "remise")
    private BigDecimal remise;

    @Column(name = "numbon_reception")
    private String numbonReception;
    
    @Column(name = "base_tva")
    private BigDecimal baseTva;
    
      // on va stocker ces deux facteurs necessaire pour le calcul du pmp : oldPMP+oldQuantity
    @Column(name = "pmp_precedant")
    private BigDecimal pmpPrecedent;
    // c est la quantite avant l'avoir
    @Column(name = "quantite_precedante")
    private BigDecimal quantitePrecedante;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codeMvtstoAF")
    private List<DetailMvtStoAF> detailMvtStoAFList;

    @JoinColumn(name = "numbon", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    private AvoirFournisseur avoirFournisseur;

    public MvtstoAF() {
    }

    public MvtstoAF(BigDecimal montht, BigDecimal quantite, BigDecimal priuni) {
        super(quantite, priuni);
        this.montht = montht;
    }

    public MvtstoAF(BigDecimal quantite, BigDecimal priuni) {
        super(quantite, priuni);
    }

    public BigDecimal getMontht() {
        return montht;
    }

    public void setMontht(BigDecimal montht) {
        this.montht = montht;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public String getNumbonReception() {
        return numbonReception;
    }

    public void setNumbonReception(String numbonReception) {
        this.numbonReception = numbonReception;
    }

    public List<DetailMvtStoAF> getDetailMvtStoAFList() {
        return detailMvtStoAFList;
    }

    public void setDetailMvtStoAFList(List<DetailMvtStoAF> detailMvtStoAFList) {
        this.detailMvtStoAFList = detailMvtStoAFList;
    }

    public AvoirFournisseur getAvoirFournisseur() {
        return avoirFournisseur;
    }

    public void setAvoirFournisseur(AvoirFournisseur avoirFournisseur) {
        this.avoirFournisseur = avoirFournisseur;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getBaseTva() {
        return baseTva;
    }

    public void setBaseTva(BigDecimal baseTva) {
        this.baseTva = baseTva;
    }

    public BigDecimal getPmpPrecedent() {
        return pmpPrecedent;
    }

    public void setPmpPrecedent(BigDecimal pmpPrecedent) {
        this.pmpPrecedent = pmpPrecedent;
    }

    public BigDecimal getQuantitePrecedante() {
        return quantitePrecedante;
    }

    public void setQuantitePrecedante(BigDecimal quantitePrecedante) {
        this.quantitePrecedante = quantitePrecedante;
    }

    @Override
    public String toString() {
        return "MvtstoAF{" + "montht=" + montht + ", tautva=" + tautva + ", codtva=" + codtva + ", remise=" + remise + ", numbonReception=" + numbonReception + ", baseTva=" + baseTva + ", pmpPrecedent=" + pmpPrecedent + ", quantitePrecedante=" + quantitePrecedante + '}';
    }

}
