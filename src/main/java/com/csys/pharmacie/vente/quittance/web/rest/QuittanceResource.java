package com.csys.pharmacie.vente.quittance.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.client.dto.ListeArticleNonMvtDTOWrapper;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.BaseAvoirQuittance;
import com.csys.pharmacie.vente.quittance.dto.AdmissionDemandePECDTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.ReceptionConstants;
import com.csys.pharmacie.helper.SatisfactionFactureEnum;
import com.csys.pharmacie.vente.quittance.dto.FactureDTO;
import com.csys.pharmacie.vente.quittance.dto.FactureWithFacturation;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDTO;
import com.csys.pharmacie.vente.quittance.dto.QuittanceAndQuittanceDepotFrs;
import com.csys.pharmacie.vente.quittance.dto.QuittanceDTO;
import com.csys.pharmacie.vente.quittance.service.FactureService;
import com.csys.pharmacie.vente.avoir.service.AvoirService;
import com.csys.pharmacie.vente.quittance.dto.OrdonnanceDTO;
import com.csys.pharmacie.vente.quittance.dto.FacturationPayementPharmacieDTO;
import com.csys.pharmacie.vente.quittance.dto.PrestationParDemandeDTO;
import com.csys.pharmacie.vente.quittance.dto.PrestationParDetailsAdmissionDTO;
import com.csys.pharmacie.vente.quittance.dto.QuittancePharmacieReglementDTO;
import com.csys.pharmacie.vente.quittance.dto.ReglementDTO;
import com.csys.pharmacie.vente.quittance.factory.FactureFactory;
import com.csys.pharmacie.vente.quittance.service.MvtstoService;
import com.csys.pharmacie.vente.quittance.service.ReceptionServiceClient;
import static com.csys.util.Preconditions.checkBusinessLogique;
import com.csys.util.RestPreconditions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Quittance.
 */
@RestController
@RequestMapping("/api")
public class QuittanceResource {

    private static final String ENTITY_NAME = "facture";

    private final FactureService factureService;

    private final MvtstoService mvtstoService;

    private final AvoirService avoirService;

    private final ReceptionServiceClient receptionServiceClient;
     
    private final ParamAchatServiceClient paramAchatServiceClient;

    private final Logger log = LoggerFactory.getLogger(FactureService.class);

    public QuittanceResource(FactureService factureService, MvtstoService mvtstoService, AvoirService avoirService, ReceptionServiceClient receptionServiceClient, ParamAchatServiceClient paramAchatServiceClient) {
        this.factureService = factureService;
        this.mvtstoService = mvtstoService;
        this.avoirService = avoirService;
        this.receptionServiceClient = receptionServiceClient;
        this.paramAchatServiceClient = paramAchatServiceClient;
    }

  

    /**
     * POST /quittances : Create a new facture.
     *
     * @param quittanceDTO
     * @param pharmacieExterne
     * @param withFacturation
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new facture, or with status 400 (Bad Request) if the facture has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/quittances")
    public ResponseEntity<FactureWithFacturation> createFacture(@Valid @RequestBody QuittanceDTO quittanceDTO,
            @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne,
            @RequestParam(name = "withFacturation", defaultValue = "true") Boolean withFacturation, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException, NoSuchAlgorithmException {
        log.debug("REST request to save Quittance : {}", quittanceDTO);
        if (quittanceDTO.getNumbon() != null) {
            bindingResult.addError(new FieldError("QuittanceDTO", "numBon", "POST method does not accepte " + ENTITY_NAME + " with numBon"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        List<FactureDTO> listFacturesDTO = factureService.save(quittanceDTO, pharmacieExterne);
        FactureWithFacturation result;
        if (withFacturation) {
            result = facturationQuittance(quittanceDTO.getReglementDTOs(), quittanceDTO.getNumdoss(), listFacturesDTO, Arrays.asList(quittanceDTO));
        } else {
            result = new FactureWithFacturation();
            result.setQuittances(listFacturesDTO);
        }
        ListeArticleNonMvtDTOWrapper articleNonMvtDTOWrapper = FactureFactory.factureDTOToListeArticleNonMvtDTOWrapper(listFacturesDTO);
        log.debug("articleNonMvtDTOWrapper to send: {}", articleNonMvtDTOWrapper);
               paramAchatServiceClient.updateListArticleNonMvt(articleNonMvtDTOWrapper); 
        return ResponseEntity.created(new URI("/api/factures/")).body(result);
    }

    /**
     * POST /quittances : Create a new facture.
     *
     * @param quittanceDTO
     * @param pharmacieExterne
     * @param withFacturation
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new facture, or with status 400 (Bad Request) if the facture has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/quittances/depot-fournisseur")
    public ResponseEntity<FactureWithFacturation> createFactureDepotFrs(@Valid @RequestBody QuittanceDTO quittanceDTO, @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne,
            @RequestParam(name = "withFacturation", defaultValue = "true") Boolean withFacturation, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException, NoSuchAlgorithmException {
        log.debug("REST request to save Quittance : {}", quittanceDTO);
        if (quittanceDTO.getNumbon() != null) {
            bindingResult.addError(new FieldError("QuittanceDTO", "numBon", "POST method does not accepte " + ENTITY_NAME + " with numBon"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        List<FactureDTO> listFacturesDTO = factureService.saveDepotFrs(quittanceDTO, pharmacieExterne);
        FactureWithFacturation result;
        if (withFacturation) {
            result = facturationQuittance(quittanceDTO.getReglementDTOs(), quittanceDTO.getNumdoss(), listFacturesDTO, Arrays.asList(quittanceDTO));
        } else {
            result = new FactureWithFacturation();
            result.setQuittances(listFacturesDTO);
        }
        log.debug("listFacturesDTO : {}", listFacturesDTO);
        ListeArticleNonMvtDTOWrapper articleNonMvtDTOWrapper = FactureFactory.factureDTOToListeArticleNonMvtDTOWrapper(listFacturesDTO);
        log.debug("articleNonMvtDTOWrapper to send: {}", articleNonMvtDTOWrapper);
               paramAchatServiceClient.updateListArticleNonMvt(articleNonMvtDTOWrapper);
        return ResponseEntity.created(new URI("/api/factures/depot-fournisseur")).body(result);
    }

    /**
     * POST /quittances : Create a new facture.
     *
     * @param quittanceDTO
     * @param pharmacieExterne
     * @param withFacturation
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new facture, or with status 400 (Bad Request) if the facture has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/quittances/depot-fournisseur-and-quittance")
    public ResponseEntity<FactureWithFacturation> createQuittanceAndQuittanceDepotFrs(@Valid @RequestBody QuittanceAndQuittanceDepotFrs quittanceDTO, @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne,
            @RequestParam(name = "withFacturation", defaultValue = "true") Boolean withFacturation, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException, NoSuchAlgorithmException {
        log.debug("REST request to save Quittance : {}", quittanceDTO);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        List<FactureDTO> listFacturesDTO = factureService.saveQuittanceAndQuittanceDepotFrs(quittanceDTO, pharmacieExterne);
        List<QuittanceDTO> listQuittanceDTO = new ArrayList<>();
        if (quittanceDTO.getQuittanceDepotFrs() != null) {
            listQuittanceDTO.add(quittanceDTO.getQuittanceDepotFrs());
        }
        if (quittanceDTO.getQuittance() != null) {
            listQuittanceDTO.add(quittanceDTO.getQuittance());
        }
        FactureWithFacturation result;
        if (withFacturation) {
            result = facturationQuittance(quittanceDTO.getReglementDTOs(), quittanceDTO.getQuittance().getNumdoss(), listFacturesDTO, listQuittanceDTO);
        } else {
            result = new FactureWithFacturation();
            result.setQuittances(listFacturesDTO);
        }
        ListeArticleNonMvtDTOWrapper articleNonMvtDTOWrapper = FactureFactory.factureDTOToListeArticleNonMvtDTOWrapper(listFacturesDTO);
        log.debug("articleNonMvtDTOWrapper to send: {}", articleNonMvtDTOWrapper);
               paramAchatServiceClient.updateListArticleNonMvt(articleNonMvtDTOWrapper); 
        return ResponseEntity.created(new URI("/api/factures/depot-fournisseur-and-quittance")).body(result);
    }

    /**
     * POST /quittances : Create a new facture.
     *
     * @param quittanceDTOs
     * @param pharmacieExterne
     * @param withFacturation
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new facture, or with status 400 (Bad Request) if the facture has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/quittances/depot-fournisseur-and-quittance/list")
    public ResponseEntity<FactureWithFacturation> createListQuittanceAndQuittanceDepotFrs(@Valid @RequestBody List<QuittanceAndQuittanceDepotFrs> quittanceDTOs, @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne,
            @RequestParam(name = "withFacturation", defaultValue = "true") Boolean withFacturation, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException, NoSuchAlgorithmException {
        log.debug("REST request to save Quittance : {}", quittanceDTOs);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        List<String> listNumdoss = quittanceDTOs.stream().map(quittanceDTO -> quittanceDTO.getQuittance().getNumdoss()).distinct().collect(Collectors.toList());
        listNumdoss.addAll(quittanceDTOs.stream().map(quittanceDTO -> quittanceDTO.getQuittance().getNumdoss()).distinct().collect(Collectors.toList()));
        listNumdoss = listNumdoss.stream().distinct().collect(Collectors.toList());
        checkBusinessLogique(listNumdoss.size() == 1, "quittance.unique.dossier");
        String numdoss = listNumdoss.get(0);
        List<FactureDTO> listFactureDTO = factureService.saveListQuittanceAndQuittanceDepotFrs(quittanceDTOs, pharmacieExterne);
        List<QuittanceDTO> listQuittanceDTO = new ArrayList<>();
        for (QuittanceAndQuittanceDepotFrs quittanceDTO : quittanceDTOs) {
            if (quittanceDTO.getQuittanceDepotFrs() != null) {
                listQuittanceDTO.add(quittanceDTO.getQuittanceDepotFrs());
            }
            if (quittanceDTO.getQuittance() != null) {
                listQuittanceDTO.add(quittanceDTO.getQuittance());
            }
        }
        FactureWithFacturation result;
        if (withFacturation) {
            result = facturationQuittance(null, numdoss, listFactureDTO, listQuittanceDTO);
        } else {
            result = new FactureWithFacturation();
            result.setQuittances(listFactureDTO);
        }
        ListeArticleNonMvtDTOWrapper facturePRDTOWrapper = FactureFactory.factureDTOToListeArticleNonMvtDTOWrapper(listFactureDTO);
        log.debug("facturePRDTOWrapper to send: {}", facturePRDTOWrapper);
               paramAchatServiceClient.updateListArticleNonMvt(facturePRDTOWrapper); 
        return ResponseEntity.created(new URI("/api/factures/depot-fournisseur-and-quittance")).body(result);
    }

    /**
     * POST /quittances : Create a new facture.
     *
     * @param quittanceDTO
     * @param codePriceList
     * @param codeListCouverture
     * @param codeNatureAdmission
     * @param codeConvention
     * @param pharmacieExterne
     * @param codeSociete
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new facture, or with status 400 (Bad Request) if the facture has already
     * an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/quittances/calcul-montant")
    public ResponseEntity<QuittanceDTO> calculQuittances(@Valid @RequestBody QuittanceDTO quittanceDTO,
            @RequestParam(name = "codePriceList") Integer codePriceList,
            @RequestParam(name = "codeListCouverture", required = false) Integer codeListCouverture,
            @RequestParam(name = "codeNatureAdmission") Integer codeNatureAdmission,
            @RequestParam(name = "codeConvention", required = false) Integer codeConvention,
            @RequestParam(name = "pharmacieExterne", required = true, defaultValue = "false") Boolean pharmacieExterne,
            @RequestParam(name = "codeSociete", required = false) Integer codeSociete,
            BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save Quittance : {}", quittanceDTO);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        QuittanceDTO result = factureService.calculQuittances(quittanceDTO, codePriceList, codeListCouverture, codeNatureAdmission, codeConvention, codeSociete, pharmacieExterne);
        return ResponseEntity.created(new URI("/api/factures/calcul-montant")).body(result);
    }

    /**
     * GET /quittances/{id} : get the "id" facture.
     *
     * @param id the id of the facture to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of facture,
     * or with status 404 (Not Found)
     */
    @GetMapping("/quittances/{id}")
    public ResponseEntity<FactureDTO> getFacture(@PathVariable String id) {
        log.debug("Request to get Quittance: {}", id);
        FactureDTO dto = factureService.findOne(id);
        RestPreconditions.checkFound(dto, "quittance.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /quittances/{id} : get the "id" facture.
     *
     * @param codeDepot
     * @param categ
     * @return the ResponseEntity with status 200 (OK) and with body of facture,
     * or with status 404 (Not Found)
     */
    @GetMapping("/quittances/client")
    public ResponseEntity<List<AdmissionDemandePECDTO>> getFacture(@RequestParam Integer codeDepot, CategorieDepotEnum categ) {
        log.debug("Request to get Client by code dépôt: {}", codeDepot);
        List<AdmissionDemandePECDTO> dto = mvtstoService.findDossierByCoddep(codeDepot, categ);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/quittances/{numBon}/details")
    public @ResponseBody
    List<MvtstoDTO> findDetailsTransfert(@PathVariable("numBon") String numBon) {
        List<MvtstoDTO> details = mvtstoService.findDetails(numBon);
        return details;
    }

    /**
     * GET /quittances : get all the quittances.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param deleted
     * @param codeAdmission
     * @param satisfactions
     * @param depotID
     * @param etatPatient
     * @param search
     * @return the ResponseEntity with status 200 (OK) and the list of
     * quittances in body
     */
    @GetMapping("/quittances")
    public List<FactureDTO> getAllFactures(
            @RequestParam(name = "categ-depots", required = false) List<CategorieDepotEnum> categ,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "deleted", required = false) Boolean deleted,
            @RequestParam(name = "code-admission", required = false) String codeAdmission,
            @RequestParam(name = "satisfactions", required = false) SatisfactionFactureEnum satisfactions,
            @RequestParam(name = "depot-id", required = false) Integer depotID,
            @RequestParam(name = "etat-patient", required = false) List<ReceptionConstants> etatPatient,
            @RequestParam(name = "search", required = false) String search) {
        log.debug("Request to get all  quittances : {}");
        return factureService.findAll(categ, fromDate, toDate, codeAdmission, deleted, satisfactions, depotID, etatPatient, search);
    }

    /**
     * DELETE /quittances/{id} : delete the "id" quittance.
     *
     * @param id the id of the quittance to delete
     * @param codeMotifSuppression
     * @param withFacturation
     * @param isPanier
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quittances/{id}")
    public ResponseEntity<FactureDTO> deleteFacture(@PathVariable String id,
            @RequestParam(name = "code-motif-suppression") Integer codeMotifSuppression,
            @RequestParam(name = "withFacturation", defaultValue = "true") Boolean withFacturation,
            @RequestParam(name = "isPanier", defaultValue = "false") Boolean isPanier) {
        log.debug("Request to delete quittance: {}", id);
        FactureDTO dto = factureService.delete(id, codeMotifSuppression, withFacturation, isPanier);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * DELETE /quittances/ : delete the "ids" quittances.
     *
     * @param quittancesIDs
     * @param codeMotifSuppression
     * @param withFacturation
     * @param isPanier
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quittances/")
    public ResponseEntity<List<FactureDTO>> deleteFactures(@ApiParam("list of quittances ids") @RequestBody List<String> quittancesIDs,
            @RequestParam(name = "code-motif-suppression", required = false) Integer codeMotifSuppression,
            @RequestParam(name = "withFacturation", defaultValue = "true") Boolean withFacturation,
            @RequestParam(name = "isPanier", defaultValue = "false") Boolean isPanier) {
        log.debug("Request to delete quittance: {}", quittancesIDs);
        List<FactureDTO> dto = factureService.deletes(quittancesIDs, codeMotifSuppression, withFacturation, isPanier);
        return ResponseEntity.ok().body(dto);
    }

    @ApiOperation(value = "Search details of the given quittances grouped by articles and unity.")
    @GetMapping("/quittances/avoir/details")
    public List<MvtstoDTO> searchDetailsOfAvoir(@ApiParam("Only PH or UU could be choosen. If EC is choosed then a response with status 409 will be returned") @RequestParam("categ-depot") CategorieDepotEnum categDepot,
            @ApiParam("Numdoss") @RequestParam String Numdoss,
            @ApiParam("code-depot") @RequestParam Integer coddep,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "grouped", defaultValue = "true") Boolean grouped) {
        log.debug("REST request to search details of quittances  ");
        return mvtstoService.findDetailsAvoir(categDepot, Numdoss, coddep, search, grouped);

    }

    @ApiOperation(value = "Search details of the given quittances grouped by articles and unity.")
    @GetMapping("/quittances/details")
    public List<MvtstoDTO> findDetailsQuittance(
            @ApiParam("Only PH or UU could be choosen. If EC is choosed then a response with status 409 will be returned") @RequestParam(name = "categ-depot") List<CategorieDepotEnum> categDepot,
            @RequestParam(name = "Numdoss", required = false) String Numdoss,
            @RequestParam(name = "code-depot", required = false) Integer coddep,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "isPanier", defaultValue = "true,false") List<Boolean> isPanier,
            @RequestParam(name = "deleted", defaultValue = "false") Boolean deleted) {
        log.debug("REST request to search details of quittances  ");
        return mvtstoService.findDetailsQuittance(categDepot, Numdoss, coddep, fromDate, toDate, isPanier, deleted);

    }

    @ApiOperation(value = "Search details of the given quittances.")
    @PostMapping("/quittances/searches/mouvements")
    public List<MvtstoDTO> searchMouvements(@ApiParam("list of quittances ids") @RequestBody String[] quittancesIDs, @ApiParam("Only PH or UU could be choosen. If EC is choosed then a response with status 409 will be returned") @RequestParam(name = "categ-depot", required = false) List<CategorieDepotEnum> categDepot,
            @RequestParam(name = "codeAdmission", required = false) String codeAdmission) {
        log.debug("REST request to search details of quittances  ");
        return mvtstoService.searchMouvements(quittancesIDs, categDepot, codeAdmission);

    }

    @ApiOperation(value = "Search code article of the given quittances.")
    @PostMapping("/quittances/searches/code-article")
    public List<Integer> searchCodeArticle(@ApiParam("list of quittances ids") @RequestBody List<String> quittancesIDs) {
        log.debug("REST request to search details of quittances  ");
        return mvtstoService.findCodartByMvtstoPK_NumbonIn(quittancesIDs);

    }

    @ApiOperation(value = "Search details of the given quittances and avoirs.")
    @PostMapping("/quittancesAndAvoirs/searches/mouvements")
    public List<MvtstoDTO> searchMouvementsQuittancesAndAvoirs(@ApiParam("list of quittances and  avoirs ids") @RequestBody String[] quittancesIDs, @ApiParam("Only PH or UU could be choosen. If EC is choosed then a response with status 409 will be returned") @RequestParam(name = "categ-depot", required = false) List<CategorieDepotEnum> categDepot,
            @RequestParam(name = "codeAdmission", required = false) String codeAdmission) {
        log.debug("REST request to search details of quittances  ");
        List<MvtstoDTO> resultat = avoirService.searchMouvements(quittancesIDs, categDepot, codeAdmission);
        resultat.addAll(mvtstoService.searchMouvements(quittancesIDs, categDepot, codeAdmission));
        return resultat;

    }

    @ApiOperation(value = "search quittances and avoirs  by date and coddep .")
    @GetMapping("/quittancesAndAvoirs")
    public List<FactureDTO> findQuittancesAndAvoirs(
            @ApiParam("code-depot") @RequestParam Integer coddep,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "etat-patient", required = false) List<ReceptionConstants> etatPatient,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "withClient", required = false, defaultValue = "false") Boolean withClient) {
        log.debug("REST request to search details of quittances  ");
        List<FactureDTO> resultat = avoirService.findAvoirs(fromDate, toDate, coddep, etatPatient, search, withClient);
        resultat.addAll(factureService.findQuittances(fromDate, toDate, coddep, etatPatient, search, withClient));
        return resultat;

    }

    @ApiOperation(value = "Search numBons quittances and avoirs by num doss.")
    @GetMapping("/quittancesAndAvoirs/numBons")
    public List<String> findNumBonsQuittancesAndAvoirsByNumDoss(
            @RequestParam(name = "numDoss", required = true) String numDoss
    ) {
        log.debug("REST request to search num bons of quittances and avoirs by num doss");
        List<String> nomBons = avoirService.findListNumBonsByNumDoss(numDoss);
        nomBons.addAll(factureService.findListNumBonsByNumDoss(numDoss));
        return nomBons;
    }

    @ApiOperation(value = "Search  the given quittances.")
    @PostMapping("/quittances/searches/")
    public List<FactureDTO> searches(@ApiParam("list of quittances ids") @RequestBody String[] quittancesIDs, @ApiParam("Only PH or UU could be choosen. If EC is choosed then a response with status 409 will be returned") @RequestParam(name = "categ-depot", required = false) List<CategorieDepotEnum> categDepot,
            @RequestParam(name = "lazy", required = false, defaultValue = "false") Boolean lazy) {
        log.debug("REST request to search details of quittances  ");
        return factureService.searches(quittancesIDs, categDepot, lazy);

    }

    @ApiOperation(value = "update organisme pec the given quittances.")
    @PostMapping("/quittances/updateOrganismePEC")
    public List<BaseAvoirQuittance> updateOrganismePEC(@ApiParam("list of quittances ids") @RequestBody List<String> codes,
            @RequestParam(name = "codePriceList") Integer codePriceList,
            @RequestParam(name = "codeListCouverture", required = false) Integer codeListCouverture,
            @RequestParam(name = "codeNatureAdmission") Integer codeNatureAdmission,
            @RequestParam(name = "codeConvention", required = false) Integer codeConvention,
            @RequestParam(name = "numdoss") String numdoss,
            @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne,
            @RequestParam(name = "codeSociete", required = false) Integer codeSociete) {
        return factureService.updateOrganismePEC(codes, codePriceList, codeListCouverture, codeNatureAdmission, codeConvention, numdoss, codeSociete, pharmacieExterne);

    }

    @ApiOperation(value = "Search details of the given quittances grouped by articles and using only principale qte.")
    @PostMapping("/quittances/searches/details")
    public Collection<MvtstoDTO> searchDetailsOfDemandes(
            @ApiParam("list of quittances ids") 
            @RequestBody List<String> quittancesIDs, 
            @ApiParam("Only PH or UU could be choosen. If EC is choosed then a response with status 409 will be returned") 
            @RequestParam("categ-depot") CategorieDepotEnum categDepot,
            @ApiParam(value = "If \"true\" the api will return only the current user's articles")
            @RequestParam(name = "only-my-articles", required = false) Boolean onlyMyArticles) {
        log.debug("REST request to search details of quittances  ");
        return mvtstoService.searchDetailsOfQuittances(quittancesIDs, categDepot, onlyMyArticles);

    }

    @ApiOperation(value = "update partie patient and taux couverture the given quittances.")
    @PutMapping("/quittances/updatePartiePatient")
    public Boolean updatePartiePatient(@ApiParam("list of quittances (numbon, partiePatient, partiePEC)") @RequestBody List<QuittanceDTO> quittanceDTOs) {
        return factureService.updatePartiePatient(quittanceDTOs);

    }

    public FactureWithFacturation facturationQuittance(List<ReglementDTO> reglementDTOs, String numdoss, List<FactureDTO> listFactureDTO, List<QuittanceDTO> listQuittanceDTO) {
        try {
            //traitement facturation (module facturation)
            QuittancePharmacieReglementDTO quittancePharmacieReglementDTO = new QuittancePharmacieReglementDTO();
            quittancePharmacieReglementDTO.setQuittancePharmacieDTOs(listFactureDTO);
            quittancePharmacieReglementDTO.setReglementDTOs(reglementDTOs);
            FacturationPayementPharmacieDTO detailsAdmissions = receptionServiceClient.facturationQuittanceAvoir(quittancePharmacieReglementDTO, numdoss);
            checkBusinessLogique(detailsAdmissions != null, "error-facturation");
            FactureWithFacturation factureWithFacturation = new FactureWithFacturation();
            factureWithFacturation.setFacture(detailsAdmissions);
            factureWithFacturation.setQuittances(listFactureDTO);
            return factureWithFacturation;
        } catch (Exception e) {
            List<String> numQuittances = listFactureDTO.stream().map(quittanceDTO -> quittanceDTO.getNumbon()).distinct().collect(Collectors.toList());
            factureService.deletesPermanent(numQuittances, null, Boolean.TRUE, listQuittanceDTO);
            checkBusinessLogique(false, "error-facturation");
            return null;
        }
    }

    /**
     * GET /quittances/{id} : get the "id" Avoir.
     *
     * @param numBon
     * @return the ResponseEntity with status 200 (OK) and with body of Avoir,
     * or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/quittances/edition/{numBon}")
    public ResponseEntity<byte[]> getEdition(@PathVariable String numBon) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get quittance: {}", numBon);
        byte[] bytePdf = factureService.edition(numBon);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    @ApiOperation(value = "update organisme pec the given quittances.")
    @PostMapping("/quittances/recalculTauxCouverture")
    public List<BaseAvoirQuittance> recalculTauxCouverture(@ApiParam("list of quittances ids") @RequestBody List<String> codes,
            @RequestParam(name = "codePriceList") Integer codePriceList,
            @RequestParam(name = "codeListCouverture", required = false) Integer codeListCouverture,
            @RequestParam(name = "codeNatureAdmission") Integer codeNatureAdmission,
            @RequestParam(name = "codeConvention", required = false) Integer codeConvention,
            @RequestParam(name = "numdoss") String numdoss,
            @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne,
            @RequestParam(name = "codeSociete", required = false) Integer codeSociete) {
        return factureService.recalculTauxCouverture(codes, codePriceList, codeListCouverture, codeNatureAdmission, codeConvention, numdoss, codeSociete, pharmacieExterne);

    }

    @ApiOperation(value = "Search details of the given quittances.")
    @PostMapping("/quittances/pharmacie-externe")
    public List<MvtstoDTO> findByCoddepAndNumdossAndDatbon(@ApiParam("list of patients ids") @RequestBody List<String> numdoss,
            @RequestParam(name = "code-depot") Integer coddep,
            @RequestParam(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate) {
        log.debug("REST request to search details of quittances  ");
        return mvtstoService.findByCoddepAndNumdossAndDatbon(coddep, numdoss, fromDate, toDate);

    }

    /**
     * DELETE /quittances: delete quittances.
     *
     * @param prestationParDemandeDTOs
     * @param numdoss
     * @param codeMotifSuppression
     * @param withFacturation
     * @param isPanier
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quittances/deletesByCodeDemande/{numdoss}")
    public ResponseEntity<List<FactureDTO>> deletesByCodeDemande(@PathVariable String numdoss, @RequestBody List<PrestationParDemandeDTO> prestationParDemandeDTOs,
            @RequestParam(name = "code-motif-suppression", required = false) Integer codeMotifSuppression,
            @RequestParam(name = "withFacturation", defaultValue = "true") Boolean withFacturation,
            @RequestParam(name = "isPanier", defaultValue = "false") Boolean isPanier) {
        log.debug("Request to delete quittance: {}", numdoss);
        List<FactureDTO> dtos = factureService.deletesByCodeDemande(numdoss, prestationParDemandeDTOs, codeMotifSuppression, withFacturation, isPanier);
        return ResponseEntity.ok().body(dtos);
    }

    /**
     * DELETE /quittances: delete quittances.
     *
     * @param numdoss
     * @param prestationParDetailsAdmissionDTOs
     * @param codeMotifSuppression
     * @param withFacturation
     * @param isPanier
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quittances/deletesByCodeDetailsAdmission/{numdoss}")
    public ResponseEntity<List<FactureDTO>> deletesByCodeDetailsAdmission(@PathVariable String numdoss, @RequestBody List<PrestationParDetailsAdmissionDTO> prestationParDetailsAdmissionDTOs,
            @RequestParam(name = "code-motif-suppression", required = false) Integer codeMotifSuppression,
            @RequestParam(name = "withFacturation", defaultValue = "true") Boolean withFacturation,
            @RequestParam(name = "isPanier", defaultValue = "false") Boolean isPanier) {
        log.debug("Request to delete quittance: {}", numdoss);
        List<FactureDTO> dtos = factureService.deletesByCodeDetailsAdmission(numdoss, prestationParDetailsAdmissionDTOs, codeMotifSuppression, withFacturation, isPanier);
        return ResponseEntity.ok().body(dtos);
    }

    @ApiOperation(value = "Search quittances and avoirs  by numbonComplementaire.")
    @GetMapping("/quittancesAndAvoirs/findByNumbonComplementaire")
    public List<FactureDTO> findByNumbonComplementaire(String numbonComplementaire) {
        log.debug("REST request to search details of quittances By numbonComplementaire ");
        List<FactureDTO> resultat = factureService.findByNumbonComplementaire(numbonComplementaire);
        resultat.addAll(avoirService.findByNumbonComplementaire(numbonComplementaire));
        return resultat;
    }

    @ApiOperation(value = "Search quittances by ordonnances id.")
    @PostMapping("/quittances/findByIdOrdonnanceIn")
    public List<FactureDTO> findByIdOrdonnanceIn(@ApiParam("list of ordonnances ids") @RequestBody Long[] idOrdonnances,
            @RequestParam(name = "lazy", required = false, defaultValue = "false") Boolean lazy,
            @RequestParam(name = "withClient", required = false, defaultValue = "false") Boolean withClient) {
        log.debug("REST request to search quittances By  ordonnances id ");
        List<FactureDTO> resultat = factureService.findByIdOrdonnanceIn(idOrdonnances, lazy, withClient);
        return resultat;
    }

    @ApiOperation(value = "Search the given quittances and avoirs.")
    @PostMapping("/quittancesAndAvoirs/searches")
    public List<FactureDTO> searchQuittancesAndAvoirs(
            @ApiParam("list of quittances and  avoirs ids") @RequestBody String[] quittancesIDs,
            @ApiParam("Only PH or UU could be choosen. If EC is choosed then a response with status 409 will be returned") @RequestParam(name = "categ-depot", required = false) List<CategorieDepotEnum> categDepot,
            @RequestParam(name = "lazy", required = false, defaultValue = "false") Boolean lazy,
            BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to search the given quittances and avoirs");
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        List<FactureDTO> resultat = avoirService.findAvoirsByNumbonIn(quittancesIDs, categDepot);
        resultat.addAll(factureService.searches(quittancesIDs, categDepot, lazy));
        return resultat;
    }
    
    @ApiOperation(value = "check whether ordonnances are factured or not.")
    @PostMapping("/quittances/checkFacturationOrdonnance")
    public List<OrdonnanceDTO> checkFacturationOrdonnance(@ApiParam("list of ordonnances") @RequestBody List<OrdonnanceDTO> ordonnanceDTOs) {
        log.debug("REST request to check Facturation of ordonnances");
        List<OrdonnanceDTO> resultat = factureService.checkFacturationOrdonnance(ordonnanceDTOs);
        return resultat;
    }
}
