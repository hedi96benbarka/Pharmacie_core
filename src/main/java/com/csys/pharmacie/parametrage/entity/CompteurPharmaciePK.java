/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.parametrage.entity;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Embeddable
public class CompteurPharmaciePK implements Serializable {

    @Basic(optional = false)
    @NotNull 
    @Enumerated(EnumType.STRING)
    @Column(name = "code_depot", nullable = false, length = 10)
    private CategorieDepotEnum codeDepot;
    @Basic(optional = false)
    @NotNull 
    @Enumerated(EnumType.STRING)
    @Column(name = "type_bon", nullable = false, length = 10)
    private TypeBonEnum typeBon;

    public CompteurPharmaciePK(CategorieDepotEnum codeDepot, TypeBonEnum typeBon) {
        this.codeDepot = codeDepot;
        this.typeBon = typeBon;
    }

    
    
    public CompteurPharmaciePK() {
    }

    public CategorieDepotEnum getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(CategorieDepotEnum codeDepot) {
        this.codeDepot = codeDepot;
    }

    public TypeBonEnum getTypeBon() {
        return typeBon;
    }

    public void setTypeBon(TypeBonEnum typeBon) {
        this.typeBon = typeBon;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final CompteurPharmaciePK other = (CompteurPharmaciePK) obj;
        if (this.codeDepot != other.codeDepot) {
            return false;
        }
        if (this.typeBon != other.typeBon) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CompteurPharmaciePK{" + "codeDepot=" + codeDepot + ", typeBon=" + typeBon + '}';
    }

   
    
}
