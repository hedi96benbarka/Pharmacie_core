package com.csys.pharmacie.inventaire.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.inventaire.domain.DetailInventaire;
import com.csys.pharmacie.inventaire.domain.Inventaire;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Inventaire entity.
 */
@Repository
public interface InventaireRepository extends JpaRepository<Inventaire, Integer> {

    List<Inventaire> findByDepotAndCategorieDepot(Integer numDepot, String categorie);

    List<Inventaire> findByDateClotureIsNull();

//    List<Inventaire> findByDateClotureIsNullAndDepot(Integer depot);
    
    List<Inventaire> findByDateClotureIsNullAndDateAnnuleIsNullAndDepot(Integer depot);

    List<Inventaire> findByDepotAndCategorieArticleParentAndDateClotureIsNull(Integer numDepot, Integer catArticle); //inventaire ouvert

    List<Inventaire> findByDepotAndCategorieArticleParentAndDateClotureIsNotNull(Integer numDepot, Integer catArticle); //inventaire fermee

    public List<Inventaire> findByDateClotureBetween(Date date1, Date date2);

    public List<Inventaire> findByDateClotureBetweenAndDepot(Date date1, Date date2, Integer depot);
    
    public List<Inventaire> findByDepotAndIsDemarrageAndCategorieDepot(Integer depot, Boolean isDemarrage,CategorieDepotEnum categDepot);

    List<Inventaire> findByDepotAndCategorieDepotAndDateClotureIsNull(Integer numDepot, CategorieDepotEnum categ_depot);

    List<Inventaire> findByDepotAndCategorieDepotAndDateClotureIsNullAndDateAnnuleIsNull(Integer numDepot, CategorieDepotEnum categ_depot);

    List<Inventaire> findByDepotAndCategorieDepotAndDateClotureIsNullAndDateAnnuleIsNotNull(Integer numDepot, CategorieDepotEnum categ_depot);

    List<Inventaire> findByDepotAndCategorieDepotAndDateClotureNotNull(Integer numDepot, CategorieDepotEnum categ_depot);

    List<Inventaire> findByCategorieDepotAndDateClotureIsNull(CategorieDepotEnum categ_depot);

    List<Inventaire> findByCategorieDepotAndDateClotureIsNullAndDateAnnuleIsNotNull(CategorieDepotEnum categ_depot);
    
    List<Inventaire> findByCategorieDepotAndDateClotureIsNullAndDateAnnuleIsNull(CategorieDepotEnum categ_depot);

    List<Inventaire> findByDateClotureIsNullAndDepotAndCategorieDepot(Integer depot, CategorieDepotEnum categ_depot);

    @Query(value = "select code "
            + "from param_achat.inventaire i  inner join param_achat.detail_inventaire d on i.code = d.inventaire "
            + "where depot = ?1 and categorie_depot = ?2 and d.categorie_article = ?3 and dateCloture is null  ",
             nativeQuery = true)
    Integer invByDepotAndCatDepAndCatArt(Integer coddep, String categDepot, Integer categArt);
    
    List<Inventaire> findByDepotAndCategorieDepotAndDetailInventaireCollection_DetailInventairePK_CategorieArticleAndDateClotureIsNullAndDateAnnuleIsNull(Integer coddep, CategorieDepotEnum categDepot, Integer categArt);

}
