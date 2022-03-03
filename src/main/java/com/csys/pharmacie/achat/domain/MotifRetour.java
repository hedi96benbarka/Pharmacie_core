/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author bassatine
 */
@Table(name = "motif_Retour_perime")
@Entity
public class MotifRetour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "description")
    private String description;
    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "description_sec")
    private String descriptionSec;

    @OneToMany(mappedBy = "motifRetour", fetch = FetchType.LAZY)
    List<RetourPerime> factureRetour_perime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionSec() {
        return descriptionSec;
    }

    public void setDescriptionSec(String descriptionSec) {
        this.descriptionSec = descriptionSec;
    }

    public List<RetourPerime> getFactureRetour_perime() {
        return factureRetour_perime;
    }

    public void setFactureRetour_perime(List<RetourPerime> factureRetour_perime) {
        this.factureRetour_perime = factureRetour_perime;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MotifRetour other = (MotifRetour) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public MotifRetour() {
    }

    public MotifRetour(Integer id, String description, String descriptionSec) {
        this.id = id;
        this.description = description;
        this.descriptionSec = descriptionSec;
    }
//    private static final Logger LOG = Logger.getLogger(MotifRetour.class.getName());

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
