/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
@Embeddable
public class ReceptionTemporaireDetailCaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "receptionTemporaire")
    private String receptionTemporaire;
    @Basic(optional = false)
    @NotNull
    @Column(name = "commande_achat")
    private Integer commandeAchat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "article")
    private Integer article;

    public ReceptionTemporaireDetailCaPK() {
    }

    public ReceptionTemporaireDetailCaPK(String receptionTemporaire, int commandeAchat, int article) {
        this.receptionTemporaire = receptionTemporaire;
        this.commandeAchat = commandeAchat;
        this.article = article;
    }

    public String getReceptionTemporaire() {
        return receptionTemporaire;
    }

    public void setReceptionTemporaire(String receptionTemporaire) {
        this.receptionTemporaire = receptionTemporaire;
    }

    public Integer getCommandeAchat() {
        return commandeAchat;
    }

    public void setCommandeAchat(Integer commandeAchat) {
        this.commandeAchat = commandeAchat;
    }

    public Integer getArticle() {
        return article;
    }

    public void setArticle(Integer article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (receptionTemporaire != null ? receptionTemporaire.hashCode() : 0);
        hash += (int) commandeAchat;
        hash += (int) article;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReceptionTemporaireDetailCaPK)) {
            return false;
        }
        ReceptionTemporaireDetailCaPK other = (ReceptionTemporaireDetailCaPK) object;
        if ((this.receptionTemporaire == null && other.receptionTemporaire != null) || (this.receptionTemporaire != null && !this.receptionTemporaire.equals(other.receptionTemporaire))) {
            return false;
        }
        if (this.commandeAchat != other.commandeAchat) {
            return false;
        }
        if (this.article != other.article) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCaPK[ receptionTemporaire=" + receptionTemporaire + ", commandeAchat=" + commandeAchat + ", article=" + article + " ]";
    }
   
}
