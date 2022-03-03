package com.csys.pharmacie.achat.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.RetourPerimeDTO;
import com.csys.pharmacie.achat.service.RetourPerimeService;
import com.csys.pharmacie.helper.CategorieDepotEnum;

import com.csys.util.RestPreconditions;
import java.io.IOException;
import java.lang.String;
import java.lang.Void;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * REST controller for managing FactureRetour_perime.
 */
@RestController
@RequestMapping("/api")
public class RetourPerimeResource {

    private static final String ENTITY_NAME = "factureretourPerime";

    private final RetourPerimeService factureretourPerimeService;

    private final Logger log = LoggerFactory.getLogger(RetourPerimeService.class);

    public RetourPerimeResource(RetourPerimeService factureretour_perimeService) {
        this.factureretourPerimeService = factureretour_perimeService;
    }

    /**
     * POST /factureretour_perimes : Create a new factureretour_perime.
     *
     * @param bonRetourPerimeDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new factureretour_perime, or with status 400 (Bad Request) if the
     * factureretour_perime has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/retours-perimes")
    public ResponseEntity<RetourPerimeDTO> createFactureRetour_perime(@Valid @RequestBody RetourPerimeDTO bonRetourPerimeDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save FactureRetour_perime : {}", bonRetourPerimeDTO);
        if (bonRetourPerimeDTO.getNumbon() != null) {
            bindingResult.addError(new FieldError("FactureBEDTO", "code", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        RetourPerimeDTO result = factureretourPerimeService.save(bonRetourPerimeDTO);
        return ResponseEntity.created(new URI("/api/factureretour_perimes/" + result.getNumbon())).body(result);

    }

    /**
     * PUT /factureretour_perimes : Updates an existing factureretour_perime.
     *
     * @param id
     * @param factureretour_perimeDTO the factureretour_perime to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * factureretour_perime, or with status 400 (Bad Request) if the
     * factureretour_perime is not valid, or with status 500 (Internal Server
     * Error) if the factureretour_perime couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PutMapping("/retours-perimes/{id}")
    public ResponseEntity<RetourPerimeDTO> updateFactureRetour_perime(@PathVariable String id, @Valid @RequestBody RetourPerimeDTO factureretour_perimeDTO) throws MethodArgumentNotValidException {
        log.debug("Request to update FactureRetour_perime: {}", id);
        factureretour_perimeDTO.setNumbon(id);
        RetourPerimeDTO result = factureretourPerimeService.update(factureretour_perimeDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET /factureretour_perimes/{id} : get the "id" factureretour_perime.
     *
     * @param id the id of the factureretour_perime to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * factureretour_perime, or with status 404 (Not Found)
     */
    @GetMapping("/retours-perimes/{id}")
    public ResponseEntity<RetourPerimeDTO> getFactureRetour_perime(@PathVariable String id) {
        log.debug("Request to get FactureRetour_perime: {}", id);
        RetourPerimeDTO dto = factureretourPerimeService.findOne(id);
        RestPreconditions.checkFound(dto, "factureretour_perime.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /factureretour_perimes : get all the factureretour_perimes.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param codeFrs
     * @return the ResponseEntity with status 200 (OK) and the list of
     * factureretour_perimes in body
     */
    @GetMapping("/retours-perimes")
    public List<RetourPerimeDTO> getAllFactureRetour_perimes(
            @RequestParam(name = "categ-depot", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "code-frs", required = false) String codeFrs,
            @RequestParam(name = "invoiced", required = false) Boolean invoiced) {
        log.debug("Request to get all  FactureRetour_perimes : {}");
        return factureretourPerimeService.findAll(categ, fromDate, toDate, codeFrs, invoiced);
    }

    /**
     * DELETE /factureretour_perimes/{id} : delete the "id"
     * factureretour_perime.
     *
     * @param id the id of the factureretour_perime to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/retours-perimes/{id}")
    public ResponseEntity<Void> deleteFactureRetour_perime(@PathVariable String id) {
        log.debug("Request to delete FactureRetour_perime: {}", id);
        factureretourPerimeService.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/retours-perimes/edition/{id}")
    public ResponseEntity<byte[]> edition(@PathVariable String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = factureretourPerimeService.edition(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }


    @GetMapping("/retours-perimes/configs/expiration-date-inteval")
    public String getexpirationDateInteval() {

        return factureretourPerimeService.getINTERVAL();
    }
}
