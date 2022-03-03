package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.FactureRetourPerime;
import java.lang.String;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FactureRetourPerime entity.
 */
@Repository
public interface FactureRetourPerimeRepository extends JpaRepository<FactureRetourPerime, String> , QueryDslPredicateExecutor<FactureRetourPerime> {
    
    public FactureRetourPerime findByReferenceFournisseur(String referenceFournisseur); 
}

