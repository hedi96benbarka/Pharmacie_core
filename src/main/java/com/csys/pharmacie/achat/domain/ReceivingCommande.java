/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "receiving_commande")
@NamedQueries({
    @NamedQuery(name = "ReceivingCommande.findAll", query = "SELECT r FROM ReceivingCommande r")})
public class ReceivingCommande implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReceivingCommandePK receivingCommandePK;
    @MapsId("reciveing")
    @JoinColumn(name = "reciveing", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private Receiving receiving;

    public ReceivingCommande() {
    }

    public ReceivingCommande(ReceivingCommandePK receivingCommandePK) {
        this.receivingCommandePK = receivingCommandePK;
    }

    public ReceivingCommande(Integer reciveing, Integer commandeParamAchat) {
        this.receivingCommandePK = new ReceivingCommandePK(reciveing, commandeParamAchat);
    }

    public ReceivingCommandePK getReceivingCommandePK() {
        return receivingCommandePK;
    }

    public void setReceivingCommandePK(ReceivingCommandePK receivingCommandePK) {
        this.receivingCommandePK = receivingCommandePK;
    }

    public Receiving getReceiving() {
        return receiving;
    }

    public void setReceiving(Receiving receiving) {
        this.receiving = receiving;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (receivingCommandePK != null ? receivingCommandePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReceivingCommande)) {
            return false;
        }
        ReceivingCommande other = (ReceivingCommande) object;
        if ((this.receivingCommandePK == null && other.receivingCommandePK != null) || (this.receivingCommandePK != null && !this.receivingCommandePK.equals(other.receivingCommandePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.ReceivingCommande[ receivingCommandePK=" + receivingCommandePK + " ]";
    }

}
