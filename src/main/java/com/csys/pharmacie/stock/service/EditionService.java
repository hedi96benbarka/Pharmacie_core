package com.csys.pharmacie.stock.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.achat.dto.BonEditionDTO;
import com.csys.pharmacie.achat.dto.CategorieArticleDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.DetailEditionDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.achat.dto.PriceVarianceDTO;
import com.csys.pharmacie.achat.dto.ReceivingDTO;
import com.csys.pharmacie.achat.dto.ReceivingDetailsDTO;
import com.csys.pharmacie.achat.dto.ReceptionEditionDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.service.AvoirFournisseurService;
import com.csys.pharmacie.achat.service.FactureBAService;
import com.csys.pharmacie.achat.service.MvtStoBAService;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.achat.service.ReceivingService;
import com.csys.pharmacie.achat.service.RetourPerimeService;
import static com.csys.pharmacie.config.ServicesConfig.contextReception;
//import com.csys.pharmacie.article.repository.StockRepositoryImpl;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Helper;
import static com.csys.pharmacie.helper.Helper.distinctByKey;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.MouvementConsomation;
import com.csys.pharmacie.helper.QtePrixMouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.stock.domain.BaseMouvementStock;
import com.csys.pharmacie.stock.domain.ConsomationReels;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.domain.MouvementStock;
import com.csys.pharmacie.stock.domain.QConsomationReels;
import com.csys.pharmacie.stock.domain.QDepsto;
import com.csys.pharmacie.stock.domain.QMouvementStock;
import com.csys.pharmacie.stock.domain.QValeurStock;
import com.csys.pharmacie.stock.domain.QValeurStockGlobale;
import com.csys.pharmacie.stock.domain.ValeurStock;
import com.csys.pharmacie.stock.domain.ValeurStockGlobale;
import com.csys.pharmacie.stock.dto.DepstoDTO;
import com.csys.pharmacie.stock.dto.DepstoEditionDTO;
import com.csys.pharmacie.stock.dto.DepstoEditionValeurStockDTO;
import com.csys.pharmacie.stock.dto.MouvementStockEditionDTO;
import com.csys.pharmacie.stock.dto.RotationStockDTO;
import com.csys.pharmacie.stock.dto.ValeurStockDTO;
import com.csys.pharmacie.stock.factory.ConsomationReelsFactory;
import com.csys.pharmacie.stock.factory.DepstoDTOAssembler;
import static com.csys.pharmacie.stock.factory.DepstoFactory.DepstoTodepstoDTOs;
import static com.csys.pharmacie.stock.factory.DepstoFactory.depstoDTOToDepsto;
import com.csys.pharmacie.stock.factory.MouvementStockFactory;
import com.csys.pharmacie.stock.factory.ValeurStockAssembler;
import com.csys.pharmacie.stock.factory.ValeurStockFactory;
import com.csys.pharmacie.stock.repository.ConsomationReelsRepository;
import com.csys.pharmacie.stock.repository.DepstoRepository;
import com.csys.pharmacie.stock.repository.MouvementStockRepository;
import com.csys.pharmacie.stock.repository.ValeurStockGlobaleRepository;
import com.csys.pharmacie.stock.repository.ValeurStockRepository;
import com.csys.pharmacie.transfert.domain.FactureBE;
import com.csys.pharmacie.transfert.dto.FactureBEDTO;
import com.csys.pharmacie.transfert.dto.MvtStoBEDTO;
import com.csys.pharmacie.transfert.service.FactureBEService;
import com.csys.pharmacie.vente.avoir.service.AvoirService;
import com.csys.pharmacie.vente.quittance.service.MvtstoService;
import com.csys.util.Preconditions;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

@Service("EditionService")
public class EditionService {

    private final AvoirService avoirService;

    private final MvtstoService mvtstoService;

    private final MvtStoBAService mvtStoBAService;

    private final ParamServiceClient parametrageService;

    private final ParamAchatServiceClient paramAchatServiceClient;

    private final ValeurStockRepository valeurStockRepository;

    private final StockService stockService;

    private final DepstoRepository depstorepository;

    private final RetourPerimeService retourPerimeService;

    private final FactureBAService factureBAService;

    private final ConsomationReelsRepository consomationReelsRepository;

    private final ConsomationReelsFactory consomationReelsFactory;

    private final MouvementStockRepository mouvementStockRepository;

    private final ValeurStockGlobaleRepository valeurStockGlobalekRepository;

    private final AvoirFournisseurService avoirFournisseurService;

    private final FicheStockService ficheStockService;

    private final ValeurStockFactory valeurStockFactory;
    private final ReceivingService receivingService;

    private final FactureBEService factureBEService;

    @Autowired
    MessageSource messages;
    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }
    private final Logger log = LoggerFactory.getLogger(EditionService.class);

    public EditionService(AvoirService avoirService, MvtstoService mvtstoService, MvtStoBAService mvtStoBAService, ParamServiceClient parametrageService, ParamAchatServiceClient paramAchatServiceClient, ValeurStockRepository valeurStockRepository, StockService stockService, DepstoRepository depstorepository, RetourPerimeService retourPerimeService, FactureBAService factureBAService, ConsomationReelsRepository consomationReelsRepository, ConsomationReelsFactory consomationReelsFactory, MouvementStockRepository mouvementStockRepository, ValeurStockGlobaleRepository valeurStockGlobalekRepository, AvoirFournisseurService avoirFournisseurService, FicheStockService ficheStockService, ValeurStockFactory valeurStockFactory, ReceivingService receivingService, FactureBEService factureBEService) {
        this.avoirService = avoirService;
        this.mvtstoService = mvtstoService;
        this.mvtStoBAService = mvtStoBAService;
        this.parametrageService = parametrageService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.valeurStockRepository = valeurStockRepository;
        this.stockService = stockService;
        this.depstorepository = depstorepository;
        this.retourPerimeService = retourPerimeService;
        this.factureBAService = factureBAService;
        this.consomationReelsRepository = consomationReelsRepository;
        this.consomationReelsFactory = consomationReelsFactory;
        this.mouvementStockRepository = mouvementStockRepository;
        this.valeurStockGlobalekRepository = valeurStockGlobalekRepository;
        this.avoirFournisseurService = avoirFournisseurService;
        this.ficheStockService = ficheStockService;
        this.valeurStockFactory = valeurStockFactory;
        this.receivingService = receivingService;
        this.factureBEService = factureBEService;
    }

    public byte[] editionDetailsStockArticle(Integer codArt, String det, CategorieDepotEnum categ, String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<Depsto> listDepsto = depstorepository.findByCodartAndQteGreaterThanAndCategDepot(codArt, BigDecimal.ZERO, categ);
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;

        if (type.equalsIgnoreCase("P")) {
            if ("Prix".equals(det)) {
                reportClientDoc.open("Reports/StockDep" + local + ".rpt", 0);
            } else {
                reportClientDoc.open("Reports/StockDep-Per" + local + ".rpt", 0);
            }
        } else {
            if ("Prix".equals(det)) {
                reportClientDoc.open("Reports/StockDep_excel" + local + ".rpt", 0);
            } else {
                reportClientDoc.open("Reports/StockDep-Per_excel" + local + ".rpt", 0);
            }
        }
        checkBusinessLogique(listDepsto.size() > 0, "article.NotFoundInDepsto");
        List<Integer> unityIDs = listDepsto.stream().map(Depsto::getUnite).distinct().collect(Collectors.toList());
        List<Integer> depotIDs = listDepsto.stream().map(Depsto::getCoddep).distinct().collect(Collectors.toList());
        List<Integer> articleIDs = listDepsto.stream().map(Depsto::getCodart).distinct().collect(Collectors.toList());
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unityIDs);
        List<DepstoEditionDTO> detailedResult = listDepsto.stream().map(depsto -> {
            UniteDTO unite = unities.stream()
                    .filter(unit -> unit.getCode().equals(depsto.getUnite()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(unite != null, "stock.edition.missing-unity");

            DepstoEditionDTO depsoDTO = DepstoDTOAssembler.assembleEditionDTO(depsto, unite);
            return depsoDTO;
        }).collect(toList());
        List<DepotDTO> dep = paramAchatServiceClient.findDepotsByCodes(depotIDs);
        checkBusinessLogique(dep.size() > 0, "stock.depotNotFound");
        List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(articleIDs);
        checkBusinessLogique(articles.size() > 0, "stock.articleNotFound");

        reportClientDoc
                .getDatabaseController().setDataSource(articles, ArticleDTO.class,
                        "Commande_article", "Commande_article");
        reportClientDoc
                .getDatabaseController().setDataSource(dep, DepotDTO.class,
                        "Commande_depot", "Commande_depot");
        reportClientDoc
                .getDatabaseController().setDataSource(detailedResult, DepstoEditionDTO.class,
                        "Commande_depsto", "Commande_depsto");
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    public byte[] editionValeurStock(List<Integer> coddep, Integer categorieArticle, String det, CategorieDepotEnum categ, String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<Depsto> listDepsto;
        String CategorieArticleDesignation = "";
        if (coddep != null) {
            listDepsto = depstorepository.findByCoddepInAndQteGreaterThanAndCategDepot(coddep, BigDecimal.ZERO, categ);
        } else {
            listDepsto = depstorepository.findByQteGreaterThanAndCategDepot(BigDecimal.ZERO, categ);
        }
        List<DepstoDTO> listDepstoDTO = DepstoTodepstoDTOs(listDepsto);
        if (categorieArticle != null) {
            Collection<ArticleDTO> listeArticleAffectedTOCategorieArticle = paramAchatServiceClient.articleByCategArt(categorieArticle);
            if (language.equals(new Locale(LANGUAGE_SEC).getLanguage())) {
                CategorieArticleDesignation = paramAchatServiceClient.categorieArticleById(categorieArticle).getDesignation();
            } else {
                CategorieArticleDesignation = paramAchatServiceClient.categorieArticleById(categorieArticle).getDesignationSec();
            }

            List<DepstoDTO> resultatToReturn = new ArrayList<>();
            listDepstoDTO.forEach(el -> {
                Optional<ArticleDTO> matchedArt = listeArticleAffectedTOCategorieArticle
                        .stream()
                        .filter(art -> el.getArticleID().equals(art.getCode())).findFirst();
                if (matchedArt.isPresent()) {
                    resultatToReturn.add(el);
                }
            });
            listDepstoDTO = resultatToReturn;
        }
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        if (type.equalsIgnoreCase("P")) {
            if ("Prix".equals(det)) {
                reportClientDoc.open("Reports/Valeur de Stock" + local + ".rpt", 0);
            } else {
                reportClientDoc.open("Reports/Valeur de Stock - datePer" + local + ".rpt", 0);
            }

        } else {
            if ("Prix".equals(det)) {
                reportClientDoc.open("Reports/Valeur de Stock_excel" + local + ".rpt", 0);
            } else {
                reportClientDoc.open("Reports/Valeur de Stock - datePer_excel" + local + ".rpt", 0);
            }
        }
        List<Depsto> depstos = new ArrayList<>();
        log.debug("size depsos est {}", depstos.size());
        listDepstoDTO.forEach(x -> {
            depstos.add(depstoDTOToDepsto(x));
        });
        checkBusinessLogique(depstos.size() > 0, "article.NotFoundInDepsto");
        List<Integer> unityIDs = new ArrayList();
        List<Integer> depotIDs = new ArrayList();
        List<Integer> articleIDs = new ArrayList();
        depstos.forEach(elt -> {
            unityIDs.add(elt.getUnite());
            depotIDs.add(elt.getCoddep());
            articleIDs.add(elt.getCodart());
        });
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unityIDs.stream().distinct().collect(Collectors.toList()));
        List<DepotDTO> depots = paramAchatServiceClient.findDepotsByCodes(depotIDs.stream().distinct().collect(Collectors.toList()));
        List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(articleIDs.stream().distinct().collect(Collectors.toList()));
        List<DepstoEditionValeurStockDTO> detailedResult = depstos.stream().map(depsto -> {
            UniteDTO unite = unities.stream()
                    .filter(unit -> unit.getCode().equals(depsto.getUnite()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(unite != null, "stock.edition.missing-unity");

            DepotDTO depot = depots.stream()
                    .filter(dep -> dep.getCode().equals(depsto.getCoddep()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(depot != null, "stock.edition.missing-depot");

            ArticleDTO article = articles.stream()
                    .filter(art -> art.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(article != null, "stock.edition.missing-article");
            DepstoEditionValeurStockDTO depsoDTO = DepstoDTOAssembler.assembleDepstoEditionValeurStockDTO(depsto, article, unite, depot);
            return depsoDTO;
        }).collect(toList());
        reportClientDoc
                .getDatabaseController().setDataSource(detailedResult, DepstoEditionValeurStockDTO.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "CategorieArticleDesignation", CategorieArticleDesignation);
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }

    }

    public byte[] editionStockPrime(List<Integer> coddep, CategorieDepotEnum categ, String type, LocalDate date) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        QDepsto qDepsto = QDepsto.depsto;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(coddep, () -> qDepsto.coddep.in(coddep))
                .and(qDepsto.categDepot.eq(categ))
                .and(qDepsto.qte.gt(BigDecimal.ZERO))
                .and(qDepsto.datPer.before(date));
        List<Depsto> listDepsto = depstorepository.findAll(builder);
        checkBusinessLogique(listDepsto.size() > 0, "article.NotFoundInDepsto");
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/Etat Du Stock Perime" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/Etat Du Stock Perime_excel" + local + ".rpt", 0);
        }
        List<Integer> unityIDs = listDepsto.stream().map(Depsto::getUnite).distinct().collect(Collectors.toList());
        List<Integer> depotIDs = listDepsto.stream().map(Depsto::getCoddep).distinct().collect(Collectors.toList());
        List<Integer> articleIDs = listDepsto.stream().map(Depsto::getCodart).distinct().collect(Collectors.toList());
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unityIDs.stream().distinct().collect(Collectors.toList()));
        List<DepotDTO> depots = paramAchatServiceClient.findDepotsByCodes(depotIDs.stream().distinct().collect(Collectors.toList()));
        List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(articleIDs.stream().distinct().collect(Collectors.toList()));
        List<DepstoEditionValeurStockDTO> detailedResult = listDepsto.stream().map(depsto -> {
            UniteDTO unite = unities.stream()
                    .filter(unit -> unit.getCode().equals(depsto.getUnite()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(unite != null, "stock.edition.missing-unity");

            DepotDTO depot = depots.stream()
                    .filter(dep -> dep.getCode().equals(depsto.getCoddep()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(depot != null, "stock.edition.missing-depot");

            ArticleDTO article = articles.stream()
                    .filter(art -> art.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            log.debug("***************** articleDTO {} **********************", article);
            checkBusinessLogique(article != null, "stock.edition.missing-article");
            DepstoEditionValeurStockDTO depsoDTO = DepstoDTOAssembler.assembleDepstoEditionValeurStockDTO(depsto, article, unite, depot);
            return depsoDTO;
        }).collect(toList());
        log.debug("************** list depsto to edition {} *************", detailedResult);
        reportClientDoc
                .getDatabaseController().setDataSource(detailedResult, DepstoEditionValeurStockDTO.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "date", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    public byte[] editionStockProchainementPrime(List<Integer> coddep, CategorieDepotEnum categ, String type, LocalDate fromDate, LocalDate toDate) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        QDepsto qDepsto = QDepsto.depsto;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(coddep, () -> qDepsto.coddep.in(coddep))
                .and(qDepsto.categDepot.eq(categ))
                .and(qDepsto.qte.gt(BigDecimal.ZERO))
                .and(qDepsto.datPer.goe(fromDate))
                .and(qDepsto.datPer.loe(toDate));
        List<Depsto> listDepsto = depstorepository.findAll(builder);
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/Etat Du Stock Perime date per" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/Etat Du Stock Perime date per_excel" + local + ".rpt", 0);
        }

        checkBusinessLogique(listDepsto.size() > 0, "article.NotFoundInDepsto");
        List<Integer> unityIDs = listDepsto.stream().map(Depsto::getUnite).distinct().collect(Collectors.toList());
        List<Integer> depotIDs = listDepsto.stream().map(Depsto::getCoddep).distinct().collect(Collectors.toList());
        List<Integer> articleIDs = listDepsto.stream().map(Depsto::getCodart).distinct().collect(Collectors.toList());
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unityIDs);
        List<DepotDTO> depots = paramAchatServiceClient.findDepotsByCodes(depotIDs);
        List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(articleIDs);
        List<DepstoEditionValeurStockDTO> detailedResult = listDepsto.stream().map(depsto -> {
            UniteDTO unite = unities.stream()
                    .filter(unit -> unit.getCode().equals(depsto.getUnite()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(unite != null, "stock.edition.missing-unity");

            DepotDTO depot = depots.stream()
                    .filter(dep -> dep.getCode().equals(depsto.getCoddep()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(depot != null, "stock.edition.missing-depot");

            ArticleDTO article = articles.stream()
                    .filter(art -> art.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            log.debug("***************** articleDTO {} **********************", article);
            checkBusinessLogique(article != null, "stock.edition.missing-article");
            DepstoEditionValeurStockDTO depsoDTO = DepstoDTOAssembler.assembleDepstoEditionValeurStockDTO(depsto, article, unite, depot);
            return depsoDTO;
        }).collect(toList());
        log.debug("************** list depsto to edition {} *************", detailedResult);
        reportClientDoc
                .getDatabaseController().setDataSource(detailedResult, DepstoEditionValeurStockDTO.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "du", Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "au", Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    /**
     * Edition receiving by id.
     *
     * @param codArt
     * @param codfrs
     * @param fromDate
     * @param toDate
     * @param categ
     * @param type
     * @param groupby
     * @param codeCategorieArticle
     * @return
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Transactional(readOnly = true)
    public byte[] editionEtatAchat(Integer codArt, String codfrs, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ, String type, String groupby,
            Integer codeCategorieArticle) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print Fournisseurs : {}");
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        List<Integer> listeCodeArticle = null;
        List<ArticleDTO> articleDTOs = new ArrayList<>();
        if (codArt != null) {
            listeCodeArticle = java.util.Arrays.asList(codArt);
            articleDTOs = paramAchatServiceClient.articleFindbyListCode(listeCodeArticle);

        } else if (codeCategorieArticle != null) {
            articleDTOs = paramAchatServiceClient.articleByCategArt(codeCategorieArticle);
            listeCodeArticle = articleDTOs.stream().map(ArticleDTO::getCode).collect(Collectors.toList());
        }
        List<BonEditionDTO> bonEditionDTOs = factureBAService.editionEtatAchat(codfrs, fromDate, toDate, categ, listeCodeArticle);
        bonEditionDTOs.addAll(retourPerimeService.editionEtatAchat(codfrs, fromDate, toDate, categ, listeCodeArticle));
        bonEditionDTOs.addAll(avoirFournisseurService.editionEtatAchat(codfrs, fromDate, toDate, categ, listeCodeArticle));
        if (type.equalsIgnoreCase("P")) {
            if ("grpfrs".equals(groupby)) {
                reportClientDoc.open("Reports/Listes Des Achats Par Frs" + local + ".rpt", 0);
            } else {
                reportClientDoc.open("Reports/Listes Des Achats Par Article" + local + ".rpt", 0);
            }
        } else {
            if ("grpfrs".equals(groupby)) {
                reportClientDoc.open("Reports/Listes Des Achats Par Frs_excel" + local + ".rpt", 0);
            } else {
                reportClientDoc.open("Reports/Listes Des Achats Par Article_excel" + local + ".rpt", 0);
            }

        }
        List<Integer> codeArticles = listeCodeArticle;// to remove probleme variable in lambda expressions must be final or effectively final;
        Set<DetailEditionDTO> mvtstos = new HashSet<>();
        for (BonEditionDTO bonEditionDTO : bonEditionDTOs) {
            mvtstos.addAll(bonEditionDTO.getDetails());
        }

        if (codeArticles != null) {
            mvtstos = mvtstos.stream().filter(y -> codeArticles.contains(y.getRefArt())).collect(Collectors.toSet());
        }
        if (codeCategorieArticle == null) {
            listeCodeArticle = mvtstos.stream().map(DetailEditionDTO::getRefArt).collect(Collectors.toList());
            articleDTOs = paramAchatServiceClient.articleFindbyListCode(listeCodeArticle);
        }
        for (DetailEditionDTO mvtsto : mvtstos) {

            ArticleDTO articleDTO = articleDTOs
                    .stream()
                    .filter(y -> mvtsto.getRefArt().equals(y.getCode()))
                    .findFirst().orElse(null);
            checkBusinessLogique(!articleDTO.getDesignation().equals("Not Available"), "missing-article");
            mvtsto.setCodeCategorieArticle(articleDTO.getCategorieArticle().getCode());
            mvtsto.setDesignationCategorieArticle(articleDTO.getCategorieArticle().getDesignation());
        }

        Set<FournisseurDTO> fournisseurs = bonEditionDTOs.stream().map(item -> item.getFournisseur()).collect(Collectors.toSet());
        checkBusinessLogique(mvtstos.size() > 0, "achat.NotFound");
        reportClientDoc.getDatabaseController().setDataSource(mvtstos, DetailEditionDTO.class, "MvtSto", "MvtSto");
        reportClientDoc.getDatabaseController().setDataSource(bonEditionDTOs, BonEditionDTO.class, "Facture", "Facture");
        reportClientDoc.getDatabaseController().setDataSource(fournisseurs, FournisseurDTO.class, "Fournisseur", "Fournisseur");
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class, "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "du", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "au", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    @Transactional(readOnly = true)
    public Set<DetailEditionDTO> listeEtatAchat(Integer codArt, String codfrs, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categorieDepotEnum,
            Integer codeCategorieArticle) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to get liste etats achats Fournisseurs : {}");

        List<Integer> listeCodeArticles = null;
        List<ArticleDTO> articleDTOs = new ArrayList<>();
        if (codArt != null) {
            listeCodeArticles = java.util.Arrays.asList(codArt);
            articleDTOs = paramAchatServiceClient.articleFindbyListCode(listeCodeArticles);

        } else if (codeCategorieArticle != null) {
            articleDTOs = paramAchatServiceClient.articleByCategArt(codeCategorieArticle);
            listeCodeArticles = articleDTOs.stream().map(ArticleDTO::getCode).collect(Collectors.toList());
        }
        List<BonEditionDTO> bonEditionDTOs = factureBAService.editionEtatAchat(codfrs, fromDate, toDate, categorieDepotEnum, listeCodeArticles);
        bonEditionDTOs.addAll(retourPerimeService.editionEtatAchat(codfrs, fromDate, toDate, categorieDepotEnum, listeCodeArticles));
        bonEditionDTOs.addAll(avoirFournisseurService.editionEtatAchat(codfrs, fromDate, toDate, categorieDepotEnum, listeCodeArticles));
        checkBusinessLogique(bonEditionDTOs.size() > 0, "achat.NotFound");

        Set<DetailEditionDTO> resultedMvtstoDTOs = new HashSet<>();
        for (BonEditionDTO bonEditionDTO : bonEditionDTOs) {
            resultedMvtstoDTOs.addAll(bonEditionDTO.getDetails());
        }
        checkBusinessLogique(resultedMvtstoDTOs.size() > 0, "achat.NotFound");
        List<Integer> codeArticles = listeCodeArticles;// to remove probleme variable in lambda expressions must be final or effectively final;
        if (codeArticles != null) {
            resultedMvtstoDTOs = resultedMvtstoDTOs.stream().filter(y -> codeArticles.contains(y.getRefArt())).collect(Collectors.toSet());
        }
        if (codeCategorieArticle == null) {
            listeCodeArticles = resultedMvtstoDTOs.stream().map(DetailEditionDTO::getRefArt).collect(Collectors.toList());
            articleDTOs = paramAchatServiceClient.articleFindbyListCode(listeCodeArticles);
        }
        for (DetailEditionDTO mvtsto : resultedMvtstoDTOs) {

            ArticleDTO articleDTO = articleDTOs
                    .stream()
                    .filter(y -> mvtsto.getRefArt().equals(y.getCode()))
                    .findFirst().orElse(null);
            checkBusinessLogique(!articleDTO.getDesignation().equals("Not Available"), "missing-article");
            mvtsto.setCodeCategorieArticle(articleDTO.getCategorieArticle().getCode());
            mvtsto.setDesignationCategorieArticle(articleDTO.getCategorieArticle().getDesignation());
        }

        return resultedMvtstoDTOs;

    }

    public byte[] editionStockALaDate(List<Integer> coddep, CategorieDepotEnum categ, String type, LocalDate date, String glob, String detailsPrix, String datePer) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        QValeurStock _valeurStock = QValeurStock.valeurStock;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(coddep, () -> _valeurStock.coddep.in(coddep))
                .and(_valeurStock.categDepot.eq(categ))
                .and(_valeurStock.qte.gt(BigDecimal.ZERO))
                .and(_valeurStock.valeurStockPK().datesys.eq(date));
        List<ValeurStock> listValeurStock = (List<ValeurStock>) valeurStockRepository.findAll(builder);
        checkBusinessLogique(listValeurStock.size() > 0, "article.NotFoundInDepsto");
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;

        if (type.equalsIgnoreCase("P")) {
            if ("Det".equals(glob)) {
                if ("Dat".equals(datePer)) {
                    reportClientDoc.open("Reports/ValeurStkDetaileDatePer" + local + ".rpt", 0);
                } else {
                    if ("PU".equals(detailsPrix)) {
                        reportClientDoc.open("Reports/ValeurStkDetailePU" + local + ".rpt", 0);
                    } else {
                        reportClientDoc.open("Reports/ValeurStkDetailePMP" + local + ".rpt", 0);
                    }
                }
            } else {
                if ("PU".equals(detailsPrix)) {
                    reportClientDoc.open("Reports/ValeurStkGlobalPU" + local + ".rpt", 0);
                } else {
                    reportClientDoc.open("Reports/ValeurStkGlobalPMP" + local + ".rpt", 0);
                }
            }
        } else {
            if ("Det".equals(glob)) {
                if ("Dat".equals(datePer)) {
                    reportClientDoc.open("Reports/ValeurStkDetaileDatePer_excel" + local + ".rpt", 0);
                } else {
                    if ("PU".equals(detailsPrix)) {
                        reportClientDoc.open("Reports/ValeurStkDetailePU_excel" + local + ".rpt", 0);
                    } else {
                        reportClientDoc.open("Reports/ValeurStkDetailePMP_excel" + local + ".rpt", 0);
                    }
                }
            } else {
                if ("PU".equals(detailsPrix)) {
                    reportClientDoc.open("Reports/ValeurStkGlobalPU_excel" + local + ".rpt", 0);
                } else {
                    reportClientDoc.open("Reports/ValeurStkGlobalPMP_excel" + local + ".rpt", 0);
                }
            }
        }

        List<Integer> unityIDs = listValeurStock.stream().map(ValeurStock::getUnite).distinct().collect(Collectors.toList());
        List<Integer> depotIDs = listValeurStock.stream().map(ValeurStock::getCoddep).distinct().collect(Collectors.toList());
        List<Integer> articleIDs = listValeurStock.stream().map(ValeurStock::getCodart).distinct().collect(Collectors.toList());
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unityIDs.stream().distinct().collect(Collectors.toList()));
        List<DepotDTO> depots = paramAchatServiceClient.findDepotsByCodes(depotIDs.stream().distinct().collect(Collectors.toList()));
        List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(articleIDs.stream().distinct().collect(Collectors.toList()));
        List<ValeurStockDTO> detailedResult = listValeurStock.stream().map(depsto -> {
            UniteDTO unite = unities.stream()
                    .filter(unit -> unit.getCode().equals(depsto.getUnite()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(unite != null, "stock.edition.missing-unity");

            DepotDTO depot = depots.stream()
                    .filter(dep -> dep.getCode().equals(depsto.getCoddep()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(depot != null, "stock.edition.missing-depot");

            ArticleDTO article = articles.stream()
                    .filter(art -> art.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(article != null, "stock.edition.missing-article");
            ValeurStockDTO depsoDTO = ValeurStockAssembler.assembleDepstoEditionValeurStockDTO(depsto, article, unite, depot);
            return depsoDTO;
        }).collect(toList());
        reportClientDoc
                .getDatabaseController().setDataSource(detailedResult, ValeurStockDTO.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");

        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "date", Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    public List<CategorieArticleDTO> getCategorieArticleParent(List<CategorieArticleDTO> result,
            List<CategorieArticleDTO> listCategorieArticle, Integer lastParent, Integer firstChild, Integer curseurParent) {
        for (CategorieArticleDTO x : listCategorieArticle) {

            if (result.isEmpty() && x.getCode().equals(firstChild)) {
//                log.debug("firstChild: {}", firstChild);
//             log.debug("lastParent: {}", lastParent);
//             log.debug("x.getCode(): {}", x.getCode());
//             log.debug("curseurParent first: {}", x.getParent());
                result.add(new CategorieArticleDTO(x.getCode(), x.getDesignation()));
//                log.debug("result recurs first: {}", result.toString());
                getCategorieArticleParent(result, listCategorieArticle, lastParent, firstChild, x.getParent());
            } else if (!result.isEmpty() && x.getCode().equals(curseurParent) && !curseurParent.equals(lastParent)) {
//             log.debug("x.getCode() second: {}", x.getCode());
                result.add(new CategorieArticleDTO(x.getCode(), x.getDesignation()));
//                log.debug("result recurs second: {}", result.toString());
//             log.debug("curseurParent second: {}", x.getParent());
                getCategorieArticleParent(result, listCategorieArticle, lastParent, firstChild, x.getParent());
            } else if (!result.isEmpty() && curseurParent.equals(lastParent)) {
//             log.debug("x.getCode() last: {}", x.getCode());
//              log.debug("curseurParent last: {}", x.getParent());
                result.add(new CategorieArticleDTO(x.getCode(), x.getDesignation()));
//                log.debug("result recurs last: {}", result.toString());
                break;
            }
//        }
//        );

        }
        return result;
    }

    public byte[] editionEtatVentes(List<Integer> coddep, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ, Integer categorieArticle, String type, Boolean net, Boolean groupedBycodep)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        checkBusinessLogique(!categ.equals(CategorieDepotEnum.EC), "article-economat");
        String language = LocaleContextHolder.getLocale().getLanguage();

        List<ArticleDTO> articleDTOs = paramAchatServiceClient.articleByCategArt(categorieArticle);
        List<Integer> articleIds = articleDTOs.stream().map(ArticleDTO::getCode).collect(Collectors.toList());
        Preconditions.checkBusinessLogique(!articleIds.isEmpty(), "pharmacie.categ.item.empty");

        List<QtePrixMouvement> totalVentes = mvtstoService.findQuantitePrixMouvementByCodartIn(coddep, fromDate, toDate, categ, articleIds);
        if ((Boolean.FALSE).equals(net)) {
            totalVentes.addAll(avoirService.findQuantitePrixMouvementByCodartIn(coddep, fromDate, toDate, categ, articleIds));
        }
        checkBusinessLogique(totalVentes.size() > 0, "vente.NotFound");

        List<Integer> articleMouvementIds = totalVentes.stream().map(x -> x.getCodart()).collect(Collectors.toList());
        List<ArticleDTO> articleDTOsFinal = articleDTOs.stream().filter(x -> articleMouvementIds.contains(x.getCode())).collect(Collectors.toList());

        List<CategorieArticleDTO> listCategorieArticleDTO = paramAchatServiceClient.findNodesByCategorieArticleParent(categorieArticle);
        HashMap<Integer, List<CategorieArticleDTO>> listCategAticleGrouppedByChildCateg = new HashMap<>();
        articleDTOsFinal.forEach((ArticleDTO art)
                -> {
            if (!listCategAticleGrouppedByChildCateg.containsKey(art.getCategorieArticle().getCode())) {
                listCategAticleGrouppedByChildCateg.put(
                        art.getCategorieArticle().getCode(),
                        getCategorieArticleParent(new ArrayList<CategorieArticleDTO>(), listCategorieArticleDTO, categorieArticle, art.getCategorieArticle().getCode(), art.getCategorieArticle().getParent()));
            }
            String newLine = System.getProperty("line.separator");
            if (type.equalsIgnoreCase("P")) {
                totalVentes.forEach(x -> {
                    if (x.getCodart().equals(art.getCode())) {
                        x.setListCategorieArticle(
                                listCategAticleGrouppedByChildCateg.get(art.getCategorieArticle().getCode()).stream()
                                        .sorted(Comparator.comparing(CategorieArticleDTO::getCode))
                                        .map(c -> c.getDesignation()).collect(Collectors.joining(newLine)));
                    }
                });
            } else {
                totalVentes.forEach(x -> {
                    if (x.getCodart().equals(art.getCode())) {
                        x.setListCategorieArticle(
                                listCategAticleGrouppedByChildCateg.get(art.getCategorieArticle().getCode()).stream()
                                        .sorted(Comparator.comparing(CategorieArticleDTO::getCode))
                                        .map(c -> c.getDesignation()).collect(Collectors.joining(",")));
                    }
                });
            }
        }
        );

//        log.debug("totalVentes: {}", totalVentes.toString());
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        if (type.equalsIgnoreCase("P")) {
            if (!groupedBycodep) {
                reportClientDoc.open("Reports/Etat Des Ventes" + local + ".rpt", 0);
            } else {
                reportClientDoc.open("Reports/Etat Des Ventes grouped" + local + ".rpt", 0);
            }

        } else {
            if (!groupedBycodep) {
                reportClientDoc.open("Reports/Etat Des Ventes_excel" + local + ".rpt", 0);
            } else {
                reportClientDoc.open("Reports/Etat Des Ventes grouped_excel" + local + ".rpt", 0);
            }
        }

        List<Integer> unityIDs = new ArrayList();
        List<Integer> depotIDs = new ArrayList();
        totalVentes.forEach(elt -> {
            unityIDs.add(elt.getCodeunite());
            depotIDs.add(elt.getCoddep());
        });
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unityIDs.stream().distinct().collect(Collectors.toList()));
        List<DepotDTO> depots = paramAchatServiceClient.findDepotsByCodes(depotIDs.stream().distinct().collect(Collectors.toList()));
        totalVentes.stream().forEach(vente -> {
            UniteDTO unite = unities.stream()
                    .filter(unit -> unit.getCode().equals(vente.getCodeunite()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(unite != null, "stock.edition.missing-unity");
            vente.setDesignationunite(unite.getDesignation());

            DepotDTO depot = depots.stream()
                    .filter(unit -> unit.getCode().equals(vente.getCoddep()))
                    .findFirst()
                    .orElse(null);
            checkBusinessLogique(depot != null, "stock.edition.missing-depot");
            vente.setCodeSaisiDepot(depot.getCodeSaisi());
            vente.setDesignationDepot(depot.getDesignation());
        });
        log.debug("totalVentes {}", totalVentes);
        reportClientDoc
                .getDatabaseController().setDataSource(totalVentes, QtePrixMouvement.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> listDepot = totalVentes.stream().map(QtePrixMouvement::getDesignationDepot).distinct().collect(Collectors.toList());
        StringBuilder listeDepot = new StringBuilder("");
        listDepot.stream().forEach(item -> {
            listeDepot.append(item).append("، ");
        });
        String listDepots = listeDepot.toString();
        if (!groupedBycodep) {
            paramController.setCurrentValue("", "listeDepot", listDepots.substring(0, listDepots.lastIndexOf("،")));
        }
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "du", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "au", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    public byte[] editionConsomationReels(List<Integer> coddep, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ, String glob, String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
//        List<MouvementConsomation> totalConsomations = mvtstoService.findConsomationReel(categ, coddep, fromDate, toDate);
//        totalConsomations.addAll(mvtStoPRService.findConsomationReel(categ, coddep, fromDate, toDate));
        QConsomationReels _consomationReels = QConsomationReels.consomationReels;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_consomationReels.categDepot.eq(categ.categ()))
                .optionalAnd(coddep, () -> _consomationReels.coddep.in(coddep))
                .optionalAnd(fromDate, () -> _consomationReels.date.goe(fromDate))
                .optionalAnd(toDate, () -> _consomationReels.date.loe(toDate));
        List<ConsomationReels> list = (List<ConsomationReels>) consomationReelsRepository.findAll(builder);
        checkBusinessLogique(list.size() > 0, "article.NotFoundInDepsto");
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        if (type.equalsIgnoreCase("P")) {
            if (glob.equals("true")) {
                reportClientDoc.open("Reports/ConsomationReelle_glob" + local + ".rpt", 0);
            } else if (glob.equals("false")) {
                reportClientDoc.open("Reports/ConsomationReelle" + local + ".rpt", 0);
            } else {
                reportClientDoc.open("Reports/ConsomationReelleByNumBon" + local + ".rpt", 0);
            }

        } else {
            if (glob.equals("true")) {
                reportClientDoc.open("Reports/ConsomationReelle_glob_excel" + local + ".rpt", 0);
            } else if (glob.equals("false")) {
                reportClientDoc.open("Reports/ConsomationReelle_excel" + local + ".rpt", 0);
            } else {
                reportClientDoc.open("Reports/ConsomationReelleByNumBon_excel" + local + ".rpt", 0);
            }
        }

        reportClientDoc
                .getDatabaseController().setDataSource(consomationReelsFactory.toMouvement(list), MouvementConsomation.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "du", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "au", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    public List<TotalMouvement> consomationReels(List<Integer> codarts, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ) {
        if (categ.equals(CategorieDepotEnum.PH)) {
            List<ArticlePHDTO> articlesPH = paramAchatServiceClient.articlePHFindbyListCode(codarts);
            List<TotalMouvement> list = consomationReelsRepository.findQuantiteMouvement(codarts, fromDate, toDate);
            list = list.stream().map(mvtstoDTO -> {
                ArticlePHDTO matchedArticle = articlesPH.stream()
                        .filter(art -> art.getCode().equals(mvtstoDTO.getCodart())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));
                ArticleUniteDTO matchedUnite = matchedArticle.getArticleUnites().stream()
                        .filter(unity -> unity.getCodeUnite().equals(mvtstoDTO.getCodeUnite()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unity"));
                BigDecimal qteInPrincipaleUnit = mvtstoDTO.getQuantite().divide(matchedUnite.getNbPiece(), 0, RoundingMode.HALF_UP);
                mvtstoDTO.setQuantite(qteInPrincipaleUnit);
                mvtstoDTO.setCodeUnite(matchedArticle.getCodeUnite());
                return mvtstoDTO;
            }).collect(Collectors.groupingBy(item -> item.getCodart(),
                    Collectors.reducing(new TotalMouvement(BigDecimal.ZERO), (a, b) -> {
                        b.setQuantite(a.getQuantite().add(b.getQuantite()));
                        return b;
                    })))
                    .values().stream().collect(toList());
            return list;

        } else {
            return consomationReelsRepository.findQuantiteMouvement(codarts, fromDate, toDate);
        }
    }

    @Transactional(
            readOnly = true
    )
    public List<TotalMouvement> consomationReelss(List<Integer> codarts, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        QConsomationReels _consomationReels = QConsomationReels.consomationReels;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_consomationReels.categDepot.eq(categ.categ()))
                .and(_consomationReels.codart.in(codarts))
                .and(_consomationReels.date.goe(fromDate))
                .and(_consomationReels.date.loe(toDate));
        List<ConsomationReels> consomationReels = (List<ConsomationReels>) consomationReelsRepository.findAll(builder);
        if (categ.equals(CategorieDepotEnum.PH)) {
            List<ArticlePHDTO> articlesPH = paramAchatServiceClient.articlePHFindbyListCode(codarts);
            List<TotalMouvement> list = consomationReels.stream().map(consomationReel -> {
                TotalMouvement totalMouvement = new TotalMouvement(consomationReel.getCodart(), BigDecimal.ZERO);
                ArticlePHDTO matchedArticle = articlesPH.stream().filter(art -> art.getCode().equals(consomationReel.getCodart())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));
                ArticleUniteDTO matchedUnite = matchedArticle.getArticleUnites().stream()
                        .filter(unity -> unity.getCodeUnite().equals(consomationReel.getCodeUnite()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unity"));
                BigDecimal qteInPrincipaleUnit = consomationReel.getQuantite().divide(matchedUnite.getNbPiece(), 0, RoundingMode.UP);
                totalMouvement.setQuantite(qteInPrincipaleUnit);
                totalMouvement.setCodeUnite(matchedArticle.getCodeUnite());
                return totalMouvement;
            }).collect(Collectors.groupingBy(item -> item.getCodart(),
                    Collectors.reducing(new TotalMouvement(BigDecimal.ZERO), (a, b) -> {
                        b.setQuantite(a.getQuantite().add(b.getQuantite()));
                        return b;
                    })))
                    .values().stream().collect(toList());
            return list;
        } else {
            List<TotalMouvement> list = consomationReels.stream().map(consomationReel -> {
                TotalMouvement totalMouvement = new TotalMouvement(consomationReel.getCodart(), BigDecimal.ZERO);
                totalMouvement.setQuantite(consomationReel.getQuantite());
                totalMouvement.setCodeUnite(consomationReel.getCodeUnite());
                return totalMouvement;
            }).collect(Collectors.groupingBy(item -> item.getCodart(),
                    Collectors.reducing(new TotalMouvement(BigDecimal.ZERO), (a, b) -> {
                        b.setQuantite(a.getQuantite().add(b.getQuantite()));
                        return b;
                    })))
                    .values().stream().collect(toList());
            return list;
        }

    }

    public byte[] editionDetectionStock(CategorieDepotEnum categ, String type, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print detection Stock");

        String language = LocaleContextHolder.getLocale().getLanguage();
//         checkBusinessLogique(articles.size() > 0, "article.NotFoundInDepsto");
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        if (type.equalsIgnoreCase("E")) {
            reportClientDoc.open("Reports/EtatDetailleDetectionDuStock_excel" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/EtatDetailleDetectionDuStock" + local + ".rpt", 0);
            log.debug("REST request to print EtatDetailleDetectionDuStock");
        }
        reportClientDoc.getDatabaseController().setDataSource(findMouvementStock(categ, coddep, fromDate, toDate), BaseMouvementStock.class,
                "Commande", "Commande");
        if (!type.equalsIgnoreCase("E")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "du", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "au", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("E")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);

        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        }
    }

    @Transactional(readOnly = true)
    public List<BaseMouvementStock> findMouvementStock(CategorieDepotEnum categ, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {

        List<BaseMouvementStock> result = new ArrayList<>();
        QValeurStockGlobale _ValeurStockGlobale = QValeurStockGlobale.valeurStockGlobale;
        WhereClauseBuilder builderValeurStock = new WhereClauseBuilder()
                .and(_ValeurStockGlobale.coddep.eq(coddep))
                .and(_ValeurStockGlobale.categDepot.eq(categ))
                .and(_ValeurStockGlobale.datbon.eq(Date.from(fromDate.minusDays(1).atZone(ZoneId.systemDefault()).toInstant())));
        List<ValeurStockGlobale> valeursStockGlobales = (List<ValeurStockGlobale>) valeurStockGlobalekRepository.findAll(builderValeurStock);

        QMouvementStock _MouvementStock = QMouvementStock.mouvementStock;
        WhereClauseBuilder builderMvtStock = new WhereClauseBuilder().and(_MouvementStock.categDepot.eq(categ))
                .and(_MouvementStock.coddep.eq(coddep))
                .and(_MouvementStock.datbon.goe(Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant())))
                .and(_MouvementStock.datbon.loe(Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant())));
        List<MouvementStock> mouvementsStock = (List<MouvementStock>) mouvementStockRepository.findAll(builderMvtStock);

        result.addAll(mouvementsStock.stream()
                .filter(distinctByKey(MouvementStock::getCodart))
                .filter(mvtStk -> !valeursStockGlobales.stream().anyMatch(valeurStock -> valeurStock.getCodart().equals(mvtStk.getCodart())))
                .map(missingMvtStk -> {
                    BaseMouvementStock newValeurStk = (BaseMouvementStock) missingMvtStk.clone();
                    newValeurStk.setDesignationMvt("رصيد أول المدة");
                    newValeurStk.setNumaffiche("");
                    newValeurStk.setNumbon("");
                    newValeurStk.setDatbon(Date.from(fromDate.minusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
                    newValeurStk.setQuantite(BigDecimal.ZERO);
                    newValeurStk.setValeur(BigDecimal.ZERO);

                    return newValeurStk;
                })
                .collect(toList())
        );

        result.addAll(valeursStockGlobales);
        result.addAll(mouvementsStock);

        return result;

    }

    public byte[] editionEcartStock(CategorieDepotEnum categDep, Integer codeDepot, String type) throws ReportSDKException, SQLException, IOException {

        log.debug("REST request to print EcartStock ");
        List<DepstoEditionValeurStockDTO> listeEcartDepsto = stockService.findEcartStock(categDep, codeDepot);
        checkBusinessLogique(listeEcartDepsto.size() > 0, "No-data-to-print");
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/stockFixParDepot" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/stockFixParDepot_excel" + local + ".rpt", 0);
        }
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }

        reportClientDoc.getDatabaseController().setDataSource(listeEcartDepsto, DepstoEditionValeurStockDTO.class,
                "Commande", "Commande");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
//        paramController.setCurrentValue("", "categ", categDep);
        if (type.equalsIgnoreCase("P")) {

            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    public byte[] editionRotationStock(CategorieDepotEnum categ, Integer categorieArticle, LocalDateTime fromDate, LocalDateTime toDate, String type) throws ReportSDKException, SQLException, IOException {

        log.debug("REST request to print EcartStock ");
        List<RotationStockDTO> listeEcartDepsto = stockService.rotationStock(categ, categorieArticle, fromDate, toDate);
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/RotationStock" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/RotationStock_excel" + local + ".rpt", 0);
        }
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }

        reportClientDoc.getDatabaseController().setDataSource(listeEcartDepsto, RotationStockDTO.class,
                "Commande", "Commande");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "du", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "au", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    public byte[] editionFicheStock(CategorieDepotEnum categ, String type, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print Fiche Stock v2 : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();

        DepotDTO depot = paramAchatServiceClient.findDepotByCode(coddep);

        ArticleDTO article = paramAchatServiceClient.findArticlebyCategorieDepotAndCodeArticle(categ, codart);

        List<Mouvement> mouvements = ficheStockService.findListMouvement(categ, codart, coddep, fromDate, toDate, typeDate, depot);

        log.debug("mouvements avant traitement {}", mouvements.toString());
        //ajout valeur depart a partie du valeur stock
        List<ValeurStock> listValeurStock = (List<ValeurStock>) valeurStockRepository.findByCoddepAndCodartAndValeurStockPK_Datesys(coddep, codart, fromDate.toLocalDate().minusDays(1));
        log.debug("listValeurStock {}", listValeurStock.toString());
        List<Integer> codeUnites = listValeurStock.stream().map(item -> item.getUnite()).collect(Collectors.toList());
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);

//        listValeurStock = listValeurStock.stream()
//                .collect(Collectors.groupingBy(item -> item.getUnite(),
//                        Collectors.reducing(new ValeurStock(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
//                            b.setQte(a.getQte().add(b.getQte()));
//                            b.setValeur(a.getPu().multiply(a.getQte())
//                                    .multiply((a.getTauxTvaAchat().add(new BigDecimal(100))).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP))
//                                    .add(b.getPu().multiply(b.getQte())
//                                            .multiply((b.getTauxTvaAchat().add(new BigDecimal(100))).divide(new BigDecimal(100), 6, RoundingMode.HALF_UP))));
//                            return b;
//                        }))).values().stream().collect(toList());
        List<Mouvement> mouvementsSoldeDepart = new ArrayList<>();
        listValeurStock.forEach((mouvement) -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mouvement.getUnite())).findFirst().orElse(null);
            mouvementsSoldeDepart.add(valeurStockFactory.toMouvement(mouvement, depot, article, unite));
        });
        mouvements.addAll(mouvementsSoldeDepart);

        //fin ajout valeur depart a partie du valeur stock
        Collections.sort(mouvements, (p1, p2) -> p1.getDate().compareTo(p2.getDate()));

        log.debug("mouvements {}", mouvements.toString());

        List<Mouvement> mouvementsWithSolde = new ArrayList<>();
        Map<Integer, List<Mouvement>> mouvementGroupByUnite
                = mouvements.stream().collect(Collectors.groupingBy(x -> x.getCodeUnite()));
        for (Map.Entry<Integer, List<Mouvement>> entry1 : mouvementGroupByUnite.entrySet()) {
            List<Mouvement> valueMouvement = entry1.getValue();
            BigDecimal solde = BigDecimal.ZERO;
            for (Mouvement mouvement : valueMouvement) {
                solde = solde.add(mouvement.getEntree().subtract(mouvement.getSortie()));
                mouvement.setSolde(solde);
                mouvementsWithSolde.add(mouvement);
            }
        }

        Collections.sort(mouvementsWithSolde, (p1, p2) -> p1.getDate().compareTo(p2.getDate()));
        log.debug("mouvementsFinal {}", mouvementsWithSolde.toString());
        checkBusinessLogique(mouvements.size() > 0, "article.NotFoundInDepsto");

        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
            if (type.equalsIgnoreCase("P")) {
                log.debug("Reports/Fiche De Stock_V2_ar.rpt");
                reportClientDoc.open("Reports/Fiche De Stock_ar.rpt", 0);
            } else {
                reportClientDoc.open("Reports/Fiche De Stock_excel_ar.rpt", 0);
            }
        } else {
            if (type.equalsIgnoreCase("P")) {
                log.debug("Fiche De StockSelonDate_ar");
                reportClientDoc.open("Reports/Fiche De StockSelonDate_ar.rpt", 0);
            } else {
                reportClientDoc.open("Reports/Fiche De StockSelonDate_excel_ar.rpt", 0);
            }
        }
        reportClientDoc
                .getDatabaseController().setDataSource(mouvementsWithSolde, Mouvement.class,
                        "FicheStockArticle", "FicheStockArticle");

        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
            if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
                paramController.setCurrentValue("", "title", messages.getMessage("pharmacie.edition.byDateDetail", null, LocaleContextHolder.getLocale()));
            } else if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO)) {
                paramController.setCurrentValue("", "title", messages.getMessage("pharmacie.edition.byDateMvtsto", null, LocaleContextHolder.getLocale()));
            } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
                paramController.setCurrentValue("", "title", messages.getMessage("pharmacie.edition.sansDatePer", null, LocaleContextHolder.getLocale()));
            }
        }
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "du", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "au", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    public byte[] editionMouvementStockGrouppedByDepot(CategorieDepotEnum categ, String type, List<Integer> codeDepot, LocalDateTime fromDate, LocalDateTime toDate) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print detection Stock");

        List<MouvementStockEditionDTO> mouvementStockGrouppedByDepot = mouvementStockRepository.findMouvementStockGrouppedByDepot(categ, codeDepot, java.util.Date.from(fromDate.atZone(ZoneId.systemDefault())
                .toInstant()), java.util.Date.from(toDate.atZone(ZoneId.systemDefault())
                        .toInstant()));

        List<MouvementStockEditionDTO> valeurStockGrouppedByDepot = valeurStockRepository.findValeurStockGrouppedByDepot(categ, codeDepot, fromDate.toLocalDate().minusDays(1));

        List<DepotDTO> depotDTOs = paramAchatServiceClient.findDepotsByCodes(codeDepot);

        mouvementStockGrouppedByDepot.addAll(valeurStockGrouppedByDepot);

        List<MouvementStockEditionDTO> mouvementStockGrouppedByDepotFinal = MouvementStockFactory.buildMouvementStockEditionDTOs(mouvementStockGrouppedByDepot, depotDTOs);
        log.debug("rotationStockList : {}", mouvementStockGrouppedByDepotFinal.toString());

        String language = LocaleContextHolder.getLocale().getLanguage();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        if (type.equalsIgnoreCase("E")) {
            reportClientDoc.open("Reports/EtatMouvementStockGrouppedByDepot_excel_ar.rpt", 0);
        } else {
            reportClientDoc.open("Reports/EtatMouvementStockGrouppedByDepot_ar.rpt", 0);
            log.debug("REST request to print EtatDetailleDetectionDuStock");
        }
        reportClientDoc.getDatabaseController().setDataSource(mouvementStockGrouppedByDepotFinal, MouvementStockEditionDTO.class,
                "Commande", "Commande");
        if (!type.equalsIgnoreCase("E")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "du", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "au", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        if (contextReception.contains("company")) {
            paramController.setCurrentValue("", "incoming", "ارجاع تحويل بعد الاستلام");
            paramController.setCurrentValue("", "expenses", "تحويل بعد الاستلام");
        } else {
            paramController.setCurrentValue("", "incoming", "تحويل بعد الاستلام");
            paramController.setCurrentValue("", "expenses", "ارجاع تحويل بعد الاستلام");
        }
        if (type.equalsIgnoreCase("E")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);

        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        }
    }

    public byte[] editionPriceVariance(LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categDepot, Integer categorieArticle, String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        List<PriceVarianceDTO> result = mvtStoBAService.calculPriceVariance(fromDate, toDate, categDepot, categorieArticle);
        String language = LocaleContextHolder.getLocale().getLanguage();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/EtatPriceVariance" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/EtatPriceVariance_excel" + local + ".rpt", 0);

        }

        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        reportClientDoc.getDatabaseController().setDataSource(result, PriceVarianceDTO.class,
                "priceVariance", "priceVariance");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();

        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "du", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "au", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }

    }

    @Transactional(readOnly = true)
    public byte[] editionListeBonReceptionAvecTVa(LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categDepot, Boolean orderDateBon, String type, Boolean avoir, List<Boolean> annule)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {

        List<ReceptionEditionDTO> resultDTO = factureBAService.findListeBonReceptionAvecTVa(fromDate, toDate, categDepot, orderDateBon, avoir, annule);

        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/EtatBonReceptionAvecTva" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/EtatBonReceptionAvecTva_excel" + local + ".rpt", 0);
        }

        reportClientDoc.getDatabaseController().setDataSource(resultDTO, ReceptionEditionDTO.class,
                "BonReception", "BonReception");
        List<String> codesFrs = resultDTO.stream().map(item -> item.getCodeFournisseu()).distinct().collect(Collectors.toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
        reportClientDoc.getDatabaseController().setDataSource(fournisseurs, FournisseurDTO.class,
                "fournisseur", "fournisseur");
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "debut", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "fin", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "order", orderDateBon);
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    @Transactional(readOnly = true)
    public byte[] editionBonReceiving(CategorieDepotEnum categ, LocalDateTime fromDate,
            LocalDateTime toDate, String type, Boolean deleted, Boolean valid) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        List<ReceivingDTO> resultDTO = receivingService.findValidatedReceiving(categ, fromDate, toDate, deleted, valid);

        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/EtatBonReceiving" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/EtatBonReceiving_excel" + local + ".rpt", 0);
        }

        reportClientDoc.getDatabaseController().setDataSource(resultDTO, ReceivingDTO.class,
                "receiving", "receiving");
        List<ReceivingDetailsDTO> receivingDetailsDTO = new ArrayList<>();

        resultDTO.stream().map(x -> x.getReceivingDetailsList()).forEach(
                (detailsDTO) -> {

                    receivingDetailsDTO.addAll(detailsDTO.stream().distinct().collect(Collectors.toList()));
//                    for (ReceivingDetailsDTO detailDto : detailsDTO) {
//                        receivingDetailsDTO.add(detailDto);
//                    }
                }
        );
        reportClientDoc.getDatabaseController().setDataSource(receivingDetailsDTO, ReceivingDetailsDTO.class,
                "receivingDetailsList", "receivingDetailsList");
//         receivingDetailsDTO=resultDTO.get(3).getReceivingDetailsList();
        // reportClientDoc.getDatabaseController().setDataSource(receivingDetailsDTO, ReceivingDetailsDTO.class, "receiving_details", "receiving_details");
//        List<String> codesFrs = resultDTO.stream().map(item -> item.getCodeFournisseu()).distinct().collect(Collectors.toList());
//        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
//        reportClientDoc.getDatabaseController().setDataSource(fournisseurs, FournisseurDTO.class, "fournisseur", "fournisseur");
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "debut", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "fin", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
//        paramController.setCurrentValue("", "order", orderDateBon);
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    @Transactional(readOnly = true)
    public byte[] editionBonReception(CategorieDepotEnum categ, LocalDateTime fromDate,
            LocalDateTime toDate, String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        FactureBE queriedFacBE = new FactureBE();
//        queriedFacBE.setCoddep(codeDep);
        queriedFacBE.setCategDepot(categ);
        List<FactureBEDTO> resultDTO = factureBEService.findAll(queriedFacBE, fromDate, toDate, true);
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/listeBonRedressement" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/listeBonRedressement_excel" + local + ".rpt", 0);
        }
        List<MvtStoBEDTO> receivingDetailsDTO = new ArrayList<>();
        Set<Integer> codesUnite = new HashSet<>();

        resultDTO.stream().map(x -> x.getDetails()).forEach(
                (detailsDTO) -> {
                    log.debug("list= {}", detailsDTO);
                    for (MvtStoBEDTO mvtStoBEDTO : detailsDTO) {
                        receivingDetailsDTO.add(mvtStoBEDTO);
                        codesUnite.add(mvtStoBEDTO.getCodeUnite());
                    }
                }
        );
        List<UniteDTO> unites = paramAchatServiceClient.findUnitsByCodes(codesUnite);

        reportClientDoc.getDatabaseController().setDataSource(resultDTO, FactureBEDTO.class,
                "redressement", "redressement");
        reportClientDoc.getDatabaseController().setDataSource(unites, UniteDTO.class,
                "unites", "unites");
        reportClientDoc.getDatabaseController().setDataSource(receivingDetailsDTO, MvtStoBEDTO.class,
                "redressementDetailsList", "redressementDetailsList");
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "debut", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "fin", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }

    @Transactional(readOnly = true)
    public byte[] editionArticlesGratuit(CategorieDepotEnum categ, LocalDateTime fromDate,
            LocalDateTime toDate, String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/freeItems" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/freeItems_excel" + local + ".rpt", 0);
        }

        List<MvtstoBADTO> listArticleGratuit = mvtStoBAService.findArticleGratuit(categ, fromDate, toDate);
        List<FactureBA> listeFactureBA = new ArrayList();
//                listArticleGratuit.stream().map(item -> item.getFactureBA()).distinct().collect(Collectors.toList());
        reportClientDoc.getDatabaseController().setDataSource(listeFactureBA, FactureBA.class,
                "FactureBA", "FactureBA");

        List<String> codesFrs = new ArrayList();
//                listArticleGratuit.stream().map(item -> item.getFactureBA().getCodfrs()).distinct().collect(Collectors.toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
        reportClientDoc.getDatabaseController().setDataSource(fournisseurs, FournisseurDTO.class,
                "Fournisseur", "Fournisseur");

        reportClientDoc.getDatabaseController().setDataSource(listArticleGratuit, MvtstoBADTO.class,
                "Commande", "Commande");

        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "debut", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "fin", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        if (type.equalsIgnoreCase("P")) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }
    }
}
