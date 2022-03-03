package com.csys.pharmacie.stock.dto;

import java.math.BigDecimal;

public class RotationStock {

    private Integer codArt;
    private Integer coddep;
    private String codeSaisi;
    private String designationDepot;
    private String codeSaisiDepot;
    private String desart;

    private BigDecimal stkDep = BigDecimal.ZERO;
    private BigDecimal mvte = BigDecimal.ZERO;
    private BigDecimal mvts = BigDecimal.ZERO;

    private BigDecimal transent = BigDecimal.ZERO;
    private BigDecimal avoir = BigDecimal.ZERO;
    private BigDecimal recep = BigDecimal.ZERO;
    private BigDecimal decoupE = BigDecimal.ZERO;
    private BigDecimal redressE = BigDecimal.ZERO;
    private BigDecimal invenE = BigDecimal.ZERO;

    private BigDecimal sortie = BigDecimal.ZERO;
    private BigDecimal transf = BigDecimal.ZERO;
    private BigDecimal redressS = BigDecimal.ZERO;
    private BigDecimal retour = BigDecimal.ZERO;
    private BigDecimal decoupS = BigDecimal.ZERO;
    private BigDecimal bonPrelevement = BigDecimal.ZERO;
    private BigDecimal bonRetourPrelevement = BigDecimal.ZERO;
    private BigDecimal avoirFournisseur = BigDecimal.ZERO;
    private BigDecimal retourPerime = BigDecimal.ZERO;
    private BigDecimal invenS = BigDecimal.ZERO;
    private Integer codeCategorieArticle;

    private String designationCategorieArticle;

    private Integer codeunite;
    private String desunite;

    public RotationStock() {

    }

    public RotationStock(Integer codArt, Integer codeunite, Integer coddep) {
        this.codArt = codArt;
        this.coddep = coddep;
        this.codeunite = codeunite;
    }

    public Integer getCodArt() {
        return codArt;
    }

    public void setCodArt(Integer codArt) {
        this.codArt = codArt;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public String getDesignationDepot() {
        return designationDepot;
    }

    public void setDesignationDepot(String designationDepot) {
        this.designationDepot = designationDepot;
    }

    public String getCodeSaisiDepot() {
        return codeSaisiDepot;
    }

    public void setCodeSaisiDepot(String codeSaisiDepot) {
        this.codeSaisiDepot = codeSaisiDepot;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public BigDecimal getStkDep() {
        return stkDep;
    }

    public void setStkDep(BigDecimal stkDep) {
        this.stkDep = stkDep;
    }

    public BigDecimal getSortie() {
        return sortie;
    }

    public void setSortie(BigDecimal sortie) {
        this.sortie = sortie;
    }

    public BigDecimal getTransf() {
        return transf;
    }

    public void setTransf(BigDecimal transf) {
        this.transf = transf;
    }

    public BigDecimal getRedressS() {
        return redressS;
    }

    public void setRedressS(BigDecimal redressS) {
        this.redressS = redressS;
    }

    public BigDecimal getRecep() {
        return recep;
    }

    public void setRecep(BigDecimal recep) {
        this.recep = recep;
    }

    public BigDecimal getAvoir() {
        return avoir;
    }

    public void setAvoir(BigDecimal avoir) {
        this.avoir = avoir;
    }

    public BigDecimal getRedressE() {
        return redressE;
    }

    public void setRedressE(BigDecimal redressE) {
        this.redressE = redressE;
    }

    public BigDecimal getDecoupS() {
        return decoupS;
    }

    public void setDecoupS(BigDecimal decoupS) {
        this.decoupS = decoupS;
    }

    public BigDecimal getDecoupE() {
        return decoupE;
    }

    public void setDecoupE(BigDecimal decoupE) {
        this.decoupE = decoupE;
    }

    public BigDecimal getMvte() {
        return mvte;
    }

    public void setMvte(BigDecimal mvte) {
        this.mvte = mvte;
    }

    public BigDecimal getMvts() {
        return mvts;
    }

    public void setMvts(BigDecimal mvts) {
        this.mvts = mvts;
    }

    public BigDecimal getTransent() {
        return transent;
    }

    public void setTransent(BigDecimal transent) {
        this.transent = transent;
    }

    public BigDecimal getRetour() {
        return retour;
    }

    public void setRetour(BigDecimal retour) {
        this.retour = retour;
    }

    public BigDecimal getAvoirFournisseur() {
        return avoirFournisseur;
    }

    public void setAvoirFournisseur(BigDecimal avoirFournisseur) {
        this.avoirFournisseur = avoirFournisseur;
    }

    public BigDecimal getRetourPerime() {
        return retourPerime;
    }

    public void setRetourPerime(BigDecimal retourPerime) {
        this.retourPerime = retourPerime;
    }

    public BigDecimal getBonPrelevement() {
        return bonPrelevement;
    }

    public void setBonPrelevement(BigDecimal bonPrelevement) {
        this.bonPrelevement = bonPrelevement;
    }

    public Integer getCodeunite() {
        return codeunite;
    }

    public void setCodeunite(Integer codeunite) {
        this.codeunite = codeunite;
    }

    public String getDesunite() {
        return desunite;
    }

    public void setDesunite(String desunite) {
        this.desunite = desunite;
    }

    public BigDecimal getInvenE() {
        return invenE;
    }

    public void setInvenE(BigDecimal invenE) {
        this.invenE = invenE;
    }

    public BigDecimal getInvenS() {
        return invenS;
    }

    public void setInvenS(BigDecimal invenS) {
        this.invenS = invenS;
    }

    public Integer getCodeCategorieArticle() {
        return codeCategorieArticle;
    }

    public void setCodeCategorieArticle(Integer codeCategorieArticle) {
        this.codeCategorieArticle = codeCategorieArticle;
    }

    public String getDesignationCategorieArticle() {
        return designationCategorieArticle;
    }

    public void setDesignationCategorieArticle(String designationCategorieArticle) {
        this.designationCategorieArticle = designationCategorieArticle;
    }

    public BigDecimal getBonRetourPrelevement() {
        return bonRetourPrelevement;
    }

    public void setBonRetourPrelevement(BigDecimal bonRetourPrelevement) {
        this.bonRetourPrelevement = bonRetourPrelevement;
    }

    @Override
    public String toString() {
        return "RotationStock{" + "codArt=" + codArt + ", coddep=" + coddep + ", codeSaisi=" + codeSaisi + ", designationDepot=" + designationDepot + ", codeSaisiDepot=" + codeSaisiDepot + ", desart=" + desart + ", stkDep=" + stkDep + ", mvte=" + mvte + ", mvts=" + mvts + ", transent=" + transent + ", avoir=" + avoir + ", recep=" + recep + ", decoupE=" + decoupE + ", redressE=" + redressE + ", invenE=" + invenE + ", sortie=" + sortie + ", transf=" + transf + ", redressS=" + redressS + ", retour=" + retour + ", decoupS=" + decoupS + ", bonPrelevement=" + bonPrelevement + ", invenS=" + invenS + ", codeCategorieArticle=" + codeCategorieArticle + ", designationCategorieArticle=" + designationCategorieArticle + ", codeunite=" + codeunite + ", desunite=" + desunite + '}';
    }

}
