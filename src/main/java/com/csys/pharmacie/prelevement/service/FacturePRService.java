package com.csys.pharmacie.prelevement.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticleIMMODTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.EmplacementDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.service.DemandeServiceClient;
import com.csys.pharmacie.achat.service.ImmobilisationService;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.client.dto.ImmobilisationDTO;
import com.csys.pharmacie.client.dto.ListeImmobilisationDTOWrapper;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.DetailMvtSto;
import com.csys.pharmacie.helper.Helper;
import static com.csys.pharmacie.helper.PrelevmentOrderState.NOT_PRELEVE;
import static com.csys.pharmacie.helper.PrelevmentOrderState.PARTIALLY_PRELEVE;
import static com.csys.pharmacie.helper.PrelevmentOrderState.PRELEVE;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.prelevement.domain.DetailMvtStoPR;
import com.csys.pharmacie.prelevement.domain.EtatDPR;
import com.csys.pharmacie.prelevement.domain.FacturePR;
import com.csys.pharmacie.prelevement.domain.Motif;
import com.csys.pharmacie.prelevement.domain.MvtStoPR;
import com.csys.pharmacie.prelevement.domain.PrelevementDetailDPR;
import com.csys.pharmacie.prelevement.domain.PrelevementDetailDPRPK;
import com.csys.pharmacie.prelevement.domain.QFacturePR;
import com.csys.pharmacie.prelevement.domain.QMvtStoPR;
import com.csys.pharmacie.prelevement.domain.TraceDetailRetourPr;
import com.csys.pharmacie.prelevement.dto.DemandePrDTO;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.prelevement.dto.DetailDemandePrTrDTO;
import com.csys.pharmacie.prelevement.dto.EtatDPRDTO;
import com.csys.pharmacie.prelevement.dto.FacturePRDTO;
import com.csys.pharmacie.prelevement.dto.FacturePREditionDTO;
import com.csys.pharmacie.prelevement.dto.ListeFacturePRDTOWrapper;
import com.csys.pharmacie.prelevement.dto.MvtStoPRDTO;
import com.csys.pharmacie.prelevement.dto.MvtStoPREditionDTO;
import com.csys.pharmacie.prelevement.factory.FacturePRFactory;
import com.csys.pharmacie.prelevement.factory.MvtStoPRFactory;
import com.csys.pharmacie.prelevement.repository.FacturePRRepository;
import com.csys.pharmacie.prelevement.repository.MvtStoPRRepository;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.vente.dto.PMPArticleDTO;
import com.csys.pharmacie.vente.service.PricingService;
import com.csys.util.Preconditions;
import edu.emory.mathcs.backport.java.util.Collections;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Service Implementation for managing FacturePR.
 */
@Service
@Transactional
public class FacturePRService {

//   
//    private static String limitOfPrelevements;
    private final Logger log = LoggerFactory.getLogger(FacturePRService.class);

    private static String limitOfPrelevements;

    private final FacturePRRepository factureprRepository;

    @Autowired
    MvtStoPRRepository mvtStoPRRepository;
    @Autowired
    ParamService paramService;
    @Autowired
    MotifService motifService;
    @Autowired
    private StockService stockService;
    @Autowired
    DemandeServiceClient demandeServiceClient;
    @Autowired
    PrelevementDetailDPRService prelevementDetailDPRService;
    @Autowired
    EtatDPRService etatDPRService;
    @Autowired
    ParamAchatServiceClient paramAchatServiceClient;
    @Autowired
    private PricingService pricingService;

    @Value("${default-number-of-prelevements}")
    public void setdefaultLimitOfPrelevements(String defaultLimitOfPrelevements) {
        limitOfPrelevements = defaultLimitOfPrelevements;
    }

    @Autowired
    ImmobilisationService immobilisationService;
    private final ParamServiceClient parametrageService;
    private final TraceDetailRetourPrService TraceDetailRetourPrService;
    private final InventaireService inventaireService;

    public FacturePRService(FacturePRRepository factureprRepository, ParamServiceClient parametrageService, TraceDetailRetourPrService TraceDetailRetourPrService, InventaireService inventaireService) {
        this.factureprRepository = factureprRepository;
        this.parametrageService = parametrageService;
        this.TraceDetailRetourPrService = TraceDetailRetourPrService;
        this.inventaireService = inventaireService;
    }

    /**
     * Save a factureprDTO.
     *
     * @param id
     * @return the persisted entity
     */
    public FacturePRDTO delete(String id) {

//        log.info("cancelling new FacturePR {}", id);
        FacturePR facturepr = factureprRepository.findByNumbon(id);
        log.debug("=================================================== {}", facturepr);

        Preconditions.checkBusinessLogique(facturepr != null, "prelevment.delete.prelevment-not-found");
        Preconditions.checkBusinessLogique(facturepr.getCodAnnul() == null, "prelevment.delete.prelevment-canceld");
        Preconditions.checkBusinessLogique(!facturepr.getIntegrer(), "facture-deja-integre");

        Set<Integer> codesDetails = new HashSet();
        List<DetailMvtStoPR> details = facturepr.getDetailFacturePRCollection().stream().flatMap(item -> item.getDetailMvtStoPRList().stream()).collect(Collectors.toList());
        details.forEach(x -> {
            x.getDepsto().setQte(x.getDepsto().getQte().add(x.getQuantitePrelevee()));
//            x.setQuantiteDisponible(x.getQuantiteDisponible().add(x.getQuantitePrelevee()));
//            x.setQuantitePrelevee(x.getQuantitePrelevee().subtract(x.getQuantitePrelevee()));
            codesDetails.add(x.getCode());
        });
        //CTRL pour savoir si le prelevement a un retour ou pas
        List<TraceDetailRetourPr> listeTrace = TraceDetailRetourPrService.findByCodeDetailMvtstoprIn(codesDetails);
        log.debug("listeTrace est {}", listeTrace);
        Preconditions.checkBusinessLogique(listeTrace.isEmpty(), "prelevment.is.returned");

        processDPROnDeletePrelevement(facturepr);
        facturepr.setCodAnnul(SecurityContextHolder.getContext().getAuthentication().getName());
        facturepr.setDatAnnul(LocalDateTime.now());

        return FacturePRFactory.factureprToFacturePRDTO(facturepr);

    }

    @Transactional
    public void processDPROnDeletePrelevement(FacturePR prelevement) {

        /**
         * processing the purchase order
         */
        List<PrelevementDetailDPR> prelevedDetails = prelevementDetailDPRService.findByCodePrelevement(prelevement.getNumbon());
        List<Integer> listCodeDPR = prelevedDetails.stream().map(item -> item.getCodeDPR()).distinct().collect(Collectors.toList());
        etatDPRService.deleteByCodedprIn(listCodeDPR);
        List<PrelevementDetailDPR> previousPrelevedDetails = prelevementDetailDPRService.findByCodesDPrIn(listCodeDPR);
        List<EtatDPR> list = previousPrelevedDetails.stream()
                .filter(item -> !item.getPk().getCodePrelevment().equals(prelevement.getNumbon()) && item.getQuantite_prelevee().compareTo(BigDecimal.ZERO) > 0)
                .map(filtredItem -> {
                    return new EtatDPR(filtredItem.getCodeDPR(), PARTIALLY_PRELEVE);
                })
                .collect(Collectors.toList());
        prelevementDetailDPRService.deleteList(prelevedDetails);
        etatDPRService.save(list);
    }

    public FacturePRDTO save(FacturePRDTO dto) {

        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(dto.getCoddepotSrc());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + depotd.getDesignation() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getDesignation() + "] est un dépot fournisseur");

        FacturePR facturepr = FacturePRFactory.factureprDTOToFacturePR(dto);
        String numbon = paramService.getcompteur(dto.getCategDepot(), TypeBonEnum.PR);
        facturepr.setNumbon(numbon);

        DepartementDTO departementDTO = paramAchatServiceClient.findDepartment(dto.getCoddepartDesti());
        facturepr.setCodeCostCenter(departementDTO.getCodeCostCenter());
        Motif mt = motifService.findMotif(dto.getMotifID());
        facturepr.setMotif(mt);

        Set<Integer> CodeArticles = new HashSet();
        Set<String> CodeSaisiArticles = new HashSet();
        Set<Integer> codesEmplacements = new HashSet();
        dto.getDetails().forEach(item -> {
            CodeArticles.add(item.getArticleID());
            CodeSaisiArticles.add(item.getCodeSaisi());
            if (item.getCodeEmplacement() != null) {
                codesEmplacements.add(item.getCodeEmplacement());
            }
        });
        List<ArticleDTO> articleUnderInventory = inventaireService.checkOpenInventoryByListArticleAndCodeDep(new ArrayList<>(CodeArticles), facturepr.getCoddepotSrc());
        Preconditions.checkBusinessLogique(articleUnderInventory.isEmpty(), "article-under-inventory", articleUnderInventory.stream().map(ArticleDTO::getCodeSaisi).collect(toList()).toString());

        List<PMPArticleDTO> articlespmp = pricingService.findPMPsByArticleIn(CodeArticles.toArray(new Integer[CodeArticles.size()]));

        List<Depsto> depstos = stockService.findByCodartInAndCoddepAndQteGreaterThan(new ArrayList<>(CodeArticles), facturepr.getCoddepotSrc(), BigDecimal.ZERO);
        List<ArticleDTO> articlesDTOs = dto.getCategDepot().equals(CategorieDepotEnum.IMMO) ? (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(dto.getCategDepot(), CodeArticles.toArray(new Integer[CodeArticles.size()])) : new ArrayList();

        List<MvtStoPR> detailsFacturePR = new ArrayList();
        BigDecimal totaleMnt = BigDecimal.ZERO;

        List< ImmobilisationDTO> listeimmoDTOs = dto.getCategDepot().equals(CategorieDepotEnum.IMMO) ? immobilisationService.findImmobilisationsBylisteCodeArticle(new ArrayList<>(CodeSaisiArticles), Boolean.TRUE) : new ArrayList();
        List<EmplacementDTO> emplacements = dto.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(codesEmplacements,null) : new ArrayList();

        log.debug("listeimmoDTOs size est {}", listeimmoDTOs);
        if (dto.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            Preconditions.checkBusinessLogique(listeimmoDTOs != null, "error-immo");
            Preconditions.checkBusinessLogique(emplacements != null, "error-emplacaments");
        }

        List< ImmobilisationDTO> listeUpdatedImmoDTOs = new ArrayList();
        List< ImmobilisationDTO> listeAllUpdatedImmoDTOs = new ArrayList();
        for (MvtStoPRDTO mvtstoPRDTO : dto.getDetails()) {

            MvtStoPR detailFacture = MvtStoPRFactory.mvtstoprDTOToMvtStoPR(mvtstoPRDTO);

            detailFacture.setFacturePR(facturepr);

            if (detailFacture.getCategDepot().equals(CategorieDepotEnum.EC)) {

                PMPArticleDTO pmpArticle = articlespmp.stream().filter(item -> item.getArticleID().equals(detailFacture.getCodart())).findFirst().get();
//                Preconditions.checkBusinessLogique(!(pmpArticle.getPMP().equals(BigDecimal.ZERO)), "prelevement.add.pmp-null");
                detailFacture.setPriuni(pmpArticle.getPMP());
                totaleMnt = totaleMnt.add(pmpArticle.getPMP().multiply(detailFacture.getQuantite()));
            }

            detailFacture.setNumbon(numbon);
            detailFacture.setQtecom(detailFacture.getQuantite());

            if (dto.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                detailFacture.setDatPer(LocalDate.now());
                log.debug("lot inter est {}", mvtstoPRDTO.getLotinter() == null);
                if ("" == mvtstoPRDTO.getLotinter()) {
                    detailFacture.setLotinter("-");
                }
            }

            detailsFacturePR.add(detailFacture);

            List<DetailMvtSto> detailMvtSto = dto.getCategDepot().equals(CategorieDepotEnum.IMMO) ? stockService.GestionDetailFactureImmo(depstos, detailFacture, DetailMvtStoPR.class, false) : stockService.GestionDetailFacture(depstos, detailFacture, DetailMvtStoPR.class, false);

            List<DetailMvtStoPR> detailMvtStoPRs
                    = detailMvtSto.stream()
                            .map(item -> {
                                DetailMvtStoPR detailMvtStoPR = (DetailMvtStoPR) item;
                                detailMvtStoPR.setMvtStoPR(detailFacture);
                                detailMvtStoPR.setQtecom(detailMvtStoPR.getQuantitePrelevee());
                                return detailMvtStoPR;
                            })
                            .collect(toList());
            detailFacture.setDetailMvtStoPRList(detailMvtStoPRs);

            if (dto.getCategDepot().equals(CategorieDepotEnum.IMMO)) {

                ArticleIMMODTO matchedArticleImmo = (ArticleIMMODTO) articlesDTOs
                        .stream()
                        .filter(elt -> elt.getCode().equals(detailFacture.getCodart()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(detailFacture.getCodart().toString())));
                EmplacementDTO matchingEmplacement = new EmplacementDTO();
                if (detailFacture.getCodeEmplacement() != null) {
                    matchingEmplacement = emplacements.stream().filter(x -> x.getCode().equals(detailFacture.getCodeEmplacement()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-emplacement", new Throwable(detailFacture.getCodeEmplacement().toString())));
                }

                if (Boolean.TRUE.equals(matchedArticleImmo.getGenererImmobilisation())) {
                    listeUpdatedImmoDTOs = gererImmobilisation(listeimmoDTOs, detailFacture, matchedArticleImmo, matchingEmplacement);
                    listeAllUpdatedImmoDTOs.addAll(listeUpdatedImmoDTOs);
                    log.debug("listeUpdatedImmoDTOs apres treatemet est ,{}", listeUpdatedImmoDTOs);
                    log.debug("listeAllUpdatedImmoDTOs  est ,{}", listeAllUpdatedImmoDTOs);
                }
            }
        }

        //fin traitement mvtstoPr
        ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper = new ListeImmobilisationDTOWrapper();

        log.debug("listeUpdatedImmoDTOs after treatemet est ,{}", listeUpdatedImmoDTOs);
        log.debug("listeAllUpdatedImmoDTOs  est {}", listeAllUpdatedImmoDTOs);

        if (totaleMnt.equals(BigDecimal.ZERO)) {
            totaleMnt = detailsFacturePR.stream().map(item -> item.getPriuni().multiply(item.getQuantite())).reduce(BigDecimal.ZERO, (p, q) -> p.add(q));
        }

        facturepr.setMntFac(totaleMnt);

        facturepr.setDetailFacturePRCollection(detailsFacturePR);

        facturepr = factureprRepository.save(facturepr);

        List<Integer> listeCodeDPR = dto.getDemandeDPR().stream().map(item -> item.getCode()).collect(toList());

        List<DetailDemandePrTrDTO> detailPrelevment = getDetailOfPrelevmentOrders(listeCodeDPR);

        processPrelevmentOrders(facturepr, detailPrelevment);

        paramService.updateCompteurPharmacie(facturepr.getCategDepot(), TypeBonEnum.PR);
        FacturePRDTO resultDTO = FacturePRFactory.factureprToFacturePRDTO(facturepr);
        if (dto.getCategDepot().equals(CategorieDepotEnum.IMMO) && !(listeAllUpdatedImmoDTOs.isEmpty())) {
            listeImmobilisationDTOWrapper.setImmobilisation(listeAllUpdatedImmoDTOs);
            log.debug("listeImmobilisationDTOWrapper {}", listeImmobilisationDTOWrapper);
            ListeImmobilisationDTOWrapper listeImmo = immobilisationService.updateImmo(listeImmobilisationDTOWrapper);
            Preconditions.checkBusinessLogique(listeImmo != null, "error-saving-immo");
        }
        return resultDTO;
    }

    /**
     * Update a factureprDTO.
     *
     * @param factureprDTO
     * @return the updated entity
     */
    public FacturePRDTO update(FacturePRDTO factureprDTO) {
        log.debug("Request to update FacturePR: {}", factureprDTO);
        FacturePR inBase = factureprRepository.findOne(factureprDTO.getNumbon());
        Preconditions.checkBusinessLogique(!inBase.getIntegrer(), "facture-deja-integre");
        // Preconditions.checkArgument(inBase != null, "facturepr.NotFound");
        FacturePR facturepr = FacturePRFactory.factureprDTOToFacturePR(factureprDTO);
        facturepr = factureprRepository.save(facturepr);
        FacturePRDTO resultDTO = FacturePRFactory.factureprToFacturePRDTO(facturepr);
        return resultDTO;
    }

    /**
     * Getting the details of prelevment orders that will be used to create the
     * reception.
     *
     * @param prelevmentOrdersCodes
     * @return details of prelevment orders sorted by creation date desc
     *
     *
     */
    @Transactional
    public List<DetailDemandePrTrDTO> getDetailOfPrelevmentOrders(List<Integer> prelevmentOrdersCodes) {

        /* getting the list of prelevment orders
         */
        Set<DemandePrDTO> listeDPR = demandeServiceClient.findListDemandePr(prelevmentOrdersCodes, LocaleContextHolder.getLocale().getLanguage());
        Preconditions.checkBusinessLogique(listeDPR.size() == prelevmentOrdersCodes.size(), "reception.add.missing-PDR");
        for (DemandePrDTO demandepr : listeDPR) {
            Preconditions.checkBusinessLogique(demandepr.getDateValidation() != null && Boolean.TRUE.equals(demandepr.getAccepted()), "demande.not.valid");
            Preconditions.checkBusinessLogique(demandepr.getDateArchive() == null, "demande.archived");
        }
        List<EtatDPRDTO> listeEtatsDemandes = etatDPRService.FindByDPRIn(prelevmentOrdersCodes);

        EtatDPRDTO Satisfiedetat = listeEtatsDemandes.stream().filter(demande -> PRELEVE.equals(demande.getEtat())).findFirst().orElse(null);

        Preconditions.checkBusinessLogique(Satisfiedetat == null, "reception.add.recived-DPR");

        List<PrelevementDetailDPR> recivedDetailDPR = prelevementDetailDPRService.findByCodesDPrIn(prelevmentOrdersCodes);
        //);
        List<DetailDemandePrTrDTO> listDetailPR = listeDPR.stream()
                .sorted((a, b) -> a.getDateCreate().compareTo(b.getDateCreate()))
                .flatMap(e -> e.getDetailsDemande().stream())
                .map(detailDPR -> {
                    Integer recivedOteDPR = recivedDetailDPR.stream()
                            .filter(item -> item.getPk().getCodedetailDPR().equals(detailDPR.getCode()))
                            .map(PrelevementDetailDPR::getQuantite_prelevee)
                            .collect(Collectors.summingInt(item -> item.intValue()));

                    Integer qterestant = recivedOteDPR != null ? detailDPR.getQuantiteValide() - recivedOteDPR : detailDPR.getQuantiteValide();
                    Preconditions.checkBusinessLogique(qterestant >= 0, "reception.add.recived-DPR");
                    detailDPR.setQuantiteRestante(qterestant);

                    return detailDPR;
                })
                .collect(Collectors.toList());

        return listDetailPR;
    }

    /**
     * This method choose which details of purchase orders that we used for
     * creating the reception. By choosing we mean subtracking each recived
     * quantite from the detail of purchase order that we choose
     *
     * @param facturePR the created reception
     * @param prelevmentOrdersDetails
     */
    @Transactional
    public void processPrelevmentOrders(FacturePR facturePR, List<DetailDemandePrTrDTO> prelevmentOrdersDetails) {
        log.debug("processing Prelevment orders {}", prelevmentOrdersDetails);
        Map<Integer, Integer> articlesGroupeByCodart = facturePR.getDetailFacturePRCollection()
                .stream()
                .collect(Collectors.groupingBy(item -> item.getCodart(), Collectors.summingInt(item -> item.getQuantite().intValue())));

        List<PrelevementDetailDPR> prelevementDetailDPRs = new ArrayList();
        for (Map.Entry<Integer, Integer> mvtStoPR : articlesGroupeByCodart.entrySet()) {
            BigDecimal resteMvtstoPR = BigDecimal.valueOf(mvtStoPR.getValue());
            BigDecimal min;
            BigDecimal totalDemandedQtyPerItem = BigDecimal.ZERO;
            Boolean existsAtLeastOnce = false;
            for (DetailDemandePrTrDTO detailDPR : prelevmentOrdersDetails) {
                if (detailDPR.getArticle().getCode().equals(mvtStoPR.getKey())) {
                    if ((resteMvtstoPR.compareTo(BigDecimal.ZERO) > 0)) {

                        Preconditions.checkBusinessLogique(detailDPR.getQuantiteRestante() > 0, "reception.add.recived-DPR");
                        BigDecimal qteRestanteDetailDPR = new BigDecimal(detailDPR.getQuantiteRestante());
                        totalDemandedQtyPerItem = totalDemandedQtyPerItem.add(new BigDecimal(detailDPR.getQuantiteRestante()));
                        min = qteRestanteDetailDPR.min(resteMvtstoPR);
                        detailDPR.setQuantiteRestante(qteRestanteDetailDPR.subtract(min).intValue());
                        resteMvtstoPR = resteMvtstoPR.subtract(min);

                        PrelevementDetailDPRPK PK = new PrelevementDetailDPRPK(detailDPR.getCode());
                        PK.setCodePrelevment(facturePR.getNumbon());
                        prelevementDetailDPRs.add(new PrelevementDetailDPR(PK, detailDPR.getCodeDemande(), min, facturePR));
                        existsAtLeastOnce = true;
                    } else {
                        break;

                    }

                }
            }
            log.debug("la quantite preleve est {},demande {},pour le code article", mvtStoPR.getValue(), totalDemandedQtyPerItem, mvtStoPR.getKey());
            Preconditions.checkBusinessLogique(BigDecimal.valueOf(mvtStoPR.getValue()).compareTo(totalDemandedQtyPerItem) <= 0, "preleved.quantity.greater.than.demanded.quantity", mvtStoPR.getKey().toString());
            if (!facturePR.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                Preconditions.checkBusinessLogique(existsAtLeastOnce, "reception.add.detailDPR-missing", mvtStoPR.getKey().toString());
            }
        }
        prelevementDetailDPRService.save(prelevementDetailDPRs);

        Map<Integer, Optional<DetailDemandePrTrDTO>> QteResteValidByDPR = prelevmentOrdersDetails.stream()
                .collect(groupingBy(DetailDemandePrTrDTO::getCodeDemande, Collectors.reducing((a, b) -> {
                    a.setQuantiteRestante(a.getQuantiteRestante() + b.getQuantiteRestante());
                    a.setQuantiteValide(a.getQuantiteValide() + b.getQuantiteValide());
                    return a;
                })));
        List<EtatDPR> etatDPR = new ArrayList();

        QteResteValidByDPR.forEach((key, value) -> {
            DetailDemandePrTrDTO mergedDetailDPR = value.get();
            if (mergedDetailDPR.getQuantiteRestante() == 0) {

                etatDPR.add(new EtatDPR(value.get().getCodeDemande(), PRELEVE));

            } else if (mergedDetailDPR.getQuantiteRestante().equals(mergedDetailDPR.getQuantiteValide())) {

                etatDPR.add(new EtatDPR(value.get().getCodeDemande(), NOT_PRELEVE));

            } else if (mergedDetailDPR.getQuantiteRestante().compareTo(mergedDetailDPR.getQuantiteValide()) < 0) {

                etatDPR.add(new EtatDPR(value.get().getCodeDemande(), PARTIALLY_PRELEVE));
            }

        });
        etatDPRService.save(etatDPR);

    }

    /**
     * Get one factureprDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(readOnly = true)
    public List<MvtStoPRDTO> findDetailFactureById(String id) {
        log.debug("Request to get FacturePR: {}", id);

        List<MvtStoPR> mvt = mvtStoPRRepository.findByNumbon(id);

        List<MvtStoPRDTO> mvtdto = MvtStoPRFactory.mvtstoprToMvtStoPRDTOs(mvt);
        List<Integer> listCodeUnites = new ArrayList();
        Set<Integer> setEmplacement = new HashSet();

        mvt.forEach(mvtPR -> {
            listCodeUnites.add(mvtPR.getUnite());
            if (mvtPR.getCodeEmplacement() != null) {
                setEmplacement.add(mvtPR.getCodeEmplacement());
            }
        });

        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(listCodeUnites);
        List<EmplacementDTO> emplacements = mvt.get(0).getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(setEmplacement,null) : new ArrayList();

        mvtdto.forEach(item -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getUnite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            item.setDesignationunite(unite.getDesignation());

            if (mvt.get(0).getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                EmplacementDTO emplacement = emplacements.stream()
                        .filter(x -> x.getCode().equals(item.getCodeEmplacement()))
                        .findFirst().orElse(null);
                if (item.getCodeEmplacement() != null) {
                    item.setDesignationEmplacement(emplacement.getDesignation());
                }
            }

        });
        return mvtdto;
    }

    @Transactional(readOnly = true)
    public FacturePRDTO findOne(String id) {
        log.debug("Request to get FacturePR: {}", id);
        FacturePR facturepr = factureprRepository.findOne(id);
        FacturePRDTO dto = FacturePRFactory.factureprToFacturePRDTO(facturepr);
        DepartementDTO departement = paramAchatServiceClient.findDepartment(dto.getCoddepartDesti());

        dto.setDesignationDepartDestination(departement.getDesignation());
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(dto.getCoddepotSrc());
        Set<Integer> setEmplacement = new HashSet();
        List<Integer> codesUnites = new ArrayList();
        dto.getDetails().forEach(item -> {
            codesUnites.add(item.getUnite());
            if (item.getCodeEmplacement() != null) {
                setEmplacement.add(item.getCodeEmplacement());
            }
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codesUnites);
        List<EmplacementDTO> emplacements = facturepr.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(setEmplacement,null) : new ArrayList();

        dto.setDepotDesignation(depot.getDesignation());
        dto.getDetails().forEach(item -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getUnite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            item.setDesignationunite(unite.getDesignation());

            if (facturepr.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                EmplacementDTO emplacement = emplacements.stream()
                        .filter(x -> x.getCode().equals(item.getCodeEmplacement()))
                        .findFirst().orElse(null);
                if (item.getCodeEmplacement() != null) {
                    item.setDesignationEmplacement(emplacement.getDesignation());
                }
            }
            List<PrelevementDetailDPR> listprelevment = prelevementDetailDPRService.findByCodePrelevement(dto.getNumbon());
            List<Integer> codedemande = listprelevment.stream().map(iteme -> iteme.getCodeDPR()).distinct().collect(Collectors.toList());
            Set<DemandePrDTO> setDPR = demandeServiceClient.findListDemandePr(codedemande, LocaleContextHolder.getLocale().getLanguage());
            List<DemandePrDTO> listedpr = setDPR.stream().collect(Collectors.toList());
            dto.setDemandeDPR(listedpr);

            dto.setDestinationCodeSaisi(departement.getCodeSaisi());

            DepartementDTO departementDepot = paramAchatServiceClient.findDepartment(dto.getCoddepotSrc());
            dto.setSourceCodeSaisi(departementDepot.getCodeSaisi());
        });

        return dto;
    }

    /**
     * Get one facturepr by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FacturePR findFacturePR(String id) {
        log.debug("Request to get FacturePR: {}", id);
        FacturePR facturepr = factureprRepository.findOne(id);
        return facturepr;
    }

    /**
     * Get all the factureprs.
     *
     * @param queriedFacPR
     * @param fromDate
     * @param toDate
     * @param deleted
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<FacturePRDTO> findAll(FacturePR queriedFacPR, LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted) {
        log.debug("Request to get All FacturePRs");
        QFacturePR _FacturePR = QFacturePR.facturePR;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_FacturePR.typbon.eq(TypeBonEnum.PR))
                .optionalAnd(fromDate, () -> _FacturePR.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FacturePR.datbon.loe(toDate))
                .optionalAnd(queriedFacPR.getCategDepot(), () -> _FacturePR.categDepot.eq(queriedFacPR.getCategDepot()))
                .optionalAnd(queriedFacPR.getCoddepotSrc(), () -> _FacturePR.coddepotSrc.eq(queriedFacPR.getCoddepotSrc()))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _FacturePR.codAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _FacturePR.codAnnul.isNull());

        List<FacturePR> listFacturePRs = (List<FacturePR>) factureprRepository.findAll(builder);
        Set<Integer> codeDepots = new HashSet();
        listFacturePRs.stream().forEach(item -> {
            codeDepots.add(item.getCoddepartDest());
            codeDepots.add(item.getCoddepotSrc());
        });
        List<DepartementDTO> listDepartment = paramAchatServiceClient.findListDepartments(codeDepots);

        return listFacturePRs.stream().map(item -> {
            FacturePRDTO dto = FacturePRFactory.factureprToFacturePRDTOLazy(item);
            listDepartment.stream().filter(x -> x.getCode().equals(item.getCoddepotSrc()) || x.getCode().equals(item.getCoddepartDest())).forEach(departement -> {
                if (departement.getCode().equals(item.getCoddepotSrc())) {
                    dto.setDepotDesignation(departement.getDesignation());
                    dto.setSourceCodeSaisi(departement.getCodeSaisi());
                }
                if (departement.getCode().equals(item.getCoddepartDest())) {
                    dto.setDesignationDepartDestination(departement.getDesignation());
                    dto.setDestinationCodeSaisi(departement.getCodeSaisi());
                }
            });
            return dto;

        }).collect(toList());

    }

    /**
     * Delete facturepr by id.
     *
     * @param numBon
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public byte[] edition(String numBon)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        log.debug("logo *************************** {}", cliniqueDto.get(0).getLogoClinique());
        FacturePR facturepr = factureprRepository.findOne(numBon);
        FacturePREditionDTO dto = FacturePRFactory.factureprToFacturePREditionDTO(facturepr);
        DepartementDTO dp = paramAchatServiceClient.findDepartment(dto.getCoddepartDesti());
        dto.setDeparDestinationDesignation(dp.getDesignation());
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(dto.getCoddepotSrc());
        dto.setDepotDesignation(depot.getDesignation());

        List<Integer> codesUnites = new ArrayList();
        Set<Integer> setEmplacement = new HashSet();
        dto.getDetails().stream().forEach(item -> {
            codesUnites.add(item.getUnite());
            if (item.getCodeEmplacement() != null) {
                setEmplacement.add(item.getCodeEmplacement());
            }
        });

        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codesUnites);
        List<EmplacementDTO> emplacements = facturepr.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(setEmplacement,null) : new ArrayList();

        dto.getDetails().forEach(item -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getUnite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            item.setDesignationunite(unite.getDesignation());

            if (facturepr.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                EmplacementDTO emplacement = emplacements.stream()
                        .filter(emp -> emp.getCode().equals(item.getCodeEmplacement()))
                        .findFirst().orElse(null);
                if (item.getCodeEmplacement() != null) {
                    item.setDesignationEmplacement(emplacement.getDesignation());
                }
            }
        });
        List<PrelevementDetailDPR> listprelevment = prelevementDetailDPRService.findByCodePrelevement(dto.getNumbon());
        List<Integer> codedemande = listprelevment.stream().map(iteme -> iteme.getCodeDPR()).distinct().collect(Collectors.toList());
        Set<DemandePrDTO> setDPR = demandeServiceClient.findListDemandePr(codedemande, LocaleContextHolder.getLocale().getLanguage());
        List<DemandePrDTO> listedpr = setDPR.stream().collect(Collectors.toList());
        log.debug("list listedpr ***************** {}", listedpr);

        StringBuilder listDPRStrBuilder = new StringBuilder("");
        listedpr.stream().forEach(item -> {
            listDPRStrBuilder.append(item.getNumeroDemande()).append(", ");
        });
        dto.setListdpr(listDPRStrBuilder.toString());
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        if (facturepr.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            reportClientDoc.open("Reports/BonPrelevment_IMMO" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/BonPrelevment" + local + ".rpt", 0);
        }

        log.debug("list dpr ***************** {}", dto.getListdpr());

        reportClientDoc
                .getDatabaseController().setDataSource(dto.getDetails(), MvtStoPREditionDTO.class,
                        "Detaille", "Detaille");

        reportClientDoc
                .getDatabaseController().setDataSource(Arrays.asList(dto), FacturePREditionDTO.class,
                        "Entete", "Entete");

        reportClientDoc
                .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                "clinique", "clinique");

        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }

    @Transactional(readOnly = true)
    public List<MvtStoPR> findListDetails(CategorieDepotEnum categ, LocalDateTime fromDate, LocalDateTime toDate, Integer codeDepotSrc, Integer codeDepartDest) {
        log.debug("Request to get All FacturePRs");
        log.debug("categ est {}", categ);
        QFacturePR _FacturePR = QFacturePR.facturePR;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                //                .and(_FacturePR.typbon.eq(TypeBonEnum.PR))

                .and(_FacturePR.codAnnul.isNull())
                .optionalAnd(fromDate, () -> _FacturePR.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FacturePR.datbon.loe(toDate))
                .optionalAnd(categ, () -> _FacturePR.categDepot.eq(categ))
                .optionalAnd(codeDepotSrc, () -> _FacturePR.coddepotSrc.eq(codeDepotSrc))
                .optionalAnd(codeDepartDest, () -> _FacturePR.coddepartDest.eq(codeDepartDest));
        List<FacturePR> listFacturePRs = (List<FacturePR>) factureprRepository.findAll(builder);
        List<String> listFacturePRsIds = listFacturePRs.stream().map(FacturePR::getNumbon).collect(Collectors.toList());

        return mvtStoPRRepository.findByFacturePR_numbonIn(listFacturePRsIds);

    }

    @Transactional(readOnly = true)
    public List<MvtStoPRDTO> findListDetailsDTOGrouped(CategorieDepotEnum categ, LocalDateTime fromDate, LocalDateTime toDate, Integer codeDepotSrc, Integer codeDepartDest) {
        log.debug("Request to get All FacturePRs");

        List<MvtStoPR> listMvtstoPR = findListDetails(categ, fromDate, toDate, codeDepotSrc, codeDepartDest);

        MvtStoPRDTO mvtstoPrelevementDTO = new MvtStoPRDTO();
        mvtstoPrelevementDTO.setQuantiteNette(BigDecimal.ZERO);
        List<MvtStoPRDTO> listMvtstoPrDTO = new ArrayList();
        listMvtstoPR.stream()
                .map(mvtstoPr -> {
                    MvtStoPRDTO mvtstoPrDTO = MvtStoPRFactory.mvtstoprToMvtStoPRDTO(mvtstoPr);
                    return mvtstoPrDTO;
                })
                .collect(
                        groupingBy(MvtStoPRDTO::getArticleID,
                                (groupingBy(MvtStoPRDTO::getUnite,
                                        (groupingBy(MvtStoPRDTO::getDatPer,
                                                groupingBy(MvtStoPRDTO::getLotinter,
                                                        Collectors.reducing(mvtstoPrelevementDTO, (a, b) -> {
                                                            b.setQuantiteNette(b.getQuantiteNette().add(a.getQuantiteNette()));
                                                            return b;
                                                        }))
                                        ))))))
                .forEach((k, v) -> {
                    Collection<Map<LocalDate, Map<String, MvtStoPRDTO>>> secondLevelMap = v.values();
                    Collection<Map<String, MvtStoPRDTO>> thirdLevelMap = secondLevelMap.stream().flatMap(elt -> elt.values().stream()).collect(toList());
                    listMvtstoPrDTO.addAll(thirdLevelMap.stream().flatMap(elt -> elt.values().stream()).collect(toList()));
                });

        List<Integer> codeUnite = listMvtstoPrDTO.stream().map(item -> item.getUnite()).collect(Collectors.toList());
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnite);

        return listMvtstoPrDTO.stream()
                .map(item -> {
                    UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getUnite())).findFirst().orElse(null);
                    Preconditions.checkBusinessLogique(unite != null, "missing-unity");
                    item.setDesignationunite(unite.getDesignation());
                    return item;
                })
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public MvtStoPRDTO findByQrCodeAndUnity(String QRCode, Integer whareHouseSrcId, Integer departementDestId, LocalDateTime fromDate, LocalDateTime toDate) {
        MvtStoPR qMvtSto = QRCodeToMvtstoPR(QRCode);

        QMvtStoPR _MvtStoPR = QMvtStoPR.mvtStoPR;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                //                .and(_FacturePR.typbon.eq(TypeBonEnum.PR))

                .and(_MvtStoPR.facturePR().codAnnul.isNull())
                .and(_MvtStoPR.codart.eq(qMvtSto.getCodart()))
                .and(_MvtStoPR.unite.eq(qMvtSto.getUnite()))
                .optionalAnd(fromDate, () -> _MvtStoPR.facturePR().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _MvtStoPR.facturePR().datbon.loe(toDate))
                .optionalAnd(whareHouseSrcId, () -> _MvtStoPR.facturePR().coddepotSrc.eq(whareHouseSrcId))
                .optionalAnd(departementDestId, () -> _MvtStoPR.facturePR().coddepartDest.eq(departementDestId));
        Set<MvtStoPR> listMvtstoPRs = new HashSet((List<MvtStoPR>) mvtStoPRRepository.findAll(builder));
        log.debug("***liste size est :{}", listMvtstoPRs.size());
//        log.debug("listMvtstoPRs est :{}", listMvtstoPRs);
        log.debug("*****");

        MvtStoPRDTO result = MvtStoPRFactory.mvtstoprToMvtStoPRDTO(qMvtSto);
        result.setQuantiteNette(BigDecimal.ZERO);
        result.setQuantiteGlobaleNette(BigDecimal.ZERO);
        List<ArticleDTO> articleDTOs = paramAchatServiceClient.articleFindbyListCode(Collections.singletonList(result.getArticleID()));
        result.setCategDepot(articleDTOs.get(0).getCategorieDepot());
        result.setDesignation(articleDTOs.get(0).getDesignation());
        result.setSecondDesignation(articleDTOs.get(0).getDesignationSec());
        result.setCodeSaisi(articleDTOs.get(0).getCodeSaisi());
        result.setDesignationunite(articleDTOs.get(0).getDesignationUnite());
        result = listMvtstoPRs.stream()
                .reduce(result, (a, b) -> {
                    a.setQuantiteGlobaleNette(a.getQuantiteGlobaleNette().add(b.getQtecom()));
                    if (b.getLotinter().equals(a.getLotinter()) && b.getDatPer().equals(a.getDatPer())) {
                        a.setQuantiteNette(a.getQuantiteNette().add(b.getQtecom()));
                    }
                    return a;
                }, (a, b) -> {
                    return a;
                });

        return result;
    }

    private MvtStoPR QRCodeToMvtstoPR(String QRCode) {
        try {

            log.debug("the qr code is {}", QRCode);
            String[] decodedQR = QRCode.split(";");
            Integer codArt = Integer.parseInt(decodedQR[0]);
            LocalDate preemption = LocalDate.parse(decodedQR[1], DateTimeFormatter.BASIC_ISO_DATE);
            log.debug("la date de peremption est {}:", preemption);
            String lot = decodedQR[2];
            log.debug("le lot est {}:", lot);
            Integer unitID = Integer.parseInt(decodedQR[3]);
            MvtStoPR m = new MvtStoPR();
            m.setCodart(codArt);
            m.setUnite(unitID);
            m.setLotinter(lot);
            m.setDatPer(preemption);

            return m;
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
    public List<MvtStoPR> findByExample(Example<MvtStoPR> expl) {
        return mvtStoPRRepository.findAll(expl);
    }

    private List<ImmobilisationDTO> gererImmobilisation(List< ImmobilisationDTO> listeimmoDTOs, MvtStoPR mvtstoPR, ArticleIMMODTO articleIMMO, EmplacementDTO matchingDetailEmplacementDTO) {
        log.debug("*------------------------debut gerer immobilisation------------------------------*");
        BigDecimal availableQteNotUsed = listeimmoDTOs.stream()
                .filter(immo -> immo.getCodeArticle().equals(mvtstoPR.getCodeSaisi()) && immo.getDateDebutAmortissement() == null)
                .map(ImmobilisationDTO::getQuantite)
                .collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add));

//             
        Preconditions.checkBusinessLogique(availableQteNotUsed.compareTo(mvtstoPR.getQuantite()) >= 0, "insuffisant-qte-immo", mvtstoPR.getCodeSaisi(), availableQteNotUsed.toString());
        List<ImmobilisationDTO> listeUpdatedImmo = new ArrayList();
        BigDecimal qteMvtstoPR = mvtstoPR.getQuantite();
        //mm NumeroSerieet mm Emplacement 
        if (mvtstoPR.getCodeEmplacement() != null)//code emplacement not  null ==> matching emplacement non null ctrl deja fait
        {
            List<ImmobilisationDTO> firstCandidats = listeimmoDTOs.stream()
                    .filter(item -> item.getCodeArticle().equals(mvtstoPR.getCodeSaisi())
                    && item.getDateDebutAmortissement() == null
                    && mvtstoPR.getLotinter().equals(item.getNumeroSerie())
                    && matchingDetailEmplacementDTO.getCodeSaisi().equals(item.getCodeEmplacement())
                    ).collect(toList());

            log.debug("first Candidats size est {}", firstCandidats.size());

            for (ImmobilisationDTO firstCandid : firstCandidats) {
                log.debug("--------firstCandid--------{}", firstCandid);

                BigDecimal qteMin = qteMvtstoPR.min(firstCandid.getQuantite());

//            log.debug("---------qteMin----------{}", qteMin);
                qteMvtstoPR = qteMvtstoPR.subtract(qteMin);

//            if (articleIMMO.getTauxAmortissement() != null) {
//                Integer anneeAmortissement = 100 / articleIMMO.getTauxAmortissement().intValue();
//                log.debug("anneeAmortissement est {}", anneeAmortissement);
//
//                firstCandid.setDateFinAmortissement(Date.valueOf(LocalDate.now().plusYears(anneeAmortissement)));
//            }
                firstCandid.setDateDebutAmortissement(Date.valueOf(LocalDate.now()));
                firstCandid.setCodeEmplacement(matchingDetailEmplacementDTO.getCodeSaisi());

                listeUpdatedImmo.add(firstCandid);
                if (qteMvtstoPR.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }

            }
            if (qteMvtstoPR.compareTo(BigDecimal.ZERO) == 0) {
                return listeUpdatedImmo;
            }
        }
        log.debug("listeUpdatedImmo 1  size est {}", listeUpdatedImmo.size());
        //mm NumeroSerie(lot)
        List<ImmobilisationDTO> secondCandidats = listeimmoDTOs.
                stream()
                .filter(item -> item.getCodeArticle().equals(mvtstoPR.getCodeSaisi())
                && item.getDateDebutAmortissement() == null
                && item.getNumeroSerie().equals(mvtstoPR.getLotinter())
                ).collect(toList());

        log.debug("secondCandidats size est {}", secondCandidats.size());
        for (ImmobilisationDTO secondCandid : secondCandidats) {
            log.debug("--------secondCandid---------{}", secondCandid);

            BigDecimal qteMin = qteMvtstoPR.min(secondCandid.getQuantite());

//            log.debug("*************qteMin**************{}", qteMin);
            qteMvtstoPR = qteMvtstoPR.subtract(qteMin);

//            if (articleIMMO.getTauxAmortissement() != null) {
//                Integer anneeAmortissement = 100 / articleIMMO.getTauxAmortissement().intValue();
//                secondCandid.setDateFinAmortissement(Date.valueOf(LocalDate.now().plusYears(anneeAmortissement)));
//            }
            secondCandid.setDateDebutAmortissement(Date.valueOf(LocalDate.now()));
            if (mvtstoPR.getCodeEmplacement() != null) {
                secondCandid.setCodeEmplacement(matchingDetailEmplacementDTO.getCodeSaisi());
            }

            listeUpdatedImmo.add(secondCandid);
            if (qteMvtstoPR.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }

        }
        if (qteMvtstoPR.compareTo(BigDecimal.ZERO) == 0) {
            return listeUpdatedImmo;
        }
        log.debug("listeUpdatedImmo 2 est {}", listeUpdatedImmo.size());

        //mm Emplacement
        if (mvtstoPR.getCodeEmplacement() != null) {
            List<ImmobilisationDTO> thirdCandidats = listeimmoDTOs.
                    stream()
                    .filter(item -> item.getCodeArticle().equals(mvtstoPR.getCodeSaisi())
                    && item.getDateDebutAmortissement() == null
                    && matchingDetailEmplacementDTO.getCodeSaisi().equals(item.getCodeEmplacement())
                    ).collect(toList());

            log.debug("thirdCandidats size est {}", thirdCandidats.size());
            for (ImmobilisationDTO thirdCandid : thirdCandidats) {
                log.debug("-------------thirdCandid---------{}", thirdCandid);

                BigDecimal qteMin = qteMvtstoPR.min(thirdCandid.getQuantite());

//            log.debug("*************qteMin**************{}", qteMin);
                qteMvtstoPR = qteMvtstoPR.subtract(qteMin);

//            if (articleIMMO.getTauxAmortissement() != null) {
//                Integer anneeAmortissement = 100 / articleIMMO.getTauxAmortissement().intValue();
//                thirdCandid.setDateFinAmortissement(Date.valueOf(LocalDate.now().plusYears(anneeAmortissement)));
//            }
                thirdCandid.setCodeEmplacement(matchingDetailEmplacementDTO.getCodeSaisi());
                thirdCandid.setNumeroSerie(mvtstoPR.getLotinter());
                thirdCandid.setDateDebutAmortissement(Date.valueOf(LocalDate.now()));
                listeUpdatedImmo.add(thirdCandid);
                if (qteMvtstoPR.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }

            }
            if (qteMvtstoPR.compareTo(BigDecimal.ZERO) == 0) {
                return listeUpdatedImmo;
            }
        }
        log.debug("listeUpdatedImmo 3 est {}", listeUpdatedImmo.size());
        //autres
        List<ImmobilisationDTO> fourthCandidats = listeimmoDTOs.
                stream()
                .filter(item -> item.getCodeArticle().equals(mvtstoPR.getCodeSaisi())
                && item.getDateDebutAmortissement() == null
                //                && mvtstoPR.getCodeEmplacement().equals(item.getCodeEmplacement())
                ).collect(toList());

        log.debug("fourthCandidats size est {}", fourthCandidats.size());
        for (ImmobilisationDTO fourthCandid : fourthCandidats) {
            log.debug("----------fourthCandid-------------{}", fourthCandid);

            BigDecimal qteMin = qteMvtstoPR.min(fourthCandid.getQuantite());

//            log.debug("*************qteMin**************{}", qteMin);
            qteMvtstoPR = qteMvtstoPR.subtract(qteMin);

//            if (articleIMMO.getTauxAmortissement() != null) {
//                Integer anneeAmortissement = 100 / articleIMMO.getTauxAmortissement().intValue();
//                fourthCandid.setDateFinAmortissement(Date.valueOf(LocalDate.now().plusYears(anneeAmortissement)));
//            }
            fourthCandid.setNumeroSerie(mvtstoPR.getLotinter());
            if (mvtstoPR.getCodeEmplacement() != null)//code emplacement not  null ==> matching emplacement non null ctrl deja fait
            {
                fourthCandid.setCodeEmplacement(matchingDetailEmplacementDTO.getCodeSaisi());
            }
            fourthCandid.setDateDebutAmortissement(Date.valueOf(LocalDate.now()));
            listeUpdatedImmo.add(fourthCandid);
            if (qteMvtstoPR.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }

        }

        log.debug("listeUpdatedImmo 4 est {}", listeUpdatedImmo.size());
        log.debug("listeUpdatedImmo in gerer immo est {}", listeUpdatedImmo);
        return listeUpdatedImmo;
    }

    public ListeFacturePRDTOWrapper saveListeFacturePR(ListeFacturePRDTOWrapper liste) {
        log.debug("Request to save Liste FacturePR");
        ListeFacturePRDTOWrapper result = new ListeFacturePRDTOWrapper();
        List<FacturePRDTO> listeResulted = new ArrayList();
        liste.getListeFacturePRDTO().forEach(facturePr
                -> {
            FacturePRDTO resultedPR = save(facturePr);
            listeResulted.add(resultedPR);

        });
        result.setListeFacturePRDTO(listeResulted);
        return result;

    }

    @Transactional(readOnly = true)
    public List<FacturePRDTO> findLastFacturePRByCodart(CategorieDepotEnum categ, Integer codeDepot, Integer codeArticle) {

        int limit = Integer.parseInt(limitOfPrelevements);
        List<MvtStoPR> results = mvtStoPRRepository.findTop50ByCodartAndCodeDepotAndCodAnnulIsNullOrderByDatBonDesc(codeArticle, categ.categ(), codeDepot);
//        Page<MvtStoPR> results = mvtStoPRRepository.findByCodartAndCategDepotAndFacturePR_CoddepartDestAndFacturePR_CodAnnulIsNullOrderByFacturePR_DatbonDesc(codeArticle, categ, codeDepot, new PageRequest(0, limit));

        log.debug("results.getContent() sont {}", results);
        List<FacturePRDTO> listeFacturePR = results.stream()
                .map(elt -> {
                    FacturePRDTO dto = FacturePRFactory.factureprToFacturePRDTO(elt.getFacturePR());
                    return dto;
                }).distinct()
                .limit(limit)
                .collect(Collectors.toList());
        return listeFacturePR;

    }

}
