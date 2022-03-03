package com.csys.pharmacie.achat.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.MvtStoBAPK;
import com.csys.pharmacie.achat.domain.MvtstoBACodartFrs;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.QteMouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

@Repository("MvtstoBARepository")
public interface MvtstoBARepository extends JpaRepository<MvtStoBA, MvtStoBAPK>, QueryDslPredicateExecutor<MvtStoBA> {

    @Override
    @EntityGraph(value = "MvtStoBA.factureBA", type = EntityGraph.EntityGraphType.LOAD)
    public Iterable<MvtStoBA> findAll(Predicate prdct);
//    @Query("SELECT m FROM MvtStoBA m WHERE m.numfacbl= ?1 ")
//    public List<MvtStoBA> findByNumfacblQ(String numfacbl);
//    @Query("SELECT m FROM MvtStoBA m WHERE m.pk.numbon = ?1 order by m.pk.numbon desc")

    public List<MvtStoBA> findByPkNumbon(String numbon);

    public List<MvtStoBA> findByPkNumbonIn(List<String> numbons);

    @Modifying
    @Query("DELETE FROM MvtStoBA m WHERE m.pk.numbon = ?1")
    public void deleteByNumbon(String numBon);

    @Query("SELECT m FROM MvtStoBA m WHERE m.pk.numbon = ?1 and m.pk.codart = ?2")
    public MvtStoBA findByNumbonAndCodArt(String numbon, String codart);

    @Query("SELECT NEW com.csys.pharmacie.achat.domain.MvtstoBACodartFrs(m.factureBA.codfrs,m.pk.codart,m.factureBA.raisoc) FROM MvtStoBA m WHERE  m.pk.codart in ?1 and m.typbon='BA' ORDER BY m.pk.numbon DESC")
    public List<MvtstoBACodartFrs> findByCodArtIn(List<Integer> codart);

    public MvtStoBA findByPk_CodartAndPk_NumbonAndLotInter(String numBon, String codArt, String lot);

//    @Query("SELECT NEW com.csys.pharmacie.vente.quittance.dto.MvtstoVO(m.pk.numbon,m.pk.codart, m.desart, m.codtva,m.tautva,m.qtecom,m.priuni,m.remise,m.lotInter,m.datPer) FROM MvtStoBA m WHERE  m.numfacbl=?1 and m.typbon=?2 ORDER BY m.pk.codart DESC")
//    public List<MvtstoVO> findByNumfacblAndTypbon(String numFacBl, String typbon);
//    @Query("SELECT NEW com.csys.pharmacie.vente.quittance.dto.MvtstoVO(m.pk.numbon,m.pk.codart, m.desart, m.codtva,m.tautva,m.qtecom,m.priuni,m.remise,m.lotInter,m.datPer) FROM MvtStoBA m WHERE  m.numfacbl=?1 and m.typbon=?2 and m.coddep=?3 ORDER BY m.pk.codart DESC")
//    public List<MvtstoVO> findByNumfacblAndTypbonAndCoddep(String numFacBl, String typbon,String coddep);
//    
//    @Query("SELECT new com.csys.pharmacie.helper.Mouvement( m.factureBA.datesys, m.factureBA.codfrs,m.factureBA.typbon, m.factureBA.numaffiche,m.priuni,sum(m.quantite),m.factureBA.heuresys) FROM MvtStoBA m"
//            + " WHERE  m.pk.codart = ?1  and m.coddep = ?2 and m.factureBA.datesys BETWEEN ?3 and ?4 and m.factureBA.typbon IN ('BA','RT')"
//            + " group by  m.factureBA.datesys, m.factureBA.codfrs,m.factureBA.raisoc,m.factureBA.typbon, m.factureBA.numaffiche,m.priuni,m.factureBA.heuresys")
//    public List<Mouvement> findListMouvement(String codart, String coddep, Date datedeb, Date datefin);
//    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.pk.codart,sum(m.quantite)) FROM MvtStoBA m"
//            + " WHERE  m.codart.famArt = ?1  and m.coddep = ?2 and m.factureBA.datesys BETWEEN ?3 and ?4 and m.factureBA.typbon ='BA'"
//            + " group by  m.pk.codart")
//    public List<QteMouvement> findQuantiteMouvement(String famart, String coddep, Date datedeb, Date datefin);
    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.pk.codart,sum(m.quantite)) FROM MvtStoBA m"
            + " WHERE m.coddep = ?1 and m.factureBA.datesys BETWEEN ?2 and ?3 and m.factureBA.typbon ='BA'"
            + " group by  m.pk.codart")
    public List<QteMouvement> findQuantiteMouvementTous(String coddep, Date datedeb, Date datefin);

//    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.pk.codart,sum(m.quantite)) FROM MvtStoBA m"
//            + " WHERE  m.codart.famArt = ?1  and m.coddep = ?2 and m.factureBA.datesys BETWEEN ?3 and ?4 and m.factureBA.typbon ='RT'"
//            + " group by  m.pk.codart")
//    public List<QteMouvement> findQuantiteMouvementRetour(String famart, String coddep, Date datedeb, Date datefin);
    @Query("SELECT new com.csys.pharmacie.helper.QteMouvement( m.pk.codart,sum(m.quantite)) FROM MvtStoBA m"
            + " WHERE m.coddep = ?1 and m.factureBA.datesys BETWEEN ?2 and ?3 and m.factureBA.typbon ='RT'"
            + " group by  m.pk.codart")
    public List<QteMouvement> findQuantiteMouvementRetourTous(String coddep, Date datedeb, Date datefin);

    //farouk
    @Query("SELECT  coalesce(SUM(m.quantite),0) FROM MvtStoBA m WHERE m.pk.codart = ?1  and m.coddep = ?2 and (m.factureBA.datesys BETWEEN ?3 and ?4 ) and m.factureBA.typbon ='BA'")
    public Integer findTotalMouvement(String codart, String coddep, Date datedeb, Date datefin);

    //farouk
    @Query("SELECT  coalesce(SUM(m.quantite),0) FROM MvtStoBA m WHERE m.pk.codart = ?1  and m.coddep = ?2 and (m.factureBA.datesys   BETWEEN ?3 and ?4 ) and m.factureBA.typbon ='RT'")
    public Integer findTotalMouvementRetour(String codart, String coddep, Date datedeb, Date datefin);

    @Query("SELECT  m.priuni from MvtStoBA m where  m.pk.codart=?1 order by m.factureBA.datesys desc ,m.factureBA.heuresys desc ")
    public List<BigDecimal> getDernierPrixAchat(String codart);

    @Query("Select m  from MvtStoBA m where m.pk.numbon In ?1 and m.pk.codart In ?2")
    List<MvtStoBA> findByNumBonInAndCodartIn(List<String> numBons, List<Integer> codarts);

    @Query("Select max(m.priuni)  from MvtStoBA m where m.pk.codart = ?1 and m.isPrixReference  = ?2 and m.qtecom>0 and m.factureBA.codAnnul is null and m.pk.numbon <> ?3")
    public BigDecimal findMaxPriuniByCodeArticleAndIsPrixRefAndNotReturnedAndNumBonNot(Integer codeArticle, Boolean isPrixReference, String numbon);

    @Query("Select max(m.priuni)  from MvtStoBA m where m.pk.codart = ?1 and m.isPrixReference  = ?2 and m.qtecom>0 and m.factureBA.codAnnul is null and m.typbon='BA' ")
    public BigDecimal findMaxPriuniByCodeArticleAndIsPrixRefAndNotReturned(Integer codeArticle, Boolean isPrixReference);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.pk.codart,m.codeUnite,sum(m.quantite), m.factureBA.coddep) FROM MvtStoBA m"
            + " WHERE   m.factureBA.coddep = ?1 and m.pk.codart= ?2 and m.factureBA.datbon BETWEEN ?3 and ?4 and m.factureBA.typbon=?5 and m.factureBA.codAnnul is null"
            + " group by  m.pk.codart,m.codeUnite, m.factureBA.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime datedeb, LocalDateTime datefin, TypeBonEnum typbon);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.pk.codart,m.codeUnite,sum(m.quantite),m.factureBA.coddep) FROM MvtStoBA m "
            + " WHERE   m.factureBA.coddep = ?1 and m.pk.codart= ?2 and m.factureBA.datbon < ?3 and m.factureBA.typbon=?4  and m.factureBA.codAnnul is null"
            + " group by  m.pk.codart,m.codeUnite, m.factureBA.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, Integer codart, LocalDateTime date, TypeBonEnum typbon);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.pk.codart,m.codeUnite,sum(m.quantite), m.factureBA.coddep) FROM MvtStoBA m"
            + " WHERE   m.factureBA.coddep = ?1 and m.pk.codart in ?2 and m.factureBA.datbon BETWEEN ?3 and ?4 and m.factureBA.typbon=?5  and m.factureBA.codAnnul is null"
            + " group by  m.pk.codart,m.codeUnite, m.factureBA.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime datedeb, LocalDateTime datefin, TypeBonEnum typbon);

    @Async
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.pk.codart,m.codeUnite,sum(m.quantite),m.factureBA.coddep) FROM MvtStoBA m "
            + " WHERE   m.factureBA.coddep = ?1 and m.pk.codart in ?2 and m.factureBA.datbon < ?3 and m.factureBA.typbon=?4  and m.factureBA.codAnnul is null"
            + " group by  m.pk.codart,m.codeUnite, m.factureBA.coddep")
    public CompletableFuture<List<TotalMouvement>> findTotalMouvement(Integer coddep, List<Integer> codart, LocalDateTime date, TypeBonEnum typbon);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.pk.codart,m.codeUnite,sum(m.quantite),m.factureBA.coddep) FROM MvtStoBA m "
            + " WHERE   m.factureBA.coddep = ?1 and m.factureBA.datbon < ?2 and m.factureBA.typbon=?3 and m.factureBA.codAnnul is null"
            + " group by  m.pk.codart,m.codeUnite,m.factureBA.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime date, TypeBonEnum typbon);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.pk.codart,m.codeUnite,sum(m.quantite),m.factureBA.coddep) FROM MvtStoBA m"
            + " WHERE   m.factureBA.coddep = ?1 and m.factureBA.datbon BETWEEN ?2 and ?3 and m.factureBA.typbon=?4 and m.factureBA.codAnnul is null"
            + " group by  m.pk.codart,m.codeUnite,m.factureBA.coddep")
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin, TypeBonEnum typbon);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.pk.codart,m.codeUnite,sum(m.quantite),m.factureBA.coddep) FROM MvtStoBA m "
            + " WHERE   m.factureBA.datbon < ?1 and m.factureBA.typbon=?2 and m.factureBA.codAnnul is null"
            + " group by  m.pk.codart,m.codeUnite,m.factureBA.coddep")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime date, TypeBonEnum typbon);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.pk.codart,m.codeUnite,sum(m.quantite),m.factureBA.coddep) FROM MvtStoBA m"
            + " WHERE m.factureBA.datbon BETWEEN ?1 and ?2 and m.factureBA.typbon=?3 and m.factureBA.codAnnul is null"
            + " group by  m.pk.codart,m.codeUnite,m.factureBA.coddep")
    public List<TotalMouvement> findTotalMouvement(LocalDateTime datedeb, LocalDateTime datefin, TypeBonEnum typbon);

    public List<MvtStoBA> findByPk_CodartInAndFactureBA_DatbonAfterAndTypbon(Set<Integer> codart, LocalDateTime datbon, String typeBon);

    public List<MvtStoBA> findByPk_CodartAndFactureBA_NumbonIn(Integer codarts, Set<String> numBons);

////    @EntityGraph(value = "FactureBA.detailFactureBACollection", type = EntityGraph.EntityGraphType.LOAD)
//    public MvtStoBA findTopByPk_CodartAndFactureBA_DatbonLessThanAndFactureBA_CodAnnulIsNullAndFactureBA_TypbonAndPriuniIsNotNullOrderByFactureBA_DatbonDesc(Integer codart, LocalDateTime datbon, TypeBonEnum type);
    @EntityGraph(value = "MvtStoBA.factureBA", type = EntityGraph.EntityGraphType.LOAD)
    public List<MvtStoBA> findByPk_CodartAndFactureBA_DatbonLessThanAndFactureBA_CodAnnulIsNullAndFactureBA_TypbonAndPriuniIsNotNull(Integer codart, LocalDateTime datbon, TypeBonEnum type);

    public Boolean existsByPk_CodartAndIsPrixReference(Integer codeArticle, Boolean isPrixReference);

    public Boolean existsByPk_Codart(Integer codeArticle);

    public List<MvtStoBA> findByPk_CodartInAndFactureBA_DatbonAfterAndFactureBA_CodfrsInAndTypbon(Set<Integer> codart, LocalDateTime datbon, Set<String> codeFournisseurs, String typeBon);

    public List<MvtStoBA> findByPk_CodartInAndFactureBA_DatbonAfterAndFactureBA_CodfrsAndTypbon(Set<Integer> codart, LocalDateTime datbon, String codeFournisseurs, String typeBon);

    @Query(value = "select distinct codart from param_achat.mvtstoba m inner join param_achat.FactureBA f on m.numbon= f.numbon \n"
            + "where F.categ_depot=:categDepot AND f.Automatique is null and f.codAnnul is null", nativeQuery = true)
    public List<String> findReceivedItemsByCategorieDepot(@Param("categDepot") String categDepot);

    @Query(value = "select distinct codart from param_achat.mvtstoba m inner join param_achat.FactureBA f on m.numbon= f.numbon \n"
            + "where F.categ_depot=:categDepot AND f.Automatique is null and f.codAnnul is null and m.codart in:articleIDs", nativeQuery = true)
    public List<String> findReceivedItemsByCategorieDepotAndCodesArticleIn(@Param("categDepot") String categDepot, @Param("articleIDs") List<Integer> articleIDs);

    @Query("SELECT m from MvtStoBA m where  m.priuni=?1 and m.categDepot=?2 and m.factureBA.datbon BETWEEN ?3 and ?4 and m.factureBA.codAnnul is null")
    public List<MvtStoBA> findByPriuniAndByCategDepot(BigDecimal valeur, CategorieDepotEnum categDepot, LocalDateTime datedeb, LocalDateTime datefin);

}
