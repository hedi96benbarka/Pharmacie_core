/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.csys.pharmacie.transfert.domain.MvtStoBE;
import com.csys.pharmacie.transfert.domain.MvtStoBEPK;

import com.csys.pharmacie.helper.QteMouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author Farouk
 */
public interface MvtstoBERepository extends JpaRepository<MvtStoBE, MvtStoBEPK>, QueryDslPredicateExecutor<MvtStoBE> {

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.codart= ?2 and m.factureBE.datbon BETWEEN ?3 and ?4 and m.quantite>0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.codart= ?2 and m.factureBE.datbon < ?3  and m.quantite>0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, Integer codart, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,-sum(m.quantite),m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.codart= ?2 and m.factureBE.datbon BETWEEN ?3 and ?4 and m.quantite<0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,-sum(m.quantite),m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.codart= ?2 and m.factureBE.datbon < ?3  and m.quantite<0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, Integer codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.codart in ?2 and m.factureBE.datbon BETWEEN ?3 and ?4 and m.quantite>0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementEntree(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.codart in ?2 and m.factureBE.datbon < ?3  and m.quantite>0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementEntree(Integer coddep, List<Integer> codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,-sum(m.quantite),m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.codart in ?2 and m.factureBE.datbon BETWEEN ?3 and ?4 and m.quantite<0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementSortie(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,-sum(m.quantite),m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.codart in ?2 and m.factureBE.datbon < ?3  and m.quantite<0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvementSortie(Integer coddep, List<Integer> codart, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.factureBE.datbon BETWEEN ?2 and ?3 and m.quantite>0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.factureBE.datbon < ?2  and m.quantite>0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(-m.quantite), m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.factureBE.datbon BETWEEN ?2 and ?3 and m.quantite<0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(-m.quantite), m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.factureBE.datbon < ?2  and m.quantite<0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE  m.factureBE.datbon BETWEEN ?1 and ?2 and m.quantite>0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite), m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE m.factureBE.datbon < ?1  and m.quantite>0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementEntree(LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(-m.quantite), m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE m.factureBE.datbon BETWEEN ?1 and ?2 and m.quantite<0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(-m.quantite), m.factureBE.coddep) FROM MvtStoBE m"
            + " WHERE   m.factureBE.datbon < ?1  and m.quantite<0"
            + " group by  m.codart,m.unite, m.factureBE.coddep")
    public List<TotalMouvement> findTotalMouvementSortie(LocalDateTime date);
//	@Query("SELECT  coalesce(SUM(m.quantite),0) FROM MvtStoBE m WHERE m.pk.codart = ?1  and m.factureBE.coddep = ?2 and (CAST(m.factureBE.datesys as DATETIME) + CAST(m.factureBE.heuresys as time) BETWEEN ?3 and ?4 ) and m.quantite > 0")
//	Integer findTotalMouvement(String codart, String coddep, Date datedeb, Date datefin);TODO
//	
//	@Query("SELECT  coalesce(-SUM(m.quantite),0) FROM MvtStoBE m WHERE m.pk.codart = ?1  and m.factureBE.coddep = ?2 and (CAST(m.factureBE.datesys as DATETIME) + CAST(m.factureBE.heuresys as time) BETWEEN ?3 and ?4 ) and m.quantite < 0")
//	Integer findTotalMouvementSortie(String codart, String coddep, Date datedeb, Date datefin);TODO
    //---------------------------------------------hamdi
//      List<MvtStoBE> findByPk_Numbon(String numbon);
//
//    @Query("SELECT NEW com.csys.pharmacie.transfert.dto.MouvementRedressement( m.factureBE.datesys, m.factureBE.coddep, m.factureBE.numaffiche,m.priuni,m.quantite,m.factureBE.heuresys) FROM MvtStoBE m"
//            + " WHERE  m.pk.codart = ?1  and m.factureBE.coddep = ?2 and m.factureBE.datesys BETWEEN ?3 and ?4")
//    List<MouvementRedressement> findListMouvement(String codart, String coddep, Date datedeb, Date datefin);
//    
//   
//    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.pk.codart,sum(m.quantite)) FROM MvtStoBE m"
//            + " WHERE   m.factureBE.coddep = ?2 and m.quantite > 0 and m.factureBE.datesys BETWEEN ?3 and ?4"
//            + " group by  m.pk.codart")
//    public List<QteMouvement> findQuantiteMouvement(String famart, String coddep, Date datedeb, Date datefin);
//
//    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.pk.codart,sum(m.quantite)) FROM MvtStoBE m"
//            + " WHERE  m.factureBE.coddep = ?1 and m.quantite > 0 and m.factureBE.datesys BETWEEN ?2 and ?3"
//            + " group by  m.pk.codart")
//    public List<QteMouvement> findQuantiteMouvementTous(String coddep, Date datedeb, Date datefin);
//
//    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.pk.codart,-sum(m.quantite)) FROM MvtStoBE m"
//            + " WHERE   m.factureBE.coddep = ?2 and m.quantite < 0 and m.factureBE.datesys BETWEEN ?3 and ?4"
//            + " group by  m.pk.codart")
//    List<QteMouvement> findQuantiteMouvementSortie(String famart, String coddep, Date datedeb, Date datefin);
//
//    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.pk.codart,-sum(m.quantite)) FROM MvtStoBE m"
//            + " WHERE   m.factureBE.coddep = ?1 and m.quantite < 0 and m.factureBE.datesys BETWEEN ?2 and ?3"
//            + " group by  m.pk.codart")
//    List<QteMouvement> findQuantiteMouvementSortieTous(String coddep, Date datedeb, Date datefin);
//    

    List<MvtStoBE> findByNumbon(String numbon);

//    @Query("SELECT NEW com.csys.pharmacie.transfert.dto.MouvementRedressement( m.factureBE.datesys, m.factureBE.coddep, m.factureBE.numaffiche,m.priuni,m.quantite,m.factureBE.heuresys) FROM MvtStoBE m"
//            + " WHERE  m.codart = ?1  and m.factureBE.coddep = ?2 and m.factureBE.datesys BETWEEN ?3 and ?4")
//    List<MouvementRedressement> findListMouvement(String codart, String coddep, Date datedeb, Date datefin);
    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.codart,sum(m.quantite)) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?2 and m.quantite > 0 and m.factureBE.datesys BETWEEN ?3 and ?4"
            + " group by  m.codart")
    public List<QteMouvement> findQuantiteMouvement(String famart, String coddep, Date datedeb, Date datefin);

    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.codart,sum(m.quantite)) FROM MvtStoBE m"
            + " WHERE  m.factureBE.coddep = ?1 and m.quantite > 0 and m.factureBE.datesys BETWEEN ?2 and ?3"
            + " group by  m.codart")
    public List<QteMouvement> findQuantiteMouvementTous(String coddep, Date datedeb, Date datefin);

    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.codart,-sum(m.quantite)) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?2 and m.quantite < 0 and m.factureBE.datesys BETWEEN ?3 and ?4"
            + " group by  m.codart")
    List<QteMouvement> findQuantiteMouvementSortie(String famart, String coddep, Date datedeb, Date datefin);

    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.codart,-sum(m.quantite)) FROM MvtStoBE m"
            + " WHERE   m.factureBE.coddep = ?1 and m.quantite < 0 and m.factureBE.datesys BETWEEN ?2 and ?3"
            + " group by  m.codart")
    List<QteMouvement> findQuantiteMouvementSortieTous(String coddep, Date datedeb, Date datefin);
    
    
     public List<MvtStoBE> findByCodartInAndFactureBE_DatbonAfterAndQuantiteGreaterThan(Set<Integer> codart, LocalDateTime datbon,BigDecimal quantite);

}
