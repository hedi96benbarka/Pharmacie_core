/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

/**
 *
 * @author Administrateur
 */
public class CategorieArticleWithRoot {

    private Integer code;
    private Integer parent;
    private Integer root;
    private PricelisteParCategorieArticleDTO priceListe;

    public CategorieArticleWithRoot() {
    }
    
    

    public CategorieArticleWithRoot(Integer code, Integer parent) {
        this.code = code;
        this.parent = parent;
    }

    public CategorieArticleWithRoot(Integer code, Integer parent, Integer root) {
        this.code = code;
        this.parent = parent;
        this.root = root;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getRoot() {
        return root;
    }

    public void setRoot(Integer root) {
        this.root = root;
    }

    public PricelisteParCategorieArticleDTO getPriceListe() {
        return priceListe;
    }

    public void setPriceListe(PricelisteParCategorieArticleDTO priceListe) {
        this.priceListe = priceListe;
    }

}
