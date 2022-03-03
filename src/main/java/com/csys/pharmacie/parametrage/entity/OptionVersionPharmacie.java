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
@Table(name = "OptionVersionPharmacie")

@NamedQueries({
    @NamedQuery(name = "OptionVersionPharmacie.findAll", query = "SELECT o FROM OptionVersionPharmacie o"),
    @NamedQuery(name = "OptionVersionPharmacie.findById", query = "SELECT o FROM OptionVersionPharmacie o WHERE o.id = :id"),
    @NamedQuery(name = "OptionVersionPharmacie.findByDescript", query = "SELECT o FROM OptionVersionPharmacie o WHERE o.descript = :descript"),
    @NamedQuery(name = "OptionVersionPharmacie.findByValeur", query = "SELECT o FROM OptionVersionPharmacie o WHERE o.valeur = :valeur")})
public class OptionVersionPharmacie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Descript")
    private String descript;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Valeur")
    private String valeur;

    public OptionVersionPharmacie() {
    }

    public OptionVersionPharmacie(String id) {
        this.id = id;
    }

    public OptionVersionPharmacie(String id, String descript, String valeur) {
        this.id = id;
        this.descript = descript;
        this.valeur = valeur;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OptionVersionPharmacie)) {
            return false;
        }
        OptionVersionPharmacie other = (OptionVersionPharmacie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.achat.model.pharmacie.OptionVersionPharmacie[ id=" + id + " ]";
    }
    
}
