package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.MvtstoAF;
import com.csys.pharmacie.helper.TotalMouvement;
import java.lang.Integer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MvtstoAF entity.
 */
@Repository
public interface MvtstoAFRepository extends JpaRepository<MvtstoAF, Integer>,QueryDslPredicateExecutor<MvtstoAF>  {

    public List<MvtstoAF> findByNumbon (String id);
    
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.avoirFournisseur.coddep) FROM MvtstoAF m"
            + " WHERE   m.avoirFournisseur.coddep = ?1 and m.codart= ?2 and m.avoirFournisseur.datbon BETWEEN ?3 and ?4 "
            + " group by  m.codart,m.unite, m.avoirFournisseur.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.avoirFournisseur.coddep) FROM MvtstoAF m "
            + " WHERE   m.avoirFournisseur.coddep = ?1 and m.codart= ?2 and m.avoirFournisseur.datbon < ?3 "
            + " group by  m.codart,m.unite, m.avoirFournisseur.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.avoirFournisseur.coddep) FROM MvtstoAF m"
            + " WHERE   m.avoirFournisseur.coddep = ?1 and m.codart in ?2 and m.avoirFournisseur.datbon BETWEEN ?3 and ?4 "
            + " group by  m.codart,m.unite, m.avoirFournisseur.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.avoirFournisseur.coddep) FROM MvtstoAF m "
            + " WHERE   m.avoirFournisseur.coddep = ?1 and m.codart in ?2 and m.avoirFournisseur.datbon < ?3 "
            + " group by  m.codart,m.unite, m.avoirFournisseur.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime date);

    
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.avoirFournisseur.coddep) FROM MvtstoAF m"
            + " WHERE   m.avoirFournisseur.coddep = ?1 and m.avoirFournisseur.datbon BETWEEN ?2 and ?3 "
            + " group by  m.codart,m.unite, m.avoirFournisseur.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.avoirFournisseur.coddep) FROM MvtstoAF m"
            + " WHERE   m.avoirFournisseur.coddep = ?1 and m.avoirFournisseur.datbon < ?2  "
            + " group by  m.codart,m.unite, m.avoirFournisseur.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.avoirFournisseur.coddep) FROM MvtstoAF m"
            + " WHERE  m.avoirFournisseur.datbon BETWEEN ?1 and ?2 "
            + " group by  m.codart,m.unite, m.avoirFournisseur.coddep")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.avoirFournisseur.coddep) FROM MvtstoAF m"
            + " WHERE  m.avoirFournisseur.datbon < ?1  "
            + " group by  m.codart,m.unite, m.avoirFournisseur.coddep")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime date);
}

