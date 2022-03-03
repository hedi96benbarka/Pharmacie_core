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
public enum CategorieDepotEnum {
    //Objets directement construits
    PH("PH"),
    UU("UU"),
    EC("EC"),
    IMMO("IMMO");
//    CA("CA");//CA for Canceled items. Don't use it .created for an exceptionnal case 
    private final String categ;

    //Constructeur
    CategorieDepotEnum(String categ) {
        this.categ = categ;
    }

    public String categ() {
        return categ;
    }


}
