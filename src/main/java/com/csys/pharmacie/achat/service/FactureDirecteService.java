package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.DetailFactureDirecte;
import com.csys.pharmacie.achat.domain.FactureDirecte;
import com.csys.pharmacie.achat.domain.QFactureDirecte;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.BaseTvaFactureDirecteDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DetailFactureDirecteDTO;
import com.csys.pharmacie.achat.dto.FactureDirecteDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.factory.BaseTvaFactureDirecteFactory;
import com.csys.pharmacie.achat.factory.DetailFactureDirecteFactory;
import com.csys.pharmacie.achat.factory.FactureDirecteFactory;
import com.csys.pharmacie.achat.repository.FactureDirecteRepository;
import com.csys.pharmacie.config.SenderComptable;
import com.csys.pharmacie.helper.*;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.util.Preconditions;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.domain.FactureDirecteCostCenter;
import com.csys.pharmacie.achat.domain.FcptfrsPH;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.CostProfitCentreDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.EtatReceptionCADTO;
import com.csys.pharmacie.achat.dto.FactureDirecteCostCenterDTO;
import com.csys.pharmacie.achat.dto.FactureDirecteModeReglementDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.factory.FactureDirecteCostCenterFactory;
import com.csys.pharmacie.achat.factory.TransfertCompanyBranchFactory;
import com.csys.pharmacie.achat.repository.FcptFrsPHRepository;
import com.csys.pharmacie.client.dto.DeviseDTO;
import com.csys.pharmacie.client.dto.ModeReglementDTO;
import com.csys.pharmacie.client.dto.MotifPaiementDTO;
import com.csys.pharmacie.client.service.CaisseServiceClient;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import static java.util.stream.Collectors.toSet;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Service Implementation for managing FactureDirecte.
 */
@Service
@Transactional
public class FactureDirecteService {

    private final Logger log = LoggerFactory.getLogger(FactureDirecteService.class);

    private final FactureDirecteRepository facturedirecteRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final ParamService paramService;
    private final DetailFactureDirecteService detailFactureDirecteService;
    private final FcptFrsPHService fcptFrsPHService;
    private final FcptFrsPHRepository fcptFrsPHRepository;
    private final ParamServiceClient parametrageService;
    private final CaisseServiceClient caisseServiceClient;
    private final DemandeServiceClient demandeServiceClient;
    private final EtatReceptionCAService etatReceptionCAService;
    private final SenderComptable senderComptable;
    @Value("${kafka.topic.direct-bill-management-for-accounting}")
    private String topicDirectBillManagementForAccounting;

    public FactureDirecteService(FactureDirecteRepository facturedirecteRepository, ParamAchatServiceClient paramAchatServiceClient, ParamService paramService, DetailFactureDirecteService detailFactureDirecteService, FcptFrsPHService fcptFrsPHService, FcptFrsPHRepository fcptFrsPHRepository, ParamServiceClient parametrageService, CaisseServiceClient caisseServiceClient, DemandeServiceClient demandeServiceClient, EtatReceptionCAService etatReceptionCAService, SenderComptable senderComptable) {
        this.facturedirecteRepository = facturedirecteRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.paramService = paramService;
        this.detailFactureDirecteService = detailFactureDirecteService;
        this.fcptFrsPHService = fcptFrsPHService;
        this.fcptFrsPHRepository = fcptFrsPHRepository;
        this.parametrageService = parametrageService;
        this.caisseServiceClient = caisseServiceClient;
        this.demandeServiceClient = demandeServiceClient;
        this.etatReceptionCAService = etatReceptionCAService;
        this.senderComptable=senderComptable;
    }

    /**
     * Save a facturedirecteDTO.
     *
     * @param facturedirecteDTO
     * @return the persisted entity
     */
    public FactureDirecteDTO save(FactureDirecteDTO facturedirecteDTO) {
        log.debug("Request to save FactureDirecte: {}", facturedirecteDTO);

        FactureDirecte facturedirecte = FactureDirecteFactory.facturedirecteDTOToFactureDirecte(facturedirecteDTO);

        FactureDirecte factureWithSameRef = facturedirecteRepository.findByReferenceFournisseur(facturedirecte.getReferenceFournisseur());
        Preconditions.checkBusinessLogique(factureWithSameRef == null, "reffrs-fournisseur-exists");

        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(facturedirecteDTO.getCodeFournisseur());
        Preconditions.checkBusinessLogique(fournisseurDTO != null, "missing-supplier", facturedirecteDTO.getCodeFournisseur());
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseurDTO.getAnnule()) && !(Boolean.TRUE.equals(fournisseurDTO.getStopped())), "fournisseur.stopped", fournisseurDTO.getCode());

        DeviseDTO deviseDTO = caisseServiceClient.findDeviseById(facturedirecteDTO.getCodeDevise());
        Preconditions.checkBusinessLogique(deviseDTO != null, "missing-devise", facturedirecteDTO.getCodeDevise());

        facturedirecte.setTypbon(TypeBonEnum.DIR);

        List<Integer> articlesID = facturedirecte.getDetailFactureDirecteCollection().stream().map(DetailFactureDirecte::getCodart).collect(toList());
        List<ArticleDTO> articles = paramAchatServiceClient.articleECFindbyListCode(articlesID);
        // verifying that all items are assets
        Preconditions.checkBusinessLogique(articles.size() == articlesID.size(), "facture-directe.non-assets-item");
        //verfying that allitems are non stockable
        articles.forEach(item -> {
            Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(item.getAnnule()) && !(Boolean.TRUE.equals(item.getStopped())), "item.stopped", item.getCodeSaisi());
            Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(item.getStockable()), "facture-directe.non-stockable-item", item.getCodeSaisi());
        });

        String numbon = paramService.getcompteur(facturedirecteDTO.getCategDepot(), TypeBonEnum.DIR);
        facturedirecte.setIntegrer(false);
        facturedirecte.setNumbon(numbon);
        facturedirecte.calcul(paramAchatServiceClient.findTvas());
        BigDecimal sumCostCentersValues = facturedirecte.getCostCenters().stream().map(FactureDirecteCostCenter::getMontantTTC).collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));
        log.debug("sumCostCentersValues : {} ", sumCostCentersValues);
        log.debug("facturedirecte.getMontant : {} ", facturedirecte.getMontant());
        Preconditions.checkBusinessLogique(sumCostCentersValues.compareTo(facturedirecte.getMontant()) == 0, "facture-directe.cost-centers-wrong-values");
        facturedirecte = facturedirecteRepository.save(facturedirecte);
        fcptFrsPHService.addFcptFrsOnFactureDirecte(facturedirecte);
        paramService.updateCompteurPharmacie(CategorieDepotEnum.EC, TypeBonEnum.DIR);
        FactureDirecteDTO resultDTO = FactureDirecteFactory.facturedirecteToFactureDirecteDTO(facturedirecte, Boolean.FALSE);
        resultDTO.setAction(EnumCrudMethod.CREATE);
        senderComptable.sendDirectBill(topicDirectBillManagementForAccounting, numbon, resultDTO);
        return resultDTO;
    }
    

    public FactureDirecteDTO saveWithBonCommande(FactureDirecteDTO facturedirecteDTO) {
        log.debug("Request to save FactureDirecte with bon commande: {}", facturedirecteDTO);
        String language = LocaleContextHolder.getLocale().getLanguage();
        CommandeAchatDTO commandeAchatDTO = demandeServiceClient.findCommandeAchat(facturedirecteDTO.getCodeCommandeAchat(), language);
        Preconditions.checkBusinessLogique(commandeAchatDTO.getUserAnnul() == null, "commande-achat-annuler");
        Preconditions.checkBusinessLogique(commandeAchatDTO.getValide() == true, "commande-achat-not-valide");
        FactureDirecte facturedirecte = FactureDirecteFactory.facturedirecteDTOToFactureDirecte(facturedirecteDTO);
        log.debug("datBon est :{}", facturedirecte.getDatbon());
        FactureDirecte factureWithSameRef = facturedirecteRepository.findByReferenceFournisseur(facturedirecte.getReferenceFournisseur());
        Preconditions.checkBusinessLogique(factureWithSameRef == null, "reffrs-fournisseur-exists");

        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(facturedirecteDTO.getCodeFournisseur());
        Preconditions.checkBusinessLogique(fournisseurDTO != null, "missing-supplier", facturedirecteDTO.getCodeFournisseur());
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseurDTO.getAnnule()) && !(Boolean.TRUE.equals(fournisseurDTO.getStopped())), "fournisseur.stopped", fournisseurDTO.getCode());

        DeviseDTO deviseDTO = caisseServiceClient.findDeviseById(facturedirecteDTO.getCodeDevise());
        Preconditions.checkBusinessLogique(deviseDTO != null, "missing-devise", facturedirecteDTO.getCodeDevise());

        facturedirecte.setTypbon(TypeBonEnum.DIR);

        List<Integer> articlesID = facturedirecte.getDetailFactureDirecteCollection().stream().map(DetailFactureDirecte::getCodart).collect(toList());
        List<ArticleDTO> articles = paramAchatServiceClient.articleECFindbyListCode(articlesID);
        // verifying that all items are assets
        Preconditions.checkBusinessLogique(articles.size() == articlesID.size(), "facture-directe.non-assets-item");
        //verfying that allitems are non stockable
        articles.stream().filter(ArticleDTO::getStockable).findFirst().ifPresent(item -> {
            throw new IllegalBusinessLogiqueException("facture-directe.non-stockable-item", new Throwable(item.getCodeSaisi()));
        });
        String numbon = paramService.getcompteur(facturedirecteDTO.getCategDepot(), TypeBonEnum.DIR);
        facturedirecte.setIntegrer(false);
        facturedirecte.setNumbon(numbon);
        facturedirecte.calcul(paramAchatServiceClient.findTvas());
//        String language = LocaleContextHolder.getLocale().getLanguage();
//           CommandeAchatDTO commandeAchatDTO = demandeServiceClient.findCommandeAchat(facturedirecteDTO.getCodeCommandeAchat(), language);
//             Preconditions.checkBusinessLogique(commandeAchatDTO.getMontantttc()!= null, "missing-selling-price");
//           facturedirecte.setMontant(commandeAchatDTO.getMontantttc());
        BigDecimal sumCostCentersValues = facturedirecte.getCostCenters().stream().map(FactureDirecteCostCenter::getMontantTTC).collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));
        log.debug("sumCostCentersValues : {} ", sumCostCentersValues);
        log.debug("facturedirecte.getMontant : {} ", facturedirecte.getMontant());
        Preconditions.checkBusinessLogique(sumCostCentersValues.compareTo(facturedirecte.getMontant()) == 0, "facture-directe.cost-centers-wrong-values");
        facturedirecte.setCodeCommandeAchat(facturedirecteDTO.getCodeCommandeAchat());
        List<Integer> codeCommande = new ArrayList<Integer>();
        codeCommande.add(facturedirecte.getCodeCommandeAchat());
        demandeServiceClient.updateCommandesByCodesIn(codeCommande, facturedirecte.getNumbon());
        EtatReceptionCADTO etatReceptioncaDTO = new EtatReceptionCADTO();
        etatReceptioncaDTO.setCommandeAchat(facturedirecte.getCodeCommandeAchat());
        etatReceptioncaDTO.setEtatReception(PurchaseOrderReceptionState.RECEIVED);
        facturedirecte = facturedirecteRepository.save(facturedirecte);
        etatReceptionCAService.save(etatReceptioncaDTO);
        fcptFrsPHService.addFcptFrsOnFactureDirecte(facturedirecte);
        paramService.updateCompteurPharmacie(CategorieDepotEnum.EC, TypeBonEnum.DIR);
        FactureDirecteDTO resultDTO = FactureDirecteFactory.facturedirecteToFactureDirecteDTO(facturedirecte, Boolean.FALSE);
        resultDTO.setAction(EnumCrudMethod.CREATE);
        senderComptable.sendDirectBill(topicDirectBillManagementForAccounting, numbon, resultDTO);
        return resultDTO;
    }

    /**
     * Update a factureDirecteDTO.
     *
     * @param factureDirecteDTO
     * @return the updated entity
     */
    public FactureDirecteDTO update(FactureDirecteDTO factureDirecteDTO) {
        log.debug("Request to update FactureDirecte: {}", factureDirecteDTO);

        FactureDirecte inBase = facturedirecteRepository.findOne(factureDirecteDTO.getNumbon());

        //verification existence de la facture a modifier 
        Preconditions.checkBusinessLogique(inBase != null, "facturedirecte.NotFound");

        // verification si la facture est deja annulé 
        Preconditions.checkBusinessLogique(inBase.getUserAnnule() == null, "facturedirecte.Annule");

        //verification facture reglée ou pas 
        FcptfrsPH fcptFrs = fcptFrsPHRepository.findFirstByNumBon(inBase.getNumbon());
        BigDecimal numOpr = new BigDecimal(fcptFrs.getNumOpr());
        Preconditions.checkBusinessLogique(facturedirecteRepository.findFactureReglee(numOpr).compareTo(BigDecimal.ZERO) == 0, "factureDirecte-deja-reglee");

        //verification integrer ou pas        
        Preconditions.checkBusinessLogique(!inBase.getIntegrer(), "factureDirecte-deja-integre");

        // verification payée
        Preconditions.checkBusinessLogique(fcptFrs.getCredit().equals(fcptFrs.getReste()), "factureDirecte-is-paid");

        FactureDirecte facturedirecte = FactureDirecteFactory.facturedirecteDTOToFactureDirecte(factureDirecteDTO);
        FactureDirecte factureWithSameRef = facturedirecteRepository.findByReferenceFournisseur(facturedirecte.getReferenceFournisseur());
        Preconditions.checkBusinessLogique(factureWithSameRef == inBase || factureWithSameRef == null, "reffrs-fournisseur-exists");
        facturedirecte.setCodeFournisseur(inBase.getCodeFournisseur());

        List<Integer> articlesID = factureDirecteDTO.getDetailFactureDirecteCollection().stream().map(DetailFactureDirecteDTO::getCodart).collect(toList());
        List<ArticleDTO> articles = paramAchatServiceClient.articleECFindbyListCode(articlesID);
        Preconditions.checkBusinessLogique(articles.size() == articlesID.size(), "facture-directe.non-assets-item");
        articles.stream().filter(ArticleDTO::getStockable).findFirst().ifPresent(item -> {
            throw new IllegalBusinessLogiqueException("facture-directe.non-stockable-item", new Throwable(item.getCodeSaisi()));
        });

        facturedirecte.setDatbon(inBase.getDatbon());
        facturedirecte.setDatesys(inBase.getDatesys());
        facturedirecte.setHeuresys(inBase.getHeuresys());
        facturedirecte.setTypbon(inBase.getTypbon());

        facturedirecte.calcul(paramAchatServiceClient.findTvas());
        if (inBase.getCodeCommandeAchat() != null) {
            facturedirecte.setCodeCommandeAchat(inBase.getCodeCommandeAchat());
//        String language = LocaleContextHolder.getLocale().getLanguage();
//           CommandeAchatDTO commandeAchatDTO = demandeServiceClient.findCommandeAchat(facturedirecte.getCodeCommandeAchat(), language);
//             Preconditions.checkBusinessLogique(commandeAchatDTO.getMontantttc()!= null, "missing-selling-price");
//           facturedirecte.setMontant(commandeAchatDTO.getMontantttc());
        }
        facturedirecte.calcul(paramAchatServiceClient.findTvas());
        BigDecimal sumCostCentersValues = facturedirecte.getCostCenters().stream().map(FactureDirecteCostCenter::getMontantTTC).collect(Collectors.reducing(BigDecimal.ZERO, (a, b) -> a.add(b)));
        log.debug("sumCostCentersValues : {} ", sumCostCentersValues);
        log.debug("facturedirecte.getMontant : {} ", facturedirecte.getMontant());
        Preconditions.checkBusinessLogique(sumCostCentersValues.compareTo(facturedirecte.getMontant()) == 0, "facture-directe.cost-centers-wrong-values");
        facturedirecte.setNumaffiche(inBase.getNumaffiche());
        facturedirecte.setIntegrer(inBase.getIntegrer());

        facturedirecte = facturedirecteRepository.save(facturedirecte);

        fcptFrsPHService.updateFcptFrsOnFactureDirecte(fcptFrs, facturedirecte);
        FactureDirecteDTO resultDTO = FactureDirecteFactory.facturedirecteToFactureDirecteDTO(facturedirecte, Boolean.FALSE);
        resultDTO.setAction(EnumCrudMethod.UPDATE);
        senderComptable.sendDirectBill(topicDirectBillManagementForAccounting, factureDirecteDTO.getNumbon(), resultDTO);
        return resultDTO;
    }

    /**
     * Get one facturedirecteDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public FactureDirecteDTO findOne(String id) {
        log.debug("Request to get FactureDirecte: {}", id);
        FactureDirecte facturedirecte = facturedirecteRepository.findOne(id);
        FactureDirecteDTO factureDTO = FactureDirecteFactory.lazyfacturedirecteToFactureDirecteDTO(facturedirecte);
        FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(factureDTO.getCodeFournisseur());

        Preconditions.checkBusinessLogique(fournisseur != null, "missing-supplier", factureDTO.getCodeFournisseur());
        factureDTO.setFournisseur(fournisseur);
        DeviseDTO deviseDTO = caisseServiceClient.findDeviseById(facturedirecte.getCodeDevise());
        Preconditions.checkBusinessLogique(deviseDTO != null, "missing-devise", facturedirecte.getCodeDevise());
        factureDTO.setDevise(deviseDTO);
        List<Integer> idUnits = facturedirecte.getDetailFactureDirecteCollection().stream().map(DetailFactureDirecte::getUnite).collect(toList());
        List<UniteDTO> units = paramAchatServiceClient.findUnitsByCodes(idUnits);
        Set<DetailFactureDirecteDTO> detailsFactureDirecteDTO = facturedirecte.getDetailFactureDirecteCollection().stream().map(detail -> {
            DetailFactureDirecteDTO dto = DetailFactureDirecteFactory.detailfacturedirecteToDetailFactureDirecteDTO(detail);
            UniteDTO matchedUnite = units.stream()
                    .filter(unit -> unit.getCode().equals(detail.getUnite()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit", new Throwable(detail.getUnite().toString())));
            dto.setDesignationUnite(matchedUnite.getDesignation());
            return dto;
        }).collect(toSet());
        factureDTO.setDetailFactureDirecteCollection(detailsFactureDirecteDTO);

        Integer[] idCOstCenters = facturedirecte.getCostCenters().stream().map(item -> item.getPk().getCodeCostCenter()).toArray(Integer[]::new);
        Collection<CostProfitCentreDTO> costCenters = parametrageService.findCostProfitCentreByCodeIn(idCOstCenters);

        List<FactureDirecteCostCenterDTO> costCentersDTO = facturedirecte.getCostCenters().stream().map(item -> {
            FactureDirecteCostCenterDTO costCenter = FactureDirecteCostCenterFactory.facturedirectecostcenterToFactureDirecteCostCenterDTO(item);
            CostProfitCentreDTO matchedCostCenter = costCenters.stream()
                    .filter(elt -> elt.getCode().equals(costCenter.getCodeCostCenter()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-cost-center", new Throwable(item.getPk().getCodeCostCenter().toString())));
            costCenter.setCodeSaisiCostCenter(matchedCostCenter.getCodeSaisie());
            costCenter.setDesignationCostCenter(matchedCostCenter.getDesignation());
            return costCenter;
        }).collect(Collectors.toList());
        factureDTO.setCostCenters(costCentersDTO);
        List<BaseTvaFactureDirecteDTO> baseTvaFactureDirecteCollectionDtos = new ArrayList<>();
        facturedirecte.getBaseTvaFactureDirecteCollection().forEach(x -> {
            BaseTvaFactureDirecteDTO basetvafacturedirecteDto = BaseTvaFactureDirecteFactory.basetvafacturedirecteToBaseTvaFactureDirecteDTO(x);
            baseTvaFactureDirecteCollectionDtos.add(basetvafacturedirecteDto);
        });
        factureDTO.setBaseTvaFactureDirecteCollection(baseTvaFactureDirecteCollectionDtos);

        log.debug("Request to get Facture Direct ModeReglementList: {}", facturedirecte.getFactureDirecteModeReglement());
        log.debug("Request to get Facture Direct ModeReglementList DTO {}", factureDTO.getModeReglementList());
        if (factureDTO.getModeReglementList() != null && !factureDTO.getModeReglementList().isEmpty()) {
            Integer[] codesModeReg = factureDTO.getModeReglementList().stream().map(x -> x.getCodeReglement()).toArray(Integer[]::new);
            Collection<ModeReglementDTO> modeReglementDTOs = parametrageService.findModeReglementByCodes(codesModeReg);

            Integer[] codesMotifPaiement = factureDTO.getModeReglementList().stream().map(x -> x.getCodeMotifPaiement()).toArray(Integer[]::new);
            Collection<MotifPaiementDTO> motifPaiementDTOs = paramAchatServiceClient.findMotifPaiementByCodes(codesMotifPaiement);

            factureDTO.getModeReglementList().forEach(x -> {
                x.setDesignationReglement(modeReglementDTOs.stream().filter(mR -> mR.getCode().equals(x.getCodeReglement()))
                        .map(mR -> mR.getDesignation()).findFirst().get());
                x.setDesignationMotifPaiement(motifPaiementDTOs.stream().filter(mP -> mP.getCode().equals(x.getCodeMotifPaiement()))
                        .map(mR -> mR.getDesignation()).findFirst().get());
            });
        }
        String language = LocaleContextHolder.getLocale().getLanguage();
        if (factureDTO.getCodeCommandeAchat() != null) {
            CommandeAchatDTO commandeAchatDTO = demandeServiceClient.findCommandeAchat(factureDTO.getCodeCommandeAchat(), language);
            factureDTO.setCommandeAchat(commandeAchatDTO);
        }

        return factureDTO;
    }

    /**
     * Get one facturedirecte by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public FactureDirecte findFactureDirecte(String id) {
        log.debug("Request to get FactureDirecte: {}", id);
        FactureDirecte facturedirecte = facturedirecteRepository.findOne(id);
        return facturedirecte;
    }

    /**
     * Get all the facturedirectes.
     *
     * @param fromDate
     * @param toDate
     * @param deleted
     * @param withDetails
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public Collection<FactureDirecteDTO> findAll(LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted, Boolean withDetails) {
        log.debug("Request to get All FactureDirectes");

        QFactureDirecte _FactureDirecte = QFactureDirecte.factureDirecte;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(fromDate, () -> _FactureDirecte.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FactureDirecte.datbon.loe(toDate))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _FactureDirecte.dateAnnule.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _FactureDirecte.dateAnnule.isNull());

        Set<FactureDirecte> listFactureDirectes = new HashSet<>((List<FactureDirecte>) facturedirecteRepository.findAll(builder));
        List<String> fournisseursID = listFactureDirectes.stream().map(FactureDirecte::getCodeFournisseur).collect(toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(fournisseursID);
        return listFactureDirectes.stream().map(facture -> {
            FactureDirecteDTO dto;
            if (Boolean.TRUE.equals(withDetails)) {
                dto = FactureDirecteFactory.facturedirecteToFactureDirecteDTO(facture, Boolean.TRUE);
            } else {
                dto = FactureDirecteFactory.lazyfacturedirecteToFactureDirecteDTO(facture);
            }
            FournisseurDTO matchedFournisseur = fournisseurs.stream()
                    .filter(fournisseur -> fournisseur.getCode().equals(facture.getCodeFournisseur()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-supplier", new Throwable(facture.getCodeFournisseur())));

            dto.setFournisseur(matchedFournisseur);
            dto.setDesignationFournisseur(matchedFournisseur.getDesignation());
            return dto;
        }).collect(toList());

    }

    /**
     * Delete facturedirecte by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete FactureDirecte: {}", id);
        FactureDirecte facturedirecte = facturedirecteRepository.findOne(id);
        Preconditions.checkFound(facturedirecte, "factureDirecte.NotFound");
//          BigDecimal factureReglee=facturedirecteRepository.findFactureReglee(BigDecimal.ZERO);
        Preconditions.checkBusinessLogique(facturedirecte.getUserAnnule() == null, "facturedirecte.Annule");
        FcptfrsPH fcptFrs = fcptFrsPHRepository.findFirstByNumBon(id);
        BigDecimal numOpr = new BigDecimal(fcptFrs.getNumOpr());
        //verification facture reglée
        Preconditions.checkBusinessLogique(facturedirecteRepository.findFactureReglee(numOpr).compareTo(BigDecimal.ZERO) == 0, "factureDirecte-deja-reglee");
        //verification facture integrée
        Preconditions.checkBusinessLogique(!facturedirecte.getIntegrer(), "factureDirecte-deja-integre");
        if (facturedirecte.getCodeCommandeAchat() != null) {
            String language = LocaleContextHolder.getLocale().getLanguage();

            CommandeAchatDTO commandeAchatDTO = demandeServiceClient.findCommandeAchat(facturedirecte.getCodeCommandeAchat(), language);
            List<Integer> codeCommande = new ArrayList<Integer>();
            codeCommande.add(facturedirecte.getCodeCommandeAchat());
            facturedirecte.setCodeCommandeAchat(null);
            demandeServiceClient.updateCommandesByCodesIn(codeCommande, null);
        }
        LocalDateTime date = LocalDateTime.now();
        facturedirecte.setDateAnnule(date);
        facturedirecte.setUserAnnule(SecurityContextHolder.getContext().getAuthentication().getName());
        fcptFrsPHService.deleteFcptfrsByNumBonDao(id, facturedirecte.getTypbon());
        FactureDirecteDTO resultDTO = FactureDirecteFactory.facturedirecteToFactureDirecteDTO(facturedirecte, Boolean.FALSE);
        resultDTO.setAction(EnumCrudMethod.CANCEL);
        senderComptable.sendDirectBill(topicDirectBillManagementForAccounting, facturedirecte.getNumbon(), resultDTO);
        facturedirecteRepository.save(facturedirecte);
    }

    /**
     * Get details of FactureDirect.
     *
     * @param id the id of the FactureDirect
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public List<DetailFactureDirecteDTO> findDetailFactureById(String id) {
        log.debug("Request to get FacturePR: {}", id);

        Collection<DetailFactureDirecte> details = detailFactureDirecteService.findByNumBon(id);

        List<Integer> list = details.stream().map(item -> item.getUnite()).collect(Collectors.toList());
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(list);
        return details.stream().map(item -> {
            DetailFactureDirecteDTO dto = DetailFactureDirecteFactory.detailfacturedirecteToDetailFactureDirecteDTO(item);
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getUnite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            dto.setDesignationUnite(unite.getDesignation());
            return dto;

        }).collect(toList());

    }

    @Transactional(readOnly = true)
    public byte[] editionFactureDirecte(String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureDirecte : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        FactureDirecteDTO factureDirecteDTO = findOne(id);

        if (factureDirecteDTO.getModeReglementList() != null && !factureDirecteDTO.getModeReglementList().isEmpty()) {
            Integer[] codesModeReg = factureDirecteDTO.getModeReglementList().stream().map(x -> x.getCodeReglement()).toArray(Integer[]::new);
            Collection<ModeReglementDTO> modeReglementDTOs = parametrageService.findModeReglementByCodes(codesModeReg);

            Integer[] codesMotifPaiement = factureDirecteDTO.getModeReglementList().stream().map(x -> x.getCodeMotifPaiement()).toArray(Integer[]::new);
            Collection<MotifPaiementDTO> motifPaiementDTOs = paramAchatServiceClient.findMotifPaiementByCodes(codesMotifPaiement);

            factureDirecteDTO.getModeReglementList().forEach(x -> {
                x.setDesignationReglement(modeReglementDTOs.stream().filter(mR -> mR.getCode().equals(x.getCodeReglement()))
                        .map(mR -> mR.getDesignation()).findFirst().get());
                x.setDesignationMotifPaiement(motifPaiementDTOs.stream().filter(mP -> mP.getCode().equals(x.getCodeMotifPaiement()))
                        .map(mR -> mR.getDesignation()).findFirst().get());
            });
        }
        if (factureDirecteDTO.getModeReglementList() != null && !factureDirecteDTO.getModeReglementList().isEmpty()) {
            Integer[] codesModeReg = factureDirecteDTO.getModeReglementList().stream().map(x -> x.getCodeReglement()).toArray(Integer[]::new);
            Collection<ModeReglementDTO> modeReglementDTOs = parametrageService.findModeReglementByCodes(codesModeReg);

            Integer[] codesMotifPaiement = factureDirecteDTO.getModeReglementList().stream().map(x -> x.getCodeMotifPaiement()).toArray(Integer[]::new);
            Collection<MotifPaiementDTO> motifPaiementDTOs = paramAchatServiceClient.findMotifPaiementByCodes(codesMotifPaiement);

            factureDirecteDTO.getModeReglementList().forEach(x -> {
                x.setDesignationReglement(modeReglementDTOs.stream().filter(mR -> mR.getCode().equals(x.getCodeReglement()))
                        .map(mR -> mR.getDesignation()).findFirst().get());
                x.setDesignationMotifPaiement(motifPaiementDTOs.stream().filter(mP -> mP.getCode().equals(x.getCodeMotifPaiement()))
                        .map(mR -> mR.getDesignation()).findFirst().get());
            });
        }

        reportClientDoc.open("Reports/Facture_Directe" + local + ".rpt", 0);

        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(factureDirecteDTO), FactureDirecteDTO.class, "Entete", "Entete");
        reportClientDoc.getDatabaseController().setDataSource(factureDirecteDTO.getDetailFactureDirecteCollection(), DetailFactureDirecteDTO.class, "Detail", "Detail");
        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(factureDirecteDTO.getFournisseur()), FournisseurDTO.class, "Fournisseur", "Fournisseur");
        reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class, "clinique", "clinique");

        reportClientDoc.getSubreportController().getSubreport("Detail").getDatabaseController().setDataSource(factureDirecteDTO.getDetailFactureDirecteCollection(), DetailFactureDirecteDTO.class, "Detail", "Detail");
        reportClientDoc.getSubreportController().getSubreport("BaseTVA").getDatabaseController().setDataSource(factureDirecteDTO.getBaseTvaFactureDirecteCollection(), BaseTvaFactureDirecteDTO.class, "Commande", "Commande");
        reportClientDoc.getSubreportController().getSubreport("CostCenters").getDatabaseController().setDataSource(factureDirecteDTO.getCostCenters(), FactureDirecteCostCenterDTO.class, "CostCenetrs", "CostCenters");
        reportClientDoc.getSubreportController().getSubreport("modeReglementList").getDatabaseController().setDataSource(factureDirecteDTO.getModeReglementList(), FactureDirecteModeReglementDTO.class, "modeReglementList", "modeReglementList");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        if (factureDirecteDTO.getCodeCommandeAchat() != null) {
            CommandeAchatDTO commandeAchatDTO = demandeServiceClient.findCommandeAchat(factureDirecteDTO.getCodeCommandeAchat(), language);
            paramController.setCurrentValue("", "numbonCommande", commandeAchatDTO.getNumbon());
        } else {
            paramController.setCurrentValue("", "numbonCommande", "");
        }
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
//         paramController.setCurrentValue("", "montantHTVA", factureDirecteDTO.getCommandeAchat().getMontantht());
//           paramController.setCurrentValue("", "montantTVA", factureDirecteDTO.getCommandeAchat().getMontanttva());
//         paramController.setCurrentValue("", "montantTtotal", factureDirecteDTO.getCommandeAchat().getMontantttc());
        if ("ar".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.AR(factureDirecteDTO.getMontant().toString(), "جنيه", "قرش"));
        } else if ("fr".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.FR(factureDirecteDTO.getMontant().toString(), "livres", "pence"));
        } else {
            paramController.setCurrentValue("", "montantLettre", Convert.EN(factureDirecteDTO.getMontant().toString(), "pounds", "penny"));
        }
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }

    public byte[] findAllFactureDirecteWithCostCenterForEdition(LocalDateTime fromDate, LocalDateTime toDate, String type, Boolean annule) throws SQLException, ReportSDKException, IOException {
        log.debug("Request to get All FactureDirecteWithCostCenterForEdition");

        Collection<FactureDirecteDTO> listeFactureDirecteEdition = findAll(fromDate, toDate, annule, true);
        Preconditions.checkBusinessLogique(!listeFactureDirecteEdition.isEmpty(), "data.empty");

        List<DetailFactureDirecteDTO> detailFactureDirecteEdition = listeFactureDirecteEdition.stream()
                .flatMap(facture -> facture.getDetailFactureDirecteCollection().stream()).collect(Collectors.toList());

        List<FactureDirecteCostCenterDTO> factureDirecteCostCenterEdition = listeFactureDirecteEdition.stream()
                .flatMap(facture -> facture.getCostCenters().stream()).collect(Collectors.toList());

        Set<Integer> setCodeCostCenter = factureDirecteCostCenterEdition.stream()
                .map(item -> item.getCodeCostCenter())
                .collect(Collectors.toSet());
        List<Integer> targetList = new ArrayList<>(setCodeCostCenter);
        Integer[] listCodeCostCenter = targetList.toArray(new Integer[0]);
        Collection<CostProfitCentreDTO> listeCostCenterEdition = parametrageService.findCostProfitCentreByCodeIn(listCodeCostCenter);
        Preconditions.checkBusinessLogique(!listeCostCenterEdition.isEmpty(), "data.empty");

        Set<Integer> setCodeArticle = detailFactureDirecteEdition.stream()
                .map(item -> item.getCodart())
                .collect(Collectors.toSet());
        List<Integer> targetListArticle = new ArrayList<>(setCodeArticle);
        Integer[] listCodeArticle = targetListArticle.toArray(new Integer[0]);
        List<ArticleDTO> articlesEdition = paramAchatServiceClient.articlesWithCategoriesParentsByListCodeArticles(CategorieDepotEnum.EC, listCodeArticle);
        Preconditions.checkBusinessLogique(!articlesEdition.isEmpty(), "data.empty");

        List<CliniqueDto> cliniqueDTOs = parametrageService.findClinique();
        cliniqueDTOs.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();

        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/listeFactureDirectewithCostCenter" + local + ".rpt", 0);
            reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDTOs, CliniqueDto.class, "clinique", "clinique");

        } else {
            reportClientDoc.open("Reports/listeFactureDirectewithCostCenter_excel" + local + ".rpt", 0);
        }

        reportClientDoc.getDatabaseController().setDataSource(listeFactureDirecteEdition, FactureDirecteDTO.class, "entete", "entete");
        reportClientDoc.getDatabaseController().setDataSource(detailFactureDirecteEdition, DetailFactureDirecteDTO.class, "detail_facture_directe", "detail_facture_directe");
        reportClientDoc.getDatabaseController().setDataSource(factureDirecteCostCenterEdition, FactureDirecteCostCenterDTO.class, "Commande", "Commande");
        reportClientDoc.getDatabaseController().setDataSource(listeCostCenterEdition, CostProfitCentreDTO.class, "cost_center", "cost_center");
        reportClientDoc.getDatabaseController().setDataSource(articlesEdition, ArticleDTO.class, "article", "article");
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
}
