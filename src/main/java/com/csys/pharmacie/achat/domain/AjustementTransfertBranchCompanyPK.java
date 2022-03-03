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
 * @author Administrateur
 */
@Embeddable
public class AjustementTransfertBranchCompanyPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbon")
    private String numbon;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_article")
    private Integer codeArticle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_unite")
    private Integer codeUnite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbon_retour")
    private String numbonRetour;

    public AjustementTransfertBranchCompanyPK() {
    }

    public AjustementTransfertBranchCompanyPK(String numbon, Integer codeArticle, Integer codeUnite, String numbonRetour) {
        this.numbon = numbon;
        this.codeArticle = codeArticle;
        this.codeUnite = codeUnite;
        this.numbonRetour = numbonRetour;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getNumbonRetour() {
        return numbonRetour;
    }

    public void setNumbonRetour(String numbonRetour) {
        this.numbonRetour = numbonRetour;
    }

   
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numbon != null ? numbon.hashCode() : 0);
        hash += (int) codeArticle;
        hash += (int) codeUnite;
        hash += (numbonRetour != null ? numbonRetour.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AjustementTransfertBranchCompanyPK)) {
            return false;
        }
        AjustementTransfertBranchCompanyPK other = (AjustementTransfertBranchCompanyPK) object;
        if ((null == this.numbon && other.numbon != null) || (this.numbon != null && !this.numbon.equals(other.numbon))) {
            return false;
        }
        if (this.codeArticle != other.codeArticle) {
            return false;
        }
        if (this.codeUnite != other.codeUnite) {
            return false;
        }
        if ((this.numbonRetour == null && other.numbonRetour != null) || (this.numbonRetour != null && !this.numbonRetour.equals(other.numbonRetour))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.service.AjustementTransfertBranchCompanyPK[ numbon=" + numbon + ", codeArticle=" + codeArticle + ", codeUnite=" + codeUnite + ", numbonRetour=" + numbonRetour + " ]";
    }
    
}
