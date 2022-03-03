/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.helper.BaseBon;
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
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.util.Pair;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Administrateur
 */
@Entity
@NamedEntityGraph(
        name = "FactureDirecte",
        attributeNodes = {
          // @NamedAttributeNode("costCenters")
           @NamedAttributeNode("detailFactureDirecteCollection")

        })

@Table(name = "facture_directe")
public class FactureDirecte extends BaseBon implements Serializable {

    private static final long serialVersionUID = 1L;
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
    @Column(name = "montant")
    private BigDecimal montant;
    @Basic(optional = false)
    @Size(min = 0, max = 300)
    @Column(name = "observation")
    private String observation;
    @Column(name = "date_annule")
    private LocalDateTime dateAnnule;
    @Size(max = 50)
    @Column(name = "user_annule", length = 50)
    private String userAnnule;

    @Column(name = "integrer")
    private Boolean integrer;
    
    @Column(name = "code_devise")
    private String codeDevise;
    
    @Column(name = "taux_devise")
    private BigDecimal tauxDevise;
    
    @Column(name = "montant_en_devise")
    private BigDecimal montantDevise;
       @Column(name = "code_commande_achat")
    private Integer codeCommandeAchat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureDirecte", orphanRemoval = true)
    private Collection<BaseTvaFactureDirecte> baseTvaFactureDirecteCollection;
    @OneToMany(cascade = CascadeType.ALL, /*mappedBy = "numbon"*/mappedBy = "factureDirecte", orphanRemoval = true)
    private Set<DetailFactureDirecte> detailFactureDirecteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureDirecte", orphanRemoval = true)
    private Collection<FactureDirecteCostCenter> costCenters;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureDirecte", orphanRemoval = true)
    private Collection<FactureDirecteModeReglement> factureDirecteModeReglement;

    @Override
    @PrePersist
    public void prePersist() {
        this.setDatesys(LocalDate.now());
        this.setHeuresys(LocalTime.now());
        this.setDatbon(this.getDatbon());
        this.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());
        this.setNumaffiche(this.getNumbon().substring(2));
    }

    @PreUpdate
    public void preUpdate(){
    this.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());

    }
     
    
    public FactureDirecte() {
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

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Collection<BaseTvaFactureDirecte> getBaseTvaFactureDirecteCollection() {
        return baseTvaFactureDirecteCollection;
    }

    public void setBaseTvaFactureDirecteCollection(Collection<BaseTvaFactureDirecte> baseTvaFactureDirecteCollection) {
        this.baseTvaFactureDirecteCollection = baseTvaFactureDirecteCollection;
    }

    public Set<DetailFactureDirecte> getDetailFactureDirecteCollection() {
        return detailFactureDirecteCollection;
    }

    public void setDetailFactureDirecteCollection(Set<DetailFactureDirecte> detailFactureDirecteCollection) {
        this.detailFactureDirecteCollection = detailFactureDirecteCollection;
    }

    public Collection<FactureDirecteCostCenter> getCostCenters() {
        return costCenters;
    }

    public void setCostCenters(Collection<FactureDirecteCostCenter> costCenters) {
        this.costCenters = costCenters;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDateTime getDateAnnule() {
        return dateAnnule;
    }

    public void setDateAnnule(LocalDateTime dateAnnule) {
        this.dateAnnule = dateAnnule;
    }

    public String getUserAnnule() {
        return userAnnule;
    }

    public void setUserAnnule(String userAnnule) {
        this.userAnnule = userAnnule;
    }

    public Boolean getIntegrer() {
        return integrer;
    }

    public void setIntegrer(Boolean integrer) {
        this.integrer = integrer;
    }

    public Collection<FactureDirecteModeReglement> getFactureDirecteModeReglement() {
        return factureDirecteModeReglement;
    }

    public void setFactureDirecteModeReglement(Collection<FactureDirecteModeReglement> factureDirecteModeReglement) {
        this.factureDirecteModeReglement = factureDirecteModeReglement;
    }

    public String getCodeDevise() {
        return codeDevise;
    }

    public void setCodeDevise(String codeDevise) {
        this.codeDevise = codeDevise;
    }

    public BigDecimal getTauxDevise() {
        return tauxDevise;
    }

    public void setTauxDevise(BigDecimal tauxDevise) {
        this.tauxDevise = tauxDevise;
    }

    public BigDecimal getMontantDevise() {
        return montantDevise;
    }

    public void setMontantDevise(BigDecimal montantDevise) {
        this.montantDevise = montantDevise;
    }

    public Integer getCodeCommandeAchat() {
        return codeCommandeAchat;
    }

    public void setCodeCommandeAchat(Integer codeCommandeAchat) {
        this.codeCommandeAchat = codeCommandeAchat;
    }


         
    public void calcul(List<TvaDTO> listTVA) {

        List<BaseTvaFactureDirecte> listeBaseTVA = new ArrayList<>();
        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVA = detailFactureDirecteCollection.stream()
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva()), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getPriuni().multiply(art.getQuantite()), BigDecimal::add)));
        BigDecimal montantHT = BigDecimal.ZERO;
        BigDecimal montantTva = BigDecimal.ZERO;

        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : baseTVA.entrySet()) {
            BaseTvaFactureDirecte base = new BaseTvaFactureDirecte();
            base.setBaseTva(entry.getValue());
            base.setCodeTva(entry.getKey().getFirst());
            base.setTauxTva(entry.getKey().getSecond());
            base.setMontantTva(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
            base.setFactureDirecte(this);
            listeBaseTVA.add(base);
            montantHT = montantHT.add(entry.getValue().setScale(7, RoundingMode.HALF_UP));
            montantTva = montantTva.add(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
        }
        for (TvaDTO tva : listTVA) {
            if (!listeBaseTVA.stream().anyMatch(t -> t.getCodeTva().equals(tva.getCode()))) {
                BaseTvaFactureDirecte base = new BaseTvaFactureDirecte();
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(tva.getCode());
                base.setTauxTva(tva.getValeur());
                base.setMontantTva(BigDecimal.ZERO);
                base.setFactureDirecte(this);
                listeBaseTVA.add(base);
            }
        }
        this.setBaseTvaFactureDirecteCollection(listeBaseTVA);
        this.montant = montantHT.add(montantTva).setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.FactureDirecte[ numbon=" + getNumbon() + " ]";
    }
    }
