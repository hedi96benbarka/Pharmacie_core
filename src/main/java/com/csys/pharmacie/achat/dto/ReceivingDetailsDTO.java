package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReceivingDetailsDTO {

    private Integer receiving;

    @NotNull
    private Integer codart;

    @NotNull
    @Size(min = 1, max = 255)
    private String desart;

    @NotNull
    @Size(min = 1, max = 255)
    private String desartSec;

    @NotNull
    @Size(min = 1, max = 50)
    private String codeSaisi;

    @NotNull
    private BigDecimal quantite;

    private BigDecimal quantiteReceptionne;

    private Date datPer;

    private String lotInter;
    
    private Integer codeEmplacement;
    
    private String designationEmplacement;
    private LocalDate datePréemption;
    private BigDecimal quantiteReceiving;
    private BigDecimal quantiteReceivingValide;

    private Integer quantiteGratuite;

    private Integer codtva;

    private BigDecimal tautva;

    private Integer delaiLivraison;

    private BigDecimal quantiteRestante;

    private BigDecimal quantiteGratuiteRestante;

    private BigDecimal prixUnitaire;
    private Boolean perissable;

    @Size(max = 150)
    private String memo;

    private String unitDesignation;
    private Integer codeUnite;
    private boolean corporel;

    private BigDecimal remise;
    private Boolean free;
    private BigDecimal dernierPrixAchat;
    private BigDecimal prixVente;
    private Boolean isCapitalize;
    private Boolean isAppelOffre;
    private Boolean quantityFromComannde;
    public Integer getReceiving() {
        return receiving;
    }

    public void setReceiving(Integer receiving) {
        this.receiving = receiving;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesartSec() {
        return desartSec;
    }

    public void setDesartSec(String desartSec) {
        this.desartSec = desartSec;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getQuantiteReceptionne() {
        return quantiteReceptionne;
    }

    public void setQuantiteReceptionne(BigDecimal quantiteReceptionne) {
        this.quantiteReceptionne = quantiteReceptionne;
    }

    public Date getDatPer() {
        return datPer;
    }

    public void setDatPer(Date datPer) {
        this.datPer = datPer;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public Integer getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(Integer codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
    }

    public String getDesignationEmplacement() {
        return designationEmplacement;
    }

    public void setDesignationEmplacement(String designationEmplacement) {
        this.designationEmplacement = designationEmplacement;
    }

    public LocalDate getDatePréemption() {
        return datePréemption;
    }

    public void setDatePréemption(LocalDate datePréemption) {
        this.datePréemption = datePréemption;
    }

    public BigDecimal getQuantiteReceiving() {
        return quantiteReceiving;
    }

    public void setQuantiteReceiving(BigDecimal quantiteReceiving) {
        this.quantiteReceiving = quantiteReceiving;
    }

   

    public BigDecimal getQuantiteReceivingValide() {
        return quantiteReceivingValide;
    }

    public void setQuantiteReceivingValide(BigDecimal quantiteReceivingValide) {
        this.quantiteReceivingValide = quantiteReceivingValide;
    }


    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public Integer getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(Integer delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public BigDecimal getQuantiteRestante() {
        return quantiteRestante;
    }

    public void setQuantiteRestante(BigDecimal quantiteRestante) {
        this.quantiteRestante = quantiteRestante;
    }

    public BigDecimal getQuantiteGratuiteRestante() {
        return quantiteGratuiteRestante;
    }

    public void setQuantiteGratuiteRestante(BigDecimal quantiteGratuiteRestante) {
        this.quantiteGratuiteRestante = quantiteGratuiteRestante;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Boolean getPerissable() {
        return perissable;
    }

    public void setPerissable(Boolean perissable) {
        this.perissable = perissable;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getUnitDesignation() {
        return unitDesignation;
    }

    public void setUnitDesignation(String unitDesignation) {
        this.unitDesignation = unitDesignation;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public boolean isCorporel() {
        return corporel;
    }

    public void setCorporel(boolean corporel) {
        this.corporel = corporel;
    }

    public Integer getQuantiteGratuite() {
        return quantiteGratuite;
    }

    public void setQuantiteGratuite(Integer quantiteGratuite) {
        this.quantiteGratuite = quantiteGratuite;
    }

 

    public BigDecimal getDernierPrixAchat() {
        return dernierPrixAchat;
    }

    public void setDernierPrixAchat(BigDecimal dernierPrixAchat) {
        this.dernierPrixAchat = dernierPrixAchat;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
    }

    public Boolean getIsAppelOffre() {
        return isAppelOffre;
    }

    public void setIsAppelOffre(Boolean isAppelOffre) {
        this.isAppelOffre = isAppelOffre;
    }

    public Boolean getQuantityFromComannde() {
        return quantityFromComannde;
    }

    public void setQuantityFromComannde(Boolean quantityFromComannde) {
        this.quantityFromComannde = quantityFromComannde;
    }

    public Boolean getFree() {
        return free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    
    }
