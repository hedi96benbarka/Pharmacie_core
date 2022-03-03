package com.csys.pharmacie.inventaire.repository;

import com.csys.pharmacie.inventaire.domain.Inventaire;
import com.csys.pharmacie.inventaire.domain.TraceTransfertInventaire;
import java.lang.Long;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TraceTransfertInventaire entity.
 */
@Repository
public interface TraceTransfertInventaireRepository extends JpaRepository<TraceTransfertInventaire, Long> {

List<TraceTransfertInventaire> findByInventaire_Code(Integer codeInventaire);
    
}

