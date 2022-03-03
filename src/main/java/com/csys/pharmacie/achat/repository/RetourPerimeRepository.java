package com.csys.pharmacie.achat.repository;


import com.csys.pharmacie.achat.domain.RetourPerime;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FactureRetour_perime entity.
 */
@Repository
public interface RetourPerimeRepository extends JpaRepository<RetourPerime, String>, QueryDslPredicateExecutor<RetourPerime> {

    Set<RetourPerime> findByNumbonIn(List<String> numBons);
//    @Override
//    @EntityGraph(value="FactureRetourPerime.motifRetour",type = EntityGraph.EntityGraphType.FETCH)
//    Iterable<FactureRetourPerime> findAll(Predicate prdct);
}
