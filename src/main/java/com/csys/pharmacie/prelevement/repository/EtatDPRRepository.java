package com.csys.pharmacie.prelevement.repository;

import com.csys.pharmacie.prelevement.domain.EtatDPR;
import java.lang.Integer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EtatDPR entity.
 */
@Repository
public interface EtatDPRRepository extends JpaRepository<EtatDPR, Integer> {
     public List<EtatDPR> findByCodedprIn(List<Integer> codesDPR);
     public void deleteByCodedprIn(List<Integer> codesDPR);
}

