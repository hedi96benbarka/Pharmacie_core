/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.MvtStoBAPK;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.factory.FactureBAFactory;
import com.csys.pharmacie.achat.repository.FactureBARepository;
import static com.csys.pharmacie.achat.service.FactureBABranchFacadeService.codeSiteConfig;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.EnumCrudMethod;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.vente.quittance.domain.Facture;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class FactureBaBrancheService {
    
    private final Logger log = LoggerFactory.getLogger(FactureBaBrancheService.class);
    private final FactureBARepository factureBARepository;
    private final ParamService paramService;
    private final FcptFrsPHService fcptFrsPHService;
//    private final FactureBAFactory factureBAFactory;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final EtatReceptionCAService etatReceptionCAService;
    private final ParamServiceClient paramServiceClient;
    
    private final Environment env;
    
    public FactureBaBrancheService(FactureBARepository factureBARepository, ParamService paramService, FcptFrsPHService fcptFrsPHService, ParamAchatServiceClient paramAchatServiceClient, EtatReceptionCAService etatReceptionCAService, ParamServiceClient paramServiceClient, Environment env) {
        this.factureBARepository = factureBARepository;
        this.paramService = paramService;
        this.fcptFrsPHService = fcptFrsPHService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.etatReceptionCAService = etatReceptionCAService;
        this.paramServiceClient = paramServiceClient;
        this.env = env;
    }
    
    public Map<Integer, Object> ajoutBonReceptionDepotFrs(Facture facture, DepotDTO depotPrincipale, FournisseurDTO fourniss, Boolean appliquerExoneration, String exoneration) {
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
                bonRecep.setDateDebutExoneration(fourniss.getDateDebutExenoration());
                bonRecep.setDateFinExenoration(fourniss.getDateFinExenoration());
            }
        }
        List<MvtStoBA> details = genererListeMvtstoBaFromFactureWhenOnshelf(facture, bonRecep);
        bonRecep.setDetailFactureBACollection(details);
        
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        bonRecep.calcul(listTvas);
        String numBon = paramService.getcompteur(facture.getCategDepot(), TypeBonEnum.BA);
        bonRecep.setNumbon(numBon);
        bonRecep.setIntegrer(Boolean.FALSE);
        
        FactureBA result = factureBARepository.save(bonRecep);
        Map<Integer, Object> mapResultedObjects = new HashMap();
        mapResultedObjects.put(1, result);
        BonRecepDTO bonRecepDto = FactureBAFactory.factureBAToBonRecepDTONotLazy(bonRecep);
        log.debug("******************* kafka branch *************************");
        paramService.updateCompteurPharmacie(facture.getCategDepot(), TypeBonEnum.BA);
        List<CliniqueDto> cliniqueDto = paramServiceClient.findClinique();
        Preconditions.checkBusinessLogique(cliniqueDto != null, "error.parametrage");
        Integer codeSite = env.acceptsProfiles("testCentral") ? codeSiteConfig : cliniqueDto.get(0).getCodeSite();
        log.debug("codeSiteIs : {}", codeSite.toString());
        bonRecepDto.setCodeSite(codeSite);
        bonRecepDto.setAction(EnumCrudMethod.CREATE);
        mapResultedObjects.put(2, bonRecepDto);
//        sender.send(topicBonReceptionOnShelfManagement, numBon, bonRecepDto);
        return mapResultedObjects;
        
    }
    
    public List<MvtStoBA> genererListeMvtstoBaFromFactureWhenOnshelf(Facture facture, FactureBA bonRecep) {
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
                mvtstoBA.setCodeSaisi(mvtsto.getCodeSaisi());
                mvtstoBA.setDesart(mvtsto.getDesart());
                mvtstoBA.setDesArtSec(mvtsto.getDesArtSec());
                //Prix
                mvtstoBA.setLotInter(mvtsto.getLotInter());
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
                
                mvtstoBA.setQtecom(mvtsto.getQuantite());
                
                mvtstoBA.setIsPrixReference(false);
                mvtstoBA.setDatPer(mvtsto.getDatPer());
                mvtstoBA.setCodeUnite(mvtsto.getUnite());
                mvtstoBA.setBaseTva(BigDecimal.ZERO);
                mvtstoBA.setFactureBA(bonRecep);
                details.add(mvtstoBA);
                numordre = Helper.IncrementString(numordre, 4);
            }
        }
        return details;
    }
    
    public BonRecepDTO cancelBonReceptionDepotFrs(String numBon) {
        
        log.debug("cancelling new FactureBA {}", numBon);
        FactureBA factureBA = factureBARepository.findOne(numBon);
        Preconditions.checkBusinessLogique(factureBA != null, "reception.notFound");
        Preconditions.checkBusinessLogique(factureBA.getCodAnnul() == null, "reception.delete.reception-canceld");
        Preconditions.checkBusinessLogique(factureBA.getFactureBonReception() == null, "facture-bon-reception.invoiced-reception", factureBA.getNumbon());
//        fcptFrsPHService.deleteFcptfrsByNumBonDao(numBon, factureBA.getTypbon());
        factureBA.setCodAnnul(SecurityContextHolder.getContext().getAuthentication().getName());
        factureBA.setDatAnnul(LocalDateTime.now());
//        List<Integer> listCodesCA = factureBA.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());// list of received purchase orders
//        etatReceptionCAService.deleteByCommandeAchatIn(listCodesCA); // delete the state of the recived purchase orders 
//        List<ReceptionDetailCA> receptionDetailCAs = receptionDetailCAService.findByCodesCAIn(listCodesCA);
//        List<EtatReceptionCA> partRecivedPurchOrdes = receptionDetailCAs.stream() // recalculate their state 
//                .filter(item -> !item.getPk().getReception().equals(factureBA.getNumbon()) && item.getQuantiteReceptione().compareTo(BigDecimal.ZERO) > 0)
//                .map(filtredItem -> {
//                    return new EtatReceptionCA(filtredItem.getPk().getCommandeAchat(), NOT_RECEIVED);
//                })
//                .collect(Collectors.toList());
//        factureBA.getRecivedDetailCA().clear();
//        etatReceptionCAService.save(partRecivedPurchOrdes);
        FactureBA result = factureBARepository.save(factureBA);
        BonRecepDTO bonRecepDto = FactureBAFactory.factureBAToBonRecepDTO(factureBA);
        bonRecepDto.setDatbon(null);//probleme de serialisation
        List<CliniqueDto> cliniqueDto = paramServiceClient.findClinique();
        Preconditions.checkBusinessLogique(cliniqueDto != null, "error.parametrage");
        Integer codeSite = env.acceptsProfiles("testCentral") ? codeSiteConfig : cliniqueDto.get(0).getCodeSite();
        bonRecepDto.setCodeSite(codeSite);
        bonRecepDto.setAction(EnumCrudMethod.UPDATE);
        
        return bonRecepDto;
    }
    
    public BonRecepDTO cancelBonReceptionDepotFrsPermanent(String numBon) {
        
        log.debug("cancelling new FactureBA {}", numBon);
        FactureBA factureBA = factureBARepository.findOne(numBon);
        Preconditions.checkBusinessLogique(factureBA != null, "reception.notFound");
        Preconditions.checkBusinessLogique(factureBA.getCodAnnul() == null, "reception.delete.reception-canceld");
        fcptFrsPHService.deleteFcptfrsByNumBonDao(numBon, factureBA.getTypbon());
        List<Integer> listCodesCA = factureBA.getRecivedDetailCA().stream().map(item -> item.getPk().getCommandeAchat()).distinct().collect(Collectors.toList());// list of received purchase orders
        etatReceptionCAService.deleteByCommandeAchatIn(listCodesCA); // delete the state of the recived purchase orders 
        factureBARepository.delete(factureBA);
        BonRecepDTO bonRecepDto = FactureBAFactory.factureBAToBonRecepDTO(factureBA);
        List<CliniqueDto> cliniqueDto = paramServiceClient.findClinique();
        Preconditions.checkBusinessLogique(cliniqueDto != null, "error.parametrage");
        Integer codeSite = env.acceptsProfiles("testCentral") ? codeSiteConfig : cliniqueDto.get(0).getCodeSite();
        bonRecepDto.setCodeSite(codeSite);
        bonRecepDto.setAction(EnumCrudMethod.DELETE);
        
        return bonRecepDto;
    }
}
