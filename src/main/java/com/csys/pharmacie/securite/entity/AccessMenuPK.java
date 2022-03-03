/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.securite.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Lenovo
 */
@Embeddable
public class AccessMenuPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "Module")
    private String module;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "Grp")
    private String grp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MENU")
    private String menu;

    public AccessMenuPK() {
    }

    public AccessMenuPK(String module, String grp, String menu) {
        this.module = module;
        this.grp = grp;
        this.menu = menu;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getGrp() {
        return grp;
    }

    public void setGrp(String grp) {
        this.grp = grp;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (module != null ? module.hashCode() : 0);
        hash += (grp != null ? grp.hashCode() : 0);
        hash += (menu != null ? menu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccessMenuPK)) {
            return false;
        }
        AccessMenuPK other = (AccessMenuPK) object;
        if ((this.module == null && other.module != null) || (this.module != null && !this.module.equals(other.module))) {
            return false;
        }
        if ((this.grp == null && other.grp != null) || (this.grp != null && !this.grp.equals(other.grp))) {
            return false;
        }
        if ((this.menu == null && other.menu != null) || (this.menu != null && !this.menu.equals(other.menu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.securite.entity.AccessMenuPK[ module=" + module + ", grp=" + grp + ", menu=" + menu + " ]";
    }
    
}
