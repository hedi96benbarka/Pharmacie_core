package com.csys.pharmacie.vente.quittance.dto;

import java.time.LocalDate;
import java.util.Date;

public class PatientDto {

	private String nom;
	private String prenom;
	private LocalDate dateNaissance;
	private String numChambre;
	
	
	public PatientDto() {
	}
	public PatientDto(String nom, String prenom, LocalDate dateNaissance, String numChambre) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.dateNaissance = dateNaissance;
		this.numChambre = numChambre;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public LocalDate getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public String getNumChambre() {
		return numChambre;
	}
	public void setNumChambre(String numChambre) {
		this.numChambre = numChambre;
	}
	
}
