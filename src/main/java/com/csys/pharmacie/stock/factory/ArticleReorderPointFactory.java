package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.stock.domain.ArticleReorderPoint;
import com.csys.pharmacie.stock.domain.ArticleReorderPointPlanning;
import com.csys.pharmacie.stock.dto.ArticleReorderPointDTO;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ArticleReorderPointFactory {

    public static ArticleReorderPointDTO articleReorderPointToArticleReorderPointDTO(ArticleReorderPoint articlereorderpoint) {
        ArticleReorderPointDTO articlereorderpointDTO = new ArticleReorderPointDTO();
        articlereorderpointDTO.setCode(articlereorderpoint.getCode());
        articlereorderpointDTO.setCodeSaisie(articlereorderpoint.getCodeSaisie());
        articlereorderpointDTO.setDateDuReference(articlereorderpoint.getDateDuReference());
        articlereorderpointDTO.setDateAuReference(articlereorderpoint.getDateAuReference());
        articlereorderpointDTO.setDateCreate(articlereorderpoint.getDateCreate());
        articlereorderpointDTO.setUserCreate(articlereorderpoint.getUserCreate());
        articlereorderpointDTO.setConsummingDays(articlereorderpoint.getConsummingDays());
        articlereorderpointDTO.setCategDepot(articlereorderpoint.getCategDepot());
        articlereorderpointDTO.setDateDuReferenceEdition(Date.from(articlereorderpoint.getDateDuReference().toInstant(ZoneOffset.UTC)));
        articlereorderpointDTO.setDateAuReferenceEdition(Date.from(articlereorderpoint.getDateAuReference().atZone(ZoneId.systemDefault()).toInstant()));
        articlereorderpointDTO.setDateCreateEdition(Date.from(articlereorderpoint.getDateCreate().toInstant(ZoneOffset.UTC)));
        articlereorderpointDTO.setDetailArticleReorderPointCollection(
                DetailArticleReordrePointFactory.detailArticleReorderPointTodetailArticleReorderPointDTOs(articlereorderpoint.getDetailArticleReorderPointCollection()));

        return articlereorderpointDTO;
    }

        public static ArticleReorderPointDTO articleReorderPointToArticleReorderPointDTOLazy(ArticleReorderPoint articlereorderpoint) {
        ArticleReorderPointDTO articlereorderpointDTO = new ArticleReorderPointDTO();
        articlereorderpointDTO.setCode(articlereorderpoint.getCode());
        articlereorderpointDTO.setCodeSaisie(articlereorderpoint.getCodeSaisie());
        articlereorderpointDTO.setDateDuReference(articlereorderpoint.getDateDuReference());
        articlereorderpointDTO.setDateAuReference(articlereorderpoint.getDateAuReference());
        articlereorderpointDTO.setDateCreate(articlereorderpoint.getDateCreate());
        articlereorderpointDTO.setUserCreate(articlereorderpoint.getUserCreate());
        articlereorderpointDTO.setConsummingDays(articlereorderpoint.getConsummingDays());
        articlereorderpointDTO.setCategDepot(articlereorderpoint.getCategDepot());
        articlereorderpointDTO.setDateDuReferenceEdition(Date.from(articlereorderpoint.getDateDuReference().toInstant(ZoneOffset.UTC)));
     articlereorderpointDTO.setDateAuReferenceEdition(Date.from(articlereorderpoint.getDateAuReference().atZone(ZoneId.systemDefault()).toInstant()));
        articlereorderpointDTO.setDateCreateEdition(Date.from(articlereorderpoint.getDateCreate().toInstant(ZoneOffset.UTC)));

        return articlereorderpointDTO;
    }
        
    public static ArticleReorderPoint articleReorderPointDTOToArticleReorderPointLazy(ArticleReorderPointDTO articlereorderpointDTO) {
        ArticleReorderPoint articleReorderPoint = new ArticleReorderPoint();
        articleReorderPoint.setCode(articlereorderpointDTO.getCode());
        articleReorderPoint.setCodeSaisie(articlereorderpointDTO.getCodeSaisie());
        articleReorderPoint.setDateDuReference(articlereorderpointDTO.getDateDuReference().toLocalDate().atStartOfDay());
        articleReorderPoint.setDateAuReference(articlereorderpointDTO.getDateAuReference().toLocalDate().atTime(23, 59, 59));
        articleReorderPoint.setConsummingDays(articlereorderpointDTO.getConsummingDays());
        articleReorderPoint.setCategDepot(articlereorderpointDTO.getCategDepot());
        articleReorderPoint.setUserCreate(articlereorderpointDTO.getUserCreate());
        articleReorderPoint.setCodePlaning(articlereorderpointDTO.getCodePlanning());
        return articleReorderPoint;
    }
    public static Collection<ArticleReorderPointDTO> articleReorderPointToArticleReorderPointDTOsLazy(Collection<ArticleReorderPoint> articlereorderpoints) {
        List<ArticleReorderPointDTO> articlereorderpointsDTO = new ArrayList<>();
        articlereorderpoints.forEach(x -> {
            articlereorderpointsDTO.add(articleReorderPointToArticleReorderPointDTOLazy(x));
        });
        return articlereorderpointsDTO;
    }
    
    public static Collection<ArticleReorderPointDTO> articleReorderPointToArticleReorderPointDTOs(Collection<ArticleReorderPoint> articlereorderpoints) {
        List<ArticleReorderPointDTO> articlereorderpointsDTO = new ArrayList<>();
        articlereorderpoints.forEach(x -> {
            articlereorderpointsDTO.add(articleReorderPointToArticleReorderPointDTO(x));
        });
        return articlereorderpointsDTO;
    }
}
