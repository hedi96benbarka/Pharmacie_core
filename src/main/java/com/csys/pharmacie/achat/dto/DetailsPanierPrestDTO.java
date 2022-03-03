/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
public class DetailsPanierPrestDTO {
    
    @NotNull
    private Boolean facturationPanier;
    @NotNull
    private Boolean modifPanierQteCategorie;
    @NotNull
    private BigDecimal prixFixePanier;
    
    @NotNull
    List<DetailsPanierPrestationPHDTO> detailsPanierPrestationDTOMedicaments;
    @NotNull
    List<DetailsPanierPrestationUUDTO> detailsPanierPrestationDTOUsageUnique;


    public DetailsPanierPrestDTO() {
    }

    public List<DetailsPanierPrestationPHDTO> getDetailsPanierPrestationDTOMedicaments() {
        return detailsPanierPrestationDTOMedicaments;
    }

    public void setDetailsPanierPrestationDTOMedicaments(List<DetailsPanierPrestationPHDTO> detailsPanierPrestationDTOMedicaments) {
        this.detailsPanierPrestationDTOMedicaments = detailsPanierPrestationDTOMedicaments;
    }

    public List<DetailsPanierPrestationUUDTO> getDetailsPanierPrestationDTOUsageUnique() {
        return detailsPanierPrestationDTOUsageUnique;
    }

    public void setDetailsPanierPrestationDTOUsageUnique(List<DetailsPanierPrestationUUDTO> detailsPanierPrestationDTOUsageUnique) {
        this.detailsPanierPrestationDTOUsageUnique = detailsPanierPrestationDTOUsageUnique;
    }

    public Boolean getFacturationPanier() {
        return facturationPanier;
    }

    public void setFacturationPanier(Boolean facturationPanier) {
        this.facturationPanier = facturationPanier;
    }

    public Boolean getModifPanierQteCategorie() {
        return modifPanierQteCategorie;
    }

    public void setModifPanierQteCategorie(Boolean modifPanierQteCategorie) {
        this.modifPanierQteCategorie = modifPanierQteCategorie;
    }

    public BigDecimal getPrixFixePanier() {
        return prixFixePanier;
    }

    public void setPrixFixePanier(BigDecimal prixFixePanier) {
        this.prixFixePanier = prixFixePanier;
    }

    @Override
    public String toString() {
        return "DetailsPanierPrestDTO{" + "facturationPanier=" + facturationPanier + ", modifPanierQteCategorie=" + modifPanierQteCategorie + ", prixFixePanier=" + prixFixePanier + ", detailsPanierPrestationDTOMedicaments=" + detailsPanierPrestationDTOMedicaments + ", detailsPanierPrestationDTOUsageUnique=" + detailsPanierPrestationDTOUsageUnique + '}';
    }   

}
