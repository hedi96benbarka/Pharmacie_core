/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

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
public class ReceivingDetailsPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "codart", nullable = false)
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "receiving", nullable = false)
    private Integer receiving;

    public ReceivingDetailsPK() {
    }

    public ReceivingDetailsPK(int codart, int receiving) {
        this.codart = codart;
        this.receiving = receiving;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public Integer getReceiving() {
        return receiving;
    }

    public void setReceiving(Integer receiving) {
        this.receiving = receiving;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.codart);
        hash = 79 * hash + Objects.hashCode(this.receiving);
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
        final ReceivingDetailsPK other = (ReceivingDetailsPK) obj;
        if (!Objects.equals(this.codart, other.codart)) {
            return false;
        }
        if (!Objects.equals(this.receiving, other.receiving)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReceivingDetailsPK{" + "codart=" + codart + ", receiving=" + receiving + '}';
    }

  
    
}
