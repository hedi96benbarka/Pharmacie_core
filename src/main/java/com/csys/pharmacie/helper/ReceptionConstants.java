/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

/**
 *
 * @author Administrateur
 */
public enum ReceptionConstants {
    CODE_ETAT_PATIENT_RESIDENT(0),
    CODE_ETAT_PATIENT_SUSPENDU(2),
    CODE_ETAT_PATIENT_FERME(1);

    private final Integer code;

    //Constructeur
    ReceptionConstants(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return code;
    }
}
