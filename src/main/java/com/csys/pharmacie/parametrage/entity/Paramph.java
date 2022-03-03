/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.parametrage.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 * @author ERRAYHAN
 */
@Entity
@Table(name = "Param")
@NamedQueries({
    @NamedQuery(name = "Paramph.findAll", query = "SELECT p FROM Paramph p"),
    @NamedQuery(name = "Paramph.findByCode", query = "SELECT p FROM Paramph p WHERE p.code = :code"),
    @NamedQuery(name = "Paramph.findByValeur", query = "SELECT p FROM Paramph p WHERE p.valeur = :valeur")})
public class Paramph implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "Valeur")
    private String valeur;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "Nature")
    private String nature;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Module")
    private String module;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Code")
    private String code;
//    @Size(max = 20)
//    @Column(name = "valeur")
//    private String valeur;

    public Paramph() {
    }

    public Paramph(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paramph)) {
            return false;
        }
        Paramph other = (Paramph) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.achat.model.Paramph[ code=" + code + " ]";
    }

//    public String getValeur() {
//        return valeur;
//    }
//
//    public void setValeur(String valeur) {
//        this.valeur = valeur;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
    
}
