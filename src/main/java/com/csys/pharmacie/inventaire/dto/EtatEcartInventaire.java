/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class EtatEcartInventaire {

    private String codeSasie;
    private Integer coddep;
    private String designationDepot;
    private CategorieDepotEnum codeCathegDepot;
    private String desCathegorieDepot;

    private Integer codeInventaire;

    private Integer codart;
    private String designation;
    private Integer UnityCode;

    private String UnityDesignation;
    private BigDecimal quantiteReel;
    private BigDecimal valeurReel;
    private BigDecimal valeurReelTTC;
    private BigDecimal priUniReel;
    private BigDecimal priUniReelTTC;
    private BigDecimal quantiteTheorique;
    private BigDecimal valeurTheorique;
    private BigDecimal valeurTheoriqueTTC;
    private BigDecimal priUniTheorique;
    private BigDecimal priUniTheoriqueTTC;

    private String codeCategorieArticle;
    private String categorieArticle;
    private String codeSaisie;
    private String designationCategorieArt;
    private String codeSaisiArticle;
    private List <FactureBTDTO> listeFactureBT;

    public EtatEcartInventaire(BigDecimal ZERO) {
        valeurTheorique = BigDecimal.ZERO;
        valeurReel = BigDecimal.ZERO;
        quantiteTheorique = BigDecimal.ZERO;
        quantiteReel = BigDecimal.ZERO;
        valeurReelTTC = BigDecimal.ZERO;
        valeurTheoriqueTTC = BigDecimal.ZERO;
    }

    public BigDecimal getPriUniReel() {
        return priUniReel;
    }

    public void setPriUniReel(BigDecimal priUniReel) {
        this.priUniReel = priUniReel;
    }

    public BigDecimal getPriUniTheorique() {
        return priUniTheorique;
    }

    public void setPriUniTheorique(BigDecimal priUniTheorique) {
        this.priUniTheorique = priUniTheorique;
    }

    public String getCodeSaisie() {
        return codeSaisie;
    }

    public void setCodeSaisie(String codeSaisie) {
        this.codeSaisie = codeSaisie;
    }

    public String getDesignationCategorieArt() {
        return designationCategorieArt;
    }

    public void setDesignationCategorieArt(String designationCategorieArt) {
        this.designationCategorieArt = designationCategorieArt;
    }

    public String getCodeCategorieArticle() {
        return codeCategorieArticle;
    }

    public void setCodeCategorieArticle(String codeCategorieArticle) {
        this.codeCategorieArticle = codeCategorieArticle;
    }

    public String getCategorieArticle() {
        return categorieArticle;
    }

    public void setCategorieArticle(String categorieArticle) {
        this.categorieArticle = categorieArticle;
    }

    public EtatEcartInventaire() {
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

    public CategorieDepotEnum getCodeCathegDepot() {
        return codeCathegDepot;
    }

    public void setCodeCathegDepot(CategorieDepotEnum codeCathegDepot) {
        this.codeCathegDepot = codeCathegDepot;
    }

    public String getDesCathegorieDepot() {
        return desCathegorieDepot;
    }

    public void setDesCathegorieDepot(String desCathegorieDepot) {
        this.desCathegorieDepot = desCathegorieDepot;
    }

    public Integer getCodeInventaire() {
        return codeInventaire;
    }

    public void setCodeInventaire(Integer codeInventaire) {
        this.codeInventaire = codeInventaire;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getUnityCode() {
        return UnityCode;
    }

    public void setUnityCode(Integer UnityCode) {
        this.UnityCode = UnityCode;
    }

    public String getUnityDesignation() {
        return UnityDesignation;
    }

    public void setUnityDesignation(String UnityDesignation) {
        this.UnityDesignation = UnityDesignation;
    }

    public BigDecimal getQuantiteReel() {
        return quantiteReel;
    }

    public void setQuantiteReel(BigDecimal quantiteReel) {
        this.quantiteReel = quantiteReel;
    }

    public BigDecimal getValeurReel() {
        return valeurReel;
    }

    public void setValeurReel(BigDecimal valeurReel) {
        this.valeurReel = valeurReel;
    }

    public BigDecimal getQuantiteTheorique() {
        return quantiteTheorique;
    }

    public void setQuantiteTheorique(BigDecimal quantiteTheorique) {
        this.quantiteTheorique = quantiteTheorique;
    }

    public BigDecimal getValeurTheorique() {
        return valeurTheorique;
    }

    public void setValeurTheorique(BigDecimal valeurTheorique) {
        this.valeurTheorique = valeurTheorique;
    }

    public String getCodeSaisiArticle() {
        return codeSaisiArticle;
    }

    public void setCodeSaisiArticle(String codeSaisiArticle) {
        this.codeSaisiArticle = codeSaisiArticle;
    }

    public String getCodeSasie() {
        return codeSasie;
    }

    public void setCodeSasie(String codeSasie) {
        this.codeSasie = codeSasie;
    }

    public BigDecimal getValeurReelTTC() {
        return valeurReelTTC;
    }

    public void setValeurReelTTC(BigDecimal valeurReelTTC) {
        this.valeurReelTTC = valeurReelTTC;
    }

    public BigDecimal getValeurTheoriqueTTC() {
        return valeurTheoriqueTTC;
    }

    public void setValeurTheoriqueTTC(BigDecimal valeurTheoriqueTTC) {
        this.valeurTheoriqueTTC = valeurTheoriqueTTC;
    }

    @Override
    public String toString() {
        return "EtatEcartInventaire{" + "codeSasie=" + codeSasie + ", coddep=" + coddep + ", designationDepot=" + designationDepot + ", codeCathegDepot=" + codeCathegDepot + ", desCathegorieDepot=" + desCathegorieDepot + ", codeInventaire=" + codeInventaire + ", codart=" + codart + ", designation=" + designation + ", UnityCode=" + UnityCode + ", UnityDesignation=" + UnityDesignation + ", quantiteReel=" + quantiteReel + ", valeurReel=" + valeurReel + ", quantiteTheorique=" + quantiteTheorique + ", valeurTheorique=" + valeurTheorique + ", codeCategorieArticle=" + codeCategorieArticle + ", categorieArticle=" + categorieArticle + ", codeSaisie=" + codeSaisie + ", designationCategorieArt=" + designationCategorieArt + '}';
    }

    public BigDecimal getPriUniReelTTC() {
        return priUniReelTTC;
    }

    public void setPriUniReelTTC(BigDecimal priUniReelTTC) {
        this.priUniReelTTC = priUniReelTTC;
    }

    public BigDecimal getPriUniTheoriqueTTC() {
        return priUniTheoriqueTTC;
    }

    public void setPriUniTheoriqueTTC(BigDecimal priUniTheoriqueTTC) {
        this.priUniTheoriqueTTC = priUniTheoriqueTTC;
    }

    public List<FactureBTDTO> getListeFactureBT() {
        return listeFactureBT;
    }

    public void setListeFactureBT(List<FactureBTDTO> listeFactureBT) {
        this.listeFactureBT = listeFactureBT;
    }

}
