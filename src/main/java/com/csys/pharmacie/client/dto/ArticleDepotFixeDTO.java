/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.dto;


import java.math.BigDecimal;

/**
 *
 * @author DELL
 */
public class ArticleDepotFixeDTO {
    private Integer codeDepot;
    private Integer codeArticle;
    private Integer codeUnite;
    private BigDecimal stockFixe;

    public ArticleDepotFixeDTO() {
    }

    public ArticleDepotFixeDTO(Integer codeDepot, Integer codeArticle, BigDecimal stockFixe) {
        this.codeDepot = codeDepot;
        this.codeArticle = codeArticle;
        this.stockFixe = stockFixe;
    }

    public ArticleDepotFixeDTO(BigDecimal stockFixe) {
        this.stockFixe = stockFixe;
    }

    public Integer getCodeDepot() {
        return codeDepot;
    }

    public void setCodeDepot(Integer codeDepot) {
        this.codeDepot = codeDepot;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public BigDecimal getStockFixe() {
        return stockFixe;
    }

    public void setStockFixe(BigDecimal stockFixe) {
        this.stockFixe = stockFixe;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    @Override
    public String toString() {
        return "ArticleDepotDTO{" + "codeDepot=" + codeDepot + ", codeArticle=" + codeArticle + ", codeUnite=" + codeUnite + ", stockFixe=" + stockFixe + '}';
    }


}
