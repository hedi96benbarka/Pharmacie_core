package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.FactureBonReception;
import java.lang.String;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FactureBonReception entity.
 */
@Repository
public interface FactureBonReceptionRepository extends JpaRepository<FactureBonReception, String>, QueryDslPredicateExecutor<FactureBonReception> {

    public FactureBonReception findByReferenceFournisseur(String referenceFournisseur);

    @Override
    @EntityGraph(value = "FactureBonReception.BonReceptionCollection", type = EntityGraph.EntityGraphType.FETCH)
    public FactureBonReception findOne(String numBon);

//    @Query(value = "select count(*) from GtresorerieEgypte.dbo.Notif_add_ded_fournisseur  where numopr_fcptfrs =?1 ",nativeQuery =true)  
    @Query(value = "select count(*) from dbo.Notif_add_ded_fournisseur  where numopr_fcptfrs =?1 ", nativeQuery = true)
    BigDecimal findFactureReglee(BigDecimal numopFcptfrs);
}
