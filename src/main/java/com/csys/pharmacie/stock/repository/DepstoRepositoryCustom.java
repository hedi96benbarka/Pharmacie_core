/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.stock.domain.Depsto;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author DELL
 */
public interface DepstoRepositoryCustom  {

    /**
     * @param prdct
     * @param pgbl limit begin from zero to pgbl
     * @return depstos groupped by codart, categDepot, unite, coddep, lotInter,
     * datPer, pu
     */
    List<Depsto> findAllGrouped(Predicate prdct, Pageable pgbl);
    
        /**
     * @param prdct
     * @return sum(qte) + sum(qte0) depstos groupped by codart, categDepot, unite, coddep, lotInter,
     * datPer, pu
     */
    List<Depsto> findAllGroupedByCodartAndUnite(Predicate prdct);

}
