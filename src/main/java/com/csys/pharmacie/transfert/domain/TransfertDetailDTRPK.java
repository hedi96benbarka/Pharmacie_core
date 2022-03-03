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
 * @author bassatine
 */
@Embeddable
public class TransfertDetailDTRPK implements Serializable {
     @Basic(optional = false)
    @NotNull
    @Column(name = "code_detailDTR")
    private Integer codedetailDTR;
     @Basic(optional = false)
    @NotNull
    @Column(name = "code_transfert")
    private String codeTransfert ;    

    public Integer getCodedetailDTR() {
        return codedetailDTR;
    }

    public void setCodedetailDTR(Integer codedetailDTR) {
        this.codedetailDTR = codedetailDTR;
    }

    public String getCodeTransfert() {
        return codeTransfert;
    }

    public void setCodeTransfert(String codeTransfert) {
        this.codeTransfert = codeTransfert;
    }

    public TransfertDetailDTRPK() {
    }

    public TransfertDetailDTRPK(Integer codedetailDTR, String codeTransfert) {
        this.codedetailDTR = codedetailDTR;
        this.codeTransfert = codeTransfert;
    }

    @Override
    public String toString() {
        return "TransfertDetailDTRPK{" + "codedetailDTR=" + codedetailDTR + ", codeTransfert=" + codeTransfert + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.codedetailDTR);
        hash = 59 * hash + Objects.hashCode(this.codeTransfert);
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
        final TransfertDetailDTRPK other = (TransfertDetailDTRPK) obj;
        if (!Objects.equals(this.codeTransfert, other.codeTransfert)) {
            return false;
        }
        if (!Objects.equals(this.codedetailDTR, other.codedetailDTR)) {
            return false;
        }
        return true;
    }

    public TransfertDetailDTRPK(Integer codedetailDTR) {
        this.codedetailDTR = codedetailDTR;
    }
 
    
}
