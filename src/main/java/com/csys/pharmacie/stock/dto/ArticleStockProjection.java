/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import java.math.BigDecimal;

/**
 *
 * @author admin
 */
public class ArticleStockProjection {

    private Boolean aInventorier;
    private Integer codart;
    private Integer coddep;
    private Integer unite;
    private BigDecimal qte;
    private BigDecimal qte0;
    private BigDecimal availableQte;

    public ArticleStockProjection() {
    }

    public ArticleStockProjection(Integer codart, Integer unite, BigDecimal qte) {
        this.codart = codart;
        this.unite = unite;
        this.qte = qte;
    }

    public ArticleStockProjection(Boolean aInventorier, Integer codart, Integer unite, BigDecimal qte, BigDecimal qte0) {
        this.aInventorier = aInventorier;
        this.codart = codart;
        this.unite = unite;
        this.qte = qte;
        this.qte0 = qte0;
    }

    public ArticleStockProjection(Boolean aInventorier, Integer codart, Integer coddep, Integer unite, BigDecimal qte, BigDecimal qte0) {
        this.aInventorier = aInventorier;
        this.codart = codart;
        this.coddep = coddep;
        this.unite = unite;
        this.qte = qte;
        this.qte0 = qte0;
    }
    
    public ArticleStockProjection(BigDecimal availableQte) {
        this.availableQte = availableQte;
    }

    public ArticleStockProjection(BigDecimal qte, BigDecimal qte0) {
        this.qte = qte;
        this.qte0 = qte0;
    }

    public ArticleStockProjection(Integer codart, Integer unite, Integer coddep, BigDecimal qte) {
        this.codart = codart;
        this.unite = unite;
        this.coddep = coddep;
        this.qte = qte;
    }

    public Boolean getaInventorier() {
        return aInventorier;
    }

    public void setaInventorier(Boolean aInventorier) {
        this.aInventorier = aInventorier;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public BigDecimal getQte0() {
        return qte0;
    }

    public void setQte0(BigDecimal qte0) {
        this.qte0 = qte0;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public BigDecimal getAvailableQte() {
        return availableQte;
    }

    public void setAvailableQte(BigDecimal availableQte) {
        this.availableQte = availableQte;
    }

    @Override
    public String toString() {
        return "ArticleStockProjection{" + "aInventorier=" + aInventorier + ", codart=" + codart + ", coddep=" + coddep + ", unite=" + unite + ", qte=" + qte + ", qte0=" + qte0 + ", availableQte=" + availableQte + '}';
    }

}
