package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.DetailMvtStoRetourPerime;
import java.lang.Integer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Detail_MvtStoRetourPerime entity.
 */
@Repository
public interface DetailMvtStoRetourPerimeRepository extends JpaRepository<DetailMvtStoRetourPerime, Integer> {
}

