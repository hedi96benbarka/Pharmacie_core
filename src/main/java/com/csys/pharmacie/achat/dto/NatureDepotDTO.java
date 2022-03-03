/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Administrateur
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NatureDepotDTO {

    private CategorieDepotEnum categorieDepot;
    private Integer prestationID;

    public CategorieDepotEnum getCategorieDepot() {
        return categorieDepot;
    }

    public void setCategorieDepot(CategorieDepotEnum categorieDepot) {
        this.categorieDepot = categorieDepot;
    }

    public Integer getPrestationID() {
        return prestationID;
    }

    public void setPrestationID(Integer prestationID) {
        this.prestationID = prestationID;
    }

}
