/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csys.pharmacie.achat.dto.DemandeTrDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.service.DemandeServiceClient;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.DetailMvtSto;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.transfert.domain.FactureBT;

import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.QteMouvement;
import com.csys.pharmacie.helper.SatisfactionEnum;
import static com.csys.pharmacie.helper.TransferOrderState.NOT_TRANSFERRED;
import static com.csys.pharmacie.helper.TransferOrderState.PARTIALLY_TRANSFERRED;
import static com.csys.pharmacie.helper.TransferOrderState.TRANSFERRED;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.prelevement.dto.DetailDemandePrTrDTO;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.service.StockService;

import com.csys.pharmacie.transfert.domain.DetailMvtStoBT;
import com.csys.pharmacie.transfert.domain.EtatDTR;
import com.csys.pharmacie.transfert.domain.MvtStoBT;
import com.csys.pharmacie.transfert.domain.QFactureBT;
import com.csys.pharmacie.transfert.domain.TransfertDetailDTR;
import com.csys.pharmacie.transfert.domain.TransfertDetailDTRPK;
import com.csys.pharmacie.transfert.dto.BonTransferInterDepotDTO;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;
import com.csys.pharmacie.transfert.dto.MvtstoBTDTO;
import com.csys.pharmacie.transfert.factory.BonTransfertInterDepotFactory;
import com.csys.pharmacie.transfert.factory.FactureBTFactory;
import com.csys.pharmacie.transfert.factory.MvtstoBTFactory;
import com.csys.pharmacie.transfert.repository.FactureBTRepository;
import com.csys.pharmacie.transfert.repository.MvtstoBTRepository;
import com.csys.pharmacie.transfert.repository.TransfertDetailDTRRepository;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Farouk
 */
@Service("BonTransfertInterDepotService")
public class BonTransfertInterDepotService {

    @Autowired
    FactureBTRepository factureBTRepository;
    @Autowired
    MvtstoBTRepository mvtstoBTRepository;
    @Autowired
    ParamService paramService;


    @Autowired
    private StockService stockService;

    @Autowired
    DemandeServiceClient demandeServiceClient;
    @Autowired
    TransfertDetailDTRService transfertDetailDTRService;
    @Autowired
    TransfertDetailDTRRepository transfertDetailDTRRepository;
    @Autowired
    EtatDTRService etatDTRService;
    @Autowired
    private ParamAchatServiceClient paramAchatServiceClient;
    @Autowired
    private InventaireService inventaireService;

    private final Logger log = LoggerFactory.getLogger(BonTransfertRecupService.class);
    private static String validationTransfertAuto;
    private static String requestTransfert;

    @Value("${validation-transfert-auto}")
    public void setValidation(String validation) {
        validationTransfertAuto = validation;
    }

    @Value("${transfert-only-with-request}")
    public void setTransfert(String request) {
        requestTransfert = request;
    }
    static String EXPIRATION_DATE_INTERVAL;

    @Value("${transfert-perime.config.expiration-date-interval}")
    public void setEXPIRATION_DATE_INTERVAL(String EXPIRATION_DATE_INTERVAL) {
        BonTransfertInterDepotService.EXPIRATION_DATE_INTERVAL = EXPIRATION_DATE_INTERVAL;
    }

    public String getEXPIRATION_DATE_INTERVAL() {
        return EXPIRATION_DATE_INTERVAL;
    }

    @Transactional
    public FactureBTDTO addBonTransferInterDepot(FactureBTDTO dto) {

        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(dto.getDestinationID());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + depotd.getDesignation() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getCode() + "] est un dépot fournisseur");

        DepotDTO depots = paramAchatServiceClient.findDepotByCode(dto.getSourceID());
        Preconditions.checkBusinessLogique(!depots.getDesignation().equals("depot.deleted"), "Depot [" + depots.getDesignation() + "] introuvable");
        Preconditions.checkBusinessLogique(!depots.getDepotFrs(), "Dépot [" + depots.getCode() + "] est un dépot fournisseur");

        Boolean existeTransfertNonValide = factureBTRepository.existsByCoddepAndDeptrAndValideFalseAndCodAnnulIsNull(dto.getSourceID(), dto.getDestinationID());
        Preconditions.checkBusinessLogique(!existeTransfertNonValide, "existe.transfert.non.validé");
        log.debug("getRetourPerime {}", dto.getPerime());
//         if (requestTransfert.equals("true")) {
//              Preconditions.checkBusinessLogique(!dto.getRelativesBons().isEmpty(), "relatifBon-transfert-non-Validé");}
        if (Boolean.TRUE.equals(dto.getPerime())) {
            Preconditions.checkBusinessLogique(Boolean.TRUE.equals(depotd.getPerime()), "depot-not-perime");

            // test si tout les dates sont prochenement perimées ou non moved
            dto.getDetails().stream()
                    .filter(x -> !x.getPreemptionDate().isBefore(LocalDate.now().plusDays(Long.parseLong(EXPIRATION_DATE_INTERVAL))) && Boolean.FALSE.equals(x.getNonMoved()))
                    .findFirst()
                    .ifPresent(elt -> {
                        throw new IllegalBusinessLogiqueException("article-avecDataNonPerime", new Throwable(elt.getCodeSaisi()));
                    });
        }
        if (Boolean.FALSE.equals(dto.getPerime())) {
            Preconditions.checkBusinessLogique(Boolean.FALSE.equals(depotd.getPerime()), "depot-not-perime");
        }
        FactureBT bonTransferInterDep = FactureBTFactory.factureBTDTOToFactureBT(dto);
        bonTransferInterDep.setValide(false);
        String numbon = paramService.getcompteur(dto.getCategDepot(), TypeBonEnum.BT);
        bonTransferInterDep.setNumbon(numbon);

        List<Integer> codArticles = dto.getDetails().stream().map(item -> item.getArticleID()).collect(toList());
        List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(codArticles);
        log.debug("articleFindbyListCode  {}", articles);
        // checking for inventory
        List<ArticleDTO> articleUnderInventory = inventaireService.checkOpenInventoryByListArticleAndCodeDep(codArticles, dto.getDestinationID());
        Preconditions.checkBusinessLogique(articleUnderInventory.isEmpty(), "article-under-inventory", articleUnderInventory.stream().map(ArticleDTO::getCodeSaisi).collect(toList()).toString());

        List<Depsto> depstos = stockService.findByCodartInAndCoddepAndQteGreaterThan(codArticles, bonTransferInterDep.getCoddep(), BigDecimal.ZERO);
        log.debug("liste of depstos to be processed  {}", depstos);
        List<Depsto> newDepstos = new ArrayList(); //the new stock to be created as a result of this transfert.
        List<MvtStoBT> detailsFactureBT = new ArrayList();
        String numordre1 = "0001";
        for (MvtstoBTDTO mvtstoBTDTO : dto.getDetails()) {
            MvtStoBT detailFacture = MvtstoBTFactory.mvtstoBTDTOToMvtstoBT(mvtstoBTDTO);
            /*SET entete ds detail*/
            detailFacture.setFactureBT(bonTransferInterDep);

            detailFacture.setCodart(mvtstoBTDTO.getArticleID());
            detailFacture.setNumbon(numbon);
            detailFacture.setNumordre(numordre1);

            numordre1 = Helper.IncrementString(numordre1, 4);
            detailsFactureBT.add(detailFacture);

            log.debug("*** Begin treating detail {} ****", detailFacture);

            List<DetailMvtSto> detailMvtSto = stockService.GestionDetailFacture(depstos, detailFacture, DetailMvtStoBT.class, Boolean.TRUE.equals(dto.getPerime()));

            List<DetailMvtStoBT> detailMvtStoBTs = new ArrayList<>();
            detailMvtSto.forEach(item -> {
                DetailMvtStoBT detailMvtStoBT = (DetailMvtStoBT) item;
                detailMvtStoBT.setMvtStoBT(detailFacture);

                detailMvtStoBTs.add(detailMvtStoBT);

                //set tva
                Depsto newDepsto = new Depsto(detailMvtStoBT);

                newDepsto.setCoddep(dto.getDestinationID());
                newDepsto.setCodart(detailFacture.getCodart());
                newDepsto.setCategDepot(detailFacture.getCategDepot());
                newDepsto.setCategDepot(detailFacture.getCategDepot());
                newDepsto.setNumBon(detailFacture.getNumbon());
                newDepsto.setNumBonOrigin(detailMvtStoBT.getNumBonOrigin());
                Optional<Depsto> matchedDepsto = depstos.stream().filter(dep -> dep.getCode().equals(detailMvtStoBT.getCodeDepsto())).findFirst();
                if (matchedDepsto.isPresent()) {
                    newDepsto.setMemo(matchedDepsto.get().getMemo());
                }
                newDepstos.add(newDepsto);

            });
            detailFacture.setDetailMvtStoBTList(detailMvtStoBTs);

        }
        BigDecimal totaleMnt = detailsFactureBT.stream().map(item -> item.getPriuni().multiply(item.getQuantite())).reduce(BigDecimal.ZERO, (p, q) -> p.add(q));

        bonTransferInterDep.setMntbon(totaleMnt);

        bonTransferInterDep.setDetailFactureBTCollection(detailsFactureBT);
        stockService.saveDepsto(newDepstos);

        if (validationTransfertAuto.equalsIgnoreCase("true")) {
            bonTransferInterDep.setConforme(Boolean.TRUE);
            bonTransferInterDep.setValide(Boolean.TRUE);
            bonTransferInterDep.setUserValidate(SecurityContextHolder.getContext().getAuthentication().getName());
            bonTransferInterDep.setDateValidate(LocalDateTime.now());
        }

        factureBTRepository.save(bonTransferInterDep);

        if (!dto.getRelativesBons().isEmpty()) {
            List<Integer> listeCodeDTR = dto.getRelativesBons().stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());

            List<DetailDemandePrTrDTO> detailTransfert = getDetailOfTransferttOrders(listeCodeDTR);
            processTransfertOrders(bonTransferInterDep, detailTransfert);

        }

        paramService.updateCompteurPharmacie(bonTransferInterDep.getCategDepot(), TypeBonEnum.BT);
        return BonTransfertInterDepotFactory.factureBTToFactureBTDTO(bonTransferInterDep);
    }

    @Transactional
    public List<DetailDemandePrTrDTO> getDetailOfTransferttOrders(List<Integer> TransfertOrdersCodes) {

        List<DemandeTrDTO> listeDTR = demandeServiceClient.findListDemandeTr(TransfertOrdersCodes, LocaleContextHolder.getLocale().getLanguage(), false);

        for (DemandeTrDTO demandetr : listeDTR) {
            Preconditions.checkBusinessLogique(demandetr.getDateValidation() != null && Boolean.TRUE.equals(demandetr.getAccepted()), "demande.not.valid");
            Preconditions.checkBusinessLogique(demandetr.getDateArchive() == null, "demande.archived");
        }

        Preconditions.checkBusinessLogique(listeDTR.size() == TransfertOrdersCodes.size(), "reception.add.missing-DTR");
        List<TransfertDetailDTR> recivedDetailDTR = transfertDetailDTRService.findByCodesDtrIn(TransfertOrdersCodes);

        List<DetailDemandePrTrDTO> listDetailTR = listeDTR.stream()
                .sorted((a, b) -> a.getDateCreate().compareTo(b.getDateCreate())).peek(item -> log.debug("sizedetail----{}", item.getDetailsDemande().size()))
                .flatMap(e -> e.getDetailsDemande().stream())
                .map(detailDTR -> {
                    Integer recivedOteDTR = recivedDetailDTR.stream()
                            .filter(item -> item.getPk().getCodedetailDTR().equals(detailDTR.getCode()))
                            .map(TransfertDetailDTR::getQuantiteTransferred)
                            .collect(Collectors.summingInt(item -> item.intValue()));

                    Integer qterestant = recivedOteDTR != null ? detailDTR.getQuantiteValide() - recivedOteDTR : detailDTR.getQuantiteValide();
                    Preconditions.checkBusinessLogique(qterestant >= 0, "reception.add.recived-DTR");
                    detailDTR.setQuantiteRestante(qterestant);

                    return detailDTR;
                })
                .collect(Collectors.toList());
        return listDetailTR;
    }

    @Transactional
    public void processTransfertOrders(FactureBT factureBT, List<DetailDemandePrTrDTO> transfertOrdersDetails) {

        log.debug("processing Prelevment orders {}", transfertOrdersDetails);
        Map<Integer, Integer> articlesGroupeByCodart = factureBT.getDetailFactureBTCollection()
                .stream()
                .collect(Collectors.groupingBy(item -> item.getCodart(), Collectors.summingInt(item -> item.getQuantite().intValue())));

        List<TransfertDetailDTR> transfertDetailDTRs = new ArrayList();

        for (Map.Entry<Integer, Integer> mvtStoBT : articlesGroupeByCodart.entrySet()) {
            BigDecimal reste = BigDecimal.valueOf(mvtStoBT.getValue());
            BigDecimal min;
            Boolean existsAtLeastOnce = false;
            for (DetailDemandePrTrDTO detailDTR : transfertOrdersDetails) {
                if (detailDTR.getArticle().getCode().equals(mvtStoBT.getKey())) {
                    if (reste.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal qteRest = new BigDecimal(detailDTR.getQuantiteRestante());
                        min = qteRest.min(reste);
                        detailDTR.setQuantiteRestante(qteRest.subtract(min).intValue());
                        reste = reste.subtract(min);

                        TransfertDetailDTRPK PK = new TransfertDetailDTRPK(detailDTR.getCode());

                        PK.setCodeTransfert(factureBT.getNumbon());
                        transfertDetailDTRs.add(new TransfertDetailDTR(PK, detailDTR.getCodeDemande(), min, factureBT));
                        existsAtLeastOnce = true;
                    } else {
                        break;

                    }

                }
            }
            Preconditions.checkBusinessLogique(existsAtLeastOnce, "reception.add.detailDTR-missing", mvtStoBT.getKey().toString());
        }
        transfertDetailDTRService.save(transfertDetailDTRs);

        Map<Integer, Optional<DetailDemandePrTrDTO>> QteResteValidByDTR = transfertOrdersDetails.stream()
                .collect(groupingBy(DetailDemandePrTrDTO::getCodeDemande, Collectors.reducing((a, b) -> {
                    a.setQuantiteRestante(a.getQuantiteRestante() + b.getQuantiteRestante());
                    a.setQuantiteValide(a.getQuantiteValide() + b.getQuantiteValide());
                    return a;
                })));
        List<EtatDTR> etatDTRs = new ArrayList();

        QteResteValidByDTR.forEach((key, value) -> {
            DetailDemandePrTrDTO mergedDetailDPR = value.get();
            if (mergedDetailDPR.getQuantiteRestante() == 0) {

                etatDTRs.add(new EtatDTR(value.get().getCodeDemande(), TRANSFERRED));

            } else if (mergedDetailDPR.getQuantiteRestante().equals(mergedDetailDPR.getQuantiteValide())) {

                etatDTRs.add(new EtatDTR(value.get().getCodeDemande(), NOT_TRANSFERRED));

            } else if (mergedDetailDPR.getQuantiteRestante().compareTo(mergedDetailDPR.getQuantiteValide()) < 0) {

                etatDTRs.add(new EtatDTR(value.get().getCodeDemande(), PARTIALLY_TRANSFERRED));
            }

        });
        etatDTRService.save(etatDTRs);

    }

    public FactureBT addBonTransferInterDepotDepotFrs(FactureBA factureBA, DepotDTO depots, DepotDTO depotd) {

        Preconditions.checkBusinessLogique(!depots.getDesignation().equals("depot.deleted"), "Depot [" + depots.getDesignation() + "] introuvable");
        Preconditions.checkBusinessLogique(!depots.getDepotFrs(), "Dépot [" + depots.getCode() + "] est un dépot fournisseur");

        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + depotd.getDesignation() + "] introuvable");

        FactureBT bonTransferInterDep = FactureBTFactory.factureToFactureBT(factureBA, depots, depotd);
        String numbon = paramService.getcompteur(factureBA.getCategDepot(), TypeBonEnum.BT);
        bonTransferInterDep.setNumbon(numbon);

//        List<Integer> codArticles = facture.getMvtstoCollection().stream().map(item -> item.getMvtstoPK().getCodart()).collect(toList());
        // checking for inventory
//        List<ArticleDTO> articleUnderInventoryDepotS = inventaireService.checkOpenInventoryByListArticleAndCodeDep(codArticles, depots.getCode());
//        Preconditions.checkBusinessLogique(articleUnderInventoryDepotS.isEmpty(), "article-under-inventory", articleUnderInventoryDepotS.stream().map(ArticleDTO::getCodeSaisi).collect(toList()).toString());
//        List<ArticleDTO> articleUnderInventoryDepotD = inventaireService.checkOpenInventoryByListArticleAndCodeDep(codArticles, depotd.getCode());
//        Preconditions.checkBusinessLogique(articleUnderInventoryDepotD.isEmpty(), "article-under-inventory", articleUnderInventoryDepotD.stream().map(ArticleDTO::getCodeSaisi).collect(toList()).toString());
        List<MvtStoBT> detailsFactureBT = new ArrayList();
        String numordre = "0001";
        for (MvtStoBA mvtstoBA : factureBA.getDetailFactureBACollection()) {
            MvtStoBT detailFacture = MvtstoBTFactory.mvtstoToMvtstoBT(mvtstoBA);
            detailFacture.setFactureBT(bonTransferInterDep);

            detailFacture.setCodart(mvtstoBA.getPk().getCodart());
            detailFacture.setNumbon(numbon);
            detailFacture.setNumordre(numordre);
            numordre = Helper.IncrementString(numordre, 4);
            detailsFactureBT.add(detailFacture);
        }
        BigDecimal totaleMnt = detailsFactureBT.stream().map(item -> item.getPriuni().multiply(item.getQuantite())).reduce(BigDecimal.ZERO, (p, q) -> p.add(q));

        bonTransferInterDep.setMntbon(totaleMnt);
        bonTransferInterDep.setDetailFactureBTCollection(detailsFactureBT);
        bonTransferInterDep = factureBTRepository.save(bonTransferInterDep);
        paramService.updateCompteurPharmacie(bonTransferInterDep.getCategDepot(), TypeBonEnum.BT);
        return bonTransferInterDep;
    }

    public FactureBT cancelBonTransferInterDepotDepotFrs(String numBon) {

        log.debug("cancelling new FactureBA {}", numBon);
        FactureBT factureBT = factureBTRepository.findOne(numBon);
        Preconditions.checkBusinessLogique(factureBT != null, "reception.delete.reception-not-found");
        Preconditions.checkBusinessLogique(factureBT.getCodAnnul() == null, "reception.delete.reception-canceld");
        factureBT.setCodAnnul(SecurityContextHolder.getContext().getAuthentication().getName());
        factureBT.setDatAnnul(LocalDateTime.now());
        factureBT = factureBTRepository.save(factureBT);
        return factureBT;
    }

    public Boolean cancelBonTransferInterDepotDepotFrsPermanent(String numBon) {

        log.debug("cancelling new FactureBA {}", numBon);
        FactureBT factureBT = factureBTRepository.findOne(numBon);
        Preconditions.checkBusinessLogique(factureBT != null, "reception.delete.reception-not-found");
        Preconditions.checkBusinessLogique(factureBT.getCodAnnul() == null, "reception.delete.reception-canceld");
        factureBTRepository.delete(factureBT);
//        paramService.updateCompteurPharmacie(CategorieDepotEnum.UU, TypeBonEnum.BT, factureBTRepository.findMaxNumbonByCategDepot(CategorieDepotEnum.UU));
//        paramService.updateCompteurPharmacie(CategorieDepotEnum.PH, TypeBonEnum.BT, factureBTRepository.findMaxNumbonByCategDepot(CategorieDepotEnum.PH));
        return Boolean.TRUE;
    }

    public List<FactureBT> findByDatbonBetween(String typBon, Date du, Date au, boolean interdepot, boolean stup) throws ParseException {
        return factureBTRepository.findByTypbonAndDatbonBetweenAndInterdepotOrderByDatbonDesc(typBon, du, au, interdepot);
    }

    public List<MvtStoBT> findDetails(String numBon) {
        return mvtstoBTRepository.findByNumbon(numBon);
    }

   

    public List<QteMouvement> findQuantiteMouvement(String famart, String coddep, Date datedeb, Date datefin) {

        return null;
    }

    public List<QteMouvement> findQuantiteMouvementSortie(String famart, String coddep, Date datedeb, Date datefin) {
//        if (famart.equalsIgnoreCase("tous")) {
//            return mvtstoBTRepository.findQuantiteMouvementSortieTous(coddep, datedeb, datefin);
//        } else {
//            return mvtstoBTRepository.findQuantiteMouvementSortie(famart, coddep, datedeb, datefin);
//        }
        return null;
    }

    public Integer findTotalMouvement(String codart, String coddep, Date datedeb, Date datefin) {

//        return mvtstoBTRepository.findTotalMouvement(codart, coddep, datedeb, datefin);
        return null;
    }

    public Integer findTotalMouvementSortie(String codart, String coddep, Date datedeb, Date datefin) {
//        return mvtstoBTRepository.findTotalMouvementSortie(codart, coddep, datedeb, datefin);
        return null;
    }

    @Transactional(readOnly = true)
    public BonTransferInterDepotDTO findOne(String numBon) {
        log.debug("Request to get Facture: {}", numBon);
        FactureBT facture = factureBTRepository.findOne(numBon);

        BonTransferInterDepotDTO dto = BonTransfertInterDepotFactory.factureBTToFactureBTDTO(facture);
        dto.setDetails(MvtstoBTFactory.mvtstoBTToMvtstoBTDTOs(facture.getDetailFactureBTCollection()));
        List<Integer> codeDepots = new ArrayList<>();
        codeDepots.add(dto.getDestinationID());
        codeDepots.add(dto.getSourceID());
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots);
        listDepot.stream().forEach(item -> {
            if (item.getCode().equals(dto.getSourceID())) {
                dto.setSourceDesignation(item.getDesignation());
            } else {
                dto.setDestinationDesignation(item.getDesignation());
            }
        });
        List<Integer> codeUnites = new ArrayList<>();
        facture.getDetailFactureBTCollection().forEach(x -> {
            codeUnites.add(x.getUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        dto.getDetails().forEach((mvtstoDTO) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(mvtstoDTO.getUnityID())).findFirst();
            if (unite.isPresent()) {
                mvtstoDTO.setUnityDesignation(unite.get().getDesignation());
            }
        });

        String language = LocaleContextHolder.getLocale().getLanguage();

        List<TransfertDetailDTR> listtransfert = transfertDetailDTRRepository.findByPk_CodeTransfert(numBon);
        List<Integer> codeDemande = listtransfert.stream().map(iteme -> iteme.getCodeDTR()).distinct().collect(Collectors.toList());

        List<DemandeTrDTO> listDemandeTrs = demandeServiceClient.findListDemandeTr(codeDemande, language, true);
        dto.setListDemandeTrs(listDemandeTrs);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<BonTransferInterDepotDTO> findAll(CategorieDepotEnum categ, LocalDateTime fromDate,
            LocalDateTime toDate, Boolean interdepot, Boolean avoirTransfert,
            List<SatisfactionEnum> satisfactions, Integer depotID_src, Integer depotID_des, Boolean deleted, Boolean perime, Boolean validated, Boolean conforme) throws ParseException {
        log.debug("Request to get All FactureBTs");
        QFactureBT _FactureBT = QFactureBT.factureBT;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_FactureBT.typbon.eq(TypeBonEnum.BT))
                .optionalAnd(categ, () -> _FactureBT.categDepot.eq(categ))
                .optionalAnd(fromDate, () -> _FactureBT.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FactureBT.datbon.loe(toDate))
                .optionalAnd(interdepot, () -> _FactureBT.interdepot.eq(interdepot))
                .optionalAnd(satisfactions, () -> _FactureBT.satisf.in(satisfactions))
                .optionalAnd(depotID_src, () -> _FactureBT.coddep.eq(depotID_src))
                .optionalAnd(depotID_des, () -> _FactureBT.deptr.eq(depotID_des))
                .optionalAnd(avoirTransfert, () -> _FactureBT.avoirTransfert.eq(avoirTransfert))
                .optionalAnd(validated, () -> _FactureBT.valide.eq(validated))
                .optionalAnd(conforme, () -> _FactureBT.conforme.eq(conforme))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _FactureBT.datAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _FactureBT.datAnnul.isNull())
                .optionalAnd(perime, () -> _FactureBT.perime.eq(perime));
        Set<FactureBT> listFactureBTs = new HashSet((Collection) factureBTRepository.findAll(builder));

        Set<Integer> codeDepots = new HashSet();
        List<String> numbons = new ArrayList<>();
        listFactureBTs.forEach(x -> {
            codeDepots.add(x.getCoddep());
            codeDepots.add(x.getDeptr());
            numbons.add(x.getNumbon());
        });

        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots);

        Set<Integer> demandeTransfertsIDs = transfertDetailDTRRepository.findByPk_CodeTransfertIn(numbons)
                .stream()
                .map(TransfertDetailDTR::getCodeDTR)
                .collect(Collectors.toSet());
        List<DemandeTrDTO> listDemandeTrs = demandeServiceClient.findListDemandeTr(demandeTransfertsIDs, LocaleContextHolder.getLocaleContext().getLocale().getLanguage(), true);

        List<BonTransferInterDepotDTO> listFactureBTDTO = listFactureBTs.stream().map(transfert -> {
            BonTransferInterDepotDTO dto = BonTransfertInterDepotFactory.factureBTToFactureBTDTO(transfert);
            listDepot.stream().filter(x -> x.getCode().equals(transfert.getCoddep()) || x.getCode().equals(transfert.getDeptr())).forEach(filtredItem -> {
                if (filtredItem.getCode().equals(transfert.getCoddep())) {
                    dto.setSourceDesignation(filtredItem.getDesignation());
                    dto.setSourceCodeSaisi(filtredItem.getCodeSaisi());
                } else {
                    dto.setDestinationDesignation(filtredItem.getDesignation());
                    dto.setDestinationCodeSaisi(filtredItem.getCodeSaisi());
                }
            });
// return les DemandetrDTO bour chaque facture : filtrer par DetailTransfertDTR
            List<DemandeTrDTO> listtransfertFacture = listDemandeTrs.stream().
                    filter(demandeTransfer -> transfert.getDetailTransfertDTR().stream().filter(elt -> elt.getCodeDTR().equals(demandeTransfer.getCode())).findFirst().isPresent())
                    .collect(toList());

            if (!listtransfertFacture.isEmpty()) {

                dto.setListDemandeTrs(listtransfertFacture);
            }

            return dto;
        }).collect(toList());
        return listFactureBTDTO;
    }

    public void save(FactureBT factureBT) {
        factureBTRepository.save(factureBT);
    }
}
