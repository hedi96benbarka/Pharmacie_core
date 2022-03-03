/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import javax.validation.constraints.NotNull;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Hamdi
 */
@Entity
@Table(name = "MotifPR")
public class Motif implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Column(name = "idMotif")
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "designation")
    private String designation;

    @Size(max = 50)
    @Column(name = "designation_sec")
    private String designation_sec;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "motif", orphanRemoval = true)
    private Collection<FacturePR> facturePR;

    public Motif(Integer id, String designation, String designation_sec, Collection<FacturePR> facturePR) {
        this.id = id;
        this.designation = designation;
        this.designation_sec = designation_sec;
        this.facturePR = facturePR;
    }

    public Motif() {
    }

    public Motif(Integer id, String designation) {
        this.id = id;
        this.designation = designation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignation_sec() {
        return designation_sec;
    }

    public void setDesignation_sec(String designation_sec) {
        this.designation_sec = designation_sec;
    }

    public Collection<FacturePR> getFacturePR() {
        return facturePR;
    }

    public void setFacturePR(Collection<FacturePR> facturePR) {
        this.facturePR = facturePR;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Motif)) {
            return false;
        }
        Motif other = (Motif) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.prelevement.domain.Motif{" + "id=" + id + ", designation=" + designation + ", designation_sec=" + designation_sec + ", facturePR=" + facturePR + '}';
    }

}
