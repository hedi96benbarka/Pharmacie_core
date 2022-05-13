/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.repository;

import java.util.Date;

import com.csys.pharmacie.transfert.domain.DetailMvtStoBE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//import com.csys.pharmacie.transfert.dto.FactureBEProjection;
import com.csys.pharmacie.transfert.domain.FactureBE;

import com.csys.pharmacie.helper.MouvementDuJour;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 *
 * @author Farouk
 */
public interface FactureBERepository extends JpaRepository<FactureBE, String>, QueryDslPredicateExecutor<FactureBE> {

//    List< FactureBEProjection> findByCoddepAndDatbonBetweenOrderByDatbonDescNumafficheDesc(String coddep, Date deb, Date fin);
//
//    List< FactureBEProjection> findByDatbonBetweenOrderByDatbonDescNumafficheDesc(Date deb, Date fin);

    FactureBE findByNumbon(String numbon);


   @Deprecated 
    @Query(value = "SELECT new com.csys.pharmacie.helper.MouvementDuJour(COUNT(f.numbon),coalesce(SUM(f.mntbon),0)) FROM FactureBE f  WHERE f.datesys=?1 ")
    public MouvementDuJour findMouvementDuJour(Date date, Boolean stup);

}
