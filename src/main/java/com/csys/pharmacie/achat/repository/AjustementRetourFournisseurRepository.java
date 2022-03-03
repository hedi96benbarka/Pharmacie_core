package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.AjustementRetourFournisseur;
import com.csys.pharmacie.achat.domain.AjustementRetourFournisseurPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AjustementRetourFournisseur entity.
 */
@Repository
public interface AjustementRetourFournisseurRepository extends JpaRepository<AjustementRetourFournisseur, AjustementRetourFournisseurPK> {
}

