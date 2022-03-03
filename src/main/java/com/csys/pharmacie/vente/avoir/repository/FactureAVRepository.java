/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csys.pharmacie.vente.avoir.domain.FactureAV;

import com.csys.pharmacie.helper.MouvementDuJour;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 *
 * @author Farouk
 */
public interface FactureAVRepository extends JpaRepository<FactureAV, String>, QueryDslPredicateExecutor<FactureAV> {

    @Query(value = "SELECT new com.csys.pharmacie.helper.MouvementDuJour(COUNT(f.numbon),coalesce(SUM(f.mntbon),0)) FROM FactureAV f  WHERE f.datesys=?1   ")
    public MouvementDuJour findMouvementDuJour(Date date);

    @Query(value = "SELECT MAX(f.numbon) FROM FactureAV f  WHERE f.categDepot = ?1")
    public String findMaxNumbonByCategDepot(CategorieDepotEnum categDepot);
    
    public List<FactureAV> findByNumbonComplementaire(String numbonComplementaire);
    
    @Query(value = "SELECT f.numbon FROM FactureAV f  WHERE f.numdoss = ?1")
    public List<String> findListNumBonsByNumDoss(String numDoss);

    public List<FactureAV> findByHashCodeAndDatbonGreaterThan(byte[] hashCode, LocalDateTime datbon);
}
