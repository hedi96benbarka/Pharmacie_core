/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.csys.pharmacie.transfert.domain.Btfe;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author Farouk
 */
public interface BtfeRepository extends JpaRepository<Btfe, Integer>, QueryDslPredicateExecutor<Btfe> {

    
    @Async
    CompletableFuture<List<Btfe>> findByNumFEIn(List<String> numsQuittances);
   
}
