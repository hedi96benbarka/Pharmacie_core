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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lenovo
 */
@Entity
@Table(name = "MenuP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MenuP.findAll", query = "SELECT m FROM MenuP m"),
    @NamedQuery(name = "MenuP.findByModule", query = "SELECT m FROM MenuP m WHERE m.menuPPK.module = :module"),
    @NamedQuery(name = "MenuP.findByCodMnP", query = "SELECT m FROM MenuP m WHERE m.menuPPK.codMnP = :codMnP"),
    @NamedQuery(name = "MenuP.findByDesMenuP", query = "SELECT m FROM MenuP m WHERE m.desMenuP = :desMenuP"),
    @NamedQuery(name = "MenuP.findByMnName", query = "SELECT m FROM MenuP m WHERE m.mnName = :mnName")})
public class MenuP implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MenuPPK menuPPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    @Column(name = "DesMenuP")
    private String desMenuP;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 120)
    @Column(name = "MnName")
    private String mnName;

    public MenuP() {
    }

    public MenuP(MenuPPK menuPPK) {
        this.menuPPK = menuPPK;
    }

    public MenuP(MenuPPK menuPPK, String desMenuP, String mnName) {
        this.menuPPK = menuPPK;
        this.desMenuP = desMenuP;
        this.mnName = mnName;
    }

    public MenuP(String module, String codMnP) {
        this.menuPPK = new MenuPPK(module, codMnP);
    }

    public MenuPPK getMenuPPK() {
        return menuPPK;
    }

    public void setMenuPPK(MenuPPK menuPPK) {
        this.menuPPK = menuPPK;
    }

    public String getDesMenuP() {
        return desMenuP;
    }

    public void setDesMenuP(String desMenuP) {
        this.desMenuP = desMenuP;
    }

    public String getMnName() {
        return mnName;
    }

    public void setMnName(String mnName) {
        this.mnName = mnName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (menuPPK != null ? menuPPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MenuP)) {
            return false;
        }
        MenuP other = (MenuP) object;
        if ((this.menuPPK == null && other.menuPPK != null) || (this.menuPPK != null && !this.menuPPK.equals(other.menuPPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.securite.entity.MenuP[ menuPPK=" + menuPPK + " ]";
    }
    
}
