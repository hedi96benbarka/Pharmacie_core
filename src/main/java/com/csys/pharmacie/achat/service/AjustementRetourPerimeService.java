package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.AjustementRetourPerime;
import com.csys.pharmacie.achat.domain.AjustementRetourPerimePK;
import com.csys.pharmacie.achat.domain.MvtstoRetourPerime;
import com.csys.pharmacie.achat.domain.RetourPerime;
import com.csys.pharmacie.achat.repository.AjustementRetourPerimeRepository;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing AjustementRetourPerime.
 */
@Service
@Transactional
public class AjustementRetourPerimeService {

    private final Logger log = LoggerFactory.getLogger(AjustementRetourPerimeService.class);

    private final AjustementRetourPerimeRepository ajustementRetourRerimeRepository;
    private final ParamService paramService;

    public AjustementRetourPerimeService(AjustementRetourPerimeRepository ajustementRetourRerimeRepository, ParamService paramService) {
        this.ajustementRetourRerimeRepository = ajustementRetourRerimeRepository;
        this.paramService = paramService;
    }

    public Collection<AjustementRetourPerime> saveAjustementRetour(RetourPerime factureRP) {
        List<MvtstoRetourPerime> mvtstoRetourPerimes = factureRP.getDetailFactureRPCollection();
        Collection<AjustementRetourPerime> ajustementRetours = new ArrayList<>();
        MvtstoRetourPerime mvtstoRPIdentity = new MvtstoRetourPerime(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        mvtstoRPIdentity.setDetailMvtStoList(new ArrayList<>());

        String numbon = paramService.getcompteur(mvtstoRetourPerimes.get(0).getCategDepot(), TypeBonEnum.AJ);

        Map<Integer, Map<Integer, MvtstoRetourPerime>> xxx = mvtstoRetourPerimes.stream()
                .collect(groupingBy(MvtstoRetourPerime::getCodart,
                        groupingBy(MvtstoRetourPerime::getUnite,
                                Collectors.reducing(mvtstoRPIdentity, (a, b) -> {

                                    AjustementRetourPerime ajustementRetourPerime = new AjustementRetourPerime();

                                    BigDecimal mntTTCdepsto = ((a.getDetailMvtStoList().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> (x.getPriuni().multiply(x.getTauxTva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                            .multiply(x.getQuantitePrelevee()),
                                                    BigDecimal::add)))
                                            .add(b.getDetailMvtStoList().stream()
                                                    .collect(Collectors.reducing(BigDecimal.ZERO,
                                                            x -> (x.getPriuni().multiply(x.getTauxTva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                                    .multiply(x.getQuantitePrelevee()),
                                                            BigDecimal::add))));

//                                    log.error("mntTTCdepsto => {}", mntTTCdepsto);
                                    BigDecimal mntTTCRetour = ((a.getPriuni().multiply(a.getTautva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                            .multiply(a.getQuantite()))
                                            .add((b.getPriuni().multiply(b.getTautva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                    .multiply(b.getQuantite()));

//                                    log.error("mntTTC => {}", mntTTC);
//                                    log.error("result => {}", (mntTTCdepsto.subtract(mntTTC)).setScale(7, RoundingMode.HALF_UP));
                                    ajustementRetourPerime.setDiffMntTtc(
                                            (mntTTCRetour.subtract(mntTTCdepsto)).setScale(7, RoundingMode.HALF_UP)
                                    );

                                    BigDecimal mntHTdepsto = (a.getDetailMvtStoList().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> x.getPriuni().multiply(x.getQuantitePrelevee())
                                                            .setScale(7, RoundingMode.HALF_UP),
                                                    BigDecimal::add)))
                                            .add(b.getDetailMvtStoList().stream()
                                                    .collect(Collectors.reducing(BigDecimal.ZERO,
                                                            x -> x.getPriuni().multiply(x.getQuantitePrelevee())
                                                                    .setScale(7, RoundingMode.HALF_UP),
                                                            BigDecimal::add)));
//                                    log.error("mntHTdepsto => {}", mntHTdepsto);

                                    BigDecimal mntHTRetour = a.getPriuni().multiply(a.getQuantite())
                                            .add(b.getPriuni().multiply(b.getQuantite()));

//                                    log.error("mntHT => {}", mntHT);
//                                    log.error("result => {}", (mntHTdepsto.subtract(mntHT)).setScale(7, RoundingMode.HALF_UP));
                                    ajustementRetourPerime.setDiffMntHt(mntHTRetour.subtract(mntHTdepsto).setScale(7, RoundingMode.HALF_UP));

                                    if (ajustementRetourPerime.getDiffMntHt().compareTo(BigDecimal.ZERO) != 0  || ajustementRetourPerime.getDiffMntTtc().compareTo(BigDecimal.ZERO) != 0) {
                                        ajustementRetourPerime.setAjustementRetourPerimePK(new AjustementRetourPerimePK(numbon, b.getCodart(), b.getUnite(), b.getNumbon()));
                                        ajustementRetourPerime.setCodeDepot(factureRP.getCoddep());
                                        ajustementRetours.add(ajustementRetourPerime);
                                    }
                                    return mvtstoRPIdentity;
                                }
                                ))));
        if (!ajustementRetours.isEmpty()) {
            ajustementRetourRerimeRepository.save(ajustementRetours);
            paramService.updateCompteurPharmacie(factureRP.getCategDepot(), TypeBonEnum.AJ);
        }
        return ajustementRetours;
    }


}
