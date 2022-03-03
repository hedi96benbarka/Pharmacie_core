/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Farouk
 */
@Audited
@Entity
@Table(name = "prix_reference_article")
@NamedQueries({@NamedQuery(name = "PrixReferenceArticle.findAll", query = "SELECT p FROM PrixReferenceArticle p")})
@AuditTable("prix_reference_article_AUD")
public class PrixReferenceArticle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id @Basic(optional = false) @NotNull @Column(name = "article")
    private Integer article;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false) @NotNull @Column(name = "prix_reference")
    private BigDecimal prixReference;

    public PrixReferenceArticle() {
    }

    public PrixReferenceArticle(Integer article) {
        this.article = article;
    }

    public PrixReferenceArticle(Integer article, BigDecimal prixReference) {
        this.article = article;
        this.prixReference = prixReference;
    }

    public Integer getArticle() {
        return article;
    }

    public void setArticle(Integer article) {
        this.article = article;
    }

    public BigDecimal getPrixReference() {
        return prixReference;
    }

    public void setPrixReference(BigDecimal prixReference) {
        this.prixReference = prixReference;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (article != null ? article.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrixReferenceArticle)) {
            return false;
        }
        PrixReferenceArticle other = (PrixReferenceArticle) object;
        if ((this.article == null && other.article != null) || (this.article != null && !this.article.equals(other.article))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PrixReferenceArticle{" + "article=" + article + ", prixReference=" + prixReference + '}';
    }

    
    
}
