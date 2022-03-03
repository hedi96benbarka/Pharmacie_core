package com.csys.pharmacie.vente.quittance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdmissionDTO {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private LitDTO lit;

    public LitDTO getLit() {
        return lit;
    }

    public void setLit(LitDTO lit) {
        this.lit = lit;
    }
}
