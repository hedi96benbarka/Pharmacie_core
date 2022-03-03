/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.BaseTva;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "base_tva_retour_perime")
public class BaseTvaRetourPerime extends BaseTva implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "retour_perime", referencedColumnName = "numbon")
    private RetourPerime retour;

    public RetourPerime getRetour() {
        return retour;
    }

    public void setRetour(RetourPerime retour) {
        this.retour = retour;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.getCode());
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
        final BaseTva other = (BaseTva) obj;
        if (!Objects.equals(this.getCode(), other.getCode())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BaseTva{" + "code=" + getCode() + ", numbon=" + retour.getNumbon() + ", codeTva=" + getCodeTva() + '}';
    }

}
