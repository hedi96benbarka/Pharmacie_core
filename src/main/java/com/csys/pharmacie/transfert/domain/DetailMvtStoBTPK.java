/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Embeddable
public class DetailMvtStoBTPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_depsto")
    private long codeDepsto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_article")
    private long codeArticle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbon")
    private String numbon;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "numorder")
    private String numorder;

    public DetailMvtStoBTPK() {
    }

    public DetailMvtStoBTPK(long codeDepsto, long codeArticle, String numbon, String numorder) {
        this.codeDepsto = codeDepsto;
        this.codeArticle = codeArticle;
        this.numbon = numbon;
        this.numorder = numorder;
    }

    public long getCodeDepsto() {
        return codeDepsto;
    }

    public void setCodeDepsto(long codeDepsto) {
        this.codeDepsto = codeDepsto;
    }

    public long getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(long codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumorder() {
        return numorder;
    }

    public void setNumorder(String numorder) {
        this.numorder = numorder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codeDepsto;
        hash += (int) codeArticle;
        hash += (numbon != null ? numbon.hashCode() : 0);
        hash += (numorder != null ? numorder.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailMvtStoBTPK)) {
            return false;
        }
        DetailMvtStoBTPK other = (DetailMvtStoBTPK) object;
        if (this.codeDepsto != other.codeDepsto) {
            return false;
        }
        if (this.codeArticle != other.codeArticle) {
            return false;
        }
        if ((this.numbon == null && other.numbon != null) || (this.numbon != null && !this.numbon.equals(other.numbon))) {
            return false;
        }
        if ((this.numorder == null && other.numorder != null) || (this.numorder != null && !this.numorder.equals(other.numorder))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.transfert.domain.DetailMvtStoBTPK[ codeDepsto=" + codeDepsto + ", codeArticle=" + codeArticle + ", numbon=" + numbon + ", numorder=" + numorder + " ]";
    }
    
}
