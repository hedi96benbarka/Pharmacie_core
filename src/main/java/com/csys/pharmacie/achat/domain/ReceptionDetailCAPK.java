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
import javax.validation.constraints.Size;

/**
 *
 * @author Farouk
 */
@Embeddable
public class ReceptionDetailCAPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "reception")
    private String reception;
    @Basic(optional = false)
    @NotNull 
    @Column(name = "commande_achat")
    private Integer commandeAchat;
    @Basic(optional = false) 
    @NotNull
    @Column(name = "article")
    private Integer article;

    public ReceptionDetailCAPK() {
    }

    public ReceptionDetailCAPK(String reception, Integer commandeAchat, Integer article) {
        this.reception = reception;
        this.commandeAchat = commandeAchat;
        this.article = article;

    }

    public String getReception() {
        return reception;
    }

    public void setReception(String reception) {
        this.reception = reception;
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
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.reception);
        hash = 53 * hash + Objects.hashCode(this.commandeAchat);
        hash = 53 * hash + Objects.hashCode(this.article);
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
        final ReceptionDetailCAPK other = (ReceptionDetailCAPK) obj;
        if (!Objects.equals(this.reception, other.reception)) {
            return false;
        }
        if (!Objects.equals(this.commandeAchat, other.commandeAchat)) {
            return false;
        }
        if (!Objects.equals(this.article, other.article)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReceptionDetailCAPK{" + "reception=" + reception + ", commandeAchat=" + commandeAchat + ", article=" + article + '}';
    }

}
