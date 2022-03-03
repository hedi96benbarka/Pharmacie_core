/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.repository;

import com.csys.pharmacie.transfert.domain.FactureBT;
import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Farouk
 */
public interface BonTransferRecupRepository extends FactureBTRepository {

    @Override
    @EntityGraph(value = "FactureBT.BTFE", type = EntityGraph.EntityGraphType.LOAD)
    public Iterable<FactureBT> findAll(Predicate prdct);

    
        @Query(value = "select top 1 *from   param_achat.FactureBT "
                + "where categ_depot=:categDepot and deptr =:deptr  order by datbon desc ", nativeQuery = true)
      public FactureBT findTopByCategDepotAndCoddepOrderByDatbonDesc(@Param("categDepot") String categorieDepot, @Param("deptr") Integer deptr);
      
        public List<FactureBT> findByHashCodeAndDatbonGreaterThan(byte[] hashCode, LocalDateTime datbon);
    
}
