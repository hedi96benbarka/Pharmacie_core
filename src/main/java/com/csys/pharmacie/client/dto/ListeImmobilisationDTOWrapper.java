/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.dto;

import java.util.List;

/**
 *
 * @author DELL
 */
public class ListeImmobilisationDTOWrapper {
    private List<ImmobilisationDTO> Immobilisation ;

    public List<ImmobilisationDTO> getImmobilisation() {
        return Immobilisation;
    }

    public void setImmobilisation(List<ImmobilisationDTO> Immobilisation) {
        this.Immobilisation = Immobilisation;
    }

    @Override
    public String toString() {
        return "ListeImmobilisationDTOWrapper{" + "Immobilisation=" + Immobilisation + '}';
    }
    

    
}
