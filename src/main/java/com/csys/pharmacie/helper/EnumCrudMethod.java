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
public enum EnumCrudMethod {
    CREATE("CREATE"),
    READ("READ"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String crudMethod;

    EnumCrudMethod(String crudMethod) {
        this.crudMethod = crudMethod;
    }

    public String getCrudMethod() {
        return crudMethod;
    }
}