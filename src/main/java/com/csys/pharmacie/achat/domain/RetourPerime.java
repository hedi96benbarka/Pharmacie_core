/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.helper.BaseBon;
import com.mysema.commons.lang.Pair;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author bassatine
 */
@Entity
@Table(name = "retour_perime")
public class RetourPerime extends BaseBon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "montant_fac", precision = 18, scale = 3)
    private BigDecimal mntbon;
    @Column(name = "remarque", length = 140)
    private String memop;
    @Column(name = "coddep")
    private Integer coddep;
    @Size(max = 10)
   
    @Column(name = "codFRS", length = 10)
    private String codFrs;
    @Size(max = 20)
    @Column(name = "codAnnul", length = 20)
    private String codAnnul;
    @Column(name = "datAnnul")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datAnnul;
  
    @JoinColumn(name = "idMotif", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MotifRetour motifRetour;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numbon")
    private List<MvtstoRetourPerime> detailFactureRPCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "retour", orphanRemoval = true)
    private List<BaseTvaRetourPerime> baseTvaRetourPerime;

    @Column(name = "reffrs")
    private String refFrs;
    @Column(name = "datReffrs")
    private LocalDate datRefFrs;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "retourPerime")
    private Collection<AjustementRetourPerime> ajustementRetourPerime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "num_facture_retour_perime")
    private FactureRetourPerime factureRetourPerime;
    
    public RetourPerime() {
    }

    public RetourPerime(List<MvtstoRetourPerime> detailFactureRPCollection) {
        this.detailFactureRPCollection = detailFactureRPCollection;
    }
    
    public List<BaseTvaRetourPerime> getBaseTvaRetourPerime() {
        return baseTvaRetourPerime;
    }

    public void setBaseTvaRetourPerime(List<BaseTvaRetourPerime> baseTvaRetourPerime) {
        this.baseTvaRetourPerime = baseTvaRetourPerime;
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

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public String getCodAnnul() {
        return codAnnul;
    }

    public void setCodAnnul(String codAnnul) {
        this.codAnnul = codAnnul;
    }

    public Date getDatAnnul() {
        return datAnnul;
    }

    public void setDatAnnul(Date datAnnul) {
        this.datAnnul = datAnnul;
    }

    public String getCodFrs() {
        return codFrs;
    }

    public void setCodFrs(String codFrs) {
        this.codFrs = codFrs;
    }

    public MotifRetour getMotifRetour() {
        return motifRetour;
    }

    public void setMotifRetour(MotifRetour motifRetour) {
        this.motifRetour = motifRetour;
    }

    public List<MvtstoRetourPerime> getDetailFactureRPCollection() {
        return detailFactureRPCollection;
    }

    public void setDetailFactureRPCollection(List<MvtstoRetourPerime> detailFactureRPCollection) {
        this.detailFactureRPCollection = detailFactureRPCollection;
    }

    public Collection<AjustementRetourPerime> getAjustementRetourPerime() {
        return ajustementRetourPerime;
    }

    public void setAjustementRetourPerime(Collection<AjustementRetourPerime> ajustementRetourPerime) {
        this.ajustementRetourPerime = ajustementRetourPerime;
    }

    public FactureRetourPerime getFactureRetourPerime() {
        return factureRetourPerime;
    }

    public void setFactureRetourPerime(FactureRetourPerime factureRetourPerime) {
        this.factureRetourPerime = factureRetourPerime;
    }

    @Override
    public String toString() {
        return "RetourPerime{" + "mntbon=" + mntbon + ", memop=" + memop + ", coddep=" + coddep + ", codFrs=" + codFrs + ", codAnnul=" + codAnnul + ", datAnnul=" + datAnnul + ", motifRetour=" + motifRetour + ", detailFactureRPCollection=" + detailFactureRPCollection + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final RetourPerime other = (RetourPerime) obj;
        if (!Objects.equals(this.memop, other.memop)) {
            return false;
        }
        if (!Objects.equals(this.codFrs, other.codFrs)) {
            return false;
        }
        if (!Objects.equals(this.codAnnul, other.codAnnul)) {
            return false;
        }
        if (!Objects.equals(this.mntbon, other.mntbon)) {
            return false;
        }
        if (!Objects.equals(this.coddep, other.coddep)) {
            return false;
        }
        if (!Objects.equals(this.datAnnul, other.datAnnul)) {
            return false;
        }
        if (!Objects.equals(this.motifRetour, other.motifRetour)) {
            return false;
        }
        if (!Objects.equals(this.detailFactureRPCollection, other.detailFactureRPCollection)) {
            return false;
        }
        return true;
    }

    public String getRefFrs() {
        return refFrs;
    }

    public void setRefFrs(String refFrs) {
        this.refFrs = refFrs;
    }

    public LocalDate getDatRefFrs() {
        return datRefFrs;
    }

    public void setDatRefFrs(LocalDate datRefFrs) {
        this.datRefFrs = datRefFrs;
    }

    public void calcul(List<TvaDTO> listTVA) {

        List<BaseTvaRetourPerime> listeBaseTVA = new ArrayList<>();
        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVA = detailFactureRPCollection.stream()
                .filter(eltMvtstoBA -> eltMvtstoBA.getPriuni().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getPriuni().multiply(art.getQuantite()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add)
                ));

        BigDecimal montantHT = BigDecimal.ZERO;
        BigDecimal montantTva = BigDecimal.ZERO;
        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseTVA.entrySet()) {
            BaseTvaRetourPerime base = new BaseTvaRetourPerime();
            base.setBaseTva(entry.getValue());
            base.setCodeTva(entry.getKey().getFirst());
            base.setTauxTva(entry.getKey().getSecond());
            base.setMontantTva(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));

            base.setRetour(this);
            listeBaseTVA.add(base);

            montantHT = montantHT.add(entry.getValue().setScale(7, RoundingMode.HALF_UP));
            montantTva = montantTva.add(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));

        }
        mntbon = montantHT.add(montantTva).setScale(2, RoundingMode.HALF_UP);
        for (TvaDTO tva : listTVA) {
            if (!listeBaseTVA.stream().anyMatch(t -> t.getCodeTva().equals(tva.getCode()))) {
                BaseTvaRetourPerime base = new BaseTvaRetourPerime();
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(tva.getCode());
                base.setTauxTva(tva.getValeur());
                base.setMontantTva(BigDecimal.ZERO);
                base.setRetour(this);
                listeBaseTVA.add(base);
            }

        }
        this.baseTvaRetourPerime = listeBaseTVA;
    }

    }
