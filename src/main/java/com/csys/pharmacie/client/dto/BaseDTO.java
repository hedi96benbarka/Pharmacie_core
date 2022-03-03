/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.dto;


/**
 *
 * @author Farouk
 */
public class BaseDTO {

    private Integer code;
    private String designation;
    private String designationSec;
    private Boolean actif;

    
    public BaseDTO(Integer code, String designation, String designationSec, Boolean actif) {
        this.code = code;
        this.designation = designation;
        this.designationSec = designationSec;
        this.actif = actif;
    }

    public BaseDTO(String designation, String designationSec, Boolean actif) {
        this.designation = designation;
        this.designationSec = designationSec;
        this.actif = actif;
    }

    public BaseDTO() {
    }

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

    public String getDesignationSec() {
        return designationSec;
    }

    public void setDesignationSec(String designationSec) {
        this.designationSec = designationSec;
    }

    public Boolean isActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "BaseDTO{" + "code=" + code + ", designation=" + designation + ", designationSec=" + designationSec + ", actif=" + actif + '}';
    }

}
