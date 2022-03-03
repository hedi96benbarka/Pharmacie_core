package com.csys.pharmacie.transfert.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;


public interface MvtStoBEProjection {
    @Value("#{target.mvtStoBEPK.codart} ")
    String getRefart();
	String getDesart();
	String getLot();
	BigDecimal getPriuni();
	BigDecimal getQuantite();
	BigDecimal getTautva();	
	BigDecimal getCodtva();	
	BigDecimal getMontht();
	Date getDatPer();
}
