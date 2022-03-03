package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.stock.domain.Decoupage;
import java.lang.String;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Decoupage entity.
 */
@Repository
public interface DecoupageRepository extends JpaRepository<Decoupage, String> ,QueryDslPredicateExecutor<Decoupage>{
}

