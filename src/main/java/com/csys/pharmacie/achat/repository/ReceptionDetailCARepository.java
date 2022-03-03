package com.csys.pharmacie.achat.repository;

import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.pharmacie.achat.domain.ReceptionDetailCAPK;
import com.csys.pharmacie.achat.dto.ReceptionDetailCADTO;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReceptionDetailCA entity.
 */
@Repository
public interface ReceptionDetailCARepository extends JpaRepository<ReceptionDetailCA, ReceptionDetailCAPK>, QueryDslPredicateExecutor<ReceptionDetailCA> {

    public List<ReceptionDetailCA> findByPkCommandeAchatIn(List<Integer> listCodeCAs);

    public List<ReceptionDetailCA> findByPkCommandeAchatNotIn(List<Integer> listCodeCAs);

    public List<ReceptionDetailCA> findByPkCommandeAchat(Integer id);

    public List<ReceptionDetailCA> findByPkReceptionIn(List<String> receptionIDs);

    
//    @EntityGraph(value = "reception_detail_ca.reception", type = EntityGraph.EntityGraphType.LOAD)
    public Set<ReceptionDetailCA> findByPk_CommandeAchatAndPk_Article(Integer codeCa,Integer codeArticle);
    
        public List<ReceptionDetailCA> findByPk_CommandeAchatIn(List<Integer> listCodeCAs);

}
