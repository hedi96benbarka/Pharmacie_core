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
public class ReceivingCommandePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "reciveing", nullable = false)
    private Integer reciveing;
    @Basic(optional = false)
    @NotNull
    @Column(name = "commande_param_achat", nullable = false)
    private Integer commandeParamAchat;

    public ReceivingCommandePK() {
    }

    public ReceivingCommandePK(Integer reciveing, Integer commandeParamAchat) {
        this.reciveing = reciveing;
        this.commandeParamAchat = commandeParamAchat;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.reciveing);
        hash = 23 * hash + Objects.hashCode(this.commandeParamAchat);
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
        final ReceivingCommandePK other = (ReceivingCommandePK) obj;
        if (!Objects.equals(this.reciveing, other.reciveing)) {
            return false;
        }
        if (!Objects.equals(this.commandeParamAchat, other.commandeParamAchat)) {
            return false;
        }
        return true;
    }

    public Integer getReciveing() {
        return reciveing;
    }

    public void setReciveing(Integer reciveing) {
        this.reciveing = reciveing;
    }

    public Integer getCommandeParamAchat() {
        return commandeParamAchat;
    }

    public void setCommandeParamAchat(Integer commandeParamAchat) {
        this.commandeParamAchat = commandeParamAchat;
    }


   

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.ReceivingCommandePK[ reciveing=" + reciveing + ", commandeParamAchat=" + commandeParamAchat + " ]";
    }
    
}
