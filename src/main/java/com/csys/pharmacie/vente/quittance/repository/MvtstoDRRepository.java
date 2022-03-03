package com.csys.pharmacie.vente.quittance.repository;

import com.csys.pharmacie.vente.quittance.domain.MvtstoDR;
import com.csys.pharmacie.vente.quittance.domain.MvtstoDRPK;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MvtstoDR entity.
 */
@Repository
public interface MvtstoDRRepository extends JpaRepository<MvtstoDR, MvtstoDRPK>, QueryDslPredicateExecutor<MvtstoDR> {
    
//    @Async
//    @EntityGraph(value = "MvtstoDR.factureDR", type = EntityGraph.EntityGraphType.LOAD)
    List<MvtstoDR> findByFactureDR_NumdossAndFactureDR_CoddepAndFactureDR_DatbonBetween(String numDoss, Integer coddep, LocalDateTime dateDebut, LocalDateTime dateFin);
    
}

