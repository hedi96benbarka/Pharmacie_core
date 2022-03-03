/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import com.csys.pharmacie.helper.BaseBon;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "decoupage")
public class Decoupage extends BaseBon implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotNull
    @Column(name = "coddep")
    private Integer coddep;

    @Column(name = "memo")
    private String memo;

    @Basic(optional = false)
    @NotNull
    @Column(name = "auto")
    private Boolean auto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "decoupage")
    private List<DetailDecoupage> detailDecoupageList;

    public Decoupage() {
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public List<DetailDecoupage> getDetailDecoupageList() {
        return detailDecoupageList;
    }

    public void setDetailDecoupageList(List<DetailDecoupage> detailDecoupageList) {
        this.detailDecoupageList = detailDecoupageList;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getNumbon() != null ? getNumbon().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Decoupage)) {
            return false;
        }
        Decoupage other = (Decoupage) object;
        if ((this.getNumbon() == null && other.getNumbon() != null) || (this.getNumbon() != null && !this.getNumbon().equals(other.getNumbon()))) {
            return false;
        }
        return true;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "Decoupage{ code=" + super.getNumbon() + ", coddep=" + coddep + ", memo=" + memo + ", detailDecoupageList=" + detailDecoupageList + ", auto=" + auto + '}';
    }

}
