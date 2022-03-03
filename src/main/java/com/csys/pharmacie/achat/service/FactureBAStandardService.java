/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.EtatReceptionCA;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.FcptfrsPH;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.MvtStoBAPK;
import com.csys.pharmacie.achat.domain.PieceJointeReception;
import com.csys.pharmacie.achat.domain.Receiving;
import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.pharmacie.achat.domain.ReceptionTemporaire;
import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCa;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticleIMMODTO;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.BonRetourDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatLazyDTO;
import com.csys.pharmacie.achat.dto.CommandeAchatModeReglementDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.DetailCommandeAchatDTO;
import com.csys.pharmacie.achat.dto.DetailReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.EmplacementDTO;
import com.csys.pharmacie.achat.dto.FactureBADTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.achat.dto.ReceptionTemporaireDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.factory.AvoirFournisseurFactory;
import com.csys.pharmacie.achat.factory.CommandeAchatFactory;
import com.csys.pharmacie.achat.factory.DetailReceptionFactory;
import com.csys.pharmacie.achat.factory.FactureBAFactory;
import com.csys.pharmacie.achat.factory.MvtstoBAFactory;
import com.csys.pharmacie.achat.repository.AvoirFournisseurRepository;
import com.csys.pharmacie.achat.repository.FactureBARepository;
import com.csys.pharmacie.achat.repository.ReceptionDetailCARepository;
import com.csys.pharmacie.achat.repository.RetourPerimeRepository;
import static com.csys.pharmacie.achat.service.ReceptionTemporaireService.LANGUAGE_SEC;
import com.csys.pharmacie.client.dto.ImmobilisationDTO;
import com.csys.pharmacie.client.dto.ListeImmobilisationDTOWrapper;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import static com.csys.pharmacie.helper.Action.ADD;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import static com.csys.pharmacie.helper.CategorieDepotEnum.IMMO;
import static com.csys.pharmacie.helper.CategorieDepotEnum.PH;
import static com.csys.pharmacie.helper.CategorieDepotEnum.UU;
import com.csys.pharmacie.helper.EmailDTO;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.PurchaseOrderReceptionState;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.NOT_RECEIVED;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.PARTIALLY_RECIVED;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.RECEIVED;
import com.csys.pharmacie.helper.TypeBonEnum;
import static com.csys.pharmacie.helper.TypeBonEnum.RT;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.repository.DepstoRepository;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.transfert.service.CliniSysService;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import com.csys.pharmacie.vente.service.PricingService;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrateur
 */
@Service
@Transactional
public class FactureBAStandardService extends FactureBAService {

    public FactureBAStandardService(FactureBARepository factureBARepository, ReceptionDetailCARepository receptionDetailCARepository, ParamService paramService, FcptFrsPHService fcptFrsPHService, StockService stockService, DemandeServiceClient demandeServiceClient, ReceptionDetailCAService receptionDetailCAService, ReceptionTemporaireDetailCaService receptionTemporaireDetailCaService,@Lazy PricingService pricingService, ParamAchatServiceClient paramAchatServiceClient, ReceivingService receivingService, EtatReceptionCAService etatReceptionCAService, ParamServiceClient paramServiceClient, MvtStoBAService mvtStoBAService, AjustementRetourFournisseurService ajustementRetourFournisseurService, RetourPerimeRepository retourPerimeRepository, AvoirFournisseurFactory avoirFournisseurFactory, AvoirFournisseurRepository avoirfournisseurRepository, InventaireService inventaireService, ImmobilisationService immobilisationService, DepstoRepository depstoRepository, ReceptionTemporaireService receptionTemporaireService, MessageSource messages, CliniSysService cliniSysService) {
        super(factureBARepository, receptionDetailCARepository, paramService, fcptFrsPHService, stockService, demandeServiceClient, receptionDetailCAService, receptionTemporaireDetailCaService, pricingService, paramAchatServiceClient, receivingService, etatReceptionCAService, paramServiceClient, mvtStoBAService, ajustementRetourFournisseurService, retourPerimeRepository, avoirFournisseurFactory, avoirfournisseurRepository, inventaireService, immobilisationService, depstoRepository, receptionTemporaireService, messages, cliniSysService);
    }

    @Transactional
    @Override
    public BonRecepDTO ajoutBonReception(BonRecepDTO bonReceptionDTO) {

        log.debug("ajout reception standard");
        Receiving receiving = receivingService.findReceiving(bonReceptionDTO.getReceivingCode());
        Preconditions.checkBusinessLogique(receiving != null, "reception.add.missing-receiving");
        Preconditions.checkBusinessLogique(receiving.getDateAnnule() == null, "reception.add.canceld-receiving");
//        Preconditions.checkBusinessLogique(receiving.getDateValidate() == null, "reception.add.validated-receiving");  //juste test ne pas commenter 

        //------- Controle commandes achats--------/
//        List<Integer> codeCommandeAchat = receiving.getReceivingCommandeList().stream().map(item -> item.getReceivingCommandePK().getCommandeParamAchat()).distinct().collect(Collectors.toList());
//        log.debug("listeCodeCommande{}", codeCommandeAchat);
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
        Set<Integer> categArticleIDs = new HashSet();
        articles.forEach(item -> {
            Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(item.getAnnule()) && !(Boolean.TRUE.equals(item.getStopped())), "item.stopped", item.getCodeSaisi());
            categArticleIDs.add(item.getCategorieArticle().getCode());
        });
        //-------  emplacements immo--------/  
        List<EmplacementDTO> emplacements = bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(codesEmplacements,null) : new ArrayList();
        if (bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            Preconditions.checkBusinessLogique(emplacements != null, "error-emplacaments");
        }

        //-------  check inventory--------/    
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(new ArrayList(categArticleIDs), bonReceptionDTO.getCoddep());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = articles.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }
//        bonReceptionDTO.setNumbon(numBon);
        FactureBA bonRecep = FactureBAFactory.factureBADTOTOFactureBA(bonReceptionDTO);
        bonRecep.setAutomatique(bonReceptionDTO.getAutomatique());
        bonRecep.setNumbon(numBon);

        //-------  fournisseur --------/
        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(bonReceptionDTO.getFournisseur().getCode());
        Preconditions.checkBusinessLogique(!fournisseurDTO.getDesignation().equals("fournisseur.deleted"), "Fournisseur avec code : " + bonReceptionDTO.getFournisseur().getCode() + " est introuvable");
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseurDTO.getAnnule()) && !(Boolean.TRUE.equals(fournisseurDTO.getStopped())), "fournisseur.stopped", fournisseurDTO.getCode());
        bonRecep.setCodfrs(fournisseurDTO.getCode());
        bonRecep.setDateDebutExoneration(fournisseurDTO.getDateDebutExenoration());
        bonRecep.setDateFinExenoration(fournisseurDTO.getDateFinExenoration());
        bonRecep.setIntegrer(Boolean.FALSE);
        //-------  depot --------/
        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(bonReceptionDTO.getCoddep());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + bonReceptionDTO.getCoddep() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getCode() + "] est un dépot fournisseur");
        bonRecep.setCoddep(depotd.getCode());
        //-------  /////////////DETAILS//////////--------/    
        List<MvtStoBA> details = new ArrayList<>();
        String numordre1 = "0001";
        Date date = new Date(3000, 01, 01);
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper = new ListeImmobilisationDTOWrapper();
        List<ImmobilisationDTO> immobilisationDTOs = new ArrayList();
        List<Depsto> depstos = new ArrayList<>();

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
                    mvtstoBA.setOldCodTva(5);
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
                //depsto 
                Depsto depsto = genererDepstoFromMvtstoBa(detailReceptionDTO, bonRecep);
                depsto.setIsCapitalize(detailReceptionDTO.getIsCapitalize());
                depsto.setUnite(matchingItem.getCodeUnite());
                depstos.add(depsto);

                if (bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                    mvtstoBA.setDatPer(localDate);
                    depsto.setDatPer(localDate);
                    if (detailReceptionDTO.getLotInter() == null) {
                        mvtstoBA.setLotInter("-");
                        depsto.setLotInter("-");
                    }
                    mvtstoBA.setCodeEmplacement(detailReceptionDTO.getCodeEmplacement());
                    depsto.setCodeEmplacement(detailReceptionDTO.getCodeEmplacement());

                    ArticleIMMODTO articleIMMO = (ArticleIMMODTO) matchingItem;
                    if (articleIMMO.getGenererImmobilisation()) {
                        immobilisationDTOs.add(genererImmoDTO(mvtstoBA, articleIMMO, bonReceptionDTO.getFournisseur().getCode(), emplacements, LocalDate.now(), bonRecep.getNumbon().substring(2)));
                    }
                }
            }
        }
        depstoRepository.save(depstos);
        depstoRepository.flush();
        bonRecep.setDetailFactureBACollection(details);

        //-------  documents--------/
        bonRecep.setPiecesJointes(bonReceptionDTO.getAttachedFilesIDs().stream().map(id -> new PieceJointeReception(id.toString(), bonRecep)).collect(toList()));
        //-------save immobilisations --------/    
        if (bonReceptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO) && !immobilisationDTOs.isEmpty()) {
            listeImmobilisationDTOWrapper.setImmobilisation(immobilisationDTOs);
            ListeImmobilisationDTOWrapper listeImmo = immobilisationService.saveImmo(listeImmobilisationDTOWrapper);
            Preconditions.checkBusinessLogique(listeImmo != null, "error-saving-immo");
        }
        //-------calcul tva et bases tva --------/   
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
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

        stockService.saveDepsto(depstos);

        //-------PMP ,prix de reference, update dernier prix achat --------/  
        pricingService.updatePricesAfterReception(result, result.getCategDepot(), ADD);
        paramAchatServiceClient.ArticlePriceUpdateAndArticleFrsPriceUpdate(FactureBAFactory.dtoToBonRecepDTOforUpdatePrices(bonReceptionDTO, result.getCodfrs()));

        paramService.updateCompteurPharmacie(bonReceptionDTO.getCategDepot(), bonReceptionDTO.getTypeBon());

        //-------return full dto pour affichage immo prelevement automatique --------/ 
        if (result.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            BonRecepDTO reultDTO = FactureBAFactory.factureBAToBonRecepDTO(result);
            List<MvtstoBADTO> detailsReception = new ArrayList();
            result.getDetailFactureBACollection().forEach(mvtstoBA -> {
                MvtstoBADTO detailRecep = new MvtstoBADTO();
                DetailReceptionFactory.toDTO(mvtstoBA, detailRecep);
                detailRecep.setQteReturned(mvtstoBA.getQuantite().subtract(mvtstoBA.getQtecom()));
                detailsReception.add(detailRecep);
            });
            reultDTO.setDetails((List) detailsReception);
            reultDTO.setDesignationDepot(depotd.getDesdep());
            reultDTO.setFournisseur(fournisseurDTO);
            reultDTO.getDetails().forEach(item -> {
                EmplacementDTO emplacement = emplacements.stream()
                        .filter(x -> x.getCode().equals(item.getCodeEmplacement()))
                        .findFirst().orElse(null);
                if (item.getCodeEmplacement() != null) {
                    item.setDesignationEmplacement(emplacement.getDesignation());
                    item.setCodeDepartementEmplacement(emplacement.getCodeDepartement().getCode());
                    item.setDesigationDepartementEmplacement(emplacement.getCodeDepartement().getDesignation());
                }

            });
            return reultDTO;
        }

        BonRecepDTO reultDTO = FactureBAFactory.factureBAToBonRecepDTO(result);

        return reultDTO;

    }

    public FactureBA ajoutBonReceptionDepotFrs(Facture facture, DepotDTO depotPrincipale, FournisseurDTO fournisseurDTO, Boolean appliquerExoneration, String exoneration) {

        FactureBA bonRecep = new FactureBA();
        bonRecep.setRefFrs("");
        bonRecep.setCodfrs(facture.getCodfrs());
        bonRecep.setDatRefFrs(facture.getDatbon().toLocalDate());
        bonRecep.setCategDepot(facture.getCategDepot());
        bonRecep.setMemop(facture.getMemop());

        bonRecep.setTypbon(TypeBonEnum.BA);
        bonRecep.setCoddep(depotPrincipale.getCode());
//        f.setSatisf("NST");
        bonRecep.setAutomatique(Boolean.TRUE);

        bonRecep.setValrem(BigDecimal.ZERO);
        bonRecep.setFournisseurExonere(Boolean.FALSE);
        if (exoneration.toUpperCase().equals("O")) {
            if (appliquerExoneration != null && appliquerExoneration.equals(Boolean.TRUE)) {
                bonRecep.setFournisseurExonere(Boolean.TRUE);
                bonRecep.setDateDebutExoneration(fournisseurDTO.getDateDebutExenoration());
                bonRecep.setDateFinExenoration(fournisseurDTO.getDateFinExenoration());
            }
        }
        List<MvtStoBA> details = new ArrayList<>();
        String numordre = "0001";
        for (Mvtsto mvtsto : facture.getMvtstoCollection()) {
            if (mvtsto.getQuantite().compareTo(BigDecimal.ZERO) != 0) {
                MvtStoBA mvtstoBA = new MvtStoBA();

                String ordre = "BA" + numordre;

                MvtStoBAPK pk = new MvtStoBAPK();
                pk.setCodart(mvtsto.getMvtstoPK().getCodart());
                pk.setNumbon(bonRecep.getNumbon());
                pk.setNumordre(ordre);
                mvtstoBA.setPk(pk);

                mvtstoBA.setTypbon("BA");
                mvtstoBA.setQuantite(mvtsto.getQuantite());

                //Prix
                mvtstoBA.setPriuni(mvtsto.getPriach());
                mvtstoBA.setRemise(BigDecimal.ZERO);
                mvtstoBA.setMontht(mvtstoBA.getPriuni().multiply(mvtstoBA.getQuantite()));

                mvtstoBA.setCodtva(mvtsto.getCodTvaAch());
                mvtstoBA.setTautva(mvtsto.getTauTvaAch());
                mvtstoBA.setOldTauTva(mvtsto.getTauTvaAch());
                mvtstoBA.setOldCodTva(mvtsto.getCodTvaAch());
                if (bonRecep.getFournisseurExonere()) {
                    mvtstoBA.setCodtva(5);
                    mvtstoBA.setTautva(BigDecimal.ZERO);
                    mvtstoBA.setOldTauTva(mvtsto.getTauTvaAch());
                    mvtstoBA.setOldCodTva(mvtsto.getCodTvaAch());

                }
                mvtstoBA.setCategDepot(facture.getCategDepot());
                mvtstoBA.setCoddep(facture.getCoddep());
                mvtstoBA.setDesart(mvtsto.getDesart());
                mvtstoBA.setDesArtSec(mvtsto.getDesArtSec());
                mvtstoBA.setCodeSaisi(mvtsto.getCodeSaisi());
                mvtstoBA.setQtecom(mvtsto.getQuantite());
                mvtstoBA.setLotInter(mvtsto.getLotInter());
                mvtstoBA.setIsPrixReference(false);
                mvtstoBA.setDatPer(mvtsto.getDatPer());
                mvtstoBA.setCodeUnite(mvtsto.getUnite());
                mvtstoBA.setBaseTva(BigDecimal.ZERO);
                mvtstoBA.setFactureBA(bonRecep);
                details.add(mvtstoBA);
                numordre = Helper.IncrementString(numordre, 4);
            }
        }
        bonRecep.setDetailFactureBACollection(details);
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        bonRecep.calcul(listTvas);
        String numBon = paramService.getcompteur(facture.getCategDepot(), TypeBonEnum.BA);
        bonRecep.setNumbon(numBon);
        bonRecep.setIntegrer(Boolean.FALSE);
        CommandeAchatDTO commandeAchatDTO = demandeServiceClient.createCommandeAchat(CommandeAchatFactory.commandeachatToCommandeAchatDTO(bonRecep, fournisseurDTO));
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

        FactureBA result = factureBARepository.save(bonRecep);
        EtatReceptionCA etatReceptionCA = new EtatReceptionCA(commandeAchatDTO.getCode(), RECEIVED);
        etatReceptionCAService.save(etatReceptionCA);
        fcptFrsPHService.addFcptFrsOnReception(bonRecep);
        paramService.updateCompteurPharmacie(facture.getCategDepot(), TypeBonEnum.BA);
        return result;
    }

    @Transactional
    public BonRecepDTO ajoutReeceptionSuiteValidationReceptionTemporaire(BonRecepDTO receptionDTO) {
        ReceptionTemporaire receptionTemp = receptionTemporaireService.findReceptionTemporaire(receptionDTO.getNumbon());
        Preconditions.checkBusinessLogique(!receptionTemp.isIsValidated(), "Réception [" + receptionTemp.getNumbon() + "] est validé");
        log.debug("validate reception temporaire ");
        // fournisseur
        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(receptionDTO.getFournisseur().getCode());
        log.debug("fourniss est {}", fournisseurDTO);
        Preconditions.checkBusinessLogique(!fournisseurDTO.getDesignation().equals("fournisseur.deleted"), "Fournisseur avec code : " + receptionDTO.getCodeFournisseu() + " est introuvable");
        FactureBA reception = FactureBAFactory.factureBADTOTOFactureBA(receptionDTO);
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseurDTO.getAnnule()) && !(Boolean.TRUE.equals(fournisseurDTO.getStopped())), "item.stopped", fournisseurDTO.getCode());
        reception.setIntegrer(Boolean.FALSE);
        log.debug("reception {}", reception.toString());
        reception.setCodfrs(fournisseurDTO.getCode());
        reception.setDateDebutExoneration(fournisseurDTO.getDateDebutExenoration());
        reception.setDateFinExenoration(fournisseurDTO.getDateFinExenoration());

        String numBon = paramService.getcompteur(receptionDTO.getCategDepot(), TypeBonEnum.BA);
        reception.setNumbon(numBon);
        reception.setIntegrer(Boolean.FALSE);
        //depot
        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(receptionDTO.getCoddep());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + receptionDTO.getCoddep() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getCode() + "] est un dépot fournisseur");
        reception.setCoddep(depotd.getCode());
//        reception.setSatisf("NST");

        // documents 
        //  f.setPiecesJointes(receptionTemporaireDTO.getAttachedilesIDs().stream().map(id -> new PieceJointeReception(id.toString(), f)).collect(toList()));
        //details
        log.debug("receptionDTO et {}", receptionDTO.getDetails().toString());
        Set<Integer> codesEmplacements = new HashSet();
        Set<Integer> codeArticles = new HashSet();

        receptionDTO.getDetails().forEach(detail -> {

            codeArticles.add(detail.getRefArt());
            if (detail.getCodeEmplacement() != null) {
                codesEmplacements.add(detail.getCodeEmplacement());
            }
        });
        log.debug("codeArticles sont {}", codeArticles);
        List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(receptionDTO.getCategDepot(), codeArticles.toArray(new Integer[codeArticles.size()]));
        List<EmplacementDTO> emplacements = receptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(codesEmplacements,null) : new ArrayList();
        if (receptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            Preconditions.checkBusinessLogique(emplacements != null, "error-emplacaments");
        }

        // check for inventory 
        List<Integer> categArticleIDs = articles.stream().map(item -> item.getCategorieArticle().getCode()).collect(toList());
        List<Integer> categArticleUnderInventory = inventaireService.checkCategorieIsInventorie(categArticleIDs, receptionDTO.getCoddep());
        if (categArticleUnderInventory.size() > 0) {
            List<String> articlesUnderInventory = articles.stream().filter(item -> categArticleUnderInventory.contains(item.getCategorieArticle().getCode())).map(ArticleDTO::getCodeSaisi).collect(toList());
            throw new IllegalBusinessLogiqueException("article-under-inventory", new Throwable(articlesUnderInventory.toString()));
        }
        log.debug("codeArticles sont {}", categArticleUnderInventory);
        List<MvtStoBA> details = new ArrayList<>();
        List<Depsto> depstos = new ArrayList<>();

        String numordre1 = "0001";
        Date date = new Date(3000, 01, 01);
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ListeImmobilisationDTOWrapper listeImmobilisationDTOWrapper = new ListeImmobilisationDTOWrapper();
        List<ImmobilisationDTO> Immobilisations = new ArrayList();

        for (MvtstoBADTO detailReceptionDTO : receptionDTO.getDetails()) {
            if (detailReceptionDTO.getQuantite().compareTo(BigDecimal.ZERO) != 0) {
                ArticleDTO matchingItem = articles.stream()
                        .filter(article -> article.getCode().equals(detailReceptionDTO.getRefArt()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("stock.find-all.missing-article", new Throwable(detailReceptionDTO.getCodeSaisi())));

                String ordre = "BA" + numordre1;
                MvtStoBA mvtstoBA = MvtstoBAFactory.MvtStoBADTOTOMvtstoBA(detailReceptionDTO, ordre, receptionDTO, numBon);
                mvtstoBA.setTypbon("BA");
                if (!reception.getFournisseurExonere()) {
                    mvtstoBA.setOldCodTva(5);
                    mvtstoBA.setOldTauTva(BigDecimal.ZERO);
                } else {
                    mvtstoBA.setOldCodTva(detailReceptionDTO.getOldCodTva());
                    mvtstoBA.setOldTauTva(detailReceptionDTO.getOldTauTva());
                }

                if (receptionDTO.getCategDepot().equals(PH)) {
                    log.debug("ctrl Ph");
                    Preconditions.checkBusinessLogique(detailReceptionDTO.getSellingPrice() != null, "reception.add.missing-selling-price", detailReceptionDTO.getCodeSaisi());
                    mvtstoBA.setPrixVente(detailReceptionDTO.getSellingPrice());
                }
                if (receptionDTO.getCategDepot().equals(UU)) {
                    log.debug("ctrl UU ET PRIX REF EST {}", detailReceptionDTO.getIsPrixRef());
                    Preconditions.checkBusinessLogique(detailReceptionDTO.getIsPrixRef() != null, "reception.add.missing-selling-price", detailReceptionDTO.getCodeSaisi());
                    mvtstoBA.setIsPrixReference(detailReceptionDTO.getIsPrixRef());
                }
                mvtstoBA.setCodeUnite(matchingItem.getCodeUnite());
                mvtstoBA.setAncienPrixAchat(matchingItem.getPrixAchat());
                mvtstoBA.setCodtva(0);
                mvtstoBA.setFactureBA(reception);
                details.add(mvtstoBA);
                Depsto depsto = genererDepstoFromMvtstoBa(detailReceptionDTO, reception);
                depsto.setUnite(matchingItem.getCodeUnite());
                depsto.setIsCapitalize(detailReceptionDTO.getIsCapitalize());
                if (receptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
                    mvtstoBA.setDatPer(localDate);
                    depsto.setDatPer(localDate);
                    if (detailReceptionDTO.getLotInter() == null) {
                        mvtstoBA.setLotInter("-");
                        depsto.setLotInter("-");
                    }
                    mvtstoBA.setCodeEmplacement(detailReceptionDTO.getCodeEmplacement());
                    depsto.setCodeEmplacement(detailReceptionDTO.getCodeEmplacement());

                    ArticleIMMODTO articleIMMO = (ArticleIMMODTO) matchingItem;
                    Immobilisations.add(genererImmoDTO(mvtstoBA, articleIMMO, receptionDTO.getFournisseur().getCode(), emplacements, reception.getDatRefFrs(), reception.getRefFrs()));

                }

                depstos.add(depsto);
                numordre1 = Helper.IncrementString(numordre1, 4);
            }
        }

        ////////////////IMMO/////////
        reception.setDetailFactureBACollection(details);
        // log.debug("Immobilisations sont {}",Immobilisations);
        if (receptionDTO.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
            listeImmobilisationDTOWrapper.setImmobilisation(Immobilisations);
            ListeImmobilisationDTOWrapper listeImmo = immobilisationService.saveImmo(listeImmobilisationDTOWrapper);
            Preconditions.checkBusinessLogique(listeImmo != null, "error-saving-immo");
        }

        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        log.debug("listTvas sont {}", listTvas.toString());
        reception.calcul(listTvas);

        depstoRepository.save(depstos);
        depstoRepository.flush();

        Receiving receiving = receivingService.findReceiving(receptionDTO.getReceivingCode());
        Preconditions.checkBusinessLogique(receiving != null, "reception.add.missing-receiving");
        Preconditions.checkBusinessLogique(receiving.getDateAnnule() == null, "reception.add.canceld-receiving");
        reception.setReceiving(receiving);

        if (receptionDTO.getAttachedFilesIDs() != null) {
            reception.setPiecesJointes(receptionDTO.getAttachedFilesIDs().stream().map(id -> new PieceJointeReception(id.toString(), reception)).collect(toList()));
        }
        reception.setNumAfficheRecetionTemporaire(receptionTemp.getNumaffiche());

        List<ReceptionDetailCA> listeReceptionDetailCa = managingPurchaseOrdersForReception(receptionDTO, numBon);
        listeReceptionDetailCa.forEach(elt -> elt.setReception(reception));
        reception.setRecivedDetailCA(listeReceptionDetailCa);

        FactureBA result = factureBARepository.save(reception);
        fcptFrsPHService.addFcptFrsOnReception(reception);
        stockService.saveDepsto(depstos);

        //PMP ,prix reference
        pricingService.updatePricesAfterReception(result, result.getCategDepot(), ADD);
        paramService.updateCompteurPharmacie(receptionDTO.getCategDepot(), TypeBonEnum.BA);
        //dernier prix achat
        paramAchatServiceClient.ArticlePriceUpdateAndArticleFrsPriceUpdate(FactureBAFactory.dtoToBonRecepDTOforUpdatePrices(receptionDTO, result.getCodfrs()));

        BonRecepDTO reultDTO = FactureBAFactory.receptionTemporaireToBonRecepDTOEager(result);
        if (result.getCategDepot().equals(CategorieDepotEnum.IMMO)) {

            reultDTO.getDetails().forEach(item -> {
                EmplacementDTO emplacement = emplacements.stream()
                        .filter(x -> x.getCode().equals(item.getCodeEmplacement()))
                        .findFirst().orElse(null);
                if (item.getCodeEmplacement() != null) {
                    item.setDesignationEmplacement(emplacement.getDesignation());
                    item.setCodeDepartementEmplacement(emplacement.getCodeDepartement().getCode());
                    item.setDesigationDepartementEmplacement(emplacement.getCodeDepartement().getDesignation());
                }

            });
        }

        receptionTemp.setFactureBA(result);
        receptionTemp.setIsTemporaire(false);
        receptionTemp.setIsValidated(Boolean.TRUE);

        return reultDTO;
    }

    public ImmobilisationDTO genererImmoDTO(MvtStoBA mvtsto, ArticleIMMODTO articleIMMO, String codeFournisseur, List<EmplacementDTO> emplacements, LocalDate datBon, String numAffiche) {

        ImmobilisationDTO immoDTO = new ImmobilisationDTO(mvtsto.getQuantite(), mvtsto.getPriuni(), 0, numAffiche, articleIMMO.getCodeSaisi());

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
        if (mvtsto.getCodeEmplacement() != null) {
            EmplacementDTO matchingEmplacement = emplacements.stream().filter(x -> x.getCode().equals(mvtsto.getCodeEmplacement()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-emplacement", new Throwable(mvtsto.getCodeEmplacement().toString())));
            immoDTO.setCodeEmplacement(matchingEmplacement.getCodeSaisi());
        }
        immoDTO.setExiste(Boolean.TRUE);
        immoDTO.setValeurProratat(BigDecimal.ZERO);
        immoDTO.setAutreFrais(BigDecimal.ZERO);
        immoDTO.setValeurAmortissementAnterieur(BigDecimal.ZERO);
        immoDTO.setTauxAmortissement(articleIMMO.getTauxAmortissement());
        immoDTO.setCodeTva(articleIMMO.getCodeTvaAch());
        immoDTO.setNumeroSerie(mvtsto.getLotInter());
        immoDTO.setDetaille(articleIMMO.getDetaille());
        immoDTO.setCodeFournisseur(codeFournisseur);
        immoDTO.setFamilleArticle(articleIMMO.getCategorieArticle().getCodeSaisi());
        immoDTO.setTauxIfrs(articleIMMO.getTauxIfrs());
        immoDTO.setTauxAmortFiscale1(articleIMMO.getTauxAmortFiscale1());
        immoDTO.setTauxAmortFiscale2(articleIMMO.getTauxAmortFiscale2());
        immoDTO.setDateFacture(datBon);

        return immoDTO;
    }

    public Depsto genererDepstoFromMvtstoBa(MvtstoBADTO mvtstoBADTODTO, FactureBA factureBA) {

        Depsto depsto = new Depsto();

        depsto.setCodart(mvtstoBADTODTO.getRefArt());
        depsto.setLotInter(mvtstoBADTODTO.getLotInter());
        depsto.setDatPer(mvtstoBADTODTO.getDatPer());
        depsto.setQte(mvtstoBADTODTO.getQuantite());
        depsto.setPu(mvtstoBADTODTO.getPriuni().multiply((BigDecimal.valueOf(100).subtract(mvtstoBADTODTO.getRemise())).divide(BigDecimal.valueOf(100))));
        depsto.setCoddep(factureBA.getCoddep());
        depsto.setCategDepot(factureBA.getCategDepot());
        depsto.setNumBon(factureBA.getNumbon());
        depsto.setCodeTva(mvtstoBADTODTO.getCodTVA());
        depsto.setTauxTva(mvtstoBADTODTO.getTauTVA());
        depsto.setNumBonOrigin(factureBA.getNumbon());
        if (mvtstoBADTODTO.getPriuni().compareTo(BigDecimal.ZERO) == 0) {
            depsto.setMemo("FREE" + factureBA.getNumbon());
            if (mvtstoBADTODTO.getBaseTva().compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal divisor = BigDecimal.valueOf(100).add(mvtstoBADTODTO.getTauTVA()).divide(BigDecimal.valueOf(100));//100+tva/100
                depsto.setPu(mvtstoBADTODTO.getBaseTva()
                        .multiply(mvtstoBADTODTO.getTauTVA())
                        .divide(new BigDecimal(100)).//baseTva   *0.14-> mntTva gratuite
                        divide(divisor, 5, RoundingMode.CEILING));
            }
        }

        return depsto;
    }

    @Transactional
    public BonRecepDTO updateBonReception(BonRecepDTO bonReceptionDTO) {

        log.debug("updating  factureba {}", bonReceptionDTO.getNumbon());
        FactureBA inBaseReception = factureBARepository.findOne(bonReceptionDTO.getNumbon());
        Preconditions.checkBusinessLogique(inBaseReception != null, "reception.update.notFound");
        Preconditions.checkBusinessLogique(inBaseReception.getDatAnnul() == null, "reception.update.canceled-reception");
        if (inBaseReception.getAutomatique() != null) {
            Preconditions.checkBusinessLogique(!inBaseReception.getAutomatique(), "reception.update.reception-automatique");
        }
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(inBaseReception.getIntegrer()), "facture-deja-integre");
        Preconditions.checkBusinessLogique(!isReceptionUsed(bonReceptionDTO.getNumbon()), "reception.update.used-reception");
        Receiving receiving = receivingService.findReceiving(bonReceptionDTO.getReceivingCode());
        Preconditions.checkBusinessLogique(receiving != null, "reception.add.missing-receiving");
        FcptfrsPH fcptFrs = fcptFrsPHService.findFirstByNumBon(bonReceptionDTO.getNumbon());
//        // verification fcptFRS Reste= Credit 
//        Preconditions.checkBusinessLogique(fcptFrs.getCredit().equals(fcptFrs.getReste()), "reception-is-paid");
        Preconditions.checkBusinessLogique(inBaseReception.getFactureBonReception() == null, "invoiced-reception");
        stockService.deleteByNumBon(bonReceptionDTO.getNumbon());

        FactureBA factureBa = FactureBAFactory.FactureBADTOTOFactureBAForUpdate(bonReceptionDTO, inBaseReception);

        //-------  documents--------/
        if (factureBa.getPiecesJointes() != null) {
            List<PieceJointeReception> newPieceJointes = bonReceptionDTO.getAttachedFilesIDs().stream().map(id -> new PieceJointeReception(id.toString(), factureBa)).collect(toList());
            factureBa.getPiecesJointes().retainAll(newPieceJointes);
            factureBa.getPiecesJointes().addAll(newPieceJointes);
        } else {
            factureBa.setPiecesJointes(bonReceptionDTO.getAttachedFilesIDs().stream().map(id -> new PieceJointeReception(id.toString(), factureBa)).collect(toList()));
        }
//        List<MvtstoBADTO> listdto = dto.getDetails();
        Set<Integer> codeArticles = bonReceptionDTO.getDetails().stream().map(item -> item.getRefArt()).collect(Collectors.toSet());

        List<ArticleDTO> listeArticleDTOs = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(bonReceptionDTO.getCategDepot(), codeArticles.toArray(new Integer[codeArticles.size()]));
//        List<MvtStoBA> details = f.getDetailFactureBACollection();
//        log.debug("les details sont{}:", f.getDetailFactureBACollection());
        List<Depsto> depstos = new ArrayList();
        for (MvtstoBADTO mvtstoDTO : bonReceptionDTO.getDetails()) {
            MvtStoBA newUpdatedMvtStoBA = new MvtStoBA();

            for (MvtStoBA m : inBaseReception.getDetailFactureBACollection()) {

                if (m.getPk().getCodart().equals(mvtstoDTO.getRefArt()) && m.getPk().getNumordre().equalsIgnoreCase(mvtstoDTO.getNumOrdre())) {
                    newUpdatedMvtStoBA = m;
                    break;
                }
            }

            newUpdatedMvtStoBA.setDatPer(mvtstoDTO.getDatPer());
            newUpdatedMvtStoBA.setLotInter(mvtstoDTO.getLotInter());
            newUpdatedMvtStoBA.setPriuni(mvtstoDTO.getPriuni());
            newUpdatedMvtStoBA.setRemise(mvtstoDTO.getRemise());
            newUpdatedMvtStoBA.setMontht(mvtstoDTO.getMontht());
            newUpdatedMvtStoBA.setTautva(mvtstoDTO.getTauTVA());
            newUpdatedMvtStoBA.setCodtva(mvtstoDTO.getCodTVA());
            if (factureBa.getFournisseurExonere()) {
                newUpdatedMvtStoBA.setOldCodTva(5);
                newUpdatedMvtStoBA.setOldTauTva(BigDecimal.ZERO);
            } else {
                newUpdatedMvtStoBA.setOldCodTva(mvtstoDTO.getOldCodTva());
                newUpdatedMvtStoBA.setOldTauTva(mvtstoDTO.getOldTauTva());
            }
            newUpdatedMvtStoBA.setBaseTva(mvtstoDTO.getBaseTva());
            if (bonReceptionDTO.getCategDepot().equals(PH)) {
                Preconditions.checkBusinessLogique(mvtstoDTO.getSellingPrice() != null, "reception.add.missing-selling-price", mvtstoDTO.getCodeSaisi());
                newUpdatedMvtStoBA.setPrixVente(mvtstoDTO.getSellingPrice());
            }
            newUpdatedMvtStoBA.setIsPrixReference(mvtstoDTO.getIsPrixRef());

//            details.add(mvtstoBA);
            ArticleDTO matchingItem = listeArticleDTOs.stream()
                    .filter(article -> article.getCode().equals(mvtstoDTO.getRefArt()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("stock.find-all.missing-article", new Throwable(mvtstoDTO.getCodeSaisi())));
            Depsto depsto = genererDepstoFromMvtstoBa(mvtstoDTO, factureBa);
            depsto.setUnite(matchingItem.getCodeUnite());
            depstos.add(depsto);

        }

        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        factureBa.calcul(listTvas);

//        return map;
//    }
        stockService.saveDepsto(depstos);
        FactureBA result = factureBARepository.save(inBaseReception);
        fcptFrsPHService.updateFcptFrsOnReception(fcptFrs, inBaseReception);
//        pricingService.updatePricesAfterReception(result, result.getCategDepot(), ADD);
//        paramAchatServiceClient.ArticlePriceUpdateAndArticleFrsPriceUpdate(factureBAFactory.dtoToBonRecepDTOforUpdatePrices(bDto, result.getCodfrs()));
        return FactureBAFactory.factureBAToBonRecepDTO(result);
    }

    /**
     * Treating the purchases orders and it's details that will be used for
     * creating the reception
     *
     * @param bonRecepDTO
     * @param numBon
     * @return
     */
    ////************************** satisfaction bon commandes*********************///
    @Transactional
    public List<ReceptionDetailCA> managingPurchaseOrdersForReception(BonRecepDTO bonRecepDTO, String numBon) {
        List<DetailCommandeAchatDTO> purchaseOrdersDetails = getDetailOfPurchaseOrders(bonRecepDTO.getPurchaseOrdersCodes());//detail commande +historique des reception et recep temp faite sur cette commande 100/recep 80 qterest =20
        List<ReceptionDetailCA> listeReceptionDetailCa = processPurchaseOrders(bonRecepDTO, purchaseOrdersDetails, numBon);//qteRest c1 20= recep --10 ==> qte rest :10
        return listeReceptionDetailCa;
    }
//adding preaddReceptionTemp

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
    public List<ReceptionTemporaireDetailCa> managingPurchaseOrdersForReceptionTemp(ReceptionTemporaireDTO receptionTempDTO) {
        List<DetailCommandeAchatDTO> purchaseOrdersDetails = getDetailOfPurchaseOrders(receptionTempDTO.getPurchaseOrdersCodes());//detail commande +historique des reception et recep temp faite sur cette commande 100/recep 80 qterest =20
//        FactureBADTO factureBADTO=ReceptionTemporaireFactory.receptiontemporaireDTOTofactureDto(receptionTempDTO);//qteRest c1 20= recep temp --10 ==> qte rest :10
        List<ReceptionTemporaireDetailCa> listeReceptionDetailCa = processPurchaseOrdersTemporaires(receptionTempDTO, purchaseOrdersDetails, TypeBonEnum.valueOf(receptionTempDTO.getTypbon()));
        log.debug("ReceptionTemporaireDetailCa {} ", listeReceptionDetailCa);
        return listeReceptionDetailCa;

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

            BigDecimal quantiteGrtauiteRestanteCommandeTotale = matchingDetailCAs
                    .stream()
                    .collect(Collectors.reducing(BigDecimal.ZERO,
                            detailCA -> detailCA.getQuantiteGratuiteRestante(), BigDecimal::add));
            Preconditions.checkBusinessLogique(quantiteGrtauiteRestanteCommandeTotale.compareTo(freeDetailReceptionTemp.getQuantite()) >= 0, "reception.add.recived-ca");

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

    ////***************cancel receptions ********//
    @Transactional
    public BonRecepDTO cancelBonReception(String numBon)  {

        log.debug("cancelling new FactureBA {}", numBon);
        FactureBA factureBA = factureBARepository.findOne(numBon);
        Preconditions.checkBusinessLogique(factureBA != null, "reception.delete.reception-not-found");
        Preconditions.checkBusinessLogique(factureBA.getCodAnnul() == null, "reception.delete.reception-canceld");
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(factureBA.getIntegrer()), "facture-deja-integre");
        if (factureBA.getAutomatique() != null) {
            Preconditions.checkBusinessLogique(!factureBA.getAutomatique(), "reception.delete.reception-automatique");
        }

//        for (MvtStoBA mvtstoBA : factureBA.getDetailFactureBACollection()) {
//            MvtStoBA result = mvtStoBAService.findDernierPrixAchat(factureBA, mvtstoBA.getCodart());
//            log.debug("result est {}", result);
//        }
//        MvtStoBA nullMvtstoBa = null;
//        BigDecimal valeur = nullMvtstoBa.getBaseTva();
        Preconditions.checkBusinessLogique(factureBA.getFactureBonReception() == null, "invoiced-reception");
        Preconditions.checkBusinessLogique(!isReceptionUsed(numBon), "reception.delete.used-reception");
        fcptFrsPHService.deleteFcptfrsByNumBonDao(numBon, factureBA.getTypbon());
        processPurchaseOrdersOnDeleteRecept(factureBA);
        factureBA.setCodAnnul(SecurityContextHolder.getContext().getAuthentication().getName());
        factureBA.setDatAnnul(LocalDateTime.now());
        Receiving receiving = factureBA.getReceiving();
        receiving.setDateValidate(null);
        receiving.setUserValidate(null);
        factureBA.setReceiving(null);
        FactureBA result = factureBARepository.save(factureBA);
        stockService.deleteByNumBon(numBon);

        Set<Integer> codeArts = result.getDetailFactureBACollection().stream().map(item -> item.getCodart()).distinct().collect(Collectors.toSet());
        List<MvtStoBA> listeMvtstoBAReceptionnesApres = mvtStoBAService.findByPk_CodartInAndFactureBA_DatbonAfterAndTypeBon(codeArts, factureBA.getDatbon(), TypeBonEnum.BA);
        log.debug("listeDerniersMvtstoBA est {}", listeMvtstoBAReceptionnesApres);
        Set<Integer> codeArtsReceptionnesApres = listeMvtstoBAReceptionnesApres.stream().map(item -> item.getCodart()).distinct().collect(Collectors.toSet());
        List<MvtStoBA> listeToUpdateNotReceivedAfter = new ArrayList(factureBA.getDetailFactureBACollection());

        listeToUpdateNotReceivedAfter.removeIf(mvt -> codeArtsReceptionnesApres.contains(mvt.getCodart()));
        if (!listeToUpdateNotReceivedAfter.isEmpty()) {
            log.debug("details after remove sont {}", listeToUpdateNotReceivedAfter);
            pricingService.updatePricesAfterDeleteReception(factureBA, listeToUpdateNotReceivedAfter, result.getCategDepot());

            BonRecepDTO receptionToUpdateDTO = FactureBAFactory.factureBAToBonRecepDTOForRevertPrices(factureBA.getCodfrs(), listeToUpdateNotReceivedAfter);

            paramAchatServiceClient.articlePriceRevertAndArticleFrsPriceRevert(receptionToUpdateDTO);
        }
        return FactureBAFactory.factureBAToBonRecepDTO(result);
    }

    public BonRecepDTO cancelBonReceptionDepotFrs(String numBon) {

        log.debug("cancelling new FactureBA {}", numBon);
        FactureBA factureBA = factureBARepository.findOne(numBon);
        Preconditions.checkBusinessLogique(factureBA != null, "reception.notFound");
        Preconditions.checkBusinessLogique(factureBA.getCodAnnul() == null, "reception.delete.reception-canceld");
        Preconditions.checkBusinessLogique(factureBA.getFactureBonReception() == null, "facture-bon-reception.invoiced-reception", factureBA.getNumbon());
        fcptFrsPHService.deleteFcptfrsByNumBonDao(numBon, factureBA.getTypbon());
        factureBA.setCodAnnul(SecurityContextHolder.getContext().getAuthentication().getName());
        factureBA.setDatAnnul(LocalDateTime.now());
        List<Integer> listCodesCA = factureBA.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());// list of received purchase orders
        etatReceptionCAService.deleteByCommandeAchatIn(listCodesCA); // delete the state of the recived purchase orders 
        List<ReceptionDetailCA> receptionDetailCAs = receptionDetailCAService.findByCodesCAIn(listCodesCA);
        List<EtatReceptionCA> partRecivedPurchOrdes = receptionDetailCAs.stream() // recalculate their state 
                .filter(item -> item.getPk().getReception().equals(factureBA.getNumbon()) && item.getQuantiteReceptione().compareTo(BigDecimal.ZERO) > 0)
                .map(filtredItem -> {
                    return new EtatReceptionCA(filtredItem.getPk().getCommandeAchat(), PurchaseOrderReceptionState.A);
                })
                .collect(Collectors.toList());
        factureBA.getRecivedDetailCA().clear();
//        log.debug("EtatReceptionCA sont {}",partRecivedPurchOrdes);
        etatReceptionCAService.save(partRecivedPurchOrdes);
        FactureBA result = factureBARepository.save(factureBA);
        return FactureBAFactory.factureBAToBonRecepDTO(result);
    }

    public Boolean cancelBonReceptionDepotFrsPermanent(String numBon) {

        log.debug("cancelling new FactureBA {}", numBon);
        FactureBA factureBA = factureBARepository.findOne(numBon);
        Preconditions.checkBusinessLogique(factureBA != null, "reception.notFound");
        Preconditions.checkBusinessLogique(factureBA.getCodAnnul() == null, "reception.delete.reception-canceld");
        fcptFrsPHService.deleteFcptfrsByNumBonDao(numBon, factureBA.getTypbon());
        List<Integer> listCodesCA = factureBA.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());// list of received purchase orders
        etatReceptionCAService.deleteByCommandeAchatIn(listCodesCA); // delete the state of the recived purchase orders 
        factureBARepository.delete(factureBA);
//        paramService.updateCompteurPharmacie(CategorieDepotEnum.UU, TypeBonEnum.BA, factureBARepository.findMaxNumbonByCategDepot(CategorieDepotEnum.UU));
//        paramService.updateCompteurPharmacie(CategorieDepotEnum.PH, TypeBonEnum.BA, factureBARepository.findMaxNumbonByCategDepot(CategorieDepotEnum.PH));

        return Boolean.TRUE;
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

    @Transactional(readOnly = true)
    private Boolean isReceptionUsed(String numBon) {
        FactureBA factureBA = factureBARepository.findOne(numBon);
        Integer overAllRecivedQte = factureBA.getDetailFactureBACollection().stream().map(item -> item.getQuantite().intValue()).collect(Collectors.summingInt(Integer::new));
        Integer overAllQteInStock = stockService.findDepstoByNumBon(numBon).stream().map(item -> item.getQte().intValue()).collect(Collectors.summingInt(Integer::new));
        return overAllQteInStock.compareTo(overAllRecivedQte) < 0;
    }

    @Transactional
    public BonRetourDTO ajoutBonRetour(BonRetourDTO bonRetourDTO) {

        FactureBA bonReceptionCorrespondant = factureBARepository.findFirstByNumbon(bonRetourDTO.getReceptionID());
        Preconditions.checkBusinessLogique(bonReceptionCorrespondant.getFactureBonReception() == null, "invoiced-reception");
//        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(bonReceptionCorrespondant.getIntegrer()), "facture-deja-integre");
        String numBon = paramService.getcompteur(bonRetourDTO.getCategDepot(), bonRetourDTO.getTypeBon());
//        bonRetourDTO.setNumbon(numBon);
        FactureBA bonRetour = FactureBAFactory.factureBADTOTOFactureBA((FactureBADTO) bonRetourDTO);
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
        List<MvtStoBA> listeMvtStoBAs = processReceptionOnReturn(resutedRetour);
        fcptFrsPHService.addFcptFrsOnReturn(resutedRetour);
        stockService.processStockOnReturn(resutedRetour);
//          FactureBA result = factureBARepository.save(factureBa);
        pricingService.updatePricesAfterReturn(resutedRetour, listeMvtStoBAs, bonReceptionCorrespondant.getDatbon());
        ajustementRetourFournisseurService.saveAjustementRetour(resutedRetour);
        paramService.updateCompteurPharmacie(bonRetourDTO.getCategDepot(), bonRetourDTO.getTypeBon());
        return FactureBAFactory.factureBAToBonRetourDTOEager(resutedRetour);
    }

 

//    public MouvementDuJour findMouvementDuJour(String typbon, boolean stup) throws ParseException {
//        Date date = new Date();
//        Date datesys = formatter.parse(formatter.format(date));
//        return factureBARepository.findMouvementDuJour(datesys, typbon, stup);
//    }
    /**
     * Return the returned details of a reception
     *
     * @param numbon
     * @return
     */
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

        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(codeFournisseur);
        Preconditions.checkBusinessLogique(fournisseurDTO != null, "missing-supplier", codeFournisseur);
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseurDTO.getAnnule()) && !(Boolean.TRUE.equals(fournisseurDTO.getStopped())), "fournisseur.stopped", fournisseurDTO.getCode());

        receptionInBase.setCodfrs(fournisseurDTO.getCode());
        FcptfrsPH fcptFrs = fcptFrsPHService.findFirstByNumBon(numBon);
        fcptFrs.setCodFrs(fournisseurDTO.getCode());

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

    @Override
    public void processStockAndPricesAfterTransefrtCompanyToBranch(TransfertCompanyBranch transfertCompanyBranch, TransfertCompanyBranchDTO transfertCompanyBranchDTO, List<ArticleDTO> articles) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

   

//    @Transactional(readOnly = true)
//    @Override
//    public List<BonRecepDTO> findAllReception(FactureBA exempleFactureBa, LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted, Boolean invoiced, Integer codeArticle) {
//
//        log.debug("Request to get All receptions  in standard");
//
//        List<FactureBA> result = findAll(exempleFactureBa, fromDate, toDate, deleted, invoiced, codeArticle);
//
//        log.debug("result size est {}", result.size());
//        List<BonRecepDTO> resultDTO = new ArrayList<>();
//        if (!result.isEmpty()) {
//            List<String> codesFrs = result.stream().map(item -> item.getCodfrs()).distinct().collect(Collectors.toList());
//            List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
//
//            result.forEach((factureBA) -> {
//                BonRecepDTO bonRecepDTO = FactureBAFactory.factureBAToBonRecepDTO(factureBA);
//                Optional<FournisseurDTO> optFrs = fournisseurs.stream().filter(item -> item.getCode().equals(factureBA.getCodfrs())).findFirst();
//                Preconditions.checkBusinessLogique(optFrs.isPresent(), "reception.get-all.missing-frs", factureBA.getCodfrs());
//                bonRecepDTO.setFournisseur(optFrs.get());
//                if (factureBA.getNumAfficheRecetionTemporaire() != null) {
//                    bonRecepDTO.setNumAfficheRecetionTemporaire(factureBA.getNumAfficheRecetionTemporaire());
//                }
//                resultDTO.add(bonRecepDTO);
//            });
//            // this may not seem logic, but the only interface in whch we must precise if the reception has a return or not, is the invoice interface. This interface will ask for the list of receptions of a given frs. So whenever the codfrs is supplied we will presume that it's a request from the invoice interface
//            if (exempleFactureBa.getCodfrs() != null) {
//                isReturned(resultDTO);
//            }
//        }
//        return resultDTO;
//    }
//
//    @Transactional(readOnly = true)
//    public List<BonRetourDTO> findAllReturns(FactureBA queriedFacBA, LocalDateTime fromDate, LocalDateTime toDate, Integer codeArticle) {
//        log.debug("Request to get All returns");
//        List<FactureBA> result = findAll(queriedFacBA, fromDate, toDate, null, null, codeArticle);
//
//        List<String> codesFrs = result.stream().map(item -> item.getCodfrs()).distinct().collect(Collectors.toList());
//        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
//
//        List<BonRetourDTO> resultDTO = new ArrayList();
//        result.forEach((factureBA) -> {
//            BonRetourDTO bonRetourDTO = FactureBAFactory.factureBAToBonRetourDTOLazy(factureBA);
//
//            FactureBA reception = factureBARepository.findOne(factureBA.getNumpiece());
//            Optional<FournisseurDTO> optFrs = fournisseurs.stream().filter(item -> item.getCode().equals(factureBA.getCodfrs())).findFirst();
//            Preconditions.checkBusinessLogique(optFrs.isPresent(), "returns.get-all.missing-frs", factureBA.getCodfrs());
//            bonRetourDTO.setFournisseur(optFrs.get());
//            bonRetourDTO.setNumAfficheReception(reception.getNumaffiche());
//            resultDTO.add(bonRetourDTO);
//        });
//        return resultDTO;
//    }
//
//    @Transactional(readOnly = true)
//    public List<FactureBA> findAll(FactureBA queriedFacBA, LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted, Boolean invoiced, Integer codeArticle) {
//        QFactureBA _FactureBA = QFactureBA.factureBA;
//        WhereClauseBuilder builder = new WhereClauseBuilder()
//                .and(_FactureBA.typbon.eq(queriedFacBA.getTypbon()))
//                .optionalAnd(queriedFacBA.getCategDepot(), () -> _FactureBA.categDepot.eq(queriedFacBA.getCategDepot()))
//                .optionalAnd(fromDate, () -> _FactureBA.datbon.goe(fromDate))
//                .optionalAnd(toDate, () -> _FactureBA.datbon.loe(toDate))
//                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _FactureBA.datAnnul.isNotNull())
//                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _FactureBA.datAnnul.isNull())
//                .optionalAnd(queriedFacBA.getCodfrs(), () -> _FactureBA.codfrs.eq(queriedFacBA.getCodfrs()))
//                .optionalAnd(queriedFacBA.getCoddep(), () -> _FactureBA.coddep.eq(queriedFacBA.getCoddep()))
//                .booleanAnd(Objects.equals(queriedFacBA.getAutomatique(), Boolean.TRUE), () -> _FactureBA.automatique.eq(Boolean.TRUE))
//                .booleanAnd(Objects.equals(queriedFacBA.getAutomatique(), Boolean.FALSE), () -> _FactureBA.automatique.isNull().or(_FactureBA.automatique.eq(Boolean.FALSE)))
//                .booleanAnd(Objects.equals(invoiced, Boolean.TRUE), () -> _FactureBA.factureBonReception().isNotNull())
//                .booleanAnd(Objects.equals(invoiced, Boolean.FALSE), () -> _FactureBA.factureBonReception().isNull())
//                .optionalAnd(codeArticle, () -> _FactureBA.detailFactureBACollection.any().pk().codart.eq(codeArticle));
//
//        return (List<FactureBA>) factureBARepository.findAll(builder);
//    }
//    @Transactional(readOnly = true)
//    public BonRecepDTO findOneReception(String numBon) {
//        
//        log.debug("find one reception in standard ");
//        FactureBA reception = factureBARepository.findOne(numBon);
//        Preconditions.checkBusinessLogique(reception != null, "reception.missing");
//        BonRecepDTO bonRecepDTO = FactureBAFactory.factureBAToBonRecepDTO(reception);
//        bonRecepDTO.setDesignationDepot(paramAchatServiceClient.findDepotByCode(reception.getCoddep()).getDesdep());
//        bonRecepDTO.setFournisseur(paramAchatServiceClient.findFournisseurByCode(reception.getCodfrs()));
//        bonRecepDTO.setCodeFournisseu(reception.getCodfrs());
//        bonRecepDTO.setFournisseurExonere(reception.getFournisseurExonere());
//
//        Set<Integer> setEmplacement = new HashSet();
//        List<Integer> codeUnites = new ArrayList();
//        List<MvtstoBADTO> detailsReception = new ArrayList();
//        reception.getDetailFactureBACollection().forEach(mvtstoBA -> {
//            codeUnites.add(mvtstoBA.getCodeUnite());
//            if (mvtstoBA.getCodeEmplacement() != null) {
//                setEmplacement.add(mvtstoBA.getCodeEmplacement());
//            }
//            MvtstoBADTO dto = new MvtstoBADTO();
//            MvtstoBAFactory.toDTO(mvtstoBA, dto);
//            dto.setQteReturned(mvtstoBA.getQuantite().subtract(mvtstoBA.getQtecom()));
//            detailsReception.add(dto);
//        });
//        bonRecepDTO.setDetails((List) detailsReception);
//        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
//        List<EmplacementDTO> emplacements = reception.getCategDepot().equals(CategorieDepotEnum.IMMO) ? paramAchatServiceClient.findEmplacementsByCodes(setEmplacement) : new ArrayList();
//        if (reception.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
//            Preconditions.checkBusinessLogique(emplacements != null, "error-emplacaments");
//        }
//        bonRecepDTO.getDetails().forEach(item -> {
//            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getCodeUnite())).findFirst().orElse(null);
//            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
//            item.setUnitDesignation(unite.getDesignation());
//
//            if (reception.getCategDepot().equals(CategorieDepotEnum.IMMO)) {
//                EmplacementDTO emplacement = emplacements.stream()
//                        .filter(x -> x.getCode().equals(item.getCodeEmplacement()))
//                        .findFirst().orElse(null);
//                if (item.getCodeEmplacement() != null) {
//                    item.setDesignationEmplacement(emplacement.getDesignation());
//                    item.setCodeDepartementEmplacement(emplacement.getCodeDepartement().getCode());
//                    item.setDesigationDepartementEmplacement(emplacement.getCodeDepartement().getDesignation());
//                }
//            }
//        });
//        List<Integer> listCodesCA = reception.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());
//        bonRecepDTO.setPurchaseOrders(demandeServiceClient.findListCommandeAchat(listCodesCA, LocaleContextHolder.getLocale().getLanguage()));
//
//        return bonRecepDTO;
//    }
//
//    @Transactional(readOnly = true)
//    public BonRetourDTO findOneRetour(String numBon) {
//        FactureBA retour = factureBARepository.findOne(numBon);
//        Preconditions.checkBusinessLogique(retour != null, "reception.missing");
//        BonRetourDTO result = FactureBAFactory.factureBAToBonRetourDTOEager(retour);
//        result.setDesignationDepot(paramAchatServiceClient.findDepotByCode(retour.getCoddep()).getDesdep());
//        result.setFournisseur(paramAchatServiceClient.findFournisseurByCode(retour.getCodfrs()));
//
//        FactureBA reception = factureBARepository.findOne(retour.getNumpiece());
//        result.setDateReception(reception.getDatbon());
//        result.setMntBonReception(reception.getMntbon());
//        result.setNumAfficheReception(reception.getNumaffiche());
//        result.setReceptionID(reception.getNumbon());
//
//        return result;
//    }
//    public List<BonRecepDTO> isReturned(List<BonRecepDTO> receptions) {
//        List<String> recptionsID = receptions.stream().map(BonRecepDTO::getNumbon).collect(toList());
//        List<FactureBA> returns = factureBARepository.findByNumpieceInAndTypbon(recptionsID, RT);
//        receptions.forEach(item -> {
//            boolean isReturned = returns.stream().anyMatch(rt -> rt.getNumpiece().equals(item.getNumbon()));
//            item.setIsReturned(isReturned);
//        });
//        return receptions;
//    }
//
//    public BonRecepDTO isReturned(BonRecepDTO bonRecepDTO) {
////        String recptionID = receptions.stream().map(BonRecepDTO::getNumbon).collect(toList());
//        Set<FactureBA> returns = factureBARepository.findByNumpieceAndTypbon(bonRecepDTO.getNumbon(), RT);
////        receptions.forEach(item -> {
//        boolean isReturned = returns.stream().anyMatch(rt -> rt.getNumpiece().equals(bonRecepDTO.getNumbon()));
//        bonRecepDTO.setIsReturned(isReturned);
////        });
//        return bonRecepDTO;
//    }
//
//    @Transactional
//    public BonRecepDTO saveBonReceptionOrReceptionTemporaire(BonRecepDTO bonReeceptionDTO) throws ParseException {
//        BonRecepDTO result = new BonRecepDTO();
//        if (receptionSurDeuxEtapes.equalsIgnoreCase("true")) {
//            log.debug("reception sur deux etapes");
//            receptionTemporaireService.save(bonReeceptionDTO);
//        } else {
//            log.debug("reception sur une seule etape");
//            result = ajoutBonReception(bonReeceptionDTO);
//        }
//        return result;
//
//    }