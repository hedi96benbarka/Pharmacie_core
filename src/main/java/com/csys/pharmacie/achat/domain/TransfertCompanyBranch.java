/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Administrateur
 */
@Entity

@NamedEntityGraph(name = "TransfertCompanyBranch.receptionRelative",
        attributeNodes
        = @NamedAttributeNode(value = "receptionRelative", subgraph = "FactureBA.receiving"),
        subgraphs
        = @NamedSubgraph(
                name = "FactureBA.receiving",
                attributeNodes
                = @NamedAttributeNode("receiving")
        )
)
@Table(name = "transfert_company_branch")
@Audited
@AuditTable("transfert_company_branch_AUD")
public class TransfertCompanyBranch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "num_bon")
    private String numBon;
    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_bon")
    private TypeBonEnum typeBon;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "categ_depot")
    private CategorieDepotEnum categDepot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "num_affiche")
    private String numAffiche;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "user_create")
    private String userCreate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_create")
    private LocalDateTime dateCreate;
    @Basic(optional = false)

    @Column(name = "date_create_reception")
    private LocalDateTime dateCreateReception;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "code_fournisseur")
    private String codeFournisseur;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @NotNull
    @Column(name = "montant_ttc")
    private BigDecimal montantTtc;

    @Size(max = 20)
    @Column(name = "numbon_reception")
    private String numbonReception;
    @ManyToOne
    @JoinColumn(name = "numbon_reception", referencedColumnName = "numbon", insertable = false, updatable = false)
    private FactureBA receptionRelative;
    @NotNull
    @Column(name = "code_depot")
    private Integer codeDepot;

    @Column(name = "immobilisation_genere")
    private Boolean immobilisationGenere;

    @Column(name = "replicated")
    private Boolean replicated;

    @Column(name = "integrer")
    private Boolean integrer;

    @Column(name = "Num_Vir")
    private String codeIntegration;

    @Column(name = "numbon_transfert_relatif")
    private String numBonTransfertRelatif;

    @Column(name = "code_site")
    Integer codeSite;

    @Column(name = "returned_to_supplier")
    private Boolean returnedToSupplier;
    
    @Column(name = "onshelf")
    private Boolean onShelf;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transfertCompanyBranch", orphanRemoval = true)
    private Collection<DetailTransfertCompanyBranch> detailTransfertCompanyBranchCollection;

    public TransfertCompanyBranch() {
    }

    public TransfertCompanyBranch(String numBon) {
        this.numBon = numBon;
    }

    public TransfertCompanyBranch(String numBon, TypeBonEnum typeBon, String numAffiche, String userCreate, LocalDateTime dateCreate, String codeFournisseur) {
        this.numBon = numBon;
        this.typeBon = typeBon;
        this.numAffiche = numAffiche;
        this.userCreate = userCreate;
        this.dateCreate = dateCreate;
        this.codeFournisseur = codeFournisseur;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public TypeBonEnum getTypeBon() {
        return typeBon;
    }

    public void setTypeBon(TypeBonEnum typeBon) {
        this.typeBon = typeBon;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getNumAffiche() {
        return numAffiche;
    }

    public void setNumAffiche(String numAffiche) {
        this.numAffiche = numAffiche;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getCodeFournisseur() {
        return codeFournisseur;
    }

    public void setCodeFournisseur(String codeFournisseur) {
        this.codeFournisseur = codeFournisseur;
    }

    public BigDecimal getMontantTtc() {
        return montantTtc;
    }

    public void setMontantTtc(BigDecimal montantTtc) {
        this.montantTtc = montantTtc;
    }

    public String getNumbonReception() {
        return numbonReception;
    }

    public void setNumbonReception(String numbonReception) {
        this.numbonReception = numbonReception;
    }

    public Collection<DetailTransfertCompanyBranch> getDetailTransfertCompanyBranchCollection() {
        return detailTransfertCompanyBranchCollection;
    }

    public void setDetailTransfertCompanyBranchCollection(Collection<DetailTransfertCompanyBranch> detailTransfertCompanyBranchCollection) {
        this.detailTransfertCompanyBranchCollection = detailTransfertCompanyBranchCollection;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public Boolean getImmobilisationGenere() {
        return immobilisationGenere;
    }

    public void setImmobilisationGenere(Boolean immobilisationGenere) {
        this.immobilisationGenere = immobilisationGenere;
    }

    public Boolean getReplicated() {
        return replicated;
    }

    public void setReplicated(Boolean replicated) {
        this.replicated = replicated;
    }

    public Boolean getIntegrer() {
        return integrer;
    }

    public void setIntegrer(Boolean integrer) {
        this.integrer = integrer;
    }

    public String getCodeIntegration() {
        return codeIntegration;
    }

    public void setCodeIntegration(String codeIntegration) {
        this.codeIntegration = codeIntegration;
    }

    public String getNumBonTransfertRelatif() {
        return numBonTransfertRelatif;
    }

    public void setNumBonTransfertRelatif(String numBonTransfertRelatif) {
        this.numBonTransfertRelatif = numBonTransfertRelatif;
    }

    public LocalDateTime getDateCreateReception() {
        return dateCreateReception;
    }

    public void setDateCreateReception(LocalDateTime dateCreateReception) {
        this.dateCreateReception = dateCreateReception;
    }

    public Integer getCodeSite() {
        return codeSite;
    }

    public void setCodeSite(Integer codeSite) {
        this.codeSite = codeSite;
    }

    public FactureBA getReceptionRelative() {
        return receptionRelative;
    }

    public void setReceptionRelative(FactureBA receptionRelative) {
        this.receptionRelative = receptionRelative;
    }

    public Boolean getReturnedToSupplier() {
        return returnedToSupplier;
    }

    public void setReturnedToSupplier(Boolean returnedToSupplier) {
        this.returnedToSupplier = returnedToSupplier;
    }

    public Boolean getOnShelf() {
        return onShelf;
    }

    public void setOnShelf(Boolean onShelf) {
        this.onShelf = onShelf;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numBon != null ? numBon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TransfertCompanyBranch)) {
            return false;
        }
        TransfertCompanyBranch other = (TransfertCompanyBranch) object;
        if ((this.numBon == null && other.numBon != null) || (this.numBon != null && !this.numBon.equals(other.numBon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TransfertCompanyBranch{" + "numBon=" + numBon + ", typeBon=" + typeBon + ", categDepot=" + categDepot + ", numAffiche=" + numAffiche + ", userCreate=" + userCreate + ", dateCreate=" + dateCreate + ", codeFournisseur=" + codeFournisseur + ", montantTtc=" + montantTtc + ", numbonReception=" + numbonReception + ", codeDepot=" + codeDepot + ", immobilisationGenere=" + immobilisationGenere + ", detailTransfertCompanyBranchCollection=" + detailTransfertCompanyBranchCollection + '}';
    }

}
