/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csys.pharmacie.vente.quittance.domain.Facture;

import com.csys.pharmacie.helper.MouvementDuJour;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 *
 * @author Farouk
 */
public interface FactureRepository extends JpaRepository<Facture, String>, QueryDslPredicateExecutor<Facture> {

    @EntityGraph(value = "Facture.mvtstoCollection", type = EntityGraph.EntityGraphType.LOAD)
    public List<Facture> findAll(Predicate prdct);

    public List<Facture> findByNumbonIn(List<String> codeList);

    public List<Facture> findByHashCodeAndDatbonGreaterThan(byte[] hashCode, LocalDateTime datbon);

    public List<Facture> findByNumdossAndCoddep(String numdoss, Integer coddep);

    @Query(value = "SELECT MAX(f.numbon) FROM Facture f  WHERE f.categDepot = ?1")
    public String findMaxNumbonByCategDepot(CategorieDepotEnum categDepot);

    @Query(value = "SELECT f.numdoss FROM Facture f  WHERE f.coddep = ?1")
    public List<String> findNumdossByCoddep(Integer coddep);

    @Query(value = "SELECT new com.csys.pharmacie.helper.MouvementDuJour(COUNT(f.numbon),coalesce(SUM(f.mntbon),0)) FROM Facture f  WHERE f.datesys >=?1 and f.typbon = 'FC'")
    public MouvementDuJour findQuittancesDuJour(Date date);

    public List<Facture> findByNumbonComplementaire(String numbonComplementaire);

    public List<Facture> findByCoddepAndCategDepotAndNumbonNotIn(Integer coddep, CategorieDepotEnum categDepot, List<String> numbonsInBtfe);

    @Query(value = "SELECT f.numbon FROM Facture f  WHERE f.numdoss = ?1 and f.codAnnul IS NULL")
    public List<String> findListNumBonsByNumDoss(String numDoss);
}
