package com.csys.pharmacie.prelevement.repository;

import com.csys.pharmacie.prelevement.domain.PrelevementDetailDPR;
import com.csys.pharmacie.prelevement.domain.PrelevementDetailDPRPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PrelevementDetailDPR entity.
 */
@Repository
public interface PrelevementDetailDPRRepository extends JpaRepository<PrelevementDetailDPR, PrelevementDetailDPRPK> {
    
  
   List<PrelevementDetailDPR> findByCodeDPRIn(List<Integer>listCodepr);
   List<PrelevementDetailDPR> findByCodeDPR(Integer id);
   List<PrelevementDetailDPR> findByPk_CodePrelevment(String numbon);
}

