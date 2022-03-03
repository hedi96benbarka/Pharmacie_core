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

/**
 *
 * @author Administrateur
 */
@Embeddable
public class DepstoDetailDecoupagePK implements Serializable {


    @Basic(optional = false)
    @NotNull
    @Column(name = "code_depsto")
    private Integer codeDepsto;

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_detail_decoupage"  )
    private Integer codeDetailDecoupage;

    public DepstoDetailDecoupagePK() {
    }

    public Integer getCodeDepsto() {
        return codeDepsto;
    }

    public void setCodeDepsto(Integer codeDepsto) {
        this.codeDepsto = codeDepsto;
    }

    public Integer getCodeDetailDecoupage() {
        return codeDetailDecoupage;
    }

    public void setCodeDetailDecoupage(Integer codeDetailDecoupage) {
        this.codeDetailDecoupage = codeDetailDecoupage;
    }

    public DepstoDetailDecoupagePK(Integer codeDepsto, Integer codeDetailDecoupage) {
        this.codeDepsto = codeDepsto;
        this.codeDetailDecoupage = codeDetailDecoupage;
    }

    @Override
    public String toString() {
        return "DepstoDetailDecoupagePK{" + "codeDepsto=" + codeDepsto + ", codeDetailDecoupage=" + codeDetailDecoupage + '}';
    }

    public DepstoDetailDecoupagePK(Integer codeDepsto) {
        this.codeDepsto = codeDepsto;
    }

    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.codeDepsto);
        hash = 37 * hash + Objects.hashCode(this.codeDetailDecoupage);
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
        final DepstoDetailDecoupagePK other = (DepstoDetailDecoupagePK) obj;
        if (!Objects.equals(this.codeDepsto, other.codeDepsto)) {
            return false;
        }
        if (!Objects.equals(this.codeDetailDecoupage, other.codeDetailDecoupage)) {
            return false;
        }
        return true;
    }

}
