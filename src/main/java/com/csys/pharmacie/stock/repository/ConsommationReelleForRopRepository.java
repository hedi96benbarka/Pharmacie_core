package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.stock.domain.ConsommationReelleForRop;
import com.querydsl.core.types.Predicate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsommationReelleForRopRepository extends JpaRepository<ConsommationReelleForRop, Integer>, QueryDslPredicateExecutor<ConsommationReelleForRop> {

  @Override
  public List<ConsommationReelleForRop> findAll(Predicate predict);
  
  @Modifying
  @Query(value = "delete from param_achat.consommation_reelle_for_rop where categ_depot =:categDepot", nativeQuery = true)
    int  deleteByCategDepot(@Param("categDepot") String categDepot);
}

