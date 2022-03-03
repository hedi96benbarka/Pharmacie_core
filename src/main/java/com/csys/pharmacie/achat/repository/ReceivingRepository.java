package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.Receiving;
import com.querydsl.core.types.Predicate;

import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Receiving entity.
 */
@Repository
public interface ReceivingRepository extends JpaRepository<Receiving, Integer>, QueryDslPredicateExecutor<Receiving> {

   
    @Override
    @EntityGraph(value = "Receiving.allNodes",type =EntityGraph.EntityGraphType.LOAD )
    public Set<Receiving> findAll(Predicate prdct);

    public List<Receiving> findByCodeIn(List<Integer> codeList);
    public Receiving findByNumaffiche(String numbon);
}
