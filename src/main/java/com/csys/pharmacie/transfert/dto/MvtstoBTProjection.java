/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Farouk
 */
public interface MvtstoBTProjection {

    BigDecimal getQuantite();

    String getDesart();

    BigDecimal getMontht();

    @Value("#{target.tautva}")
    BigDecimal getTauTVA();

    String getCodtva();

    BigDecimal getPriuni();

    @Value("#{target.pk.codart} ")
    String getRefArt();
    
    Date getDatPer();
}
