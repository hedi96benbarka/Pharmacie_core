package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.EtatReceptionCA;
import java.lang.Integer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EtatReceptionCA entity.
 */
@Repository
public interface EtatReceptionCARepository extends JpaRepository<EtatReceptionCA, Integer> {

    public List<EtatReceptionCA> findByCommandeAchatIn(List<Integer> codesCA);

    public void deleteByCommandeAchatIn(List<Integer> id);
}

