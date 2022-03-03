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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "ACCESS_MENU_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccessMenuUser.findAll", query = "SELECT a FROM AccessMenuUser a"),
    @NamedQuery(name = "AccessMenuUser.findByModule", query = "SELECT a FROM AccessMenuUser a WHERE a.accessMenuUserPK.module = :module"),
    @NamedQuery(name = "AccessMenuUser.findByUser", query = "SELECT a FROM AccessMenuUser a WHERE a.accessMenuUserPK.user = :user"),
    @NamedQuery(name = "AccessMenuUser.findByMenu", query = "SELECT a FROM AccessMenuUser a WHERE a.accessMenuUserPK.menu = :menu"),
    @NamedQuery(name = "AccessMenuUser.findByVisible", query = "SELECT a FROM AccessMenuUser a WHERE a.visible = :visible")})
public class AccessMenuUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AccessMenuUserPK accessMenuUserPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VISIBLE")
    private boolean visible;

    public AccessMenuUser() {
    }

    public AccessMenuUser(AccessMenuUserPK accessMenuUserPK) {
        this.accessMenuUserPK = accessMenuUserPK;
    }

    public AccessMenuUser(AccessMenuUserPK accessMenuUserPK, boolean visible) {
        this.accessMenuUserPK = accessMenuUserPK;
        this.visible = visible;
    }

    public AccessMenuUser(String module, String user, String menu) {
        this.accessMenuUserPK = new AccessMenuUserPK(module, user, menu);
    }

    public AccessMenuUserPK getAccessMenuUserPK() {
        return accessMenuUserPK;
    }

    public void setAccessMenuUserPK(AccessMenuUserPK accessMenuUserPK) {
        this.accessMenuUserPK = accessMenuUserPK;
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
        hash += (accessMenuUserPK != null ? accessMenuUserPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccessMenuUser)) {
            return false;
        }
        AccessMenuUser other = (AccessMenuUser) object;
        if ((this.accessMenuUserPK == null && other.accessMenuUserPK != null) || (this.accessMenuUserPK != null && !this.accessMenuUserPK.equals(other.accessMenuUserPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.securite.entity.AccessMenuUser[ accessMenuUserPK=" + accessMenuUserPK + " ]";
    }
    
}
