package com.csys.pharmacie.achat.dto;

import java.io.Serializable;
import org.hibernate.validator.constraints.NotEmpty;

public class CategorieDepotDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String code;
    private String description;
    @NotEmpty
    private String designation;
    @NotEmpty
    private String designationSec;
    private boolean actif;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public String getCodeSaisi() {
        return code;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.code = codeSaisi;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
