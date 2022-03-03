/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Embeddable
public class ResteRecuperationPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_article")
    private Integer codeArticle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_depot")
    private Integer codeDepot;

    public ResteRecuperationPK() {
    }

    public ResteRecuperationPK(Integer codeArticle, Integer codeDepot) {
        this.codeArticle = codeArticle;
        this.codeDepot = codeDepot;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.codeArticle);
        hash = 67 * hash + Objects.hashCode(this.codeDepot);
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
        final ResteRecuperationPK other = (ResteRecuperationPK) obj;
        if (!Objects.equals(this.codeArticle, other.codeArticle)) {
            return false;
        }
        if (!Objects.equals(this.codeDepot, other.codeDepot)) {
            return false;
        }
        return true;
    }

    
    @Override
    public String toString() {
        return "com.csys.pharmacie.transfert.domain.ResteRecuperationPK[ codeArticle=" + codeArticle + ", codeDepot=" + codeDepot + " ]";
    }
    
}
