/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.util.List;

public class MaquetteDto {
	private Integer id;
	private List<MargeDto> marges;
	private List<Integer> depots;
	
	
	public List<Integer> getDepots() {
		return depots;
	}
	public void setDepots(List<Integer> depots) {
		this.depots = depots;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<MargeDto> getMarges() {
		return marges;
	}
	public void setMarges(List<MargeDto> marges) {
		this.marges = marges;
	}

} 