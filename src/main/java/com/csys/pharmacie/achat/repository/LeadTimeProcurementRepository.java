package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.LeadTimeProcurement;
import com.csys.pharmacie.achat.dto.LeadTimeProcurementDTO;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadTimeProcurementRepository extends JpaRepository<LeadTimeProcurement, String> , QueryDslPredicateExecutor<LeadTimeProcurement>{

    List<LeadTimeProcurement> findByDateValidateDaBetweenAndCategorieArticle(Date deb, Date fin, String categ);

    @Query(value = "SELECT new com.csys.pharmacie.achat.dto.LeadTimeProcurementDTO(l.codeArticle as codeArticle, l.codeUnite as codeUnite,(coalesce(SUM(l.differenceDaReception),0)/ coalesce(count(l.codeArticle),0)) as leadTimeProcurement)  from LeadTimeProcurement l where l.dateValidateDa>=?1 and l.dateValidateDa<=?2 and categorieArticle=?3 group by l.codeArticle,l.codeUnite")
    List<LeadTimeProcurementDTO> findLeadTimeProcurementByArticleUnite(Date deb, Date fin, String categ);
}
