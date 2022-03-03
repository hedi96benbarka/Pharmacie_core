/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.achat.dto.TvaDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.csys.pharmacie.helper.BaseBon;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import com.mysema.commons.lang.Pair;
import java.math.RoundingMode;
import java.util.Date;
import java.util.logging.Logger;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 *
 * @author farouk
 */
@Entity
@NamedEntityGraphs({
    @NamedEntityGraph(name = "FactureBA.receiving",
            attributeNodes = {
                @NamedAttributeNode("receiving")
            }),
    @NamedEntityGraph(name = "FactureBA.detailFactureBACollection",
            attributeNodes = {
                @NamedAttributeNode("detailFactureBACollection")
            })

})
@Audited
@Table(name = "FactureBA")
@AuditTable("FactureBA_AUD")
//@EntityListeners(AuditingEntityListener.class)
public class FactureBA extends BaseBon implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(FactureBA.class.getName());

    @Column(name = "coddep")
    private Integer coddep;

//    @Column(name = "Stup")
//    private Boolean stup;
    @Size(max = 50)
    @Column(name = "numpiece")
    private String numpiece;
    @Column(name = "datepiece")
    private LocalDateTime datepiece;
    @Column(name = "codfrs")
    private String codfrs;
    @Column(name = "mntbon")
    private BigDecimal mntbon;
    @Size(max = 140)
    @Column(name = "memop")
    private String memop;
    @Size(max = 50)
    @Column(name = "Reffrs")
    private String refFrs;
    @Column(name = "DatRefFrs")
    private LocalDate datRefFrs;

    @Basic(optional = false)
    @NotNull
    @Column(name = "valrem")
    private BigDecimal valrem;
//    @Basic(optional = false)
//    @Size(min = 1, max = 3)
//    @Column(name = "Satisf")
//    private String satisf;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "CodAnnul")
    private String codAnnul;
    @Column(name = "DatAnnul")
    private LocalDateTime datAnnul;
    @Column(name = "raisoc")
    private String raisoc;

    @Column(name = "Automatique")
    private Boolean automatique;

    @Column(name = "frs_exonere")
    private Boolean fournisseurExonere;

    @Column(name = "date_debut_exoneration")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateDebutExoneration;

    @Column(name = "date_fin_exoneration")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateFinExenoration;
    @NotAudited
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "reciveing")
    private Receiving receiving;

    @Column(name = "max_delai_paiement")
    private Integer maxDelaiPaiement;

    @Column(name = "integrer")
    private Boolean integrer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reception", orphanRemoval = true)
    @JsonBackReference
    private List<ReceptionDetailCA> recivedDetailCA;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureBA", orphanRemoval = true)
    @JsonBackReference
    private List<MvtStoBA> detailFactureBACollection;
    @NotAudited
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureBA", orphanRemoval = true)
    private List<BaseTvaReception> baseTvaReceptionList;
    @NotAudited
    @OneToMany(mappedBy = "reception", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PieceJointeReception> piecesJointes;
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "num_facture_bon_recep")
    private FactureBonReception factureBonReception;
    @Transient
    List<Integer> listBC;

    @Transient
    boolean used;

    @Transient
    boolean returned;
    @Column(name = "num_affiche_reception_temporaire")
    private String numAfficheRecetionTemporaire;
    @Column(name = "numbon_origin")
    private String numbonOrigin;
    @Column(name = "code_site")
    private Integer codeSite;

    @Column(name = "numbon_retour_company_branch")
    private String numbonRetourCompanyBranch;

    public void calcul(List<TvaDTO> listTVA) {

        List<BaseTvaReception> listeBaseTVA = new ArrayList<>();
        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVA = detailFactureBACollection.stream()
                .filter(eltMvtstoBA -> eltMvtstoBA.getPriuni().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getPriuni()
                                .multiply((BigDecimal.valueOf(100).subtract(art.getRemise())).divide(BigDecimal.valueOf(100))
                                        .setScale(7, RoundingMode.HALF_UP))
                                .multiply(art.getQuantite()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add)
                ));

        Map<Pair<Integer, BigDecimal>, BigDecimal> baseGratuite = detailFactureBACollection
                .stream()
                .filter(eltMvtstoBA -> eltMvtstoBA.getPriuni().compareTo(BigDecimal.ZERO) == 0 && eltMvtstoBA.getBaseTva().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getBaseTva()
                                .multiply(art.getQuantite()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add)));

        BigDecimal montantHT = BigDecimal.ZERO;
        BigDecimal montantTva = BigDecimal.ZERO;
        BigDecimal mntTVaGratuite = BigDecimal.ZERO;

        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseTVA.entrySet()) {
            BaseTvaReception base = new BaseTvaReception();
            base.setBaseTva(entry.getValue());
            base.setCodeTva(entry.getKey().getFirst());
            base.setTauxTva(entry.getKey().getSecond());
            base.setMontantTva(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
            BigDecimal baseTvaGratuite = baseGratuite.getOrDefault(entry.getKey(), BigDecimal.ZERO);

            base.setBaseTvaGratuite(baseTvaGratuite);
            base.setMontantTvaGratuite(baseTvaGratuite.multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
            base.setFactureBA(this);
            listeBaseTVA.add(base);

            montantHT = montantHT.add(entry.getValue().setScale(7, RoundingMode.HALF_UP));
            montantTva = montantTva.add(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
            mntTVaGratuite = mntTVaGratuite.add(base.getMontantTvaGratuite());
        }

        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseGratuite.entrySet()) {
            if (baseTVA.get(entry.getKey()) == null) {
                BaseTvaReception base = new BaseTvaReception();
                base.setBaseTvaGratuite(entry.getValue());
                base.setMontantTvaGratuite(base.getBaseTvaGratuite().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(entry.getKey().getFirst());
                base.setTauxTva(entry.getKey().getSecond());
                base.setMontantTva(BigDecimal.ZERO);
                base.setFactureBA(this);
                listeBaseTVA.add(base);

                mntTVaGratuite = mntTVaGratuite.add(base.getMontantTvaGratuite());

            }

        }

//        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseGratuite.entrySet()) {
//            Optional<BaseTvaReception> matched = listeBaseTVA.stream().filter(elt -> elt.getCodeTva().equals(entry.getKey().getFirst()) && elt.getTauxTva().equals(entry.getKey().getSecond())).findFirst();
//
//            if (matched.isPresent()) {
//                matched.get().setMontantTvaGratuite(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
//                matched.get().setBaseTvaGratuite(entry.getValue());
//                mntTVaGratuite = mntTVaGratuite.add(matched.get().getMontantTvaGratuite());
//            }
//
//        }
        for (TvaDTO tva : listTVA) {
            if (!listeBaseTVA.stream().anyMatch(t -> t.getCodeTva().equals(tva.getCode()))) {
                BaseTvaReception base = new BaseTvaReception();
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(tva.getCode());
                base.setTauxTva(tva.getValeur());
                base.setMontantTva(BigDecimal.ZERO);
                base.setBaseTvaGratuite(BigDecimal.ZERO);
                base.setMontantTvaGratuite(BigDecimal.ZERO);
                base.setFactureBA(this);
                listeBaseTVA.add(base);
            }

        }
        if (this.getBaseTvaReceptionList() != null) {
            this.getBaseTvaReceptionList().clear();
            this.getBaseTvaReceptionList().addAll(listeBaseTVA);
        } else {
            this.setBaseTvaReceptionList(listeBaseTVA);
        }

        this.mntbon = montantHT.add(montantTva);
        this.mntbon = this.mntbon.add(mntTVaGratuite).setScale(2, RoundingMode.HALF_UP);

    }

    public List<PieceJointeReception> getPiecesJointes() {
        return piecesJointes;
    }

    public void setPiecesJointes(List<PieceJointeReception> piecesJointes) {
        this.piecesJointes = piecesJointes;
    }

    public FactureBonReception getFactureBonReception() {
        return factureBonReception;
    }

    public void setFactureBonReception(FactureBonReception factureBonReception) {
        this.factureBonReception = factureBonReception;
    }

    public List<MvtStoBA> getDetailFactureBACollection() {
        return detailFactureBACollection;
    }

    public void setDetailFactureBACollection(List<MvtStoBA> detailFactureBACollection) {
        this.detailFactureBACollection = detailFactureBACollection;
    }

    public List<ReceptionDetailCA> getRecivedDetailCA() {
        return recivedDetailCA;
    }

    public void setRecivedDetailCA(List<ReceptionDetailCA> recivedDetailCA) {
        this.recivedDetailCA = recivedDetailCA;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public List<Integer> getListBC() {
        return listBC;
    }

    public void setListbc(List<Integer> listbc) {
        this.listBC = listbc;
    }

    public String getRaisoc() {
        return raisoc;
    }

    public void setRaisoc(String raisoc) {
        this.raisoc = raisoc;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }
//
//    public Boolean getStup() {
//        return stup;
//    }
//
//    public void setStup(Boolean stup) {
//        this.stup = stup;
//    }

    public Receiving getReceiving() {
        return receiving;
    }

    public void setReceiving(Receiving receiving) {
        this.receiving = receiving;
    }

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

    public String getCodfrs() {
        return codfrs;
    }

    public void setCodfrs(String codfrs) {
        this.codfrs = codfrs;
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

//    public String getSatisf() {
//        return satisf;
//    }
//
//    public void setSatisf(String satisf) {
//        this.satisf = satisf;
//    }
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

    public BigDecimal getValrem() {
        return valrem;
    }

    public void setValrem(BigDecimal valrem) {
        this.valrem = valrem;
    }

    public Boolean getAutomatique() {
        return automatique;
    }

    public void setAutomatique(Boolean automatique) {
        this.automatique = automatique;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getNumbon() != null ? getNumbon().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactureBA)) {
            return false;
        }
        FactureBA other = (FactureBA) object;
        if ((this.getNumbon() == null && other.getNumbon() != null) || (this.getNumbon() != null && !this.getNumbon().equals(other.getNumbon()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.achat.model.pharmacie.FactureBA[ numbon=" + getNumbon() + " ]";
    }

    public FactureBA() {
    }

    public List<BaseTvaReception> getBaseTvaReceptionList() {
        return baseTvaReceptionList;
    }

    public void setBaseTvaReceptionList(List<BaseTvaReception> baseTvaReceptionList) {
        this.baseTvaReceptionList = baseTvaReceptionList;
    }

    public Boolean getFournisseurExonere() {
        return fournisseurExonere;
    }

    public void setFournisseurExonere(Boolean fournisseurExonere) {
        this.fournisseurExonere = fournisseurExonere;
    }

    public Date getDateDebutExoneration() {
        return dateDebutExoneration;
    }

    public void setDateDebutExoneration(Date dateDebutExoneration) {
        this.dateDebutExoneration = dateDebutExoneration;
    }

    public Date getDateFinExenoration() {
        return dateFinExenoration;
    }

    public void setDateFinExenoration(Date dateFinExenoration) {
        this.dateFinExenoration = dateFinExenoration;
    }

    public Integer getMaxDelaiPaiement() {
        return maxDelaiPaiement;
    }

    public void setMaxDelaiPaiement(Integer maxDelaiPaiement) {
        this.maxDelaiPaiement = maxDelaiPaiement;
    }

    public String getNumAfficheRecetionTemporaire() {
        return numAfficheRecetionTemporaire;
    }

    public void setNumAfficheRecetionTemporaire(String numAfficheRecetionTemporaire) {
        this.numAfficheRecetionTemporaire = numAfficheRecetionTemporaire;
    }

    public Boolean getIntegrer() {
        return integrer;
    }

    public void setIntegrer(Boolean integrer) {
        this.integrer = integrer;
    }

    public String getNumbonOrigin() {
        return numbonOrigin;
    }

    public void setNumbonOrigin(String numbonOrigin) {
        this.numbonOrigin = numbonOrigin;
    }

    public Integer getCodeSite() {
        return codeSite;
    }

    public void setCodeSite(Integer codeSite) {
        this.codeSite = codeSite;
    }

    public String getNumbonRetourCompanyBranch() {
        return numbonRetourCompanyBranch;
    }

    public void setNumbonRetourCompanyBranch(String numbonRetourCompanyBranch) {
        this.numbonRetourCompanyBranch = numbonRetourCompanyBranch;
    }

}
