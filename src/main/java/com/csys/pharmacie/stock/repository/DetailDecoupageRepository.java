package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.stock.domain.DetailDecoupage;
import com.csys.pharmacie.stock.domain.DetailDecoupagePK;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetailDecoupage entity.
 */
@Repository
public interface DetailDecoupageRepository extends JpaRepository<DetailDecoupage, DetailDecoupagePK>, QueryDslPredicateExecutor<DetailDecoupage> {

    public List<DetailDecoupage> findByCodeDecoupage(String decoupageID);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteOrigine,sum(m.quantite), m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.codart= ?2 and m.decoupage.datbon BETWEEN ?3 and ?4 "
            + " group by  m.codart,m.uniteOrigine, m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteOrigine,sum(m.quantite), m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.codart= ?2 and m.decoupage.datbon < ?3  "
            + " group by  m.codart,m.uniteOrigine, m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, Integer codart, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteFinal,sum(m.quantiteObtenue), m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.codart= ?2 and m.decoupage.datbon BETWEEN ?3 and ?4 "
            + " group by  m.codart,m.uniteFinal, m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteFinal,sum(m.quantiteObtenue), m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.codart= ?2 and m.decoupage.datbon < ?3  "
            + " group by  m.codart,m.uniteFinal, m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, Integer codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteOrigine,sum(m.quantite), m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.codart in ?2 and m.decoupage.datbon BETWEEN ?3 and ?4 "
            + " group by  m.codart,m.uniteOrigine, m.decoupage.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementSortie(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteOrigine,sum(m.quantite), m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.codart in ?2 and m.decoupage.datbon < ?3  "
            + " group by  m.codart,m.uniteOrigine, m.decoupage.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementSortie(Integer coddep, List<Integer>  codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteFinal,sum(m.quantiteObtenue), m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.codart in ?2 and m.decoupage.datbon BETWEEN ?3 and ?4 "
            + " group by  m.codart,m.uniteFinal, m.decoupage.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementEntree(Integer coddep, List<Integer>  codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteFinal,sum(m.quantiteObtenue), m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.codart in ?2 and m.decoupage.datbon < ?3  "
            + " group by  m.codart,m.uniteFinal, m.decoupage.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementEntree(Integer coddep, List<Integer>  codart, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteOrigine,sum(m.quantite),m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.decoupage.datbon BETWEEN ?2 and ?3 "
            + " group by  m.codart,m.uniteOrigine,m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteOrigine,sum(m.quantite),m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.decoupage.datbon < ?2  "
            + " group by  m.codart,m.uniteOrigine,m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteFinal,sum(m.quantiteObtenue),m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep in ?1 and m.decoupage.datbon BETWEEN ?2 and ?3 "
            + " group by  m.codart,m.uniteFinal,m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteFinal,sum(m.quantiteObtenue),m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE   m.decoupage.coddep = ?1 and m.decoupage.datbon < ?2  "
            + " group by  m.codart,m.uniteFinal,m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteOrigine,sum(m.quantite),m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE m.decoupage.datbon BETWEEN ?1 and ?2 "
            + " group by  m.codart,m.uniteOrigine,m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteOrigine,sum(m.quantite),m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE  m.decoupage.datbon < ?1  "
            + " group by  m.codart,m.uniteOrigine,m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteFinal,sum(m.quantiteObtenue),m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE m.decoupage.datbon BETWEEN ?1 and ?2 "
            + " group by  m.codart,m.uniteFinal,m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.uniteFinal,sum(m.quantiteObtenue),m.decoupage.coddep) FROM DetailDecoupage m"
            + " WHERE m.decoupage.datbon < ?1  "
            + " group by  m.codart,m.uniteFinal,m.decoupage.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(LocalDateTime date);
}
