package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.stock.domain.ValeurStock;
import com.csys.pharmacie.stock.domain.ValeurStockPK;
import com.csys.pharmacie.stock.dto.MouvementStockEditionDTO;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.scheduling.annotation.Async;

public interface ValeurStockRepository extends JpaRepository<ValeurStock, ValeurStockPK>, QueryDslPredicateExecutor<ValeurStock> {
   List<ValeurStock> findByCoddepAndCodartAndValeurStockPK_Datesys(Integer coddep, Integer codart, LocalDate datesys);
   
       @Query("SELECT  NEW com.csys.pharmacie.stock.dto.MouvementStockEditionDTO(s.coddep, 'VS' as typbon, SUM(s.qte*s.pu*(1+s.tauxTvaAchat/100)) as valeur) FROM ValeurStock s"
            + " WHERE s.categDepot=?1 and s.coddep in ?2 and s.valeurStockPK.datesys=?3"
            + " GROUP BY s.coddep")
    public List<MouvementStockEditionDTO> findValeurStockGrouppedByDepot(CategorieDepotEnum categ, List<Integer> codeDepot, LocalDate datesys);
    
//           @Query("SELECT  NEW com.csys.pharmacie.stock.dto.MouvementStockEditionDTO(s.coddep, s.codart, s.unite, 'VS' as typbon, SUM(s.qte), SUM(s.qte*s.pu*(1+s.tauxTvaAchat/100)) as valeur) FROM ValeurStock s"
//            + " WHERE s.categDepot=?1 and s.coddep=?2 and s.codart in ?3 and s.valeurStockPK.datesys=?4"
//            + " GROUP BY s.coddep, s.codart, s.unite")
//    public List<MouvementStockEditionDTO> findValeurStockByListCodeArticle(CategorieDepotEnum categ, Integer codeDepot, List<Integer> codeArticles,  LocalDate datesys);
//    
//           @Query("SELECT  NEW com.csys.pharmacie.stock.dto.MouvementStockEditionDTO(s.coddep, s.codart, s.unite, 'VS' as typbon, SUM(s.qte), SUM(s.qte*s.pu*(1+s.tauxTvaAchat/100)) as valeur) FROM ValeurStock s"
//            + " WHERE s.categDepot=?1 and s.coddep=?2 and s.valeurStockPK.datesys=?3"
//            + " GROUP BY s.coddep, s.codart, s.unite")
//    public List<MouvementStockEditionDTO> findValeurStock(CategorieDepotEnum categ, Integer codeDepot, LocalDate datesys);
}
