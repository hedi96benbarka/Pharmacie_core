package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.ReceivingCommande;
import com.csys.pharmacie.achat.domain.ReceivingCommandePK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReceivingCommande entity.
 */
@Repository
public interface ReceivingCommandeRepository extends JpaRepository<ReceivingCommande, ReceivingCommandePK> {
    List<ReceivingCommande> findByReceivingCommandePK_Reciveing(Integer reciveing);
}

