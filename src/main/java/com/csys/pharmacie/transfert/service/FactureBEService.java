package com.csys.pharmacie.transfert.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DemandeRedressementDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.service.DemandeServiceClient;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import static com.csys.pharmacie.helper.CategorieDepotEnum.EC;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.config.SenderComptable;
import com.csys.pharmacie.helper.DetailMvtSto;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.repository.DepstoRepository;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.transfert.domain.BaseTvaRedressement;

import com.csys.pharmacie.transfert.domain.DetailMvtStoBE;
import com.csys.pharmacie.transfert.domain.FactureBE;
import com.csys.pharmacie.transfert.domain.MvtStoBE;
import com.csys.pharmacie.transfert.domain.QFactureBE;
import com.csys.pharmacie.transfert.dto.FactureBEDTO;
import com.csys.pharmacie.transfert.dto.FactureBEEditionDTO;
import com.csys.pharmacie.transfert.dto.MotifDemandeRedressementDTO;
import com.csys.pharmacie.transfert.dto.MvtStoBEDTO;
import com.csys.pharmacie.transfert.dto.MvtStoBEEditionDTO;
import com.csys.pharmacie.transfert.factory.FactureBEFactory;
import com.csys.pharmacie.transfert.factory.MvtStoBEFactory;
import com.csys.pharmacie.transfert.repository.FactureBERepository;
import com.csys.pharmacie.vente.dto.PMPArticleDTO;
import com.csys.pharmacie.vente.service.PricingService;
import com.csys.util.Preconditions;
import com.mysema.commons.lang.Pair;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing FactureBE.
 */
@Service
@Transactional
public class FactureBEService {

    private final Logger log = LoggerFactory.getLogger(FactureBEService.class);

    private final FactureBERepository facturebeRepository;

    private final ParamService paramService;
    private final StockService stockService;
//    private final MotifRedressementService motifRedressementService;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final ParamServiceClient parametrageService;
    private final InventaireService inventaireService;
    private final PricingService pricingService;
    private final DemandeServiceClient demandeServiceClient;

    private final DepstoRepository depstoRepository;
    private final SenderComptable senderComptable;

    @Value("${kafka.topic.redressement-bill-management-for-accounting}")
    private String topicRedressementBillManagementForAccounting;

    public FactureBEService(FactureBERepository facturebeRepository, ParamService paramService, StockService stockService, ParamAchatServiceClient paramAchatServiceClient, ParamServiceClient parametrageService, InventaireService inventaireService, PricingService pricingService, DemandeServiceClient demandeServiceClient,SenderComptable senderComptable,DepstoRepository depstoRepository) {
        this.facturebeRepository = facturebeRepository;
        this.paramService = paramService;
        this.stockService = stockService;
//        this.motifRedressementService = motifRedressementService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.parametrageService = parametrageService;
        this.inventaireService = inventaireService;
        this.pricingService = pricingService;
        this.demandeServiceClient = demandeServiceClient;
        this.senderComptable=senderComptable;
        this.depstoRepository=depstoRepository;
    }

    /**
     * Save a facturebeDTO.
     *
     * @param facturebeDTO
     * @return the persisted entity
     */
    public FactureBEDTO save(FactureBEDTO facturebeDTO) {

        log.debug("Request to save FactureBE: {}", facturebeDTO);
        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(facturebeDTO.getCoddep());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + depotd.getDesignation() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getDesignation() + "] est un dépot fournisseur");

        FactureBE facturebe = FactureBEFactory.facturebeDTOToFactureBE(facturebeDTO);
        String numbon = paramService.getcompteur(facturebeDTO.getCategDepot(), TypeBonEnum.BE);
        facturebe.setNumbon(numbon);



        Set<Integer>  itemIds = facturebeDTO.getDetails().stream().map(MvtStoBEDTO::getCodArt).collect(Collectors.toSet());
        List<ArticleDTO> listAllItems = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(facturebeDTO.getCategDepot(), itemIds.toArray(new Integer[itemIds.size()]));        //List<ArticleDTO> articles=paramAchatServiceClient.findArticlebyCategorieDepotAndCodeArticle(facturebe.getCategDepot(), detail.getCode())
        log.debug("liste des articels {}",listAllItems);
        // check inventory
        Map<Boolean, List<MvtStoBEDTO>> partitioned = facturebeDTO.getDetails().stream().collect(partitioningBy(e -> e.getQuantite().compareTo(BigDecimal.ZERO) > 0));
        Set<Integer> listItemsIdsWithQuantityGreaterThanZero=partitioned.get(true).stream().map(MvtStoBEDTO::getCodArt).collect(Collectors.toSet());


        Set<Integer> categArticleIDs = new HashSet();
        //List<ArticleDTO> articlesAveQteGreaterThanZero = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(facturebeDTO.getCategDepot(), partitioned.get(true).stream().map(MvtStoBEDTO::getCodArt).toArray(Integer[]::new));
        List<ArticleDTO> articlesAveQteGreaterThanZero =listAllItems
                .stream()
                .filter(article->listItemsIdsWithQuantityGreaterThanZero.contains(article.getCode()))
                .collect(Collectors.toList());
        log.debug("liste des articels positifs  {} ****",articlesAveQteGreaterThanZero);
        articlesAveQteGreaterThanZero.forEach(item -> {
            Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(item.getAnnule()) && !(Boolean.TRUE.equals(item.getStopped())), "item.stopped", item.getCodeSaisi());
            categArticleIDs.add(item.getCategorieArticle().getCode());
        });

        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie( new ArrayList(categArticleIDs), facturebeDTO.getCoddep());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = articlesAveQteGreaterThanZero.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }

        Set<Integer> codeArticles = facturebeDTO.getDetails().stream().map(MvtStoBEDTO::getCodArt).collect(Collectors.toSet());
        List<Depsto> depstos = stockService.findByCodartInAndCoddepAndQteGreaterThan(new ArrayList(codeArticles), facturebeDTO.getCoddep(), BigDecimal.ZERO);
       log.debug("depstos sont {}", depstos);
        List<MvtStoBE> listeMvtstoBE = new ArrayList();
        List<Depsto> newStock = new ArrayList();
        String numordre1 = "0001";
        MotifDemandeRedressementDTO motifRedressement = paramAchatServiceClient.findMotifDemandeRedressementById(facturebeDTO.getCodeMotifRedressement());

        ///////////////////////traitement de la quantite negative////////////////////////////////////
        if (Boolean.TRUE.equals(motifRedressement.getCorrectionLot())) {
            log.debug("**********************debut traitement correction lot*************** ");
            Map<Pair<Integer, Integer>, List<MvtStoBEDTO>> listeMvtstoBEGroupedByItemAndUnit = facturebeDTO.getDetails().stream()
                    .collect(Collectors.groupingBy(mvtStoBEDTO -> Pair.of(mvtStoBEDTO.getCodArt(), mvtStoBEDTO.getCodeUnite())));

            for (Map.Entry<Pair<Integer, Integer>, List<MvtStoBEDTO>> entry : listeMvtstoBEGroupedByItemAndUnit.entrySet()) {
                List<MvtStoBEDTO> listeMvtstoBEGroupes = entry.getValue();

                BigDecimal quantiteTotale = listeMvtstoBEGroupes.stream()
                        .collect(Collectors.reducing(BigDecimal.ZERO,
                                mvtstoBE -> mvtstoBE.getQuantite(), BigDecimal::add));
                Preconditions.checkBusinessLogique((BigDecimal.ZERO).compareTo(quantiteTotale) == 0, "details-de-redressement-erronnes");

                List<MvtStoBEDTO> listeMvtstoBEToRemove = listeMvtstoBEGroupes.stream()
                        .filter(elt -> elt.getQuantite().compareTo(BigDecimal.ZERO) < 0)
                        .collect(Collectors.toList());

                for (MvtStoBEDTO mvtstoBEDTOToRemove : listeMvtstoBEToRemove) {
                    log.debug("**********************mvtstoBEDTOToRemove est {}*************** ", mvtstoBEDTOToRemove);

                    MvtStoBE mvtStoBEToRemove = MvtStoBEFactory.mvtstobeDTOToMvtStoBE(mvtstoBEDTOToRemove);
                    mvtStoBEToRemove.setNumbon(numbon);
                    mvtStoBEToRemove.setFactureBE(facturebe);
                    mvtStoBEToRemove.setNumordre(numordre1);
                    listeMvtstoBE.add(mvtStoBEToRemove);
                    Helper.IncrementString(numordre1, 4);

                    List<Depsto> listeMatchingDepstos = depstos.stream()
                        .peek(x-> log.debug("lot inter du depsto est {} et lot du mvtsto est {} et equal est {}",x.getLotInter(),mvtStoBEToRemove.getLotinter(),x.getLotInter().equals(mvtStoBEToRemove.getLotinter())))
                            .filter(elt
                                    -> elt.getCodart().equals(mvtStoBEToRemove.getCodart())
                            && elt.getUnite().equals(mvtStoBEToRemove.getUnite())
                            && elt.getLotInter().equals(mvtStoBEToRemove.getLotinter())
                            && elt.getDatPer().equals(mvtStoBEToRemove.getDatPer())
                            && elt.getQte().compareTo(BigDecimal.ZERO) > 0
                            ).collect(Collectors.toList());
//                     log.debug("listeMatchingDepstos sont {}", listeMatchingDepstos);
                    List<Depsto> listeNewDepsto = stockService.processStockOnRedressementAvecMotifCorrectionLot(listeMatchingDepstos, mvtStoBEToRemove, numbon);

                    newStock.addAll(listeNewDepsto);
                    //      log.debug("listeMatchingDepstos sont {}", listeMatchingDepstos);
                }
            }
        } else if (Boolean.FALSE.equals(motifRedressement.getCorrectionLot())) {
            List<PMPArticleDTO> pmps = new ArrayList();
            if (facturebeDTO.getCategDepot().equals(EC)) {
                pmps = pricingService.findPMPsByArticleIn(codeArticles.toArray(new Integer[codeArticles.size()]));
            }
            for (MvtStoBEDTO mvtstoBEDTO : partitioned.get(false)) {
                MvtStoBE detailFacture = MvtStoBEFactory.mvtstobeDTOToMvtStoBE(mvtstoBEDTO);
                detailFacture.setCodart(mvtstoBEDTO.getCodArt());
                detailFacture.setNumbon(numbon);
                detailFacture.setNumordre(numordre1);
                detailFacture.setFactureBE(facturebe);
                if (facturebeDTO.getCategDepot().equals(EC)) {
                    BigDecimal matchedPMP = pmps.stream().filter(item -> item.getArticleID().equals(mvtstoBEDTO.getCodArt())).findFirst().get().getPMP();
                    detailFacture.setPriuni(matchedPMP);
                }
                numordre1 = Helper.IncrementString(numordre1, 4);
                listeMvtstoBE.add(detailFacture);
                List<DetailMvtSto> detailMvtSto = stockService.GestionDetailFacture(depstos, detailFacture, DetailMvtStoBE.class, false);

                List<DetailMvtStoBE> detailMvtStoBEs
                        = detailMvtSto.stream()
                                .map(item -> {
                                    DetailMvtStoBE detailMvtStoBE = (DetailMvtStoBE) item;
                                    detailMvtStoBE.setMvtStoBE(detailFacture);

                                    return detailMvtStoBE;
                                })
                                .collect(toList());

                detailFacture.setDetailMvtStoBEList(detailMvtStoBEs);

            }

        }

        for (MvtStoBEDTO mvtStoDTO : partitioned.get(true)) {
            if (Boolean.FALSE.equals(motifRedressement.getCorrectionLot())) {
                MvtStoBE mvtStoBE = MvtStoBEFactory.mvtstobeDTOToMvtStoBE(mvtStoDTO);
                mvtStoBE.setCodart(mvtStoDTO.getCodArt());
                mvtStoBE.setNumbon(numbon);
                mvtStoBE.setNumordre(numordre1);
                mvtStoBE.setFactureBE(facturebe); //set the parent of each MvtStoBE
                listeMvtstoBE.add(mvtStoBE);
                Helper.IncrementString(numordre1, 4);

                Depsto depsto = new Depsto(mvtStoBE);
                depsto.setNumBonOrigin(numbon);
                newStock.add(depsto);
            } else if (Boolean.TRUE.equals(motifRedressement.getCorrectionLot())) {

                BigDecimal quantityMvtstoBEToAddDepsto = mvtStoDTO.getQuantite();
                log.debug("newStock size {}est newStock esr {}", newStock.size(), newStock);

                List<Depsto> listeNewDepstoMatching = newStock.stream()
                        .filter(elt -> elt.getCodart().equals(mvtStoDTO.getCodArt())
                        && elt.getUnite().equals(mvtStoDTO.getCodeUnite())
                        //                && elt.getLotInter().equals(mvtStoDTO.getLotInter())
                        //                && elt.getDatPer().equals(mvtStoDTO.getDatPer())
                        ).collect(Collectors.toList());

                for (Depsto depsto : listeNewDepstoMatching) {

                    log.debug("************  treating depsto {}   ", depsto);
                    if (depsto.getLotInter() != null) {
                        continue;
                    }
                    BigDecimal qteMin = (mvtStoDTO.getQuantite()).min(depsto.getQte());
                    if (depsto.getQte().compareTo(qteMin) > 0) {
//                        log.debug("quantite du depsto est {} ,qteMin est {}", depsto.getQte(), qteMin);
                        Depsto depstoReste = new Depsto(depsto);
                        depstoReste.setQte(depsto.getQte().subtract(qteMin));
                        newStock.add(depstoReste);
//                        log.debug("newStock size est {} et liste Depsto est {}", newStock.size(), newStock);
                    }

                    depsto.setQte(qteMin);
                    depsto.setLotInter(mvtStoDTO.getLotInter());
                    depsto.setDatPer(mvtStoDTO.getDatPer());
                    quantityMvtstoBEToAddDepsto = quantityMvtstoBEToAddDepsto.subtract(qteMin);

                    if (quantityMvtstoBEToAddDepsto.compareTo(BigDecimal.ZERO) == 0) {
                        break;
                    }

                }
//                mvtStoBE.setPriuni(priuni.divide(mvtStoDTO.getQuantite().abs(), 2, RoundingMode.CEILING));
//            if (articles.stream().filter(elt -> elt.getCode().equals(mvtStoBE.getCodart()) && elt.getCodeUnite().equals(elt.getCodeUnite())).findFirst().isPresent()) {
//                articlesToRecalculatePMP.add(mvtStoBE);
//
            }
        }
        // log.debug("newStock sont {}", newStock);
        if (Boolean.TRUE.equals(motifRedressement.getCorrectionLot())) {
            for (Depsto elt : newStock) {
                //    log.debug("elt est {}", elt);

                MvtStoBE mvtstoBE = new MvtStoBE(elt.getQte(), elt.getCodart(), elt.getLotInter(), elt.getDatPer(), elt.getUnite());
                mvtstoBE.setNumbon(numbon);
                mvtstoBE.setCategDepot(elt.getCategDepot());
                mvtstoBE.setNumordre(numordre1);
                mvtstoBE.setCodtva(elt.getCodeTva());
                mvtstoBE.setTautva(elt.getTauxTva());
                mvtstoBE.setFactureBE(facturebe);
                mvtstoBE.setPriuni(elt.getPu());
                MvtStoBEDTO matchingMvstoBE = partitioned.get(true).stream()
                        .filter(mvtstoBEdto -> mvtstoBEdto.getCodArt().equals(elt.getCodart()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-supplier", new Throwable(elt.getCodart().toString())));
                mvtstoBE.setCodeSaisi(matchingMvstoBE.getCodeSaisi());
                mvtstoBE.setDesart(matchingMvstoBE.getDesart());
                mvtstoBE.setDesArtSec(matchingMvstoBE.getDesArtSec());

                listeMvtstoBE.add(mvtstoBE);

            }
        }
        facturebe.setDetailFactureBECollection(listeMvtstoBE);
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        calcul(listTvas, facturebe);
        facturebe = facturebeRepository.save(facturebe);
        stockService.saveDepsto(newStock);
        if (Boolean.TRUE.equals(motifRedressement.getRegenererPMP())) {
            pricingService.updatePricesAfterRedressement(facturebe, facturebeDTO.getCategDepot());
        }
       /* DemandeRedressementDTO updatedDemande = demandeServiceClient.UpdateSatisfiedDemande(facturebeDTO.getNumeroDemande(), facturebe.getNumbon());
        Preconditions.checkBusinessLogique(updatedDemande != null, "error-updating-demande");*/

        paramService.updateCompteurPharmacie(facturebeDTO.getCategDepot(), TypeBonEnum.BE);
        FactureBEDTO resultDTO = FactureBEFactory.facturebeToFactureBEDTO(facturebe, false);

        //added by oumayma
        DepartementDTO departementDTO = paramAchatServiceClient.findDepartment(facturebe.getCoddep());
        log.info("CostCenter  : ",departementDTO.getCodeCostCenter());
        resultDTO.setCostCenter(departementDTO.getCodeCostCenter());

        //badelt ha4i false


        List<MvtStoBEDTO> mvtstoBEDTOs=new ArrayList<>();
        facturebe.getDetailFactureBECollection().stream().forEach(detail->{
            MvtStoBEDTO mvtstoBEDTO= MvtStoBEFactory.mvtstobeToMvtStoBEDTO(detail);
           ArticleDTO matchedItem=  listAllItems.stream().filter(article->article.getCode().equals(mvtstoBEDTO.getCodArt()))
                   .findFirst()
                     .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article", new Throwable(detail.getCodart().toString())));

            mvtstoBEDTO.setCodeCategorie(matchedItem.getCategorieArticle().getCode());
            log.debug("code categorie1 : "+matchedItem.getCategorieArticle().getCode());
            log.debug("code categorie2 : "+mvtstoBEDTO.getCodeCategorie());
            mvtstoBEDTOs.add(mvtstoBEDTO);
             });
                resultDTO.setDetails(mvtstoBEDTOs);

        senderComptable.sendRedressementBill(topicRedressementBillManagementForAccounting, numbon, resultDTO);
        return resultDTO;
    }

    public FactureBEDTO update(FactureBEDTO facturebeDTO) {
        log.debug("Request to update FactureBE: {}", facturebeDTO);
        FactureBE inBase = facturebeRepository.findOne(facturebeDTO.getNumbon());
        Preconditions.checkBusinessLogique(inBase != null, "facturebe.NotFound");
        FactureBE facturebe = FactureBEFactory.facturebeDTOToFactureBE(facturebeDTO);
        facturebe = facturebeRepository.save(facturebe);
        FactureBEDTO resultDTO = FactureBEFactory.facturebeToFactureBEDTO(facturebe, false);
        return resultDTO;
    }

    @Transactional(readOnly = true)
    public List<MvtStoBEDTO> findByNumBon(String numBon) {
        FactureBE facturebe = facturebeRepository.findOne(numBon);
        Set<Integer> unitIds = facturebe.getDetailFactureBECollection().stream().map(MvtStoBE::getUnite).collect(toSet());
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unitIds);

        List<MvtStoBEDTO> listMvtStoWithPositifQuantity = facturebe.getDetailFactureBECollection().stream()
                .filter(mvtstoBE -> mvtstoBE.getQuantite().compareTo(BigDecimal.ZERO) == 1)
                .map(item -> {
                    MvtStoBEDTO mvtstoBEDTO = MvtStoBEFactory.mvtstobeToMvtStoBEDTO(item);
                    UniteDTO matchedUnit = unities.stream()
                            .filter(unit -> unit.getCode().equals(mvtstoBEDTO.getCodeUnite()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit", new Throwable(mvtstoBEDTO.getCodeUnite().toString())));
                    mvtstoBEDTO.setDesignationUnite(matchedUnit.getDesignation());
                    return mvtstoBEDTO;
                }).collect(toList());
        List<MvtStoBEDTO> listMvtStoWithNegatifQuantity = facturebe.getDetailFactureBECollection().stream()
                .filter(mvtstoBE -> mvtstoBE.getQuantite().compareTo(BigDecimal.ZERO) < 0)
                .flatMap(mvtstoBE -> mvtstoBE.getDetailMvtStoBEList().stream())
                .map(item -> {
                    MvtStoBEDTO mvtstoBEDTO = new MvtStoBEDTO();
                    mvtstoBEDTO.setCodArt(item.getMvtStoBE().getCodart());
                    mvtstoBEDTO.setCodeSaisi(item.getMvtStoBE().getCodeSaisi());
                    mvtstoBEDTO.setQuantite(item.getQuantitePrelevee().negate());
                    mvtstoBEDTO.setPriuni(item.getPriuni().setScale(2, RoundingMode.CEILING));
                    mvtstoBEDTO.setTautva(item.getTauxTva());
                    mvtstoBEDTO.setCodtva(item.getCodeTva());

                    mvtstoBEDTO.setCodeUnite(item.getUnite());
                    mvtstoBEDTO.setLotInter(item.getLotinter());
                    mvtstoBEDTO.setDatPer(item.getDatPer());
                    mvtstoBEDTO.setDesart(item.getMvtStoBE().getDesart());
                    mvtstoBEDTO.setDesArtSec(item.getMvtStoBE().getDesArtSec());
                    //TODO all for dto produced to compta
                   // Depsto depsto=depstoRepository.findOne(item.getDepsto().getCode()) ;
                    //mvtstoBEDTO.setPriuni_depsto(depsto.getPu());

                 //   log.debug(depsto.toString());
                    UniteDTO matchedUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUnite()))
                            .findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit", new Throwable(item.getUnite().toString())));
                    mvtstoBEDTO.setDesignationUnite(matchedUnit.getDesignation());
                    return mvtstoBEDTO;
                }).collect(toList());
        List<MvtStoBEDTO> resultedList = new ArrayList();
        resultedList.addAll(listMvtStoWithPositifQuantity);
        resultedList.addAll(listMvtStoWithNegatifQuantity);

        return resultedList;
    }

    /**
     * Get one facturebeDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(readOnly = true)
    public FactureBEDTO findOne(String id) {
        log.debug("Request to get FactureBE: {}", id);
        FactureBE facturebe = facturebeRepository.findOne(id);
        Set<Integer> unitIds = facturebe.getDetailFactureBECollection().stream().map(MvtStoBE::getUnite).collect(toSet());
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unitIds);
        FactureBEDTO dto = FactureBEFactory.facturebeToFactureBEDTO(facturebe, false);
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(facturebe.getCoddep());
        dto.setDesignationDepot(depot.getDesignation());
        dto.setCodeSaisiDepot(depot.getCodeSaisi());
        MotifDemandeRedressementDTO motifDTO = paramAchatServiceClient.findMotifDemandeRedressementById(facturebe.getCodeMotifRedressement());
        dto.setDesignationMotifRedressement(motifDTO.getDescription());

        List<MvtStoBEDTO> listMvtStoWithPositifQuantity = facturebe.getDetailFactureBECollection().stream()
                .filter(mvtstoBE -> mvtstoBE.getQuantite().compareTo(BigDecimal.ZERO) == 1)
                .map(item -> {
                    MvtStoBEDTO mvtstoBEDTO = MvtStoBEFactory.mvtstobeToMvtStoBEDTO(item);

                    log.debug("mvtstobeDTO.getUnite() est {}", mvtstoBEDTO.getCodeUnite());
                    UniteDTO matchedUnit = unities.stream()
                            .filter(unit -> unit.getCode().equals(item.getUnite())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit", new Throwable(item.getUnite().toString())));
                    mvtstoBEDTO.setDesignationUnite(matchedUnit.getDesignation());
                    return mvtstoBEDTO;
                }).collect(toList());
        List<MvtStoBEDTO> listMvtStoWithNegatifQuantity = facturebe.getDetailFactureBECollection().stream()
                .filter(mvtstoBE -> mvtstoBE.getQuantite().compareTo(BigDecimal.ZERO) < 0)
                .flatMap(mvtstoBE -> mvtstoBE.getDetailMvtStoBEList().stream())
                .map(item -> {
                    MvtStoBEDTO mvtstoBEDTO = new MvtStoBEDTO();
                    mvtstoBEDTO.setCodArt(item.getMvtStoBE().getCodart());
                    mvtstoBEDTO.setCodeSaisi(item.getMvtStoBE().getCodeSaisi());
                    mvtstoBEDTO.setQuantite(item.getQuantitePrelevee().negate());
                    mvtstoBEDTO.setPriuni(item.getPriuni().setScale(2, RoundingMode.CEILING));
                    mvtstoBEDTO.setTautva(item.getTauxTva());
                    mvtstoBEDTO.setCodtva(item.getCodeTva());
                    mvtstoBEDTO.setCodeUnite(item.getUnite());
                    mvtstoBEDTO.setLotInter(item.getLotinter());
                    mvtstoBEDTO.setDatPer(item.getDatPer());
                    mvtstoBEDTO.setDesart(item.getMvtStoBE().getDesart());
                    mvtstoBEDTO.setDesArtSec(item.getMvtStoBE().getDesArtSec());
                    UniteDTO matchedUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUnite()))
                            .findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit", new Throwable(item.getUnite().toString())));
                    mvtstoBEDTO.setDesignationUnite(matchedUnit.getDesignation());
                    return mvtstoBEDTO;
                }).collect(toList());
        List<MvtStoBEDTO> resultedList = new ArrayList();
        resultedList.addAll(listMvtStoWithPositifQuantity);
        resultedList.addAll(listMvtStoWithNegatifQuantity);

        dto.setDetails(resultedList);

        return dto;
    }

    /**
     * Get one facturebe by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public FactureBE findFactureBE(String id) {
        log.debug("Request to get FactureBE: {}", id);
        FactureBE facturebe = facturebeRepository.findOne(id);
        return facturebe;
    }

    /**
     * Get all the facturebes.
     *
     * @param queriedFacBE
     * @param fromDate
     * @param toDate
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<FactureBEDTO> findAll(FactureBE queriedFacBE, LocalDateTime fromDate, LocalDateTime toDate, Boolean edition) {
        log.debug("Request to get All FactureBEs");
        QFactureBE _FactureBE = QFactureBE.factureBE;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_FactureBE.typbon.eq(TypeBonEnum.BE))
                .optionalAnd(fromDate, () -> _FactureBE.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FactureBE.datbon.loe(toDate))
                .optionalAnd(queriedFacBE.getCategDepot(), () -> _FactureBE.categDepot.eq(queriedFacBE.getCategDepot()))
                .optionalAnd(queriedFacBE.getCoddep(), () -> _FactureBE.coddep.eq(queriedFacBE.getCoddep()));

        List<FactureBE> listFactureBEs = (List<FactureBE>) facturebeRepository.findAll(builder);

        Set<Integer> codeDepots = new HashSet();
        Set<Integer> codeMotifs = new HashSet();
        listFactureBEs.stream().forEach(factureBE -> {
            codeDepots.add(factureBE.getCoddep());
            codeMotifs.add(factureBE.getCodeMotifRedressement());
        });
        List<DepotDTO> listeDepots = paramAchatServiceClient.findDepotsByCodes(codeDepots);
        List<MotifDemandeRedressementDTO> listeMotifs = paramAchatServiceClient.findMotifDemandeRedressementByCodes(codeMotifs);

        return listFactureBEs.stream().map(item -> {
            FactureBEDTO factureBEDTO = FactureBEFactory.facturebeToFactureBEDTO(item, edition);

            DepotDTO matchedDepot = listeDepots.stream().filter(depot -> depot.getCode().equals(item.getCoddep())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-depot", new Throwable(item.getCoddep().toString())));
            factureBEDTO.setDesignationDepot(matchedDepot.getDesignation());
            factureBEDTO.setCodeSaisiDepot(matchedDepot.getCodeSaisi());
            MotifDemandeRedressementDTO matchedMotifDemande = listeMotifs.stream().filter(motif -> motif.getId().equals(item.getCodeMotifRedressement())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-motif", new Throwable(item.getCodeMotifRedressement().toString())));
            factureBEDTO.setDesignationMotifRedressement(matchedMotifDemande.getDescription());

            return factureBEDTO;
        }).distinct().collect(toList());
    }

    /**
     * Delete facturebe by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete FactureBE: {}", id);
        facturebeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public byte[] edition(String numBon)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        log.debug("logo *************************** {}", cliniqueDto.get(0).getLogoClinique());

        log.debug("Request to get FactureBE: {}", numBon);
        FactureBE facturebe = facturebeRepository.findOne(numBon);
        Set<Integer> unitIds = facturebe.getDetailFactureBECollection().stream().map(MvtStoBE::getUnite).collect(toSet());
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unitIds);
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(facturebe.getCoddep());
        MotifDemandeRedressementDTO motifDTO = paramAchatServiceClient.findMotifDemandeRedressementById(facturebe.getCodeMotifRedressement());
        DemandeRedressementDTO demandeRedressementDTO = demandeServiceClient.findDemandeRedressement(facturebe.getNumeroDemande(), language);
        FactureBEEditionDTO dto = FactureBEFactory.facturebeToFactureBEEditionDTO(facturebe, demandeRedressementDTO);
        dto.setCodeMotifRedressement(facturebe.getCodeMotifRedressement());
        dto.setDesignationMotifRedressement(motifDTO.getDescription());

        dto.setDesignationDepot(depot.getDesignation());

        List<MvtStoBEEditionDTO> listMvtStoWithPositifQuantity = facturebe.getDetailFactureBECollection().stream()
                .filter(mvtstoBE -> mvtstoBE.getQuantite().compareTo(BigDecimal.ZERO) == 1)
                .map(item -> {
                    MvtStoBEEditionDTO mvtstoBEDTO = MvtStoBEFactory.mvtstobeToMvtStoBEEditionDTO(item);
                    UniteDTO matchedUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUnite())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit", new Throwable(item.getUnite().toString())));
                    mvtstoBEDTO.setDesignationUnite(matchedUnit.getDesignation());
                    return mvtstoBEDTO;
                }).collect(toList());

        List<MvtStoBEEditionDTO> listMvtStoWithNegatifQuantity = facturebe.getDetailFactureBECollection().stream()
                .filter(mvtstoBE -> mvtstoBE.getQuantite().compareTo(BigDecimal.ZERO) < 0)
                .flatMap(mvtstoBE -> mvtstoBE.getDetailMvtStoBEList().stream())
                .map(item -> {
                    MvtStoBEEditionDTO MvtStoBEEditionDTO = new MvtStoBEEditionDTO();
                    MvtStoBEEditionDTO.setCodArt(item.getMvtStoBE().getCodart());
                    MvtStoBEEditionDTO.setDesart(item.getMvtStoBE().getDesart());
                    MvtStoBEEditionDTO.setDesArtSec(item.getMvtStoBE().getDesArtSec());
                    MvtStoBEEditionDTO.setCodeSaisi(item.getMvtStoBE().getCodeSaisi());
                    MvtStoBEEditionDTO.setQuantite(item.getQuantitePrelevee().negate());
                    MvtStoBEEditionDTO.setPriuni(item.getPriuni().setScale(2, RoundingMode.CEILING));
                    MvtStoBEEditionDTO.setCodeUnite(item.getUnite());
                    MvtStoBEEditionDTO.setLotInter(item.getLotinter());
                    MvtStoBEEditionDTO.setDatPer(Date.from(item.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    UniteDTO matchedUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUnite()))
                            .findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit", new Throwable(item.getUnite().toString())));
                    MvtStoBEEditionDTO.setDesignationUnite(matchedUnit.getDesignation());
                    return MvtStoBEEditionDTO;
                }).collect(toList());
        List<MvtStoBEEditionDTO> resultedList = new ArrayList();
        resultedList.addAll(listMvtStoWithPositifQuantity);
        resultedList.addAll(listMvtStoWithNegatifQuantity);
        dto.setDetails(resultedList);

        log.debug("edition FactureBE: {}", dto.toString());

        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        reportClientDoc.open("Reports/BonRedressement" + local + ".rpt", 0);

        reportClientDoc
                .getDatabaseController().setDataSource(dto.getDetails(), MvtStoBEEditionDTO.class,
                        "Detaille", "Detaille");

        reportClientDoc
                .getDatabaseController().setDataSource(Arrays.asList(dto), FactureBEEditionDTO.class,
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

    public FactureBE calcul(List<TvaDTO> listTVA, FactureBE factureBE) {

        List<BaseTvaRedressement> listeBaseTVA = new ArrayList<BaseTvaRedressement>();

//        List<MvtStoBE> TEST = detailFactureBECollection.stream().filter(item-> item.getQuantite().intValue()>0).collect(Collectors.toList());
//        List<MvtStoBE> TEST1=details.stream().filter(item-> item.getQuantite().intValue()<0).collect(Collectors.toList());
//        TEST1.forEach(item->{TEST.add(item);});
//        detailFactureBECollection
        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVAForItemsWithPositifQuantity = factureBE.getDetailFactureBECollection()
                .stream()
                .filter(mvtstoBE -> mvtstoBE.getQuantite().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodtva(), art.getTautva().setScale(2)), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getPriuni().multiply(art.getQuantite()), BigDecimal::add)));

        List<DetailMvtStoBE> listDetaileMvtstoBeWithNegatifQuantity = factureBE.getDetailFactureBECollection().stream()
                .filter(mvtstoBE -> mvtstoBE.getQuantite().compareTo(BigDecimal.ZERO) < 0)
                .flatMap(mvtstoBE -> mvtstoBE.getDetailMvtStoBEList().stream()).collect(toList());

        Map<Pair<Integer, BigDecimal>, BigDecimal> baseTVAForItemsWithNegatifQuantity = listDetaileMvtstoBeWithNegatifQuantity
                .stream()
                .collect(Collectors.groupingBy(art -> Pair.of(art.getCodeTva(), art.getTauxTva().setScale(2)), Collectors.reducing(BigDecimal.ZERO,
                        art -> art.getPriuni().setScale(2, RoundingMode.CEILING).multiply(art.getQuantitePrelevee().negate()), BigDecimal::add)));

        Map<Pair<Integer, BigDecimal>, BigDecimal> resultedMap = Stream.of(baseTVAForItemsWithPositifQuantity, baseTVAForItemsWithNegatifQuantity)
                .map(Map::entrySet) // converts each map into an entry set
                .flatMap(Collection::stream) // converts each set into an entry stream, then
                // "concatenates" it in place of the original set
                .collect(
                        Collectors.toMap( // collects into a map
                                Map.Entry::getKey, // where each entry is based
                                Map.Entry::getValue, // on the entries in the stream
                                BigDecimal::add)); // such that if a value already exist for a given key, the sum of the old and new value is taken

        log.debug("resultedMap est {}", resultedMap);
        BigDecimal montantHT = BigDecimal.ZERO;
        BigDecimal montantTva = BigDecimal.ZERO;

        for (Map.Entry<Pair<Integer, BigDecimal>, BigDecimal> entry : resultedMap.entrySet()) {
            BaseTvaRedressement base = new BaseTvaRedressement();
            log.debug("le codeTva est {} et le taux Tva est {} et la base est {} et le montant Ht est {}", entry.getKey().getFirst(), entry.getKey().getSecond(), entry.getValue(), entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)));
            base.setBaseTva(entry.getValue());
            base.setCodeTva(entry.getKey().getFirst());
            base.setTauxTva(entry.getKey().getSecond());
            base.setMontantTva(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)));
            base.setFactureBE(factureBE);
            listeBaseTVA.add(base);
            montantHT = montantHT.add(entry.getValue());
            montantTva = montantTva.add(entry.getValue().multiply(entry.getKey().getSecond()).divide(new BigDecimal(100)));
        }
        for (TvaDTO tva : listTVA) {
            if (!listeBaseTVA.stream().anyMatch(t -> t.getCodeTva().equals(tva.getCode()))) {
                BaseTvaRedressement base = new BaseTvaRedressement();
                base.setBaseTva(BigDecimal.ZERO);
                base.setCodeTva(tva.getCode());
                base.setTauxTva(tva.getValeur());
                base.setMontantTva(BigDecimal.ZERO);
                base.setFactureBE(factureBE);
                listeBaseTVA.add(base);
            }
        }

        factureBE.setBaseTvaRedressement(listeBaseTVA);
        factureBE.setMntbon(montantHT.add(montantTva));
        return factureBE;
    }

}
