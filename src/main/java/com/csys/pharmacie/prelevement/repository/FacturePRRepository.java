package com.csys.pharmacie.prelevement.repository;

import com.csys.pharmacie.prelevement.domain.FacturePR;
import com.querydsl.core.types.Predicate;
import java.lang.String;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FacturePR entity.
 */
@Repository
public interface FacturePRRepository extends JpaRepository<FacturePR, String>, QueryDslPredicateExecutor<FacturePR> {

    @Override
    
    @EntityGraph(value = "FacturePR.motif", type = EntityGraph.EntityGraphType.FETCH)
    Iterable<FacturePR> findAll(Predicate prdct);

    FacturePR findByNumbon(String numbon);
    Set<FacturePR> findByNumbonIn(Set<String>NumBons);

}
