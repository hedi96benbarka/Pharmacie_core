/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Hamdi
 */
@Embeddable
public class DetailMvtStoPRPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "codemvt")
    private Integer codemvt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_depsto")
    private Integer codeDepsto;

    public DetailMvtStoPRPK() {
    }

    public DetailMvtStoPRPK(Integer codemvt, Integer codeDepsto) {
        this.codemvt = codemvt;
        this.codeDepsto = codeDepsto;
    }

    public Integer getCodemvt() {
        return codemvt;
    }

    public void setCodemvt(Integer codemvt) {
        this.codemvt = codemvt;
    }

    public Integer getCodeDepsto() {
        return codeDepsto;
    }

    public void setCodeDepsto(Integer codeDepsto) {
        this.codeDepsto = codeDepsto;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.codemvt);
        hash = 17 * hash + Objects.hashCode(this.codeDepsto);
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
        final DetailMvtStoPRPK other = (DetailMvtStoPRPK) obj;
        if (!Objects.equals(this.codemvt, other.codemvt)) {
            return false;
        }
        if (!Objects.equals(this.codeDepsto, other.codeDepsto)) {
            return false;
        }
        return true;
    }

  

    @Override
    public String toString() {
        return "com.csys.pharmacie.prelevement.domain.DetailMvtStoPRPK[ codemvt=" + codemvt + ", codeDepsto=" + codeDepsto + " ]";
    }
    
}
