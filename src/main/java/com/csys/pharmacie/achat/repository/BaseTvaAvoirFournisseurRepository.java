package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.BaseTvaAvoirFournisseur;
import java.lang.Integer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BaseTvaAvoirFournisseur entity.
 */
@Repository
public interface BaseTvaAvoirFournisseurRepository extends JpaRepository<BaseTvaAvoirFournisseur, Integer> {
}

