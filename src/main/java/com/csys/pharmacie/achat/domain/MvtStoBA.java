/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.csys.pharmacie.helper.BaseDetailBon;
import java.time.LocalDate;
import java.util.List;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author farouk
 */
@Audited
@Entity
@Table(name = "MvtStoBA")
@AuditTable("MvtStoBA_AUD")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "MvtStoBA.detailMvtStoBACollection",
            attributeNodes = {
                @NamedAttributeNode("detailMvtStoBACollection")
            })
 

})
@NamedEntityGraph(
    name = "MvtStoBA.factureBA", 
    attributeNodes =
            @NamedAttributeNode(value = "factureBA", subgraph = "FactureBA.receiving"), 
    subgraphs = {
                        @NamedSubgraph(
                        name = "FactureBA.receiving",
                        attributeNodes = {
                            @NamedAttributeNode("receiving")
                        }
                )
     })

public class MvtStoBA extends BaseDetailBon implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MvtStoBAPK pk;

    @Basic(optional = false)
    @NotNull
    @Column(name = "coddep")
    private Integer coddep;

    @Column(name = "is_prix_reference")
    private Boolean isPrixReference;

    @Basic(optional = false)
    //    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "Lot_Inter")
    private String lotInter;

    @Size(max = 2)
    @Column(name = "typbon")
    private String typbon;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "priuni")
    private BigDecimal priuni;
    // In case the item is a medication (PH) the selling price must be specified 
    @Basic(optional = false)
    @Column(name = "prix_vente")
    private BigDecimal prixVente;

    @Column(name = "remise")
    private BigDecimal remise;
    @Column(name = "montht")
    private BigDecimal montht;
    @Column(name = "tautva")
    private BigDecimal tautva;

//    @Size(max = 500)
//    @Column(name = "memoart")
//    private String memoart;

    @Column(name = "codtva")
    private Integer codtva;

    @Column(name = "qtecom")
    private BigDecimal qtecom;

    @Column(name = "DatPer")
    private LocalDate datPer;

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_unite")
    private Integer codeUnite;

    @Column(name = "base_tva")
    private BigDecimal baseTva;
    // on va stocker ces deux facteurs necessaire pour le calcul du pmp : oldPMP+oldQuantity
    @Column(name = "pmp_precedant")
    private BigDecimal pmpPrecedent;
    // c est la quantite avant la reception 
    @Column(name = "quantite_precedante")
    private BigDecimal quantitePrecedante;

    @Column(name = "old_tautva")
    private BigDecimal oldTauTva;
    @Column(name = "old_codtva")
    private Integer oldCodTva;

    @Column(name = "code_emplacement")
    private Integer codeEmplacement;
    
    @Column(name = "ancien_prix_achat")
    private BigDecimal ancienPrixAchat;
       
    @Column(name = "is_capitalize")
    private Boolean isCapitalize;
    
     @Column(name = "code_article_fournisseur")
    private String codeArticleFournisseur;
     
    @MapsId("numbon")
    @JoinColumn(name = "numbon", referencedColumnName = "numbon")
    @ManyToOne(optional = false)
    @JsonBackReference
    private FactureBA factureBA;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mvtStoBA")
    private List<DetailMvtStoBA> detailMvtStoBACollection;

    @XmlTransient
    public FactureBA getFactureBA() {
        return factureBA;
    }

    public MvtStoBA() {
    }

    public MvtStoBA(BigDecimal qtecom) {
        this.qtecom = qtecom;
    }

    public MvtStoBA(BigDecimal priuni, BigDecimal quantite) {
        this.priuni = priuni;
        super.setQuantite(quantite);
    }

    public MvtStoBA(MvtStoBAPK pk, BigDecimal priuni, BigDecimal quantite, BigDecimal montht) {
        this.pk = pk;
        this.priuni = priuni;
        super.setQuantite(quantite);
        this.montht = montht;
    }

    public MvtStoBA(BigDecimal priuni, BigDecimal quantite, BigDecimal montht) {
        this.priuni = priuni;
        super.setQuantite(quantite);
        this.montht = montht;
    }

    public MvtStoBA(MvtStoBA mvtStoBA) {
        super(mvtStoBA);
        this.pk = mvtStoBA.pk;
        this.coddep = mvtStoBA.coddep;
        this.typbon = mvtStoBA.typbon;
        this.priuni = mvtStoBA.priuni;
        this.remise = mvtStoBA.remise;
        this.montht = mvtStoBA.montht;
        this.tautva = mvtStoBA.tautva;
        this.codtva = mvtStoBA.codtva;
        this.codeUnite = mvtStoBA.codeUnite;
    }

    public MvtStoBA(MvtStoBAPK mvtStoBAPK) {
        this.pk = mvtStoBAPK;
    }

    public MvtStoBAPK getPk() {
        return pk;
    }

    public void setPk(MvtStoBAPK pk) {
        this.pk = pk;
    }
    public void setFactureBA(FactureBA factureBA) {
        this.factureBA = factureBA;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public Integer getCodart() {
        return pk.getCodart();
    }

    public void setCodart(Integer codart) {
        pk.setCodart(codart);
    }
    public String getTypbon() {
        return typbon;
    }

    public void setTypbon(String typbon) {
        this.typbon = typbon;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getMontht() {
        return montht;
    }

    public void setMontht(BigDecimal montht) {
        this.montht = montht;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

//    public String getMemoart() {
//        return memoart;
//    }
//
//    public void setMemoart(String memoart) {
//        this.memoart = memoart;
//    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public BigDecimal getQtecom() {
        return qtecom;
    }

    public void setQtecom(BigDecimal qtecom) {
        this.qtecom = qtecom;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public Boolean getIsPrixReference() {
        return isPrixReference;
    }

    public void setIsPrixReference(Boolean isPrixReference) {
        this.isPrixReference = isPrixReference;
    }

    public List<DetailMvtStoBA> getDetailMvtStoBACollection() {
        return detailMvtStoBACollection;
    }

    public void setDetailMvtStoBACollection(List<DetailMvtStoBA> detailMvtStoBACollection) {
        this.detailMvtStoBACollection = detailMvtStoBACollection;
    }

    public BigDecimal getBaseTva() {
        return baseTva;
    }

    public void setBaseTva(BigDecimal baseTva) {
        this.baseTva = baseTva;
    }

    public BigDecimal getPmpPrecedent() {
        return pmpPrecedent;
    }

    public void setPmpPrecedent(BigDecimal pmpPrecedent) {
        this.pmpPrecedent = pmpPrecedent;
    }

    public BigDecimal getQuantitePrecedante() {
        return quantitePrecedante;
    }

    public void setQuantitePrecedante(BigDecimal quantitePrecedante) {
        this.quantitePrecedante = quantitePrecedante;
    }

    public BigDecimal getOldTauTva() {
        return oldTauTva;
    }

    public void setOldTauTva(BigDecimal oldTauTva) {
        this.oldTauTva = oldTauTva;
    }

    public Integer getOldCodTva() {
        return oldCodTva;
    }

    public void setOldCodTva(Integer oldCodTva) {
        this.oldCodTva = oldCodTva;
    }

    public Integer getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(Integer codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
    }

 
    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public BigDecimal getAncienPrixAchat() {
        return ancienPrixAchat;
    }

    public void setAncienPrixAchat(BigDecimal ancienPrixAchat) {
        this.ancienPrixAchat = ancienPrixAchat;
}

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
    }

    public String getCodeArticleFournisseur() {
        return codeArticleFournisseur;
    }

    public void setCodeArticleFournisseur(String codeArticleFournisseur) {
        this.codeArticleFournisseur = codeArticleFournisseur;
    }
    
    
   @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.pk);
        return hash;
    }

    @Override
    public String toString() {
        return "MvtStoBA{" + "qtecom=" + qtecom + '}';
    }





}
