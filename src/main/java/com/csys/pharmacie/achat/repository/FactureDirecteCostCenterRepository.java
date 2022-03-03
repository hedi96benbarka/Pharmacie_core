package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.FactureDirecteCostCenter;
import com.csys.pharmacie.achat.domain.FactureDirecteCostCenterPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FactureDirecteCostCenter entity.
 */
@Repository
public interface FactureDirecteCostCenterRepository extends JpaRepository<FactureDirecteCostCenter, FactureDirecteCostCenterPK>,QueryDslPredicateExecutor<FactureDirecteCostCenter> {
}

