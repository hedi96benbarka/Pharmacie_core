/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

/**
 *
 * @author admin
 */
public enum EnumEtatPatient {
    CODE_ETAT_PATIENT_RESIDENT("0"),
    CODE_ETAT_PATIENT_FERME("1"),
    CODE_ETAT_PATIENT_SUSPENDU("2");
    private String name = "";

    //Constructeur
    EnumEtatPatient(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Integer intValue() {
        return Integer.parseInt(this.toString());
    }
}
