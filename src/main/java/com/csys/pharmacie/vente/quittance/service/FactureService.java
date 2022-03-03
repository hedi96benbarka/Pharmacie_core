package com.csys.pharmacie.vente.quittance.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.pharmacie.vente.quittance.dto.*;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.achat.dto.ArticleUuDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.DetailsPanierPrestDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.NatureDepotDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.service.FactureBAService;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.achat.service.ReceptionDetailCAService;
import com.csys.pharmacie.client.dto.ArticleDepotDto;
import com.csys.pharmacie.client.dto.OperationDTO;
import com.csys.pharmacie.helper.BaseAvoirQuittance;
import com.csys.pharmacie.helper.BaseTVADTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.MethodeTraitementdeStockEnum;
import com.csys.pharmacie.helper.ReceptionConstants;
import com.csys.pharmacie.helper.SatisfactionFactureEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.parametrage.entity.CompteurPharmacie;
import com.csys.pharmacie.parametrage.entity.Paramph;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.stock.domain.Decoupage;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.domain.DepstoDetailDecoupage;
import com.csys.pharmacie.stock.domain.DetailDecoupage;
import com.csys.pharmacie.stock.factory.DetailDecoupageFactory;
import com.csys.pharmacie.stock.repository.DecoupageRepository;
import com.csys.pharmacie.stock.repository.DepstoRepository;
import com.csys.pharmacie.vente.quittance.domain.DetailMvtsto;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.transfert.domain.Btfe;
import com.csys.pharmacie.transfert.domain.FactureBT;
import com.csys.pharmacie.transfert.repository.BtfeRepository;
import com.csys.pharmacie.transfert.service.BonTransfertInterDepotService;
import com.csys.pharmacie.vente.avoir.dto.FactureFactureAV;
import com.csys.pharmacie.vente.avoir.repository.MvtstomvtstoAVRepository;
import com.csys.pharmacie.vente.avoir.service.AvoirService;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import com.csys.pharmacie.vente.quittance.domain.FactureDR;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import com.csys.pharmacie.vente.quittance.domain.MvtstoPK;
import com.csys.pharmacie.vente.quittance.domain.QFacture;
import com.csys.pharmacie.vente.quittance.factory.FactureFactory;
import com.csys.pharmacie.vente.quittance.repository.FactureRepository;
import com.csys.pharmacie.vente.service.PricingService;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.csys.pharmacie.vente.quittance.repository.DetailMvtstoRepository;
import com.csys.util.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Service Implementation for managing Facture.
 */
@Service
@Transactional
public class FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureService.class);
    static String LANGUAGE_SEC;
    static Boolean BLOCKAGE_SALE_PRICE_PURCHASE;
    static long BLOCKING_DELAY_BETWEEN_QUITTANCES;
    static Boolean APPLY_MARGINAL_FOR_MEDICATION_ITEMS;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    @Value("${blockage-sale-price-purchase}")
    public void setBlockageSalePricePurchase(Boolean db) {
        BLOCKAGE_SALE_PRICE_PURCHASE = db;
    }

    @Value("${blocking-delay-between-quittances}")
    public void setBlockingDelayBetweenQuittances(long db) {
        BLOCKING_DELAY_BETWEEN_QUITTANCES = db;
    }

    @Value("${apply-marginal-for-medication-items}")
    public void setApplyMarginalForMedicationItems(Boolean db) {
        APPLY_MARGINAL_FOR_MEDICATION_ITEMS = db;
    }

    private final FactureRepository factureRepository;
    private final DetailMvtstoRepository detailMvtstoRepository;
    private final DepstoRepository depstoRepository;
    private final ParamServiceClient paramServiceClient;
    private final FactureFactory factureFactory;
    private final StockService stockService;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final ReceptionServiceClient receptionServiceClient;
    private final ParamService paramService;
    private final PricingService pricingService;
    private final BtfeRepository btfeRepository;
    private final FactureDRService factureDRService;
    private final InventaireService inventaireService;
    private final DecoupageRepository decoupageRepository;
    private final ParamServiceClient parametrageService;
    private final FactureBAService factureBAService;
    private final BonTransfertInterDepotService bonTransfertInterDepotService;
    private final ReceptionDetailCAService receptionDetailCAService;
    private final MvtstomvtstoAVRepository mvtstomvtstoAVRepository;
    @Autowired
    private AvoirService avoirService;

    public FactureService(FactureRepository factureRepository, DetailMvtstoRepository detailMvtstoRepository, DepstoRepository depstoRepository, ParamServiceClient paramServiceClient, FactureFactory factureFactory, StockService stockService, ParamAchatServiceClient paramAchatServiceClient, ReceptionServiceClient receptionServiceClient, ParamService paramService, PricingService pricingService, BtfeRepository btfeRepository, FactureDRService factureDRService, InventaireService inventaireService, DecoupageRepository decoupageRepository, ParamServiceClient parametrageService, FactureBAService factureBAService, BonTransfertInterDepotService bonTransfertInterDepotService, ReceptionDetailCAService receptionDetailCAService, MvtstomvtstoAVRepository mvtstomvtstoAVRepository) {
        this.factureRepository = factureRepository;
        this.detailMvtstoRepository = detailMvtstoRepository;
        this.depstoRepository = depstoRepository;
        this.paramServiceClient = paramServiceClient;
        this.factureFactory = factureFactory;
        this.stockService = stockService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.receptionServiceClient = receptionServiceClient;
        this.paramService = paramService;
        this.pricingService = pricingService;
        this.btfeRepository = btfeRepository;
        this.factureDRService = factureDRService;
        this.inventaireService = inventaireService;
        this.decoupageRepository = decoupageRepository;
        this.parametrageService = parametrageService;
        this.factureBAService = factureBAService;
        this.bonTransfertInterDepotService = bonTransfertInterDepotService;
        this.receptionDetailCAService = receptionDetailCAService;
        this.mvtstomvtstoAVRepository = mvtstomvtstoAVRepository;
    }

    /**
     * Save a quittanceDTO.
     *
     * @param quittanceDTO
     * @param pharmacieExterne
     * @return the persisted entity
     */
    public List<FactureDTO> save(QuittanceDTO quittanceDTO, Boolean pharmacieExterne) throws NoSuchAlgorithmException {
        checkBusinessLogique(receptionServiceClient.CheckEtatPatientInPatient(quittanceDTO.getNumdoss()), "return.add.patientInexistant", quittanceDTO.getNumdoss());
        DepotDTO depotDTO = paramAchatServiceClient.findDepotByCode(quittanceDTO.getCoddep());
        checkBusinessLogique(!depotDTO.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
        checkBusinessLogique(!depotDTO.getDepotFrs(), "depot.source.fournisseur", depotDTO.getDesignation());
        //find societe pec
        SocieteDTO societe = receptionServiceClient.findSocieteByCodeAdmission(quittanceDTO.getNumdoss());
        List<Facture> listFacture = createListFacture(quittanceDTO, pharmacieExterne, societe, depotDTO);
        if (pharmacieExterne) {
            Paramph param = paramService.findparambycode("numbonComplementaireQuittance");
            String numbonComplementaire = "II" + param.getValeur();
            param.setValeur(Helper.IncrementString(param.getValeur(), 7));
            paramService.updateparam(param);
            for (Facture facture : listFacture) {
                facture.setNumbonComplementaire(numbonComplementaire);
            }
        }
        log.debug(" codePrestation {}", quittanceDTO.getCodePrestation());
        //update mnt  prestation
        if (quittanceDTO.getCodePrestation() != null) {
            updateMntPanier(listFacture, quittanceDTO.getCodePrestation(), societe, pharmacieExterne, quittanceDTO.getQuantitePrestation());
        }
        if (quittanceDTO.getCodeOperation() != null) {
            updateMntPanierOperation(listFacture, quittanceDTO.getCodeOperation(), societe, pharmacieExterne);
        }
        List<FactureDTO> listFacturesDTO = verificationMontantFacture(listFacture, java.util.Arrays.asList(depotDTO), quittanceDTO);
        return listFacturesDTO;
    }

    /**
     * create liste des factures a partie de quittanceDTO.
     *
     * @param quittanceDTO
     * @param pharmacieExterne test sur pharmacie externe
     * @param societe societe prise en charge
     * @param depotDTO depot source
     * @return the persisted entity
     */
    public List<Facture> createListFacture(QuittanceDTO quittanceDTO, Boolean pharmacieExterne, SocieteDTO societe, DepotDTO depotDTO) throws NoSuchAlgorithmException {
        List<Facture> listFacture = new ArrayList<>();
        List<Depsto> listDepstoTrace = new ArrayList<>();
        List<DetailMvtsto> listDetailMvtstoTrace = new ArrayList<>();
        List<Integer> codarts = quittanceDTO.getMvtQuittance().stream().map(x -> x.getCodart()).collect(Collectors.toList());
        List<Integer> codartDepstos = codarts;
        //find article
        List<ArticleDTO> listArticleDTOs = paramAchatServiceClient.articleFindbyListCode(codarts);
        List<Integer> categArticleIDs = listArticleDTOs.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
        //test inventaire
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, quittanceDTO.getCoddep());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = listArticleDTOs.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }
        //get remise conventionnel article
        List<RemiseConventionnelleDTO> remiseConventionnelles = paramServiceClient.pricelisteParArticle(codarts, quittanceDTO.getNumdoss(), pharmacieExterne);
        listArticleDTOs.forEach(x -> {
            RemiseConventionnelleDTO remiseConventionnelle = remiseConventionnelles.stream().filter(
                    item -> item.getCodeArticle().equals(x.getCode())).findFirst().orElse(null);
            checkBusinessLogique(remiseConventionnelle != null, "error-remise-article");
            x.setRemiseConventionnelle(remiseConventionnelle);
        });
        //affection des articles et nature depot
        List<MvtQuittanceDTO> mvtQuittances = new ArrayList<>();
        quittanceDTO.getMvtQuittance().forEach(x -> {
            MvtQuittanceDTO mvtQuittance = x;
            ArticleDTO articleDTO = listArticleDTOs.stream().filter(y -> y.getCode().equals(x.getCodart())).collect(Collectors.toList()).get(0);
            checkBusinessLogique(!articleDTO.getDesignation().equals("Not Available"), "return.add.article-inexistant", mvtQuittance.getCodart().toString());
            checkBusinessLogique(articleDTO.getActif(), "return.add.article-inactif", articleDTO.getDesignation());
            if (x.getDatPer() != null) {
                checkBusinessLogique(x.getDatPer().compareTo(LocalDate.now()) > 0, "date.peremption.invalide", x.getDatPer().toString(), articleDTO.getDesignation());
            }
            mvtQuittance.setArticle(articleDTO);
            mvtQuittances.add(mvtQuittance);
        });
        //groupement des mouvements by categorie article
        List<CategorieDepotEnum> categorieDepots = mvtQuittances.stream().map(item -> item.getArticle().getCategorieDepot()).distinct().collect(Collectors.toList());
        checkBusinessLogique(!categorieDepots.contains(CategorieDepotEnum.EC), "article-economat");
        quittanceDTO.setMvtQuittance(mvtQuittances);
        Map<CategorieDepotEnum, List<MvtQuittanceDTO>> groupByMvtQuittance
                = quittanceDTO.getMvtQuittance().stream().collect(Collectors.groupingBy(x -> x.getArticle().getCategorieDepot()));
        //traitement des mouvements by categorie article
        for (Map.Entry<CategorieDepotEnum, List<MvtQuittanceDTO>> entry : groupByMvtQuittance.entrySet()) {
            CategorieDepotEnum categDepot = entry.getKey();
            List<MvtQuittanceDTO> mvtQuittance = entry.getValue();
            String numBon = paramService.getcompteur(categDepot, TypeBonEnum.FE);
            // find depsto
            List<Depsto> depstos = stockService.findByCodartInAndCoddepAndQteGreaterThanAndDatPerGreaterThan(codartDepstos, quittanceDTO.getCoddep());
            checkBusinessLogique(!depstos.isEmpty(), "quittance.add.stock-inavailable");
            //find article by categorie article
            List<ArticlePHDTO> articlePHDTOs = new ArrayList<>();
            List<ArticleUuDTO> articleUuDTOs = new ArrayList<>();
            if (categDepot.equals(CategorieDepotEnum.PH)) {
                codarts = mvtQuittance.stream().map(x -> x.getCodart()).collect(Collectors.toList());
                articlePHDTOs = paramAchatServiceClient.articlePHFindbyListCode(codarts);
            } else if (categDepot.equals(CategorieDepotEnum.UU)) {
                codarts = mvtQuittance.stream().map(x -> x.getCodart()).collect(Collectors.toList());
                articleUuDTOs = paramAchatServiceClient.articleUUFindbyListCode(codarts, quittanceDTO.getCoddep(), true, true);
            }
            //creation facture
            Facture facture = new Facture();
            facture.setSatisf(SatisfactionFactureEnum.NOT_RECOVRED);
            facture.setMemop(quittanceDTO.getMemop());
            facture.setNumdoss(quittanceDTO.getNumdoss());
            facture.setEtatbon(false);
            facture.setCoddep(quittanceDTO.getCoddep());
            facture.setCodeDemande(quittanceDTO.getCodeDemande());
            if (societe != null) {
                facture.setCodeSociete(societe.getCode());
                facture.setRemiseConventionnellePharmacie(societe.getRemiseConventionnellePharmacie());
            }
            facture.setCodeDetailsAdmission(quittanceDTO.getCodeDetailsAdmission());
            facture.setIdOrdonnance(quittanceDTO.getIdOrdonnance());
            facture.setNumbon(numBon);
            facture.setTypbon(TypeBonEnum.FE);
            facture.setCategDepot(categDepot);
            facture.setPanier(Boolean.FALSE);
            facture.setIntegrer(Boolean.FALSE);

            //traitement CostCenterAnalytique
            AdmissionDTO admissionDTO = receptionServiceClient.findAdmissionDTOByCodeAdmission(quittanceDTO.getNumdoss());
            checkBusinessLogique(admissionDTO != null && admissionDTO.getCode() != null, "admission.notFound", quittanceDTO.getNumdoss());
//si condition est true cette admission est inpatient => set costCenter
            if (admissionDTO.getLit() != null && admissionDTO.getLit().getNumLit() != null) {
                LitDTO litDTO = parametrageService.litFindOne(admissionDTO.getLit().getNumLit());
                checkBusinessLogique(litDTO != null, "lit.notFound", admissionDTO.getLit().getNumLit());
                facture.setCodeCostCenterAnalytique(litDTO.getCodeCostCenterAnalytique());
            }

            List<Mvtsto> details = new ArrayList<>();
            //Sort details quittance selon date per
            Collections.sort(quittanceDTO.getMvtQuittance(), (p1, p2) -> {
                if (p1.getDatPer() != null && p2.getDatPer() != null) {
                    return p1.getDatPer().compareTo(p2.getDatPer());
                } else if (p2.getDatPer() == null && p1.getDatPer() == null) {
                    return 0;
                } else if (p2.getDatPer() != null && p1.getDatPer() == null) {
                    return 1;
                } else {
                    return -1;
                }
            });
            //traitement details quittance by categorie article
            String numordre = "0001";
            for (MvtQuittanceDTO detailFacture : mvtQuittance) {
                checkBusinessLogique((detailFacture.getLot() == null && detailFacture.getDatPer() == null) || (detailFacture.getLot() != null && detailFacture.getDatPer() != null), "return.add.datePerOrLotMissing", detailFacture.getCodart().toString());
                BigDecimal qte = detailFacture.getQuantite();
                List<Depsto> depstoByArticle
                        = getDepstoByArticle(depstos, detailFacture, categDepot, depotDTO, qte, articlePHDTOs);
                BigDecimal maxPriuniDepsto = BigDecimal.ZERO;
                BigDecimal priAchMoyen = BigDecimal.ZERO;
                //create new mvtsto by categorie article
                Mvtsto mvtsto = new Mvtsto();
                String ordre = "FE" + numordre;
                MvtstoPK pk = new MvtstoPK();
                pk.setCodart(detailFacture.getCodart());
                pk.setNumbon(numBon);
                pk.setNumordre(ordre);
                mvtsto.setMvtstoPK(pk);
                //traitemenet depsto and trace depsto
                for (Depsto depsto : depstoByArticle) {
                    BigDecimal qteToRmv = qte.min(depsto.getQte());
                    if (qteToRmv.compareTo(BigDecimal.ZERO) > 0) {
                        depsto.setQte(depsto.getQte().subtract(qteToRmv));
                        qte = qte.subtract(qteToRmv);
                        maxPriuniDepsto = maxPriuniDepsto.max(depsto.getPu());
                        priAchMoyen = priAchMoyen.add(qteToRmv.multiply(depsto.getPu()));
                        DetailMvtsto detailMvtsto = new DetailMvtsto(depsto, mvtsto);
                        detailMvtsto.setQte(qteToRmv);
                        listDetailMvtstoTrace.add(detailMvtsto);
                        listDepstoTrace.add(depsto);
                    }

                    if (qte.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                }
                detailFacture.getArticle().setPrixAchat(priAchMoyen.divide(detailFacture.getQuantite(), 7, RoundingMode.HALF_UP));
                //create mvtosto
                createMvtsto(mvtsto, detailFacture, categDepot, articlePHDTOs, articleUuDTOs, maxPriuniDepsto, depotDTO, quittanceDTO, pharmacieExterne);
                mvtsto.setFacture(facture);
                details.add(mvtsto);
                numordre = Helper.IncrementString(numordre, 4);
                log.debug("*** End of treating article {} ****", detailFacture.getCodart());
            }
            facture.setMvtstoCollection(details);
            String code = facture.getNumdoss().concat(facture.getCoddep().toString());
            for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                code = code.concat(mvtsto.getMvtstoPK().getCodart().toString());
            }
            byte[] hashCode = Helper.hashing(code);
            if (!pharmacieExterne) {
                List<Facture> listFactureByHashCodeAndDatbonGreaterThan = factureRepository.findByHashCodeAndDatbonGreaterThan(hashCode, LocalDateTime.now().minusMinutes(BLOCKING_DELAY_BETWEEN_QUITTANCES));
                checkBusinessLogique(listFactureByHashCodeAndDatbonGreaterThan.isEmpty(), "blockage.quittance.double");
            }
            facture.setHashCode(hashCode);
            //calcul mnt bon
            List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
            facture.calcul(listTvas, societe, pharmacieExterne);

            DepartementDTO departementDTO = paramAchatServiceClient.findDepartment(quittanceDTO.getCoddep());
            facture.setCodeCostCenter(departementDTO.getCodeCostCenter());

            paramService.updateCompteurPharmacie(facture.getCategDepot(), TypeBonEnum.FE);
            //----------traitement factureDR-----------//
            if (quittanceDTO.getFactureDr() != null) {
                facture.setNumpiece(quittanceDTO.getFactureDr().getNumbon());
                facture.setDatepiece(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
            }
            //--------------------------//
            listFacture.add(facture);

        }
        //-------------- traitement factureDR -----------//
        if (quittanceDTO.getFactureDr() != null) {
            FactureDR factureDr = factureDRService.update(quittanceDTO.getFactureDr(), quittanceDTO.getMvtQuittance());
        }
        //------------------------------//
        listFacture = factureRepository.save(listFacture);
        depstoRepository.save(listDepstoTrace);
        detailMvtstoRepository.save(listDetailMvtstoTrace);
        return listFacture;
    }

    /**
     * facturation liste des factures .
     *
     * @param reglementDTOs
     * @param numdoss
     * @param listFactureDTO
     * @return the persisted entity
     */
    /**
     * Save a factureDTO.
     *
     * @param quittanceDTO
     * @param pharmacieExterne
     * @return the persisted entity
     */
    public List<FactureDTO> saveDepotFrs(QuittanceDTO quittanceDTO, Boolean pharmacieExterne) throws NoSuchAlgorithmException {
        log.debug("Request to save Facture: {}", quittanceDTO);
        DepotDTO depotDTO = paramAchatServiceClient.findDepotByCode(quittanceDTO.getCoddep());
        checkBusinessLogique(!depotDTO.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
        checkBusinessLogique(depotDTO.getDepotFrs(), "depot.source.not.fournisseur", depotDTO.getDesignation());
        //find societe pec
        SocieteDTO societe = receptionServiceClient.findSocieteByCodeAdmission(quittanceDTO.getNumdoss());
        checkBusinessLogique(receptionServiceClient.CheckEtatPatientInPatient(quittanceDTO.getNumdoss()), "return.add.patientInexistant", quittanceDTO.getNumdoss());
        List<Facture> listFacture = createListFactureDepotFrs(quittanceDTO, pharmacieExterne, societe, depotDTO);
        if (pharmacieExterne) {
            Paramph param = paramService.findparambycode("numbonComplementaireQuittance");
            String numbonComplementaire = "II" + param.getValeur();
            param.setValeur(Helper.IncrementString(param.getValeur(), 7));
            paramService.updateparam(param);
            for (Facture facture : listFacture) {
                facture.setNumbonComplementaire(numbonComplementaire);
            }
        }
        //update mnt  prestation
        if (quittanceDTO.getCodePrestation() != null) {
            updateMntPanier(listFacture, quittanceDTO.getCodePrestation(), societe, pharmacieExterne, quittanceDTO.getQuantitePrestation());
        }
        if (quittanceDTO.getCodeOperation() != null) {
            updateMntPanierOperation(listFacture, quittanceDTO.getCodeOperation(), societe, pharmacieExterne);
        }
        List<FactureDTO> listFacturesDTO = verificationMontantFacture(listFacture, java.util.Arrays.asList(depotDTO), quittanceDTO);
        return listFacturesDTO;
    }

    /**
     * Save a quittance and quittance depot fournisseur.
     *
     * @param quittanceDTO
     * @param pharmacieExterne
     * @return the persisted entity
     */
    public List<FactureDTO> saveQuittanceAndQuittanceDepotFrs(QuittanceAndQuittanceDepotFrs quittanceDTO, Boolean pharmacieExterne) throws NoSuchAlgorithmException {
        if (quittanceDTO.getQuittanceDepotFrs() != null) {
            checkBusinessLogique(quittanceDTO.getQuittance().getNumdoss().equals(quittanceDTO.getQuittanceDepotFrs().getNumdoss()), "quittance.unique.dossier");
            String numdoss = quittanceDTO.getQuittance().getNumdoss();
            checkBusinessLogique(receptionServiceClient.CheckEtatPatientInPatient(numdoss), "return.add.patientInexistant", numdoss);
            DepotDTO depotFrs = paramAchatServiceClient.findDepotByCode(quittanceDTO.getQuittanceDepotFrs().getCoddep());
            checkBusinessLogique(!depotFrs.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
            checkBusinessLogique(depotFrs.getDepotFrs(), "depot.source.not.fournisseur", depotFrs.getDesignation());
            SocieteDTO societe = receptionServiceClient.findSocieteByCodeAdmission(numdoss);
            List<Facture> listFacture = createListFactureDepotFrs(quittanceDTO.getQuittanceDepotFrs(), pharmacieExterne, societe, depotFrs);

            DepotDTO depot = paramAchatServiceClient.findDepotByCode(quittanceDTO.getQuittance().getCoddep());
            checkBusinessLogique(!depot.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
            checkBusinessLogique(!depot.getDepotFrs(), "depot.source.fournisseur", depot.getDesignation());
            listFacture.addAll(createListFacture(quittanceDTO.getQuittance(), pharmacieExterne, societe, depot));
            List<DepotDTO> depots = new ArrayList<>();
            depots.add(depot);
            depots.add(depotFrs);
            if (pharmacieExterne) {
                Paramph param = paramService.findparambycode("numbonComplementaireQuittance");
                String numbonComplementaire = "II" + param.getValeur();
                param.setValeur(Helper.IncrementString(param.getValeur(), 7));
                paramService.updateparam(param);
                for (Facture facture : listFacture) {
                    facture.setNumbonComplementaire(numbonComplementaire);
                }
            }
            //update mnt  prestation
            if (quittanceDTO.getCodePrestation() != null) {
                updateMntPanier(listFacture, quittanceDTO.getCodePrestation(), societe, pharmacieExterne, quittanceDTO.getQuantitePrestation());
            }

            if (quittanceDTO.getCodeOperation() != null) {
                updateMntPanierOperation(listFacture, quittanceDTO.getCodeOperation(), societe, pharmacieExterne);
            }
            //traitement facturation (module facturation)
            List<FactureDTO> listFacturesDTO = verificationMontantFacture(listFacture, depots, null);
            return listFacturesDTO;
        } else {
            String numdoss = quittanceDTO.getQuittance().getNumdoss();
            checkBusinessLogique(receptionServiceClient.CheckEtatPatientInPatient(numdoss), "return.add.patientInexistant", numdoss);
            DepotDTO depot = paramAchatServiceClient.findDepotByCode(quittanceDTO.getQuittance().getCoddep());
            checkBusinessLogique(!depot.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
            checkBusinessLogique(!depot.getDepotFrs(), "depot.source.fournisseur", depot.getDesignation());
            SocieteDTO societe = receptionServiceClient.findSocieteByCodeAdmission(numdoss);
            List<Facture> listFacture = createListFacture(quittanceDTO.getQuittance(), pharmacieExterne, societe, depot);
            List<DepotDTO> depots = new ArrayList<>();
            depots.add(depot);
            if (pharmacieExterne) {
                Paramph param = paramService.findparambycode("numbonComplementaireQuittance");
                String numbonComplementaire = "II" + param.getValeur();
                param.setValeur(Helper.IncrementString(param.getValeur(), 7));
                paramService.updateparam(param);
                for (Facture facture : listFacture) {
                    facture.setNumbonComplementaire(numbonComplementaire);
                }
            }
            //update mnt  prestation
            if (quittanceDTO.getCodePrestation() != null) {
                updateMntPanier(listFacture, quittanceDTO.getCodePrestation(), societe, pharmacieExterne, quittanceDTO.getQuantitePrestation());
            }

            if (quittanceDTO.getCodeOperation() != null) {
                updateMntPanierOperation(listFacture, quittanceDTO.getCodeOperation(), societe, pharmacieExterne);
            }
            //traitement facturation (module facturation)
            List<FactureDTO> listFacturesDTO = verificationMontantFacture(listFacture, depots, null);
            return listFacturesDTO;
        }

    }

    /**
     * Save a liste des quittances and quittances depot fournisseur.
     *
     * @param quittanceDTOs
     * @param pharmacieExterne
     * @return the persisted entity
     */
    public List<FactureDTO> saveListQuittanceAndQuittanceDepotFrs(List<QuittanceAndQuittanceDepotFrs> quittanceDTOs, Boolean pharmacieExterne) throws NoSuchAlgorithmException {
        List<FactureDTO> listFactureDTO = new ArrayList<>();
        List<String> listNumdoss = quittanceDTOs.stream().map(quittanceDTO -> quittanceDTO.getQuittance().getNumdoss()).distinct().collect(Collectors.toList());
        listNumdoss.addAll(quittanceDTOs.stream().map(quittanceDTO -> quittanceDTO.getQuittance().getNumdoss()).distinct().collect(Collectors.toList()));
        listNumdoss = listNumdoss.stream().distinct().collect(Collectors.toList());
        checkBusinessLogique(listNumdoss.size() == 1, "quittance.unique.dossier");
        String numdoss = listNumdoss.get(0);
        checkBusinessLogique(receptionServiceClient.CheckEtatPatientInPatient(numdoss), "return.add.patientInexistant", numdoss);
        SocieteDTO societe = receptionServiceClient.findSocieteByCodeAdmission(numdoss);
        for (QuittanceAndQuittanceDepotFrs quittanceDTO : quittanceDTOs) {
            if (quittanceDTO.getQuittanceDepotFrs() != null && quittanceDTO.getQuittance() != null && quittanceDTO.getQuittance().getMvtQuittance().size() > 0) {
                log.debug("getQuittance + getQuittanceDepotFrs");
                DepotDTO depotFrs = paramAchatServiceClient.findDepotByCode(quittanceDTO.getQuittanceDepotFrs().getCoddep());
                checkBusinessLogique(!depotFrs.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
                checkBusinessLogique(depotFrs.getDepotFrs(), "depot.source.not.fournisseur", depotFrs.getDesignation());
                List<Facture> listFacture = createListFactureDepotFrs(quittanceDTO.getQuittanceDepotFrs(), pharmacieExterne, societe, depotFrs);
                DepotDTO depot = paramAchatServiceClient.findDepotByCode(quittanceDTO.getQuittance().getCoddep());
                checkBusinessLogique(!depot.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
                checkBusinessLogique(!depot.getDepotFrs(), "depot.source.fournisseur", depot.getDesignation());
                listFacture.addAll(createListFacture(quittanceDTO.getQuittance(), pharmacieExterne, societe, depot));
                List<DepotDTO> depots = new ArrayList<>();
                depots.add(depot);
                depots.add(depotFrs);
                if (pharmacieExterne) {
                    Paramph param = paramService.findparambycode("numbonComplementaireQuittance");
                    String numbonComplementaire = "II" + param.getValeur();
                    param.setValeur(Helper.IncrementString(param.getValeur(), 7));
                    paramService.updateparam(param);
                    for (Facture facture : listFacture) {
                        facture.setNumbonComplementaire(numbonComplementaire);
                    }
                }
                //update mnt  prestation
                if (quittanceDTO.getCodePrestation() != null) {
                    updateMntPanier(listFacture, quittanceDTO.getCodePrestation(), societe, pharmacieExterne, quittanceDTO.getQuantitePrestation());
                }

                if (quittanceDTO.getCodeOperation() != null) {
                    updateMntPanierOperation(listFacture, quittanceDTO.getCodeOperation(), societe, pharmacieExterne);
                }
//                //traitement facturation (module facturation)
                List<FactureDTO> listFacturesDTO = verificationMontantFacture(listFacture, depots, null);
                listFactureDTO.addAll(listFacturesDTO);
            } else if (quittanceDTO.getQuittanceDepotFrs() == null && quittanceDTO.getQuittance() != null && quittanceDTO.getQuittance().getMvtQuittance().size() > 0) {
                log.debug("getQuittance");
                DepotDTO depot = paramAchatServiceClient.findDepotByCode(quittanceDTO.getQuittance().getCoddep());
                checkBusinessLogique(!depot.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
                checkBusinessLogique(!depot.getDepotFrs(), "depot.source.fournisseur", depot.getDesignation());
                List<Facture> listFacture = createListFacture(quittanceDTO.getQuittance(), pharmacieExterne, societe, depot);
                List<DepotDTO> depots = new ArrayList<>();
                depots.add(depot);
                if (pharmacieExterne) {
                    Paramph param = paramService.findparambycode("numbonComplementaireQuittance");
                    String numbonComplementaire = "II" + param.getValeur();
                    param.setValeur(Helper.IncrementString(param.getValeur(), 7));
                    paramService.updateparam(param);
                    for (Facture facture : listFacture) {
                        facture.setNumbonComplementaire(numbonComplementaire);
                    }
                }
                //update mnt  prestation
                if (quittanceDTO.getCodePrestation() != null) {
                    updateMntPanier(listFacture, quittanceDTO.getCodePrestation(), societe, pharmacieExterne, quittanceDTO.getQuantitePrestation());
                }

                if (quittanceDTO.getCodeOperation() != null) {
                    updateMntPanierOperation(listFacture, quittanceDTO.getCodeOperation(), societe, pharmacieExterne);
                }
                //traitement facturation (module facturation)
                List<FactureDTO> listFacturesDTO = verificationMontantFacture(listFacture, depots, null);
                listFactureDTO.addAll(listFacturesDTO);
            } else if (quittanceDTO.getQuittanceDepotFrs() != null && (quittanceDTO.getQuittance() == null || quittanceDTO.getQuittance().getMvtQuittance().isEmpty())) {
                log.debug("getQuittanceDepotFrs");
                DepotDTO depotFrs = paramAchatServiceClient.findDepotByCode(quittanceDTO.getQuittanceDepotFrs().getCoddep());
                checkBusinessLogique(!depotFrs.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
                checkBusinessLogique(depotFrs.getDepotFrs(), "depot.source.not.fournisseur", depotFrs.getDesignation());
                List<Facture> listFacture = createListFactureDepotFrs(quittanceDTO.getQuittanceDepotFrs(), pharmacieExterne, societe, depotFrs);
                List<DepotDTO> depots = new ArrayList<>();
                depots.add(depotFrs);
                if (pharmacieExterne) {
                    Paramph param = paramService.findparambycode("numbonComplementaireQuittance");
                    String numbonComplementaire = "II" + param.getValeur();
                    param.setValeur(Helper.IncrementString(param.getValeur(), 7));
                    paramService.updateparam(param);
                    for (Facture facture : listFacture) {
                        facture.setNumbonComplementaire(numbonComplementaire);
                    }
                }
                //update mnt  prestation
                if (quittanceDTO.getCodePrestation() != null) {
                    updateMntPanier(listFacture, quittanceDTO.getCodePrestation(), societe, pharmacieExterne, quittanceDTO.getQuantitePrestation());
                }

                if (quittanceDTO.getCodeOperation() != null) {
                    updateMntPanierOperation(listFacture, quittanceDTO.getCodeOperation(), societe, pharmacieExterne);
                }
                //traitement facturation (module facturation)
                List<FactureDTO> listFacturesDTO = verificationMontantFacture(listFacture, depots, null);
                listFactureDTO.addAll(listFacturesDTO);
            }

        }

        return listFactureDTO;
    }

    /**
     * create liste des factures depot fournisseur a partie de quittanceDTO.
     *
     * @param quittanceDTO
     * @param pharmacieExterne test sur pharmacie externe
     * @param societe societe prise en charge
     * @param depotDTO depot source
     * @return the persisted entity
     * @throws java.security.NoSuchAlgorithmException
     */
    public List<Facture> createListFactureDepotFrs(QuittanceDTO quittanceDTO, Boolean pharmacieExterne, SocieteDTO societe, DepotDTO depotDTO) throws NoSuchAlgorithmException {

        List<Facture> listFacture = new ArrayList<>();
        List<Integer> codarts = quittanceDTO.getMvtQuittance().stream().map(x -> x.getCodart()).collect(Collectors.toList());
        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(quittanceDTO.getCodeFournisseur());
        checkBusinessLogique(!fournisseurDTO.getDesignation().equals("fournisseur.deleted"), "Fournisseur avec code : " + quittanceDTO.getCodeFournisseur() + " est introuvable");
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseurDTO.getAnnule()) && !(Boolean.TRUE.equals(fournisseurDTO.getStopped())), "item.stopped", fournisseurDTO.getCode());

        List<ArticleDTO> listArticleDTOs = paramAchatServiceClient.articleFindbyListCode(codarts);
        List<ArticleDTO> listPrixArticleDTOs = paramAchatServiceClient.articleParFournisseurFindbyListCode(codarts, quittanceDTO.getCodeFournisseur());
        listArticleDTOs.forEach(x -> {
            Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(x.getAnnule()) && !(Boolean.TRUE.equals(x.getStopped())), "item.stopped", x.getCodeSaisi());
            BigDecimal prixAchatParFournisseur = listPrixArticleDTOs.stream().filter(y -> y.getCode().equals(x.getCode()))
                    .findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-prix-achat-article")).getPrixAchat();
            Preconditions.checkBusinessLogique(prixAchatParFournisseur.compareTo(new BigDecimal(0.01)) > 0, "prix-achat-onshelf-zero", x.getCodeSaisi());
            x.setPrixAchat(prixAchatParFournisseur);
        });
        List<RemiseConventionnelleDTO> remiseConventionnelles = paramServiceClient.pricelisteParArticle(codarts, quittanceDTO.getNumdoss(), pharmacieExterne);
        listArticleDTOs.forEach(x -> {
            RemiseConventionnelleDTO remiseConventionnelle = remiseConventionnelles.stream().filter(
                    item -> item.getCodeArticle().equals(x.getCode())).findFirst().orElse(null);
            checkBusinessLogique(remiseConventionnelle != null, "error-remise-article");
            x.setRemiseConventionnelle(remiseConventionnelle);
        });
        List<MvtQuittanceDTO> mvtQuittances = new ArrayList<>();
        quittanceDTO.getMvtQuittance().forEach(x -> {
            MvtQuittanceDTO mvtQuittance = x;
            ArticleDTO articleDTO = listArticleDTOs.stream().filter(y -> y.getCode().equals(x.getCodart())).collect(Collectors.toList()).get(0);
            checkBusinessLogique(!articleDTO.getDesignation().equals("Not Available"), "return.add.article-inexistant", mvtQuittance.getCodart().toString());
            checkBusinessLogique(articleDTO.getActif(), "return.add.article-inactif", articleDTO.getDesignation());
            if (x.getPrixAchat() != null) {
                //prix achat saisi par utilusateur (dmi)
                articleDTO.setPrixAchat(x.getPrixAchat());
            }
            if (x.getValeurTvaAch() != null && x.getCodeTvaAch() != null) {
                articleDTO.setCodeTvaAch(x.getCodeTvaAch());
                articleDTO.setValeurTvaAch(x.getValeurTvaAch());
            }
            if (x.getDatPer() != null) {
                checkBusinessLogique(x.getDatPer().compareTo(LocalDate.now()) > 0, "date.peremption.invalide", x.getDatPer().toString(), articleDTO.getDesignation());
            }
            mvtQuittance.setArticle(articleDTO);

            if (articleDTO.getCategorieDepot().equals(CategorieDepotEnum.PH)
                    && !Boolean.TRUE.equals(pharmacieExterne) && Boolean.TRUE.equals(APPLY_MARGINAL_FOR_MEDICATION_ITEMS)) {
                checkBusinessLogique(mvtQuittance.getUnite().equals(articleDTO.getCodeUnite()), "error-unity-must-be-main-unit-for-onshelf", articleDTO.getCodeSaisi());
                checkBusinessLogique(articleDTO.getMarge() != null, "error-item-without-marge", articleDTO.getCodeSaisi());

                BigDecimal marginalValue = articleDTO.getMarge().divide(BigDecimal.valueOf(100.0), 4);
                BigDecimal prixVenteAvecMarge = articleDTO.getPrixAchat().multiply(marginalValue.add(BigDecimal.ONE)).setScale(2, RoundingMode.HALF_UP);
                mvtQuittance.setPrixVente(prixVenteAvecMarge);
            }

            if (articleDTO.getCategorieDepot().equals(CategorieDepotEnum.UU)) {
                mvtQuittance.setPrixVente(pricingService.resolveSellingPrice(depotDTO.getMaquette(), articleDTO.getCategorieArticle(), articleDTO.getPrixAchat()));
            }
            mvtQuittances.add(mvtQuittance);
        });
        quittanceDTO.getMvtQuittance().forEach(mvtQtce -> {
            NatureDepotDTO natureDepot = depotDTO.getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(mvtQtce.getArticle().getCategorieDepot())).findFirst().orElse(null);
            checkBusinessLogique(natureDepot != null, "error-article-depot", mvtQtce.getArticle().getDesignation());
        });
        List<CategorieDepotEnum> categorieDepots = mvtQuittances.stream().map(item -> item.getArticle().getCategorieDepot()).distinct().collect(Collectors.toList());
        checkBusinessLogique(!categorieDepots.contains(CategorieDepotEnum.EC), "article-economat");
        quittanceDTO.setMvtQuittance(mvtQuittances);
        Map<CategorieDepotEnum, List<MvtQuittanceDTO>> groupByMvtQuittance
                = quittanceDTO.getMvtQuittance().stream().collect(Collectors.groupingBy(x -> x.getArticle().getCategorieDepot()));
        for (Map.Entry<CategorieDepotEnum, List<MvtQuittanceDTO>> entry : groupByMvtQuittance.entrySet()) {
            CategorieDepotEnum categDepot = entry.getKey();
            List<MvtQuittanceDTO> mvtQuittance = entry.getValue();
            String numBon = paramService.getcompteur(categDepot, TypeBonEnum.FE);
            List<ArticlePHDTO> articlePHDTOs = new ArrayList<>();
            List<ArticleUuDTO> articleUuDTOs = new ArrayList<>();
            DepotDTO depotPrincipale = paramAchatServiceClient.findDepotPrincipalByCategorieDepot(categDepot);
            checkBusinessLogique(!depotPrincipale.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
            checkBusinessLogique(!depotPrincipale.getDepotFrs(), "depot.source.not.fournisseur", depotPrincipale.getDesignation());

            if (categDepot.equals(CategorieDepotEnum.PH)) {
                codarts = mvtQuittance.stream().map(x -> x.getCodart()).collect(Collectors.toList());
                articlePHDTOs = paramAchatServiceClient.articlePHFindbyListCode(codarts);
            } else if (categDepot.equals(CategorieDepotEnum.UU)) {
                codarts = mvtQuittance.stream().map(x -> x.getCodart()).collect(Collectors.toList());
                articleUuDTOs = paramAchatServiceClient.articleUUFindbyListCode(codarts, quittanceDTO.getCoddep(), true, true);
            }
            Facture facture = new Facture();
            facture.setSatisf(SatisfactionFactureEnum.NOT_RECOVRED);
            facture.setMemop(quittanceDTO.getMemop());
            facture.setNumdoss(quittanceDTO.getNumdoss());
            facture.setEtatbon(false);
            facture.setCoddep(quittanceDTO.getCoddep());
            facture.setCodeDemande(quittanceDTO.getCodeDemande());
            facture.setCodeDetailsAdmission(quittanceDTO.getCodeDetailsAdmission());
            facture.setIdOrdonnance(quittanceDTO.getIdOrdonnance());
            if (societe != null) {
                facture.setCodeSociete(societe.getCode());
                facture.setRemiseConventionnellePharmacie(societe.getRemiseConventionnellePharmacie());
            }
            facture.setNumbon(numBon);
            facture.setTypbon(TypeBonEnum.FE);
            facture.setCategDepot(categDepot);
            facture.setPanier(Boolean.FALSE);
            facture.setIntegrer(Boolean.FALSE);
            List<Mvtsto> details = new ArrayList<>();
            String numordre = "0001";
            for (MvtQuittanceDTO detailFacture : mvtQuittance) {
                checkBusinessLogique((detailFacture.getLot() == null && detailFacture.getDatPer() == null) || (detailFacture.getLot() != null && detailFacture.getDatPer() != null), "return.add.datePerOrLotMissing", detailFacture.getCodart().toString());
                log.debug("*** Begin treating article {} ****", detailFacture.toString());
                BigDecimal priuni = detailFacture.getPrixAchat();
                Mvtsto mvtsto = new Mvtsto();
                String ordre = "FE" + numordre;
                MvtstoPK pk = new MvtstoPK();
                pk.setCodart(detailFacture.getCodart());
                pk.setNumbon(numBon);
                pk.setNumordre(ordre);
                mvtsto.setMvtstoPK(pk);
                //create mvtosto
                createMvtsto(mvtsto, detailFacture, categDepot, articlePHDTOs, articleUuDTOs, priuni, depotDTO, quittanceDTO, pharmacieExterne);
                mvtsto.setFacture(facture);
                details.add(mvtsto);
                numordre = Helper.IncrementString(numordre, 4);
                log.debug("*** End of treating article {} ****", detailFacture.getCodart());
            }
            facture.setMvtstoCollection(details);
            String code = facture.getNumdoss().concat(facture.getCoddep().toString());
            for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                code = code.concat(mvtsto.getMvtstoPK().getCodart().toString());
            }
            byte[] hashCode = Helper.hashing(code);
            if (!pharmacieExterne) {
                List<Facture> listFactureByHashCodeAndDatbonGreaterThan = factureRepository.findByHashCodeAndDatbonGreaterThan(hashCode, LocalDateTime.now().minusMinutes(BLOCKING_DELAY_BETWEEN_QUITTANCES));
                checkBusinessLogique(listFactureByHashCodeAndDatbonGreaterThan.isEmpty(), "blockage.quittance.double");
            }
            facture.setHashCode(hashCode);
            List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
            facture.calcul(listTvas, societe, pharmacieExterne);
            paramService.updateCompteurPharmacie(facture.getCategDepot(), TypeBonEnum.FE);

            DepartementDTO departementDTO = paramAchatServiceClient.findDepartment(quittanceDTO.getCoddep());
            facture.setCodeCostCenter(departementDTO.getCodeCostCenter());
            //----------traitement factureDR-----------//
            if (quittanceDTO.getFactureDr() != null) {
                facture.setNumpiece(quittanceDTO.getFactureDr().getNumbon());
                facture.setDatepiece(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
            }
            //----------traitement bon de reception -----------//
            facture.setDatbon(LocalDateTime.now());
            facture.setCodfrs(fournisseurDTO.getCode());
            if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
                facture.setReffrs(fournisseurDTO.getDesignationSec());
                facture.setReffrsAr(fournisseurDTO.getDesignation());
            } else {
                facture.setReffrs(fournisseurDTO.getDesignation());
                facture.setReffrsAr(fournisseurDTO.getDesignationSec());
            }
            String exoneration = paramService.findValeurOptionVersionPharmacieByID("exoneration");
            FactureBA factureBA = factureBAService.ajoutBonReceptionDepotFrs(facture, depotPrincipale, fournisseurDTO, quittanceDTO.getAppliquerExoneration(), exoneration);
            FactureBT factureBT = bonTransfertInterDepotService.addBonTransferInterDepotDepotFrs(factureBA, depotPrincipale, depotDTO);
            if (factureBA.getFournisseurExonere()) {
                facture.getMvtstoCollection().forEach(mvtsto -> {
                    mvtsto.setCodTvaAch(5);
                    mvtsto.setTauTvaAch(BigDecimal.ZERO);
                });
            }
            facture.setNumbonRecept(factureBA.getNumbon());
            facture.setNumbonTransfert(factureBT.getNumbon());
            listFacture.add(facture);

        }
        //-------------- traitement factureDR -----------//
        if (quittanceDTO.getFactureDr() != null) {
            FactureDR factureDr = factureDRService.update(quittanceDTO.getFactureDr(), quittanceDTO.getMvtQuittance());
        }
        listFacture = factureRepository.save(listFacture);
        return listFacture;
    }

    /**
     * get liste des depsto filtrer par lot et date perem.
     *
     * @param depstos
     * @param detailFacture
     * @param categDepot
     * @param qte
     * @param depotDTO
     * @param articlePHDTOs
     * @return
     */
    public List<Depsto> getDepstoByArticle(List<Depsto> depstos, MvtQuittanceDTO detailFacture, CategorieDepotEnum categDepot, DepotDTO depotDTO, BigDecimal qte, List<ArticlePHDTO> articlePHDTOs) {
        log.debug("*****getDepstoByArticle*****");
        Boolean Bloque_Quittance = paramService.findOptionVersionPharmacieByID("Bloque_Quittance").getValeur().equalsIgnoreCase("n");
        //filter depsto by codart and unite and lot and dateper
        List<Depsto> depstoByArticle = depstos.stream()
                .filter(item -> {
                    Boolean VerifCodart = item.getCodart().equals(detailFacture.getCodart());
                    Boolean VerifUnite = item.getUnite().equals(detailFacture.getUnite());
                    Boolean VerifLotInte = detailFacture.getLot() != null ? item.getLotInter().equals(detailFacture.getLot()) : Boolean.TRUE;
                    Boolean VerifDatPer = detailFacture.getDatPer() != null ? item.getDatPer().equals(detailFacture.getDatPer()) : Boolean.TRUE;
                    return VerifCodart && VerifUnite && VerifLotInte && VerifDatPer;
                }).collect(Collectors.toList());
        //sort depstoByArticle by methode traitement 
        if (depotDTO.getMethodeTraitementdeStock().equals(MethodeTraitementdeStockEnum.FEFO)) {
            Collections.sort(depstoByArticle, (p1, p2) -> p1.getDatPer().compareTo(p2.getDatPer()));
        } else if (depotDTO.getMethodeTraitementdeStock().equals(MethodeTraitementdeStockEnum.FIFO)) {
            Collections.sort(depstoByArticle, (p1, p2) -> p1.getDatesys().compareTo(p2.getDatesys()));// TODO heureSys
        }
        // test available qte meme lot and dateper
        Integer availableQteInStock = depstoByArticle.stream()
                .filter(item -> item.getCodart().equals(detailFacture.getCodart()))
                .map(filtredItem -> filtredItem.getQte().intValue())
                .collect(Collectors.summingInt(Integer::new));
        //filter depsto by codart and unite if bloque =  no and qte available < qte demande
        if (Bloque_Quittance && availableQteInStock < qte.intValue()) {
            List<Depsto> depstoByArticleWithoutDatperLot = depstos.stream()
                    .filter(item -> {
                        Boolean VerifCodart = item.getCodart().equals(detailFacture.getCodart());
                        Boolean VerifUnite = item.getUnite().equals(detailFacture.getUnite());
                        return VerifCodart && VerifUnite;
                    }).filter(x -> !depstoByArticle.contains(x)).collect(Collectors.toList());
            //sort depstoByArticle by methode traitement 
            if (depotDTO.getMethodeTraitementdeStock().equals(MethodeTraitementdeStockEnum.FEFO)) {
                Collections.sort(depstoByArticleWithoutDatperLot, (p1, p2) -> p1.getDatPer().compareTo(p2.getDatPer()));
            } else if (depotDTO.getMethodeTraitementdeStock().equals(MethodeTraitementdeStockEnum.FIFO)) {
                Collections.sort(depstoByArticleWithoutDatperLot, (p1, p2) -> p1.getDatesys().compareTo(p2.getDatesys()));
            }
            depstoByArticle.addAll(depstoByArticleWithoutDatperLot);
        }
        // test available qte
        availableQteInStock = depstoByArticle.stream()
                .filter(item -> item.getCodart().equals(detailFacture.getCodart()))
                .map(filtredItem -> filtredItem.getQte().intValue())
                .collect(Collectors.summingInt(Integer::new));

        //decoupage and filter depsto by codart and unite before decoupage
        if (categDepot.equals(CategorieDepotEnum.PH) && availableQteInStock < qte.intValue()) {
            ArticlePHDTO matchedArticle = articlePHDTOs.stream().filter(x -> x.getCode().equals(detailFacture.getCodart())).findFirst().orElse(null);
            checkBusinessLogique(matchedArticle != null, "return.add.article-inexistant", detailFacture.getCodart().toString());
            //decoupage
            createDecoupage(depstos, availableQteInStock, detailFacture, matchedArticle);
            //filter depsto by codart and unite and lot and dateper
            List<Depsto> depstoByArticleAfterDecoupage = depstos.stream()
                    .filter(item -> {
                        Boolean VerifCodart = item.getCodart().equals(detailFacture.getCodart());
                        Boolean VerifUnite = item.getUnite().equals(detailFacture.getUnite());
                        Boolean VerifLotInte = detailFacture.getLot() != null ? item.getLotInter().equals(detailFacture.getLot()) : Boolean.TRUE;
                        Boolean VerifDatPer = detailFacture.getDatPer() != null ? item.getDatPer().equals(detailFacture.getDatPer()) : Boolean.TRUE;
                        return VerifCodart && VerifUnite && VerifLotInte && VerifDatPer;
                    }).filter(x -> !depstoByArticle.contains(x)).collect(Collectors.toList());
            //sort depstoByArticle by methode traitement 
            if (depotDTO.getMethodeTraitementdeStock().equals(MethodeTraitementdeStockEnum.FEFO)) {
                Collections.sort(depstoByArticle, (p1, p2) -> p1.getDatPer().compareTo(p2.getDatPer()));
            } else if (depotDTO.getMethodeTraitementdeStock().equals(MethodeTraitementdeStockEnum.FIFO)) {
                Collections.sort(depstoByArticle, (p1, p2) -> p1.getDatesys().compareTo(p2.getDatesys()));// TODO heureSys
            }
            depstoByArticle.addAll(depstoByArticleAfterDecoupage);
            // test available qte meme lot and dateper
            availableQteInStock = depstoByArticle.stream()
                    .filter(item -> item.getCodart().equals(detailFacture.getCodart()))
                    .map(filtredItem -> filtredItem.getQte().intValue())
                    .collect(Collectors.summingInt(Integer::new));
            //filter depsto by codart and unite if bloque =  no and qte available < qte demande
            if (Bloque_Quittance && availableQteInStock < qte.intValue()) {
                List<Depsto> depstoByArticleWithoutDatperLotAfterDecoupage = depstos.stream()
                        .filter(item -> {
                            Boolean VerifCodart = item.getCodart().equals(detailFacture.getCodart());
                            Boolean VerifUnite = item.getUnite().equals(detailFacture.getUnite());
                            return VerifCodart && VerifUnite;
                        }).filter(x -> !depstoByArticle.contains(x)).collect(Collectors.toList());
                //sort depstoByArticle by methode traitement 
                if (depotDTO.getMethodeTraitementdeStock().equals(MethodeTraitementdeStockEnum.FEFO)) {
                    Collections.sort(depstoByArticleWithoutDatperLotAfterDecoupage, (p1, p2) -> p1.getDatPer().compareTo(p2.getDatPer()));
                } else if (depotDTO.getMethodeTraitementdeStock().equals(MethodeTraitementdeStockEnum.FIFO)) {
                    Collections.sort(depstoByArticleWithoutDatperLotAfterDecoupage, (p1, p2) -> p1.getDatesys().compareTo(p2.getDatesys()));
                }
                depstoByArticle.addAll(depstoByArticleWithoutDatperLotAfterDecoupage);
            }
            // test available qte
            availableQteInStock = depstoByArticle.stream()
                    .filter(item -> item.getCodart().equals(detailFacture.getCodart()))
                    .map(filtredItem -> filtredItem.getQte().intValue())
                    .collect(Collectors.summingInt(Integer::new));
        }
        checkBusinessLogique(availableQteInStock >= qte.intValue(), "insuffisant-qte", detailFacture.getArticle().getCodeSaisi(), availableQteInStock.toString());
        return depstoByArticle;
    }

    /**
     * decoupage liste des depsto.
     *
     * @param stock
     * @param quantiteValable
     * @param mvt
     * @param matchedArticle
     *
     */
    public void createDecoupage(List<Depsto> stock, Integer quantiteValable, MvtQuittanceDTO mvt, ArticlePHDTO matchedArticle) {

        log.debug("decoupage debut ");

        ArticleUniteDTO finalUnit = matchedArticle.getArticleUnites().stream().filter(unity -> unity.getCodeUnite().equals(mvt.getUnite())).findFirst().orElse(null);
        checkBusinessLogique(finalUnit != null, "missing-unity");
        List<ArticleUniteDTO> fromUnities = matchedArticle.getArticleUnites().stream().filter(unity -> unity.getNbPiece().compareTo(finalUnit.getNbPiece()) < 0).collect(Collectors.toList());
        Collections.sort(fromUnities, (p1, p2) -> {
            return p2.getNbPiece().compareTo(p1.getNbPiece());
        });
        CompteurPharmacie compteurDecoupage = paramService.findcompteurbycode(CategorieDepotEnum.PH, TypeBonEnum.DC);
        boolean needMore = true;
        BigDecimal remainingQty = mvt.getQuantite().subtract(new BigDecimal(quantiteValable));

        while (needMore) {
            Boolean decoupedAtLeastOnce = false;
            // preparing decoupage
            Decoupage decoupage = new Decoupage();
            decoupage.setNumbon(CategorieDepotEnum.PH + compteurDecoupage.toString());
            decoupage.setCategDepot(CategorieDepotEnum.PH);
            decoupage.setCoddep(stock.get(0).getCoddep());
            decoupage.setTypbon(TypeBonEnum.DC);
            decoupage.setAuto(true);
            List<DetailDecoupage> detailsDecoupage = new ArrayList();
            decoupage.setDetailDecoupageList(detailsDecoupage);

            ArticleUniteDTO previousUnit = finalUnit;
            BigDecimal remainingQtyInCurrentUnit = remainingQty;
            for (ArticleUniteDTO fromUnit : fromUnities) {
                remainingQtyInCurrentUnit = (remainingQtyInCurrentUnit.multiply(fromUnit.getNbPiece())).divide(previousUnit.getNbPiece(), 0, RoundingMode.UP);
                log.debug("remaining qty  in  {} is {}", fromUnit, remainingQtyInCurrentUnit);
                List<Depsto> candidatesForDecoupage = stock.stream().filter(item -> item.getCodart().equals(mvt.getCodart()) && item.getUnite().equals(fromUnit.getCodeUnite()) && item.getQte().compareTo(BigDecimal.ZERO) > 0).collect(toList());

                for (Depsto candidate : candidatesForDecoupage) {
                    log.debug("candidate {}", candidate);
                    BigDecimal qtyToDecoup = remainingQtyInCurrentUnit.min(candidate.getQte());
                    if (qtyToDecoup.compareTo(BigDecimal.ZERO) > 0) {
                        decoupedAtLeastOnce = true;
                    }
                    log.debug("decouping {} {}", qtyToDecoup, fromUnit.getUnityDesignation());
                    BigDecimal unitMultiplicand = previousUnit.getNbPiece().divide(fromUnit.getNbPiece(), 0);
                    BigDecimal obtainedQty = qtyToDecoup.multiply(unitMultiplicand);
                    log.debug("obtained qty is {} {} ", obtainedQty, previousUnit.getUnityDesignation());

                    // Creating Decoupage
                    DetailDecoupage detailDecoup = DetailDecoupageFactory.mvtQuittanceDTOToDetailDecoupage(mvt);
                    detailDecoup.setCategDepot(CategorieDepotEnum.PH);
                    detailDecoup.setDecoupage(decoupage);
                    detailDecoup.setCodeDecoupage(decoupage.getNumbon());
                    detailDecoup.setUniteOrigine(fromUnit.getCodeUnite());
                    detailDecoup.setUniteFinal(previousUnit.getCodeUnite());
                    detailDecoup.setQuantite(qtyToDecoup);
                    detailDecoup.setQuantiteObtenue(obtainedQty);
                    detailsDecoupage.add(detailDecoup);
                    List<DepstoDetailDecoupage> depstoDetailDecoupList = new ArrayList();
                    detailDecoup.setDepstoDetailDecoupageList(depstoDetailDecoupList);
                    DepstoDetailDecoupage depstoDetailDecoup = new DepstoDetailDecoupage(qtyToDecoup, candidate.getQte(), candidate);
                    depstoDetailDecoup.setDetailDecoupage(detailDecoup);
                    depstoDetailDecoupList.add(depstoDetailDecoup);

                    // processing the storage
                    candidate.setQte(candidate.getQte().subtract(qtyToDecoup));

                    Depsto newDepsto = new Depsto(candidate);
                    newDepsto.setQte(obtainedQty);
                    newDepsto.setUnite(previousUnit.getCodeUnite());
                    newDepsto.setPu(candidate.getPu().divide(unitMultiplicand, 7, RoundingMode.HALF_UP));
                    newDepsto.setNumBon(detailDecoup.getCodeDecoupage());
                    newDepsto.setNumBonOrigin(candidate.getNumBonOrigin());
                    stockService.saveDepsto(newDepsto);
                    stock.add(newDepsto);
                    if (previousUnit.getCodeUnite().equals(finalUnit.getCodeUnite())) {
                        remainingQty = remainingQty.subtract(obtainedQty.min(remainingQty));
                    }
                    log.debug("remainingQty qty after decouping {} {} is {}", qtyToDecoup, fromUnit.getUnityDesignation(), remainingQty);
                    remainingQtyInCurrentUnit = remainingQtyInCurrentUnit.subtract(qtyToDecoup);
                    if (remainingQty.compareTo(BigDecimal.ZERO) == 0) {
                        break;
                    }

                }
                previousUnit = fromUnit;
                if (remainingQty.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }

            BigDecimal qtyAfterDecoup = stock.stream().filter(item -> item.getCodart().equals(mvt.getCodart()) && item.getUnite().equals(mvt.getUnite())).map(Depsto::getQte).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
            log.debug("qtyAfterDecoup qty {}", qtyAfterDecoup);
            checkBusinessLogique(decoupedAtLeastOnce, "insuffisant-qte", matchedArticle.getDesignation(), qtyAfterDecoup.toBigInteger().toString());
            needMore = qtyAfterDecoup.compareTo(mvt.getQuantite()) < 0;
            decoupageRepository.save(decoupage);
            String suffixe = compteurDecoupage.getP2();
            compteurDecoupage.setP2(Helper.IncrementString(suffixe, suffixe.length()));

        }

    }

    /**
     * update montant bon panier prestation
     *
     */
    private void updateMntPanier(List<Facture> listFacture, Integer codePrestation, SocieteDTO societe, Boolean pharmacieExterne, BigDecimal quantitePrestation) {
        //update mnt  prestation
        log.debug(" codePrestation {}", codePrestation);
        BigDecimal mntBons = listFacture.stream()
                .map(facure -> facure.getMntbon())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ajustement = BigDecimal.valueOf(100);
        Paramph param = paramService.findparambycode("numbonPanier");
        String numbonPanier = param.getValeur();
        param.setValeur(Helper.IncrementString(param.getValeur(), 7));
        paramService.updateparam(param);
        for (Facture facture : listFacture) {
            facture.setPanier(Boolean.TRUE);
            if (facture.getNumbonPanier() == null) {
                facture.setNumbonPanier(numbonPanier);
            }
            facture.setCodePrestation(codePrestation);
            facture.setQuantitePrestation(quantitePrestation);
            log.debug(" mnt bon {}: {}", facture.getNumbon(), facture.getMntbon());
        }
        DetailsPanierPrestDTO detailsPanierPrestation = paramServiceClient.detailsPanierBycodePrestation(codePrestation);
        log.debug("Prix Fixe Panier: {}", detailsPanierPrestation.getPrixFixePanier());
        if (!detailsPanierPrestation.getFacturationPanier() && detailsPanierPrestation.getPrixFixePanier().compareTo(BigDecimal.ZERO) > 0) {
            ajustement = mntBons.subtract(detailsPanierPrestation.getPrixFixePanier().multiply(quantitePrestation)).multiply(ajustement).divide(mntBons, 7, RoundingMode.HALF_UP);
        }
        log.debug("ajustement {}", ajustement);
        if (!detailsPanierPrestation.getFacturationPanier()) {
            for (Facture facture : listFacture) {
                for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                    mvtsto.setAjustement(ajustement);
                    mvtsto.setPriuni(mvtsto.getPriuni().multiply((BigDecimal.valueOf(100).subtract(mvtsto.getAjustement())).divide(BigDecimal.valueOf(100)).setScale(7, RoundingMode.HALF_UP)));
                    mvtsto.setMontht(mvtsto.getPriuni().multiply(mvtsto.getQuantite()));
                }
                List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
                facture.calcul(listTvas, societe, pharmacieExterne);
            }
        }
    }

    /**
     * update mnt selon prix fixe panier operation (une facture contient qu'une
     * seulr operation panier)
     */
    private void updateMntPanierOperation(List<Facture> listFacture, Integer codeOperation, SocieteDTO societe, Boolean pharmacieExterne) {
        //update mnt  prestation
        log.debug("methode updateMntPanierOperation codeOperation {}", codeOperation);
        BigDecimal mntBons = listFacture.stream()
                .map(facure -> facure.getMntbon())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ajustement = BigDecimal.valueOf(100);
        Paramph param = paramService.findparambycode("numbonPanier");
        String numbonPanier = param.getValeur();
        param.setValeur(Helper.IncrementString(param.getValeur(), 7));
        paramService.updateparam(param);
        for (Facture facture : listFacture) {
            facture.setPanier(Boolean.TRUE);
            if (facture.getNumbonPanier() == null) {
                facture.setNumbonPanier(numbonPanier);
            }
            facture.setCodeOperation(codeOperation);
            log.debug(" mnt bon {}: {}", facture.getNumbon(), facture.getMntbon());
        }
        OperationDTO operationDTO = paramServiceClient.findOperationbyCode(codeOperation);
        log.debug("Prix Fixe Panier operation: {}", operationDTO.getPrixFixePanier());
        if (!operationDTO.getFacturationPanier()) {
            Preconditions.checkBusinessLogique(operationDTO.getPrixFixePanier() != null, "operation.prix.fixe.not.exist", operationDTO.getDesignation());
            if (operationDTO.getPrixFixePanier().compareTo(BigDecimal.ZERO) > 0) {
                ajustement = mntBons.subtract(operationDTO.getPrixFixePanier()).multiply(ajustement).divide(mntBons, 7, RoundingMode.HALF_UP);
            }
        }
        log.debug("ajustement operation{}", ajustement);
        if (!operationDTO.getFacturationPanier()) {
            for (Facture facture : listFacture) {
                for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                    mvtsto.setAjustement(ajustement);
                    mvtsto.setPriuni(mvtsto.getPriuni().multiply((BigDecimal.valueOf(100).subtract(mvtsto.getAjustement())).divide(BigDecimal.valueOf(100)).setScale(7, RoundingMode.HALF_UP)));
                    mvtsto.setMontht(mvtsto.getPriuni().multiply(mvtsto.getQuantite()));
                }
                List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
                facture.calcul(listTvas, societe, pharmacieExterne);
            }
        }
    }

    public List<FactureDTO> verificationMontantFacture(List<Facture> listFacture, List<DepotDTO> depotDTOs, QuittanceDTO quittanceDTO) {
        //test mnt
        if (quittanceDTO != null) {
            BigDecimal mntbon = listFacture.stream()
                    .map(Facture::getMntbon).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            BigDecimal partiePEC = listFacture.stream()
                    .map(Facture::getPartiePEC).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            BigDecimal partiePatient = listFacture.stream()
                    .map(Facture::getPartiePatient).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
            log.debug("********  mnt bon calculé  {} ***********", mntbon);
            log.debug("**********     mnt bon     {}   ************", quittanceDTO.getMntbon());
            log.debug("********  partiePEC calculé  {} ***********", partiePEC);
            log.debug("**********     partiePEC bon     {}   ************", quittanceDTO.getPartiePEC());
            log.debug("********  partiePatient calculé  {} ***********", partiePatient);
            log.debug("**********     partiePatient bon     {}   ************", quittanceDTO.getPartiePatient());

            checkBusinessLogique(quittanceDTO.getMntbon() == null || mntbon.compareTo(quittanceDTO.getMntbon()) == 0, "error-montant-bon");
            checkBusinessLogique(quittanceDTO.getPartiePEC() == null || partiePEC.compareTo(quittanceDTO.getPartiePEC()) == 0, "error-montant-bon");
            checkBusinessLogique(quittanceDTO.getPartiePatient() == null || partiePatient.compareTo(quittanceDTO.getPartiePatient()) == 0, "error-montant-bon");

        }
        for (Facture facture : listFacture) {
            facture.setMntbon(facture.getMntbon().setScale(2, RoundingMode.HALF_UP));
            facture.setPartiePEC(facture.getPartiePEC().setScale(2, RoundingMode.HALF_UP));
            facture.setPartiePatient(facture.getPartiePatient().setScale(2, RoundingMode.HALF_UP));
        }

        listFacture = factureRepository.save(listFacture);
        List<FactureDTO> listFactureDTO = factureFactory.factureToFactureDTOLazys(listFacture);
        listFactureDTO.forEach(factureDTO -> {
            NatureDepotDTO natureDepot = depotDTOs.stream().filter(item -> item.getCode().equals(factureDTO.getCodeDepot())).findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("Depot introuvable"))
                    .getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(factureDTO.getCategDepot())).findFirst().orElse(null);
            checkBusinessLogique(natureDepot != null, "missing-prestation-depot");
            factureDTO.setMntmpl(BigDecimal.valueOf(factureDTO.getMvtstoCollection().stream().map(x -> (x.getPrixBrute().multiply(x.getQuantite())).multiply(BigDecimal.valueOf(100).add(x.getTautva())).divide(BigDecimal.valueOf(100))).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
            factureDTO.setCodePrestation(natureDepot.getPrestationID());
        });
        return listFactureDTO;
    }

    /**
     * create mvtsto .
     *
     */
    private void createMvtsto(Mvtsto mvtsto, MvtQuittanceDTO detailFacture, CategorieDepotEnum categDepot, List<ArticlePHDTO> articlePHDTOs, List<ArticleUuDTO> articleUuDTOs, BigDecimal priuni, DepotDTO depotDTO, QuittanceDTO quittanceDTO, Boolean pharmacieExterne) {
        log.debug("*******createMvtsto*******");
        mvtsto.setRemise(BigDecimal.ZERO);
        mvtsto.setMajoration(BigDecimal.ZERO);
        BigDecimal prixVente = BigDecimal.ZERO;
        if ("MAJ".equals(detailFacture.getArticle().getRemiseConventionnelle().getNatureException())) {
            mvtsto.setMajoration(detailFacture.getArticle().getRemiseConventionnelle().getTaux());
        } else {
            mvtsto.setRemise(detailFacture.getArticle().getRemiseConventionnelle().getTaux());
        }
        prixVente = detailFacture.getPrixVente();
        if (prixVente == null) {
            if (categDepot.equals(CategorieDepotEnum.PH)) {
                ArticlePHDTO matchedArticle = articlePHDTOs.stream().filter(x -> x.getCode().equals(detailFacture.getCodart())).findFirst().orElse(null);
                checkBusinessLogique(!matchedArticle.getDesignation().equals("Not Available"), "return.add.article-inexistant", matchedArticle.getCode().toString());
                if (!Boolean.TRUE.equals(pharmacieExterne) && Boolean.TRUE.equals(APPLY_MARGINAL_FOR_MEDICATION_ITEMS)) {
                    if (matchedArticle.getPrixAchat().compareTo(priuni) > 0) {
                        prixVente = matchedArticle.
                                getArticleUnites().stream().filter(y -> y.getCodeUnite().equals(detailFacture.getUnite())).collect(Collectors.toList()).get(0).getPrixVenteAvecMarge();
                    } else {
                        //priuni c'est le max du depsto 
                        //si on a priuni > prixAchat on applique la marge sur priuni
                        checkBusinessLogique(matchedArticle.getMarge() != null, "error-item-without-marge", matchedArticle.getCodeSaisi());
                        BigDecimal marginalValue = matchedArticle.getMarge().divide(BigDecimal.valueOf(100.0), 4);
                        prixVente = priuni.multiply(marginalValue.add(BigDecimal.ONE)).setScale(2, RoundingMode.HALF_UP);
                    }
                } else {
                    prixVente = matchedArticle.
                            getArticleUnites().stream().filter(y -> y.getCodeUnite().equals(detailFacture.getUnite())).collect(Collectors.toList()).get(0).getPrixVente();
                }

            } else if (categDepot.equals(CategorieDepotEnum.UU)) {
                ArticleUuDTO articleUuDTO = articleUuDTOs.stream().filter(x -> x.getCode().equals(detailFacture.getCodart())).collect(Collectors.toList()).get(0);
                checkBusinessLogique(!articleUuDTO.getDesignation().equals("Not Available"), "return.add.article-inexistant", articleUuDTO.getCode().toString());
                Optional<ArticleDepotDto> articleDepotDto = articleUuDTO.getDepotArticleCollection().stream()
                        .filter(x -> x.getCodeDesignationDepot().getCode().equals(depotDTO.getCode()))
                        .findFirst();
                Preconditions.checkBusinessLogique(articleDepotDto.isPresent(), "must-insert-prix-by-depot", articleUuDTO.getDesignation());
                log.debug("articleDepotDto.get()******* : {}", articleDepotDto.get().toString());
                if (articleDepotDto.get().getPrixFixe()) {
                    prixVente = articleUuDTO.getPrixVente();
                } else if (Boolean.TRUE.equals(articleDepotDto.get().getMargeFixe()) && BigDecimal.ZERO.compareTo(articleDepotDto.get().getMarge()) <= 0) {
                    log.debug("articleUuDTO.getReferencePrice()******* : {}", articleUuDTO.getReferencePrice());
                    log.debug("priuni ********** : {}", priuni);
                    if (articleUuDTO.getReferencePrice().compareTo(priuni) > 0) {
                        prixVente = articleUuDTO.getReferencePrice().multiply(BigDecimal.valueOf(100).add(articleDepotDto.get().getMarge())).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
                    } else {
                        prixVente = priuni.multiply(BigDecimal.valueOf(100).add(articleDepotDto.get().getMarge())).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
                    }
                } else {
                    if (articleUuDTO.getReferencePrice().compareTo(priuni) > 0) {
                        prixVente = articleUuDTO.getPrixVente();
                    } else {
                        prixVente = pricingService.resolveSellingPrice(depotDTO.getMaquette(), articleUuDTO.getCategorieArticle(), priuni);
                    }
                }
            }
        }
        checkBusinessLogique(prixVente.compareTo(BigDecimal.ZERO) > 0, "prix-vente-zero", detailFacture.getArticle().getDesignation());
        if ((categDepot.equals(CategorieDepotEnum.PH) || categDepot.equals(CategorieDepotEnum.UU)) && quittanceDTO.getCodePrestation() == null && BLOCKAGE_SALE_PRICE_PURCHASE.equals(Boolean.TRUE)) {
            checkBusinessLogique(prixVente.compareTo(priuni) >= 0, "prix-vente-achat", detailFacture.getArticle().getDesignation(), prixVente.toString(), priuni.toString());
        }
        mvtsto.setTauxCouverture(detailFacture.getArticle().getRemiseConventionnelle().getTauxCouverture());
        //verification remise, majoration et couverture
        checkBusinessLogique(detailFacture.getRemise() == null || detailFacture.getRemise().compareTo(mvtsto.getRemise()) == 0, "error-remise-article");
        checkBusinessLogique(detailFacture.getMajoration() == null || detailFacture.getMajoration().compareTo(mvtsto.getMajoration()) == 0, "error-remise-article");
        checkBusinessLogique(detailFacture.getTauxCouverture() == null || detailFacture.getTauxCouverture().compareTo(mvtsto.getTauxCouverture()) == 0, "error-taux-couverture-article");
        mvtsto.setTypbon(TypeBonEnum.FE);
        mvtsto.setCategDepot(categDepot);
        mvtsto.setMemoart(detailFacture.getMemoart());
        mvtsto.setLotInter(detailFacture.getLot());
        mvtsto.setDatPer(detailFacture.getDatPer());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtsto.setDesart(detailFacture.getArticle().getDesignationSec());
            mvtsto.setDesArtSec(detailFacture.getArticle().getDesignation());
        } else {
            mvtsto.setDesart(detailFacture.getArticle().getDesignation());
            mvtsto.setDesArtSec(detailFacture.getArticle().getDesignationSec());
        }
        mvtsto.setCodeSaisi(detailFacture.getArticle().getCodeSaisi());
        mvtsto.setQuantite(detailFacture.getQuantite());
        mvtsto.setQteben(detailFacture.getQuantite());
        mvtsto.setAjustement(BigDecimal.ZERO);
        mvtsto.setPrixBrute(prixVente);
        mvtsto.setPriuni(mvtsto.getPrixBrute().multiply((BigDecimal.valueOf(100).subtract(mvtsto.getRemise()).add(mvtsto.getMajoration())).divide(BigDecimal.valueOf(100)).setScale(7, RoundingMode.HALF_UP)));
        mvtsto.setPriach(detailFacture.getArticle().getPrixAchat());
        mvtsto.setCodTvaAch(detailFacture.getArticle().getCodeTvaAch());
        mvtsto.setTauTvaAch(detailFacture.getArticle().getValeurTvaAch());
        mvtsto.setUnite(detailFacture.getUnite());
        mvtsto.setMontht(mvtsto.getPriuni().multiply(mvtsto.getQuantite()));
        if (detailFacture.getCodeTva() == null && detailFacture.getTauxTva() == null) {
            mvtsto.setTautva(detailFacture.getArticle().getValeurTvaVente());
            mvtsto.setCodtva(detailFacture.getArticle().getCodeTvaVente());
        } else {
            mvtsto.setTautva(detailFacture.getTauxTva());
            mvtsto.setCodtva(detailFacture.getCodeTva());
        }
    }

    /**
     * Get one factureDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public FactureDTO findOne(String id) {
        log.debug("Request to get Facture: {}", id);
        Facture facture = factureRepository.findOne(id);
        FactureDTO dto = factureFactory.factureToFactureDTOLazy(facture);
        List<String> codeAdmissions = new ArrayList<>();
        codeAdmissions.add(facture.getNumdoss());
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        if (!clients.isEmpty()) {
            dto.setClient(clients.get(0));
        }
        List<Integer> codeUnites = new ArrayList<>();
        facture.getMvtstoCollection().forEach(x -> {
            codeUnites.add(x.getUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        dto.getMvtstoCollection().forEach((mvtstoDTO) -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst().orElse(null);
            checkBusinessLogique(unite != null, "missing-unity");
            mvtstoDTO.setUnityCode(unite.getCode());
            mvtstoDTO.setUnityDesignation(unite.getDesignation());
        });
        if (dto.getNumbonRecept() != null) {
            List<ReceptionDetailCA> receptionDetailCAs = receptionDetailCAService.findRecivedDetailCAByReceptionIn(java.util.Arrays.asList(dto.getNumbonRecept()));
            if (!receptionDetailCAs.isEmpty()) {
                dto.setCommandeAchat(receptionDetailCAs.get(0).getPk().getCommandeAchat());
            }
        }
        return dto;
    }

    /**
     * Get all the factures by code in.
     *
     * @param codes
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<FactureDTO> findByNumbonInDetailed(List<String> codes
    ) {
        log.debug("Request to get All factures by code in");

        List<Facture> result = (List<Facture>) factureRepository.findByNumbonIn(codes);
        List<FactureDTO> resultDTO = factureFactory.factureToFactureDTOLazys(result);
        List<String> codeAdmissions = new ArrayList<>();
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(facture -> {
            codeAdmissions.add(facture.getNumdoss());
            facture.getMvtstoCollection().forEach(x -> {
                codeUnites.add(x.getUnite());
            });
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        resultDTO.forEach(factureDTO -> {
            AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(factureDTO.getNumdoss())).findFirst().orElse(null);
            checkBusinessLogique(client != null, "missing-client");
            factureDTO.setClient(client);
            factureDTO.getMvtstoCollection().forEach((mvtstoDTO) -> {
                UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst().orElse(null);
                checkBusinessLogique(unite != null, "missing-unity");
                mvtstoDTO.setUnityCode(unite.getCode());
                mvtstoDTO.setUnityDesignation(unite.getDesignation());
            });
        });
        return resultDTO;
    }

    /**
     * Get all the factures by code in.
     *
     * @param codes
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<FactureDTO> findDTOsByNumbonIn(List<String> codes
    ) {
        log.debug("Request to get All factures by code in");
        List<Facture> result = (List<Facture>) factureRepository.findByNumbonIn(codes);
        List<FactureDTO> resultDTO = factureFactory.factureToFactureDTOs(result);
        List<String> codeAdmissions = new ArrayList<>();
        result.forEach(facture -> {
            codeAdmissions.add(facture.getNumdoss());
        });
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        resultDTO.forEach(factureDTO -> {
            AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(factureDTO.getNumdoss())).findFirst().orElse(null);
            checkBusinessLogique(client != null, "missing-client");
            factureDTO.setClient(client);
        });
        return resultDTO;
    }

    /**
     * Get all the factures by code in.
     *
     * @param codes
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<Facture> findByNumbonIn(List<String> codes) {
        log.debug("Request to get All factures by code in");
        List<Facture> result = new ArrayList();

        if (codes != null && codes.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codes.size() / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<String> codesChunk = codes.subList(i * 2000, Math.min(i * 2000 + 2000, codes.size()));
                result.addAll(factureRepository.findByNumbonIn(codesChunk));
            }
        }
        return result;
    }

    /**
     * Get one facture by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public Facture findFacture(String id
    ) {
        log.debug("Request to get Facture: {}", id);
        Facture facture = factureRepository.findOne(id);
        return facture;
    }

    /**
     * Get list facture by Numdoss.
     *
     * @param categ
     * @param Numdoss
     * @param coddep
     * @param numFacture
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public List<Facture> findByNumdossAndCoddepAndCategDepotAndNotDeleted(CategorieDepotEnum categ, String Numdoss,
            Integer coddep, String numFacture
    ) {
        log.debug("Request to get Facture: {}", Numdoss);
        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_facture.categDepot.eq(categ))
                .optionalAnd(numFacture, () -> _facture.numbon.eq(numFacture))
                .and(_facture.numdoss.eq(Numdoss))
                .and(_facture.panier.ne(Boolean.TRUE))
                .and(_facture.coddep.eq(coddep))
                .and(_facture.codAnnul.isNull());
        List<Facture> result = (List<Facture>) factureRepository.findAll(builder);
        return result;
    }

    /**
     * Get list facture by Numdoss.
     *
     * @param Numdoss
     * @param numFacture
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public Facture findByNumdossAndNumbon(String Numdoss,
            String numFacture
    ) {
        log.debug("Request to get Facture: {}", Numdoss);
        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(_facture.numbon.eq(numFacture))
                .and(_facture.numdoss.eq(Numdoss));
        List<Facture> result = (List<Facture>) factureRepository.findAll(builder);
        Facture facture = result.stream().findFirst().orElse(null);
        return facture;
    }

    /**
     * Get all the factures.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param codeAdmission
     * @param deleted
     * @param satisfaction
     * @param coddep
     * @param etatPatient
     * @param search
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<FactureDTO> findAll(List<CategorieDepotEnum> categ, LocalDateTime fromDate,
            LocalDateTime toDate, String codeAdmission,
            Boolean deleted, SatisfactionFactureEnum satisfaction,
            Integer coddep, List<ReceptionConstants> etatPatient, String search
    ) {

        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(categ, () -> _facture.categDepot.in(categ))
                .optionalAnd(fromDate, () -> _facture.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _facture.datbon.loe(toDate))
                .optionalAnd(codeAdmission, () -> _facture.numdoss.eq(codeAdmission))
                .optionalAnd(coddep, () -> _facture.coddep.eq(coddep))
                .optionalAnd(search, () -> _facture.numaffiche.like("%" + search + "%").or(_facture.numdoss.like("%" + search + "%")))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _facture.codAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _facture.codAnnul.isNull());
        log.debug("Request to get All Factures");
        List<Facture> result = (List<Facture>) factureRepository.findAll(builder);
        List<String> queriedQuittancesIDs = result.stream().map(item -> item.getNumbon()).collect(toList());
//        List<String> recoverdQuittancesIDs = btfeRepository.findByNumFEIn(queriedQuittancesIDs).stream().map(Btfe::getNumFE).collect(toList());
        List<Btfe> btFes = new ArrayList<>();
        if (satisfaction != null && queriedQuittancesIDs.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) queriedQuittancesIDs.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {
                List<String> codesChunk = queriedQuittancesIDs.subList(i * 2000, Math.min(i * 2000 + 2000, queriedQuittancesIDs.size()));
                completableFuture[i] = btfeRepository.findByNumFEIn(codesChunk).whenComplete((btfes, exception) -> {
                    btFes.addAll(btfes);
                });
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        List<String> recoverdQuittancesIDs = btFes.stream().map(Btfe::getNumFE).collect(toList());
        List<FactureDTO> resultDTO = new ArrayList<>();
        if (satisfaction != null) {
            List<Facture> filtredResult = new ArrayList();
            switch (satisfaction) {
                case NOT_RECOVRED:
                    filtredResult = result.stream().filter(item -> !recoverdQuittancesIDs.contains(item.getNumbon())).collect(toList());
                    break;
                case RECOVRED:
                    filtredResult = result.stream().filter(item -> recoverdQuittancesIDs.contains(item.getNumbon())).collect(toList());
                    break;
            }
            resultDTO = factureFactory.factureToFactureDTOs(filtredResult);
        } else {
            resultDTO = factureFactory.factureToFactureDTOs(result);
        }
        List<String> codeAdmissions = new ArrayList<>();
        result.forEach(facture -> {
            codeAdmissions.add(facture.getNumdoss());
        });
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        resultDTO.forEach(factureDTO -> {
            AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(factureDTO.getNumdoss())).findFirst().orElse(null);
            checkBusinessLogique(client != null, "missing-client");
            factureDTO.setClient(client);
        });
        if (etatPatient != null) {
            List<Integer> codeEtatAdmission = new ArrayList<>();
            etatPatient.forEach(x -> {
                codeEtatAdmission.add(x.code());
            });
            resultDTO = resultDTO.stream().filter(item -> codeEtatAdmission.contains(item.getClient().getCodeEtatPatient())).collect(toList());
        }

        List<Integer> codeDepots = new ArrayList<>();
        resultDTO.forEach(x -> {
            codeDepots.add(x.getCodeDepot());
        });
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        resultDTO.forEach(factureDTO -> {
            DepotDTO depot = listDepot.stream().filter(x -> x.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            checkBusinessLogique(depot != null, "missing-depot");
            factureDTO.setDesignationDepot(depot.getDesignation());
        });
        List<String> numbonRecepts = resultDTO.stream().map(x -> x.getNumbonRecept()).distinct().collect(Collectors.toList());
        List<ReceptionDetailCA> receptionDetailCAs = receptionDetailCAService.findRecivedDetailCAByReceptionIn(numbonRecepts);
        for (FactureDTO dto : resultDTO) {
            if (dto.getNumbonRecept() != null) {
                ReceptionDetailCA receptionDetailCA = receptionDetailCAs.stream().filter(x -> {
                    return x.getPk().getReception().equals(dto.getNumbonRecept());
                }).findFirst().orElse(null);
                if (receptionDetailCA != null) {
                    dto.setCommandeAchat(receptionDetailCA.getPk().getCommandeAchat());
                }
            }
        }

        return resultDTO;
    }

    /**
     * Get all the factures.
     *
     * @param codeAdmission
     * @param codes
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<Facture> findAll(String codeAdmission, List<String> codes
    ) {
        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_facture.numbon.in(codes))
                .and(_facture.numdoss.eq(codeAdmission))
                .and(_facture.codAnnul.isNull());
        log.debug("Request to get All Factures");
        List<Facture> result = (List<Facture>) factureRepository.findAll(builder);
        return result;
    }

    /**
     *
     * trouver les quittances selon satisfaction (les quittances non satisfaits
     * ou satisfaits )
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param deleted
     * @param satisfaction
     * @param coddep
     * @param search
     * @return
     */
    @Transactional(readOnly = true)
    public List<Facture> findBySatisfaction(CategorieDepotEnum categ, LocalDateTime fromDate,
            LocalDateTime toDate,
            Boolean deleted, SatisfactionFactureEnum satisfaction,
            Integer coddep) {

        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(categ, () -> _facture.categDepot.eq(categ))
                .optionalAnd(fromDate, () -> _facture.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _facture.datbon.loe(toDate))
                .optionalAnd(coddep, () -> _facture.coddep.eq(coddep))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _facture.codAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _facture.codAnnul.isNull());
        log.debug("Request to get All Factures");
        List<Facture> result = (List<Facture>) factureRepository.findAll(builder);
        List<String> queriedQuittancesIDs = result.stream().map(item -> item.getNumbon()).collect(toList());
//        List<String> recoverdQuittancesIDs = btfeRepository.findByNumFEIn(queriedQuittancesIDs).stream().map(Btfe::getNumFE).collect(toList());
        List<Btfe> btFes = new ArrayList<>();
        if (satisfaction != null && queriedQuittancesIDs.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) queriedQuittancesIDs.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {
                List<String> codesChunk = queriedQuittancesIDs.subList(i * 2000, Math.min(i * 2000 + 2000, queriedQuittancesIDs.size()));
                completableFuture[i] = btfeRepository.findByNumFEIn(codesChunk).whenComplete((btfes, exception) -> {
                    btFes.addAll(btfes);
                });
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        List<String> recoverdQuittancesIDs = btFes.stream().map(Btfe::getNumFE).collect(toList());
        List<Facture> filtredResult = new ArrayList();
        if (satisfaction != null) {
            switch (satisfaction) {
                case NOT_RECOVRED:
                    filtredResult = result.stream().filter(item -> !recoverdQuittancesIDs.contains(item.getNumbon())).collect(toList());
                    log.debug("filtredResult noooooooot recovered size est {}", filtredResult.size());
                    break;
                case RECOVRED:
                    filtredResult = result.stream().filter(item -> recoverdQuittancesIDs.contains(item.getNumbon())).collect(toList());
                    log.debug("filtredResult recovered size est {}", filtredResult.size());
                    break;
            }

        }

        return filtredResult;
    }

    /**
     * Get all the factures.
     *
     * @param quittancesIDs
     * @param categ
     * @param lazy
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<FactureDTO> searches(String[] quittancesIDs, List<CategorieDepotEnum> categ, Boolean lazy) {
        if (categ != null) {
            checkBusinessLogique(!categ.contains(CategorieDepotEnum.EC), "article-economat");
        }
        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(categ, () -> _facture.categDepot.in(categ))
                .and(_facture.numbon.in(quittancesIDs));
        log.debug("Request to get All Factures");
        List<Facture> result = (List<Facture>) factureRepository.findAll(builder);
        List<FactureDTO> resultDTO = new ArrayList<>();
        if (lazy) {
            resultDTO = factureFactory.factureToFactureDTOLazys(result);
            List<Integer> codeDepots = resultDTO.stream().map(FactureDTO::getCodeDepot).distinct().collect(Collectors.toList());
            List<DepotDTO> depotDTOs = paramAchatServiceClient.findDepotsByCodes(codeDepots);
            resultDTO.forEach(factureDTO -> {
                NatureDepotDTO natureDepot = depotDTOs.stream().filter(item -> item.getCode().equals(factureDTO.getCodeDepot())).findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("Depot introuvable"))
                        .getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(factureDTO.getCategDepot())).findFirst().orElse(null);
                checkBusinessLogique(natureDepot != null, "missing-prestation-depot");
                factureDTO.setMntmpl(BigDecimal.valueOf(factureDTO.getMvtstoCollection().stream().map(x -> (x.getPrixBrute().multiply(x.getQuantite())).multiply(BigDecimal.valueOf(100).add(x.getTautva())).divide(BigDecimal.valueOf(100))).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
                factureDTO.setCodePrestation(natureDepot.getPrestationID());
            });
            return resultDTO;
        } else {
            resultDTO = factureFactory.factureToFactureDTOs(result);
        }

        List<String> codeAdmissions = new ArrayList<>();
        result.forEach(facture -> {
            codeAdmissions.add(facture.getNumdoss());
        });
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        resultDTO.forEach(factureDTO -> {
            AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(factureDTO.getNumdoss())).findFirst().orElse(null);
            checkBusinessLogique(client != null, "missing-client");
            factureDTO.setClient(client);
        });
        List<Integer> codeDepots = new ArrayList<>();
        resultDTO.forEach(x -> {
            codeDepots.add(x.getCodeDepot());
        });
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        resultDTO.forEach(factureDTO -> {
            DepotDTO depot = listDepot.stream().filter(x -> x.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            checkBusinessLogique(depot != null, "missing-depot");
            factureDTO.setDesignationDepot(depot.getDesignation());
        });
        return resultDTO;
    }

    /**
     * Delete facture by liste id.
     *
     * @param quittancesIDs
     * @param codeMotifSuppression
     * @param withFacturation annulation facturation quittance
     * @param isPanier
     * @return
     */
    public List<FactureDTO> deletes(List<String> quittancesIDs, Integer codeMotifSuppression, Boolean withFacturation, Boolean isPanier) {
        log.debug("Request to delete quittances: {}", quittancesIDs.toString());
        List<Facture> factures = new ArrayList<>();
        for (String id : quittancesIDs) {
            factures.add(deleteFacture(id, isPanier));
        }
        int compteNumdoss = factures.stream().map(Facture::getNumdoss).distinct().collect(Collectors.toList()).size();
        checkBusinessLogique(compteNumdoss == 1, "facture.unique.dossier");
        factures = factureRepository.save(factures);
        List<FactureDTO> factureDTOs = factureFactory.factureToFactureDTOs(factures);
        if (withFacturation.equals(Boolean.TRUE)) {
            Boolean deleteFacturation = receptionServiceClient.deleteFacturationByNumQuittance(codeMotifSuppression, quittancesIDs, factures.get(0).getNumdoss());
            checkBusinessLogique(Objects.equals(deleteFacturation, Boolean.TRUE), "error-facturation");
        }
        return factureDTOs;
    }

    /**
     * Delete facture by liste id.
     *
     * @param quittancesIDs
     * @param codeMotifSuppression
     * @param withFacturation annulation facturation quittance
     * @param listQuittanceDTO
     * @return
     */
    public List<Facture> deletesPermanent(List<String> quittancesIDs, Integer codeMotifSuppression, Boolean withFacturation, List<QuittanceDTO> listQuittanceDTO) {
        log.debug("Request to delete quittances: {}", quittancesIDs.toString());
        List<Facture> factures = new ArrayList<>();
        for (String id : quittancesIDs) {
            factures.add(deleteFacturePermanent(id));
        }
        for (QuittanceDTO quittanceDTO : listQuittanceDTO) {
            if (quittanceDTO.getFactureDr() != null) {
                factureDRService.annulationUpdate(quittanceDTO.getFactureDr(), quittanceDTO.getMvtQuittance());
            }
        }
        factureRepository.delete(factures);
        return factures;
    }

    /**
     * Delete facture by id.
     *
     * @param id the id of the entity
     * @param codeMotifSuppression
     * @param withFacturation annulation facturation quittance
     * @return Facture
     */
    private Facture deleteFacturePermanent(String id) {
        log.debug("Request to delete quittance: {}", id);
        Facture facture = factureRepository.findOne(id);
        checkBusinessLogique(facture != null, "facture.NotFound");
        checkBusinessLogique(facture.getCodAnnul() == null, "facture.Annule");
        checkBusinessLogique(!facture.isEtatbon(), "facture.Avoir");
        if (facture.getNumbonRecept() == null) {
            for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                for (DetailMvtsto detailMvtsto : mvtsto.getDetailMvtstoCollection()) {
                    Depsto depsto = detailMvtsto.getDepsto();
                    depsto.setQte(depsto.getQte().add(detailMvtsto.getQte()));
                }
            }
        } else {
            bonTransfertInterDepotService.cancelBonTransferInterDepotDepotFrsPermanent(facture.getNumbonTransfert());
            factureBAService.cancelBonReceptionDepotFrsPermanent(facture.getNumbonRecept());
        }
        return facture;
    }

    /**
     * Delete facture by id.
     *
     * @param id the id of the entity
     * @param codeMotifSuppression
     * @param withFacturation annulation facturation quittance
     * @return Facture
     */
    private Facture deleteFacture(String id, Boolean isPanier) {
        log.debug("Request to delete quittance: {}", id);
        Facture facture = factureRepository.findOne(id);
        checkBusinessLogique(facture != null, "facture.NotFound");
        checkBusinessLogique(facture.getCodAnnul() == null, "facture.Annule");
        checkBusinessLogique(!facture.isEtatbon(), "facture.Avoir");
        checkBusinessLogique(Boolean.FALSE.equals(facture.getIntegrer()) && facture.getCodeIntegration().isEmpty(), "facture-deja-integre");
        if (!isPanier) {
            checkBusinessLogique(!facture.getPanier(), "facture-is-panier");
        }

        //test inventaire       
        List<Integer> codarts = facture.getMvtstoCollection().stream().map(x -> x.getMvtstoPK().getCodart()).collect(Collectors.toList());
        List<ArticleDTO> listArticleDTOs = paramAchatServiceClient.articleFindbyListCode(codarts);
        List<Integer> categArticleIDs = listArticleDTOs.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, facture.getCoddep());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = listArticleDTOs.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }

        LocalDateTime date = LocalDateTime.now();
        facture.setDatAnnul(date);
        facture.setCodAnnul(SecurityContextHolder.getContext().getAuthentication().getName());
        if (facture.getNumbonRecept() == null) {
            for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                mvtsto.setQteben(BigDecimal.ZERO);
                for (DetailMvtsto detailMvtsto : mvtsto.getDetailMvtstoCollection()) {
                    detailMvtsto.setQteAvoir(detailMvtsto.getQte());
                    Depsto depsto = detailMvtsto.getDepsto();
                    depsto.setQte(depsto.getQte().add(detailMvtsto.getQte()));
                }
            }

        } else {
            for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                mvtsto.setQteben(BigDecimal.ZERO);
            }
            bonTransfertInterDepotService.cancelBonTransferInterDepotDepotFrs(facture.getNumbonTransfert());
            factureBAService.cancelBonReceptionDepotFrs(facture.getNumbonRecept());
        }
        return facture;
    }

    /**
     * Delete facture by id.
     *
     * @param id the id of the entity
     * @param codeMotifSuppression
     * @param withFacturation annulation facturation quittance
     * @param isPanier
     * @return FactureDTO
     */
    public FactureDTO delete(String id, Integer codeMotifSuppression, Boolean withFacturation, Boolean isPanier) {
        Facture facture = factureRepository.save(deleteFacture(id, isPanier));
        List<String> ids = new ArrayList<>();
        ids.add(id);
        FactureDTO factureDTO = factureFactory.factureToFactureDTO(facture);
        if (withFacturation.equals(Boolean.TRUE)) {
            Boolean deleteFacturation = receptionServiceClient.deleteFacturationByNumQuittance(codeMotifSuppression, ids, facture.getNumdoss());
            checkBusinessLogique(Objects.equals(deleteFacturation, Boolean.TRUE), "error-facturation");
        }
        return factureDTO;
    }

    /**
     * Update organisme pour list des quittances and avoirs.
     *
     * @param codes list des ids pour des quittances
     * @param codePriceList code price list
     * @param codeListCouverture code list couverture
     * @param codeNatureAdmission code nature admisssion
     * @param codeConvention code convention
     * @param codeSociete code societe
     * @param numdoss code admission
     * @param pharmacieExterne test pharmacie externe
     * @return Boolean
     */
    public List<BaseAvoirQuittance> updateOrganismePEC(List<String> codes, Integer codePriceList, Integer codeListCouverture,
            Integer codeNatureAdmission, Integer codeConvention, String numdoss, Integer codeSociete, Boolean pharmacieExterne) {
        SocieteDTO societe = paramServiceClient.findSocietebyCode(codeSociete);
        List<BaseAvoirQuittance> resultat = new ArrayList<>();
        List<Facture> listQuittances = findAll(numdoss, codes);
        List<Integer> codarts = listQuittances.stream().flatMap(x -> x.getMvtstoCollection().stream()).map(x -> x.getMvtstoPK().getCodart()).collect(Collectors.toList());
        List<RemiseConventionnelleDTO> remiseConventionnelles = paramServiceClient.pricelisteParArticle(codarts, codePriceList, codeListCouverture, codeNatureAdmission, codeConvention, pharmacieExterne);
        Collection<Integer> coddeps = listQuittances.stream().map(x -> x.getCoddep()).collect(Collectors.toList());
        List<DepotDTO> depotDTOs = paramAchatServiceClient.findDepotsByCodes(coddeps);
        List<Facture> listQuittancesWithPrestation = listQuittances.stream().filter(x -> x.getPanier().equals(Boolean.TRUE)).collect(Collectors.toList());
        List<Facture> listQuittancesWithoutPrestation = listQuittances.stream().filter(x -> x.getPanier().equals(Boolean.FALSE)).collect(Collectors.toList());
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();

        for (Facture facture : listQuittancesWithoutPrestation) {
            for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                RemiseConventionnelleDTO remiseConventionnelle = remiseConventionnelles.stream().filter(
                        item -> item.getCodeArticle().equals(mvtsto.getMvtstoPK().getCodart())).findFirst().orElse(null);
                checkBusinessLogique(remiseConventionnelle != null, "error-remise-article");
                if ("MAJ".equals(remiseConventionnelle.getNatureException())) {
                    mvtsto.setMajoration(remiseConventionnelle.getTaux());
                    mvtsto.setRemise(BigDecimal.ZERO);
                } else {
                    mvtsto.setRemise(remiseConventionnelle.getTaux());
                    mvtsto.setMajoration(BigDecimal.ZERO);
                }
                mvtsto.setTauxCouverture(remiseConventionnelle.getTauxCouverture());
                mvtsto.setAjustement(BigDecimal.ZERO);
                mvtsto.setPriuni(mvtsto.getPrixBrute().multiply((BigDecimal.valueOf(100).subtract(mvtsto.getRemise()).add(mvtsto.getMajoration())).divide(BigDecimal.valueOf(100)).setScale(7, RoundingMode.HALF_UP)));
                mvtsto.setMontht(mvtsto.getPriuni().multiply(mvtsto.getQuantite()));
            }
            if (societe != null) {
                facture.setCodeSociete(codeSociete);
                facture.setRemiseConventionnellePharmacie(societe.getRemiseConventionnellePharmacie());
            } else {
                facture.setCodeSociete(null);
                facture.setRemiseConventionnellePharmacie(null);
            }
            facture.calcul(listTvas, societe, pharmacieExterne);

        }
        Map<String, List<Facture>> listQuittancesGroupedPanier
                = listQuittancesWithPrestation.stream().collect(Collectors.groupingBy(x -> x.getNumbonPanier()));
        for (Map.Entry<String, List<Facture>> entry : listQuittancesGroupedPanier.entrySet()) {
            List<Facture> list = entry.getValue();

            Integer codePrestation = null;
            BigDecimal quantitePrestation = null;
            Integer codeOperation = null;
            if (list.get(0).getCodePrestation() != null) {
                codePrestation = list.get(0).getCodePrestation();
                quantitePrestation = list.get(0).getQuantitePrestation();
            }
            if (list.get(0).getCodeOperation() != null) {
                codeOperation = list.get(0).getCodeOperation();
            }
            for (Facture facture : list) {
                for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                    RemiseConventionnelleDTO remiseConventionnelle = remiseConventionnelles.stream().filter(
                            item -> item.getCodeArticle().equals(mvtsto.getMvtstoPK().getCodart())).findFirst().orElse(null);
                    checkBusinessLogique(remiseConventionnelle != null, "error-remise-article");
                    if ("MAJ".equals(remiseConventionnelle.getNatureException())) {
                        mvtsto.setMajoration(remiseConventionnelle.getTaux());
                        mvtsto.setRemise(BigDecimal.ZERO);
                    } else {
                        mvtsto.setRemise(remiseConventionnelle.getTaux());
                        mvtsto.setMajoration(BigDecimal.ZERO);
                    }
                    mvtsto.setTauxCouverture(remiseConventionnelle.getTauxCouverture());
                    mvtsto.setAjustement(BigDecimal.ZERO);
                    mvtsto.setPriuni(mvtsto.getPrixBrute().multiply((BigDecimal.valueOf(100).subtract(mvtsto.getRemise()).add(mvtsto.getMajoration())).divide(BigDecimal.valueOf(100)).setScale(7, RoundingMode.HALF_UP)));
                    mvtsto.setMontht(mvtsto.getPriuni().multiply(mvtsto.getQuantite()));
                }
                if (societe != null) {
                    facture.setCodeSociete(codeSociete);
                    facture.setRemiseConventionnellePharmacie(societe.getRemiseConventionnellePharmacie());
                } else {
                    facture.setCodeSociete(null);
                    facture.setRemiseConventionnellePharmacie(null);
                }
                facture.calcul(listTvas, societe, pharmacieExterne);
            }
            if (codePrestation != null) {
                updateMntPanier(list, codePrestation, societe, pharmacieExterne, quantitePrestation);
            }
            if (codeOperation != null) {
                log.debug("methode updateOrganismePEC codeOperation {}", codeOperation);
                updateMntPanierOperation(list, codeOperation, societe, pharmacieExterne);
            }
        }
        for (Facture facture : listQuittances) {
            facture.setMntbon(facture.getMntbon().setScale(2, RoundingMode.HALF_UP));
            facture.setPartiePEC(facture.getPartiePEC().setScale(2, RoundingMode.HALF_UP));
            facture.setPartiePatient(facture.getPartiePatient().setScale(2, RoundingMode.HALF_UP));
        }
        listQuittances = factureRepository.save(listQuittances);
        List<FactureDTO> listFactureDTO = factureFactory.factureToFactureDTOLazys(listQuittances);
        listFactureDTO.forEach(factureDTO -> {
            DepotDTO depotDTO = depotDTOs.stream().filter(
                    item -> item.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            checkBusinessLogique(!depotDTO.getDesignation().equals("depot.deleted"), "Depot [" + depotDTO.getDesignation() + "] introuvable");
            NatureDepotDTO natureDepot = depotDTO.getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(factureDTO.getCategDepot())).findFirst().orElse(null);
            checkBusinessLogique(natureDepot != null, "missing-prestation-depot");
            factureDTO.setMntmpl(BigDecimal.valueOf(factureDTO.getMvtstoCollection().stream().map(x -> (x.getPrixBrute().multiply(x.getQuantite())).multiply(BigDecimal.valueOf(100).add(x.getTautva())).divide(BigDecimal.valueOf(100))).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
            factureDTO.setCodePrestation(natureDepot.getPrestationID());
        });
        resultat.addAll(listFactureDTO);
        resultat.addAll(avoirService.updateOrganismePEC(codes, codePriceList, codeListCouverture, codeNatureAdmission, codeConvention, numdoss, pharmacieExterne, societe));
        return resultat;
    }

    /**
     * calcul partie pateint, partie patient, montant bon pour une quittance
     * dans un panier.
     *
     * @param quittanceDTO list des articles
     * @param codePriceList code price list
     * @param codeListCouverture code list couverture
     * @param codeNatureAdmission code nature admisssion
     * @param codeConvention code convention
     * @param codeSociete code societe
     * @param pharmacieExterne test pharmacie externe
     * @return Boolean
     */
    public QuittanceDTO calculQuittances(QuittanceDTO quittanceDTO, Integer codePriceList, Integer codeListCouverture,
            Integer codeNatureAdmission, Integer codeConvention, Integer codeSociete, Boolean pharmacieExterne) {
        DepotDTO depotDTO = paramAchatServiceClient.findDepotByCode(quittanceDTO.getCoddep());
        checkBusinessLogique(!depotDTO.getDesignation().equals("depot.deleted"), "depot.source.introuvable");
        checkBusinessLogique(!depotDTO.getDepotFrs(), "depot.source.fournisseur", depotDTO.getDesignation());
        log.debug("quittanceDTO {}", quittanceDTO);
        //find societe pec
        SocieteDTO societe = receptionServiceClient.findSocieteByCodeAdmission(quittanceDTO.getNumdoss());
        List<Integer> codarts = quittanceDTO.getMvtQuittance().stream().map(x -> x.getCodart()).collect(Collectors.toList());
        List<ArticleDTO> listArticleDTOs = paramAchatServiceClient.articleFindbyListCode(codarts);
        List<Integer> categArticleIDs = listArticleDTOs.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
        //test inventaire
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, quittanceDTO.getCoddep());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = listArticleDTOs.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }
        //get remise conventionnel article
        List<RemiseConventionnelleDTO> remiseConventionnelles = paramServiceClient.pricelisteParArticle(codarts, codePriceList, codeListCouverture, codeNatureAdmission, codeConvention, pharmacieExterne);
        listArticleDTOs.forEach(article -> {
            RemiseConventionnelleDTO remiseConventionnelle = remiseConventionnelles.stream().filter(
                    item -> item.getCodeArticle().equals(article.getCode())).findFirst().orElse(null);
            checkBusinessLogique(remiseConventionnelle != null, "error-remise-article");
            article.setRemiseConventionnelle(remiseConventionnelle);
        });
        //affection des articles et nature depot
        quittanceDTO.getMvtQuittance().forEach(mvtQuittance -> {
            ArticleDTO articleDTO = listArticleDTOs.stream().filter(y -> y.getCode().equals(mvtQuittance.getCodart())).collect(Collectors.toList()).get(0);
            checkBusinessLogique(!articleDTO.getDesignation().equals("Not Available"), "return.add.article-inexistant", mvtQuittance.getCodart().toString());
            checkBusinessLogique(articleDTO.getActif(), "return.add.article-inactif", articleDTO.getDesignation());
            if (mvtQuittance.getDatPer() != null) {
                checkBusinessLogique(mvtQuittance.getDatPer().compareTo(LocalDate.now()) > 0, "date.peremption.invalide", mvtQuittance.getDatPer().toString(), articleDTO.getDesignation());
            }
            mvtQuittance.setArticle(articleDTO);
            mvtQuittance.setRemise(BigDecimal.ZERO);
            mvtQuittance.setMajoration(BigDecimal.ZERO);
            if ("MAJ".equals(articleDTO.getRemiseConventionnelle().getNatureException())) {
                mvtQuittance.setMajoration(articleDTO.getRemiseConventionnelle().getTaux());
            } else {
                mvtQuittance.setRemise(articleDTO.getRemiseConventionnelle().getTaux());
            }
            mvtQuittance.setTauxTva(articleDTO.getValeurTvaVente());
            mvtQuittance.setCodeTva(articleDTO.getCodeTvaVente());
        });
        //groupement des mouvements by categorie article
        List<CategorieDepotEnum> categorieDepots = quittanceDTO.getMvtQuittance().stream().map(item -> item.getArticle().getCategorieDepot()).distinct().collect(Collectors.toList());
        checkBusinessLogique(!categorieDepots.contains(CategorieDepotEnum.EC), "article-economat");
        Map<CategorieDepotEnum, List<MvtQuittanceDTO>> groupByMvtQuittance
                = quittanceDTO.getMvtQuittance().stream().collect(Collectors.groupingBy(x -> x.getArticle().getCategorieDepot()));
        //traitement des mouvements by categorie article
        for (Map.Entry<CategorieDepotEnum, List<MvtQuittanceDTO>> entry : groupByMvtQuittance.entrySet()) {
            CategorieDepotEnum categDepot = entry.getKey();
            List<MvtQuittanceDTO> mvtQuittance = entry.getValue();
            //find article by categorie article
            List<ArticlePHDTO> articlePHDTOs = new ArrayList<>();
            List<ArticleUuDTO> articleUuDTOs = new ArrayList<>();
            if (categDepot.equals(CategorieDepotEnum.PH)) {
                codarts = mvtQuittance.stream().map(x -> x.getCodart()).collect(Collectors.toList());
                articlePHDTOs = paramAchatServiceClient.articlePHFindbyListCode(codarts);
            } else if (categDepot.equals(CategorieDepotEnum.UU)) {
                codarts = mvtQuittance.stream().map(x -> x.getCodart()).collect(Collectors.toList());
                articleUuDTOs = paramAchatServiceClient.articleUUFindbyListCode(codarts, quittanceDTO.getCoddep(), true, false);
            }
            for (MvtQuittanceDTO detailFacture : mvtQuittance) {
                if (categDepot.equals(CategorieDepotEnum.PH)) {
                    ArticlePHDTO matchedArticle = articlePHDTOs.stream().filter(x -> x.getCode().equals(detailFacture.getCodart())).findFirst().orElse(null);
                    checkBusinessLogique(!matchedArticle.getDesignation().equals("Not Available"), "return.add.article-inexistant", matchedArticle.getCode().toString());
                    detailFacture.setPrixVente(matchedArticle.
                            getArticleUnites().stream().filter(y -> y.getCodeUnite().equals(detailFacture.getUnite())).collect(Collectors.toList()).get(0).getPrixVente());

                } else if (categDepot.equals(CategorieDepotEnum.UU)) {
                    ArticleUuDTO articleUuDTO = articleUuDTOs.stream().filter(x -> x.getCode().equals(detailFacture.getCodart())).collect(Collectors.toList()).get(0);

                    checkBusinessLogique(!articleUuDTO.getDesignation().equals("Not Available"), "return.add.article-inexistant", articleUuDTO.getCode().toString());
//prix vente envoyer par param achat car on a envoyer code depot
                    detailFacture.setPrixVente(articleUuDTO.getPrixVente());
                }
                checkBusinessLogique(detailFacture.getPrixVente().compareTo(BigDecimal.ZERO) > 0, "prix-vente-zero");
                detailFacture.setTauxCouverture(detailFacture.getArticle().getRemiseConventionnelle().getTauxCouverture());
                detailFacture.setAjustement(BigDecimal.ZERO);
            }
        }
        quittanceDTO.calcul(societe, pharmacieExterne);
        BigDecimal ajustement = BigDecimal.valueOf(100);
        if (quittanceDTO.getCodePrestation() != null) {
            log.debug("Code prestation quittanceDTO: {}", quittanceDTO.getCodeOperation());
            DetailsPanierPrestDTO detailsPanierPrestation = paramServiceClient.detailsPanierBycodePrestation(quittanceDTO.getCodePrestation());
            if (!detailsPanierPrestation.getFacturationPanier() && detailsPanierPrestation.getPrixFixePanier().compareTo(BigDecimal.ZERO) > 0) {
                /*panier n'est une facturation (a prix fixe > 0)*/
                ajustement = quittanceDTO.getMntbon().subtract(detailsPanierPrestation.getPrixFixePanier().multiply(quittanceDTO.getQuantitePrestation())).multiply(ajustement).divide(quittanceDTO.getMntbon(), 7, RoundingMode.HALF_UP);
            }

            log.debug("ajustement {}", ajustement);
            if (!detailsPanierPrestation.getFacturationPanier()) {
                for (MvtQuittanceDTO mvtsto : quittanceDTO.getMvtQuittance()) {
                    mvtsto.setAjustement(ajustement);
                }
            }
        }
        if (quittanceDTO.getCodeOperation() != null) {
            log.debug("Code Operation quittanceDTO: {}", quittanceDTO.getCodeOperation());
            OperationDTO operationDTO = paramServiceClient.findOperationbyCode(quittanceDTO.getCodeOperation());
            if (!operationDTO.getFacturationPanier() && operationDTO.getPrixFixePanier().compareTo(BigDecimal.ZERO) > 0) {
                ajustement = quittanceDTO.getMntbon().subtract(operationDTO.getPrixFixePanier()).multiply(ajustement).divide(quittanceDTO.getMntbon(), 7, RoundingMode.HALF_UP);
            }
            log.debug("ajustement {}", ajustement);
            if (!operationDTO.getFacturationPanier()) {
                for (MvtQuittanceDTO mvtsto : quittanceDTO.getMvtQuittance()) {
                    mvtsto.setAjustement(ajustement);
                }
            }
        }
        quittanceDTO.calcul(societe, pharmacieExterne);
        log.debug("**********     mnt bon     {}   ************", quittanceDTO.getMntbon());
        log.debug("**********     partiePEC bon     {}   ************", quittanceDTO.getPartiePEC());
        log.debug("**********     partiePatient bon     {}   ************", quittanceDTO.getPartiePatient());
        quittanceDTO.setMntbon(quittanceDTO.getMntbon().setScale(2, RoundingMode.HALF_UP));
        quittanceDTO.setPartiePEC(quittanceDTO.getPartiePEC().setScale(2, RoundingMode.HALF_UP));
        quittanceDTO.setPartiePatient(quittanceDTO.getPartiePatient().setScale(2, RoundingMode.HALF_UP));
        return quittanceDTO;
    }

    /**
     * Update partie patient pour list des quittances.
     *
     * @param quittanceDTOs list des quittances pour update partie patient
     * @return Boolean
     */
    public Boolean updatePartiePatient(List<QuittanceDTO> quittanceDTOs) {
        List<String> listNumdoss = quittanceDTOs.stream().map(quittanceDTO -> quittanceDTO.getNumdoss()).distinct().collect(Collectors.toList());
        checkBusinessLogique(listNumdoss.size() == 1, "quittance.unique.dossier");
        String numdoss = listNumdoss.get(0);
        List<String> codes = quittanceDTOs.stream().map(quittanceDTO -> quittanceDTO.getNumbon()).distinct().collect(Collectors.toList());
        List<Facture> listQuittances = findAll(numdoss, codes);
        for (Facture facture : listQuittances) {
            QuittanceDTO quittanceDTO = quittanceDTOs.stream().filter(item -> {
                return item.getNumbon().equals(facture.getNumbon());
            }).findFirst()
                    .orElse(null);
            facture.setPartiePEC(quittanceDTO.getPartiePEC());
            facture.setPartiePatient(quittanceDTO.getPartiePatient());
            BigDecimal tauxCouverture = (facture.getPartiePEC().multiply(BigDecimal.valueOf(100))).divide(facture.getMntbon(), 7, RoundingMode.HALF_UP);
            for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                mvtsto.setTauxCouverture(tauxCouverture);
            }
        }
        factureRepository.save(listQuittances);
        avoirService.updatePartiePatient(quittanceDTOs);
        return Boolean.TRUE;
    }

    /**
     * Edition FactureAV by numBon.
     *
     * @param numBon the numBon of the entity
     * @return
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public byte[] edition(String numBon) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        Facture facture = factureRepository.findOne(numBon);
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        reportClientDoc.open("Reports/Quittance" + local + ".rpt", 0);
        Preconditions.checkFound(facture, "factureAV.NotFound");
        FactureEditionDTO factureDTO = factureFactory.factureToFactureEditionDTO(facture);
        List<String> codeAdmissions = new ArrayList<>();
        codeAdmissions.add(facture.getNumdoss());
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        if (!clients.isEmpty()) {
            factureDTO.setClient(clients.get(0));
        }
        List<Integer> codeUnites = new ArrayList<>();
        facture.getMvtstoCollection().forEach(x -> {
            codeUnites.add(x.getUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        factureDTO.getMvtStoCollection().forEach((mvtstoDTO) -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityCode())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            mvtstoDTO.setUnityCode(unite.getCode());
            mvtstoDTO.setUnityDesignation(unite.getDesignation());
        });
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(factureDTO.getCodeDepot());
        factureDTO.setDesignationDepot(depot.getDesignation());
        reportClientDoc
                .getDatabaseController().setDataSource(java.util.Arrays.asList(factureDTO), FactureEditionDTO.class,
                        "entete", "entete");
        reportClientDoc
                .getDatabaseController().setDataSource(java.util.Arrays.asList(factureDTO.getClient()), AdmissionDemandePECDTO.class,
                        "client", "client");
        reportClientDoc
                .getDatabaseController().setDataSource(factureDTO.getMvtStoCollection(), MvtstoDTO.class,
                        "mvtstoCollection", "mvtstoCollection");
        reportClientDoc
                .getSubreportController().getSubreport("tva").getDatabaseController().setDataSource(factureDTO.getBasetvaFactureCollection(), BaseTVADTO.class,
                "Commande", "Commande");
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

    /**
     * recalcul taux couverture pour list des quittances and avoirs.
     *
     * @param codes list des ids pour des quittances
     * @param codePriceList code price list
     * @param codeListCouverture code list couverture
     * @param codeNatureAdmission code nature admisssion
     * @param codeConvention code convention
     * @param codeSociete code societe
     * @param numdoss code admission
     * @param pharmacieExterne test pharmacie externe
     * @return Boolean
     */
    @Transactional(
            readOnly = true
    )
    public List<BaseAvoirQuittance> recalculTauxCouverture(List<String> codes, Integer codePriceList, Integer codeListCouverture,
            Integer codeNatureAdmission, Integer codeConvention, String numdoss, Integer codeSociete, Boolean pharmacieExterne) {
        SocieteDTO societe = paramServiceClient.findSocietebyCode(codeSociete);
        List<BaseAvoirQuittance> resultat = new ArrayList<>();
        List<Facture> listQuittances = findAll(numdoss, codes);
        List<Integer> codarts = listQuittances.stream().flatMap(x -> x.getMvtstoCollection().stream()).map(x -> x.getMvtstoPK().getCodart()).collect(Collectors.toList());
        List<RemiseConventionnelleDTO> remiseConventionnelles = paramServiceClient.pricelisteParArticle(codarts, codePriceList, codeListCouverture, codeNatureAdmission, codeConvention, pharmacieExterne);
        Collection<Integer> coddeps = listQuittances.stream().map(x -> x.getCoddep()).collect(Collectors.toList());
        List<DepotDTO> depotDTOs = paramAchatServiceClient.findDepotsByCodes(coddeps);

        for (Facture facture : listQuittances) {
            for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                RemiseConventionnelleDTO remiseConventionnelle = remiseConventionnelles.stream().filter(
                        item -> item.getCodeArticle().equals(mvtsto.getMvtstoPK().getCodart())).findFirst().orElse(null);
                checkBusinessLogique(remiseConventionnelle != null, "error-remise-article");
                mvtsto.setTauxCouverture(remiseConventionnelle.getTauxCouverture());
            }
            facture.calculPartiePatientPartiePEC(societe, pharmacieExterne);
        }
        for (Facture facture : listQuittances) {
            facture.setPartiePEC(facture.getPartiePEC().setScale(2, RoundingMode.HALF_UP));
            facture.setPartiePatient(facture.getPartiePatient().setScale(2, RoundingMode.HALF_UP));
        }
        List<FactureDTO> listFactureDTO = factureFactory.factureToFactureDTOLazys(listQuittances);
        listFactureDTO.forEach(factureDTO -> {
            DepotDTO depotDTO = depotDTOs.stream().filter(
                    item -> item.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            checkBusinessLogique(!depotDTO.getDesignation().equals("depot.deleted"), "Depot [" + depotDTO.getDesignation() + "] introuvable");
            NatureDepotDTO natureDepot = depotDTO.getNatureDepot().stream().filter(item -> item.getCategorieDepot().equals(factureDTO.getCategDepot())).findFirst().orElse(null);
            checkBusinessLogique(natureDepot != null, "missing-prestation-depot");
            factureDTO.setMntmpl(BigDecimal.valueOf(factureDTO.getMvtstoCollection().stream().map(x -> (x.getPrixBrute().multiply(x.getQuantite())).multiply(BigDecimal.valueOf(100).add(x.getTautva())).divide(BigDecimal.valueOf(100))).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
            factureDTO.setCodePrestation(natureDepot.getPrestationID());
        });
        resultat.addAll(listFactureDTO);
        resultat.addAll(avoirService.recalculTauxCouverture(codes, codePriceList, codeListCouverture, codeNatureAdmission, codeConvention, numdoss, societe, pharmacieExterne));
        return resultat;
    }

    /**
     * Get all the factures.
     *
     * @param fromDate
     * @param toDate
     * @param coddep
     * @param etatPatient
     * @param search
     * @param withClient
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<FactureDTO> findQuittances(LocalDateTime fromDate,
            LocalDateTime toDate,
            Integer coddep, List<ReceptionConstants> etatPatient, String search, Boolean withClient
    ) {

        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(fromDate, () -> _facture.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _facture.datbon.loe(toDate))
                .optionalAnd(coddep, () -> _facture.coddep.eq(coddep))
                .optionalAnd(search, () -> _facture.numaffiche.like("%" + search + "%").or(_facture.numdoss.like("%" + search + "%")))
                .and(_facture.codAnnul.isNull());
        log.debug("Request to get All Factures");
        List<Facture> result = (List<Facture>) factureRepository.findAll(builder);

        List<FactureDTO> resultDTO = factureFactory.factureToFactureDTOs(result);
        if (withClient) {
            List<String> codeAdmissions = result.stream().map(x -> {
                return x.getNumdoss();
            }).distinct().collect(Collectors.toList());
            List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
            resultDTO.forEach(factureDTO -> {
                AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(factureDTO.getNumdoss())).findFirst().orElse(null);
                checkBusinessLogique(client != null, "missing-client");
                factureDTO.setClient(client);
            });
            if (etatPatient != null) {
                List<Integer> codeEtatAdmission = etatPatient.stream().map(x -> {
                    return x.code();
                }).distinct().collect(Collectors.toList());
                resultDTO = resultDTO.stream().filter(item -> codeEtatAdmission.contains(item.getClient().getCodeEtatPatient())).collect(toList());
            }
        }

        List<Integer> codeDepots = resultDTO.stream().map(x -> {
            return x.getCodeDepot();
        }).distinct().collect(Collectors.toList());
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        resultDTO.forEach(factureDTO -> {
            DepotDTO depot = listDepot.stream().filter(x -> x.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            checkBusinessLogique(depot != null, "missing-depot");
            factureDTO.setDesignationDepot(depot.getDesignation());
        });
        List<String> numbons = resultDTO.stream().map(FactureDTO::getNumbon).distinct().collect(Collectors.toList());
        List<FactureFactureAV> factureFactureAVs = avoirService.findFactureFactureAV(numbons, withClient);
        resultDTO.forEach(factureDTO -> {
            List<FactureDTO> factureAVs = factureFactureAVs.stream().filter(x -> x.getNumBonFacture().equals(factureDTO.getNumbon())).map(FactureFactureAV::getFactureAV)
                    .collect(Collectors.toList());
            factureDTO.setFactureCorespondantes(factureAVs);
        });
        return resultDTO;
    }

    public List<FactureFactureAV> findFactureFactureAV(List<String> codeQuittances
    ) {
        List<FactureFactureAV> factureFactureAVs = new ArrayList<>();
        Integer numberOfChunks = (int) Math.ceil((double) codeQuittances.size() / 2000);
        for (int i = 0; i < numberOfChunks; i++) {
            List<String> codesChunk = codeQuittances.subList(i * 2000, Math.min(i * 2000 + 2000, codeQuittances.size()));
            factureFactureAVs.addAll(mvtstomvtstoAVRepository.findFactureFactureAVByNumBonMvtstoAV(codesChunk));
        }
        List<String> codes = factureFactureAVs.stream().map(x -> {
            return x.getNumBonFacture();
        }).distinct().collect(Collectors.toList());
        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_facture.numbon.in(codes));
        List<Facture> result = (List<Facture>) factureRepository.findAll(builder);
        List<FactureDTO> resultDTO = factureFactory.factureToFactureDTOs(result);
        List<String> codeAdmissions = result.stream().map(x -> {
            return x.getNumdoss();
        }).distinct().collect(Collectors.toList());
        List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
        resultDTO.forEach(factureDTO -> {
            AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(factureDTO.getNumdoss())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(client != null, "missing-client");
            factureDTO.setClient(client);
        });

        List<Integer> codeDepots = resultDTO.stream().map(x -> {
            return x.getCodeDepot();
        }).distinct().collect(Collectors.toList());
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        resultDTO.forEach(factureDTO -> {
            DepotDTO depot = listDepot.stream().filter(x -> x.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(depot != null, "missing-depot");
            factureDTO.setDesignationDepot(depot.getDesignation());
        });
        factureFactureAVs.forEach(factureFactureAV -> {
            FactureDTO factureDTO = resultDTO.stream().filter(x -> x.getNumbon().equals(factureFactureAV.getNumBonFacture())).findFirst().orElse(null);
            factureFactureAV.setFacture(factureDTO);
        });
        return factureFactureAVs;
    }

    /**
     * Delete facture by liste id.
     *
     * @param numdoss
     * @param prestationParDemandeDTOs
     * @param codeMotifSuppression
     * @param withFacturation annulation facturation quittance
     * @param isPanier
     * @return
     */
    public List<FactureDTO> deletesByCodeDemande(String numdoss, List<PrestationParDemandeDTO> prestationParDemandeDTOs,
            Integer codeMotifSuppression,
            Boolean withFacturation,
            Boolean isPanier) {
        List<Facture> factures = new ArrayList<>();
        for (PrestationParDemandeDTO prestationParDemandeDTO : prestationParDemandeDTOs) {
            factures.addAll(deleteByCodeDemande(numdoss, prestationParDemandeDTO.getCodeDemande(), prestationParDemandeDTO.getCodePrestation(), isPanier));
        }
        factures = factureRepository.save(factures);
        List<FactureDTO> factureDTOs = factureFactory.factureToFactureDTOs(factures);
        if (withFacturation.equals(Boolean.TRUE)) {
            List<String> quittancesIDs = factures.stream().map(Facture::getNumbon).distinct().collect(Collectors.toList());
            Boolean deleteFacturation = receptionServiceClient.deleteFacturationByNumQuittance(codeMotifSuppression, quittancesIDs, numdoss);
            checkBusinessLogique(Objects.equals(deleteFacturation, Boolean.TRUE), "error-facturation");
        }
        return factureDTOs;
    }

    /**
     * Delete facture by id.
     *
     * @param id the id of the entity
     * @param codeMotifSuppression
     * @param withFacturation annulation facturation quittance
     * @return Facture
     */
    private List<Facture> deleteByCodeDemande(String numdoss, Long codeDemande, Integer codePrestation, Boolean isPanier) {
        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(_facture.numdoss.eq(numdoss))
                .and(_facture.codeDemande.eq(codeDemande))
                .and(_facture.codePrestation.eq(codePrestation))
                .and(_facture.codAnnul.isNull())
                .and(_facture.etatbon.eq(Boolean.FALSE))
                .and(_facture.integrer.eq(Boolean.FALSE));
        List<Facture> factures = (List<Facture>) factureRepository.findAll(builder);
        for (Facture facture : factures) {
            if (!isPanier) {
                checkBusinessLogique(!facture.getPanier(), "facture-is-panier");
            }
            //test inventaire       
            List<Integer> codarts = facture.getMvtstoCollection().stream().map(x -> x.getMvtstoPK().getCodart()).collect(Collectors.toList());
            List<ArticleDTO> listArticleDTOs = paramAchatServiceClient.articleFindbyListCode(codarts);
            List<Integer> categArticleIDs = listArticleDTOs.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
            List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, facture.getCoddep());
            if (categArticleUnderInventory.size() > 0) {
                List<String> articlesUnderInventory = listArticleDTOs.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
                throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
            }

            LocalDateTime date = LocalDateTime.now();
            facture.setDatAnnul(date);
            facture.setCodAnnul(SecurityContextHolder.getContext().getAuthentication().getName());
            if (facture.getNumbonRecept() == null) {
                for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                    mvtsto.setQteben(BigDecimal.ZERO);
                    for (DetailMvtsto detailMvtsto : mvtsto.getDetailMvtstoCollection()) {
                        detailMvtsto.setQteAvoir(detailMvtsto.getQte());
                        Depsto depsto = detailMvtsto.getDepsto();
                        depsto.setQte(depsto.getQte().add(detailMvtsto.getQte()));
                    }
                }

            } else {
                for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                    mvtsto.setQteben(BigDecimal.ZERO);
                }
                bonTransfertInterDepotService.cancelBonTransferInterDepotDepotFrs(facture.getNumbonTransfert());
                factureBAService.cancelBonReceptionDepotFrs(facture.getNumbonRecept());
            }
        }
        return factures;
    }

    /**
     * Delete facture by liste id.
     *
     * @param numdoss
     * @param prestationParDetailsAdmissionDTOs
     * @param codeMotifSuppression
     * @param withFacturation annulation facturation quittance
     * @param isPanier
     * @return
     */
    public List<FactureDTO> deletesByCodeDetailsAdmission(String numdoss, List<PrestationParDetailsAdmissionDTO> prestationParDetailsAdmissionDTOs,
            Integer codeMotifSuppression,
            Boolean withFacturation,
            Boolean isPanier) {
        List<Facture> factures = new ArrayList<>();
        for (PrestationParDetailsAdmissionDTO prestationParDetailsAdmissionDTO : prestationParDetailsAdmissionDTOs) {
            factures.addAll(deletesByCodeDetailsAdmission(numdoss, prestationParDetailsAdmissionDTO.getCodeDetailsAdmission(), prestationParDetailsAdmissionDTO.getCodePrestation(), isPanier));
        }
        factures = factureRepository.save(factures);
        List<FactureDTO> factureDTOs = factureFactory.factureToFactureDTOs(factures);
        if (withFacturation.equals(Boolean.TRUE)) {
            List<String> quittancesIDs = factures.stream().map(Facture::getNumbon).distinct().collect(Collectors.toList());
            Boolean deleteFacturation = receptionServiceClient.deleteFacturationByNumQuittance(codeMotifSuppression, quittancesIDs, numdoss);
            checkBusinessLogique(Objects.equals(deleteFacturation, Boolean.TRUE), "error-facturation");
        }
        return factureDTOs;
    }

    /**
     * Delete facture by id.
     *
     * @param id the id of the entity
     * @param codeMotifSuppression
     * @param withFacturation annulation facturation quittance
     * @return Facture
     */
    private List<Facture> deletesByCodeDetailsAdmission(String numdoss, Long codeDetailsAdmission, Integer codePrestation, Boolean isPanier) {
        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(_facture.numdoss.eq(numdoss))
                .and(_facture.codeDetailsAdmission.eq(codeDetailsAdmission))
                .and(_facture.codePrestation.eq(codePrestation))
                .and(_facture.codAnnul.isNull())
                .and(_facture.etatbon.eq(Boolean.FALSE))
                .and(_facture.integrer.eq(Boolean.FALSE));
        List<Facture> factures = (List<Facture>) factureRepository.findAll(builder);
        for (Facture facture : factures) {
            if (!isPanier) {
                checkBusinessLogique(!facture.getPanier(), "facture-is-panier");
            }

            //test inventaire       
            List<Integer> codarts = facture.getMvtstoCollection().stream().map(x -> x.getMvtstoPK().getCodart()).collect(Collectors.toList());
            List<ArticleDTO> listArticleDTOs = paramAchatServiceClient.articleFindbyListCode(codarts);
            List<Integer> categArticleIDs = listArticleDTOs.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
            List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, facture.getCoddep());
            if (categArticleUnderInventory.size() > 0) {
                List<String> articlesUnderInventory = listArticleDTOs.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
                throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
            }

            LocalDateTime date = LocalDateTime.now();
            facture.setDatAnnul(date);
            facture.setCodAnnul(SecurityContextHolder.getContext().getAuthentication().getName());
            if (facture.getNumbonRecept() == null) {
                for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                    mvtsto.setQteben(BigDecimal.ZERO);
                    for (DetailMvtsto detailMvtsto : mvtsto.getDetailMvtstoCollection()) {
                        detailMvtsto.setQteAvoir(detailMvtsto.getQte());
                        Depsto depsto = detailMvtsto.getDepsto();
                        depsto.setQte(depsto.getQte().add(detailMvtsto.getQte()));
                    }
                }

            } else {
                for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
                    mvtsto.setQteben(BigDecimal.ZERO);
                }
                bonTransfertInterDepotService.cancelBonTransferInterDepotDepotFrs(facture.getNumbonTransfert());
                factureBAService.cancelBonReceptionDepotFrs(facture.getNumbonRecept());
            }
        }
        return factures;
    }

    public List<FactureDTO> findByNumbonComplementaire(String numbonComplementaire) {
        log.debug("Request to find By numbonComplementaire");
        List<FactureDTO> resultDTO = new ArrayList<>();
        List<Facture> result = factureRepository.findByNumbonComplementaire(numbonComplementaire);
        if (!result.isEmpty()) {
            resultDTO = factureFactory.factureToFactureDTOs(result);
            List<String> numbons = resultDTO.stream().map(FactureDTO::getNumbon).distinct().collect(Collectors.toList());
            List<FactureFactureAV> factureFactureAVs = avoirService.findFactureFactureAV(numbons, Boolean.FALSE);
            resultDTO.forEach(factureDTO -> {
                List<FactureDTO> factureAVs = factureFactureAVs.stream()
                        .filter(x -> x.getNumBonFacture().equals(factureDTO.getNumbon())).map(FactureFactureAV::getFactureAV)
                        .collect(Collectors.toList());
                factureDTO.setFactureCorespondantes(factureAVs);
            });
        }
        return resultDTO;
    }

    /**
     * Get all the factures.
     *
     * @param idOrdonnances
     * @param lazy
     * @param withClient
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<FactureDTO> findByIdOrdonnanceIn(Long[] idOrdonnances, Boolean lazy, Boolean withClient) {
        log.debug("Request to find By IdOrdonnanceIn");
        QFacture _facture = QFacture.facture;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(_facture.idOrdonnance.in(idOrdonnances));
        log.debug("Request to get All Factures");
        List<Facture> result = (List<Facture>) factureRepository.findAll(builder);
        List<FactureDTO> resultDTO = new ArrayList<>();
        if (lazy) {
            resultDTO = factureFactory.factureToFactureDTOLazys(result);
        } else {
            resultDTO = factureFactory.factureToFactureDTOs(result);
        }

        if (withClient) {
            List<String> codeAdmissions = new ArrayList<>();
            result.forEach(facture -> {
                codeAdmissions.add(facture.getNumdoss());
            });
            List<AdmissionDemandePECDTO> clients = receptionServiceClient.findAdmissionByCodeInForDemandePEC(codeAdmissions, null);
            resultDTO.forEach(factureDTO -> {
                AdmissionDemandePECDTO client = clients.stream().filter(x -> x.getCode().equals(factureDTO.getNumdoss())).findFirst().orElse(null);
                checkBusinessLogique(client != null, "missing-client");
                factureDTO.setClient(client);
            });
            List<Integer> codeDepots = new ArrayList<>();
            resultDTO.forEach(x -> {
                codeDepots.add(x.getCodeDepot());
            });
            List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
            resultDTO.forEach(factureDTO -> {
                DepotDTO depot = listDepot.stream().filter(x -> x.getCode().equals(factureDTO.getCodeDepot())).findFirst().orElse(null);
                checkBusinessLogique(depot != null, "missing-depot");
                factureDTO.setDesignationDepot(depot.getDesignation());
            });
        }
        return resultDTO;
    }

    @Transactional(readOnly = true)

    public List<Facture> findByCoddepAndCategDepotAndNumbonNotIn(Integer coddep, CategorieDepotEnum categDepot, List<String> numbonsInBtfe) {

        return factureRepository.findByCoddepAndCategDepotAndNumbonNotIn(coddep, categDepot, numbonsInBtfe);
    }

    @Transactional(readOnly = true)
    public List<String> findListNumBonsByNumDoss(String numDoss) {
        log.debug("Request to find num bons avoir By numDoss");
        return factureRepository.findListNumBonsByNumDoss(numDoss);
    }

    /**
     * checkFacturationOrdonnance
     *
     * @param ordonnanceDTOs
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<OrdonnanceDTO> checkFacturationOrdonnance(List<OrdonnanceDTO> ordonnanceDTOs) {
        log.debug("Request to check facturation irdonnance");

        Long[] idOrdonnances = ordonnanceDTOs.stream().map(x -> x.getId()).toArray(Long[]::new);

        List<FactureDTO> factureDTOs = findByIdOrdonnanceIn(idOrdonnances, false, false);
        ordonnanceDTOs.forEach(x -> {
            Optional<FactureDTO> quittanceOpt = factureDTOs.stream()
                    .filter(q -> q.getIdOrdonnance() != null && q.getIdOrdonnance().compareTo(x.getId()) == 0)
                    .findFirst();
            if (quittanceOpt.isPresent()) {
                x.setIsFacturee(true);
            }
        });
        return ordonnanceDTOs;
    }
}
