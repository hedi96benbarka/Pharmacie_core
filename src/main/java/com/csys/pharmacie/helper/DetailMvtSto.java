/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import com.csys.pharmacie.stock.domain.Depsto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author bassatine
 */
@MappedSuperclass
public class DetailMvtSto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;

    @Column(name = "code_mvtsto", insertable = false, updatable = false)
    private Integer codeMvtSto;

    @Column(name = "code_depsto")
    private Integer codeDepsto;

    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_disponible")
    private BigDecimal quantiteDisponible;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_prelevee")
    private BigDecimal quantitePrelevee;

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

    @Size(min = 0, max = 20)
    @Column(name = "numbon_origin")
    private String numBonOrigin;
    @Column(name = "is_capitalize")
    private Boolean isCapitalize;

    public DetailMvtSto() {
    }

    public DetailMvtSto(Integer code, BigDecimal quantitePrelevee, BigDecimal priuni, BigDecimal tauxTva) {
        this.code = code;
        this.quantitePrelevee = quantitePrelevee;
        this.priuni = priuni;
        this.tauxTva = tauxTva;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCodeMvtSto() {
        return codeMvtSto;
    }

    public void setCodeMvtSto(Integer codeMvtSto) {
        this.codeMvtSto = codeMvtSto;
    }

    public Integer getCodeDepsto() {
        return codeDepsto;
    }

    public void setCodeDepsto(Integer codeDepsto) {
        this.codeDepsto = codeDepsto;
    }

    public BigDecimal getQuantiteDisponible() {
        return quantiteDisponible;
    }

    public void setQuantiteDisponible(BigDecimal quantiteDisponible) {
        this.quantiteDisponible = quantiteDisponible;
    }

    public BigDecimal getQuantitePrelevee() {
        return quantitePrelevee;
    }

    public void setQuantitePrelevee(BigDecimal quantitePrelevee) {
        this.quantitePrelevee = quantitePrelevee;
    }

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

    public String getNumBonOrigin() {
        return numBonOrigin;
    }

    public void setNumBonOrigin(String numBonOrigin) {
        this.numBonOrigin = numBonOrigin;
    }

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
    }

    public void setPk(Integer codemvt, Depsto depsto) {
        this.codeMvtSto = codemvt;

//        this.codeDepsto = depsto.getCoddep();
        this.codeDepsto = depsto.getCode();
        this.stkRel = depsto.getStkrel();
        this.priuni = depsto.getPu();
        this.datPer = depsto.getDatPer();
        this.unite = depsto.getUnite();
        this.lotinter = depsto.getLotInter();
        this.datSYS = depsto.getDatesys();
        this.numbon = depsto.getNumBon();
        this.quantiteDisponible = depsto.getQte();
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

    public DetailMvtSto(Integer codemvt, Depsto depsto, BigDecimal quantiteDisponible, BigDecimal quantitePrelevee, String numbon, String lotinter, LocalDate datPer, BigDecimal priuni, Integer unite, LocalDateTime datSYS, BigDecimal stkRel) {
        this.codeMvtSto = codemvt;

//        this.codeDepsto = depsto.getCoddep();
        this.codeDepsto = depsto.getCode();
        this.quantiteDisponible = quantiteDisponible;
        this.quantitePrelevee = quantitePrelevee;
        this.numbon = numbon;
        this.lotinter = lotinter;
        this.datPer = datPer;
        this.priuni = priuni;
        this.unite = unite;
        this.datSYS = datSYS;
        this.stkRel = stkRel;
    }

    public DetailMvtSto(Integer codemvt, Integer codeDepsto, Depsto depsto, BigDecimal quantiteDisponible, BigDecimal quantitePrelevee, String numbon, String lotinter, LocalDate datPer, BigDecimal priuni, Integer unite, LocalDateTime datSYS, BigDecimal stkRel) {
        this.codeMvtSto = codemvt;

//        this.codeDepsto = depsto.getCoddep();
        this.codeDepsto = depsto.getCode();
        this.quantiteDisponible = quantiteDisponible;
        this.quantitePrelevee = quantitePrelevee;
        this.numbon = numbon;
        this.lotinter = lotinter;
        this.datPer = datPer;
        this.priuni = priuni;
        this.unite = unite;
        this.datSYS = datSYS;
        this.stkRel = stkRel;
    }

    public DetailMvtSto(Integer codmvt, Depsto depsto) {

        this.codeMvtSto = codmvt;
//        this.codeDepsto = depsto.getCoddep();
        this.codeDepsto = depsto.getCode();
        this.stkRel = depsto.getStkrel();
        this.priuni = depsto.getPu();
        this.datPer = depsto.getDatPer();
        this.unite = depsto.getUnite();
        this.lotinter = depsto.getLotInter();
        this.datSYS = depsto.getDatesys();
        this.numbon = depsto.getNumBon();
        this.quantiteDisponible = depsto.getQte();
    }

}
