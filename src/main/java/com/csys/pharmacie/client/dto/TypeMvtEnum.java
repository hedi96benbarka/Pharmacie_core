/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.dto;

/**
 *
 * @author khouloud
 */
public enum TypeMvtEnum {
    Prelevement("Prelevement"),
    Vente("Vente");
    private String stringValue;

    private TypeMvtEnum() {
    }

    private TypeMvtEnum(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

}
