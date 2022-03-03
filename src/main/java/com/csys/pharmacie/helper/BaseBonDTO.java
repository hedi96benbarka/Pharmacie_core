/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Farouk
 * @param <T>
 */
public abstract class BaseBonDTO<T extends BaseDetailBonDTO> {

    private String codvend;

    private List<T> details;
    @NotNull
    private CategorieDepotEnum categDepot;

    private TypeBonEnum typeBon;

    private String typbon;

    private String numbon;

    private String numaffiche;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime datbon;

    public BaseBonDTO() {
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }

    public List<T> getDetails() {
        return details;
    }

    public void setDetails(List<T> details) {
        this.details = details;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public TypeBonEnum getTypeBon() {
        return typeBon;
    }

    public void setTypeBon(TypeBonEnum typeBon) {
        this.typeBon = typeBon;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public LocalDateTime getDatbon() {
        return datbon;
    }

    public void setDatbon(LocalDateTime datbon) {
        this.datbon = datbon;
    }

    public String getTypbon() {
        return typbon;
    }

    public void setTypbon(String typbon) {
        this.typbon = typbon;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final BaseBonDTO<?> other = (BaseBonDTO<?>) obj;
        if (!Objects.equals(this.numbon, other.numbon)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "{" + "codvend=" + codvend + ", details=" + details + ", categDepot=" + categDepot + ", typeBon=" + typeBon + ", typbon=" + typbon + ", numbon=" + numbon + ", numaffiche=" + numaffiche + ", datbon=" + datbon + '}';
    }

}
