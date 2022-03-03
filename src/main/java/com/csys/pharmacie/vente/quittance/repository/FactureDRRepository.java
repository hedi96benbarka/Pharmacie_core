package com.csys.pharmacie.vente.quittance.repository;

import com.csys.pharmacie.vente.quittance.domain.FactureDR;
import com.querydsl.core.types.Predicate;
import java.util.Set;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FactureDR entity.
 */
@Repository
public interface FactureDRRepository extends JpaRepository<FactureDR, String>, QueryDslPredicateExecutor<FactureDR> {

    @Override
    @EntityGraph(value = "factureDR.allNodes", type = EntityGraph.EntityGraphType.LOAD)
    public Set<FactureDR> findAll(Predicate prdct);

}
