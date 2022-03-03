/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.dto;

import java.math.BigDecimal;

/**
 *
 * @author admin
 */
public class OperationDTO {

    private Integer code;
    private String designation;
    private String designationAr;
    private Boolean facturationPanier;
    private BigDecimal prixFixePanier;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignationAr() {
        return designationAr;
    }

    public void setDesignationAr(String designationAr) {
        this.designationAr = designationAr;
    }

    public Boolean getFacturationPanier() {
        return facturationPanier;
    }

    public void setFacturationPanier(Boolean facturationPanier) {
        this.facturationPanier = facturationPanier;
    }

    public BigDecimal getPrixFixePanier() {
        return prixFixePanier;
    }

    public void setPrixFixePanier(BigDecimal prixFixePanier) {
        this.prixFixePanier = prixFixePanier;
    }

    @Override
    public String toString() {
        return "OperationDTO{" + "code=" + code + ", designation=" + designation + ", designationAr=" + designationAr + ", facturationPanier=" + facturationPanier + ", prixFixePanier=" + prixFixePanier + '}';
    }
    
    

}
