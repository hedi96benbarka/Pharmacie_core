/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.dto;

/**
 *
 * @author Administrateur
 */
public enum TypeEnvoieEtatEnum {
    AE("AE"),
    SE("SE"),
    ALL("ALL");
    private String typeEnvoie;
    
    TypeEnvoieEtatEnum (String typeEnvoie ){
        this.typeEnvoie = typeEnvoie ;  
    }
    
    public String typeEnvoie(){
        return typeEnvoie ; 
    }
    
}
