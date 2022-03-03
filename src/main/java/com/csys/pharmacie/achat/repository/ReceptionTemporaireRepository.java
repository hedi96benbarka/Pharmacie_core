package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.ReceptionTemporaire;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReceptionTemporaireRepository extends JpaRepository<ReceptionTemporaire, String>, QueryDslPredicateExecutor<ReceptionTemporaire> {

    @EntityGraph(value = "ReceptionTemporaire.reception", type = EntityGraph.EntityGraphType.LOAD)
    @Override
    Iterable<ReceptionTemporaire> findAll(Predicate prdct);

    public ReceptionTemporaire findFirstByNumbon(String numbon);

    @Transactional
    @Modifying
    @Query("update ReceptionTemporaire reception set reception.isValidated = true  ,isTemporaire=false WHERE reception.numbon =?1")
    public void updateReceptionTemporaire_IsValidated(String numbon);

}
