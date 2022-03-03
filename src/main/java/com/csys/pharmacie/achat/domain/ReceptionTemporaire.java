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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 *
 * @author Administrateur
 */
@Entity
@NamedEntityGraphs({
    @NamedEntityGraph(name = "ReceptionTemporaire.reception",
            attributeNodes = {
                @NamedAttributeNode("factureBA")
            })

})
@Table(name = "reception_temporaire")
@Audited
@AuditTable("reception_temporaire_AUD")
public class ReceptionTemporaire extends BaseBon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codfrs")
    private String codfrs;
    @Column(name = "mntbon")
    private BigDecimal mntbon;
    @Size(max = 140)
    @Column(name = "memop")
    private String memo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Reffrs")
    private String referenceFournisseur;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatRefFrs")
    private LocalDateTime dateFrs;
    @Column(name = "coddep")
    private Integer codeDepot;
    @Size(max = 50)
    @Column(name = "CodAnnul")
    private String userAnnule;
    @Column(name = "DatAnnul")

    private LocalDateTime datAnnul;

    @Column(name = "code_piece_jointe")
    private BigInteger codePieceJointe;
    @Column(name = "frs_exonere")
    private Boolean frsExonere;
    @Column(name = "date_debut_exoneration")

    private LocalDateTime dateDebutExoneration;
    @Column(name = "date_fin_exoneration")

    private LocalDateTime dateFinExoneration;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "receivingNumaffiche")
    private String receivingNumaffiche;

    @Column(name = "max_delai_paiement")
    private Integer maxDelaiPaiement;
    @Column(name = "code_receiving")
    private Integer codeReceiving;
    @Column(name = "isTemporaire")
    private boolean isTemporaire = true;
    @Column(name = "isValidated")
    private boolean isValidated = false;

    @NotAudited
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reception", orphanRemoval = true)
    private List<DetailReceptionTemporaire> detailReceptionTempraire;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reception", orphanRemoval = true)
    private List<BaseTvaReceptionTemporaire> baseTvaReceptionList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receptionTemporaire", orphanRemoval = true)
    private List<ReceptionTemporaireDetailCa> recivedDetailTempraireCA;

    @NotAudited
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    
    @JoinColumn(name = "reception" , referencedColumnName = "numbon")
    private FactureBA factureBA;

    public ReceptionTemporaire() {
    }

    public ReceptionTemporaire(String numbon, String typbon, String numaffiche, Date datbon, String codfrs, String codvend, String reffrs, LocalDateTime datRefFrs) {
        super();
        this.codfrs = codfrs;
        this.referenceFournisseur = reffrs;
        this.dateFrs = datRefFrs;
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
        return memo;
    }

    public void setMemop(String memop) {
        this.memo = memop;
    }

    public String getReffrs() {
        return referenceFournisseur;
    }

    public void setReffrs(String reffrs) {
        this.referenceFournisseur = reffrs;
    }

    public LocalDateTime getDatRefFrs() {
        return dateFrs;
    }

    public void setDatRefFrs(LocalDateTime datRefFrs) {
        this.dateFrs = datRefFrs;
    }

    public Integer getCoddep() {
        return codeDepot;
    }

    public void setCoddep(Integer coddep) {
        this.codeDepot = coddep;
    }

    public String getCodAnnul() {
        return userAnnule;
    }

    public void setCodAnnul(String codAnnul) {
        this.userAnnule = codAnnul;
    }

    public LocalDateTime getDatAnnul() {
        return datAnnul;
    }

    public void setDatAnnul(LocalDateTime datAnnul) {
        this.datAnnul = datAnnul;
    }

    public BigInteger getCodePieceJointe() {
        return codePieceJointe;
    }

    public void setCodePieceJointe(BigInteger codePieceJointe) {
        this.codePieceJointe = codePieceJointe;
    }

    public Boolean getFrsExonere() {
        return frsExonere;
    }

    public void setFrsExonere(Boolean frsExonere) {
        this.frsExonere = frsExonere;
    }

    public LocalDateTime getDateDebutExoneration() {
        return dateDebutExoneration;
    }

    public void setDateDebutExoneration(LocalDateTime dateDebutExoneration) {
        this.dateDebutExoneration = dateDebutExoneration;
    }

    public LocalDateTime getDateFinExoneration() {
        return dateFinExoneration;
    }

    public void setDateFinExoneration(LocalDateTime dateFinExoneration) {
        this.dateFinExoneration = dateFinExoneration;
    }

    public List<DetailReceptionTemporaire> getDetailReceptionTempraire() {
        return detailReceptionTempraire;
    }

    public void setDetailReceptionTempraire(List<DetailReceptionTemporaire> detailReceptionTempraire) {
        this.detailReceptionTempraire = detailReceptionTempraire;
    }

    public Integer getcodeReceiving() {
        return codeReceiving;
    }

    public void setcodeReceiving(Integer codeReceiving) {
        this.codeReceiving = codeReceiving;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getReferenceFournisseur() {
        return referenceFournisseur;
    }

    public void setReferenceFournisseur(String referenceFournisseur) {
        this.referenceFournisseur = referenceFournisseur;
    }

    public LocalDateTime getDateReferenceFournisseur() {
        return dateFrs;
    }

    public void setDateReferenceFournisseur(LocalDateTime dateReferenceFournisseur) {
        this.dateFrs = dateReferenceFournisseur;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    


    public List<BaseTvaReceptionTemporaire> getBaseTvaReceptionList() {
        return baseTvaReceptionList;
    }

    public void setBaseTvaReceptionList(List<BaseTvaReceptionTemporaire> baseTvaReceptionList) {
        this.baseTvaReceptionList = baseTvaReceptionList;
    }

    public Integer getMaxDelaiPaiement() {
        return maxDelaiPaiement;
    }

    public void setMaxDelaiPaiement(Integer maxDelaiPaiement) {
        this.maxDelaiPaiement = maxDelaiPaiement;
    }

    public List<ReceptionTemporaireDetailCa> getRecivedDetailTempraireCA() {
        return recivedDetailTempraireCA;
    }

    public void setRecivedDetailTempraireCA(List<ReceptionTemporaireDetailCa> recivedDetailTempraireCA) {
        this.recivedDetailTempraireCA = recivedDetailTempraireCA;
    }

    public FactureBA getFactureBA() {
        return factureBA;
    }

    public void setFactureBA(FactureBA factureBA) {
        this.factureBA = factureBA;
    }

    public boolean getIsTemporaire() {
        return isTemporaire;
    }

    public void setIsTemporaire(boolean isTemporaire) {
        this.isTemporaire = isTemporaire;
    }

    public String getReceivingNumaffiche() {
        return receivingNumaffiche;
    }

    public void setReceivingNumaffiche(String receivingNumaffiche) {
        this.receivingNumaffiche = receivingNumaffiche;
    }

    public boolean isIsValidated() {
        return isValidated;
    }

    public void setIsValidated(boolean isValidated) {
        this.isValidated = isValidated;
    }

    public void calcul(List<TvaDTO> listTVA) {

        List<BaseTvaReceptionTemporaire> listeBaseTVATemporaire = new ArrayList<>();
        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVA = detailReceptionTempraire.stream()
                .filter(eltMvtstoBA -> eltMvtstoBA.getPriuni().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getPriuni()
                                .multiply((BigDecimal.valueOf(100).subtract(art.getRemise())).divide(BigDecimal.valueOf(100))
                                        .setScale(7, RoundingMode.HALF_UP))
                                .multiply(art.getQuantite()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add)
                ));

        Map<Pair<Integer, BigDecimal>, BigDecimal> baseGratuite = detailReceptionTempraire
                .stream()
                .filter(eltMvtstoBA -> eltMvtstoBA.getPriuni().compareTo(BigDecimal.ZERO) == 0 && eltMvtstoBA.getBaseTva().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getBaseTva()
                                .multiply(art.getQuantite()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add)));

        BigDecimal montantHT = BigDecimal.ZERO;
        BigDecimal montantTva = BigDecimal.ZERO;
        BigDecimal mntTVaGratuite = BigDecimal.ZERO;

        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseTVA.entrySet()) {
            BaseTvaReceptionTemporaire base = new BaseTvaReceptionTemporaire();
            base.setBaseTva(entry.getValue());
            base.setCodeTva(entry.getKey().getFirst());
            base.setTauxTva(entry.getKey().getSecond());
            base.setMontantTva(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
            BigDecimal baseTvaGratuite = baseGratuite.getOrDefault(entry.getKey(), BigDecimal.ZERO);

            base.setBaseTvaGratuite(baseTvaGratuite);
            base.setMntTvaGrtauite(baseTvaGratuite.multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
            base.setReception(this);
            listeBaseTVATemporaire.add(base);

            montantHT = montantHT.add(entry.getValue().setScale(7, RoundingMode.HALF_UP));
            montantTva = montantTva.add(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
            mntTVaGratuite = mntTVaGratuite.add(base.getMntTvaGrtauite());
        }

        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseGratuite.entrySet()) {
            if (baseTVA.get(entry.getKey()) == null) {
                BaseTvaReceptionTemporaire base = new BaseTvaReceptionTemporaire();
                base.setBaseTvaGratuite(entry.getValue());
                base.setMntTvaGrtauite(base.getBaseTvaGratuite().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(entry.getKey().getFirst());
                base.setTauxTva(entry.getKey().getSecond());
                base.setMontantTva(BigDecimal.ZERO);
                base.setReception(this);
                listeBaseTVATemporaire.add(base);

                mntTVaGratuite = mntTVaGratuite.add(base.getMntTvaGrtauite());

            }

        }
        for (TvaDTO tva : listTVA) {
            if (!listeBaseTVATemporaire.stream().anyMatch(t -> new Integer(t.getCodeTva()).equals(tva.getCode()))) {

                BaseTvaReceptionTemporaire base = new BaseTvaReceptionTemporaire();
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(tva.getCode());
                base.setTauxTva(tva.getValeur());
                base.setMontantTva(BigDecimal.ZERO);
                base.setBaseTvaGratuite(BigDecimal.ZERO);
                base.setMntTvaGrtauite(BigDecimal.ZERO);
                base.setReception(this);
                listeBaseTVATemporaire.add(base);
            }

        }
        if (this.getBaseTvaReceptionList() != null) {
            this.getBaseTvaReceptionList().clear();
            this.getBaseTvaReceptionList().addAll(listeBaseTVATemporaire);
        } else {
            this.setBaseTvaReceptionList(listeBaseTVATemporaire);
        }

        this.mntbon = montantHT.add(montantTva);
        this.mntbon = this.mntbon.add(mntTVaGratuite).setScale(2, RoundingMode.HALF_UP);

    }

}
