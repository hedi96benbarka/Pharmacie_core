package com.csys.pharmacie.client.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class ArticleDepotDto {

    private CodeDesignation codeDesignationArticle;
    private CodeDesignation codeDesignationDepot;
    private BigDecimal prixVente;
    @NotNull
    private BigDecimal minPrixVente;
    private Boolean prixFixe;

    private Boolean margeFixe;
    private BigDecimal marge;

    public CodeDesignation getCodeDesignationArticle() {
        return codeDesignationArticle;
    }

    public void setCodeDesignationArticle(CodeDesignation codeDesignationArticle) {
        this.codeDesignationArticle = codeDesignationArticle;
    }

    public CodeDesignation getCodeDesignationDepot() {
        return codeDesignationDepot;
    }

    public void setCodeDesignationDepot(CodeDesignation codeDesignationDepot) {
        this.codeDesignationDepot = codeDesignationDepot;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public BigDecimal getMinPrixVente() {
        return minPrixVente;
    }

    public void setMinPrixVente(BigDecimal minPrixVente) {
        this.minPrixVente = minPrixVente;
    }

    public Boolean getPrixFixe() {
        return prixFixe;
    }

    public void setPrixFixe(Boolean prixFixe) {
        this.prixFixe = prixFixe;
    }

    public Boolean getMargeFixe() {
        return margeFixe;
    }

    public void setMargeFixe(Boolean margeFixe) {
        this.margeFixe = margeFixe;
    }

    public BigDecimal getMarge() {
        return marge;
    }

    public void setMarge(BigDecimal marge) {
        this.marge = marge;
    }

    @Override
    public String toString() {
        return "ArticleDepotDto{" + "codeDesignationArticle=" + codeDesignationArticle + ", codeDesignationDepot=" + codeDesignationDepot + ", prixVente=" + prixVente + ", minPrixVente=" + minPrixVente + ", prixFixe=" + prixFixe + ", margeFixe=" + margeFixe + ", marge=" + marge + '}';
    }

}
