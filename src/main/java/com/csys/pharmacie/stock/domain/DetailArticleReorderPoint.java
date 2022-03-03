/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

import javax.validation.constraints.NotNull;

import com.csys.pharmacie.helper.ClassificationArticleEnum;

/**
 *
 * @author KHOULOUD
 */
@Entity
@Table(name = "detail_article_reorder_point")
public class DetailArticleReorderPoint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_article")
    private Integer codeArticle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_unite")
    private Integer codeUnite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "safety_stock_per_day")
    private Integer safetyStockPerDay;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lead_time_Procurement")
    private Integer leadTimeProcurement;
    @Basic(optional = false)
    @NotNull
    @Column(name = "real_consumming")
    private BigDecimal realConsumming;
    @Basic(optional = false)
    @NotNull
    @Column(name = "consuming_per_day")
    private BigDecimal consumingPerDay;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lead_time")
    private BigDecimal leadTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "safety_stock")
    private BigDecimal safetyStock;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rop")
    private BigDecimal rop;
    @Basic(optional = false)
    @NotNull
    @Column(name = "safety_stock_per_days_consumming")
    private BigDecimal safetyStockPerDaysConsumming;
    @Basic(optional = false)
    @NotNull
    @Column(name = "maximum_stock")
    private BigDecimal maximumStock;
    @Basic(optional = false)
    @NotNull
    @Column(name = "current_stock")
    private BigDecimal currentStock;

    @JoinColumn(name = "code_article_rop", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private ArticleReorderPoint articleReorderPoint;

    @Transient
    private ClassificationArticleEnum classificationArticle;

    public DetailArticleReorderPoint() {
    }

    public DetailArticleReorderPoint(Integer code) {
        this.code = code;
    }

    public DetailArticleReorderPoint(BigDecimal realConsumming) {
        this.realConsumming = realConsumming;
    }

    public DetailArticleReorderPoint(Integer codeArticle, BigDecimal realConsumming) {
        this.codeArticle = codeArticle;
        this.realConsumming = realConsumming;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public Integer getLeadTimeProcurement() {
        return leadTimeProcurement;
    }

    public void setLeadTimeProcurement(Integer leadtimeProcurement) {
        this.leadTimeProcurement = leadtimeProcurement;
    }

    public Integer getSafetyStockPerDay() {
        return safetyStockPerDay;
    }

    public void setSafetyStockPerDay(Integer safetyStockPerDay) {
        this.safetyStockPerDay = safetyStockPerDay;
    }

    public BigDecimal getRealConsumming() {
        return realConsumming;
    }

    public void setRealConsumming(BigDecimal realConsumming) {
        this.realConsumming = realConsumming;
    }

    public BigDecimal getConsumingPerDay() {
        return consumingPerDay;
    }

    public void setConsumingPerDay(BigDecimal consumingPerDay) {
        this.consumingPerDay = consumingPerDay;
    }

    public BigDecimal getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(BigDecimal leadTime) {
        this.leadTime = leadTime;
    }

    public BigDecimal getSafetyStock() {
        return safetyStock;
    }

    public void setSafetyStock(BigDecimal safetyStock) {
        this.safetyStock = safetyStock;
    }

    public BigDecimal getRop() {
        return rop;
    }

    public void setRop(BigDecimal rop) {
        this.rop = rop;
    }

    public BigDecimal getSafetyStockPerDaysConsumming() {
        return safetyStockPerDaysConsumming;
    }

    public void setSafetyStockPerDaysConsumming(BigDecimal safetyStockPerDaysConsumming) {
        this.safetyStockPerDaysConsumming = safetyStockPerDaysConsumming;
    }

    public BigDecimal getMaximumStock() {
        return maximumStock;
    }

    public void setMaximumStock(BigDecimal maximumStock) {
        this.maximumStock = maximumStock;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public ArticleReorderPoint getArticleReorderPoint() {
        return articleReorderPoint;
    }

    public void setArticleReorderPoint(ArticleReorderPoint articleReorderPoint) {
        this.articleReorderPoint = articleReorderPoint;
    }

    public ClassificationArticleEnum getClassificationArticle() {
        return classificationArticle;
    }

    public void setClassificationArticle(ClassificationArticleEnum classificationArticle) {
        this.classificationArticle = classificationArticle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailArticleReorderPoint)) {
            return false;
        }
        DetailArticleReorderPoint other = (DetailArticleReorderPoint) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetailArticleReorderPoint{" + "code=" + code + ", codeArticle=" + codeArticle + ", codeUnite=" + codeUnite + ", safetyStockPerDay=" + safetyStockPerDay + ", leadTimeProcurement=" + leadTimeProcurement + ", realConsumming=" + realConsumming + ", consumingPerDay=" + consumingPerDay + ", leadTime=" + leadTime + ", safetyStock=" + safetyStock + ", rop=" + rop + ", safetyStockPerDaysConsumming=" + safetyStockPerDaysConsumming + ", maximumStock=" + maximumStock + ", currentStock=" + currentStock + '}';
    }



}
