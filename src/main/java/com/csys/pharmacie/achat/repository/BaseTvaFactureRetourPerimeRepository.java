package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.BaseTvaFactureRetourPerime;
import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BaseTvaFactureRetourPerime entity.
 */
@Repository
public interface BaseTvaFactureRetourPerimeRepository extends JpaRepository<BaseTvaFactureRetourPerime, Long> {
}

