/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import com.csys.pharmacie.stock.domain.Depsto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "trace_detail_transfert_company_branch")
@Audited
@AuditTable("trace_detail_transfert_company_branch_AUD")
public class TraceDetailTransfertCompanyBranch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
  
    @JoinColumn(name = "code_detail_transfert", referencedColumnName = "code", insertable = false, updatable = false)
    @ManyToOne
    private DetailTransfertCompanyBranch detailTransfertCompanyBranch;
    
    @NotNull
    @Column(name = "code_detail_transfert")
    private Integer codeDetailTransfert;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_depsto")
    private Integer codeDepsto;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_article")
    private Integer codeArticle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "numbon_transfert")
    private String numbonTransfert;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_disponible")
    private BigDecimal quantiteDisponible;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite_prelevee")
    private BigDecimal quantitePrelevee;
    
    @Size(max = 50)
    @Column(name = "numbon_depsto")
    private String numbonDepsto;
    
    @Size(max = 50)
    @Column(name = "lot_inter_depsto")
    private String lotInterDepsto;
    
    @Column(name = "date_peremption_depsto")
    private LocalDate datePeremptionDepsto;
    
    @Column(name = "prix_unitaire_depsto")
    private BigDecimal prixUnitaireDepsto;
    
    @Column(name = "unite")
    private Integer unite;
    
    @Column(name = "code_tva_depsto")
    private Integer codeTvaDepsto;
    @Column(name = "taux_tva_depsto")
    private BigDecimal tauxTvaDepsto;
    
    @Size(max = 20)
    @Column(name = "numbon_origin_depsto")
    private String numbonOriginDepsto;
    
    @Column(name = "is_capitalize")
    private Boolean isCapitalize;

     @JoinColumn(name = "code_depsto", referencedColumnName = "code", insertable = false, updatable = false)
    @OneToOne
    private Depsto depsto;


    
    public TraceDetailTransfertCompanyBranch() {
    }

    public TraceDetailTransfertCompanyBranch(Depsto depsto, DetailTransfertCompanyBranch detailTransfertCompanyBranch) {
        this.depsto = depsto;
        this.detailTransfertCompanyBranch = detailTransfertCompanyBranch;
    }

    
    
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCodeDepsto() {
        return codeDepsto;
    }

    public void setCodeDepsto(Integer codeDepsto) {
        this.codeDepsto = codeDepsto;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getNumbonTransfert() {
        return numbonTransfert;
    }

    public void setNumbonTransfert(String numbonTransfert) {
        this.numbonTransfert = numbonTransfert;
    }

    public BigDecimal getQuantiteDisponible() {
        return quantiteDisponible;
    }

    public void setQuantiteDisponible(BigDecimal quantiteDisponible) {
        this.quantiteDisponible = quantiteDisponible;
    }

    public BigDecimal getQuantitePrelevee() {
        return quantitePrelevee;
    }

    public void setQuantitePrelevee(BigDecimal quantitePrelevee) {
        this.quantitePrelevee = quantitePrelevee;
    }

    public String getNumbonDepsto() {
        return numbonDepsto;
    }

    public void setNumbonDepsto(String numbonDepsto) {
        this.numbonDepsto = numbonDepsto;
    }

    public String getLotInterDepsto() {
        return lotInterDepsto;
    }

    public void setLotInterDepsto(String lotInterDepsto) {
        this.lotInterDepsto = lotInterDepsto;
    }

    public LocalDate getDatePeremptionDepsto() {
        return datePeremptionDepsto;
    }

    public void setDatePeremptionDepsto(LocalDate datePeremptionDepsto) {
        this.datePeremptionDepsto = datePeremptionDepsto;
    }

    public BigDecimal getPrixUnitaireDepsto() {
        return prixUnitaireDepsto;
    }

    public void setPrixUnitaireDepsto(BigDecimal prixUnitaireDepsto) {
        this.prixUnitaireDepsto = prixUnitaireDepsto;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public Integer getCodeTvaDepsto() {
        return codeTvaDepsto;
    }

    public void setCodeTvaDepsto(Integer codeTvaDepsto) {
        this.codeTvaDepsto = codeTvaDepsto;
    }

    public BigDecimal getTauxTvaDepsto() {
        return tauxTvaDepsto;
    }

    public void setTauxTvaDepsto(BigDecimal tauxTvaDepsto) {
        this.tauxTvaDepsto = tauxTvaDepsto;
    }

    public String getNumbonOriginDepsto() {
        return numbonOriginDepsto;
    }

    public void setNumbonOriginDepsto(String numbonOriginDepsto) {
        this.numbonOriginDepsto = numbonOriginDepsto;
    }

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
    }

    public Integer getCodeDetailTransfert() {
        return codeDetailTransfert;
    }

    public void setCodeDetailTransfert(Integer codeDetailTransfert) {
        this.codeDetailTransfert = codeDetailTransfert;
    }

    public Depsto getDepsto() {
        return depsto;
    }

    public void setDepsto(Depsto depsto) {
        this.depsto = depsto;
    }

    public DetailTransfertCompanyBranch getDetailTransfertCompanyBranch() {
        return detailTransfertCompanyBranch;
    }

    public void setDetailTransfertCompanyBranch(DetailTransfertCompanyBranch detailTransfertCompanyBranch) {
        this.detailTransfertCompanyBranch = detailTransfertCompanyBranch;
    }

   

  
    
}
