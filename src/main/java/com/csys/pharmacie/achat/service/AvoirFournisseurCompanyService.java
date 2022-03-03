/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.FactureBonReception;
import com.csys.pharmacie.achat.domain.MvtstoAF;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.AvoirFournisseurDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoAFDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.factory.AvoirFournisseurFactory;
import com.csys.pharmacie.achat.factory.MvtstoAFFactory;
import com.csys.pharmacie.achat.repository.AvoirFournisseurRepository;
import com.csys.pharmacie.client.dto.SiteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.util.Preconditions;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrateur
 */
@Service
@Transactional
public class AvoirFournisseurCompanyService extends AvoirFournisseurService {

    private final FactureBAService factureBAService;
    private final TransfertCompanyFacadeService transfertCompanyFacadeService;

    public AvoirFournisseurCompanyService(@Lazy FactureBAService factureBAService, TransfertCompanyFacadeService transfertCompanyFacadeService, AvoirFournisseurRepository avoirfournisseurRepository, FactureBonReceptionService factureBonReceptionService, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient, MvtStoBAService mvtStoBAService, FcptFrsPHService fcptFrsPHService, MvtstoAFService mvtstoAFService, ParamServiceClient parametrageService, MvtstoAFFactory mvtstoAFFactory, AvoirFournisseurFactory avoirFournisseurFactory) {
        super(avoirfournisseurRepository, factureBonReceptionService, paramService, paramAchatServiceClient, mvtStoBAService, fcptFrsPHService, mvtstoAFService, parametrageService, mvtstoAFFactory, avoirFournisseurFactory);
        this.factureBAService = factureBAService;
        this.transfertCompanyFacadeService = transfertCompanyFacadeService;
    }

    /**
     * Save a avoirfournisseurDTO.
     *
     * @param avoirfournisseurDTO
     * @return the persisted entity
     */
    public AvoirFournisseurDTO save(AvoirFournisseurDTO avoirfournisseurDTO) {
        log.debug("Request to save AvoirFournisseur company : {}", avoirfournisseurDTO);
        TransfertCompanyBranch retourTransfertCompanyBranchCorrespondant = transfertCompanyFacadeService.findOneTransfert(avoirfournisseurDTO.getNumbonRetourTransfertCompanyBranch());
        Preconditions.checkBusinessLogique(retourTransfertCompanyBranchCorrespondant != null, "missing-retour-transfert ");
        retourTransfertCompanyBranchCorrespondant.setReturnedToSupplier(Boolean.TRUE);
        transfertCompanyFacadeService.saveTransfertCompany(retourTransfertCompanyBranchCorrespondant);

        FactureBA factureBa = factureBAService.findOne(retourTransfertCompanyBranchCorrespondant.getNumbonReception());
        FactureBonReception facturebonReceptionCorrespondante = factureBa.getFactureBonReception();
        log.debug("facture bon reception ****{}", facturebonReceptionCorrespondante);
//        FactureBonReception facturebonReceptionCorrespondante = factureBonReceptionService.findFactureBonReception(avoirfournisseurDTO.getNumFactureBonRecep());
        checkBusinessLogique(facturebonReceptionCorrespondante != null, "missing-factureBonReception");
        avoirfournisseurDTO.setNumFactureBonRecep(facturebonReceptionCorrespondante.getNumbon());
        FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(avoirfournisseurDTO.getCodeFournisseur());
        checkBusinessLogique(fournisseur != null, "missing-supplier", avoirfournisseurDTO.getCodeFournisseur());

        String numBon = paramService.getcompteur(avoirfournisseurDTO.getCategDepot(), avoirfournisseurDTO.getTypbon());

        AvoirFournisseur avoirFournisseur = avoirFournisseurFactory.avoirFournisseurDTOToAvoirFournisseur(avoirfournisseurDTO);
        avoirFournisseur.setNumbon(numBon);
        avoirFournisseur.setNumbonRetourCompanyBranch(retourTransfertCompanyBranchCorrespondant.getNumBon());
        
        SiteDTO codeSiteBranch = parametrageService.findSiteByCode(avoirfournisseurDTO.getCodeSite());
        Preconditions.checkBusinessLogique(codeSiteBranch != null, "parametrage.clinique.error");
        avoirFournisseur.setCodeSite(codeSiteBranch.getCode());

        List<MvtstoAF> listeMvtstoAF = new ArrayList();
        for (MvtstoAFDTO mvtstoAFDTO : avoirfournisseurDTO.getMvtstoAFList()) {
            MvtstoAF mvtstoAF = mvtstoAFFactory.mvtstoAFDTOToMvtstoAF(mvtstoAFDTO);
            mvtstoAF.setAvoirFournisseur(avoirFournisseur);
            mvtstoAF.setNumbonReception(factureBa.getNumbon());
            listeMvtstoAF.add(mvtstoAF);
        }
        avoirFournisseur.setMvtstoAFList(listeMvtstoAF);
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        avoirFournisseur.calcul(listTvas);
        avoirFournisseur = avoirfournisseurRepository.save(avoirFournisseur);

        mvtStoBAService.updateQteComOnAvoirFournisseur(avoirFournisseur);
        fcptFrsPHService.addFcptFrsOnAvoirFournisseur(avoirFournisseur);
//        stockService.processStockOnAvoirFournisseur(avoirFournisseur);
//        //logique changé plus de calcul de pmp lors des avoir
////        pricingService.updatePricesAfterAvoirFournisseur(avoirFournisseur, avoirFournisseur.getCategDepot());
//        ajustementAvoirFournisseurService.saveAjustementAvoirFournisseur(avoirFournisseur);
        paramService.updateCompteurPharmacie(avoirfournisseurDTO.getCategDepot(), avoirfournisseurDTO.getTypbon());
        AvoirFournisseurDTO resultDTO = avoirFournisseurFactory.avoirFournisseurToAvoirFournisseurDTOLazy(avoirFournisseur);
        return resultDTO;
    }

    /**
     * Update a avoirfournisseurDTO.
     *
     * @param avoirfournisseurDTO
     * @return the updated entity
     */
    public AvoirFournisseurDTO update(AvoirFournisseurDTO avoirfournisseurDTO) {
        log.debug("Request to update AvoirFournisseur: {}", avoirfournisseurDTO);
        AvoirFournisseur inBase = avoirfournisseurRepository.findOne(avoirfournisseurDTO.getNumbon());
        checkBusinessLogique(inBase != null, "avoirfournisseur.NotFound");
        AvoirFournisseur avoirfournisseur = avoirFournisseurFactory.avoirFournisseurDTOToAvoirFournisseur(avoirfournisseurDTO);
        avoirfournisseur = avoirfournisseurRepository.save(avoirfournisseur);
        AvoirFournisseurDTO resultDTO = avoirFournisseurFactory.avoirFournisseurToAvoirFournisseurDTO(avoirfournisseur);
        return resultDTO;
    }

}
