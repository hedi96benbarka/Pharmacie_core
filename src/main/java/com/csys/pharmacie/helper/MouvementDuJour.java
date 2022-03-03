package com.csys.pharmacie.helper;

import java.math.BigDecimal;

public class MouvementDuJour {

	private Long nombre;
	private BigDecimal valeur;
	
	
	
	public MouvementDuJour(Long nombre, BigDecimal valeur) {
		this.nombre = nombre;
		this.valeur = valeur;
	}
	public Long getNombre() {
		return nombre;
	}
	public void setNombre(Long nombre) {
		this.nombre = nombre;
	}
	public BigDecimal getValeur() {
		return valeur;
	}
	public void setValeur(BigDecimal valeur) {
		this.valeur = valeur;
	}
	
}
