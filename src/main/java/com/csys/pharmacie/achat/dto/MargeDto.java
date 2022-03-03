/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;

public class MargeDto {

	private Long id;
	private BigDecimal du;
	private BigDecimal au;
	private BigDecimal marge;
	
	
	
	public MargeDto(Long id, BigDecimal du, BigDecimal au, BigDecimal marge) {
		super();
		this.id = id;
		this.du = du;
		this.au = au;
		this.marge = marge;
	}


	public MargeDto() {
		
	}
	
	
	public BigDecimal getMarge() {
		return marge;
	}
	public void setMarge(BigDecimal marge) {
		this.marge = marge;
	}
	public BigDecimal getAu() {
		return au;
	}
	public void setAu(BigDecimal au) {
		this.au = au;
	}
	public BigDecimal getDu() {
		return du;
	}
	public void setDu(BigDecimal du) {
		this.du = du;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	
}