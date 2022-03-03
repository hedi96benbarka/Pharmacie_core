package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.BaseTvaFactureDirecte;
import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BaseTvaFactureDirecte entity.
 */
@Repository
public interface BaseTvaFactureDirecteRepository extends JpaRepository<BaseTvaFactureDirecte, Long> {
}

