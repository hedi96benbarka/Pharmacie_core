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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "ajustement_transfert_branch_company")

public class AjustementTransfertBranchCompany implements Serializable {


    @EmbeddedId
    protected AjustementTransfertBranchCompanyPK ajustementTransfertBranchCompanyPK;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_depot")
    private Integer codeDepot;
    
  
    @Basic(optional = false)
    @NotNull
    @Column(name = "diff_mnt_ht")
    private BigDecimal diffMntHt;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "diff_mnt_ttc")
    private BigDecimal diffMntTtc;
    
   
    @Column(name = "integrer")
    private Boolean integrer;
    

    
    @Column(name = "Num_Vir")
    private String codeIntegration;
   
     
    @JoinColumn(name = "numbon_retour", referencedColumnName = "num_bon", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TransfertCompanyBranch transfertCompanyBranch;

    public AjustementTransfertBranchCompany() {
    }

    public AjustementTransfertBranchCompany(AjustementTransfertBranchCompanyPK ajustementTransfertBranchCompanyPK) {
        this.ajustementTransfertBranchCompanyPK = ajustementTransfertBranchCompanyPK;
    }

    public AjustementTransfertBranchCompanyPK getAjustementTransfertBranchCompanyPK() {
        return ajustementTransfertBranchCompanyPK;
    }

    public void setAjustementTransfertBranchCompanyPK(AjustementTransfertBranchCompanyPK ajustementTransfertBranchCompanyPK) {
        this.ajustementTransfertBranchCompanyPK = ajustementTransfertBranchCompanyPK;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public BigDecimal getDiffMntHt() {
        return diffMntHt;
    }

    public void setDiffMntHt(BigDecimal diffMntHt) {
        this.diffMntHt = diffMntHt;
    }

    public BigDecimal getDiffMntTtc() {
        return diffMntTtc;
    }

    public void setDiffMntTtc(BigDecimal diffMntTtc) {
        this.diffMntTtc = diffMntTtc;
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

    public TransfertCompanyBranch getTransfertCompanyBranch() {
        return transfertCompanyBranch;
    }

    public void setTransfertCompanyBranch(TransfertCompanyBranch transfertCompanyBranch) {
        this.transfertCompanyBranch = transfertCompanyBranch;
    }

  

  
    
}
