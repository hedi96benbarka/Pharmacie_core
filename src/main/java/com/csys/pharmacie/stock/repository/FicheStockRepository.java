/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.stock.domain.FicheStock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 *
 * @author Administrateur
 */
public interface FicheStockRepository extends JpaRepository<FicheStock, String>, QueryDslPredicateExecutor<FicheStock> {

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(f.codart,f.codeUnite,sum(f.quantite), f.coddep) FROM FicheStock f"
            + " WHERE   f.coddep = ?1 and f.codart= ?2 and f.datbon < ?3 and f.typbon<>'IN' "
            + " group by  f.codart,f.codeUnite,f.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(f.codart,f.codeUnite,sum(f.quantite), f.coddep) FROM FicheStock f"
            + " WHERE   f.coddep = ?1 and f.codart= ?2 and f.datbon BETWEEN ?3 and ?4 and f.typbon<>'IN' "
            + " group by  f.codart,f.codeUnite,f.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin);
}
