/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.csys.pharmacie.transfert.domain.Btbt;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

/**
 *
 * @author Farouk
 */
public interface BtbtRepository extends JpaRepository<Btbt, Integer> {

    @EntityGraph(value = "Btbt.numBT", type = EntityGraph.EntityGraphType.FETCH)
    List<Btbt> findByNumBTReturnIn(List<String> numFactureBT);

}
