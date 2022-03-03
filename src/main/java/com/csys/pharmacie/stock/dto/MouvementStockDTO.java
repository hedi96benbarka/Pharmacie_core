/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Administrateur
 */
public class MouvementStockDTO implements Serializable{
    @JsonIgnore
    private String id;
    @JsonIgnore
    private String numbon;
    private Integer codart;
    @JsonIgnore
    private String typbon;
    @JsonIgnore
    private CategorieDepotEnum categDepot;
    @JsonIgnore
    private String numaffiche;
    @JsonIgnore
    private Date datbon;
    @JsonIgnore
    private Integer coddep;
    @JsonIgnore
    private String codeSaisi;
    @JsonIgnore
    private String desart;
    @JsonIgnore
    private String desartSec;

    private BigDecimal quantite;

    private BigDecimal valeur;

    private Integer codeUnite;
    @JsonIgnore
    private String desUnite;
    @JsonIgnore
    private String ordreDesignationMvt;
    @JsonIgnore
    private String designationMvt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
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

    public Date getDatbon() {
        return datbon;
    }

    public void setDatbon(Date datbon) {
        this.datbon = datbon;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
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

    public String getOrdreDesignationMvt() {
        return ordreDesignationMvt;
    }

    public void setOrdreDesignationMvt(String ordreDesignationMvt) {
        this.ordreDesignationMvt = ordreDesignationMvt;
    }

    public String getDesignationMvt() {
        return designationMvt;
    }

    public void setDesignationMvt(String designationMvt) {
        this.designationMvt = designationMvt;
    }

    public MouvementStockDTO(Integer codart, Integer codeUnite, BigDecimal quantite, BigDecimal valeur) {
        this.codart = codart;
        this.quantite = quantite;
        this.valeur = valeur;
        this.codeUnite = codeUnite;
    }
    
    public MouvementStockDTO() {
    }
    
}
