//package com.csys.pharmacie.inventaire.repository;
//
//import com.csys.pharmacie.helper.DateInv;
//import com.csys.pharmacie.helper.TotalMouvement;
//import com.csys.pharmacie.inventaire.domain.MvtStoINV;
//import com.csys.pharmacie.inventaire.dto.DateInventaire;
//import java.time.LocalDateTime;
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.querydsl.QueryDslPredicateExecutor;
//import org.springframework.stereotype.Repository;
//import org.springframework.data.jpa.repository.Query;
//
///**
// * Spring Data JPA repository for the MvtStoINV entity.
// */
//@Repository
//public interface MvtStoINVRepository extends JpaRepository<MvtStoINV, Long>, QueryDslPredicateExecutor<MvtStoINV> {
//
//    @Query("Select max(m.heureSysteme)  from MvtStoINV m where m.codart = ?1 and m.coddep  = ?2 and m.heureSysteme < ?3")
//    public LocalDateTime findMaxHeureSystemByCodArtAndCoddepAndHeureSystemeGreaterThan(Integer codeArticle, Integer coddep, LocalDateTime heureSysteme);
//
//    @Query("Select new com.csys.pharmacie.helper.DateInv(m.coddep,max(m.heureSysteme))  from MvtStoINV m where m.codart = ?1 and m.coddep  = ?2 and m.heureSysteme < ?3  group by  m.coddep")
//    public List<DateInv> findMaxHeureSystemByCodArtAndCoddepInAndHeureSystemeGreaterThan(Integer codeArticle, Integer coddep, LocalDateTime heureSysteme);
//
//    @Query("Select new com.csys.pharmacie.helper.DateInv(m.coddep,max(m.heureSysteme))  from MvtStoINV m where m.codart = ?1 and m.heureSysteme < ?2  group by  m.coddep")
//    public List<DateInv> findMaxHeureSystemByCodArtAndHeureSystemeGreaterThan(Integer codeArticle, LocalDateTime heureSysteme);
//
//    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.coddep) FROM MvtStoINV m "
//            + " WHERE   m.coddep = ?1 and m.heureSysteme < ?2 and m.quantite>0"
//            + " group by  m.codart,m.unite,m.coddep")
//    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime date);
//
//    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.coddep) FROM MvtStoINV m"
//            + " WHERE   m.coddep = ?1 and m.heureSysteme BETWEEN ?2 and ?3 and m.quantite>0"
//            + " group by  m.codart,m.unite,m.coddep")
//    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);
//
//    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,-sum(m.quantite),m.coddep) FROM MvtStoINV m "
//            + " WHERE   m.coddep = ?1 and m.heureSysteme < ?2 and m.quantite<0"
//            + " group by  m.codart,m.unite,m.coddep")
//    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime date);
//
//    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,-sum(m.quantite),m.coddep) FROM MvtStoINV m"
//            + " WHERE   m.coddep = ?1 and m.heureSysteme BETWEEN ?2 and ?3 and m.quantite<0"
//            + " group by  m.codart,m.unite,m.coddep")
//    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime datedeb, LocalDateTime datefin);
//    
//    
//    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.coddep) FROM MvtStoINV m "
//            + " WHERE   m.heureSysteme < ?1 and m.quantite>0"
//            + " group by  m.codart,m.unite,m.coddep")
//    public List<TotalMouvement> findTotalMouvementEntree(LocalDateTime date);
//
//    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,sum(m.quantite),m.coddep) FROM MvtStoINV m"
//            + " WHERE m.heureSysteme BETWEEN ?1 and ?2 and m.quantite>0"
//            + " group by  m.codart,m.unite,m.coddep")
//    public List<TotalMouvement> findTotalMouvementEntree(LocalDateTime datedeb, LocalDateTime datefin);
//
//    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,-sum(m.quantite),m.coddep) FROM MvtStoINV m "
//            + " WHERE   m.heureSysteme < ?1 and m.quantite<0"
//            + " group by  m.codart,m.unite,m.coddep")
//    public List<TotalMouvement> findTotalMouvementSortie(LocalDateTime date);
//
//    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement(m.codart,m.unite,-sum(m.quantite),m.coddep) FROM MvtStoINV m"
//            + " WHERE   m.heureSysteme BETWEEN ?1 and ?2 and m.quantite<0"
//            + " group by  m.codart,m.unite,m.coddep")
//    public List<TotalMouvement> findTotalMouvementSortie(LocalDateTime datedeb, LocalDateTime datefin);
//    
//}
