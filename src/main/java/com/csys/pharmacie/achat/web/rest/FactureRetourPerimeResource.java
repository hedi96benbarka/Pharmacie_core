package com.csys.pharmacie.achat.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.FactureRetourPerimeDTO;
import com.csys.pharmacie.achat.dto.MvtstoRetourPerimeDTO;
import com.csys.pharmacie.achat.service.FactureRetourPerimeService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
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
 * REST controller for managing FactureRetourPerime.
 */
@RestController
@RequestMapping("/api")
public class FactureRetourPerimeResource {

    private static final String ENTITY_NAME = "factureRetourPerime";

    private final FactureRetourPerimeService factureRetourPerimeService;

    private final Logger log = LoggerFactory.getLogger(FactureRetourPerimeService.class);

    public FactureRetourPerimeResource(FactureRetourPerimeService factureRetourPerimeService) {
        this.factureRetourPerimeService = factureRetourPerimeService;
    }

    /**
     * POST /facture-retour-perimes : Create a new factureretourperime.
     *
     * @param factureRetourPerimeDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new factureretourperime, or with status 400 (Bad Request) if the
     * factureretourperime has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/facture-retour-perimes")
    public ResponseEntity<FactureRetourPerimeDTO> createFactureRetourPerime(@Valid @RequestBody FactureRetourPerimeDTO factureRetourPerimeDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save FactureRetourPerime : {}", factureRetourPerimeDTO);

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        FactureRetourPerimeDTO result = factureRetourPerimeService.save(factureRetourPerimeDTO);
        return ResponseEntity.created(new URI("/api/facture-retour-perimes/" + result.getNumbon())).body(result);
    }

    /**
     * PUT /facture-retour-perimes : Updates an existing factureretourperime.
     *
     * @param id
     * @param factureRetourPerimeDTO the factureretourperime to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * factureretourperime, or with status 400 (Bad Request) if the
     * factureretourperime is not valid, or with status 500 (Internal Server
     * Error) if the factureretourperime couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PutMapping("/facture-retour-perimes/{id}")
    public ResponseEntity<FactureRetourPerimeDTO> updateFactureRetourPerime(@PathVariable String id, @Valid @RequestBody FactureRetourPerimeDTO factureRetourPerimeDTO) throws MethodArgumentNotValidException {
        log.debug("Request to update FactureRetourPerime: {}", id);
        factureRetourPerimeDTO.setNumbon(id);
        FactureRetourPerimeDTO result = factureRetourPerimeService.update(factureRetourPerimeDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET /facture-retour-perimes/{id} : get the "id" factureretourperime.
     *
     * @param id the id of the factureretourperime to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * factureretourperime, or with status 404 (Not Found)
     */
    @GetMapping("/facture-retour-perimes/{id}")
    public ResponseEntity<FactureRetourPerimeDTO> getFactureRetourPerime(@PathVariable String id) {
        log.debug("Request to get FactureRetourPerime: {}", id);
        FactureRetourPerimeDTO dto = factureRetourPerimeService.findOne(id);
        RestPreconditions.checkFound(dto, "factureretourperime.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /facture-retour-perimes : get all the factureretourperimes.
     *
     * @param categDepot
     * @param fromDate
     * @param toDate
     * @param deleted
     * @return the ResponseEntity with status 200 (OK) and the list of
     * factureretourperimes in body
     */
    @GetMapping("/facture-retour-perimes")
    public Collection<FactureRetourPerimeDTO> getAllFactureRetourPerimes(@RequestParam(name = "categDepot", required = false) CategorieDepotEnum categDepot,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "deleted", required = false) Boolean deleted) {

        log.debug("Request to get all  FactureRetourPerimes : {}");
        return factureRetourPerimeService.findAll(categDepot, fromDate, toDate, deleted);
    }

    @GetMapping("/facture-retour-perimes/edition/{id}")
    public ResponseEntity<byte[]> getEditionFactureBonReception(@PathVariable String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = factureRetourPerimeService.editionFactureRetourPerime(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    /**
     * DELETE /facture-retour-perimes/{id} : delete the "id"
     * factureretourperime.
     *
     * @param id the id of the factureretourperime to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/facture-retour-perimes/{id}")
    public ResponseEntity<Void> deleteFactureRetourPerime(@PathVariable String id) {
        log.debug("Request to delete FactureRetourPerime: {}", id);
        factureRetourPerimeService.delete(id);
        return ResponseEntity.ok().build();
    }
    
        @GetMapping("/facture-retour-perimes/{id}/details")
    public Collection<MvtstoRetourPerimeDTO> getFactureRetourPerimeDetails(@PathVariable String id) {
        log.debug("Request to get details of Facture Retour perime: {}", id);

        return factureRetourPerimeService.findDetailsFactureById(id);

    }
}
