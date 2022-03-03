package com.csys.pharmacie.prelevement.repository;

import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.prelevement.domain.DetailRetourPrelevement;
import java.lang.Long;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetailRetourPrelevement entity.
 */
@Repository
public interface DetailRetourPrelevementRepository extends JpaRepository<DetailRetourPrelevement, Integer>, QueryDslPredicateExecutor<DetailRetourPrelevement> {

    public List<DetailRetourPrelevement> findByNumbon(String numBon);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.retourPrelevement.coddepDesti) FROM DetailRetourPrelevement m"
            + " WHERE   m.retourPrelevement.coddepDesti = ?1 and m.codart= ?2 and m.retourPrelevement.datbon BETWEEN ?3 and ?4"
            + " group by  m.codart,m.unite,m.retourPrelevement.coddepDesti")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.retourPrelevement.coddepDesti) FROM DetailRetourPrelevement m"
            + " WHERE   m.retourPrelevement.coddepDesti = ?1 and m.codart= ?2 and m.retourPrelevement.datbon < ?3"
            + " group by  m.codart,m.unite,m.retourPrelevement.coddepDesti")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.retourPrelevement.coddepDesti) FROM DetailRetourPrelevement m"
            + " WHERE   m.retourPrelevement.coddepDesti = ?1 and m.codart in ?2 and m.retourPrelevement.datbon BETWEEN ?3 and ?4"
            + " group by  m.codart,m.unite,m.retourPrelevement.coddepDesti")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.retourPrelevement.coddepDesti) FROM DetailRetourPrelevement m"
            + " WHERE   m.retourPrelevement.coddepDesti = ?1 and m.codart in ?2 and m.retourPrelevement.datbon < ?3 "
            + " group by  m.codart,m.unite,m.retourPrelevement.coddepDesti")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime date);

    
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.retourPrelevement.coddepDesti) FROM DetailRetourPrelevement m"
            + " WHERE   m.retourPrelevement.coddepDesti = ?1 and m.retourPrelevement.datbon BETWEEN ?2 and ?3"
            + " group by  m.codart,m.unite, m.retourPrelevement.coddepDesti")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.retourPrelevement.coddepDesti) FROM DetailRetourPrelevement m"
            + " WHERE   m.retourPrelevement.coddepDesti = ?1 and m.retourPrelevement.datbon < ?2"
            + " group by  m.codart,m.unite, m.retourPrelevement.coddepDesti")
    public List<TotalMouvement> findTotalMouvement(Integer  coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.retourPrelevement.coddepDesti) FROM DetailRetourPrelevement m"
            + " WHERE m.retourPrelevement.datbon BETWEEN ?1 and ?2 "
            + " group by  m.codart,m.unite, m.retourPrelevement.coddepDesti")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.retourPrelevement.coddepDesti) FROM DetailRetourPrelevement m"
            + " WHERE   m.retourPrelevement.datbon < ?1  "
            + " group by  m.codart,m.unite, m.retourPrelevement.coddepDesti")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime date);
    
}
