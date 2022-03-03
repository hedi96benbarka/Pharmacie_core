package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.FactureDirecteModeReglement;
import com.csys.pharmacie.achat.domain.FactureDirecteModeReglementPK;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FactureDirecteModeReglement entity.
 */
@Repository
public interface FactureDirecteModeReglementRepository extends JpaRepository<FactureDirecteModeReglement, FactureDirecteModeReglementPK> {
    Collection<FactureDirecteModeReglement> findByFactureDirecteModeReglementPK_NumBonIn(List<Integer> numBons);
}

