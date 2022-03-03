/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.EtatReceptionCA;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.PieceJointeReception;
import com.csys.pharmacie.achat.domain.Receiving;
import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCa;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticleIMMODTO;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.BonRetourDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatModeReglementDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.DetailCommandeAchatDTO;
import com.csys.pharmacie.achat.dto.DetailReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.EmplacementDTO;
import com.csys.pharmacie.achat.dto.FactureBADTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.KafkaConsumerErrorDTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.achat.dto.ReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.factory.AvoirFournisseurFactory;
import com.csys.pharmacie.achat.factory.CommandeAchatFactory;
import com.csys.pharmacie.achat.factory.FactureBAFactory;
import com.csys.pharmacie.achat.factory.MvtstoBAFactory;
import com.csys.pharmacie.achat.repository.AvoirFournisseurRepository;
import com.csys.pharmacie.achat.repository.FactureBARepository;
import com.csys.pharmacie.achat.repository.ReceptionDetailCARepository;
import com.csys.pharmacie.achat.repository.RetourPerimeRepository;
import static com.csys.pharmacie.achat.service.FactureBABranchFacadeService.topicBonReceptionOnShelfManagement;
import com.csys.pharmacie.client.dto.ImmobilisationDTO;
import com.csys.pharmacie.client.dto.SiteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import static com.csys.pharmacie.helper.CategorieDepotEnum.IMMO;
import static com.csys.pharmacie.helper.CategorieDepotEnum.PH;
import com.csys.pharmacie.helper.EnumCrudMethod;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.PurchaseOrderReceptionState;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.NOT_RECEIVED;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.PARTIALLY_RECIVED;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.RECEIVED;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.repository.DepstoRepository;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.transfert.service.CliniSysService;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import com.csys.pharmacie.vente.service.PricingService;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrateur
 */
@Service
@Transactional
@EnableRetry
public class FactureBACompanyService extends FactureBAService {

    public final TopicPartitionOffsetService topicPartitionOffsetService;
//    @Value("${kafka.topic.transfert-management}")
//    private String topicTransferManagement;
//    @Value("${kafka.topic.transfert-bon-reception-onShelf-management}")
//    @Autowired
//    private Sender sender;
    @Autowired
    @Lazy
    TransfertCompanyFacadeService transfertCompanyService;
    private final KafkaConsumerErrorService kafkaConsumerErrorService;

    public FactureBACompanyService(TopicPartitionOffsetService topicPartitionOffsetService, KafkaConsumerErrorService kafkaConsumerErrorService, FactureBARepository factureBARepository, ReceptionDetailCARepository receptionDetailCARepository, ParamService paramService, FcptFrsPHService fcptFrsPHService, StockService stockService, DemandeServiceClient demandeServiceClient, ReceptionDetailCAService receptionDetailCAService, @Lazy ReceptionTemporaireDetailCaService receptionTemporaireDetailCaService, @Lazy PricingService pricingService, ParamAchatServiceClient paramAchatServiceClient, ReceivingService receivingService, EtatReceptionCAService etatReceptionCAService, ParamServiceClient paramServiceClient, MvtStoBAService mvtStoBAService, AjustementRetourFournisseurService ajustementRetourFournisseurService, RetourPerimeRepository retourPerimeRepository, @Lazy AvoirFournisseurFactory avoirFournisseurFactory, AvoirFournisseurRepository avoirfournisseurRepository, InventaireService inventaireService, ImmobilisationService immobilisationService, DepstoRepository depstoRepository, ReceptionTemporaireService receptionTemporaireService, MessageSource messages, CliniSysService cliniSysService) {
        super(factureBARepository, receptionDetailCARepository, paramService, fcptFrsPHService, stockService, demandeServiceClient, receptionDetailCAService, receptionTemporaireDetailCaService, pricingService, paramAchatServiceClient, receivingService, etatReceptionCAService, paramServiceClient, mvtStoBAService, ajustementRetourFournisseurService, retourPerimeRepository, avoirFournisseurFactory, avoirfournisseurRepository, inventaireService, immobilisationService, depstoRepository, receptionTemporaireService, messages, cliniSysService);
        this.topicPartitionOffsetService = topicPartitionOffsetService;
        this.kafkaConsumerErrorService = kafkaConsumerErrorService;
    }

    @Transactional
    public BonRecepDTO ajoutBonReception(BonRecepDTO bonReceptionDTO) {

        log.debug(" ajout reception in companyy");
        Receiving receiving = receivingService.findReceiving(bonReceptionDTO.getReceivingCode());
        Preconditions.checkBusinessLogique(receiving != null, "reception.add.missing-receiving");
        Preconditions.checkBusinessLogique(receiving.getDateAnnule() == null, "reception.add.canceld-receiving");
//        Preconditions.checkBusinessLogique(receiving.getDateValidate() == null, "reception.add.validated-receiving");
        ////code site destination///////
        SiteDTO siteBrancheDTO = paramServiceClient.findSiteByCode(bonReceptionDTO.getCodeSite());
        log.info("codeSiteBranch  est {}", siteBrancheDTO);
        Preconditions.checkBusinessLogique(siteBrancheDTO != null, "parametrage.clinique.error");

        //------- Controle commandes achats--------/
        Set<CommandeAchatDTO> listeCommandeAchat = demandeServiceClient.findListCommandeAchat(bonReceptionDTO.getPurchaseOrdersCodes(), LocaleContextHolder.getLocale().getLanguage());
        CommandeAchatDTO commandeAchat = listeCommandeAchat.stream().filter(item -> item.getUserArchive() != null).findFirst().orElse(null);
        Preconditions.checkBusinessLogique(commandeAchat == null, "receiving-with-archeived-purchase-order");

        String numBon = paramService.getcompteur(bonReceptionDTO.getCategDepot(), bonReceptionDTO.getTypeBon());
        //-------  get emplacements et articles and check items --------/
        Set<Integer> codesEmplacements = new HashSet();
        Set<Integer> codeArticles = new HashSet();

        bonReceptionDTO.getDetails().forEach(detail -> {
            codeArticles.add(detail.getRefArt());
            if (detail.getCodeEmplacement() != null) {
                codesEmplacements.add(detail.getCodeEmplacement());
            }
        });
        log.debug("codeArticles sont {}", codeArticles);

        //-------  check items--------/
        List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(bonReceptionDTO.getCategDepot(), codeArticles.toArray(new Integer[codeArticles.size()]));
//        Set<Integer> categArticleIDs = new HashSet();
        articles.forEach(item -> {
            Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(item.getAnnule()) && !(Boolean.TRUE.equals(item.getStopped())), "item.stopped", item.getCodeSaisi());
//            categArticleIDs.add(item.getCategorieArticle().getCode());
        });
        //-------  emplacements immo--------/  
        List<EmplacementDTO> emplacements = bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(codesEmplacements, siteBrancheDTO.getIpAdress()) : new ArrayList();
        if (bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            Preconditions.checkBusinessLogique(emplacements != null, "error-emplacaments");
        }

        FactureBA bonRecep = FactureBAFactory.factureBADTOTOFactureBA(bonReceptionDTO);
        bonRecep.setAutomatique(Boolean.FALSE);
        bonRecep.setNumbon(numBon);
        bonRecep.setCodeSite(siteBrancheDTO.getCode());

        //-------  fournisseur--------/
        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(bonReceptionDTO.getFournisseur().getCode());
        Preconditions.checkBusinessLogique(!fournisseurDTO.getDesignation().equals("fournisseur.deleted"), "Fournisseur avec code : " + bonReceptionDTO.getFournisseur().getCode() + " est introuvable");
        bonRecep.setCodfrs(fournisseurDTO.getCode());
        bonRecep.setDateDebutExoneration(fournisseurDTO.getDateDebutExenoration());
        bonRecep.setDateFinExenoration(fournisseurDTO.getDateFinExenoration());
        bonRecep.setIntegrer(Boolean.FALSE);
        //-------  depot--------/
        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(bonReceptionDTO.getCoddep());//depot entré par le client peut etre non principal 
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + bonReceptionDTO.getCoddep() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getCode() + "] est un dépot fournisseur");
        bonRecep.setCoddep(depotd.getCode());
        //-------  /////////////DETAILS//////////--------/    
        List<MvtStoBA> details = new ArrayList<>();
        String numordre1 = "0001";
        Date date = new Date(3000, 01, 01);
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper = new ListeImmobilisationDTOWrapper();
//        List<ImmobilisationDTO> immobilisationDTOs = new ArrayList();
//        List<Depsto> depstos = new ArrayList<>();
        //---------------- liste tva pour fixer tva en cas de fournisseur exonore et pour calcul base de tva et mnt tva 
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();

        TvaDTO tvaZeroDTO = !bonRecep.getFournisseurExonere() ? listTvas.stream().filter(elt -> (BigDecimal.ZERO).compareTo(elt.getValeur()) == 0)
                .findFirst()
                .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-vat")) : null;

        for (MvtstoBADTO detailReceptionDTO : bonReceptionDTO.getDetails()) {
            if (detailReceptionDTO.getQuantite().compareTo(BigDecimal.ZERO) != 0) {
                ArticleDTO matchingItem = articles.stream()
                        .filter(article -> article.getCode().equals(detailReceptionDTO.getRefArt()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("stock.find-all.missing-article", new Throwable(detailReceptionDTO.getCodeSaisi())));

                String ordre = "BA" + numordre1;

                MvtStoBA mvtstoBA = MvtstoBAFactory.MvtStoBADTOTOMvtstoBA(detailReceptionDTO, ordre, bonReceptionDTO, numBon);
                mvtstoBA.setTypbon("BA");
                mvtstoBA.setAncienPrixAchat(matchingItem.getPrixAchat());
                mvtstoBA.setCodeUnite(matchingItem.getCodeUnite());
//                log.debug("le fournisseur est exonere :{}",f.getFournisseurExonere());
                if (!bonRecep.getFournisseurExonere()) {
                    mvtstoBA.setOldCodTva(tvaZeroDTO.getCode());
                    mvtstoBA.setOldTauTva(BigDecimal.ZERO);
                } else {
                    mvtstoBA.setOldCodTva(detailReceptionDTO.getOldCodTva());
                    mvtstoBA.setOldTauTva(detailReceptionDTO.getOldTauTva());
                }

                if (bonReceptionDTO.getCategDepot().equals(PH)) {
                    Preconditions.checkBusinessLogique(detailReceptionDTO.getSellingPrice() != null, "reception.add.missing-selling-price", detailReceptionDTO.getCodeSaisi());
                    mvtstoBA.setPrixVente(detailReceptionDTO.getSellingPrice());
                }

                if (bonReceptionDTO.getCategDepot().equals(IMMO)) {
                    mvtstoBA.setIsCapitalize(detailReceptionDTO.getIsCapitalize());
                } else {
                    mvtstoBA.setIsCapitalize(Boolean.FALSE);
                }

                mvtstoBA.setFactureBA(bonRecep);
                numordre1 = Helper.IncrementString(numordre1, 4);
                details.add(mvtstoBA);

                if (bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                    mvtstoBA.setDatPer(localDate);

                    if (detailReceptionDTO.getLotInter() == null) {
                        mvtstoBA.setLotInter("-");

                    }
                    mvtstoBA.setCodeEmplacement(detailReceptionDTO.getCodeEmplacement());

//                    ArticleIMMODTO articleIMMO = (ArticleIMMODTO) matchingItem;
////                    if (articleIMMO.getGenererImmobilisation()) {
////                        immobilisationDTOs.add(genererImmoDTO(mvtstoBA, articleIMMO, bonReceptionDTO.getFournisseur().getCode(), emplacements, LocalDate.now(), bonRecep.getNumbon().substring(2)));
////                    }
                }
            }
        }

        bonRecep.setDetailFactureBACollection(details);

        //-------  documents--------/
        bonRecep.setPiecesJointes(bonReceptionDTO.getAttachedFilesIDs().stream().map(id -> new PieceJointeReception(id.toString(), bonRecep)).collect(toList()));

        //-------calcul tva et bases tva --------/   
        bonRecep.calcul(listTvas);
        //-------update receiving --------/  
        receiving.setDateValidate(LocalDateTime.now());
        receiving.setUserValidate(SecurityContextHolder.getContext().getAuthentication().getName());
        bonRecep.setReceiving(receiving);
        //-------satisfaction commande achat --------/ 
        List<ReceptionDetailCA> listeReceptionDetailCa = managingPurchaseOrdersForReception(bonReceptionDTO, numBon);
        listeReceptionDetailCa.forEach(elt -> elt.setReception(bonRecep));
        bonRecep.setRecivedDetailCA(listeReceptionDetailCa);
        //-------mode reglement et delai paiement --------/ 
        List<CommandeAchatModeReglementDTO> commandeAhatModeReglements = demandeServiceClient.findListCommandeAchatModeReglementDTOByCodeCaIn(bonReceptionDTO.getPurchaseOrdersCodes());
        Integer maxDelaiPaiement = commandeAhatModeReglements.stream().map(CommandeAchatModeReglementDTO::getDelaiPaiement).max(Integer::compare).get();
        bonRecep.setMaxDelaiPaiement(maxDelaiPaiement);

        FactureBA result = factureBARepository.save(bonRecep);
        //-------fcptfrs --------/ 
        fcptFrsPHService.addFcptFrsOnReception(bonRecep);

//        stockService.saveDepsto(depstos);
//        //-------PMP ,prix de reference, update dernier prix achat --------/  
//        pricingService.updatePricesAfterReception(result, result.getCategDepot(), ADD);
//        paramAchatServiceClient.ArticlePriceUpdateAndArticleFrsPriceUpdate(FactureBAFactory.dtoToBonRecepDTOforUpdatePrices(bonReceptionDTO, result.getCodfrs()));
        paramService.updateCompteurPharmacie(bonReceptionDTO.getCategDepot(), bonReceptionDTO.getTypeBon());

        BonRecepDTO reultDTO = FactureBAFactory.factureBAToBonRecepDTO(result);
        transfertCompanyService.generateTransfertCompanySuiteReceptionInCompany(result);
        return reultDTO;
//create transfert dto inter branche 
    }

    @Override
    public FactureBA ajoutBonReceptionDepotFrs(Facture facture, DepotDTO depotPrincipale, FournisseurDTO fourniss, Boolean appliquerExoneration, String exoneration) {
        throw new UnsupportedOperationException("Not supported ");
    }

    @Retryable(
            maxAttempts = 2,
            backoff = @Backoff(delay = 5000)
    )
    public BonRecepDTO saveRecordOnShelfInCompany(ConsumerRecord<String, BonRecepDTO> record, String groupId) {
        log.debug("************* saveRecordOnShelfInCompany ************** {}", record.value().getDetails().get(0).getDesart());
//        FactureBA bonRecep = FactureBAFactory.factureBADTOTOFactureBANotLzy(record.value());
        BonRecepDTO bonRecepDTO = record.value();
        FactureBA factureBA = factureBARepository.findByNumbonOrigin(record.key());
        Preconditions.checkBusinessLogique(factureBA == null, "reception.existe.deja-company");
        String numBon = paramService.getcompteur(bonRecepDTO.getCategDepot(), TypeBonEnum.BA);
        FactureBA bonRecep = FactureBAFactory.factureBADTOTOFactureBANotLzy(bonRecepDTO, numBon);
        bonRecep.setNumbonOrigin(record.key());
        log.debug("************* factory  **************");
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        bonRecep.calcul(listTvas);
        bonRecep.setIntegrer(Boolean.FALSE);
        log.debug("************* tva  **************");
        FournisseurDTO fourniss = paramAchatServiceClient.findFournisseurByCode(bonRecep.getCodfrs());
        CommandeAchatDTO commandeAchatDTO = demandeServiceClient.createCommandeAchat(CommandeAchatFactory.commandeachatToCommandeAchatDTO(bonRecep, fourniss));
        List<ReceptionDetailCA> receptionDetailCAs = new ArrayList();
        bonRecep.getDetailFactureBACollection().forEach(item -> {
            receptionDetailCAs.add(new ReceptionDetailCA(bonRecep, commandeAchatDTO.getCode(), item.getPk().getCodart(), item.getQuantite(), BigDecimal.ZERO));
        });
        bonRecep.setRecivedDetailCA(receptionDetailCAs);

        Collection<CommandeAchatModeReglementDTO> commandeAhatModeReglements = commandeAchatDTO.getModeReglementList();
        log.debug("commandeAhatModeReglements sont {}", commandeAhatModeReglements);
        Integer maxDelaiPaiement = commandeAhatModeReglements.stream().map(CommandeAchatModeReglementDTO::getDelaiPaiement).max(Integer::compare).get();
        log.debug("maxDelaiPaiement est {}", maxDelaiPaiement);
        bonRecep.setMaxDelaiPaiement(maxDelaiPaiement);
        bonRecep.setValrem(BigDecimal.ZERO);
        bonRecep.setAutomatique(Boolean.TRUE);
        bonRecep.setDateDebutExoneration(fourniss.getDateDebutExenoration());
        bonRecep.setDateFinExenoration(fourniss.getDateFinExenoration());
//        bonRecep.setCodfrs(fourniss.getCode());
//        bonRecep.setAutomatique(Boolean.TRUE);
//        bonRecep.setDatbon(LocalDateTime.MIN);
        bonRecep.setTypbon(TypeBonEnum.BA);
        FactureBA result = factureBARepository.save(bonRecep);
        log.debug("save reception on shelf ");

        log.debug("****** facture **** {}", result);
        log.debug("****** facture **** {}", result.getCodfrs());
        log.debug("****** bonRecep **** {}", bonRecep);
        log.debug("****** bonRecep **** {}", bonRecep.getCodfrs());
        log.debug("****** bonRecep **** {}", bonRecep.getCoddep());
        EtatReceptionCA etatReceptionCA = new EtatReceptionCA(commandeAchatDTO.getCode(), RECEIVED);
        etatReceptionCAService.save(etatReceptionCA);
        log.debug("****** etat reception save **** {}", bonRecep.getCoddep());
        fcptFrsPHService.addFcptFrsOnReception(bonRecep);
        log.debug("****** etat reception save **** {}", bonRecep.getCoddep());
        paramService.updateCompteurPharmacie(bonRecep.getCategDepot(), TypeBonEnum.BA);
        transfertCompanyService.generateTransfertCompanySuiteReceptionInCompany(result);
        topicPartitionOffsetService.commitOffsetByGroupIdToDB(record, groupId);
        return record.value();
    }

//    public ImmobilisationDTO genererImmoDTO(MvtStoBA mvtsto, ArticleIMMODTO articleIMMO, String codeFournisseur, List<EmplacementDTO> emplacements, LocalDate datBon, String numAffiche) {
//
//        ImmobilisationDTO immoDTO = new ImmobilisationDTO(mvtsto.getQuantite(), mvtsto.getPriuni(), 0, numAffiche, articleIMMO.getCodeSaisi());
//
//        immoDTO.setTauxAmortissement(articleIMMO.getTauxAmortissement());
//        immoDTO.setAvecNumeroSerie(articleIMMO.getAvecNumeroSerie());
//        immoDTO.setEttiquetable(articleIMMO.getEtiquettable());
//        immoDTO.setGenererImmobilisation(articleIMMO.getGenererImmobilisation());
//
//        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
//            immoDTO.setDesignation(articleIMMO.getDesignationSec());
//            immoDTO.setDesignationSec(articleIMMO.getDesignation());
//
//        } else {
//            immoDTO.setDesignation(articleIMMO.getDesignation());
//            immoDTO.setDesignationSec(articleIMMO.getDesignationSec());
//        }
//
//        immoDTO.setCodeSaisiArticle(articleIMMO.getCodeSaisi());
//        if (mvtsto.getCodeEmplacement() != null) {
//            EmplacementDTO matchingEmplacement = emplacements.stream().filter(x -> x.getCode().equals(mvtsto.getCodeEmplacement()))
//                    .findFirst()
//                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-emplacement", new Throwable(mvtsto.getCodeEmplacement().toString())));
//            immoDTO.setCodeEmplacement(matchingEmplacement.getCodeSaisi());
//        }
//        immoDTO.setExiste(Boolean.TRUE);
//        immoDTO.setValeurProratat(BigDecimal.ZERO);
//        immoDTO.setAutreFrais(BigDecimal.ZERO);
//        immoDTO.setValeurAmortissementAnterieur(BigDecimal.ZERO);
//        immoDTO.setTauxAmortissement(articleIMMO.getTauxAmortissement());
//        immoDTO.setCodeTva(articleIMMO.getCodeTvaAch());
//        immoDTO.setNumeroSerie(mvtsto.getLotInter());
//        immoDTO.setDetaille(articleIMMO.getDetaille());
//        immoDTO.setCodeFournisseur(codeFournisseur);
//        immoDTO.setFamilleArticle(articleIMMO.getCategorieArticle().getCodeSaisi());
//        immoDTO.setTauxIfrs(articleIMMO.getTauxIfrs());
//        immoDTO.setTauxAmortFiscale1(articleIMMO.getTauxAmortFiscale1());
//        immoDTO.setTauxAmortFiscale2(articleIMMO.getTauxAmortFiscale2());
//        immoDTO.setDateFacture(datBon);
//
//        return immoDTO;
//    }
//    public Depsto genererDepstoFromMvtstoBa(MvtstoBADTO mvtstoBADTODTO, FactureBA factureBA) {
//
//        Depsto depsto = new Depsto();
//
//        depsto.setCodart(mvtstoBADTODTO.getRefArt());
//        depsto.setLotInter(mvtstoBADTODTO.getLotInter());
//        depsto.setDatPer(mvtstoBADTODTO.getDatPer());
//        depsto.setQte(mvtstoBADTODTO.getQuantite());
//        depsto.setPu(mvtstoBADTODTO.getPriuni().multiply((BigDecimal.valueOf(100).subtract(mvtstoBADTODTO.getRemise())).divide(BigDecimal.valueOf(100))));
//        depsto.setCoddep(factureBA.getCoddep());
//        depsto.setCategDepot(factureBA.getCategDepot());
//        depsto.setNumBon(factureBA.getNumbon());
//        depsto.setCodeTva(mvtstoBADTODTO.getCodTVA());
//        depsto.setTauxTva(mvtstoBADTODTO.getTauTVA());
//        depsto.setNumBonOrigin(factureBA.getNumbon());
//        if (mvtstoBADTODTO.getPriuni().compareTo(BigDecimal.ZERO) == 0) {
//            depsto.setMemo("FREE" + factureBA.getNumbon());
//            if (mvtstoBADTODTO.getBaseTva().compareTo(BigDecimal.ZERO) > 0) {
//
//                BigDecimal divisor = BigDecimal.valueOf(100).add(mvtstoBADTODTO.getTauTVA()).divide(BigDecimal.valueOf(100));//100+tva/100
//                depsto.setPu(mvtstoBADTODTO.getBaseTva()
//                        .multiply(mvtstoBADTODTO.getTauTVA())
//                        .divide(new BigDecimal(100)).//baseTva   *0.14-> mntTva gratuite
//                        divide(divisor, 5, RoundingMode.CEILING));
//            }
//        }
//
//        return depsto;
//    }
    @Override
    public BonRecepDTO ajoutReeceptionSuiteValidationReceptionTemporaire(BonRecepDTO receptionDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    ////************************** satisfaction bon commandes*********************///

    @Transactional
    public List<ReceptionDetailCA> managingPurchaseOrdersForReception(BonRecepDTO bonRecepDTO, String numBon) {
        List<DetailCommandeAchatDTO> purchaseOrdersDetails = getDetailOfPurchaseOrders(bonRecepDTO.getPurchaseOrdersCodes());//detail commande +historique des reception et recep temp faite sur cette commande 100/recep 80 qterest =20
        List<ReceptionDetailCA> listeReceptionDetailCa = processPurchaseOrders(bonRecepDTO, purchaseOrdersDetails, numBon);//qteRest c1 20= recep --10 ==> qte rest :10
        return listeReceptionDetailCa;
    }
//adding preaddReceptionTemp

    @Transactional
    public List<ReceptionTemporaireDetailCa> managingPurchaseOrdersForReceptionTemp(ReceptionTemporaireDTO receptionTempDTO) {
        List<DetailCommandeAchatDTO> purchaseOrdersDetails = getDetailOfPurchaseOrders(receptionTempDTO.getPurchaseOrdersCodes());//detail commande +historique des reception et recep temp faite sur cette commande 100/recep 80 qterest =20
//        FactureBADTO factureBADTO=ReceptionTemporaireFactory.receptiontemporaireDTOTofactureDto(receptionTempDTO);//qteRest c1 20= recep temp --10 ==> qte rest :10
        List<ReceptionTemporaireDetailCa> listeReceptionDetailCa = processPurchaseOrdersTemporaires(receptionTempDTO, purchaseOrdersDetails, TypeBonEnum.valueOf(receptionTempDTO.getTypbon()));
        log.debug("ReceptionTemporaireDetailCa {} ", listeReceptionDetailCa);
        return listeReceptionDetailCa;

    }

    /**
     * Getting the details of purchase orders that will be used to create the
     * reception. ca
     *
     * @param purchaseOrdersCodes
     * @return details of purchase orders sorted by creation date desc
     */
    @Transactional
    public List<DetailCommandeAchatDTO> getDetailOfPurchaseOrders(List<Integer> purchaseOrdersCodes) {
        /**
         * getting the liste of purchase orders
         */
        Set<CommandeAchatDTO> listeCA = demandeServiceClient.findListCommandeAchat(purchaseOrdersCodes, LocaleContextHolder.getLocale().getLanguage());
        log.debug("liste  CA est {} ", listeCA);

        Preconditions.checkBusinessLogique(listeCA.size() == purchaseOrdersCodes.size(), "reception.add.missing-ca");
        /*
         * getting the list of previous receptions of the given purchase orders
         */
        List<ReceptionDetailCA> recivedDetailCAs = receptionDetailCAService.findByCodesCAIn(purchaseOrdersCodes);
        List<ReceptionTemporaireDetailCa> recivedTempDetailCAs = receptionTemporaireDetailCaService.findByCodesCAInAndNotValidated(purchaseOrdersCodes);// find Recepetion temp detail ca by code in and non receptionnes

        /*
         * setting the qteRestante of each detail of each purchase order
         */
        List<DetailCommandeAchatDTO> listDetailCA = listeCA.stream()
                .sorted((a, b) -> a.getNumbon().compareTo(b.getNumbon()))// sorting the liste of purchase orders :order ancienete
                .flatMap(e -> e.getDetailCommandeAchatCollection().stream())
                .map(detailCA -> {
                    BigDecimal quantiteReceptionnee = BigDecimal.ZERO;
                    BigDecimal quantiteGratuiteReceptionnee = BigDecimal.ZERO;
                    log.debug("recivedDetailCAs sont {}", recivedDetailCAs);
//                    log.debug("recivedTempDetailCAs sont {}", recivedTempDetailCAs);

                    List<ReceptionDetailCA> MatchingListReceivedDetailCa = recivedDetailCAs.stream()
                            .filter(item -> item.getPk().getCommandeAchat().equals(detailCA.getCode()) && item.getPk().getArticle().equals(detailCA.getCodart()))
                            .collect(Collectors.toList());

                    log.debug("MatchingListReceivedDetailCa sont {}", MatchingListReceivedDetailCa);

                    List<ReceptionTemporaireDetailCa> MatchingListReceivedTempDetailCa = recivedTempDetailCAs.stream()
                            .filter(item -> item.getReceptionTemporaireDetailCaPK().getCommandeAchat().equals(detailCA.getCode()) && item.getReceptionTemporaireDetailCaPK().getArticle().equals(detailCA.getCodart()))
                            .collect(Collectors.toList());

//                    log.debug("MatchingListReceivedTempDetailCa sont {}", MatchingListReceivedTempDetailCa);
                    for (ReceptionTemporaireDetailCa receptionTemporaireDetailCa : MatchingListReceivedTempDetailCa) {

//                        log.debug("quantiteGratuiteReceptionnee  temp est {} et  la quantite gratuite receptionne precedemment  temp est {}", quantiteGratuiteReceptionnee, receptionTemporaireDetailCa.getQuantiteGratuite());
                        quantiteReceptionnee = quantiteReceptionnee.add(receptionTemporaireDetailCa.getQuantiteReceptione());
                        quantiteGratuiteReceptionnee = quantiteGratuiteReceptionnee.add(receptionTemporaireDetailCa.getQuantiteGratuite());
                    };

                    for (ReceptionDetailCA receptionDetailCA : MatchingListReceivedDetailCa) {
                        log.debug("quantiteeReceptionnee est {} et  receptionDetailCA.getQuantiteReceptione {} et gratuite est {}", quantiteReceptionnee, receptionDetailCA.getQuantiteReceptione(), quantiteGratuiteReceptionnee);
//                        log.debug("quantiteGratuiteReReceptionDetailCAceptionnee est {} et  la quantite gratuite receptionne precedemment est {}", quantiteGratuiteReceptionnee, receptionDetailCA.getQuantiteGratuite());
                        quantiteReceptionnee = quantiteReceptionnee.add(receptionDetailCA.getQuantiteReceptione());
                        quantiteGratuiteReceptionnee = quantiteGratuiteReceptionnee.add(receptionDetailCA.getQuantiteGratuite());
                    };

                    BigDecimal qteRestante = detailCA.getQuantite().subtract(quantiteReceptionnee);
                    BigDecimal qteGratuiteRestante = detailCA.getQuantiteGratuite().subtract(quantiteGratuiteReceptionnee);
                    log.debug("****************qteRestante est {} et  la quantite gratuite restante  {}*****************", qteRestante, qteGratuiteRestante);
//                    checking if the purchase order hase been treated by another thread
                    Preconditions.checkBusinessLogique((qteRestante.compareTo(BigDecimal.ZERO) >= 0) || (qteGratuiteRestante.compareTo(BigDecimal.ZERO) >= 0), "reception.add.recived-ca");
                    detailCA.setQuantiteRestante(qteRestante);
                    detailCA.setQuantiteGratuiteRestante(qteGratuiteRestante);
                    return detailCA;
                })
                .collect(Collectors.toList());
        log.debug("liste detail CA  fin est {} ", listDetailCA);
        return listDetailCA;

    }

    /**
     * This method choose which details of purchase orders that we used for
     * creating the reception.By choosing we mean subtracking each recived
     * quantite from the detail of purchase order that we choose
     *
     * @param bonReceptionDTO the created reception
     * @param purchaseOrdersDetails
     * @param numBonReceptionFinale
     * @return
     */
    @Transactional
    public List<ReceptionDetailCA> processPurchaseOrders(FactureBADTO bonReceptionDTO, List<DetailCommandeAchatDTO> purchaseOrdersDetails, String numBonReceptionFinale) {
        log.debug("processing Purchase orders {}", purchaseOrdersDetails);

        /**
         * processing the purchase order
         */
        Map<Integer, Integer> articlesGroupeByCodart = bonReceptionDTO.getDetails()
                .stream()
                .filter(item -> item.getPriuni().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.groupingBy(Item -> Item.getRefArt(), Collectors.summingInt(Item -> Item.getQuantite().intValue())));
        List<ReceptionDetailCA> receptionDetailCAs = new ArrayList();

        for (Map.Entry<Integer, Integer> mvtstoBA : articlesGroupeByCodart.entrySet()) {
            BigDecimal reste = BigDecimal.valueOf(mvtstoBA.getValue());
            BigDecimal resteMvtstoba = BigDecimal.valueOf(mvtstoBA.getValue());
            BigDecimal quantiteRestanteCommandeTotale = purchaseOrdersDetails
                    .stream()
                    .filter(detailCA -> detailCA.getCodart().equals(mvtstoBA.getKey()) && detailCA.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0)
                    .collect(Collectors.reducing(BigDecimal.ZERO,
                            detailCA -> detailCA.getQuantiteRestante(), BigDecimal::add));
            Preconditions.checkBusinessLogique(quantiteRestanteCommandeTotale.compareTo(resteMvtstoba) >= 0, "reception.add.recived-ca");
            BigDecimal min;
            Boolean existsAtLeastOnce = false;
            for (DetailCommandeAchatDTO detailCA : purchaseOrdersDetails) {
                if (detailCA.getCodart().equals(mvtstoBA.getKey()) && detailCA.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0) {

                    if (reste.compareTo(BigDecimal.ZERO) > 0) {
                        min = detailCA.getQuantiteRestante().min(reste);
                        detailCA.setQuantiteRestante(detailCA.getQuantiteRestante().subtract(min));
                        reste = reste.subtract(min);
                        receptionDetailCAs.add(new ReceptionDetailCA(numBonReceptionFinale, detailCA.getCode(), detailCA.getCodart(), min, BigDecimal.ZERO));
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
        List<MvtstoBADTO> freeDetailReceptions = bonReceptionDTO.getDetails()
                .stream()
                .filter(item -> (item.getPriuni().compareTo(BigDecimal.ZERO) == 0) && (Boolean.TRUE.equals(item.getQuantiteGratuiteFromCommande())))
                .collect(Collectors.toList());

        for (MvtstoBADTO freeDetailReception : freeDetailReceptions) {

            List<DetailCommandeAchatDTO> matchingDetailCAs = purchaseOrdersDetails
                    .stream()
                    .filter(item -> item.getCodart().equals(freeDetailReception.getRefArt()))
                    .collect(Collectors.toList());

            BigDecimal quantiteGrtauiteRestanteCommandeTotale = matchingDetailCAs
                    .stream()
                    .collect(Collectors.reducing(BigDecimal.ZERO,
                            detailCA -> detailCA.getQuantiteGratuiteRestante(), BigDecimal::add));
            Preconditions.checkBusinessLogique(quantiteGrtauiteRestanteCommandeTotale.compareTo(freeDetailReception.getQuantite()) >= 0, "reception.add.recived-ca");

            Preconditions.checkBusinessLogique(!(matchingDetailCAs.isEmpty()), "reception.add.detail-ca-missing", freeDetailReception.getRefArt().toString());
//         
            BigDecimal reste = freeDetailReception.getQuantite();
            BigDecimal minQteGratuite;
            for (DetailCommandeAchatDTO detailCA : matchingDetailCAs) {

                if (reste.compareTo(BigDecimal.ZERO) > 0) {
                    minQteGratuite = detailCA.getQuantiteGratuiteRestante().min(reste);
                    detailCA.setQuantiteGratuiteRestante(detailCA.getQuantiteGratuiteRestante().subtract(minQteGratuite));
                    reste = reste.subtract(minQteGratuite);
                    Optional<ReceptionDetailCA> matchedReceptionCA = receptionDetailCAs
                            .stream()
                            .filter(elt -> elt.getPk().getArticle().equals(detailCA.getCodart()) && elt.getPk().getCommandeAchat().equals(detailCA.getCode()))
                            .findFirst();
                    if (!matchedReceptionCA.isPresent()) {

                        receptionDetailCAs.add(new ReceptionDetailCA(bonReceptionDTO.getNumbon(), detailCA.getCode(), detailCA.getCodart(), BigDecimal.ZERO, minQteGratuite));
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
                        detailCA.setQuantiteRestante(detailCA.getQuantiteRestante().subtract(min));
                        reste = reste.subtract(min);
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
                .filter(item -> (item.getPriuni().compareTo(BigDecimal.ZERO) == 0) && (item.getCode() != null))
                .collect(Collectors.toList());

        for (DetailReceptionTemporaireDTO freeDetailReceptionTemp : freeDetailReceptionsTemp) {
            log.debug("freeDetailReception est {}", freeDetailReceptionTemp);
            List<DetailCommandeAchatDTO> matchingDetailCAs = purchaseOrdersDetails
                    .stream()
                    .filter(item -> item.getCodart().equals(freeDetailReceptionTemp.getRefArt()))
                    .collect(Collectors.toList());
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

    @Override
    public ImmobilisationDTO genererImmoDTO(MvtStoBA mvtsto, ArticleIMMODTO articleIMMO, String codeFournisseur, List<EmplacementDTO> emplacements, LocalDate datBon, String numAffiche) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Depsto genererDepstoFromMvtstoBa(MvtstoBADTO mvtstoBADTODTO, FactureBA factureBA) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//@Transactional
//    public BonRecepDTO testsuitetransfert(BonRecepDTO bonReceptionDTO) {
////
////        Receiving receiving = receivingService.findReceiving(bonReceptionDTO.getReceivingCode());
////        Preconditions.checkBusinessLogique(receiving != null, "reception.add.missing-receiving");
////        Preconditions.checkBusinessLogique(receiving.getDateAnnule() == null, "reception.add.canceld-receiving");
////        Preconditions.checkBusinessLogique(receiving.getDateValidate() == null, "reception.add.validated-receiving");  
//
//        //------- Controle commandes achats--------/
//
////        Set<CommandeAchatDTO> listeCommandeAchat = demandeServiceClient.findListCommandeAchat(bonReceptionDTO.getPurchaseOrdersCodes(), LocaleContextHolder.getLocale().getLanguage());
////        CommandeAchatDTO commandeAchat = listeCommandeAchat.stream().filter(item -> item.getUserArchive() != null).findFirst().orElse(null);
////        Preconditions.checkBusinessLogique(commandeAchat == null, "receiving-with-archeived-purchase-order");
//
//        String numBon = paramService.getcompteur(bonReceptionDTO.getCategDepot(), bonReceptionDTO.getTypeBon());
//      //  -------  get emplacements et articles and check items --------/
//        Set<Integer> codesEmplacements = new HashSet();
//        Set<Integer> codeArticles = new HashSet();
//
//        bonReceptionDTO.getDetails().forEach(detail -> {
//            codeArticles.add(detail.getRefArt());
//            if (detail.getCodeEmplacement() != null) {
//                codesEmplacements.add(detail.getCodeEmplacement());
//            }
//        });
//        log.debug("codeArticles sont {}", codeArticles);
//     //   -------  emplacements immo--------/    
//        List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(bonReceptionDTO.getCategDepot(), codeArticles.toArray(new Integer[codeArticles.size()]));
//        List<EmplacementDTO> emplacements = bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(codesEmplacements) : new ArrayList();
//        if (bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
//            Preconditions.checkBusinessLogique(emplacements != null, "error-emplacaments");
//        }
//        
//        Set<Integer> categArticleIDs = new HashSet();
//        articles.forEach(item -> {
//            Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(item.getAnnule()) && !(Boolean.TRUE.equals(item.getStopped())), "item.stopped", item.getCodeSaisi());
//            categArticleIDs.add(item.getCategorieArticle().getCode());
//        });
//       // -------  check inventory--------/    
//        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(new ArrayList(categArticleIDs), bonReceptionDTO.getCoddep());
//        if (categArticleUnderInventory.size() > 0) {
//            List<String> articlesUnderInventory = articles.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
//            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
//        }
//        bonReceptionDTO.setNumbon(numBon);
//        FactureBA bonRecep = FactureBAFactory.factureBADTOTOFactureBA(bonReceptionDTO);
//        bonRecep.setAutomatique(bonReceptionDTO.getAutomatique());
//        bonRecep.setNumbon(numBon);
//
//       // -------  fournisseur--------/
//        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(bonReceptionDTO.getFournisseur().getCode());
//        Preconditions.checkBusinessLogique(!fournisseurDTO.getDesignation().equals("fournisseur.deleted"), "Fournisseur avec code : " + bonReceptionDTO.getFournisseur().getCode() + " est introuvable");
//        bonRecep.setCodfrs(fournisseurDTO.getCode());
//        bonRecep.setDateDebutExoneration(fournisseurDTO.getDateDebutExenoration());
//        bonRecep.setDateFinExenoration(fournisseurDTO.getDateFinExenoration());
//        bonRecep.setIntegrer(Boolean.FALSE);
//       // -------  depot--------/
//        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(bonReceptionDTO.getCoddep());
//        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + bonReceptionDTO.getCoddep() + "] introuvable");
//        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getCode() + "] est un dépot fournisseur");
//        bonRecep.setCoddep(depotd.getCode());
//    //    -------  /////////////DETAILS//////////--------/    
//        List<MvtStoBA> details = new ArrayList<>();
//        String numordre1 = "0001";
//        Date date = new Date(3000, 01, 01);
//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper = new ListeImmobilisationDTOWrapper();
//        List<ImmobilisationDTO> immobilisationDTOs = new ArrayList();
//        List<Depsto> depstos = new ArrayList<>();
//
//        for (MvtstoBADTO detailReceptionDTO : bonReceptionDTO.getDetails()) {
//            if (detailReceptionDTO.getQuantite().compareTo(BigDecimal.ZERO) != 0) {
//                ArticleDTO matchingItem = articles.stream()
//                        .filter(article -> article.getCode().equals(detailReceptionDTO.getRefArt()))
//                        .findFirst()
//                        .orElseThrow(() -> new IllegalBusinessLogiqueException("stock.find-all.missing-article", new Throwable(detailReceptionDTO.getCodeSaisi())));
//
//                String ordre = "BA" + numordre1;
//
//                MvtStoBA mvtstoBA = MvtstoBAFactory.MvtStoBADTOTOMvtstoBA(detailReceptionDTO, ordre, bonReceptionDTO, numBon);
//                mvtstoBA.setTypbon("BA");
//                mvtstoBA.setAncienPrixAchat(matchingItem.getPrixAchat());
//                mvtstoBA.setCodeUnite(matchingItem.getCodeUnite());
//                log.debug("le fournisseur est exonere :{}",f.getFournisseurExonere());
//                if (!bonRecep.getFournisseurExonere()) {
//                    mvtstoBA.setOldCodTva(5);
//                    mvtstoBA.setOldTauTva(BigDecimal.ZERO);
//                } else {
//                    mvtstoBA.setOldCodTva(detailReceptionDTO.getOldCodTva());
//                    mvtstoBA.setOldTauTva(detailReceptionDTO.getOldTauTva());
//                }
//
//                if (bonReceptionDTO.getCategDepot().equals(PH)) {
//                    Preconditions.checkBusinessLogique(detailReceptionDTO.getSellingPrice() != null, "reception.add.missing-selling-price", detailReceptionDTO.getCodeSaisi());
//                    mvtstoBA.setPrixVente(detailReceptionDTO.getSellingPrice());
//                }
//
//                if (bonReceptionDTO.getCategDepot().equals(IMMO)) {
//                    mvtstoBA.setIsCapitalize(detailReceptionDTO.getIsCapitalize());
//                } else {
//                    mvtstoBA.setIsCapitalize(Boolean.FALSE);
//                }
//
//                mvtstoBA.setFactureBA(bonRecep);
//                numordre1 = Helper.IncrementString(numordre1, 4);
//                details.add(mvtstoBA);
//               
//                Depsto depsto = genererDepstoFromMvtstoBa(detailReceptionDTO, bonRecep);
//                depsto.setIsCapitalize(detailReceptionDTO.getIsCapitalize());
//                depsto.setUnite(matchingItem.getCodeUnite());
//                depstos.add(depsto);
//
//                if (bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
//                    mvtstoBA.setDatPer(localDate);
//                    depsto.setDatPer(localDate);
//                    if (detailReceptionDTO.getLotInter() == null) {
//                        mvtstoBA.setLotInter("-");
//                        depsto.setLotInter("-");
//                    }
//                    mvtstoBA.setCodeEmplacement(detailReceptionDTO.getCodeEmplacement());
//                    depsto.setCodeEmplacement(detailReceptionDTO.getCodeEmplacement());
//
//                    ArticleIMMODTO articleIMMO = (ArticleIMMODTO) matchingItem;
//                    if (articleIMMO.getGenererImmobilisation()) {
//                        immobilisationDTOs.add(genererImmoDTO(mvtstoBA, articleIMMO, bonReceptionDTO.getFournisseur().getCode(), emplacements, LocalDate.now(), bonRecep.getNumbon().substring(2)));
//                    }
//                }
//            }
//        }
//        depstoRepository.save(depstos);
//        depstoRepository.flush();
//        bonRecep.setDetailFactureBACollection(details);
//
//     //  -------save immobilisations--------/    
//        if (bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO) && !immobilisationDTOs.isEmpty()) {
//            listeImmobilisationDTOWrapper.setImmobilisation(immobilisationDTOs);
//            ListeImmobilisationDTOWrapper listeImmo = immobilisationService.saveImmo(listeImmobilisationDTOWrapper);
//            Preconditions.checkBusinessLogique(listeImmo != null, "error-saving-immo");
//        }
//        //-------calcul tva et bases tva --------/   
//        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
//        bonRecep.calcul(listTvas);
////        //-------update receiving --------/  
////        receiving.setDateValidate(LocalDateTime.now());
////        receiving.setUserValidate(SecurityContextHolder.getContext().getAuthentication().getName());
////        bonRecep.setReceiving(receiving);
//        //-------satisfaction commande achat --------/ 
//        List<ReceptionDetailCA> listeReceptionDetailCa = managingPurchaseOrdersForReception(bonReceptionDTO, numBon);
//        listeReceptionDetailCa.forEach(elt -> elt.setReception(bonRecep));
//        bonRecep.setRecivedDetailCA(listeReceptionDetailCa);
//        //-------mode reglement et delai paiement --------/ 
//        List<CommandeAchatModeReglementDTO> commandeAhatModeReglements = demandeServiceClient.findListCommandeAchatModeReglementDTOByCodeCaIn(bonReceptionDTO.getPurchaseOrdersCodes());
//        Integer maxDelaiPaiement = commandeAhatModeReglements.stream().map(CommandeAchatModeReglementDTO::getDelaiPaiement).max(Integer::compare).get();
//        bonRecep.setMaxDelaiPaiement(maxDelaiPaiement);
//
//        FactureBA result = factureBARepository.save(bonRecep);
//        //-------fcptfrs --------/ 
//        fcptFrsPHService.addFcptFrsOnReception(bonRecep);
//
//        stockService.saveDepsto(depstos);
//
//        //-------PMP ,prix de reference, update dernier prix achat --------/  
//        pricingService.updatePricesAfterReception(bonRecep, bonRecep.getCategDepot(), ADD);
//        paramAchatServiceClient.ArticlePriceUpdateAndArticleFrsPriceUpdate(FactureBAFactory.dtoToBonRecepDTOforUpdatePrices(bonReceptionDTO, result.getCodfrs()));
//
//        paramService.updateCompteurPharmacie(bonReceptionDTO.getCategDepot(), bonReceptionDTO.getTypeBon());
//
//        //-------return full dto pour affichage immo prelevement automatique --------/ 
//        if (result.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
//            BonRecepDTO reultDTO = FactureBAFactory.factureBAToBonRecepDTO(result);
//                        List<MvtstoBADTO> detailsReception = new ArrayList();
//            result.getDetailFactureBACollection().forEach(mvtstoBA -> {
//                MvtstoBADTO detailRecep = new MvtstoBADTO();
//                DetailReceptionFactory.toDTO(mvtstoBA, detailRecep);
//                detailRecep.setQteReturned(mvtstoBA.getQuantite().subtract(mvtstoBA.getQtecom()));
//                detailsReception.add(detailRecep);
//            });
//            reultDTO.setDetails((List) detailsReception);
//            reultDTO.setDesignationDepot(depotd.getDesdep());
//            reultDTO.setFournisseur(fournisseurDTO);
//            reultDTO.getDetails().forEach(item -> {
//                EmplacementDTO emplacement = emplacements.stream()
//                        .filter(x -> x.getCode().equals(item.getCodeEmplacement()))
//                        .findFirst().orElse(null);
//                if (item.getCodeEmplacement() != null) {
//                    item.setDesignationEmplacement(emplacement.getDesignation());
//                    item.setCodeDepartementEmplacement(emplacement.getCodeDepartement().getCode());
//                    item.setDesigationDepartementEmplacement(emplacement.getCodeDepartement().getDesignation());
//                }
//
////            });
//            return reultDTO;
//        }
//
//        BonRecepDTO reultDTO = FactureBAFactory.factureBAToBonRecepDTO(result);
//
//        return reultDTO;
//
//    }
//  
//    }
    @Override
    public BonRecepDTO updateBonReception(BonRecepDTO bonReceptionDTO) {

        log.debug("updating  factureba {}", bonReceptionDTO.getNumbon());
        FactureBA inBaseReception = factureBARepository.findOne(bonReceptionDTO.getNumbon());
        Preconditions.checkBusinessLogique(inBaseReception != null, "reception.update.notFound");
        Preconditions.checkBusinessLogique(inBaseReception.getDatAnnul() == null, "reception.update.canceled-reception");
        if (inBaseReception.getAutomatique() != null) {
            Preconditions.checkBusinessLogique(!inBaseReception.getAutomatique(), "reception.update.reception-automatique");
        }
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(inBaseReception.getIntegrer()), "facture-deja-integre");
        Receiving receiving = receivingService.findReceiving(bonReceptionDTO.getReceivingCode());
        Preconditions.checkBusinessLogique(receiving != null, "reception.add.missing-receiving");

        Preconditions.checkBusinessLogique(inBaseReception.getFactureBonReception() == null, "invoiced-reception");

        inBaseReception.setRefFrs(bonReceptionDTO.getRefFrs());
        inBaseReception.setDatRefFrs(bonReceptionDTO.getDateFrs());
        inBaseReception.setMemop(bonReceptionDTO.getMemop());
        //-------  documents--------/
        if (inBaseReception.getPiecesJointes() != null) {
            List<PieceJointeReception> newPieceJointes = bonReceptionDTO.getAttachedFilesIDs().stream().map(id -> new PieceJointeReception(id.toString(), inBaseReception)).collect(toList());
            inBaseReception.getPiecesJointes().retainAll(newPieceJointes);
            inBaseReception.getPiecesJointes().addAll(newPieceJointes);
        } else {
            inBaseReception.setPiecesJointes(bonReceptionDTO.getAttachedFilesIDs().stream().map(id -> new PieceJointeReception(id.toString(), inBaseReception)).collect(toList()));
        }

        FactureBA result = factureBARepository.save(inBaseReception);
//        fcptFrsPHService.updateFcptFrsOnReception(fcptFrs, inBaseReception);
//        pricingService.updatePricesAfterReception(result, result.getCategDepot(), ADD);
//        paramAchatServiceClient.ArticlePriceUpdateAndArticleFrsPriceUpdate(factureBAFactory.dtoToBonRecepDTOforUpdatePrices(bDto, result.getCodfrs()));
        return FactureBAFactory.factureBAToBonRecepDTO(result);
    }

    @Recover
    public BonRecepDTO recover(Exception exc, ConsumerRecord<String, BonRecepDTO> record, String groupId) {
        log.error("RECOVERING: I will handle my exception in article ph  {} occured with object {} ", exc.toString(), record.value().toString());
        log.error("exception est {},{},{},{}", exc.getCause(), exc.getMessage(), exc.getStackTrace());
        KafkaConsumerErrorDTO kafkaConsumerErrorDTO = new KafkaConsumerErrorDTO();
        kafkaConsumerErrorDTO.setTopic(topicBonReceptionOnShelfManagement);
        kafkaConsumerErrorDTO.setPartition(record.partition());
        kafkaConsumerErrorDTO.setOffset((int) record.offset());
        kafkaConsumerErrorDTO.setGroupId(groupId);
        kafkaConsumerErrorDTO.setTryCount(1);
        kafkaConsumerErrorDTO.setCreateTime(LocalDateTime.now());
        kafkaConsumerErrorDTO.setHandled(Boolean.FALSE);
        String errorMessage;
        if (exc.getCause() != null && exc.getMessage() != null) {
            errorMessage = String.valueOf("exception cause est :").concat(exc.getCause().toString()).concat(" message exception est : ").concat(exc.getMessage()).concat(" message stack  est : ").concat(Arrays.toString(exc.getStackTrace()));
        } else {
            errorMessage = String.valueOf(" message stack  est : ").concat(Arrays.toString(exc.getStackTrace()));
        }
        kafkaConsumerErrorDTO.setExceptionDetails(errorMessage);
        kafkaConsumerErrorDTO.setRecordKey(record.key());
        kafkaConsumerErrorDTO.setRecord(record.value().toString());
        kafkaConsumerErrorDTO.setAction(EnumCrudMethod.CREATE);
        log.debug("******* RECOVERING: save kafkaConsumerError ph item************ {}", kafkaConsumerErrorDTO.toString());
        kafkaConsumerErrorService.save(kafkaConsumerErrorDTO);
        topicPartitionOffsetService.commitOffsetByGroupIdToDB(record, groupId);
        return record.value();
    }

    @Override
    public void processStockAndPricesAfterTransefrtCompanyToBranch(TransfertCompanyBranch transfertCompanyBranch, TransfertCompanyBranchDTO transfertCompanyBranchDTO, List<ArticleDTO> articles) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BonRecepDTO cancelBonReception(String numBon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BonRetourDTO ajoutBonRetour(BonRetourDTO bonRetourDTO) {

        FactureBA bonReceptionCorrespondant = factureBARepository.findFirstByNumbon(bonRetourDTO.getReceptionID());
        Preconditions.checkBusinessLogique(bonReceptionCorrespondant.getFactureBonReception() == null, "invoiced-reception");

        TransfertCompanyBranch retourTransfertCompanyBranchCorrespondant = transfertCompanyService.findOneTransfert(bonRetourDTO.getNumbonRetourTransfertCompanyBranch());
        Preconditions.checkBusinessLogique(retourTransfertCompanyBranchCorrespondant != null, "missing-retour-transfert ");
        retourTransfertCompanyBranchCorrespondant.setReturnedToSupplier(Boolean.TRUE);
        //   removed by ahmad gamal and bilel
//        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(bonReceptionCorrespondant.getIntegrer()), "facture-deja-integre");
        String numBon = paramService.getcompteur(bonRetourDTO.getCategDepot(), bonRetourDTO.getTypeBon());
//        bonRetourDTO.setNumbon(numBon);
        FactureBA bonRetour = FactureBAFactory.factureBADTOTOFactureBA((FactureBADTO) bonRetourDTO);

        SiteDTO siteBrancheDTO = paramServiceClient.findSiteByCode(bonRetourDTO.getCodeSite());
        log.info("codeSiteBranch  est {}", siteBrancheDTO);
        Preconditions.checkBusinessLogique(siteBrancheDTO != null, "parametrage.clinique.error");
        bonRetour.setCodeSite(siteBrancheDTO.getCode());
        //-------  fournisseur--------/
        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(bonRetourDTO.getFournisseur().getCode());
        Preconditions.checkBusinessLogique(!fournisseurDTO.getDesignation().equals("fournisseur.deleted"), "Fournisseur avec code : " + bonRetourDTO.getFournisseur().getCode() + " est introuvable");
        bonRetour.setCodfrs(fournisseurDTO.getCode());

        //-------  depot--------/
        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(bonRetourDTO.getCoddep());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + bonRetourDTO.getCoddep() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getCode() + "] est un dépot fournisseur");
        bonRetour.setCoddep(depotd.getCode());

        bonRetour.setIntegrer(Boolean.FALSE);
        bonRetour.setNumbon(numBon);
        bonRetour.setNumpiece(bonRetourDTO.getReceptionID());
        bonRetour.setNumbonRetourCompanyBranch(retourTransfertCompanyBranchCorrespondant.getNumBon());
//        FactureBA bonRetour = factureBAFactory.factureBAFromDTO(bDto);
        List<MvtStoBA> details = new ArrayList<>();
        String numordre1 = "0001";
        for (MvtstoBADTO mvtstoDTO : bonRetourDTO.getDetails()) {
            String ordre = "RT" + numordre1;
            MvtStoBA detailRetour = MvtstoBAFactory.MvtStoBADTOTOMvtstoBA(mvtstoDTO, ordre, bonRetourDTO, numBon);
            detailRetour.setTypbon("RT");
            detailRetour.setPrixVente(mvtstoDTO.getSellingPrice());
            detailRetour.setCodeUnite(mvtstoDTO.getCodeUnite());

            detailRetour.setFactureBA(bonRetour);
            details.add(detailRetour);
            numordre1 = Helper.IncrementString(numordre1, 4);
        }
        bonRetour.setDetailFactureBACollection(details);
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        bonRetour.calcul(listTvas);
        FactureBA resutedRetour = factureBARepository.save(bonRetour);
        processReceptionOnReturn(resutedRetour);
        fcptFrsPHService.addFcptFrsOnReturn(resutedRetour);
//        stockService.processStockOnReturn(resutedRetour);
//          FactureBA result = factureBARepository.save(factureBa);
//        pricingService.updatePricesAfterReturn(resutedRetour, listeMvtStoBAs, bonReceptionCorrespondant.getDatbon());
//        ajustementRetourFournisseurService.saveAjustementRetour(resutedRetour);
        paramService.updateCompteurPharmacie(bonRetourDTO.getCategDepot(), bonRetourDTO.getTypeBon());
        return FactureBAFactory.factureBAToBonRetourDTOEager(resutedRetour);
    }

    public BonRecepDTO cancelBonReceptionDepotFrs(ConsumerRecord<String, BonRecepDTO> record, String groupId) {
        String numBon = record.value().getNumbon();
        log.debug("cancelling new FactureBA {}", numBon);
        FactureBA factureBA = factureBARepository.findByNumbonOrigin(numBon);
        Preconditions.checkBusinessLogique(factureBA != null, "reception.notFound");
        Preconditions.checkBusinessLogique(factureBA.getCodAnnul() == null, "reception.delete.reception-canceld");
//        Preconditions.checkBusinessLogique(factureBA.getFactureBonReception() == null, "facture-bon-reception.invoiced-reception", factureBA.getNumbon());
//        fcptFrsPHService.deleteFcptfrsByNumBonDao(factureBA.getNumbon(), factureBA.getTypbon());
        factureBA.setCodAnnul(SecurityContextHolder.getContext().getAuthentication().getName());
        factureBA.setDatAnnul(LocalDateTime.now());
        List<Integer> listCodesCA = factureBA.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());// list of received purchase orders
        etatReceptionCAService.deleteByCommandeAchatIn(listCodesCA); // delete the state of the recived purchase orders 
        List<ReceptionDetailCA> receptionDetailCAs = receptionDetailCAService.findByCodesCAIn(listCodesCA);
        List<EtatReceptionCA> partRecivedPurchOrdes = receptionDetailCAs.stream() // recalculate their state 
                .filter(item -> !item.getPk().getReception().equals(factureBA.getNumbon()) && item.getQuantiteReceptione().compareTo(BigDecimal.ZERO) > 0)
                .map(filtredItem -> {
                    return new EtatReceptionCA(filtredItem.getPk().getCommandeAchat(), PurchaseOrderReceptionState.A);
                })
                .collect(Collectors.toList());
        factureBA.getRecivedDetailCA().clear();
        etatReceptionCAService.save(partRecivedPurchOrdes);
        FactureBA result = factureBARepository.save(factureBA);
//        BonRecepDTO bonRecepDto = FactureBAFactory.factureBAToBonRecepDTO(factureBA);
        return FactureBAFactory.factureBAToBonRecepDTO(result);
    }

    public Boolean cancelBonReceptionDepotFrsPermanent(ConsumerRecord<String, BonRecepDTO> record, String groupId) {
        String numBon = record.value().getNumbon();
        log.debug("cancelling new FactureBA {}", numBon);
        FactureBA factureBA = factureBARepository.findByNumbonOrigin(numBon);
        Preconditions.checkBusinessLogique(factureBA != null, "reception.notFound");
        Preconditions.checkBusinessLogique(factureBA.getCodAnnul() == null, "reception.delete.reception-canceld");
        fcptFrsPHService.deleteFcptfrsByNumBonDao(factureBA.getNumbon(), factureBA.getTypbon());
        List<Integer> listCodesCA = factureBA.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());// list of received purchase orders
        etatReceptionCAService.deleteByCommandeAchatIn(listCodesCA); // delete the state of the recived purchase orders 
        factureBARepository.delete(factureBA);
//        paramService.updateCompteurPharmacie(CategorieDepotEnum.UU, TypeBonEnum.BA, factureBARepository.findMaxNumbonByCategDepot(CategorieDepotEnum.UU));
//        paramService.updateCompteurPharmacie(CategorieDepotEnum.PH, TypeBonEnum.BA, factureBARepository.findMaxNumbonByCategDepot(CategorieDepotEnum.PH));

        return Boolean.TRUE;
    }

    @Override
    public BonRecepDTO cancelBonReceptionDepotFrs(String numBon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean cancelBonReceptionDepotFrsPermanent(String numBon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
