/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseMouvementStock implements Cloneable {

    @Id
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbon")
    private String numbon;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codart")
    private Integer codart;
    @Size(max = 2)
    @Column(name = "typbon")
    private String typbon;
    @Enumerated(EnumType.STRING)
    @Column(name = "categ_depot")
    private CategorieDepotEnum categDepot;
    @Size(max = 20)
    @Column(name = "numaffiche")
    private String numaffiche;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datbon")
    private Date datbon;
    @Column(name = "coddep")
    private Integer coddep;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "code_saisi")
    private String codeSaisi;
    @Size(max = 255)
    @Column(name = "desart")
    private String desart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "desart_sec")
    private String desartSec;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private BigDecimal quantite;
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
    @Size(min = 1, max = 150)
    @Column(name = "ordreDesignationMvt")
    private String ordreDesignationMvt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "designationMvt")
    private String designationMvt;

    public BaseMouvementStock() {
    }

    public BaseMouvementStock(Integer codart, Integer codeUnite, BigDecimal quantite, BigDecimal valeur) {
        this.codart = codart;
        this.codeUnite = codeUnite;
        this.quantite = quantite;
        this.valeur = valeur;
    }

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BaseMouvementStock other = (BaseMouvementStock) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "BaseMouvementStock{" + "id=" + id + '}';
//    }
//    @Override
//    public String toString() {
//        return "BaseMouvementStock{" + "typbon=" + typbon + ", coddep=" + coddep + ", valeur=" + valeur + ", ordreDesignationMvt=" + ordreDesignationMvt + '}';
//    }

    @Override
    public Object clone() {
        Object o = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la 
            // méthode super.clone()
            o = super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implémentons 
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }
        // on renvoie le clone
        return o;
    }

    }
