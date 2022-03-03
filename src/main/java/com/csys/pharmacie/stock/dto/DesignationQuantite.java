package com.csys.pharmacie.stock.dto;

import java.math.BigDecimal;

public class DesignationQuantite {
private String designation;
private BigDecimal quantite;



public DesignationQuantite(String designation, BigDecimal quantite) {
	this.designation = designation;
	this.quantite = quantite;
}
public BigDecimal getQuantite() {
	return quantite;
}
public void setQuantite(BigDecimal quantite) {
	this.quantite = quantite;
}
public String getDesignation() {
	return designation;
}
public void setDesignation(String designation) {
	this.designation = designation;
}


	
}
