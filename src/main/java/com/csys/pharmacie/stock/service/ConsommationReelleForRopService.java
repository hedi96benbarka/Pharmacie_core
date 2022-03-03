package com.csys.pharmacie.stock.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.stock.domain.ConsommationReelleForRop;
import com.csys.pharmacie.stock.domain.QConsommationReelleForRop;
import com.csys.pharmacie.stock.dto.ConsommationReelleForRopDTO;
import com.csys.pharmacie.stock.factory.ConsomationReelleForRopFactory;
import com.csys.pharmacie.stock.repository.ConsommationReelleForRopRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class ConsommationReelleForRopService {

    private final Logger log = LoggerFactory.getLogger(ConsommationReelleForRopService.class);

    private final ConsommationReelleForRopRepository consommationReelleForRopRepository;

    public ConsommationReelleForRopService(ConsommationReelleForRopRepository consommationReelleForRopRepository) {
        this.consommationReelleForRopRepository = consommationReelleForRopRepository;
    }

//    @Transactional(readOnly = true)
//    public List<ConsommationReelleForRopDTO> findAll(CategorieDepotEnum categ, LocalDateTime fromDate, LocalDateTime toDate, List<Integer> codarts) {
//        QConsommationReelleForRop _consommationReelleForRop = QConsommationReelleForRop.consommationReelleForRop;
//        WhereClauseBuilder builder = new WhereClauseBuilder()
//                .and(_consommationReelleForRop.categDepot.eq(categ.categ()))
//                .booleanAnd(codarts != null, () -> _consommationReelleForRop.codart.in(codarts))
//                .and(_consommationReelleForRop.date.goe(fromDate))
//                .and(_consommationReelleForRop.date.loe(toDate));
//        List<ConsommationReelleForRop> result = consommationReelleForRopRepository.findAll(builder);
//        return ConsomationReelleForRopFactory.consomationReelleForRopToConsommationReelleForRopDTOs(result);
//    }
    
      @Transactional(readOnly = true)
    public List<ConsommationReelleForRopDTO> findAll(CategorieDepotEnum categ,List<Integer> codarts) {
        log.debug("findAll ConsommationReelleForRopDTO");
        QConsommationReelleForRop _consommationReelleForRop = QConsommationReelleForRop.consommationReelleForRop;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(_consommationReelleForRop.categDepot.eq(categ.categ()))
                .booleanAnd(codarts != null, () -> _consommationReelleForRop.codart.in(codarts));
        List<ConsommationReelleForRop> result = consommationReelleForRopRepository.findAll(builder);
        return ConsomationReelleForRopFactory.consomationReelleForRopToConsommationReelleForRopDTOs(result);
    }

    /**
     * this method used just for categorie depot PH
     *
     * @param listConsummationReels
     * @param listArticlesPH
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConsommationReelleForRopDTO> grouppedConsommationReelleForRopByCodeAticleAndPrincipalUnity(List<ConsommationReelleForRopDTO> listConsummationReels, List<ArticlePHDTO> listArticlesPH) {

        listConsummationReels = listConsummationReels.stream().map(consommation -> {

            ArticlePHDTO matchedArticle = listArticlesPH.stream()
                    .filter(art -> art.getCode().equals(consommation.getCodart()))
                    .findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));

            ArticleUniteDTO matchedUnite = matchedArticle.getArticleUnites().stream()
                    .filter(unity -> unity.getCodeUnite().equals(consommation.getCodeUnite()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unity"));

            //      log.debug("article is : {}, consommation.getValeur():{} ", consommation.getCodart(),
            //  consommation.getQuantite().multiply(consommation.getPriach()).multiply(BigDecimal.ONE.add(consommation.getTauTvaAch().divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP))));
            BigDecimal qteInPrincipaleUnit = consommation.getQuantite().divide(matchedUnite.getNbPiece(), 0, RoundingMode.HALF_UP);
            consommation.setQuantite(qteInPrincipaleUnit);

            BigDecimal valeurInPrincipaleUnit = consommation.getQuantite().multiply(consommation.getPriach()).multiply(BigDecimal.ONE.add(consommation.getTauTvaAch().divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP))).divide(matchedUnite.getNbPiece(), 0, RoundingMode.HALF_UP);
            consommation.setValeur(valeurInPrincipaleUnit);

            consommation.setCodeUnite(matchedArticle.getCodeUnite());
            return consommation;
        }).collect(Collectors.groupingBy(item -> item.getCodart(),
                Collectors.reducing(new ConsommationReelleForRopDTO(BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
                    b.setQuantite(a.getQuantite().add(b.getQuantite()));
                    b.setValeur(a.getValeur().add(b.getValeur()));
                    // log.debug("article is : {},valeur is : {} ", a.getCodart(), a.getValeur());
                    return b;
                })))
                .values().stream().collect(toList());
        return listConsummationReels;
    }

    @Transactional(readOnly = true)
    public List<ConsommationReelleForRopDTO> grouppedConsommationReelleForRopByCodeAticle(List<ConsommationReelleForRopDTO> listConsummationReels, List<ArticleDTO> listArticles) {

        listConsummationReels = listConsummationReels.stream().map(consommation -> {
            BigDecimal valeur = consommation.getQuantite().multiply(consommation.getPriach()).multiply(BigDecimal.ONE.add(consommation.getTauTvaAch().divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)));
            consommation.setValeur(valeur);
            return consommation;
        }).collect(Collectors.groupingBy(item -> item.getCodart(),
                Collectors.reducing(new ConsommationReelleForRopDTO(BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
                    b.setQuantite(a.getQuantite().add(b.getQuantite()));
                    b.setValeur(a.getValeur().add(b.getValeur()));
                    return b;
                })))
                .values().stream().collect(toList());
        return listConsummationReels;
    }

}
