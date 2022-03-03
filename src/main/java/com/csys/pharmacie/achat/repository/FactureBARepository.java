package com.csys.pharmacie.achat.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csys.pharmacie.achat.domain.FactureBA;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

@Repository("FactureBARepository")
public interface FactureBARepository extends JpaRepository<FactureBA, String>, QueryDslPredicateExecutor<FactureBA> {

    @Override

    @EntityGraph(value = "FactureBA.receiving", type = EntityGraph.EntityGraphType.LOAD)
    public Iterable<FactureBA> findAll(Predicate prdct);


    public FactureBA findFirstByNumbon(String numbon);

    public List<FactureBA> findByRefFrsAndCodfrs(String reffrs, String codfrs);

    @EntityGraph(value = "FactureBA.detailFactureBACollection", type = EntityGraph.EntityGraphType.LOAD)
    public Set<FactureBA> findByNumpieceAndTypbon(String numbon, TypeBonEnum type);

    public List<FactureBA> findByNumpieceInAndTypbon(List<String> numbon, TypeBonEnum type);

 

    @Query("SELECT f.numbon FROM FactureBA f WHERE f.numpiece in ?1 and f.typbon = ?2")
    public List<String> findByNumpieceInAndTypbon(Collection<String> numbons, String type);

//    @Query(value = "SELECT new com.csys.pharmacie.helper.MouvementDuJour(COUNT(f.numbon),coalesce(SUM(f.mntbon),0)) FROM FactureBA f  WHERE f.datesys=?1 and f.typbon=?2 and f.stup= ?3")
//    public MouvementDuJour findMouvementDuJour(Date date, String typBon, Boolean stup);

    @EntityGraph(value = "FactureBA.detailFactureBACollection", type = EntityGraph.EntityGraphType.LOAD)
    Set<FactureBA> findByNumbonIn(List<String> numbons);

    public Set<FactureBA> findByReceiving_codeIn(List<Integer> codes);

    public FactureBA findByReceiving_code(Integer code);

    @Query(value = "SELECT MAX(f.numbon) FROM FactureBA f  WHERE f.categDepot = ?1 and f.typbon='BA'")
    public String findMaxNumbonByCategDepot(CategorieDepotEnum categDepot);
  

    public List<FactureBA> findByNumpieceInAndTypbonAndDatbonLessThanEqual(List<String> numbon, TypeBonEnum type,LocalDateTime date);    
     public FactureBA findByNumbonOrigin(String numbon);
}
