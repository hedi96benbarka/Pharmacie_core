/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Farouk
 */
public class QteMouvement{

    private Integer codart;
    private BigDecimal quantite;
    
    
    
	public QteMouvement(Integer codart, BigDecimal quantite) {
		this.codart = codart;
		this.quantite = quantite;
	}
	public QteMouvement() {
	}
	public Integer getCodart() {
		return codart;
	}
	public void setCodart(Integer codart) {
		this.codart = codart;
	}

	public BigDecimal getQuantite() {
		return quantite;
	}
	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}   




}
