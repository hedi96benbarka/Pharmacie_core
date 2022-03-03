package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.csys.pharmacie.stock.domain.Depsto;

import com.csys.pharmacie.helper.QteMouvement;
import com.csys.pharmacie.stock.dto.ArticleStockProjection;
import com.csys.pharmacie.stock.dto.RotationStock;
import com.querydsl.core.types.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface DepstoRepository extends JpaRepository<Depsto, Integer>, QueryDslPredicateExecutor<Depsto>, DepstoRepositoryCustom {

    List<Depsto> findByCodartAndQteGreaterThanAndCategDepot(Integer coddep, BigDecimal qte, CategorieDepotEnum categ);

    List<Depsto> findByCoddepAndQteGreaterThanAndCategDepot(Integer coddep, BigDecimal qte, CategorieDepotEnum categ);

    List<Depsto> findByCoddepInAndQteGreaterThanAndCategDepot(List<Integer> coddep, BigDecimal qte, CategorieDepotEnum categ);

    List<Depsto> findByQteGreaterThanAndCategDepot(BigDecimal qte, CategorieDepotEnum categ);

    List<Depsto> findByQteGreaterThan(BigDecimal qte);

    Boolean existsByCodartAndAInventorierTrueAndAndStkrelGreaterThan(Integer codart, BigDecimal stkrel);

    @Override

    public List<Depsto> findAll(Predicate prdct);

//    @Query("SELECT d FROM Depsto d WHERE d.lot like ?1")
//    public List<Depsto> findByLotLike(String lot);
//    @Modifying
//    @Query("UPDATE Depsto d SET d.qte = ?1 WHERE d.lot like ?2")
//    public void updateQteDepsto(BigDecimal qte, String numBon);
//    @Modifying
//
//    @Query("UPDATE Depsto d SET d.pu = ?1 WHERE d.lot like ?2 And d.codart = ?3")
//    public void UpdatePUByNumBonAndCodArt(BigDecimal pu, String numBon, String codart);
//    @Query(value = "SELECT s.desart as desArt ,d.Num as num,d.codart as codArt, d.DatPer as datePer, d.lot_inter as lot_inter, s.priach as priUni, coalesce(SUM(d.qte),0) as qte  FROM depsto d inner join stock s on s.codart=d.codart and (d.coddep =?1) and d.qte<>0 ORDER BY d.DatPer DESC", nativeQuery = true)
//    public List<ArticleInDep> getArtsInDep(String coddep);
    @Query(value = "SELECT coalesce(SUM(d.qte),0) FROM Depsto d WHERE d.codart = ?1 and d.coddep= ?2 ")
    BigDecimal findQteArtInDep(Integer codart, Integer coddep);

//    @Query(value = "SELECT d.codart as id, coalesce(SUM(d.qte),0) as qte from Depsto d where d.coddep=?1 and d.codart in ?2 group by d.codart HAVING SUM( d.qte)>0")
//    List<ArticleInDepProjection> findQuantiteOfArtInDep(String coddep, List<String> codarts);
    @Query(value = "SELECT   d.codart as id,coalesce(SUM(d.qte),0) as qte from Depsto d where d.coddep=?1 group by d.codart HAVING SUM(d.qte)>0")
    List<ArticleInDepProjection> findArtInDep(Integer coddep);

//    @Query(value = "SELECT  d.codart as id,coalesce(SUM(d.qte),0) as qte from Depsto d where d.coddep=?1 group by d.codart")
//    List<ArticleInDepProjection> findAllArtInDep(Integer coddep);
//
//    public List<LotInterDatPerQteProjection> findByCodartAndCoddepAndQteGreaterThan(Integer codArt, String codDep, BigDecimal qte);
//  
//    @Query(value = "SELECT    d.codart as id   ,coalesce(SUM(d.qte),0) as qte from Depsto d where d.coddep=?1 and d.codart in ?2 group by d.codart  ")
//    List<ArticleInDepProjection> findQuantiteOfArtInDepV1(Integer coddep, List<Integer> codarts);// ferou9
////      @Query(value = "SELECT  d.codart.codtva,d.codart.desart as desart , d.codart as id, d.codart.priach as priuni  ,SUM(d.qte) as qte from Depsto d where d.coddep=?1 and d.codart in ?2 group by d.codart,d.codart.desart,d.codart.priach,d.codart.codtva ")
    int countByCodartAndCoddepAndIdentifiantAndReferenceAndQteGreaterThan(Integer codArt, String codDep, String id, String ref, BigDecimal zero);// ferou9

//    @Query(value = "SELECT coalesce(SUM(d.qte),0) FROM Depsto d WHERE d.codart = ?1 and d.coddep= ?2 and d.identifiant=?3 and d.lotFrs=?4 ")
//    BigDecimal findQteArtInDepFrs(Integer codArt, String codDep, String id, String lotFrs);
//    @Query(value = "SELECT  d.codart as id  ,coalesce(SUM(d.qte),0) as qte from Depsto d where d.codart in ?1 group by d.codart")
//    List<ArticleInDepProjection> findQteArtsInAllDep(List<Integer> codarts);// ferou9
//    @Query(value = "SELECT  d.pu FROM Depsto d WHERE d.codart = ?1 and d.coddep= ?2 and d.identifiant=?3 and d.lotFrs=?4 and d.datPer=?5 order by d.num desc ")
//    Depsto findFirstByCodartAndCoddepAndIdentifiantAndLotFrsAndDatPerOrderByNumDesc(Integer codart, String coddep, String identifiant, String lotFrs, Date datPer);
    @Query(value = "SELECT coalesce(SUM(d.stkdep),0) FROM Depsto d WHERE d.codart = ?1 and d.coddep= ?2")
    Integer findStkDepByCodartAndCoddep(Integer codArt, String codDep);

    @Query(value = "SELECT new com.csys.pharmacie.helper.QteMouvement( d.codart,SUM(d.stkdep)) FROM Depsto d WHERE d.coddep= ?2 group by d.codart")
    List<QteMouvement> findQuantiteMouvement(String famart, String coddep);

    @Query(value = "SELECT new com.csys.pharmacie.helper.QteMouvement( d.codart,SUM(d.stkdep)) FROM Depsto d WHERE d.coddep= ?1 group by d.codart")
    List<QteMouvement> findQuantiteMouvementTous(String coddep);

    @Query(value = "SELECT MAX(d.pu) FROM Depsto d WHERE d.qte <> 0  and d.codart = ?1")
    //    @Query(value = "SELECT MAX(d.pu) FROM Depsto d WHERE d.qte <> 0 and d.depot.valoriser = 0 and d.codart = ?1")
    public BigDecimal findPrixAchatByCodart(Integer codart);//TODO valoriseer ?!

    @Query(value = "SELECT COUNT(d.coddep) FROM Depsto d WHERE d.qte <> 0 and d.coddep=?1")
    public Integer existsCoddep(String coddep);

    @Query(value = "SELECT coalesce(SUM(d.qte),0) FROM Depsto d WHERE d.codart = ?1")
    BigDecimal findQteArt(Integer codArt);

    public Long countByCoddepAndCodart(String dep, Integer s);

    @Query("SELECT COUNT( DISTINCT d.codart )FROM Depsto d where d.datPer <= ?1 and d.qte <> 0 ")
    public Integer findNombreArticlePerime(Date date);

    @Query("SELECT COUNT( DISTINCT d.codart )FROM Depsto d where d.datPer <= ?1 and d.datPer >= ?2  and d.qte <> 0  ")
    public Integer findNombreArticleProchPerime(Date date, Date now);

    @Query(value = "SELECT COUNT( DISTINCT d.codart )FROM Depsto d where d.datPer <= ?1 and d.datPer >= ?2 and d.qte > 0 ")
    public Integer findNombreArticleDepotFrsProchPerime(Date date, Date now);

//    @Async
    public CompletableFuture<List<Depsto>> findByCodartInAndQteGreaterThan(Collection<Integer> codArts, BigDecimal gt);

    public List<Depsto> findByCodartInAndNumBonNot(List<Integer> codeArts, String numBon);

    public List<Depsto> findByNumBon(String numBon);

//    @Async
    public CompletableFuture<List<Depsto>> findByCodartInAndCoddepAndQteGreaterThan(List<Integer> codArticles, Integer coddep, BigDecimal ZERO);

    public List<Depsto> findByCodartInAndCoddepAndQteGreaterThanAndDatPerGreaterThan(List<Integer> codArticles, Integer coddep, BigDecimal ZERO, LocalDate today);

    public Boolean existsByCodart(Integer articleID);

    public Boolean existsByCodartAndQteGreaterThan(Integer articleID, BigDecimal ZERO);

    @Query("SELECT   DISTINCT d.codart FROM Depsto d  where d.categDepot=?1 and d.coddep = ?2 ")
    public Integer[] findDistinctCodartByCategDepotAndCoddep(CategorieDepotEnum categDepot, Integer coddep);

    @Query("SELECT   DISTINCT d.codart FROM Depsto d  where d.categDepot=?1 and d.coddep = ?2 ")
    public Integer[] findByCoddepAndQteGreaterThan(CategorieDepotEnum categDepot, Integer coddep);

    public List<Depsto> findByCodeIn(List<Integer> ids);

    public List<Depsto> findByCodeIn(Integer[] ids);

    @Modifying
    @Query("DELETE FROM Depsto  d WHERE d.categDepot = ?1 and d.coddep = ?2 and d.stkrel <> 0 and cast(cast(d.codart as string)+cast(d.unite as string) as int) in ?3 ")
    public void deleteByCategDepotAndCoddepAndCodArt(CategorieDepotEnum categDepot, Integer coddep, List<Integer> codarts);

    public List<Depsto> findByCodartAndCoddepAndCategDepotAndUniteAndQte0GreaterThan(Integer codArticles, Integer coddep, CategorieDepotEnum categDepot, Integer unite, BigDecimal ZERO);

    @Query("SELECT d FROM Depsto  d WHERE d.categDepot = ?1 and d.coddep = ?2 and d.qte0 <> 0 and d.codart in ?3 ")
    public List<Depsto> findByCategDepotAndCoddepAndCodArt(CategorieDepotEnum categDepot, Integer coddep, List<Integer> codarts);

    @Query("SELECT d FROM Depsto  d WHERE d.categDepot = ?1 and d.coddep = ?2 and d.stkrel <> 0 and d.codart in ?3 ")
    public List<Depsto> findByCategDepotAndCoddepAndCodArtAndStkRel(CategorieDepotEnum categDepot, Integer coddep, List<Integer> codarts);

    @Query("SELECT d FROM Depsto  d WHERE d.categDepot = ?1 and d.coddep = ?2 and d.codart in ?3 ")
    public List<Depsto> findByCategDepotAndCoddepAndCodartIn(CategorieDepotEnum categDepot, Integer coddep, List<Integer> codarts);

    @Modifying
    @Query("update   Depsto  d set d.aInventorier = 0  WHERE d.categDepot = ?1 and d.coddep = ?2 and d.codart in ?3")
    public void updateInventerier(CategorieDepotEnum categDepot, Integer coddep, List<Integer> codarts);

    @Modifying(clearAutomatically = true)

    @Query(value = "update param_achat.depsto set qte = stkrel , stkdep = stkrel , stkrel = 0 where "
            + " coddep = ?1 and categ_depot =?2 and codart in ?3 and stkrel <> 0  ", nativeQuery = true)
    public void updateInventaireDepsto(Integer coddep, CategorieDepotEnum categDepot, List<Integer> listeArticle);

    @Query("SELECT DISTINCT new com.csys.pharmacie.stock.dto.RotationStock(d.codart,d.unite,d.coddep) FROM Depsto d where d.categDepot =?1 and d.coddep = ?2")
    public List<RotationStock> findByCategDepotAndCoddep(CategorieDepotEnum categDepot, Integer coddep);

    @Query("SELECT DISTINCT new com.csys.pharmacie.stock.dto.RotationStock(d.codart,d.unite,d.coddep) FROM Depsto d where d.categDepot =?1")
    public List<RotationStock> findByCategDepot(CategorieDepotEnum categDepot);

    @Query("SELECT DISTINCT new com.csys.pharmacie.stock.dto.RotationStock(d.codart,d.unite,d.coddep) FROM Depsto d where d.categDepot =?1 and d.codart in ?2")
    public CompletableFuture<List<RotationStock>> findByCategDepotAndCodArtIn(CategorieDepotEnum categDepot, List<Integer> codArts);

    @Query("SELECT isnull(SUM( d.qte ),0) FROM Depsto d where d.categDepot <= ?1 and d.coddep = ?2 and d.unite = ?3  and d.codart = ?4 and d.qte <> 0  ")
    public Integer getQteArtDepsto(CategorieDepotEnum categDepot, Integer coddep, Integer unite, Integer codart);

    @Query(value = "select actif from param_achat.article where code = ?1 ", nativeQuery = true)
    public Boolean getArticleActif(Integer codart);

    public List<Depsto> findByCategDepotAndCodartIn(CategorieDepotEnum categDepot, List<Integer> codArts);

    List<Depsto> findByCodartAndUniteAndCoddepAndQteGreaterThan(Integer codart, Integer unite, Integer coddep, BigDecimal qte);

    public Depsto findFirstByQteGreaterThanAndCodartOrderByPuDesc(BigDecimal qte, Integer codart);

    @Query("SELECT d FROM Depsto  d WHERE d.categDepot = ?1 and d.coddep = ?2 and d.codart in ?3 and d.datPer < ?4 and d.stkrel <> 0 ")
    public List<Depsto> findByCategDepotAndCoddepAndCodArtAndStkRelAndDatPerBefore(CategorieDepotEnum categDepot, Integer coddep, List<Integer> codarts, LocalDate date);

    public List<Depsto> findByCategDepotAndCodartAndUniteAndCoddep(CategorieDepotEnum categDepot, Integer codart, Integer unite, Integer coddep);

    @Query(value = "SELECT new com.csys.pharmacie.stock.dto.ArticleStockProjection(d.aInventorier, d.coddep as coddep, d.codart as codart, d.unite as unite, coalesce(SUM(d.qte),0) as qte, coalesce(SUM(d.qte0),0) as qte0) "
            + "from Depsto d where d.coddep=?1 and d.codart in ?2 and (((d.aInventorier = 0 OR d.aInventorier is null) AND qte > 0) or (d.aInventorier = 1 and qte0 > 0) ) group by d.coddep,d.codart,d.unite, d.aInventorier")
    List<ArticleStockProjection> findByCodeDepotAndCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot(Integer coddep, List<Integer> codarts);

    @Query(value = "SELECT new com.csys.pharmacie.stock.dto.ArticleStockProjection(d.codart as codart, d.unite as unite, (coalesce(SUM(d.qte),0) + coalesce(SUM(d.qte0),0)) as qte) "
            + "from Depsto d where d.codart in ?1 and ((d.aInventorier = 1 AND d.qte0>0) OR ((d.aInventorier = 0 OR d.aInventorier is null) AND qte > 0)) group by d.codart,d.unite")
    List<ArticleStockProjection> findByCodeArticleInGrouppedByCodeArticleAndCodeUnite(List<Integer> codarts);

    @Query(value = "SELECT new com.csys.pharmacie.stock.dto.ArticleStockProjection(d.codart as codart, d.unite as unite, (coalesce(SUM(d.qte),0) + coalesce(SUM(d.qte0),0) + coalesce(SUM(d.stkrel),0)) as qte) "
            + "from Depsto d where d.codart in ?1 and ((d.aInventorier = 1 AND d.qte0>0) OR (d.aInventorier = 1 AND d.stkrel>0) OR ((d.aInventorier = 0 OR d.aInventorier is null) AND qte > 0)) group by d.codart,d.unite")
    List<ArticleStockProjection> findSumQteAndQte0AndStkreelByCodeArticleInGrouppedByCodeArticleAndCodeUnite(List<Integer> codarts);

    @Query(value = "SELECT new com.csys.pharmacie.stock.dto.ArticleStockProjection(d.codart as codart, d.unite as unite,d.coddep as coddep, (coalesce(SUM(d.qte),0) + coalesce(SUM(d.qte0),0)) as qte) "
            + "from Depsto d where d.codart in ?1 and ((d.aInventorier = 1 AND d.qte0>0) OR ((d.aInventorier = 0 OR d.aInventorier is null) AND qte > 0)) group by d.codart,d.unite,d.coddep")
    List<ArticleStockProjection> findByCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot(List<Integer> codarts);

    @Query(value = "SELECT new com.csys.pharmacie.stock.dto.ArticleStockProjection(d.codart as codart, d.unite as unite,d.coddep as coddep, (coalesce(SUM(d.qte),0) + coalesce(SUM(d.qte0),0) + coalesce(SUM(d.stkrel),0)) as qte) "
            + "from Depsto d where d.codart in ?1 and ((d.aInventorier = 1 AND d.qte0>0) OR (d.aInventorier = 1 AND d.stkrel>0) OR ((d.aInventorier = 0 OR d.aInventorier is null) AND qte > 0)) group by d.codart,d.unite,d.coddep")
    List<ArticleStockProjection> findSumQteAndQte0AndStkreelByCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot(List<Integer> codarts);

    /**
     * methode for inventaire when we have a go life initialisation les donées
     * des despsto par code depot
     *
     * @param coddep
     * @param dateSys
     * @param categArticleIds
     */
    @Modifying
    @Query(value = "INSERT INTO param_achat.depsto (coddep,codart,qte,stkdep,stkrel,qte0,PU,DatPer,Lot_Inter,NumBon,numbon_origin,unite,categ_depot,datesys,code_tva,taux_tva,version,A_Inventorier) "
            + "select :coddep,a.code,0,0,0,1,(a.prix_achat / au.nbre_piece),'02/02/2030','lot_inj_inv','nb_inj_inv','nbo_inj_inv',au.fk_unite_code,'PH',:dateSys,a.fk_tva_codeA,tva.valeur,0,1\n"
            + " from param_achat.article a \n"
            + " inner join param_achat.article_unite au on au.fk_article_code = a.code\n"
            + " inner join param_achat.tva tva on tva.code = a.fk_tva_codeA\n"
            + " where   a.visible=1 and a.actif=1 and a.stopped <> 1 and a.type  = 'PH' and a.fk_categorie_article_code in :categArticleIds", nativeQuery = true)
    public void insertDepstoForDemarrageByDepotAndCategDepotPH(@Param("coddep") Integer coddep, @Param("dateSys") LocalDateTime dateSys, @Param("categArticleIds") List<Integer> categArticleIds);

    @Modifying
    @Query(value = "INSERT INTO param_achat.depsto (coddep,codart,qte,stkdep,stkrel,qte0,PU,DatPer,Lot_Inter,NumBon,numbon_origin,unite,categ_depot,datesys,code_tva,taux_tva,version,A_Inventorier) "
            + "select :coddep,a.code,0,0,0,1,(a.prix_achat),'02/02/2030','lot_inj_inv','nb_inj_inv','nbo_inj_inv',a.fk_unite_code,:categDepot,:dateSys,a.fk_tva_codeA,tva.valeur,0,1\n"
            + " from param_achat.article a \n"
            + " inner join param_achat.tva tva on tva.code = a.fk_tva_codeA\n"
            + " where   a.visible=1 and a.actif=1 and a.stopped <> 1 and a.type  = :categDepot and a.fk_categorie_article_code in :categArticleIds", nativeQuery = true)
    public void insertDepstoForDemarrageByDepotAndCategDepot(@Param("coddep") Integer coddep, @Param("categDepot") String categDepot, @Param("dateSys") LocalDateTime dateSys, @Param("categArticleIds") List<Integer> categArticleIds);

    @Modifying
    @Query("DELETE FROM Depsto  d WHERE d.categDepot = ?1 and d.coddep = ?2 and d.numBonOrigin = 'nbo_inj_inv' ")
    public void deleteByCategDepotAndCoddepAndNumBonOrigineIsDemarrage(CategorieDepotEnum categDepot, Integer coddep);

    @Modifying
    @Query(value = "update param_achat.depsto set pu= (select prix_achat from param_achat.article a where codart=a.code) where categ_depot=:categDepot\n"
            + "update param_achat.depsto set code_tva=(select fk_tva_codeV from param_achat.article a where  codart=a.code ) where categ_depot=:categDepot\n"
            + "update param_achat.DEPSTO SET taux_tva=(select valeur from param_achat.tva tva inner join param_achat.article a on a.fk_tva_codeV=tva.code\n"
            + "where  codart=a.code)  where categ_depot=:categDepot", nativeQuery = true)
    public void updatePrixAchatDepstoPourDemarrage(@Param("categDepot") String categDepot);

    @Modifying
    @Query(value = "update param_achat.depsto set pu= (select prix_achat from param_achat.article a where codart=a.code) where categ_depot=:categDepot  and codart in :codes\n"
            + "update param_achat.depsto set code_tva=(select fk_tva_codeV from param_achat.article a where  codart=a.code ) where categ_depot=:categDepot  and codart in :codes\n"
            + "update param_achat.DEPSTO SET taux_tva=(select valeur from param_achat.tva tva inner join param_achat.article a on a.fk_tva_codeV=tva.code\n"
            + "where  codart=a.code)  where categ_depot=:categDepot and codart in :codes", nativeQuery = true)
    public void updatePrixAchatDepstoPourDemarrageByArticleIn(@Param("categDepot") String categDepot, @Param("codes") List<Integer> articleID);

    @Modifying
    @Query(value = "update param_achat.depsto   \n"
            + "set  pu =  (a.prix_achat / au.nbre_piece)\n"
            + "from param_achat.depsto d\n"
            + "inner join param_achat.article a on d.codart = a.code\n"
            + "inner join param_achat.article_unite au on au.fk_article_code = d.codart and au.fk_unite_code = d.unite\n"
            + "where d.categ_depot=:categDepot \n"
            + "update param_achat.depsto set code_tva=(select fk_tva_codeV from param_achat.article a where  codart=a.code ) where categ_depot=:categDepot\n"
            + "update param_achat.DEPSTO SET taux_tva=(select valeur from param_achat.tva tva inner join param_achat.article a on a.fk_tva_codeV=tva.code\n"
            + "where  codart=a.code)  where categ_depot=:categDepot", nativeQuery = true)
    public void updatePrixAchatDepstoPourDemarragePH(@Param("categDepot") String categDepot);

    @Modifying
    @Query(value = "update param_achat.depsto   \n"
            + "set  pu =  (a.prix_achat / au.nbre_piece)\n"
            + "from param_achat.depsto d\n"
            + "inner join param_achat.article a on d.codart = a.code\n"
            + "inner join param_achat.article_unite au on au.fk_article_code = d.codart and au.fk_unite_code = d.unite\n"
            + "where d.categ_depot=:categDepot  and codart in :codes \n"
            + "update param_achat.depsto set code_tva=(select fk_tva_codeV from param_achat.article a where  codart=a.code ) where categ_depot=:categDepot  and codart in :codes \n"
            + "update param_achat.DEPSTO SET taux_tva=(select valeur from param_achat.tva tva inner join param_achat.article a on a.fk_tva_codeV=tva.code\n"
            + "where  codart=a.code)  where categ_depot=:categDepot and codart in :codes", nativeQuery = true)
    public void updatePrixAchatDepstoPourDemarrageByPHArticleIn(@Param("categDepot") String categDepot, @Param("codes") List<Integer> articleID);

    @Modifying
    @Query(value = "update param_achat.depsto \n"
            + "set qte0 = qte, qte = 0, stkrel = 0, A_Inventorier = 1 \n"
            + "from param_achat.depsto d\n"
            + "where code in :codes \n", nativeQuery = true)
    public void updateDepstoForOpenInventoryByCodeIn(@Param("codes") List<Integer> codeDepsto);

    @Modifying
    @Query(value = "update param_achat.depsto \n"
            + "set qte = stkrel, stkdep = stkrel, stkrel = 0, A_Inventorier = 0 \n"
            + "from param_achat.depsto d\n"
            + "where code in :codes \n", nativeQuery = true)
    public void updateDepstoQuantityToValidateInventoryByCodeIn(@Param("codes") List<Integer> codeDepsto);

    @Modifying
    @Query(value = "update param_achat.depsto \n"
            + "set A_Inventorier = 0 \n"
            + "from param_achat.depsto d\n"
            + "where code in :codes \n", nativeQuery = true)
    public void updateDepstoAInventorierToValidateInventoryByCodeIn(@Param("codes") List<Integer> codeDepsto);

}
