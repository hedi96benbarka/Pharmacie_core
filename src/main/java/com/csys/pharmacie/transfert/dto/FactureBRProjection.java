package com.csys.pharmacie.transfert.dto;

import java.math.BigDecimal;
import java.util.Date;


public interface FactureBRProjection {
	String getNumaffiche();
	Date getDatbon();
	String getCoddep();
	String getDesdepd();
	BigDecimal getMntbon();
	String getMemop();
	String getNumbon();
}
