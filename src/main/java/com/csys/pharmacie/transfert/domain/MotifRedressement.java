package com.csys.pharmacie.transfert.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The persistent class for the Motif_Redressement database table.
 *
 */
@Table(name = "Motif_Redressement")
@Entity
public class MotifRedressement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Id")
    private Integer id;

    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "Description")
    private String description;
    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "description_sec")
    private String descriptionSec;
//    
//    @OneToMany(mappedBy = "motifRedressement")
//    List<FactureBE> factureBEs;

    public MotifRedressement() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDescriptionSec() {
        return descriptionSec;
    }

    public void setDescriptionSec(String descriptionSec) {
        this.descriptionSec = descriptionSec;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
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
        final MotifRedressement other = (MotifRedressement) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
