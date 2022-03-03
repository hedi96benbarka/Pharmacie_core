/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.BaseTVADTO;
import java.math.BigDecimal;

/**
 *
 * @author DELL
 */
public class BaseTvaReceptionDTO extends BaseTVADTO {

    private BigDecimal baseTvaGratuite;
    
    private BigDecimal montantTvaGratuite;

    public BigDecimal getBaseTvaGratuite() {
        return baseTvaGratuite;
    }

    public void setBaseTvaGratuite(BigDecimal baseTvaGratuite) {
        this.baseTvaGratuite = baseTvaGratuite;
    }

    public BigDecimal getMontantTvaGratuite() {
        return montantTvaGratuite;
    }

    public void setMontantTvaGratuite(BigDecimal montantTvaGratuite) {
        this.montantTvaGratuite = montantTvaGratuite;
    }

    @Override
    public String toString() {
        return "BaseTvaReceptionDTO{" + "baseTvaGratuite=" + baseTvaGratuite + ", montantTvaGratuite=" + montantTvaGratuite + '}';
    }
    
}
