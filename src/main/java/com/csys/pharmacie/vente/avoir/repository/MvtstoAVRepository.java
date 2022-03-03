/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.QtePrixMouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import org.springframework.data.jpa.repository.JpaRepository;

import com.csys.pharmacie.vente.avoir.domain.MvtStoAV;
import com.csys.pharmacie.vente.avoir.domain.MvtStoAVPK;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;

/**
 *
 * @author Farouk
 */
public interface MvtstoAVRepository extends JpaRepository<MvtStoAV, MvtStoAVPK>, QueryDslPredicateExecutor<MvtStoAV> {

    List<MvtStoAV> findByMvtStoAVPK_Numbon(String numBon);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtStoAVPK.codart,m.unite,sum(m.quantite),m.factureAV.coddep) FROM MvtStoAV m"
            + " WHERE   m.factureAV.coddep = ?1 and m.mvtStoAVPK.codart= ?2 and m.factureAV.datbon BETWEEN ?3 and ?4 "
            + " group by  m.mvtStoAVPK.codart,m.unite,m.factureAV.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtStoAVPK.codart,m.unite,sum(m.quantite),m.factureAV.coddep) FROM MvtStoAV m"
            + " WHERE   m.factureAV.coddep = ?1 and m.mvtStoAVPK.codart= ?2 and m.factureAV.datbon < ?3  "
            + " group by  m.mvtStoAVPK.codart,m.unite,m.factureAV.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime date);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtStoAVPK.codart,m.unite,sum(m.quantite),m.factureAV.coddep) FROM MvtStoAV m"
            + " WHERE   m.factureAV.coddep = ?1 and m.mvtStoAVPK.codart in ?2 and m.factureAV.datbon BETWEEN ?3 and ?4 "
            + " group by  m.mvtStoAVPK.codart,m.unite,m.factureAV.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtStoAVPK.codart,m.unite,sum(m.quantite),m.factureAV.coddep) FROM MvtStoAV m"
            + " WHERE   m.factureAV.coddep = ?1 and m.mvtStoAVPK.codart in ?2 and m.factureAV.datbon < ?3  "
            + " group by  m.mvtStoAVPK.codart,m.unite,m.factureAV.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtStoAVPK.codart,m.unite,sum(m.quantite), m.factureAV.coddep) FROM MvtStoAV m"
            + " WHERE   m.factureAV.coddep = ?1 and m.factureAV.datbon BETWEEN ?2 and ?3 "
            + " group by  m.mvtStoAVPK.codart,m.unite, m.factureAV.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtStoAVPK.codart,m.unite,sum(m.quantite), m.factureAV.coddep) FROM MvtStoAV m"
            + " WHERE   m.factureAV.coddep = ?1 and m.factureAV.datbon < ?2  "
            + " group by  m.mvtStoAVPK.codart,m.unite, m.factureAV.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtStoAVPK.codart,m.unite,sum(m.quantite), m.factureAV.coddep) FROM MvtStoAV m"
            + " WHERE  m.factureAV.datbon BETWEEN ?1 and ?2 "
            + " group by  m.mvtStoAVPK.codart,m.unite, m.factureAV.coddep")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime datedeb, LocalDateTime datefin);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.mvtStoAVPK.codart,m.unite,sum(m.quantite), m.factureAV.coddep) FROM MvtStoAV m"
            + " WHERE  m.factureAV.datbon < ?1  "
            + " group by  m.mvtStoAVPK.codart,m.unite, m.factureAV.coddep")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime date);

   
}
