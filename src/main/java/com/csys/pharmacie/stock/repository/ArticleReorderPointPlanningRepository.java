package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.stock.domain.ArticleReorderPointPlanning;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleReorderPointPlanningRepository extends JpaRepository<ArticleReorderPointPlanning, Integer>, QueryDslPredicateExecutor<ArticleReorderPointPlanning> {

    ArticleReorderPointPlanning findFirstByCategDepotAndPlannedTrue(CategorieDepotEnum categDepot);

    ArticleReorderPointPlanning findFirstByCategDepotAndPlannedTrueAndLtpExecutedTrueAndDataPreparedFalseAndExecutedFalse(CategorieDepotEnum categDepot);

    ArticleReorderPointPlanning findFirstByCategDepotAndPlannedTrueAndLtpExecutedTrueAndDataPreparedTrueAndExecutedFalse(CategorieDepotEnum categDepot);
    
    @Override
    List<ArticleReorderPointPlanning> findAll(Predicate predicate);

}
