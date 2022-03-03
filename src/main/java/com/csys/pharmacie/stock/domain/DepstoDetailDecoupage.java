/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "depsto_detail_decoupage")
@NamedQueries({
    @NamedQuery(name = "DepstoDetailDecoupage.findAll", query = "SELECT d FROM DepstoDetailDecoupage d")})
public class DepstoDetailDecoupage implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DepstoDetailDecoupagePK depstoDetailDecoupagePK;

    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_decoupee")
    private BigDecimal quantiteDecoupee;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_disponible")
    private BigDecimal quantiteDisponible;
    @JoinColumn(name = "code_depsto", referencedColumnName = "code", insertable = false, updatable = false)
    @ManyToOne(optional = false)

    private Depsto depsto;

    @JoinColumn(name = "code_detail_decoupage", referencedColumnName = "code")

    @ManyToOne
    @MapsId("codeDetailDecoupage")
    private DetailDecoupage detailDecoupage;

    @Column(name = "numbon")
    private String numbon;

    @Column(name = "lot_inter")
    private String lotinter;

    @Column(name = "DatPer")
    private LocalDate datPer;

    @Basic(optional = false)
    @NotNull
    @Column(name = "priuni")
    private BigDecimal priuni;

    @Column(name = "unite")
    private Integer unite;
    @Column(name = "datesys")
    private LocalDateTime datSYS;

    @Column(name = "stkrel")
    private BigDecimal stkRel;

    @Column(name = "code_tva")
    private Integer codeTva;

    @Column(name = "taux_tva")
    private BigDecimal tauxTva;

    public DepstoDetailDecoupage() {
    }

    public DepstoDetailDecoupage(BigDecimal quantiteDecoupee, BigDecimal quantiteDisponible, Depsto depsto) {
        DepstoDetailDecoupagePK pk = new DepstoDetailDecoupagePK();
        pk.setCodeDepsto(depsto.getCode());
        this.depstoDetailDecoupagePK = pk;
        this.quantiteDecoupee = quantiteDecoupee;
        this.quantiteDisponible = quantiteDisponible;
        this.numbon = depsto.getNumBon();
        this.datPer = depsto.getDatPer();
        this.datSYS = depsto.getDatesys();
        this.lotinter = depsto.getLotInter();
        this.priuni = depsto.getPu();
        this.stkRel = depsto.getStkdep();
        this.unite = depsto.getUnite();
        this.codeTva = depsto.getCodeTva();
        this.tauxTva = depsto.getTauxTva();
    }



    public BigDecimal getQuantiteDecoupee() {
        return quantiteDecoupee;
    }

    public void setQuantiteDecoupee(BigDecimal quantiteDecoupee) {
        this.quantiteDecoupee = quantiteDecoupee;
    }

    public BigDecimal getQuantiteDisponible() {
        return quantiteDisponible;
    }

    public void setQuantiteDisponible(BigDecimal quantiteDisponible) {
        this.quantiteDisponible = quantiteDisponible;
    }

    public Depsto getDepsto() {
        return depsto;
    }

    public void setDepsto(Depsto depsto) {
        this.depsto = depsto;
    }

    public DetailDecoupage getDetailDecoupage() {
        return detailDecoupage;
    }

    public void setDetailDecoupage(DetailDecoupage detailDecoupage) {
        this.detailDecoupage = detailDecoupage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (depstoDetailDecoupagePK != null ? depstoDetailDecoupagePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepstoDetailDecoupage)) {
            return false;
        }
        DepstoDetailDecoupage other = (DepstoDetailDecoupage) object;
        if ((this.depstoDetailDecoupagePK == null && other.depstoDetailDecoupagePK != null) || (this.depstoDetailDecoupagePK != null && !this.depstoDetailDecoupagePK.equals(other.depstoDetailDecoupagePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.stock.domain.DepstoDetailDecoupage[ depstoDetailDecoupagePK=" + depstoDetailDecoupagePK + " ]";
    }
//    public Integer getUniteOrigine() {
//        return uniteOrigine;
//    }
//
//    public void setUniteOrigine(Integer uniteOrigine) {
//        this.uniteOrigine = uniteOrigine;
//    }
//    
//    public Integer getCodeDepsto() {
//        return codeDepsto;
//    }
//
//    public void setCodeDepsto(Integer codeDepsto) {
//        this.codeDepsto = codeDepsto;
//    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getLotinter() {
        return lotinter;
    }

    public void setLotinter(String lotinter) {
        this.lotinter = lotinter;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public LocalDateTime getDatSYS() {
        return datSYS;
    }

    public void setDatSYS(LocalDateTime datSYS) {
        this.datSYS = datSYS;
    }

    public BigDecimal getStkRel() {
        return stkRel;
    }

    public void setStkRel(BigDecimal stkRel) {
        this.stkRel = stkRel;
    }

    public DepstoDetailDecoupage(DepstoDetailDecoupagePK depstoDetailDecoupagePK, BigDecimal quantiteDecoupee, BigDecimal quantiteDisponible, Depsto depsto, DetailDecoupage detailDecoupage, String numbon, String lotinter, LocalDate datPer, BigDecimal priuni, Integer unite, LocalDateTime datSYS, BigDecimal stkRel) {
        this.depstoDetailDecoupagePK = depstoDetailDecoupagePK;
        this.quantiteDecoupee = quantiteDecoupee;
        this.quantiteDisponible = quantiteDisponible;
        this.depsto = depsto;
        this.detailDecoupage = detailDecoupage;
        this.numbon = numbon;
        this.lotinter = lotinter;
        this.datPer = datPer;
        this.priuni = priuni;
        this.unite = unite;
        this.datSYS = datSYS;
        this.stkRel = stkRel;
    }

    public DepstoDetailDecoupagePK getDepstoDetailDecoupagePK() {
        return depstoDetailDecoupagePK;
    }

    public void setDepstoDetailDecoupagePK(DepstoDetailDecoupagePK depstoDetailDecoupagePK) {
        this.depstoDetailDecoupagePK = depstoDetailDecoupagePK;
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
    
    

}
