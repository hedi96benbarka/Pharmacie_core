/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Embeddable
public class DetailInventairePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "inventaire")
    private Integer inventaire;
    @Basic(optional = false)
    @NotNull
    @Column(name = "categorie_article")
    private Integer categorieArticle;

    public DetailInventairePK() {
    }

    public Integer getInventaire() {
        return inventaire;
    }

    public void setInventaire(Integer inventaire) {
        this.inventaire = inventaire;
    }

    public Integer getCategorieArticle() {
        return categorieArticle;
    }

    public void setCategorieArticle(Integer categorieArticle) {
        this.categorieArticle = categorieArticle;
    }

  
    public void setCategorieArticle(int categorieArticle) {
        this.categorieArticle = categorieArticle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) inventaire;
        hash += (int) categorieArticle;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailInventairePK)) {
            return false;
        }
        DetailInventairePK other = (DetailInventairePK) object;
        if (this.inventaire != other.inventaire) {
            return false;
        }
        if (this.categorieArticle != other.categorieArticle) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetailInventairePK{" + "inventaire=" + inventaire + ", categorieArticle=" + categorieArticle + '}';
    }

  
}
