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
public enum TypeBonEnum {
    RC("RC"), // Receveing
    BA("BA"), // Reception
    RT("RT"),// Retour
    BR("BR"),
    BE("BE"),// Redressement
    AA("AA"),
    BT("BT"), // transfert
    FC("FC"),
    FE("FE"),
    PR("PR"), // prelevement
    RPR("RPR"),// retour prelevement
    DC("DC"), // decoupage
    AV("AV"), // avoir client
    IN("IN"),
    RP("RP"),
    PA("PA"),
    DR("DR"),
    DIR("DIR"),//direct
    FBR("FBR"),// facture bon reception
    AJ("AJ"),
    AF("AF"), // ajustement 
    FRP("FRP"), //facture retour perime
    BAT("BAT") , // Reception temprelle
    ROP("ROP"),
    TCB("TCB"),//transgert company branch
    TBC("TBC");//RETURN transger company branch
    private String type;

    //Constructeur
    TypeBonEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
    }
