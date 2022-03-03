package com.csys.pharmacie.prelevement.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.prelevement.domain.FacturePR;
import com.csys.pharmacie.prelevement.domain.MvtStoPR;
import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MvtStoPR entity.
 */
@Repository
// public interface MvtStoPRRepository extends JpaRepository<MvtStoPR, MvtStoPRPK> {

public interface MvtStoPRRepository extends JpaRepository<MvtStoPR, Integer>, QueryDslPredicateExecutor<MvtStoPR> {

    List<MvtStoPR> findByNumbon(String numbon);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.facturePR.coddepotSrc) FROM MvtStoPR m"
            + " WHERE   m.facturePR.coddepotSrc = ?1 and m.codart= ?2 and m.facturePR.datbon BETWEEN ?3 and ?4 and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite,m.facturePR.coddepotSrc")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.facturePR.coddepotSrc) FROM MvtStoPR m"
            + " WHERE   m.facturePR.coddepotSrc = ?1 and m.codart= ?2 and m.facturePR.datbon < ?3   and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite,m.facturePR.coddepotSrc")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.facturePR.coddepotSrc) FROM MvtStoPR m"
            + " WHERE   m.facturePR.coddepotSrc = ?1 and m.codart in ?2 and m.facturePR.datbon BETWEEN ?3 and ?4  and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite,m.facturePR.coddepotSrc")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.facturePR.coddepotSrc) FROM MvtStoPR m"
            + " WHERE   m.facturePR.coddepotSrc = ?1 and m.codart in ?2 and m.facturePR.datbon < ?3   and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite,m.facturePR.coddepotSrc")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.facturePR.coddepotSrc) FROM MvtStoPR m"
            + " WHERE   m.facturePR.coddepotSrc = ?1 and m.facturePR.datbon BETWEEN ?2 and ?3   and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite, m.facturePR.coddepotSrc")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.facturePR.coddepotSrc) FROM MvtStoPR m"
            + " WHERE   m.facturePR.coddepotSrc = ?1 and m.facturePR.datbon < ?2    and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite, m.facturePR.coddepotSrc")
    public List<TotalMouvement> findTotalMouvement(Integer  coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.facturePR.coddepotSrc) FROM MvtStoPR m"
            + " WHERE m.facturePR.datbon BETWEEN ?1 and ?2   and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite, m.facturePR.coddepotSrc")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.facturePR.coddepotSrc) FROM MvtStoPR m"
            + " WHERE   m.facturePR.datbon < ?1    and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite, m.facturePR.coddepotSrc")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime date);

    
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.facturePR.coddepartDest) FROM MvtStoPR m"
            + " WHERE   m.facturePR.coddepartDest = ?1 and m.facturePR.datbon BETWEEN ?2 and ?3   and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite, m.facturePR.coddepartDest")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddepartDest, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.facturePR.coddepartDest) FROM MvtStoPR m"
            + " WHERE   m.facturePR.coddepartDest = ?1 and m.facturePR.datbon < ?2    and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite, m.facturePR.coddepartDest")
    public List<TotalMouvement> findTotalMouvementSortie(Integer  coddepartDest, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.facturePR.coddepartDest) FROM MvtStoPR m"
            + " WHERE m.facturePR.datbon BETWEEN ?1 and ?2   and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite, m.facturePR.coddepartDest")
    public List<TotalMouvement> findTotalMouvementSortie(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.facturePR.coddepartDest) FROM MvtStoPR m"
            + " WHERE   m.facturePR.datbon < ?1    and m.facturePR.codAnnul is null"
            + " group by  m.codart,m.unite, m.facturePR.coddepartDest")
    public List<TotalMouvement> findTotalMouvementSortie(LocalDateTime date);

    public List<MvtStoPR> findByFacturePR_numbonIn(List<String> numBons);

    @Override
    @EntityGraph(value = "MvtStoPR.FacturePR", type = EntityGraph.EntityGraphType.FETCH)
    Iterable<MvtStoPR> findAll(Predicate prdct);
    

    public List<MvtStoPR> findTop50ByCodartAndCategDepotAndFacturePR_CoddepartDestAndFacturePR_CodAnnulIsNullOrderByFacturePR_DatbonDesc(Integer codeArticle, CategorieDepotEnum categDepotEnum,Integer codeDepotSrc );
 @Query(value ="SELECT top 50 * FROM  param_achat.MvtStoPR M  INNER JOIN param_achat.FacturePR F ON F.numbon=M.numbon"
        + " WHERE m.codart =?1 and  f.categ_depot= ?2 and f.coddepart_desti = ?3 and f.codAnnul is null order by f.datbon desc  ",nativeQuery = true)
    public List<MvtStoPR> findTop50ByCodartAndCodeDepotAndCodAnnulIsNullOrderByDatBonDesc(Integer codeArticle, String categDepotEnum,Integer codeDepot);
}
