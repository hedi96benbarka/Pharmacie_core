/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.time.LocalDateTime;

/**
 *
 * @author Administrateur
 */
public class DateInv {
    private Integer coddep;
    private LocalDateTime dateInv;

    public DateInv(Integer coddep, LocalDateTime dateInv) {
        this.coddep = coddep;
        this.dateInv = dateInv;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public LocalDateTime getDateInv() {
        return dateInv;
    }

    public void setDateInv(LocalDateTime dateInv) {
        this.dateInv = dateInv;
    }
    
    
    
}
