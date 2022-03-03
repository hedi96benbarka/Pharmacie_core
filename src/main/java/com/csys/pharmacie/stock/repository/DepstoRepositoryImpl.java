/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.domain.QDepsto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.hibernate.boot.model.source.internal.hbm.Helper.coalesce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 *
 * @author DELL
 */
@Repository
public class DepstoRepositoryImpl implements DepstoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;
    private final Logger log = LoggerFactory.getLogger(DepstoRepositoryImpl.class);

    @Override
    public List<Depsto> findAllGrouped(Predicate prdct, Pageable pgbl) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QDepsto depsto = QDepsto.depsto;
        List<Tuple> depstos = query.select(
                depsto.qte.sum(), depsto.codart, depsto.categDepot, depsto.unite, depsto.coddep, depsto.lotInter, depsto.datPer, depsto.pu).from(depsto)
                .where(prdct)
                .groupBy(depsto.codart, depsto.categDepot, depsto.unite, depsto.coddep, depsto.lotInter, depsto.datPer, depsto.pu)
                .limit(pgbl.getPageSize())
                .fetch();
        log.debug("depstos size {}", depstos.size());
        List<Depsto> result = new ArrayList();
        Integer index = 0;
        for (Tuple i : depstos) {
            Depsto deps = new Depsto();
            deps.setCode(index);
            deps.setQte(i.get(0, BigDecimal.class));
            deps.setCodart(i.get(1, Integer.class));
            deps.setCategDepot(i.get(2, CategorieDepotEnum.class));
            deps.setUnite(i.get(3, Integer.class));
            deps.setCoddep(i.get(4, Integer.class));
            deps.setLotInter(i.get(5, String.class));
            deps.setDatPer(i.get(6, LocalDate.class));
            deps.setPu(i.get(7, BigDecimal.class));
           result.add(deps) ;
            index++;

        }

        return result;
    }
    
    @Override
    public List<Depsto> findAllGroupedByCodartAndUnite(Predicate prdct) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QDepsto depsto = QDepsto.depsto;
        List<Tuple> depstos = query.select(
                 (depsto.qte.sum().coalesce(BigDecimal.ZERO).asNumber()).add(depsto.qte0.sum().coalesce(BigDecimal.ZERO).asNumber()) , depsto.codart, depsto.categDepot, depsto.unite).from(depsto)
                .where(prdct)
                .groupBy(depsto.codart, depsto.categDepot, depsto.unite)
                .fetch();
        log.debug("depstos size {}", depstos.size());
        List<Depsto> result = new ArrayList();
        Integer index = 0;
        for (Tuple i : depstos) {
            Depsto deps = new Depsto();
            deps.setCode(index);
            deps.setQte(i.get(0, BigDecimal.class));
            deps.setCodart(i.get(1, Integer.class));
            deps.setCategDepot(i.get(2, CategorieDepotEnum.class));
            deps.setUnite(i.get(3, Integer.class));
            result.add(deps);
            index++;
        }

        return result;
    }
}
