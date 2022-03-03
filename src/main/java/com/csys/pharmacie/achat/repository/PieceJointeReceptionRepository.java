package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.PieceJointeReception;
import java.lang.Long;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PieceJointeReception entity.
 */
@Repository
public interface PieceJointeReceptionRepository extends JpaRepository<PieceJointeReception, Long> {
}

