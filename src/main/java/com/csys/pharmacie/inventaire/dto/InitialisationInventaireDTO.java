/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author DELL
 */
public class InitialisationInventaireDTO {

    @NotNull
    private Integer coddep;
    @NotNull
    private CategorieDepotEnum categ_depot;
    @NotNull
    private Integer codCatArticle;
    
    private Boolean isDemarrage;
    private List<String> factureBTs;

    public InitialisationInventaireDTO(Integer coddep, CategorieDepotEnum categ_depot, Integer codCatArticle, List<String> factureBTs) {
        this.coddep = coddep;
        this.categ_depot = categ_depot;
        this.codCatArticle = codCatArticle;
        this.factureBTs = factureBTs;
    }

    public InitialisationInventaireDTO() {
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public CategorieDepotEnum getCateg_depot() {
        return categ_depot;
    }

    public void setCateg_depot(CategorieDepotEnum categ_depot) {
        this.categ_depot = categ_depot;
    }

    public Integer getCodCatArticle() {
        return codCatArticle;
    }

    public void setCodCatArticle(Integer codCatArticle) {
        this.codCatArticle = codCatArticle;
    }

    public List<String> getFactureBTs() {
        return factureBTs;
    }

    public void setFactureBTs(List<String> factureBTs) {
        this.factureBTs = factureBTs;
    }

    public Boolean getIsDemarrage() {
        return isDemarrage;
    }

    public void setIsDemarrage(Boolean isDemarrage) {
        this.isDemarrage = isDemarrage;
    }

}
