package com.csys.pharmacie.transfert.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.transfert.domain.ResteRecuperation;
import com.csys.pharmacie.transfert.domain.ResteRecuperationPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ResteRecuperation entity.
 */
@Repository
public interface ResteRecuperationRepository extends JpaRepository<ResteRecuperation, ResteRecuperationPK> {

    public List<ResteRecuperation> findByPk_CodeDepotAndCategorieDepot(Integer depotID,CategorieDepotEnum categorieDepot);
}
