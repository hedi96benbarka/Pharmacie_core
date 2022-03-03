/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.stock.domain.Depsto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author bassatine
 */

@Audited
@Entity
@Table(name = "detail_MvtStoBA")
@AuditTable("detail_MvtStoBA_AUD")
public class DetailMvtStoBA implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetailMvtStoBAPK pk;

    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_disponible")
    private BigDecimal quantiteDisponible;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_retourne")
    private BigDecimal quantite_retourne;

    @Column(name = "numbon_depsto")
    private String numbonDepsto;

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

    @Column(name = "taux_tva")
    private BigDecimal tauxTva;

    @Size(min = 0, max = 20)
    @Column(name = "numbon_origin")
    private String numBonOrigin;
        
    @Column(name = "code_tva")
    private Integer codeTva;
    @JoinColumns({
        @JoinColumn(name = "codart", referencedColumnName = "codart", insertable = false, updatable = false)
        , @JoinColumn(name = "numbon", referencedColumnName = "numbon", insertable = false, updatable = false)
        , @JoinColumn(name = "numordre", referencedColumnName = "numordre", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private MvtStoBA mvtStoBA;

    public DetailMvtStoBAPK getPk() {
        return pk;
    }

    public void setPk(DetailMvtStoBAPK pk) {
        this.pk = pk;
    }

    public DetailMvtStoBA(DetailMvtStoBAPK pk, MvtStoBA mvtStoBA, Depsto depsto) {
        this.pk = pk;
        this.stkRel = depsto.getStkrel();
        this.priuni = depsto.getPu();
        this.datPer = depsto.getDatPer();
        this.unite = depsto.getUnite();
        this.lotinter = depsto.getLotInter();
        this.datSYS = depsto.getDatesys();
        this.numbonDepsto = depsto.getNumBon();
        this.quantiteDisponible = depsto.getQte();

    }

    public BigDecimal getQuantiteDisponible() {
        return quantiteDisponible;
    }

    public void setQuantiteDisponible(BigDecimal quantiteDisponible) {
        this.quantiteDisponible = quantiteDisponible;
    }

    public BigDecimal getQuantite_retourne() {
        return quantite_retourne;
    }

    public void setQuantite_retourne(BigDecimal quantite_retourne) {
        this.quantite_retourne = quantite_retourne;
    }

    public String getNumbonDepsto() {
        return numbonDepsto;
    }

    public void setNumbonDepsto(String numbonDepsto) {
        this.numbonDepsto = numbonDepsto;
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

    public MvtStoBA getMvtStoBA() {
        return mvtStoBA;
    }

    public void setMvtStoBA(MvtStoBA mvtStoBA) {
        this.mvtStoBA = mvtStoBA;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public String getNumBonOrigin() {
        return numBonOrigin;
    }

    public void setNumBonOrigin(String numBonOrigin) {
        this.numBonOrigin = numBonOrigin;
    }

    public void setDepsto(MvtStoBA mvt, Depsto depsto) {

        this.stkRel = depsto.getStkrel();
        this.priuni = depsto.getPu();
        this.datPer = depsto.getDatPer();
        this.unite = depsto.getUnite();
        this.lotinter = depsto.getLotInter();
        this.datSYS = depsto.getDatesys();
        this.numbonDepsto = depsto.getNumBon();
        this.quantiteDisponible = depsto.getQte();

    }

    public DetailMvtStoBA(DetailMvtStoBAPK pk) {
        this.pk = pk;
    }

    public DetailMvtStoBA() {
    }

    @Override
    public String toString() {
        return "DetailMvtStoBA{" + "pk=" + pk + ", quantite_retourne=" + quantite_retourne + ", numbonDepsto=" + numbonDepsto + ", priuni=" + priuni + ", unite=" + unite + ", numBonOrigin=" + numBonOrigin + ", mvtStoBA=" + mvtStoBA + '}';
    }


    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

}
