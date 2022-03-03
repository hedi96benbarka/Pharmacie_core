package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.DetailMvtStoBA;
import com.csys.pharmacie.achat.domain.DetailMvtStoBAPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetailMvtStoBA entity.
 */
@Repository
public interface DetailMvtStoBARepository extends JpaRepository<DetailMvtStoBA, DetailMvtStoBAPK> {
}

