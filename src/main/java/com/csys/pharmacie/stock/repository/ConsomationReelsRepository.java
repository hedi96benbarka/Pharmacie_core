package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TotalMouvement;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.csys.pharmacie.stock.domain.ConsomationReels;

import com.querydsl.core.types.Predicate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface ConsomationReelsRepository extends JpaRepository<ConsomationReels, String>, QueryDslPredicateExecutor<ConsomationReels> {

    @Override
    public List<ConsomationReels> findAll(Predicate prdct);

    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement( m.codart,m.codeUnite,sum(m.quantite)) FROM ConsomationReels m\n"
            + "WHERE  m.codart in ?1 and m.date BETWEEN ?2 and ?3\n"
            + "group by  m.codart,m.codeUnite")
    public List<TotalMouvement> findQuantiteMouvement(List<Integer> codarts, LocalDateTime datedeb, LocalDateTime datefin);
    
    
    @Query("SELECT new com.csys.pharmacie.helper.TotalMouvement( m.codart,m.codeUnite,sum(m.quantite), sum(m.quantite * m.priach *(1+m.tauTvaAch/100)) as valeur) FROM ConsomationReels m\n"
            + "WHERE m.date BETWEEN ?1 and ?2 and m.categDepot=?3\n"
            + "group by  m.codart,m.codeUnite")
    public List<TotalMouvement> findQuantiteMouvement(LocalDateTime datedeb, LocalDateTime datefin, String categDepot);
    
}
