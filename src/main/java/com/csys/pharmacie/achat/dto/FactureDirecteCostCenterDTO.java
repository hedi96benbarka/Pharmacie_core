package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class FactureDirecteCostCenterDTO {

    @NotNull
    private BigDecimal valueTTC;

    private String numeroFactureDirecte;

    private String numeroAfficheFactureDirecte;

    @NotNull
    private Integer codeCostCenter;

    private String designationCostCenter;

    private String codeSaisiCostCenter;
    
    private BigDecimal proportionCostCenter;

    public BigDecimal getValueTTC() {
        return valueTTC;
    }

    public void setValueTTC(BigDecimal valueTTC) {
        this.valueTTC = valueTTC;
    }

    public String getNumeroFactureDirecte() {
        return numeroFactureDirecte;
    }

    public void setNumeroFactureDirecte(String numeroFactureDirecte) {
        this.numeroFactureDirecte = numeroFactureDirecte;
    }

    public String getNumeroAfficheFactureDirecte() {
        return numeroAfficheFactureDirecte;
    }

    public void setNumeroAfficheFactureDirecte(String numeroAfficheFactureDirecte) {
        this.numeroAfficheFactureDirecte = numeroAfficheFactureDirecte;
    }

    public Integer getCodeCostCenter() {
        return codeCostCenter;
    }

    public void setCodeCostCenter(Integer codeCostCenter) {
        this.codeCostCenter = codeCostCenter;
    }

    public String getDesignationCostCenter() {
        return designationCostCenter;
    }

    public void setDesignationCostCenter(String designationCostCenter) {
        this.designationCostCenter = designationCostCenter;
    }

    public String getCodeSaisiCostCenter() {
        return codeSaisiCostCenter;
    }

    public void setCodeSaisiCostCenter(String codeSaisiCostCenter) {
        this.codeSaisiCostCenter = codeSaisiCostCenter;
    }

    public BigDecimal getProportionCostCenter() {
        return proportionCostCenter;
    }

    public void setProportionCostCenter(BigDecimal proportionCostCenter) {
        this.proportionCostCenter = proportionCostCenter;
    }

    
}
