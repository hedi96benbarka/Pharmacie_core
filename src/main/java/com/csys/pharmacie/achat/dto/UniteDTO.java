/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

/**
 *
 * @author Administrateur
 */
public class UniteDTO {

    private Integer code;

    private String designation;

    private String designationSec;

    public UniteDTO(Integer code, String designation, String designationSec) {
        this.code = code;
        this.designation = designation;
        this.designationSec = designationSec;
    }

    public UniteDTO() {
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

}
