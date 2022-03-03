/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "depsto_scan")
@NamedQueries({
    @NamedQuery(name = "DepstoScan.findAll", query = "SELECT d FROM DepstoScan d")})
public class DepstoScan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
      
    @Column(name = "Num")
    private Long num;
    @Basic(optional = false)
    @NotNull
     
    @Column(name = "codart")
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "unite")
    private Integer unite;
    @Column(name = "coddep")
    private Integer coddep;
    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "categ_depot")
    private CategorieDepotEnum categDepot;
    @Basic(optional = false)
     
    @Size(min = 1, max = 255) 
    @Column(name = "desart")
    private String desart;
    @Basic(optional = false)
     
    @Column(name = "uniteDesignation")
    private String uniteDesignation;
    
    @Size(min = 1, max = 255)
    @Column(name = "desart_sec")
    private String desartSec;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite")
    private BigDecimal quantite;
    @Column(name = "DatPer")
    private LocalDate datPer;
    @Size(max = 50)
    @Column(name = "Lot_Inter")
    private String lotInter;
    @Basic(optional = false)
     
    @Size(min = 1, max = 50)
    @Column(name = "code_saisi")
    private String codeSaisi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "defectueux")
    private boolean defectueux;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Importer")
    private boolean importer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Inventerier")
    private boolean inventerier;
    @Basic(optional = false)
    
    @Column(name = "HeureSysteme")

     
    private LocalDateTime heureSysteme;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "adresseMac")
    private String adresseMac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "userName")
    private String userName;
    
    @Column(name = "categArt")
    private Integer categArt ; 
    
    
    @Column(name = "inventaire")
    private Integer codInv ; 

    public Integer getCodInv() {
        return codInv;
    }

    public void setCodInv(Integer codInv) {
        this.codInv = codInv;
    }

    public Integer getCategArt() {
        return categArt;
    }

    public void setCategArt(Integer categArt) {
        this.categArt = categArt;
    }
    
    

    public DepstoScan() {
    }

    public DepstoScan(Long num) {
        this.num = num;
    }

    public DepstoScan(Long num, Integer codart, Integer unite, CategorieDepotEnum categDepot, String desart, String desartSec, BigDecimal quantite, String codeSaisi, boolean defectueux, boolean importer, boolean inventerier, LocalDateTime heureSysteme, String adresseMac, String userName , String uniteDesignation , Integer codInv) {
        this.num = num;
        this.codart = codart;
        this.unite = unite; 
        this.categDepot = categDepot;
        this.desart = desart;
        this.desartSec = desartSec;
        this.quantite = quantite;
        this.codeSaisi = codeSaisi;
        this.defectueux = defectueux;
        this.importer = importer;
        this.inventerier = inventerier;
        this.heureSysteme = heureSysteme;
        this.adresseMac = adresseMac;
        this.userName = userName;
        this.uniteDesignation = uniteDesignation ; 
        this.codInv = codInv ; 
    }

    public String getUniteDesignation() {
        return uniteDesignation;
    }

    public void setUniteDesignation(String uniteDesignation) {
        this.uniteDesignation = uniteDesignation;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

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

    public String getDesartSec() {
        return desartSec;
    }

    public void setDesartSec(String desartSec) {
        this.desartSec = desartSec;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDatPer() {
        return datPer;
    }
    
 

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public boolean getDefectueux() {
        return defectueux;
    }

    public void setDefectueux(boolean defectueux) {
        this.defectueux = defectueux;
    }

    public boolean getImporter() {
        return importer;
    }

    public void setImporter(boolean importer) {
        this.importer = importer;
    }

    public boolean getInventerier() {
        return inventerier;
    }

    public void setInventerier(boolean inventerier) {
        this.inventerier = inventerier;
    }

    public LocalDateTime getHeureSysteme() {
        return heureSysteme;
    }

    public void setHeureSysteme(LocalDateTime heureSysteme) {
        this.heureSysteme = heureSysteme;
    }

    public String getAdresseMac() {
        return adresseMac;
    }

    public void setAdresseMac(String adresseMac) {
        this.adresseMac = adresseMac;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (num != null ? num.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepstoScan)) {
            return false;
        }
        DepstoScan other = (DepstoScan) object;
        if ((this.num == null && other.num != null) || (this.num != null && !this.num.equals(other.num))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.inventaire.domain.DepstoScan[ num=" + num + " ]";
    }
    
}
