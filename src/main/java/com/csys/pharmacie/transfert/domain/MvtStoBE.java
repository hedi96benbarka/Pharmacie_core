/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import com.csys.pharmacie.helper.MvtSto;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "MvtStoBE")
public class MvtStoBE extends MvtSto implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mvtStoBE")
    private List<DetailMvtStoBE> detailMvtStoBEList;

    @Size(min = 1, max = 6)
    @Column(name = "numordre")
    private String numordre;

    @Column(name = "codtva")
    private Integer codtva;

    @Column(name = "tautva")
    private BigDecimal tautva;

    // on va stocker ces deux facteurs necessaire pour le calcul du pmp : oldPMP+oldQuantity
    @Column(name = "pmp_precedant")
    private BigDecimal pmpPrecedent;
    // c est la quantite avant la reception 
    @Column(name = "quantite_precedante")
    private BigDecimal quantitePrecedante;

         
    @JoinColumn(name = "numbon", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    private FactureBE factureBE;

    
    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public MvtStoBE() {
    }

    public MvtStoBE(BigDecimal quantite, BigDecimal priuni) {
        super(quantite, priuni);
    }

    public MvtStoBE(BigDecimal quantite, Integer codart, String lotinter, LocalDate datPer, Integer unite) {
        super(quantite, codart, lotinter, datPer, unite);
    }

    public MvtStoBE(BigDecimal quantite, Integer codart, BigDecimal priuni, Integer unite) {
        super(quantite, codart, priuni, unite);
    }

     public MvtStoBE(MvtStoBE mvtsoBE) {
         super(mvtsoBE);
         this.codtva=mvtsoBE.codtva;
         this.tautva=mvtsoBE.tautva;
         
         
    }

    public FactureBE getFactureBE() {
        return factureBE;
    }

    public void setFactureBE(FactureBE factureBE) {
        this.factureBE = factureBE;

    }

    public List<DetailMvtStoBE> getDetailMvtStoBEList() {
        return detailMvtStoBEList;
    }

    public void setDetailMvtStoBEList(List<DetailMvtStoBE> detailMvtStoBEList) {
        this.detailMvtStoBEList = detailMvtStoBEList;
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

//    @Override
//    public String toString() {
//        return "MvtStoBE{" +
//                "detailMvtStoBEList=" + detailMvtStoBEList +
//                ", numordre='" + numordre + '\'' +
//                ", codtva=" + codtva +
//                ", tautva=" + tautva +
//                ", pmpPrecedent=" + pmpPrecedent +
//                ", quantitePrecedante=" + quantitePrecedante +
//                ", factureBE=" + factureBE +
//                '}';
//    }

    //    @Override
//    public String toString() {
//        return "MvtStoBE{" + "detailMvtStoBEList=" + detailMvtStoBEList + ", numordre=" + numordre + ", codtva=" + codtva + ", tautva=" + tautva + ", pmpPrecedent=" + pmpPrecedent + ", quantitePrecedante=" + quantitePrecedante + ", factureBE=" + factureBE + '}';
//    }
 
}
