/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import java.io.Serializable;
import java.util.Objects;
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
public class DetailDecoupagePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "codart")
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "code_decoupage")
    private String codeDecoupage;
    
    
     @Basic(optional = false)
    @NotNull
    @Column(name = "unite_origine")
    private Integer uniteOrigine;

    public DetailDecoupagePK() {
    }

    public DetailDecoupagePK(Integer codart, String codeDecoupage,Integer uniteOrigine) {
        this.codart = codart;
        this.codeDecoupage = codeDecoupage;
        this.uniteOrigine=uniteOrigine ;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

  

    public String getCodeDecoupage() {
        return codeDecoupage;
    }

    public void setCodeDecoupage(String codeDecoupage) {
        this.codeDecoupage = codeDecoupage;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.codart);
        hash = 17 * hash + Objects.hashCode(this.codeDecoupage);
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
        final DetailDecoupagePK other = (DetailDecoupagePK) obj;
        if (!Objects.equals(this.codeDecoupage, other.codeDecoupage)) {
            return false;
        }
        if (!Objects.equals(this.codart, other.codart)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.stock.domain.DetailDecoupagePK[ codart=" + codart + ", codeDecoupage=" + codeDecoupage + " ]";
    }

    public Integer getUniteOrigine() {
        return uniteOrigine;
    }

    public void setUniteOrigine(Integer uniteOrigine) {
        this.uniteOrigine = uniteOrigine;
    }

}
