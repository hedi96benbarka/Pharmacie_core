package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.querydsl.core.types.Predicate;
import java.util.Set;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransfertCompanyBranchRepository extends JpaRepository<TransfertCompanyBranch, String>, QueryDslPredicateExecutor<TransfertCompanyBranch> {

    public Set<TransfertCompanyBranch> findByNumBonIn(Set<String> numBons);

    @EntityGraph(value = "TransfertCompanyBranch.receptionRelative", type = EntityGraph.EntityGraphType.LOAD)
    @Override
    public Iterable<TransfertCompanyBranch> findAll(Predicate prdct);

}
