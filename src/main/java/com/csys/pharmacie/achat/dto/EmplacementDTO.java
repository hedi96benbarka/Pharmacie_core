/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
public class EmplacementDTO {

    private Integer code;
    @NotNull
    @Size(min = 1, max = 150)
    private String designation;

    @NotNull
    @Size(min = 1, max = 150)
    private String designationSec;

    @NotNull
    private boolean actif;

    @Size(min = 1, max = 50)
    private String codeSaisi;

    private DepartementDTO codeDepartement;

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public DepartementDTO getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(DepartementDTO codeDepartement) {
        this.codeDepartement = codeDepartement;
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

}
