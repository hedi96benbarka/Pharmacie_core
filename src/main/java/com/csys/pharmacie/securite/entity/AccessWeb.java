/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.securite.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ERRAYHAN
 */
@Entity
@Table(name = "Access_Web")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccessWeb.findAll", query = "SELECT a FROM AccessWeb a"),
    @NamedQuery(name = "AccessWeb.findByUserNameByaction", query = "SELECT a FROM AccessWeb a WHERE a.accessWebPK.userName = :userName and a.accessWebPK.action = :action"),
    @NamedQuery(name = "AccessWeb.findByAction", query = "SELECT a FROM AccessWeb a WHERE a.accessWebPK.action = :action"),
    @NamedQuery(name = "AccessWeb.findByUtil", query = "SELECT a FROM AccessWeb a WHERE a.util = :util"),
    @NamedQuery(name = "AccessWeb.findByDroit", query = "SELECT a FROM AccessWeb a WHERE a.droit = :droit")})
public class AccessWeb implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AccessWebPK accessWebPK;
    @Column(name = "Util")
    private Integer util;
    @Column(name = "Droit")
    private Boolean droit;
  

    
    public AccessWeb() {
    }

    public AccessWeb(AccessWebPK accessWebPK) {
        this.accessWebPK = accessWebPK;
    }

    public AccessWeb(String userName, String action) {
        this.accessWebPK = new AccessWebPK(userName, action);
    }

    public AccessWebPK getAccessWebPK() {
        return accessWebPK;
    }

    public void setAccessWebPK(AccessWebPK accessWebPK) {
        this.accessWebPK = accessWebPK;
    }

    public Integer getUtil() {
        return util;
    }

    public void setUtil(Integer util) {
        this.util = util;
    }

    public Boolean getDroit() {
        return droit;
    }

    public void setDroit(Boolean droit) {
        this.droit = droit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accessWebPK != null ? accessWebPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccessWeb)) {
            return false;
        }
        AccessWeb other = (AccessWeb) object;
        if ((this.accessWebPK == null && other.accessWebPK != null) || (this.accessWebPK != null && !this.accessWebPK.equals(other.accessWebPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.achat.model.pharmacie.AccessWeb[ accessWebPK=" + accessWebPK + " ]";
    }
    
}
