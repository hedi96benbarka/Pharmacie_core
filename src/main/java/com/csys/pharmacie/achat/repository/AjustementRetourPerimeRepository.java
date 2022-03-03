package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.AjustementRetourPerime;
import com.csys.pharmacie.achat.domain.AjustementRetourPerimePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AjustementRetourPerime entity.
 */
@Repository
public interface AjustementRetourPerimeRepository extends JpaRepository<AjustementRetourPerime, AjustementRetourPerimePK> {
}

