/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author Farouk
 */

@Audited
@Entity
@Table(name = "prix_moy_pondere_article")
@NamedQueries({@NamedQuery(name = "PrixMoyPondereArticle.findAll", query = "SELECT p FROM PrixMoyPondereArticle p")})
@AuditTable("prix_moy_pondere_article_AUD")
public class PrixMoyPondereArticle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id @Basic(optional = false) @NotNull @Column(name = "article")
    private Integer article;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false) @NotNull @Column(name = "prix_moy_pondere")
    private BigDecimal prixMoyPondere;
    @Column(name = "date_calcule") @Temporal(TemporalType.TIMESTAMP)
    private Date dateCalcule;

    public PrixMoyPondereArticle(Integer article, BigDecimal prixMoyPondere, Date dateCalcule) {
        this.article = article;
        this.prixMoyPondere = prixMoyPondere;
        this.dateCalcule = dateCalcule;
    }
    
    

    public PrixMoyPondereArticle() {
    }

    public PrixMoyPondereArticle(Integer article) {
        this.article = article;
    }

    public PrixMoyPondereArticle(Integer article, BigDecimal prixMoyPondere) {
        this.article = article;
        this.prixMoyPondere = prixMoyPondere;
    }

    public Integer getArticle() {
        return article;
    }

    public void setArticle(Integer article) {
        this.article = article;
    }

    public BigDecimal getPrixMoyPondere() {
        return prixMoyPondere;
    }

    public void setPrixMoyPondere(BigDecimal prixMoyPondere) {
        this.prixMoyPondere = prixMoyPondere;
    }

    public Date getDateCalcule() {
        return dateCalcule;
    }

    public void setDateCalcule(Date dateCalcule) {
        this.dateCalcule = dateCalcule;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PrixMoyPondereArticle other = (PrixMoyPondereArticle) obj;
        if (!Objects.equals(this.article, other.article)) {
            return false;
        }
        if (!Objects.equals(this.prixMoyPondere, other.prixMoyPondere)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PrixMoyPondereArticle{" + "article=" + article + ", prixMoyPondere=" + prixMoyPondere + '}';
    }

   
    
}
