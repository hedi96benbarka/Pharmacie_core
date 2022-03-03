/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.DetailTransfertCompanyBranch;
import com.csys.pharmacie.achat.domain.EtatReceptionCA;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCa;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticleIMMODTO;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.BonRetourDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.DetailCommandeAchatDTO;
import com.csys.pharmacie.achat.dto.EmplacementDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.achat.dto.ReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.factory.AvoirFournisseurFactory;
import com.csys.pharmacie.achat.repository.AvoirFournisseurRepository;
import com.csys.pharmacie.achat.repository.FactureBARepository;
import com.csys.pharmacie.achat.repository.ReceptionDetailCARepository;
import com.csys.pharmacie.achat.repository.RetourPerimeRepository;
import static com.csys.pharmacie.achat.service.ReceptionTemporaireService.LANGUAGE_SEC;
import com.csys.pharmacie.client.dto.ImmobilisationDTO;
import com.csys.pharmacie.client.dto.ListeImmobilisationDTOWrapper;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.config.Sender;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.PARTIALLY_RECIVED;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrateur
 */
@Service
@Transactional
public class FactureBABranchFacadeService extends FactureBAService {

    @Autowired
    private Sender sender;

    static Integer codeSiteConfig;
    public static String topicBonReceptionOnShelfManagement;
    @Value("${code-site}")
    public void setCodeSite(Integer codeSite) {
        codeSiteConfig = codeSite;
    }
    @Value("${kafka.topic.transfert-bon-reception-onShelf-management}")
     public void setTopicTransfertOnshelf(String topicBonReceptionOnShelf) {
        topicBonReceptionOnShelfManagement = topicBonReceptionOnShelf;
    }
    public final TopicPartitionOffsetService topicPartitionOffsetService;
    public final FactureBaBrancheService factureBaBrancheService;
    private final TransfertCompanyBranchService transfertCompanyBranchService;

    public FactureBABranchFacadeService(TopicPartitionOffsetService topicPartitionOffsetService, FactureBaBrancheService factureBaBrancheService,@Lazy TransfertCompanyBranchService transfertCompanyBranchService, FactureBARepository factureBARepository, ReceptionDetailCARepository receptionDetailCARepository, ParamService paramService, FcptFrsPHService fcptFrsPHService, StockService stockService, DemandeServiceClient demandeServiceClient, ReceptionDetailCAService receptionDetailCAService, ReceptionTemporaireDetailCaService receptionTemporaireDetailCaService, @Lazy PricingService pricingService, ParamAchatServiceClient paramAchatServiceClient, ReceivingService receivingService, EtatReceptionCAService etatReceptionCAService, ParamServiceClient paramServiceClient, MvtStoBAService mvtStoBAService, AjustementRetourFournisseurService ajustementRetourFournisseurService, RetourPerimeRepository retourPerimeRepository, AvoirFournisseurFactory avoirFournisseurFactory, AvoirFournisseurRepository avoirfournisseurRepository, InventaireService inventaireService, ImmobilisationService immobilisationService, DepstoRepository depstoRepository, ReceptionTemporaireService receptionTemporaireService, MessageSource messages, CliniSysService cliniSysService) {
        super(factureBARepository, receptionDetailCARepository, paramService, fcptFrsPHService, stockService, demandeServiceClient, receptionDetailCAService, receptionTemporaireDetailCaService, pricingService, paramAchatServiceClient, receivingService, etatReceptionCAService, paramServiceClient, mvtStoBAService, ajustementRetourFournisseurService, retourPerimeRepository, avoirFournisseurFactory, avoirfournisseurRepository, inventaireService, immobilisationService, depstoRepository, receptionTemporaireService, messages, cliniSysService);
        this.topicPartitionOffsetService = topicPartitionOffsetService;
        this.factureBaBrancheService = factureBaBrancheService;
        this.transfertCompanyBranchService = transfertCompanyBranchService;
    }

    @Transactional
    @Override
    public void processStockAndPricesAfterTransefrtCompanyToBranch(TransfertCompanyBranch transfertCompanyBranch, TransfertCompanyBranchDTO transfertCompanyBranchDTO, List<ArticleDTO> articles) {
        log.debug("process StockAndPricesAfterReceptionOrTransfert  with transfert {}", transfertCompanyBranch);

        //-------   emplacements  --------/
        Set<Integer> codesEmplacements = new HashSet();
        transfertCompanyBranchDTO.getDetailTransfertCompanyBranchDTOs().forEach(detail -> {
            if (detail.getCodeEmplacement() != null) {
                codesEmplacements.add(detail.getCodeEmplacement());
            }
        });
        List<EmplacementDTO> emplacements = transfertCompanyBranch.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(codesEmplacements, null) : new ArrayList();
        if (transfertCompanyBranch.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            Preconditions.checkBusinessLogique(emplacements != null, "error-emplacaments");
        }
        // -------  check inventory--------/  
        Set<Integer> categArticleIDs = articles.stream().map(elt -> elt.getCategorieArticle().getCode()).collect(Collectors.toSet());
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(new ArrayList(categArticleIDs), transfertCompanyBranch.getCodeDepot());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = articles.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }

        //-------  /////////////DETAILS//////////--------/    
        Date date = new Date(3000, 01, 01);
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper = new ListeImmobilisationDTOWrapper();
        List<ImmobilisationDTO> immobilisationDTOs = new ArrayList();
        List<Depsto> depstos = new ArrayList<>();

        for (DetailTransfertCompanyBranch detailTransfertCompanyBranch : transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()) {
            if (detailTransfertCompanyBranch.getQuantite().compareTo(BigDecimal.ZERO) != 0) {
                ArticleDTO matchingItem = articles.stream()
                        .filter(article -> article.getCode().equals(detailTransfertCompanyBranch.getCodeArticle()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("stock.find-all.missing-article", new Throwable(detailTransfertCompanyBranch.getCodeSaisi())));

                log.debug("***********matching item **********{}", matchingItem);
//                MvtStoBA mvtstoBA = MvtstoBAFactory.MvtStoBADTOTOMvtstoBA(detailReceptionDTO, ordre, bonReceptionDTO, numBon);
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
                Depsto depsto = genererDepstoFromDetailTransfertCompanyBranch(detailTransfertCompanyBranch, transfertCompanyBranch);
                depsto.setIsCapitalize(detailTransfertCompanyBranch.getIsCapitalize());
                depsto.setUnite(matchingItem.getCodeUnite());
                depstos.add(depsto);

                if (transfertCompanyBranch.getCategDepot().equals(CategorieDepotEnum.IMMO)) {

                    depsto.setDatPer(localDate);
                    if (detailTransfertCompanyBranch.getLotInter() == null) {
                        depsto.setLotInter("-");
                    }

                    ArticleIMMODTO articleIMMO = (ArticleIMMODTO) matchingItem;
                    if (articleIMMO.getGenererImmobilisation()) {
                        immobilisationDTOs.add(genererImmoDTO(detailTransfertCompanyBranch, articleIMMO, transfertCompanyBranch.getCodeFournisseur(), emplacements, LocalDate.now(), transfertCompanyBranch.getNumBon().substring(2)));
                    }
                }
            }
        }
        depstoRepository.save(depstos);
        depstoRepository.flush();

        ////-------save immobilisations--------/    
        if (transfertCompanyBranch.getCategDepot().equals(CategorieDepotEnum.IMMO) && !immobilisationDTOs.isEmpty()) {
            listeImmobilisationDTOWrapper.setImmobilisation(immobilisationDTOs);
            ListeImmobilisationDTOWrapper listeImmo = immobilisationService.saveImmo(listeImmobilisationDTOWrapper);
            if (listeImmo != null) {
                transfertCompanyBranch.setImmobilisationGenere(Boolean.TRUE);
            } else {
                transfertCompanyBranch.setImmobilisationGenere(Boolean.FALSE);
            }
//            Preconditions.checkBusinessLogique(listeImmo != null, "error-saving-immo");
        }

        ////-------update prices--------/    
        processPricesAfterReceptionOrTransfert(transfertCompanyBranchDTO, transfertCompanyBranch);

    }

    public void processPricesAfterReceptionOrTransfert(TransfertCompanyBranchDTO transfertCompanyBranchDTO, TransfertCompanyBranch transfertCompanyBranch) {

//// -------PMP ,prix de reference, update dernier prix achat --------/ to remove comment 
        pricingService.updatePricesAfterTransfertCompanyToBranch(transfertCompanyBranch, transfertCompanyBranch.getCategDepot());

        BonRecepDTO resultedBonRecepDTO = generateReceptionFromTransfertDTO(transfertCompanyBranchDTO);//used not to modify the metod update Prices Article 
        paramAchatServiceClient.ArticlePriceUpdateAndArticleFrsPriceUpdate(resultedBonRecepDTO);

    }

    public FactureBA ajoutBonReceptionDepotFrs(Facture facture, DepotDTO depotPrincipale, FournisseurDTO fourniss, Boolean appliquerExoneration, String exoneration) {

        Map<Integer, Object> resultedObjects = factureBaBrancheService.ajoutBonReceptionDepotFrs(facture, depotPrincipale, fourniss, appliquerExoneration, exoneration);
        FactureBA factureBa = (FactureBA) resultedObjects.get(1);
        BonRecepDTO bonRecepDto = (BonRecepDTO) resultedObjects.get(2);
        sender.send(topicBonReceptionOnShelfManagement, bonRecepDto.getNumbon(), bonRecepDto);
        return factureBa;

    }

    public BonRecepDTO cancelBonReceptionDepotFrs(String numBon) {

        BonRecepDTO bonRecepDto = factureBaBrancheService.cancelBonReceptionDepotFrs(numBon);
        sender.send(topicBonReceptionOnShelfManagement, numBon, bonRecepDto);
        return bonRecepDto;
    }

    @Override
    public Boolean cancelBonReceptionDepotFrsPermanent(String numBon) {
        BonRecepDTO bonRecepDto = factureBaBrancheService.cancelBonReceptionDepotFrsPermanent(numBon);
        sender.send(topicBonReceptionOnShelfManagement, numBon, bonRecepDto);

        return Boolean.TRUE;
    }

    /**
     * *************************immossssss********************
     */

    public ImmobilisationDTO genererImmoDTO(DetailTransfertCompanyBranch detailTransfertCompanyBranch, ArticleIMMODTO articleIMMO, String codeFournisseur, List<EmplacementDTO> emplacements, LocalDate datBon, String numAffiche) {

        ImmobilisationDTO immoDTO = new ImmobilisationDTO(detailTransfertCompanyBranch.getQuantite(), detailTransfertCompanyBranch.getPrixUnitaire(), 0, numAffiche, articleIMMO.getCodeSaisi());

        immoDTO.setTauxAmortissement(articleIMMO.getTauxAmortissement());
        immoDTO.setAvecNumeroSerie(articleIMMO.getAvecNumeroSerie());
        immoDTO.setEttiquetable(articleIMMO.getEtiquettable());
        immoDTO.setGenererImmobilisation(articleIMMO.getGenererImmobilisation());

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            immoDTO.setDesignation(articleIMMO.getDesignationSec());
            immoDTO.setDesignationSec(articleIMMO.getDesignation());

        } else {
            immoDTO.setDesignation(articleIMMO.getDesignation());
            immoDTO.setDesignationSec(articleIMMO.getDesignationSec());
        }

        immoDTO.setCodeSaisiArticle(articleIMMO.getCodeSaisi());
        if (detailTransfertCompanyBranch.getCodeEmplacement() != null) {
            EmplacementDTO matchingEmplacement = emplacements.stream().filter(x -> x.getCode().equals(detailTransfertCompanyBranch.getCodeEmplacement()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-emplacement", new Throwable(detailTransfertCompanyBranch.getCodeEmplacement().toString())));
            immoDTO.setCodeEmplacement(matchingEmplacement.getCodeSaisi());
        }
        immoDTO.setExiste(Boolean.TRUE);
        immoDTO.setValeurProratat(BigDecimal.ZERO);
        immoDTO.setAutreFrais(BigDecimal.ZERO);
        immoDTO.setValeurAmortissementAnterieur(BigDecimal.ZERO);
        immoDTO.setTauxAmortissement(articleIMMO.getTauxAmortissement());
        immoDTO.setCodeTva(articleIMMO.getCodeTvaAch());
        immoDTO.setNumeroSerie(detailTransfertCompanyBranch.getLotInter());
        immoDTO.setDetaille(articleIMMO.getDetaille());
        immoDTO.setCodeFournisseur(codeFournisseur);
        immoDTO.setFamilleArticle(articleIMMO.getCategorieArticle().getCodeSaisi());
        immoDTO.setTauxIfrs(articleIMMO.getTauxIfrs());
        immoDTO.setTauxAmortFiscale1(articleIMMO.getTauxAmortFiscale1());
        immoDTO.setTauxAmortFiscale2(articleIMMO.getTauxAmortFiscale2());
        immoDTO.setDateFacture(datBon);

        return immoDTO;
    }

    // used to retry save IMMo  when fallback save IMMo  first time save transfertCompanyBranch 
    public ListeImmobilisationDTOWrapper generateImmobilisationSFromTransfertCompanyBranch(String numbonTransfertCompanyBranch) {
        TransfertCompanyBranch transfertCompanyBranch = transfertCompanyBranchService.findOneTransfert(numbonTransfertCompanyBranch);

        Preconditions.checkBusinessLogique(CategorieDepotEnum.IMMO.equals(transfertCompanyBranch.getCategDepot()), "only-immos");
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(transfertCompanyBranch.getImmobilisationGenere()), "immos-already-generated");
        //-------   emplacements et articles   --------/
        Set<Integer> codeArticles = new HashSet();
        Set<Integer> codesEmplacements = new HashSet();
        transfertCompanyBranch.getDetailTransfertCompanyBranchCollection().forEach(detail -> {
            codeArticles.add(detail.getCodeArticle());
            if (detail.getCodeEmplacement() != null) {
                codesEmplacements.add(detail.getCodeEmplacement());
            }
        });

        List<EmplacementDTO> emplacements = transfertCompanyBranch.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(codesEmplacements, null) : new ArrayList();
        if (transfertCompanyBranch.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            Preconditions.checkBusinessLogique(emplacements != null, "error-emplacaments");
        }
        List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(transfertCompanyBranch.getCategDepot(), codeArticles.toArray(new Integer[codeArticles.size()]));

        ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper = new ListeImmobilisationDTOWrapper();
        List<ImmobilisationDTO> immobilisationDTOs = new ArrayList();

        for (DetailTransfertCompanyBranch detailTransfertCompanyBranch : transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()) {
            if (detailTransfertCompanyBranch.getQuantite().compareTo(BigDecimal.ZERO) != 0) {
                ArticleDTO matchingItem = articles.stream()
                        .filter(article -> article.getCode().equals(detailTransfertCompanyBranch.getCodeArticle()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("stock.find-all.missing-article", new Throwable(detailTransfertCompanyBranch.getCodeSaisi())));
                log.debug("***********matching item **********{}", matchingItem);

                ArticleIMMODTO articleIMMO = (ArticleIMMODTO) matchingItem;
                if (articleIMMO.getGenererImmobilisation()) {
                    immobilisationDTOs.add(genererImmoDTO(detailTransfertCompanyBranch, articleIMMO, transfertCompanyBranch.getCodeFournisseur(), emplacements, LocalDate.now(), transfertCompanyBranch.getNumBon().substring(2)));
                }
            }
        }

        listeImmobilisationDTOWrapper.setImmobilisation(immobilisationDTOs);
        ListeImmobilisationDTOWrapper listeImmo = immobilisationService.saveImmo(listeImmobilisationDTOWrapper);
        if (listeImmo != null) {
            transfertCompanyBranch.setImmobilisationGenere(Boolean.TRUE);
        } else {
            transfertCompanyBranch.setImmobilisationGenere(Boolean.FALSE);
        }

        return listeImmobilisationDTOWrapper;
    }

    public Depsto genererDepstoFromDetailTransfertCompanyBranch(DetailTransfertCompanyBranch detailTransfertCompanyBranch, TransfertCompanyBranch transfertCompanyBranch) {

        Depsto depsto = new Depsto();

        depsto.setCodart(detailTransfertCompanyBranch.getCodeArticle());
        depsto.setLotInter(detailTransfertCompanyBranch.getLotInter());
        depsto.setDatPer(detailTransfertCompanyBranch.getDatePeremption());
        depsto.setQte(detailTransfertCompanyBranch.getQuantite());
        log.debug("*****************detailTransfertCompanyBranch: PrixUnitaire()::::{} *********  remise:::: {}************ ", detailTransfertCompanyBranch.getPrixUnitaire(), detailTransfertCompanyBranch.getRemise());
        depsto.setPu(detailTransfertCompanyBranch.getPrixUnitaire().multiply((BigDecimal.valueOf(100).subtract(detailTransfertCompanyBranch.getRemise())).divide(BigDecimal.valueOf(100))));
        depsto.setCoddep(transfertCompanyBranch.getCodeDepot());
        depsto.setCategDepot(transfertCompanyBranch.getCategDepot());
        depsto.setNumBon(transfertCompanyBranch.getNumBon());
        depsto.setCodeTva(detailTransfertCompanyBranch.getCodeTva());
        depsto.setTauxTva(detailTransfertCompanyBranch.getTauxTva());
        depsto.setNumBonOrigin(transfertCompanyBranch.getNumBon());
        if (detailTransfertCompanyBranch.getPrixUnitaire().compareTo(BigDecimal.ZERO) == 0) {
            depsto.setMemo("FREE" + transfertCompanyBranch.getNumBon());
            if (detailTransfertCompanyBranch.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal divisor = BigDecimal.valueOf(100).add(detailTransfertCompanyBranch.getTauxTva()).divide(BigDecimal.valueOf(100));//100+tva/100
                depsto.setPu(detailTransfertCompanyBranch.getBaseTva()
                        .multiply(detailTransfertCompanyBranch.getTauxTva())
                        .divide(new BigDecimal(100)).//baseTva   *0.14-> mntTva gratuite
                        divide(divisor, 5, RoundingMode.CEILING));
            }
        }

        return depsto;
    }

    @Transactional
    public void processPurchaseOrdersOnDeleteRecept(FactureBA bonReception) {

        /**
         * processing the purchase order
         */
        List<Integer> listCodesCA = bonReception.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());// list of received purchase orders
        etatReceptionCAService.deleteByCommandeAchatIn(listCodesCA); // delete the state of the recived purchase orders 
        List<ReceptionDetailCA> receptionDetailCAs = receptionDetailCAService.findByCodesCAIn(listCodesCA);
        List<EtatReceptionCA> partRecivedPurchOrdes = receptionDetailCAs.stream() // recalculate their state 
                .filter(item -> !item.getPk().getReception().equals(bonReception.getNumbon()) && item.getQuantiteReceptione().compareTo(BigDecimal.ZERO) > 0)
                .map(filtredItem -> {
//                    filtredItem.getPk().setReception(null);
                    return new EtatReceptionCA(filtredItem.getPk().getCommandeAchat(), PARTIALLY_RECIVED);
                })
                .collect(Collectors.toList());
        bonReception.getRecivedDetailCA().clear();
        etatReceptionCAService.save(partRecivedPurchOrdes);
    }

    private BonRecepDTO generateReceptionFromTransfertDTO(TransfertCompanyBranchDTO transfertCompanyBranchDTO) {
        BonRecepDTO resultedBonRecepDTO = new BonRecepDTO();
        resultedBonRecepDTO.setCodeFournisseu(transfertCompanyBranchDTO.getCodeFournisseur());
//        resultedBonRecepDTO.setTypbon(transfertCompanyBranch.getTypbon());

        List<MvtstoBADTO> results = transfertCompanyBranchDTO.getDetailTransfertCompanyBranchDTOs()
                .stream()
                .filter(elt -> elt.getPrixUnitaire().compareTo(BigDecimal.ZERO) > 0)
                .map(mvtsto -> {
                    MvtstoBADTO dto = new MvtstoBADTO();
                    dto.setPriuni(mvtsto.getPrixUnitaire());
                    dto.setRefArt(mvtsto.getCodeArticle());
                    dto.setSellingPrice(mvtsto.getPrixVente());
                    dto.setCodeArtFrs(mvtsto.getCodeArticleFournisseur());
                    return dto;
                }).collect(Collectors.toList());

        List<MvtstoBADTO> mvtstosDTOs = new ArrayList(results);
        resultedBonRecepDTO.setDetails(mvtstosDTOs);
        return resultedBonRecepDTO;
    }

    @Override
    public BonRecepDTO ajoutReeceptionSuiteValidationReceptionTemporaire(BonRecepDTO receptionDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ReceptionDetailCA> managingPurchaseOrdersForReception(BonRecepDTO bonRecepDTO, String numBon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ReceptionTemporaireDetailCa> managingPurchaseOrdersForReceptionTemp(ReceptionTemporaireDTO receptionTempDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DetailCommandeAchatDTO> getDetailOfPurchaseOrders(List<Integer> purchaseOrdersCodes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    @Override
//    public BonRecepDTO ajoutBonReception(BonRecepDTO bonReceptionDTO) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    public BonRecepDTO ajoutBonReception(BonRecepDTO bonReceptionDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Depsto genererDepstoFromMvtstoBa(MvtstoBADTO mvtstoBADTODTO, FactureBA factureBA) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ImmobilisationDTO genererImmoDTO(MvtStoBA mvtsto, ArticleIMMODTO articleIMMO, String codeFournisseur, List<EmplacementDTO> emplacements, LocalDate datBon, String numAffiche) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BonRecepDTO updateBonReception(BonRecepDTO bonReceptionDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BonRecepDTO cancelBonReception(String numBon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BonRetourDTO ajoutBonRetour(BonRetourDTO bonRetourDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
