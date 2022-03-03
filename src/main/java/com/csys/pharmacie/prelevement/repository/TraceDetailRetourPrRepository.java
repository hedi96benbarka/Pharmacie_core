package com.csys.pharmacie.prelevement.repository;

import com.csys.pharmacie.prelevement.domain.TraceDetailRetourPr;
import com.csys.pharmacie.prelevement.domain.TraceDetailRetourPrPK;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TraceDetailRetourPr entity.
 */
@Repository
public interface TraceDetailRetourPrRepository extends JpaRepository<TraceDetailRetourPr, TraceDetailRetourPrPK> {
    
    
    List <TraceDetailRetourPr> findByTraceDetailRetourPrPK_CodeDetailMvtstoprIn(Set<Integer> codes);
}

