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
public enum NatureExceptionPriceListEnum {
    //Objets directement construits
    REM("REM"),
    MAJ("MAJ"),
    VAL("VAL");
    private String nature;

    //Constructeur
    NatureExceptionPriceListEnum(String nature) {
        this.nature = nature;
    }

    public String categ() {
        return nature;
    }
}
