/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.Audited;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Farouk
 */
@MappedSuperclass
@Audited
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseBon {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "numbon")
    private String numbon;
    @Size(max = 20)
    @Column(name = "codvend")
    private String codvend;
    @Column(name = "datbon")
    protected LocalDateTime datbon;
    @Column(name = "datesys")
    private LocalDate datesys;
    @Column(name = "heuresys")
    private LocalTime heuresys;
    @Enumerated(EnumType.STRING)
    @Column(name = "typbon")
    private TypeBonEnum typbon;
    @Size(max = 16)
    @Column(name = "numaffiche")
    private String numaffiche;

    @Enumerated(EnumType.STRING)
    @Column(name = "categ_depot")
    private CategorieDepotEnum categDepot;

    @PrePersist
    public void prePersist() {
        this.datesys = LocalDate.now();  
        this.heuresys = LocalTime.now();  
        this.datbon = LocalDateTime.now(); 
        this.codvend = SecurityContextHolder.getContext().getAuthentication().getName();
        this.numaffiche = this.numbon.substring(2);
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }
    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
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

    public LocalDate getDatesys() {
        return datesys;
    }

    public void setDatesys(LocalDate datesys) {
        this.datesys = datesys;
    }

    public LocalTime getHeuresys() {
        return heuresys;
    }

    public void setHeuresys(LocalTime heuresys) {
        this.heuresys = heuresys;
    }

    public TypeBonEnum getTypbon() {
        return typbon;
    }

    public void setTypbon(TypeBonEnum typbon) {
        this.typbon = typbon;
    }

    public String getNumaffiche() {
        return numaffiche;
    }

    public void setNumaffiche(String numaffiche) {
        this.numaffiche = numaffiche;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseBon baseBon = (BaseBon) o;
        return numbon.equals(baseBon.numbon) && Objects.equals(codvend, baseBon.codvend) && Objects.equals(datbon, baseBon.datbon) && Objects.equals(datesys, baseBon.datesys) && Objects.equals(heuresys, baseBon.heuresys) && typbon == baseBon.typbon && Objects.equals(numaffiche, baseBon.numaffiche) && categDepot == baseBon.categDepot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numbon, codvend, datbon, datesys, heuresys, typbon, numaffiche, categDepot);
    }
}
