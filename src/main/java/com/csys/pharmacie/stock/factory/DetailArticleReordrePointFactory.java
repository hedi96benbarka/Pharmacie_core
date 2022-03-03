/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.stock.domain.DetailArticleReorderPoint;
import com.csys.pharmacie.stock.dto.DetailArticleReorderPointDTO;
import static com.csys.pharmacie.stock.factory.DetailDecoupageFactory.LANGUAGE_SEC;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author khouloud
 *
 */
public class DetailArticleReordrePointFactory {

    public static DetailArticleReorderPointDTO detailArticlereorderpointToDetailArticleReorderPointDTO(DetailArticleReorderPoint detailArticlereorderpoint, ArticleDTO article, UniteDTO unite) {
        DetailArticleReorderPointDTO detailArticleReorderPointDTO = new DetailArticleReorderPointDTO();
        detailArticleReorderPointDTO.setCode(detailArticlereorderpoint.getCode());
        detailArticleReorderPointDTO.setCodeArticle(detailArticlereorderpoint.getCodeArticle());
        detailArticleReorderPointDTO.setCodeUnite(detailArticlereorderpoint.getCodeUnite());
        detailArticleReorderPointDTO.setSafetyStockPerDay(detailArticlereorderpoint.getSafetyStockPerDay());
        detailArticleReorderPointDTO.setLeadTimeProcurement(detailArticlereorderpoint.getLeadTimeProcurement());
        detailArticleReorderPointDTO.setRealConsumming(detailArticlereorderpoint.getRealConsumming());
        detailArticleReorderPointDTO.setConsumingPerDay(detailArticlereorderpoint.getConsumingPerDay());
        detailArticleReorderPointDTO.setLeadTime(detailArticlereorderpoint.getLeadTime());
        detailArticleReorderPointDTO.setSafetyStock(detailArticlereorderpoint.getSafetyStock());
        detailArticleReorderPointDTO.setRop(detailArticlereorderpoint.getRop());
        detailArticleReorderPointDTO.setSafetyStockPerDaysConsumming(detailArticlereorderpoint.getSafetyStockPerDaysConsumming());
        detailArticleReorderPointDTO.setMaximumStock(detailArticlereorderpoint.getMaximumStock());
        detailArticleReorderPointDTO.setCurrentStock(detailArticlereorderpoint.getCurrentStock());
        detailArticleReorderPointDTO.setCodeArticleReorder(detailArticleReorderPointDTO.getCodeArticleReorder());
        detailArticleReorderPointDTO.setCodeSaisiArticle(article.getCodeSaisi());
        detailArticleReorderPointDTO.setDesignationUnite(unite.getDesignation());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            detailArticleReorderPointDTO.setDesignationArticle(article.getDesignation());
            detailArticleReorderPointDTO.setDesignationCategArticle(article.getCategorieArticle().getDesignation());

        } else {
            detailArticleReorderPointDTO.setDesignationArticle(article.getDesignationSec());
            detailArticleReorderPointDTO.setDesignationCategArticle(article.getCategorieArticle().getDesignationSec());

        }
        return detailArticleReorderPointDTO;
    }

    public static DetailArticleReorderPointDTO detailArticlereorderpointToDetailArticleReorderPointDTO(DetailArticleReorderPoint detailArticlereorderpoint) {
        DetailArticleReorderPointDTO detailArticleReorderPointDTO = new DetailArticleReorderPointDTO();
        detailArticleReorderPointDTO.setCode(detailArticlereorderpoint.getCode());
        detailArticleReorderPointDTO.setCodeArticle(detailArticlereorderpoint.getCodeArticle());
        detailArticleReorderPointDTO.setCodeUnite(detailArticlereorderpoint.getCodeUnite());
        detailArticleReorderPointDTO.setSafetyStockPerDay(detailArticlereorderpoint.getSafetyStockPerDay());
        detailArticleReorderPointDTO.setLeadTimeProcurement(detailArticlereorderpoint.getLeadTimeProcurement());
        detailArticleReorderPointDTO.setRealConsumming(detailArticlereorderpoint.getRealConsumming());
        detailArticleReorderPointDTO.setConsumingPerDay(detailArticlereorderpoint.getConsumingPerDay());
        detailArticleReorderPointDTO.setLeadTime(detailArticlereorderpoint.getLeadTime());
        detailArticleReorderPointDTO.setSafetyStock(detailArticlereorderpoint.getSafetyStock());
        detailArticleReorderPointDTO.setRop(detailArticlereorderpoint.getRop());
        detailArticleReorderPointDTO.setSafetyStockPerDaysConsumming(detailArticlereorderpoint.getSafetyStockPerDaysConsumming());
        detailArticleReorderPointDTO.setMaximumStock(detailArticlereorderpoint.getMaximumStock());
        detailArticleReorderPointDTO.setCurrentStock(detailArticlereorderpoint.getCurrentStock());
        detailArticleReorderPointDTO.setCodeArticleReorder(detailArticleReorderPointDTO.getCodeArticleReorder());
        return detailArticleReorderPointDTO;
    }

    public static Collection<DetailArticleReorderPointDTO> detailArticleReorderPointTodetailArticleReorderPointDTOs(Collection<DetailArticleReorderPoint> detailArticlereorderpoints) {
        List<DetailArticleReorderPointDTO> detailArticlereorderpointsDTO = new ArrayList<>();
        detailArticlereorderpoints.forEach(x -> {
            detailArticlereorderpointsDTO.add(detailArticlereorderpointToDetailArticleReorderPointDTO(x));
        });
        return detailArticlereorderpointsDTO;
    }
}
