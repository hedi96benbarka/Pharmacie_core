package com.csys.pharmacie.stock.service;

import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.dto.ClassificationArticleDTO;
import com.csys.pharmacie.client.dto.PalierClassificationArticleDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.*;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.domain.ArticleReorderPoint;
import com.csys.pharmacie.stock.domain.DetailArticleReorderPoint;
import com.csys.pharmacie.stock.domain.QArticleReorderPoint;
import com.csys.pharmacie.stock.domain.ValeurStock;
import com.csys.pharmacie.stock.dto.ArticleReorderPointDTO;
import com.csys.pharmacie.stock.dto.ArticleReorderPointPlanningDTO;
import com.csys.pharmacie.stock.dto.ConsommationReelleForRopDTO;
import com.csys.pharmacie.stock.dto.DetailArticleReorderPointDTO;
import com.csys.pharmacie.stock.factory.ArticleReorderPointFactory;
import com.csys.pharmacie.stock.factory.DetailArticleReordrePointFactory;
import com.csys.pharmacie.stock.repository.ArticleReorderPointRepository;
import com.csys.pharmacie.stock.repository.ConsommationReelleForRopRepository;
import com.csys.util.Preconditions;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticleReorderPointService {

    private final Logger log = LoggerFactory.getLogger(ArticleReorderPointService.class);

    private final ArticleReorderPointRepository articleReorderPointRepository;
    private final ConsommationReelleForRopService consommationReelleForRopService;
    private final ArticleReorderPointPlanningService articleReorderPointPlanningService;
    private final ConsommationReelleForRopRepository consommationReelleForRopRepository;
    private final ValeurStockService valeurStockService;
    private final ParamService paramService;
    private final ParamAchatServiceClient paramAchatServiceClient;

    public ArticleReorderPointService(ArticleReorderPointRepository articleReorderPointRepository, ConsommationReelleForRopService consommationReelleForRopService, ArticleReorderPointPlanningService articleReorderPointPlanningService, ConsommationReelleForRopRepository consommationReelleForRopRepository, ValeurStockService valeurStockService, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient) {
        this.articleReorderPointRepository = articleReorderPointRepository;
        this.consommationReelleForRopService = consommationReelleForRopService;
        this.articleReorderPointPlanningService = articleReorderPointPlanningService;
        this.consommationReelleForRopRepository = consommationReelleForRopRepository;
        this.valeurStockService = valeurStockService;
        this.paramService = paramService;
        this.paramAchatServiceClient = paramAchatServiceClient;
    }

    public void saveByPlaning(ArticleReorderPointPlanningDTO articleReorderPointPlanningDTO) {

        //construct articlereorderpointDTO from articleReorderPointPlanningDTO
        ArticleReorderPointDTO articleReorderPointDTO = new ArticleReorderPointDTO();
        articleReorderPointDTO.setCategDepot(articleReorderPointPlanningDTO.getCategDepot());
        articleReorderPointDTO.setConsummingDays(articleReorderPointPlanningDTO.getConsummingDays());
        articleReorderPointDTO.setDateDuReference(articleReorderPointPlanningDTO.getDateDuReference());
        articleReorderPointDTO.setDateAuReference(articleReorderPointPlanningDTO.getDateAuReference());
        articleReorderPointDTO.setUserCreate(articleReorderPointPlanningDTO.getUserCreate());
        articleReorderPointDTO.setCodePlanning(articleReorderPointPlanningDTO.getCode());

        //traitement Consommation reel : get consommation  entre date du au choisi ds interface add planning rop
        // eliminer consommation pour les depots onshelf
//        Period differenceReferenceDate = Period.between(articleReorderPointPlanningDTO.getDateDuReference().toLocalDate(), articleReorderPointPlanningDTO.getDateAuReference().toLocalDate());
//        log.debug("differenceReferenceDate.getMonths est {}", differenceReferenceDate.getMonths());
//        List<ConsommationReelleForRopDTO> listConsomationReels = new ArrayList();
//        for (int i = 0; i < differenceReferenceDate.getMonths() + 1; ++i) {
//            log.debug("i egale : {}", i);
//            LocalDateTime dateAuReferenceMaximal = articleReorderPointPlanningDTO.getDateDuReference().plusMonths(i).withHour(23).withMinute(59).withSecond(59).with(TemporalAdjusters.lastDayOfMonth());
//            log.debug("dateAuReferenceMaximal before : {}", dateAuReferenceMaximal.toString());
//
//            log.debug("articleReorderPointPlanningDTO.getDateAuReference() before : {}", articleReorderPointPlanningDTO.getDateAuReference());
//            if (dateAuReferenceMaximal.isAfter(articleReorderPointPlanningDTO.getDateAuReference())) {
//                dateAuReferenceMaximal = articleReorderPointPlanningDTO.getDateAuReference();
//            }
//            log.debug("dateAuReferenceMaximal after : {}", dateAuReferenceMaximal.toString());
//            List<ConsommationReelleForRopDTO> listConsomationReelsForTwoMonth = consommationReelleForRopService.findAll(articleReorderPointPlanningDTO.getCategDepot(),
//                    articleReorderPointPlanningDTO.getDateDuReference().plusMonths(i), dateAuReferenceMaximal, null);
//
//            log.debug("listConsomationReelsForTwoMonth : {}", listConsomationReels.size());
//            listConsomationReels.addAll(listConsomationReelsForTwoMonth);
//
//        }
//        log.debug("listConsomationReels : {}", listConsomationReels.size());
        List<ConsommationReelleForRopDTO> listConsummationReelsNotShelf = consommationReelleForRopService.findAll(articleReorderPointPlanningDTO.getCategDepot(), null);

//        Set<Integer> depotsIds = listConsomationReels.stream().map(item -> item.getCoddep()).collect(Collectors.toSet());
//
//        List<DepotDTO> listDepotDTOs = paramAchatServiceClient.findDepotsByCodes((Collection<Integer>) depotsIds);
//        List<Integer> depotsOnshelfIds = listDepotDTOs.stream().filter(depot -> Boolean.TRUE.equals(depot.getDepotFrs()))
//                .map(depot -> depot.getCode()).collect(Collectors.toList());
//        
//        List<ConsommationReelleForRopDTO> listConsummationReelsNotShelf = listConsomationReels.stream()
//                .filter(item -> !depotsOnshelfIds.contains(item.getCoddep())).collect(Collectors.toList());
        
//filter que les articles qu'on a une somme de valeur positifs
        listConsummationReelsNotShelf = listConsummationReelsNotShelf.stream()
                .filter(item -> item.getValeur().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(ConsommationReelleForRopDTO::getValeur).reversed())
                .collect(Collectors.toList());
        
        Preconditions.checkBusinessLogique(!listConsummationReelsNotShelf.isEmpty(), "pas.de.consommation.pour.cette.periode");

        Set<Integer> articleIds = listConsummationReelsNotShelf.stream().map(m -> m.getCodart()).collect(Collectors.toSet());

        if (CategorieDepotEnum.PH.equals(articleReorderPointPlanningDTO.getCategDepot())) {
            List<ArticlePHDTO> listArticlesPH = paramAchatServiceClient.articlePHFindbyListCode(articleIds);
            checkBusinessLogique(listArticlesPH.size() > 0, "stock.articleNotFound");

            //eliminer les artiticles isn't regeneration
//            Set<Integer> codeArticlesIsRegeneration = listArticlesPH.stream().filter(item -> item.isRegeneration()).map(item -> item.getCode()).collect(Collectors.toSet());
//            listConsummationReelsNotShelf = listConsummationReelsNotShelf.stream()
//                    .filter(c -> codeArticlesIsRegeneration.contains(c.getCodart()))
//                    .collect(Collectors.toList());
            log.debug("listConsummationReelsNotShelf before grouppement : {}", listConsummationReelsNotShelf.size());
//            listConsummationReelsNotShelf = consommationReelleForRopService.grouppedConsommationReelleForRopByCodeAticleAndPrincipalUnity(listConsummationReelsNotShelf, listArticlesPH);
            log.debug("listConsummationReelsNotShelf after grouppement : {}", listConsummationReelsNotShelf.size());

//calcul STD 
            listArticlesPH = this.updateConsummingPerDayForCategoriePH(listConsummationReelsNotShelf, listArticlesPH);
// save ROP + update min , max table article
            this.saveROPAndUpdateArticleForCategoriePH(articleReorderPointDTO, listConsummationReelsNotShelf, listArticlesPH);
        } else {
            List<ArticleDTO> listArticles = paramAchatServiceClient.articleFindbyListCode((Collection<Integer>) articleIds);
            checkBusinessLogique(listArticles.size() > 0, "stock.articleNotFound");

            //eliminer les artiticles isn't regeneration
//            Set<Integer> codeArticlesIsRegeneration = listArticles.stream().filter(item -> item.isRegeneration()).map(item -> item.getCode()).collect(Collectors.toSet());
//            listConsummationReelsNotShelf = listConsummationReelsNotShelf.stream()
//                    .filter(c -> codeArticlesIsRegeneration.contains(c.getCodart()))
//                    .collect(Collectors.toList());
//            listConsummationReelsNotShelf = consommationReelleForRopService.grouppedConsommationReelleForRopByCodeAticle(listConsummationReelsNotShelf, listArticles);
            //calcul STD 
            listArticles = this.updateConsummingPerDay(listConsummationReelsNotShelf, listArticles);
            // save ROP + update min , max table article
            this.saveROPAndUpdateArticle(articleReorderPointDTO, listConsummationReelsNotShelf, listArticles);
        }

        //update articleReorderPointPlanning
        articleReorderPointPlanningService.updateExcuted(articleReorderPointPlanningDTO.getCode());
        consommationReelleForRopRepository.deleteByCategDepot(articleReorderPointPlanningDTO.getCategDepot().categ());
    }

    public List<ArticlePHDTO> updateConsummingPerDayForCategoriePH(List<ConsommationReelleForRopDTO> listConsomationReels, List<ArticlePHDTO> listArticlesPH) {

//        listConsomationReels = listConsomationReels.stream()
//                .filter(item -> item.getValeur().compareTo(BigDecimal.ZERO) > 0)
//                .sorted(Comparator.comparing(ConsommationReelleForRopDTO::getValeur).reversed())
//                .collect(Collectors.toList());

        //sum valeur all consommation
        BigDecimal sommeValueOfConsumming = listConsomationReels.stream()
                .map(item -> item.getValeur())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b)).setScale(2, RoundingMode.HALF_UP);

        /* List<Integer> articlesIds = listConsomationReels.stream().map(item -> item.getCodart()).collect(Collectors.toList());
        List<Article> listArticlesInBase = articleRepository.findByCodeIn(articlesIds.toArray(new Integer[articlesIds.size()]));*/
        Collection<ClassificationArticleDTO> listClassificationArticle = paramAchatServiceClient.classificationArticleFindAll();
        Collection<PalierClassificationArticleDTO> palierClassificationArticleDTOs = paramAchatServiceClient.palierClassificationArticleFindAll();
        log.debug("sommeValueOfConsumming :{}", sommeValueOfConsumming.toString());
        Map<String, BigDecimal> mapPalier = new HashMap();
        palierClassificationArticleDTOs.forEach(palier -> {
            BigDecimal pourcentage = palier.getDu().compareTo(BigDecimal.ZERO) == 0 ? palier.getAu() : palier.getAu();
            BigDecimal seuil = sommeValueOfConsumming.multiply(pourcentage).divide(BigDecimal.valueOf(100));
            mapPalier.put(palier.getClassificationArticle(), seuil);
        });

        log.debug("seuilA :{},,seuilB :{},,seuilC :{}", mapPalier.get(ClassificationArticleEnum.A.name()).toString(),
                mapPalier.get(ClassificationArticleEnum.B.name()).toString(),
                mapPalier.get(ClassificationArticleEnum.C.name()).toString());

        BigDecimal accumulationValueQuantityOfConsumming = BigDecimal.ZERO;
        for (ConsommationReelleForRopDTO consommation : listConsomationReels) {
            accumulationValueQuantityOfConsumming = accumulationValueQuantityOfConsumming.add(consommation.getValeur());

            if (mapPalier.get(ClassificationArticleEnum.A.name()).compareTo(accumulationValueQuantityOfConsumming) > 0) {
                consommation.setClassificationArticle(ClassificationArticleEnum.A);
                log.debug("codearticle : {}, qte: {}", consommation.getCodart().toString(), consommation.getValeur().toString());
                log.debug("is class A : {}, {}", accumulationValueQuantityOfConsumming.toString(), mapPalier.get(ClassificationArticleEnum.A.name()));
            } else {
                if (mapPalier.get(ClassificationArticleEnum.B.name()).compareTo(accumulationValueQuantityOfConsumming) > 0) {
                    consommation.setClassificationArticle(ClassificationArticleEnum.B);
                } else {
                    consommation.setClassificationArticle(ClassificationArticleEnum.C);
                }
            }

            listArticlesPH = listArticlesPH.stream()
                    .filter(article -> article.isRegeneration())
                    .map(article -> {
                        if (article.getCode().equals(consommation.getCodart())) {
                            article.setClassificationArticle(consommation.getClassificationArticle());
                            if (article.getClassificationArticle() != null) {
                                article.setSafetyStockPerDay(listClassificationArticle.stream()
                                        .filter(c -> c.getCode().equals(article.getClassificationArticle().name()))
                                        .map(c -> c.getPalierClassificationArticle().getSafetyStockPerDay()).findFirst().get());
                            }
                        }
                        return article;
                    })
                    .collect(Collectors.toList());
        }

        return listArticlesPH;
    }

    public List<ArticleDTO> updateConsummingPerDay(List<ConsommationReelleForRopDTO> listConsomationReels, List<ArticleDTO> listArticles) {
//
//        listConsomationReels = listConsomationReels.stream()
//                .peek(item -> log.debug("item.getValeur() is : {}", item.toString()))
//                .filter(item -> item.getValeur().compareTo(BigDecimal.ZERO) > 0)
//                .sorted(Comparator.comparing(ConsommationReelleForRopDTO::getValeur).reversed())
//                .collect(Collectors.toList());

        //sum valeur all consommation
        BigDecimal sommeValueOfConsumming = listConsomationReels.stream()
                .map(item -> item.getValeur())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b)).setScale(2, RoundingMode.HALF_UP);

        /* List<Integer> articlesIds = listConsomationReels.stream().map(item -> item.getCodart()).collect(Collectors.toList());
        List<Article> listArticlesInBase = articleRepository.findByCodeIn(articlesIds.toArray(new Integer[articlesIds.size()]));*/
        Collection<ClassificationArticleDTO> listClassificationArticle = paramAchatServiceClient.classificationArticleFindAll();
        Collection<PalierClassificationArticleDTO> palierClassificationArticleDTOs = paramAchatServiceClient.palierClassificationArticleFindAll();
        log.debug("sommeValueOfConsumming :{}", sommeValueOfConsumming.toString());
        Map<String, BigDecimal> mapPalier = new HashMap();
        palierClassificationArticleDTOs.forEach(palier -> {
            BigDecimal pourcentage = palier.getDu().compareTo(BigDecimal.ZERO) == 0 ? palier.getAu() : palier.getAu();
            BigDecimal seuil = sommeValueOfConsumming.multiply(pourcentage).divide(BigDecimal.valueOf(100));
            mapPalier.put(palier.getClassificationArticle(), seuil);
        });

        log.debug("seuilA :{},,seuilB :{},,seuilC :{}", mapPalier.get(ClassificationArticleEnum.A.name()).toString(),
                mapPalier.get(ClassificationArticleEnum.B.name()).toString(),
                mapPalier.get(ClassificationArticleEnum.C.name()).toString());

        BigDecimal accumulationValueQuantityOfConsumming = BigDecimal.ZERO;
        for (ConsommationReelleForRopDTO consommation : listConsomationReels) {
            accumulationValueQuantityOfConsumming = accumulationValueQuantityOfConsumming.add(consommation.getValeur());

            if (mapPalier.get(ClassificationArticleEnum.A.name()).compareTo(accumulationValueQuantityOfConsumming) > 0) {
                consommation.setClassificationArticle(ClassificationArticleEnum.A);
                log.debug("codearticle : {}, qte: {}", consommation.getCodart().toString(), consommation.getValeur().toString());
                log.debug("is class A : {}, {}", accumulationValueQuantityOfConsumming.toString(), mapPalier.get(ClassificationArticleEnum.A.name()));
            } else {
                if (mapPalier.get(ClassificationArticleEnum.B.name()).compareTo(accumulationValueQuantityOfConsumming) > 0) {
                    consommation.setClassificationArticle(ClassificationArticleEnum.B);
                } else {
                    consommation.setClassificationArticle(ClassificationArticleEnum.C);
                }
            }

            listArticles = listArticles.stream()
                    .filter(article -> article.isRegeneration())
                    .map(article -> {
                        if (article.getCode().equals(consommation.getCodart())) {
                            article.setClassificationArticle(consommation.getClassificationArticle());
                            if (article.getClassificationArticle() != null) {
                                article.setSafetyStockPerDay(listClassificationArticle.stream()
                                        .filter(c -> c.getCode().equals(article.getClassificationArticle().name()))
                                        .map(c -> c.getPalierClassificationArticle().getSafetyStockPerDay()).findFirst().get());
                            }
                        }
                        return article;
                    })
                    .collect(Collectors.toList());
        }

        return listArticles;
    }

    public ArticleReorderPointDTO saveROPAndUpdateArticleForCategoriePH(ArticleReorderPointDTO articlereorderpointDTO, List<ConsommationReelleForRopDTO> listConsomationReels, List<ArticlePHDTO> listArticlesPH) {
        log.debug("Request to save ArticleReorderPoint: {}", articlereorderpointDTO);

        String codeSaisi = paramService.getcompteur(articlereorderpointDTO.getCategDepot(), TypeBonEnum.ROP);
        articlereorderpointDTO.setCodeSaisie(codeSaisi);
        ArticleReorderPoint articleReorderPoint = ArticleReorderPointFactory.articleReorderPointDTOToArticleReorderPointLazy(articlereorderpointDTO);

        long periodForConsuming = ChronoUnit.DAYS.between(articleReorderPoint.getDateDuReference().toLocalDate(), articleReorderPoint.getDateAuReference().toLocalDate()) + 1;

        BigDecimal consummingDays = articleReorderPoint.getConsummingDays();
        Set<Integer> articlesIds = listConsomationReels.stream().map(item -> item.getCodart()).collect(Collectors.toSet());
        log.debug("saveROPAndUpdateArticleForCategoriePH ******** articlesIds: {}", articlesIds.size());
        List<ValeurStock> listValeurStocks = valeurStockService.findAll(articleReorderPoint.getCategDepot(), articleReorderPoint.getDateAuReference().toLocalDate(), new ArrayList(articlesIds));

        List<ArticleDTO> listArticleToUpdateROP = new ArrayList<>();

        List<DetailArticleReorderPoint> listeDetailArticleReorderPoint = listConsomationReels.stream().map(consommationReel -> {

            DetailArticleReorderPoint detailArticleReorderPoint = new DetailArticleReorderPoint(consommationReel.getCodart(), BigDecimal.ZERO);

            ArticlePHDTO matchedArticle = listArticlesPH.stream().filter(art -> art.getCode().equals(consommationReel.getCodart()))
                    .findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));

            detailArticleReorderPoint.setCodeUnite(matchedArticle.getCodeUnite());

            Preconditions.checkBusinessLogique(matchedArticle.getLeadTimeProcurement() != null, "article-missing-leadTimeProcurement", matchedArticle.getCodeSaisi());
            detailArticleReorderPoint.setLeadTimeProcurement(matchedArticle.getLeadTimeProcurement());

            // Preconditions.checkBusinessLogique(matchedArticle.getSafetyStockPerDay() != null, "article-missing-safetyStockPerDay", matchedArticle.getCodeSaisi());
            if (matchedArticle.getSafetyStockPerDay() != null) {
                detailArticleReorderPoint.setSafetyStockPerDay(matchedArticle.getSafetyStockPerDay());
            } else {
                detailArticleReorderPoint.setSafetyStockPerDay(0);
            }

            detailArticleReorderPoint.setRealConsumming(consommationReel.getQuantite());

            BigDecimal qteStockInPrincipaleUnit = listValeurStocks.stream()
                    .filter(valeurStock -> valeurStock.getCodart().equals(consommationReel.getCodart()))
                    //.peek(valeurStock -> log.debug("valeurStock qte: {}", valeurStock.getQte()))
                    .map(valeurStock -> valeurStock.getQte().divide(matchedArticle.getArticleUnites().stream()
                    .filter(unity -> unity.getCodeUnite().equals(valeurStock.getUnite())).findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(valeurStock.getUnite().toString())))
                    .getNbPiece(), 0, RoundingMode.UP))
                    .reduce(BigDecimal.ZERO, (a, b) -> a.add(b))
                    .setScale(0, RoundingMode.UP);
            detailArticleReorderPoint.setCurrentStock(qteStockInPrincipaleUnit);
            detailArticleReorderPoint.setClassificationArticle(matchedArticle.getClassificationArticle());

            return detailArticleReorderPoint;
        })
                .collect(Collectors.groupingBy(item -> item.getCodeArticle(),
                        Collectors.reducing(new DetailArticleReorderPoint(BigDecimal.ZERO), (a, b) -> {
                            b.setRealConsumming(a.getRealConsumming().add(b.getRealConsumming()));
                            return b;
                        })))
                .values()
                .stream()
                // .peek(detailArticleReorderPoint -> log.debug("detailArticleReorderPoint: {}", detailArticleReorderPoint.toString()))
                .map(detailArticleReorderPoint -> {
                    detailArticleReorderPoint.setConsumingPerDay(detailArticleReorderPoint.getRealConsumming().divide(new BigDecimal(periodForConsuming), 4, RoundingMode.UP));
                    detailArticleReorderPoint.setLeadTime(new BigDecimal(detailArticleReorderPoint.getLeadTimeProcurement()).multiply(detailArticleReorderPoint.getConsumingPerDay()));
                    detailArticleReorderPoint.setSafetyStock(new BigDecimal(detailArticleReorderPoint.getSafetyStockPerDay()).multiply(detailArticleReorderPoint.getConsumingPerDay()));
                    detailArticleReorderPoint.setRop(detailArticleReorderPoint.getLeadTime().add(detailArticleReorderPoint.getSafetyStock()));
                    detailArticleReorderPoint.setSafetyStockPerDaysConsumming(detailArticleReorderPoint.getConsumingPerDay().multiply(consummingDays));
                    detailArticleReorderPoint.setMaximumStock(detailArticleReorderPoint.getRop().add(detailArticleReorderPoint.getSafetyStockPerDaysConsumming()));
                    detailArticleReorderPoint.setArticleReorderPoint(articleReorderPoint);
                    //listArticleToUpdateROP to send parametrage-achat-core pour update classification, min ,max
                    listArticleToUpdateROP.add(new ArticleDTO(
                            detailArticleReorderPoint.getCodeArticle(),
                            detailArticleReorderPoint.getClassificationArticle(),
                            detailArticleReorderPoint.getSafetyStockPerDay(),
                            detailArticleReorderPoint.getRop().setScale(0, RoundingMode.UP).intValueExact(),
                            detailArticleReorderPoint.getMaximumStock().setScale(0, RoundingMode.UP).intValueExact()));
                    return detailArticleReorderPoint;
                })
                .collect(toList());

        articleReorderPoint.setDetailArticleReorderPointCollection(listeDetailArticleReorderPoint);

        ArticleReorderPoint articleReorderPointInBase = articleReorderPointRepository.save(articleReorderPoint);
        log.debug("code articleReorderPoint : {}", articleReorderPointInBase.getCode());

        paramAchatServiceClient.articleMinimumAndMaximumStockUpdate(articlereorderpointDTO.getCategDepot(), articleReorderPointInBase.getCode(), listArticleToUpdateROP);

        paramService.updateCompteurPharmacie(articleReorderPoint.getCategDepot(), TypeBonEnum.ROP);
        return ArticleReorderPointFactory.articleReorderPointToArticleReorderPointDTO(articleReorderPoint);
    }

    public ArticleReorderPointDTO saveROPAndUpdateArticle(ArticleReorderPointDTO articlereorderpointDTO, List<ConsommationReelleForRopDTO> listConsomationReels, List<ArticleDTO> listArticles) {
        log.debug("Request to save ArticleReorderPoint: {}", articlereorderpointDTO);

        String codeSaisi = paramService.getcompteur(articlereorderpointDTO.getCategDepot(), TypeBonEnum.ROP);
        articlereorderpointDTO.setCodeSaisie(codeSaisi);
        ArticleReorderPoint articleReorderPoint = ArticleReorderPointFactory.articleReorderPointDTOToArticleReorderPointLazy(articlereorderpointDTO);

        long periodForConsuming = ChronoUnit.DAYS.between(articleReorderPoint.getDateDuReference().toLocalDate(), articleReorderPoint.getDateAuReference().toLocalDate());

        BigDecimal consummingDays = articleReorderPoint.getConsummingDays();
        Set<Integer> articlesIds = listConsomationReels.stream().map(item -> item.getCodart()).collect(Collectors.toSet());

        List<ValeurStock> listValeurStocks = valeurStockService.findAll(articleReorderPoint.getCategDepot(), articleReorderPoint.getDateAuReference().toLocalDate(), new ArrayList(articlesIds));

        List<ArticleDTO> listArticleToUpdateROP = new ArrayList<>();
        List<DetailArticleReorderPoint> listeDetailArticleReorderPoint = listConsomationReels.stream().map(consommationReel -> {

            DetailArticleReorderPoint detailArticleReorderPoint = new DetailArticleReorderPoint(consommationReel.getCodart(), BigDecimal.ZERO);

            ArticleDTO matchedArticle = listArticles.stream().filter(art -> art.getCode().equals(consommationReel.getCodart()))
                    .findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));

            Preconditions.checkBusinessLogique(matchedArticle.getLeadTimeProcurement() != null, "article-missing-leadTimeProcurement", matchedArticle.getCodeSaisi());
            detailArticleReorderPoint.setLeadTimeProcurement(matchedArticle.getLeadTimeProcurement());

            detailArticleReorderPoint.setCodeUnite(matchedArticle.getCodeUnite());
            detailArticleReorderPoint.setLeadTimeProcurement(matchedArticle.getLeadTimeProcurement());

            //Preconditions.checkBusinessLogique(matchedArticle.getSafetyStockPerDay() != null, "article-missing-safetyStockPerDay", matchedArticle.getCodeSaisi());
            BigDecimal qteConsummingInPrincipaleUnit = consommationReel.getQuantite();
            if (matchedArticle.getSafetyStockPerDay() != null) {
                detailArticleReorderPoint.setSafetyStockPerDay(matchedArticle.getSafetyStockPerDay());
            } else {
                detailArticleReorderPoint.setSafetyStockPerDay(0);
            }
            detailArticleReorderPoint.setRealConsumming(qteConsummingInPrincipaleUnit);

            BigDecimal qteStockInPrincipaleUnit = listValeurStocks.stream()
                    .filter(valeurStock -> valeurStock.getCodart().equals(consommationReel.getCodart()))
                    .map(valeurStock -> valeurStock.getQte())
                    .reduce(BigDecimal.ZERO, (a, b) -> a.add(b))
                    .setScale(0, RoundingMode.UP);

            detailArticleReorderPoint.setCurrentStock(qteStockInPrincipaleUnit);
            detailArticleReorderPoint.setClassificationArticle(matchedArticle.getClassificationArticle());
            return detailArticleReorderPoint;
        }).collect(Collectors.groupingBy(item -> item.getCodeArticle(),
                Collectors.reducing(new DetailArticleReorderPoint(BigDecimal.ZERO), (a, b) -> {
                    b.setRealConsumming(a.getRealConsumming().add(b.getRealConsumming()));
                    return b;
                })))
                .values().stream()
                //.peek(detailArticleReorderPoint -> log.debug("detailArticleReorderPoint : {}", detailArticleReorderPoint.toString()))
                .map(detailArticleReorderPoint -> {
                    detailArticleReorderPoint.setConsumingPerDay(detailArticleReorderPoint.getRealConsumming().divide(new BigDecimal(periodForConsuming), 4, RoundingMode.UP));
                    detailArticleReorderPoint.setLeadTime(new BigDecimal(detailArticleReorderPoint.getLeadTimeProcurement()).multiply(detailArticleReorderPoint.getConsumingPerDay()));
                    detailArticleReorderPoint.setSafetyStock(new BigDecimal(detailArticleReorderPoint.getSafetyStockPerDay()).multiply(detailArticleReorderPoint.getConsumingPerDay()));
                    detailArticleReorderPoint.setRop(detailArticleReorderPoint.getLeadTime().add(detailArticleReorderPoint.getSafetyStock()));
                    detailArticleReorderPoint.setSafetyStockPerDaysConsumming(detailArticleReorderPoint.getConsumingPerDay().multiply(consummingDays));
                    detailArticleReorderPoint.setMaximumStock(detailArticleReorderPoint.getRop().add(detailArticleReorderPoint.getSafetyStockPerDaysConsumming()));
                    detailArticleReorderPoint.setArticleReorderPoint(articleReorderPoint);
                    //listArticleToUpdateROP to send parametrage-achat-core pour update classification, min ,max
                    listArticleToUpdateROP.add(new ArticleDTO(
                            detailArticleReorderPoint.getCodeArticle(),
                            detailArticleReorderPoint.getClassificationArticle(),
                            detailArticleReorderPoint.getSafetyStockPerDay(),
                            detailArticleReorderPoint.getRop().setScale(0, RoundingMode.UP).intValueExact(),
                            detailArticleReorderPoint.getMaximumStock().setScale(0, RoundingMode.UP).intValueExact()));
                    return detailArticleReorderPoint;
                })
                .collect(toList());

        articleReorderPoint.setDetailArticleReorderPointCollection(listeDetailArticleReorderPoint);

        ArticleReorderPoint articleReorderPointInBase = articleReorderPointRepository.save(articleReorderPoint);
        log.debug("code articleReorderPoint : {}", articleReorderPointInBase.getCode());

        paramAchatServiceClient.articleMinimumAndMaximumStockUpdate(articlereorderpointDTO.getCategDepot(), articleReorderPointInBase.getCode(), listArticleToUpdateROP);
        paramService.updateCompteurPharmacie(articleReorderPoint.getCategDepot(), TypeBonEnum.ROP);
        return ArticleReorderPointFactory.articleReorderPointToArticleReorderPointDTO(articleReorderPoint);
    }

    @Transactional(readOnly = true)
    public ArticleReorderPoint findOne(Integer id) {
        log.debug("Request to get ArticleReorderPoint: {}", id);
        ArticleReorderPoint articlereorderpoint = articleReorderPointRepository.findOne(id);
        Preconditions.checkBusinessLogique(articlereorderpoint != null, "ArticleReorderPoint does not exist");
        return articlereorderpoint;
    }

    @Transactional(readOnly = true)
    public Collection<ArticleReorderPointDTO> findAll(CategorieDepotEnum categorieDepot, LocalDate fromDate, LocalDate toDate) {
        log.debug("Request to get all ArticleReorderPoint");
        QArticleReorderPoint articleAOP = QArticleReorderPoint.articleReorderPoint;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(articleAOP.categDepot.eq(categorieDepot))
                .and(articleAOP.dateCreate.goe(fromDate.atStartOfDay()))
                .and(articleAOP.dateCreate.loe(toDate.atTime(23, 59, 59)));
        List<ArticleReorderPoint> articlereorderpoint = articleReorderPointRepository.findAll(builder);
        Collection<ArticleReorderPointDTO> articleReorderPointDTOs = ArticleReorderPointFactory.articleReorderPointToArticleReorderPointDTOsLazy((Collection<ArticleReorderPoint>) articlereorderpoint);
        return articleReorderPointDTOs;
    }

    @Transactional(readOnly = true)
    public ArticleReorderPointDTO getArticleReorderPoint(Integer id) {
        log.debug("Request to get ArticleReorderPoint: {}", id);
        ArticleReorderPoint articlereorderpoint = articleReorderPointRepository.findOne(id);
        Preconditions.checkBusinessLogique(articlereorderpoint != null, "ArticleReorderPoint does not exist");
        ArticleReorderPointDTO dto = ArticleReorderPointFactory.articleReorderPointToArticleReorderPointDTO(articlereorderpoint);
        return dto;
    }

    @Transactional(readOnly = true)
    public ArticleReorderPointDTO getLastArticleReorderPoint(CategorieDepotEnum categorieDepot) {
        log.debug("Request to get LastArticleReorderPoint");
        List<ArticleReorderPoint> listArticlereorderpoint = articleReorderPointRepository.findByCategDepotOrderByDateCreateDesc(categorieDepot);
        Preconditions.checkBusinessLogique(!listArticlereorderpoint.isEmpty(), "ArticleReorderPoint does not exist");
        ArticleReorderPointDTO dto = ArticleReorderPointFactory.articleReorderPointToArticleReorderPointDTO(listArticlereorderpoint.get(0));
        return dto;
    }

    @Transactional(readOnly = true)
    public ArticleReorderPoint findArticleReorderPoint(Integer id) {
        log.debug("Request to get ArticleReorderPoint: {}", id);
        ArticleReorderPoint articlereorderpoint = articleReorderPointRepository.findOne(id);
        Preconditions.checkBusinessLogique(articlereorderpoint != null, "ArticleReorderPoint does not exist");
        return articlereorderpoint;
    }

    public void delete(Integer id) {
        log.debug("Request to delete ArticleReorderPoint: {}", id);
        articleReorderPointRepository.delete(id);
    }

    public byte[] editionArticleReorderPoint(Integer codeArticleReorderPoint) throws SQLException, ReportSDKException, IOException {
        log.debug("Request to get All FactureDirecteWithCostCenterForEdition");

        ArticleReorderPoint articleReorderPoint = findOne(codeArticleReorderPoint);

        Preconditions.checkBusinessLogique(articleReorderPoint != null, "articleReorderPoint.NotFound");
        Preconditions.checkBusinessLogique(!articleReorderPoint.getDetailArticleReorderPointCollection().isEmpty(), "data.empty");

        Collection<Integer> uniteIds = articleReorderPoint.getDetailArticleReorderPointCollection().stream().map(d -> d.getCodeUnite()).distinct().collect(Collectors.toList());
        Collection<Integer> articleIds = articleReorderPoint.getDetailArticleReorderPointCollection().stream().map(d -> d.getCodeArticle()).distinct().collect(Collectors.toList());

        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(uniteIds);
        List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(articleIds);

        List<DetailArticleReorderPointDTO> ListDetailArticleReorderPointDTOs = articleReorderPoint.getDetailArticleReorderPointCollection().stream().map(detail -> {

            ArticleDTO article = articles.stream()
                    .filter(art -> art.getCode().equals(detail.getCodeArticle()))
                    .findFirst()
                    .orElse(null);
            UniteDTO unite = unities.stream()
                    .filter(unit -> unit.getCode().equals(detail.getCodeUnite()))
                    .findFirst()
                    .orElse(null);

            DetailArticleReorderPointDTO detailArticleReorderPointDTO = DetailArticleReordrePointFactory.detailArticlereorderpointToDetailArticleReorderPointDTO(detail, article, unite);
            detailArticleReorderPointDTO.setRop(BigDecimal.valueOf(Math.ceil(detail.getRop().doubleValue())));
            detailArticleReorderPointDTO.setMaximumStock(BigDecimal.valueOf(Math.ceil(detail.getMaximumStock().doubleValue())));

            return detailArticleReorderPointDTO;
        }).collect(toList());

        Collection<ArticleReorderPointDTO> articlesReorderPointDTOImpression = new ArrayList<>();
        articlesReorderPointDTOImpression.add(ArticleReorderPointFactory.articleReorderPointToArticleReorderPointDTOLazy(articleReorderPoint));

        ReportClientDocument reportClientDoc = new ReportClientDocument();
        reportClientDoc.open("Reports/articleROP_excel_ar.rpt", 0);
        reportClientDoc.getDatabaseController().setDataSource(articlesReorderPointDTOImpression, ArticleReorderPointDTO.class, "Entete", "Entete");
        reportClientDoc.getDatabaseController().setDataSource(ListDetailArticleReorderPointDTOs, DetailArticleReorderPointDTO.class, "Commande", "Commande");
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
        headers.setContentType(excelMediaType);
        return Helper.read(byteArrayInputStream);
    }

}
