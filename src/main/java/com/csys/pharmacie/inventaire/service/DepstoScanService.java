package com.csys.pharmacie.inventaire.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.ArticlePHDTO;
import com.csys.pharmacie.achat.dto.CategorieArticleDTO;
import com.csys.pharmacie.achat.dto.CategorieDepotDTO;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.inventaire.domain.DepstoScan;
import com.csys.pharmacie.inventaire.domain.DetailInventaire;
import com.csys.pharmacie.inventaire.domain.Inventaire;
import com.csys.pharmacie.inventaire.dto.DepstoScanDTO;
import com.csys.pharmacie.inventaire.factory.DepstoScanFactory;
import com.csys.pharmacie.inventaire.repository.DepstoScanRepository;
import com.csys.pharmacie.inventaire.repository.DetailInventaireRepository;
import com.csys.pharmacie.inventaire.repository.InventaireRepository;
import com.csys.pharmacie.stock.dto.DepstoDTO;
import com.csys.util.Preconditions;
import com.csys.util.RestPreconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.Long;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
 * Service Implementation for managing DepstoScan.
 */
@Service
@Transactional
public class DepstoScanService {

    private final Logger log = LoggerFactory.getLogger(DepstoScanService.class);

    private final DepstoScanRepository depstoscanRepository;
    private final InventaireRepository inventaireRepository;
    private final DetailInventaireRepository detailInventaireRepository;
    private final ParamServiceClient parametrageService;
    @Autowired
    private ParamAchatServiceClient paramAchatServiceClient;
    @Value("${lang.secondary}")
    private String langage;

    public DepstoScanService(DepstoScanRepository depstoscanRepository, InventaireRepository inventaireRepository, DetailInventaireRepository detailInventaireRepository, ParamServiceClient parametrageService) {
        this.depstoscanRepository = depstoscanRepository;
        this.inventaireRepository = inventaireRepository;
        this.detailInventaireRepository = detailInventaireRepository;
        this.parametrageService = parametrageService;
    }

    /**
     * Save a depstoscanDTO.
     *
     * @param depstoscanDTO
     * @return the persisted entity
     */
    public DepstoScanDTO save(DepstoScanDTO depstoscanDTO) {
        log.debug("Request to save DepstoScan: {}", depstoscanDTO);
        DepstoScan depstoscan = DepstoScanFactory.depstoscanDTOToDepstoScan(depstoscanDTO);
        Integer articleID = depstoscan.getCodart();

        ArticleDTO articleDTO;
        if (CategorieDepotEnum.PH.equals(depstoscanDTO.getCategDepot())) {
            ArticlePHDTO articlePHDto = paramAchatServiceClient.articlePHFindByCode(articleID);
                articlePHDto.getArticleUnites()
                        .stream()
                        .filter(au -> depstoscanDTO.getUnite().equals(au.getCodeUnite()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit", new Throwable(depstoscanDTO.getUnite().toString())));
                articleDTO = (ArticleDTO)articlePHDto;
        } else {
            articleDTO = paramAchatServiceClient.articleFindByCode(articleID);
            Preconditions.checkBusinessLogique(articleDTO.getCodeUnite().equals(depstoscanDTO.getUnite()), "missing-unit", depstoscanDTO.getUnite().toString());
        }

        Preconditions.checkBusinessLogique(!Boolean.TRUE.equals(articleDTO.getStopped()), "item.stopped", articleDTO.getCodeSaisi());

        if (langage.equals(LocaleContextHolder.getLocale().getLanguage())) {
            depstoscan.setDesart(articleDTO.getDesignationSec());
            depstoscan.setDesartSec(articleDTO.getDesignation());
        } else {
            depstoscan.setDesart(articleDTO.getDesignation());
            depstoscan.setDesartSec(articleDTO.getDesignationSec());
        }
        depstoscan.setCategArt(articleDTO.getCategorieArticle().getCode());
        depstoscan.setCodeSaisi(articleDTO.getCodeSaisi());
        if (depstoscan.getDatPer().isBefore(LocalDate.now()) || depstoscan.getDatPer().isEqual(LocalDate.now())) {
            depstoscan.setDefectueux(true);
        }
        //Savoir et enregistrer l'inventaire pour cet article
        List<DetailInventaire> listInventaireOuvert = detailInventaireRepository.findByDetailInventairePK_CategorieArticleAndInventaire1_DepotAndInventaire1_CategorieDepotAndInventaire1_DateClotureIsNullAndInventaire1_DateAnnuleIsNull(depstoscan.getCategArt(), depstoscan.getCoddep(), depstoscan.getCategDepot());
        Preconditions.checkBusinessLogique(listInventaireOuvert != null && !listInventaireOuvert.isEmpty(), "inventaire.categorie.closed");
        Preconditions.checkBusinessLogique(listInventaireOuvert.size() == 1, "inventaire.can.not.be.open.more.than.once");

        Integer codeInventaire = listInventaireOuvert.stream().map(inv -> inv.getDetailInventairePK().getInventaire()).findFirst().get();
        depstoscan.setCodInv(codeInventaire);
        depstoscan = depstoscanRepository.save(depstoscan);
        DepstoScanDTO resultDTO = DepstoScanFactory.depstoscanToDepstoScanDTO(depstoscan);
        return resultDTO;
    }

    /**
     * Update a depstoscanDTO.
     *
     * @param depstoscanDTO
     * @return the updated entity
     */
    public DepstoScanDTO update(DepstoScanDTO depstoscanDTO) {
        log.debug("Request to update DepstoScan: {}", depstoscanDTO);
        DepstoScan inBase = depstoscanRepository.findOne(depstoscanDTO.getNum());
        Preconditions.checkBusinessLogique(inBase != null, "depstoscan.NotFound");
        DepstoScan depstoscan = DepstoScanFactory.depstoscanDTOToDepstoScan(depstoscanDTO);
        depstoscan = depstoscanRepository.save(depstoscan);
        DepstoScanDTO resultDTO = DepstoScanFactory.depstoscanToDepstoScanDTO(depstoscan);
        return resultDTO;
    }

    /**
     * Get one depstoscanDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public DepstoScanDTO findOne(Long id) {
        log.debug("Request to get DepstoScan: {}", id);
        DepstoScan depstoscan = depstoscanRepository.findOne(id);
        DepstoScanDTO dto = DepstoScanFactory.depstoscanToDepstoScanDTO(depstoscan);
        return dto;
    }

    /**
     * Get one depstoscan by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public DepstoScan findDepstoScan(Long id) {
        log.debug("Request to get DepstoScan: {}", id);
        DepstoScan depstoscan = depstoscanRepository.findOne(id);
        return depstoscan;
    }

    /**
     * Get all the depstoscans.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<DepstoScanDTO> findAll() {
        log.debug("Request to get All DepstoScans");
        List<DepstoScan> result = depstoscanRepository.findAll();
        return DepstoScanFactory.depstoscanToDepstoScanDTOs(result);
    }

    /**
     * Delete depstoscan by id.
     *
     * @param id the id of the entity
     */
    public List<DepstoScanDTO> findByCodartAndUniteAndCategDepotAndCoddepAndInventerier(Integer codArt, Integer unite, CategorieDepotEnum categDep, Integer coddep, boolean invnterier, String userName, Boolean importer) {
        log.debug("Request to get detailler DepstoScans");
        List<DepstoScan> listeDepstoScan = null;
        if (userName == null && codArt != null) { // Quantité deja saisie
            listeDepstoScan = depstoscanRepository.findByCodartAndUniteAndCategDepotAndCoddepAndInventerier(codArt, unite, categDep, coddep, invnterier);
        } else if (userName != null && codArt == null) { // Liste saisie par utilisateur
            listeDepstoScan = depstoscanRepository.findByCategDepotAndCoddepAndInventerierAndUserName(categDep, coddep, invnterier, userName);
        } else if (userName == null && codArt == null) {
            if (importer == null) {
                importer = false;
            }
            listeDepstoScan = depstoscanRepository.findByCategDepotAndCoddepAndInventerierAndImporter(categDep, coddep, invnterier, importer);
        }

        List<DepstoScanDTO> listeDepstoScanDTO = DepstoScanFactory.depstoscanToDepstoScanDTOs(listeDepstoScan);

        for (DepstoScanDTO listeDepstoScanB : listeDepstoScanDTO) {
            String desPermut;
            if (langage.equals(LocaleContextHolder.getLocale().getLanguage())) {
                desPermut = listeDepstoScanB.getDesart();
                listeDepstoScanB.setDesart(listeDepstoScanB.getDesartSec());
                listeDepstoScanB.setDesartSec(desPermut);
            }

        }
        Collections.sort(listeDepstoScanDTO, (d1, d2) -> d2.getNum().compareTo(d1.getNum()));
        return listeDepstoScanDTO;
    }

    public List<DepstoScanDTO> listeArticlePerime(Integer codeInventaire, Boolean ouvert) {
        log.debug("Request gor liste article perimer");
        List<DepstoScan> listeDepstoScan = null;

        //Controle Si l'inventaire et encore ouverte ou nn
        Inventaire inventaire = inventaireRepository.findOne(codeInventaire);
        com.csys.util.Preconditions.checkBusinessLogique(inventaire != null, "inventaire.depot-inexistant");
        if (ouvert == true) {
            com.csys.util.Preconditions.checkBusinessLogique(inventaire.getDateCloture() == null, "inventaire.depot-ferme");
        } else {
            com.csys.util.Preconditions.checkBusinessLogique(inventaire.getDateCloture() != null, "invt.init.inventaire-ouvert");
        }
        listeDepstoScan = depstoscanRepository.findByCodInvAndDefectueuxAndImporter(codeInventaire, true, true);
        List<DepstoScanDTO> listeDepstoScanDTO = DepstoScanFactory.depstoscanToDepstoScanDTOs(listeDepstoScan);

        for (DepstoScanDTO listeDepstoScanB : listeDepstoScanDTO) {
            String desPermut;
            if (langage.equals(LocaleContextHolder.getLocale().getLanguage())) {
                desPermut = listeDepstoScanB.getDesart();
                listeDepstoScanB.setDesart(listeDepstoScanB.getDesartSec());
                listeDepstoScanB.setDesartSec(desPermut);
            }

        }
        Collections.sort(listeDepstoScanDTO, (d1, d2) -> d2.getNum().compareTo(d1.getNum()));
        return listeDepstoScanDTO;
    }

    public void delete(List<Long> id) {

        if (!depstoscanRepository.nombreRowForDelete(id).equals(id.size())) {

            com.csys.util.Preconditions.checkBusinessLogique(false, "reception.add.missing-ca");
        }
        depstoscanRepository.deleteByCategDepotAndCoddepAndCodArt(id);
    }

    public Integer nombreRowForSelectDepot(CategorieDepotEnum categDepot, Integer coddep) {
        return depstoscanRepository.nombreRowForSelectDepot(categDepot, coddep);
    }

    public byte[] editionEtatAvImporter(CategorieDepotEnum categ_depot, Integer coddep, boolean inventerier, boolean importer, String type)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print operations PEC: {}");
        List<DepstoScanDTO> listDepstoScanDTO = this.findByCodartAndUniteAndCategDepotAndCoddepAndInventerier(null, null, categ_depot, coddep, false, null, false);
        com.csys.util.Preconditions.checkBusinessLogique(listDepstoScanDTO.size() > 0, "invt.init.empty-depot", "pas des articles");

        CategorieDepotDTO categorieDepotDTO = paramAchatServiceClient.findCategorieDepot(categ_depot.toString());
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(coddep);
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        if (!cliniqueDto.isEmpty()) {
            cliniqueDto.get(0).setLogoClinique();
        }

        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/listeDejaSaisie.rpt", 0);
        } else {
            reportClientDoc.open("Reports/listeDejaSaisieExel.rpt", 0);
        }
        reportClientDoc
                .getDatabaseController().setDataSource(listDepstoScanDTO, DepstoScanDTO.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "categDepot", categorieDepotDTO.getDesignation());
        paramController.setCurrentValue("", "depot", depot.getDesignation());
        paramController.setCurrentValue("", "titre", "الأصناف الغير محمـلة ");
        ByteArrayInputStream byteArrayInputStream = null;
        if (type.equalsIgnoreCase("P")) {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController()
                    .export(ReportExportFormat.PDF);
        } else {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
        }
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return Helper.read(byteArrayInputStream);
    }

    public byte[] editionEtatArtPerim(Integer codeInventaire, Boolean ouvert, String type)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("REST request to print operations PEC: {}");
        List<DepstoScanDTO> listDepstoScanDTO = this.listeArticlePerime(codeInventaire, ouvert);
        com.csys.util.Preconditions.checkBusinessLogique(listDepstoScanDTO.size() > 0, "invt.init.empty-depot", "pas des articles");
        Inventaire inventaire = inventaireRepository.findOne(codeInventaire);
        CategorieDepotDTO categorieDepotDTO = paramAchatServiceClient.findCategorieDepot(inventaire.getCategorieDepot().toString());
        DepotDTO depot = paramAchatServiceClient.findDepotByCode(inventaire.getDepot());
        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        if (!cliniqueDto.isEmpty()) {
            cliniqueDto.get(0).setLogoClinique();
        }

        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc.open("Reports/listeDejaSaisie.rpt", 0);
        } else {
            reportClientDoc.open("Reports/listeDejaSaisieExel.rpt", 0);
        }
        reportClientDoc
                .getDatabaseController().setDataSource(listDepstoScanDTO, DepstoScanDTO.class,
                        "Commande", "Commande");
        if (type.equalsIgnoreCase("P")) {
            reportClientDoc
                    .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                    "clinique", "clinique");
        }
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "categDepot", categorieDepotDTO.getDesignation());
        paramController.setCurrentValue("", "depot", depot.getDesignation());
        paramController.setCurrentValue("", "titre", "الأصناف المنتهية الصلحية ");
        ByteArrayInputStream byteArrayInputStream = null;
        if (type.equalsIgnoreCase("P")) {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController()
                    .export(ReportExportFormat.PDF);
        } else {
            byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController().export(ReportExportFormat.MSExcel);
        }
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return Helper.read(byteArrayInputStream);
    }

    @Transactional(readOnly = true)
    public List<DepstoScanDTO> findListDepstoScanByCodeInventaireAndDatPerBefore(Integer codeInventaire, LocalDate date) {
        List<DepstoScan> depstoScans = depstoscanRepository.findByCodInvAndDatPerBeforeAndImporter(codeInventaire, date, Boolean.FALSE);
        log.debug("depStoHists {}:", depstoScans.toString());
        return DepstoScanFactory.depstoscanToDepstoScanDTOs(depstoScans);
    }

    @Transactional(readOnly = true)
    public List<DepstoScanDTO> findByCodartInAndCategDepotAndInventerierAndImporter(List<Integer> codeArticle, CategorieDepotEnum categ) {
        List<DepstoScan> depstoScans = depstoscanRepository.findByCodartInAndCategDepotAndInventerierAndImporter(codeArticle, categ, Boolean.FALSE, Boolean.FALSE);
        log.debug("depstoScans {}:", depstoScans.toString());
        return DepstoScanFactory.depstoscanToDepstoScanDTOs(depstoScans);
    }

}
