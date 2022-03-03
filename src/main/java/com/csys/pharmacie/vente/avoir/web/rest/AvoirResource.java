/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.avoir.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.csys.pharmacie.vente.avoir.service.AvoirService;
//import com.csys.pharmacie.vente.quittance.service.QuittanceService;

import com.csys.pharmacie.helper.ReceptionConstants;
import com.csys.pharmacie.vente.avoir.dto.Avoir;
import com.csys.pharmacie.vente.avoir.dto.AvoirWithReglement;
import com.csys.pharmacie.vente.avoir.dto.FactureAVDTO;
import com.csys.pharmacie.vente.avoir.dto.MvtStoAVDTO;
import com.csys.pharmacie.vente.avoir.dto.MvtstomvtstoAVDTO;
import com.csys.pharmacie.vente.quittance.dto.FacturationPayementPharmacieDTO;
import com.csys.pharmacie.vente.quittance.dto.FactureWithFacturation;
import com.csys.pharmacie.vente.quittance.dto.QuittancePharmacieReglementDTO;
import com.csys.pharmacie.vente.quittance.dto.ReglementDTO;
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
import java.util.Collection;
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
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Farouk
 */
@RestController
@RequestMapping("/api")
public class AvoirResource {

    private static final String ENTITY_NAME = "factureav";

    private final Logger log = LoggerFactory.getLogger(AvoirService.class);

    private final AvoirService avoirService;
    private final ReceptionServiceClient receptionServiceClient;

    public AvoirResource(AvoirService avoirService, ReceptionServiceClient receptionServiceClient) {
        this.avoirService = avoirService;
        this.receptionServiceClient = receptionServiceClient;
    }

    /**
     * POST /Avoir : Create a new factureav.
     *
     * @param avoir
     * @param pharmacieExterne
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new factureav, or with status 400 (Bad Request) if the factureav has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/Avoir")
    public ResponseEntity<FactureWithFacturation> createFactureAV(@Valid @RequestBody Avoir avoir,
            @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException, NoSuchAlgorithmException {
        log.debug("REST request to save FactureAV : {}", avoir);
        if (avoir.getNumbon() != null) {
            bindingResult.addError(new FieldError("avoir", "numbon", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        List<FactureAVDTO> factureAVDTOs = avoirService.save(avoir, pharmacieExterne);
        FactureWithFacturation result = facturationAvoir(avoir.getReglementDTOs(), avoir.getNumdoss(), factureAVDTOs, null);
        return ResponseEntity.created(new URI("/api/Avoir")).body(result);
    }

    /**
     * POST /Avoir : Create a new factureav.
     *
     * @param numQuittances
     * @param codeAdmission
     * @param pharmacieExterne
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new factureav, or with status 400 (Bad Request) if the factureav has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/AvoirTotal")
    public ResponseEntity<FactureWithFacturation> createFactureAV(@Valid @RequestBody List<String> numQuittances, @RequestParam(name = "code-admission", required = true) String codeAdmission,
            @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException, NoSuchAlgorithmException {
        log.debug("REST request to save AvoirTotal : {}", numQuittances);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        List<FactureAVDTO> factureAVDTOs = avoirService.saveAvoirTotals(numQuittances, codeAdmission, pharmacieExterne);
        FactureWithFacturation result = facturationAvoir(null, codeAdmission, factureAVDTOs, null);
        return ResponseEntity.created(new URI("/api/Avoir")).body(result);
    }

    /**
     * POST /Avoir : Create a new factureav.
     *
     * @param avoirWithReglement
     * @param pharmacieExterne
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new factureav, or with status 400 (Bad Request) if the factureav has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/Avoir/list")
    public ResponseEntity<FactureWithFacturation> createListFactureAV(@Valid @RequestBody AvoirWithReglement avoirWithReglement,
            @RequestParam(name = "pharmacieExterne", defaultValue = "false") Boolean pharmacieExterne, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException, NoSuchAlgorithmException {
        log.debug("REST request to save FactureAV : {}", avoirWithReglement);
        for (Avoir avoir : avoirWithReglement.getAvoirs()) {
            if (avoir.getNumbon() != null) {
                bindingResult.addError(new FieldError("avoir", "numbon", "POST method does not accepte " + ENTITY_NAME + " with code"));
                throw new MethodArgumentNotValidException(null, bindingResult);
            }
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        List<FactureAVDTO> factureAVDTOs = avoirService.saves(avoirWithReglement, pharmacieExterne);
        
        Long codeDetailsAdmissionPosDelivery = null;
        if(avoirWithReglement.getCodeDetailsAdmissionPosDelivery() != null){
            log.debug("avoirWithReglement.getCodeDetailsAdmissionPosDelivery() : {}", avoirWithReglement.getCodeDetailsAdmissionPosDelivery());
            codeDetailsAdmissionPosDelivery = avoirWithReglement.getCodeDetailsAdmissionPosDelivery();
        }
        FactureWithFacturation result = facturationAvoir(avoirWithReglement.getReglementDTOs(), avoirWithReglement.getAvoirs().get(0).getNumdoss(), 
                factureAVDTOs, codeDetailsAdmissionPosDelivery);
        return ResponseEntity.created(new URI("/api/Avoir")).body(result);
    }

    /**
     * GET /Avoir/{id} : get the "id" factureav.
     *
     * @param id the id of the factureav to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * factureav, or with status 404 (Not Found)
     */
    @GetMapping("/Avoir/{id}")
    public ResponseEntity<FactureAVDTO> getFactureAV(@PathVariable String id) {
        log.debug("Request to get FactureAV: {}", id);
        FactureAVDTO dto = avoirService.findOne(id);
        RestPreconditions.checkFound(dto, "factureav.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /Avoir : get all the Avoir.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param codeAdmission
     * @param depotID
     * @param etatPatient
     * @return the ResponseEntity with status 200 (OK) and the list of Avoir in
     * body
     */
    @GetMapping("/Avoir")
    public Collection<FactureAVDTO> getAllFactureAVs(@RequestParam(name = "categ-depots", required = false) List<CategorieDepotEnum> categ,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "code-admission", required = false) String codeAdmission,
            @RequestParam(name = "depot-id", required = false) Integer depotID,
            @RequestParam(name = "etat-patient", required = false) List<ReceptionConstants> etatPatient) {
        log.debug("Request to get all  FactureAVs : {}");
        return avoirService.findAll(categ, fromDate, toDate, codeAdmission, depotID, etatPatient);
    }

    /**
     * Get details factureAV.
     *
     * @param numBon
     * @return the the list of entities
     */
    @GetMapping("/Avoir/{numBon}/details")
    public @ResponseBody
    List<MvtStoAVDTO> findDetailsTransfert(@PathVariable("numBon") String numBon) {
        return avoirService.findDetails(numBon);
    }

    /**
     * Get details factureAV.
     *
     * @param numBonMvtsto
     * @param numordreMvtstoAV
     * @param codart
     * @return the the list of entities
     */
    @GetMapping("/Avoir/mvtStoAV/details")
    public @ResponseBody
    List<MvtstomvtstoAVDTO> findDetailsMvtStoAV(@RequestParam(name = "numBonMvtsto", required = true) String numBonMvtsto,
            @RequestParam(name = "numordreMvtstoAV", required = true) String numordreMvtstoAV,
            @RequestParam(name = "codart", required = true) Integer codart
    ) {
        return avoirService.findDetailsMvtStoAV(numBonMvtsto, numordreMvtstoAV, codart);
    }

    /**
     * GET /Avoir/{id} : get the "id" Avoir.
     *
     * @param numBon
     * @return the ResponseEntity with status 200 (OK) and with body of Avoir,
     * or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/Avoir/edition/{numBon}")
    public ResponseEntity<byte[]> getEdition(@PathVariable String numBon) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get Avoir: {}", numBon);
        byte[] bytePdf = avoirService.edition(numBon);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    @ApiOperation(value = "Search details of the given quittances grouped by articles and unity.")
    @GetMapping("/Avoir/details")
    public List<MvtStoAVDTO> findDetailsAvoir(
            @ApiParam("Only PH or UU could be choosen. If EC is choosed then a response with status 409 will be returned") @RequestParam(name = "categ-depot") List<CategorieDepotEnum> categDepot,
            @RequestParam(name = "Numdoss", required = false) String Numdoss,
            @RequestParam(name = "code-depot", required = false) Integer coddep,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate) {
        log.debug("REST request to search details of quittances  ");
        return avoirService.findDetailsAvoir(categDepot, Numdoss, coddep, fromDate, toDate);

    }

    /**
     * facturation liste des factures .
     *
     * @param reglementDTOs
     * @param numdoss
     * @param listFactureAVDTO
     * @return the persisted entity
     */
    public FactureWithFacturation facturationAvoir(List<ReglementDTO> reglementDTOs, String numdoss, List<FactureAVDTO> listFactureAVDTO,
            Long codeDetailsAdmissionPosDelivery) {
        try {
//traitement facturation (module facturation)
            QuittancePharmacieReglementDTO quittancePharmacieReglementDTO = new QuittancePharmacieReglementDTO();
            quittancePharmacieReglementDTO.setQuittancePharmacieDTOs(listFactureAVDTO);
            quittancePharmacieReglementDTO.setReglementDTOs(reglementDTOs);
            if(codeDetailsAdmissionPosDelivery != null)
                quittancePharmacieReglementDTO.setCodeDetailsAdmissionPosDelivery(codeDetailsAdmissionPosDelivery);
            FacturationPayementPharmacieDTO detailsAdmissions = receptionServiceClient.facturationQuittanceAvoir(quittancePharmacieReglementDTO, numdoss);
            checkBusinessLogique(detailsAdmissions != null, "error-facturation");
            FactureWithFacturation factureWithFacturation = new FactureWithFacturation();
            factureWithFacturation.setFacture(detailsAdmissions);
            factureWithFacturation.setQuittances(listFactureAVDTO);
            return factureWithFacturation;
        } catch (Exception e) {
            List<String> numFactureAVs = listFactureAVDTO.stream().map(factureAVDTO -> factureAVDTO.getNumbon()).distinct().collect(Collectors.toList());
            avoirService.deletesPermanent(numFactureAVs);
            checkBusinessLogique(false, "error-facturation");
            return null;
        }
    }

    /**
     * DELETE /quittances/ : delete the "ids" quittances.
     *
     * @param avoirIDs
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/Avoir/")
    public ResponseEntity<List<FactureAVDTO>> deleteFactures(@ApiParam("list of avoirs ids") @RequestBody List<String> avoirIDs) {
        log.debug("Request to delete avoirs: {}", avoirIDs);
        List<FactureAVDTO> dto = avoirService.deletes(avoirIDs);
        return ResponseEntity.ok().body(dto);
    }

}
