package com.csys.pharmacie.achat.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.BaseTvaReceptionTemporaire;

import com.csys.pharmacie.achat.domain.DetailReceptionTemporaire;
import com.csys.pharmacie.achat.domain.EtatReceptionCA;
import com.csys.pharmacie.achat.domain.QReceptionTemporaire;
import com.csys.pharmacie.achat.domain.Receiving;
import com.csys.pharmacie.achat.domain.ReceptionTemporaire;
import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCa;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.BaseTvaReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatModeReglementDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.DetailCommandeAchatDTO;
import com.csys.pharmacie.achat.dto.DetailReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.EmplacementDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.ReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.factory.DetailReceptionTemporaireFactory;
import com.csys.pharmacie.achat.factory.ReceptionTemporaireFactory;
import com.csys.pharmacie.achat.repository.ReceptionTemporaireRepository;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import static com.csys.pharmacie.helper.CategorieDepotEnum.PH;
import static com.csys.pharmacie.helper.CategorieDepotEnum.UU;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.NOT_RECEIVED;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.PARTIALLY_RECIVED;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.RECEIVED;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.parametrage.repository.ParamService;
import java.time.LocalDateTime;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

@Service
@Transactional
public class ReceptionTemporaireService {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }
    private final Logger log = LoggerFactory.getLogger(ReceptionTemporaireService.class);

    private final ReceptionTemporaireRepository receptiontemporaireRepository;
    private final ReceivingService receivingService;
    private final ParamService paramService;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final InventaireService inventaireService;
    private final DemandeServiceClient demandeServiceClient;
    private final FactureBAService factureBAService;
    private final EtatReceptionCAService etatReceptionCAService;

    public ReceptionTemporaireService(ReceptionTemporaireRepository receptiontemporaireRepository, ReceivingService receivingService, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient, InventaireService inventaireService, DemandeServiceClient demandeServiceClient,@Lazy FactureBAService factureBAService, EtatReceptionCAService etatReceptionCAService) {
        this.receptiontemporaireRepository = receptiontemporaireRepository;
        this.receivingService = receivingService;
        this.paramService = paramService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.inventaireService = inventaireService;
        this.demandeServiceClient = demandeServiceClient;
        this.factureBAService = factureBAService;
        this.etatReceptionCAService = etatReceptionCAService;
    }

    public ReceptionTemporaireDTO save(BonRecepDTO dto) {
        ReceptionTemporaireDTO receptiontemporaireDTO = ReceptionTemporaireFactory.BonReceptionDTOToReceptiontTemporaire(dto);
        log.debug("Request to save ReceptionTemporaire: {}", receptiontemporaireDTO);
        receptiontemporaireDTO.setIsTemporaire(true);
        Receiving receiving = receivingService.findReceiving(receptiontemporaireDTO.getReceivingCode());
        Preconditions.checkBusinessLogique(receiving != null, "reception.add.missing-receiving");
        Preconditions.checkBusinessLogique(receiving.getDateAnnule() == null, "reception.add.canceld-receiving");
        Preconditions.checkBusinessLogique(receiving.getDateValidate() == null, "reception.add.validated-receiving", receptiontemporaireDTO.getReceivingCode().toString());

        receiving.setDateValidate(LocalDateTime.now());
        receiving.setUserValidate(SecurityContextHolder.getContext().getAuthentication().getName());
        // fournisseur
        FournisseurDTO fourniss = paramAchatServiceClient.findFournisseurByCode(receptiontemporaireDTO.getCodfrs());
        log.debug("fourniss est {}", fourniss);
        Preconditions.checkBusinessLogique(!fourniss.getDesignation().equals("fournisseur.deleted"), "Fournisseur  est introuvable", receptiontemporaireDTO.getCodfrs());
        //depot
        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(receptiontemporaireDTO.getCoddep());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + receptiontemporaireDTO.getCoddep() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getCode() + "] est un dépot fournisseur");

        String numBon = paramService.getcompteur(receptiontemporaireDTO.getCategDepot(), TypeBonEnum.BAT);
        log.debug("categdepot est {}", receptiontemporaireDTO.getCategDepot());
        log.debug("numbon 1 est ....  {}", numBon);

        receptiontemporaireDTO.setNumbon(numBon);
        ReceptionTemporaire receptionTemporaire = ReceptionTemporaireFactory.receptiontemporaireDTOToReceptionTemporaire(receptiontemporaireDTO);
        receptionTemporaire.setCodfrs(fourniss.getCode());
        receptionTemporaire.setCoddep(depotd.getCode());
        receptionTemporaire.setTypbon(TypeBonEnum.BAT);
        receptionTemporaire.setNumbon(numBon);
        log.debug("receiving est ....  {}", receiving.getNumaffiche());
        // reception.setDateDebutExoneration(fourniss.getDateDebutExenoration());
        // reception.setDateDebutExoneration(fourniss.getDateFinExenoration();
        // documents 
        // f.setPiecesJointes(receptiontemporaireDTO.getAttachedFilesIDs().stream().map(id -> new PieceJointeReception(id.toString(), f)).collect(toList()));
        //details
        Set<Integer> codesEmplacements = new HashSet();
        Set<Integer> codeArticles = new HashSet();

        receptiontemporaireDTO.getDetailReceptionTempraireDTO().forEach(detail -> {
            codeArticles.add(detail.getRefArt());
            if (detail.getCodeEmplacement() != null) {
                codesEmplacements.add(detail.getCodeEmplacement());
            }
        });
        log.debug("codeArticles sont {}", codeArticles);
        List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(receptiontemporaireDTO.getCategDepot(), codeArticles.toArray(new Integer[codeArticles.size()]));
        List<EmplacementDTO> emplacements = receptiontemporaireDTO.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(codesEmplacements,null) : new ArrayList();
        if (receptiontemporaireDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            Preconditions.checkBusinessLogique(emplacements != null, "error-emplacaments");
        }
        // check for inventory 
        List<Integer> categArticleIDs = articles.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, receptiontemporaireDTO.getCoddep());
        Preconditions.checkBusinessLogique(categArticleUnderInventory.isEmpty(), "article-under-inventory");
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = articles.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }
        log.debug("after inventory");
        List<DetailReceptionTemporaire> details = new ArrayList<>();
        for (DetailReceptionTemporaireDTO detailReceptionTemporaireDTO : receptiontemporaireDTO.getDetailReceptionTempraireDTO()) {
            if (detailReceptionTemporaireDTO.getQuantite().compareTo(BigDecimal.ZERO) != 0) {
                ArticleDTO matchingItem = articles.stream().filter(article -> article.getCode().equals(detailReceptionTemporaireDTO.getRefArt()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("reception.add.missing-article", new Throwable(detailReceptionTemporaireDTO.getCodeSaisi())));

                log.debug(" matchingItem{}", matchingItem);

//                log.debug(" le priuni du detailReception {} est {}et sa baseTva est{} :", detailReceptionTemporaireDTO.getCodeSaisi(), detailReceptionTemporaireDTO.getPriuni().toString(), detailReceptionTemporaireDTO.getBaseTva());
                Preconditions.checkBusinessLogique((detailReceptionTemporaireDTO.getPriuni().compareTo(BigDecimal.ZERO) == 1 && detailReceptionTemporaireDTO.getBaseTva().compareTo(BigDecimal.ZERO) == 0) || (detailReceptionTemporaireDTO.getPriuni().compareTo(BigDecimal.ZERO) == 0), "reception.add.error-baseTva", detailReceptionTemporaireDTO.getCodeSaisi());
                log.debug(" code saisi detail  {}", detailReceptionTemporaireDTO.getCodeSaisi());
                log.debug(" details dto est ....  {}", detailReceptionTemporaireDTO);

                DetailReceptionTemporaire detailReceptionTemporaire = DetailReceptionTemporaireFactory.detailReceptionTemporaireDTOToDetailReceptionTemporaire(detailReceptionTemporaireDTO);
                log.debug("categ dto est {} et entity est {}", detailReceptionTemporaireDTO.getCategDepot(), detailReceptionTemporaire.getCategDepot());
                log.debug(" detail apres factory est ....  {}", detailReceptionTemporaire);

                detailReceptionTemporaire.setUnite(matchingItem.getCodeUnite());
                log.debug(" detail Unite  est....  {}", matchingItem.getCodeUnite());
                if (receptiontemporaireDTO.getCategDepot().equals(PH)) {

                    Preconditions.checkBusinessLogique(detailReceptionTemporaireDTO.getSellingPrice() != null, "reception.add.missing-selling-price", detailReceptionTemporaireDTO.getCodeSaisi());
                    detailReceptionTemporaire.setSellingPrice(detailReceptionTemporaireDTO.getSellingPrice());
                }
                if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
                    detailReceptionTemporaire.setDesart(detailReceptionTemporaireDTO.getDesartSec());
                    detailReceptionTemporaire.setDesArtSec(detailReceptionTemporaireDTO.getDesart());
                } else {
                    detailReceptionTemporaire.setDesart(detailReceptionTemporaireDTO.getDesart());
                    detailReceptionTemporaire.setDesArtSec(detailReceptionTemporaireDTO.getDesartSec());
                }
                if (receptiontemporaireDTO.getCategDepot().equals(UU)) {
                    detailReceptionTemporaire.setIsPrixReference(detailReceptionTemporaireDTO.getIsPrixRef());
                }
                detailReceptionTemporaire.setReception(receptionTemporaire);
                details.add(detailReceptionTemporaire);

            }
        }

        receptionTemporaire.setDetailReceptionTempraire(details);

        //calcul tva 
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();

        log.debug("listTvas{}", listTvas.toString());
        receptionTemporaire.calcul(listTvas);
        log.debug("listTvas{}", listTvas.toString());

        receptionTemporaire.setcodeReceiving(receiving.getCode());

        //gestion des commandes
        List<ReceptionTemporaireDetailCa> listeReceptionTempDetailCa = managingPurchaseOrdersForReceptionTemp(receptiontemporaireDTO);
        for (ReceptionTemporaireDetailCa receptionTemp : listeReceptionTempDetailCa) {
            receptionTemp.setReceptionTemporaire1(receptionTemporaire);
        }

        log.debug("listeReceptionDetailCa sont {}", listeReceptionTempDetailCa.toString());

        // log.debug(" listeReceptionDetailCa ....  {}", listeReceptionDetailCa);
        //    listeReceptionDetailCa.forEach(elt -> elt.setReception(receptionTemporaire));
        receptionTemporaire.setRecivedDetailTempraireCA(listeReceptionTempDetailCa);

        List<CommandeAchatModeReglementDTO> commandeAhatModeReglements = demandeServiceClient.findListCommandeAchatModeReglementDTOByCodeCaIn(receptiontemporaireDTO.getPurchaseOrdersCodes());
        log.debug("commandeAhatModeReglements sont {}", commandeAhatModeReglements);
        Integer maxDelaiPaiement = commandeAhatModeReglements.stream().map(CommandeAchatModeReglementDTO::getDelaiPaiement).max(Integer::compare).get();
        log.debug("maxDelaiPaiement est {}", maxDelaiPaiement);
        receptionTemporaire.setMaxDelaiPaiement(maxDelaiPaiement);
        receptionTemporaire.setReceivingNumaffiche(receiving.getNumaffiche());
        receptionTemporaire.setIsTemporaire(true);
        receptionTemporaire = receptiontemporaireRepository.save(receptionTemporaire);
        log.debug("receptionTemporaire datbon est {}et numaffiche est {}", receptionTemporaire.getDatbon(), receptionTemporaire.getNumaffiche());
        paramService.updateCompteurPharmacie(receptionTemporaire.getCategDepot(), TypeBonEnum.BAT);

        ReceptionTemporaireDTO resultDTO = ReceptionTemporaireFactory.receptiontemporaireToReceptionTemporaireDTONotLazy(receptionTemporaire);

        return resultDTO;

    }

//    public ReceptionTemporaireDTO updateStatusRecptionTemporaire(ReceptionTemporaireDTO receptiontemporaireDTO) {
//        log.debug("Request to update ReceptionTemporaire: {}", receptiontemporaireDTO);
//        ReceptionTemporaire inBase = receptiontemporaireRepository.findOne(receptiontemporaireDTO.getNumbon());
//        Preconditions.checkBusinessLogique(inBase != null, "ReceptionTemporaire does not exist");
//        receptiontemporaireDTO.setIsValidated(true);
//        ReceptionTemporaireDTO result = save(receptiontemporaireDTO);
//        return result;
//    }
    @Transactional(readOnly = true)
    public ReceptionTemporaireDTO findOne(String id) {
        log.debug("Request to get ReceptionTemporaire: {}", id);
        ReceptionTemporaire receptionTemporaire = receptiontemporaireRepository.findOne(id);
        Preconditions.checkBusinessLogique(receptionTemporaire != null, "ReceptionTemporaire does not exist");
        log.debug("details sont {}", receptionTemporaire.getDetailReceptionTempraire());
        ReceptionTemporaireDTO dto = ReceptionTemporaireFactory.receptiontemporaireToReceptionTemporaireDTONotLazy(receptionTemporaire);
        dto.setFournisseur(paramAchatServiceClient.findFournisseurByCode(receptionTemporaire.getCodfrs()));

        List<Integer> listCodesCA = receptionTemporaire.getRecivedDetailTempraireCA().stream().map(item -> item.getReceptionTemporaireDetailCaPK().getCommandeAchat()).distinct().collect(Collectors.toList());
        //dto.setPurchaseOrders(demandeServiceClient.findListCommandeAchat(listCodesCA, LocaleContextHolder.getLocale().getLanguage()));
        dto.setPurchaseOrdersCodes(listCodesCA);

        List<Integer> codeUnites = new ArrayList();

        for (DetailReceptionTemporaireDTO detailDto : dto.getDetailReceptionTempraireDTO()) {

            log.debug("detail.getUnite(): {}", detailDto.getCodeUnite());
            codeUnites.add(detailDto.getCodeUnite());

        }

        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);

        for (DetailReceptionTemporaireDTO detailDto : dto.getDetailReceptionTempraireDTO()) {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(detailDto.getCodeUnite())).findFirst().orElse(null);
            com.google.common.base.Preconditions.checkArgument(unite != null, "missing-unity");

            detailDto.setUnitDesignation(unite.getDesignation());
        }

        if (receptionTemporaire.getBaseTvaReceptionList() != null) {
            log.debug("receptionTemporaire.getBaseTvaReceptionList {} ", receptionTemporaire.getBaseTvaReceptionList().toString());
            List<BaseTvaReceptionTemporaireDTO> basesTVA = new ArrayList<BaseTvaReceptionTemporaireDTO>();

            List<BaseTvaReceptionTemporaire> basetvareceptionlist = receptionTemporaire.getBaseTvaReceptionList();
            for (BaseTvaReceptionTemporaire baseTvaReception : basetvareceptionlist) {
                BaseTvaReceptionTemporaireDTO baseTvaReceptionDTO = new BaseTvaReceptionTemporaireDTO();
                baseTvaReceptionDTO.setBaseTva(baseTvaReception.getBaseTva());
                baseTvaReceptionDTO.setCodeTva(baseTvaReception.getCodeTva());
                baseTvaReceptionDTO.setMontantTva(baseTvaReception.getMontantTva());
                baseTvaReceptionDTO.setTauxTva(baseTvaReception.getTauxTva());
                baseTvaReceptionDTO.setBaseTvaGratuite(baseTvaReception.getBaseTvaGratuite());
                baseTvaReceptionDTO.setMontantTvaGratuite(baseTvaReception.getMontantTva());
                baseTvaReceptionDTO.setNumbon(receptionTemporaire.getNumbon());

                basesTVA.add(baseTvaReceptionDTO);
                log.debug("basesTVA {} ", basesTVA.toString());

            }
            dto.setReceptionBasesTVA(basesTVA);
            dto.setDesignationDepot(paramAchatServiceClient.findDepotByCode(dto.getCoddep()).getDesignation());
        }

        dto.setIsValidated(receptionTemporaire.isIsValidated());
        if(receptionTemporaire.getFactureBA()!=null )
        dto.setNumBonFactureBa(receptionTemporaire.getFactureBA().getNumbon());
        return dto;
    }

    @Transactional(readOnly = true)
    public ReceptionTemporaire findReceptionTemporaire(String id) {
        log.debug("Request to get ReceptionTemporaire: {}", id);
        ReceptionTemporaire receptiontemporaire = receptiontemporaireRepository.findOne(id);
        Preconditions.checkBusinessLogique(receptiontemporaire != null, "ReceptionTemporaire does not exist");
        return receptiontemporaire;
    }

    @Transactional(readOnly = true)
    public Collection<ReceptionTemporaireDTO> findAll(CategorieDepotEnum categDepot, LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted, String codeFournisseur) {
        log.debug("Request to get All ReceptionTemporaires");
        QReceptionTemporaire _QReceptionTemporaire = QReceptionTemporaire.receptionTemporaire;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(fromDate, () -> _QReceptionTemporaire.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _QReceptionTemporaire.datbon.loe(toDate))
                .optionalAnd(categDepot, () -> _QReceptionTemporaire.categDepot.eq(categDepot))
                .optionalAnd(codeFournisseur, () -> _QReceptionTemporaire.codfrs.eq(codeFournisseur))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _QReceptionTemporaire.datAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _QReceptionTemporaire.datAnnul.isNull());

        List<ReceptionTemporaire> result = (List<ReceptionTemporaire>) receptiontemporaireRepository.findAll(builder);

        //List<ReceptionTemporaire> result = receptiontemporaireRepository.findAll();
        log.debug("result {}",result);
        Collection< ReceptionTemporaireDTO> listeReceptionTempDTO = ReceptionTemporaireFactory.receptiontemporaireToReceptionTemporaireDTOs(result);
         if (!result.isEmpty()) {
        List<String> codesFrs = listeReceptionTempDTO.stream().map(item -> item.getCodfrs()).distinct().collect(Collectors.toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
        log.debug("Request to get codesFrs: {}", fournisseurs);
        listeReceptionTempDTO.forEach((receptionDTO) -> {
            Optional<FournisseurDTO> optFrs = fournisseurs.stream().filter(item -> item.getCode().equals(receptionDTO.getCodfrs())).findFirst();
            Preconditions.checkBusinessLogique(optFrs.isPresent(), "reception.get-all.missing-frs", receptionDTO.getCodfrs());
            receptionDTO.setFournisseur(optFrs.get());
           
        });
   
         }
        return listeReceptionTempDTO;
    }

    @Transactional
    public List<ReceptionTemporaireDetailCa> processPurchaseOrdersTemporaires(ReceptionTemporaireDTO receptionTempDTO, List<DetailCommandeAchatDTO> purchaseOrdersDetails, TypeBonEnum typeBon) {
        log.debug("processing Purchase orders {}", purchaseOrdersDetails);

        /**
         * processing the purchase order
         */
        Map<Integer, Integer> articlesGroupeByCodart = receptionTempDTO.getDetailReceptionTempraireDTO()
                .stream()
                .filter(item -> item.getPriuni().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.groupingBy(Item -> Item.getRefArt(), Collectors.summingInt(Item -> Item.getQuantite().intValue())));
        List<ReceptionTemporaireDetailCa> receptionDetailCAs = new ArrayList();

        for (Map.Entry<Integer, Integer> mvtstoBA : articlesGroupeByCodart.entrySet()) {
            BigDecimal reste = BigDecimal.valueOf(mvtstoBA.getValue());
            BigDecimal min;
            Boolean existsAtLeastOnce = false;
            for (DetailCommandeAchatDTO detailCA : purchaseOrdersDetails) {
                if (detailCA.getCodart().equals(mvtstoBA.getKey()) && detailCA.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0) {

                    if (reste.compareTo(BigDecimal.ZERO) > 0) {
                        min = detailCA.getQuantiteRestante().min(reste);
                        log.debug("min est  est {} ", min);
                        detailCA.setQuantiteRestante(detailCA.getQuantiteRestante().subtract(min));
                        reste = reste.subtract(min);
                        log.debug("reste est  est {} ", reste);
                        receptionDetailCAs.add(new ReceptionTemporaireDetailCa(receptionTempDTO.getNumbon(), detailCA.getCode(), detailCA.getCodart(), min, BigDecimal.ZERO));
                        existsAtLeastOnce = true;
                    } else {
                        break;
                    }
                }
            }
            log.debug("receptionDetailCAs from non gratuite est {} ", receptionDetailCAs);
//            Preconditions.checkBusinessLogique(existsAtLeastOnce, "reception.add.detail-ca-missing", mvtstoBA.getKey().toString());
        }

        //satisfactions des details receptions venant des commandes gratuites
        List<DetailReceptionTemporaireDTO> freeDetailReceptionsTemp = receptionTempDTO.getDetailReceptionTempraireDTO()
                .stream()
                .filter(item ->  (item.getPriuni().compareTo(BigDecimal.ZERO) == 0) && (item.getCodeCommande() != null))
                .collect(Collectors.toList());

        for (DetailReceptionTemporaireDTO freeDetailReceptionTemp : freeDetailReceptionsTemp) {
            log.debug("freeDetailReception est {}", freeDetailReceptionTemp);
            List<DetailCommandeAchatDTO> matchingDetailCAs = purchaseOrdersDetails
                    .stream()
                    .filter(item -> item.getCodart().equals(freeDetailReceptionTemp.getRefArt()))
                    .collect(Collectors.toList());
            log.debug("freeDetailCa");
            Preconditions.checkBusinessLogique(!(matchingDetailCAs.isEmpty()), "reception.add.detail-ca-missing", freeDetailReceptionTemp.getDesart());
//         
            BigDecimal reste = freeDetailReceptionTemp.getQuantite();
            BigDecimal minQteGratuite;
            for (DetailCommandeAchatDTO detailCA : matchingDetailCAs) {

                if (reste.compareTo(BigDecimal.ZERO) > 0) {
                    minQteGratuite = detailCA.getQuantiteGratuiteRestante().min(reste);
                    detailCA.setQuantiteGratuiteRestante(detailCA.getQuantiteGratuiteRestante().subtract(minQteGratuite));
                    reste = reste.subtract(minQteGratuite);
                    Optional<ReceptionTemporaireDetailCa> matchedReceptionCA = receptionDetailCAs
                            .stream()
                            .filter(elt -> elt.getReceptionTemporaireDetailCaPK().getArticle().equals(detailCA.getCodart())
                            && elt.getReceptionTemporaireDetailCaPK().getCommandeAchat().equals(detailCA.getCode()))
                            .findFirst();
                    if (!matchedReceptionCA.isPresent()) {

                        receptionDetailCAs.add(new ReceptionTemporaireDetailCa(receptionTempDTO.getNumbon(), detailCA.getCode(), detailCA.getCodart(), BigDecimal.ZERO, minQteGratuite));
                    } else {
                        matchedReceptionCA.get().setQuantiteGratuite(minQteGratuite);
                    }
                } else {
                    break;
                }
            }
        }

//            bonReception.setRecivedDetailCA(receptionDetailCAs);
        Map<Integer, Optional<DetailCommandeAchatDTO>> qteRestByCA = purchaseOrdersDetails.stream()
                .collect(groupingBy(DetailCommandeAchatDTO::getCode, Collectors.reducing((a, b) -> {
                    a.setQuantite(a.getQuantite().add(b.getQuantite()));
                    a.setQuantiteRestante(a.getQuantiteRestante().add(b.getQuantiteRestante()));
                    a.setQuantiteGratuite(a.getQuantiteGratuite().add(b.getQuantiteGratuite()));
                    a.setQuantiteGratuiteRestante(a.getQuantiteGratuiteRestante().add(b.getQuantiteGratuiteRestante()));
                    return a;
                })));
        List<EtatReceptionCA> etatsCA = new ArrayList();
        qteRestByCA.forEach((key, value) -> {
            DetailCommandeAchatDTO mergedDetailCA = value.get();
//                log.debug("mergedDetailCA.getQuantiteRestante()) est {} et mergedDetailCA.getQuantiteGratuiteRestante()est {} ", mergedDetailCA.getQuantiteRestante(), mergedDetailCA.getQuantiteGratuiteRestante());
//                log.debug(" mergedDetailCA.getQuantiteRestante().compareTo(BigDecimal.ZERO)==0  {}", mergedDetailCA.getQuantiteRestante().compareTo(BigDecimal.ZERO) == 0);
            if (mergedDetailCA.getQuantiteRestante().compareTo(BigDecimal.ZERO) == 0 && mergedDetailCA.getQuantiteGratuiteRestante().compareTo(BigDecimal.ZERO) == 0) {
                etatsCA.add(new EtatReceptionCA(key, RECEIVED));
            } else if (mergedDetailCA.getQuantiteRestante().equals(mergedDetailCA.getQuantite()) && mergedDetailCA.getQuantiteGratuiteRestante().equals(mergedDetailCA.getQuantiteGratuite())) {
                etatsCA.add(new EtatReceptionCA(key, NOT_RECEIVED));
            } else {
                etatsCA.add(new EtatReceptionCA(key, PARTIALLY_RECIVED));
            }
        }
        );
        etatReceptionCAService.save(etatsCA);

        return receptionDetailCAs;
    }

    @Transactional
    public List<ReceptionTemporaireDetailCa> managingPurchaseOrdersForReceptionTemp(ReceptionTemporaireDTO receptionTempDTO) {
        List<DetailCommandeAchatDTO> purchaseOrdersDetails = factureBAService.getDetailOfPurchaseOrders(receptionTempDTO.getPurchaseOrdersCodes());//detail commande +historique des reception et recep temp faite sur cette commande 100/recep 80 qterest =20
//        FactureBADTO factureBADTO=ReceptionTemporaireFactory.receptiontemporaireDTOTofactureDto(receptionTempDTO);//qteRest c1 20= recep temp --10 ==> qte rest :10
        List<ReceptionTemporaireDetailCa> listeReceptionDetailCa = processPurchaseOrdersTemporaires(receptionTempDTO, purchaseOrdersDetails, TypeBonEnum.valueOf(receptionTempDTO.getTypbon()));
        log.debug("ReceptionTemporaireDetailCa {} ", listeReceptionDetailCa);
        return listeReceptionDetailCa;

    }

    @Transactional(readOnly = true)
    public List<ReceptionTemporaireDTO> findAllReceptionTempraireNotValidated(CategorieDepotEnum categDepot, LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted, String codeFournisseur) {
        log.debug("Request to get All ReceptionTemporaires");
          log.debug("Request to get All ReceptionTemporaires");
        QReceptionTemporaire _QReceptionTemporaire = QReceptionTemporaire.receptionTemporaire;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(fromDate, () -> _QReceptionTemporaire.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _QReceptionTemporaire.datbon.loe(toDate))
                .optionalAnd(categDepot, () -> _QReceptionTemporaire.categDepot.eq(categDepot))
                .optionalAnd(codeFournisseur, () -> _QReceptionTemporaire.codfrs.eq(codeFournisseur))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _QReceptionTemporaire.datAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _QReceptionTemporaire.datAnnul.isNull());

        List<ReceptionTemporaire> result = (List<ReceptionTemporaire>) receptiontemporaireRepository.findAll(builder);
        Collection< ReceptionTemporaireDTO> receptionTEmpDTO = ReceptionTemporaireFactory.receptiontemporaireToReceptionTemporaireDTOs(result);
        List<ReceptionTemporaireDTO> receptionsTemporairseDto = new ArrayList<>();
        List<String> codesFrs = receptionTEmpDTO.stream().map(item -> item.getCodfrs()).distinct().collect(Collectors.toList());
        log.debug("Request to get codesFrs: {}", codesFrs);
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
        log.debug("Request to get codesFrs: {}", fournisseurs);
        receptionTEmpDTO.forEach((receptionDTO) -> {
            Optional<FournisseurDTO> optFrs = fournisseurs.stream().filter(item -> item.getCode().equals(receptionDTO.getCodfrs())).findFirst();
            log.debug("Request to get receptionDTO.getCodfrs(): {}", receptionDTO.getCodfrs());
            log.debug("Request to get receptionDTO.getCodfrs(): {}", receptionDTO.toString());
            log.debug("Request to get receptionDTO.getCodfrs(): {}", receptionDTO.getDateFrs());
            Preconditions.checkBusinessLogique(optFrs.isPresent(), "reception.get-all.missing-frs", receptionDTO.getCodfrs());
            receptionDTO.setFournisseur(optFrs.get());
            receptionsTemporairseDto.add(receptionDTO);
        });

        List<ReceptionTemporaireDTO> resultDto = receptionsTemporairseDto.stream().filter(reception -> !reception.isIsValidated()).collect(Collectors.toList());
        return resultDto;
    }

}
