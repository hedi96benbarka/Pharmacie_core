package com.csys.pharmacie.vente.quittance.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.SatisfactionEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.vente.quittance.domain.FactureDR;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import com.csys.pharmacie.vente.quittance.domain.MvtstoDR;
import com.csys.pharmacie.vente.quittance.domain.QFactureDR;
import com.csys.pharmacie.vente.quittance.dto.ChambreDTO;
import com.csys.pharmacie.vente.quittance.dto.DemandeArticlesDto;
import com.csys.pharmacie.vente.quittance.dto.DemandeRecuperationDto;
import com.csys.pharmacie.vente.quittance.dto.FactureDRDTO;
import com.csys.pharmacie.vente.quittance.dto.FactureDRDetailDTO;
import com.csys.pharmacie.vente.quittance.dto.FactureDREditionDTO;
import com.csys.pharmacie.vente.quittance.dto.ListDemandeRecuperationDto;
import com.csys.pharmacie.vente.quittance.dto.LitDTO;
import com.csys.pharmacie.vente.quittance.dto.MvtQuittanceDTO;
import com.csys.pharmacie.vente.quittance.dto.MvtStoDrPKDTO;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDRDTO;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDTO;
import com.csys.pharmacie.vente.quittance.factory.DemandeRecuperationDtoAssembler;
import com.csys.pharmacie.vente.quittance.factory.FactureDREditionDTOFactory;
import com.csys.pharmacie.vente.quittance.factory.FactureDRFactory;
import com.csys.pharmacie.vente.quittance.factory.ListDemandeRecuperationDtoAssembler;
import com.csys.pharmacie.vente.quittance.factory.MvtstoDRFactory;
import com.csys.pharmacie.vente.quittance.factory.MvtstoFactory;
import com.csys.pharmacie.vente.quittance.repository.FactureDRRepository;
import com.csys.pharmacie.vente.quittance.repository.MvtstoDRRepository;
import com.csys.util.Preconditions;
import static com.csys.util.Preconditions.checkBusinessLogique;
import static com.csys.util.RestPreconditions.checkFound;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing FactureDR.
 */
@Service
@Transactional
public class FactureDRService {

    private final Logger log = LoggerFactory.getLogger(FactureDRService.class);

    private final FactureDRRepository facturedrRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final ParamServiceClient paramServiceClient;
    private final ParamService paramService;
    private final MvtstoDRRepository mvtstoDRRepository;

    public FactureDRService(FactureDRRepository facturedrRepository, ParamAchatServiceClient paramAchatServiceClient, ParamServiceClient paramServiceClient, ParamService paramService, MvtstoDRRepository mvtstoDRRepository) {
        this.facturedrRepository = facturedrRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.paramServiceClient = paramServiceClient;
        this.paramService = paramService;
        this.mvtstoDRRepository = mvtstoDRRepository;
    }

    /**
     * Get all the factures DR.
     *
     * @param categ
     * @param fromDate
     * @param codeAdmission
     * @param toDate
     * @param coddep
     * @param deleted
     * @param codeEtage
     * @param satisfactions
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public List<ListDemandeRecuperationDto> findAll(List<CategorieDepotEnum> categ, LocalDateTime fromDate,
            LocalDateTime toDate, String codeAdmission, Integer coddep, Boolean deleted, Integer codeEtage, List<SatisfactionEnum> satisfactions
    ) {

        QFactureDR _factureDR = QFactureDR.factureDR;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(categ, () -> _factureDR.categDepot.in(categ))
                .optionalAnd(satisfactions, () -> _factureDR.satisf.in(satisfactions))
                .optionalAnd(fromDate, () -> _factureDR.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _factureDR.datbon.loe(toDate))
                .optionalAnd(codeAdmission, () -> _factureDR.numdoss.eq(codeAdmission))
                .optionalAnd(coddep, () -> _factureDR.coddep.eq(coddep))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _factureDR.codAnnul.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _factureDR.codAnnul.isNull());
        log.debug("Request to get All Factures");

        List<FactureDR> result = new ArrayList<>(facturedrRepository.findAll(builder));

        List<Integer> codeDepots = new ArrayList<>();
        List<String> numeroLits = new ArrayList<>();
        result.forEach(facture -> {
            codeDepots.add(facture.getCoddep());
            numeroLits.add(facture.getNumCha());
        });
        log.debug("numeroLits {}", numeroLits);
        List<LitDTO> listLits = paramServiceClient.litsFindbyListCode(numeroLits.stream().distinct().collect(Collectors.toList()));
        log.debug("listLits {}", listLits);
        List<DepotDTO> listDepots = paramAchatServiceClient.findDepotsByCodes(codeDepots.stream().distinct().collect(Collectors.toList()));
        List<ListDemandeRecuperationDto> resultDTO = new ArrayList<>();
        result.forEach(facture -> {
            DepotDTO matchingDepotDTO = listDepots.stream().filter(x -> x.getCode().equals(facture.getCoddep())).findFirst().orElse(null);
           Preconditions.checkBusinessLogique(matchingDepotDTO!=null,"missing-depot", facture.getCoddep().toString() );
            LitDTO lit = listLits.stream().filter(x -> x.getNumLit().equals(facture.getNumCha())).findFirst().orElse(null);
            resultDTO.add(ListDemandeRecuperationDtoAssembler.assembler(facture, matchingDepotDTO, lit));
        });
        if (codeEtage != null) {
            List<ListDemandeRecuperationDto> res = resultDTO.stream().filter(x -> x.getLit().getCodeEtage().equals(codeEtage)).collect(Collectors.toList());
            return res;
        }
        return resultDTO;
    }

    /**
     * Get one facturedr by id.
     *
     * @param factureDRDTO
     * @param mvtQuittances
     * @return the entity
     */
    public FactureDR update(FactureDRDTO factureDRDTO, List<MvtQuittanceDTO> mvtQuittances) {
        FactureDR factureDr = facturedrRepository.findOne(factureDRDTO.getNumbon());
        Preconditions.checkBusinessLogique(factureDr!=null,"factureDr.NotFound",factureDRDTO.getNumbon());
        if (mvtQuittances.size() > 0 || (factureDRDTO.getListMvtStoDr() != null && factureDRDTO.getListMvtStoDr().size() > 0)) {
            checkFound(factureDr, "factureDR.NotFound");
            checkBusinessLogique(factureDr.getCodAnnul() == null, "factureDR.Annule");
            checkBusinessLogique(!factureDr.getSatisf().equals(SatisfactionEnum.RECOVRED), "factureDR.SatisfaitTotalement");
            factureDr.setEtatbon(Boolean.TRUE);
            factureDr.setReffrs(SecurityContextHolder.getContext().getAuthentication().getName());
            factureDr.setDatreffrs(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
            for (MvtQuittanceDTO mvt : mvtQuittances) {
                List<MvtstoDR> listeMvtstoDRByCodArt = factureDr.getDetailFactureCollection().stream()
                        .filter(item -> item.getMvtstoDRPK().getCodart().equals(mvt.getCodart())
                        && item.getQteben().compareTo(BigDecimal.ZERO) > 0
                        && item.getCodeUnite().equals(mvt.getUnite())
                        && item.isaRemplacer() == false)
                        .collect(Collectors.toList());
                log.debug(" listeMvtstoDRByCodArt after collect {}", listeMvtstoDRByCodArt);
                BigDecimal qte = mvt.getQuantite();
                Integer availableQteMvtStoDr = listeMvtstoDRByCodArt.stream()
                        .map(filtredItem -> filtredItem.getQteben().intValue())
                        .collect(Collectors.summingInt(Integer::new));
                log.debug(" qte {}", qte);
                log.debug(" availableQteMvtStot {}", availableQteMvtStoDr);
                checkBusinessLogique(availableQteMvtStoDr >= qte.intValue(), "return.add.insuffisant-qte-demande", mvt.getCodart().toString());

                log.debug(" listeMvtstoDRByCodArt before {}", listeMvtstoDRByCodArt);
                for (MvtstoDR mvtstoDR : listeMvtstoDRByCodArt) {
                    log.debug(" mvtstoDR before {}", mvtstoDR);
                    BigDecimal qteToRmvFromMvtstoDr = qte.min(mvtstoDR.getQteben());
                    if (qteToRmvFromMvtstoDr.compareTo(BigDecimal.ZERO) > 0) {
                        mvtstoDR.setQteben(mvtstoDR.getQteben().subtract(qteToRmvFromMvtstoDr));
                        qte = qte.subtract(qteToRmvFromMvtstoDr);
                    }
                    log.debug(" mvtstoDR after {}", mvtstoDR);
                    log.debug(" listeMvtstoDRByCodArt {}", listeMvtstoDRByCodArt);
                    if (qte.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                }
                log.debug(" listeMvtstoDRByCodArt after {}", listeMvtstoDRByCodArt);
            }
            if (factureDRDTO.getListMvtStoDr() != null) {
                for (MvtStoDrPKDTO mvt : factureDRDTO.getListMvtStoDr()) {
                    MvtstoDR mvtstoDR = factureDr.getDetailFactureCollection().stream()
                            .filter(item -> item.getMvtstoDRPK().getCodart().equals(mvt.getCodart())
                            && item.getMvtstoDRPK().getNumbon().equals(mvt.getNumbon())
                            && item.getMvtstoDRPK().getNumordre().equals(mvt.getNumordre()))
                            .findFirst()
                            .orElse(null);
                    checkBusinessLogique(mvtstoDR != null, "missing.mvt");
                    checkBusinessLogique(!mvtstoDR.isaRemplacer(), "mvt.deja.remplacer");
                    checkBusinessLogique(mvtstoDR.getQteben().compareTo(BigDecimal.ZERO) > 0, "mvt.deja.RECOVRED");
                    mvtstoDR.setaRemplacer(true);
                }
            }
            SatisfactionEnum stisfaction = SatisfactionEnum.RECOVRED;
            for (MvtstoDR mvtstoDR : factureDr.getDetailFactureCollection()) {
                if (mvtstoDR.getQteben().compareTo(BigDecimal.ZERO) > 0 && !mvtstoDR.isaRemplacer()) {
                    stisfaction = SatisfactionEnum.PARTIALLY_RECOVRED;
                    continue;
                }
            }
            factureDr.setSatisf(stisfaction);
            factureDr = facturedrRepository.save(factureDr);
        }

        return factureDr;
    }

    /**
     * Get one facturedr by id.
     *
     * @param factureDRDTO
     * @param mvtQuittances
     * @return the entity
     */
    public FactureDR annulationUpdate(FactureDRDTO factureDRDTO, List<MvtQuittanceDTO> mvtQuittances) {
        FactureDR factureDr = facturedrRepository.findOne(factureDRDTO.getNumbon());
        if (mvtQuittances.size() > 0 || (factureDRDTO.getListMvtStoDr() != null && factureDRDTO.getListMvtStoDr().size() > 0)) {
            checkFound(factureDr, "factureDR.NotFound");
            checkBusinessLogique(factureDr.getCodAnnul() == null, "factureDR.Annule");
            for (MvtQuittanceDTO mvt : mvtQuittances) {
                List<MvtstoDR> listeMvtstoDRByCodArt = factureDr.getDetailFactureCollection().stream()
                        .filter(item -> item.getMvtstoDRPK().getCodart().equals(mvt.getCodart())
                        && item.getQteben().compareTo(BigDecimal.ZERO) > 0
                        && item.getCodeUnite().equals(mvt.getUnite()))
                        .collect(Collectors.toList());
                BigDecimal qte = mvt.getQuantite();
                Integer availableQteMvtStoDr = listeMvtstoDRByCodArt.stream()
                        .map(filtredItem -> filtredItem.getQuantite().subtract(filtredItem.getQteben()).intValue())
                        .collect(Collectors.summingInt(Integer::new));

                checkBusinessLogique(availableQteMvtStoDr >= qte.intValue(), "return.add.insuffisant-qte-demande", mvt.getCodart().toString());
                for (MvtstoDR mvtstoDR : listeMvtstoDRByCodArt) {
                    BigDecimal qteToAddFromMvtstoDr = qte.min(mvtstoDR.getQuantite().subtract(mvtstoDR.getQteben()));
                    if (qteToAddFromMvtstoDr.compareTo(BigDecimal.ZERO) > 0) {
                        mvtstoDR.setQteben(mvtstoDR.getQteben().add(qteToAddFromMvtstoDr));
                        qte = qte.subtract(qteToAddFromMvtstoDr);
                    }
                    if (qte.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                }
                log.debug(" listeMvtstoDRByCodArt after {}", listeMvtstoDRByCodArt);
            }
            if (factureDRDTO.getListMvtStoDr() != null) {
                for (MvtStoDrPKDTO mvt : factureDRDTO.getListMvtStoDr()) {
                    MvtstoDR mvtstoDR = factureDr.getDetailFactureCollection().stream()
                            .filter(item -> item.getMvtstoDRPK().getCodart().equals(mvt.getCodart())
                            && item.getMvtstoDRPK().getNumbon().equals(mvt.getNumbon())
                            && item.getMvtstoDRPK().getNumordre().equals(mvt.getNumordre()))
                            .findFirst()
                            .orElse(null);
                    mvtstoDR.setaRemplacer(false);
                }
            }
            SatisfactionEnum stisfaction = SatisfactionEnum.NOT_RECOVRED;
            for (MvtstoDR mvtstoDR : factureDr.getDetailFactureCollection()) {
                if (mvtstoDR.getQteben().compareTo(BigDecimal.ZERO) > 0 && !mvtstoDR.isaRemplacer()) {
                    stisfaction = SatisfactionEnum.PARTIALLY_RECOVRED;
                    continue;
                }
            }
            factureDr.setSatisf(stisfaction);
            factureDr = facturedrRepository.save(factureDr);
        }

        return factureDr;
    }

    /**
     * Get one facturedr by id.
     *
     * @param factureDRDTO
     * @return the entity
     */
    public DemandeRecuperationDto update(FactureDRDTO factureDRDTO) {
        checkBusinessLogique(factureDRDTO.getListMvtStoDr() != null && factureDRDTO.getListMvtStoDr().size() > 0, "missing.details");
        FactureDR facture = update(factureDRDTO, new ArrayList<>());
        facture = facturedrRepository.save(facture);
        DemandeRecuperationDto dto = DemandeRecuperationDtoAssembler.assembler(facture);
        if (facture.getFactures() != null) {
            List<String> listQuitance = new ArrayList<>();
            log.debug("list des quittances {}", facture.getFactures());
            facture.getFactures().forEach(item -> {
                listQuitance.add(item.getNumbon());
            });
            log.debug("list des num quittances {}", listQuitance);
            dto.setNumQuittance(listQuitance);
        }

        List<Integer> codeUnites = new ArrayList<>();
        facture.getDetailFactureCollection().forEach(x -> {
            codeUnites.add(x.getCodeUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        dto.getDemandes().forEach((demande) -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(demande.getCodeunite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            demande.setDesignationunite(unite.getDesignation());

        });
        return dto;
    }

    /**
     * Get one factures DR by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public DemandeRecuperationDto findOne(String id) {
        log.debug("Request to get Facture: {}", id);
        FactureDR factureDr = facturedrRepository.findOne(id);
        DemandeRecuperationDto dto = DemandeRecuperationDtoAssembler.assembler(factureDr);
        log.debug("Request to get Facture DemandeRecuperationDto dto : {}", dto);
        if (factureDr.getFactures() != null) {
            List<String> listQuitance = new ArrayList<>();
            log.debug("list des quittances {}", factureDr.getFactures());
            factureDr.getFactures().forEach(item -> {
                listQuitance.add(item.getNumbon());
            });
            log.debug("list des num quittances {}", listQuitance);
            dto.setNumQuittance(listQuitance);
        }

        List<Integer> codeUnites = new ArrayList<>();
        factureDr.getDetailFactureCollection().forEach(x -> {
            codeUnites.add(x.getCodeUnite());
        });
        DepotDTO depot = paramAchatServiceClient.findDepotByCode((int) factureDr.getCoddep());
        Preconditions.checkBusinessLogique(!depot.getDesignation().equals("depot.deleted"), "Depot [" + (int) factureDr.getCoddep() + "] introuvable");
        dto.setDesignationDepot(depot.getDesignation());
        dto.setDesignationDepotSec(depot.getDesignationSec());
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        dto.getDemandes().forEach((demande) -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(demande.getCodeunite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            demande.setDesignationunite(unite.getDesignation());
            DepotDTO depotd = paramAchatServiceClient.findDepotByCode((int) demande.getCoddep());
            log.debug("test id {}", depotd);
            Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + demande.getCoddep() + "] introuvable");
            demande.setDesignationDepot(depotd.getDesignation());
            demande.setDesignationDepotSec(depotd.getDesignationSec());

        });
        return dto;

    }

    /**
     * Delete factureDR by id.
     *
     * @param id the id of the entity
     * @return
     */
    public DemandeRecuperationDto delete(String id) {
        log.debug("Request to delete Receiving: {}", id);
        FactureDR factureDR = facturedrRepository.findOne(id);
        checkBusinessLogique(factureDR!=null, "factureDR.NotFound");
        checkBusinessLogique(factureDR.getCodAnnul() == null, "factureDR.Annule");
        checkBusinessLogique(!factureDR.getEtatbon(), "factureDR.Valide");
        LocalDateTime date = LocalDateTime.now();
        factureDR.setDatAnnul(date);
        factureDR.setCodAnnul(SecurityContextHolder.getContext().getAuthentication().getName());
        factureDR = facturedrRepository.save(factureDR);
        DemandeRecuperationDto dto = DemandeRecuperationDtoAssembler.assembler(factureDR);
        if (factureDR.getFactures() != null) {
            List<String> listQuitance = new ArrayList<>();
            factureDR.getFactures().forEach(item -> {
                listQuitance.add(item.getNumbon());
            });
            dto.setNumQuittance(listQuitance);
        }

        List<Integer> codeUnites = new ArrayList<>();
        factureDR.getDetailFactureCollection().forEach(x -> {
            codeUnites.add(x.getCodeUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        dto.getDemandes().forEach((demande) -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(demande.getCodeunite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            demande.setDesignationunite(unite.getDesignation());

        });
        return dto;
    }

//    @Transactional(
//            readOnly = true
//    )
    public byte[] editionFactureDR(String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = paramServiceClient.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        reportClientDoc.open("Reports/Dispensation" + local + ".rpt", 0);
        FactureDR factureDr = facturedrRepository.findOne(id);
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(factureDr.getCoddep());
        FactureDREditionDTO dto = FactureDREditionDTOFactory.EntityToFactureDREditionDTO(factureDr, depot);
        log.debug("Request to get Facture DemandeRecuperationDto dto : {}", dto);

        List<Integer> codeUnites = new ArrayList<>();
        factureDr.getDetailFactureCollection().forEach(x -> {
            codeUnites.add(x.getCodeUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        dto.getDemandes().forEach((demande) -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(demande.getCodeunite())).findFirst().orElse(null);
            Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            demande.setDesignationunite(unite.getDesignation());

        });
        List<DemandeArticlesDto> listeDemandes = dto.getDemandes().stream().filter(x -> x.isaRemplacer() == false).collect(Collectors.toList());
        Preconditions.checkBusinessLogique(!listeDemandes.isEmpty(), "factureDR.NoInformationsToPrint");
        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(dto), FactureDREditionDTO.class, "commande", "commande");
        reportClientDoc.getDatabaseController().setDataSource(listeDemandes, DemandeArticlesDto.class, "detail", "detail");
        reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class, "clinique", "clinique");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        List<String> codesLits = new ArrayList<>();
        codesLits.add(factureDr.getNumCha());
        LitDTO litDTO = paramServiceClient.litsFindbyListCode(codesLits).stream().findFirst().get();
        ChambreDTO chambreDto = paramServiceClient.findChambreById(litDTO.getCodeChambre());
        paramController.setCurrentValue("", "depotDes", chambreDto.getDepartement().getDesignation());
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        imprimer(id);
        return Helper.read(byteArrayInputStream);
    }

    private Boolean imprimer(String id) {
        log.debug("");
        FactureDR factureDr = facturedrRepository.findOne(id);
        log.debug("factureDr.setImprimer {}", factureDr.isImprimer());
        Preconditions.checkBusinessLogique(factureDr != null, "factureDr.NotFound");
        factureDr.setImprimer(true);
        log.debug("factureDr.setImprimer {}", factureDr.isImprimer());

        facturedrRepository.save(factureDr);
        return true;
    }

    @Transactional
    public FactureDR save(FactureDRDetailDTO factureDRDTO) {
        DepotDTO depotd = paramAchatServiceClient.findDepotByCode(factureDRDTO.getCoddep());
        Preconditions.checkBusinessLogique(!depotd.getDesignation().equals("depot.deleted"), "Depot [" + factureDRDTO.getCoddep() + "] introuvable");

        List<Integer> codeUnites = factureDRDTO.getDetailFactureDTOCollection().stream().map(x -> x.getCodeUnite()).collect(toList());
        List<Integer> codeArticles = factureDRDTO.getDetailFactureDTOCollection().stream().map(x -> x.getCodart()).collect(toList());

        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        List<ArticleDTO> listArticles = paramAchatServiceClient.articleFindbyListCode(codeArticles.stream().distinct().collect(Collectors.toList()));

        for (MvtstoDRDTO mvtstoDRDTO : factureDRDTO.getDetailFactureDTOCollection()) {
            //check unite if exist
            UniteDTO matchingUnite = listUnite.stream()
                    .filter(unite -> unite.getCode().equals(mvtstoDRDTO.getCodeUnite()))
                    .findFirst().orElse(null);
            Preconditions.checkBusinessLogique(matchingUnite != null, "missing-unity");

            mvtstoDRDTO.setDesignationUnite(matchingUnite.getDesignation());

            mvtstoDRDTO.setQteben(mvtstoDRDTO.getQuantite());
// check article if exist
            ArticleDTO matchingItem = listArticles.stream()
                    .filter(article -> article.getCode().equals(mvtstoDRDTO.getCodart()))
                    .findFirst()
                    .orElse(null);
            Preconditions.checkBusinessLogique(matchingItem != null, "missing-article");

            mvtstoDRDTO.setCodeSaisi(matchingItem.getCodeSaisi());
            mvtstoDRDTO.setDesart(matchingItem.getDesignation());
            mvtstoDRDTO.setDesartSec(matchingItem.getDesignationSec());

        }
        // par défaut la categorie depot enum de facture DR prend la valeur de PH
        paramService.updateCompteurPharmacie(CategorieDepotEnum.PH, factureDRDTO.getTypbon());
        String numbon = paramService.getcompteur(factureDRDTO.getCategDepot(), TypeBonEnum.DR);
        factureDRDTO.setNumbon(numbon.substring(2));
        FactureDR factureDr = FactureDRFactory.FactureDRDTOTOFactureDR(factureDRDTO);

        facturedrRepository.save(factureDr);
        //factureDr.setNumbon(factureDr.getNumaffiche());
        return factureDr;

    }

    @Transactional(readOnly = true)
    public List<MvtstoDRDTO> findDetailsFactureDRs(String codeAdmission, Integer codeDepot, LocalDateTime fromDate, LocalDateTime toDate) {
        List<MvtstoDR> mvtstoDRs = mvtstoDRRepository.findByFactureDR_NumdossAndFactureDR_CoddepAndFactureDR_DatbonBetween(codeAdmission, codeDepot, fromDate, toDate);
        log.debug("resultat mvtstoDRs : {}", mvtstoDRs.toString());
        List<MvtstoDRDTO> result = MvtstoDRFactory.mvtstodrsToMvtstodrDtos(mvtstoDRs);
        List<Integer> codeUnites = new ArrayList<>();
        result.forEach(x -> {
            codeUnites.add(x.getCodeUnite());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        result.forEach((detail) -> {
            Optional<UniteDTO> unite = listUnite.stream().filter(x -> x.getCode().equals(detail.getCodeUnite())).findFirst();
            if (unite.isPresent()) {
                detail.setDesignationUnite(unite.get().getDesignation());
            }
        });
        return result;
    }

}
