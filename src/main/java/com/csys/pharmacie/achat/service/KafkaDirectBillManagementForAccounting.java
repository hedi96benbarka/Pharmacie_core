/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csys.pharmacie.achat.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.DetailFactureDirecte;
import com.csys.pharmacie.achat.domain.FactureDirecte;
import com.csys.pharmacie.achat.domain.FactureDirecteCostCenter;
import com.csys.pharmacie.achat.domain.FcptfrsPH;
import com.csys.pharmacie.achat.dto.*;
import com.csys.pharmacie.achat.factory.FactureDirecteFactory;
import com.csys.pharmacie.achat.repository.FactureDirecteRepository;
import com.csys.pharmacie.achat.repository.FcptFrsPHRepository;
import com.csys.pharmacie.client.dto.DeviseDTO;
import com.csys.pharmacie.client.service.CaisseServiceClient;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.config.SenderComptable;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.EnumCrudMethod;
import com.csys.pharmacie.helper.PurchaseOrderReceptionState;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
/*
@Service
public class KafkaDirectBillManagementForAccounting {

    private final Logger log = LoggerFactory.getLogger(KafkaDirectBillManagementForAccounting.class);
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

    public KafkaDirectBillManagementForAccounting(FactureDirecteRepository facturedirecteRepository, ParamAchatServiceClient paramAchatServiceClient, ParamService paramService, DetailFactureDirecteService detailFactureDirecteService, FcptFrsPHService fcptFrsPHService, FcptFrsPHRepository fcptFrsPHRepository, ParamServiceClient parametrageService, CaisseServiceClient caisseServiceClient, DemandeServiceClient demandeServiceClient, EtatReceptionCAService etatReceptionCAService,SenderComptable senderComptable) {
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
        this.senderComptable= senderComptable;
    }


    public FactureDirecteDTO saveReocrd(FactureDirecteDTO facturedirecteDTO){
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


    public FactureDirecteDTO saveRecordWithBonCommande(FactureDirecteDTO facturedirecteDTO){
        String language = LocaleContextHolder.getLocale().getLanguage();
        CommandeAchatDTO commandeAchatDTO = demandeServiceClient.findCommandeAchat(facturedirecteDTO.getCodeCommandeAchat(), language);
        Preconditions.checkBusinessLogique(commandeAchatDTO.getUserAnnul() == null, "commande-achat-annuler");
        Preconditions.checkBusinessLogique(commandeAchatDTO.getValide() == true, "commande-achat-not-valide");
        FactureDirecte facturedirecte = FactureDirecteFactory.facturedirecteDTOToFactureDirecte(facturedirecteDTO);
        log.debug("datBon est :{}", facturedirecte.getDatbon());
        FactureDirecte factureWithSameRef = facturedirecteRepository.findByReferenceFournisseur(facturedirecte.getReferenceFournisseur());
        Preconditions.checkBusinessLogique(factureWithSameRef == null, "000000000");

        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(facturedirecteDTO.getCodeFournisseur());
        Preconditions.checkBusinessLogique(fournisseurDTO != null, "missing-supplier", facturedirecteDTO.getCodeFournisseur());
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseurDTO.getAnnule()) && !(Boolean.TRUE.equals(fournisseurDTO.getStopped())), "fournisseur.stopped", fournisseurDTO.getCode());

        DeviseDTO deviseDTO = caisseServiceClient.findDeviseById(facturedirecteDTO.getCodeDevise());
        Preconditions.checkBusinessLogique(deviseDTO != null, "missing-devise", facturedirecteDTO.getCodeDevise());

        facturedirecte.setTypbon(TypeBonEnum.DIR);

        List<Integer> articlesID = facturedirecte.getDetailFactureDirecteCollection().stream().map(DetailFactureDirecte::getCodart).collect(toList());
        List<ArticleDTO> articles = paramAchatServiceClient.articleECFindbyListCode(articlesID);
        Preconditions.checkBusinessLogique(articles.size() == articlesID.size(), "facture-directe.non-assets-item");
        articles.stream().filter(ArticleDTO::getStockable).findFirst().ifPresent(item -> {
            throw new IllegalBusinessLogiqueException("facture-directe.non-stockable-item", new Throwable(item.getCodeSaisi()));
        });
        String numbon = paramService.getcompteur(facturedirecteDTO.getCategDepot(), TypeBonEnum.DIR);
        facturedirecte.setIntegrer(false);
        facturedirecte.setNumbon(numbon);
        facturedirecte.calcul(paramAchatServiceClient.findTvas());
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
        senderComptable.sendDirectBill(topicDirectBillManagementForAccounting, numbon, resultDTO);
        return resultDTO;
    }


    public FactureDirecteDTO saveReocrdUpdate(FactureDirecteDTO factureDirecteDTO){
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
        Preconditions.checkBusinessLogique(factureWithSameRef.equals(inBase) || factureWithSameRef == null, "reffrs-fournisseur-exists");
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

    //Annuler facture directe

    @Transactional
    public FactureDirecteDTO saveReocrdAnnule(String id) {
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
        facturedirecte=facturedirecteRepository.save(facturedirecte);
        FactureDirecteDTO resultDTO = FactureDirecteFactory.facturedirecteToFactureDirecteDTO(facturedirecte, Boolean.FALSE);
        resultDTO.setAction(EnumCrudMethod.CANCEL);
        senderComptable.sendDirectBill(topicDirectBillManagementForAccounting, facturedirecte.getNumbon(), resultDTO);
        return resultDTO;
    }

}
*/