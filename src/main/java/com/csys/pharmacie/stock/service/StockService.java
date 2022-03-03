package com.csys.pharmacie.stock.service;

import com.csys.pharmacie.stock.factory.DepstoDTOAssembler;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import com.csys.pharmacie.achat.domain.DetailMvtStoAF;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.DetailMvtStoBA;
import com.csys.pharmacie.achat.domain.DetailMvtStoBAPK;
import com.csys.pharmacie.achat.domain.DetailTransfertCompanyBranch;
import com.csys.pharmacie.achat.domain.MvtstoAF;
import com.csys.pharmacie.achat.domain.TraceDetailTransfertCompanyBranch;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticleIMMODTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.achat.dto.ArticleUuDTO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csys.util.RestPreconditions;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.stock.dto.DepstoDTO;

import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.repository.DetailMvtStoBARepository;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.client.dto.ArticleDepotFixeDTO;

import com.csys.pharmacie.client.dto.ArticleNonMvtDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import static com.csys.pharmacie.helper.CategorieDepotEnum.EC;
import static com.csys.pharmacie.helper.CategorieDepotEnum.PH;
import static com.csys.pharmacie.helper.CategorieDepotEnum.UU;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.helper.DetailMvtSto;
import static com.csys.pharmacie.helper.MethodeTraitementdeStockEnum.FEFO;
import com.csys.pharmacie.helper.MvtSto;
import com.csys.pharmacie.helper.QteMouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.prelevement.domain.MvtStoPR;
import com.csys.pharmacie.stock.domain.QDepsto;
import com.csys.pharmacie.stock.dto.ArticleStockProjection;
import com.csys.pharmacie.stock.dto.DepstoEditionValeurStockDTO;
import com.csys.pharmacie.stock.dto.IsArticleQteAvailable;
import com.csys.pharmacie.stock.dto.RotationStockDTO;
import com.csys.pharmacie.stock.repository.ArticleInDepProjection;
import com.csys.pharmacie.stock.repository.DepstoRepository;

import com.csys.pharmacie.vente.dto.PMPArticleDTO;
import com.csys.pharmacie.vente.service.PricingService;
import com.csys.pharmacie.transfert.domain.DetailMvtStoBE;
import com.csys.pharmacie.transfert.domain.MvtStoBE;

import com.csys.util.Preconditions;
import com.mysema.commons.lang.Pair;
import java.io.IOException;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.util.stream.StreamSupport;
import javax.validation.constraints.NotNull;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.multipart.MultipartFile;

@Service("StockService")
public class StockService {
    
    private final Logger log = LoggerFactory.getLogger(StockService.class);
    private static String differencePermiseBonRetour;
    static String INTERVAL;
    
    @Value("${transfert-perime.config.expiration-date-interval}")
    public void setINTERVAL(String db) {
        INTERVAL = db;
    }
    
    public String getINTERVAL() {
        return INTERVAL;
    }
    
    @Value("${difference-permise-bon-retour}")
    public void setDifference(String differencePermise) {
        differencePermiseBonRetour = differencePermise;
    }
    
    @Value("${apply-marginal-for-medication-items}")
    private Boolean applyMarginalForMedicationItems;

//    @Autowired
//    private DepotRepository depotrepository;
    @Autowired
    private DepstoRepository depstorepository;
    
    @Autowired
    private ParamService paramService;
    
    @Autowired
    private ParamAchatServiceClient paramAchatServiceClient;
    
    @Autowired
    ParamServiceClient parametrageService;
    
    @Autowired
    @Lazy
    PricingService pricingService;
    @Autowired
    DetailMvtStoBARepository detailMvtStoBARepository;
    
    @Autowired
    @Lazy
    EditionService editionService;
    
    @Autowired
    MessageSource messages;

//    @Value("${lang.secondary}")
//    private static String LANGUAGE_SEC;
    public List<Depsto> findByCodartInAndCoddepAndQteGreaterThan(List<Integer> codArticles, Integer coddep) {
        List<Depsto> lists = new ArrayList<>();
        if (codArticles != null && codArticles.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codArticles.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = codArticles.subList(i * 2000, Math.min(i * 2000 + 2000, codArticles.size()));
                completableFuture[i] = depstorepository.findByCodartInAndCoddepAndQteGreaterThan(codesChunk, coddep, BigDecimal.ZERO).whenComplete((list, exception) -> {
                    lists.addAll(list);
                });
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }
    
    public List<Depsto> findByCodartInAndCoddepAndQteGreaterThanAndDatPerGreaterThan(List<Integer> codArticles, Integer coddep) {
        return depstorepository.findByCodartInAndCoddepAndQteGreaterThanAndDatPerGreaterThan(codArticles, coddep, BigDecimal.ZERO, LocalDate.now());
    }
    
    public List<Depsto> findByCodartInAndQteGreaterThan(Collection<Integer> codArticles, BigDecimal gt) {
        List<Depsto> lists = new ArrayList<>();
        List<Integer> codes = new ArrayList<>();
        codes.addAll(codArticles);
        if (codes != null && codes.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codes.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = codes.subList(i * 2000, Math.min(i * 2000 + 2000, codes.size()));
                Collection<Integer> codesChunkCollection = new ArrayList<>();
                codesChunkCollection.addAll(codesChunk);
                completableFuture[i] = depstorepository.findByCodartInAndQteGreaterThan(codesChunkCollection, gt).whenComplete((list, exception) -> {
                    lists.addAll(list);
                });
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }
    
    public String getDesDepotByCodDep(Integer codDep) {
        DepotDTO dep = paramAchatServiceClient.findDepotByCode(codDep);
        RestPreconditions.checkFound(dep);
        return (String) dep.getDesdep();
    }
    
    public List<Depsto> findDepstoByNumBon(String numBon) {
        
        List<Depsto> dep = depstorepository.findByNumBon(numBon);
        
        return dep;
    }

//    @Transactional
//    public void updateQteDepstoToZero(String numBon) {
//        depstorepository.updateQteDepsto(BigDecimal.ZERO, numBon);
//    }
    @Transactional
    public boolean saveDepsto(List<Depsto> l) {
//        log.debug("saving list depsto {}", l);
        depstorepository.save(l);
        return true;
    }
    
    @Transactional
    public boolean saveDepsto(Depsto l) {
        log.debug("saving list depsto {}", l);
        depstorepository.save(l);
        return true;
    }

//    public List<ArticleInDep> getArtsInDep(String coddep) {
//        List<ArticleInDep> list = depstorepository.getArtsInDep(coddep);
//        return (list);
//    }
//    @Deprecated
//    public String checkArtNotUsed(String numBon) {
//        List<MvtstoBADTO> mvtBA = mvtstoBAService.getDetailsReception(numBon);
//        List<Depsto> dep = findDepstoByNumBon(numBon);
//        String used = "false";
//        for (Depsto d : dep) {
//            for (MvtstoBADTO m : mvtBA) {
//                if (m.getRefArt().equals(d.getCodart())) {
//                    if (d.getQte().compareTo(m.getQuantite()) != 0) {
//                        used = "true";
//                    }
//                    break;
//                }
//            }
//            if (used.equalsIgnoreCase("true")) {
//                break;
//            }
//        }
//        return used;
//
//    }
//    @Deprecated
//    public Integer findDepDefByTypeBon(String typBon) {
//        String coddep;
//        if (typBon.equalsIgnoreCase("BT")) {
//            coddep = paramService.findValeurOptionVersionPharmacieByID("CodDepDefBT");
//        } else if (typBon.equalsIgnoreCase("BR") || typBon.equalsIgnoreCase("BE")) {
//            coddep = paramService.findValeurOptionVersionPharmacieByID("CodDepDefBRBE");
//        } else if (typBon.equalsIgnoreCase("AA")) {
//            coddep = paramService.findValeurOptionVersionPharmacieByID("CodDepDefAA");
//        } else {
//            coddep = paramService.findValeurOptionVersionPharmacieByID("CodDepDefCA");
//        }
//        return findFirstByCoddep(Integer.valueOf(coddep)).getCode();
//    }
//    public boolean checkArtExistInDep(Integer codArt, Integer codDep, BigDecimal qte) {
//        return (findQteArtInDep(codArt, codDep).compareTo(qte) >= 0);
//    }
//    public BigDecimal findQteArtInDep(Integer codArt, Integer codDep) {
//        return depstorepository.findQteArtInDep(codArt, codDep);
//    }
//    public boolean checkArtExistInDepFrs(Integer codArt, String codDep, String id, String lotFrs, BigDecimal qte) {
//        return (findQteArtInDepFrs(codArt, codDep, id, lotFrs).compareTo(qte) >= 0);
//    }
//    public BigDecimal findQteArtInDepFrs(Integer codArt, String codDep, String id, String lotFrs) {
//        return depstorepository.findQteArtInDepFrs(codArt, codDep, id, lotFrs);
//    }
//    public DepotDTO findFirstByCoddep(Integer codDep) {
//        return paramAchatServiceClient.findDepotByCode(codDep);
//    }
    @Deprecated //has a rest (to verify uses from android or inventory web)
    public List<ArticleInDepProjection> findArticlesInDep(Integer codDep) {
        return depstorepository.findArtInDep(codDep);
    }

//    public List<ArticleInDepProjection> findAllArticlesInDep(Integer codDep) {
//        return depstorepository.findAllArtInDep(codDep);
//    }
//    public List<ArticleInDepProjection> findQuantiteOfArticlesInDep(String codDep, List<String> listCodArt) {
//
//        return depstorepository.findQuantiteOfArtInDep(codDep, listCodArt);
//    }
//    public List<LotInterDatPerQteProjection> findAllLotsOfArticleInDep(Integer codArt, String codDep) {
//
//        return depstorepository.findByCodartAndCoddepAndQteGreaterThan(codArt, codDep, BigDecimal.ZERO);
//    }
//    public List<ArticleInDepProjection> findQuantiteOfArticlesInDepV1(Integer codDep, List<Integer> listCodArt) {
//
//        return depstorepository.findQuantiteOfArtInDepV1(codDep, listCodArt);
//    }
    public Integer findStkDepByCodartAndCoddep(Integer codArt, String codDep) {
        return depstorepository.findStkDepByCodartAndCoddep(codArt, codDep);
    }
    
    public List<QteMouvement> findQuantiteMouvement(String famart, String coddep) {
        if (famart.equalsIgnoreCase("tous")) {
            return depstorepository.findQuantiteMouvementTous(coddep);
        } else {
            return depstorepository.findQuantiteMouvement(famart, coddep);
        }
    }

//    public List<ArticleInDepProjection> findQteArtsInAllDep(List<Integer> codArts) {
//        return depstorepository.findQteArtsInAllDep(codArts);
//    }
//    public BigDecimal findFirstPu(Integer codart, String coddep, String identifiant, String lotFrs, Date datPer) {
//        return depstorepository.findFirstByCodartAndCoddepAndIdentifiantAndLotFrsAndDatPerOrderByNumDesc(codart, coddep, identifiant, lotFrs, datPer).getPu();
//    }
//    public List<ParametrageDepot> findAllDepot(boolean stup) {
//        return depotrepository.findAllDepot(stup);
//
//    }
//    @Transactional
//    public Boolean addDepot(ParametrageDepot dto) {
//
//        Boolean exist = depotrepository.exists(dto.getCode());
//        Preconditions.checkBusinessLogique(!exist, "Le code de dépôt existe déjà");
//
//        Depot depot = new Depot();
//        depot.setCoddep(dto.getCode());
//        depot.setDesdep(dto.getDesignation());
//        depot.setValoriser(dto.getPhblanche());
//        depot.setCodFrs(dto.getCodfrs());
//        String methodeTraitementdeStock = paramService.findValeurOptionVersionPharmacieByID("Meth_Trait_Stock_Def");
//        depot.setMethodeTraitementdeStock(methodeTraitementdeStock);
//        depot.setOuvrenouvex(false);
//        depot.setUserOuv("");
//        depot.setVldCge1('0');
//        depot.setVldCge2('0');
//        depot.setStup(dto.getStup());
//        String PrmDepotOrtho = paramService.findValeurOptionVersionPharmacieByID("PrmDepotOrtho");
//        if (PrmDepotOrtho.equalsIgnoreCase("O")) {
//            depot.setOrthopedie(dto.getOrthopedie());
//        }
//
//        if (dto.getCodfrs() != null) {
//            depot.setDepotFrs(true);
//            depot.setCodFrs(dto.getCodfrs());
//
//            PrmBloc prmbloc = new PrmBloc();
//            prmbloc.setSalle(dto.getCode());
//            prmbloc.setMp(dto.getCode());
//            prmbloc.setLibelle(dto.getDesignation());
//            prmbloc.setBloc(dto.getSalle());
//            prmbloc.setSalleBloc("");
//            prmbloc.setFe1("F" + dto.getCode());
//            prmbloc.setFe2("00001");
//            prmbloc.setEx1("X" + dto.getCode());
//            prmbloc.setEx2("00001");
//            prmbloc.setPn1("P" + dto.getCode());
//            prmbloc.setPn2("00001");
//            prmbloc.setAutomatique(false);
//
//            prmBlocRepository.save(prmbloc);
//
//        } else {
//            depot.setCodFrs("");
//
//        }
//
//        depotrepository.save(depot);
//        return true;
//    }
    public BigDecimal findPrixAchatByCodart(Integer codart) {
        
        BigDecimal prix = depstorepository.findPrixAchatByCodart(codart);
        Preconditions.checkBusinessLogique(prix != null, "article inexistant dans le stock");
        return prix;
    }
    
    public BigDecimal findMaxPrixAchatByCodart(Integer codart) {
        
        BigDecimal prix = depstorepository.findPrixAchatByCodart(codart);
        return prix;
    }
    
    public BigDecimal findQteArt(Integer Codart) {
        return depstorepository.findQteArt(Codart);
    }
    
    public Integer findNombreArticlePerime(Boolean stup) {
        Date date = new Date();
        return depstorepository.findNombreArticlePerime(date);
    }
    
    public Integer findNombreArticleProchPerime(Boolean stup) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 30);
        return depstorepository.findNombreArticleProchPerime(c.getTime(), new Date());
    }
    
    public Integer findNombreArticleDepotFrsProchPerime() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String interv = paramService.findValeurOptionVersionPharmacieByID("Interv_Alerte_Art_DF");
        
        c.add(Calendar.DATE, Integer.parseInt(interv));
        return depstorepository.findNombreArticleDepotFrsProchPerime(c.getTime(), new Date());
        
    }

    /**
     * Find list of depstos with code of article in the given liste and with
     * quantity greater than zero.
     *
     * @param codArts
     * @return
     */
    @Transactional(readOnly = true)
    public List<Depsto> findByCodartInAndQteGreaterThanZero(Collection<Integer> codArts) {
        log.debug("find List Depstos By codart and qte>0");
        List<Depsto> lists = new ArrayList<>();
        List<Integer> codes = new ArrayList<>();
        codes.addAll(codArts);
        if (codes != null && codes.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codes.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = codes.subList(i * 2000, Math.min(i * 2000 + 2000, codes.size()));
                Collection<Integer> codesChunkCollection = new ArrayList<>();
                codesChunkCollection.addAll(codesChunk);
                completableFuture[i] = depstorepository.findByCodartInAndQteGreaterThan(codesChunkCollection, BigDecimal.ZERO).whenComplete((list, exception) -> {
                    lists.addAll(list);
                });
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
        
    }

    /**
     * Find list of depstos with code of article in the given liste and with
     * quantity greater than zero and with numbon diffrent than the giveng
     *
     * @param numBon.
     *
     * @param codeArts
     * @return list of depstos
     */
    public List<Depsto> findByCodeArticlesAndNumBonNot(List<Integer> codeArts, String numBon) {
        return depstorepository.findByCodartInAndNumBonNot(codeArts, numBon);
    }
    
    public Boolean articleExistsWithQuantity(Integer articleID) {
        return depstorepository.existsByCodartAndQteGreaterThan(articleID, BigDecimal.ZERO);
        
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private List<Depsto> groupDepstosByAritcleAndUnitAndCoddep(List<Depsto> depstos) {
        List<Depsto> simplifiedResult = new ArrayList();
        if (!depstos.isEmpty()) {
            depstos.stream()
                    .collect(groupingBy(Depsto::getCoddep, (groupingBy(Depsto::getCodart,
                            groupingBy(Depsto::getUnite,
                                    Collectors.reducing(new Depsto(BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
                                        b.setQte(b.getQte().add(a.getQte()));
                                        if (b.getStkrel() != null) {
                                            b.setStkrel(a.getStkrel().add(b.getStkrel()));
                                        } else {
                                            b.setStkrel(a.getStkrel());
                                        }
                                        b.setLotInter(null);
                                        b.setDatPer(null);
                                        return b;
                                    }))
                    ))))
                    .forEach((k, v) -> {
                        Collection<Map<Integer, Depsto>> secondLevelMap = v.values();
                        simplifiedResult.addAll(secondLevelMap.stream().flatMap(elt -> elt.values().stream()).collect(toList()));
                    });
        }
        return simplifiedResult;
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    private List<Depsto> groupDepstosByCoddepCodartUnitLotDatPer(List<Depsto> depstos) {
        
        List<Depsto> simplifiedResult = new ArrayList();
        
        depstos.stream()
                .collect(groupingBy(Depsto::getCoddep, (groupingBy(Depsto::getCodart,
                        groupingBy(Depsto::getUnite, groupingBy(Depsto::getDatPer, (groupingBy(Depsto::getLotInter,
                                Collectors.reducing(new Depsto(BigDecimal.ZERO, BigDecimal.ZERO), (a, b) -> {
                                    b.setQte(b.getQte().add(a.getQte()));
                                    if (b.getStkrel() != null) {
                                        b.setStkrel(a.getStkrel().add(b.getStkrel()));
                                    } else {
                                        b.setStkrel(a.getStkrel());
                                    }
                                    return b;
                                }))))
                        )
                ))))
                .forEach((k, v) -> {
                    Collection<Map<Integer, Map<LocalDate, Map<String, Depsto>>>> secondLevelMap = v.values();
                    Collection<  Map<LocalDate, Map<String, Depsto>>> thirdLevelMap = secondLevelMap.stream().flatMap(elt -> elt.values().stream()).collect(toList());
                    Collection<   Map<String, Depsto>> forthLevelMap = thirdLevelMap.stream().flatMap(elt -> elt.values().stream()).collect(toList());
                    simplifiedResult.addAll(forthLevelMap.stream().flatMap(elt -> elt.values().stream()).collect(toList()));
                });
        
        return simplifiedResult;
    }

    /**
     * Return qte of each article in the given depot or all the depots
     *
     * @param articleIds
     * @param coddep
     * @param detailed
     * @param useMinimumData if true, all informations relevant to the article
     * will not be provided ( designation, codeSaisi, unitDesignation, etc).
     * Only ids and quantity will be returned.
     * @param onlyMyArticles
     * @param pharmacieExterne
     * @return
     */
    public List<DepstoDTO> findQuantiteOfArticles(List<Integer> articleIds, Integer coddep, boolean detailed, boolean useMinimumData, Boolean onlyMyArticles, Boolean pharmacieExterne) {
        if (onlyMyArticles != null && Boolean.TRUE.equals(onlyMyArticles)) {
            List<ArticleDTO> articleDTOs = paramAchatServiceClient.articleFindbyListCode(articleIds, onlyMyArticles);
            articleIds = articleDTOs.stream().map(art -> art.getCode()).collect(Collectors.toList());
        }
        if (useMinimumData) {
            List<Depsto> result = new ArrayList();
            Integer numberOfChunks = (int) Math.ceil((double) articleIds.size() / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> articleIDChunk = articleIds.subList(i * 2000, Math.min(i * 2000 + 2000, articleIds.size()));
                result.addAll(findAll(coddep, articleIDChunk, false, null, detailed, null));
            }
            return DepstoDTOAssembler.assembleList(result, null);
        } else {
            List<DepstoDTO> result = new ArrayList();
            Integer numberOfChunks = (int) Math.ceil((double) articleIds.size() / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> articleIDChunk = articleIds.subList(i * 2000, Math.min(i * 2000 + 2000, articleIds.size()));
                
                result.addAll(findAll(coddep, null, articleIDChunk, false, detailed, false, pharmacieExterne));
                
            }
            
            List<DepotDTO> depots = paramAchatServiceClient.findDepotsByCodes(result.stream().map(item -> item.getCodeDepot()).collect(toSet()));
            
            result.stream().forEach(item -> {
                DepotDTO matcheDepot = depots.stream().filter(depot -> depot.getCode().equals(item.getCodeDepot())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-depot-" + item.getCodeDepot()));
                item.setDepotDesignation(matcheDepot.getDesignation());
            });
            return result;
        }
        
    }

    /**
     *
     * @param coddep
     * @param categ_depot
     * @param articleIDs the size of this list must be < 2100
     * @param includeNullQty
     * @param detailed. If False it will return depstos grouped by codart and
     * unit.
     * @param size Page size. If null it ill return all the records that matches
     * the passed criterias.
     * @return
     */
    public List<Depsto> findAll(Integer coddep, List<Integer> articleIDs, boolean includeNullQty, Integer size, boolean detailed, List<CategorieDepotEnum> categ_depot) {
        List<Depsto> result = new ArrayList();
        if (articleIDs != null && !articleIDs.isEmpty()) {
            Integer numberOfChunks = (int) Math.ceil((double) articleIDs.size() / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> articleIDChunk = articleIDs.subList(i * 2000, Math.min(i * 2000 + 2000, articleIDs.size()));
                QDepsto qDepsto = QDepsto.depsto;
                WhereClauseBuilder builder = new WhereClauseBuilder()
                        .and(qDepsto.codart.in(articleIDChunk))
                        .optionalAnd(coddep, () -> qDepsto.coddep.eq(coddep))
                        .booleanAnd(categ_depot != null && categ_depot.size() > 0, () -> qDepsto.categDepot.in(categ_depot))
                        .booleanAnd(!includeNullQty, () -> qDepsto.qte.gt(BigDecimal.ZERO));
                if (size != null) {
                    log.debug("sizeeeeeeeee");
                    result.addAll((List<Depsto>) depstorepository.findAllGrouped(builder, new PageRequest(0, size)));
                } else {
                    result.addAll((List<Depsto>) depstorepository.findAll(builder));
                }
            }
        } else {
            QDepsto qDepsto = QDepsto.depsto;
            WhereClauseBuilder builder = new WhereClauseBuilder()
                    .optionalAnd(coddep, () -> qDepsto.coddep.eq(coddep))
                    .booleanAnd(categ_depot != null && categ_depot.size() > 0, () -> qDepsto.categDepot.in(categ_depot))
                    .booleanAnd(!includeNullQty, () -> qDepsto.qte.gt(BigDecimal.ZERO));
            log.debug("sizeeeeeeeee2");
            result = size != null ? (List<Depsto>) depstorepository.findAllGrouped(builder, new PageRequest(0, size)) : (List<Depsto>) depstorepository.findAll(builder);
        }
        
        if (!detailed) {
            return groupDepstosByAritcleAndUnitAndCoddep(result);
        } else {
            return result;
        }
        
    }

    /**
     *
     * @param coddep not required
     * @param articleIDs required or return empty list
     * @param categDepot
     * @return sum(qte) plus sum(qte0) of depstos groupped by codeArticle and
     * codeUnite
     */
    public List<ArticleStockProjection> findAllGrouppedByByCodeArticleAndCodeUnite(CategorieDepotEnum categDepot, Integer coddep, List<Integer> articleIDs) {
        List<Depsto> result = new ArrayList();
        if (articleIDs != null && !articleIDs.isEmpty()) {
            Integer numberOfChunks = (int) Math.ceil((double) articleIDs.size() / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> articleIDChunk = articleIDs.subList(i * 2000, Math.min(i * 2000 + 2000, articleIDs.size()));
                QDepsto qDepsto = QDepsto.depsto;
                WhereClauseBuilder builder = new WhereClauseBuilder()
                        .booleanAnd(categDepot != null, () -> qDepsto.categDepot.eq(categDepot))
                        .optionalAnd(coddep, () -> qDepsto.coddep.eq(coddep))
                        .and(qDepsto.codart.in(articleIDChunk))
                        .and(((qDepsto.aInventorier.isNull().or(qDepsto.aInventorier.isFalse())).and(qDepsto.qte.gt(BigDecimal.ZERO)))
                                .or(qDepsto.aInventorier.isTrue().and(qDepsto.qte0.gt(BigDecimal.ZERO))));
                
                result.addAll((List<Depsto>) depstorepository.findAllGroupedByCodartAndUnite(builder));
            }
        }
        log.debug("despsto result : {}", result.toString());
        return DepstoDTOAssembler.desposToArticleStockProjectionList(result);
    }

    /**
     * Find articles stored in the diffrent warehouses that are expired
     *
     * @param coddep depot id
     * @param categDepots
     * @param includeNullQty
     * @param detailed If False it will return depstos grouped by codart and
     * unit.
     * @param withSellingPrice
     * @return liste of articles that been used in the given depot
     */
    public List<DepstoDTO> findAllPerime(Integer coddep, List<CategorieDepotEnum> categDepots, boolean includeNullQty, boolean detailed, boolean withSellingPrice, Boolean nonMoved) {
        QDepsto qDepsto = QDepsto.depsto;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                // .and((qDepsto.datPer.loe(LocalDate.now().minusDays(1))))
                .and(qDepsto.datPer.loe(LocalDate.now().plusDays(Long.parseLong(INTERVAL))))
                .optionalAnd(coddep, () -> qDepsto.coddep.eq(coddep))
                .booleanAnd(categDepots != null && categDepots.size() > 0, () -> qDepsto.categDepot.in(categDepots))
                .booleanAnd(!includeNullQty, () -> qDepsto.qte.gt(BigDecimal.ZERO));
        List<Depsto> result = (List<Depsto>) depstorepository.findAll(builder);
        log.debug("first liste from builder:{}", result);
        List<Depsto> depstos = new ArrayList<>();
        if (!detailed) {
            depstos = groupDepstosByAritcleAndUnitAndCoddep(result);
        } else {
            depstos = result;
        }
        Set<Integer> articlesPHIDs = new HashSet();
        Set<Integer> articlesUUIDs = new HashSet();
        Set<Integer> articlesECIDs = new HashSet();
        
        depstos.forEach(elt -> {
            switch (elt.getCategDepot()) {
                case PH:
                    articlesPHIDs.add(elt.getCodart());
                    break;
                case UU:
                    articlesUUIDs.add(elt.getCodart());
                    break;
                case EC:
                    articlesECIDs.add(elt.getCodart());
                    break;
            }
        });
        List<ArticlePHDTO> articlePHs = paramAchatServiceClient.articlePHFindbyListCode(articlesPHIDs);
        List<DepstoDTO> detailedResult = DepstoDTOAssembler.assembleListPH(depstos, articlePHs);
        if (withSellingPrice) {
            List<ArticleUuDTO> articleUUs = paramAchatServiceClient.articleUUFindbyListCode(articlesUUIDs, coddep, true, false);
            detailedResult.addAll(DepstoDTOAssembler.assembleListUU(depstos, articleUUs));
            
            List<PMPArticleDTO> pmps = pricingService.findPMPsByArticleIn(articlesECIDs.toArray(new Integer[articlesECIDs.size()]));
            List<ArticleDTO> articleECs = paramAchatServiceClient.articleFindbyListCode(articlesECIDs);
            detailedResult.addAll(DepstoDTOAssembler.assembleListEC(depstos, articleECs, pmps));
        } else {
            Set<Integer> NonPHArtIDs = new HashSet(articlesUUIDs);
            NonPHArtIDs.addAll(articlesECIDs);
            List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(NonPHArtIDs);
            detailedResult.addAll(DepstoDTOAssembler.assembleList(depstos.stream().filter(depsto -> !depsto.getCategDepot().equals(CategorieDepotEnum.PH)).collect(toList()), articles));
        }
        if (nonMoved) {
            
            List<ArticleNonMvtDTO> articlesNonMvt = paramAchatServiceClient.articleNonMvtByNonMoved(nonMoved, categDepots.get(0).categ(), Boolean.TRUE);
            List<Integer> codArts = articlesNonMvt.stream().map(ArticleNonMvtDTO::getCodart).collect(Collectors.toList());
            List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(codArts);
            List<Depsto> articlesNonMvtInDepsto = new ArrayList<>();
            if (coddep != null) {
                articlesNonMvtInDepsto = findByCodartInAndCoddepAndQteGreaterThan(codArts, coddep, new BigDecimal(0));
            } else {
                articlesNonMvtInDepsto = findByCodartInAndQteGreaterThan(codArts, new BigDecimal(0));
            }
            
            List<DepstoDTO> listDepstoArtNonMvt = DepstoDTOAssembler.assembleList(articlesNonMvtInDepsto, articles);
            List<Integer> codeArtDetailedResult = detailedResult.stream().map(DepstoDTO::getCode).collect(Collectors.toList());
            List<DepstoDTO> listeDepstoNonMvtFinal = listDepstoArtNonMvt.stream()
                    .filter(el -> !codeArtDetailedResult.contains(el.getCode()))
                    .map(el -> {
                        el.setNonMoved(Boolean.TRUE);
                        return el;
                    }).collect(Collectors.toList());
            List<Integer> codeUnites = listeDepstoNonMvtFinal.stream().map(DepstoDTO::getUnityCode).collect(Collectors.toList());
            List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
            listeDepstoNonMvtFinal.forEach(item -> {
                UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getUnityCode())).findFirst().orElse(null);
                Preconditions.checkBusinessLogique(unite != null, "missing-unity");
                item.setUnityDesignation(unite.getDesignation());
            });
            detailedResult.addAll(listeDepstoNonMvtFinal);
        }
        return detailedResult;
        
    }

    /**
     * Find Top 100 articles that are stored in the diffrent warehouses
     *
     * @param coddep depot id
     * @param categDepots
     * @param includeNullQty
     * @param detailed If False it will return depstos grouped by codart and
     * unit.
     * @param withSellingPrice
     * @param pharmacieExterne
     * @return liste of articles that been used in the given depot
     */
    public List<DepstoDTO> findTop100(Integer coddep, List<CategorieDepotEnum> categDepots, boolean includeNullQty, boolean detailed, boolean withSellingPrice, Boolean pharmacieExterne) {
        log.debug("find Top 100 Depstos");
        List<Depsto> depstos = findAll(coddep, null, includeNullQty, 100, detailed, categDepots);
        if (detailed) {
            depstos = groupDepstosByCoddepCodartUnitLotDatPer(depstos);
        }
        
        Set<Integer> articlesPHIDs = new HashSet();
        Set<Integer> articlesUUIDs = new HashSet();
        Set<Integer> articlesECIDs = new HashSet();
        Set<Integer> articleIMMOIDs = new HashSet();
        depstos.forEach(elt -> {
            
            switch (elt.getCategDepot()) {
                case PH:
                    articlesPHIDs.add(elt.getCodart());
                    break;
                case UU:
                    articlesUUIDs.add(elt.getCodart());
                    break;
                case EC:
                    articlesECIDs.add(elt.getCodart());
                    break;
                case IMMO:
                    articleIMMOIDs.add(elt.getCodart());
            }
        });
        List<ArticlePHDTO> articlePHs = paramAchatServiceClient.articlePHFindbyListCode(articlesPHIDs);
        
        List<DepstoDTO> detailedResult = new ArrayList();
        Boolean applyMarginal = (!Boolean.TRUE.equals(pharmacieExterne)) && (Boolean.TRUE.equals(applyMarginalForMedicationItems));
        log.debug("apply marginal est {}", applyMarginal);
        List<DepstoDTO> listeDepstoDTOPH = applyMarginal ? DepstoDTOAssembler.assembleListPHhenApplyMarginal(depstos, articlePHs) : DepstoDTOAssembler.assembleListPH(depstos, articlePHs);
        detailedResult.addAll(listeDepstoDTOPH);
        
        if (withSellingPrice) {
            List<ArticleUuDTO> articleUUs = paramAchatServiceClient.articleUUFindbyListCode(articlesUUIDs, coddep, true, false);
            detailedResult.addAll(DepstoDTOAssembler.assembleListUU(depstos, articleUUs));
            
            List<PMPArticleDTO> pmps = pricingService.findPMPsByArticleIn(articlesECIDs.toArray(new Integer[articlesECIDs.size()]));
            List<ArticleDTO> articleECs = paramAchatServiceClient.articleFindbyListCode(articlesECIDs);
            detailedResult.addAll(DepstoDTOAssembler.assembleListEC(depstos, articleECs, pmps));
        } else {
            Set<Integer> nonPHArtIDs = new HashSet(articlesUUIDs);
            nonPHArtIDs.addAll(articlesECIDs);
            nonPHArtIDs.addAll(articleIMMOIDs);
            List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(nonPHArtIDs);
            detailedResult.addAll(DepstoDTOAssembler.assembleList(depstos.stream().filter(depsto -> !depsto.getCategDepot().equals(CategorieDepotEnum.PH)).collect(toList()), articles));
        }
        
        return detailedResult;
        
    }

    /**
     * Find list of articles that are stored in the diffrent warehouses.
     *
     * @param coddep depot id
     * @param categDepots categorie of depot
     * @param q query criteria ( codesaisi, designation, categorieArticle)
     * @param includeNullQty
     * @param detailed If False it will return depstos grouped by codart and
     * unit.
     * @param withSellingPrice
     * @param onlyMyArticles In case of non medical items, passing this value as
     * true will return only he items that belongs to the current user
     * @param pharmacieExterne
     * @return liste of articles
     */
    public List<DepstoDTO> findAll(Integer coddep, @NotNull List<CategorieDepotEnum> categDepots, String q, boolean includeNullQty, boolean detailed, boolean withSellingPrice, boolean onlyMyArticles, Boolean pharmacieExterne) {
        log.debug("find All depstos");
        List<Integer> articleIDs = new ArrayList();
        List<ArticlePHDTO> articlePHs = new ArrayList();
        List<ArticleUuDTO> articleUUs = new ArrayList();
        List<ArticleDTO> articleECs = new ArrayList();
        List<ArticleIMMODTO> articleIMMOs = new ArrayList();
        
        for (CategorieDepotEnum categorie : categDepots) {
            switch (categorie) {
                case PH:
                    articlePHs = paramAchatServiceClient.articlePHFindAll(q, onlyMyArticles);
                    articleIDs.addAll(articlePHs.stream().map(ArticlePHDTO::getCode).collect(toList()));
                    break;
                case UU:
                    articleUUs = paramAchatServiceClient.articleUUFindAll(q, withSellingPrice, onlyMyArticles, coddep);
                    articleIDs.addAll(articleUUs.stream().map(ArticleDTO::getCode).collect(toList()));
                    break;
                case EC:
                    articleECs = paramAchatServiceClient.articleECFindAll(q, onlyMyArticles);
                    articleIDs.addAll(articleECs.stream().map(ArticleDTO::getCode).collect(toList()));
                    break;
                case IMMO:
                    articleIMMOs = paramAchatServiceClient.articleIMMOFindAll(q);
                    articleIDs.addAll(articleIMMOs.stream().map(ArticleDTO::getCode).collect(toList()));
            }
        }
        if (articleIDs.isEmpty()) {
            return new ArrayList();
        }
        
        List<Depsto> depstos = findAll(coddep, articleIDs, includeNullQty, null, detailed, categDepots);
        
        if (detailed) {
            depstos = groupDepstosByCoddepCodartUnitLotDatPer(depstos);
        }
        
        Boolean applyMarginal = (!Boolean.TRUE.equals(pharmacieExterne)) && (Boolean.TRUE.equals(applyMarginalForMedicationItems));
        log.debug("apply marginal est {}", applyMarginal);
        List<DepstoDTO> detailedResult = applyMarginal ? DepstoDTOAssembler.assembleListPHhenApplyMarginal(depstos, articlePHs) : DepstoDTOAssembler.assembleListPH(depstos, articlePHs);
        
        detailedResult.addAll(DepstoDTOAssembler.assembleListUU(depstos, articleUUs));
        if (withSellingPrice) {
            List<PMPArticleDTO> pmps = pricingService.findPMPsByArticleIn(articleECs.stream().map(ArticleDTO::getCode).toArray(Integer[]::new));
            detailedResult.addAll(DepstoDTOAssembler.assembleListEC(depstos, articleECs, pmps));
        } else {
            detailedResult.addAll(DepstoDTOAssembler.assembleListEC(depstos, articleECs));
        }
        detailedResult.addAll(DepstoDTOAssembler.assembleListIMMO(depstos, articleIMMOs));
        
        return detailedResult;
        
    }

    /**
     * Find list of distinct articles that are stored in the diffrent warehouses
     *
     * @param coddep depot id
     * @param categDepots categorie of depot
     * @param articleIDs the size of this list must be < 2100
     * @param includeNullQty
     * @param detailed If False it will return depstos grouped by codart and
     * unit.
     * @param withSellingPrice
     * @param pharmacieExterne
     * @return liste of articles
     */
    public List<DepstoDTO> findAll(Integer coddep, CategorieDepotEnum categDepots, List<Integer> articleIDs, boolean includeNullQty, boolean detailed, boolean withSellingPrice, Boolean pharmacieExterne) {
        log.debug("find stock assembled with articles categDepots est {},categdepot est equal IMMO ?{} ", categDepots, CategorieDepotEnum.IMMO.equals(categDepots));
        List<Depsto> depstos;
        if (categDepots != null) {
            depstos = findAll(coddep, articleIDs, includeNullQty, null, detailed, Collections.singletonList(categDepots));
        } else {
            depstos = findAll(coddep, articleIDs, includeNullQty, null, detailed, null);
        }
        
        Set<Integer> articlesPHIDs = new HashSet();
        Set<Integer> articlesUUIDs = new HashSet();
        Set<Integer> articlesECIDs = new HashSet();
        Set<Integer> articlesIMMOIDs = new HashSet();
        List<Depsto> listeDepstoUUAndEC = new ArrayList();
        depstos.forEach(elt -> {
            
            switch (elt.getCategDepot()) {
                case PH:
                    articlesPHIDs.add(elt.getCodart());
                    break;
                case UU:
                    articlesUUIDs.add(elt.getCodart());
                    listeDepstoUUAndEC.add(elt);
                    break;
                case EC:
                    articlesECIDs.add(elt.getCodart());
                    listeDepstoUUAndEC.add(elt);
                    break;
                case IMMO:
                    articlesIMMOIDs.add(elt.getCodart());
                
            }
        });

//        if (CategorieDepotEnum.IMMO.equals(categDepots)) {
        log.debug("article immo to search sont {}", articlesIMMOIDs);
        List<ArticleIMMODTO> articleIMMOs = paramAchatServiceClient.articleIMMOFindbyListCode(articlesIMMOIDs);
        log.debug("liste articles immo : {}", articleIMMOs.toString());
        List<DepstoDTO> detailedResult = DepstoDTOAssembler.assembleListIMMO(depstos, articleIMMOs);
//        } else {
        List<ArticlePHDTO> articlePHs = paramAchatServiceClient.articlePHFindbyListCode(articlesPHIDs);
        
        Boolean applyMarginal = (!Boolean.TRUE.equals(pharmacieExterne)) && (Boolean.TRUE.equals(applyMarginalForMedicationItems));
        log.debug("apply marginal est {}", applyMarginal);
        List<DepstoDTO> listeDepstoDTOPH = applyMarginal ? DepstoDTOAssembler.assembleListPHhenApplyMarginal(depstos, articlePHs) : DepstoDTOAssembler.assembleListPH(depstos, articlePHs);
        detailedResult.addAll(listeDepstoDTOPH);
        if (withSellingPrice) {
            List<ArticleUuDTO> articleUUs = paramAchatServiceClient.articleUUFindbyListCode(articlesUUIDs, coddep, true, false);
            detailedResult.addAll(DepstoDTOAssembler.assembleListUU(depstos, articleUUs));
            
            List<PMPArticleDTO> pmps = pricingService.findPMPsByArticleIn(articlesECIDs.toArray(new Integer[articlesECIDs.size()]));
            List<ArticleDTO> articleECs = paramAchatServiceClient.articleFindbyListCode(articlesECIDs);
            detailedResult.addAll(DepstoDTOAssembler.assembleListEC(depstos, articleECs, pmps));
        } else {
            Set<Integer> nonPHArtIDs = new HashSet(articlesUUIDs);
            nonPHArtIDs.addAll(articlesECIDs);
            List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(nonPHArtIDs);
            detailedResult.addAll(DepstoDTOAssembler.assembleList(listeDepstoUUAndEC, articles));
        }
//        }
        return detailedResult;
        
    }
    
    public List<DepstoDTO> findAllFr(Integer coddep, CategorieDepotEnum categDepots, List<Integer> articleIDs, boolean includeNullQty, boolean detailed, boolean withSellingPrice) {
        log.debug("find stock assembled with articles with language FR");
        List<Depsto> depstos;
        if (categDepots != null) {
            depstos = findAll(coddep, articleIDs, includeNullQty, null, detailed, Collections.singletonList(categDepots));
        } else {
            depstos = findAll(coddep, articleIDs, includeNullQty, null, detailed, null);
        }
        log.debug("find stock assembled with articles with language FR : depstos : {}", depstos.toString());
        Set<Integer> articlesPHIDs = new HashSet();
        Set<Integer> articlesUUIDs = new HashSet();
        Set<Integer> articlesECIDs = new HashSet();
        Set<Integer> articlesIMMOIDs = new HashSet();
        
        depstos.forEach(elt -> {
            switch (elt.getCategDepot()) {
                case PH:
                    articlesPHIDs.add(elt.getCodart());
                    break;
                case UU:
                    articlesUUIDs.add(elt.getCodart());
                    break;
                case EC:
                    articlesECIDs.add(elt.getCodart());
                    break;
                case IMMO:
                    articlesIMMOIDs.add(elt.getCodart());
                    break;
            }
        });
        
        List<DepstoDTO> detailedResult = new ArrayList<>();
        if (CategorieDepotEnum.IMMO.equals(categDepots)) {
            List<ArticleIMMODTO> articleIMMOs = paramAchatServiceClient.articleIMMOFindbyListCode(articlesIMMOIDs);
            log.debug("liste articles immo : {}", articleIMMOs.toString());
            detailedResult = DepstoDTOAssembler.assembleListIMMO(depstos, articleIMMOs);
        } else {
            List<ArticlePHDTO> articlePHs = paramAchatServiceClient.articlePHFindbyListCodeFr(articlesPHIDs);
            detailedResult = DepstoDTOAssembler.assembleListPH(depstos, articlePHs);
            if (withSellingPrice) {
                List<ArticleUuDTO> articleUUs = paramAchatServiceClient.articleUUFindbyListCode(articlesUUIDs, coddep, true, false);
                detailedResult.addAll(DepstoDTOAssembler.assembleListUU(depstos, articleUUs));
                
                List<PMPArticleDTO> pmps = pricingService.findPMPsByArticleIn(articlesECIDs.toArray(new Integer[articlesECIDs.size()]));
                List<ArticleDTO> articleECs = paramAchatServiceClient.articleFindbyListCodeWithLanguageFR(articlesECIDs);
                detailedResult.addAll(DepstoDTOAssembler.assembleListEC(depstos, articleECs, pmps));
            } else {
                Set<Integer> nonPHArtIDs = new HashSet(articlesUUIDs);
                nonPHArtIDs.addAll(articlesECIDs);
                List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCodeWithLanguageFR(nonPHArtIDs);
                detailedResult.addAll(DepstoDTOAssembler.assembleList(depstos.stream().filter(depsto -> !depsto.getCategDepot().equals(CategorieDepotEnum.PH)).collect(toList()), articles));
            }
        }
        
        return detailedResult;
        
    }
    
    public List<Depsto> findall() {
        
        return depstorepository.findAll();
        
    }
    
    @Transactional(readOnly = true)
    public DepstoDTO findByQrCodeAndUnity(String QrCode, Integer codDep, Boolean pharmacieExterne) {
        Example<Depsto> expl = QRCodeToDepstoExample(QrCode);
        
        expl.getProbe().setCoddep(codDep);
        List<Depsto> depstos = findByExample(expl);
        expl.getProbe().setQte(BigDecimal.ZERO);
        if (!depstos.isEmpty()) {
            expl.getProbe().setCategDepot(depstos.get(0).getCategDepot());
        }
        Depsto depsto = depstos.stream().reduce(expl.getProbe(), (a, b) -> {
            BigDecimal newQte = b.getQte() != null ? a.getQte().add(b.getQte()) : b.getQte(); // This line is Because the expl.getProbe(), which is the identity in the reduce, has a null qte so we must start with the qte of the second element   

            b.setQte(newQte);
            return b;
        });
        if (depsto.getQte() == null) { // in case findByExample dosen't return any match then we have to set the quantity to ZERO
            depsto.setQte(BigDecimal.ZERO);
        }
        
        List<Depsto> listSimilarDepstos = depstorepository.findByCodartAndUniteAndCoddepAndQteGreaterThan(expl.getProbe().getCodart(), expl.getProbe().getUnite(), codDep, BigDecimal.ZERO);
        Depsto oldestDepsto = null;
        String messageAlerte = "";
        if (!listSimilarDepstos.isEmpty()) {
            
            List<Depsto> listSimilarDepstosSorted = listSimilarDepstos
                    .stream()
                    .sorted((a, b) -> a.getDatPer().compareTo(b.getDatPer()))
                    .collect(Collectors.toList());
            oldestDepsto = listSimilarDepstosSorted.get(0);
            log.debug("oldestDepsto est {}", oldestDepsto);
            Object[] obj = new Object[1];
            obj[0] = oldestDepsto.getDatPer().toString();
            messageAlerte = messages.getMessage("there-is-an-older-depsto", obj, LocaleContextHolder.getLocale());
        }
        
        ArticleDTO article = paramAchatServiceClient.articleFindbyListCode(Collections.singletonList(depsto.getCodart())).get(0);
        Preconditions.checkBusinessLogique(article.getCategorieDepot() != null, "bad-QR");
        DepstoDTO result;
        
        switch (article.getCategorieDepot()) {
            case PH:
                ArticlePHDTO articlePH = paramAchatServiceClient.articlePHFindbyListCode(Arrays.asList(expl.getProbe().getCodart())).get(0);
                Boolean applyMarginal = (!Boolean.TRUE.equals(pharmacieExterne)) && (Boolean.TRUE.equals(applyMarginalForMedicationItems));
                log.debug("apply marginal est {}", applyMarginal);
                result = applyMarginal ? DepstoDTOAssembler.assemblePHhenApplyMarginal(depsto, articlePH) : DepstoDTOAssembler.assemblePH(depsto, articlePH);
                
                if (oldestDepsto != null && oldestDepsto.getDatPer().isBefore(depsto.getDatPer())) {
                    result.setMessageAlerteDatPer(messageAlerte);
                }
                return result;
            case UU:
                ArticleUuDTO articleUU = paramAchatServiceClient.articleUUFindbyListCode(Arrays.asList(expl.getProbe().getCodart()), codDep, true, false).get(0);
                result = DepstoDTOAssembler.assemble(depsto, articleUU);
                if (oldestDepsto != null && oldestDepsto.getDatPer().isBefore(depsto.getDatPer())) {
                    result.setMessageAlerteDatPer(messageAlerte);
                }
                return result;
            case EC:
                BigDecimal pmp = pricingService.findPMPByArticle(expl.getProbe().getCodart()).getPMP();
                ArticleDTO articleEC = paramAchatServiceClient.articleFindbyListCode(Arrays.asList(expl.getProbe().getCodart())).get(0);
                articleEC.setPrixVente(pmp);
                result = DepstoDTOAssembler.assemble(depsto, articleEC);
                if (oldestDepsto != null && oldestDepsto.getDatPer().isBefore(depsto.getDatPer())) {
                    result.setMessageAlerteDatPer(messageAlerte);
                }
                return result;
            default:
                throw new IllegalBusinessLogiqueException("bad-QR");
            
        }
    }
    
    private Example<Depsto> QRCodeToDepstoExample(String QRCode) {
        try {
            
            log.debug("the qr code is {}", QRCode);
            String[] decodedQR = QRCode.split(";");
            Integer codArt = Integer.parseInt(decodedQR[0]);
            LocalDate preemption = LocalDate.parse(decodedQR[1], DateTimeFormatter.BASIC_ISO_DATE);
            String lot = decodedQR[2];
            Integer unitID = Integer.parseInt(decodedQR[3]);
            Depsto d = new Depsto();
            d.setCodart(codArt);
            d.setLotInter(lot);
            d.setDatPer(preemption);
            d.setUnite(unitID);
            return Example.of(d);
        } catch (NumberFormatException | DateTimeParseException ex) {
            if (ex instanceof DateTimeParseException) {
                log.debug(ex.getMessage());
                throw new IllegalBusinessLogiqueException("bad-QR.bad-date");
            } else {
                throw new IllegalBusinessLogiqueException("bad-QR.bad-integer");
            }
        }
    }
    
    @Transactional(readOnly = true)
    public List<Depsto> findByExample(Example<Depsto> expl) {
        return depstorepository.findAll(expl);
    }
    
    public List<IsArticleQteAvailable> areDemandedDepstosAvailable(Collection<DepstoDTO> demandedArticles, Integer coddep) {
        
        if (demandedArticles.size() > 0) {
            List<Integer> articleIds = demandedArticles.stream().map(DepstoDTO::getArticleID).distinct().collect(toList());
            List<ArticleStockProjection> depstos = new ArrayList();
            Integer numberOfChunks = (int) Math.ceil((double) articleIds.size() / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> articleIDChunk = articleIds.subList(i * 2000, Math.min(i * 2000 + 2000, articleIds.size()));
                depstos.addAll(findByCodeDepotAndCodeArticleInGrouppedByCodeArticleAndCodeUnite(coddep, articleIDChunk));
            }
            
            List<IsArticleQteAvailable> result = depstos.stream().map(depsto -> {
                
                DepstoDTO demandedArt = demandedArticles.stream()
                        .filter(demande -> depsto.getCodart().equals(demande.getArticleID())).findFirst().get();
                
                IsArticleQteAvailable isArticleQteAvailable = new IsArticleQteAvailable();
                isArticleQteAvailable.setArticleId(depsto.getCodart());
                isArticleQteAvailable.setUniteId(depsto.getUnite());
                isArticleQteAvailable.setDemandedQte(demandedArt.getQuantity());

//                 if (Boolean.TRUE.equals(depsto.getaInventorier())) {
//                    isArticleQteAvailable.setAvailableQte(depsto.getQte0());
//                } else {
                isArticleQteAvailable.setAvailableQte(depsto.getQte());
//                }
                isArticleQteAvailable.setIsAvailable(depsto.getQte().compareTo(demandedArt.getQuantity()) >= 0);
                return isArticleQteAvailable;
            })
                    //                    .peek(item -> log.debug("itemitemitem : {}", item.toString()))
                    //                    .collect(Collectors.groupingBy(item -> item.getUniteId(),
                    //                    Collectors.reducing(new IsArticleQteAvailable(BigDecimal.ZERO), (a, b) -> {
                    //                        b.setAvailableQte(a.getAvailableQte().add(b.getAvailableQte()));
                    //                        return b;
                    //                    })))
                    //                    .values().stream()
                    //                    .map(item -> {
                    //                        
                    //                item.setIsAvailable(item.getAvailableQte().compareTo(item.getDemandedQte()) >= 0);
                    //                        return item;
                    //                    })
                    .collect(toList());
            
            return result;
        } else {
            return new ArrayList();
        }
    }
    
    public List<Depsto> findByCodartInAndCoddepAndQteGreaterThan(List<Integer> codArticles, Integer codDep, BigDecimal gt) {
        List<Depsto> depstos = new ArrayList<>();
        if (codArticles != null && codArticles.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codArticles.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = codArticles.subList(i * 2000, Math.min(i * 2000 + 2000, codArticles.size()));
                completableFuture[i] = depstorepository.findByCodartInAndCoddepAndQteGreaterThan(codesChunk, codDep, gt).whenComplete((list, exception) -> {
                    depstos.addAll(list);
                });
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        // List<Depsto> depstos = depstorepository.findByCodartInAndCoddepAndQteGreaterThan(codArticles, codDep, gt);
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(codDep);
//        Preconditions.checkBusinessLogique(!depot.getOuvrenouvex(), "depot-closed");
        Collections.sort(depstos, (a, b) -> {
            if (depot.getMethodeTraitementdeStock().equals(FEFO)) {
                return a.getDatPer().compareTo(b.getDatPer());
            } else {
                return b.getDatesys().compareTo(a.getDatesys());
            }
        });
        return depstos;
    }
    
       public List<ArticleStockProjection> findSumQteAndQte0AndStkreelByCodeDepotAndCodeArticleInGrouppedByCodeArticleAndCodeUnite(Integer codeDepot, List<Integer> codeArticles) {
        log.debug("findSumQteAndQte0AndStkreelByCodeDepotAndCodeArticleInGrouppedByCodeArticleAndCodeUnite");
        return depstorepository.findSumQteAndQte0AndStkreelByCodeArticleInGrouppedByCodeArticleAndCodeUnite(codeArticles);
    }
       
    public List<ArticleStockProjection> findByCodeDepotAndCodeArticleInGrouppedByCodeArticleAndCodeUnite(Integer codeDepot, List<Integer> codeArticles) {
        log.debug("findByCodeDepotAndCodeArticleInGrouppedByCodeArticleAndCodeUnite");
        return depstorepository.findByCodeArticleInGrouppedByCodeArticleAndCodeUnite(codeArticles);
    }
    
    public List<ArticleStockProjection> findSumQteAndQte0AndStkreelByCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot(List<Integer> codeArticles) {
        log.debug("findSumQteAndQte0AndStkreelByCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot");
        return depstorepository.findByCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot(codeArticles);
    }
    
    public List<ArticleStockProjection> findByCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot(List<Integer> codeArticles) {
        log.debug("findByCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot");
        return depstorepository.findByCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot(codeArticles);
    }
    
    public List<Depsto> findByCodartInAndQteGreaterThan(List<Integer> codArticles, Integer codDep, BigDecimal gt) {
        List<Depsto> depstos = new ArrayList<>();
        List<Integer> codes = new ArrayList<>();
        codes.addAll(codArticles);
        if (codes != null && codes.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codes.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = codes.subList(i * 2000, Math.min(i * 2000 + 2000, codes.size()));
                Collection<Integer> codesChunkCollection = new ArrayList<>();
                codesChunkCollection.addAll(codesChunk);
                completableFuture[i] = depstorepository.findByCodartInAndQteGreaterThan(codesChunkCollection, gt).whenComplete((list, exception) -> {
                    depstos.addAll(list);
                });
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(codDep);
//        Preconditions.checkBusinessLogique(!depot.getOuvrenouvex(), "depot-closed");
        Collections.sort(depstos, (a, b) -> {
            if (depot.getMethodeTraitementdeStock().equals(FEFO)) {
                return a.getDatPer().compareTo(b.getDatPer());
            } else {
                return b.getDatesys().compareTo(a.getDatesys());
            }
        });
        return depstos;
    }

    /**
     * **** Stock processing
     *
     *****
     * @param numBon
     */
    @Transactional
    public void deleteByNumBon(String numBon) {
        List<Depsto> depstosOfRece = findDepstoByNumBon(numBon);
        log.debug("deleting depstos");
        depstorepository.delete(depstosOfRece);
        
    }
    /////////////////*****************************process stock mouvement retour de fournisseur******************/

    @Transactional
    public void processStockOnReturn(FactureBA factureBA) {
        
        List<Integer> codArticles = factureBA.getDetailFactureBACollection().stream().map(item -> item.getPk().getCodart()).collect(toList());
        List<Depsto> depstos = findByCodartInAndCoddepAndQteGreaterThan(codArticles, factureBA.getCoddep(), BigDecimal.ZERO);
        log.debug("liste of depstos to be processed  {}", depstos);
//        List<DetailMvtStoBA> detailsMvtStoBA = new ArrayList<>();
        for (MvtStoBA detailFacture : factureBA.getDetailFactureBACollection()) {
            List<DetailMvtStoBA> listeDetailMvtstoBA = new ArrayList();
            log.debug("*** Begin treating article {} ****", detailFacture.getCodart());
            BigDecimal qteReturned = detailFacture.getQuantite();
            log.debug("returning {} of mvtstoBA {}", qteReturned, detailFacture);
            Integer availableQteInStock = depstos.stream()
                    .filter(item
                            -> item.getCodart().equals(detailFacture.getPk().getCodart())
                    && item.getUnite().equals(detailFacture.getCodeUnite())
                    && item.getPu()
                            .subtract(detailFacture.getPriuni().multiply((BigDecimal.valueOf(100).subtract(detailFacture.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0)
                    .map(filtredItem -> filtredItem.getQte().intValue())
                    .collect(Collectors.summingInt(Integer::new));
            log.debug("Available quantite is {}", availableQteInStock);
            Preconditions.checkBusinessLogique(availableQteInStock >= qteReturned.intValue(), "return.add.insuffisant-qte", detailFacture.getPk().getCodart().toString());
            /// mm numBon et mm lot et DatPer
            Depsto firstCandiatedepsto = depstos.stream()
                    .filter(item
                            -> item.getCodart().equals(detailFacture.getPk().getCodart())
                    && item.getLotInter().equals(detailFacture.getLotInter())
                    && item.getDatPer().equals(detailFacture.getDatPer())
                    && item.getNumBon().equals(factureBA.getNumpiece())
                    && item.getPu()
                            .subtract(detailFacture.getPriuni().multiply((BigDecimal.valueOf(100).subtract(detailFacture.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0)
                    .findFirst()
                    .orElse(null);
            log.debug(" returning from the same record ");
            log.debug("The candidate is {}", firstCandiatedepsto);
            if (firstCandiatedepsto != null) {
                
                BigDecimal qteToRmv = qteReturned.min(firstCandiatedepsto.getQte());
                firstCandiatedepsto.setQte(firstCandiatedepsto.getQte().subtract(qteToRmv));
                qteReturned = qteReturned.subtract(qteToRmv);
                log.debug(" removing {} from depsto {} ", qteToRmv, firstCandiatedepsto);
                DetailMvtStoBAPK pk = new DetailMvtStoBAPK(detailFacture.getPk().getNumbon(), detailFacture.getPk().getCodart(), detailFacture.getPk().getNumordre(), firstCandiatedepsto.getCode());
                DetailMvtStoBA detailMvtStoBA = new DetailMvtStoBA(pk, detailFacture, firstCandiatedepsto);
                
                detailMvtStoBA.setQuantite_retourne(qteToRmv);
                detailMvtStoBA.setTauxTva(firstCandiatedepsto.getTauxTva());
                detailMvtStoBA.setCodeTva(firstCandiatedepsto.getCodeTva());
                detailMvtStoBA.setNumBonOrigin(firstCandiatedepsto.getNumBonOrigin());
                listeDetailMvtstoBA.add(detailMvtStoBA);
//                result.add(firstCandiatedepsto);   
            }
            
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                detailFacture.setDetailMvtStoBACollection(listeDetailMvtstoBA);
                continue;
            }
//mm codart et mm NumBon mais pas la meme LOT ET datPer
            List<Depsto> secondCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(detailFacture.getPk().getCodart())
                    && item.getNumBon().equals(factureBA.getNumpiece())
                    && !item.equals(firstCandiatedepsto)
                    && item.getPu()
                            .subtract(detailFacture.getPriuni().multiply((BigDecimal.valueOf(100).subtract(detailFacture.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0)
                    .collect(toList());
            log.debug(" returning from the same reception ");
            log.debug("The candidates are {}", secondCandidatsDepstos);
            for (Depsto secCandidDeps : secondCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(secCandidDeps.getQte());
                secCandidDeps.setQte(secCandidDeps.getQte().subtract(qteToRmv));
                qteReturned = qteReturned.subtract(qteToRmv);
                DetailMvtStoBAPK pk = new DetailMvtStoBAPK(detailFacture.getPk().getNumbon(), detailFacture.getPk().getCodart(), detailFacture.getPk().getNumordre(), secCandidDeps.getCode());
                DetailMvtStoBA detailMvtStoBA = new DetailMvtStoBA(pk, detailFacture, secCandidDeps);
                
                detailMvtStoBA.setDepsto(detailFacture, secCandidDeps);
                detailMvtStoBA.setQuantite_retourne(qteToRmv);
                detailMvtStoBA.setTauxTva(secCandidDeps.getTauxTva());
                detailMvtStoBA.setCodeTva(secCandidDeps.getCodeTva());
                detailMvtStoBA.setNumBonOrigin(secCandidDeps.getNumBonOrigin());
                listeDetailMvtstoBA.add(detailMvtStoBA);
                
                log.debug(" removing {} from depsto {} ", qteToRmv, secCandidDeps);
//                result.add(secCandidDeps);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                detailFacture.setDetailMvtStoBACollection(listeDetailMvtstoBA);
                continue;
            }
            // diff numbon ,m lot ,m Pu
            List<Depsto> thirdCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(detailFacture.getPk().getCodart())
                    && item.getUnite().equals(detailFacture.getCodeUnite())
                    && !item.getNumBon().equals(factureBA.getNumpiece())
                    && item.getLotInter().equals(detailFacture.getLotInter())
                    && item.getDatPer().equals(detailFacture.getDatPer())
                    && item.getPu()
                            .subtract(detailFacture.getPriuni().multiply((BigDecimal.valueOf(100).subtract(detailFacture.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal("0.01")) <= 0)
                    .collect(toList());
            log.debug(" returning from third CandidatsDepstos");
            log.debug("The candidates are {}", thirdCandidatsDepstos);
            for (Depsto thirdCandidDeps : thirdCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(thirdCandidDeps.getQte());
                thirdCandidDeps.setQte(thirdCandidDeps.getQte().subtract(qteToRmv));
                log.debug(" removing {} from depsto {} ", qteToRmv, thirdCandidDeps);
                qteReturned = qteReturned.subtract(qteToRmv);
                DetailMvtStoBAPK pk = new DetailMvtStoBAPK(detailFacture.getPk().getNumbon(), detailFacture.getPk().getCodart(), detailFacture.getPk().getNumordre(), thirdCandidDeps.getCode());
                DetailMvtStoBA detailMvtStoBA = new DetailMvtStoBA(pk, detailFacture, thirdCandidDeps);
                detailMvtStoBA.setDepsto(detailFacture, thirdCandidDeps);
                detailMvtStoBA.setQuantite_retourne(qteToRmv);
                detailMvtStoBA.setTauxTva(thirdCandidDeps.getTauxTva());
                detailMvtStoBA.setCodeTva(thirdCandidDeps.getCodeTva());
                detailMvtStoBA.setNumBonOrigin(thirdCandidDeps.getNumBonOrigin());
                listeDetailMvtstoBA.add(detailMvtStoBA);
                
                log.debug(" removing {} from depsto {} ", qteToRmv, thirdCandidDeps);
//                result.add(secCandidDeps);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                detailFacture.setDetailMvtStoBACollection(listeDetailMvtstoBA);
                continue;
            }
            //MM LOT , mm dat diff pu
            List<Depsto> fourthCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(detailFacture.getPk().getCodart())
                    && item.getUnite().equals(detailFacture.getCodeUnite())
                    && !item.getNumBon().equals(factureBA.getNumpiece())
                    && item.getLotInter().equals(detailFacture.getLotInter())
                    && item.getDatPer().equals(detailFacture.getDatPer())
                    && item.getPu()
                            .subtract(detailFacture.getPriuni().multiply((BigDecimal.valueOf(100).subtract(detailFacture.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0
                    && item.getQte().compareTo(BigDecimal.ZERO) > 0)
                    .collect(toList());
            log.debug(" returning fourth CandidatsDepstos");
            log.debug("The candidates are {}", fourthCandidatsDepstos);
            for (Depsto fourthCandidDeps : fourthCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(fourthCandidDeps.getQte());
                fourthCandidDeps.setQte(fourthCandidDeps.getQte().subtract(qteToRmv));
                log.debug(" removing {} from depsto {} ", qteToRmv, fourthCandidDeps);
                qteReturned = qteReturned.subtract(qteToRmv);
                
                DetailMvtStoBAPK pk = new DetailMvtStoBAPK(detailFacture.getPk().getNumbon(), detailFacture.getPk().getCodart(), detailFacture.getPk().getNumordre(), fourthCandidDeps.getCode());
                DetailMvtStoBA detailMvtStoBA = new DetailMvtStoBA(pk, detailFacture, fourthCandidDeps);
                detailMvtStoBA.setQuantite_retourne(qteToRmv);
                detailMvtStoBA.setDepsto(detailFacture, fourthCandidDeps);
                detailMvtStoBA.setTauxTva(fourthCandidDeps.getTauxTva());
                detailMvtStoBA.setCodeTva(fourthCandidDeps.getCodeTva());
                detailMvtStoBA.setNumBonOrigin(fourthCandidDeps.getNumBonOrigin());
                listeDetailMvtstoBA.add(detailMvtStoBA);

//                result.add(thirdCandid);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                detailFacture.setDetailMvtStoBACollection(listeDetailMvtstoBA);
                continue;
            }
            List<Depsto> fifthCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(detailFacture.getPk().getCodart())
                    && item.getUnite().equals(detailFacture.getCodeUnite())
                    && item.getPu()
                            .subtract(detailFacture.getPriuni().multiply((BigDecimal.valueOf(100).subtract(detailFacture.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0
                    && item.getQte().compareTo(BigDecimal.ZERO) > 0)
                    .collect(toList());
            log.debug(" returning from fifth CandidatsDepstos");
            log.debug("The candidates are {}", fifthCandidatsDepstos);
            for (Depsto fifthCandidDeps : fifthCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(fifthCandidDeps.getQte());
                fifthCandidDeps.setQte(fifthCandidDeps.getQte().subtract(qteToRmv));
                log.debug(" removing {} from depsto {} ", qteToRmv, fifthCandidDeps);
                qteReturned = qteReturned.subtract(qteToRmv);
                
                DetailMvtStoBAPK pk = new DetailMvtStoBAPK(detailFacture.getPk().getNumbon(), detailFacture.getPk().getCodart(), detailFacture.getPk().getNumordre(), fifthCandidDeps.getCode());
                DetailMvtStoBA detailMvtStoBA = new DetailMvtStoBA(pk, detailFacture, fifthCandidDeps);
                detailMvtStoBA.setQuantite_retourne(qteToRmv);
                detailMvtStoBA.setDepsto(detailFacture, fifthCandidDeps);
                detailMvtStoBA.setTauxTva(fifthCandidDeps.getTauxTva());
                detailMvtStoBA.setCodeTva(fifthCandidDeps.getCodeTva());
                detailMvtStoBA.setNumBonOrigin(fifthCandidDeps.getNumBonOrigin());
                listeDetailMvtstoBA.add(detailMvtStoBA);

//                result.add(thirdCandid);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            log.debug("listeDetailMvtstoBA est {}", listeDetailMvtstoBA);
            detailFacture.setDetailMvtStoBACollection(listeDetailMvtstoBA);
            
        }

//        detailMvtStoBARepository.save(detailsMvtStoBA);
//        log.debug("list of depsto after processing {}", depstos);
//        log.debug("saving/updating liste of depstos after processing {}", result);
//        depstorepository.save(result);
    }
    
    @Transactional
    public void processStockOnAvoirFournisseur(AvoirFournisseur avoirFournisseur) {
        
        List<Integer> codArticles = avoirFournisseur.getMvtstoAFList().stream().map(item -> item.getCodart()).collect(toList());
        List<Depsto> depstos = findByCodartInAndCoddepAndQteGreaterThan(codArticles, avoirFournisseur.getCoddep(), BigDecimal.ZERO);
        log.debug("liste of depstos to be processed  {}", depstos);
//        List<DetailMvtStoBA> detailsMvtStoBA = new ArrayList<>();
        for (MvtstoAF mvtstoAF : avoirFournisseur.getMvtstoAFList()) {
            List<DetailMvtStoAF> listeDetailMvtstoAF = new ArrayList();
            log.debug("*** Begin treating article {} ****", mvtstoAF.getCodart());
            BigDecimal qteReturned = mvtstoAF.getQuantite();
            log.debug("returning {} of mvtstoAF {}", qteReturned, mvtstoAF);
            Integer availableQteInStock = depstos.stream()
                    .filter(item -> item.getCodart().equals(mvtstoAF.getCodart())
                    && item.getUnite().equals(mvtstoAF.getUnite()))
                    .map(filtredItem -> filtredItem.getQte().intValue())
                    .collect(Collectors.summingInt(Integer::new));
            log.debug("Available quantite is {}", availableQteInStock);
            Preconditions.checkBusinessLogique(availableQteInStock >= qteReturned.intValue(), "return.add.insuffisant-qte", mvtstoAF.getCodart().toString());
            /// mm numBon et mm lot et DatPer
            Depsto firstCandiatedepsto = depstos.stream()
                    .filter(item
                            -> item.getCodart().equals(mvtstoAF.getCodart())
                    && item.getLotInter().equals(mvtstoAF.getLotinter())
                    && item.getDatPer().equals(mvtstoAF.getDatPer())
                    && item.getNumBon().equals(mvtstoAF.getNumbonReception())
                    && item.getPu()
                            .subtract(mvtstoAF.getPriuni().multiply((BigDecimal.valueOf(100).subtract(mvtstoAF.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0)
                    .findFirst()
                    .orElse(null);
            log.debug(" returning from the same record ");
            log.debug("The candidate is {}", firstCandiatedepsto);
            if (firstCandiatedepsto != null) {
                
                BigDecimal qteToRmv = qteReturned.min(firstCandiatedepsto.getQte());
                firstCandiatedepsto.setQte(firstCandiatedepsto.getQte().subtract(qteToRmv));
                qteReturned = qteReturned.subtract(qteToRmv);
                log.debug(" removing {} from depsto {} ", qteToRmv, firstCandiatedepsto);
                
                DetailMvtStoAF detailMvtStoAF = new DetailMvtStoAF(firstCandiatedepsto, mvtstoAF);
                detailMvtStoAF.setQuantitePrelevee(qteToRmv);
                detailMvtStoAF.setTauxTva(firstCandiatedepsto.getTauxTva());
                detailMvtStoAF.setCodeTva(firstCandiatedepsto.getCodeTva());
                detailMvtStoAF.setQuantiteDisponible(firstCandiatedepsto.getQte());
                detailMvtStoAF.setCodeDepsto(firstCandiatedepsto.getCode());
                detailMvtStoAF.setNumbon(firstCandiatedepsto.getNumBon());
                detailMvtStoAF.setCodeMvtSto(mvtstoAF.getCode());
                detailMvtStoAF.setNumBonOrigin(firstCandiatedepsto.getNumBonOrigin());
                detailMvtStoAF.setPriuni(firstCandiatedepsto.getPu());
                
                listeDetailMvtstoAF.add(detailMvtStoAF);
//                result.add(firstCandiatedepsto);   
            }
            
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                mvtstoAF.setDetailMvtStoAFList(listeDetailMvtstoAF);
                continue;
            }
            //mm codart et mm NumBon mais pas la meme LOT ET datPer
            List<Depsto> secondCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(mvtstoAF.getCodart())
                    && item.getNumBon().equals(mvtstoAF.getNumbonReception())
                    && !item.equals(firstCandiatedepsto)
                    && item.getPu()
                            .subtract(mvtstoAF.getPriuni().multiply((BigDecimal.valueOf(100).subtract(mvtstoAF.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0)
                    .collect(toList());
            log.debug(" returning from the same reception ");
            log.debug("The candidates are {}", secondCandidatsDepstos);
            for (Depsto secCandidDeps : secondCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(secCandidDeps.getQte());
                secCandidDeps.setQte(secCandidDeps.getQte().subtract(qteToRmv));
                qteReturned = qteReturned.subtract(qteToRmv);
                
                DetailMvtStoAF detailMvtStoAF = new DetailMvtStoAF(secCandidDeps, mvtstoAF);
                detailMvtStoAF.setQuantitePrelevee(qteToRmv);
                detailMvtStoAF.setTauxTva(secCandidDeps.getTauxTva());
                detailMvtStoAF.setCodeTva(secCandidDeps.getCodeTva());
                detailMvtStoAF.setQuantiteDisponible(secCandidDeps.getQte());
                detailMvtStoAF.setCodeDepsto(secCandidDeps.getCode());
                detailMvtStoAF.setNumbon(secCandidDeps.getNumBon());
                detailMvtStoAF.setCodeMvtSto(mvtstoAF.getCode());
                detailMvtStoAF.setPriuni(secCandidDeps.getPu());
                detailMvtStoAF.setNumBonOrigin(secCandidDeps.getNumBonOrigin());
                listeDetailMvtstoAF.add(detailMvtStoAF);
                
                log.debug(" removing {} from depsto {} ", qteToRmv, secCandidDeps);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                mvtstoAF.setDetailMvtStoAFList(listeDetailMvtstoAF);
                continue;
            }
            // diff numbon ,m lot ,m Pu
            List<Depsto> thirdCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(mvtstoAF.getCodart())
                    && item.getUnite().equals(mvtstoAF.getUnite())
                    && !item.getNumBon().equals(mvtstoAF.getNumbonReception())
                    && item.getLotInter().equals(mvtstoAF.getLotinter())
                    && item.getDatPer().equals(mvtstoAF.getDatPer())
                    && item.getPu()
                            .subtract(mvtstoAF.getPriuni())
                            .abs().compareTo(new BigDecimal("0.01")) <= 0)
                    .collect(toList());
            log.debug(" returning from third CandidatsDepstos");
            log.debug("The candidates are {}", thirdCandidatsDepstos);
            for (Depsto thirdCandid : thirdCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(thirdCandid.getQte());
                thirdCandid.setQte(thirdCandid.getQte().subtract(qteToRmv));
                log.debug(" removing {} from depsto {} ", qteToRmv, thirdCandid);
                qteReturned = qteReturned.subtract(qteToRmv);
                
                DetailMvtStoAF detailMvtStoAF = new DetailMvtStoAF(thirdCandid, mvtstoAF);
                detailMvtStoAF.setQuantitePrelevee(qteToRmv);
                detailMvtStoAF.setTauxTva(thirdCandid.getTauxTva());
                detailMvtStoAF.setCodeTva(thirdCandid.getCodeTva());
                detailMvtStoAF.setQuantiteDisponible(thirdCandid.getQte());
                detailMvtStoAF.setCodeDepsto(thirdCandid.getCode());
                detailMvtStoAF.setNumbon(thirdCandid.getNumBon());
                detailMvtStoAF.setNumBonOrigin(thirdCandid.getNumBonOrigin());
                detailMvtStoAF.setCodeMvtSto(mvtstoAF.getCode());
                detailMvtStoAF.setPriuni(thirdCandid.getPu());
                listeDetailMvtstoAF.add(detailMvtStoAF);
                
                log.debug(" removing {} from depsto {} ", qteToRmv, thirdCandid);
//                result.add(secCandidDeps);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                mvtstoAF.setDetailMvtStoAFList(listeDetailMvtstoAF);
                continue;
            }

            //MM LOT , mm dat diff pu
            List<Depsto> fourthCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(mvtstoAF.getCodart())
                    && item.getUnite().equals(mvtstoAF.getUnite())
                    && !item.getNumBon().equals(mvtstoAF.getNumbonReception())
                    && item.getLotInter().equals(mvtstoAF.getLotinter())
                    && item.getDatPer().equals(mvtstoAF.getDatPer())
                    && item.getPu()
                            .subtract(mvtstoAF.getPriuni().multiply((BigDecimal.valueOf(100).subtract(mvtstoAF.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0
                    && item.getQte().compareTo(BigDecimal.ZERO) > 0)
                    .collect(toList());
            log.debug(" returning fourth CandidatsDepstos");
            log.debug("The candidates are {}", fourthCandidatsDepstos);
            for (Depsto fourthCandidDeps : fourthCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(fourthCandidDeps.getQte());
                fourthCandidDeps.setQte(fourthCandidDeps.getQte().subtract(qteToRmv));
                log.debug(" removing {} from depsto {} ", qteToRmv, fourthCandidDeps);
                qteReturned = qteReturned.subtract(qteToRmv);
                
                DetailMvtStoAF detailMvtStoAF = new DetailMvtStoAF(fourthCandidDeps, mvtstoAF);
                detailMvtStoAF.setQuantitePrelevee(qteToRmv);
                detailMvtStoAF.setTauxTva(fourthCandidDeps.getTauxTva());
                detailMvtStoAF.setCodeTva(fourthCandidDeps.getCodeTva());
                detailMvtStoAF.setQuantiteDisponible(fourthCandidDeps.getQte());
                detailMvtStoAF.setCodeDepsto(fourthCandidDeps.getCode());
                detailMvtStoAF.setNumbon(fourthCandidDeps.getNumBon());
                detailMvtStoAF.setCodeMvtSto(mvtstoAF.getCode());
                detailMvtStoAF.setNumBonOrigin(fourthCandidDeps.getNumBonOrigin());
                detailMvtStoAF.setPriuni(fourthCandidDeps.getPu());
                listeDetailMvtstoAF.add(detailMvtStoAF);

//                result.add(thirdCandid);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
// n'importe
            List<Depsto> fifthCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(mvtstoAF.getCodart())
                    && item.getUnite().equals(mvtstoAF.getUnite())
                    && item.getPu()
                            .subtract(mvtstoAF.getPriuni().multiply((BigDecimal.valueOf(100).subtract(mvtstoAF.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0
                    && item.getQte().compareTo(BigDecimal.ZERO) > 0)
                    .collect(toList());
            log.debug(" returning from fifth CandidatsDepstos");
            log.debug("The candidates are {}", fifthCandidatsDepstos);
            for (Depsto fifthCandidDeps : fifthCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(fifthCandidDeps.getQte());
                fifthCandidDeps.setQte(fifthCandidDeps.getQte().subtract(qteToRmv));
                log.debug(" removing {} from depsto {} ", qteToRmv, fifthCandidDeps);
                qteReturned = qteReturned.subtract(qteToRmv);
                
                DetailMvtStoAF detailMvtStoAF = new DetailMvtStoAF(fifthCandidDeps, mvtstoAF);
                detailMvtStoAF.setQuantitePrelevee(qteToRmv);
                detailMvtStoAF.setTauxTva(fifthCandidDeps.getTauxTva());
                detailMvtStoAF.setCodeTva(fifthCandidDeps.getCodeTva());
                detailMvtStoAF.setQuantiteDisponible(fifthCandidDeps.getQte());
                detailMvtStoAF.setCodeDepsto(fifthCandidDeps.getCode());
                detailMvtStoAF.setNumbon(fifthCandidDeps.getNumBon());
                detailMvtStoAF.setCodeMvtSto(mvtstoAF.getCode());
                detailMvtStoAF.setNumBonOrigin(fifthCandidDeps.getNumBonOrigin());
                detailMvtStoAF.setPriuni(fifthCandidDeps.getPu());
                listeDetailMvtstoAF.add(detailMvtStoAF);

//                result.add(thirdCandid);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            log.debug("listeDetailMvtstoBA est {}", listeDetailMvtstoAF);
            mvtstoAF.setDetailMvtStoAFList(listeDetailMvtstoAF);
//
        }

//        detailMvtStoBARepository.save(detailsMvtStoBA);
//        log.debug("list of depsto after processing {}", depstos);
//        log.debug("saving/updating liste of depstos after processing {}", result);
//        depstorepository.save(result);
    }
    
    @Transactional
    public void processStockOnTransferBranchCompany(TransfertCompanyBranch transfertCompanyBranch) {
        
        List<Integer> codArticles = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection().stream().map(item -> item.getCodeArticle()).collect(toList());
        List<Depsto> depstos = findByCodartInAndCoddepAndQteGreaterThan(codArticles, transfertCompanyBranch.getCodeDepot(), BigDecimal.ZERO);
        log.debug("liste of depstos to be processed  {}", depstos);
//        List<DetailMvtStoBA> detailsMvtStoBA = new ArrayList<>();
        for (DetailTransfertCompanyBranch detailTransfert : transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()) {
            List<TraceDetailTransfertCompanyBranch> listeTraceDetailTransfertCompanyBranche = new ArrayList();
            log.debug("*** Begin treating article {} ****", detailTransfert.getCodeArticle());
            BigDecimal qteReturned = detailTransfert.getQuantite();
            log.debug("returning {} of detailTransfert {}", qteReturned, detailTransfert);
            Integer availableQteInStock = depstos.stream()
                    .filter(item
                            -> item.getCodart().equals(detailTransfert.getCodeArticle())
                    && item.getUnite().equals(detailTransfert.getCodeUnite())
                    && item.getPu()
                            .subtract(detailTransfert.getPrixUnitaire().multiply((BigDecimal.valueOf(100).subtract(detailTransfert.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0)
                    .map(filtredItem -> filtredItem.getQte().intValue())
                    .collect(Collectors.summingInt(Integer::new));
            log.debug("Available quantite is {}", availableQteInStock);
            Preconditions.checkBusinessLogique(availableQteInStock >= qteReturned.intValue(), "return.add.insuffisant-qte", detailTransfert.getCodeArticle().toString());
            /// mm numBon et mm lot et DatPer
            Depsto firstCandiatedepsto = depstos.stream()
                    .filter(item
                            -> item.getCodart().equals(detailTransfert.getCodeArticle())
                    && item.getLotInter().equals(detailTransfert.getLotInter())
                    && item.getDatPer().equals(detailTransfert.getDatePeremption())
                    && item.getNumBon().equals(transfertCompanyBranch.getNumBonTransfertRelatif())
                    && item.getPu()
                            .subtract(detailTransfert.getPrixUnitaire().multiply((BigDecimal.valueOf(100).subtract(detailTransfert.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0)
                    .findFirst()
                    .orElse(null);
            
            if (firstCandiatedepsto != null) {
                log.debug(" returning from the same record ");
                log.debug("The candidate is {}", firstCandiatedepsto);
                BigDecimal qteToRmv = qteReturned.min(firstCandiatedepsto.getQte());
                firstCandiatedepsto.setQte(firstCandiatedepsto.getQte().subtract(qteToRmv));
                qteReturned = qteReturned.subtract(qteToRmv);
                log.debug(" removing {} from depsto {} ", qteToRmv, firstCandiatedepsto);
                
                TraceDetailTransfertCompanyBranch traceDetailTransfertCompanyBranch = new TraceDetailTransfertCompanyBranch(firstCandiatedepsto, detailTransfert);
                traceDetailTransfertCompanyBranch.setNumbonTransfert(transfertCompanyBranch.getNumBon());
                traceDetailTransfertCompanyBranch.setCodeArticle(detailTransfert.getCodeArticle());
                traceDetailTransfertCompanyBranch.setQuantitePrelevee(qteToRmv);
                traceDetailTransfertCompanyBranch.setTauxTvaDepsto(firstCandiatedepsto.getTauxTva());
                traceDetailTransfertCompanyBranch.setCodeTvaDepsto(firstCandiatedepsto.getCodeTva());
                traceDetailTransfertCompanyBranch.setQuantiteDisponible(firstCandiatedepsto.getQte());
                traceDetailTransfertCompanyBranch.setCodeDepsto(firstCandiatedepsto.getCode());
                traceDetailTransfertCompanyBranch.setNumbonDepsto(firstCandiatedepsto.getNumBon());
                traceDetailTransfertCompanyBranch.setCodeDetailTransfert(detailTransfert.getCode());
                traceDetailTransfertCompanyBranch.setNumbonOriginDepsto(firstCandiatedepsto.getNumBonOrigin());
                traceDetailTransfertCompanyBranch.setPrixUnitaireDepsto(firstCandiatedepsto.getPu());
                listeTraceDetailTransfertCompanyBranche.add(traceDetailTransfertCompanyBranch);
//                result.add(firstCandiatedepsto);   
            }
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                detailTransfert.setListeTraceDetailTransfertCompanyBranch(listeTraceDetailTransfertCompanyBranche);
                continue;
            }
//mm codart et mm NumBon mais pas la meme LOT ET datPer
            List<Depsto> secondCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(detailTransfert.getCodeArticle())
                    && item.getNumBon().equals(transfertCompanyBranch.getNumBonTransfertRelatif())
                    && !item.equals(firstCandiatedepsto)
                    && item.getPu()
                            .subtract(detailTransfert.getPrixUnitaire().multiply((BigDecimal.valueOf(100).subtract(detailTransfert.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0)
                    .collect(toList());
            log.debug(" returning from the same reception ");
            log.debug("The candidates are {}", secondCandidatsDepstos);
            for (Depsto secCandidDeps : secondCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(secCandidDeps.getQte());
                secCandidDeps.setQte(secCandidDeps.getQte().subtract(qteToRmv));
                qteReturned = qteReturned.subtract(qteToRmv);
                
                TraceDetailTransfertCompanyBranch traceDetailTransfertCompanyBranch = new TraceDetailTransfertCompanyBranch(firstCandiatedepsto, detailTransfert);
                traceDetailTransfertCompanyBranch.setNumbonTransfert(transfertCompanyBranch.getNumBon());
                traceDetailTransfertCompanyBranch.setQuantitePrelevee(qteToRmv);
                traceDetailTransfertCompanyBranch.setTauxTvaDepsto(secCandidDeps.getTauxTva());
                traceDetailTransfertCompanyBranch.setCodeTvaDepsto(secCandidDeps.getCodeTva());
                traceDetailTransfertCompanyBranch.setQuantiteDisponible(secCandidDeps.getQte());
                traceDetailTransfertCompanyBranch.setCodeDepsto(secCandidDeps.getCode());
                traceDetailTransfertCompanyBranch.setNumbonDepsto(secCandidDeps.getNumBon());
                traceDetailTransfertCompanyBranch.setCodeDetailTransfert(detailTransfert.getCode());
                traceDetailTransfertCompanyBranch.setNumbonOriginDepsto(secCandidDeps.getNumBonOrigin());
                traceDetailTransfertCompanyBranch.setPrixUnitaireDepsto(secCandidDeps.getPu());
                traceDetailTransfertCompanyBranch.setCodeArticle(detailTransfert.getCodeArticle());
                listeTraceDetailTransfertCompanyBranche.add(traceDetailTransfertCompanyBranch);
                
                log.debug(" removing {} from depsto {} ", qteToRmv, secCandidDeps);
//                result.add(secCandidDeps);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                detailTransfert.setListeTraceDetailTransfertCompanyBranch(listeTraceDetailTransfertCompanyBranche);
                continue;
            }
            // diff numbon ,m lot ,m Pu
            List<Depsto> thirdCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(detailTransfert.getCodeArticle())
                    && item.getUnite().equals(detailTransfert.getCodeUnite())
                    && !item.getNumBon().equals(transfertCompanyBranch.getNumBonTransfertRelatif())
                    && item.getLotInter().equals(detailTransfert.getLotInter())
                    && item.getDatPer().equals(detailTransfert.getDatePeremption())
                    && item.getPu()
                            .subtract(detailTransfert.getPrixUnitaire().multiply((BigDecimal.valueOf(100).subtract(detailTransfert.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal("0.01")) <= 0)
                    .collect(toList());
            log.debug(" returning from third CandidatsDepstos");
            log.debug("The candidates are {}", thirdCandidatsDepstos);
            for (Depsto thirdCandidDeps : thirdCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(thirdCandidDeps.getQte());
                thirdCandidDeps.setQte(thirdCandidDeps.getQte().subtract(qteToRmv));
                log.debug(" removing {} from depsto {} ", qteToRmv, thirdCandidDeps);
                qteReturned = qteReturned.subtract(qteToRmv);
                
                TraceDetailTransfertCompanyBranch traceDetailTransfertCompanyBranch = new TraceDetailTransfertCompanyBranch(thirdCandidDeps, detailTransfert);
                traceDetailTransfertCompanyBranch.setCodeArticle(detailTransfert.getCodeArticle());
                traceDetailTransfertCompanyBranch.setNumbonTransfert(transfertCompanyBranch.getNumBon());
                traceDetailTransfertCompanyBranch.setQuantitePrelevee(qteToRmv);
                traceDetailTransfertCompanyBranch.setTauxTvaDepsto(thirdCandidDeps.getTauxTva());
                traceDetailTransfertCompanyBranch.setCodeTvaDepsto(thirdCandidDeps.getCodeTva());
                traceDetailTransfertCompanyBranch.setQuantiteDisponible(thirdCandidDeps.getQte());
                traceDetailTransfertCompanyBranch.setCodeDepsto(thirdCandidDeps.getCode());
                traceDetailTransfertCompanyBranch.setNumbonDepsto(thirdCandidDeps.getNumBon());
                traceDetailTransfertCompanyBranch.setCodeDetailTransfert(detailTransfert.getCode());
                traceDetailTransfertCompanyBranch.setNumbonOriginDepsto(thirdCandidDeps.getNumBonOrigin());
                traceDetailTransfertCompanyBranch.setPrixUnitaireDepsto(thirdCandidDeps.getPu());
                listeTraceDetailTransfertCompanyBranche.add(traceDetailTransfertCompanyBranch);
                
                log.debug(" removing {} from depsto {} ", qteToRmv, thirdCandidDeps);
//                result.add(secCandidDeps);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                detailTransfert.setListeTraceDetailTransfertCompanyBranch(listeTraceDetailTransfertCompanyBranche);
                continue;
            }
            //MM LOT , mm dat diff pu
            List<Depsto> fourthCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(detailTransfert.getCodeArticle())
                    && item.getUnite().equals(detailTransfert.getCodeUnite())
                    && !item.getNumBon().equals(transfertCompanyBranch.getNumBonTransfertRelatif())
                    && item.getLotInter().equals(detailTransfert.getLotInter())
                    && item.getDatPer().equals(detailTransfert.getDatePeremption())
                    && item.getPu()
                            .subtract(detailTransfert.getPrixUnitaire().multiply((BigDecimal.valueOf(100).subtract(detailTransfert.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0
                    && item.getQte().compareTo(BigDecimal.ZERO) > 0)
                    .collect(toList());
            log.debug(" returning fourth CandidatsDepstos");
            log.debug("The candidates are {}", fourthCandidatsDepstos);
            for (Depsto fourthCandidDeps : fourthCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(fourthCandidDeps.getQte());
                fourthCandidDeps.setQte(fourthCandidDeps.getQte().subtract(qteToRmv));
                log.debug(" removing {} from depsto {} ", qteToRmv, fourthCandidDeps);
                qteReturned = qteReturned.subtract(qteToRmv);
                
                TraceDetailTransfertCompanyBranch traceDetailTransfertCompanyBranch = new TraceDetailTransfertCompanyBranch(fourthCandidDeps, detailTransfert);
                traceDetailTransfertCompanyBranch.setCodeArticle(detailTransfert.getCodeArticle());
                traceDetailTransfertCompanyBranch.setNumbonTransfert(transfertCompanyBranch.getNumBon());
                traceDetailTransfertCompanyBranch.setQuantitePrelevee(qteToRmv);
                traceDetailTransfertCompanyBranch.setTauxTvaDepsto(fourthCandidDeps.getTauxTva());
                traceDetailTransfertCompanyBranch.setCodeTvaDepsto(fourthCandidDeps.getCodeTva());
                traceDetailTransfertCompanyBranch.setQuantiteDisponible(fourthCandidDeps.getQte());
                traceDetailTransfertCompanyBranch.setCodeDepsto(fourthCandidDeps.getCode());
                traceDetailTransfertCompanyBranch.setNumbonDepsto(fourthCandidDeps.getNumBon());
                traceDetailTransfertCompanyBranch.setCodeDetailTransfert(detailTransfert.getCode());
                traceDetailTransfertCompanyBranch.setNumbonOriginDepsto(fourthCandidDeps.getNumBonOrigin());
                traceDetailTransfertCompanyBranch.setPrixUnitaireDepsto(fourthCandidDeps.getPu());
                listeTraceDetailTransfertCompanyBranche.add(traceDetailTransfertCompanyBranch);

//                result.add(thirdCandid);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                detailTransfert.setListeTraceDetailTransfertCompanyBranch(listeTraceDetailTransfertCompanyBranche);
                continue;
            }
            List<Depsto> fifthCandidatsDepstos = depstos.stream()
                    .filter(item -> item.getCodart().equals(detailTransfert.getCodeArticle())
                    && item.getUnite().equals(detailTransfert.getCodeUnite())
                    && item.getPu()
                            .subtract(detailTransfert.getPrixUnitaire().multiply((BigDecimal.valueOf(100).subtract(detailTransfert.getRemise())).divide(BigDecimal.valueOf(100))))
                            .abs().compareTo(new BigDecimal(differencePermiseBonRetour)) <= 0
                    && item.getQte().compareTo(BigDecimal.ZERO) > 0)
                    .collect(toList());
            log.debug(" returning from fifth CandidatsDepstos");
            log.debug("The candidates are {}", fifthCandidatsDepstos);
            for (Depsto fifthCandidDeps : fifthCandidatsDepstos) {
                BigDecimal qteToRmv = qteReturned.min(fifthCandidDeps.getQte());
                fifthCandidDeps.setQte(fifthCandidDeps.getQte().subtract(qteToRmv));
                log.debug(" removing {} from depsto {} ", qteToRmv, fifthCandidDeps);
                qteReturned = qteReturned.subtract(qteToRmv);
                
                TraceDetailTransfertCompanyBranch traceDetailTransfertCompanyBranch = new TraceDetailTransfertCompanyBranch(fifthCandidDeps, detailTransfert);
                traceDetailTransfertCompanyBranch.setCodeArticle(detailTransfert.getCodeArticle());
                traceDetailTransfertCompanyBranch.setNumbonTransfert(transfertCompanyBranch.getNumBon());
                traceDetailTransfertCompanyBranch.setQuantitePrelevee(qteToRmv);
                traceDetailTransfertCompanyBranch.setTauxTvaDepsto(fifthCandidDeps.getTauxTva());
                traceDetailTransfertCompanyBranch.setCodeTvaDepsto(fifthCandidDeps.getCodeTva());
                traceDetailTransfertCompanyBranch.setQuantiteDisponible(fifthCandidDeps.getQte());
                traceDetailTransfertCompanyBranch.setCodeDepsto(fifthCandidDeps.getCode());
                traceDetailTransfertCompanyBranch.setNumbonDepsto(fifthCandidDeps.getNumBon());
                traceDetailTransfertCompanyBranch.setCodeDetailTransfert(detailTransfert.getCode());
                traceDetailTransfertCompanyBranch.setNumbonOriginDepsto(fifthCandidDeps.getNumBonOrigin());
                traceDetailTransfertCompanyBranch.setPrixUnitaireDepsto(fifthCandidDeps.getPu());
                listeTraceDetailTransfertCompanyBranche.add(traceDetailTransfertCompanyBranch);

//                result.add(thirdCandid);
                if (qteReturned.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            
            log.debug("listeDetailMvtstoBA est {}", listeTraceDetailTransfertCompanyBranche);
            detailTransfert.setListeTraceDetailTransfertCompanyBranch(listeTraceDetailTransfertCompanyBranche);
            
        }

//        detailMvtStoBARepository.save(detailsMvtStoBA);
//        log.debug("list of depsto after processing {}", depstos);
//        log.debug("saving/updating liste of depstos after processing {}", result);
//        depstorepository.save(result);
    }

/////////////////*****************************process stock mouvement retour de fournisseur******************/
    @Transactional
    public List<Depsto> processStockOnRedressementAvecMotifCorrectionLot(List<Depsto> listeMatchingDepstos, MvtStoBE mvtstoBEToRemove, String numBon) {
        List<Depsto> newStock = new ArrayList();

//         log.debug("codart est {},uite est {},lot est {},datPer est {},", mvtstoBEToRemove.getCodart(),mvtstoBEToRemove.getUnite(),mvtstoBEToRemove.getLotinter(),mvtstoBEToRemove.getDatPer());
//        log.debug("listeMatchingDepstos est {}", listeMatchingDepstos);
        BigDecimal availableQuantity = listeMatchingDepstos.stream().collect(Collectors.reducing(BigDecimal.ZERO,
                depsto -> depsto.getQte(), BigDecimal::add));
        log.debug("availableQuantity du codart {} est {} et qteToRemov est {}", mvtstoBEToRemove.getCodart().toString(), availableQuantity, mvtstoBEToRemove.getQuantite().abs());
        Preconditions.checkBusinessLogique(!(availableQuantity.compareTo(mvtstoBEToRemove.getQuantite().abs()) < 0), "quantite-insuffisante-pour-ce-lot", mvtstoBEToRemove.getCodeSaisi(), mvtstoBEToRemove.getLotinter());
        
        List<DetailMvtStoBE> listeDetailMvtstoBE = new ArrayList();
        
        log.debug("*** Begin treating article {} ****", mvtstoBEToRemove.getCodart());
        BigDecimal qteToRedress = mvtstoBEToRemove.getQuantite().abs();
        
        BigDecimal priuni = BigDecimal.ZERO;
        
        for (Depsto depsto : listeMatchingDepstos) {
            
            BigDecimal qteToRmv = qteToRedress.min(depsto.getQte());
            log.debug(" removing {} from depsto {} ", qteToRmv, depsto);
//            BigDecimal qtePreleveeDepsto = qteToRedress.min(depsto.getQte());
            depsto.setQte(depsto.getQte().subtract(qteToRmv));
            qteToRedress = qteToRedress.subtract(qteToRmv);
            
            DetailMvtStoBE detailMvtStoBE = new DetailMvtStoBE(depsto, mvtstoBEToRemove);
            detailMvtStoBE.setQuantitePrelevee(qteToRmv);
            detailMvtStoBE.setTauxTva(depsto.getTauxTva());
            detailMvtStoBE.setCodeTva(depsto.getCodeTva());
            detailMvtStoBE.setQuantiteDisponible(depsto.getQte());
            detailMvtStoBE.setCodeDepsto(depsto.getCode());
            detailMvtStoBE.setNumbon(depsto.getNumBon());
            detailMvtStoBE.setCodeMvtSto(mvtstoBEToRemove.getCode());
            detailMvtStoBE.setPriuni(depsto.getPu());
            detailMvtStoBE.setUnite(depsto.getUnite());
            detailMvtStoBE.setDatPer(depsto.getDatPer());
            detailMvtStoBE.setLotinter(depsto.getLotInter());
            listeDetailMvtstoBE.add(detailMvtStoBE);

            //
            priuni = priuni.add(depsto.getPu().multiply(qteToRmv));
            
            Depsto depstoToAdd = new Depsto(depsto);
            depstoToAdd.setQte(qteToRmv);
            depstoToAdd.setPu(depsto.getPu());
            depstoToAdd.setNumBon(numBon);
            depstoToAdd.setNumBonOrigin(depsto.getNumBonOrigin());
            depstoToAdd.setLotInter(null);
            
            newStock.add(depstoToAdd);
//                    depstoToAdd.setLotInter(mvtstoBEWithPositifQuantity.getLotInter());
//                    depstoToAdd.setDatPer(mvtstoBEWithPositifQuantity.getDatPer());
//                    mvtstoBEWithPositifQuantity.setQuantite(mvtstoBEWithPositifQuantity.getQuantite().subtract(qteMin));
//                    qtePreleveeDepsto = qtePreleveeDepsto.subtract(qteToRmv);
//                    newStock.add(depstoToAdd);
//                for (MvtStoBEDTO mvtstoBEWithPositifQuantity : listeMvtstoBEGroupesWithPositifQuantity) {
//                    if (mvtstoBEWithPositifQuantity.getQuantite().compareTo(BigDecimal.ZERO) == 0) {
//                        continue;
//                    }
//
//                    BigDecimal qteMin = (mvtstoBEWithPositifQuantity.getQuantite()).min(qteToRmv);
//                    Depsto depstoToAdd = new Depsto(depsto);
//                    depstoToAdd.setQte(qteMin);
//                    depstoToAdd.setPu(depsto.getPu());
//                    depstoToAdd.setNumBon(numBon);
//                    depstoToAdd.setLotInter(mvtstoBEWithPositifQuantity.getLotInter());
//                    depstoToAdd.setDatPer(mvtstoBEWithPositifQuantity.getDatPer());
//                    mvtstoBEWithPositifQuantity.setQuantite(mvtstoBEWithPositifQuantity.getQuantite().subtract(qteMin));
//                    qtePreleveeDepsto = qtePreleveeDepsto.subtract(qteToRmv);
//                    newStock.add(depstoToAdd);
//
//                    if (qtePreleveeDepsto.compareTo(BigDecimal.ZERO) == 0) {
//                        break;
//                    }
//                }
            if (qteToRedress.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }
        }
        
        mvtstoBEToRemove.setDetailMvtStoBEList(listeDetailMvtstoBE);
        mvtstoBEToRemove.setPriuni(priuni.divide(mvtstoBEToRemove.getQuantite().abs(), 2, RoundingMode.CEILING));
        
        return newStock;
    }

    /**
     *
     * @param <T>
     * @param depstos
     * @param detailFacture
     * @param clazz
     * @param strict (prend valeur true si depot destinataire pour les articles
     * perimee)
     * @return
     */
    public <T extends DetailMvtSto> List<DetailMvtSto> GestionDetailFacture(List<Depsto> depstos, MvtSto detailFacture, Class<T> clazz, boolean strict) {
        
        List<DetailMvtSto> detailsMvtSto = new ArrayList<>();
        
        BigDecimal qteTransferd = detailFacture.getQuantite().abs(); // abs for negative decimals

        log.debug("get codart detailFacture **************************{}", detailFacture.getCodart());
        log.debug("  depstoo.size[]**************************{}", depstos.size());
        Integer availableQteInStock = depstos.stream()
                .filter(item
                        -> strict ? (item.getCodart().equals(detailFacture.getCodart()) && item.getUnite().equals(detailFacture.getUnite()) && item.getLotInter().equals(detailFacture.getLotinter()) && item.getDatPer().equals(detailFacture.getDatPer()))
                        : (item.getCodart().equals(detailFacture.getCodart()) && item.getUnite().equals(detailFacture.getUnite())))
                .map(elt -> elt.getQte().intValue())
                .collect(Collectors.summingInt(Integer::new));
        log.debug("************availableQteInStock**************{}", availableQteInStock);
        log.debug("*************qteTransferd.intValue()**************{}", qteTransferd.intValue());
        com.csys.util.Preconditions.checkBusinessLogique(availableQteInStock >= qteTransferd.intValue(), "insuffisant-qte", detailFacture.getCodeSaisi(), detailFacture.getDesart(), availableQteInStock.toString());
        
        BigDecimal priuni = BigDecimal.ZERO;
        BigDecimal qteRemoved = BigDecimal.ZERO;
        
        List<Depsto> firstCandidatsDepstos = depstos.stream()
                .filter(item -> item.getCodart().equals(detailFacture.getCodart()) && item.getDatPer().equals(detailFacture.getDatPer())
                && item.getLotInter().equals(detailFacture.getLotinter()) && item.getUnite().equals(detailFacture.getUnite())
                ).collect(toList());
        
        log.debug("*************firstCandidatsDepstos**************{}", firstCandidatsDepstos);
        for (Depsto firstCandidDeps : firstCandidatsDepstos) {
            log.debug("*************firstCandidDeps**************{}", firstCandidDeps);
            try {
                BigDecimal qteToRmv = qteTransferd.min(firstCandidDeps.getQte());
                
                log.debug("*************qteToRmv**************{}", qteToRmv);
                DetailMvtSto detailMvtSto = clazz.newInstance();
                
                detailMvtSto.setPk(detailFacture.getCode(), firstCandidDeps);
                detailMvtSto.setCodeMvtSto(detailFacture.getCode());
                
                detailMvtSto.setQuantitePrelevee(qteToRmv);

                //set tva
                detailMvtSto.setCodeTva(firstCandidDeps.getCodeTva());
                detailMvtSto.setTauxTva(firstCandidDeps.getTauxTva());
                detailMvtSto.setNumBonOrigin(firstCandidDeps.getNumBonOrigin());
                detailsMvtSto.add(detailMvtSto);
                firstCandidDeps.setQte(firstCandidDeps.getQte().subtract(qteToRmv));
                qteTransferd = qteTransferd.subtract(qteToRmv);
                
                priuni = priuni.add(firstCandidDeps.getPu().multiply(qteToRmv));
                qteRemoved = qteRemoved.add(qteToRmv);
                log.debug(" removing {} from depsto {} ", qteToRmv, firstCandidDeps);
                if (qteTransferd.compareTo(BigDecimal.ZERO) == 0) {
                    
                    break;
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(StockService.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (!detailFacture.getCategDepot().equals(CategorieDepotEnum.EC)) {
            if (qteTransferd.compareTo(BigDecimal.ZERO) == 0) {
                BigDecimal priuniArt = priuni.divide(qteRemoved, 2, RoundingMode.CEILING);
                detailFacture.setPriuni(priuniArt);
                
                return detailsMvtSto;
            }
            
        }
        
        List<Depsto> secondCandidatsDepstos = depstos.stream()
                .filter(item -> item.getCodart().equals(detailFacture.getCodart()) && item.getUnite().equals(detailFacture.getUnite())
                && item.getQte().compareTo(BigDecimal.ZERO) > 0).collect(toList());
        
        for (Depsto secondCandidDeps : secondCandidatsDepstos) {
            
            try {
                BigDecimal qteToRmv = qteTransferd.min(secondCandidDeps.getQte());
                
                DetailMvtSto detailMvtSto = clazz.newInstance();
                detailMvtSto.setPk(detailFacture.getCode(), secondCandidDeps);
                detailMvtSto.setQuantitePrelevee(qteToRmv);

                //set tva
                detailMvtSto.setCodeTva(secondCandidDeps.getCodeTva());
                detailMvtSto.setTauxTva(secondCandidDeps.getTauxTva());
                detailMvtSto.setNumBonOrigin(secondCandidDeps.getNumBonOrigin());
                
                detailsMvtSto.add(detailMvtSto);
                secondCandidDeps.setQte(secondCandidDeps.getQte().subtract(qteToRmv));
                qteTransferd = qteTransferd.subtract(qteToRmv);
                priuni = priuni.add(secondCandidDeps.getPu().multiply(qteToRmv));
                qteRemoved = qteRemoved.add(qteToRmv);
                log.debug("******{}", qteRemoved);
                if (qteTransferd.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(StockService.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        log.debug("*** End of treating article {} ****", detailFacture);
        if (!detailFacture.getCategDepot().equals(CategorieDepotEnum.EC)) {
            
            detailFacture.setPriuni(priuni.divide(qteRemoved, 2, RoundingMode.CEILING));
            
        }
        return detailsMvtSto;
    }
    
    public <T extends DetailMvtSto> List<DetailMvtSto> GestionDetailFactureImmo(List<Depsto> depstos, MvtStoPR detailFacture, Class<T> clazz, boolean strict) {
        
        List<DetailMvtSto> detailsMvtSto = new ArrayList<>();
        
        BigDecimal qteTransferd = detailFacture.getQuantite().abs(); // abs for negative decimals

        log.debug("get codart detailFacture **************************{}", detailFacture.getCodart());
        log.debug("  depstoo.size[]**************************{}", depstos.size());
        Integer availableQteInStock = depstos.stream()
                .filter(item
                        -> strict ? (item.getCodart().equals(detailFacture.getCodart()) && item.getUnite().equals(detailFacture.getUnite()) && detailFacture.getCodeEmplacement().equals(item.getCodeEmplacement()))
                        : (item.getCodart().equals(detailFacture.getCodart()) && item.getUnite().equals(detailFacture.getUnite())))
                .map(elt -> elt.getQte().intValue())
                .collect(Collectors.summingInt(Integer::new));
        log.debug("************availableQteInStock**************{}", availableQteInStock);
        log.debug("*************qteTransferd.intValue()**************{}", qteTransferd.intValue());
        com.csys.util.Preconditions.checkBusinessLogique(availableQteInStock >= qteTransferd.intValue(), "insuffisant-qte", detailFacture.getCodeSaisi(), availableQteInStock.toString());
        
        BigDecimal priuni = BigDecimal.ZERO;
        BigDecimal qteRemoved = BigDecimal.ZERO;
        //mm NumeroSerieet mm Emplacement 
        List<Depsto> firstCandidatsDepstos = depstos.stream()
                .filter(item -> item.getCodart().equals(detailFacture.getCodart()) && item.getCodeEmplacement() != null
                && item.getCodeEmplacement().equals(detailFacture.getCodeEmplacement())
                && item.getLotInter().equals(detailFacture.getLotinter())
                && item.getUnite().equals(detailFacture.getUnite())
                ).collect(toList());
        
        log.debug("*************firstCandidatsDepstos**************{}", firstCandidatsDepstos);
        for (Depsto firstCandidDeps : firstCandidatsDepstos) {
            log.debug("*************firstCandidDeps**************{}", firstCandidDeps);
            try {
                BigDecimal qteToRmv = qteTransferd.min(firstCandidDeps.getQte());
                
                log.debug("*************qteToRmv**************{}", qteToRmv);
                DetailMvtSto detailMvtSto = clazz.newInstance();
                
                detailMvtSto.setPk(detailFacture.getCode(), firstCandidDeps);
                detailMvtSto.setCodeMvtSto(detailFacture.getCode());
                
                detailMvtSto.setQuantitePrelevee(qteToRmv);

                //set tva
                detailMvtSto.setCodeTva(firstCandidDeps.getCodeTva());
                detailMvtSto.setTauxTva(firstCandidDeps.getTauxTva());
                detailMvtSto.setNumBonOrigin(firstCandidDeps.getNumBonOrigin());
                
                detailMvtSto.setIsCapitalize(firstCandidDeps.getIsCapitalize());
                log.debug("detailMvtSto 1.is capitalise est {}", detailMvtSto.getIsCapitalize());
                detailsMvtSto.add(detailMvtSto);
                firstCandidDeps.setQte(firstCandidDeps.getQte().subtract(qteToRmv));
                qteTransferd = qteTransferd.subtract(qteToRmv);
                
                priuni = priuni.add(firstCandidDeps.getPu().multiply(qteToRmv));
                qteRemoved = qteRemoved.add(qteToRmv);
                log.debug(" removing {} from depsto {} ", qteToRmv, firstCandidDeps);
                if (qteTransferd.compareTo(BigDecimal.ZERO) == 0) {
                    
                    break;
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(StockService.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (qteTransferd.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal priuniArt = priuni.divide(qteRemoved, 2, RoundingMode.CEILING);
            detailFacture.setPriuni(priuniArt);
            return detailsMvtSto;
        }
        //mm NumeroSerie(lot)
        List<Depsto> secondCandidatsDepstos = depstos.stream()
                .filter(item -> item.getCodart().equals(detailFacture.getCodart())
                && item.getUnite().equals(detailFacture.getUnite())
                && item.getLotInter().equals(detailFacture.getLotinter())
                && item.getQte().compareTo(BigDecimal.ZERO) > 0).collect(toList());
        
        for (Depsto secondCandidDeps : secondCandidatsDepstos) {
            
            try {
                BigDecimal qteToRmv = qteTransferd.min(secondCandidDeps.getQte());
                
                DetailMvtSto detailMvtSto = clazz.newInstance();
                detailMvtSto.setPk(detailFacture.getCode(), secondCandidDeps);
                detailMvtSto.setQuantitePrelevee(qteToRmv);

                //set tva
                detailMvtSto.setCodeTva(secondCandidDeps.getCodeTva());
                detailMvtSto.setTauxTva(secondCandidDeps.getTauxTva());
                detailMvtSto.setNumBonOrigin(secondCandidDeps.getNumBonOrigin());
                detailMvtSto.setIsCapitalize(secondCandidDeps.getIsCapitalize());
                
                log.debug("detailMvtSto 2.is capitalise est {}", detailMvtSto.getIsCapitalize());
                detailsMvtSto.add(detailMvtSto);
                secondCandidDeps.setQte(secondCandidDeps.getQte().subtract(qteToRmv));
                qteTransferd = qteTransferd.subtract(qteToRmv);
                priuni = priuni.add(secondCandidDeps.getPu().multiply(qteToRmv));
                qteRemoved = qteRemoved.add(qteToRmv);
                log.debug("******{}", qteRemoved);
                if (qteTransferd.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(StockService.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (qteTransferd.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal priuniArt = priuni.divide(qteRemoved, 2, RoundingMode.CEILING);
            detailFacture.setPriuni(priuniArt);
            return detailsMvtSto;
        }
        //mm Emplacement 
        List<Depsto> thirdCandidatsDepstos = depstos.stream()
                .filter(item -> item.getCodart().equals(detailFacture.getCodart()) && item.getUnite().equals(detailFacture.getUnite())
                && item.getCodeEmplacement() != null
                && item.getCodeEmplacement().equals(detailFacture.getCodeEmplacement())
                && item.getQte().compareTo(BigDecimal.ZERO) > 0).collect(toList());
        
        for (Depsto thirdCandidDeps : thirdCandidatsDepstos) {
            
            try {
                BigDecimal qteToRmv = qteTransferd.min(thirdCandidDeps.getQte());
                
                DetailMvtSto detailMvtSto = clazz.newInstance();
                detailMvtSto.setPk(detailFacture.getCode(), thirdCandidDeps);
                detailMvtSto.setQuantitePrelevee(qteToRmv);

                //set tva
                detailMvtSto.setCodeTva(thirdCandidDeps.getCodeTva());
                detailMvtSto.setTauxTva(thirdCandidDeps.getTauxTva());
                detailMvtSto.setNumBonOrigin(thirdCandidDeps.getNumBonOrigin());
                detailMvtSto.setIsCapitalize(thirdCandidDeps.getIsCapitalize());
                
                log.debug("detailMvtSto 3.is capitalise est {}", detailMvtSto.getIsCapitalize());
                detailsMvtSto.add(detailMvtSto);
                thirdCandidDeps.setQte(thirdCandidDeps.getQte().subtract(qteToRmv));
                qteTransferd = qteTransferd.subtract(qteToRmv);
                priuni = priuni.add(thirdCandidDeps.getPu().multiply(qteToRmv));
                qteRemoved = qteRemoved.add(qteToRmv);
                log.debug("******{}", qteRemoved);
                if (qteTransferd.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(StockService.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (qteTransferd.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal priuniArt = priuni.divide(qteRemoved, 2, RoundingMode.CEILING);
            detailFacture.setPriuni(priuniArt);
            return detailsMvtSto;
        }
        
        List<Depsto> fourthCandidatsDepstos = depstos.stream()
                .filter(item -> item.getCodart().equals(detailFacture.getCodart())
                && item.getUnite().equals(detailFacture.getUnite())
                && item.getQte().compareTo(BigDecimal.ZERO) > 0).collect(toList());
        
        for (Depsto fourthCandidDeps : fourthCandidatsDepstos) {
            
            try {
                BigDecimal qteToRmv = qteTransferd.min(fourthCandidDeps.getQte());
                
                DetailMvtSto detailMvtSto = clazz.newInstance();
                detailMvtSto.setPk(detailFacture.getCode(), fourthCandidDeps);
                detailMvtSto.setQuantitePrelevee(qteToRmv);
                detailMvtSto.setIsCapitalize(fourthCandidDeps.getIsCapitalize());
                log.debug("detailMvtSto 4.is capitalise est {}", detailMvtSto.getIsCapitalize());
                //set tva
                detailMvtSto.setCodeTva(fourthCandidDeps.getCodeTva());
                detailMvtSto.setTauxTva(fourthCandidDeps.getTauxTva());
                detailMvtSto.setNumBonOrigin(fourthCandidDeps.getNumBonOrigin());
                
                detailsMvtSto.add(detailMvtSto);
                fourthCandidDeps.setQte(fourthCandidDeps.getQte().subtract(qteToRmv));
                qteTransferd = qteTransferd.subtract(qteToRmv);
                priuni = priuni.add(fourthCandidDeps.getPu().multiply(qteToRmv));
                qteRemoved = qteRemoved.add(qteToRmv);
                log.debug("******{}", qteRemoved);
                if (qteTransferd.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            } catch (InstantiationException | IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(StockService.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        detailFacture.setPriuni(priuni.divide(qteRemoved, 2, RoundingMode.CEILING));
        log.debug("*** End of treating article {} ****", detailFacture);
        return detailsMvtSto;
    }
    
    public List<DepstoEditionValeurStockDTO> findEcartStock(CategorieDepotEnum categDepot, Integer coddep) {
        log.debug("REST request to findEcartStock ");
        
        List<Depsto> depstos = depstorepository.findByCoddepAndQteGreaterThanAndCategDepot(coddep, BigDecimal.ZERO, categDepot);
        Set<Integer> codesArticles = depstos.stream().map(item -> item.getCodart()).collect(Collectors.toSet());
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(coddep);
        List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(categDepot, codesArticles.stream().toArray(Integer[]::new));
        List<ArticleUniteDTO> articlePHunits = categDepot.equals(CategorieDepotEnum.PH) ? articles.stream()
                .flatMap(articlePH -> ((ArticlePHDTO) articlePH).getArticleUnites()
                .stream()).collect(toList()) : new ArrayList();
        log.debug("les articlesPHUnites sont {}", articlePHunits);
        
        Depsto depstoIdentity = new Depsto(BigDecimal.ZERO);
        Collection< Depsto> resultedDepstoAfterGrouping = depstos.stream() //groupement des depstos par code article avec calcul des quantites
                .collect(groupingBy(Depsto::getCodart,
                        Collectors.reducing(depstoIdentity, (a, b) -> {
                            if (categDepot.equals(CategorieDepotEnum.PH)) {
                                BigDecimal nbrePiece = articlePHunits.stream()
                                        .filter(elt -> elt.getCodeUnite().equals(b.getUnite()) && elt.getCodeArticle().equals(b.getCodart()))
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(b.getCodart().toString())))
                                        .getNbPiece();
                                b.setQte(b.getQte().divide(nbrePiece, 5, RoundingMode.CEILING));
                            }
                            b.setQte(b.getQte().add(a.getQte()));
                            return b;
                        })))
                .values();
        log.debug("resultedDepstoAfterGrouping sont {}", resultedDepstoAfterGrouping);
        
        ArticleDepotFixeDTO articleDepotDTOIdentity = new ArticleDepotFixeDTO(BigDecimal.ZERO);
        List<ArticleDepotFixeDTO> listeArticleDepotGrouped = new ArrayList();
        List<DepstoEditionValeurStockDTO> resultedList = new ArrayList();
        
        List<ArticleDepotFixeDTO> listeArticleDepot = paramAchatServiceClient.findArticleDepotbyCodeDepotAndCategDepot(coddep, categDepot);
        
        Collection<ArticleDepotFixeDTO> articleDepotsSansDepstos = listeArticleDepot.stream().filter(ad -> !codesArticles.contains(ad.getCodeArticle())).collect(Collectors.toList());
        
        Set<Integer> codesArticlesSansDepstos = new HashSet();
        List<Depsto> resultedDepstos = new ArrayList(resultedDepstoAfterGrouping);

        //creation de depsto pour les articles depots qui n ont pas de depsto
        articleDepotsSansDepstos.forEach(elt -> {
            if (!codesArticlesSansDepstos.contains(elt.getCodeArticle())) // ctrl pour eliminer les doublons:creer un seul depsto pour chaque article 
            {
                codesArticlesSansDepstos.add(elt.getCodeArticle());
                Depsto depsto = new Depsto(BigDecimal.ZERO);
                depsto.setCodart(elt.getCodeArticle());
                Date date = new Date(3000, 01, 06);
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                depsto.setDatPer(localDate);
                resultedDepstos.add(depsto);
            }
        });
        log.debug("resultedDepstos est {}", resultedDepstos);
        List<ArticleDTO> articlesToAdd = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(categDepot, codesArticlesSansDepstos.stream().toArray(Integer[]::new));
        articles.addAll(articlesToAdd);
        
        if (categDepot.equals(CategorieDepotEnum.PH)) {
            List<ArticleUniteDTO> newArticlesPH = articlesToAdd.stream()
                    .flatMap(articlePH -> ((ArticlePHDTO) articlePH).getArticleUnites()
                    .stream()).collect(toList());
            articlePHunits.addAll(newArticlesPH);
        }
        
        listeArticleDepot.stream()
                .collect(groupingBy(ArticleDepotFixeDTO::getCodeArticle,
                        (groupingBy(ArticleDepotFixeDTO::getCodeDepot,
                                Collectors.reducing(articleDepotDTOIdentity, (a, b) -> {
                                    if (categDepot.equals(CategorieDepotEnum.PH)) {
                                        BigDecimal nbrePiece = articlePHunits.stream()
                                                .filter(elt -> elt.getCodeUnite().equals(b.getCodeUnite()) && elt.getCodeArticle().equals(b.getCodeArticle()))
                                                .findFirst()
                                                .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(b.getCodeArticle().toString() + b.getCodeUnite().toString())))
                                                .getNbPiece();
                                        b.setStockFixe(b.getStockFixe().divide(nbrePiece, 5, RoundingMode.CEILING));
                                        
                                    }
                                    
                                    b.setStockFixe(b.getStockFixe().add(a.getStockFixe()));
                                    return b;
                                })))))
                .forEach((k, v) -> {
                    Collection<ArticleDepotFixeDTO> articlesDepots = v.values();
                    listeArticleDepotGrouped.addAll(articlesDepots);
                    
                });
        
        resultedDepstos.forEach(depsto -> {
            log.debug("depsto a iterer est {}", depsto);
            ArticleDepotFixeDTO articleDepotCorrespondant = listeArticleDepotGrouped
                    .stream()
                    .filter(articleDepot -> articleDepot.getCodeArticle().equals(depsto.getCodart()))
                    .findFirst()
                    .orElseGet(() -> new ArticleDepotFixeDTO(null));
//                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-depot", new Throwable(articleDepot.getCodeArticle().toString())));
            log.debug("articleDepotCorrespondant est {}", articleDepotCorrespondant);
            //                 a verifier
            ArticleDTO articleCorrespondant = articles
                    .stream()
                    .filter(article -> article.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(depsto.getCodart().toString())));
            
            DepstoEditionValeurStockDTO depstoDTO = DepstoDTOAssembler.assembleDepstoEditionValeurStockDTO(depsto, articleCorrespondant, null, depot);
            depstoDTO.setLot(null);
            depstoDTO.setCodtva(null);
            depstoDTO.setTautva(null);
            depstoDTO.setDatPer(null);
            depstoDTO.setPu(null);
            depstoDTO.setQteFixeDepot(articleDepotCorrespondant.getStockFixe());
            resultedList.add(depstoDTO);
            
        });
        
        return resultedList;
        
    }
    
    @Transactional(readOnly = true)
    public List<RotationStockDTO> rotationStock(CategorieDepotEnum categ, Integer categorieArticle, LocalDateTime fromDate, LocalDateTime toDate) {
        
        List<RotationStockDTO> rotationStockDTOs = new ArrayList<>();
        
        List<ArticleDTO> articleDTOs = paramAchatServiceClient.articleByCategArt(categorieArticle);
        Integer[] articleIds = articleDTOs.stream().map(ArticleDTO::getCode).toArray(Integer[]::new);
        Preconditions.checkBusinessLogique(articleIds.length > 0, "pharmacie.categ.item.empty");
//        List<Depsto> depstos = depstorepository.findByCategDepotAndCodartIn(categ, Arrays.asList(articleIds));
        List<CategorieDepotEnum> categorieDepotEnums = Arrays.asList(categ);
        List<Depsto> depstos = findAll(null, Arrays.asList(articleIds), true, null, true, categorieDepotEnums);

//        log.debug("depstos avant grouppement par unite principal {}", depstos.toString());
        Preconditions.checkBusinessLogique(!depstos.isEmpty(), "pharmacie.depsto.empty");
        List<ArticleUniteDTO> articlePHunits = categ.equals(CategorieDepotEnum.PH)
                ? paramAchatServiceClient.articlePHFindbyListCode(Arrays.asList(articleIds))
                        .stream().flatMap(articlePH -> articlePH.getArticleUnites().stream()).collect(toList())
                : new ArrayList();
        
        Depsto depstoIdentity = new Depsto(BigDecimal.ZERO);
        Collection<Depsto> newDepsto = depstos.stream()
                .collect(groupingBy(Depsto::getCodart,
                        Collectors.reducing(depstoIdentity, (a, b) -> {
                            if (categ.equals(CategorieDepotEnum.PH)) {
                                BigDecimal nbrePiece = articlePHunits.stream()
                                        .filter(elt -> elt.getCodeUnite().equals(b.getUnite()) && elt.getCodeArticle().equals(b.getCodart()))
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-unity", new Throwable(b.getCodart().toString())))
                                        .getNbPiece();
                                b.setQte(b.getQte().divide(nbrePiece, 5, RoundingMode.CEILING));
                            }
                            
                            b.setQte(b.getQte().add(a.getQte()));
                            return b;
                        })))
                .values();

//        log.debug("depstos apres grouppement par unite principal {} ", newDepsto.toString());
        List<TotalMouvement> totalMouvements = editionService.consomationReels(Arrays.asList(articleIds), fromDate, toDate, categ);
//        log.debug("totalMouvements {}", totalMouvements.toString());

        newDepsto.stream().forEach(depsto -> {
            
            ArticleDTO article = articleDTOs.stream().filter(x -> x.getCode().equals(depsto.getCodart()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article-depot", new Throwable(depsto.getCodart().toString())));
            
            TotalMouvement totalMouvement = totalMouvements.stream().filter(x -> x.getCodart().equals(depsto.getCodart()))
                    .findFirst()
                    .orElse(null);
            RotationStockDTO rotationStockDTO = new RotationStockDTO();
            rotationStockDTO.setCategArticle(article.getCategorieArticle().getDesignation());
            rotationStockDTO.setCodeArticle(article.getCode());
            rotationStockDTO.setCodeSaisiArticle(article.getCodeSaisi());
            rotationStockDTO.setDesignationArticle(article.getDesignation());
            rotationStockDTO.setCodeUnite(article.getCodeUnite());
            rotationStockDTO.setDesignationUnite(article.getDesignationUnite());
            if (totalMouvement != null) {
                rotationStockDTO.setConsommationReel(totalMouvement.getQuantite());
            } else {
                rotationStockDTO.setConsommationReel(BigDecimal.ZERO);
            }
            rotationStockDTO.setStockActuel(depsto.getQte());
            rotationStockDTOs.add(rotationStockDTO);
        });
        
        return rotationStockDTOs;
        
    }
    
    @Transactional(readOnly = true)
    public List<DepstoDTO> mappingActualStockWithInventory(Integer codeDepot, MultipartFile masterDataFile) throws IOException, InvalidFormatException {
        
        List<DepstoDTO> injectDespstoDTOs = injectMasterData(codeDepot, masterDataFile);
        Set<Integer> codeArticles = injectDespstoDTOs.stream().map(d -> d.getArticleID()).collect(Collectors.toSet());
        List<Depsto> depstos = findByCodartInAndCoddepAndQteGreaterThan(new ArrayList<>(codeArticles), codeDepot, BigDecimal.ZERO);
        List<DepstoDTO> result = DepstoDTOAssembler.assembleDepstosWithActualStock(depstos, injectDespstoDTOs);
        return result;
        
    }
    
    @Transactional(readOnly = true)
    public List<DepstoDTO> injectMasterData(Integer codeDepot, MultipartFile masterDataFile) throws IOException, InvalidFormatException {
        log.debug("injectMasterDate  :{} ", masterDataFile.getName());
        
        Workbook workbook = WorkbookFactory.create(masterDataFile.getInputStream());
        Sheet worksheetStock = workbook.getSheet("stock");
        Preconditions.checkBusinessLogique(worksheetStock != null, "");
        
        Row headerStock = worksheetStock.getRow(0);
        Integer indexCodeDepot;
        Integer indexDesignationDepot;
        Integer indexCodeArticle;
        Integer indexCodeSaisiArticle;
        Integer indexDesignationArticle;
        Integer indexCodeUnite;
        Integer indexDesignationUnite;
        Integer indexQuantite;
        
        List<Cell> headerCellsStock = StreamSupport.stream(headerStock.spliterator(), false).collect(Collectors.toList());
        
        List<Cell> headerCodeDepot = headerCellsStock.stream().filter(f -> f.getStringCellValue().equals("code_depot")).collect(Collectors.toList());
        Preconditions.checkBusinessLogique(headerCodeDepot.size() == 1, "inject-code-depot");
        indexCodeDepot = headerCodeDepot.get(0).getColumnIndex();
        
        List<Cell> headerDesignationDepot = headerCellsStock.stream().filter(f -> f.getStringCellValue().equals("designation_depot")).collect(Collectors.toList());
        Preconditions.checkBusinessLogique(headerDesignationDepot.size() == 1, "inject-designation-depot");
        indexDesignationDepot = headerDesignationDepot.get(0).getColumnIndex();
        
        List<Cell> headerCodeArticle = headerCellsStock.stream().filter(f -> f.getStringCellValue().equals("code_article")).collect(Collectors.toList());
        Preconditions.checkBusinessLogique(headerCodeArticle.size() == 1, "inject-code-article");
        indexCodeArticle = headerCodeArticle.get(0).getColumnIndex();
        
        List<Cell> headerCodeSaisiArticle = headerCellsStock.stream().filter(f -> f.getStringCellValue().equals("code_saisi_article")).collect(Collectors.toList());
        Preconditions.checkBusinessLogique(headerCodeSaisiArticle.size() == 1, "inject-code-saisi-article");
        indexCodeSaisiArticle = headerCodeSaisiArticle.get(0).getColumnIndex();
        
        List<Cell> headerDesignationArticle = headerCellsStock.stream().filter(f -> f.getStringCellValue().equals("designation_article")).collect(Collectors.toList());
        Preconditions.checkBusinessLogique(headerDesignationArticle.size() == 1, "inject-designation-article");
        indexDesignationArticle = headerDesignationArticle.get(0).getColumnIndex();
        
        List<Cell> headerCodeUnite = headerCellsStock.stream().filter(f -> f.getStringCellValue().equals("code_unite")).collect(Collectors.toList());
        Preconditions.checkBusinessLogique(headerCodeUnite.size() == 1, "inject-code-unite");
        indexCodeUnite = headerCodeUnite.get(0).getColumnIndex();
        
        List<Cell> headerDesignationUnite = headerCellsStock.stream().filter(f -> f.getStringCellValue().equals("designation_unite")).collect(Collectors.toList());
        Preconditions.checkBusinessLogique(headerDesignationUnite.size() == 1, "inject-designation-unite");
        indexDesignationUnite = headerDesignationUnite.get(0).getColumnIndex();
        
        List<Cell> headerCodeQuantite = headerCellsStock.stream().filter(f -> f.getStringCellValue().equals("quantite")).collect(Collectors.toList());
        Preconditions.checkBusinessLogique(headerCodeQuantite.size() == 1, "inject-quantite");
        indexQuantite = headerCodeQuantite.get(0).getColumnIndex();
        
        List<DepstoDTO> injectDespstoDTOs = new ArrayList<>();
        List<Row> rowsStock = StreamSupport.stream(worksheetStock.spliterator(), false).collect(Collectors.toList());
        rowsStock.remove(headerStock);
        
        for (Row row : rowsStock) {
            Cell cellCodeDepot = row.getCell(indexCodeDepot);
            Cell cellDesignationDepot = row.getCell(indexDesignationDepot);
            Cell cellCodeArticle = row.getCell(indexCodeArticle);
            Cell cellCodeSaisiArticle = row.getCell(indexCodeSaisiArticle);
            Cell cellDesignationArticle = row.getCell(indexDesignationArticle);
            Cell cellDesignationUnite = row.getCell(indexDesignationUnite);
            Cell cellCodeUnite = row.getCell(indexCodeUnite);
            
            Cell cellQuantite = row.getCell(indexQuantite);
            Preconditions.checkBusinessLogique(cellQuantite.getCellType() == CellType.NUMERIC, "inject-quantite-must-be-numeric");
            /*on traite par code depot*/
            if (codeDepot.equals((int) cellCodeDepot.getNumericCellValue())) {
                DepstoDTO injectDepstoDTO = new DepstoDTO();
                injectDepstoDTO.setCodeDepot((int) cellCodeDepot.getNumericCellValue());
                injectDepstoDTO.setDessignationDepot(cellDesignationDepot.getStringCellValue());
                injectDepstoDTO.setArticleID((int) cellCodeArticle.getNumericCellValue());
                injectDepstoDTO.setCodeSaisiArticle(cellCodeSaisiArticle.getStringCellValue());
                injectDepstoDTO.setDesignation(cellDesignationArticle.getStringCellValue());
                injectDepstoDTO.setUnityCode((int) cellCodeUnite.getNumericCellValue());
                injectDepstoDTO.setUnityDesignation(cellDesignationUnite.getStringCellValue());
                injectDepstoDTO.setQuantityOfOldStock(new BigDecimal(cellQuantite.getNumericCellValue()));
                injectDespstoDTOs.add(injectDepstoDTO);
            }
        }
        
        Long countingListInjectDepstoGroupped = injectDespstoDTOs.stream()
                .collect(Collectors.groupingBy(p -> Pair.of(p.getArticleID(), p.getUnityCode())))
                .entrySet().stream()
                .map(x -> x.getValue()).collect(Collectors.counting());
        Preconditions.checkBusinessLogique(countingListInjectDepstoGroupped.compareTo(Long.valueOf(injectDespstoDTOs.size())) == 0, "inject-depsto-duplicated-item-with-same-unity");
        
        log.debug("injectDespstoDTOs : {}", injectDespstoDTOs);
        return injectDespstoDTOs;
    }
    
    @Transactional
    public void updateDatePerDepsto(List<DepstoDTO> depstoDTOs) {
        List<Depsto> depstos = depstorepository.findByCodeIn(depstoDTOs.stream().map(d -> d.getCode()).collect(Collectors.toList()));
        depstoDTOs.forEach(depstoDTO -> {
            depstos.stream()
                    .filter(depsto -> depsto.getCode().equals(depstoDTO.getCode()))
                    .map(depsto -> {
                        depsto.setDatPer(depstoDTO.getPreemptionDate());
                        return depsto;
                    })
                    .collect(Collectors.toList());
        });
    }
    
    @Transactional
    public Boolean updatePrixAchatDepstoPourDemarrage(CategorieDepotEnum categorieDepot) {
        if (categorieDepot.equals(CategorieDepotEnum.PH)) {
            depstorepository.updatePrixAchatDepstoPourDemarragePH(CategorieDepotEnum.PH.categ());
        } else {
            depstorepository.updatePrixAchatDepstoPourDemarrage(categorieDepot.categ());
        }
        return Boolean.TRUE;
    }
    
    @Transactional
    public Boolean updatePrixAchatDepstoPourDemarrageByArticleIn(CategorieDepotEnum categorieDepot, List<Integer> articleIDs) {
        if (articleIDs.isEmpty()) {
            return Boolean.TRUE;
        }
        if (categorieDepot.equals(CategorieDepotEnum.PH)) {
            // articleID.add(76355) ;
            depstorepository.updatePrixAchatDepstoPourDemarrageByPHArticleIn(CategorieDepotEnum.PH.categ(), articleIDs);
        } else {
            depstorepository.updatePrixAchatDepstoPourDemarrageByArticleIn(categorieDepot.categ(), articleIDs);
        }
        return Boolean.TRUE;
    }
//    @Transactional
//    public Boolean updateDepstoWithPMPAfterGoLive(CategorieDepotEnum categorieDepot) {
//        depstorepository.updateDepstoWithPMPAfterGoLive(categorieDepot.categ());
//        return Boolean.TRUE;
//    }
//
//    
//        @Transactional
//    public Boolean updatePrixAchatDepstoApresDemarrage( CategorieDepotEnum categorieDepot,List<Integer> articleIDs) {
//        if (categorieDepot.equals(CategorieDepotEnum.PH)) {
//            depstorepository.updatePrixAchatDepstoApresDemarragePH(articleIDs);
//        } else {
//            depstorepository.updatePrixAchatDepstoApresDemarrage(categorieDepot.categ());
//        }
//        return Boolean.TRUE;
//    }
// 

    public List<IsArticleQteAvailable> findTotalQuantitiesInStockByItemIdsIn(List<Integer> articleIds, Boolean detailedDepot, Boolean withQteSaisiInInventory) {
        
        if (articleIds.size() > 0) {
            
            List<ArticleStockProjection> listeArticleStockProjection = new ArrayList();
            Integer numberOfChunks = (int) Math.ceil((double) articleIds.size() / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> articleIDChunk = articleIds.subList(i * 2000, Math.min(i * 2000 + 2000, articleIds.size()));
                if (Boolean.TRUE.equals(detailedDepot)) {
                    if (Boolean.TRUE.equals(withQteSaisiInInventory)) {
                        listeArticleStockProjection.addAll(findSumQteAndQte0AndStkreelByCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot(articleIDChunk));
                    } else {
                        listeArticleStockProjection.addAll(findByCodeArticleInGrouppedByCodeArticleAndCodeUniteAndCodeDepot(articleIDChunk));
                    }
                } else {
                    if (Boolean.TRUE.equals(withQteSaisiInInventory)) {
                    } else {
                        listeArticleStockProjection.addAll(findByCodeDepotAndCodeArticleInGrouppedByCodeArticleAndCodeUnite(null, articleIDChunk));
                    }
                }
            }
            List<DepotDTO> depots = new ArrayList();
            List<UniteDTO> unites = new ArrayList();
            Set<Integer> listeCodeDepots = new HashSet();
            Set<Integer> listeCodeUnites = new HashSet();
            if (Boolean.TRUE.equals(detailedDepot)) {
                listeArticleStockProjection.forEach(articleStock -> {
                    listeCodeDepots.add(articleStock.getCoddep());
                    listeCodeUnites.add(articleStock.getUnite());
                });
                
                depots = paramAchatServiceClient.findDepotsByCodes(listeCodeDepots);
                unites = paramAchatServiceClient.findUnitsByCodes(listeCodeUnites);
            }
            List<IsArticleQteAvailable> result = new ArrayList();
            log.debug("ArticleStockProjection sont {} ", listeArticleStockProjection);
            for (ArticleStockProjection articleStock : listeArticleStockProjection) {
                IsArticleQteAvailable isArticleQteAvailable = new IsArticleQteAvailable();
                isArticleQteAvailable.setArticleId(articleStock.getCodart());
                isArticleQteAvailable.setUniteId(articleStock.getUnite());
                isArticleQteAvailable.setAvailableQte(articleStock.getQte());
                if (Boolean.TRUE.equals(detailedDepot)) {
                    isArticleQteAvailable.setCodeDepot(articleStock.getCoddep());
                    
                    DepotDTO matcheDepot = depots
                            .stream()
                            .filter(depot -> depot.getCode().equals(articleStock.getCoddep()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-depot" + isArticleQteAvailable.getCodeDepot()));
                    isArticleQteAvailable.setDepotDesignation(matcheDepot.getDesignation());
                    UniteDTO matchingUnit = unites
                            .stream()
                            .filter(unit -> unit.getCode().equals(articleStock.getUnite()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unite" + isArticleQteAvailable.getUniteId()));
                    isArticleQteAvailable.setUnityDesignation(matchingUnit.getDesignation());
                }
                result.add(isArticleQteAvailable);
            }
            
            return result;
        } else {
            return new ArrayList();
        }
    }
    
    public Boolean existsByCodartAndAInventorierTrueAndAndStkrelGreaterThan(Integer codeArticle) {
        return depstorepository.existsByCodartAndAInventorierTrueAndAndStkrelGreaterThan(codeArticle, BigDecimal.ZERO);
    }
}
