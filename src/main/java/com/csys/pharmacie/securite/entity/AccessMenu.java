/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.securite.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "ACCESS_MENU")
@NamedQueries({
    @NamedQuery(name = "AccessMenu.findAll", query = "SELECT a FROM AccessMenu a"),
    @NamedQuery(name = "AccessMenu.findByModule", query = "SELECT a FROM AccessMenu a WHERE a.accessMenuPK.module = :module"),
    @NamedQuery(name = "AccessMenu.findByGrp", query = "SELECT a FROM AccessMenu a WHERE a.accessMenuPK.grp = :grp"),
    @NamedQuery(name = "AccessMenu.findByMenu", query = "SELECT a FROM AccessMenu a WHERE a.accessMenuPK.menu = :menu"),
    @NamedQuery(name = "AccessMenu.findByVisible", query = "SELECT a FROM AccessMenu a WHERE a.visible = :visible")})
public class AccessMenu implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AccessMenuPK accessMenuPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VISIBLE")
    private boolean visible;

    public AccessMenu() {
    }

    public AccessMenu(AccessMenuPK accessMenuPK) {
        this.accessMenuPK = accessMenuPK;
    }

    public AccessMenu(AccessMenuPK accessMenuPK, boolean visible) {
        this.accessMenuPK = accessMenuPK;
        this.visible = visible;
    }

    public AccessMenu(String module, String grp, String menu) {
        this.accessMenuPK = new AccessMenuPK(module, grp, menu);
    }

    public AccessMenuPK getAccessMenuPK() {
        return accessMenuPK;
    }

    public void setAccessMenuPK(AccessMenuPK accessMenuPK) {
        this.accessMenuPK = accessMenuPK;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accessMenuPK != null ? accessMenuPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccessMenu)) {
            return false;
        }
        AccessMenu other = (AccessMenu) object;
        if ((this.accessMenuPK == null && other.accessMenuPK != null) || (this.accessMenuPK != null && !this.accessMenuPK.equals(other.accessMenuPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.securite.entity.AccessMenu[ accessMenuPK=" + accessMenuPK + " ]";
    }
    
}
