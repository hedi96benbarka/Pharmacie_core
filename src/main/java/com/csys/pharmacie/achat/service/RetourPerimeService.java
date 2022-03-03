package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.domain.DetailMvtStoRetourPerime;
import com.csys.pharmacie.achat.domain.MvtstoRetourPerime;
import com.csys.pharmacie.achat.domain.QRetourPerime;
import com.csys.pharmacie.achat.domain.RetourPerime;
import com.csys.pharmacie.achat.dto.BonEditionDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.RetourPerimeDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoRetourPerimeDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.factory.RetourPerimeFactory;
import com.csys.pharmacie.achat.factory.MvtstoRetourPerimeFactory;
import com.csys.pharmacie.achat.repository.RetourPerimeRepository;
import com.csys.pharmacie.helper.BaseTVADTO;
import com.csys.pharmacie.helper.BaseTVAFactory;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.DetailMvtSto;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.parametrage.entity.CompteurPharmacie;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.stock.domain.Depsto;
import com.csys.pharmacie.stock.service.StockService;
import com.csys.pharmacie.helper.Convert;

import com.csys.util.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.String;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing FactureRetour_perime.
 */
@Service
@Transactional
public class RetourPerimeService {

    private final Logger log = LoggerFactory.getLogger(RetourPerimeService.class);

    @Autowired
    MotifRetourService motifRetourService;

    @Autowired
    StockService stockService;
    @Autowired
    ParamAchatServiceClient paramAchatServiceClient;

    static String INTERVAL;

    private final RetourPerimeRepository retourPerimeRepository;

    private final ParamService paramService;

    private final ParamServiceClient parametrageService;

    private final AjustementRetourPerimeService ajustementRetourPerimeService;

    public RetourPerimeService(RetourPerimeRepository retourPerimeRepository, ParamService paramService, ParamServiceClient parametrageService, AjustementRetourPerimeService ajustementRetourPerimeService) {
        this.retourPerimeRepository = retourPerimeRepository;
        this.paramService = paramService;
        this.parametrageService = parametrageService;
        this.ajustementRetourPerimeService = ajustementRetourPerimeService;
    }

    @Value("${retour-perime.config.expiration-date-interval}")
    public void setINTERVAL(String db) {
        INTERVAL = db;
    }

    public String getINTERVAL() {
        return INTERVAL;
    }

    /**
     * Save a factureretour_perimeDTO.
     *
     * @param facturereRPDTO
     * @return the persisted entity
     */
    public RetourPerimeDTO save(RetourPerimeDTO facturereRPDTO) {

        log.debug("Request to save FactureRetour: {}", facturereRPDTO);
       //-------  depot --------/
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(facturereRPDTO.getCoddep());
        Preconditions.checkBusinessLogique(!depot.getDesignation().equals("depot.deleted"), "missing-warehouse", "missing-warehouse");
        Preconditions.checkBusinessLogique(!depot.getDepotFrs(), "bad-warehouse");
        Preconditions.checkBusinessLogique(depot.getPerime(), "bad-warehouse");
        
        //-------  fournisseur --------/
        FournisseurDTO fournisseurDTO = paramAchatServiceClient.findFournisseurByCode(facturereRPDTO.getCodFrs());
        Preconditions.checkBusinessLogique(!fournisseurDTO.getDesignation().equals("fournisseur.deleted"), "Fournisseur avec code : " + facturereRPDTO.getCodFrs() + " est introuvable");
        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(fournisseurDTO.getAnnule()) && !(Boolean.TRUE.equals(fournisseurDTO.getStopped())), "fournisseur.stopped", fournisseurDTO.getCode());

        
        RetourPerime factureRP = RetourPerimeFactory.retourPerimeDTOToRetourPerime(facturereRPDTO);
        factureRP.setTypbon(TypeBonEnum.RP);
        String numbon = paramService.getcompteur(facturereRPDTO.getCategDepot(), TypeBonEnum.RP);
        factureRP.setNumbon(numbon);
        factureRP.setMotifRetour(motifRetourService.findMotifRetour(facturereRPDTO.getMotifRetourDTO().getId()));
        List<MvtstoRetourPerime> detailsFacture = new ArrayList();

        List<Integer> codArticles = facturereRPDTO.getDetailFactureRPCollection().stream().map(item -> item.getCodart()).collect(Collectors.toList());

        List<Depsto> stock = stockService.findByCodartInAndCoddepAndQteGreaterThan(codArticles, factureRP.getCoddep(), BigDecimal.ZERO);
        String numordre1 = "0001";
        for (MvtstoRetourPerimeDTO mvtstoDTO : facturereRPDTO.getDetailFactureRPCollection()) {
            MvtstoRetourPerime detailFacture = MvtstoRetourPerimeFactory.mvtStoretourPerimeDTOToMvtStoRetourPerime(mvtstoDTO);
            Preconditions.checkBusinessLogique(detailFacture.getDatPer().isBefore(LocalDate.now().plusDays(Long.parseLong(INTERVAL))), "Article-not-Perime");
            detailFacture.setCodart(mvtstoDTO.getCodart());
            detailFacture.setNumbon(numbon);
            detailFacture.setNumordre(numordre1);
            detailFacture.setFactureRetourPerime(factureRP);
            detailFacture.setCategDepot(factureRP.getCategDepot());
            numordre1 = Helper.IncrementString(numordre1, 4);
            detailsFacture.add(detailFacture);

            List<DetailMvtSto> detailMvtSto = stockService.GestionDetailFacture(stock, detailFacture, DetailMvtStoRetourPerime.class, false);
            detailFacture.setPriuni(mvtstoDTO.getPriuni());
            List<DetailMvtStoRetourPerime> detailMvtStoRetourP
                    = detailMvtSto.stream()
                            .map(item -> {
                                DetailMvtStoRetourPerime detailMvtStoRP = (DetailMvtStoRetourPerime) item;
                                detailMvtStoRP.setMvtstoRetourPerime(detailFacture);
                                return detailMvtStoRP;
                            })
                            .collect(Collectors.toList());

            detailFacture.setDetailMvtStoList(detailMvtStoRetourP);

        }
        factureRP.setDetailFactureRPCollection(detailsFacture);
        factureRP.calcul(paramAchatServiceClient.findTvas());
        factureRP = retourPerimeRepository.save(factureRP);
        paramService.updateCompteurPharmacie(factureRP.getCategDepot(), TypeBonEnum.RP);

        ajustementRetourPerimeService.saveAjustementRetour(factureRP);

        RetourPerimeDTO resultDTO = RetourPerimeFactory.retourPerimeToRetourPerimeDTO(factureRP);

        return resultDTO;
    }

    /**
     * Update a factureretour_perimeDTO.
     *
     * @param factureretour_perimeDTO
     * @return the updated entity
     */
    public RetourPerimeDTO update(RetourPerimeDTO factureretour_perimeDTO) {
        log.debug("Request to update FactureRetour_perime: {}", factureretour_perimeDTO);
        RetourPerime inBase = retourPerimeRepository.findOne(factureretour_perimeDTO.getNumbon());
//    Preconditions.checkArgument(inBase != null, "factureretour_perime.NotFound");
        RetourPerime factureretour_perime = RetourPerimeFactory.retourPerimeDTOToRetourPerime(factureretour_perimeDTO);
        factureretour_perime = retourPerimeRepository.save(factureretour_perime);
        RetourPerimeDTO resultDTO = RetourPerimeFactory.retourPerimeToRetourPerimeDTO(factureretour_perime);
        return resultDTO;
    }

    /**
     * Get one factureretour_perimeDTO by id.
     *
     * @param numbon
     * @return the entity DTO
     */
    @Transactional(readOnly = true)
    public RetourPerimeDTO findOne(String numbon) {
        log.debug("Request to get FactureRetour_perime: {}", numbon);
        RetourPerime retourPerime = retourPerimeRepository.findOne(numbon);
        RetourPerimeDTO dto = RetourPerimeFactory.lazyRetourPerimeToRetourPerimeDTO(retourPerime);
        dto.setBasesTVA(BaseTVAFactory.listeEntitiesToListDTos(retourPerime.getBaseTvaRetourPerime()));
        FournisseurDTO fournisseurs = paramAchatServiceClient.findFournisseurByCode(dto.getCodFrs());
        String desig = paramAchatServiceClient.findDepotByCode(retourPerime.getCoddep()).getDesdep();
        dto.setDesignationDepot(desig);
        dto.setFournisseur(fournisseurs);
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(retourPerime.getDetailFactureRPCollection().stream().map(MvtstoRetourPerime::getUnite).collect(toList()));
        List<MvtstoRetourPerimeDTO> detailsRetour = retourPerime.getDetailFactureRPCollection().stream().map(item -> {
            MvtstoRetourPerimeDTO mvtstoRetourPerimeDTO = MvtstoRetourPerimeFactory.mvtStoretourPerimeToMvtStoRetourPerimeDTO(item);
            Optional<UniteDTO> matchedUnity = unities.stream().filter(unity -> unity.getCode().equals(mvtstoRetourPerimeDTO.getCodeUnite())).findFirst();
            Preconditions.checkBusinessLogique(matchedUnity.isPresent(), "returns.find-one.missing-unity", retourPerime.getCoddep().toString());
            mvtstoRetourPerimeDTO.setDesignationUnite(matchedUnity.get().getDesignation());
            return mvtstoRetourPerimeDTO;
        }).collect(toList());
        dto.setDetailFactureRPCollection(detailsRetour);
        return dto;
    }

    /**
     * Get one factureretour_perime by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public RetourPerime findFactureRetour_perime(String id) {
        log.debug("Request to get FactureRetour_perime: {}", id);
        RetourPerime factureretour_perime = retourPerimeRepository.findOne(id);
        return factureretour_perime;
    }

    /**
     * Get all the factureretour_perimes.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param codeFrs
     * @param invoiced
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<RetourPerimeDTO> findAll(CategorieDepotEnum categ, LocalDateTime fromDate, LocalDateTime toDate, String codeFrs, Boolean invoiced) {

        QRetourPerime _RetourPerime = QRetourPerime.retourPerime;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(fromDate, () -> _RetourPerime.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _RetourPerime.datbon.loe(toDate))
                .optionalAnd(categ, () -> _RetourPerime.categDepot.eq(categ))
                .optionalAnd(codeFrs, () -> _RetourPerime.codFrs.eq(codeFrs))
                .booleanAnd(Objects.equals(invoiced, Boolean.TRUE), () -> _RetourPerime.factureRetourPerime().isNotNull())
                .booleanAnd(Objects.equals(invoiced, Boolean.FALSE), () -> _RetourPerime.factureRetourPerime().isNull());

        List<RetourPerime> listFactureRPs = (List<RetourPerime>) retourPerimeRepository.findAll(builder);

        List<String> codesFrs = listFactureRPs.stream().map(item -> item.getCodFrs()).distinct().collect(Collectors.toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
        List<DepotDTO> depots = paramAchatServiceClient.findDepotsByCodes(listFactureRPs.stream().map(item -> item.getCoddep()).distinct().collect(Collectors.toList()));
        List<RetourPerimeDTO> resultDTO = new ArrayList();
        listFactureRPs.forEach((retourPerime) -> {
            RetourPerimeDTO retourPerimeDTO = RetourPerimeFactory.lazyRetourPerimeToRetourPerimeDTO(retourPerime);
            Optional<FournisseurDTO> optFrs = fournisseurs.stream().filter(item -> item.getCode().equals(retourPerime.getCodFrs())).findFirst();
            Preconditions.checkBusinessLogique(optFrs.isPresent(), "returns.get-all.missing-frs", retourPerime.getCodFrs());
            retourPerimeDTO.setFournisseur(optFrs.get());
            Optional<DepotDTO> depot = depots.stream().filter(item -> item.getCode().equals(retourPerime.getCoddep())).findFirst();
            Preconditions.checkBusinessLogique(depot.isPresent(), "returns.get-all.missing-depot", retourPerime.getCoddep().toString());
            retourPerimeDTO.setDesignationDepot(depot.get().getDesignation());
            resultDTO.add(retourPerimeDTO);
        });

        return resultDTO;
    }

    /**
     * Delete factureretour_perime by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete FactureRetour_perime: {}", id);
        retourPerimeRepository.delete(id);

    }

    /**
     * Edition BonEditionDTO by .
     *
     * @param codfrs
     * @param fromDate
     * @param toDate
     * @param categ
     * @return
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @Transactional(
            readOnly = true
    )
    public List<BonEditionDTO> editionEtatAchat(String codfrs, LocalDateTime fromDate, LocalDateTime toDate, CategorieDepotEnum categ, List<Integer> codarts) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        QRetourPerime _RetourPerime = QRetourPerime.retourPerime;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(codfrs, () -> _RetourPerime.codFrs.eq(codfrs))
                .and(_RetourPerime.codAnnul.isNull())
                .optionalAnd(fromDate, () -> _RetourPerime.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _RetourPerime.datbon.loe(toDate))
                .optionalAnd(categ, () -> _RetourPerime.categDepot.eq(categ));
        List<RetourPerime> result = (List<RetourPerime>) retourPerimeRepository.findAll(builder);
        List<BonEditionDTO> bonEditionDTOs = new ArrayList();
        if (!result.isEmpty()) {

            List<String> codesFrs = result.stream().map(item -> item.getCodFrs()).distinct().collect(Collectors.toList());
            List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
            List<DepotDTO> depots = paramAchatServiceClient.findDepotsByCodes(result.stream().map(item -> item.getCoddep()).distinct().collect(Collectors.toList()));
            List<Integer> codeUnites = new ArrayList<>();
            result.forEach(y -> {
                y.getDetailFactureRPCollection().forEach(mvt -> {
                    codeUnites.add(mvt.getUnite());
                });
            });
            List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);

            result.forEach((retourPerime) -> {
                BonEditionDTO bonEditionDTO = RetourPerimeFactory.lazyRetourPerimeToBonEditionDTO(retourPerime);
                Optional<FournisseurDTO> optFrs = fournisseurs.stream().filter(item -> item.getCode().equals(retourPerime.getCodFrs())).findFirst();
                Preconditions.checkBusinessLogique(optFrs.isPresent(), "returns.get-all.missing-frs", retourPerime.getCodFrs());
                bonEditionDTO.setFournisseur(optFrs.get());
                Optional<DepotDTO> depot = depots.stream().filter(item -> item.getCode().equals(retourPerime.getCoddep())).findFirst();
                Preconditions.checkBusinessLogique(depot.isPresent(), "returns.get-all.missing-depot", retourPerime.getCoddep().toString());
                bonEditionDTO.setDesignationDepot(depot.get().getDesignation());
                bonEditionDTO.getDetails().forEach(x -> {
                    UniteDTO unite = listUnite.stream().filter(y -> y.getCode().equals(x.getCodeUnite())).findFirst().orElse(null);
                    com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
                    x.setDesignationunite(unite.getDesignation());
                });
                bonEditionDTOs.add(bonEditionDTO);
            });
        }
        return bonEditionDTOs;
    }

    /**
     * Edition factureretour_perime by numbon.
     *
     * @param numbon the numbon of the entity
     * @return
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public byte[] edition(String numbon) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        RetourPerimeDTO dto = findOne(numbon);
        Preconditions.checkFound(dto, "Bon_RetourPerime.NotFound");
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        reportClientDoc.open("Reports/Bon_RetourPerime" + local + ".rpt", 0);

        reportClientDoc
                .getDatabaseController().setDataSource(java.util.Arrays.asList(dto), RetourPerimeDTO.class,
                        "Entete", "Entete");
        reportClientDoc
                .getDatabaseController().setDataSource(java.util.Arrays.asList(dto.getFournisseur()), FournisseurDTO.class,
                        "Fournisseur", "Fournisseur");
        reportClientDoc
                .getDatabaseController().setDataSource(dto.getDetailFactureRPCollection(), MvtstoRetourPerimeDTO.class,
                        "Detaille", "Detaille");
        reportClientDoc
                .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                "clinique", "clinique");
        reportClientDoc
                .getSubreportController().getSubreport("BaseTVA").getDatabaseController().setDataSource(dto.getBasesTVA(), BaseTVADTO.class,
                "Commande", "Commande");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        if ("ar".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.AR(dto.getMntbon().toString(), "جنيه", "قرش"));
        } else if ("fr".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.FR(dto.getMntbon().toString(), "livres", "pence"));
        } else {
            paramController.setCurrentValue("", "montantLettre", Convert.EN(dto.getMntbon().toString(), "pounds", "penny"));
        }
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }

    public Set<RetourPerime> findRetourPerimeByNumBonIn(List<String> numBons) {
        return retourPerimeRepository.findByNumbonIn(numBons);
    }
}
