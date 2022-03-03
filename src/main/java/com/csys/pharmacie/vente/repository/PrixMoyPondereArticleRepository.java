package com.csys.pharmacie.vente.repository;

import com.csys.pharmacie.vente.domain.PrixMoyPondereArticle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Receiving entity.
 */
@Repository
public interface PrixMoyPondereArticleRepository extends JpaRepository<PrixMoyPondereArticle, Integer> {
    
    List<PrixMoyPondereArticle> findByArticleIn(Integer[] articles);
    
    
}

