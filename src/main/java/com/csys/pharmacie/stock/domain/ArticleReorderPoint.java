/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import org.springframework.security.core.context.SecurityContextHolder;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 *
 * @author Administrateur
 */
@Entity
@Table(name = "article_reorder_point")
public class ArticleReorderPoint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "code_saisie")
    private String codeSaisie;
    @Column(name = "date_du_reference")
    private LocalDateTime dateDuReference;
    @Column(name = "date_au_reference")
    private LocalDateTime dateAuReference;
    @Column(name = "date_create")
    private LocalDateTime dateCreate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_create")
    private String userCreate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "days_consumming")
    private BigDecimal consummingDays;
    @NotNull
    @Column(name = "categ_depot", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private CategorieDepotEnum categDepot;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articleReorderPoint")
    private Collection<DetailArticleReorderPoint> detailArticleReorderPointCollection;

    @Column(name = "code_planing")
    private Integer codePlaning;

    @PrePersist
    public void prePersist() {
        this.dateCreate = LocalDateTime.now();
        // this.userCreate = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public ArticleReorderPoint() {
    }

    public ArticleReorderPoint(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCodeSaisie() {
        return codeSaisie;
    }

    public void setCodeSaisie(String codeSaisie) {
        this.codeSaisie = codeSaisie;
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

    public BigDecimal getConsummingDays() {
        return consummingDays;
    }

    public void setConsummingDays(BigDecimal consummingDays) {
        this.consummingDays = consummingDays;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public Integer getCodePlaning() {
        return codePlaning;
    }

    public void setCodePlaning(Integer codePlaning) {
        this.codePlaning = codePlaning;
    }

    @XmlTransient
    public Collection<DetailArticleReorderPoint> getDetailArticleReorderPointCollection() {
        return detailArticleReorderPointCollection;
    }

    public void setDetailArticleReorderPointCollection(Collection<DetailArticleReorderPoint> detailArticleReorderPointCollection) {
        this.detailArticleReorderPointCollection = detailArticleReorderPointCollection;
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
        if (!(object instanceof ArticleReorderPoint)) {
            return false;
        }
        ArticleReorderPoint other = (ArticleReorderPoint) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.parametrageachat.domain.ArticleReorderPoint[ code=" + code + " ]";
    }

}
