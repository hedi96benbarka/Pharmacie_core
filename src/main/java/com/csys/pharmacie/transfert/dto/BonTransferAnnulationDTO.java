/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.dto;

import java.util.List;

/**
 *
 * @author Farouk
 */
public class BonTransferAnnulationDTO extends FactureBTDTO {

   
    private List<String> relativesFactureBTs;
    private List<FactureBTDTO> factureBTs;
    private Boolean avoirSuiteValidation;

    public BonTransferAnnulationDTO(Integer sourceID, Integer destinationID) {
        super(sourceID, destinationID);
    }

    public BonTransferAnnulationDTO() {
    }

    public List<FactureBTDTO> getFactureBTs() {
        return factureBTs;
    }

    public void setFactureBTs(List<FactureBTDTO> factureBTs) {
        this.factureBTs = factureBTs;
    }

    public List<String> getRelativesFactureBTs() {
        return relativesFactureBTs;
    }

    public void setRelativesFactureBTs(List<String> relativesFactureBTs) {
        this.relativesFactureBTs = relativesFactureBTs;
    }

    public Boolean getAvoirSuiteValidation() {
        return avoirSuiteValidation;
    }

    public void setAvoirSuiteValidation(Boolean avoirSuiteValidation) {
        this.avoirSuiteValidation = avoirSuiteValidation;
    }

}
