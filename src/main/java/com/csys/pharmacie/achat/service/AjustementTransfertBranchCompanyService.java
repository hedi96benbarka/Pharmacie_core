package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.AjustementTransfertBranchCompany;
import com.csys.pharmacie.achat.domain.AjustementTransfertBranchCompanyPK;
import com.csys.pharmacie.achat.domain.DetailTransfertCompanyBranch;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.repository.AjustementTransfertBranchCompanyRepository;
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



@Service
@Transactional
public class AjustementTransfertBranchCompanyService {
  private final Logger log = LoggerFactory.getLogger(AjustementTransfertBranchCompanyService.class);

  private final AjustementTransfertBranchCompanyRepository ajustementTransfertBranchCompanyRepository;
      private final ParamService paramService;

    public AjustementTransfertBranchCompanyService(AjustementTransfertBranchCompanyRepository ajustementTransfertBranchCompanyRepository, ParamService paramService) {
        this.ajustementTransfertBranchCompanyRepository = ajustementTransfertBranchCompanyRepository;
        this.paramService = paramService;
    }

 
  public List<AjustementTransfertBranchCompany> saveAjustementRetour(TransfertCompanyBranch bonRetour) {

        log.debug("Request to save AjustementTransfertBranchCompany: ");
        List<AjustementTransfertBranchCompany> listeAjustementTransfertBranchCompany = new ArrayList();
        DetailTransfertCompanyBranch detailTransfertIdentity = new DetailTransfertCompanyBranch(BigDecimal.ZERO, BigDecimal.ZERO);
        detailTransfertIdentity.setTauxTva(BigDecimal.ZERO);
        detailTransfertIdentity.setRemise(BigDecimal.ZERO);
        detailTransfertIdentity.setListeTraceDetailTransfertCompanyBranch(new ArrayList());
        String numBonAjustement = paramService.getcompteur(bonRetour.getCategDepot(), TypeBonEnum.AJ);
        log.debug("DetailFactureBACollection sont {} ", bonRetour.getDetailTransfertCompanyBranchCollection());
        Map<Integer, Map<Integer, DetailTransfertCompanyBranch>> map = bonRetour.getDetailTransfertCompanyBranchCollection().stream()
                .collect(groupingBy(DetailTransfertCompanyBranch::getCodeArticle,
                        groupingBy(DetailTransfertCompanyBranch::getCodeUnite,
                                Collectors.reducing(detailTransfertIdentity, (a, b) -> {

                                    AjustementTransfertBranchCompany ajustementTransfertBranchCompany = new AjustementTransfertBranchCompany();

                                    ajustementTransfertBranchCompany.setIntegrer(Boolean.FALSE);
                                    
                                    BigDecimal mntTTCdepsto = ((a.getListeTraceDetailTransfertCompanyBranch().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> (x.getQuantitePrelevee().multiply(x.getTauxTvaDepsto().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                            .multiply(x.getQuantitePrelevee()),
                                                    BigDecimal::add)))
                                            .add(b.getListeTraceDetailTransfertCompanyBranch().stream()
                                                    .collect(Collectors.reducing(BigDecimal.ZERO,
                                                            x -> (x.getPrixUnitaireDepsto().multiply(x.getTauxTvaDepsto().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                                    .multiply(x.getQuantitePrelevee()),
                                                            BigDecimal::add))));
//                                    log.error("mntTTCdepsto => {}", mntTTCdepsto);
                                    BigDecimal mntTTCRetour = ((a.getPrixUnitaire().multiply((BigDecimal.valueOf(100).subtract(a.getRemise())).divide(BigDecimal.valueOf(100))).multiply(a.getTauxTva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                            .multiply(a.getQuantite()))
                                            .add((b.getPrixUnitaire().multiply((BigDecimal.valueOf(100).subtract(b.getRemise())).divide(BigDecimal.valueOf(100))).multiply(b.getTauxTva().add(new BigDecimal(100))).divide(new BigDecimal(100), 7, RoundingMode.HALF_UP))
                                                    .multiply(b.getQuantite()));
                                    log.debug("mntTTCRetour => {}", mntTTCRetour);
                                    log.debug("mntTTCdepsto => {}", mntTTCdepsto);
                                    ajustementTransfertBranchCompany.setDiffMntTtc(mntTTCRetour.subtract(mntTTCdepsto).setScale(7, RoundingMode.HALF_UP));

                                    BigDecimal mntA = a.getListeTraceDetailTransfertCompanyBranch().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> x.getPrixUnitaireDepsto().multiply(x.getQuantitePrelevee()).setScale(7, RoundingMode.HALF_UP), BigDecimal::add));
                                    BigDecimal mntB = b.getListeTraceDetailTransfertCompanyBranch().stream()
                                            .collect(Collectors.reducing(BigDecimal.ZERO,
                                                    x -> x.getPrixUnitaireDepsto().multiply(x.getQuantitePrelevee())
                                                            .setScale(7, RoundingMode.HALF_UP), BigDecimal::add));

                                    BigDecimal mntHTdepsto = mntA.add(mntB);
                                    BigDecimal mntHTRetour = a.getPrixUnitaire().multiply(a.getQuantite()).add(b.getPrixUnitaire().multiply(b.getQuantite()));

                                    log.debug("mntHTRetour => {}", mntHTRetour);
                                    log.debug("mntHTdepsto => {}", mntHTdepsto);

                                    ajustementTransfertBranchCompany.setDiffMntHt(mntHTRetour.subtract(mntHTdepsto).setScale(7, RoundingMode.HALF_UP));
//                                    log.debug("difTTC et difHT sont {} {}",ajustementRetourFournisseur.getDiffMntTtc(),ajustementRetourFournisseur.getDiffMntHt().toString());
//                                    Boolean test = ajustementRetourFournisseur.getDiffMntTtc().compareTo(BigDecimal.ZERO) != 0;
//                                    Boolean test2 = ajustementRetourFournisseur.getDiffMntHt().compareTo(BigDecimal.ZERO) != 0;
//                                    Boolean test3 = test || test2;
//                                    log.debug(" test , test2, test3 sont {},{},{}",test,test2,test3); 
                                    if (ajustementTransfertBranchCompany.getDiffMntTtc().compareTo(BigDecimal.ZERO) != 0 || ajustementTransfertBranchCompany.getDiffMntHt().compareTo(BigDecimal.ZERO) != 0) {
                                        log.debug("ajustementRetourFournisseurr", ajustementTransfertBranchCompany);
                                        ajustementTransfertBranchCompany.setAjustementTransfertBranchCompanyPK(new AjustementTransfertBranchCompanyPK(numBonAjustement, b.getCodeArticle(), b.getCodeUnite(), bonRetour.getNumBon()));
                                        ajustementTransfertBranchCompany.setCodeDepot(bonRetour.getCodeDepot());
                                        ajustementTransfertBranchCompany.setTransfertCompanyBranch(bonRetour);
                                        listeAjustementTransfertBranchCompany.add(ajustementTransfertBranchCompany);
                                    }
                                    return detailTransfertIdentity;
                                }
                                ))));
        if (!listeAjustementTransfertBranchCompany.isEmpty()) {
            log.debug("listeAjustementTransfertBranchCompany", listeAjustementTransfertBranchCompany);
            ajustementTransfertBranchCompanyRepository.save(listeAjustementTransfertBranchCompany);
            paramService.updateCompteurPharmacie(bonRetour.getCategDepot(), TypeBonEnum.AJ);
        }
        return listeAjustementTransfertBranchCompany;
    }
//
//  @Transactional( readOnly = true)
//  public AjustementTransfertBranchCompanyDTO findOne(AjustementTransfertBranchCompanyPK id) {
//    log.debug("Request to get AjustementTransfertBranchCompany: {}",id);
//    AjustementTransfertBranchCompany ajustementtransfertbranchcompany= ajustementtransfertbranchcompanyRepository.findOne(id);
//    Preconditions.checkArgument(ajustementtransfertbranchcompany != null, "AjustementTransfertBranchCompany does not exist");
//    AjustementTransfertBranchCompanyDTO dto = AjustementTransfertBranchCompanyFactory.ajustementtransfertbranchcompanyToAjustementTransfertBranchCompanyDTO(ajustementtransfertbranchcompany);
//    return dto;
//  }
//
//  @Transactional(
//      readOnly = true
//  )
//  public AjustementTransfertBranchCompany findAjustementTransfertBranchCompany(AjustementTransfertBranchCompanyPK id) {
//    log.debug("Request to get AjustementTransfertBranchCompany: {}",id);
//    AjustementTransfertBranchCompany ajustementtransfertbranchcompany= ajustementtransfertbranchcompanyRepository.findOne(id);
//    Preconditions.checkArgument(ajustementtransfertbranchcompany != null, "AjustementTransfertBranchCompany does not exist");
//    return ajustementtransfertbranchcompany;
//  }
//
//  @Transactional(
//      readOnly = true
//  )
//  public Collection<AjustementTransfertBranchCompanyDTO> findAll() {
//    log.debug("Request to get All AjustementTransfertBranchCompanys");
//    Collection<AjustementTransfertBranchCompany> result= ajustementtransfertbranchcompanyRepository.findAll();
//    return AjustementTransfertBranchCompanyFactory.ajustementtransfertbranchcompanyToAjustementTransfertBranchCompanyDTOs(result);
//  }

 
}

