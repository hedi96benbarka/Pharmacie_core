package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.stock.domain.ArticleReorderPointPlanning;
import com.csys.pharmacie.stock.dto.ArticleReorderPointPlanningDTO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArticleReorderPointPlanningFactory {

    public static ArticleReorderPointPlanningDTO articlereorderpointplanningToArticleReorderPointPlanningDTO(ArticleReorderPointPlanning articleReorderPointPlanning) {
        if (articleReorderPointPlanning != null) {
            ArticleReorderPointPlanningDTO articleReorderPointPlanningDTO = new ArticleReorderPointPlanningDTO();
            articleReorderPointPlanningDTO.setCode(articleReorderPointPlanning.getCode());
            articleReorderPointPlanningDTO.setCategDepot(articleReorderPointPlanning.getCategDepot());
            articleReorderPointPlanningDTO.setDateDuReference(articleReorderPointPlanning.getDateDuReference());
            articleReorderPointPlanningDTO.setDateAuReference(articleReorderPointPlanning.getDateAuReference());
            articleReorderPointPlanningDTO.setConsummingDays(articleReorderPointPlanning.getConsummingDays());
            articleReorderPointPlanningDTO.setDateCreate(articleReorderPointPlanning.getDateCreate());
            articleReorderPointPlanningDTO.setUserCreate(articleReorderPointPlanning.getUserCreate());
            articleReorderPointPlanningDTO.setPlanned(articleReorderPointPlanning.getPlanned());
            articleReorderPointPlanningDTO.setDataPrepared(articleReorderPointPlanning.isDataPrepared());
            articleReorderPointPlanningDTO.setLtpExecuted(articleReorderPointPlanning.isLtpExecuted());
            articleReorderPointPlanningDTO.setStdExecuted(articleReorderPointPlanning.isStdExecuted());
            articleReorderPointPlanningDTO.setExecuted(articleReorderPointPlanning.isExecuted());
            return articleReorderPointPlanningDTO;
        } else {
            return null;
        }
    }

    public static ArticleReorderPointPlanning articlereorderpointplanningDTOToArticleReorderPointPlanning(ArticleReorderPointPlanningDTO articleReorderPointPlanningDTO) {
        ArticleReorderPointPlanning articleReorderPointPlanning = new ArticleReorderPointPlanning();
        articleReorderPointPlanning.setCategDepot(articleReorderPointPlanningDTO.getCategDepot());
        articleReorderPointPlanning.setDateDuReference(articleReorderPointPlanningDTO.getDateDuReference());
        articleReorderPointPlanning.setDateAuReference(articleReorderPointPlanningDTO.getDateAuReference());
        articleReorderPointPlanning.setConsummingDays(articleReorderPointPlanningDTO.getConsummingDays());
        articleReorderPointPlanning.setPlanned(articleReorderPointPlanningDTO.isPlanned());
        articleReorderPointPlanning.setDataPrepared(articleReorderPointPlanningDTO.isDataPrepared());
        articleReorderPointPlanning.setExecuted(articleReorderPointPlanningDTO.isExecuted());
        return articleReorderPointPlanning;
    }
    
     public static Collection<ArticleReorderPointPlanningDTO> articleReorderPointPlanningsToArticleReorderPointPlanningDTOs(Collection<ArticleReorderPointPlanning> articleReorderPointPlannings) {
        List<ArticleReorderPointPlanningDTO> result = new ArrayList<>();
        articleReorderPointPlannings.forEach(x -> {
            result.add(articlereorderpointplanningToArticleReorderPointPlanningDTO(x));
        });
        return result;
    }
    
}
