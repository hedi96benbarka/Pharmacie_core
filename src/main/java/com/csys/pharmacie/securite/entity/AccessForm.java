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
@Table(name = "ACCESS_FORM")
@NamedQueries({
    @NamedQuery(name = "AccessForm.findAll", query = "SELECT a FROM AccessForm a"),
    @NamedQuery(name = "AccessForm.findByModule", query = "SELECT a FROM AccessForm a WHERE a.accessFormPK.module = :module"),
    @NamedQuery(name = "AccessForm.findByGrp", query = "SELECT a FROM AccessForm a WHERE a.accessFormPK.grp = :grp"),
    @NamedQuery(name = "AccessForm.findByForm", query = "SELECT a FROM AccessForm a WHERE a.accessFormPK.form = :form"),
    @NamedQuery(name = "AccessForm.findByControl", query = "SELECT a FROM AccessForm a WHERE a.accessFormPK.control = :control"),
    @NamedQuery(name = "AccessForm.findByVisible", query = "SELECT a FROM AccessForm a WHERE a.visible = :visible")})
public class AccessForm implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AccessFormPK accessFormPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Visible")
    private boolean visible;

    public AccessForm() {
    }

    public AccessForm(AccessFormPK accessFormPK) {
        this.accessFormPK = accessFormPK;
    }

    public AccessForm(AccessFormPK accessFormPK, boolean visible) {
        this.accessFormPK = accessFormPK;
        this.visible = visible;
    }

    public AccessForm(String module, String grp, String form, String control) {
        this.accessFormPK = new AccessFormPK(module, grp, form, control);
    }

    public AccessFormPK getAccessFormPK() {
        return accessFormPK;
    }

    public void setAccessFormPK(AccessFormPK accessFormPK) {
        this.accessFormPK = accessFormPK;
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
        hash += (accessFormPK != null ? accessFormPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccessForm)) {
            return false;
        }
        AccessForm other = (AccessForm) object;
        if ((this.accessFormPK == null && other.accessFormPK != null) || (this.accessFormPK != null && !this.accessFormPK.equals(other.accessFormPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.securite.entity.AccessForm[ accessFormPK=" + accessFormPK + " ]";
    }
    
}
