/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.stock.domain.ValeurStockGlobale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 *
 * @author Administrateur
 */
public interface ValeurStockGlobaleRepository extends JpaRepository<ValeurStockGlobale, String>, QueryDslPredicateExecutor<ValeurStockGlobale> {

}
