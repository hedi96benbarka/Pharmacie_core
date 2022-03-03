/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import java.math.BigDecimal;

/**
 *
 * @author Administrateur
 */
public class RotationStockDTO {

    private  String categArticle;
    
    private Integer codeArticle;

    private String codeSaisiArticle;

    private String designationArticle;

    private Integer codeUnite;

    private String designationUnite;
    
    private BigDecimal consommationReel;
    
    private BigDecimal stockActuel ;

    public RotationStockDTO() {
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getCodeSaisiArticle() {
        return codeSaisiArticle;
    }

    public void setCodeSaisiArticle(String codeSaisiArticle) {
        this.codeSaisiArticle = codeSaisiArticle;
    }

    public String getDesignationArticle() {
        return designationArticle;
    }

    public void setDesignationArticle(String designationArticle) {
        this.designationArticle = designationArticle;
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

    public BigDecimal getConsommationReel() {
        return consommationReel;
    }

    public void setConsommationReel(BigDecimal consommationReel) {
        this.consommationReel = consommationReel;
    }

    public BigDecimal getStockActuel() {
        return stockActuel;
    }

    public void setStockActuel(BigDecimal stockActuel) {
        this.stockActuel = stockActuel;
    }

    public String getCategArticle() {
        return categArticle;
    }

    public void setCategArticle(String categArticle) {
        this.categArticle = categArticle;
    }
    
    
    
}
