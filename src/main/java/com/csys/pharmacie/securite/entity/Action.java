/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.securite.entity;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ERRAYHAN
 */
@Entity
@Table(name = "Action")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Action.findAll", query = "SELECT a FROM Action a"),
    @NamedQuery(name = "Action.findByModule", query = "SELECT a FROM Action a WHERE a.module = :module"),
    @NamedQuery(name = "Action.findByMenu", query = "SELECT a FROM Action a WHERE a.menu = :menu"),
    @NamedQuery(name = "Action.findByAction", query = "SELECT a FROM Action a WHERE a.action = :action"),
    @NamedQuery(name = "Action.findByDesignationAction", query = "SELECT a FROM Action a WHERE a.designationAction = :designationAction"),
    @NamedQuery(name = "Action.findByAutre", query = "SELECT a FROM Action a WHERE a.autre = :autre")})
public class Action implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "Module")
    private String module;
    @Size(max = 20)
    @Column(name = "Menu")
    private String menu;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "Action")
    private String action;
    @Size(max = 50)
    @Column(name = "Designation_Action")
    private String designationAction;
    @Size(max = 50)
    @Column(name = "Autre")
    private String autre;

    public Action() {
    }

    public Action(String action) {
        this.action = action;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDesignationAction() {
        return designationAction;
    }

    public void setDesignationAction(String designationAction) {
        this.designationAction = designationAction;
    }

    public String getAutre() {
        return autre;
    }

    public void setAutre(String autre) {
        this.autre = autre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (action != null ? action.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Action)) {
            return false;
        }
        Action other = (Action) object;
        if ((this.action == null && other.action != null) || (this.action != null && !this.action.equals(other.action))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.achat.model.pharmacie.Action[ action=" + action + " ]";
    }
    
}
