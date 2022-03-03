package com.csys.pharmacie.stock.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepstoDTO {

    private Integer code;
    @NotNull
    private Integer articleID;

    private String codeSaisiArticle;

    private String designation;

    private String designationSec;

    @NotNull
    private Integer UnityCode;

    private String UnityDesignation;

    private LocalDate preemptionDate;

    private String lotInter;
    @NotNull
    private BigDecimal quantity;

    private BigDecimal prixAchat;

    private BigDecimal dernierPrixAchat;

    private BigDecimal prixVente;

    private Integer codeTvaVente;

    private BigDecimal valeurTvaVente;

    private Integer numCategorieArticle;

    private String categorieArticle;

    private String numBon;
    //@Min(1)
    private BigDecimal quantiteReel;

    private BigDecimal quantiteTheorique;

    private Boolean isInInventory;

    private Integer codeDepot;

    private Boolean perissable;

    private BigDecimal sommeValReel;

    private BigDecimal sommeValReelTTC;

    private BigDecimal sommeValtheorique;

    private BigDecimal sommeValtheoriqueTTC;

    private Integer genericCode;

    private String genericDesignation;

    private Integer qteDejaSaisieInv;

    private String depotDesignation;

    private String qrCode;

    private CategorieDepotEnum categorieDepot;
    private String dessignationDepot;
    private String categorieDepotDes;
    private List<DepstoDetailArticleDTO> depstoDetailArticle;

    private Integer codeTva;
    private BigDecimal tauxTva;
    private String messageAlerteDatPer;

    private Boolean nonMoved;

    /*used for inject redressement in depsto when starting application*/
    private BigDecimal quantityOfOldStock;

    private Boolean avecNumeroSerie;
    private Boolean detaille;

    public DepstoDTO(Integer code, Integer articleID, Integer UnityCode, BigDecimal quantity, String depotDesignation) {
        this.code = code;
        this.articleID = articleID;
        this.UnityCode = UnityCode;
        this.quantity = quantity;
        this.depotDesignation = depotDesignation;
    }

    
    public Integer getGenericCode() {
        return genericCode;
    }

    public void setGenericCode(Integer genericCode) {
        this.genericCode = genericCode;
    }

    public String getGenericDesignation() {
        return genericDesignation;
    }

    public void setGenericDesignation(String genericDesignation) {
        this.genericDesignation = genericDesignation;
    }

    public Integer getQteDejaSaisieInv() {
        return qteDejaSaisieInv;
    }

    public void setQteDejaSaisieInv(Integer qteDejaSaisieInv) {
        this.qteDejaSaisieInv = qteDejaSaisieInv;
    }

    public BigDecimal getSommeValReel() {
        return sommeValReel;
    }

    public void setSommeValReel(BigDecimal sommeValReel) {
        this.sommeValReel = sommeValReel;
    }

    public BigDecimal getSommeValtheorique() {
        return sommeValtheorique;
    }

    public void setSommeValtheorique(BigDecimal sommeValtheorique) {
        this.sommeValtheorique = sommeValtheorique;
    }

    public String getDesignationSec() {
        return designationSec;
    }

    public void setDesignationSec(String designationSec) {
        this.designationSec = designationSec;
    }

    public Boolean getPerissable() {
        return perissable;
    }

    public void setPerissable(Boolean perissable) {
        this.perissable = perissable;
    }

    public List<DepstoDetailArticleDTO> getDepstoDetailArticle() {
        return depstoDetailArticle;
    }

    public void setDepstoDetailArticle(List<DepstoDetailArticleDTO> depstoDetailArticle) {
        this.depstoDetailArticle = depstoDetailArticle;
    }

    public String getCategorieDepotDes() {
        return categorieDepotDes;
    }

    public void setCategorieDepotDes(String categorieDepotDes) {
        this.categorieDepotDes = categorieDepotDes;
    }

    public Integer getArticleID() {
        return articleID;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }

    public String getCodeSaisiArticle() {
        return codeSaisiArticle;
    }

    public void setCodeSaisiArticle(String codeSaisiArticle) {
        this.codeSaisiArticle = codeSaisiArticle;
    }

    public DepstoDTO(Integer code, String designation) {
        this.code = code;
        this.designation = designation;
    }

    public DepstoDTO(Integer code, String codeSaisi, String designation) {
        this.code = code;
        this.codeSaisiArticle = codeSaisi;
        this.designation = designation;
    }

    public DepstoDTO(BigDecimal quantity) {
        this.quantity = quantity;
    }
    
    public DepstoDTO() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepotDesignation() {
        return depotDesignation;
    }

    public void setDepotDesignation(String depotDesignation) {
        this.depotDesignation = depotDesignation;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.articleID);
        hash = 83 * hash + Objects.hashCode(this.UnityCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DepstoDTO other = (DepstoDTO) obj;
        if (!Objects.equals(this.articleID, other.articleID)) {
            return false;
        }
        if (!Objects.equals(this.UnityCode, other.UnityCode)) {
            return false;
        }
        return true;
    }

    public Integer getUnityCode() {
        return UnityCode;
    }

    public void setUnityCode(Integer UnityCode) {
        this.UnityCode = UnityCode;
    }

    public String getUnityDesignation() {
        return UnityDesignation;
    }

    public void setUnityDesignation(String UnityDesignation) {
        this.UnityDesignation = UnityDesignation;
    }

    public LocalDate getPreemptionDate() {
        return preemptionDate;
    }

    public void setPreemptionDate(LocalDate preemptionDate) {
        this.preemptionDate = preemptionDate;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public Integer getCodeTvaVente() {
        return codeTvaVente;
    }

    public void setCodeTvaVente(Integer codeTvaVente) {
        this.codeTvaVente = codeTvaVente;
    }

    public BigDecimal getValeurTvaVente() {
        return valeurTvaVente;
    }

    public void setValeurTvaVente(BigDecimal valeurTvaVente) {
        this.valeurTvaVente = valeurTvaVente;
    }

    public BigDecimal getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(BigDecimal prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Integer getNumCategorieArticle() {
        return numCategorieArticle;
    }

    public void setNumCategorieArticle(Integer numCategorieArticle) {
        this.numCategorieArticle = numCategorieArticle;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public BigDecimal getQuantiteReel() {
        return quantiteReel;
    }

    public void setQuantiteReel(BigDecimal quantiteReel) {
        this.quantiteReel = quantiteReel;
    }

    public BigDecimal getQuantiteTheorique() {
        return quantiteTheorique;
    }

    public void setQuantiteTheorique(BigDecimal quantiteTheorique) {
        this.quantiteTheorique = quantiteTheorique;
    }

    public Boolean getIsInInventory() {
        return isInInventory;
    }

    public void setIsInInventory(Boolean isInInventory) {
        this.isInInventory = isInInventory;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public CategorieDepotEnum getCategorieDepot() {
        return categorieDepot;
    }

    public String getCategorieArticle() {
        return categorieArticle;
    }

    public void setCategorieArticle(String categorieArticle) {
        this.categorieArticle = categorieArticle;
    }

    public void setCategorieDepot(CategorieDepotEnum categorieDepot) {
        this.categorieDepot = categorieDepot;
    }

    public String getDessignationDepot() {
        return dessignationDepot;
    }

    public void setDessignationDepot(String dessignationDepot) {
        this.dessignationDepot = dessignationDepot;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
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

    public BigDecimal getDernierPrixAchat() {
        return dernierPrixAchat;
    }

    public void setDernierPrixAchat(BigDecimal dernierPrixAchat) {
        this.dernierPrixAchat = dernierPrixAchat;
    }

    public BigDecimal getSommeValReelTTC() {
        return sommeValReelTTC;
    }

    public void setSommeValReelTTC(BigDecimal sommeValReelTTC) {
        this.sommeValReelTTC = sommeValReelTTC;
    }

    public BigDecimal getSommeValtheoriqueTTC() {
        return sommeValtheoriqueTTC;
    }

    public void setSommeValtheoriqueTTC(BigDecimal sommeValtheoriqueTTC) {
        this.sommeValtheoriqueTTC = sommeValtheoriqueTTC;
    }

    public String getMessageAlerteDatPer() {
        return messageAlerteDatPer;
    }

    public void setMessageAlerteDatPer(String messageAlerteDatPer) {
        this.messageAlerteDatPer = messageAlerteDatPer;
    }

    public Boolean getNonMoved() {
        return nonMoved;
    }

    public void setNonMoved(Boolean nonMoved) {
        this.nonMoved = nonMoved;
    }

    public BigDecimal getQuantityOfOldStock() {
        return quantityOfOldStock;
    }

    public void setQuantityOfOldStock(BigDecimal quantityOfOldStock) {
        this.quantityOfOldStock = quantityOfOldStock;
    }

    public Boolean getAvecNumeroSerie() {
        return avecNumeroSerie;
    }

    public void setAvecNumeroSerie(Boolean avecNumeroSerie) {
        this.avecNumeroSerie = avecNumeroSerie;
    }

    public Boolean getDetaille() {
        return detaille;
    }

    public void setDetaille(Boolean detaille) {
        this.detaille = detaille;
    }

//    @Override
//    public String toString() {
//        return "DepstoDTO{" + "articleID=" + articleID + ", nonMoved=" + nonMoved + '}';
//    }

    @Override
    public String toString() {
        return "DepstoDTO{" + "articleID=" + articleID + ", lotInter=" + lotInter + ", quantiteReel=" + quantiteReel + ", avecNumeroSerie=" + avecNumeroSerie + ", detaille=" + detaille + '}';
    }

   

}
