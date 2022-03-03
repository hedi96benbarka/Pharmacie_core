package com.csys.pharmacie.helper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Farouk
 */
public interface MvtstoProjection {

    boolean getFodart();

    @Value("#{target.qtecom}")
    BigDecimal getQuantite();

    String getDesart();

    BigDecimal getRemise();

    BigDecimal getMontht();

    @Value("#{target.tautva}")
    BigDecimal getTauTVA();

    String getCodtva();

    BigDecimal getPriuni();

    @Value("#{target.codart.codart}")
    String getRefArt();
}
