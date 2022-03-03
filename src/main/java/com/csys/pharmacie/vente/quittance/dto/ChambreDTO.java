/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import javax.persistence.Basic;

/**
 *
 * @author Administrateur
 */
public class ChambreDTO {
     private Integer code;
    @Basic(optional = false)

    private String numeroChambre;
    @Basic(optional = false)
    private DepartementDTO departement;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getNumeroChambre() {
        return numeroChambre;
    }

    public void setNumeroChambre(String numeroChambre) {
        this.numeroChambre = numeroChambre;
    }

    public DepartementDTO getDepartement() {
        return departement;
    }

    public void setDepartement(DepartementDTO departement) {
        this.departement = departement;
    }
    


}
