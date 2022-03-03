/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author bassatine
 */
public class MvtStoPREditionDTO {

    private Integer code;

    private Integer articleID;

    private String numbon;
    private String lotinter;

    private Date datPer;

    private BigDecimal priuni;

    private Integer unite;
    private String Designationunite;

    @Enumerated(EnumType.STRING)
    private CategorieDepotEnum categDepot;

    private String designation;

    private String secondDesignation;

    private String codeSaisi;

    private BigDecimal quantite;
    private Integer codeEmplacement;
    private String designationEmplacement;

    public MvtStoPREditionDTO(Integer code, Integer articleID, String numbon, String lotinter, Date datPer, BigDecimal priuni, Integer unite, String Designationunite, CategorieDepotEnum categDepot, String designation, String secondDesignation, String codeSaisi, BigDecimal quantite) {
        this.code = code;
        this.articleID = articleID;
        this.numbon = numbon;
        this.lotinter = lotinter;
        this.datPer = datPer;
        this.priuni = priuni;
        this.unite = unite;
        this.Designationunite = Designationunite;
        this.categDepot = categDepot;
        this.designation = designation;
        this.secondDesignation = secondDesignation;
        this.codeSaisi = codeSaisi;
        this.quantite = quantite;
    }

    public MvtStoPREditionDTO() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getArticleID() {
        return articleID;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public String getLotinter() {
        return lotinter;
    }

    public void setLotinter(String lotinter) {
        this.lotinter = lotinter;
    }

    public Date getDatPer() {
        return datPer;
    }

    public void setDatPer(Date datPer) {
        this.datPer = datPer;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public String getDesignationunite() {
        return Designationunite;
    }

    public void setDesignationunite(String Designationunite) {
        this.Designationunite = Designationunite;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSecondDesignation() {
        return secondDesignation;
    }

    public void setSecondDesignation(String secondDesignation) {
        this.secondDesignation = secondDesignation;
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

    public Integer getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(Integer codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
    }

    public String getDesignationEmplacement() {
        return designationEmplacement;
    }

    public void setDesignationEmplacement(String designationEmplacement) {
        this.designationEmplacement = designationEmplacement;
    }

}
