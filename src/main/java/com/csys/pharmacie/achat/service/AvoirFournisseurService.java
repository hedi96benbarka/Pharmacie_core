package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import com.csys.pharmacie.achat.domain.MvtstoAF;
import com.csys.pharmacie.achat.domain.QAvoirFournisseur;
import com.csys.pharmacie.achat.dto.AvoirFournisseurDTO;
import com.csys.pharmacie.achat.dto.BaseTvaAvoirFournisseurDTO;
import com.csys.pharmacie.achat.dto.BonEditionDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.FactureBonReceptionDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoAFDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.factory.AvoirFournisseurFactory;
import com.csys.pharmacie.achat.factory.MvtstoAFFactory;
import com.csys.pharmacie.achat.repository.AvoirFournisseurRepository;
import com.csys.pharmacie.client.dto.SiteDTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Convert;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.parametrage.repository.ParamService;
import static com.csys.util.Preconditions.checkBusinessLogique;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing AvoirFournisseur.
 */
//@Service
//@Transactional
public abstract class AvoirFournisseurService {

    protected final Logger log = LoggerFactory.getLogger(AvoirFournisseurService.class);

    protected final AvoirFournisseurRepository avoirfournisseurRepository;
    protected final FactureBonReceptionService factureBonReceptionService;
    protected final ParamService paramService;
    protected final ParamAchatServiceClient paramAchatServiceClient;
    protected final MvtStoBAService mvtStoBAService;
    protected final FcptFrsPHService fcptFrsPHService;

    protected final MvtstoAFService mvtstoAFService;
    protected final ParamServiceClient parametrageService;
    protected final MvtstoAFFactory mvtstoAFFactory;
    protected final AvoirFournisseurFactory avoirFournisseurFactory;

    public AvoirFournisseurService(AvoirFournisseurRepository avoirfournisseurRepository, FactureBonReceptionService factureBonReceptionService, ParamService paramService, ParamAchatServiceClient paramAchatServiceClient, MvtStoBAService mvtStoBAService, FcptFrsPHService fcptFrsPHService, MvtstoAFService mvtstoAFService, ParamServiceClient parametrageService, MvtstoAFFactory mvtstoAFFactory, AvoirFournisseurFactory avoirFournisseurFactory) {
        this.avoirfournisseurRepository = avoirfournisseurRepository;
        this.factureBonReceptionService = factureBonReceptionService;
        this.paramService = paramService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.mvtStoBAService = mvtStoBAService;
        this.fcptFrsPHService = fcptFrsPHService;
        this.mvtstoAFService = mvtstoAFService;
        this.parametrageService = parametrageService;
        this.mvtstoAFFactory = mvtstoAFFactory;
        this.avoirFournisseurFactory = avoirFournisseurFactory;
    }

    public abstract AvoirFournisseurDTO save(AvoirFournisseurDTO avoirfournisseurDTO);

    public abstract AvoirFournisseurDTO update(AvoirFournisseurDTO avoirfournisseurDTO);

    /**
     * Get one avoirfournisseurDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(readOnly = true)
    public AvoirFournisseurDTO findOne(String id) {
        log.debug("Request to get AvoirFournisseur: {}", id);
        AvoirFournisseur avoirfournisseur = avoirfournisseurRepository.findOne(id);
        AvoirFournisseurDTO dto = avoirFournisseurFactory.avoirFournisseurToAvoirFournisseurDTO(avoirfournisseur);
        FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(avoirfournisseur.getCodeFournisseur());
        checkBusinessLogique(fournisseur != null, "missing-supplier", avoirfournisseur.getCodeFournisseur());
        dto.setFournisseur(fournisseur);
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(avoirfournisseur.getCoddep());
        dto.setDesignationDepot(depot.getDesignation());
        FactureBonReceptionDTO facture = factureBonReceptionService.findOne(avoirfournisseur.getNumFactureBonRecep());
        dto.setMntFactureBonRecep(facture.getMontant());
        dto.setDateFactureBonRecep(facture.getDatBon());
        dto.setNumFactureBonRecep(facture.getNumbon());

        return dto;
    }

    /**
     * Get one avoirfournisseur by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public AvoirFournisseur findAvoirFournisseur(String id) {
        log.debug("Request to get AvoirFournisseur: {}", id);
        AvoirFournisseur avoirfournisseur = avoirfournisseurRepository.findOne(id);
        return avoirfournisseur;
    }

    @Transactional(
            readOnly = true
    )
    public List<AvoirFournisseurDTO> findAll(LocalDateTime fromDate, LocalDateTime toDate, Boolean deleted, CategorieDepotEnum categDepot) {
        log.debug("Request to get All AvoirFournisseurs");

        QAvoirFournisseur _AvoirFournisseur = QAvoirFournisseur.avoirFournisseur;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(fromDate, () -> _AvoirFournisseur.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _AvoirFournisseur.datbon.loe(toDate))
                .booleanAnd(Objects.equals(deleted, Boolean.TRUE), () -> _AvoirFournisseur.userAnnule.isNotNull())
                .booleanAnd(Objects.equals(deleted, Boolean.FALSE), () -> _AvoirFournisseur.userAnnule.isNull())
                .optionalAnd(categDepot, () -> _AvoirFournisseur.categDepot.eq(categDepot));

        List<AvoirFournisseur> listeAvoirs = (List<AvoirFournisseur>) avoirfournisseurRepository.findAll(builder);

        Set<String> fournisseursID = listeAvoirs.stream().map(AvoirFournisseur::getCodeFournisseur).collect(Collectors.toSet());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(new ArrayList(fournisseursID));
        Collection<SiteDTO> listeSiteDTOs = parametrageService.findAllSites(Boolean.TRUE, "B");
        List<AvoirFournisseurDTO> results = listeAvoirs.stream().map(avoirFournisseur -> {
            AvoirFournisseurDTO avoirFournisseurDTO = avoirFournisseurFactory.avoirFournisseurToAvoirFournisseurDTOLazy(avoirFournisseur);
            FournisseurDTO matchedFournisseur = fournisseurs.stream()
                    .filter(fournisseur -> fournisseur.getCode().equals(avoirFournisseur.getCodeFournisseur()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-supplier", new Throwable(avoirFournisseur.getCodeFournisseur())));

            avoirFournisseurDTO.setDesignationFournisseur(matchedFournisseur.getDesignation());
            if (listeSiteDTOs != null) {// en cas de fallback parametrage ne pas bloquer le retour de ws mais simplement ne pas retourner les designations des sites 
                Optional<SiteDTO> matchingSiteDTO = listeSiteDTOs
                        .stream()
                        .filter(elt -> elt.getCode().equals(avoirFournisseur.getCodeSite())).findFirst();
                if (matchingSiteDTO.isPresent()) {
                    avoirFournisseurDTO.setDesignationSite(matchingSiteDTO.get().getDesignationAr());
                    avoirFournisseurDTO.setCodeSite(avoirFournisseur.getCodeSite());
                }
            }
            return avoirFournisseurDTO;
        }).collect(toList());
        return results;
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

        QAvoirFournisseur _AvoirFournisseur = QAvoirFournisseur.avoirFournisseur;
        WhereClauseBuilder builder = new WhereClauseBuilder().optionalAnd(codfrs, () -> _AvoirFournisseur.codeFournisseur.eq(codfrs))
                .optionalAnd(fromDate, () -> _AvoirFournisseur.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _AvoirFournisseur.datbon.loe(toDate))
                .optionalAnd(categ, () -> _AvoirFournisseur.categDepot.eq(categ));
        List<AvoirFournisseur> result = (List<AvoirFournisseur>) avoirfournisseurRepository.findAll(builder);

        List<BonEditionDTO> bonEditionDTOs = new ArrayList();
        if (!result.isEmpty()) {
            List<String> codesFrs = result.stream().map(item -> item.getCodeFournisseur()).distinct().collect(Collectors.toList());
            List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
            List<DepotDTO> depots = paramAchatServiceClient.findDepotsByCodes(result.stream().map(item -> item.getCoddep()).distinct().collect(Collectors.toList()));
            List<Integer> codeUnites = new ArrayList<>();
            result.forEach(y -> {
                y.getMvtstoAFList().forEach(mvt -> {
                    codeUnites.add(mvt.getUnite());
                });
            });
            List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
            result.forEach((avoirFournisseur) -> {
                BonEditionDTO bonEditionDTO = avoirFournisseurFactory.lazyAvoirfournisseurToBonEditionDTO(avoirFournisseur);
                Optional<FournisseurDTO> optFrs = fournisseurs.stream().filter(item -> item.getCode().equals(avoirFournisseur.getCodeFournisseur())).findFirst();
                checkBusinessLogique(optFrs.isPresent(), "returns.get-all.missing-frs", avoirFournisseur.getCodeFournisseur());
                bonEditionDTO.setFournisseur(optFrs.get());
                Optional<DepotDTO> depot = depots.stream().filter(item -> item.getCode().equals(avoirFournisseur.getCoddep())).findFirst();
                checkBusinessLogique(depot.isPresent(), "returns.get-all.missing-depot", Integer.toString(avoirFournisseur.getCoddep()));
                bonEditionDTO.setDesignationDepot(depot.get().getDesignation());
                bonEditionDTO.getDetails().forEach(x -> {
                    UniteDTO unite = listUnite.stream().filter(y -> y.getCode().equals(x.getCodeUnite())).findFirst().orElse(null);
                    checkBusinessLogique(unite != null, "missing-unity");
                    x.setDesignationunite(unite.getDesignation());
                });
                bonEditionDTOs.add(bonEditionDTO);
            });
        }
        return bonEditionDTOs;
    }

    /**
     * Delete avoirfournisseur by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete AvoirFournisseur: {}", id);
        avoirfournisseurRepository.delete(id);
    }

    public List<MvtstoAFDTO> findDetailsAvoirById(String numBon) {
        log.debug("Request to get details by numBon :{},id");

        List<MvtstoAF> mvtstos = mvtstoAFService.findByNumbon(numBon);

        List<MvtstoAFDTO> mvtstoAFDTOs = mvtstoAFFactory.mvtstoAFToMvtstoAFDTOs(mvtstos);

        Set<Integer> listCodeUnites = mvtstos.stream().map(MvtstoAF::getUnite).collect(Collectors.toSet());

        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(listCodeUnites);

        mvtstoAFDTOs.forEach(item -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(item.getUnite())).findFirst().orElse(null);
            checkBusinessLogique(unite != null, "missing-unity", item.getUnite().toString());

            item.setDesignationUnite(unite.getDesignation());
        });
        return mvtstoAFDTOs;

    }

    @Transactional(readOnly = true)
    public byte[] editionAvoirFournisseur(String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print Avoir Fournisseur : {}", id);
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        reportClientDoc.open("Reports/Avoir_Fournisseur" + local + ".rpt", 0);
        AvoirFournisseur result = avoirfournisseurRepository.findOne(id);
        AvoirFournisseurDTO avoirFournisseurDTO = avoirFournisseurFactory.avoirFournisseurToAvoirFournisseurDTO(result);

        FournisseurDTO fournisseur = paramAchatServiceClient.findFournisseurByCode(result.getCodeFournisseur());
        avoirFournisseurDTO.setDesignationFournisseur(fournisseur.getDesignation());
        log.debug("fournisseur est {}", avoirFournisseurDTO.getDesignationFournisseur());
        reportClientDoc.getDatabaseController().setDataSource(java.util.Arrays.asList(avoirFournisseurDTO), AvoirFournisseurDTO.class, "Entete", "Entete");
        reportClientDoc.getDatabaseController().setDataSource(avoirFournisseurDTO.getMvtstoAFList(), MvtstoAFDTO.class, "Detaille", "Detaille");
        reportClientDoc.getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class, "clinique", "clinique");
        reportClientDoc.getSubreportController().getSubreport("BaseTVA").getDatabaseController().setDataSource(avoirFournisseurDTO.getBaseTvaAvoirFournisseurList(), BaseTvaAvoirFournisseurDTO.class, "Commande", "Commande");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        BigDecimal baseTVA = BigDecimal.ZERO;
        BigDecimal mntTVASansGratuite = BigDecimal.ZERO;
        BigDecimal mntTVAGratuite = BigDecimal.ZERO;

        for (BaseTvaAvoirFournisseurDTO x : avoirFournisseurDTO.getBaseTvaAvoirFournisseurList()) {
            baseTVA = baseTVA.add(x.getBaseTva());
            mntTVASansGratuite = mntTVASansGratuite.add(x.getMontantTva());
            mntTVAGratuite = mntTVAGratuite.add(x.getMntTvaGrtauite());
        }
        BigDecimal mntTVA = mntTVAGratuite.add(mntTVASansGratuite);

        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "baseTVA", baseTVA);
        paramController.setCurrentValue("", "mntTVA", mntTVA);

        if ("ar".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.AR(avoirFournisseurDTO.getMontantTTC().toString(), "جنيه", "قرش"));
        } else if ("fr".equals(loc.getLanguage())) {
            paramController.setCurrentValue("", "montantLettre", Convert.FR(avoirFournisseurDTO.getMontantTTC().toString(), "livres", "pence"));
        } else {
            paramController.setCurrentValue("", "montantLettre", Convert.EN(avoirFournisseurDTO.getMontantTTC().toString(), "pounds", "penny"));
        }
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }
}
