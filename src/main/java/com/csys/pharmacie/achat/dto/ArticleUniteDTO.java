/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author Administrateur
 */
public class ArticleUniteDTO {
    private Integer codeArticle;
    private Integer codeUnite;
    private BigDecimal prixVente;
    private BigDecimal prixVenteAvecMarge;
    private BigDecimal prixVenteTTC;
    private BigDecimal nbPiece;
    private String unityDesignation;

    public ArticleUniteDTO() {
    }

    public ArticleUniteDTO(Integer codeArticle, Integer codeUnite, BigDecimal nbPiece) {
        this.codeArticle = codeArticle;
        this.codeUnite = codeUnite;
        this.nbPiece = nbPiece;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public BigDecimal getPrixVenteTTC() {
        return prixVenteTTC;
    }

    public void setPrixVenteTTC(BigDecimal prixVenteTTC) {
        this.prixVenteTTC = prixVenteTTC;
    }

    public BigDecimal getPrixVenteAvecMarge() {
        return prixVenteAvecMarge;
    }

    public void setPrixVenteAvecMarge(BigDecimal prixVenteAvecMarge) {
        this.prixVenteAvecMarge = prixVenteAvecMarge;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getUnityDesignation() {
        return unityDesignation;
    }

    public void setUnityDesignation(String unityDesignation) {
        this.unityDesignation = unityDesignation;
    }

    
    @JsonProperty("codeDesignationUnite")
    private void unpackNestedUnite(Map<String, Object> unite) {
        this.codeUnite = (Integer) unite.get("code");
        this.unityDesignation = (String) unite.get("designation");
    }

    @JsonProperty("codeDesignationArticle")
    private void unpackNestedArticle(Map<String, Object> article) {
        this.codeArticle = (Integer) article.get("code");
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public BigDecimal getNbPiece() {
        return nbPiece;
    }

    public void setNbPiece(BigDecimal nbPiece) {
        this.nbPiece = nbPiece;
    }
//
//    @Override
//    public String toString() {
//        return "ArticleUniteDTO{" + "codeUnite=" + codeUnite + ", prixVente=" + prixVente + ", prixVenteTTC=" + prixVenteTTC + ", nbPiece=" + nbPiece + ", unityDesignation=" + unityDesignation + '}';
//    }

    @Override
    public String toString() {
        return "ArticleUniteDTO{" + "codeArticle=" + codeArticle + ", codeUnite=" + codeUnite + ", nbPiece=" + nbPiece + '}';
    }

    

}
