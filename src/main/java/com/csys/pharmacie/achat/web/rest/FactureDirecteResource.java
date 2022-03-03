package com.csys.pharmacie.achat.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.DetailFactureDirecteDTO;
import com.csys.pharmacie.achat.dto.FactureDirecteDTO;
import com.csys.pharmacie.achat.service.FactureDirecteService;
import com.csys.pharmacie.achat.service.KafkaDirectBillManagementForAccounting;
import com.csys.util.RestPreconditions;
import java.io.IOException;
import java.lang.String;
import java.lang.Void;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing FactureDirecte.
 */
@RestController
@RequestMapping("/api")
public class FactureDirecteResource {

    private static final String ENTITY_NAME = "facturedirecte";

    private final FactureDirecteService facturedirecteService;
    private final KafkaDirectBillManagementForAccounting kafkaDirectBillManagementForAccounting;

    private final Logger log = LoggerFactory.getLogger(FactureDirecteService.class);

    public FactureDirecteResource(FactureDirecteService facturedirecteService, KafkaDirectBillManagementForAccounting kafkaDirectBillManagementForAccounting) {
        this.facturedirecteService = facturedirecteService;
        this.kafkaDirectBillManagementForAccounting = kafkaDirectBillManagementForAccounting;
    }

    /**
     * POST /facturedirectes : Create a new facturedirecte.
     *
     * @param facturedirecteDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new facturedirecte, or with status 400 (Bad Request) if the
     * facturedirecte has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/facturedirectes")
    public ResponseEntity<FactureDirecteDTO> createFactureDirecte(@Valid @RequestBody FactureDirecteDTO facturedirecteDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save FactureDirecte : {}", facturedirecteDTO);
        if (facturedirecteDTO.getNumbon() != null) {
            bindingResult.addError(new FieldError("FactureDirecteDTO", "numbon", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
//        FactureDirecteDTO result = facturedirecteService.save(facturedirecteDTO);
        FactureDirecteDTO result = kafkaDirectBillManagementForAccounting.saveReocrd(facturedirecteDTO);
        return ResponseEntity.created(new URI("/api/facturedirectes/" + result.getNumbon())).body(result);
    }

    @PostMapping("/facture-directes-commande")
    public ResponseEntity<FactureDirecteDTO> createFactureDirecteWithBonCommande(@Valid @RequestBody FactureDirecteDTO facturedirecteDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save FactureDirecte : {}", facturedirecteDTO);
        if (facturedirecteDTO.getNumbon() != null) {
            bindingResult.addError(new FieldError("FactureDirecteDTO", "numbon", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        FactureDirecteDTO result = facturedirecteService.saveWithBonCommande(facturedirecteDTO);
        return ResponseEntity.created(new URI("/api/facturedirectes/" + result.getNumbon())).body(result);
    }

    /**
     * PUT /facturedirectes : Updates an existing facturedirecte.
     *
     * @param id
     * @param facturedirecteDTO the facturedirecte to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * facturedirecte, or with status 400 (Bad Request) if the facturedirecte is
     * not valid, or with status 500 (Internal Server Error) if the
     * facturedirecte couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PutMapping("/facturedirectes/{id}")
    public ResponseEntity<FactureDirecteDTO> updateFactureDirecte(@PathVariable String id, @Valid @RequestBody FactureDirecteDTO facturedirecteDTO) throws MethodArgumentNotValidException {
        log.debug("Request to update FactureDirecte: {}", id);
        facturedirecteDTO.setNumbon(id);
        FactureDirecteDTO result = facturedirecteService.update(facturedirecteDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET /facturedirectes/{id} : get the "id" facturedirecte.
     *
     * @param id the id of the facturedirecte to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * facturedirecte, or with status 404 (Not Found)
     */
    @GetMapping("/facturedirectes/{id}")
    public ResponseEntity<FactureDirecteDTO> getFactureDirecte(@PathVariable String id) {
        log.debug("Request to get FactureDirecte: {}", id);
        FactureDirecteDTO dto = facturedirecteService.findOne(id);
        RestPreconditions.checkFound(dto, "facturedirecte.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /facturedirectes/{id}/details : get the details of a facturedirecte.
     *
     * @param id the id of the facturedirecte
     * @return the ResponseEntity with status 200 (OK) and with body of details
     * of facture directe
     */
    @GetMapping("/facturedirectes/{id}/details")
    public Collection<DetailFactureDirecteDTO> getFactureDirecteDetails(@PathVariable String id) {
        log.debug("Request to get details of FactureDirecte: {}", id);

        return facturedirecteService.findDetailFactureById(id);
    }

    /**
     * GET /facturedirectes : get all the facturedirectes.
     *
     * @param fromDate
     * @param toDate
     * @return the ResponseEntity with status 200 (OK) and the list of
     * facturedirectes in body
     */
    @GetMapping("/facturedirectes")
    public Collection<FactureDirecteDTO> getAllFactureDirectes(
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "deleted", required = false) Boolean deleted,
            @RequestParam(name = "withDetails", required = false) Boolean withDetails) {

        log.debug("Request to get all  FactureDirectes : {}");
        return facturedirecteService.findAll(fromDate, toDate, deleted, withDetails);
    }

    @GetMapping("/facturedirecte/edition/{id}")
    public ResponseEntity<byte[]> getEditionFactureDirecte(@PathVariable String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = facturedirecteService.editionFactureDirecte(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    /**
     * DELETE /facturedirectes/{id} : delete the "id" facturedirecte.
     *
     * @param id the id of the facturedirecte to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/facturedirectes/{id}")
    public ResponseEntity<Void> deleteFactureDirecte(@PathVariable String id) {
        log.debug("Request to delete FactureDirecte: {}", id);
        facturedirecteService.delete(id);
        return ResponseEntity.ok().build();
    }
}
