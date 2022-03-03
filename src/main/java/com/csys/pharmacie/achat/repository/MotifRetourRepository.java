package com.csys.pharmacie.achat.repository;


import com.csys.pharmacie.achat.domain.MotifRetour;
import java.lang.Integer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MotifRetour entity.
 */
@Repository
public interface MotifRetourRepository extends JpaRepository<MotifRetour, Integer> {
}

