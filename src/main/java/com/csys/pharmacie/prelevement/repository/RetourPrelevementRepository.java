package com.csys.pharmacie.prelevement.repository;

import com.csys.pharmacie.prelevement.domain.RetourPrelevement;
import java.lang.String;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RetourPrelevement entity.
 */
@Repository
public interface RetourPrelevementRepository extends JpaRepository<RetourPrelevement, String> , QueryDslPredicateExecutor<RetourPrelevement>{

}

