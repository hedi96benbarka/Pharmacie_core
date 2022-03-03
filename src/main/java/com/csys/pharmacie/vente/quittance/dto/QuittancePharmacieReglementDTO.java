package com.csys.pharmacie.vente.quittance.dto;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.csys.pharmacie.helper.BaseAvoirQuittance;
import java.util.List;

/**
 *
 * @author admin
 */
public class QuittancePharmacieReglementDTO<T extends BaseAvoirQuittance> {

    private List<T> quittancePharmacieDTOs;

    private List<ReglementDTO> reglementDTOs;

    private Long codeDetailsAdmissionPosDelivery;
    
    public List<T> getQuittancePharmacieDTOs() {
        return quittancePharmacieDTOs;
    }

    public void setQuittancePharmacieDTOs(List<T> quittancePharmacieDTOs) {
        this.quittancePharmacieDTOs = quittancePharmacieDTOs;
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

    
    @Override
    public String toString() {
        return "{" + "quittancePharmacieDTOs:" + quittancePharmacieDTOs + ", reglementDTOs:" + reglementDTOs + '}';
    }

}
