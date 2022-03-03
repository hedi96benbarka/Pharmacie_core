/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "article_fournisseur")
@NamedQueries({@NamedQuery(name = "ArticleFournisseur.findAll", query = "SELECT a FROM ArticleFournisseur a")})
public class ArticleFournisseur implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ArticleFournisseurPK articleFournisseurPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false) @NotNull @Column(name = "max_prix_achat")
    private BigDecimal maxPrixAchat;
    @Basic(optional = false) @NotNull @Column(name = "min_prix_achat")
    private BigDecimal minPrixAchat;
    @Basic(optional = false) @NotNull @Column(name = "prix_achat")
    private BigDecimal prixAchat;
    @Column(name = "delai_livraison")
    private Integer delaiLivraison;
    @Size(max = 30) @Column(name = "code_art_frs")
    private String codeArtFrs;

    public ArticleFournisseur() {
    }

    public ArticleFournisseur(ArticleFournisseurPK articleFournisseurPK) {
        this.articleFournisseurPK = articleFournisseurPK;
    }

    public ArticleFournisseur(ArticleFournisseurPK articleFournisseurPK, BigDecimal maxPrixAchat, BigDecimal minPrixAchat, BigDecimal prixAchat) {
        this.articleFournisseurPK = articleFournisseurPK;
        this.maxPrixAchat = maxPrixAchat;
        this.minPrixAchat = minPrixAchat;
        this.prixAchat = prixAchat;
    }

    public ArticleFournisseur(int fkArticleCode, String fkFournisseurCode) {
        this.articleFournisseurPK = new ArticleFournisseurPK(fkArticleCode, fkFournisseurCode);
    }

    public ArticleFournisseurPK getArticleFournisseurPK() {
        return articleFournisseurPK;
    }

    public void setArticleFournisseurPK(ArticleFournisseurPK articleFournisseurPK) {
        this.articleFournisseurPK = articleFournisseurPK;
    }

    public BigDecimal getMaxPrixAchat() {
        return maxPrixAchat;
    }

    public void setMaxPrixAchat(BigDecimal maxPrixAchat) {
        this.maxPrixAchat = maxPrixAchat;
    }

    public BigDecimal getMinPrixAchat() {
        return minPrixAchat;
    }

    public void setMinPrixAchat(BigDecimal minPrixAchat) {
        this.minPrixAchat = minPrixAchat;
    }

    public BigDecimal getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(BigDecimal prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Integer getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(Integer delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public String getCodeArtFrs() {
        return codeArtFrs;
    }

    public void setCodeArtFrs(String codeArtFrs) {
        this.codeArtFrs = codeArtFrs;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (articleFournisseurPK != null ? articleFournisseurPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArticleFournisseur)) {
            return false;
        }
        ArticleFournisseur other = (ArticleFournisseur) object;
        if ((this.articleFournisseurPK == null && other.articleFournisseurPK != null) || (this.articleFournisseurPK != null && !this.articleFournisseurPK.equals(other.articleFournisseurPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.csys.pharmacie.achat.domain.ArticleFournisseur[ articleFournisseurPK=" + articleFournisseurPK + " ]";
    }
    
}
