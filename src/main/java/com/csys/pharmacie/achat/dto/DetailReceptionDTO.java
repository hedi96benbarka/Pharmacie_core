/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;


/**
 *
 * @author Farouk
 */
public class DetailReceptionDTO extends MvtstoBADTO {
    private BigDecimal qteCom;//temporary problem of constructor

    public DetailReceptionDTO() {
    }
    
   public DetailReceptionDTO(BigDecimal qteCom) {
        this.qteCom = qteCom;
    }

   
//    private BigDecimal qteReturned;
////    private Boolean free;
////    private String unitDesignation;
//    private BigDecimal qteCom;
//
    
//    public Boolean getFree() {
//        return free;
//    }
//
//    public void setFree(Boolean free) {
//        this.free = free;
//    }

    public BigDecimal getQteCom() {
        return qteCom;
    }

    public void setQteCom(BigDecimal qteCom) {
        this.qteCom = qteCom;
    }



//    public BigDecimal getQteReturned() {
//        return qteReturned;
//    }
//
//    public void setQteReturned(BigDecimal qteReturned) {
//        this.qteReturned = qteReturned;
//    }
//
////    public String getUnitDesignation() {
////        return unitDesignation;
////    }
////
////    public void setUnitDesignation(String unitDesignation) {
////        this.unitDesignation = unitDesignation;
////    }
//
//    public BigDecimal getQteCom() {
//        return qteCom;
//    }
//
//    public void setQteCom(BigDecimal qteCom) {
//        this.qteCom = qteCom;
//    }
//
//    @Override
//    public String toString() {
//        return "DetailReceptionDTO{" + "qteReturned=" + qteReturned + '}';
//    }

    @Override
    public String toString() {
        return "DetailReceptionDTO{" + "qteCom=" + qteCom + '}';
    }

  

    }
