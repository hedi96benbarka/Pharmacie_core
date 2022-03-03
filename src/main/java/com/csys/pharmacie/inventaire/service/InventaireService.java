package com.csys.pharmacie.inventaire.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.CategorieArticleDTO;
import com.csys.pharmacie.achat.dto.CategorieDepotDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.inventaire.domain.DetailInventaire;
import com.csys.pharmacie.inventaire.domain.DetailInventairePK;
import com.csys.pharmacie.inventaire.domain.Inventaire;
import com.csys.pharmacie.inventaire.domain.TraceTransfertInventaire;
import com.csys.pharmacie.inventaire.dto.DepStoHistDTO;
import com.csys.pharmacie.inventaire.dto.DepstoScanDTO;
import com.csys.pharmacie.inventaire.dto.EtatEcartInventaire;
import com.csys.pharmacie.inventaire.dto.InitialisationInventaireDTO;
import com.csys.pharmacie.inventaire.dto.InventaireDTO;
import com.csys.pharmacie.inventaire.dto.TypeEnvoieEtatEnum;
import com.csys.pharmacie.inventaire.factory.DepstoScanFactory;
import com.csys.pharmacie.inventaire.factory.InventaireFactory;
import com.csys.pharmacie.inventaire.repository.InventaireRepository;
import com.csys.pharmacie.parametrage.entity.CompteurPharmacie;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.dto.DepstoDTO;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.repository.DepstoRepository;
import com.csys.pharmacie.stock.repository.InvDepstoService;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.transfert.service.BonTransfertService;
import com.csys.util.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Inventaire.
 */
@Service
@Transactional
public class InventaireService {

    private final Logger log = LoggerFactory.getLogger(InventaireService.class);

    @Value("${lang.secondary}")
    private String langage;

    @Autowired
    MessageSource messages;

    private final InventaireRepository inventaireRepository;
    private final StockService stockService;
    private final DepstoRepository depstorepository;
    private final ParamServiceClient parametrageService;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final InvDepstoService invDepstoService;
    private final ParamService paramService;
    private final BonTransfertService bonTransfertService;
    private final TraceTransfertInventaireService traceTransfertInventaireService;
    private final DepStoHistService depStoHistService;
    private final DepstoScanService depstoScanService;

    public InventaireService(InventaireRepository inventaireRepository, @Lazy StockService stockService, DepstoRepository depstorepository, ParamServiceClient parametrageService, ParamAchatServiceClient paramAchatServiceClient, @Lazy InvDepstoService invDepstoService, ParamService paramService, BonTransfertService bonTransfertService, TraceTransfertInventaireService traceTransfertInventaireService, DepStoHistService depStoHistService, DepstoScanService depstoScanService) {
        this.inventaireRepository = inventaireRepository;
        this.stockService = stockService;
        this.depstorepository = depstorepository;
        this.parametrageService = parametrageService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.invDepstoService = invDepstoService;
        this.paramService = paramService;
        this.bonTransfertService = bonTransfertService;
        this.traceTransfertInventaireService = traceTransfertInventaireService;
        this.depStoHistService = depStoHistService;
        this.depstoScanService = depstoScanService;
    }

    /**
     * Save a inventaireDTO.
     *
     * @param inventaireDTO
     * @return the persisted entity
     */
    public InventaireDTO save(InventaireDTO inventaireDTO) {
        log.debug("Request to save Inventaire: {}", inventaireDTO);
        Inventaire inventaire = InventaireFactory.inventaireDTOToInventaire(inventaireDTO);
        inventaire = inventaireRepository.save(inventaire);
        InventaireDTO resultDTO = InventaireFactory.inventaireToInventaireDTO(inventaire);
        return resultDTO;
    }

    /**
     * Update a inventaireDTO.
     *
     * @param inventaireDTO
     * @return the updated entity
     */
    public InventaireDTO update(InventaireDTO inventaireDTO) {
        log.debug("Request to update Inventaire: {}", inventaireDTO);
        Inventaire inBase = inventaireRepository.findOne(inventaireDTO.getCode());
        Preconditions.checkFound(inBase != null, "inventaire.NotFound");
        Inventaire inventaire = InventaireFactory.inventaireDTOToInventaire(inventaireDTO);
        inventaire = inventaireRepository.save(inventaire);
        InventaireDTO resultDTO = InventaireFactory.inventaireToInventaireDTO(inventaire);
        return resultDTO;
    }

    /**
     * Get one inventaireDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public InventaireDTO findOne(Integer id) {
        log.debug("Request to get Inventaire: {}", id);
        Inventaire inventaire = inventaireRepository.findOne(id);
        InventaireDTO dto = InventaireFactory.inventaireToInventaireDTO(inventaire);
        return dto;
    }

    /**
     * Get one inventaire by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public Inventaire findInventaire(Integer id) {
        log.debug("Request to get Inventaire: {}", id);
        Inventaire inventaire = inventaireRepository.findOne(id);
        return inventaire;
    }

    /**
     * Get all the inventaires.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<InventaireDTO> findAll() {
        log.debug("Request to get All Inventaires");
        List<Inventaire> result = inventaireRepository.findAll();
        return InventaireFactory.inventaireToInventaireDTOs(result);
    }

    /**
     * Delete inventaire by id.
     *
     * @param id the id of the entity
     */
    public void delete(Integer id) {
        log.debug("Request to delete Inventaire: {}", id);
        inventaireRepository.delete(id);
    }

    List<CategorieArticleDTO> lisCategorieArticleRecurcive(List<CategorieArticleDTO> listCategorieArticleDTO) {
        return listCategorieArticleDTO.get(0).getNodes();
    }

    public List<Integer> findCodeCategorieByDepoAndCategorieArticle(Integer coddep, CategorieDepotEnum categ_depot, Integer codCatArticle) {
        List<Integer> listCode = new ArrayList<Integer>();
        List<CategorieArticleDTO> lisCategorieArticleDTO = paramAchatServiceClient.findTreeCategoriesArticlesByCategorieDepot(categ_depot.toString(), Boolean.TRUE);

        while (lisCategorieArticleDTO != null) {
            listCode.add(lisCategorieArticleDTO.get(0).getCode());
            lisCategorieArticleDTO = lisCategorieArticleRecurcive(lisCategorieArticleDTO);
        }
        return listCode;

    }

    /**
     * @param coddep 
     * @param categ_depot 
     * @param codCatArticle 
     * @return depstos with item not stopped 
     */
    public List<DepstoDTO> getDepStoByCodeDepotAndCategorieArticleInitInventaire(Integer coddep, CategorieDepotEnum categ_depot, Integer codCatArticle) {

        List<DepstoDTO> depsto = stockService.findAllFr(coddep, categ_depot, null, true, true, false).stream().collect(toList());
        log.debug("depsto: {}", depsto);
        log.debug("depsto.size(): {}", depsto.size());
        CategorieDepotDTO categorieDepotDTO = paramAchatServiceClient.findCategorieDepot(categ_depot.toString());
        log.debug("categorieDepotDTO: {}", categorieDepotDTO);

        List<CategorieArticleDTO> lisCategorieArticle = paramAchatServiceClient.findNodesByCategorieArticleParent(codCatArticle);
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(coddep);
        com.csys.util.Preconditions.checkBusinessLogique(depot != null, "depot.findOne.messing-depot", "depot introuvable");

        //TODO: we may use the categorie-article in the depstoDTO
        List<Integer> listIdArticle = depsto.stream().map(x -> x.getArticleID()).distinct().collect(Collectors.toList());
        List<ArticleDTO> listeArticle = paramAchatServiceClient.articleFindbyListCodeWithLanguageFR(listIdArticle);
//        Set<Integer> codeArticleNotStopped = listeArticle.stream()
//                .filter(item -> !Boolean.TRUE.equals(item.getStopped()))
//                .map(item -> item.getCode()).collect(Collectors.toSet());

        List<DepstoDTO> dtosResult = depsto.stream().map(item -> {

            CategorieArticleDTO categorieArticleDTO = new CategorieArticleDTO();

            ArticleDTO articleDTO = listeArticle.stream()
                    .filter(elt -> elt.getCode().equals(item.getArticleID()))
                    .findFirst()
                    .orElse(null);

            com.csys.util.Preconditions.checkBusinessLogique(articleDTO != null, "Depot.findOne.messing-article", "article introuvable");

            if (articleDTO.getCategorieArticle() != null) {
                //TODO: we may use the article's information in the depstoDTO
                item.setDesignationSec(articleDTO.getDesignationSec());
                item.setDesignation(articleDTO.getDesignation());

                if (lisCategorieArticle.size() > 0) {
                    categorieArticleDTO = lisCategorieArticle.stream()
                            .filter(elt -> elt.getCode().equals(articleDTO.getCategorieArticle().getCode()))
                            .findFirst()
                            .orElse(null);
                }

            }
            if (categorieArticleDTO != null) {
                if (articleDTO.getCategorieArticle() != null) {
                    item.setNumCategorieArticle(articleDTO.getCategorieArticle().getCode());
                    item.setCategorieArticle(articleDTO.getCategorieArticle().getDesignation());

                    item.setCodeDepot(coddep);

                }
                item.setDessignationDepot(depot.getDesignation());
                item.setCategorieDepotDes(categorieDepotDTO.getDesignation());
                return item;
            } else {
                return null;
            }
        })
                .filter(elt -> elt != null)
                .collect(toList());
        //Ordonner la liste des ecart selon la designation
//        Collections.sort(dtosResult, (d1, d2) -> d1.getDesignation().toUpperCase().compareToIgnoreCase(d2.getDesignation().toUpperCase()));
        dtosResult.sort(Comparator.comparing(DepstoDTO::getDesignation).thenComparing(DepstoDTO::getCodeSaisiArticle).thenComparing(DepstoDTO::getUnityCode));
        return dtosResult;

    }

    @Transactional
    public InventaireDTO initialisationInventaire(InitialisationInventaireDTO initialisationInventaireDTO) {
        if ((CategorieDepotEnum.PH.equals(initialisationInventaireDTO.getCateg_depot()) || CategorieDepotEnum.UU.equals(initialisationInventaireDTO.getCateg_depot())) && Boolean.FALSE.equals(initialisationInventaireDTO.getIsDemarrage())) {
            Boolean isInjected = paramAchatServiceClient.purchasePricesIsInjected(initialisationInventaireDTO.getCateg_depot());
            Preconditions.checkBusinessLogique(Boolean.TRUE.equals(isInjected), "inventaire.after.injection.price");
        }
        Preconditions.checkBusinessLogique(!initialisationInventaireDTO.getCateg_depot().equals(CategorieDepotEnum.IMMO)
                || (initialisationInventaireDTO.getCateg_depot().equals(CategorieDepotEnum.IMMO)
                && Boolean.TRUE.equals(initialisationInventaireDTO.getIsDemarrage())), "inventaire.immo.can.only.be.demarrage");

        Boolean existTransfert = bonTransfertService.checkExistanceNotValidatedTransferts(initialisationInventaireDTO.getCoddep(), initialisationInventaireDTO.getCateg_depot());
        DepotDTO depotPrincipal = paramAchatServiceClient.findDepotPrincipalByCategorieDepot(initialisationInventaireDTO.getCateg_depot());
        Preconditions.checkBusinessLogique(!depotPrincipal.getDesignation().equals("depot.deleted"), "missing-warehouse", "missing-warehouse");

        if (!depotPrincipal.getCode().equals(initialisationInventaireDTO.getCoddep())) {
            com.csys.util.Preconditions.checkBusinessLogique(!existTransfert, "existe.transfert.non.validé");
        }

        List<Integer> lisCategorieArticleID = paramAchatServiceClient.findNodesByCategorieArticleParent(initialisationInventaireDTO.getCodCatArticle())
                .stream().map(x -> x.getCode()).distinct().collect(Collectors.toList());
        com.csys.util.Preconditions.checkBusinessLogique(lisCategorieArticleID.size() > 0, "invt.init.messing-categorieArticle", "categorie Article introuvable");
        CompteurPharmacie c = paramService.findcompteurbycode(initialisationInventaireDTO.getCateg_depot(), TypeBonEnum.IN);
        String suffixe = c.getP2();
        String preffix = c.getP1();

        // get the list of inventories closed today in the given warehouse
        List<InventaireDTO> listInventeryClosedToDay = findByDateClotureNotNullAndDateCloture(initialisationInventaireDTO.getCoddep());

        /*pour inventaire demarrage on peut pas le refaire sur les meme categ articles*/
        if (Boolean.TRUE.equals(initialisationInventaireDTO.getIsDemarrage())) {
            listInventeryClosedToDay.addAll(findByDepotAndIsDemarrage(initialisationInventaireDTO.getCoddep(), initialisationInventaireDTO.getCateg_depot()));
        }
        log.debug("listInventeryClosedToDay : {}", listInventeryClosedToDay.toString());
        // extract the ids of inventoried item's category today( with a closed inventory today )
        Set<Integer> articleCatgorieAInventaireeClosedToDay = new HashSet();
        if (!listInventeryClosedToDay.isEmpty()) {
            articleCatgorieAInventaireeClosedToDay = listInventeryClosedToDay.stream().
                    flatMap(item -> item.getDetailInventaireCollection().stream())
                    .map(item -> item.getDetailInventairePK().getCategorieArticle()).collect(toSet());
        }
        log.debug("articleCatgorieAInventaireeClosedToDay: {}", articleCatgorieAInventaireeClosedToDay);

        List<Inventaire> openInventories = inventaireRepository.findByDateClotureIsNullAndDateAnnuleIsNullAndDepot(initialisationInventaireDTO.getCoddep());
        // extract the parent of each item's category that has an open inventory in the given warehouse
        Set<Integer> listCatParentOuvert = openInventories.stream().map(item -> item.getCategorieArticleParent()).collect(toSet());
        log.debug("listCatParentOuvert: {}", listCatParentOuvert);
        com.csys.util.Preconditions.checkBusinessLogique(!listCatParentOuvert.stream().anyMatch(s -> s.equals(initialisationInventaireDTO.getCodCatArticle())), "invt.init.inventaire-ouvert", "Inventaire ouvert");

        // get the list of item's category that has an open inventory in the given warehouse
        Set<Integer> listCatOuvert = openInventories.stream().flatMap(item -> item.getDetailInventaireCollection().stream())
                .map(item -> item.getDetailInventairePK().getCategorieArticle()).collect(toSet());
        log.debug("list categories articles ouvert: {}", listCatOuvert);

        List<Integer> listarticleCatgorieAInventaireeClosedToDay = new ArrayList(articleCatgorieAInventaireeClosedToDay);
        List<DepstoDTO> depstoDTOResult = getDepStoByCodeDepotAndCategorieArticleInitInventaire(initialisationInventaireDTO.getCoddep(), initialisationInventaireDTO.getCateg_depot(), initialisationInventaireDTO.getCodCatArticle());
        log.debug("size depstos à inventorier : {}", depstoDTOResult.size());

        // Eliminate the items that have an already open inventory
        List<DepstoDTO> depstoDTO = new ArrayList<>();
        if (!depstoDTOResult.isEmpty()) {
            depstoDTO = depstoDTOResult.stream()
                    .map(item -> {
                        Boolean inventaireeOuvert = false;
                        if (listCatOuvert.size() > 0) {
                            inventaireeOuvert = listCatOuvert.stream().anyMatch(s -> s.equals(item.getNumCategorieArticle()));
                        }
                        if (inventaireeOuvert == true) {
                            return null;
                        } else {
                            return item;
                        }
                    })
                    .filter(elt -> elt != null)
                    .collect(toList());
            log.debug("liste depstoDTO final à inventorier: {}", depstoDTO);
        }

        Preconditions.checkBusinessLogique(Boolean.FALSE.equals(initialisationInventaireDTO.getIsDemarrage())
                || (Boolean.TRUE.equals(initialisationInventaireDTO.getIsDemarrage()) && depstoDTO.isEmpty()), "inventaire.stock.exist.for.this.depot");

        // Eliminat the categories with an open inventory
        List<Integer> resultat = lisCategorieArticleID.stream()
                .map(item -> {
                    Boolean inventaireeOuvert = false;

                    if (listCatOuvert.size() > 0) {
                        inventaireeOuvert = listCatOuvert.stream().anyMatch(s -> s.equals(item));
                    }
                    if (inventaireeOuvert == true) {
                        return null;
                    } else {
                        return item;
                    }
                })
                .filter(elt -> elt != null)
                .collect(toList());

        com.csys.util.Preconditions.checkBusinessLogique(resultat.size() > 0, "invt.init.inventaire-ouvert", "Inventaire ouvert");

        // Eliminat the categories with a closed inventory today
        List<Integer> categorieArticlesIds = resultat.stream()
                .map(item -> {
                    Boolean inventaireeColsedToDay = false;
                    if (listarticleCatgorieAInventaireeClosedToDay.size() > 0) {
                        inventaireeColsedToDay = listarticleCatgorieAInventaireeClosedToDay.stream().anyMatch(s -> s.equals(item));
                    }
                    if (inventaireeColsedToDay == true) {
                        return null;
                    } else {
                        return item;
                    }
                })
                .filter(elt -> elt != null)
                .collect(toList());

        com.csys.util.Preconditions.checkBusinessLogique(categorieArticlesIds.size() > 0, Boolean.TRUE.equals(initialisationInventaireDTO.getIsDemarrage()) ? "inventaire.init.demarrage.just.once" : "invt.init.categorie-closedToDay");

        if (Boolean.TRUE.equals(initialisationInventaireDTO.getIsDemarrage())) {
            if (initialisationInventaireDTO.getCateg_depot().equals(CategorieDepotEnum.PH)) {
                depstorepository.insertDepstoForDemarrageByDepotAndCategDepotPH(initialisationInventaireDTO.getCoddep(), LocalDateTime.now(), categorieArticlesIds);
            } else {
                depstorepository.insertDepstoForDemarrageByDepotAndCategDepot(initialisationInventaireDTO.getCoddep(), initialisationInventaireDTO.getCateg_depot().categ(), LocalDateTime.now(), categorieArticlesIds);
            }
        }

        Inventaire inventaire = new Inventaire();
        inventaire.setDepot(initialisationInventaireDTO.getCoddep());
        inventaire.setCategorieArticleParent(initialisationInventaireDTO.getCodCatArticle());
        inventaire.setIsDemarrage(initialisationInventaireDTO.getIsDemarrage());

        List<DetailInventaire> listDetail = new ArrayList<>();

        for (Integer categorieArticleID : categorieArticlesIds) {
            DetailInventaire detail = new DetailInventaire();
            DetailInventairePK detailPK = new DetailInventairePK();
            detailPK.setCategorieArticle(categorieArticleID);
            detail.setDetailInventairePK(detailPK);
            detail.setInventaire1(inventaire);
            listDetail.add(detail);
        }
        /**
         * * depsto *
         */
        if (!Boolean.TRUE.equals(initialisationInventaireDTO.getIsDemarrage()) && depstoDTO.size() > 0) {

            List<Integer> listidDepsto = depstoDTO.stream().map(item -> item.getCode()).distinct().collect(toList());
            Integer[] codes = new Integer[listidDepsto.size()];
            codes = listidDepsto.toArray(codes);
            log.debug("list des articles {}", listidDepsto);
            //Decompser la liste des articles en des parties de 2000 id pour l'accés a la base de données
            log.debug("*1* length {}", codes.length);
            if (codes != null && codes.length > 0) {
                Integer numberOfChunks = (int) Math.ceil((double) codes.length / 2000);
                for (int i = 0; i < numberOfChunks; i++) {
                    List<Integer> codesChunk = Arrays.asList(codes).subList(i * 2000, Math.min(i * 2000 + 2000, codes.length));
                    depstorepository.updateDepstoForOpenInventoryByCodeIn(codesChunk);
                }
            }
        }

        inventaire.setDetailInventaireCollection(listDetail);
        inventaire.setCodeSaisie(preffix + suffixe);
        inventaire.setCategorieDepot(initialisationInventaireDTO.getCateg_depot());
        inventaire = inventaireRepository.save(inventaire);

        if (depotPrincipal.getCode().equals(initialisationInventaireDTO.getCoddep()) && !(initialisationInventaireDTO.getFactureBTs().isEmpty())) {
            log.debug("begining treating traceTransfertInventaire");
            List<TraceTransfertInventaire> listeTrace = new ArrayList();
            for (String transfert : initialisationInventaireDTO.getFactureBTs()) {
                TraceTransfertInventaire trace = new TraceTransfertInventaire();
                trace.setNumbon_factureBT(transfert);
                trace.setInventaire(inventaire);
                trace.setUser(SecurityContextHolder.getContext().getAuthentication().getName());
                trace.setDate(LocalDateTime.now());
                listeTrace.add(trace);
            }
            log.debug("list Trace transfert sont {}", listeTrace);
            traceTransfertInventaireService.saveAll(listeTrace);
        }

        paramService.updatecompteurpharmacie(inventaire.getCategorieDepot(), TypeBonEnum.IN, Helper.IncrmenterString5(suffixe));
        return InventaireFactory.inventaireToInventaireDTO(inventaire);
    }

    public byte[] edition(Integer coddep, CategorieDepotEnum categ_depot, Integer codCatArticle)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print operations PEC: {}");
        List<DepstoDTO> listDepsto = getDepStoByCodeDepotAndCategorieArticleInitInventaire(coddep, categ_depot, codCatArticle).stream().distinct().collect(Collectors.toList()).stream().distinct().collect(toList());
        com.csys.util.Preconditions.checkBusinessLogique(listDepsto.size() > 0, "invt.init.empty-depot", "pas des articles");

        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        if (!cliniqueDto.isEmpty()) {
            cliniqueDto.get(0).setLogoClinique();
        }

        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (categ_depot.toString().equals(CategorieDepotEnum.EC.toString())) {
            reportClientDoc.open("Reports/etatInventaireViergeEC.rpt", 0);
        } else {
            reportClientDoc.open("Reports/etatInventaireVierge.rpt", 0);
        }
        reportClientDoc
                .getDatabaseController().setDataSource(listDepsto, DepstoDTO.class,
                        "Commande", "Commande");
        reportClientDoc
                .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                "clinique", "clinique");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "titre", "مطبوعة جرد فارغة");
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }

    public Collection<ArticleDTO> articleByCategArt(Integer categorieArticleID) {

        return paramAchatServiceClient.articleByCategArt(categorieArticleID);
    }

    public Boolean checkOpenInventaire(Integer coddep, CategorieDepotEnum categ_depot, Integer codCatArticle) {
        List<Inventaire> listeInventaireOuverts = inventaireRepository.findByDateClotureIsNull();
        Set<Integer> articleCatgorieAInventairee = new HashSet();
        if (!listeInventaireOuverts.isEmpty()) {
            articleCatgorieAInventairee = listeInventaireOuverts.stream().
                    flatMap(item -> item.getDetailInventaireCollection().stream()).
                    filter(item -> item.getDetailInventairePK().getCategorieArticle().equals(codCatArticle))
                    .map(item -> item.getDetailInventairePK().getCategorieArticle()).collect(toSet());
        }
        return !articleCatgorieAInventairee.isEmpty();
    }

    public List<CategorieArticleDTO> getCategorieArticleOuvert(Integer coddep, CategorieDepotEnum categ_depot) {
        List<Integer> listCategorieArticleOuvert = new ArrayList<>();
        List<CategorieArticleDTO> categorieArticleECFindbyListId = new ArrayList<>();

        List<Inventaire> listeInventaireOuverts = inventaireRepository.findByDateClotureIsNullAndDepotAndCategorieDepot(coddep, categ_depot);

        if (!listeInventaireOuverts.isEmpty()) {
            Set<Integer> listidDepsto = listeInventaireOuverts.stream().filter(f -> f.getDepot().equals(coddep)).flatMap(item -> item.getDetailInventaireCollection().stream())
                    .map(item -> item.getDetailInventairePK().getCategorieArticle()).collect(toSet());
            com.csys.util.Preconditions.checkBusinessLogique(!listidDepsto.isEmpty(), "depot.depot.depot-is-not-open", "depot non ouvert");

            listCategorieArticleOuvert = new ArrayList<>(listidDepsto);
        }

        categorieArticleECFindbyListId = paramAchatServiceClient.categorieArticleECFindbyListId(listCategorieArticleOuvert);
        return categorieArticleECFindbyListId;
    }

    public Date resetTime(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getLastHourInDate(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public List<InventaireDTO> findByDateClotureNotNullAndDateCloture(Integer depot) {
        return InventaireFactory.inventaireToInventaireDTOs(inventaireRepository.findByDateClotureBetweenAndDepot(resetTime(new Date()), getLastHourInDate(new Date()), depot));
    }

    public List<InventaireDTO> findByDepotAndIsDemarrage(Integer depot, CategorieDepotEnum categDepot) {
        return InventaireFactory.inventaireToInventaireDTOs(inventaireRepository.findByDepotAndIsDemarrageAndCategorieDepot(depot, true, categDepot));
    }

    //Etat inventaire avant la validation Globale 
    public byte[] editionEtatEcartAv(Integer coddep, CategorieDepotEnum categ_depot, Integer codeInventaire,
            TypeEnvoieEtatEnum optionImpression, String type, String group, Boolean withPrincipalUnity)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {

        List<EtatEcartInventaire> listeEcartInventaire = invDepstoService.etatEcartAvantVldGlobByCodDepCathegDepCodInv(coddep, categ_depot, codeInventaire, optionImpression, withPrincipalUnity);
//        System.out.println("*****size" + listeEcartInventaire.size());
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            if (group.equalsIgnoreCase("O")) {
                reportClientDoc.open("Reports/etatEcartInventaire.rpt", 0);
            } else {
                reportClientDoc.open("Reports/etatEcartInventaireNonGroupper.rpt", 0);
            }
        } else {
            reportClientDoc.open("Reports/etatEcartInventaireExl.rpt", 0);
        }
        reportClientDoc
                .getDatabaseController().setDataSource(listeEcartInventaire, EtatEcartInventaire.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        paramController.setCurrentValue("", "listeNumAffichesFactureBTs", "");
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "titre", "قائمة الفائض و العجز قبل التثبيت");
        ByteArrayInputStream byteArrayInputStream = null;
        if (type.equalsIgnoreCase("P")) {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController()
                    .export(ReportExportFormat.PDF);
        } else {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
        }
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return Helper.read(byteArrayInputStream);
    }

    //Etat inventaire apres la validation Globale 
    public byte[] editionEtatEcartAp(Integer codeInventaire, TypeEnvoieEtatEnum optionImpression, String type, String group, String global, Boolean withPrincipalUnity, CategorieDepotEnum categDepot)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        List<EtatEcartInventaire> listeEcartInventaire;

        //Impression en cas ou on a besoin d'une etat detaillé ou non (afficher les articles avec tous les prix differents
        if (global.equalsIgnoreCase("O")) {
            listeEcartInventaire = invDepstoService.etatEcartApresVldGlobByCodDepCathegDepCodInv(codeInventaire, optionImpression, categDepot, withPrincipalUnity);
        } else {
            listeEcartInventaire = invDepstoService.etatEcartApresVldGlobByCodDepCathegDepCodInvDetail(codeInventaire, optionImpression, categDepot, withPrincipalUnity);
        }
        com.csys.util.Preconditions.checkBusinessLogique(!listeEcartInventaire.isEmpty(), "inventaire.inv-vide");

        List<TraceTransfertInventaire> listeTrace = traceTransfertInventaireService.findByCodeInventaire(codeInventaire);
        String listeNumAffichesFactureBTs = "";
        if (!listeTrace.isEmpty()) {
            List<String> numBonTransferts = listeTrace.stream().map(TraceTransfertInventaire::getNumbon_factureBT).collect(Collectors.toList());
            listeNumAffichesFactureBTs = String.join(",", numBonTransferts);

        }
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (global.equalsIgnoreCase("O")) {
            if (type.equalsIgnoreCase("P")) {
                if (group.equalsIgnoreCase("O")) {
                    log.debug("etatEcartInventaire");
                    reportClientDoc.open("Reports/etatEcartInventaire.rpt", 0);
                } else {
                    log.debug("etatEcartInventaireNonGroupper");
                    reportClientDoc.open("Reports/etatEcartInventaireNonGroupper.rpt", 0);
                }
            } else {
                log.debug("etatEcartInventaireExl");
                reportClientDoc.open("Reports/etatEcartInventaireExl.rpt", 0);
            }
        } else {
            if (type.equalsIgnoreCase("P")) {
                log.debug("etatEcartInventaireDetaill");
                reportClientDoc.open("Reports/etatEcartInventaireDetaill.rpt", 0);
            } else {
                log.debug("etatEcartInventaireDetaillExcel");
                reportClientDoc.open("Reports/etatEcartInventaireDetaillExcel.rpt", 0);
            }
        }

        reportClientDoc.getDatabaseController()
                .setDataSource(listeEcartInventaire, EtatEcartInventaire.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase(
                "P")) {
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }

        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "listeNumAffichesFactureBTs", listeNumAffichesFactureBTs);
        paramController.setCurrentValue(
                "", "user", user);
        if (global.equalsIgnoreCase(
                "O")) {
            paramController.setCurrentValue("", "titre", listeEcartInventaire.get(0).getCodeSaisie() + "قائمة الفائض و العجز بعد التثبيت للجرد عدد ");
        } else {
            paramController.setCurrentValue("", "titre", listeEcartInventaire.get(0).getCodeSaisie() + "قائمة الفائض و العجز مفصلة بعد التثبيت للجرد عدد ");
        }
        ByteArrayInputStream byteArrayInputStream = null;

        if (type.equalsIgnoreCase(
                "P")) {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController()
                    .export(ReportExportFormat.PDF);
        } else {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
        }

        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();

        if (type.equalsIgnoreCase(
                "P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }

        return Helper.read(byteArrayInputStream);
    }

//Etat inventaire  
    public byte[] editionEtatInventaire(Integer codeInventaire, TypeEnvoieEtatEnum optionImpression, String type, String group)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {

        List<EtatEcartInventaire> listeEcartInventaire = invDepstoService.etatEcartApresVldGlobByCodDepCathegDepCodInv(codeInventaire, optionImpression, null, null);
        com.csys.util.Preconditions.checkBusinessLogique(!listeEcartInventaire.isEmpty(), "inventaire.inv-vide");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();

        if (type.equalsIgnoreCase("P")) {
            if (group.equalsIgnoreCase("O")) {
                reportClientDoc.open("Reports/etatInv.rpt", 0);
            } else {
                reportClientDoc.open("Reports/etatInvNonGroup.rpt", 0);
            }
        } else {
            reportClientDoc.open("Reports/etatInvExcel.rpt", 0);

        }
        reportClientDoc
                .getDatabaseController().setDataSource(listeEcartInventaire, EtatEcartInventaire.class,
                        "Commande", "Commande");

        if (type.equalsIgnoreCase("P")) {
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "titre", listeEcartInventaire.get(0).getCodeSaisie() + "جرد رقم ");
        ByteArrayInputStream byteArrayInputStream = null;
        if (type.equalsIgnoreCase("P")) {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController()
                    .export(ReportExportFormat.PDF);
        } else {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
        }
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return Helper.read(byteArrayInputStream);
    }

    public List<InventaireDTO> findListInventaire(Integer coddep, CategorieDepotEnum categ_depot, Boolean ouvert, Boolean annuler) {
        List<Inventaire> listeInventaire;

        //Retourner la liste des inventaire au choix (ouvert ou fermé) avec controle
        if (ouvert) {
            if (Boolean.TRUE.equals(annuler)) {
                listeInventaire = inventaireRepository
                        .findByDepotAndCategorieDepotAndDateClotureIsNullAndDateAnnuleIsNotNull(coddep, categ_depot);
                com.csys.util.Preconditions.checkBusinessLogique(!listeInventaire.isEmpty(), "inventaire.exist-pas-ouvert");
            } else if (Boolean.FALSE.equals(annuler)) {
                listeInventaire = inventaireRepository
                        .findByDepotAndCategorieDepotAndDateClotureIsNullAndDateAnnuleIsNull(coddep, categ_depot);
                com.csys.util.Preconditions.checkBusinessLogique(!listeInventaire.isEmpty(), "inventaire.exist-pas-ouvert");
            } else {
                listeInventaire = inventaireRepository
                        .findByDepotAndCategorieDepotAndDateClotureIsNull(coddep, categ_depot);
                com.csys.util.Preconditions.checkBusinessLogique(!listeInventaire.isEmpty(), "inventaire.exist-pas-ouvert");
            }

        } else {
            listeInventaire = inventaireRepository
                    .findByDepotAndCategorieDepotAndDateClotureNotNull(coddep, categ_depot);
            com.csys.util.Preconditions.checkBusinessLogique(!listeInventaire.isEmpty(), "inventaire.exist-pas-ferme");
        }
        List<InventaireDTO> listeInventaireDto = InventaireFactory.inventaireToInventaireDTOs(listeInventaire);
        listeInventaireDto.forEach(d -> {
            //Ajouter la designation du catégorie article 
            List<CategorieArticleDTO> listeCategArticle = paramAchatServiceClient.findNodesByCategorieArticleParent(d.getCategorieArticleParent());
            listeCategArticle.forEach(k -> {
                if (k.getCode().equals(d.getCategorieArticleParent())) {

                    d.setCategArtDesignation(k.getDesignation());
                    d.setCategArtDesignationSec(k.getDesignationSec());

                }
            });
        });
        return listeInventaireDto;
    }

    public List<DepotDTO> findListDepotOuvert(CategorieDepotEnum categ_depot, Boolean annuler) {
        List<Inventaire> listeInventaire;

        if (Boolean.TRUE.equals(annuler)) {
            listeInventaire = inventaireRepository.findByCategorieDepotAndDateClotureIsNullAndDateAnnuleIsNotNull(categ_depot);
        } else if (Boolean.FALSE.equals(annuler)) {
            listeInventaire = inventaireRepository.findByCategorieDepotAndDateClotureIsNullAndDateAnnuleIsNull(categ_depot);
        } else {
            listeInventaire = inventaireRepository.findByCategorieDepotAndDateClotureIsNull(categ_depot);
        }
        com.csys.util.Preconditions.checkBusinessLogique(!listeInventaire.isEmpty(), "inventaire.exist-pas-ouvertCategDepot");

        //Recuperer la liste des id des depot
        List<Integer> listeCodeDepots = listeInventaire.stream()
                .map(d -> d.getDepot())
                .collect(Collectors.toList());

        List<DepotDTO> listeDepotDto = paramAchatServiceClient.findDepotsByCodes(listeCodeDepots);
        return listeDepotDto;
    }

    public List<ArticleDTO> checkOpenInventoryByListArticleAndCodeDep(List<Integer> listIdArticle, Integer codeDepot) {

        Set<Integer> listCatOuvert = inventaireRepository.findByDateClotureIsNullAndDateAnnuleIsNullAndDepot(codeDepot).stream().flatMap(item -> item.getDetailInventaireCollection().stream())
                .map(item -> item.getDetailInventairePK().getCategorieArticle()).collect(toSet());
//        Boolean articleIsInventory = true;
        List<ArticleDTO> catego = new ArrayList<>();
        if (!listCatOuvert.isEmpty()) {
            List<ArticleDTO> listeArticle = paramAchatServiceClient.articleFindbyListCodeWithLanguageFR(listIdArticle);
            com.csys.util.Preconditions.checkBusinessLogique(!listeArticle.isEmpty(), "inventaire.article-inexistant", "article introuvable");
//            List<Integer> categorieArticle = listeArticle.stream()
//                    .map(item -> item.getCategorieArticle().getCode())
//                    .distinct().collect(toList());

            if (!listCatOuvert.isEmpty()) {
                catego = listeArticle.stream().map(
                        item -> {
                            Integer categorieOuvert = listCatOuvert.stream()
                                    .filter(elt -> elt.equals(item.getCategorieArticle().getCode()))
                                    .findFirst()
                                    .orElse(null);
                            if (categorieOuvert != null) {
                                return item;
                            } else {
                                return null;
                            }
                        }).filter(item -> item != null).collect(toList());

//                if (!catego.isEmpty()) {
//                    articleIsInventory = false;
//                }
            }
        }

        return catego;

    }
    // false ::  categorie article sous inventaire

    public Boolean checkCategorieIsInventorie(Integer codeCategorie) {
        Set<Integer> listCatOuvert = inventaireRepository.findByDateClotureIsNull().stream().flatMap(item -> item.getDetailInventaireCollection().stream())
                .map(item -> item.getDetailInventairePK().getCategorieArticle()).collect(toSet());
        Set<Integer> listCat = listCatOuvert.stream().filter(item -> item.equals(codeCategorie)).collect(toSet());

        if (!listCat.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public List<Integer> checkCategorieIsInventorie(List<Integer> codeCategorieList, Integer coddep) {
        Set<Integer> listCatOuvert = inventaireRepository.findByDateClotureIsNullAndDateAnnuleIsNullAndDepot(coddep).stream().flatMap(item -> item.getDetailInventaireCollection().stream())
                .map(item -> item.getDetailInventairePK().getCategorieArticle()).collect(toSet());

        List<Integer> catego = new ArrayList<>();

        if (!listCatOuvert.isEmpty()) {
            catego = codeCategorieList.stream().map(
                    item -> {
                        Integer categorieOuvert = listCatOuvert.stream()
                                .filter(elt -> elt.equals(item))
                                .findFirst()
                                .orElse(null);
                        if (categorieOuvert != null) {
                            return item;
                        } else {
                            return null;
                        }
                    }).filter(item -> item != null).collect(toList());

        }
        return catego;
    }

    public byte[] editionListArticlePerimeAvantValidationInventaire(Integer codeInventaire, String type)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to edition list article perime avant validation inventaire : {}");

        Inventaire inventaire = inventaireRepository.findOne(codeInventaire);

        Preconditions.checkBusinessLogique(inventaire.getDateCloture() == null, "inventaire-cloture");

        Collection<ArticleDTO> articleDTOs = this.articleByCategArt(inventaire.getCategorieArticleParent());
        List<Integer> codarts = articleDTOs.stream().map(item -> item.getCode()).distinct().collect(Collectors.toList());
        //use chunk for articles ids

        List<Depsto> deptosChunk = new ArrayList<>();
        if (codarts != null && codarts.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codarts.size() / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = codarts.subList(i * 2000, Math.min(i * 2000 + 2000, codarts.size()));
                deptosChunk.addAll(depstorepository.findByCategDepotAndCoddepAndCodArtAndStkRelAndDatPerBefore(inventaire.getCategorieDepot(), inventaire.getDepot(), codesChunk, LocalDate.now()));
            }
        }

        List<DepstoScanDTO> depstoScanDTO = depstoScanService.findListDepstoScanByCodeInventaireAndDatPerBefore(codeInventaire, LocalDate.now());
//        log.debug("depstoScanDTO: {}", depstoScanDTO);
        depstoScanDTO.addAll(DepstoScanFactory.depstosToDepstoScanDTOs(deptosChunk, articleDTOs));
//  log.debug("All depstoScanDTO: {}", depstoScanDTO);

        CategorieDepotDTO categorieDepotDTO = paramAchatServiceClient.findCategorieDepot(inventaire.getCategorieDepot().toString());
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(inventaire.getDepot());
//        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        if (!cliniqueDto.isEmpty()) {
            cliniqueDto.get(0).setLogoClinique();
        }

        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/listeDejaSaisie.rpt", 0);
        } else {
            reportClientDoc.open("Reports/listeDejaSaisieExel.rpt", 0);
        }
        reportClientDoc
                .getDatabaseController().setDataSource(depstoScanDTO, DepstoScanDTO.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "categDepot", categorieDepotDTO.getDesignation());
        paramController.setCurrentValue("", "depot", depot.getDesignation());
        paramController.setCurrentValue("", "titre", messages.getMessage("pharmacie-edition-list-articles-perime-avant-validation-inventaire", null, LocaleContextHolder.getLocale()));
        ByteArrayInputStream byteArrayInputStream = null;
        if (type.equalsIgnoreCase("P")) {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        } else {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
        }
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return Helper.read(byteArrayInputStream);

    }

    public byte[] editionListArticlePerimeApresValidationInventaire(Integer codeInventaire, String type)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {

        log.debug("REST request to edition list article perime apres validation inventaire : {}");

        Inventaire inventaire = inventaireRepository.findOne(codeInventaire);

        Preconditions.checkBusinessLogique(inventaire.getDateCloture() != null, "inventaire-non-cloture");
        List<DepStoHistDTO> depStoHistDTOs = depStoHistService.findListDepstoHistByCodeInventaireAndDatPerBefore(codeInventaire, inventaire.getDateCloture().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        log.debug("depStoHistDTOs {}:", depStoHistDTOs.toString());

        CategorieDepotDTO categorieDepotDTO = paramAchatServiceClient.findCategorieDepot(inventaire.getCategorieDepot().toString());
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(inventaire.getDepot());
//        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        if (!cliniqueDto.isEmpty()) {
            cliniqueDto.get(0).setLogoClinique();
        }

        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
//        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/listeArticlePerimeApresValidationInventaire.rpt", 0);
        } else {
            reportClientDoc.open("Reports/listeArticlePerimeApresValidationInventaireExel.rpt", 0);
        }
        reportClientDoc
                .getDatabaseController().setDataSource(depStoHistDTOs, DepStoHistDTO.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "categDepot", categorieDepotDTO.getDesignation());
        paramController.setCurrentValue("", "depot", depot.getDesignation());
        paramController.setCurrentValue("", "titre", messages.getMessage("pharmacie-edition-list-articles-perime-apres-validation-inventaire", null, LocaleContextHolder.getLocale()));
        ByteArrayInputStream byteArrayInputStream = null;
        if (type.equalsIgnoreCase("P")) {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController()
                    .export(ReportExportFormat.PDF);
        } else {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
        }
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return Helper.read(byteArrayInputStream);

    }

}
