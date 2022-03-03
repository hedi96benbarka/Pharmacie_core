package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.DetailTransfertCompanyBranch;
import com.csys.pharmacie.achat.domain.QTransfertCompanyBranch;
import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DetailTransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.factory.TransfertCompanyBranchFactory;
import com.csys.pharmacie.achat.repository.TransfertCompanyBranchRepository;
import com.csys.pharmacie.client.dto.SiteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.config.Sender;
import static com.csys.pharmacie.config.ServicesConfig.contextReception;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.util.Preconditions;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;

public abstract class TransfertCompanyBranchService {

    @Autowired
    protected final Sender sender;

    public static Integer actualCodeSiteTest;

    @Value("${code-site}")
    public void setCodeSite(Integer codeSite) {
        actualCodeSiteTest = codeSite;
    }

    public static Integer codeCompanyForBranchConfig;

    @Value("${code-company-for-branch}")
    public void setCodeCompanyForBranch(Integer codeCompanyForBranch) {
        codeCompanyForBranchConfig = codeCompanyForBranch;
    }

    protected final Logger log = LoggerFactory.getLogger(TransfertCompanyBranchService.class);

    protected final TransfertCompanyBranchRepository transfertcompanybranchRepository;

    protected final TopicPartitionOffsetService topicPartitionOffsetService;

    protected final KafkaConsumerErrorService kafkaConsumerErrorService;

    protected final ParamService paramService;

    protected final ParamAchatServiceClient paramAchatServiceClient;

    protected final Environment env;

    protected final ParamServiceClient paramServiceClient;

    public TransfertCompanyBranchService(Sender sender, TransfertCompanyBranchRepository transfertcompanybranchRepository, TopicPartitionOffsetService topicPartitionOffsetService, KafkaConsumerErrorService kafkaConsumerErrorService, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient, Environment env, ParamServiceClient paramServiceClient) {
        this.sender = sender;
        this.transfertcompanybranchRepository = transfertcompanybranchRepository;
        this.topicPartitionOffsetService = topicPartitionOffsetService;
        this.kafkaConsumerErrorService = kafkaConsumerErrorService;
        this.paramService = paramService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.env = env;
        this.paramServiceClient = paramServiceClient;
    }

    @Transactional(readOnly = true)
    public Collection<TransfertCompanyBranchDTO> findAll(TransfertCompanyBranch transfertCompanyBranch, LocalDateTime fromDate, LocalDateTime toDate, Integer codeArticle, Boolean receptionRelativeFacturee, Boolean returnedToSupplier, Integer codeSite) {
        log.debug("Request to get All transfert Company branch");
        //on ne peut appliquer ce filtre que en company , il n ya pas de reception au niveau branch dans le filtre va erroner les resultats 
        Boolean receptionRelativeFactureeInCompany = contextReception.contains("company") ? receptionRelativeFacturee : null;

        QTransfertCompanyBranch _TransfertCompanyBranch = QTransfertCompanyBranch.transfertCompanyBranch;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(transfertCompanyBranch.getTypeBon(), () -> _TransfertCompanyBranch.typeBon.eq(transfertCompanyBranch.getTypeBon()))
                .optionalAnd(transfertCompanyBranch.getCategDepot(), () -> _TransfertCompanyBranch.categDepot.eq(transfertCompanyBranch.getCategDepot()))
                .optionalAnd(fromDate, () -> _TransfertCompanyBranch.dateCreate.goe(fromDate))
                .optionalAnd(toDate, () -> _TransfertCompanyBranch.dateCreate.loe(toDate))
                .optionalAnd(transfertCompanyBranch.getCodeFournisseur(), () -> _TransfertCompanyBranch.codeFournisseur.eq(transfertCompanyBranch.getCodeFournisseur()))
                .optionalAnd(transfertCompanyBranch.getCodeDepot(), () -> _TransfertCompanyBranch.codeDepot.eq(transfertCompanyBranch.getCodeDepot()))
                .optionalAnd(codeArticle, () -> _TransfertCompanyBranch.detailTransfertCompanyBranchCollection.any().codeArticle.eq(codeArticle))
                .booleanAnd(Objects.equals(receptionRelativeFactureeInCompany, Boolean.TRUE), () -> _TransfertCompanyBranch.receptionRelative().factureBonReception().isNotNull())
                .booleanAnd(Objects.equals(receptionRelativeFactureeInCompany, Boolean.FALSE), () -> _TransfertCompanyBranch.receptionRelative().factureBonReception().isNull())
                .booleanAnd(Objects.equals(returnedToSupplier, Boolean.TRUE), () -> _TransfertCompanyBranch.returnedToSupplier.isTrue())
                .booleanAnd(Objects.equals(returnedToSupplier, Boolean.FALSE), () -> _TransfertCompanyBranch.returnedToSupplier.isNull().or(_TransfertCompanyBranch.returnedToSupplier.eq(Boolean.FALSE)))
                .optionalAnd(codeSite, () -> _TransfertCompanyBranch.codeSite.eq(codeSite));
        List<TransfertCompanyBranch> result = (List<TransfertCompanyBranch>) transfertcompanybranchRepository.findAll(builder);
        Collection<TransfertCompanyBranchDTO> dTOs = TransfertCompanyBranchFactory.transfertcompanybranchToTransfertCompanyBranchDTOs(result);
        ////code site destination///////
        Collection<SiteDTO> listeSiteDTOs = paramServiceClient.findAllSites(Boolean.TRUE, "B");

        dTOs.forEach(dto -> {
            if (listeSiteDTOs != null) {// en cas de fallback parametrage ne pas bloquer le retour de ws mais simplement ne pas retourner les designations des sites 
                Optional<SiteDTO> matchingSiteDTO = listeSiteDTOs
                        .stream()
                        .filter(elt -> elt.getCode().equals(dto.getCodeSite())).findFirst();

//                .orElseThrow(() -> new IllegalBusinessLogiqueException("parametrage.clinique.error", new Throwable(dto.getNumAffiche())));
                if (matchingSiteDTO.isPresent()) {
                    dto.setDesignationSite(matchingSiteDTO.get().getDesignationAr());
                }
            }
        });

        return dTOs;

    }

    @Transactional(readOnly = true)
    public TransfertCompanyBranchDTO findOne(String numBon) {
        log.debug("Request to get transfer company branch : {}", numBon);
        TransfertCompanyBranch transfertCompanyBranch = transfertcompanybranchRepository.findOne(numBon);
        Preconditions.checkBusinessLogique(transfertCompanyBranch != null, "transfertCompanyBranch.NotFound");

//        transfertCompanyBranch.getDetailTransfertCompanyBranchCollection().forEach(detailTr -> {
//            codeUnites.add(detailTr.getCodeUnite());
//        });
        Set<Integer> codeUnites = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection().stream().map(e -> e.getCodeUnite()).collect(Collectors.toSet());
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        TransfertCompanyBranchDTO transferDTO = TransfertCompanyBranchFactory.transfertCompanyBranchToTransfertCompanyBranchDTO(transfertCompanyBranch, Boolean.TRUE);
        FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(transferDTO.getCodeFournisseur());
        Preconditions.checkBusinessLogique(fournisseur != null, "missing-supplier");
        transferDTO.setDesignationFournisseur(fournisseur.getDesignation());
        if (transfertCompanyBranch.getCodeSite() != null) {
            SiteDTO siteDTO = paramServiceClient.findSiteByCode(transfertCompanyBranch.getCodeSite());

            Preconditions.checkBusinessLogique(siteDTO != null, "parametrage.clinique.error");
            transferDTO.setDesignationSite(siteDTO.getDesignationAr());
        }
        transferDTO.getDetailTransfertCompanyBranchDTOs().forEach(detailTr -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(detailTr.getCodeUnite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            detailTr.setDesignationUnite(unite.getDesignation());

        });
        return transferDTO;
    }

    @Transactional(readOnly = true)
    public TransfertCompanyBranch findOneTransfert(String numBon) {
        log.debug("Request to get transfer company branch : {}", numBon);
        TransfertCompanyBranch transfertCompanyBranch = transfertcompanybranchRepository.findOne(numBon);
        Preconditions.checkBusinessLogique(transfertCompanyBranch != null, "transfertCompanyBranch.NotFound");

        return transfertCompanyBranch;
    }

    public Set<TransfertCompanyBranch> findTransfertCompanyBranchByNumBonIn(Set<String> numBons) {
        return transfertcompanybranchRepository.findByNumBonIn(numBons);

    }

    public abstract TransfertCompanyBranchDTO rereplicateTransfertWhenErrorReplication(String numbon);

    public abstract TransfertCompanyBranchDTO save(TransfertCompanyBranchDTO transfertcompanybranchDTO);

    public abstract TransfertCompanyBranchDTO ajoutTransfertBranchToCompanyPourRetour(TransfertCompanyBranchDTO transfertcompanybranchDTO);
//    log.debug("Request to save TransfertCompanyBranch: {}",transfertcompanybranchDTO);
//    TransfertCompanyBranch transfertcompanybranch = TransfertCompanyBranchFactory.transfertcompanybranchDTOToTransfertCompanyBranch(transfertcompanybranchDTO);
//    transfertcompanybranch = transfertcompanybranchRepository.save(transfertcompanybranch);
//    TransfertCompanyBranchDTO resultDTO = TransfertCompanyBranchFactory.transfertcompanybranchToTransfertCompanyBranchDTO(transfertcompanybranch);
//    return resultDTO;
//  }

    //    /*must be read only so quantity of DetailReception wont be changed in database(working on entities))*//read only is not working if we 
//    @Transactional(readOnly = true)
    public List<DetailTransfertCompanyBranchDTO> returnListeArticlesRetournesTransfertToCompanyTotalmenet(Collection<DetailTransfertCompanyBranch> listeDetailsTransfert) {

        log.debug("listeDetailsTransfert est {}", listeDetailsTransfert);
        List<DetailTransfertCompanyBranchDTO> listeDetailTransfertRetournesTotalement = new ArrayList();
        List<DetailTransfertCompanyBranchDTO> listeDetailTransfertCompanyBranchDTOs = new ArrayList();
        listeDetailTransfertRetournesTotalement
                .stream()
                .forEach(elt -> {
                    DetailTransfertCompanyBranchDTO dto = new DetailTransfertCompanyBranchDTO();
                    dto.setQuantiteRestante(elt.getQuantiteRestante());
                    dto.setCodeArticle(elt.getCodeArticle());
                    dto.setAncienPrixAchat(elt.getAncienPrixAchat());
                    listeDetailTransfertCompanyBranchDTOs.add(dto);
                });
        listeDetailTransfertCompanyBranchDTOs.stream().collect(Collectors.groupingBy(DetailTransfertCompanyBranchDTO::getCodeArticle,
                Collectors.reducing(new DetailTransfertCompanyBranchDTO(BigDecimal.ZERO), (a, b) -> {
                    b.setQuantiteRestante(b.getQuantiteRestante().add(a.getQuantiteRestante()));

                    return b;
                }))).forEach((k, v) -> {
            if (BigDecimal.ZERO.compareTo(v.getQuantiteRestante()) == 0) {
                listeDetailTransfertRetournesTotalement.add(v);
            }
        });
        return listeDetailTransfertRetournesTotalement;
    }

    @Transactional
    protected Collection<DetailTransfertCompanyBranch> processTransferOnReturnTransfert(TransfertCompanyBranch transfertCompanyBranch) {
        log.debug("transfertCompanyBranch relatif {} ", transfertCompanyBranch.getNumBonTransfertRelatif());
        Collection<DetailTransfertCompanyBranch> listeDetailTransfertCompanyBranchs = transfertcompanybranchRepository.findOne(transfertCompanyBranch.getNumBonTransfertRelatif())
                .getDetailTransfertCompanyBranchCollection();

        listeDetailTransfertCompanyBranchs.stream().forEach(detailTransfert -> {
            log.debug("lotInter du detail TRansfert {} ,code article{} et le priuni {} ", detailTransfert.getLotInter(), detailTransfert.getCodeArticle(), detailTransfert.getPrixUnitaire());

            DetailTransfertCompanyBranch matchingDetailRetour = transfertCompanyBranch.getDetailTransfertCompanyBranchCollection()
                    .stream()
                    .filter(item -> item.getLotInter().equals(detailTransfert.getLotInter())
                    && item.getCodeArticle().equals(detailTransfert.getCodeArticle())
                    && item.getDatePeremption().equals(detailTransfert.getDatePeremption())
                    && detailTransfert.getPrixUnitaire()
                            .setScale(7).equals(item.getPrixUnitaire().setScale(7))) //TODO add matching by date per
                    .findFirst()
                    .orElse(null);
            log.debug("matchingDetailRetour {}", matchingDetailRetour);
            if (matchingDetailRetour != null) {
                log.debug(" lotInter duRT trouvé {}, code article duRT trouvé{} , priuni RT trouvé{}   ", matchingDetailRetour.getLotInter(), matchingDetailRetour.getCodeArticle(), matchingDetailRetour.getPrixUnitaire());
                detailTransfert.setQuantiteRestante(detailTransfert.getQuantiteRestante().subtract(matchingDetailRetour.getQuantite()));
            }

        });
        return listeDetailTransfertCompanyBranchs;
    }

    protected Boolean isItsComapny(Integer codeBranch) {
        log.debug("resolve isItsComapny : {}", codeBranch);
        Integer actualCodeSite;
        if (env.acceptsProfiles("testCentral")) {
            actualCodeSite = actualCodeSiteTest;
            return codeCompanyForBranchConfig.equals(actualCodeSiteTest);
        } else {
            List<CliniqueDto> cliniqueDtos = paramServiceClient.findClinique();
            Preconditions.checkBusinessLogique(cliniqueDtos != null, "parametrage.clinique.error");
            actualCodeSite = cliniqueDtos.get(0).getCodeSite();
            SiteDTO codeSiteBranch = paramServiceClient.findSiteByCode(codeBranch);
            Preconditions.checkBusinessLogique(codeSiteBranch != null, "parametrage.clinique.error");
            return codeSiteBranch.getCodeCompany().equals(actualCodeSite);
        }
    }

    protected Integer resolveCodeSite() {
        log.debug("resolve codeSite from table clinique : {}");
        Integer actualCodeSite;
        if (env.acceptsProfiles("testCentral")) {
            actualCodeSite = actualCodeSiteTest;

        } else {
            List<CliniqueDto> cliniqueDtos = paramServiceClient.findClinique();
            Preconditions.checkBusinessLogique(cliniqueDtos != null, "parametrage.clinique.error");
            actualCodeSite = cliniqueDtos.get(0).getCodeSite();
        }
        return actualCodeSite;
    }

    protected Integer isMatchingSite() {
        log.debug("resolve codeSite from table clinique : {}");
        Integer actualCodeSite;
        if (env.acceptsProfiles("testCentral")) {
            actualCodeSite = actualCodeSiteTest;

        } else {
            List<CliniqueDto> cliniqueDtos = paramServiceClient.findClinique();
            Preconditions.checkBusinessLogique(cliniqueDtos != null, "parametrage.clinique.error");
            actualCodeSite = cliniqueDtos.get(0).getCodeSite();
        }
        return actualCodeSite;
    }

//    protected Boolean isMatchingSite() {
//        log.debug("resolve codeSite from table clinique : {}");
//    Integer actualCodeSite=    resolveCodeSite();
//      
//    if(actualCodeSite.equals(transfertCompanyBranchDTO.getCodeSite())){
//        return actualCodeSite;
//    }
}
