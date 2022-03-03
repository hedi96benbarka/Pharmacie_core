package com.csys.pharmacie.vente.repository;

import com.csys.pharmacie.vente.domain.PrixReferenceArticle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Receiving entity.
 */
@Repository
public interface PrixReferenceArticleRepository extends JpaRepository<PrixReferenceArticle, Integer> {

    public List<PrixReferenceArticle> findByArticleIn(Integer[] codeArticles);
    
     public PrixReferenceArticle findByArticle(Integer codeArticle);
    
}

