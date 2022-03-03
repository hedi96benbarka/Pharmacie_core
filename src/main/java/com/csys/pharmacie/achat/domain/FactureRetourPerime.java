/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.helper.BaseBon;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.mysema.commons.lang.Pair;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "facture_retour_perime")
public class FactureRetourPerime implements Serializable {

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

    @Basic(optional = false)
    @NotNull
    @Column(name = "montant_ttc")
    private BigDecimal montantTTC;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "code_fournisseur")
    private String codeFournisseur;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_fournisseur")
    private LocalDate dateFournisseur;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "reference_fournisseur")
    private String referenceFournisseur;

    @Size(max = 50)
    @Column(name = "user_annule")
    private String userAnnule;
    @Column(name = "date_annule")
    private LocalDateTime dateAnnule;
    @Basic(optional = false)
    @NotNull
    @Column(name = "integrer")
    private Boolean integrer;

    @OneToMany(mappedBy = "factureRetourPerime")
    private Collection<RetourPerime> RetourPerime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureRetourPerime")
    private Collection<BaseTvaFactureRetourPerime> baseTvaFactureRetourPerime;

    public FactureRetourPerime() {
    }

    @PrePersist
    public void prePersist() {
        this.datesys = LocalDate.now();
        this.heuresys = LocalTime.now();
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

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public LocalDate getDateFournisseur() {
        return dateFournisseur;
    }

    public void setDateFournisseur(LocalDate dateFournisseur) {
        this.dateFournisseur = dateFournisseur;
    }

    public String getReferenceFournisseur() {
        return referenceFournisseur;
    }

    public void setReferenceFournisseur(String referenceFournisseur) {
        this.referenceFournisseur = referenceFournisseur;
    }

    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    public LocalDateTime getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(LocalDateTime dateAnnule) {
        this.dateAnnule = dateAnnule;
    }

    public Boolean getIntegrer() {
        return integrer;
    }

    public void setIntegrer(Boolean integrer) {
        this.integrer = integrer;
    }

    public Collection<BaseTvaFactureRetourPerime> getBaseTvaFactureRetourPerime() {
        return baseTvaFactureRetourPerime;
    }

    public void setBaseTvaFactureRetourPerime(Collection<BaseTvaFactureRetourPerime> baseTvaFactureRetourPerime) {
        this.baseTvaFactureRetourPerime = baseTvaFactureRetourPerime;
    }

    public Collection<RetourPerime> getRetourPerime() {
        return RetourPerime;
    }

    public void setRetourPerime(Collection<RetourPerime> RetourPerime) {
        this.RetourPerime = RetourPerime;
    }

    public void calcul(List<TvaDTO> listTVA) {

        List<BaseTvaFactureRetourPerime> listeBaseTVA = new ArrayList<>();
        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVA = this.baseTvaFactureRetourPerime.stream()
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodeTva(), art.getTauxTva()),
                        Collectors.reducing(BigDecimal.ZERO,
                                art -> art.getBaseTva(), BigDecimal::add)));

        BigDecimal montantHT = BigDecimal.ZERO;
        BigDecimal montantTva = BigDecimal.ZERO;

        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseTVA.entrySet()) {
            BaseTvaFactureRetourPerime base = new BaseTvaFactureRetourPerime();
            base.setBaseTva(entry.getValue());
            base.setCodeTva(entry.getKey().getFirst());
            base.setTauxTva(entry.getKey().getSecond());
            base.setMontantTva(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            base.setFactureRetourPerime(this);
            listeBaseTVA.add(base);
            montantHT = montantHT.add(entry.getValue().setScale(2, RoundingMode.HALF_UP));
            montantTva = montantTva.add(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
        }
        for (TvaDTO tva : listTVA) {
            if (!listeBaseTVA.stream().anyMatch(t -> t.getCodeTva().equals(tva.getCode()))) {
                BaseTvaFactureRetourPerime base = new BaseTvaFactureRetourPerime();
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(tva.getCode());
                base.setTauxTva(tva.getValeur());
                base.setMontantTva(BigDecimal.ZERO);
                base.setFactureRetourPerime(this);
                listeBaseTVA.add(base);
            }
        }
        this.setBaseTvaFactureRetourPerime(listeBaseTVA);
        this.montantTTC = montantHT.add(montantTva).setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getNumbon() != null ? getNumbon().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.FactureRetourPerime[ numbon=" + getNumbon() + " ]";
    }

}
