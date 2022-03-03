/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import java.math.BigDecimal;

/**
 *
 * @author admin
 */
public interface QuantityItemInStock {

    Integer getCodeArticle();

    Integer getCodeUnite();

    Integer getCodeDepot();
    
    BigDecimal getQuantite();
    

}
