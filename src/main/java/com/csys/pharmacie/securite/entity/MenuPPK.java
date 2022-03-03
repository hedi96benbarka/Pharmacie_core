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
public class MenuPPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "Module")
    private String module;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "CodMnP")
    private String codMnP;

    public MenuPPK() {
    }

    public MenuPPK(String module, String codMnP) {
        this.module = module;
        this.codMnP = codMnP;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCodMnP() {
        return codMnP;
    }

    public void setCodMnP(String codMnP) {
        this.codMnP = codMnP;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (module != null ? module.hashCode() : 0);
        hash += (codMnP != null ? codMnP.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MenuPPK)) {
            return false;
        }
        MenuPPK other = (MenuPPK) object;
        if ((this.module == null && other.module != null) || (this.module != null && !this.module.equals(other.module))) {
            return false;
        }
        if ((this.codMnP == null && other.codMnP != null) || (this.codMnP != null && !this.codMnP.equals(other.codMnP))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.securite.entity.MenuPPK[ module=" + module + ", codMnP=" + codMnP + " ]";
    }
    
}
