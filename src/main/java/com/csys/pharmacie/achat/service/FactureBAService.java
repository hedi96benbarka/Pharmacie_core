package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import com.csys.pharmacie.achat.domain.BaseTvaAvoirFournisseur;
import com.csys.pharmacie.achat.domain.BaseTvaReception;
import com.csys.pharmacie.achat.domain.BaseTvaRetourPerime;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.FcptfrsPH;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.QAvoirFournisseur;
import com.csys.pharmacie.achat.domain.QFactureBA;
import com.csys.pharmacie.achat.domain.QRetourPerime;

import com.csys.pharmacie.achat.domain.Receiving;
import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCa;
import com.csys.pharmacie.achat.domain.RetourPerime;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticleIMMODTO;
import com.csys.pharmacie.achat.dto.BaseTvaReceptionDTO;
import com.csys.pharmacie.achat.factory.FactureBAFactory;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.BonEditionDTO;
import com.csys.pharmacie.achat.dto.BonRetourDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatLazyDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.DetailCommandeAchatDTO;
import com.csys.pharmacie.achat.dto.DetailEditionDTO;
import com.csys.pharmacie.achat.dto.EmplacementDTO;
import com.csys.pharmacie.achat.dto.FactureBADTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
//import com.csys.pharmacie.achat.dto.PriceVarianceDTO;
import com.csys.pharmacie.achat.dto.ReceptionEditionDTO;
import com.csys.pharmacie.achat.dto.ReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.factory.AvoirFournisseurFactory;
import com.csys.pharmacie.achat.factory.DetailReceptionFactory;
import com.csys.pharmacie.achat.factory.MvtstoBAFactory;
import com.csys.pharmacie.achat.factory.RetourPerimeFactory;
import com.csys.pharmacie.achat.repository.AvoirFournisseurRepository;
import com.csys.pharmacie.achat.repository.FactureBARepository;
import com.csys.pharmacie.achat.repository.ReceptionDetailCARepository;
import com.csys.pharmacie.achat.repository.RetourPerimeRepository;
import com.csys.pharmacie.client.dto.ImmobilisationDTO;
import com.csys.pharmacie.client.dto.SiteDTO;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import static com.csys.pharmacie.helper.CategorieDepotEnum.IMMO;
import static com.csys.pharmacie.helper.CategorieDepotEnum.PH;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.util.Preconditions;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import com.csys.pharmacie.vente.service.PricingService;
import com.csys.pharmacie.helper.Convert;
import com.csys.pharmacie.helper.EmailDTO;
import static com.csys.pharmacie.helper.TypeBonEnum.RT;

import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.stock.repository.DepstoRepository;

import com.csys.pharmacie.transfert.service.CliniSysService;

import com.querydsl.core.BooleanBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class FactureBAService {

    protected static String receptionSurDeuxEtapes;

    @Value("${reception-en-deux-etapes}")
    public void setValidation(String validationReception) {
        receptionSurDeuxEtapes = validationReception;
    }

    protected final Logger log = LoggerFactory.getLogger(FactureBAService.class);
    protected final FactureBARepository factureBARepository;
    protected final ReceptionDetailCARepository receptionDetailCARepository;
    protected final ParamService paramService;
    protected final FcptFrsPHService fcptFrsPHService;
//    private final FactureBAFactory factureBAFactory;
    protected final StockService stockService;
    protected final DemandeServiceClient demandeServiceClient;
    protected final ReceptionDetailCAService receptionDetailCAService;
    protected final ReceptionTemporaireDetailCaService receptionTemporaireDetailCaService;
    protected final PricingService pricingService;
    protected final ParamAchatServiceClient paramAchatServiceClient;
    protected final ReceivingService receivingService;
    protected final EtatReceptionCAService etatReceptionCAService;
    protected final ParamServiceClient paramServiceClient;
    protected final MvtStoBAService mvtStoBAService;
    protected final AjustementRetourFournisseurService ajustementRetourFournisseurService;
    protected final RetourPerimeRepository retourPerimeRepository;
    protected final AvoirFournisseurFactory avoirFournisseurFactory;
    protected final AvoirFournisseurRepository avoirfournisseurRepository;
    protected final InventaireService inventaireService;
    protected final ImmobilisationService immobilisationService;
    protected final DepstoRepository depstoRepository;
    protected final ReceptionTemporaireService receptionTemporaireService;
    protected final MessageSource messages;
    protected final CliniSysService cliniSysService;
//    @Autowired
//    private DetailReceptionFactory detailReceptionFactory;
//    public DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public FactureBAService(FactureBARepository factureBARepository, ReceptionDetailCARepository receptionDetailCARepository, ParamService paramService, FcptFrsPHService fcptFrsPHService, StockService stockService, DemandeServiceClient demandeServiceClient, ReceptionDetailCAService receptionDetailCAService, ReceptionTemporaireDetailCaService receptionTemporaireDetailCaService, PricingService pricingService, ParamAchatServiceClient paramAchatServiceClient, ReceivingService receivingService, EtatReceptionCAService etatReceptionCAService, ParamServiceClient paramServiceClient, MvtStoBAService mvtStoBAService, AjustementRetourFournisseurService ajustementRetourFournisseurService, RetourPerimeRepository retourPerimeRepository, @Lazy AvoirFournisseurFactory avoirFournisseurFactory, AvoirFournisseurRepository avoirfournisseurRepository, InventaireService inventaireService, ImmobilisationService immobilisationService, DepstoRepository depstoRepository, @Lazy ReceptionTemporaireService receptionTemporaireService, MessageSource messages, CliniSysService cliniSysService) {
        this.factureBARepository = factureBARepository;
        this.receptionDetailCARepository = receptionDetailCARepository;
        this.paramService = paramService;
        this.fcptFrsPHService = fcptFrsPHService;
        this.stockService = stockService;
        this.demandeServiceClient = demandeServiceClient;
        this.receptionDetailCAService = receptionDetailCAService;
        this.receptionTemporaireDetailCaService = receptionTemporaireDetailCaService;
        this.pricingService = pricingService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.receivingService = receivingService;
        this.etatReceptionCAService = etatReceptionCAService;
        this.paramServiceClient = paramServiceClient;
        this.mvtStoBAService = mvtStoBAService;
        this.ajustementRetourFournisseurService = ajustementRetourFournisseurService;
        this.retourPerimeRepository = retourPerimeRepository;
        this.avoirFournisseurFactory = avoirFournisseurFactory;
        this.avoirfournisseurRepository = avoirfournisseurRepository;
        this.inventaireService = inventaireService;
        this.immobilisationService = immobilisationService;
        this.depstoRepository = depstoRepository;
        this.receptionTemporaireService = receptionTemporaireService;
        this.messages = messages;
        this.cliniSysService = cliniSysService;
    }

    @Transactional(readOnly = true)
    public List<BonRecepDTO> findAllReception(FactureBA exempleFactureBa, LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted, Boolean invoiced, Integer codeArticle) {

        log.debug("Request to get All receptions");

        List<FactureBA> result = findAll(exempleFactureBa, fromDate, toDate, deleted, invoiced, codeArticle);

        log.debug("result size est {}", result.size());
        List<BonRecepDTO> resultDTO = new ArrayList<>();
        Collection<SiteDTO> listeSiteDTOs = paramServiceClient.findAllSites(Boolean.TRUE, "B");
        if (!result.isEmpty()) {
            List<String> codesFrs = result.stream().map(item -> item.getCodfrs()).distinct().collect(Collectors.toList());
            List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);

            result.forEach((factureBA) -> {
                BonRecepDTO bonRecepDTO = FactureBAFactory.factureBAToBonRecepDTO(factureBA);
                Optional<FournisseurDTO> optFrs = fournisseurs.stream().filter(item -> item.getCode().equals(factureBA.getCodfrs())).findFirst();
                Preconditions.checkBusinessLogique(optFrs.isPresent(), "reception.get-all.missing-frs", factureBA.getCodfrs());
                bonRecepDTO.setFournisseur(optFrs.get());
                if (factureBA.getNumAfficheRecetionTemporaire() != null) {
                    bonRecepDTO.setNumAfficheRecetionTemporaire(factureBA.getNumAfficheRecetionTemporaire());
                }
                if (listeSiteDTOs != null) {// en cas de fallback parametrage ne pas bloquer le retour de ws mais simplement ne pas retourner les designations des sites 
                    Optional<SiteDTO> matchingSiteDTO = listeSiteDTOs
                            .stream()
                            .filter(elt -> elt.getCode().equals(factureBA.getCodeSite())).findFirst();
                    if (matchingSiteDTO.isPresent()) {
                        bonRecepDTO.setDesignationSite(matchingSiteDTO.get().getDesignationAr());
                        bonRecepDTO.setCodeSite(factureBA.getCodeSite());
                    }
                }

//                .orElseThrow(() -> new IllegalBusinessLogiqueException("parametrage.clinique.error", new Throwable(dto.getNumAffiche())));
                resultDTO.add(bonRecepDTO);
            });
            // this may not seem logic, but the only interface in whch we must precise if the reception has a return or not, is the invoice interface. This interface will ask for the list of receptions of a given frs. So whenever the codfrs is supplied we will presume that it's a request from the invoice interface
            if (exempleFactureBa.getCodfrs() != null) {
                isReturned(resultDTO);
            }

        }
        return resultDTO;
    }

    @Transactional(readOnly = true)
    public List<BonRetourDTO> findAllReturns(FactureBA queriedFacBA, LocalDateTime fromDate, LocalDateTime toDate, Integer codeArticle) {
        log.debug("Request to get All returns");
        List<FactureBA> result = findAll(queriedFacBA, fromDate, toDate, null, null, codeArticle);

        List<String> codesFrs = result.stream().map(item -> item.getCodfrs()).distinct().collect(Collectors.toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
        Collection<SiteDTO> listeSiteDTOs = paramServiceClient.findAllSites(Boolean.TRUE, "B");
        List<BonRetourDTO> resultDTO = new ArrayList();
        result.forEach((factureBA) -> {
            BonRetourDTO bonRetourDTO = FactureBAFactory.factureBAToBonRetourDTOLazy(factureBA);

            FactureBA reception = factureBARepository.findOne(factureBA.getNumpiece());
            Optional<FournisseurDTO> optFrs = fournisseurs.stream().filter(item -> item.getCode().equals(factureBA.getCodfrs())).findFirst();
            Preconditions.checkBusinessLogique(optFrs.isPresent(), "returns.get-all.missing-frs", factureBA.getCodfrs());
            bonRetourDTO.setFournisseur(optFrs.get());
            bonRetourDTO.setNumAfficheReception(reception.getNumaffiche());
              if (listeSiteDTOs != null ) {// en cas de fallback parametrage ne pas bloquer le retour de ws mais simplement ne pas retourner les designations des sites 
                    Optional<SiteDTO> matchingSiteDTO = listeSiteDTOs
                            .stream()
                            .filter(elt -> elt.getCode().equals(factureBA.getCodeSite())).findFirst();
                    if (matchingSiteDTO.isPresent()) {
                        bonRetourDTO.setDesignationSite(matchingSiteDTO.get().getDesignationAr());
                        bonRetourDTO.setCodeSite(factureBA.getCodeSite());
                    }
                }
            
            resultDTO.add(bonRetourDTO);
        });
        return resultDTO;
    }

    @Transactional(readOnly = true)
    public List<FactureBA> findAll(FactureBA queriedFacBA, LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted, Boolean invoiced, Integer codeArticle) {
        QFactureBA _FactureBA = QFactureBA.factureBA;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(_FactureBA.typbon.eq(queriedFacBA.getTypbon()))
                .optionalAnd(queriedFacBA.getCategDepot(), () -> _FactureBA.categDepot.eq(queriedFacBA.getCategDepot()))
                .optionalAnd(fromDate, () -> _FactureBA.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FactureBA.datbon.loe(toDate))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _FactureBA.datAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _FactureBA.datAnnul.isNull())
                .optionalAnd(queriedFacBA.getCodfrs(), () -> _FactureBA.codfrs.eq(queriedFacBA.getCodfrs()))
                .optionalAnd(queriedFacBA.getCoddep(), () -> _FactureBA.coddep.eq(queriedFacBA.getCoddep()))
                .booleanAnd(Objects.equals(queriedFacBA.getAutomatique(), Boolean.TRUE), () -> _FactureBA.automatique.eq(Boolean.TRUE))
                .booleanAnd(Objects.equals(queriedFacBA.getAutomatique(), Boolean.FALSE), () -> _FactureBA.automatique.isNull().or(_FactureBA.automatique.eq(Boolean.FALSE)))
                .booleanAnd(Objects.equals(invoiced, Boolean.TRUE), () -> _FactureBA.factureBonReception().isNotNull())
                .booleanAnd(Objects.equals(invoiced, Boolean.FALSE), () -> _FactureBA.factureBonReception().isNull())
                .optionalAnd(codeArticle, () -> _FactureBA.detailFactureBACollection.any().pk().codart.eq(codeArticle));

        return (List<FactureBA>) factureBARepository.findAll(builder);
    }

    @Transactional(readOnly = true)
    public FactureBA findOne(String numBon) {
        FactureBA reception = factureBARepository.findOne(numBon);
        Preconditions.checkBusinessLogique(reception != null, "reception.missing");

        return reception;
    }

    @Transactional(readOnly = true)
    public BonRecepDTO findOneReception(String numBon) {
        FactureBA reception = factureBARepository.findOne(numBon);
        Preconditions.checkBusinessLogique(reception != null, "reception.missing");
        BonRecepDTO bonRecepDTO = FactureBAFactory.factureBAToBonRecepDTO(reception);
        bonRecepDTO.setDesignationDepot(paramAchatServiceClient.findDepotByCode(reception.getCoddep()).getDesdep());
        bonRecepDTO.setFournisseur(paramAchatServiceClient.findFournisseurByCode(reception.getCodfrs()));
//        bonRecepDTO.setCodeFournisseu(reception.getCodfrs());
//        bonRecepDTO.setFournisseurExonere(reception.getFournisseurExonere());

        SiteDTO siteDTO = reception.getCodeSite() != null ? paramServiceClient.findSiteByCode(reception.getCodeSite()) : null;
        if (reception.getCodeSite() != null) {

            Preconditions.checkBusinessLogique(siteDTO != null, "parametrage.clinique.error");
            bonRecepDTO.setDesignationSite(siteDTO.getDesignationAr());
            bonRecepDTO.setCodeSite(reception.getCodeSite());
        }
        String ipAdressSite = reception.getCodeSite() != null ? siteDTO.getIpAdress() : null;

        Set< Integer> setEmplacement = new HashSet();
        List<Integer> codeUnites = new ArrayList();
        List<MvtstoBADTO> detailsReception = new ArrayList();
        reception.getDetailFactureBACollection().forEach(mvtstoBA -> {
            codeUnites.add(mvtstoBA.getCodeUnite());
            if (mvtstoBA.getCodeEmplacement() != null) {
                setEmplacement.add(mvtstoBA.getCodeEmplacement());
            }
            MvtstoBADTO dto = new MvtstoBADTO();
            MvtstoBAFactory.toDTO(mvtstoBA, dto);
            dto.setQteReturned(mvtstoBA.getQuantite().subtract(mvtstoBA.getQtecom()));
            detailsReception.add(dto);
        });
        bonRecepDTO.setDetails((List) detailsReception);
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        List<EmplacementDTO> emplacements = reception.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(setEmplacement, ipAdressSite) : new ArrayList();
        if (reception.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            Preconditions.checkBusinessLogique(emplacements != null, "error-emplacaments");
        }
        bonRecepDTO.getDetails().forEach(item -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getCodeUnite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            item.setUnitDesignation(unite.getDesignation());

            if (reception.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                EmplacementDTO emplacement = emplacements.stream()
                        .filter(x -> x.getCode().equals(item.getCodeEmplacement()))
                        .findFirst().orElse(null);
                if (item.getCodeEmplacement() != null) {
                    item.setDesignationEmplacement(emplacement.getDesignation());
                    item.setCodeDepartementEmplacement(emplacement.getCodeDepartement().getCode());
                    item.setDesigationDepartementEmplacement(emplacement.getCodeDepartement().getDesignation());
                }
            }
        });
        List<Integer> listCodesCA = reception.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());
        bonRecepDTO.setPurchaseOrders(demandeServiceClient.findListCommandeAchat(listCodesCA, LocaleContextHolder.getLocale().getLanguage()));

        return bonRecepDTO;
    }

    @Transactional(readOnly = true)
    public BonRetourDTO findOneRetour(String numBon) {
        FactureBA retour = factureBARepository.findOne(numBon);
        Preconditions.checkBusinessLogique(retour != null, "reception.missing");
        BonRetourDTO result = FactureBAFactory.factureBAToBonRetourDTOEager(retour);
        result.setDesignationDepot(paramAchatServiceClient.findDepotByCode(retour.getCoddep()).getDesdep());
        result.setFournisseur(paramAchatServiceClient.findFournisseurByCode(retour.getCodfrs()));

        FactureBA reception = factureBARepository.findOne(retour.getNumpiece());
        result.setDateReception(reception.getDatbon());
        result.setMntBonReception(reception.getMntbon());
        result.setNumAfficheReception(reception.getNumaffiche());
        result.setReceptionID(reception.getNumbon());

        return result;
    }

    public List<BonRecepDTO> isReturned(List<BonRecepDTO> receptions) {
        List<String> recptionsID = receptions.stream().map(BonRecepDTO::getNumbon).collect(toList());
        List<FactureBA> returns = factureBARepository.findByNumpieceInAndTypbon(recptionsID, RT);
        receptions.forEach(item -> {
            boolean isReturned = returns.stream().anyMatch(rt -> rt.getNumpiece().equals(item.getNumbon()));
            item.setIsReturned(isReturned);
        });
        return receptions;
    }

    public BonRecepDTO isReturned(BonRecepDTO bonRecepDTO) {
//        String recptionID = receptions.stream().map(BonRecepDTO::getNumbon).collect(toList());
        Set<FactureBA> returns = factureBARepository.findByNumpieceAndTypbon(bonRecepDTO.getNumbon(), RT);
//        receptions.forEach(item -> {
        boolean isReturned = returns.stream().anyMatch(rt -> rt.getNumpiece().equals(bonRecepDTO.getNumbon()));
        bonRecepDTO.setIsReturned(isReturned);
//        });
        return bonRecepDTO;
    }

    public Set<MvtstoBADTO> findReturnedDetailsOfReception(String numbon) {

        Set<FactureBA> returns = factureBARepository.findByNumpieceAndTypbon(numbon, RT);
        return returns.stream()
                .flatMap(item -> item.getDetailFactureBACollection().stream()).map(item -> {
            MvtstoBADTO dto = new MvtstoBADTO();
            MvtstoBAFactory.toDTO(item, dto);
            return dto;
        }).collect(toSet());

    }

    public Set<FactureBA> findFactureBAByNumBonIn(List<String> numBons) {
        return factureBARepository.findByNumbonIn(numBons);

    }

    public List<BonRecepDTO> findBonReceptionByNumReceivingIn(List<Integer> codes) {
        Set<FactureBA> liste = factureBARepository.findByReceiving_codeIn(codes);
        List<BonRecepDTO> result = liste.stream().map(item -> FactureBAFactory.factureBAToBonRecepDTO(item)).collect(toList());
        return result;
    }

    public BonRecepDTO findBonReceptionByNumReceiving(Integer code) {
        FactureBA factureBA = factureBARepository.findByReceiving_code(code);
        BonRecepDTO bonRecepDTO = FactureBAFactory.factureBAToBonRecepDTO(factureBA);
        List<MvtstoBADTO> detailsReception = new ArrayList();
        factureBA.getDetailFactureBACollection().forEach(mvtstoBA -> {
            MvtstoBADTO dto = new MvtstoBADTO();
            MvtstoBADTO detailRecep = DetailReceptionFactory.toDTO(mvtstoBA, dto);
            detailRecep.setQteReturned(mvtstoBA.getQuantite().subtract(mvtstoBA.getQtecom()));
            detailsReception.add(detailRecep);
        });
        bonRecepDTO.setDetails((List) detailsReception);
        List<Integer> listCodesCA = factureBA.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());
        bonRecepDTO.setPurchaseOrders(demandeServiceClient.findListCommandeAchat(listCodesCA, LocaleContextHolder.getLocale().getLanguage()));
        bonRecepDTO.setDesignationDepot(paramAchatServiceClient.findDepotByCode(factureBA.getCoddep()).getDesdep());
        bonRecepDTO.setFournisseur(paramAchatServiceClient.findFournisseurByCode(factureBA.getCodfrs()));
        return bonRecepDTO;
    }

    @Transactional
    public BonRecepDTO saveBonReceptionOrReceptionTemporaire(BonRecepDTO bonReeceptionDTO) throws ParseException {
        BonRecepDTO result = new BonRecepDTO();
        if (receptionSurDeuxEtapes.equalsIgnoreCase("true")) {
            log.debug("reception sur deux etapes");
            receptionTemporaireService.save(bonReeceptionDTO);
        } else {
            log.debug("reception sur une seule etape");
            result = ajoutBonReception(bonReeceptionDTO);
        }
        return result;

    }

    public abstract void processStockAndPricesAfterTransefrtCompanyToBranch(TransfertCompanyBranch transfertCompanyBranch, TransfertCompanyBranchDTO transfertCompanyBranchDTO, List<ArticleDTO> articles);

    public abstract BonRecepDTO ajoutBonReception(BonRecepDTO bonReceptionDTO);

    public abstract FactureBA ajoutBonReceptionDepotFrs(Facture facture, DepotDTO depotPrincipale, FournisseurDTO fourniss, Boolean appliquerExoneration, String exoneration);

    public abstract BonRecepDTO ajoutReeceptionSuiteValidationReceptionTemporaire(BonRecepDTO receptionDTO);

    public abstract ImmobilisationDTO genererImmoDTO(MvtStoBA mvtsto, ArticleIMMODTO articleIMMO, String codeFournisseur, List<EmplacementDTO> emplacements, LocalDate datBon, String numAffiche);

    public abstract Depsto genererDepstoFromMvtstoBa(MvtstoBADTO mvtstoBADTODTO, FactureBA factureBA);

    public abstract BonRecepDTO updateBonReception(BonRecepDTO bonReceptionDTO);

    public abstract List<ReceptionDetailCA> managingPurchaseOrdersForReception(BonRecepDTO bonRecepDTO, String numBon);

//    {
    //        List<DetailCommandeAchatDTO> purchaseOrdersDetails = getDetailOfPurchaseOrders(bonRecepDTO.getPurchaseOrdersCodes());//detail commande +historique des reception et recep temp faite sur cette commande 100/recep 80 qterest =20
    //        List<ReceptionDetailCA> listeReceptionDetailCa = processPurchaseOrders(bonRecepDTO, purchaseOrdersDetails, numBon);//qteRest c1 20= recep --10 ==> qte rest :10
    //        return listeReceptionDetailCa;
    //    }
    ////adding preaddReceptionTemp
    //
    public abstract List<ReceptionTemporaireDetailCa> managingPurchaseOrdersForReceptionTemp(ReceptionTemporaireDTO receptionTempDTO);

    public abstract List<DetailCommandeAchatDTO> getDetailOfPurchaseOrders(List<Integer> purchaseOrdersCodes);

    ////***************cancel receptions ********//
    public abstract BonRecepDTO cancelBonReception(String numBon);

    public abstract BonRetourDTO ajoutBonRetour(BonRetourDTO bonRetourDTO);

    public abstract BonRecepDTO cancelBonReceptionDepotFrs(String numBon);

    public abstract Boolean cancelBonReceptionDepotFrsPermanent(String numBon);

    public void checkOpenInventoryForCategoriesInDeposit(Set<Integer> categArticleIDs, Integer codeDepot, List<ArticleDTO> articles) {
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(new ArrayList(categArticleIDs), codeDepot);
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = articles.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }
    }

    @Transactional
    public BonRecepDTO updateFournisseurBonReception(String numBon, String codeFournisseur) throws ParseException {
        log.debug("updating Fournisseru bon reception");
        FactureBA receptionInBase = factureBARepository.findOne(numBon);
        Preconditions.checkBusinessLogique(receptionInBase != null, "reception.update.notFound");
        Preconditions.checkBusinessLogique(receptionInBase.getDatAnnul() == null, "reception.update.canceled-reception");
        if (receptionInBase.getAutomatique() != null) {
            Preconditions.checkBusinessLogique(!receptionInBase.getAutomatique(), "reception.update.reception-automatique");
        }
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(receptionInBase.getIntegrer()), "facture-deja-integre");
        Preconditions.checkBusinessLogique(receptionInBase.getFactureBonReception() == null, "invoiced-reception");
//   car la veleur de l ancien code fournisseur va etre ecrasée
        String oldCodeFrs = receptionInBase.getCodfrs();
        log.debug("old code FRS{}", receptionInBase.getCodfrs());

        log.debug("new code FRS {}", codeFournisseur);

        Set<FactureBA> bonRetourCorrespondant = factureBARepository.findByNumpieceAndTypbon(numBon, TypeBonEnum.RT);
        log.debug("bonRetourCorrespondant{}", bonRetourCorrespondant);
        Preconditions.checkBusinessLogique(bonRetourCorrespondant.isEmpty(), "ficheStock.retourFournisseur");//retour

        FournisseurDTO fournisseurDto = paramAchatServiceClient.findFournisseurByCode(codeFournisseur);
        receptionInBase.setCodfrs(fournisseurDto.getCode());
        FcptfrsPH fcptFrs = fcptFrsPHService.findFirstByNumBon(numBon);
        fcptFrs.setCodFrs(fournisseurDto.getCode());

        Receiving receiving = receivingService.findReceiving(receptionInBase.getReceiving().getCode());

        Preconditions.checkBusinessLogique(receiving != null, "reception.add.missing-receiving");
        receiving.setFournisseur(codeFournisseur);

        Set<Integer> codesArt = receptionInBase.getDetailFactureBACollection().stream().map(item -> item.getCodart()).collect(Collectors.toSet());

        FactureBA factureBA = factureBARepository.findOne(numBon);
        factureBA.setCodfrs(codeFournisseur);
        //liste reception details ca sur les commandes de notre reception ==> check if there is a commande that has another reception
        List<Integer> commandeAchatIDs = receptionInBase.getRecivedDetailCA().stream().map(d -> d.getPk().getCommandeAchat()).collect(Collectors.toList());

        List<ReceptionDetailCA> detailsReceptionCa = receptionDetailCARepository.findByPk_CommandeAchatIn(commandeAchatIDs);

        log.debug("tous les reception details ca sur les commandes   {}", detailsReceptionCa);
        List<ReceptionDetailCA> listeDesCommandesReceptionnesAilleurs = detailsReceptionCa
                .stream()
                .filter(elt -> !elt.getPk().getReception().equals(numBon))
                .collect(Collectors.toList());

        log.debug("listeDesCommandesReceptionnesAilleurs {}", detailsReceptionCa);
        Preconditions.checkBusinessLogique(listeDesCommandesReceptionnesAilleurs.isEmpty(), "reception.add.commande-in-other-reception");

        List<MvtStoBA> listeArticleReceptionnesOldFrs = mvtStoBAService.findByPk_CodartInAndFactureBA_DatbonAfterAndCodeFournisseursAndTypeBon(codesArt, receptionInBase.getDatbon(), oldCodeFrs, TypeBonEnum.BA);
        log.debug("listeArticleReceptionnesOldFrs{}", listeArticleReceptionnesOldFrs);
        List<MvtStoBA> listeArticleReceptionnesNewFrs = mvtStoBAService.findByPk_CodartInAndFactureBA_DatbonAfterAndCodeFournisseursAndTypeBon(codesArt, receptionInBase.getDatbon(), codeFournisseur, TypeBonEnum.BA);
        log.debug("listeArticleReceptionnesNewFrs{}", listeArticleReceptionnesNewFrs);
        //traitement pour new codeFrs
        Set<Integer> codeArtsReceptionnesApresNewFournisseur = listeArticleReceptionnesNewFrs.stream().map(item -> item.getCodart()).distinct().collect(Collectors.toSet());
        List<MvtStoBA> listeToUpdateNewFournisseurNotReceivedAfter = new ArrayList(receptionInBase.getDetailFactureBACollection());
        log.debug("details listeToUpdateNewFournisseurNotReceivedAfter {}", listeToUpdateNewFournisseurNotReceivedAfter);
        listeToUpdateNewFournisseurNotReceivedAfter.removeIf(mvt -> codeArtsReceptionnesApresNewFournisseur.contains(mvt.getCodart()));
        if (!listeToUpdateNewFournisseurNotReceivedAfter.isEmpty()) {
            log.debug("details after remove sont {}", listeToUpdateNewFournisseurNotReceivedAfter);
            log.debug("codef egale {}", factureBA.getCodfrs());
            BonRecepDTO receptionToUpdateDTO = FactureBAFactory.factureBAToBonRecepDTOForRevertPrices(codeFournisseur, listeToUpdateNewFournisseurNotReceivedAfter);
            paramAchatServiceClient.updateArticleFournisseur(receptionToUpdateDTO);
        }
        //traitement pour oldcodeFRS

        Set<Integer> codeArtsReceptionnesApresOldFournisseur = listeArticleReceptionnesOldFrs.stream().map(item -> item.getCodart()).distinct().collect(Collectors.toSet());
        List<MvtStoBA> listeToUpdateOldFournisseurNotReceivedAfter = new ArrayList(receptionInBase.getDetailFactureBACollection());

        listeToUpdateOldFournisseurNotReceivedAfter.removeIf(mvt -> codeArtsReceptionnesApresOldFournisseur.contains(mvt.getCodart()));
        log.debug("details listeToUpdateOldFournisseurNotReceivedAfter {}", listeToUpdateOldFournisseurNotReceivedAfter);
        if (!listeToUpdateOldFournisseurNotReceivedAfter.isEmpty()) {
            log.debug("details old  after remove sont {}", listeToUpdateOldFournisseurNotReceivedAfter);
            BonRecepDTO receptionToUpdateDTO = FactureBAFactory.factureBAToBonRecepDTOForRevertPrices(oldCodeFrs, listeToUpdateOldFournisseurNotReceivedAfter);
            paramAchatServiceClient.ArticleFrsPriceRevert(receptionToUpdateDTO);
        }

        Set<CommandeAchatLazyDTO> listeCommandeAchatsDto = demandeServiceClient.findListCommandeAchatLazy(commandeAchatIDs, LocaleContextHolder.getLocale().getLanguage());
        List<String> commandeAchat = listeCommandeAchatsDto.stream().map(c -> c.getNumbon()).collect(Collectors.toList());
        Set<String> usersValidate = demandeServiceClient.updateCommandesAchatByCodesIn(commandeAchatIDs, codeFournisseur);
        usersValidate.add(receptionInBase.getCodvend());

//        EmailDTO email = notifyAfterUpdateSupplier(new ArrayList<>(usersValidate), codeFournisseur, commandeAchat, numBon);
//        Preconditions.checkBusinessLogique(email != null, "pas-d-emails");
//        FactureBA result = factureBARepository.save(receptionInBase);
        return FactureBAFactory.factureBAToBonRecepDTO(receptionInBase);
    }

    public EmailDTO notifyAfterUpdateSupplier(BonRecepDTO bonReceptDto) {
        FactureBA receptionInBase = factureBARepository.findOne(bonReceptDto.getNumbon());
        List<Integer> commandeAchatIDs = receptionInBase.getRecivedDetailCA().stream().map(d -> d.getPk().getCommandeAchat()).collect(Collectors.toList());
        Set<CommandeAchatDTO> commandesDtos = demandeServiceClient.findListCommandeAchat(commandeAchatIDs, LocaleContextHolder.getLocale().getLanguage());
        List<String> numbonCommandeAchat = commandesDtos.stream().map(c -> c.getNumbon()).collect(Collectors.toList());
        Set<String> usersValidate = commandesDtos.stream().map(x -> x.getUserValidate()).collect(Collectors.toSet());
        log.debug("list of usres to Notify est {}", usersValidate);
        usersValidate.add(receptionInBase.getCodvend());
        log.debug("list of usres to Notify est {}", usersValidate);
        Object[] obj = new Object[2];
        obj[0] = receptionInBase.getCodfrs();
        Object[] obj1 = new Object[2];
        obj1[0] = receptionInBase.getCodfrs();;
        obj1[1] = receptionInBase.getNumbon();

        String subject = messages.getMessage("update-Supplier-subject", null, LocaleContextHolder.getLocale());
        String part1;
        //messages.getMessage("update-supplier", obj, LocaleContextHolder.getLocale());
        String body = "";
        body += messages.getMessage("update-supplier-In-Reception", obj1, LocaleContextHolder.getLocale());
        body += "\n";
        for (String numbonCA : numbonCommandeAchat) {
            obj[1] = numbonCA;
            log.debug("list of commande {}", numbonCA);
            body += messages.getMessage("update-supplier-In-Bon-Commande", obj, LocaleContextHolder.getLocale());
            body += "\n";
        }

        log.debug("list of reception {}", obj1[1]);

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setSubject(subject);
        emailDTO.setBody(body);

        return cliniSysService.prepareEmail(new ArrayList<>(usersValidate), emailDTO);
    }

    public boolean getTypeReception() {
        return receptionSurDeuxEtapes.equalsIgnoreCase("true");
    }

    public boolean checkExistanceRefFournisseur(String codefrs, String reference) {
        List<FactureBA> f = factureBARepository.findByRefFrsAndCodfrs(reference, codefrs);
        return f.isEmpty();
    }

    @Transactional
    public List<MvtStoBA> processReceptionOnReturn(FactureBA bonRetour) {
        List<MvtStoBA> detailRecep = factureBARepository.findOne(bonRetour.getNumpiece()).getDetailFactureBACollection();

        detailRecep.stream().forEach(detailRecp -> {
            log.debug("lotInter du detail recep {} ,code article{} et le priuni {} ", detailRecp.getLotInter(), detailRecp.getCodart(), detailRecp.getPriuni());

            MvtStoBA matchingDetailRetour = bonRetour.getDetailFactureBACollection().stream()
                    .filter(item -> item.getLotInter().equals(detailRecp.getLotInter()) && item.getPk().getCodart().equals(detailRecp.getPk().getCodart()) && item.getDatPer().equals(detailRecp.getDatPer()) && detailRecp.getPriuni().setScale(7).equals(item.getPriuni().setScale(7))) //TODO add matching by date per
                    .findFirst()
                    .orElse(null);
            log.debug("matchingDetailRetour {}", matchingDetailRetour);
            if (matchingDetailRetour != null) {
                Preconditions.checkBusinessLogique(detailRecp.getQtecom().compareTo(matchingDetailRetour.getQuantite()) >= 0, "erreur-quantite-restante-reception");

                log.debug(" lotInter duRT trouvé {}, code article duRT trouvé{} , priuni RT trouvé{}   ", matchingDetailRetour.getLotInter(), matchingDetailRetour.getPk().getCodart(), matchingDetailRetour.getPriuni());
                detailRecp.setQtecom(detailRecp.getQtecom().subtract(matchingDetailRetour.getQuantite()));
            }

        });
        return detailRecep;
    }

    //************************************************editions***********/
    /**
     * Edition receiving by id.
     *
     * @param id the id of the entity
     * @return
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Transactional(readOnly = true)
    public byte[] editionReceptionRetour(String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print Fournisseurs : {}");
//        String language = LocaleContextHolder.getLocale().getLanguage();
        FactureBADTO bonReceptionDTO = findOneReception(id);
        if (bonReceptionDTO.getDateFrs() != null) {
            bonReceptionDTO.setDateFournisseurEdition(Date.from(bonReceptionDTO.getDateFrs().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (bonReceptionDTO.getDatbon() != null) {
            bonReceptionDTO.setDatebonEdition(Date.from(bonReceptionDTO.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        }
        bonReceptionDTO.setDesignationSite("");
        if (bonReceptionDTO.getCodeSite() != null) {
            SiteDTO codeSiteBranch = paramServiceClient.findSiteByCode(bonReceptionDTO.getCodeSite());
            Preconditions.checkBusinessLogique(codeSiteBranch != null, "parametrage.clinique.error");
            bonReceptionDTO.setDesignationSite(codeSiteBranch.getDesignation());
        }
        List<CliniqueDto> cliniqueDto = paramServiceClient.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        log.debug("**********bonReceptionDTO.getTypbon() {}*******", bonReceptionDTO.getTypbon());
        if (RT.type().equalsIgnoreCase(bonReceptionDTO.getTypbon())) {
            log.debug("**********edition retour*******");
            reportClientDoc.open("Reports/Bon_Retour" + local + ".rpt", 0);
        } else {
            switch (bonReceptionDTO.getCategDepot()) {
                case PH:
                    reportClientDoc.open("Reports/Bon_Reception_PH" + local + ".rpt", 0);
                    break;
                case IMMO:
                    reportClientDoc.open("Reports/Bon_Reception_IMMO" + local + ".rpt", 0);
                    break;
                default:
                    reportClientDoc.open("Reports/Bon_Reception" + local + ".rpt", 0);
                    break;
            }
        }

        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(bonReceptionDTO), FactureBADTO.class, "Entete", "Entete");
        reportClientDoc.getDatabaseController().setDataSource(bonReceptionDTO.getDetails(), MvtstoBADTO.class, "Detaille", "Detaille");
        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(bonReceptionDTO.getFournisseur()), FournisseurDTO.class, "Fournisseur", "Fournisseur");
        reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class, "clinique", "clinique");
        reportClientDoc.getSubreportController().getSubreport("BaseTVA").getDatabaseController().setDataSource(bonReceptionDTO.getBasesTVA(), BaseTvaReceptionDTO.class, "Commande", "Commande");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        if ("ar".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.AR(bonReceptionDTO.getMntBon().toString(), "جنيه", "قرش"));
        } else if ("fr".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.FR(bonReceptionDTO.getMntBon().toString(), "livres", "pence"));
        } else {
            paramController.setCurrentValue("", "montantLettre", Convert.EN(bonReceptionDTO.getMntBon().toString(), "pounds", "penny"));
        }
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }

    @Transactional(readOnly = true)
    public List<BonEditionDTO> editionEtatAchat(String codfrs, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ, List<Integer> codarts) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        QFactureBA _FactureBA = QFactureBA.factureBA;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(codfrs, () -> _FactureBA.codfrs.eq(codfrs))
                .and(_FactureBA.categDepot.eq(categ))
                .and(_FactureBA.codAnnul.isNull())
                .and(_FactureBA.datbon.goe(fromDate))
                .and(_FactureBA.datbon.loe(toDate));
//                .optionalAnd(codarts, ()->_FactureBA.detailFactureBACollection.any().pk().codart.in(codarts));
        List<FactureBA> factureBAs = (List<FactureBA>) factureBARepository.findAll(builder);
        List<BonEditionDTO> bonEditionDTOs = new ArrayList();
//        if (!factureBAs.isEmpty()) {
//            bonEditionDTOs = factureBAFactory.factureBAsToBonEditionDTOEagers(factureBAs);
//        }
//        List<BonEditionDTO> BonEditionDTOs = new ArrayList<>();
        Set<String> codeFournisseurs = factureBAs.stream().map(FactureBA::getCodfrs).collect(Collectors.toSet());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(new ArrayList(codeFournisseurs));
        Set<Integer> codeUnites = new HashSet();
        factureBAs.forEach(y -> {
            y.getDetailFactureBACollection().forEach(mvt -> {
                codeUnites.add(mvt.getCodeUnite());
            });
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        factureBAs.forEach(entity -> {
            BonEditionDTO dto = new BonEditionDTO();
            dto.setNumaffiche(entity.getNumaffiche());
            if (entity.getFactureBonReception() != null) {
                dto.setNumafficheFactureBonReception(entity.getFactureBonReception().getNumaffiche());
                dto.setNumbonFactureBonReception(entity.getFactureBonReception().getNumbon());
            } else {
                dto.setNumafficheFactureBonReception("");
                dto.setNumbonFactureBonReception("");
            }
            dto.setNumbon(entity.getNumbon());
            dto.setDatbon(entity.getDatbon());
            dto.setCodvend(entity.getCodvend());
            dto.setMntBon(entity.getMntbon());
            if (entity.getDatRefFrs() != null) {
                dto.setDateFrs(Date.from(entity.getDatRefFrs().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            dto.setDatebonEdition(Date.from(entity.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
            dto.setRefFrs(entity.getRefFrs());
            FournisseurDTO fournisseur = fournisseurs.stream().filter(item -> item.getCode().equals(entity.getCodfrs())) //TODO add matching by date per                  
                    .findFirst()
                    .orElse(null);
            Preconditions.checkBusinessLogique(fournisseur != null, "missing.fournisseur", entity.getCodfrs());
            dto.setFournisseur(fournisseur);
            dto.setCodeFournisseu(entity.getCodfrs());
            dto.setTypbon(entity.getTypbon().toString());
            List<DetailEditionDTO> detailsReception = new ArrayList(); //to regenerateeee only for test
            entity.getDetailFactureBACollection().forEach(mvtstoBA -> {
                DetailEditionDTO detailRecep = DetailReceptionFactory.toEditionDTO(mvtstoBA);
                detailRecep.setQteReturned(mvtstoBA.getQuantite().subtract(mvtstoBA.getQtecom()));
                UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoBA.getCodeUnite())).findFirst().orElse(null);
                com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
                detailRecep.setDesignationunite(unite.getDesignation());
                detailsReception.add(detailRecep);
            });
            dto.setDetails((List) detailsReception);
            bonEditionDTOs.add(dto);
        });

        return bonEditionDTOs;
    }

    @Transactional(readOnly = true)
    public byte[] editionListeBonReception(LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ, String order, String type, boolean avoir, List<Boolean> annule) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print Fournisseurs : {}");
        List<CliniqueDto> cliniqueDto = paramServiceClient.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/EtatBonReception" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/EtatBonReception_excel" + local + ".rpt", 0);
        }
        log.debug("categ {}", categ);
        QFactureBA _FactureBA = QFactureBA.factureBA;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(_FactureBA.categDepot.eq(categ));
        if (annule.contains(Boolean.TRUE) && annule.size() == 1) {
            builder.and(_FactureBA.codAnnul.isNotNull());
        }
        if (annule.contains(Boolean.FALSE) && annule.size() == 1) {
            builder.and(_FactureBA.codAnnul.isNull());
        }
        if ("datbon".equalsIgnoreCase(order)) {
            builder.and(_FactureBA.datbon.goe(fromDate));
            builder.and(_FactureBA.datbon.loe(toDate));
        } else {
            builder.and(_FactureBA.datRefFrs.goe(fromDate.toLocalDate()));
            builder.and(_FactureBA.datRefFrs.loe(toDate.toLocalDate()));
        }
        if (avoir) {
            builder.and(_FactureBA.typbon.eq(TypeBonEnum.RT).or(_FactureBA.typbon.eq(TypeBonEnum.BA)));
        } else {
            builder.and(_FactureBA.typbon.eq(TypeBonEnum.BA));
        }
        List<ReceptionEditionDTO> resultDTO = new ArrayList<>();
        if (avoir) {
            QAvoirFournisseur _avoirFournisseur = QAvoirFournisseur.avoirFournisseur;
            BooleanBuilder builderAvoir = new BooleanBuilder();
            builderAvoir.and(_avoirFournisseur.categDepot.eq(categ));
            if (annule.contains(Boolean.TRUE) && annule.size() == 1) {
                builderAvoir.and(_avoirFournisseur.userAnnule.isNotNull());
            }
            if (annule.contains(Boolean.FALSE) && annule.size() == 1) {
                builderAvoir.and(_avoirFournisseur.userAnnule.isNull());
            }
            if ("datbon".equalsIgnoreCase(order)) {
                builderAvoir.and(_avoirFournisseur.datbon.goe(fromDate));
                builderAvoir.and(_avoirFournisseur.datbon.loe(toDate));
            } else {
                builderAvoir.and(_avoirFournisseur.dateFournisseur.goe(fromDate.toLocalDate()));
                builderAvoir.and(_avoirFournisseur.dateFournisseur.loe(toDate.toLocalDate()));
            }
            List<AvoirFournisseur> avoirFournisseurs = (List<AvoirFournisseur>) avoirfournisseurRepository.findAll(builderAvoir);
            avoirFournisseurs.forEach((avoirFournisseur) -> {
                ReceptionEditionDTO bonRecepDTO = avoirFournisseurFactory.avoirFournisseurToReceptionEditionDTO(avoirFournisseur);
                resultDTO.add(bonRecepDTO);
            });
        }
        if (avoir) {
            QRetourPerime _retourPerime = QRetourPerime.retourPerime;
            BooleanBuilder builderPerime = new BooleanBuilder();
            builderPerime.and(_retourPerime.categDepot.eq(categ));
            if (annule.contains(Boolean.TRUE) && annule.size() == 1) {
                builderPerime.and(_retourPerime.codAnnul.isNotNull());
            }
            if (annule.contains(Boolean.FALSE) && annule.size() == 1) {
                builderPerime.and(_retourPerime.codAnnul.isNull());
            }
            if ("datbon".equalsIgnoreCase(order)) {
                builderPerime.and(_retourPerime.datbon.goe(fromDate));
                builderPerime.and(_retourPerime.datbon.loe(toDate));
            } else {
                builderPerime.and(_retourPerime.datRefFrs.goe(fromDate.toLocalDate()));
                builderPerime.and(_retourPerime.datRefFrs.loe(toDate.toLocalDate()));
            }
            List<RetourPerime> retourPerimes = (List<RetourPerime>) retourPerimeRepository.findAll(builderPerime);
            retourPerimes.forEach((retourPerime) -> {
                ReceptionEditionDTO bonRecepDTO = RetourPerimeFactory.retourPerimeToReceptionEditionDTO(retourPerime);
                resultDTO.add(bonRecepDTO);
            });
        }
        List<FactureBA> result = (List<FactureBA>) factureBARepository.findAll(builder);
        result.forEach((factureBA) -> {
            ReceptionEditionDTO bonRecepDTO = FactureBAFactory.factureBAToReceptionEditionDTO(factureBA);
            resultDTO.add(bonRecepDTO);
        });
        List<String> codesFrs = resultDTO.stream().map(item -> item.getCodeFournisseu()).distinct().collect(Collectors.toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
        reportClientDoc.getDatabaseController().setDataSource(resultDTO, ReceptionEditionDTO.class, "BonReception", "BonReception");
        reportClientDoc.getDatabaseController().setDataSource(fournisseurs, FournisseurDTO.class, "fournisseur", "fournisseur");
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class, "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "debut", Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "fin", Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant()));
        paramController.setCurrentValue("", "order", order);
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

    public byte[] editionListeBonReceptionNonFactures(CategorieDepotEnum categ, LocalDateTime date, String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        List<ReceptionEditionDTO> listeFactureBANonFactures = findListReceptionsNonFactures(categ, date);
        log.debug("REST request to print liste des bons receptions non facturés a la date : {}", date);

        List<CliniqueDto> cliniqueDto = paramServiceClient.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/EtatBonReceptionNonFactures" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/EtatBonReceptionNonFactures_excel" + local + ".rpt", 0);
        }
        List<String> codesFrs = listeFactureBANonFactures.stream().map(item -> item.getCodeFournisseu()).distinct().collect(Collectors.toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
        reportClientDoc
                .getDatabaseController().setDataSource(listeFactureBANonFactures, ReceptionEditionDTO.class,
                        "BonReception", "BonReception");
        reportClientDoc
                .getDatabaseController().setDataSource(fournisseurs, FournisseurDTO.class,
                        "fournisseur", "fournisseur");

        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "date", Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));

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

    public List<ReceptionEditionDTO> findListReceptionsNonFactures(CategorieDepotEnum categ, LocalDateTime date) {
        //reception    
        QFactureBA _FactureBA = QFactureBA.factureBA;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(_FactureBA.typbon.eq(TypeBonEnum.BA))
                .and(_FactureBA.codAnnul.isNull())
                .and(_FactureBA.datbon.loe(date))
                .optionalAnd(categ, () -> _FactureBA.categDepot.eq(categ))
                .and(_FactureBA.factureBonReception().isNull());

        WhereClauseBuilder builder2 = new WhereClauseBuilder()
                .and(_FactureBA.typbon.eq(TypeBonEnum.BA))
                .and(_FactureBA.codAnnul.isNull())
                .and(_FactureBA.datbon.loe(date))
                .optionalAnd(categ, () -> _FactureBA.categDepot.eq(categ))
                .and((_FactureBA.factureBonReception().datbon.goe(date)));

        //retourPerimes              
        QRetourPerime _RetourPerime = QRetourPerime.retourPerime;
        WhereClauseBuilder builderRP = new WhereClauseBuilder()
                .and(_RetourPerime.typbon.eq(TypeBonEnum.RP))
                .and(_RetourPerime.codAnnul.isNull())
                .and(_RetourPerime.datbon.loe(date))
                .optionalAnd(categ, () -> _RetourPerime.categDepot.eq(categ))
                .and(_RetourPerime.factureRetourPerime().isNull());

        WhereClauseBuilder builderRP2 = new WhereClauseBuilder()
                .and(_RetourPerime.typbon.eq(TypeBonEnum.RP))
                .and(_RetourPerime.codAnnul.isNull())
                .and(_RetourPerime.datbon.loe(date))
                .optionalAnd(categ, () -> _RetourPerime.categDepot.eq(categ))
                .and(_RetourPerime.factureRetourPerime().datbon.goe(date));

        List<ReceptionEditionDTO> resultDTO = new ArrayList();
        List<FactureBA> bonReceptions = (List<FactureBA>) factureBARepository.findAll(builder);
        bonReceptions.addAll((List<FactureBA>) factureBARepository.findAll(builder2));
        List<String> listeNumBonReceptions = bonReceptions.stream().map(FactureBA::getNumbon).collect(Collectors.toList());
        List<FactureBA> bonRetour = factureBARepository.findByNumpieceInAndTypbonAndDatbonLessThanEqual(listeNumBonReceptions, RT, date);
        bonReceptions.addAll(bonRetour);
        bonReceptions.forEach((reception) -> {
            ReceptionEditionDTO bonRecepDTO = FactureBAFactory.factureBAToReceptionEditionDTO(reception);
            resultDTO.add(bonRecepDTO);
        });

        List<RetourPerime> retourPerimes = (List<RetourPerime>) retourPerimeRepository.findAll(builderRP);
        retourPerimes.addAll((List<RetourPerime>) retourPerimeRepository.findAll(builderRP2));

        retourPerimes.forEach((retourPerime) -> {
            ReceptionEditionDTO bonRecepDTO = RetourPerimeFactory.retourPerimeToReceptionEditionDTO(retourPerime);
            resultDTO.add(bonRecepDTO);
        });

        return resultDTO;

    }

    @Transactional(readOnly = true)
    public List<ReceptionEditionDTO> findListeBonReceptionAvecTVa(LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categDepot, Boolean orderDateBon, Boolean avoir, List<Boolean> annule) {

        QFactureBA _FactureBA = QFactureBA.factureBA;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .and(_FactureBA.categDepot.eq(categDepot))
                .booleanAnd(Objects.equals(orderDateBon, Boolean.TRUE), () -> _FactureBA.datbon.goe(fromDate))
                .booleanAnd(Objects.equals(orderDateBon, Boolean.TRUE), () -> _FactureBA.datbon.loe(toDate))
                .booleanAnd(Objects.equals(orderDateBon, Boolean.FALSE), () -> _FactureBA.datRefFrs.goe(fromDate.toLocalDate()))
                .booleanAnd(Objects.equals(orderDateBon, Boolean.FALSE), () -> _FactureBA.datRefFrs.loe(toDate.toLocalDate()))
                .booleanAnd(Objects.equals(avoir, Boolean.FALSE), () -> _FactureBA.typbon.eq(TypeBonEnum.BA))
                .booleanAnd(Objects.equals(avoir, Boolean.TRUE), () -> _FactureBA.typbon.eq(TypeBonEnum.RT).or(_FactureBA.typbon.eq(TypeBonEnum.BA)));
        if (annule.contains(Boolean.TRUE) && annule.size() == 1) {
            builder.and(_FactureBA.datAnnul.isNotNull());
        }
        if (annule.contains(Boolean.FALSE) && annule.size() == 1) {
            builder.and(_FactureBA.datAnnul.isNull());
        }

        List<ReceptionEditionDTO> resultDTO = new ArrayList();
        List<FactureBA> bonReceptions = (List<FactureBA>) factureBARepository.findAll(builder);
        log.debug("bonReceptions sont {}", bonReceptions);
        bonReceptions.forEach((reception) -> {
            ReceptionEditionDTO bonRecepDTO = FactureBAFactory.factureBAToReceptionEditionDTO(reception);
            List<BaseTvaReception> listeBaseTvaReception = reception.getBaseTvaReceptionList();
            BigDecimal baseAvecTvaZero = listeBaseTvaReception
                    .stream()
                    .filter(elt -> elt.getTauxTva().compareTo(BigDecimal.ZERO) == 0)
                    .map(elt -> elt.getBaseTva())
                    .collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));
            BigDecimal baseAvecTvaDifferenteZero = listeBaseTvaReception
                    .stream()
                    .peek(x -> log.debug("x.getTauxTva().compareTo(BigDecimal.ZERO) est {}", x.getTauxTva().compareTo(BigDecimal.ZERO) > 0))
                    .filter(elt -> elt.getTauxTva().compareTo(BigDecimal.ZERO) > 0)
                    .map(elt -> elt.getBaseTva())
                    .collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));

            BigDecimal montantTva = BigDecimal.ZERO;
            for (BaseTvaReception baseTvaReception : listeBaseTvaReception) {
                log.debug("getMontantTvaGratuite est {} et getMontantTva est {}", baseTvaReception.getMontantTvaGratuite(), baseTvaReception.getMontantTva());
                montantTva = montantTva.add(baseTvaReception.getMontantTvaGratuite());
                montantTva = montantTva.add(baseTvaReception.getMontantTva());
                log.debug("montantTva est {}", montantTva);
            }
            bonRecepDTO.setBaseAvecTvaZero(baseAvecTvaZero);
            bonRecepDTO.setBaseAvecTvaDifferenteZERO(baseAvecTvaDifferenteZero);
            bonRecepDTO.setMontantTva(montantTva);
            resultDTO.add(bonRecepDTO);
        });

        if (avoir) {
            QAvoirFournisseur _avoirFournisseur = QAvoirFournisseur.avoirFournisseur;
            WhereClauseBuilder builderAvoirFournisseur = new WhereClauseBuilder()
                    .and(_avoirFournisseur.categDepot.eq(categDepot))
                    .booleanAnd(Objects.equals(orderDateBon, Boolean.TRUE), () -> _avoirFournisseur.datbon.goe(fromDate))
                    .booleanAnd(Objects.equals(orderDateBon, Boolean.TRUE), () -> _avoirFournisseur.datbon.loe(toDate))
                    .booleanAnd(Objects.equals(orderDateBon, Boolean.FALSE), () -> _avoirFournisseur.dateFournisseur.goe(fromDate.toLocalDate()))
                    .booleanAnd(Objects.equals(orderDateBon, Boolean.FALSE), () -> _avoirFournisseur.dateFournisseur.loe(toDate.toLocalDate()))
                    .booleanAnd(Objects.equals(annule, Boolean.TRUE), () -> _avoirFournisseur.userAnnule.isNotNull())
                    .booleanAnd(Objects.equals(annule, Boolean.FALSE), () -> _avoirFournisseur.userAnnule.isNull());

            List<AvoirFournisseur> avoirFournisseurs = (List<AvoirFournisseur>) avoirfournisseurRepository.findAll(builderAvoirFournisseur);
            avoirFournisseurs.forEach((avoirFournisseur) -> {
                ReceptionEditionDTO bonRecepDTO = avoirFournisseurFactory.avoirFournisseurToReceptionEditionDTO(avoirFournisseur);

                List<BaseTvaAvoirFournisseur> listeBaseTva = avoirFournisseur.getBaseTvaAvoirFournisseurList();
                BigDecimal baseAvecTvaZero = listeBaseTva
                        .stream()
                        .filter(elt -> elt.getTauxTva().compareTo(BigDecimal.ZERO) == 0)
                        .map(elt -> elt.getBaseTva())
                        .collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));
                BigDecimal baseAvecTvaDifferenteZero = listeBaseTva
                        .stream()
                        .filter(elt -> elt.getTauxTva().compareTo(BigDecimal.ZERO) > 0)
                        .map(elt -> elt.getBaseTva())
                        .collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));

                BigDecimal montantTva = BigDecimal.ZERO;
                listeBaseTva.forEach((baseTva) -> {
                    montantTva.add(baseTva.getMntTvaGrtauite());
                    montantTva.add(baseTva.getMontantTva());
                });
                bonRecepDTO.setBaseAvecTvaZero(baseAvecTvaZero);
                bonRecepDTO.setBaseAvecTvaDifferenteZERO(baseAvecTvaDifferenteZero);
                bonRecepDTO.setMontantTva(montantTva);

                resultDTO.add(bonRecepDTO);
            });
            //retour perime  
            QRetourPerime _retourPerime = QRetourPerime.retourPerime;
            WhereClauseBuilder builderRetourPerime = new WhereClauseBuilder()
                    .and(_retourPerime.categDepot.eq(categDepot))
                    .booleanAnd(Objects.equals(orderDateBon, Boolean.TRUE), () -> _retourPerime.datbon.goe(fromDate))
                    .booleanAnd(Objects.equals(orderDateBon, Boolean.TRUE), () -> _retourPerime.datbon.loe(toDate))
                    .booleanAnd(Objects.equals(orderDateBon, Boolean.FALSE), () -> _retourPerime.datRefFrs.goe(fromDate.toLocalDate()))
                    .booleanAnd(Objects.equals(orderDateBon, Boolean.FALSE), () -> _retourPerime.datRefFrs.loe(toDate.toLocalDate()))
                    .booleanAnd(Objects.equals(annule, Boolean.TRUE), () -> _retourPerime.datAnnul.isNotNull())
                    .booleanAnd(Objects.equals(annule, Boolean.FALSE), () -> _retourPerime.datAnnul.isNull());

            List<RetourPerime> listeRetourPerimes = (List<RetourPerime>) retourPerimeRepository.findAll(builderRetourPerime);
            listeRetourPerimes.forEach((retourPerime) -> {
                ReceptionEditionDTO bonRetourPerimeDTO = RetourPerimeFactory.retourPerimeToReceptionEditionDTO(retourPerime);

                List<BaseTvaRetourPerime> listeBaseTva = retourPerime.getBaseTvaRetourPerime();
                BigDecimal baseAvecTvaZero = listeBaseTva
                        .stream()
                        .filter(elt -> elt.getTauxTva().compareTo(BigDecimal.ZERO) == 0)
                        .map(elt -> elt.getBaseTva())
                        .collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));
                BigDecimal baseAvecTvaDifferenteZero = listeBaseTva
                        .stream()
                        .filter(elt -> elt.getTauxTva().compareTo(BigDecimal.ZERO) > 0)
                        .map(elt -> elt.getBaseTva())
                        .collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));

                BigDecimal montantTva = BigDecimal.ZERO;
                listeBaseTva.forEach((baseTva) -> {
                    montantTva.add(baseTva.getMontantTva());
                });
                bonRetourPerimeDTO.setBaseAvecTvaZero(baseAvecTvaZero);
                bonRetourPerimeDTO.setBaseAvecTvaDifferenteZERO(baseAvecTvaDifferenteZero);
                bonRetourPerimeDTO.setMontantTva(montantTva);

                resultDTO.add(bonRetourPerimeDTO);
            });
        }
        return resultDTO;
    }

}

//    public MouvementDuJour findMouvementDuJour(String typbon, boolean stup) throws ParseException {
//        Date date = new Date();
//        Date datesys = formatter.parse(formatter.format(date));
//        return factureBARepository.findMouvementDuJour(datesys, typbon, stup);
//    }
//    @Transactional
//    public String testPesLock() {
//        String numBon = paramService.getcompteur(CategorieDepotEnum.UU, TypeBonEnum.BA);
//        log.debug("numBon {}", numBon);
//        paramService.updateCompteurPharmacie(CategorieDepotEnum.UU, TypeBonEnum.BA);
//        return numBon;
//    }
//
//    @Transactional
//    public String testPesLock2() {
//        Depsto d = stockService.findByCodartInAndCoddepAndQteGreaterThan(Arrays.asList(57319), 99, BigDecimal.ZERO).get(0);
//        d.setQte(d.getQte().subtract(BigDecimal.TEN));
//        return d.getQte().toString();
//    }
/**
 * Edition receiving by id.
 *
 * @param id the id of the entity
 * @return
 * @throws java.net.URISyntaxException
 * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
 * @throws java.io.IOException
 * @throws java.sql.SQLException
 */
//    @Deprecated
//    @Transactional(readOnly = true)
//    public byte[] editionRetour(String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
//        log.debug("REST request to print Fournisseurs : {}");
////        String language = LocaleContextHolder.getLocale().getLanguage();
//        List<CliniqueDto> cliniqueDto = paramServiceClient.findClinique();
//        cliniqueDto.get(0).setLogoClinique();
//        ReportClientDocument reportClientDoc = new ReportClientDocument();
//        Locale loc = LocaleContextHolder.getLocale();
//        String local = "_" + loc.getLanguage();
//        reportClientDoc.open("Reports/Bon_Retour" + local + ".rpt", 0);
////        FactureBA retour = factureBARepository.findOne(id);
////        Preconditions.checkBusinessLogique(retour != null, "missing.reception");
//
//        FactureBADTO bonRetourDTO = findOneReception(id);
//
//        if (bonRetourDTO.getDateFrs() != null) {
//            bonRetourDTO.setDateFournisseurEdition(Date.from(bonRetourDTO.getDateFrs().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        }
//        if (bonRetourDTO.getDatbon() != null) {
//            bonRetourDTO.setDatebonEdition(Date.from(bonRetourDTO.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
//        }
//
//        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(bonRetourDTO), FactureBADTO.class, "Entete", "Entete");
//        reportClientDoc.getDatabaseController().setDataSource(bonRetourDTO.getDetails(), DetailReceptionDTO.class, "Detaille", "Detaille");
//        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(bonRetourDTO.getFournisseur()), FournisseurDTO.class, "Fournisseur", "Fournisseur");
//        reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class, "clinique", "clinique");
//        reportClientDoc.getSubreportController().getSubreport("BaseTVA").getDatabaseController().setDataSource(bonRetourDTO.getBasesTVA(), BaseTvaReceptionDTO.class, "Commande", "Commande");
//        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
//        String user = SecurityContextHolder.getContext().getAuthentication().getName();
//        paramController.setCurrentValue("", "user", user);
//        if ("ar".equals(loc.getLanguage())) {
//            paramController.setCurrentValue("", "montantLettre", Convert.AR(bonRetourDTO.getMntBon().toString(), "جنيه", "قرش"));
//        } else if ("fr".equals(loc.getLanguage())) {
//            paramController.setCurrentValue("", "montantLettre", Convert.FR(bonRetourDTO.getMntBon().toString(), "livres", "pence"));
//        } else {
//            paramController.setCurrentValue("", "montantLettre", Convert.EN(bonRetourDTO.getMntBon().toString(), "pounds", "penny"));
//        }
//        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
//        reportClientDoc.close();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        return Helper.read(byteArrayInputStream);
//    }
//    /**
//     * Getting the details of purchase orders that will be used to create the
//     * reception. ca
//     *
//     * @param purchaseOrdersCodes
//     * @return details of purchase orders sorted by creation date desc
//     */
//    @Transactional
//    public List<DetailCommandeAchatDTO> getDetailOfPurchaseOrders(List<Integer> purchaseOrdersCodes) {
//        /**
//         * getting the liste of purchase orders
//         */
//        Set<CommandeAchatDTO> listeCA = demandeServiceClient.findListCommandeAchat(purchaseOrdersCodes, LocaleContextHolder.getLocale().getLanguage());
//        log.debug("liste  CA est {} ", listeCA);
//
//        Preconditions.checkBusinessLogique(listeCA.size() == purchaseOrdersCodes.size(), "reception.add.missing-ca");
//        /*
//         * getting the list of previous receptions of the given purchase orders
//         */
//        List<ReceptionDetailCA> recivedDetailCAs = receptionDetailCAService.findByCodesCAIn(purchaseOrdersCodes);
//        List<ReceptionTemporaireDetailCa> recivedTempDetailCAs = receptionTemporaireDetailCaService.findByCodesCAInAndNotValidated(purchaseOrdersCodes);// find Recepetion temp detail ca by code in and non receptionnes
//
//        /*
//         * setting the qteRestante of each detail of each purchase order
//         */
//        List<DetailCommandeAchatDTO> listDetailCA = listeCA.stream()
//                .sorted((a, b) -> a.getNumbon().compareTo(b.getNumbon()))// sorting the liste of purchase orders :order ancienete
//                .flatMap(e -> e.getDetailCommandeAchatCollection().stream())
//                .map(detailCA -> {
//                    BigDecimal quantiteReceptionnee = BigDecimal.ZERO;
//                    BigDecimal quantiteGratuiteReceptionnee = BigDecimal.ZERO;
//                    log.debug("recivedDetailCAs sont {}", recivedDetailCAs);
////                    log.debug("recivedTempDetailCAs sont {}", recivedTempDetailCAs);
//
//                    List<ReceptionDetailCA> MatchingListReceivedDetailCa = recivedDetailCAs.stream()
//                            .filter(item -> item.getPk().getCommandeAchat().equals(detailCA.getCode()) && item.getPk().getArticle().equals(detailCA.getCodart()))
//                            .collect(Collectors.toList());
//
//                    log.debug("MatchingListReceivedDetailCa sont {}", MatchingListReceivedDetailCa);
//
//                    List<ReceptionTemporaireDetailCa> MatchingListReceivedTempDetailCa = recivedTempDetailCAs.stream()
//                            .filter(item -> item.getReceptionTemporaireDetailCaPK().getCommandeAchat().equals(detailCA.getCode()) && item.getReceptionTemporaireDetailCaPK().getArticle().equals(detailCA.getCodart()))
//                            .collect(Collectors.toList());
//
////                    log.debug("MatchingListReceivedTempDetailCa sont {}", MatchingListReceivedTempDetailCa);
//                    for (ReceptionTemporaireDetailCa receptionTemporaireDetailCa : MatchingListReceivedTempDetailCa) {
//
////                        log.debug("quantiteGratuiteReceptionnee  temp est {} et  la quantite gratuite receptionne precedemment  temp est {}", quantiteGratuiteReceptionnee, receptionTemporaireDetailCa.getQuantiteGratuite());
//                        quantiteReceptionnee = quantiteReceptionnee.add(receptionTemporaireDetailCa.getQuantiteReceptione());
//                        quantiteGratuiteReceptionnee = quantiteGratuiteReceptionnee.add(receptionTemporaireDetailCa.getQuantiteGratuite());
//                    };
//
//                    for (ReceptionDetailCA receptionDetailCA : MatchingListReceivedDetailCa) {
//                        log.debug("quantiteeReceptionnee est {} et  receptionDetailCA.getQuantiteReceptione {} et gratuite est {}", quantiteReceptionnee, receptionDetailCA.getQuantiteReceptione(), quantiteGratuiteReceptionnee);
////                        log.debug("quantiteGratuiteReReceptionDetailCAceptionnee est {} et  la quantite gratuite receptionne precedemment est {}", quantiteGratuiteReceptionnee, receptionDetailCA.getQuantiteGratuite());
//                        quantiteReceptionnee = quantiteReceptionnee.add(receptionDetailCA.getQuantiteReceptione());
//                        quantiteGratuiteReceptionnee = quantiteGratuiteReceptionnee.add(receptionDetailCA.getQuantiteGratuite());
//                    };
//
//                    BigDecimal qteRestante = detailCA.getQuantite().subtract(quantiteReceptionnee);
//                    BigDecimal qteGratuiteRestante = detailCA.getQuantiteGratuite().subtract(quantiteGratuiteReceptionnee);
//                    log.debug("****************qteRestante est {} et  la quantite gratuite restante  {}*****************", qteRestante, qteGratuiteRestante);
////                    checking if the purchase order hase been treated by another thread
//                    Preconditions.checkBusinessLogique(qteRestante.compareTo(BigDecimal.ZERO) > 0, "reception.add.recived-ca");
//                    detailCA.setQuantiteRestante(qteRestante);
//                    detailCA.setQuantiteGratuiteRestante(qteGratuiteRestante);
//                    return detailCA;
//                })
//                .collect(Collectors.toList());
//        log.debug("liste detail CA  fin est {} ", listDetailCA);
//        return listDetailCA;
//
//    }
//
//    /**
//     * This method choose which details of purchase orders that we used for
//     * creating the reception.By choosing we mean subtracking each recived
//     * quantite from the detail of purchase order that we choose
//     *
//     * @param bonReceptionDTO the created reception
//     * @param purchaseOrdersDetails
//     * @param numBonReceptionFinale
//     */
//    @Transactional
//    public List<ReceptionDetailCA> processPurchaseOrders(FactureBADTO bonReceptionDTO, List<DetailCommandeAchatDTO> purchaseOrdersDetails, String numBonReceptionFinale) {
//        log.debug("processing Purchase orders {}", purchaseOrdersDetails);
//
//        /**
//         * processing the purchase order
//         */
//        Map<Integer, Integer> articlesGroupeByCodart = bonReceptionDTO.getDetails()
//                .stream()
//                .filter(item -> item.getPriuni().compareTo(BigDecimal.ZERO) > 0)
//                .collect(Collectors.groupingBy(Item -> Item.getRefArt(), Collectors.summingInt(Item -> Item.getQuantite().intValue())));
//        List<ReceptionDetailCA> receptionDetailCAs = new ArrayList();
//
//        for (Map.Entry<Integer, Integer> mvtstoBA : articlesGroupeByCodart.entrySet()) {
//            BigDecimal resteMvtstoba = BigDecimal.valueOf(mvtstoBA.getValue());
//            BigDecimal quantiteRestanteCommandeTotale = purchaseOrdersDetails
//                    .stream()
//                    .filter(detailCA -> detailCA.getCodart().equals(mvtstoBA.getKey()) && detailCA.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0)
//                    .collect(Collectors.reducing(BigDecimal.ZERO,
//                            detailCA -> detailCA.getQuantiteRestante(), BigDecimal::add));
//            Preconditions.checkBusinessLogique(resteMvtstoba.compareTo(quantiteRestanteCommandeTotale)>=0, "reception.add.recived-ca");
//            BigDecimal min;
//            Boolean existsAtLeastOnce = false;
//            for (DetailCommandeAchatDTO detailCA : purchaseOrdersDetails) {
//                if (detailCA.getCodart().equals(mvtstoBA.getKey()) && detailCA.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0) {
//
//                    if (resteMvtstoba.compareTo(BigDecimal.ZERO) > 0) {
//                        min = detailCA.getQuantiteRestante().min(resteMvtstoba);
//                        detailCA.setQuantiteRestante(detailCA.getQuantiteRestante().subtract(min));
//                        resteMvtstoba = resteMvtstoba.subtract(min);
//                        receptionDetailCAs.add(new ReceptionDetailCA(numBonReceptionFinale, detailCA.getCode(), detailCA.getCodart(), min, BigDecimal.ZERO));
//                        existsAtLeastOnce = true;
//                    } else {
//                        break;
//                    }
//                }
//            }
//            log.debug("receptionDetailCAs from non gratuite est {} ", receptionDetailCAs);
////            Preconditions.checkBusinessLogique(existsAtLeastOnce, "reception.add.detail-ca-missing", mvtstoBA.getKey().toString());
//        }
//        //satisfactions des details receptions venant des commandes gratuites
//        List<MvtstoBADTO> freeDetailReceptions = bonReceptionDTO.getDetails()
//                .stream()
//                //                .filter(item -> (item.getPriuni().compareTo(BigDecimal.ZERO) == 0) && (item.getCodeCommande() != null))
//                .filter(item -> (item.getPriuni().compareTo(BigDecimal.ZERO) == 0))
//                .collect(Collectors.toList());
//
//        for (MvtstoBADTO freeDetailReception : freeDetailReceptions) {
//
//            List<DetailCommandeAchatDTO> matchingDetailCAs = purchaseOrdersDetails
//                    .stream()
//                    .filter(item -> item.getCodart().equals(freeDetailReception.getRefArt()))
//                    .collect(Collectors.toList());
//            Preconditions.checkBusinessLogique(!(matchingDetailCAs.isEmpty()), "reception.add.detail-ca-missing", freeDetailReception.getRefArt().toString());
////         
//            BigDecimal freeMvtstobaGratuit = freeDetailReception.getQuantite();
//            BigDecimal minQteGratuite;
//            for (DetailCommandeAchatDTO detailCA : matchingDetailCAs) {
//
//                if (freeMvtstobaGratuit.compareTo(BigDecimal.ZERO) > 0) {
//                    minQteGratuite = detailCA.getQuantiteGratuiteRestante().min(freeMvtstobaGratuit);
//                    detailCA.setQuantiteGratuiteRestante(detailCA.getQuantiteGratuiteRestante().subtract(minQteGratuite));
//                    freeMvtstobaGratuit = freeMvtstobaGratuit.subtract(minQteGratuite);
//                    Optional<ReceptionDetailCA> matchedReceptionCA = receptionDetailCAs
//                            .stream()
//                            .filter(elt -> elt.getPk().getArticle().equals(detailCA.getCodart()) && elt.getPk().getCommandeAchat().equals(detailCA.getCode()))
//                            .findFirst();
//                    if (!matchedReceptionCA.isPresent()) {
//
//                        receptionDetailCAs.add(new ReceptionDetailCA(bonReceptionDTO.getNumbon(), detailCA.getCode(), detailCA.getCodart(), BigDecimal.ZERO, minQteGratuite));
//                    } else {
//                        matchedReceptionCA.get().setQuantiteGratuite(minQteGratuite);
//                    }
//                } else {
//                    break;
//                }
//            }
//        }
//
////            bonReception.setRecivedDetailCA(receptionDetailCAs);
//        Map<Integer, Optional<DetailCommandeAchatDTO>> qteRestByCA = purchaseOrdersDetails.stream()
//                .collect(groupingBy(DetailCommandeAchatDTO::getCode, Collectors.reducing((a, b) -> {
//                    a.setQuantite(a.getQuantite().add(b.getQuantite()));
//                    a.setQuantiteRestante(a.getQuantiteRestante().add(b.getQuantiteRestante()));
//                    a.setQuantiteGratuite(a.getQuantiteGratuite().add(b.getQuantiteGratuite()));
//                    a.setQuantiteGratuiteRestante(a.getQuantiteGratuiteRestante().add(b.getQuantiteGratuiteRestante()));
//                    return a;
//                })));
//        List<EtatReceptionCA> etatsCA = new ArrayList();
//        qteRestByCA.forEach((key, value) -> {
//            DetailCommandeAchatDTO mergedDetailCA = value.get();
////                log.debug("mergedDetailCA.getQuantiteRestante()) est {} et mergedDetailCA.getQuantiteGratuiteRestante()est {} ", mergedDetailCA.getQuantiteRestante(), mergedDetailCA.getQuantiteGratuiteRestante());
////                log.debug(" mergedDetailCA.getQuantiteRestante().compareTo(BigDecimal.ZERO)==0  {}", mergedDetailCA.getQuantiteRestante().compareTo(BigDecimal.ZERO) == 0);
//            if (mergedDetailCA.getQuantiteRestante().compareTo(BigDecimal.ZERO) == 0 && mergedDetailCA.getQuantiteGratuiteRestante().compareTo(BigDecimal.ZERO) == 0) {
//                etatsCA.add(new EtatReceptionCA(key, RECEIVED));
//            } else if (mergedDetailCA.getQuantiteRestante().equals(mergedDetailCA.getQuantite()) && mergedDetailCA.getQuantiteGratuiteRestante().equals(mergedDetailCA.getQuantiteGratuite())) {
//                etatsCA.add(new EtatReceptionCA(key, NOT_RECEIVED));
//            } else {
//                etatsCA.add(new EtatReceptionCA(key, PARTIALLY_RECIVED));
//            }
//        }
//        );
//        etatReceptionCAService.save(etatsCA);
//
//        return receptionDetailCAs;
//    }
//
//    @Transactional
//    public List<ReceptionTemporaireDetailCa> processPurchaseOrdersTemporaires(ReceptionTemporaireDTO receptionTempDTO, List<DetailCommandeAchatDTO> purchaseOrdersDetails, TypeBonEnum typeBon) {
//        log.debug("processing Purchase orders {}", purchaseOrdersDetails);
//
//        /**
//         * processing the purchase order
//         */
//        Map<Integer, Integer> articlesGroupeByCodart = receptionTempDTO.getDetailReceptionTempraireDTO()
//                .stream()
//                .filter(item -> item.getPriuni().compareTo(BigDecimal.ZERO) > 0)
//                .collect(Collectors.groupingBy(Item -> Item.getRefArt(), Collectors.summingInt(Item -> Item.getQuantite().intValue())));
//        List<ReceptionTemporaireDetailCa> receptionDetailCAs = new ArrayList();
//
//        for (Map.Entry<Integer, Integer> mvtstoBA : articlesGroupeByCodart.entrySet()) {
//            BigDecimal reste = BigDecimal.valueOf(mvtstoBA.getValue());
//            BigDecimal min;
//            Boolean existsAtLeastOnce = false;
//            for (DetailCommandeAchatDTO detailCA : purchaseOrdersDetails) {
//                if (detailCA.getCodart().equals(mvtstoBA.getKey()) && detailCA.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0) {
//
//                    if (reste.compareTo(BigDecimal.ZERO) > 0) {
//                        min = detailCA.getQuantiteRestante().min(reste);
//                        detailCA.setQuantiteRestante(detailCA.getQuantiteRestante().subtract(min));
//                        reste = reste.subtract(min);
//                        receptionDetailCAs.add(new ReceptionTemporaireDetailCa(receptionTempDTO.getNumbon(), detailCA.getCode(), detailCA.getCodart(), min, BigDecimal.ZERO));
//                        existsAtLeastOnce = true;
//                    } else {
//                        break;
//                    }
//                }
//            }
//            log.debug("receptionDetailCAs from non gratuite est {} ", receptionDetailCAs);
////            Preconditions.checkBusinessLogique(existsAtLeastOnce, "reception.add.detail-ca-missing", mvtstoBA.getKey().toString());
//        }
//
//        //satisfactions des details receptions venant des commandes gratuites
//        List<DetailReceptionTemporaireDTO> freeDetailReceptionsTemp = receptionTempDTO.getDetailReceptionTempraireDTO()
//                .stream()
//                .filter(item -> (item.getPriuni().compareTo(BigDecimal.ZERO) == 0) && (item.getCode() != null))
//                .collect(Collectors.toList());
//
//        for (DetailReceptionTemporaireDTO freeDetailReceptionTemp : freeDetailReceptionsTemp) {
//            log.debug("freeDetailReception est {}", freeDetailReceptionTemp);
//            List<DetailCommandeAchatDTO> matchingDetailCAs = purchaseOrdersDetails
//                    .stream()
//                    .filter(item -> item.getCodart().equals(freeDetailReceptionTemp.getRefArt()))
//                    .collect(Collectors.toList());
//            Preconditions.checkBusinessLogique(!(matchingDetailCAs.isEmpty()), "reception.add.detail-ca-missing", freeDetailReceptionTemp.getDesart());
////         
//            BigDecimal reste = freeDetailReceptionTemp.getQuantite();
//            BigDecimal minQteGratuite;
//            for (DetailCommandeAchatDTO detailCA : matchingDetailCAs) {
//
//                if (reste.compareTo(BigDecimal.ZERO) > 0) {
//                    minQteGratuite = detailCA.getQuantiteGratuiteRestante().min(reste);
//                    detailCA.setQuantiteGratuiteRestante(detailCA.getQuantiteGratuiteRestante().subtract(minQteGratuite));
//                    reste = reste.subtract(minQteGratuite);
//                    Optional<ReceptionTemporaireDetailCa> matchedReceptionCA = receptionDetailCAs
//                            .stream()
//                            .filter(elt -> elt.getReceptionTemporaireDetailCaPK().getArticle().equals(detailCA.getCodart())
//                            && elt.getReceptionTemporaireDetailCaPK().getCommandeAchat().equals(detailCA.getCode()))
//                            .findFirst();
//                    if (!matchedReceptionCA.isPresent()) {
//
//                        receptionDetailCAs.add(new ReceptionTemporaireDetailCa(receptionTempDTO.getNumbon(), detailCA.getCode(), detailCA.getCodart(), BigDecimal.ZERO, minQteGratuite));
//                    } else {
//                        matchedReceptionCA.get().setQuantiteGratuite(minQteGratuite);
//                    }
//                } else {
//                    break;
//                }
//            }
//        }
//
////            bonReception.setRecivedDetailCA(receptionDetailCAs);
//        Map<Integer, Optional<DetailCommandeAchatDTO>> qteRestByCA = purchaseOrdersDetails.stream()
//                .collect(groupingBy(DetailCommandeAchatDTO::getCode, Collectors.reducing((a, b) -> {
//                    a.setQuantite(a.getQuantite().add(b.getQuantite()));
//                    a.setQuantiteRestante(a.getQuantiteRestante().add(b.getQuantiteRestante()));
//                    a.setQuantiteGratuite(a.getQuantiteGratuite().add(b.getQuantiteGratuite()));
//                    a.setQuantiteGratuiteRestante(a.getQuantiteGratuiteRestante().add(b.getQuantiteGratuiteRestante()));
//                    return a;
//                })));
//        List<EtatReceptionCA> etatsCA = new ArrayList();
//        qteRestByCA.forEach((key, value) -> {
//            DetailCommandeAchatDTO mergedDetailCA = value.get();
////                log.debug("mergedDetailCA.getQuantiteRestante()) est {} et mergedDetailCA.getQuantiteGratuiteRestante()est {} ", mergedDetailCA.getQuantiteRestante(), mergedDetailCA.getQuantiteGratuiteRestante());
////                log.debug(" mergedDetailCA.getQuantiteRestante().compareTo(BigDecimal.ZERO)==0  {}", mergedDetailCA.getQuantiteRestante().compareTo(BigDecimal.ZERO) == 0);
//            if (mergedDetailCA.getQuantiteRestante().compareTo(BigDecimal.ZERO) == 0 && mergedDetailCA.getQuantiteGratuiteRestante().compareTo(BigDecimal.ZERO) == 0) {
//                etatsCA.add(new EtatReceptionCA(key, RECEIVED));
//            } else if (mergedDetailCA.getQuantiteRestante().equals(mergedDetailCA.getQuantite()) && mergedDetailCA.getQuantiteGratuiteRestante().equals(mergedDetailCA.getQuantiteGratuite())) {
//                etatsCA.add(new EtatReceptionCA(key, NOT_RECEIVED));
//            } else {
//                etatsCA.add(new EtatReceptionCA(key, PARTIALLY_RECIVED));
//            }
//        }
//        );
//        etatReceptionCAService.save(etatsCA);
//
//        return receptionDetailCAs;
//    }
