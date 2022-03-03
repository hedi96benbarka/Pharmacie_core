/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import com.csys.pharmacie.helper.PrelevmentOrderState;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Hamdi
 */
@Entity
@Table(name = "Etat_DPR")
@NamedQueries({
    @NamedQuery(name = "EtatDPR.findAll", query = "SELECT e FROM EtatDPR e")})
public class EtatDPR implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codedpr")
    private Integer codedpr;
   
    @Column(name = "etat")
        @NotNull
    @Enumerated(EnumType.STRING)
    private PrelevmentOrderState etat;

    public EtatDPR() {
    }

    public EtatDPR(Integer codedpr) {
        this.codedpr = codedpr;
    }

    public Integer getCodedpr() {
        return codedpr;
    }

    public void setCodedpr(Integer codedpr) {
        this.codedpr = codedpr;
    }

 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codedpr != null ? codedpr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtatDPR)) {
            return false;
        }
        EtatDPR other = (EtatDPR) object;
        if ((this.codedpr == null && other.codedpr != null) || (this.codedpr != null && !this.codedpr.equals(other.codedpr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.prelevement.domain.EtatDPR[ codedpr=" + codedpr + " ]";
    }

    public EtatDPR(Integer codedpr, PrelevmentOrderState etat) {
        this.codedpr = codedpr;
        this.etat = etat;
    }

    public PrelevmentOrderState getEtat() {
        return etat;
    }

    public void setEtat(PrelevmentOrderState etat) {
        this.etat = etat;
    }

  
}
