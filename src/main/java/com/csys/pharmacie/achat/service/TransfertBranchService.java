/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.DetailTransfertCompanyBranch;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.factory.TransfertCompanyBranchFactory;
import com.csys.pharmacie.achat.repository.TransfertCompanyBranchRepository;
import static com.csys.pharmacie.achat.service.FactureBABranchFacadeService.codeSiteConfig;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.vente.service.PricingService;
import com.csys.util.Preconditions;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrateur
 */
@Service
@Transactional
public class TransfertBranchService {

    private final TransfertCompanyBranchRepository transfertcompanybranchRepository;
    private final Logger log = LoggerFactory.getLogger(FactureBAService.class);
    private final ParamService paramService;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final Environment env;
    private final FactureBABranchFacadeService factureBABranchService;
    private final ParamServiceClient paramServiceClient;

    private final StockService stockService;
    private final PricingService pricingService;
    private final AjustementTransfertBranchCompanyService ajustementTransfertBranchCompanyService;
    private final TransfertCompanyBranchService transfertCompanyBranchService;
 
    public TransfertBranchService(TransfertCompanyBranchRepository transfertcompanybranchRepository, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient, Environment env, FactureBABranchFacadeService factureBABranchService, ParamServiceClient paramServiceClient, StockService stockService,@Lazy PricingService pricingService, AjustementTransfertBranchCompanyService ajustementTransfertBranchCompanyService,@Lazy TransfertCompanyBranchService transfertCompanyBranchService) {
        this.transfertcompanybranchRepository = transfertcompanybranchRepository;
        this.paramService = paramService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.env = env;
        this.factureBABranchService = factureBABranchService;
        this.paramServiceClient = paramServiceClient;
        this.stockService = stockService;
        this.pricingService = pricingService;
        this.ajustementTransfertBranchCompanyService = ajustementTransfertBranchCompanyService;
        this.transfertCompanyBranchService = transfertCompanyBranchService;
    }

   
    public TransfertCompanyBranchDTO ajoutTransfertBranchToCompanyPourRetour(TransfertCompanyBranchDTO transfertcompanybranchDTO) {
        log.debug("*********************************ajoutTransfertBranchToCompanyPourRetour *************in branch*************: ");
        String numBonTrCB = paramService.getcompteur(transfertcompanybranchDTO.getCategDepot(), TypeBonEnum.TBC);
        transfertcompanybranchDTO.setNumBon(numBonTrCB);
        transfertcompanybranchDTO.setNumAffiche(numBonTrCB.substring(2));
        TransfertCompanyBranch transfertBranchToCompanyForReturn = TransfertCompanyBranchFactory.transfertCompanyBranchDTOToTransfertCompanyBranch(transfertcompanybranchDTO);
        TransfertCompanyBranch transfertCorrespondant = transfertcompanybranchRepository.findOne(transfertcompanybranchDTO.getNumBonTransfertRelatif());

        Preconditions.checkBusinessLogique(transfertCorrespondant != null, "missing-transfer");
        transfertBranchToCompanyForReturn.setNumBonTransfertRelatif(transfertcompanybranchDTO.getNumBonTransfertRelatif());
        transfertBranchToCompanyForReturn.setNumbonReception(transfertCorrespondant.getNumbonReception());
        transfertBranchToCompanyForReturn.setTypeBon(TypeBonEnum.TBC);

        transfertBranchToCompanyForReturn.setDateCreate(LocalDateTime.now());
        transfertBranchToCompanyForReturn.setUserCreate(SecurityContextHolder.getContext().getAuthentication().getName());
        transfertBranchToCompanyForReturn.setIntegrer(Boolean.FALSE);
        transfertBranchToCompanyForReturn.setCodeIntegration("");

        //-------  depot--------/
        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(transfertcompanybranchDTO.getCodeDepot());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + transfertcompanybranchDTO.getCodeDepot() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getCode() + "] est un dépot fournisseur");
        transfertBranchToCompanyForReturn.setCodeDepot(depotd.getCode());

        transfertBranchToCompanyForReturn.setIntegrer(Boolean.FALSE);
        transfertBranchToCompanyForReturn.setCodeIntegration("");
        transfertBranchToCompanyForReturn.setNumBon(numBonTrCB);

        List<CliniqueDto> cliniqueDTOs = paramServiceClient.findClinique();

        Integer codeSite = env.acceptsProfiles("testCentral") ? codeSiteConfig : cliniqueDTOs.get(0).getCodeSite();
        transfertBranchToCompanyForReturn.setCodeSite(codeSite);

        //-------  get emplacements et articles and check items --------/
        Set<Integer> codeArticles = new HashSet();

        transfertBranchToCompanyForReturn.getDetailTransfertCompanyBranchCollection().forEach(detail -> {
            codeArticles.add(detail.getCodeArticle());
        });
        log.debug("codeArticles sont {}", codeArticles);
        List<ArticleDTO> articles = (List<ArticleDTO>) paramAchatServiceClient.findArticlebyCategorieDepotAndListCodeArticle(transfertBranchToCompanyForReturn.getCategDepot(), codeArticles.toArray(new Integer[codeArticles.size()]));
        ////-------  check inventory  //-------  check items-------------/  
        Set<Integer> categArticleIDs = new HashSet();
        articles.forEach(item -> {
            Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(item.getAnnule()) && !(Boolean.TRUE.equals(item.getStopped())), "item.stopped", item.getCodeSaisi());
            categArticleIDs.add(item.getCategorieArticle().getCode());
        });
        factureBABranchService.checkOpenInventoryForCategoriesInDeposit(categArticleIDs, depotd.getCode(), articles);
        transfertBranchToCompanyForReturn = transfertcompanybranchRepository.save(transfertBranchToCompanyForReturn);
        log.debug("************after process stock and prices after Transfert branch Company retour  **********  ");
        Collection<DetailTransfertCompanyBranch> listeDetailTransfertRelatifs = transfertCompanyBranchService.processTransferOnReturnTransfert(transfertBranchToCompanyForReturn);
        processStockAndPricesAfterTransfertBranchToCompany(transfertBranchToCompanyForReturn, listeDetailTransfertRelatifs, transfertCorrespondant.getDateCreate());

        //manque pricing audit des table //verif 
        paramService.updateCompteurPharmacie(transfertcompanybranchDTO.getCategDepot(), TypeBonEnum.TBC);

        TransfertCompanyBranchDTO resultDTO = TransfertCompanyBranchFactory.transfertCompanyBranchToTransfertCompanyBranchDTO(transfertBranchToCompanyForReturn, Boolean.TRUE);
        log.debug("resultDTO details {}", resultDTO.getDetailTransfertCompanyBranchDTOs());
//        sender.send("transfer-branch-to-company-management", transfertBranchToCompanyForReturn.getNumBon(), resultDTO);

        return resultDTO;
    }

    ///////////***********************/
    @Transactional
    public void processStockAndPricesAfterTransfertBranchToCompany(TransfertCompanyBranch transfertCompanyBranch,
            Collection<DetailTransfertCompanyBranch> listeDetailTransfertRelatifs,
            LocalDateTime dateTransfertRelatif) {

        stockService.processStockOnTransferBranchCompany(transfertCompanyBranch);
////          FactureBA result = factureBARepository.save(factureBa);
        if (CategorieDepotEnum.UU.equals(transfertCompanyBranch.getCategDepot())) {
            pricingService.updateReferencePriceAfterTransferBranchCompany(transfertCompanyBranch, listeDetailTransfertRelatifs, dateTransfertRelatif);
        }
        ajustementTransfertBranchCompanyService.saveAjustementRetour(transfertCompanyBranch);

    }
            }
