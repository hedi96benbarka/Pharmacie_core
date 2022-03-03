/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.PurchaseOrderReceptionState;
import java.io.Serializable;
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
 * @author Farouk
 */
@Entity
@Table(name = "etat_reception_ca")
public class EtatReceptionCA implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull 
    @Column(name = "commande_achat")
    private Integer commandeAchat;
    @Column(name = "etat_reception")
    @NotNull
    @Enumerated(EnumType.STRING)
    private PurchaseOrderReceptionState etatReception;

    public EtatReceptionCA() {
    }

    public EtatReceptionCA(Integer commandeAchat, PurchaseOrderReceptionState etatReception) {
        this.commandeAchat = commandeAchat;
        this.etatReception = etatReception;
    }

    public Integer getCommandeAchat() {
        return commandeAchat;
    }

    public void setCommandeAchat(Integer commandeAchat) {
        this.commandeAchat = commandeAchat;
    }

    public PurchaseOrderReceptionState getEtatReception() {
        return etatReception;
    }

    public void setEtatReception(PurchaseOrderReceptionState etatReception) {
        this.etatReception = etatReception;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (commandeAchat != null ? commandeAchat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EtatReceptionCA)) {
            return false;
        }
        EtatReceptionCA other = (EtatReceptionCA) object;
        if ((this.commandeAchat == null && other.commandeAchat != null) || (this.commandeAchat != null && !this.commandeAchat.equals(other.commandeAchat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EtatReceptionCA{" + "commandeAchat=" + commandeAchat + ", etatReception=" + etatReception + '}';
    }

    

}
