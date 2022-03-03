package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.DetailFactureDirecte;
import java.lang.Integer;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetailFactureDirecte entity.
 */
@Repository
public interface DetailFactureDirecteRepository extends JpaRepository<DetailFactureDirecte, Integer> {

    public Collection<DetailFactureDirecte> findByNumbon(String numBon);
   

    public void deleteByCodeIn(List<Integer> codes);
}
