/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.stock.domain.MouvementStock;
import com.csys.pharmacie.stock.dto.MouvementStockDTO;
import com.csys.pharmacie.stock.dto.MouvementStockEditionDTO;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author Administrateur
 */
public interface MouvementStockRepository extends JpaRepository<MouvementStock, String>, QueryDslPredicateExecutor<MouvementStock> {


  @Query("SELECT  NEW com.csys.pharmacie.stock.dto.MouvementStockEditionDTO(m.coddep, m.typbon, SUM(m.valeur)) FROM MouvementStock m"
            + " WHERE m.categDepot=?1 and m.coddep in ?2 and m.datbon BETWEEN ?3 and ?4"
            + " GROUP BY m.coddep, m.typbon")
    public List<MouvementStockEditionDTO> findMouvementStockGrouppedByDepot(CategorieDepotEnum categ, List<Integer> codeDepot, Date fromDate, Date toDate);

   // public List<MouvementStock> findByDatbonBetweenAndCategDepotLikeGrouppedByCodart(Date du,Date au,CategorieDepotEnum categ);
    
    /* @Query("SELECT  NEW com.csys.pharmacie.stock.domain.MouvementStock(m.codart,m.codeUnite, SUM(m.quantite), SUM(m.valeur)) FROM MouvementStock m"
            + " WHERE m.categDepot=?1 and m.datbon BETWEEN ?2 and ?3"
            + " GROUP BY m.codart,m.codeUnite")
       public List<MouvementStock> findByCategDepotAndDatbonBetweenGrouppedByCategDepotAndCodeArticleAndCodeUnite(CategorieDepotEnum categ,Date du,Date au);*/
    
     @Query("SELECT  NEW com.csys.pharmacie.stock.dto.MouvementStockDTO(m.codart,m.codeUnite, SUM(m.quantite), SUM(m.valeur)) FROM MouvementStock m"
            + " WHERE m.categDepot=?1 and m.datbon BETWEEN ?2 and ?3"
            + " GROUP BY m.codart,m.codeUnite")
       public List<MouvementStockDTO> findByCategDepotAndDatbonBetweenGrouppedByCategDepotAndCodeArticleAndCodeUnite(CategorieDepotEnum categ,Date du,Date au);

//    @Query("SELECT  NEW com.csys.pharmacie.stock.dto.MouvementStockEditionDTO(m.coddep, m.codart, m.codeSaisi, m.desartSec , m.codeUnite, m.typbon, COALESCE(SUM(m.quantite), 0), SUM(m.valeur)) FROM MouvementStock m"
//            + " WHERE m.categDepot=?1 and m.coddep=?2 and m.codart in ?3 and m.datbon BETWEEN ?4 and ?5"
//            + " GROUP BY m.coddep, m.codart, m.codeSaisi, m.desartSec, m.codeUnite, m.typbon")
//    public List<MouvementStockEditionDTO> findMouvementStockByListCodeArticle(CategorieDepotEnum categ,Integer codeDepot, List<Integer> codeArticles, Date fromDate, Date toDate);
//
//    @Query("SELECT  NEW com.csys.pharmacie.stock.dto.MouvementStockEditionDTO(m.coddep, m.codart, m.codeSaisi, m.desartSec , m.codeUnite, m.typbon, COALESCE(SUM(m.quantite), 0), SUM(m.valeur)) FROM MouvementStock m"
//            + " WHERE m.categDepot=?1 and m.coddep=?2 and m.datbon BETWEEN ?3 and ?4"
//            + " GROUP BY m.coddep, m.codart, m.codeSaisi, m.desartSec, m.codeUnite, m.typbon")
//    public List<MouvementStockEditionDTO> findMouvementStock(CategorieDepotEnum categ,Integer codeDepot,Date fromDate, Date toDate);
}
