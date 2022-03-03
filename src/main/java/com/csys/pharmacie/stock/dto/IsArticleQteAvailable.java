/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import java.math.BigDecimal;

/**
 *
 * @author Administrateur
 */
public class IsArticleQteAvailable {

    public IsArticleQteAvailable() {
    }

    private boolean isAvailable;
    private Integer articleId;
    private Integer uniteId;
    private BigDecimal availableQte;
    private BigDecimal demandedQte;
    private Integer codeDepot;
    private String depotDesignation;
    private String unityDesignation;

    public IsArticleQteAvailable(Integer articleId, BigDecimal demandedQte) {
        this.articleId = articleId;
        this.demandedQte = demandedQte;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public IsArticleQteAvailable(BigDecimal availableQte) {
        this.availableQte = availableQte;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public BigDecimal getAvailableQte() {
        return availableQte;
    }

    public void setAvailableQte(BigDecimal availableQte) {
        this.availableQte = availableQte;
    }

    public BigDecimal getDemandedQte() {
        return demandedQte;
    }

    public void setDemandedQte(BigDecimal demandedQte) {
        this.demandedQte = demandedQte;
    }

    public Integer getUniteId() {
        return uniteId;
    }

    public void setUniteId(Integer uniteId) {
        this.uniteId = uniteId;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public String getDepotDesignation() {
        return depotDesignation;
    }

    public void setDepotDesignation(String depotDesignation) {
        this.depotDesignation = depotDesignation;
    }

    public String getUnityDesignation() {
        return unityDesignation;
    }

    public void setUnityDesignation(String unityDesignation) {
        this.unityDesignation = unityDesignation;
    }

    @Override
    public String toString() {
        return "IsArticleQteAvailable{" + "isAvailable=" + isAvailable + ", articleId=" + articleId + ", uniteId=" + uniteId + ", availableQte=" + availableQte + ", demandedQte=" + demandedQte + '}';
    }

}
