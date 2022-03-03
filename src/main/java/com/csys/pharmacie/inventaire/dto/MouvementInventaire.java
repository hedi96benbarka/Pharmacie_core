package com.csys.pharmacie.inventaire.dto;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.csys.pharmacie.helper.Mouvement;
import java.time.LocalDate;

public class MouvementInventaire extends Mouvement{
	private static DateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	
	public MouvementInventaire (LocalDate date, BigDecimal quantite) throws ParseException {
//			this.setDate( date );
			this.setLibelle("INVENTAIRE LE"+ formatter.format(date));
			this.setNumbon(""); 
			this.setOperation("");
//			this.setEntree(quantite); 
//			this.setSortie(BigDecimal.ZERO);
//			this.setPrix(BigDecimal.ZERO); 
		}
}
