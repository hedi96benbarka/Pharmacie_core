//package com.csys.pharmacie.achat.repository;
//
//import org.springframework.stereotype.Repository;
// 
//import com.csys.pharmacie.achat.domain.ArticleFournisseur;
//import com.csys.pharmacie.achat.domain.ArticleFournisseurPK;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.*;
//
///**
// * Spring Data JPA repository for the Brand entity.
// */
//@SuppressWarnings("unused")
//@Repository
//public interface ArticleFournisseurRepository extends JpaRepository<ArticleFournisseur, ArticleFournisseurPK> {
//
//    List<ArticleFournisseur> findByArticleFournisseurPK_fkFournisseurCodeAndArticleFournisseurPK_fkArticleCodeIn(String codfrs, Integer[] codart);
//
//    Boolean existsByArticleFournisseurPK_fkFournisseurCode(String codfrs);
//}
