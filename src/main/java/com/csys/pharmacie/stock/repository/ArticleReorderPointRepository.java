package com.csys.pharmacie.stock.repository;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.stock.domain.ArticleReorderPoint;
import com.querydsl.core.types.Predicate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleReorderPointRepository extends JpaRepository<ArticleReorderPoint, Integer> , QueryDslPredicateExecutor<ArticleReorderPoint> {
    
    @Override
    List<ArticleReorderPoint> findAll(Predicate predicate);
    
    List<ArticleReorderPoint> findByCategDepotOrderByDateCreateDesc(CategorieDepotEnum categDepot);

}

