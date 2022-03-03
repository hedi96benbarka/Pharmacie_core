/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.BaseBon;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author DELL
 */
@Entity
@Table(name = "facture_bon_reception")
@NamedEntityGraph(name = "FactureBonReception.BonReceptionCollection",
        attributeNodes =
                @NamedAttributeNode(value = "BonReceptionCollection",
                        subgraph = "FactureBA.detailFactureBACollection"),
        subgraphs = @NamedSubgraph(name = "FactureBA.detailFactureBACollection", attributeNodes = @NamedAttributeNode("detailFactureBACollection")))
public class FactureBonReception extends BaseBon implements Serializable {

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

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "montant")
    private BigDecimal montantTTC;

    @Column(name = "remise_exep")
    private BigDecimal remiseExep;

    @Column(name = "date_annule")
    private LocalDateTime dateAnnule;
    @Size(max = 50)
    @Column(name = "user_annule", length = 50)
    private String userAnnule;

    @Column(name = "integrer")
    private Boolean integrer;
    
    @Column(name = "max_delai_paiement")
    private Integer maxDelaiPaiement;

    @Column(name = "code_devise")
    private String codeDevise;
    
    @Column(name = "taux_devise")
    private BigDecimal tauxDevise;
    
    @Column(name = "montant_en_devise")
    private BigDecimal montantDevise;
    
    @OneToMany(mappedBy = "factureBonReception")
    private Set<FactureBA> BonReceptionCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "factureBonReception")
    private List<BaseTvaFactureBonReception> baseTvaFactureBonReceptionCollection;

    @Override
    @PrePersist
    public void prePersist() {
        this.setDatesys(LocalDate.now());
        this.setHeuresys(LocalTime.now());
        this.setDatbon(this.getDatbon());
        this.setCodvend(SecurityContextHolder.getContext().getAuthentication().getName());
        this.setNumaffiche(this.getNumbon().substring(2));
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
        return montantTTC;
    }

    public void setMontant(BigDecimal montant) {
        this.montantTTC = montant;
    }

    public BigDecimal getRemiseExep() {
        return remiseExep;
    }

    public void setRemiseExep(BigDecimal remiseExep) {
        this.remiseExep = remiseExep;
    }

    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public List<BaseTvaFactureBonReception> getBaseTvaFactureBonReceptionCollection() {
        return baseTvaFactureBonReceptionCollection;
    }

    public void setBaseTvaFactureBonReceptionCollection(List<BaseTvaFactureBonReception> baseTvaFactureBonReceptionCollection) {
        this.baseTvaFactureBonReceptionCollection = baseTvaFactureBonReceptionCollection;
    }

    public Set<FactureBA> getBonReceptionCollection() {
        return BonReceptionCollection;
    }

    public void setBonReceptionCollection(Set<FactureBA> BonReceptionCollection) {
        this.BonReceptionCollection = BonReceptionCollection;
    }

    public BigDecimal calcul(FactureBonReception factureBonReception) {
        this.montantTTC = this.baseTvaFactureBonReceptionCollection.stream().map(item -> item.getMontantTva().add(item.getBaseTva()).add(item.getMontantTvaGratuite()))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b)).setScale(2, RoundingMode.HALF_UP);
        return this.montantTTC;
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

    public Integer getMaxDelaiPaiement() {
        return maxDelaiPaiement;
    }

    public void setMaxDelaiPaiement(Integer maxDelaiPaiement) {
        this.maxDelaiPaiement = maxDelaiPaiement;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getNumbon() != null ? getNumbon().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.FactureBonReception[ numbon=" + getNumbon() + " ]";
    }

}