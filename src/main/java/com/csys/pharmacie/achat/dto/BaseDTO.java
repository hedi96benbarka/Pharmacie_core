/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Farouk
 */
public  class BaseDTO {

    @Id
    protected Integer code;

    @NotNull
    @Size(min = 1, max = 50)
    protected String designation;

    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty(notes = "The name of the user", required = true)
    protected String designationSec;

    @NotNull
    protected boolean actif;

    public BaseDTO(String designation, String designationSec, boolean actif) {
        this.designation = designation;
        this.designationSec = designationSec;
        this.actif = actif;
    }

    public BaseDTO(Integer code, String designation) {
        this.code = code;
        this.designation = designation;
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

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "BaseDTO{" + "code=" + code + ", designation=" + designation + '}';
    }

}
