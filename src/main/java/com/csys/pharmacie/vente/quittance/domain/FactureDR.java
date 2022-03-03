/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.SatisfactionEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "factureDR")
@NamedEntityGraph(name = "factureDR.allNodes",
        attributeNodes = {
            @NamedAttributeNode("factures")})
@Audited
@AuditTable("factureDR_AUD")
public class FactureDR implements Serializable {

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
    private LocalDateTime datbon;
    @Column(name = "datesys")
    private LocalDateTime datesys;
    @Column(name = "heuresys")
    private LocalTime heuresys;

//    @Column(name = "etat")
//    private Boolean etat;
//    @Column(name = "dat_Inv")
//    private LocalDateTime datInv;
//    @Column(name = "etat_dep")
//    private Boolean etatDep;
//    @Column(name = "dat_Inv_dep")
//    private LocalDateTime datInvDep;
    @Enumerated(EnumType.STRING)
    @Column(name = "typbon")
    private TypeBonEnum typbon;
    @Size(max = 16)
    @Column(name = "numaffiche")
    private String numaffiche;

    @Enumerated(EnumType.STRING)
    @Column(name = "categ_depot")
    private CategorieDepotEnum categDepot;
    @Size(max = 8)
    @Column(name = "numpiece", length = 8)
    private String numpiece;
    @Column(name = "datepiece")
    private LocalDateTime datepiece;
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "Satisf")
    private SatisfactionEnum satisf;

    @Column(name = "mntbon", precision = 18, scale = 3)
    private BigDecimal mntbon;
    @Size(max = 140)
    @Column(name = "memop", length = 140)
    private String memop;
    @Size(max = 12)
    @Column(name = "numdoss", length = 8)
    private String numdoss;
    @Basic(optional = false)
    @Size(min = 0, max = 30)
    @Column(name = "CodAnnul", nullable = true, length = 30)
    private String codAnnul;
    @Column(name = "DatAnnul")
    private LocalDateTime datAnnul;
    @Size(max = 10)
    @Column(name = "numfacbl")
    private String numfacbl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "etatbon")
    private Boolean etatbon;
    @Size(max = 100)
    @Column(name = "raisoc")
    private String raisoc;
    @Column(name = "NumCha")
    private String numCha;

    @Column(name = "coddep")
    private Integer coddep;
    @Basic(optional = false)
    @Column(name = "imprimer")
    private boolean imprimer;
//    @Column(name = "medecin")
//    private Integer medecin;

    @Column(name = "reffrs")
    private String reffrs;
    @Column(name = "datreffrs")
    private LocalDateTime datreffrs;

    @Size(max = 1)
    @Column(name = "sexe")
    private String sexe;
    @Column(name = "dateNaissance")
    private LocalDate dateNaissance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureDR")
    private List<MvtstoDR> detailFactureCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureDR", fetch = FetchType.EAGER)
    private List<Facture> factures;

    @PrePersist
    public void prePersist() {
        this.datesys = LocalDateTime.now();
        this.heuresys = LocalTime.now();
        this.datbon = LocalDateTime.now();
        this.codvend = SecurityContextHolder.getContext().getAuthentication().getName();
        this.numaffiche = this.numbon.substring(2);
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

    public LocalDateTime getDatesys() {
        return datesys;
    }

    public void setDatesys(LocalDateTime datesys) {
        this.datesys = datesys;
    }

    public LocalTime getHeuresys() {
        return heuresys;
    }

    public void setHeuresys(LocalTime heuresys) {
        this.heuresys = heuresys;
    }

//    public Boolean getEtat() {
//        return etat;
//    }
//
//    public void setEtat(Boolean etat) {
//        this.etat = etat;
//    }
//    public LocalDateTime getDatInv() {
//        return datInv;
//    }
//
//    public void setDatInv(LocalDateTime datInv) {
//        this.datInv = datInv;
//    }
//    public Boolean getEtatDep() {
//        return etatDep;
//    }
//
//    public void setEtatDep(Boolean etatDep) {
//        this.etatDep = etatDep;
//    }
//    public LocalDateTime getDatInvDep() {
//        return datInvDep;
//    }
//
//    public void setDatInvDep(LocalDateTime datInvDep) {
//        this.datInvDep = datInvDep;
//    }
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

//    public CategorieDepotEnum getCategDepot() {
//        return categDepot;
//    }
//
//    public void setCategDepot(CategorieDepotEnum categDepot) {
//        this.categDepot = categDepot;
//    }
    public String getNumpiece() {
        return numpiece;
    }

    public void setNumpiece(String numpiece) {
        this.numpiece = numpiece;
    }

    public LocalDateTime getDatepiece() {
        return datepiece;
    }

    public void setDatepiece(LocalDateTime datepiece) {
        this.datepiece = datepiece;
    }

    public SatisfactionEnum getSatisf() {
        return satisf;
    }

    public void setSatisf(SatisfactionEnum satisf) {
        this.satisf = satisf;
    }

    public BigDecimal getMntbon() {
        return mntbon;
    }

    public void setMntbon(BigDecimal mntbon) {
        this.mntbon = mntbon;
    }

    public String getMemop() {
        return memop;
    }

    public void setMemop(String memop) {
        this.memop = memop;
    }

    public String getNumdoss() {
        return numdoss;
    }

    public void setNumdoss(String numdoss) {
        this.numdoss = numdoss;
    }

    public String getCodAnnul() {
        return codAnnul;
    }

    public void setCodAnnul(String codAnnul) {
        this.codAnnul = codAnnul;
    }

    public LocalDateTime getDatAnnul() {
        return datAnnul;
    }

    public void setDatAnnul(LocalDateTime datAnnul) {
        this.datAnnul = datAnnul;
    }

    public String getNumfacbl() {
        return numfacbl;
    }

    public void setNumfacbl(String numfacbl) {
        this.numfacbl = numfacbl;
    }

    public void setEtatbon(Boolean etatbon) {
        this.etatbon = etatbon;
    }

    public Boolean getEtatbon() {
        return etatbon;
    }

    public String getRaisoc() {
        return raisoc;
    }

    public void setRaisoc(String raisoc) {
        this.raisoc = raisoc;
    }

    public String getNumCha() {
        return numCha;
    }

    public void setNumCha(String numCha) {
        this.numCha = numCha;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public boolean isImprimer() {
        return imprimer;
    }

    public void setImprimer(boolean imprimer) {
        this.imprimer = imprimer;
    }

    public String getReffrs() {
        return reffrs;
    }

    public void setReffrs(String reffrs) {
        this.reffrs = reffrs;
    }

    public LocalDateTime getDatreffrs() {
        return datreffrs;
    }

    public void setDatreffrs(LocalDateTime datreffrs) {
        this.datreffrs = datreffrs;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

//    public Integer getMedecin() {
//        return medecin;
//    }
//
//    public void setMedecin(Integer medecin) {
//        this.medecin = medecin;
//    }
    public List<MvtstoDR> getDetailFactureCollection() {
        return detailFactureCollection;
    }

    public void setDetailFactureCollection(List<MvtstoDR> detailFactureCollection) {
        this.detailFactureCollection = detailFactureCollection;
    }

    public List<Facture> getFactures() {
        return factures;
    }

    public void setFactures(List<Facture> factures) {
        this.factures = factures;
    }

    @Override
    public String toString() {
        return "FactureDR{" + "detailFactureCollection=" + detailFactureCollection + '}';
    }

}
