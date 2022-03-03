/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Farouk
 */
@Embeddable
public class ArticleFournisseurPK implements Serializable {

    @Basic(optional = false) @NotNull @Column(name = "fk_warticle_code")
    private int fkArticleCode;
    @Basic(optional = false) @NotNull @Size(min = 1, max = 10) @Column(name = "fk_fournisseur_code")
    private String fkFournisseurCode;

    public ArticleFournisseurPK() {
    }

    public ArticleFournisseurPK(int fkArticleCode, String fkFournisseurCode) {
        this.fkArticleCode = fkArticleCode;
        this.fkFournisseurCode = fkFournisseurCode;
    }

    public int getFkArticleCode() {
        return fkArticleCode;
    }

    public void setFkArticleCode(int fkArticleCode) {
        this.fkArticleCode = fkArticleCode;
    }

    public String getFkFournisseurCode() {
        return fkFournisseurCode;
    }

    public void setFkFournisseurCode(String fkFournisseurCode) {
        this.fkFournisseurCode = fkFournisseurCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fkArticleCode;
        hash += (fkFournisseurCode != null ? fkFournisseurCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArticleFournisseurPK)) {
            return false;
        }
        ArticleFournisseurPK other = (ArticleFournisseurPK) object;
        if (this.fkArticleCode != other.fkArticleCode) {
            return false;
        }
        if ((this.fkFournisseurCode == null && other.fkFournisseurCode != null) || (this.fkFournisseurCode != null && !this.fkFournisseurCode.equals(other.fkFournisseurCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.ArticleFournisseurPK[ fkArticleCode=" + fkArticleCode + ", fkFournisseurCode=" + fkFournisseurCode + " ]";
    }
    
}
