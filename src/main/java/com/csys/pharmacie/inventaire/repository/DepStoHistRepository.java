package com.csys.pharmacie.inventaire.repository;

import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.inventaire.domain.DepStoHist;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DepStoHist entity.
 */
@Repository
public interface DepStoHistRepository extends JpaRepository<DepStoHist, Integer>, QueryDslPredicateExecutor<DepStoHist> {

    @EntityGraph(value = "DepStoHist.inventaire", type = EntityGraph.EntityGraphType.FETCH)
    List<DepStoHist> findByInventaire_Code(Integer numInventaire);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(d.codeArticle,d.codeUnite,sum(d.stkDep)) FROM DepStoHist d"
            + " WHERE   d.inventaire.depot = ?1 and d.codeArticle= ?2 and d.inventaire.dateCloture = ?3 "
            + " group by  d.codeArticle,d.codeUnite")
    public List<TotalMouvement> findSoldeDepart(Integer coddep, Integer codart, Date date);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(d.codeArticle,d.codeUnite,sum(d.stkDep), d.inventaire.depot) FROM DepStoHist d"
            + " WHERE   d.inventaire.depot = ?1 and d.codeCategorieArticle= ?2 and d.inventaire.dateCloture = ?3 "
            + " group by  d.codeArticle,d.codeUnite, d.inventaire.depot")
    public List<TotalMouvement> findSoldeDepartByCodeCategorieArticle(Integer coddep, Integer codeCategorieArticle, Date date);

    List<DepStoHist> findByInventaire_CodeAndDatPerBefore(Integer codeInventaire, LocalDate date);

    @Modifying
    @Query(value = "INSERT INTO param_achat.DepSto_Hist ("
            + "code_inventaire,"
            + "code_article,"
            + "article_designation_Ar,"
            + "article_designation,"
            + "code_categorie_article,"
            + "categorie_article_designation_Ar,"
            + "categorie_article_designation,"
            + "code_unite,"
            + "unite_designation,"
            + "unite_designation_Ar,"
            + "Lot,"
            + "StkDep,"
            + "Qte0,"
            + "PU,"
            + "PuTotReel,"
            + "PuTotTheorique,"
            + "DatPer,"
            + "code_saisie,"
            + "taux_tva,"
            + "code_tva,"
            + "code_depsto,"
            + "numbon_origin_depsto,"
            + "numbon_depsto) "
            + "select :codeInventaire,d.codart,"
            + "a.designation_sec,a.designation,a.fk_categorie_article_code,"
            + "c.designation_sec,c.designation,"
            + "d.unite,u.designation_sec,u.designation,"
            + "isnull(d.Lot_Inter,''),isnull(d.stkrel,0),isnull(d.qte0,0),d.pu,(isnull(d.stkrel,0) * d.pu),(isnull(d.qte0,0) * d.pu),d.DatPer,"
            + "a.code_saisi, d.taux_tva,d.code_tva,d.code,d.numbon_origin,d.NumBon"
            + " from param_achat.depsto d \n"
            + "inner join param_achat.article a on d.codart = a.code \n"
            + " inner join param_achat.categorie_article c on a.fk_categorie_article_code = c.code \n"
            + "inner join param_achat.unite u on d.unite = u.code \n"
            + " where d.categ_depot =:categDepot and d.code in :codeDepsto", nativeQuery = true)
    public void insertDepstoHistWhenValidateInventory(@Param("codeInventaire") Integer codeInventaire, @Param("categDepot") String categDepot, @Param("codeDepsto") List<Integer> codeDepsto);

}
