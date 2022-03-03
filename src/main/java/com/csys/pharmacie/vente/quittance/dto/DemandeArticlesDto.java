package com.csys.pharmacie.vente.quittance.dto;

import java.math.BigDecimal;

public class DemandeArticlesDto {

    
    private Integer code;
    private String numbon;
    private String numordre;
    private String designation;
    private String designationSec;
    private String codeSaisi;
    private BigDecimal quantiteDemande;
    private BigDecimal quantiteRestante;
    private BigDecimal quantiteFacture;
    private Boolean perissable;
    private Integer codeunite;
    private String designationunite;
    private String memoart;
    private boolean aRemplacer;
     private String numdoss;
    private String medtrait;
    private String codvend;
      private String designationDepot;
         private String designationDepotSec;
       private long coddep;

    public DemandeArticlesDto() {
        super();
    }

    public String getDesignationSec() {
        return designationSec;
    }

    public void setDesignationSec(String designationSec) {
        this.designationSec = designationSec;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public Integer getCodeunite() {
        return codeunite;
    }

    public void setCodeunite(Integer codeunite) {
        this.codeunite = codeunite;
    }

    public String getDesignationunite() {
        return designationunite;
    }

    public void setDesignationunite(String designationunite) {
        this.designationunite = designationunite;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getQuantiteDemande() {
        return quantiteDemande;
    }

    public void setQuantiteDemande(BigDecimal quantiteDemande) {
        this.quantiteDemande = quantiteDemande;
    }

    public BigDecimal getQuantiteFacture() {
        return quantiteFacture;
    }

    public void setQuantiteFacture(BigDecimal quantiteFacture) {
        this.quantiteFacture = quantiteFacture;
    }

    public Boolean getPerissable() {
        return perissable;
    }

    public void setPerissable(Boolean perissable) {
        this.perissable = perissable;
    }

    public BigDecimal getQuantiteRestante() {
        return quantiteRestante;
    }

    public void setQuantiteRestante(BigDecimal quantiteRestante) {
        this.quantiteRestante = quantiteRestante;
    }

    public boolean isaRemplacer() {
        return aRemplacer;
    }

    public void setaRemplacer(boolean aRemplacer) {
        this.aRemplacer = aRemplacer;
    }

    public String getMemoart() {
        return memoart;
    }

    public void setMemoart(String memoart) {
        this.memoart = memoart;
    }

    public String getNumdoss() {
        return numdoss;
    }

    public void setNumdoss(String numdoss) {
        this.numdoss = numdoss;
    }

    public String getMedtrait() {
        return medtrait;
    }

    public void setMedtrait(String medtrait) {
        this.medtrait = medtrait;
    }

    public String getCodvend() {
        return codvend;
    }

    public void setCodvend(String codvend) {
        this.codvend = codvend;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public long getCoddep() {
        return coddep;
    }

    public void setCoddep(long coddep) {
        this.coddep = coddep;
    }

    public String getDesignationDepotSec() {
        return designationDepotSec;
    }

    public void setDesignationDepotSec(String designationDepotSec) {
        this.designationDepotSec = designationDepotSec;
    }


    
    
}
