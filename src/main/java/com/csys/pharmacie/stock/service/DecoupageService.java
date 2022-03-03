package com.csys.pharmacie.stock.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import static com.crystaldecisions12.reports.common.RootCauseID.id;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.ArticleUniteDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.domain.Decoupage;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.domain.DepstoDetailDecoupage;
import com.csys.pharmacie.stock.domain.DetailDecoupage;
import com.csys.pharmacie.stock.domain.QDecoupage;
import com.csys.pharmacie.stock.dto.DecoupageDTO;
import com.csys.pharmacie.stock.dto.DecoupageEditionDTO;
import com.csys.pharmacie.stock.dto.DetailDecoupageDTO;
import com.csys.pharmacie.stock.dto.DetailDecoupageEditionDTO;
import com.csys.pharmacie.stock.factory.DecoupageFactory;
import com.csys.pharmacie.stock.factory.DetailDecoupageFactory;
import com.csys.pharmacie.stock.repository.DecoupageRepository;
import com.csys.util.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Decoupage.
 */
@Service
@Transactional
public class DecoupageService {

    private final Logger log = LoggerFactory.getLogger(DecoupageService.class);

    private final DecoupageRepository decoupageRepository;
    private final ParamService paramService;
    private final StockService stockService;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final ParamServiceClient parametrageService;

    public DecoupageService(DecoupageRepository decoupageRepository, ParamService paramService, StockService stockService, ParamAchatServiceClient paramAchatServiceClient, ParamServiceClient parametrageService) {
        this.decoupageRepository = decoupageRepository;
        this.paramService = paramService;
        this.stockService = stockService;
        this.paramAchatServiceClient = paramAchatServiceClient;

        this.parametrageService = parametrageService;
    }

    /**
     * Save a decoupageDTO.
     *
     * @param decoupageDTO
     * @return the persisted entity
     */
    public DecoupageDTO save(DecoupageDTO decoupageDTO) {
        log.debug("Request to save Decoupage: {}", decoupageDTO);

        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(decoupageDTO.getCoddep());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + depotd.getDesignation() + "] introuvable");
        Preconditions.checkBusinessLogique(!depotd.getDepotFrs(), "Dépot [" + depotd.getDesignation() + "] est un dépot fournisseur");
        Decoupage decoupage = DecoupageFactory.decoupageDTOToDecoupage(decoupageDTO);
        String numbon = paramService.getcompteur(decoupageDTO.getCategDepot(), TypeBonEnum.DC);
        decoupage.setNumbon(numbon);
        decoupage.setAuto(false);

        List<Integer> articleIDs = decoupageDTO.getDetails().stream().map(DetailDecoupageDTO::getArticleID).collect(toList());
        List<ArticlePHDTO> articles = paramAchatServiceClient.articlePHFindbyListCode(articleIDs);

        List<Depsto> stock = stockService.findByCodartInAndCoddepAndQteGreaterThan(articleIDs, decoupageDTO.getCoddep(), BigDecimal.ZERO);
        List<DetailDecoupage> detailsDecoupage = new ArrayList();
        for (DetailDecoupageDTO detailDecDTO : decoupageDTO.getDetails()) {
            DetailDecoupage detailDecoup = DetailDecoupageFactory.detaildecoupageDTOToDetailDecoupage(detailDecDTO);
            detailDecoup.setCategDepot(decoupageDTO.getCategDepot());

            detailDecoup.setDecoupage(decoupage);
            detailDecoup.setCodeDecoupage(numbon);
            detailsDecoupage.add(detailDecoup);

            ArticlePHDTO matchedArticle = articles.stream().filter(art -> art.getCode().equals(detailDecoup.getCodart())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-article"));

            ArticleUniteDTO originUnity = matchedArticle.getArticleUnites().stream().filter(unity -> unity.getCodeUnite().equals(detailDecoup.getUniteOrigine())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unity" + detailDecoup.getUniteOrigine()));
            ArticleUniteDTO finalUnity = matchedArticle.getArticleUnites().stream().filter(unity -> unity.getCodeUnite().equals(detailDecoup.getUniteFinal())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unity" + detailDecoup.getUniteFinal()));
            Preconditions.checkBusinessLogique(originUnity.getNbPiece().compareTo(finalUnity.getNbPiece()) < 0, "decoupage.add.origin-lt-final");
            BigDecimal unitMultiplicand = finalUnity.getNbPiece().divide(originUnity.getNbPiece());
            detailDecoup.setQuantiteObtenue(detailDecoup.getQuantite().multiply(unitMultiplicand));

            List<DepstoDetailDecoupage> depstoDetailDecoupList = new ArrayList();
            detailDecoup.setDepstoDetailDecoupageList(depstoDetailDecoupList);
            log.debug("*** Begin treating detail {} ****", detailDecoup);

            BigDecimal qteToRmove = detailDecoup.getQuantite();

            Integer availableQteInStock = stock.stream()
                    .filter(item -> item.getCodart().equals(detailDecoup.getCodart()) && item.getUnite().equals(detailDecoup.getUniteOrigine()))
                    .map(filtredItem -> filtredItem.getQte().intValue())
                    .collect(Collectors.summingInt(Integer::new));
            log.debug("Available quantite is {}", availableQteInStock);

            Preconditions.checkBusinessLogique(availableQteInStock >= qteToRmove.intValue(), "decoupage.add.insuffisant-qte", detailDecoup.getCodart().toString());

            List<Depsto> firstCandidatsDepstos = stock.stream()
                    .filter(item -> item.getCodart().equals(detailDecoup.getCodart()) && item.getDatPer().equals(detailDecoup.getDatePeremption()) && item.getLotInter().equals(detailDecoup.getLotInter()) && item.getUnite().equals(detailDecoup.getUniteOrigine()))
                    .collect(toList());
            log.debug("returning from the same lot and date per. \n The candidates are {}", firstCandidatsDepstos);
            for (Depsto firstCandidDeps : firstCandidatsDepstos) {
                BigDecimal qteToRmv = qteToRmove.min(firstCandidDeps.getQte());
                DepstoDetailDecoupage depstoDetailDecoup = new DepstoDetailDecoupage(qteToRmv, firstCandidDeps.getQte(), firstCandidDeps);
                depstoDetailDecoup.setDetailDecoupage(detailDecoup);
                log.debug("code mvtsto************** {}", detailDecoup.getCode());
                depstoDetailDecoupList.add(depstoDetailDecoup);
                firstCandidDeps.setQte(firstCandidDeps.getQte().subtract(qteToRmv));
                qteToRmove = qteToRmove.subtract(qteToRmv);
                Depsto newDepsto = new Depsto(firstCandidDeps);
                newDepsto.setQte(qteToRmv.multiply(unitMultiplicand));
                newDepsto.setUnite(detailDecoup.getUniteFinal());
                newDepsto.setPu(firstCandidDeps.getPu().divide(unitMultiplicand, 3, RoundingMode.CEILING));
                newDepsto.setNumBon(detailDecoup.getCodeDecoupage());
                newDepsto.setMemo(firstCandidDeps.getMemo());
                newDepsto.setNumBonOrigin(firstCandidDeps.getNumBonOrigin());

                //set tva
                newDepsto.setCodeTva(firstCandidDeps.getCodeTva());
                newDepsto.setTauxTva(firstCandidDeps.getTauxTva());

                stockService.saveDepsto(newDepsto);
                log.debug(" removing {} from depsto {} ", qteToRmv, firstCandidDeps);
                if (qteToRmove.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }
            if (qteToRmove.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            List<Depsto> secondCandidatsDepstos = stock.stream()
                    .filter(item -> item.getCodart().equals(detailDecoup.getCodart()) && item.getUnite().equals(detailDecoup.getUniteOrigine()) && item.getQte().compareTo(BigDecimal.ZERO) > 0)
                    .collect(toList());
            log.debug("returning from any depsto. \n The candidates are {}", secondCandidatsDepstos);
            for (Depsto secondCandidDeps : secondCandidatsDepstos) {
                BigDecimal qteToRmv = qteToRmove.min(secondCandidDeps.getQte());
                DepstoDetailDecoupage depstoDetailDecoup = new DepstoDetailDecoupage(qteToRmv, secondCandidDeps.getQte(), secondCandidDeps);
                depstoDetailDecoup.setDetailDecoupage(detailDecoup);
                depstoDetailDecoupList.add(depstoDetailDecoup);
                secondCandidDeps.setQte(secondCandidDeps.getQte().subtract(qteToRmv));
                Depsto newDepsto = new Depsto(secondCandidDeps);
                newDepsto.setQte(qteToRmv.multiply(unitMultiplicand));
                newDepsto.setUnite(detailDecoup.getUniteFinal());
                newDepsto.setPu(secondCandidDeps.getPu().divide(unitMultiplicand, 2, RoundingMode.CEILING));
                newDepsto.setMemo(secondCandidDeps.getMemo());
                newDepsto.setNumBon(detailDecoup.getCodeDecoupage());
                newDepsto.setNumBonOrigin(secondCandidDeps.getNumBonOrigin());
                stockService.saveDepsto(newDepsto);
                log.debug(" removing {} from depsto {} ", qteToRmv, secondCandidDeps);
                qteToRmove = qteToRmove.subtract(qteToRmv);
                if (qteToRmove.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }
            }

            log.debug("*** End of treating article {} ****", detailDecoup);
        }
        decoupage.setDetailDecoupageList(detailsDecoupage);

        decoupageRepository.save(decoupage);

        paramService.updateCompteurPharmacie(decoupageDTO.getCategDepot(), TypeBonEnum.DC);
        DecoupageDTO resultDTO = DecoupageFactory.decoupageToDecoupageDTO(decoupage);
        return resultDTO;
    }

//  /**
//   * Update a decoupageDTO.
//   *
//   * @param decoupageDTO
//   * @return the updated entity
//   */
//  public DecoupageDTO update(DecoupageDTO decoupageDTO) {
//    log.debug("Request to update Decoupage: {}",decoupageDTO);
//    Decoupage inBase= decoupageRepository.findOne(decoupageDTO.getNumbon());
//    Preconditions.checkArgument(inBase != null, "decoupage.NotFound");
//    Decoupage decoupage = DecoupageFactory.decoupageDTOToDecoupage(decoupageDTO);
//    decoupage = decoupageRepository.save(decoupage);
//    DecoupageDTO resultDTO = DecoupageFactory.decoupageToDecoupageDTO(decoupage);
//    return resultDTO;
//  }
    /**
     * Get one decoupageDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public DecoupageDTO findOne(String id) {
        log.debug("Request to get FactureBE: {}", id);
        Decoupage decoupage = decoupageRepository.findOne(id);
        Set<Integer> unitIds = new HashSet();
        decoupage.getDetailDecoupageList().forEach(item -> {
            unitIds.add(item.getUniteFinal());
            unitIds.add(item.getUniteOrigine());

        });

        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unitIds);
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(decoupage.getCoddep());
        DecoupageDTO dto = DecoupageFactory.decoupageToDecoupageDTO(decoupage);
        dto.setDesignationDepot(depot.getDesignation());
        List<DetailDecoupageDTO> detailsDecoupageDTO = decoupage.getDetailDecoupageList().stream().map(item -> {
            DetailDecoupageDTO detailDecoupageDTO = DetailDecoupageFactory.detaildecoupageToDetailDecoupageDTO(item);
            UniteDTO originUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUniteOrigine())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit"));
            UniteDTO finalUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUniteFinal())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit"));
            detailDecoupageDTO.setOriginUnitDesignation(originUnit.getDesignation());
            detailDecoupageDTO.setFinalUnitDesignation(finalUnit.getDesignation());
            return detailDecoupageDTO;
        }).collect(toList());
        dto.setDetails(detailsDecoupageDTO);
        dto.setCodeSaisiDepot(depot.getCodeSaisi());
        return dto;
    }

    /**
     * Get one decoupage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public Decoupage findDecoupage(String id) {
        log.debug("Request to get Decoupage: {}", id);
        Decoupage decoupage = decoupageRepository.findOne(id);
        return decoupage;
    }

    /**
     * Get all the decoupages.
     *
     * @param decoupage
     * @param fromDate
     * @param toDate
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<DecoupageDTO> findAll(Decoupage decoupage, LocalDateTime fromDate, LocalDateTime toDate) {
        log.debug("Request to get All FactureBEs");
        QDecoupage _Decoupage = QDecoupage.decoupage;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_Decoupage.typbon.eq(TypeBonEnum.DC))
                .optionalAnd(fromDate, () -> _Decoupage.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _Decoupage.datbon.loe(toDate))
                .optionalAnd(decoupage.getCategDepot(), () -> _Decoupage.categDepot.eq(decoupage.getCategDepot()))
                .optionalAnd(decoupage.getCoddep(), () -> _Decoupage.coddep.eq(decoupage.getCoddep()));

        List<Decoupage> listDecoupags = (List<Decoupage>) decoupageRepository.findAll(builder);
        Set<Integer> codeDepots = listDecoupags.stream().map(Decoupage::getCoddep).collect(toSet());

        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots);

        return listDecoupags.stream().map(item -> {
            DecoupageDTO decoupageDTO = DecoupageFactory.decoupageToDecoupageDTO(item);
            DepotDTO matchedDepot = listDepot.stream().filter(depot -> depot.getCode().equals(item.getCoddep())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-depot"));
            decoupageDTO.setDesignationDepot(matchedDepot.getDesignation());
            decoupageDTO.setCodeSaisiDepot(matchedDepot.getCodeSaisi());
            return decoupageDTO;
        }).distinct().collect(toList());
    }

    /**
     * Delete decoupage by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete Decoupage: {}", id);
        decoupageRepository.delete(id);
    }

    @Transactional(
            readOnly = true
    )
    public byte[] edition(String numBon)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureAV : {}");
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();

        log.debug("Request to get FactureBE: {}", id);
        Decoupage decoupage = decoupageRepository.findOne(numBon);
        Set<Integer> unitIds = new HashSet();
        decoupage.getDetailDecoupageList().forEach(item -> {
            unitIds.add(item.getUniteFinal());
            unitIds.add(item.getUniteOrigine());

        });

        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unitIds);
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(decoupage.getCoddep());
        DecoupageEditionDTO dto = DecoupageFactory.decoupageToDecoupageEditionDTO(decoupage);
        dto.setDesignationDepot(depot.getDesignation());
        List<DetailDecoupageEditionDTO> detailsDecoupageDTO = decoupage.getDetailDecoupageList().stream().map(item -> {
            DetailDecoupageEditionDTO detailDecoupageDTO = DetailDecoupageFactory.detaildecoupageToDetailDecoupageEditionDTO(item);
            UniteDTO originUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUniteOrigine())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit"));
            UniteDTO finalUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUniteFinal())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit"));
            detailDecoupageDTO.setOriginUnitDesignation(originUnit.getDesignation());
            detailDecoupageDTO.setFinalUnitDesignation(finalUnit.getDesignation());

            return detailDecoupageDTO;
        }).collect(toList());
        dto.setDetails(detailsDecoupageDTO);

        ReportClientDocument reportClientDoc = new ReportClientDocument();
        String local = "_" + language;
        reportClientDoc.open("Reports/Decoupage" + local + ".rpt", 0);

        reportClientDoc
                .getDatabaseController().setDataSource(dto.getDetails(), DetailDecoupageEditionDTO.class,
                        "Detaille", "Detaille");

        reportClientDoc
                .getDatabaseController().setDataSource(Arrays.asList(dto), DecoupageEditionDTO.class,
                        "Entete", "Entete");

        reportClientDoc
                .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                "clinique", "clinique");

        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }
}
