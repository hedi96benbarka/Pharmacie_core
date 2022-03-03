/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author bassatine
 */
@Embeddable
public class DetailMvtStoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "codemvt")
    private Integer codemvt;
    @Basic(optional = false)
//    @NotNull
    @Column(name = "code_depsto")
    private Integer codeDepsto;

    public Integer getCodemvt() {
        return codemvt;
    }

    public void setCodemvt(Integer codemvt) {
        this.codemvt = codemvt;
    }

    public Integer getCodeDepsto() {
        return codeDepsto;
    }

    public void setCodeDepsto(Integer codeDepsto) {
        this.codeDepsto = codeDepsto;
    }

    public DetailMvtStoPK(Integer codemvt, Integer codeDepsto) {
        this.codemvt = codemvt;
        this.codeDepsto = codeDepsto;
    }

    public DetailMvtStoPK() {
    }

}
