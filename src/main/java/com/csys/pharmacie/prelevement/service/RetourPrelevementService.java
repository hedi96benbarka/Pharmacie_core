package com.csys.pharmacie.prelevement.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.prelevement.domain.DetailMvtStoPR;
import com.csys.pharmacie.prelevement.domain.DetailRetourPrelevement;
import com.csys.pharmacie.prelevement.domain.MvtStoPR;
import com.csys.pharmacie.prelevement.domain.QRetourPrelevement;
import com.csys.pharmacie.prelevement.domain.RetourPrelevement;
import com.csys.pharmacie.prelevement.domain.TraceDetailRetourPr;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.prelevement.dto.DetailRetourPrelevementDTO;
import com.csys.pharmacie.prelevement.dto.RetourPrelevementDTO;
import com.csys.pharmacie.prelevement.factory.DetailRetourPrelevementFactory;
import com.csys.pharmacie.prelevement.factory.RetourPrelevementFactory;
import com.csys.pharmacie.prelevement.repository.DetailRetourPrelevementRepository;
import com.csys.pharmacie.prelevement.repository.RetourPrelevementRepository;
import com.csys.pharmacie.prelevement.repository.TraceDetailRetourPrRepository;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.repository.DepstoRepository;
import com.csys.util.Preconditions;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing RetourPrelevement.
 */
@Service
@Transactional
public class RetourPrelevementService {

    private final Logger log = LoggerFactory.getLogger(RetourPrelevementService.class);
    private static String LANGUAGE_SEC;
    private static Integer defaultInterval;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    @Value("${default-value-interval-retour-prelevement}")
    public void setInterval(Integer interval) {
        defaultInterval = interval;
    }

    public Integer getInterval() {
        return defaultInterval;
    }
    private final RetourPrelevementRepository retourprelevementRepository;
    private final DetailRetourPrelevementRepository detailRetourPrelevementRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final ParamService paramService;
    private final FacturePRService facturePRService;
    private final ParamServiceClient parametrageService;
    private final DetailRetourPrelevementService detailRetourPrelevementService;
    private final TraceDetailRetourPrRepository traceDetailRetourPrRepository;
    private final DepstoRepository depstoRepository;
    private final InventaireService inventaireService;

    public RetourPrelevementService(RetourPrelevementRepository retourprelevementRepository, DetailRetourPrelevementRepository detailRetourPrelevementRepository, ParamAchatServiceClient paramAchatServiceClient, ParamService paramService, FacturePRService facturePRService, ParamServiceClient parametrageService, DetailRetourPrelevementService detailRetourPrelevementService, TraceDetailRetourPrRepository traceDetailRetourPrRepository, DepstoRepository depstoRepository, InventaireService inventaireService) {
        this.retourprelevementRepository = retourprelevementRepository;
        this.detailRetourPrelevementRepository = detailRetourPrelevementRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.paramService = paramService;
        this.facturePRService = facturePRService;
        this.parametrageService = parametrageService;
        this.detailRetourPrelevementService = detailRetourPrelevementService;
        this.traceDetailRetourPrRepository = traceDetailRetourPrRepository;
        this.depstoRepository = depstoRepository;
        this.inventaireService = inventaireService;
    }

    /**
     * Save a retourPrelevementDTO.
     *
     * @param retourPrelevementDTO
     * @return the persisted entity
     */
    public RetourPrelevementDTO save(RetourPrelevementDTO retourPrelevementDTO) {
        log.debug("Request to save RetourPrelevement: {}", retourPrelevementDTO);

///// verifier existence depot et inventaire sur ce depot     
        DepotDTO depotDTO = paramAchatServiceClient.findDepotByCode(retourPrelevementDTO.getCoddepDesti());
        Preconditions.checkBusinessLogique(depotDTO != null, "depot.findOne.missing-depot");

        List<Integer> codArticles = retourPrelevementDTO.getDetailRetourPrelevementDTO().stream().map(item -> item.getCodart()).collect(toList());
        List<ArticleDTO> articles = paramAchatServiceClient.articleFindbyListCode(codArticles);
        log.debug("articleFindbyListCode  {}", articles);
        List<ArticleDTO> articleUnderInventory = inventaireService.checkOpenInventoryByListArticleAndCodeDep(codArticles, retourPrelevementDTO.getCoddepDesti());
        Preconditions.checkBusinessLogique(articleUnderInventory.isEmpty(), "article-under-inventory", articleUnderInventory.stream().map(ArticleDTO::getCodeSaisi).collect(toList()).toString());

        RetourPrelevement retourPrelevement = RetourPrelevementFactory.retourPrelevementDTOToRetourPrelevement(retourPrelevementDTO);

        DepartementDTO departementDTO = paramAchatServiceClient.findDepartment(retourPrelevementDTO.getCoddepartSrc());
        retourPrelevement.setCodeCostCenter(departementDTO.getCodeCostCenter());
        List<MvtStoPR> listeMvtstoPR = facturePRService.findListDetails(retourPrelevementDTO.getCategDepot(), retourPrelevement.getDateDebut(), retourPrelevement.getDateFin(), retourPrelevementDTO.getCoddepDesti(), retourPrelevementDTO.getCoddepartSrc());
        log.debug("listeMvtstoPR est {}", listeMvtstoPR);
        Preconditions.checkBusinessLogique(!listeMvtstoPR.isEmpty(), "no-prelvements-found");

        List<Depsto> depstos = new ArrayList();
        BigDecimal mntFac = BigDecimal.ZERO;
////////////////////////////////////////////////////////////////////////////////////boucle detail retour //////////////////////////////////////////////////////////////
        for (DetailRetourPrelevement detailRetourPrelevement : retourPrelevement.getLiseDetailRetourPrelevement()) {
            BigDecimal priuniDetailRetour = BigDecimal.ZERO;
            Integer quantite = 0;
            List<TraceDetailRetourPr> listeTrace = new ArrayList();
            BigDecimal qteARetourner = detailRetourPrelevement.getQuantite();

            //verification que la quantite totale prelevee durant cette periode est suffisante 
            Integer totalQuantitePreleveNette = listeMvtstoPR.stream()
                    .filter(item -> item.getCodart().equals(detailRetourPrelevement.getCodart()) && item.getUnite().equals(detailRetourPrelevement.getUnite()))
                    .map(elt -> elt.getQtecom().intValue())
                    .collect(Collectors.summingInt(Integer::new));
            log.debug("************totalQuantitePreleveNette**************{}", totalQuantitePreleveNette);
            log.debug("*************qteARetourner.intValue()**************{}", qteARetourner.intValue());
            checkBusinessLogique(totalQuantitePreleveNette >= qteARetourner.intValue(), "insuffisant-qte", retourPrelevementDTO.getNumbon(), totalQuantitePreleveNette.toString());

            List<MvtStoPR> firstCandidatsMvtstoPRs = listeMvtstoPR.stream()
                    .filter(item -> item.getCodart().equals(detailRetourPrelevement.getCodart()) && item.getDatPer().equals(detailRetourPrelevement.getDatPer())
                    && item.getLotinter().equals(detailRetourPrelevement.getLotinter()) && item.getUnite().equals(detailRetourPrelevement.getUnite()) && item.getQtecom().compareTo(BigDecimal.ZERO) > 0)
                    .sorted((a, b) -> a.getFacturePR().getDatbon().compareTo(b.getFacturePR().getDatbon()))
                    .collect(Collectors.toList());

            log.debug("*************firstCandidatsMvtstoPRs**************{}", firstCandidatsMvtstoPRs);

            for (MvtStoPR firstCandidMvtstoPR : firstCandidatsMvtstoPRs) {

                BigDecimal qteToRmv = qteARetourner.min(firstCandidMvtstoPR.getQtecom());
                qteARetourner = qteARetourner.subtract(qteToRmv);
                firstCandidMvtstoPR.setQtecom(firstCandidMvtstoPR.getQtecom().subtract(qteToRmv));
                if (retourPrelevement.getCategDepot().equals(CategorieDepotEnum.EC)) {
                    quantite = quantite + qteToRmv.intValue();
                    priuniDetailRetour = priuniDetailRetour.add(firstCandidMvtstoPR.getPriuni().multiply(qteToRmv));
                    log.debug("priuniDetailRetour first mvtstoPr EC est {}", priuniDetailRetour);
                }
                List<DetailMvtStoPR> listeDetailMvtstoPrOrdonne = firstCandidMvtstoPR.getDetailMvtStoPRList()
                        .stream()
                        .sorted((a, b) -> a.getPriuni().compareTo(b.getPriuni()))
                        .collect(Collectors.toList());
                Iterator<DetailMvtStoPR> it = listeDetailMvtstoPrOrdonne.iterator();

                while (it.hasNext() && qteToRmv.compareTo(BigDecimal.ZERO) > 0) {
                    DetailMvtStoPR actualDetailMvtstoPR = it.next();
                    if (actualDetailMvtstoPR.getQtecom().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }

                    log.debug("********new detail11*********");
                    BigDecimal qteDetailToRmv = qteToRmv.min(actualDetailMvtstoPR.getQtecom());
                    log.debug("qteDetailToRmv  est {}", qteDetailToRmv);
                    actualDetailMvtstoPR.setQtecom(actualDetailMvtstoPR.getQtecom().subtract(qteDetailToRmv));
                    qteToRmv = qteToRmv.subtract(qteDetailToRmv);

                    if (retourPrelevement.getCategDepot().equals(CategorieDepotEnum.PH) || retourPrelevement.getCategDepot().equals(CategorieDepotEnum.UU)) {
                        quantite = quantite + qteDetailToRmv.intValue();
                        priuniDetailRetour = priuniDetailRetour.add(actualDetailMvtstoPR.getPriuni().multiply(qteDetailToRmv));

                        log.debug("********fin detail*********");
                    }

                    ////////creation de trace et depsto et sommation du priuni et qte 
                    TraceDetailRetourPr traceDetailRetour = new TraceDetailRetourPr(firstCandidMvtstoPR.getCode(), detailRetourPrelevement, actualDetailMvtstoPR, qteDetailToRmv);

                    listeTrace.add(traceDetailRetour);

                    Depsto depst = new Depsto(retourPrelevement.getCoddepDesti(), detailRetourPrelevement.getCodart(), detailRetourPrelevement.getDatPer(), retourPrelevement.getNumbon(),
                            detailRetourPrelevement.getLotinter(), actualDetailMvtstoPR.getUnite(), retourPrelevement.getCategDepot(), actualDetailMvtstoPR.getCodeTva(), actualDetailMvtstoPR.getTauxTva());

                    //!!!!!!!!!! depst pu
                    depst.setPu(actualDetailMvtstoPR.getPriuni());
                    depst.setNumBon(retourPrelevement.getNumbon());
                    depst.setQte(traceDetailRetour.getQuantite());
                    depst.setMemo(actualDetailMvtstoPR.getDepsto().getMemo());
                    depst.setNumBonOrigin(actualDetailMvtstoPR.getNumBonOrigin());
                    depstos.add(depst);
                }

                if (qteARetourner.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            List<MvtStoPR> secondCandidatsMvtstoPRs = listeMvtstoPR.stream()
                    .filter(item -> item.getCodart().equals(detailRetourPrelevement.getCodart()) && item.getUnite().equals(detailRetourPrelevement.getUnite())
                    && item.getQuantite().compareTo(BigDecimal.ZERO) > 0)
                    .collect(toList());

            List<MvtStoPR> secondCandidatsMvtstoPRsOrdonnes = secondCandidatsMvtstoPRs.stream()
                    .sorted((a, b) -> a.getFacturePR().getDatbon().compareTo(b.getFacturePR().getDatbon()))
                    .collect(toList());
            log.debug("*************secondCandidatsMvtstoPRs**************{}", secondCandidatsMvtstoPRs);
            for (MvtStoPR secondCandidMvtstoPR : secondCandidatsMvtstoPRs) {

                if (qteARetourner.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
                BigDecimal qteToRmv = qteARetourner.min(secondCandidMvtstoPR.getQtecom());
                log.debug("qteToRmv from second condidat {}", qteToRmv);
                qteARetourner = qteARetourner.subtract(qteToRmv);
                secondCandidMvtstoPR.setQtecom(secondCandidMvtstoPR.getQtecom().subtract(qteToRmv));
                log.debug("quantite second mvtstoPR apres soustraction est {}", secondCandidMvtstoPR.getQtecom());
                if (retourPrelevement.getCategDepot().equals(CategorieDepotEnum.EC)) {
                    quantite = quantite + qteToRmv.intValue();
                    priuniDetailRetour = priuniDetailRetour.add(secondCandidMvtstoPR.getPriuni().multiply(qteToRmv));
                }

                List<DetailMvtStoPR> listeDetailMvtstoPrOrdonneSecond = secondCandidMvtstoPR.getDetailMvtStoPRList()
                        .stream()
                        .sorted((a, b) -> a.getPriuni().compareTo(b.getPriuni()))
                        .collect(Collectors.toList());
                Iterator<DetailMvtStoPR> it = listeDetailMvtstoPrOrdonneSecond.iterator();

                while (it.hasNext() && qteToRmv.compareTo(BigDecimal.ZERO) > 0) {
                    DetailMvtStoPR actualDetailMvtstoPR = it.next();
                    if (actualDetailMvtstoPR.getQtecom().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }

                    BigDecimal qteDetailToRmv = qteToRmv.min(actualDetailMvtstoPR.getQtecom());
                    log.debug("qteDetailToRmv  est {}", qteDetailToRmv);
                    actualDetailMvtstoPR.setQtecom(actualDetailMvtstoPR.getQtecom().subtract(qteDetailToRmv));
                    qteToRmv = qteToRmv.subtract(qteDetailToRmv);

                    if (retourPrelevement.getCategDepot().equals(CategorieDepotEnum.PH) || retourPrelevement.getCategDepot().equals(CategorieDepotEnum.UU)) {
                        quantite = quantite + qteDetailToRmv.intValue();
                        priuniDetailRetour = priuniDetailRetour.add(actualDetailMvtstoPR.getPriuni().multiply(qteDetailToRmv));
                    }

                    //creation de la trace
                    TraceDetailRetourPr traceDetailRetour = new TraceDetailRetourPr(secondCandidMvtstoPR.getCode(), detailRetourPrelevement, actualDetailMvtstoPR, qteDetailToRmv);
                    listeTrace.add(traceDetailRetour);

//                    if (retourPrelevement.getCategDepot().equals(CategorieDepotEnum.EC)) {
//                    } else {
//                        priuniDetailRetour.add(actualDetailMvtstoPR.getPriuni());
//                    }
                    Depsto depst = new Depsto(retourPrelevement.getCoddepDesti(), detailRetourPrelevement.getCodart(), detailRetourPrelevement.getDatPer(), retourPrelevement.getNumbon(),
                            detailRetourPrelevement.getLotinter(), actualDetailMvtstoPR.getUnite(), retourPrelevement.getCategDepot(), actualDetailMvtstoPR.getCodeTva(), actualDetailMvtstoPR.getTauxTva());
                    depst.setPu(actualDetailMvtstoPR.getPriuni());
                    depst.setNumBon(retourPrelevement.getNumbon());
                    depst.setQte(traceDetailRetour.getQuantite());
                    depst.setMemo(actualDetailMvtstoPR.getDepsto().getMemo());
                    depst.setNumBonOrigin(actualDetailMvtstoPR.getNumBonOrigin());
                    depstos.add(depst);

                }
                if (qteARetourner.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }

            }
            if (!firstCandidatsMvtstoPRs.isEmpty()) {
                detailRetourPrelevement.setCodeSaisi(firstCandidatsMvtstoPRs.get(0).getCodeSaisi());
                detailRetourPrelevement.setDesart(firstCandidatsMvtstoPRs.get(0).getDesart());
                detailRetourPrelevement.setDesArtSec(firstCandidatsMvtstoPRs.get(0).getDesart());

            } else {
                detailRetourPrelevement.setCodeSaisi(secondCandidatsMvtstoPRs.get(0).getCodeSaisi());
                detailRetourPrelevement.setDesart(secondCandidatsMvtstoPRs.get(0).getDesart());
                detailRetourPrelevement.setDesArtSec(secondCandidatsMvtstoPRs.get(0).getDesart());
            }
            log.debug("somme quantite est {}", quantite);
            log.debug("priuniDetailRetour est {}", priuniDetailRetour);
            detailRetourPrelevement.setPriuni(priuniDetailRetour.divide(new BigDecimal(quantite), 3, RoundingMode.CEILING));
//            detailRetourPrelevement.setPriuni(priuniDetailRetour.divide(new BigDecimal(quantite)));
            log.debug("priuniDetailRetour final est {}", detailRetourPrelevement.getPriuni());
            detailRetourPrelevement.setTraceDetailRetourPr(listeTrace);

            mntFac = mntFac.add(detailRetourPrelevement.getPriuni().multiply(detailRetourPrelevement.getQuantite()));

        }

        retourPrelevement.setTypbon(TypeBonEnum.RPR);
        retourPrelevement.setNumbon(paramService.getcompteur(retourPrelevement.getCategDepot(), TypeBonEnum.RPR));
        retourPrelevement.setMontantFac(mntFac);
        retourPrelevement = retourprelevementRepository.save(retourPrelevement);

        for (Depsto depst : depstos) {
            depst.setNumBon(retourPrelevement.getNumbon());
        }
        depstoRepository.save(depstos);
        paramService.updateCompteurPharmacie(retourPrelevement.getCategDepot(), TypeBonEnum.RPR);
        RetourPrelevementDTO resultDTO = RetourPrelevementFactory.retourPrelevementToRetourPrelevementDTO(retourPrelevement);
        return resultDTO;
    }

    /**
     * Update a retourprelevementDTO.
     *
     * @param retourprelevementDTO
     * @return the updated entity
     */
    public RetourPrelevementDTO update(RetourPrelevementDTO retourprelevementDTO) {
        log.debug("Request to update RetourPrelevement: {}", retourprelevementDTO);
        RetourPrelevement inBase = retourprelevementRepository.findOne(retourprelevementDTO.getNumbon());
        Preconditions.checkBusinessLogique(inBase != null, "retourprelevement.NotFound");
        RetourPrelevement retourprelevement = RetourPrelevementFactory.retourPrelevementDTOToRetourPrelevementLazy(retourprelevementDTO);
        retourprelevement = retourprelevementRepository.save(retourprelevement);
        RetourPrelevementDTO resultDTO = RetourPrelevementFactory.retourPrelevementToRetourPrelevementDTO(retourprelevement);
        return resultDTO;
    }

    /**
     * Get one retourprelevementDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(readOnly = true)
    public RetourPrelevementDTO findOne(String id) {
        log.debug("Request to get RetourPrelevement: {}", id);
        RetourPrelevement retourprelevement = retourprelevementRepository.findOne(id);
        RetourPrelevementDTO dto = RetourPrelevementFactory.retourPrelevementToRetourPrelevementDTO(retourprelevement);
        DepartementDTO departement = paramAchatServiceClient.findDepartment(dto.getCoddepartSrc());
        dto.setDesignationDepartSrc(departement.getDesignation());
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(dto.getCoddepDesti());
        dto.setDesignationDepotDest(depot.getDesignation());

        List<Integer> codeUnites = dto.getDetailRetourPrelevementDTO().stream().map(item -> item.getUnite()).collect(Collectors.toList());
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);

        dto.getDetailRetourPrelevementDTO().forEach(item -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getUnite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            item.setDesignationunite(unite.getDesignation());

        });

        return dto;
    }

    /**
     * Get one retourPrelevement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public RetourPrelevement findRetourPrelevement(String id) {
        log.debug("Request to get RetourPrelevement: {}", id);
        RetourPrelevement retourprelevement = retourprelevementRepository.findOne(id);
        return retourprelevement;
    }

    /**
     * Get all the retourprelevements.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<RetourPrelevementDTO> findAll(CategorieDepotEnum categ, LocalDateTime fromDate, LocalDateTime toDate) {
        log.debug("Request to get All RetourPrelevements");

        QRetourPrelevement _RetourPrelevement = QRetourPrelevement.retourPrelevement;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(categ, () -> _RetourPrelevement.categDepot.eq(categ))
                .optionalAnd(fromDate, () -> _RetourPrelevement.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _RetourPrelevement.datbon.loe(toDate));
//                .optionalAnd(codeDepSrc, () -> _RetourPrelevement.coddepartSrc.eq(codeDepSrc));
        List<RetourPrelevement> listRetourPrelevements = (List<RetourPrelevement>) retourprelevementRepository.findAll(builder);
        Set<Integer> codeDepots = new HashSet();
        listRetourPrelevements.stream().forEach(item -> {
            codeDepots.add(item.getCoddepDesti());
            codeDepots.add(item.getCoddepartSrc());
        });
        List<DepartementDTO> listDepartment = paramAchatServiceClient.findListDepartments(codeDepots);
        List<RetourPrelevementDTO> listeRetourDTO = listRetourPrelevements.stream().map(item -> {
            RetourPrelevementDTO dto = RetourPrelevementFactory.retourPrelevementToRetourPrelevementDTOLazy(item);
            listDepartment.stream().filter(x -> x.getCode().equals(item.getCoddepDesti()) || x.getCode().equals(item.getCoddepartSrc())).forEach(departement -> {
                if (departement.getCode().equals(item.getCoddepartSrc())) {
                    dto.setDesignationDepartSrc(departement.getDesignation());
                    dto.setCodeSaisiDepotSrc(departement.getCodeSaisi());
                }
                if (departement.getCode().equals(item.getCoddepDesti())) {
                    dto.setDesignationDepotDest(departement.getDesignation());
                    dto.setCodeSaisiDepotDest(departement.getCodeSaisi());
                }
            });
            return dto;
        }).collect(toList());

        return listeRetourDTO;
    }

    public byte[] edition(String numBon, String type)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        ReportClientDocument reportClientDoc = new ReportClientDocument();

        RetourPrelevementDTO retourPrelevementDTO = findOne(numBon);

        String local = "_" + language;
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/Retour_Bon_Prelevment" + local + ".rpt", 0);
        } else {
            reportClientDoc.open("Reports/Retour_Bon_Prelevment_excel" + local + ".rpt", 0);
        }

        reportClientDoc.getDatabaseController().setDataSource(retourPrelevementDTO.getDetailRetourPrelevementDTO(), DetailRetourPrelevementDTO.class,
                "Detaille", "Detaille");
        reportClientDoc
                .getDatabaseController().setDataSource(java.util.Arrays.asList(retourPrelevementDTO), RetourPrelevementDTO.class,
                        "Entete", "Entete");

        if (type.equalsIgnoreCase("P")) {
            List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
            cliniqueDto.get(0).setLogoClinique();
            log.debug("logo *************************** {}", cliniqueDto.get(0).getLogoClinique());
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);

        if (type.equalsIgnoreCase("P")) {

            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return Helper.read(byteArrayInputStream);
        } else {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
            reportClientDoc.close();
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return Helper.read(byteArrayInputStream);
        }

    }

    /**
     * Delete retourPrelevement by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete RetourPrelevement: {}", id);
        retourprelevementRepository.delete(id);
    }

    public List<DetailRetourPrelevementDTO> findDetailsRetourPrelevementById(String id) {

        List<DetailRetourPrelevement> details = detailRetourPrelevementService.findByNumBon(id);

        List<Integer> codeUnites = details.stream().map(item -> item.getUnite()).collect(Collectors.toList());
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        return details.stream().map(item -> {
            DetailRetourPrelevementDTO dto = DetailRetourPrelevementFactory.detailretourprelevementToDetailRetourPrelevementDTO(item);
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getUnite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            dto.setDesignationunite(unite.getDesignation());
            return dto;
        }).collect(toList());
    }

    public List<MvtStoPR> test(List<MvtStoPR> listeMvtstopr) {

        List<MvtStoPR> secondCandidatsMvtstoPRsOrdonnes = listeMvtstopr.stream()
                .sorted((a, b) -> a.getFacturePR().getDatbon().compareTo(b.getFacturePR().getDatbon()))
                .collect(Collectors.toList());

        return secondCandidatsMvtstoPRsOrdonnes;
    }

    public List<DetailMvtStoPR> testPrice(List<DetailMvtStoPR> listeDetail) {

        List<DetailMvtStoPR> listeDetailMvtstoPrOrdonneSecond = listeDetail
                .stream()
                .sorted((a, b) -> a.getPriuni().compareTo(b.getPriuni()))
                .collect(Collectors.toList());

        return listeDetailMvtstoPrOrdonneSecond;
    }

    public Integer getDefaultValueIntervalRetourPrelevement() {
        return defaultInterval;
    }

}
