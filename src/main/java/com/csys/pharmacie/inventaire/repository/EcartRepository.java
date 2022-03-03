/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csys.pharmacie.inventaire.dto.MouvementInventaire;
import com.csys.pharmacie.inventaire.domain.Ecart;

/**
 *
 * @author Farouk
 */
public interface EcartRepository extends JpaRepository<Ecart, Long> {
	
	
	   @Query("SELECT NEW com.csys.pharmacie.inventaire.dto.MouvementInventaire( m.datinv, SUM(m.qteinv)) FROM Ecart m"+
	        	" WHERE  m.codart = ?1  and m.coddep = ?2 and m.datinv BETWEEN ?3 and ?4 group by m.datinv")    
		public List<MouvementInventaire> findListMouvement(String codart, String coddep, Date datedeb, Date datefin);


}
