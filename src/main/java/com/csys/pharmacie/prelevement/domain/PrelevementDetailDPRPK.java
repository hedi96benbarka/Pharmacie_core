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
public class PrelevementDetailDPRPK implements Serializable {

  
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_detailDPR")
    private Integer codedetailDPR;
     @Basic(optional = false)
    @NotNull
    @Column(name = "code_prelevement")
    private String codePrelevment ;    

    public PrelevementDetailDPRPK() {
    }


    public Integer getCodedetailDPR() {
        return codedetailDPR;
    }

    public void setCodedetailDPR(Integer codedetailDPR) {
        this.codedetailDPR = codedetailDPR;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.codedetailDPR);
        hash = 37 * hash + Objects.hashCode(this.codePrelevment);
        return hash;
    }

    public PrelevementDetailDPRPK(Integer codedetailDPR, String codePrelevment) {
        this.codedetailDPR = codedetailDPR;
        this.codePrelevment = codePrelevment;
    }

    public PrelevementDetailDPRPK(Integer codedetailDPR) {
        this.codedetailDPR = codedetailDPR;
    }


   

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrelevementDetailDPRPK)) {
            return false;
        }
        PrelevementDetailDPRPK other = (PrelevementDetailDPRPK) object;
       
        if (this.codedetailDPR != other.codedetailDPR) {
            return false;
        }
        return true;
    }

    

    public String getCodePrelevment() {
        return codePrelevment;
    }

    public void setCodePrelevment(String codePrelevment) {
        this.codePrelevment = codePrelevment;
    }

    @Override
    public String toString() {
        return "PrelevementDetailDPRPK{" + "codedetailDPR=" + codedetailDPR + ", codePrelevment=" + codePrelevment + '}';
    }

    
}
