///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package com.csys.pharmacie.helper;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@MappedSuperclass
public class MvtSto {
    
    
    //----------------------debasebon
     @NotNull
    @Column(name = "categ_depot")
    @Enumerated(EnumType.STRING)
    private CategorieDepotEnum categDepot;
    @NotNull
    @Size(max = 255)
    @Column(name = "desart")
    private String desart;
    @NotNull
    @Size(max = 255)
    @Column(name = "desart_sec")
    private String desArtSec;
    @NotNull
    @Size(max = 50)
    @Column(name = "code_saisi")
    private String codeSaisi;
    @NotNull
    @Column(name = "quantite")
    private BigDecimal quantite;
    
    //----------------------mvtsto
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)   
    @Column(name = "code")
    private Integer code;

  
    @Column(name = "codart")
    private Integer codart;            
   
    @Column(name = "numbon" , insertable = false , updatable = false)
    private String numbon;
    @Basic(optional = false)
    
    @Size(min = 1, max = 50)
    @Column(name = "lot_inter")
    private String lotinter;
    
    @Column(name = "DatPer")
    private LocalDate datPer;

    @Basic(optional = false)
    @NotNull
    @Column(name = "priuni")
    private BigDecimal priuni;

    @Basic(optional = false)

    @Column(name = "code_unite")
    private Integer unite;
   


    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesArtSec() {
        return desArtSec;
    }

    public void setDesArtSec(String desArtSec) {
        this.desArtSec = desArtSec;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
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

    public MvtSto() {
    }

    public MvtSto(BigDecimal quantite, Integer codart, String lotinter, LocalDate datPer, Integer unite) {
        this.quantite = quantite;
        this.codart = codart;
        this.lotinter = lotinter;
        this.datPer = datPer;
        this.unite = unite;
    }

    public MvtSto(BigDecimal quantite, BigDecimal priuni) {
        this.quantite = quantite;
        this.priuni = priuni;
    }

    public MvtSto(BigDecimal quantite, Integer codart, BigDecimal priuni, Integer unite) {
        this.quantite = quantite;
        this.codart = codart;
        this.priuni = priuni;
        this.unite = unite;
    }

    public MvtSto(MvtSto mvtsto ) {
        this.categDepot = mvtsto.categDepot;
        this.desart = mvtsto.desart;
        this.desArtSec = mvtsto.desArtSec;
        this.codeSaisi = mvtsto.codeSaisi;
        this.quantite = mvtsto.quantite;
        this.code = mvtsto.code;
        this.codart = mvtsto.codart;
        this.numbon = mvtsto.numbon;
        this.lotinter = mvtsto.lotinter;
        this.datPer = mvtsto.datPer;
        this.priuni = mvtsto.priuni;
        this.unite = mvtsto.unite;
    }

    
    @Override
    public String toString() {
        return "MvtSto{" + "categDepot=" + categDepot + ", desart=" + desart + ", desArtSec=" + desArtSec + ", codeSaisi=" + codeSaisi + ", quantite=" + quantite + ", code=" + code + ", codart=" + codart + ", numbon=" + numbon + ", lotinter=" + lotinter + ", datPer=" + datPer + ", priuni=" + priuni + ", unite=" + unite + '}';
    }
  
}
