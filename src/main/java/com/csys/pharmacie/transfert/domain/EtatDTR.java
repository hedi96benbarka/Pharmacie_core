/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.domain;

import com.csys.pharmacie.helper.TransferOrderState;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author bassatine
 */
@Entity
@Table(name = "Etat_DTR")
public class EtatDTR {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codeDTR")
    private Integer codedtr;

    @Column(name = "etat")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TransferOrderState etat;

    public Integer getCodedtr() {
        return codedtr;
    }

    public void setCodedtr(Integer codedtr) {
        this.codedtr = codedtr;
    }

    public TransferOrderState getEtat() {
        return etat;
    }

    public void setEtat(TransferOrderState etat) {
        this.etat = etat;
    }

    public EtatDTR() {
    }

    public EtatDTR(Integer codedtr, TransferOrderState etat) {
        this.codedtr = codedtr;
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "EtatDTR{" + "codedtr=" + codedtr + ", etat=" + etat + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.codedtr);
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
        final EtatDTR other = (EtatDTR) obj;
        if (!Objects.equals(this.codedtr, other.codedtr)) {
            return false;
        }
        return true;
    }

}
