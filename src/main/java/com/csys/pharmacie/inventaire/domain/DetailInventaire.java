/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.domain;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "detail_Inventaire")
@NamedQueries({
    @NamedQuery(name = "DetailInventaire.findAll", query = "SELECT d FROM DetailInventaire d")})
public class DetailInventaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetailInventairePK detailInventairePK;
    @JoinColumn(name = "inventaire", referencedColumnName = "code", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    @MapsId("inventaire")
    private Inventaire inventaire1;

    public DetailInventaire() {
    }

    public DetailInventaire(DetailInventairePK detailInventairePK) {
        this.detailInventairePK = detailInventairePK;
    }
    private static final Logger LOG = Logger.getLogger(DetailInventaire.class.getName());

    public DetailInventaire(DetailInventairePK detailInventairePK, Inventaire inventaire1) {
        this.detailInventairePK = detailInventairePK;
        this.inventaire1 = inventaire1;
    }

    
  
    public DetailInventairePK getDetailInventairePK() {
        return detailInventairePK;
    }

    public void setDetailInventairePK(DetailInventairePK detailInventairePK) {
        this.detailInventairePK = detailInventairePK;
    }

    public Inventaire getInventaire1() {
        return inventaire1;
    }

    public void setInventaire1(Inventaire inventaire1) {
        this.inventaire1 = inventaire1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detailInventairePK != null ? detailInventairePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailInventaire)) {
            return false;
        }
        DetailInventaire other = (DetailInventaire) object;
        if ((this.detailInventairePK == null && other.detailInventairePK != null) || (this.detailInventairePK != null && !this.detailInventairePK.equals(other.detailInventairePK))) {
            return false;
        }
        return true;
    }


   
    
}
