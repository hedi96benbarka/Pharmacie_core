/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.stock.domain.ConsomationReels;
import com.csys.pharmacie.stock.domain.QConsomationReels;
import com.csys.pharmacie.stock.factory.ConsomationReelsFactory;
import com.csys.pharmacie.stock.repository.ConsomationReelsRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author khouloud
 */
@Service
public class ConsommationReelsService {

    private final Logger log = LoggerFactory.getLogger(ConsommationReelsService.class);

    private final ConsomationReelsRepository consomationReelsRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;

    public ConsommationReelsService(ConsomationReelsRepository consomationReelsRepository, ParamAchatServiceClient paramAchatServiceClient) {
        this.consomationReelsRepository = consomationReelsRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
    }

    @Transactional(readOnly = true)
    public List<ConsomationReels> findAll(CategorieDepotEnum categ, LocalDateTime fromDate, LocalDateTime toDate, List<Integer> codarts) {
        QConsomationReels _consomationReels = QConsomationReels.consomationReels;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(_consomationReels.categDepot.eq(categ.categ()))
                .booleanAnd(codarts != null, () -> _consomationReels.codart.in(codarts))
                .and(_consomationReels.date.goe(fromDate))
                .and(_consomationReels.date.loe(toDate));
        return consomationReelsRepository.findAll(builder);
    }

    public List<TotalMouvement> findConsommationReellesGrouppedByCodeAticleAndCodeUnite(LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categDepot) {
        if (categDepot.equals(CategorieDepotEnum.PH)) {
            List<TotalMouvement> listTotalMouvement = consomationReelsRepository.findQuantiteMouvement(fromDate, toDate, categDepot.name());
            List<Integer> codarts = listTotalMouvement.stream().map(m -> m.getCodart()).collect(Collectors.toList());
            List<ArticlePHDTO> articlesPH = paramAchatServiceClient.articlePHFindbyListCode(codarts);
            listTotalMouvement = listTotalMouvement.stream().map(mvtstoDTO -> {
                ArticlePHDTO matchedArticle = articlesPH.stream()
                        .filter(art -> art.getCode().equals(mvtstoDTO.getCodart()))
                        .findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));

                ArticleUniteDTO matchedUnite = matchedArticle.getArticleUnites().stream()
                        .filter(unity -> unity.getCodeUnite().equals(mvtstoDTO.getCodeUnite()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unity"));

                log.debug("article is : {}, mvtstoDTO.getValeur():{} ", mvtstoDTO.getCodart(), mvtstoDTO.getValeur());
                BigDecimal qteInPrincipaleUnit = mvtstoDTO.getQuantite().divide(matchedUnite.getNbPiece(), 0, RoundingMode.HALF_UP);
                mvtstoDTO.setQuantite(qteInPrincipaleUnit);

                BigDecimal valeurInPrincipaleUnit = mvtstoDTO.getValeur().divide(matchedUnite.getNbPiece(), 0, RoundingMode.HALF_UP);
                mvtstoDTO.setValeur(valeurInPrincipaleUnit);

                mvtstoDTO.setCodeUnite(matchedArticle.getCodeUnite());
                return mvtstoDTO;
            }).collect(Collectors.groupingBy(item -> item.getCodart(),
                    Collectors.reducing(new TotalMouvement(BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
                        b.setQuantite(a.getQuantite().add(b.getQuantite()));
                        b.setValeur(a.getValeur().add(b.getValeur()));
                        log.debug("article is : {},valeur is : {} ", a.getCodart(), a.getValeur());
                        return b;
                    })))
                    .values().stream().collect(toList());
            return listTotalMouvement;

        } else {
            return consomationReelsRepository.findQuantiteMouvement(fromDate, toDate, categDepot.name());
        }
    }

}
