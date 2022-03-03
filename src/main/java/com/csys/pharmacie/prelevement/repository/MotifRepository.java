package com.csys.pharmacie.prelevement.repository;

import com.csys.pharmacie.prelevement.domain.Motif;
import java.lang.Integer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Motif entity.
 */
@Repository
public interface MotifRepository extends JpaRepository<Motif, Integer> {
}

