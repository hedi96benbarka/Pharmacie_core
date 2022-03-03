package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.AjustementRetourFournisseur;
import com.csys.pharmacie.achat.domain.AjustementRetourFournisseurPK;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.dto.BonRetourDTO;
import com.csys.pharmacie.achat.factory.FactureBAFactory;
import com.csys.pharmacie.achat.repository.AjustementRetourFournisseurRepository;
import com.csys.pharmacie.achat.repository.FactureBARepository;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing AjustementRetourFournisseur.
 */
@Service
@Transactional
public class AjustementRetourFournisseurService {

    private final Logger log = LoggerFactory.getLogger(AjustementRetourFournisseurService.class);
    private final ParamService paramService;
    private final AjustementRetourFournisseurRepository ajustementretourfournisseurRepository;
    private final FactureBARepository factureBARepository;

    public AjustementRetourFournisseurService(ParamService paramService, AjustementRetourFournisseurRepository ajustementretourfournisseurRepository, FactureBARepository factureBARepository) {
        this.paramService = paramService;
        this.ajustementretourfournisseurRepository = ajustementretourfournisseurRepository;
        this.factureBARepository = factureBARepository;
    }



    public List<AjustementRetourFournisseur> saveAjustementRetour(FactureBA bonRetour) {

        log.debug("Request to save AjustementRetourFournisseur: ");
        List<AjustementRetourFournisseur> listeAjustementRetourFournisseur = new ArrayList();
        MvtStoBA mvtstoRetourIdentity = new MvtStoBA(BigDecimal.ZERO, BigDecimal.ZERO);
        mvtstoRetourIdentity.setTautva(BigDecimal.ZERO);
        mvtstoRetourIdentity.setRemise(BigDecimal.ZERO);
        mvtstoRetourIdentity.setDetailMvtStoBACollection(new ArrayList());
        String numBonAjustement = paramService.getcompteur(bonRetour.getCategDepot(), TypeBonEnum.AJ);
        log.debug("DetailFactureBACollection sont {} ", bonRetour.getDetailFactureBACollection());
        Map<Integer, Map<Integer, MvtStoBA>> map = bonRetour.getDetailFactureBACollection().stream()
                .collect(groupingBy(MvtStoBA::getCodart,
                        groupingBy(MvtStoBA::getCodeUnite,
                                Collectors.reducing(mvtstoRetourIdentity, (a, b) -> {

                                    AjustementRetourFournisseur ajustementRetourFournisseur = new AjustementRetourFournisseur();

                                    BigDecimal mntTTCdepsto = ((a.getDetailMvtStoBACollection().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> (x.getPriuni().multiply(x.getTauxTva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                            .multiply(x.getQuantite_retourne()),
                                                    BigDecimal::add)))
                                            .add(b.getDetailMvtStoBACollection().stream()
                                                    .collect(Collectors.reducing(BigDecimal.ZERO,
                                                            x -> (x.getPriuni().multiply(x.getTauxTva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                                    .multiply(x.getQuantite_retourne()),
                                                            BigDecimal::add))));
//                                    log.error("mntTTCdepsto => {}", mntTTCdepsto);
                                    BigDecimal mntTTCRetour = ((a.getPriuni().multiply((BigDecimal.valueOf(100).subtract(a.getRemise())).divide(BigDecimal.valueOf(100))).multiply(a.getTautva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                            .multiply(a.getQuantite()))
                                            .add((b.getPriuni().multiply((BigDecimal.valueOf(100).subtract(b.getRemise())).divide(BigDecimal.valueOf(100))).multiply(b.getTautva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                    .multiply(b.getQuantite()));
                                    log.debug("mntTTCRetour => {}", mntTTCRetour);
                                    log.debug("mntTTCdepsto => {}", mntTTCdepsto);
                                    ajustementRetourFournisseur.setDiffMntTtc(mntTTCRetour.subtract(mntTTCdepsto).setScale(7, RoundingMode.HALF_UP));

                                    BigDecimal mntA = a.getDetailMvtStoBACollection().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> x.getPriuni().multiply(x.getQuantite_retourne()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add));
                                    BigDecimal mntB = b.getDetailMvtStoBACollection().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> x.getPriuni().multiply(x.getQuantite_retourne())
                                                            .setScale(7, RoundingMode.HALF_UP), BigDecimal::add));

                                    BigDecimal mntHTdepsto = mntA.add(mntB);
                                    BigDecimal mntHTRetour = a.getPriuni().multiply(a.getQuantite()).add(b.getPriuni().multiply(b.getQuantite()));

                                    log.debug("mntHTRetour => {}", mntHTRetour);
                                    log.debug("mntHTdepsto => {}", mntHTdepsto);

                                    ajustementRetourFournisseur.setDiffMntHt(mntHTRetour.subtract(mntHTdepsto).setScale(7, RoundingMode.HALF_UP));
//                                    log.debug("difTTC et difHT sont {} {}",ajustementRetourFournisseur.getDiffMntTtc(),ajustementRetourFournisseur.getDiffMntHt().toString());
//                                    Boolean test = ajustementRetourFournisseur.getDiffMntTtc().compareTo(BigDecimal.ZERO) != 0;
//                                    Boolean test2 = ajustementRetourFournisseur.getDiffMntHt().compareTo(BigDecimal.ZERO) != 0;
//                                    Boolean test3 = test || test2;
//                                    log.debug(" test , test2, test3 sont {},{},{}",test,test2,test3); 
                                    if (ajustementRetourFournisseur.getDiffMntTtc().compareTo(BigDecimal.ZERO) != 0 || ajustementRetourFournisseur.getDiffMntHt().compareTo(BigDecimal.ZERO) != 0) {
                                        log.debug("ajustementRetourFournisseurr", ajustementRetourFournisseur);
                                        ajustementRetourFournisseur.setAjustementRetourFournisseurPK(new AjustementRetourFournisseurPK(numBonAjustement, b.getCodart(), b.getCodeUnite(), bonRetour.getNumbon()));
                                        ajustementRetourFournisseur.setCodeDepot(bonRetour.getCoddep());
                                        ajustementRetourFournisseur.setFactureBA(bonRetour);
                                        listeAjustementRetourFournisseur.add(ajustementRetourFournisseur);
                                    }
                                    return mvtstoRetourIdentity;
                                }
                                ))));
        if (!listeAjustementRetourFournisseur.isEmpty()) {
            log.debug("listeAjustementRetourFournisseur", listeAjustementRetourFournisseur);
            ajustementretourfournisseurRepository.save(listeAjustementRetourFournisseur);
            paramService.updateCompteurPharmacie(bonRetour.getCategDepot(), TypeBonEnum.AJ);
        }
        return listeAjustementRetourFournisseur;
    }

    public List<AjustementRetourFournisseur> testAjustementRetour(String numbonbonRetour) {
        FactureBA bonRetour = factureBARepository.findOne(numbonbonRetour);
        com.csys.util.Preconditions.checkBusinessLogique(bonRetour != null, "reception.missing");
        BonRetourDTO result = FactureBAFactory.factureBAToBonRetourDTOEager(bonRetour);
//        result.setDesignationDepot(paramAchatServiceClient.findDepotByCode(retour.getCoddep()).getDesdep());
//        result.setFournisseur(paramAchatServiceClient.findFournisseurByCode(retour.getCodfrs()));

        FactureBA reception = factureBARepository.findOne(bonRetour.getNumpiece());
        result.setDateReception(reception.getDatbon());
        result.setMntBonReception(reception.getMntbon());
        result.setNumAfficheReception(reception.getNumaffiche());
        result.setReceptionID(reception.getNumbon());
        log.debug("Request to save AjustementRetourFournisseur: ");
        List<AjustementRetourFournisseur> listeAjustementRetourFournisseur = new ArrayList();
        MvtStoBA mvtstoRetourIdentity = new MvtStoBA(BigDecimal.ZERO, BigDecimal.ZERO);
        mvtstoRetourIdentity.setTautva(BigDecimal.ZERO);
        mvtstoRetourIdentity.setRemise(BigDecimal.ZERO);
        mvtstoRetourIdentity.setDetailMvtStoBACollection(new ArrayList());
//        String numBonAjustement = paramService.getcompteur(bonRetour.getCategDepot(), TypeBonEnum.AJ);
        log.debug("DetailFactureBACollection sont {} ", bonRetour.getDetailFactureBACollection());
        Map<Integer, Map<Integer, MvtStoBA>> map = bonRetour.getDetailFactureBACollection().stream()
                .collect(groupingBy(MvtStoBA::getCodart,
                        groupingBy(MvtStoBA::getCodeUnite,
                                Collectors.reducing(mvtstoRetourIdentity, (a, b) -> {

                                    AjustementRetourFournisseur ajustementRetourFournisseur = new AjustementRetourFournisseur();

                                    BigDecimal mntTTCdepsto = ((a.getDetailMvtStoBACollection().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> (x.getPriuni().multiply(x.getTauxTva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                            .multiply(x.getQuantite_retourne()),
                                                    BigDecimal::add)))
                                            .add(b.getDetailMvtStoBACollection().stream()
                                                    .collect(Collectors.reducing(BigDecimal.ZERO,
                                                            x -> (x.getPriuni().multiply(x.getTauxTva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                                    .multiply(x.getQuantite_retourne()),
                                                            BigDecimal::add))));
                                    log.error("mntTTCdepsto => {}", mntTTCdepsto);
                                    BigDecimal mntTTCRetour = ((a.getPriuni().multiply((BigDecimal.valueOf(100).subtract(a.getRemise())).divide(BigDecimal.valueOf(100))).multiply(a.getTautva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                            .multiply(a.getQuantite()))
                                            .add((b.getPriuni().multiply((BigDecimal.valueOf(100).subtract(b.getRemise())).divide(BigDecimal.valueOf(100))).multiply(b.getTautva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                    .multiply(b.getQuantite()));
                                    log.debug("mntTTCRetour => {}", mntTTCRetour);
                                    log.debug("mntTTCdepsto => {}", mntTTCdepsto);
                                    ajustementRetourFournisseur.setDiffMntTtc(mntTTCRetour.subtract(mntTTCdepsto).setScale(7, RoundingMode.HALF_UP));

                                    BigDecimal mntA = a.getDetailMvtStoBACollection().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> x.getPriuni().multiply(x.getQuantite_retourne()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add));
                                    BigDecimal mntB = b.getDetailMvtStoBACollection().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> x.getPriuni().multiply(x.getQuantite_retourne())
                                                            .setScale(7, RoundingMode.HALF_UP), BigDecimal::add));

                                    BigDecimal mntHTdepsto = mntA.add(mntB);
                                    BigDecimal mntHTRetour = a.getPriuni().multiply(a.getQuantite()).add(b.getPriuni().multiply(b.getQuantite()));

                                    log.debug("mntHTRetour => {}", mntHTRetour);
                                    log.debug("mntHTdepsto => {}", mntHTdepsto);

                                    ajustementRetourFournisseur.setDiffMntHt(mntHTRetour.subtract(mntHTdepsto).setScale(7, RoundingMode.HALF_UP));
                                    log.debug("difTTC et difHT sont {} {}",ajustementRetourFournisseur.getDiffMntTtc(),ajustementRetourFournisseur.getDiffMntHt().toString());
                                    Boolean test = ajustementRetourFournisseur.getDiffMntTtc().compareTo(BigDecimal.ZERO) != 0;
                                    Boolean test2 = ajustementRetourFournisseur.getDiffMntHt().compareTo(BigDecimal.ZERO) != 0;
                                    Boolean test3 = test || test2;
                                    log.debug(" test , test2, test3 sont {},{},{}",test,test2,test3); 
                                    if (ajustementRetourFournisseur.getDiffMntTtc().compareTo(BigDecimal.ZERO) != 0 || ajustementRetourFournisseur.getDiffMntHt().compareTo(BigDecimal.ZERO) != 0) {
                                        log.debug("ajustementRetourFournisseurr", ajustementRetourFournisseur);
//                                        ajustementRetourFournisseur.setAjustementRetourFournisseurPK(new AjustementRetourFournisseurPK(numBonAjustement, b.getCodart(), b.getCodeUnite(), bonRetour.getNumbon()));
//                                        ajustementRetourFournisseur.setCodeDepot(bonRetour.getCoddep());
//                                        ajustementRetourFournisseur.setFactureBA(bonRetour);
//                                        listeAjustementRetourFournisseur.add(ajustementRetourFournisseur);
                                    }
                                    return mvtstoRetourIdentity;
                                }
                                ))));
//        if (!listeAjustementRetourFournisseur.isEmpty()) {
            log.debug("listeAjustementRetourFournisseur", listeAjustementRetourFournisseur);
//            ajustementretourfournisseurRepository.save(listeAjustementRetourFournisseur);
//            paramService.updateCompteurPharmacie(bonRetour.getCategDepot(), TypeBonEnum.AJ);

            return listeAjustementRetourFournisseur;

//        }
       
    }
      
    /**
     * Get all the ajustementretourfournisseurs.
     *
     * @return the the list of entities
     */
//    @Transactional(readOnly = true)
//    public Collection<AjustementRetourFournisseurDTO> findAll() {
//        log.debug("Request to get All AjustementRetourFournisseurs");
//        Collection<AjustementRetourFournisseur> result = ajustementretourfournisseurRepository.findAll();
//        return AjustementRetourFournisseurFactory.ajustementretourfournisseurToAjustementRetourFournisseurDTOs(result);
//    }


   
}
