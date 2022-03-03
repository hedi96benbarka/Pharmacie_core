/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;

import javax.persistence.Table;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "valeur_stock_globale")
public class ValeurStockGlobale extends BaseMouvementStock implements Serializable {
@Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.getId());
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
        final MouvementStock other = (MouvementStock) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MouvementStock{" + "id=" + getId() + '}';
    }   
}
