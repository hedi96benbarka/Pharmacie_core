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
public enum TypeDateEnum {
    WITHOUT_DATE("WITHOUT_DATE"),
    WITH_DATE_MVTSTO("WITH_DATE_MVTSTO"),
    WITH_DATE_DETAIL("WITH_DATE_DETAIL");
    private String typeDate;

    //Constructeur
    TypeDateEnum(String typeDate) {
        this.typeDate = typeDate;
    }

    public String typeDate() {
        return typeDate;
    }
}
