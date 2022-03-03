/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.domain;

import com.csys.pharmacie.helper.BaseBon;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "retour_prelevement")

public class RetourPrelevement extends BaseBon implements Serializable {

    @Column(name = "montant_fac")
    private BigDecimal montantFac;
    @Column(name = "mntbon")
    private BigDecimal mntbon;
    @Size(max = 140)
    @Column(name = "remarque")
    private String remarque;
    @Column(name = "coddepart_src")
    private Integer coddepartSrc;
    @Column(name = "coddep_desti")
    private Integer coddepDesti;
    @Column(name = "date_debut")
    private LocalDateTime dateDebut;
    @Column(name = "date_fin")
    private LocalDateTime dateFin;
    @Column(name = "code_cost_center")
    private Integer codeCostCenter;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numbon")
    private List< DetailRetourPrelevement> liseDetailRetourPrelevement;

    public RetourPrelevement() {
    }

    public BigDecimal getMontantFac() {
        return montantFac;
    }

    public void setMontantFac(BigDecimal montantFac) {
        this.montantFac = montantFac;
    }

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Integer getCoddepartSrc() {
        return coddepartSrc;
    }

    public void setCoddepartSrc(Integer coddepartSrc) {
        this.coddepartSrc = coddepartSrc;
    }

    public Integer getCoddepDesti() {
        return coddepDesti;
    }

    public void setCoddepDesti(Integer coddepDesti) {
        this.coddepDesti = coddepDesti;
    }

    public List<DetailRetourPrelevement> getLiseDetailRetourPrelevement() {
        return liseDetailRetourPrelevement;
    }

    public void setLiseDetailRetourPrelevement(List<DetailRetourPrelevement> liseDetailRetourPrelevement) {
        this.liseDetailRetourPrelevement = liseDetailRetourPrelevement;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getCodeCostCenter() {
        return codeCostCenter;
    }

    public void setCodeCostCenter(Integer codeCostCenter) {
        this.codeCostCenter = codeCostCenter;
    }

    @Override
    public String toString() {
        return "RetourPrelevement{numbon = " + getNumbon() + '}';
    }

}
