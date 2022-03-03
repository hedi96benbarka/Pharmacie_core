/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "etat_commande_achat")
@NamedQueries({@NamedQuery(name = "EtatCommandeAchat.findAll", query = "SELECT e FROM EtatCommandeAchat e")})
public class EtatCommandeAchat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id @Basic(optional = false) @NotNull @Column(name = "commande_achat")
    private Integer commandeAchat;
    @Basic(optional = false) @NotNull @Size(min = 1, max = 10) @Column(name = "etat_commande_achat")
    private String etatCommandeAchat;

    public EtatCommandeAchat() {
    }

    public EtatCommandeAchat(Integer commandeAchat) {
        this.commandeAchat = commandeAchat;
    }

    public EtatCommandeAchat(Integer commandeAchat, String etatCommandeAchat) {
        this.commandeAchat = commandeAchat;
        this.etatCommandeAchat = etatCommandeAchat;
    }

    public Integer getCommandeAchat() {
        return commandeAchat;
    }

    public void setCommandeAchat(Integer commandeAchat) {
        this.commandeAchat = commandeAchat;
    }

    public String getEtatCommandeAchat() {
        return etatCommandeAchat;
    }

    public void setEtatCommandeAchat(String etatCommandeAchat) {
        this.etatCommandeAchat = etatCommandeAchat;
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
        if (!(object instanceof EtatCommandeAchat)) {
            return false;
        }
        EtatCommandeAchat other = (EtatCommandeAchat) object;
        if ((this.commandeAchat == null && other.commandeAchat != null) || (this.commandeAchat != null && !this.commandeAchat.equals(other.commandeAchat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.EtatCommandeAchat[ commandeAchat=" + commandeAchat + " ]";
    }
    
}
