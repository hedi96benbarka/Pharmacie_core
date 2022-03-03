/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.AjustementAvoirFournisseur;
import com.csys.pharmacie.achat.domain.AjustementRetourFournisseurPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;


/**
 *
 * @author DELL
 */
@Repository
public interface AjustementAvoirFournisseurRepository extends JpaRepository<AjustementAvoirFournisseur, AjustementRetourFournisseurPK> {
    
}
