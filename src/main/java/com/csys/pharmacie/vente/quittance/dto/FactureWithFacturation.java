/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import com.csys.pharmacie.helper.BaseAvoirQuittance;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class FactureWithFacturation<T extends BaseAvoirQuittance> {

    List<T> quittances;
    FacturationPayementPharmacieDTO facture;

    public FactureWithFacturation() {
    }

    public FactureWithFacturation(List<T> quittances, FacturationPayementPharmacieDTO facture) {
        this.quittances = quittances;
        this.facture = facture;
    }

    public List<T> getQuittances() {
        return quittances;
    }

    public void setQuittances(List<T> quittances) {
        this.quittances = quittances;
    }

    public FacturationPayementPharmacieDTO getFacture() {
        return facture;
    }

    public void setFacture(FacturationPayementPharmacieDTO facture) {
        this.facture = facture;
    }

}
