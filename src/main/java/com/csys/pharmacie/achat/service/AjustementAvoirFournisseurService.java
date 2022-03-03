/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.AjustementAvoirFournisseur;
import com.csys.pharmacie.achat.domain.AjustementAvoirFournisseurPK;
import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import com.csys.pharmacie.achat.domain.MvtstoAF;
import com.csys.pharmacie.achat.repository.AjustementAvoirFournisseurRepository;
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
 *
 * @author DELL
 */
@Service
@Transactional
public class AjustementAvoirFournisseurService {

    private final Logger log = LoggerFactory.getLogger(AjustementAvoirFournisseurService.class);
    private final ParamService paramService;
    private final AjustementAvoirFournisseurRepository ajustementAvoirFournisseurRepository;

    public AjustementAvoirFournisseurService(ParamService paramService, AjustementAvoirFournisseurRepository ajustementAvoirFournisseurRepository) {
        this.paramService = paramService;
        this.ajustementAvoirFournisseurRepository = ajustementAvoirFournisseurRepository;
    }

    public List<AjustementAvoirFournisseur> saveAjustementAvoirFournisseur(AvoirFournisseur avoirFournisseur) {
        log.debug("Request to save AjustementAvoirFournisseur: ");
        List<AjustementAvoirFournisseur> listeAjustementAvoirFournisseur = new ArrayList();
        MvtstoAF mvtstoAFIdentity = new MvtstoAF(BigDecimal.ZERO, BigDecimal.ZERO);
        mvtstoAFIdentity.setTautva(BigDecimal.ZERO);
        mvtstoAFIdentity.setRemise(BigDecimal.ZERO);
        mvtstoAFIdentity.setDetailMvtStoAFList(new ArrayList());
        String numBonAjustement = paramService.getcompteur(avoirFournisseur.getCategDepot(), TypeBonEnum.AJ);
        log.debug("MvtstoAFs sont {} ", avoirFournisseur.getMvtstoAFList());
        Map<Integer, Map<Integer, MvtstoAF>> map = avoirFournisseur.getMvtstoAFList().stream()
                .collect(groupingBy(MvtstoAF::getCodart,
                        groupingBy(MvtstoAF::getUnite,
                                Collectors.reducing(mvtstoAFIdentity, (a, b) -> {

                                    AjustementAvoirFournisseur ajustementAvoirFournisseur = new AjustementAvoirFournisseur();

                                    BigDecimal mntTTCdepsto = ((a.getDetailMvtStoAFList().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> (x.getPriuni().multiply(x.getTauxTva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                            .multiply(x.getQuantitePrelevee()),
                                                    BigDecimal::add)))
                                            .add(b.getDetailMvtStoAFList().stream()
                                                    .collect(Collectors.reducing(BigDecimal.ZERO,
                                                            x -> (x.getPriuni().multiply(x.getTauxTva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                                    .multiply(x.getQuantitePrelevee()),
                                                            BigDecimal::add))));
                                    BigDecimal mntTTCA;
                                    if (a.getPriuni().compareTo(BigDecimal.ZERO) == 0 && a.getBaseTva()!= null && a.getBaseTva().compareTo(BigDecimal.ZERO)> 0)
                                    {
                                        mntTTCA = (a.getBaseTva()
                                                .multiply(a.getTautva())
                                                .divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                .multiply(a.getQuantite()) ;       }
                                    else {
                                        mntTTCA = (a.getPriuni()
                                                .multiply((BigDecimal.valueOf(100).subtract(a.getRemise())).divide(BigDecimal.valueOf(100)))// remise
                                                .multiply(a.getTautva().add(new BigDecimal(100)))
                                                .divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))//tva
                                            .multiply(a.getQuantite());
                                    }
                                    
                               BigDecimal mntTTCB;
                                    if (b.getPriuni().compareTo(BigDecimal.ZERO) == 0  && b.getBaseTva()!= null && b.getBaseTva().compareTo(BigDecimal.ZERO) > 0) {
                                        mntTTCB = (b.getBaseTva()
                                                .multiply(b.getTautva())
                                                .divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                .multiply(b.getQuantite())  ;      }
                                    else {
                                             mntTTCB = (b.getPriuni()
                                                    .multiply((BigDecimal.valueOf(100).subtract(b.getRemise())).divide(BigDecimal.valueOf(100)))//remise
                                                    .multiply(b.getTautva().add(new BigDecimal(100)))
                                                     .divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))//tva
                                                  .multiply(b.getQuantite());}
                                       BigDecimal mntTTCRetour=  mntTTCA.add(mntTTCB);
                                    log.error("mntTTCRetour => {}", mntTTCRetour);
                                    log.error("mntTTCdepsto => {}", mntTTCdepsto);
                                    ajustementAvoirFournisseur.setDiffMntTtc(mntTTCRetour.subtract(mntTTCdepsto).setScale(3, RoundingMode.HALF_UP));
//                                    b.getDetailMvtStoAFList().forEach(x -> log.error("x.getDepsto().getMemo().contains(\"FREE\")  est{} ", x.getDepsto().getMemo().contains("FREE")));
                                    BigDecimal mntA = a.getDetailMvtStoAFList().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> x.getDepsto().getMemo().contains("FREE") ? x.getPriuni().multiply(BigDecimal.ZERO).setScale(7, RoundingMode.HALF_UP) : x.getPriuni().multiply(x.getQuantitePrelevee()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add));

                                    BigDecimal mntB = b.getDetailMvtStoAFList().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> x.getDepsto().getMemo() != null && x.getDepsto().getMemo().contains("FREE") ? x.getPriuni().multiply(BigDecimal.ZERO).setScale(7, RoundingMode.HALF_UP) : x.getPriuni().multiply(x.getQuantitePrelevee()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add));

                                    BigDecimal mntHTdepsto = mntA.add(mntB);
                                    BigDecimal mntHTRetour = a.getPriuni()
                                            .multiply((BigDecimal.valueOf(100).subtract(a.getRemise())).divide(BigDecimal.valueOf(100)))// remise
                                            .multiply(a.getQuantite())
                                            .add(b.getPriuni()
                                                    .multiply((BigDecimal.valueOf(100).subtract(b.getRemise())).divide(BigDecimal.valueOf(100)))//remise       
                                                    .multiply(b.getQuantite())).setScale(7, RoundingMode.HALF_UP);

                                    log.error("mntHTRetour => {}", mntHTRetour.toString());
                                    log.error("mntHTdepsto => {}", mntHTdepsto.toString());

                                    ajustementAvoirFournisseur.setDiffMntHt(mntHTRetour.subtract(mntHTdepsto).setScale(3, RoundingMode.HALF_UP));
                                    log.debug("difTTC et difHT sont {} {}",ajustementAvoirFournisseur.getDiffMntTtc().toString(),ajustementAvoirFournisseur.getDiffMntHt().toString());
                                    if (ajustementAvoirFournisseur.getDiffMntTtc().compareTo(BigDecimal.ZERO) != 0 || ajustementAvoirFournisseur.getDiffMntHt().compareTo(BigDecimal.ZERO) != 0) {
                                        ajustementAvoirFournisseur.setAjustementAvoirFournisseurPK(new AjustementAvoirFournisseurPK(numBonAjustement, b.getCodart(), b.getUnite(), avoirFournisseur.getNumbon()));
                                        ajustementAvoirFournisseur.setCodeDepot(avoirFournisseur.getCoddep());
                                        ajustementAvoirFournisseur.setAvoirFournisseur(avoirFournisseur);
                                        listeAjustementAvoirFournisseur.add(ajustementAvoirFournisseur);
                                    }
                                    return mvtstoAFIdentity;
                                }
                                ))));
        if (!listeAjustementAvoirFournisseur.isEmpty()) {
            ajustementAvoirFournisseurRepository.save(listeAjustementAvoirFournisseur);
            paramService.updateCompteurPharmacie(avoirFournisseur.getCategDepot(), TypeBonEnum.AJ);
        }
        return listeAjustementAvoirFournisseur;
    }

}
