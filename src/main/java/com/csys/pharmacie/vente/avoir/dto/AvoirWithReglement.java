/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.dto;

import com.csys.pharmacie.vente.quittance.dto.ReglementDTO;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
public class AvoirWithReglement {

    @Valid
    @Size(min = 1)
    List<Avoir> avoirs;

    private List<ReglementDTO> reglementDTOs;
    
    private Long codeDetailsAdmissionPosDelivery;
    
    public List<Avoir> getAvoirs() {
        return avoirs;
    }

    public void setAvoirs(List<Avoir> avoirs) {
        this.avoirs = avoirs;
    }

    public List<ReglementDTO> getReglementDTOs() {
        return reglementDTOs;
    }

    public void setReglementDTOs(List<ReglementDTO> reglementDTOs) {
        this.reglementDTOs = reglementDTOs;
    }

    public Long getCodeDetailsAdmissionPosDelivery() {
        return codeDetailsAdmissionPosDelivery;
    }

    public void setCodeDetailsAdmissionPosDelivery(Long codeDetailsAdmissionPosDelivery) {
        this.codeDetailsAdmissionPosDelivery = codeDetailsAdmissionPosDelivery;
    }

    
}
