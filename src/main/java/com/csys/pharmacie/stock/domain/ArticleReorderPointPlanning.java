/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import com.csys.pharmacie.helper.CategorieDepotEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "article_reorder_point_planning")
public class ArticleReorderPointPlanning implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @NotNull
    @Column(name = "categ_depot")
    @Enumerated(EnumType.STRING)
    private CategorieDepotEnum categDepot;
    @Column(name = "date_du_reference")
    private LocalDateTime dateDuReference;
    @Column(name = "date_au_reference")
    private LocalDateTime dateAuReference;
    @Basic(optional = false)
    @NotNull
    @Column(name = "days_consumming")
    private BigDecimal consummingDays;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_create")
    private LocalDateTime dateCreate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "user_create")
    private String userCreate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "planned")
    private Boolean planned;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_prepared")
    private Boolean dataPrepared;
    @Column(name = "ltp_executed")
    private Boolean ltpExecuted;
    @Column(name = "std_executed")
    private Boolean stdExecuted;
    @Basic(optional = false)
    @NotNull
    @Column(name = "executed")
    private Boolean executed;

    @PrePersist
    public void prePersist() {
        this.dateCreate = LocalDateTime.now();
        this.userCreate = SecurityContextHolder.getContext().getAuthentication().getName();
        this.planned = Boolean.TRUE;
        this.dataPrepared = Boolean.FALSE;
        this.ltpExecuted = Boolean.FALSE;
        this.stdExecuted =Boolean.FALSE;
        this.executed = Boolean.FALSE;
    }

    public ArticleReorderPointPlanning() {
    }

    public ArticleReorderPointPlanning(Integer code) {
        this.code = code;
    }

    public ArticleReorderPointPlanning(Integer code, LocalDateTime dateCreate, String userCreate, Boolean planned, Boolean dataPrepared, Boolean executed) {
        this.code = code;
        this.dateCreate = dateCreate;
        this.userCreate = userCreate;
        this.planned = planned;
        this.dataPrepared = dataPrepared;
        this.executed = executed;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public LocalDateTime getDateDuReference() {
        return dateDuReference;
    }

    public void setDateDuReference(LocalDateTime dateDuReference) {
        this.dateDuReference = dateDuReference;
    }

    public LocalDateTime getDateAuReference() {
        return dateAuReference;
    }

    public void setDateAuReference(LocalDateTime dateAuReference) {
        this.dateAuReference = dateAuReference;
    }

    public BigDecimal getConsummingDays() {
        return consummingDays;
    }

    public void setConsummingDays(BigDecimal consummingDays) {
        this.consummingDays = consummingDays;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Boolean getPlanned() {
        return planned;
    }

    public void setPlanned(Boolean planned) {
        this.planned = planned;
    }

    public Boolean isDataPrepared() {
        return dataPrepared;
    }

    public void setDataPrepared(Boolean dataPrepared) {
        this.dataPrepared = dataPrepared;
    }

    public Boolean isLtpExecuted() {
        return ltpExecuted;
    }

    public void setLtpExecuted(Boolean ltpExecuted) {
        this.ltpExecuted = ltpExecuted;
    }

    public Boolean isStdExecuted() {
        return stdExecuted;
    }

    public void setStdExecuted(Boolean stdExecuted) {
        this.stdExecuted = stdExecuted;
    }

    public Boolean isExecuted() {
        return executed;
    }

    public void setExecuted(Boolean executed) {
        this.executed = executed;
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
        if (!(object instanceof ArticleReorderPointPlanning)) {
            return false;
        }
        ArticleReorderPointPlanning other = (ArticleReorderPointPlanning) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ArticleReorderPointPlanning{" + "code=" + code + ", categDepot=" + categDepot + ", dateDuReference=" + dateDuReference + ", dateAuReference=" + dateAuReference + ", consummingDays=" + consummingDays + ", dateCreate=" + dateCreate + ", userCreate=" + userCreate + ", planned=" + planned + ", dataPrepared=" + dataPrepared + ", executed=" + executed + '}';
    }

    
}
