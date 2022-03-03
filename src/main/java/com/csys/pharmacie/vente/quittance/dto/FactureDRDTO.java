/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Administrateur
 */
public class FactureDRDTO {

    @NotNull
    private String numbon;

    @Valid
    private List<MvtStoDrPKDTO> listMvtStoDr;

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

    public List<MvtStoDrPKDTO> getListMvtStoDr() {
        return listMvtStoDr;
    }

    public void setListMvtStoDr(List<MvtStoDrPKDTO> listMvtStoDr) {
        this.listMvtStoDr = listMvtStoDr;
    }

}
