/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author Administrateur
 */
public class PricelisteParCategorieArticleDTO {

    private Integer codeCategorieArticle;
    private Integer codePriceList;
    private BigDecimal taux;
    private String natureException;
    private BigDecimal tauxImporte;
    private String natureExceptionImporte;

    public PricelisteParCategorieArticleDTO() {
    }

    public Integer getCodeCategorieArticle() {
        return codeCategorieArticle;
    }

    public void setCodeCategorieArticle(Integer codeCategorieArticle) {
        this.codeCategorieArticle = codeCategorieArticle;
    }

    public Integer getCodePriceList() {
        return codePriceList;
    }

    public void setCodePriceList(Integer codePriceList) {
        this.codePriceList = codePriceList;
    }

    public BigDecimal getTaux() {
        return taux;
    }

    public void setTaux(BigDecimal taux) {
        this.taux = taux;
    }

    public String getNatureException() {
        return natureException;
    }

    public void setNatureException(String natureException) {
        this.natureException = natureException;
    }

    public BigDecimal getTauxImporte() {
        return tauxImporte;
    }

    public void setTauxImporte(BigDecimal tauxImporte) {
        this.tauxImporte = tauxImporte;
    }

    public String getNatureExceptionImporte() {
        return natureExceptionImporte;
    }

    public void setNatureExceptionImporte(String natureExceptionImporte) {
        this.natureExceptionImporte = natureExceptionImporte;
    }

    @JsonProperty("codeCategorieArticle")
    private void unpackNestedCategorieArticle(Map<String, Object> categorieArticle) {
        this.codeCategorieArticle = (Integer) categorieArticle.get("code");
    }

    @JsonProperty("codePriceList")
    private void unpackNestedPriceList(Map<String, Object> priceList) {
        this.codePriceList = (Integer) priceList.get("code");
    }

    @JsonProperty("natureException")
    private void unpackNestednNatureException(Map<String, Object> natureException) {
        this.natureException = (String) natureException.get("code");
    }

    @JsonProperty("natureExceptionImporte")
    private void unpackNestednNatureExceptionImporte(Map<String, Object> natureExceptionImporte) {
        this.natureExceptionImporte = (String) natureExceptionImporte.get("code");
    }

    @Override
    public String toString() {
        return "PricelisteParCategorieArticleDTO{" + "codeCategorieArticle=" + codeCategorieArticle + ", codePriceList=" + codePriceList + ", taux=" + taux + ", natureException=" + natureException + ", tauxImporte=" + tauxImporte + ", natureExceptionImporte=" + natureExceptionImporte + '}';
    }

}
