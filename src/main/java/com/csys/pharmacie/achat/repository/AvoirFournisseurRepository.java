package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import java.lang.String;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AvoirFournisseur entity.
 */
@Repository
public interface AvoirFournisseurRepository extends JpaRepository<AvoirFournisseur, String>, QueryDslPredicateExecutor<AvoirFournisseur> {
}

