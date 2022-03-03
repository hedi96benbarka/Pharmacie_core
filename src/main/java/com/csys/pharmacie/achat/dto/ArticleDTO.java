package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.ClassificationArticleEnum;
import com.csys.pharmacie.vente.quittance.dto.RemiseConventionnelleDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;
import javax.persistence.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {

    @Id
    @NotNull
    private Integer code;

    @NotNull
    private String codeSaisi;

    private BigDecimal prixVente;

    @NotNull
    @Size(min = 1, max = 50)
    private String designation;

    @NotNull
    @Size(min = 1, max = 50)
    private String designationSec;

    private Boolean perissable;

    private BigDecimal prixAchat;

    private Integer codeUnite;

    private String designationUnite;

    private Integer codeTvaAch;

    private BigDecimal valeurTvaAch;

    private Integer codeTvaVente;

    private BigDecimal valeurTvaVente;

    private CategorieDepotEnum categorieDepot;

    private CategorieArticleDTO categorieArticle;

    private String listeCategorieArticle;

    private Integer genericCode;

    private String genericDesignation;

    private RemiseConventionnelleDTO remiseConventionnelle;

    private Boolean stockable;

    private Boolean actif;

    private boolean isImported;

    private Boolean thermosensitive;

    private BigDecimal ancienPrixAchat;

    private Integer leadTimeProcurement;
    private Integer safetyStockPerDay;

    private ClassificationArticleEnum classificationArticle;
    private Integer stockMin;
    private Integer stockMax;

    private BigDecimal marge;
    private Boolean regeneration;
    private Boolean annule;

    private Boolean stopped;   
    private Boolean corporel;
    

    public ArticleDTO(Integer code, ClassificationArticleEnum classificationArticle, Integer safetyStockPerDay, Integer stockMin, Integer stockMax) {
        this.code = code;
        this.classificationArticle = classificationArticle;
        this.safetyStockPerDay = safetyStockPerDay;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
    }

    public ArticleDTO(Integer code, String designation) {
        this.code = code;
        this.designation = designation;
    }

    public ArticleDTO(Integer code, Integer stockMin, Integer stockMax) {
        this.code = code;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
    }

    public ArticleDTO(Integer code, String codeSaisi, String designation) {
        this.code = code;
        this.codeSaisi = codeSaisi;
        this.designation = designation;
    }

    public ArticleDTO() {
    }

    public ArticleDTO(Integer code) {
        this.code = code;
    }

    public BigDecimal getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(BigDecimal prixAchat) {
        this.prixAchat = prixAchat;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
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

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public CategorieDepotEnum getCategorieDepot() {
        return categorieDepot;
    }

    public void setCategorieDepot(CategorieDepotEnum categorieDepot) {
        this.categorieDepot = categorieDepot;
    }

    public Integer getCodeTvaAch() {
        return codeTvaAch;
    }

    public void setCodeTvaAch(Integer codeTvaAch) {
        this.codeTvaAch = codeTvaAch;
    }

    public BigDecimal getValeurTvaAch() {
        return valeurTvaAch;
    }

    public void setValeurTvaAch(BigDecimal valeurTvaAch) {
        this.valeurTvaAch = valeurTvaAch;
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

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
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

    public CategorieArticleDTO getCategorieArticle() {
        return categorieArticle;
    }

    public void setCategorieArticle(CategorieArticleDTO categorieArticle) {
        this.categorieArticle = categorieArticle;
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

    public RemiseConventionnelleDTO getRemiseConventionnelle() {
        return remiseConventionnelle;
    }

    public void setRemiseConventionnelle(RemiseConventionnelleDTO remiseConventionnelle) {
        this.remiseConventionnelle = remiseConventionnelle;
    }

    public Boolean getThermosensitive() {
        return thermosensitive;
    }

    public void setThermosensitive(Boolean thermosensitive) {
        this.thermosensitive = thermosensitive;
    }

    @JsonProperty("unite")
    private void unpackNestedUnite(Map<String, Object> unite) {
        this.codeUnite = (Integer) unite.get("code");
        this.designationUnite = (String) unite.get("designation");
    }

    @JsonProperty("tvaAchat")
    private void unpackNestedTvaAchat(Map<String, Object> tvaAchat) {
        this.codeTvaAch = (Integer) tvaAchat.get("code");
        this.valeurTvaAch = BigDecimal.valueOf((Double) tvaAchat.get("valeur"));
    }

    @JsonProperty("tvaVente")
    private void unpackNestedTvaVente(Map<String, Object> tvaVente) {
        this.codeTvaVente = (Integer) tvaVente.get("code");
        this.valeurTvaVente = BigDecimal.valueOf((Double) tvaVente.get("valeur"));
    }

    @JsonProperty("generic")
    private void unpackNestedGeneric(Map<String, Object> generic) {
        this.genericCode = (Integer) generic.get("code");
        this.genericDesignation = (String) generic.get("designation");
    }

    public Boolean getStockable() {
        return stockable;
    }

    public void setStockable(Boolean stockable) {
        this.stockable = stockable;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public boolean isIsImported() {
        return isImported;
    }

    public void setIsImported(boolean isImported) {
        this.isImported = isImported;
    }

    public BigDecimal getAncienPrixAchat() {
        return ancienPrixAchat;
    }

    public void setAncienPrixAchat(BigDecimal ancienPrixAchat) {
        this.ancienPrixAchat = ancienPrixAchat;
    }

    public String getListeCategorieArticle() {
        return listeCategorieArticle;
    }

    public void setListeCategorieArticle(String listeCategorieArticle) {
        this.listeCategorieArticle = listeCategorieArticle;
    }

    public Integer getLeadTimeProcurement() {
        return leadTimeProcurement;
    }

    public void setLeadTimeProcurement(Integer leadTimeProcurement) {
        this.leadTimeProcurement = leadTimeProcurement;
    }

    public Integer getSafetyStockPerDay() {
        return safetyStockPerDay;
    }

    public void setSafetyStockPerDay(Integer safetyStockPerDay) {
        this.safetyStockPerDay = safetyStockPerDay;
    }

    public Integer getStockMin() {
        return stockMin;
    }

    public void setStockMin(Integer stockMin) {
        this.stockMin = stockMin;
    }

    public Integer getStockMax() {
        return stockMax;
    }

    public void setStockMax(Integer stockMax) {
        this.stockMax = stockMax;
    }

    public BigDecimal getMarge() {
        return marge;
    }

    public void setMarge(BigDecimal marge) {
        this.marge = marge;
    }

    public Boolean getAnnule() {
        return annule;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public Boolean getStopped() {
        return stopped;
    }

    public void setStopped(Boolean stopped) {
        this.stopped = stopped;
    }

    public Boolean isRegeneration() {
        return regeneration;
    }

    public void setRegeneration(Boolean regeneration) {
        this.regeneration = regeneration;
    }

    public ClassificationArticleEnum getClassificationArticle() {
        return classificationArticle;
    }

    public void setClassificationArticle(ClassificationArticleEnum classificationArticle) {
        this.classificationArticle = classificationArticle;
    }

    public Boolean getCorporel() {
        return corporel;
    }

    public void setCorporel(Boolean corporel) {
        this.corporel = corporel;
    }

    @Override
    public String toString() {
        return "ArticleDTO{" + "code=" + code + ", codeSaisi=" + codeSaisi + ", designation=" + designation + ", prixAchat=" + prixAchat + ", codeUnite=" + codeUnite + ", designationUnite=" + designationUnite + ", codeTvaAch=" + codeTvaAch + ", valeurTvaAch=" + valeurTvaAch + ", codeTvaVente=" + codeTvaVente + ", valeurTvaVente=" + valeurTvaVente + ", categorieDepot=" + categorieDepot + '}';
    }

}
