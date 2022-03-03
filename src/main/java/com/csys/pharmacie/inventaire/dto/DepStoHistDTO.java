package com.csys.pharmacie.inventaire.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.validation.constraints.NotNull;

public class DepStoHistDTO {

    private Integer codeArticle;

    private String articledesignationAr;

    private String articleDesignation;

    private Integer codeCategorieArticle;

    private String categoriearticledesignationAr;

    private String categorieArticleDesignation;

    private Integer codeUnite;

    private String uniteDesignation;

    private String unitedesignationAr;

    private Integer num;

    private String lot;

    private BigDecimal stkDep;

    private BigDecimal qte0;

    private BigDecimal pu;
    @NotNull
    private BigDecimal puTotReel;
    @NotNull
    private BigDecimal puTotTheorique;

    private LocalDate datPer;
    
    private Date datPerEdition;

    private Integer numInventaire;

    private Integer codeDepot;

    private String DesignationDepot;

    private CategorieDepotEnum categDepot;

    private String codeSaisie;

    private BigDecimal tauxTva;

    private Integer codeTva;

    public String getCodeSaisie() {
        return codeSaisie;
    }

    public void setCodeSaisie(String codeSaisie) {
        this.codeSaisie = codeSaisie;
    }

    public BigDecimal getPuTotReel() {
        return puTotReel;
    }

    public void setPuTotReel(BigDecimal puTotReel) {
        this.puTotReel = puTotReel;
    }

    public BigDecimal getPuTotTheorique() {
        return puTotTheorique;
    }

    public void setPuTotTheorique(BigDecimal puTotTheorique) {
        this.puTotTheorique = puTotTheorique;
    }

    private Date datInv;

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getArticledesignationAr() {
        return articledesignationAr;
    }

    public Integer getNumInventaire() {
        return numInventaire;
    }

    public void setNumInventaire(Integer numInventaire) {
        this.numInventaire = numInventaire;
    }

    public void setArticledesignationAr(String articledesignationAr) {
        this.articledesignationAr = articledesignationAr;
    }

    public String getArticleDesignation() {
        return articleDesignation;
    }

    public void setArticleDesignation(String articleDesignation) {
        this.articleDesignation = articleDesignation;
    }

    public Integer getCodeCategorieArticle() {
        return codeCategorieArticle;
    }

    public void setCodeCategorieArticle(Integer codeCategorieArticle) {
        this.codeCategorieArticle = codeCategorieArticle;
    }

    public String getCategoriearticledesignationAr() {
        return categoriearticledesignationAr;
    }

    public void setCategoriearticledesignationAr(String categoriearticledesignationAr) {
        this.categoriearticledesignationAr = categoriearticledesignationAr;
    }

    public String getCategorieArticleDesignation() {
        return categorieArticleDesignation;
    }

    public void setCategorieArticleDesignation(String categorieArticleDesignation) {
        this.categorieArticleDesignation = categorieArticleDesignation;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public String getDesignationDepot() {
        return DesignationDepot;
    }

    public void setDesignationDepot(String DesignationDepot) {
        this.DesignationDepot = DesignationDepot;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getUniteDesignation() {
        return uniteDesignation;
    }

    public void setUniteDesignation(String uniteDesignation) {
        this.uniteDesignation = uniteDesignation;
    }

    public String getUnitedesignationAr() {
        return unitedesignationAr;
    }

    public void setUnitedesignationAr(String unitedesignationAr) {
        this.unitedesignationAr = unitedesignationAr;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getDatInv() {
        return datInv;
    }

    public void setDatInv(Date datInv) {
        this.datInv = datInv;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public BigDecimal getStkDep() {
        return stkDep;
    }

    public void setStkDep(BigDecimal stkDep) {
        this.stkDep = stkDep;
    }

    public BigDecimal getQte0() {
        return qte0;
    }

    public void setQte0(BigDecimal qte0) {
        this.qte0 = qte0;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public Date getDatPerEdition() {
        return datPerEdition;
    }

    public void setDatPerEdition(Date datPerEdition) {
        this.datPerEdition = datPerEdition;
    }
    
    @Override
    public String toString() {
        return "DepStoHistDTO{" + "codeArticle=" + codeArticle + ", articledesignationAr=" + articledesignationAr + ", articleDesignation=" + articleDesignation + ", codeCategorieArticle=" + codeCategorieArticle + ", categoriearticledesignationAr=" + categoriearticledesignationAr + ", categorieArticleDesignation=" + categorieArticleDesignation + ", codeUnite=" + codeUnite + ", uniteDesignation=" + uniteDesignation + ", unitedesignationAr=" + unitedesignationAr + ", num=" + num + ", lot=" + lot + ", stkDep=" + stkDep + ", qte0=" + qte0 + ", pu=" + pu + ", puTotReel=" + puTotReel + ", puTotTheorique=" + puTotTheorique + ", datPer=" + datPer + ", numInventaire=" + numInventaire + ", codeDepot=" + codeDepot + ", DesignationDepot=" + DesignationDepot + ", categDepot=" + categDepot + ", codeSaisie=" + codeSaisie + ", tauxTva=" + tauxTva + ", codeTva=" + codeTva + ", datInv=" + datInv + '}';
    }

}
