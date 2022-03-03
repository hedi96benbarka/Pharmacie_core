/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.QtePrixMouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import com.csys.pharmacie.vente.quittance.domain.MvtstoPK;
import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author Farouk
 */
public interface MvtstoRepository extends JpaRepository<Mvtsto, MvtstoPK>, QueryDslPredicateExecutor<Mvtsto> {
//Il faut la  remplacé par 

    @Override
    @EntityGraph(value = "Mvtsto.facture", type = EntityGraph.EntityGraphType.LOAD)
    List<Mvtsto> findAll(Predicate prdct);

    List<Mvtsto> findByMvtstoPK_Numbon(String numBon);

    @Query("SELECT DISTINCT m.mvtstoPK.codart FROM Mvtsto m where m.mvtstoPK.numbon IN ?1 ")
    List<Integer> findCodartByMvtstoPK_NumbonIn(List<String> numBons);

    @Query("SELECT m FROM Mvtsto m where m.mvtstoPK.numbon IN ?1 AND m.mvtstoPK.codart=?2")
    List<Mvtsto> findByMvtstoPK_NumbonIn(List<String> numBons, Integer codart);

    @Async
    @EntityGraph(value = "Mvtsto.facture", type = EntityGraph.EntityGraphType.LOAD)
    CompletableFuture<List<Mvtsto>> findByMvtstoPK_NumbonIn(List<String> s);

    @Query("SELECT  coalesce(SUM(m.quantite),0) FROM Mvtsto m WHERE m.mvtstoPK.codart = ?1 and (m.facture.datesys  +m.facture.heuresys BETWEEN ?3 and ?4 ) and m.facture.typbon IN('FE','FC','PA')")
    Integer findTotalMouvement(String codart, String coddep, Date datedeb, Date datefin);

    @Query("SELECT COUNT(DISTINCT m.mvtstoPK.codart) FROM Mvtsto m "
            + "WHERE m.facture.typbon = 'FE' and m.facture.datesys >= ?1 and m.facture.datesys <= ?2 ")
    public Integer findNombreArticleEnAttenteDeRecup(Date date, Date now);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtstoPK.codart,m.unite,sum(m.quantite),m.facture.coddep) FROM Mvtsto m"
            + " WHERE   m.facture.coddep = ?1 and m.mvtstoPK.codart= ?2 and m.facture.datbon BETWEEN ?3 and ?4  and m.facture.codAnnul is null"
            + " group by  m.mvtstoPK.codart,m.unite,m.facture.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtstoPK.codart,m.unite,sum(m.quantite), m.facture.coddep) FROM Mvtsto m"
            + " WHERE   m.facture.coddep = ?1 and m.mvtstoPK.codart= ?2 and m.facture.datbon < ?3 and m.facture.codAnnul is null"
            + " group by  m.mvtstoPK.codart,m.unite,m.facture.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtstoPK.codart,m.unite,sum(m.quantite),m.facture.coddep) FROM Mvtsto m"
            + " WHERE   m.facture.coddep = ?1 and m.mvtstoPK.codart in ?2 and m.facture.datbon BETWEEN ?3 and ?4  and m.facture.codAnnul is null"
            + " group by  m.mvtstoPK.codart,m.unite,m.facture.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtstoPK.codart, m.unite,sum(m.quantite), m.facture.coddep) FROM Mvtsto m"
            + " WHERE   m.facture.coddep = ?1 and m.mvtstoPK.codart in ?2 and m.facture.datbon < ?3  and m.facture.codAnnul is null"
            + " group by  m.mvtstoPK.codart,m.unite,m.facture.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtstoPK.codart,m.unite,sum(m.quantite),m.facture.coddep) FROM Mvtsto m"
            + " WHERE   m.facture.coddep = ?1 and m.facture.datbon BETWEEN ?2 and ?3 and m.facture.codAnnul is null"
            + " group by  m.mvtstoPK.codart,m.unite,m.facture.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtstoPK.codart,m.unite,sum(m.quantite),m.facture.coddep) FROM Mvtsto m"
            + " WHERE   m.facture.coddep = ?1 and m.facture.datbon < ?2 and m.facture.codAnnul is null"
            + " group by  m.mvtstoPK.codart,m.unite,m.facture.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtstoPK.codart,m.unite,sum(m.quantite),m.facture.coddep) FROM Mvtsto m"
            + " WHERE   m.facture.datbon BETWEEN ?1 and ?2 and m.facture.codAnnul is null"
            + " group by  m.mvtstoPK.codart,m.unite,m.facture.coddep")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtstoPK.codart,m.unite,sum(m.quantite),m.facture.coddep) FROM Mvtsto m"
            + " WHERE  m.facture.datbon < ?1 and m.facture.codAnnul is null"
            + " group by  m.mvtstoPK.codart,m.unite,m.facture.coddep")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime date);
    
    
    
    @Query("SELECT NEW com.csys.pharmacie.helper.QtePrixMouvement( m.mvtstoPK.codart,m.codeSaisi,m.desart,m.desArtSec,m.unite,sum(m.quantite),sum(m.quantite *m.priuni *(1 + m.tautva/100)),sum(m.quantite * m.priach * (1 + m.tauTvaAch/100)),m.tautva, m.tauTvaAch,m.facture.coddep) FROM Mvtsto m"
            + " WHERE m.facture.coddep in ?1 and m.facture.datbon BETWEEN ?2 and ?3 and  m.facture.categDepot=?4 and m.mvtstoPK.codart in ?5 and m.facture.codAnnul is null and m.facture.numbonRecept is not null"
            + " group by  m.mvtstoPK.codart,m.codeSaisi,m.desart,m.desArtSec,m.unite, m.tautva,m.facture.coddep,m.tauTvaAch")
    public List<QtePrixMouvement> findQuantitePrixMouvementAndCodartIn(List<Integer> coddep, LocalDateTime datedeb, LocalDateTime datefin, CategorieDepotEnum categ, List<Integer> articleIds);

    @Query("SELECT NEW com.csys.pharmacie.helper.QtePrixMouvement( m.mvtstoPK.codart,m.codeSaisi,m.desart,m.desArtSec,m.unite,sum(m.quantite),sum(m.quantite *m.priuni *(1 + m.tautva/100)),sum(m.quantite * m.priach * (1 + m.tauTvaAch/100)),m.tautva, m.tauTvaAch,m.facture.coddep) FROM Mvtsto m"
            + " WHERE m.facture.datbon BETWEEN ?1 and ?2 and  m.facture.categDepot=?3 and m.mvtstoPK.codart in ?4 and m.facture.codAnnul is null and m.facture.numbonRecept is not null"
            + " group by  m.mvtstoPK.codart,m.codeSaisi,m.desart,m.desArtSec,m.unite, m.tautva,m.facture.coddep,m.tauTvaAch")
    public List<QtePrixMouvement> findQuantitePrixMouvementAndCodartIn(LocalDateTime datedeb, LocalDateTime datefin, CategorieDepotEnum categ, List<Integer> articleIds);


}
