/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.domain;

import com.csys.pharmacie.transfert.domain.FactureBT;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "trace_transfert_inventaire")
@NamedQueries({
    @NamedQuery(name = "TraceTransfertInventaire.findAll", query = "SELECT t FROM TraceTransfertInventaire t")})
public class TraceTransfertInventaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Long code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "user_name")
    private String user;
    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "numbon_transfert")
    private String numbon_factureBT;

    @JoinColumn(name = "code_inventaire", referencedColumnName = "code")
    @ManyToOne
    private Inventaire inventaire;

    public TraceTransfertInventaire() {
    }

    public TraceTransfertInventaire(Long code) {
        this.code = code;
    }

    public TraceTransfertInventaire(Long code, String user) {
        this.code = code;
        this.user = user;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getNumbon_factureBT() {
        return numbon_factureBT;
    }

    public void setNumbon_factureBT(String numbon_factureBT) {
        this.numbon_factureBT = numbon_factureBT;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    public void setInventaire(Inventaire inventaire) {
        this.inventaire = inventaire;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "TraceTransfertInventaire{" + "code=" + code + ", numbon_factureBT=" + numbon_factureBT + ", inventaire=" + inventaire + '}';
    }

}
