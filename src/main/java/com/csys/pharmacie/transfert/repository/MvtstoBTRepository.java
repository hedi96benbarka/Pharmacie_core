/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TotalMouvement;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.csys.pharmacie.transfert.dto.MvtstoBTProjection;
import com.csys.pharmacie.transfert.domain.MvtStoBT;
import com.csys.pharmacie.transfert.domain.MvtStoBTPK;
import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author Farouk
 */
public interface MvtstoBTRepository extends JpaRepository<MvtStoBT, MvtStoBTPK>, QueryDslPredicateExecutor<MvtStoBT>  {

    List<MvtstoBTProjection> findByFactureBT_Numbon(String numBon);

    @Override
    @EntityGraph(value = "MvtStoBT.allNodes", type =  EntityGraphType.LOAD)
    Iterable<MvtStoBT> findAll(Predicate prdct);
//     public List<MvtStoBT> findAll(Predicate prdct);
//    List<MvtStoBT> findByPk_Numbon(String numBon);
    List<MvtStoBT> findByNumbon(String numBon);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBT.coddep) FROM MvtStoBT m"
            + " WHERE   m.factureBT.coddep = ?1 and m.codart= ?2 and m.factureBT.datbon BETWEEN ?3 and ?4 and m.factureBT.codAnnul is null "
            + " group by  m.codart,m.unite, m.factureBT.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBT.coddep) FROM MvtStoBT m"
            + " WHERE   m.factureBT.coddep = ?1 and m.codart= ?2 and m.factureBT.datbon < ?3 and m.factureBT.codAnnul is null "
            + " group by  m.codart,m.unite, m.factureBT.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, Integer codart, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBT.deptr) FROM MvtStoBT m"
            + " WHERE   m.factureBT.deptr = ?1 and m.codart= ?2 and m.factureBT.datbon BETWEEN ?3 and ?4  and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite, m.factureBT.deptr")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBT.deptr) FROM MvtStoBT m"
            + " WHERE   m.factureBT.deptr = ?1 and m.codart= ?2 and m.factureBT.datbon < ?3   and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite, m.factureBT.deptr")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, Integer codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBT.coddep) FROM MvtStoBT m"
            + " WHERE   m.factureBT.coddep = ?1 and m.codart in ?2 and m.factureBT.datbon BETWEEN ?3 and ?4 and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite, m.factureBT.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementSortie(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBT.coddep) FROM MvtStoBT m"
            + " WHERE   m.factureBT.coddep = ?1 and m.codart in ?2 and m.factureBT.datbon < ?3 and m.factureBT.codAnnul is null "
            + " group by  m.codart,m.unite, m.factureBT.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementSortie(Integer coddep, List<Integer> codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBT.deptr) FROM MvtStoBT m"
            + " WHERE   m.factureBT.deptr = ?1 and m.codart in ?2 and m.factureBT.datbon BETWEEN ?3 and ?4  and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite, m.factureBT.deptr")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementEntree(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBT.deptr) FROM MvtStoBT m"
            + " WHERE   m.factureBT.deptr = ?1 and m.codart in ?2 and m.factureBT.datbon < ?3   and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite, m.factureBT.deptr")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementEntree(Integer coddep, List<Integer> codart, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBT.coddep) FROM MvtStoBT m"
            + " WHERE   m.factureBT.coddep = ?1 and m.factureBT.datbon BETWEEN ?2 and ?3  and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite,m.factureBT.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBT.coddep) FROM MvtStoBT m"
            + " WHERE   m.factureBT.coddep = ?1 and m.factureBT.datbon < ?2   and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite,m.factureBT.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBT.deptr) FROM MvtStoBT m"
            + " WHERE   m.factureBT.deptr = ?1 and m.factureBT.datbon BETWEEN ?2 and ?3  and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite,m.factureBT.deptr")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBT.deptr) FROM MvtStoBT m"
            + " WHERE   m.factureBT.deptr = ?1 and m.factureBT.datbon < ?2   and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite,m.factureBT.deptr")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBT.coddep) FROM MvtStoBT m"
            + " WHERE  m.factureBT.datbon BETWEEN ?1 and ?2  and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite,m.factureBT.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBT.coddep) FROM MvtStoBT m"
            + " WHERE   m.factureBT.datbon < ?1   and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite,m.factureBT.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBT.deptr) FROM MvtStoBT m"
            + " WHERE  m.factureBT.datbon BETWEEN ?1 and ?2  and m.factureBT.codAnnul is null"
            + " group by  m.codart,m.unite,m.factureBT.deptr")
    public List<TotalMouvement> findTotalMouvementEntree(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBT.deptr) FROM MvtStoBT m"
            + " WHERE m.factureBT.datbon < ?1  "
            + " group by  m.codart,m.unite,m.factureBT.deptr")
    public List<TotalMouvement> findTotalMouvementEntree(LocalDateTime date);

//     public Page<MvtStoBT> findByCodartAndCategDepotAndFactureBT_deptrAndFactureBT_CodAnnulIsNullOrderByFactureBT_DatbonDesc(Integer codeArticle, CategorieDepotEnum categDepotEnum,Integer codeDepot , Pageable pgbl);

     public List<MvtStoBT> findTop50ByCodartAndCategDepotAndFactureBT_deptrAndFactureBT_CodAnnulIsNullOrderByFactureBT_DatbonDesc(Integer codeArticle, CategorieDepotEnum categDepotEnum,Integer codeDepot );

    
     @Query(value ="SELECT top 50 * FROM  param_achat.MvtStoBT M  INNER JOIN param_achat.FactureBT F ON F.numbon=M.numbon"
        + " WHERE m.codart =?1 and  f.categ_depot= ?2 and f.deptr = ?3 and f.codAnnul is null order by f.datbon desc  ",nativeQuery = true)
    public List<MvtStoBT> findTop50ByCodartAndCodeDepotAndCodAnnulIsNullOrderByDatBonDesc(Integer codeArticle, String categDepotEnum,Integer codeDepot);
//	@Query("SELECT NEW com.csys.pharmacie.transfert.dto.MouvementTransfert( m.factureBT.datesys, m.factureBT.coddep, m.factureBT.desdepd, m.factureBT.deptr, m.factureBT.desdepa,m.factureBT.numaffiche,m.priuni,m.quantite,m.factureBT.heuresys) FROM MvtStoBT m"+
//        	" WHERE  m.mvtStoBTPK.codart = ?1  and (m.factureBT.coddep = ?2 or m.factureBT.deptr = ?2 ) and m.factureBT.datesys BETWEEN ?3 and ?4")

//	List<MouvementTransfert> findListMouvement(String codart, Integer coddep, Date datedeb, Date datefin);
//	
//	
//	@Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.mvtStoBTPK.codart,sum(m.quantite)) FROM MvtStoBT m"+
//	    	" WHERE  m.codart.famArt = ?1  and m.factureBT.deptr =?2 and m.factureBT.datesys BETWEEN ?3 and ?4"+
//	    	" group by  m.mvtStoBTPK.codart")
//			public List<QteMouvement> findQuantiteMouvement(String famart, String coddep, Date datedeb, Date datefin);
//	@Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.mvtStoBTPK.codart,sum(m.quantite)) FROM MvtStoBT m"+
//	    	" WHERE  m.factureBT.deptr =?1 and m.factureBT.datesys BETWEEN ?2 and ?3"+
//	    	" group by  m.mvtStoBTPK.codart")
//			public List<QteMouvement> findQuantiteMouvementTous( String coddep, Date datedeb, Date datefin);
//	@Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.mvtStoBTPK.codart,sum(m.quantite)) FROM MvtStoBT m"+
//	    	" WHERE  m.codart.famArt = ?1  and m.coddep = ?2  and m.factureBT.datesys BETWEEN ?3 and ?4"+
//	    	" group by  m.mvtStoBTPK.codart")
//			public List<QteMouvement> findQuantiteMouvementSortie(String famart, String coddep, Date datedeb, Date datefin);
//	
//	@Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.mvtStoBTPK.codart,sum(m.quantite)) FROM MvtStoBT m"+
//	    	" WHERE   m.coddep = ?1  and m.factureBT.datesys BETWEEN ?2 and ?3"+
//	    	" group by  m.mvtStoBTPK.codart")
//			public List<QteMouvement> findQuantiteMouvementSortieTous( String coddep, Date datedeb, Date datefin);
//	@Query("SELECT  coalesce(SUM(m.quantite),0) FROM MvtStoBT m WHERE m.mvtStoBTPK.codart = ?1  and m.factureBT.deptr = ?2 and (CAST(m.factureBT.datesys as DATETIME) + CAST(m.factureBT.heuresys as time) BETWEEN ?3 and ?4 )")
//	Integer findTotalMouvement(String codart, String coddep, Date datedeb, Date datefin);
//	
//	@Query("SELECT  coalesce(SUM(m.quantite),0) FROM MvtStoBT m WHERE m.mvtStoBTPK.codart = ?1  and m.coddep = ?2 and (CAST(m.factureBT.datesys as DATETIME) + CAST(m.factureBT.heuresys as time) BETWEEN ?3 and ?4 )")
//	Integer findTotalMouvementSortie(String codart, String coddep, Date datedeb, Date datefin); TODO
}
