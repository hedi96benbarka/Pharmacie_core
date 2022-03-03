/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Farouk
 */
public interface FactureAAProjection {

//    @Value("#{target.detailFactureAACollection}")
//    MvtstoAADTO getDetails();

    BigDecimal getBase0tva00();
    BigDecimal getBase1tva06();

    BigDecimal getBase1tva10();

    BigDecimal getBase1tva18();

    String getCodfrs();

    String getCodvend();

    boolean getFodcli();

    String getMemop();

    BigDecimal getMntbon();

    BigDecimal getSmfodec();

    BigDecimal getTva10();

    BigDecimal getTva18();

    BigDecimal getTva6();
    
    Date getDatbon();
 
    BigDecimal getValrem();
    
    String getNumpiece();
}
