/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.domain;

import com.csys.pharmacie.helper.ClassificationArticleEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "consommation_reelle_for_rop")
public class ConsommationReelleForRop implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codart")
    private Integer codart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantite")
    private BigDecimal quantite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codeUnite")
    private Integer codeUnite;
    @Size(max = 10)
    @Column(name = "categ_depot")
    private String categDepot;

    @Basic(optional = false)
    @NotNull
    @Column(name = "valeur")
    private BigDecimal valeur;
    
    @Transient
    private ClassificationArticleEnum classificationArticle;

    public ConsommationReelleForRop() {
    }


    public ConsommationReelleForRop(BigDecimal quantite, BigDecimal valeur) {
        this.quantite = quantite;
        this.valeur = valeur;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public int getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(int codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(String categDepot) {
        this.categDepot = categDepot;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
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
        if (!(object instanceof ConsommationReelleForRop)) {
            return false;
        }
        ConsommationReelleForRop other = (ConsommationReelleForRop) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.csys.pharmacie.stock.domain.ConsommationReelleForRop[ code=" + code + " ]";
    }

}
