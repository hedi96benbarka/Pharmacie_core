/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "valeur_stock")
public class ValeurStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ValeurStockPK valeurStockPK;
    @Column(name = "coddep")
    private Integer coddep;
    @Column(name = "codart")
    private Integer codart;
    @Size(max = 50)
    @Column(name = "Lot_Inter", length = 50)
    private String lotInter;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qte", precision = 18, scale = 3)
    private BigDecimal qte;
    @Column(name = "PU", precision = 18, scale = 5)
    private BigDecimal pu;
    @Enumerated(EnumType.STRING)
    @Column(name = "categ_depot")
    private CategorieDepotEnum categDepot;
    @Column(name = "PMP", precision = 18, scale = 3)
    private BigDecimal pmp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unite")
    private Integer unite;
    @Column(name = "DatPer")
    private LocalDate datPer;
    @Column(name = "taux_tva_achat")
    private BigDecimal tauxTvaAchat;
    @Column(name = "taux_tva_vente")
    private BigDecimal tauxTvaVente;
    
    transient BigDecimal valeur;

    public ValeurStock() {
    }

    public ValeurStock(BigDecimal qte, BigDecimal pu, BigDecimal tauxTvaAchat) {
        this.qte = qte;
        this.pu = pu;
        this.tauxTvaAchat = tauxTvaAchat;
    }

    public ValeurStock(Integer codart, BigDecimal qte) {
        this.codart = codart;
        this.qte = qte;
    }


    public ValeurStock(ValeurStockPK valeurStockPK) {
        this.valeurStockPK = valeurStockPK;
    }

    public ValeurStock(LocalDate datesys, long codeDepsto) {
        this.valeurStockPK = new ValeurStockPK(datesys, codeDepsto);
    }

    public ValeurStockPK getValeurStockPK() {
        return valeurStockPK;
    }

    public void setValeurStockPK(ValeurStockPK valeurStockPK) {
        this.valeurStockPK = valeurStockPK;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public BigDecimal getPmp() {
        return pmp;
    }

    public void setPmp(BigDecimal pmp) {
        this.pmp = pmp;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public BigDecimal getTauxTvaAchat() {
        return tauxTvaAchat;
    }

    public void setTauxTvaAchat(BigDecimal tauxTvaAchat) {
        this.tauxTvaAchat = tauxTvaAchat;
    }

    public BigDecimal getTauxTvaVente() {
        return tauxTvaVente;
    }

    public void setTauxTvaVente(BigDecimal tauxTvaVente) {
        this.tauxTvaVente = tauxTvaVente;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (valeurStockPK != null ? valeurStockPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValeurStock)) {
            return false;
        }
        ValeurStock other = (ValeurStock) object;
        if ((this.valeurStockPK == null && other.valeurStockPK != null) || (this.valeurStockPK != null && !this.valeurStockPK.equals(other.valeurStockPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ValeurStock{" + "valeurStockPK=" + valeurStockPK + ", coddep=" + coddep + ", codart=" + codart + ", lotInter=" + lotInter + ", qte=" + qte + ", pu=" + pu + ", categDepot=" + categDepot + ", unite=" + unite + ", datPer=" + datPer + ", tauxTvaAchat=" + tauxTvaAchat + '}';
    }

  
    
}
