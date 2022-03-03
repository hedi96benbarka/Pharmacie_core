/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csys.pharmacie.transfert.domain.FactureBT;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 *
 * @author Farouk
 */
public interface FactureBTRepository extends JpaRepository<FactureBT, String>, QueryDslPredicateExecutor<FactureBT> {

    @Override
    @EntityGraph(value = "FactureBT.DetailTransfertDTR", type = EntityGraph.EntityGraphType.LOAD)
    public Iterable<FactureBT> findAll(Predicate prdct);

    List<FactureBT> findByTypbonAndDatbonBetweenAndInterdepotOrderByDatbonDesc(String typ, Date deb, Date fin, boolean interdepot);

    List<FactureBT> findByNumbonIn(List<String> numbon);

////    List<FactureBTProjection> findByTypbonAndDatbonBetweenAndInterdepotOrderByDatbonDesc(String typ, Date deb, Date fin, boolean interdepot);
//    FactureBTProjection findByNumbon(String numbon);

    @Query("select f from FactureBT f where f.numbon =?1")
    FactureBT findFactureBTByNumbon(String numbon);

    @Query(value = "SELECT MAX(f.numbon) FROM FactureBT f  WHERE f.categDepot = ?1")
    public String findMaxNumbonByCategDepot(CategorieDepotEnum categDepot);

    Boolean existsByCoddepAndDeptrAndValideFalseAndCodAnnulIsNull(Integer coddep, Integer deptr);

    Boolean existsByCoddepAndValideFalseAndCategDepotAndCodAnnulIsNull(Integer coddep, CategorieDepotEnum categDepot);

    Boolean existsByDeptrAndValideFalseAndCategDepotAndCodAnnulIsNull(Integer deptr, CategorieDepotEnum categDepot);

    List<FactureBT> findByCoddepAndValideFalseAndCodAnnulIsNullAndCategDepot(Integer coddep, CategorieDepotEnum categDepot);

    List<FactureBT> findByDeptrAndValideFalseAndCodAnnulIsNullAndCategDepot(Integer deptr, CategorieDepotEnum categDepot);

    List<FactureBT> findByCoddepAndValideFalseAndCodAnnulIsNull(Integer coddep);
    List<FactureBT> findByDeptrAndValideFalseAndCodAnnulIsNull(Integer deptr);
}
