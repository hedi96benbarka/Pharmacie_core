/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Farouk
 */
@Entity
@Table(name = "DepSto_Hist")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "DepStoHist.inventaire",
            attributeNodes = {
                @NamedAttributeNode("inventaire")
            })

})
public class DepStoHist implements Serializable {

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Num")
    private Integer num;

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_saisie")
    private String codeSaisie;

    @NotNull
    @Column(name = "code_article")
    private Integer codeArticle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "article_designation_Ar")
    private String articledesignationAr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "article_designation")
    private String articleDesignation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "code_categorie_article")
    private Integer codeCategorieArticle;

    @NotNull
    @Column(name = "code_unite")
    private Integer codeUnite;

    @Basic(optional = false)
    @Column(name = "Lot")
    private String lot;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "StkDep")
    private BigDecimal stkDep;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Qte0")
    private BigDecimal qte0;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PU")
    private BigDecimal pu;
    @Column(name = "DatPer")
    private LocalDate datPer;

    @Basic(optional = false)
    @NotNull
    @Column(name = "PuTotReel")
    private BigDecimal puTotReel;

    @Basic(optional = false)
    @NotNull
    @Column(name = "PuTotTheorique")
    private BigDecimal puTotTheorique;

    @Basic(optional = false)
    @NotNull
    @Column(name = "taux_tva")
    private BigDecimal tauxTva;

    @Basic(optional = false)
    @NotNull
    @Column(name = "code_tva")
    private Integer codeTva;

    @Column(name = "code_depsto")
    private Integer codeDepsto;

    @Size(min = 0, max = 20)
    @Column(name = "numbon_origin_depsto")
    private String numBonOriginDepsto;

    @Size(min = 0, max = 20)
    @Column(name = "numbon_depsto")
    private String numBonDepsto;

    public DepStoHist(BigDecimal pu) {
        this.pu = pu;
    }

    public DepStoHist() {
    }

    public BigDecimal getPuTotReel() {
        return puTotReel;
    }

    public void setPuTotReel(BigDecimal puTotReel) {
        this.puTotReel = puTotReel;
    }

    public BigDecimal getPuTotTheorique() {
        return puTotTheorique;
    }

    public void setPuTotTheorique(BigDecimal puTotTheorique) {
        this.puTotTheorique = puTotTheorique;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_inventaire", referencedColumnName = "code")
    private Inventaire inventaire;

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getCodeSaisie() {
        return codeSaisie;
    }

    public void setCodeSaisie(String codeSaisie) {
        this.codeSaisie = codeSaisie;
    }

    public String getArticledesignationAr() {
        return articledesignationAr;
    }

    public void setArticledesignationAr(String articledesignationAr) {
        this.articledesignationAr = articledesignationAr;
    }

    public String getArticleDesignation() {
        return articleDesignation;
    }

    public void setArticleDesignation(String articleDesignation) {
        this.articleDesignation = articleDesignation;
    }

    public Integer getCodeCategorieArticle() {
        return codeCategorieArticle;
    }

    public void setCodeCategorieArticle(Integer codeCategorieArticle) {
        this.codeCategorieArticle = codeCategorieArticle;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public BigDecimal getStkDep() {
        return stkDep;
    }

    public void setStkDep(BigDecimal stkDep) {
        this.stkDep = stkDep;
    }

    public BigDecimal getQte0() {
        return qte0;
    }

    public void setQte0(BigDecimal qte0) {
        this.qte0 = qte0;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    public void setInventaire(Inventaire inventaire) {
        this.inventaire = inventaire;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public Integer getCodeDepsto() {
        return codeDepsto;
    }

    public void setCodeDepsto(Integer codeDepsto) {
        this.codeDepsto = codeDepsto;
    }

    public String getNumBonOriginDepsto() {
        return numBonOriginDepsto;
    }

    public void setNumBonOriginDepsto(String numBonOriginDepsto) {
        this.numBonOriginDepsto = numBonOriginDepsto;
    }

    public String getNumBonDepsto() {
        return numBonDepsto;
    }

    public void setNumBonDepsto(String numBonDepsto) {
        this.numBonDepsto = numBonDepsto;
    }

    @Override
    public String toString() {
        return "DepStoHist{" + "num=" + num + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.num);
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
        final DepStoHist other = (DepStoHist) obj;
        if (!Objects.equals(this.num, other.num)) {
            return false;
        }
        return true;
    }

}
