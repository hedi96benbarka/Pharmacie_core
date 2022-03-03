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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
@Table(name = "avoir_fournisseur")

public class AvoirFournisseur extends BaseBon implements Serializable {

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

    @Basic(optional = false)
    @NotNull
    @Column(name = "coddep")
    private int coddep;

    @Basic(optional = false)
    @NotNull
    @Column(name = "montant")
    private BigDecimal montantTTC;


    @Column(name = "user_annule")
    private String userAnnule;
    @Column(name = "date_annule")

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAnnule;

    @Column(name = "num_facture_bon_recep")
    private String numFactureBonRecep;


    @Column(name = "memo")
    private String memo;

    @Column(name = "integrer")
    private boolean integrer;

    @Column(name = "numbon_retour_company_branch")
    private String numbonRetourCompanyBranch;

    @Column(name = "code_site")
    private Integer codeSite;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numbon")
    private List<MvtstoAF> mvtstoAFList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "avoirFournisseur")
    private List<BaseTvaAvoirFournisseur> baseTvaAvoirFournisseurList;

    public AvoirFournisseur() {
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

    public int getCoddep() {
        return coddep;
    }

    public void setCoddep(int coddep) {
        this.coddep = coddep;
    }

    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    public Date getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(Date dateAnnule) {
        this.dateAnnule = dateAnnule;
    }

    public boolean isIntegrer() {
        return integrer;
    }

    public void setIntegrer(boolean integrer) {
        this.integrer = integrer;
    }

    public String getNumFactureBonRecep() {
        return numFactureBonRecep;
    }

    public void setNumFactureBonRecep(String numFactureBonRecep) {
        this.numFactureBonRecep = numFactureBonRecep;
    }

    public List<MvtstoAF> getMvtstoAFList() {
        return mvtstoAFList;
    }

    public void setMvtstoAFList(List<MvtstoAF> mvtstoAFList) {
        this.mvtstoAFList = mvtstoAFList;
    }

    public List<BaseTvaAvoirFournisseur> getBaseTvaAvoirFournisseurList() {
        return baseTvaAvoirFournisseurList;
    }

    public void setBaseTvaAvoirFournisseurList(List<BaseTvaAvoirFournisseur> baseTvaAvoirFournisseurList) {
        this.baseTvaAvoirFournisseurList = baseTvaAvoirFournisseurList;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getNumbonRetourCompanyBranch() {
        return numbonRetourCompanyBranch;
    }

    public void setNumbonRetourCompanyBranch(String numbonRetourCompanyBranch) {
        this.numbonRetourCompanyBranch = numbonRetourCompanyBranch;
    }

    public Integer getCodeSite() {
        return codeSite;
    }

    public void setCodeSite(Integer codeSite) {
        this.codeSite = codeSite;
    }

    public void calcul(List<TvaDTO> listTVA) {

        List<BaseTvaAvoirFournisseur> listeBaseTVA = new ArrayList();
        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVA = mvtstoAFList.stream()
                .filter(eltMvtstoAF -> eltMvtstoAF.getPriuni().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getPriuni()
                                .multiply((BigDecimal.valueOf(100).subtract(art.getRemise())).divide(BigDecimal.valueOf(100))
                                        .setScale(7, RoundingMode.HALF_UP))
                                .multiply(art.getQuantite()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add)
                ));

        Map<Pair<Integer, BigDecimal>, BigDecimal> baseGratuite = mvtstoAFList
                .stream()
                .filter(eltMvtstoAF -> eltMvtstoAF.getPriuni().compareTo(BigDecimal.ZERO) == 0 && eltMvtstoAF.getBaseTva().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getBaseTva()
                                .multiply(art.getQuantite()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add)));

        BigDecimal montantHT = BigDecimal.ZERO;
        BigDecimal montantTva = BigDecimal.ZERO;
        BigDecimal mntTVaGratuite = BigDecimal.ZERO;

        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseTVA.entrySet()) {
            BaseTvaAvoirFournisseur base = new BaseTvaAvoirFournisseur();
            base.setBaseTva(entry.getValue());
            base.setCodeTva(entry.getKey().getFirst());
            base.setTauxTva(entry.getKey().getSecond());
            base.setMontantTva(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
            BigDecimal baseTvaGratuite = baseGratuite.getOrDefault(entry.getKey(), BigDecimal.ZERO);

            base.setBaseTvaGratuite(baseTvaGratuite);
            base.setMntTvaGrtauite(baseTvaGratuite.multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
            base.setAvoirFournisseur(this);
            listeBaseTVA.add(base);

            montantHT = montantHT.add(entry.getValue().setScale(7, RoundingMode.HALF_UP));
            montantTva = montantTva.add(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
            mntTVaGratuite = mntTVaGratuite.add(base.getMntTvaGrtauite());
        }

        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseGratuite.entrySet()) {
            if (baseTVA.get(entry.getKey()) == null) {
                BaseTvaAvoirFournisseur base = new BaseTvaAvoirFournisseur();
                base.setBaseTvaGratuite(entry.getValue());
                base.setMntTvaGrtauite(base.getBaseTvaGratuite().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(entry.getKey().getFirst());
                base.setTauxTva(entry.getKey().getSecond());
                base.setMontantTva(BigDecimal.ZERO);
                base.setAvoirFournisseur(this);
                listeBaseTVA.add(base);

                mntTVaGratuite = mntTVaGratuite.add(base.getMntTvaGrtauite());

            }

        }

        for (TvaDTO tva : listTVA) {
            if (!listeBaseTVA.stream().anyMatch(t -> t.getCodeTva().equals(tva.getCode()))) { 
                BaseTvaAvoirFournisseur base = new BaseTvaAvoirFournisseur();
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(tva.getCode());
                base.setTauxTva(tva.getValeur());
                base.setMontantTva(BigDecimal.ZERO);
                base.setBaseTvaGratuite(BigDecimal.ZERO);
                base.setMntTvaGrtauite(BigDecimal.ZERO);
                base.setAvoirFournisseur(this);
                listeBaseTVA.add(base);
            }

        }
        if (this.getBaseTvaAvoirFournisseurList() != null) {
            this.getBaseTvaAvoirFournisseurList().clear();
            this.getBaseTvaAvoirFournisseurList().addAll(listeBaseTVA);
        } else {
            this.setBaseTvaAvoirFournisseurList(listeBaseTVA);
        }

        this.montantTTC = montantHT.add(montantTva);
        this.montantTTC = this.montantTTC.add(mntTVaGratuite).setScale(2, RoundingMode.HALF_UP);

    }

}
