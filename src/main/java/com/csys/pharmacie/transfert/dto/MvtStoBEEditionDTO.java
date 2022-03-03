/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author bassatine
 */
public class MvtStoBEEditionDTO {

  
    
    private Integer codArt;

    private String numOrdre;


    private String lotInter;

   
    private BigDecimal priuni;

    private Date datPer;

    private String numBon;

   
    private CategorieDepotEnum categDepot;

   
    private String desart;

   
    private String desArtSec;

 
    private String codeSaisi;

    
    private BigDecimal quantite;

  
    private Integer codeUnite;

    
    private String designationUnite;

    
    
      public MvtStoBEEditionDTO(Integer codArt, String numOrdre, String lotInter, BigDecimal priuni, Date datPer, String numBon, CategorieDepotEnum categDepot, String desart, String desArtSec, String codeSaisi, BigDecimal quantite, Integer codeUnite, String designationUnite) {
        this.codArt = codArt;
        this.numOrdre = numOrdre;
        this.lotInter = lotInter;
        this.priuni = priuni;
        this.datPer = datPer;
        this.numBon = numBon;
        this.categDepot = categDepot;
        this.desart = desart;
        this.desArtSec = desArtSec;
        this.codeSaisi = codeSaisi;
        this.quantite = quantite;
        this.codeUnite = codeUnite;
        this.designationUnite = designationUnite;
    }

    public Integer getCodArt() {
        return codArt;
    }

    public void setCodArt(Integer codArt) {
        this.codArt = codArt;
    }

    public String getNumOrdre() {
        return numOrdre;
    }

    public void setNumOrdre(String numOrdre) {
        this.numOrdre = numOrdre;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public Date getDatPer() {
        return datPer;
    }

    public void setDatPer(Date datPer) {
        this.datPer = datPer;
    }

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesArtSec() {
        return desArtSec;
    }

    public void setDesArtSec(String desArtSec) {
        this.desArtSec = desArtSec;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    public MvtStoBEEditionDTO() {
    }
    
      
}
