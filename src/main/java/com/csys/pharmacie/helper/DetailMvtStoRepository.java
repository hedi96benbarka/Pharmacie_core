package com.csys.pharmacie.helper;

import java.lang.Integer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetailMvtSto entity.
 */
@Repository
public interface DetailMvtStoRepository extends JpaRepository<DetailMvtSto, Integer> {
}

