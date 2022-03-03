/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.parametrage.entity;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "Compteur_Pharmacie")
public class CompteurPharmacie implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CompteurPharmaciePK compteurPharmaciePK;
    @Size(max = 10)
    @Column(name = "P1", length = 10)
    private String p1;
    @Size(max = 10)
    @Column(name = "P2", length = 10)
    private String p2;
    @Column(name = "long")
    private Integer long1;

    public CompteurPharmacie() {
    }

    public CompteurPharmacie(CompteurPharmaciePK compteurPharmaciePK) {
        this.compteurPharmaciePK = compteurPharmaciePK;
    }

    public CompteurPharmacie(CategorieDepotEnum codeDepot, TypeBonEnum typeBon) {

    }

    public CompteurPharmacie(CategorieDepotEnum codeDepot, TypeBonEnum typeBon, String p1, String p2, Integer long1) {
        this.compteurPharmaciePK = new CompteurPharmaciePK(codeDepot, typeBon);
        this.p1 = p1;
        this.p2 = p2;
        this.long1 = long1;
    }

    public CompteurPharmaciePK getCompteurPharmaciePK() {
        return compteurPharmaciePK;
    }

    public void setCompteurPharmaciePK(CompteurPharmaciePK compteurPharmaciePK) {
        this.compteurPharmaciePK = compteurPharmaciePK;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public Integer getLong1() {
        return long1;
    }

    public void setLong1(Integer long1) {
        this.long1 = long1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (compteurPharmaciePK != null ? compteurPharmaciePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompteurPharmacie)) {
            return false;
        }
        CompteurPharmacie other = (CompteurPharmacie) object;
        if ((this.compteurPharmaciePK == null && other.compteurPharmaciePK != null) || (this.compteurPharmaciePK != null && !this.compteurPharmaciePK.equals(other.compteurPharmaciePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.p1 + this.p2;
    }

}
