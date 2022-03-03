package com.csys.pharmacie.inventaire.service;

import com.crystaldecisions.sdk.occa.report.application.ParameterFieldController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat;
import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.CliniqueDto;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.client.service.ParamServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Helper;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.inventaire.domain.DepStoHist;
import com.csys.pharmacie.inventaire.domain.QDepStoHist;
import com.csys.pharmacie.inventaire.dto.DepStoHistDTO;
import com.csys.pharmacie.inventaire.dto.InventaireDTO;
import com.csys.pharmacie.inventaire.factory.DepStoHistFactory;
import com.csys.pharmacie.inventaire.repository.DepStoHistRepository;
import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing DepStoHist.
 */
@Service
@Transactional
public class DepStoHistService {

    private final Logger log = LoggerFactory.getLogger(DepStoHistService.class);

    private final DepStoHistRepository depstohistRepository;
    private final InventaireService inventaireService;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final ParamServiceClient parametrageService;
    private final DepStoHistFactory depStoHistFactory;

    @Value("${lang.secondary}")
    private String langage;

    public DepStoHistService(DepStoHistRepository depstohistRepository,@Lazy InventaireService inventaireService, ParamAchatServiceClient paramAchatServiceClient, ParamServiceClient parametrageService, DepStoHistFactory depStoHistFactory) {
        this.depstohistRepository = depstohistRepository;
        this.inventaireService = inventaireService;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.parametrageService = parametrageService;
        this.depStoHistFactory = depStoHistFactory;
    }

 

    /**
     * Save a depstohistDTO.
     *
     * @param depstohistDTO
     * @return the persisted entity
     */
    public DepStoHistDTO save(DepStoHistDTO depstohistDTO) {
        log.debug("Request to save DepStoHist: {}", depstohistDTO);
        DepStoHist depstohist = DepStoHistFactory.depstohistDTOToDepStoHist(depstohistDTO);
        depstohist = depstohistRepository.save(depstohist);
        DepStoHistDTO resultDTO = DepStoHistFactory.depstohistToDepStoHistDTO(depstohist);
        return resultDTO;
    }

    /**
     * Update a depstohistDTO.
     *
     * @param depstohistDTO
     * @return the updated entity
     */
    public DepStoHistDTO update(DepStoHistDTO depstohistDTO) {
        log.debug("Request to update DepStoHist: {}", depstohistDTO);
        DepStoHist inBase = depstohistRepository.findOne(depstohistDTO.getNum());
        Preconditions.checkArgument(inBase != null, "depstohist.NotFound");
        DepStoHist depstohist = DepStoHistFactory.depstohistDTOToDepStoHist(depstohistDTO);
        depstohist = depstohistRepository.save(depstohist);
        DepStoHistDTO resultDTO = DepStoHistFactory.depstohistToDepStoHistDTO(depstohist);
        return resultDTO;
    }

    /**
     * Get one depstohistDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public DepStoHistDTO findOne(Integer id) {
        log.debug("Request to get DepStoHist: {}", id);
        DepStoHist depstohist = depstohistRepository.findOne(id);
        DepStoHistDTO dto = DepStoHistFactory.depstohistToDepStoHistDTO(depstohist);
        return dto;
    }

    /**
     * Get one depstohist by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public DepStoHist findDepStoHist(Integer id) {
        log.debug("Request to get DepStoHist: {}", id);
        DepStoHist depstohist = depstohistRepository.findOne(id);
        return depstohist;
    }

    /**
     * Get all the depstohists.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<DepStoHistDTO> findAll() {
        log.debug("Request to get All DepStoHists");
        List<DepStoHist> result = depstohistRepository.findAll();
        return DepStoHistFactory.depstohistToDepStoHistDTOs(result);
    }

    /**
     * Delete depstohist by id.
     *
     * @param id the id of the entity
     */
    public void delete(Integer id) {
        log.debug("Request to delete DepStoHist: {}", id);
        depstohistRepository.delete(id);
    }

    public List<DepStoHistDTO> findByInventaire_Code(Integer numInventaire) {

        List<DepStoHistDTO> histDepstoDTO = new ArrayList<>();
        List<DepStoHistDTO> listHistInv = DepStoHistFactory.depstohistToDepStoHistDTOs(depstohistRepository.findByInventaire_Code(numInventaire));
        com.csys.util.Preconditions.checkBusinessLogique(listHistInv.size() > 0, "inventaire.history.empty-history", "inventaire introuvable");
        InventaireDTO inventaire = inventaireService.findOne(numInventaire);
        com.csys.util.Preconditions.checkBusinessLogique(inventaire != null, "inventaire.missing", "inventaire introuvable");

        DepotDTO depot = paramAchatServiceClient.findDepotByCode(inventaire.getDepot());
        Map<Integer, List<DepStoHistDTO>> map
                = listHistInv.stream().filter(f -> f.getCodeCategorieArticle() != null).collect(Collectors.groupingBy(p -> p.getCodeCategorieArticle()));

        for (Map.Entry<Integer, List<DepStoHistDTO>> e : map.entrySet()) {
            if (e.getValue() != null) {
                e.getValue().forEach(p -> {
                    p.setCodeDepot(depot.getCode());
                    p.setCategDepot(inventaire.getCategorieDepot());
                    p.setDatInv(inventaire.getDateOuverture());
                    if (langage.equals(LocaleContextHolder.getLocale().getLanguage())) {
                        p.setDesignationDepot(depot.getDesignationSec());
                        p.setArticleDesignation(p.getArticledesignationAr());
                        p.setCategorieArticleDesignation(p.getCategoriearticledesignationAr());

                    } else {
                        p.setDesignationDepot(depot.getDesignation());
                        p.setArticleDesignation(p.getArticleDesignation());
                        p.setCategorieArticleDesignation(p.getCategorieArticleDesignation());

                    }

                });
                histDepstoDTO.addAll(e.getValue());
            }
        }

        return histDepstoDTO;
    }

    public byte[] editionEtatEcartAv(Integer numInventaire)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {

        List<DepStoHistDTO> listDepStoHistDTO = findByInventaire_Code(numInventaire);

        String language = LocaleContextHolder.getLocale().getLanguage();
        List<CliniqueDto> cliniqueDto = parametrageService.findClinique();
        cliniqueDto.get(0).setLogoClinique();
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        Locale loc = LocaleContextHolder.getLocale();
        String local = "_" + loc.getLanguage();

        reportClientDoc.open("Reports/etatInventaire.rpt", 0);

        reportClientDoc
                .getDatabaseController().setDataSource(listDepStoHistDTO, DepStoHistDTO.class,
                        "Commande", "Commande");
        reportClientDoc
                .getSubreportController().getSubreport("entete.rpt").getDatabaseController().setDataSource(cliniqueDto, CliniqueDto.class,
                "clinique", "clinique");
        ParameterFieldController paramController = reportClientDoc.getDataDefController().getParameterFieldController();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        paramController.setCurrentValue("", "user", user);
        paramController.setCurrentValue("", "titre", "مطبوعة جرد بتاريخ");
        ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) reportClientDoc.getPrintOutputController()
                .export(ReportExportFormat.PDF);
        reportClientDoc.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return Helper.read(byteArrayInputStream);
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findSoldeDepart(Integer codart, Integer coddep, Date date) {
        return depstohistRepository.findSoldeDepart(coddep, codart, date);
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findSoldeDepartByCodeCategorieArticle(Integer codeCategorieArticle, Integer coddep, Date date) {
        return depstohistRepository.findSoldeDepartByCodeCategorieArticle(coddep, codeCategorieArticle, date);
    }

    @Transactional(readOnly = true)
    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
        QDepStoHist _depStoHist = QDepStoHist.depStoHist;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_depStoHist.inventaire().categorieDepot.eq(categ))
                .optionalAnd(codart, () -> _depStoHist.codeArticle.eq(codart))
                .optionalAnd(coddep, () -> _depStoHist.inventaire().depot.eq(coddep))
                .optionalAnd(fromDate, () -> _depStoHist.inventaire().dateCloture.goe(Date.from(fromDate.atZone(ZoneId.systemDefault()).toInstant())))
                .optionalAnd(toDate, () -> _depStoHist.inventaire().dateCloture.loe(Date.from(toDate.atZone(ZoneId.systemDefault()).toInstant())))
                .and(_depStoHist.qte0.gt(BigDecimal.ZERO).or(_depStoHist.stkDep.gt(BigDecimal.ZERO)));
        List<DepStoHist> list = (List<DepStoHist>) depstohistRepository.findAll(builder);
        List<Mouvement> mouvements = depStoHistFactory.toMouvements(list, typeDate);
        List<Integer> codeUnites = new ArrayList<>();
        mouvements.forEach(x -> {
            x.getList().forEach(y -> {
                codeUnites.add(y.getCodeUnite());
            });
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        mouvements.forEach((mouvement) -> {
            mouvement.getList().forEach(y -> {
                UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(y.getCodeUnite())).findFirst().orElse(null);
                com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
                y.setDesignationUnite(unite.getDesignation());
            });

        });
        return mouvements;
    }

    @Transactional(readOnly = true)
    public List<DepStoHistDTO> findListDepstoHistByCodeInventaireAndDatPerBefore(Integer codeInventaire, LocalDate dateClotureInventaire) {
        List<DepStoHist> depStoHists = depstohistRepository.findByInventaire_CodeAndDatPerBefore(codeInventaire, dateClotureInventaire);
        log.debug("depStoHists {}:", depStoHists.toString());
          return DepStoHistFactory.depstohistToDepStoHistDTOs(depStoHists);
    }
}
