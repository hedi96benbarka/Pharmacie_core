/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "Ecart")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ecart.findAll", query = "SELECT e FROM Ecart e"),
    @NamedQuery(name = "Ecart.findByCoddep", query = "SELECT e FROM Ecart e WHERE e.coddep = :coddep"),
    @NamedQuery(name = "Ecart.findByCodart", query = "SELECT e FROM Ecart e WHERE e.codart = :codart"),
    @NamedQuery(name = "Ecart.findByStkdep", query = "SELECT e FROM Ecart e WHERE e.stkdep = :stkdep"),
    @NamedQuery(name = "Ecart.findByDatinv", query = "SELECT e FROM Ecart e WHERE e.datinv = :datinv"),
    @NamedQuery(name = "Ecart.findByQteinv", query = "SELECT e FROM Ecart e WHERE e.qteinv = :qteinv"),
    @NamedQuery(name = "Ecart.findByNum", query = "SELECT e FROM Ecart e WHERE e.num = :num")})
public class Ecart implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "coddep")
    private String coddep;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "codart")
    private String codart;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "stkdep")
    private BigDecimal stkdep;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datinv")
   
    private LocalDate datinv;
    @Column(name = "qteinv")
    private BigDecimal qteinv;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Num")
    private Long num;

    public Ecart() {
    }

    public Ecart(Long num) {
        this.num = num;
    }

    public Ecart(Long num, String coddep, String codart, LocalDate datinv) {
        this.num = num;
        this.coddep = coddep;
        this.codart = codart;
        this.datinv = datinv;
    }

    public String getCoddep() {
        return coddep;
    }

    public void setCoddep(String coddep) {
        this.coddep = coddep;
    }

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    public BigDecimal getStkdep() {
        return stkdep;
    }

    public void setStkdep(BigDecimal stkdep) {
        this.stkdep = stkdep;
    }

    public LocalDate getDatinv() {
        return datinv;
    }

    public void setDatinv(LocalDate datinv) {
        this.datinv = datinv;
    }

    public BigDecimal getQteinv() {
        return qteinv;
    }

    public void setQteinv(BigDecimal qteinv) {
        this.qteinv = qteinv;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (num != null ? num.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ecart)) {
            return false;
        }
        Ecart other = (Ecart) object;
        if ((this.num == null && other.num != null) || (this.num != null && !this.num.equals(other.num))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "helper.Ecart[ num=" + num + " ]";
    }

}
