/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "detail_transfert_company_branch")
@Audited
@AuditTable("detail_transfert_company_branch_AUD")
public class DetailTransfertCompanyBranch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_article")
    private Integer codeArticle;
    @Basic(optional = false)
    @Size(min = 0, max = 50)
    @Column(name = "lot_inter")
    private String lotInter;
    @NotNull
    @Column(name = "date_peremption")
    private LocalDate datePeremption;
    @Size(max = 10)
    @Column(name = "categ_depot")
    private String categDepot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "code_saisi")
    private String codeSaisi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_unite")
    private Integer codeUnite;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "designation_sec")
    private String designationSec;
    @NotNull
    @Size(max = 255)
    @Column(name = "designation")
    private String designation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @NotNull
    @Column(name = "quantite")
    private BigDecimal quantite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prix_unitaire")
    private BigDecimal prixUnitaire;

    @Column(name = "montant_ht")
    private BigDecimal montantHt;
    @NotNull
    @Column(name = "code_tva")
    private Integer codeTva;
    @NotNull
    @Column(name = "taux_tva")
    private BigDecimal tauxTva;

    @Column(name = "base_tva")
    private BigDecimal baseTva;

    @Column(name = "remise")
    private BigDecimal remise;

    @Column(name = "is_capitalize")
    private Boolean isCapitalize;

    @Column(name = "code_emplacement")
    private Integer codeEmplacement;
    
    // on va stocker ces deux facteurs necessaire pour le calcul du pmp : oldPMP+oldQuantity
    @Column(name = "pmp_precedant")
    private BigDecimal pmpPrecedent;

    // c est la quantite avant la reception 
    @Column(name = "quantite_precedante")
    private BigDecimal quantitePrecedante;

    @Column(name = "prix_vente")
    private BigDecimal prixVente;

    @Column(name = "is_reference_price")
    private Boolean isReferencePrice;

    @Column(name = "quantite_restante")
    private BigDecimal quantiteRestante;

    @Column(name = "ancien_prix_achat")
    private BigDecimal ancienPrixAchat;

    @JoinColumn(name = "num_bon", referencedColumnName = "num_bon")
    @ManyToOne(optional = false)
    private TransfertCompanyBranch transfertCompanyBranch;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codeDetailTransfert")
    private List<TraceDetailTransfertCompanyBranch> listeTraceDetailTransfertCompanyBranch;

    public DetailTransfertCompanyBranch() {
    }

    public DetailTransfertCompanyBranch(Integer code) {
        this.code = code;
    }

    public DetailTransfertCompanyBranch(BigDecimal quantite, BigDecimal prixUnitaire) {
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public DetailTransfertCompanyBranch(Integer code, Integer codeArticle, String codeSaisi, Integer codeUnite, String designationSec, BigDecimal prixUnitaire) {
        this.code = code;
        this.codeArticle = codeArticle;
        this.codeSaisi = codeSaisi;
        this.codeUnite = codeUnite;
        this.designationSec = designationSec;
        this.prixUnitaire = prixUnitaire;
    }

    public DetailTransfertCompanyBranch(BigDecimal quantite, BigDecimal prixUnitaire, BigDecimal montantHt) {
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.montantHt = montantHt;
    }

    public Integer getCode() {
        return code;
    }

    public DetailTransfertCompanyBranch(DetailTransfertCompanyBranch detailTransfertCompanyBranch) {
        this.categDepot = detailTransfertCompanyBranch.categDepot;
        this.designation = detailTransfertCompanyBranch.designation;
        this.designationSec = detailTransfertCompanyBranch.designationSec;
        this.codeSaisi = detailTransfertCompanyBranch.codeSaisi;
        this.quantite = detailTransfertCompanyBranch.quantite;
        this.prixUnitaire = detailTransfertCompanyBranch.prixUnitaire;
        this.remise = detailTransfertCompanyBranch.remise;
        this.montantHt = detailTransfertCompanyBranch.montantHt;
        this.tauxTva = detailTransfertCompanyBranch.tauxTva;
        this.codeTva = detailTransfertCompanyBranch.codeTva;
        this.codeUnite = detailTransfertCompanyBranch.codeUnite;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public LocalDate getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(LocalDate datePeremption) {
        this.datePeremption = datePeremption;
    }

    public String getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(String categDepot) {
        this.categDepot = categDepot;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationSec() {
        return designationSec;
    }

    public void setDesignationSec(String designationSec) {
        this.designationSec = designationSec;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getMontantHt() {
        return montantHt;
    }

    public void setMontantHt(BigDecimal montantHt) {
        this.montantHt = montantHt;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public TransfertCompanyBranch getTransfertCompanyBranch() {
        return transfertCompanyBranch;
    }

    public void setTransfertCompanyBranch(TransfertCompanyBranch transfertCompanyBranch) {
        this.transfertCompanyBranch = transfertCompanyBranch;
    }

    public BigDecimal getBaseTva() {
        return baseTva;
    }

    public void setBaseTva(BigDecimal baseTva) {
        this.baseTva = baseTva;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
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

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public BigDecimal getQuantiteRestante() {
        return quantiteRestante;
    }

    public void setQuantiteRestante(BigDecimal quantiteRestante) {
        this.quantiteRestante = quantiteRestante;
    }

    public List<TraceDetailTransfertCompanyBranch> getListeTraceDetailTransfertCompanyBranch() {
        return listeTraceDetailTransfertCompanyBranch;
    }

    public void setListeTraceDetailTransfertCompanyBranch(List<TraceDetailTransfertCompanyBranch> listeTraceDetailTransfertCompanyBranch) {
        this.listeTraceDetailTransfertCompanyBranch = listeTraceDetailTransfertCompanyBranch;
    }

    public Boolean getIsReferencePrice() {
        return isReferencePrice;
    }

    public void setIsReferencePrice(Boolean isReferencePrice) {
        this.isReferencePrice = isReferencePrice;
    }

    public BigDecimal getAncienPrixAchat() {
        return ancienPrixAchat;
    }

    public void setAncienPrixAchat(BigDecimal ancienPrixAchat) {
        this.ancienPrixAchat = ancienPrixAchat;
    }

    public Integer getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(Integer codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailTransfertCompanyBranch)) {
            return false;
        }
        DetailTransfertCompanyBranch other = (DetailTransfertCompanyBranch) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetailTransfertCompanyBranch{" + "code=" + code + ", codeArticle=" + codeArticle + ", lotInter=" + lotInter + ", datePeremption=" + datePeremption + ", categDepot=" + categDepot + ", codeSaisi=" + codeSaisi + ", codeUnite=" + codeUnite + ", designationSec=" + designationSec + ", designation=" + designation + ", quantite=" + quantite + ", prixUnitaire=" + prixUnitaire + ", montantHt=" + montantHt + ", codeTva=" + codeTva + ", tauxTva=" + tauxTva + ", baseTva=" + baseTva + ", remise=" + remise + ", isCapitalize=" + isCapitalize + ", pmpPrecedent=" + pmpPrecedent + ", quantitePrecedante=" + quantitePrecedante + ", prixVente=" + prixVente + '}';
    }

}
