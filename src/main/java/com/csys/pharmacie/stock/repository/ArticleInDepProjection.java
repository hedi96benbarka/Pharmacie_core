/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.repository;

import java.math.BigDecimal;


/**
 *
 * @author Farouk
 */
public interface ArticleInDepProjection {

    public Integer getId();

    public String getDesart();

    public BigDecimal getQte();

    public BigDecimal getPriuni();

//    public Tva getCodtva();
  
    
}
