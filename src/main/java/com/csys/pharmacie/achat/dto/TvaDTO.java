/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class TvaDTO {

    @NotNull
    private Integer code;
    @NotNull
    private BigDecimal valeur;

    public TvaDTO() {
    }

    public TvaDTO(Integer code) {
        this.code = code;
    }

    public TvaDTO(Integer code, BigDecimal valeur) {
        super();
        this.code = code;
        this.valeur = valeur;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return "TvaDTO{" + "code=" + code + ", valeur=" + valeur + '}';
    }

}
