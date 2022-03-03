/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.math.BigDecimal;

/**
 *
 * @author Farouk
 */
public class QtePrixMouvement {

    private Integer codart;
    private String codeSaisi;
    private String designationunite;
    private String desart;
    private String desartSec;
    private Integer codeunite;
    private BigDecimal qte;
    private BigDecimal valeurVente;
    private BigDecimal valeurAchat;
    private BigDecimal tauTva;
    private BigDecimal tauTvaAch;
    private Integer coddep;
    private String designationDepot;
    private String codeSaisiDepot;
    private String listCategorieArticle;
    private String numBonOrigin;

    public QtePrixMouvement() {
    }

    public QtePrixMouvement(Integer codart, String codeSaisi, String designationunite, String desart, String desartSec, Integer codeunite, BigDecimal qte, BigDecimal valeurVente, BigDecimal valeurAchat, BigDecimal tauTva, BigDecimal tauTvaAch, Integer coddep, String designationDepot, String codeSaisiDepot, String listCategorieArticle, String numBonOrigin) {
        this.codart = codart;
        this.codeSaisi = codeSaisi;
        this.designationunite = designationunite;
        this.desart = desart;
        this.desartSec = desartSec;
        this.codeunite = codeunite;
        this.qte = qte;
        this.valeurVente = valeurVente;
        this.valeurAchat = valeurAchat;
        this.tauTva = tauTva;
        this.tauTvaAch = tauTvaAch;
        this.coddep = coddep;
        this.designationDepot = designationDepot;
        this.codeSaisiDepot = codeSaisiDepot;
        this.listCategorieArticle = listCategorieArticle;
        this.numBonOrigin = numBonOrigin;
    }

    public QtePrixMouvement(Integer codart, String codeSaisi, String designationunite, String desart, String desartSec, Integer codeunite, BigDecimal qte, BigDecimal valeurVente, BigDecimal tauTva, BigDecimal tauTvaAch, Integer coddep, String numBonOrigin) {
        this.codart = codart;
        this.codeSaisi = codeSaisi;
        this.designationunite = designationunite;
        this.desart = desart;
        this.desartSec = desartSec;
        this.codeunite = codeunite;
        this.qte = qte;
        this.valeurVente = valeurVente;
        this.tauTva = tauTva;
        this.tauTvaAch = tauTvaAch;
        this.coddep = coddep;
        this.numBonOrigin = numBonOrigin;
    }


    public QtePrixMouvement(Integer codart, String codeSaisi, String desart, String desartSec, Integer codeunite, BigDecimal qte, BigDecimal valeurVente, BigDecimal valeurAchat, BigDecimal tauTva, BigDecimal tauTvaAch, Integer coddep) {
        this.codart = codart;
        this.codeSaisi = codeSaisi;
        this.desart = desart;
        this.desartSec = desartSec;
        this.codeunite = codeunite;
        this.qte = qte;
        this.valeurVente = valeurVente;
        this.valeurAchat = valeurAchat;
        this.tauTva = tauTva;
        this.tauTvaAch = tauTvaAch;
        this.coddep = coddep;
    }

    public QtePrixMouvement(Integer codart, String codeSaisi, String desart, String desartSec, Integer codeunite, BigDecimal qte, BigDecimal valeurVente, BigDecimal valeurAchat, BigDecimal tauTva, BigDecimal tauTvaAch, Integer coddep, String numBonOrigin) {
        this.codart = codart;
        this.codeSaisi = codeSaisi;
        this.desart = desart;
        this.desartSec = desartSec;
        this.codeunite = codeunite;
        this.qte = qte;
        this.valeurVente = valeurVente;
        this.valeurAchat = valeurAchat;
        this.tauTva = tauTva;
        this.tauTvaAch = tauTvaAch;
        this.coddep = coddep;
        this.numBonOrigin = numBonOrigin;
    }


    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
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

    public String getDesartSec() {
        return desartSec;
    }

    public void setDesartSec(String desartSec) {
        this.desartSec = desartSec;
    }

    public Integer getCodeunite() {
        return codeunite;
    }

    public void setCodeunite(Integer codeunite) {
        this.codeunite = codeunite;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public BigDecimal getValeurVente() {
        return valeurVente;
    }

    public void setValeurVente(BigDecimal valeurVente) {
        this.valeurVente = valeurVente;
    }

    public BigDecimal getValeurAchat() {
        return valeurAchat;
    }

    public void setValeurAchat(BigDecimal valeurAchat) {
        this.valeurAchat = valeurAchat;
    }

    public BigDecimal getTauTva() {
        return tauTva;
    }

    public void setTauTva(BigDecimal tauTva) {
        this.tauTva = tauTva;
    }

    public String getDesignationunite() {
        return designationunite;
    }

    public void setDesignationunite(String designationunite) {
        this.designationunite = designationunite;
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

    public BigDecimal getTauTvaAch() {
        return tauTvaAch;
    }

    public void setTauTvaAch(BigDecimal tauTvaAch) {
        this.tauTvaAch = tauTvaAch;
    }

    public String getListCategorieArticle() {
        return listCategorieArticle;
    }

    public void setListCategorieArticle(String listCategorieArticle) {
        this.listCategorieArticle = listCategorieArticle;
    }

    public String getNumBonOrigin() {
        return numBonOrigin;
    }

    public void setNumBonOrigin(String numBonOrigin) {
        this.numBonOrigin = numBonOrigin;
    }

    @Override
    public String toString() {
        return "QtePrixMouvement{" + "codart=" + codart + ", codeSaisi=" + codeSaisi + ", designationunite=" + designationunite + ", desart=" + desart + ", desartSec=" + desartSec + ", codeunite=" + codeunite + ", qte=" + qte + ", valeurVente=" + valeurVente + ", valeurAchat=" + valeurAchat + ", tauTva=" + tauTva + ", tauTvaAch=" + tauTvaAch + ", coddep=" + coddep + ", designationDepot=" + designationDepot + ", codeSaisiDepot=" + codeSaisiDepot + ", listCategorieArticle=" + listCategorieArticle + '}';
    }
    
 

}
