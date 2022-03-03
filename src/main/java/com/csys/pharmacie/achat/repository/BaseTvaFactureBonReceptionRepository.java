package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.BaseTvaFactureBonReception;
import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BaseTvaFactureBonReception entity.
 */
@Repository
public interface BaseTvaFactureBonReceptionRepository extends JpaRepository<BaseTvaFactureBonReception, Long> {
}

