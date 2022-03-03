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
public enum EnumNatureAdmission {
    CODE_NATURE_ADMISSION_INPATIENT("1"),
    CODE_NATURE_ADMISSION_ER("3"),
    CODE_NATURE_ADMISSION_OPD("5"),
    CODE_NATURE_ADMISSION_PHARMACIE("6"),
    CODE_NATURE_ADMISSION_POS("7");

    private String name = "";

    //Constructeur
    EnumNatureAdmission(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Integer intValue() {
        return Integer.parseInt(this.toString());
    }
}
