/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.DetailFactureDirecte;
import com.csys.pharmacie.achat.domain.FactureDirecte;
import com.csys.pharmacie.achat.domain.FactureDirecteCostCenter;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.FactureDirecteDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.factory.FactureDirecteFactory;
import com.csys.pharmacie.achat.repository.FactureDirecteRepository;
import com.csys.pharmacie.achat.repository.FcptFrsPHRepository;
import com.csys.pharmacie.client.dto.DeviseDTO;
import com.csys.pharmacie.client.service.CaisseServiceClient;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.config.SenderComptable;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author USER
 */
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
        
        senderComptable.send(topicDirectBillManagementForAccounting, numbon, resultDTO);
        return resultDTO;
        
    }
}
