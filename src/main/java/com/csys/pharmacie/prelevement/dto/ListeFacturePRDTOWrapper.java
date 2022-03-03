/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.dto;

import java.util.List;

/**
 *
 * @author DELL
 */
public class ListeFacturePRDTOWrapper {

    private List<FacturePRDTO> listeFacturePRDTO;

    public ListeFacturePRDTOWrapper(List<FacturePRDTO> listeFacturePRDTO) {
        this.listeFacturePRDTO = listeFacturePRDTO;
    }

    public ListeFacturePRDTOWrapper() {
    }

    public List<FacturePRDTO> getListeFacturePRDTO() {
        return listeFacturePRDTO;
    }

    public void setListeFacturePRDTO(List<FacturePRDTO> listeFacturePRDTO) {
        this.listeFacturePRDTO = listeFacturePRDTO;
    }

}
