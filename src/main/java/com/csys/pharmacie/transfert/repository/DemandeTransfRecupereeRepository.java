/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.csys.pharmacie.transfert.domain.DemandeRecup;

/**
 *
 * @author Farouk
 */
public interface DemandeTransfRecupereeRepository extends JpaRepository<DemandeRecup, Integer> {

    List<DemandeRecup> findByCodedemandeTrIn(List<Integer> codeDemandesTrs);

}
