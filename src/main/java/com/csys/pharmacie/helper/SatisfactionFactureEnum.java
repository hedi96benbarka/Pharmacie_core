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
public enum SatisfactionFactureEnum {
    RECOVRED("RECOVRED"),
    NOT_RECOVRED("NOT_RECOVRED");
    private String satisfaction;

    //Constructeur
    SatisfactionFactureEnum(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String satisfaction() {
        return satisfaction;
    }
}
