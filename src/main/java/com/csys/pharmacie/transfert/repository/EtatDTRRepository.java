package com.csys.pharmacie.transfert.repository;

import com.csys.pharmacie.transfert.domain.EtatDTR;
import java.lang.Integer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EtatDTR entity.
 */
@Repository
public interface EtatDTRRepository extends JpaRepository<EtatDTR, Integer> {
     public List<EtatDTR> findByCodedtrIn(List<Integer> codesDTR);
}

