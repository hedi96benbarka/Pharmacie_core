package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.FactureRetourPerime;
import com.csys.pharmacie.achat.domain.FcptfrsPH;
import com.csys.pharmacie.achat.domain.QFactureRetourPerime;
import com.csys.pharmacie.achat.domain.RetourPerime;
import com.csys.pharmacie.achat.dto.BaseTvaFactureRetourPerimeDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.FactureRetourPerimeDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoRetourPerimeDTO;
import com.csys.pharmacie.achat.dto.TvaDTO;
import com.csys.pharmacie.achat.factory.FactureRetourPerimeFactory;
import com.csys.pharmacie.achat.factory.RetourPerimeFactory;
import com.csys.pharmacie.achat.repository.FactureBonReceptionRepository;
import com.csys.pharmacie.achat.repository.FactureRetourPerimeRepository;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Convert;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.parametrage.repository.ParamService;
import com.csys.util.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
 * Service Implementation for managing FactureRetourPerime.
 */
@Service
@Transactional
public class FactureRetourPerimeService {

    private final Logger log = LoggerFactory.getLogger(FactureRetourPerimeService.class);

    @Value("${max-marge-montant-facture}")
    private String maxMargeMontant;

    private final FactureRetourPerimeRepository factureRetourPerimeRepository;

    private final ParamAchatServiceClient paramAchatServiceClient;
    private final ParamService paramService;
    private final ParamServiceClient paramServiceClient;
    private final FcptFrsPHService fcptFrsPHService;
    private final RetourPerimeService retourPerimeService;
    private final FactureBonReceptionRepository facturebonreceptionRepository;
    private final MvtstoRetourPerimeService mvtstoRetourPerimeService;

    public FactureRetourPerimeService(FactureRetourPerimeRepository factureRetourPerimeRepository, ParamAchatServiceClient paramAchatServiceClient, ParamService paramService, ParamServiceClient paramServiceClient, FcptFrsPHService fcptFrsPHService, RetourPerimeService retourPerimeService, FactureBonReceptionRepository facturebonreceptionRepository, MvtstoRetourPerimeService mvtstoRetourPerimeService) {
        this.factureRetourPerimeRepository = factureRetourPerimeRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.paramService = paramService;
        this.paramServiceClient = paramServiceClient;
        this.fcptFrsPHService = fcptFrsPHService;
        this.retourPerimeService = retourPerimeService;
        this.facturebonreceptionRepository = facturebonreceptionRepository;
        this.mvtstoRetourPerimeService = mvtstoRetourPerimeService;
    }

    /**
     * Save a factureretourperimeDTO.
     *
     * @param factureRetourPerimeDTO
     * @return the persisted entity
     */
    public FactureRetourPerimeDTO save(FactureRetourPerimeDTO factureRetourPerimeDTO) {

        log.debug("Request to save FactureRetourPerime: {}", factureRetourPerimeDTO);

        FactureRetourPerime factureRetourPerime = FactureRetourPerimeFactory.factureRetourPerimeDTOToFactureRetourPerime(factureRetourPerimeDTO);
        log.debug("dateBonDto : {}", factureRetourPerimeDTO.getDateBon());
        log.debug("dateBon : {}", factureRetourPerime.getDatbon());

        FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(factureRetourPerimeDTO.getCodeFournisseur());
        Preconditions.checkBusinessLogique(fournisseur != null, "missing-supplier", factureRetourPerimeDTO.getCodeFournisseur());
        FactureRetourPerime factureWithSameRef = factureRetourPerimeRepository.findByReferenceFournisseur(factureRetourPerimeDTO.getReferenceFournisseur());
        Preconditions.checkBusinessLogique(factureWithSameRef == null, "reffrs-fournisseur-exists");

        factureRetourPerime.setTypbon(TypeBonEnum.FRP);
        String numBon = paramService.getcompteur(factureRetourPerime.getCategDepot(), TypeBonEnum.FRP);
        factureRetourPerime.setNumbon(numBon);
        paramService.updateCompteurPharmacie(factureRetourPerimeDTO.getCategDepot(), TypeBonEnum.FRP);
        factureRetourPerime.setIntegrer(Boolean.FALSE);

        List<String> munBonsRPRelative = factureRetourPerimeDTO.getRetourPerimeRelative().stream().map(item -> item.getNumbon()).collect(Collectors.toList());

        Set<RetourPerime> listeRetourPerimeRelative = retourPerimeService.findRetourPerimeByNumBonIn(munBonsRPRelative);

        Preconditions.checkBusinessLogique(!listeRetourPerimeRelative.isEmpty(), "retour-perime-not-exist");

        for (RetourPerime retourPerimeRelative : listeRetourPerimeRelative) {
            if (retourPerimeRelative.getNumbon() != null) {
                log.debug("num bon retour perime {} et le num bon de sa facture ", retourPerimeRelative.getNumbon());
            }
            Preconditions.checkBusinessLogique(retourPerimeRelative.getFactureRetourPerime() == null, "facture-retour-perime-invoiced-retour-perime", retourPerimeRelative.getNumbon());
            retourPerimeRelative.setFactureRetourPerime(factureRetourPerime);
        }

        Preconditions.checkBusinessLogique(!factureRetourPerimeDTO.getDateBon().isBefore(
                listeRetourPerimeRelative.stream().sorted(Comparator.comparing(RetourPerime::getDatbon).reversed()).findFirst().get().getDatbon()
        )
                && !factureRetourPerimeDTO.getDateBon().toLocalDate().isAfter(LocalDateTime.now().toLocalDate()), "date-bon-not-valid");

        factureRetourPerime.setRetourPerime(listeRetourPerimeRelative);
        factureRetourPerimeDTO.calcul(factureRetourPerimeDTO);
        List<TvaDTO> listTvas = paramAchatServiceClient.findTvas();
        factureRetourPerime.calcul(listTvas);

//        log.debug("resuuult {}", (factureRetourPerimeDTO.getMontantTTC().subtract(factureRetourPerime.getMontantTTC())).compareTo(new BigDecimal(maxMargeMontant)));
        Preconditions.checkBusinessLogique((factureRetourPerimeDTO.getMontantTTC().subtract(factureRetourPerime.getMontantTTC())).compareTo(new BigDecimal(maxMargeMontant)) <= 0, "marge-montant-hors-intervalle", maxMargeMontant);

        log.debug("dateBon to save : {}", factureRetourPerime.getDatbon());
        factureRetourPerime = factureRetourPerimeRepository.save(factureRetourPerime);

        fcptFrsPHService.addFcptFrsOnFactureRetourPerime(factureRetourPerime);

        FactureRetourPerimeDTO resultDTO = FactureRetourPerimeFactory.factureRetourPerimeToFactureRetourPerimeDTO(factureRetourPerime);
        return resultDTO;
    }

    /**
     * Update a factureretourperimeDTO.
     *
     * @param factureRetourPerimeDTO
     * @return the updated entity
     */
    public FactureRetourPerimeDTO update(FactureRetourPerimeDTO factureRetourPerimeDTO) {
        log.debug("Request to update FactureRetourPerime: {}", factureRetourPerimeDTO);
        FactureRetourPerime inBase = factureRetourPerimeRepository.findOne(factureRetourPerimeDTO.getNumbon());
        Preconditions.checkBusinessLogique(inBase != null, "factureretourperime.NotFound");

        FcptfrsPH fcptFrs = fcptFrsPHService.findFirstByNumBon(inBase.getNumbon());
        BigDecimal numOpr = new BigDecimal(fcptFrs.getNumOpr());
        Preconditions.checkBusinessLogique(facturebonreceptionRepository.findFactureReglee(numOpr).compareTo(BigDecimal.ZERO) == 0, "factureBonReception-deja-reglee");
        Preconditions.checkBusinessLogique(!inBase.getIntegrer(), "facture-deja-integre");

        FactureRetourPerime factureWithSameRef = factureRetourPerimeRepository.findByReferenceFournisseur(factureRetourPerimeDTO.getReferenceFournisseur());
        Preconditions.checkBusinessLogique(factureWithSameRef == null || factureWithSameRef.getReferenceFournisseur().equalsIgnoreCase(inBase.getReferenceFournisseur()), "reffrs-fournisseur-exists");

        factureRetourPerimeDTO.calcul(factureRetourPerimeDTO);

        Preconditions.checkBusinessLogique(factureRetourPerimeDTO.getMontantTTC().equals(inBase.getMontantTTC()), "montant-not-allow-to-update");

        Preconditions.checkBusinessLogique(!factureRetourPerimeDTO.getDateBon().isBefore(
                inBase.getRetourPerime().stream().sorted(Comparator.comparing(RetourPerime::getDatbon).reversed()).findFirst().get().getDatbon()
        )
                && !factureRetourPerimeDTO.getDateBon().toLocalDate().isAfter(LocalDateTime.now().toLocalDate()), "date-bon-not-valid");

        inBase.setDatbon(factureRetourPerimeDTO.getDateBon());
        inBase.setReferenceFournisseur(factureRetourPerimeDTO.getReferenceFournisseur());
        inBase.setDateFournisseur(factureRetourPerimeDTO.getDateFournisseur());

        Calendar cal = new GregorianCalendar();
        cal.setTime(Date.from(inBase.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        cal.set(Calendar.HOUR_OF_DAY, inBase.getHeuresys().getHour());
        cal.set(Calendar.MINUTE, inBase.getHeuresys().getMinute());
        cal.set(Calendar.SECOND, inBase.getHeuresys().getSecond());

        fcptFrs.setDateOpr(cal.getTime().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        FactureRetourPerimeDTO resultDTO = FactureRetourPerimeFactory.factureRetourPerimeToFactureRetourPerimeDTO(inBase);
        return resultDTO;
    }

    /**
     * Get one factureretourperimeDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(readOnly = true)
    public FactureRetourPerimeDTO findOne(String id) {
        log.debug("Request to get FactureRetourPerime: {}", id);
        FactureRetourPerime factureRetourPerime = factureRetourPerimeRepository.findOne(id);

        log.debug("Request to get FactureRetourPerime: {}", id);
        FactureRetourPerimeDTO factureRetourPerimeDTO = FactureRetourPerimeFactory.factureRetourPerimeToFactureRetourPerimeDTO(factureRetourPerime);
        if (factureRetourPerimeDTO != null) {
            FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(factureRetourPerimeDTO.getCodeFournisseur());
            Preconditions.checkBusinessLogique(fournisseur != null, "missing-supplier", factureRetourPerimeDTO.getCodeFournisseur());
            factureRetourPerimeDTO.setFournisseur(fournisseur);
            factureRetourPerimeDTO.setRetourPerimeRelative(factureRetourPerime.getRetourPerime().stream()
                    .map(item -> RetourPerimeFactory.lazyRetourPerimeToRetourPerimeDTO(item)).distinct().collect(Collectors.toList()));
        }
        return factureRetourPerimeDTO;
    }

    /**
     * Get one factureretourperime by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FactureRetourPerime findFactureRetourPerime(String id) {
        log.debug("Request to get FactureRetourPerime: {}", id);
        FactureRetourPerime factureRetourPerime = factureRetourPerimeRepository.findOne(id);
        return factureRetourPerime;
    }

    @Transactional(readOnly = true)
    public Collection<MvtstoRetourPerimeDTO> findDetailsFactureById(String id) {
        log.debug("Request to get Facture Retour Perime details: {}", id);
        FactureRetourPerime factureRetourPerime = factureRetourPerimeRepository.findOne(id);
        List<String> numBonRetourPerimes = factureRetourPerime.getRetourPerime().stream().map(x -> x.getNumbon()).collect(Collectors.toList());
        List<MvtstoRetourPerimeDTO> mvtstoRetourPerimeDTOs = mvtstoRetourPerimeService.getDetailsRetourPerime(numBonRetourPerimes);
        return mvtstoRetourPerimeDTOs;
    }

    @Transactional(readOnly = true)
    public byte[] editionFactureRetourPerime(String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print FactureBonReception : {}");
        List<CliniqueDto> cliniqueDto = paramServiceClient.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();

        FactureRetourPerimeDTO factureRetourPerimeDTO = this.findOne(id);

        List<String> listeNumBonsRetourPerime = factureRetourPerimeDTO.getRetourPerimeRelative().stream().map(x -> x.getNumaffiche()).collect(Collectors.toList());

        factureRetourPerimeDTO.setListeBonsRetourPerime(String.join(",", listeNumBonsRetourPerime));

        reportClientDoc.open("Reports/Facture_Retour_Perime" + local + ".rpt", 0);

        Collection<MvtstoRetourPerimeDTO> mvtstoRetourPerimeDTOs = this.findDetailsFactureById(id);

        log.debug("Details {}", mvtstoRetourPerimeDTOs);

        reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class, "clinique", "clinique");
        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(factureRetourPerimeDTO), FactureRetourPerimeDTO.class, "Entete", "Entete");
        reportClientDoc.getDatabaseController().setDataSource(mvtstoRetourPerimeDTOs, MvtstoRetourPerimeDTO.class, "Details", "Details");
        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(factureRetourPerimeDTO.getFournisseur()), FournisseurDTO.class, "Fournisseur", "Fournisseur");
        reportClientDoc.getSubreportController().getSubreport("BaseTVA").getDatabaseController().setDataSource(factureRetourPerimeDTO.getBaseTvaFactureRetourPerime(), BaseTvaFactureRetourPerimeDTO.class, "BaseTVA", "BaseTVA");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        BigDecimal baseTVA = BigDecimal.ZERO;
        BigDecimal mntTVA = BigDecimal.ZERO;

        for (BaseTvaFactureRetourPerimeDTO x : factureRetourPerimeDTO.getBaseTvaFactureRetourPerime()) {
            baseTVA = baseTVA.add(x.getBaseTva());
            mntTVA = mntTVA.add(x.getMontantTva());
        }

        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "baseTVA", baseTVA);
        paramController.setCurrentValue("", "mntTVA", mntTVA);
        if ("ar".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.AR(factureRetourPerimeDTO.getMontantTTC().toString(), "جنيه", "قرش"));
        } else if ("fr".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.FR(factureRetourPerimeDTO.getMontantTTC().toString(), "livres", "pence"));
        } else {
            paramController.setCurrentValue("", "montantLettre", Convert.EN(factureRetourPerimeDTO.getMontantTTC().toString(), "pounds", "penny"));
        }
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }

    /**
     * Get all the factureretourperimes.
     *
     * @param categDepot
     * @param fromDate
     * @param toDate
     * @param deleted
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public Collection<FactureRetourPerimeDTO> findAll(CategorieDepotEnum categDepot, LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted) {
        log.debug("Request to get All FactureRetourPerimes");
        QFactureRetourPerime _FactureRetourPerime = QFactureRetourPerime.factureRetourPerime;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(categDepot, () -> _FactureRetourPerime.categDepot.eq(categDepot))
                .optionalAnd(fromDate, () -> _FactureRetourPerime.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _FactureRetourPerime.datbon.loe(toDate))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _FactureRetourPerime.dateAnnule.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _FactureRetourPerime.dateAnnule.isNull());

        Collection<FactureRetourPerime> listFactureRetourPerime = (Collection<FactureRetourPerime>) factureRetourPerimeRepository.findAll(builder);

        List<String> fournisseursID = listFactureRetourPerime.stream().map(FactureRetourPerime::getCodeFournisseur).collect(toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(fournisseursID);

        return listFactureRetourPerime.stream().map(factureRP -> {
            FactureRetourPerimeDTO factureRetourPerimeDTO = FactureRetourPerimeFactory.factureRetourPerimeToFactureRetourPerimeDTO(factureRP);
            FournisseurDTO matchedFournisseur = fournisseurs.stream()
                    .filter(fournisseur -> fournisseur.getCode().equals(factureRP.getCodeFournisseur()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-supplier", new Throwable(factureRP.getCodeFournisseur())));

            factureRetourPerimeDTO.setFournisseur(matchedFournisseur);

            return factureRetourPerimeDTO;
        }).collect(toList());
    }

    /**
     * Delete factureretourperime by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete FactureRetourPerime: {}", id);
        FactureRetourPerime factureRetourPerime = factureRetourPerimeRepository.findOne(id);
        Preconditions.checkBusinessLogique(factureRetourPerime != null, "factureretourperime.NotFound");
        Preconditions.checkBusinessLogique(factureRetourPerime.getDateAnnule() == null, "facture-annule");

        FcptfrsPH fcptFrs = fcptFrsPHService.findFirstByNumBon(factureRetourPerime.getNumbon());
        BigDecimal numOpr = new BigDecimal(fcptFrs.getNumOpr());
        Preconditions.checkBusinessLogique(facturebonreceptionRepository.findFactureReglee(numOpr).compareTo(BigDecimal.ZERO) == 0, "factureBonReception-deja-reglee");
        Preconditions.checkBusinessLogique(!factureRetourPerime.getIntegrer(), "facture-deja-integre");

        fcptFrsPHService.deleteFcptfrsByNumBonDao(id, factureRetourPerime.getTypbon());
        factureRetourPerime.setUserAnnule(SecurityContextHolder.getContext().getAuthentication().getName());
        factureRetourPerime.setDateAnnule(LocalDateTime.now());
        Set<RetourPerime> listRetourPerime = factureRetourPerime.getRetourPerime()
                .stream()
                .map(RP -> {
                    RP.setFactureRetourPerime(null);
                    return RP;
                })
                .collect(Collectors.toSet());
        factureRetourPerimeRepository.save(factureRetourPerime);
    }
}
