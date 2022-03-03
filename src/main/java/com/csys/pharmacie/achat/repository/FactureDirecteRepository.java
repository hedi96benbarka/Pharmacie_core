package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.FactureDirecte;
import com.querydsl.core.types.Predicate;
import java.lang.String;
import java.math.BigDecimal;
import java.util.List;
import org.h2.engine.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FactureDirecte entity.
 */
@Repository
public interface FactureDirecteRepository extends JpaRepository<FactureDirecte, String>, QueryDslPredicateExecutor<FactureDirecte> {

    public FactureDirecte findByReferenceFournisseur(String referenceFournisseur);

//       @Query(value = "select count(g) from GtresorerieEgypte.dbo.Notif_add_ded_fournisseur g where g.numopr_fcptfrs =?1 ",nativeQuery =true)     
    @Query(value = "select count(*) from dbo.Notif_add_ded_fournisseur  where numopr_fcptfrs =?1 ", nativeQuery = true)
    BigDecimal findFactureReglee(BigDecimal numopFcptfrs);

    @EntityGraph(value = "FactureDirecte", type = EntityGraph.EntityGraphType.LOAD)
    @Override
    public List<FactureDirecte> findAll(Predicate prdct);
}
