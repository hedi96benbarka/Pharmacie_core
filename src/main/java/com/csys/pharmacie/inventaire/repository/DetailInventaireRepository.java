package com.csys.pharmacie.inventaire.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.inventaire.domain.DetailInventaire;
import com.csys.pharmacie.inventaire.domain.DetailInventairePK;
import com.csys.pharmacie.inventaire.dto.DateInventaire;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetailInventaire entity.
 */
@Repository
public interface DetailInventaireRepository extends JpaRepository<DetailInventaire, DetailInventairePK> {             
     
    List<DetailInventaire> findByDetailInventairePK_Inventaire(Integer code ) ; 
    List<DetailInventaire> findByDetailInventairePK_InventaireIn(List<Integer> code ) ;

    @Query("Select new com.csys.pharmacie.inventaire.dto.DateInventaire(m.detailInventairePK.categorieArticle,max(m.inventaire1.dateCloture), m.inventaire1.depot)  from DetailInventaire m where m.inventaire1.depot  = ?1 and m.inventaire1.dateCloture < ?2 group by m.detailInventairePK.categorieArticle, m.inventaire1.depot")
    public List<DateInventaire> findMaxHeureSystemByCoddepAndHeureSystemeGreaterThan(Integer coddep, Date heureSysteme);

    @Query("Select new com.csys.pharmacie.inventaire.dto.DateInventaire(m.detailInventairePK.categorieArticle,max(m.inventaire1.dateCloture), m.inventaire1.depot)  from DetailInventaire m where m.inventaire1.dateCloture < ?1 group by m.detailInventairePK.categorieArticle, m.inventaire1.depot")
    public List<DateInventaire> findMaxHeureSystemByHeureSystemeGreaterThan(Date heureSysteme);
    
      public List<DetailInventaire> findByDetailInventairePK_CategorieArticleAndInventaire1_DepotAndInventaire1_CategorieDepotAndInventaire1_DateClotureIsNullAndInventaire1_DateAnnuleIsNull(Integer categArt, Integer coddep, CategorieDepotEnum categDepot);
    
}

