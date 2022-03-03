package com.csys.pharmacie.achat.domain;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.csys.pharmacie.helper.Mouvement;
import java.time.LocalDate;
import java.time.LocalTime;

public class MouvementAvoir extends Mouvement{
	
	private static DateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	
	public MouvementAvoir (LocalDate date, String codfrs, String raisoc, String numbon, BigDecimal prix, BigDecimal quantite,LocalTime heureSys) throws ParseException {
//			this.setDate(date);
			this.setLibelle(codfrs+' '+raisoc);
			this.setOperation("AVOIR FOURNISSEUR");
			this.setNumbon(numbon); 
//			this.setPrix(prix); 
//			this.setEntree(BigDecimal.ZERO); 
//			this.setSortie(quantite);
		}
}
