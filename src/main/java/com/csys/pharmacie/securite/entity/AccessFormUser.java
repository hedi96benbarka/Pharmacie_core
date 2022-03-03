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
@Table(name = "ACCESS_FORM_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccessFormUser.findAll", query = "SELECT a FROM AccessFormUser a"),
    @NamedQuery(name = "AccessFormUser.findByModule", query = "SELECT a FROM AccessFormUser a WHERE a.accessFormUserPK.module = :module"),
    @NamedQuery(name = "AccessFormUser.findByUser", query = "SELECT a FROM AccessFormUser a WHERE a.accessFormUserPK.user = :user"),
    @NamedQuery(name = "AccessFormUser.findByForm", query = "SELECT a FROM AccessFormUser a WHERE a.accessFormUserPK.form = :form"),
    @NamedQuery(name = "AccessFormUser.findByControl", query = "SELECT a FROM AccessFormUser a WHERE a.accessFormUserPK.control = :control"),
    @NamedQuery(name = "AccessFormUser.findByVisible", query = "SELECT a FROM AccessFormUser a WHERE a.visible = :visible")})
public class AccessFormUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AccessFormUserPK accessFormUserPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Visible")
    private boolean visible;

    public AccessFormUser() {
    }

    public AccessFormUser(AccessFormUserPK accessFormUserPK) {
        this.accessFormUserPK = accessFormUserPK;
    }

    public AccessFormUser(AccessFormUserPK accessFormUserPK, boolean visible) {
        this.accessFormUserPK = accessFormUserPK;
        this.visible = visible;
    }

    public AccessFormUser(String module, String user, String form, String control) {
        this.accessFormUserPK = new AccessFormUserPK(module, user, form, control);
    }

    public AccessFormUserPK getAccessFormUserPK() {
        return accessFormUserPK;
    }

    public void setAccessFormUserPK(AccessFormUserPK accessFormUserPK) {
        this.accessFormUserPK = accessFormUserPK;
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
        hash += (accessFormUserPK != null ? accessFormUserPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccessFormUser)) {
            return false;
        }
        AccessFormUser other = (AccessFormUser) object;
        if ((this.accessFormUserPK == null && other.accessFormUserPK != null) || (this.accessFormUserPK != null && !this.accessFormUserPK.equals(other.accessFormUserPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.securite.entity.AccessFormUser[ accessFormUserPK=" + accessFormUserPK + " ]";
    }
    
}
