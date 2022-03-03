package com.csys.pharmacie.inventaire.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.inventaire.domain.DepstoScan;
import java.lang.Long;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DepstoScan entity.
 */
@Repository
public interface DepstoScanRepository extends JpaRepository<DepstoScan, Long> {

    List<DepstoScan> findByCodartAndUniteAndCategDepotAndCoddepAndInventerier(Integer codArt, int unite, CategorieDepotEnum categDepot, Integer coddep, boolean invnterier);

    List<DepstoScan> findByCategDepotAndCoddepAndInventerierAndUserName(CategorieDepotEnum categDepot, Integer coddep, boolean invnterier, String userName);

    List<DepstoScan> findByCategDepotAndCoddepAndInventerierAndImporter(CategorieDepotEnum categDepot, Integer coddep, boolean invnterier, boolean importer);

    List<DepstoScan> findByCodInvAndDefectueuxAndImporter(Integer codeInventaire, boolean defect, boolean importer);

    @Query("select isnull(sum(u.quantite),0) from DepstoScan u where u.codart = ?1 and u.unite = ?2  and u.categDepot = ?3 and u.coddep = ?4 and u.inventerier = 0")
    BigDecimal sumQuaniteByCodartAndUnite(Integer codArt, int unite, CategorieDepotEnum categDepot, Integer coddep);

    @Query("select isnull(count(d.num),0) from DepstoScan d where d.num in ?1 and d.inventerier = 0 and d.importer = 0")
    Integer nombreRowForDelete(List<Long> num);

    @Query("select isnull(count(distinct d.codart),0) from DepstoScan d where d.categDepot = ?1 and d.coddep = ?2 and d.inventerier = 0 and d.importer = 0")
    Integer nombreRowForSelectDepot(CategorieDepotEnum categDepot, Integer coddep);

    @Modifying
    @Query("DELETE FROM DepstoScan  d WHERE d.num in ?1 and d.inventerier = 0 and d.importer = 0  ")
    public void deleteByCategDepotAndCoddepAndCodArt(List<Long> num);

    @Modifying
    @Query("update   DepstoScan  d set d.defectueux = 1  WHERE d.codart in ?1  and d.categDepot = ?2 and d.coddep = ?3 and GETDATE() >= d.datPer and d.importer = 0 and d.inventerier = 0 and d.defectueux = 0 ")
    public void updatePerimer(Set<Integer> codArt, CategorieDepotEnum categDepot, Integer coddep);

    @Modifying
    @Query("update   DepstoScan  d set d.importer = 1  WHERE d.codart in ?1  and d.categDepot = ?2 and d.coddep = ?3 and d.importer = 0 and d.inventerier = 0 ")
    public void updateImporter(Set<Integer> codArt, CategorieDepotEnum categDepot, Integer coddep);

    @Query("select isnull(count(d.num),0) from DepstoScan d where d.categArt in ?1  and d.categDepot = ?2 and d.coddep = ?3 and d.inventerier = 0 and d.importer = 0")
    Integer controleCloture(List<Integer> categArt, CategorieDepotEnum categDepot, Integer coddep);

    @Modifying
    @Query("update   DepstoScan  d set d.inventerier = 1  WHERE d.categArt in ?1  and d.categDepot = ?2 and d.coddep = ?3 and d.importer = 1 and d.inventerier = 0 ")
    public void updateCloture(List<Integer> categArt, CategorieDepotEnum categDepot, Integer coddep);

    List<DepstoScan> findByCodInvAndDatPerBeforeAndImporter(Integer codeInventaire, LocalDate date, Boolean importer);

    List<DepstoScan> findByCodInvAndImporterAndInventerier(Integer codeInventaire, boolean importer, boolean inventerier);

    List<DepstoScan> findByCodInv(Integer codeInventaire);

    public List<DepstoScan> findByCodartInAndCategDepotAndInventerierAndImporter(List<Integer> codeArticle, CategorieDepotEnum categDepot, Boolean invnterier, Boolean importer);

//select  * from param_achat.depsto_scan 
//where categ_depot = 'PH' and codart in (55531,55645) and Importer = 0 and Inventerier = 0 
}
