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
public enum MethodeTraitementdeStockEnum {
    FIFO("FIFO"),
    FEFO("FEFO");
    private String methode;

    //Constructeur
    MethodeTraitementdeStockEnum(String methode) {
        this.methode = methode;
    }

    public String methode() {
        return methode;
    }
}
