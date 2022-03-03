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
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "fiche_stock")
public class FicheStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 767)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 21)
    @Column(name = "designationMvt")
    private String designationMvt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbon")
    private String numbon;
    @Size(max = 4)
    @Column(name = "typbon")
    private String typbon;
    @Enumerated(EnumType.STRING)
    @Column(name = "categ_depot")
    private CategorieDepotEnum categDepot;
    @Size(max = 20)
    @Column(name = "numaffiche")
    private String numaffiche;
    @Column(name = "datbon")
    private LocalDateTime datbon;
    @Column(name = "coddep")
    private Integer coddep;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codart")
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "code_saisi")
    private String codeSaisi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "desart")
    private String desart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "desart_sec")
    private String desartSec;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private BigDecimal quantite;
    @Column(name = "priuni")
    private BigDecimal priuni;
    @Column(name = "taux_tva")
    private BigDecimal tauxTva;
    @Column(name = "valeur")
    private BigDecimal valeur;
    @Column(name = "code_unite")
    private Integer codeUnite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "des_unite")
    private String desUnite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "depot")
    private String depot;
    @Column(name = "datperMvt")
    private LocalDate datperMvt;
    @Size(max = 70)
    @Column(name = "lotMvt")
    private String lotMvt;
    @Column(name = "datdepsto")
    private LocalDate datdepsto;
    @Size(max = 70)
    @Column(name = "lotdepsto")
    private String lotdepsto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "libelle")
    private String libelle;

    public FicheStock() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesignationMvt() {
        return designationMvt;
    }

    public void setDesignationMvt(String designationMvt) {
        this.designationMvt = designationMvt;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getTypbon() {
        return typbon;
    }

    public void setTypbon(String typbon) {
        this.typbon = typbon;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public LocalDateTime getDatbon() {
        return datbon;
    }

    public void setDatbon(LocalDateTime datbon) {
        this.datbon = datbon;
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

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
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

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesUnite() {
        return desUnite;
    }

    public void setDesUnite(String desUnite) {
        this.desUnite = desUnite;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public LocalDate getDatperMvt() {
        return datperMvt;
    }

    public void setDatperMvt(LocalDate datperMvt) {
        this.datperMvt = datperMvt;
    }

    public String getLotMvt() {
        return lotMvt;
    }

    public void setLotMvt(String lotMvt) {
        this.lotMvt = lotMvt;
    }

    public LocalDate getDatdepsto() {
        return datdepsto;
    }

    public void setDatdepsto(LocalDate datdepsto) {
        this.datdepsto = datdepsto;
    }

    public String getLotdepsto() {
        return lotdepsto;
    }

    public void setLotdepsto(String lotdepsto) {
        this.lotdepsto = lotdepsto;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

}
