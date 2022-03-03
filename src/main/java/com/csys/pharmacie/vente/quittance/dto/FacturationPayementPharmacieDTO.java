/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import java.util.List;

/**
 *
 * @author Administrateur
 */
public class FacturationPayementPharmacieDTO {

    private List<DetailsAdmissionDTO> detailsAdmissionDTOs;
    private String[] codesReglement;

    public List<DetailsAdmissionDTO> getDetailsAdmissionDTOs() {
        return detailsAdmissionDTOs;
    }

    public void setDetailsAdmissionDTOs(List<DetailsAdmissionDTO> detailsAdmissionDTOs) {
        this.detailsAdmissionDTOs = detailsAdmissionDTOs;
    }

    public String[] getCodesReglement() {
        return codesReglement;
    }

    public void setCodesReglement(String[] codesReglement) {
        this.codesReglement = codesReglement;
    }

}
