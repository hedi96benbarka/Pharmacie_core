package com.csys.pharmacie.prelevement.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.prelevement.dto.DetailRetourPrelevementDTO;
import com.csys.pharmacie.prelevement.dto.RetourPrelevementDTO;
import com.csys.pharmacie.prelevement.service.RetourPrelevementService;
import com.csys.util.RestPreconditions;
import java.io.IOException;
import java.lang.String;
import java.lang.Void;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
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
 * REST controller for managing RetourPrelevement.
 */
@RestController
@RequestMapping("/api")
public class RetourPrelevementResource {

    private static final String ENTITY_NAME = "retourprelevement";

    private final RetourPrelevementService retourprelevementService;
 

    private final Logger log = LoggerFactory.getLogger(RetourPrelevementService.class);

    public RetourPrelevementResource(RetourPrelevementService retourprelevementService) {
        this.retourprelevementService = retourprelevementService;
    }

    /**
     * POST /retourprelevements : Create a new retourprelevement.
     *
     * @param retourprelevementDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new retourprelevement, or with status 400 (Bad Request) if the
     * retourprelevement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/retourprelevements")
    public ResponseEntity<RetourPrelevementDTO> createRetourPrelevement(@Valid @RequestBody RetourPrelevementDTO retourPrelevementDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save RetourPrelevement : {}", retourPrelevementDTO);
        if (retourPrelevementDTO.getNumbon() != null) {
            bindingResult.addError(new FieldError("RetourPrelevementDTO", "numbon", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        RetourPrelevementDTO result = retourprelevementService.save(retourPrelevementDTO);
        return ResponseEntity.created(new URI("/api/retourprelevements/" + result.getNumbon())).body(result);
    }

    /**
     * PUT /retourprelevements : Updates an existing retourprelevement.
     *
     * @param id
     * @param retourprelevementDTO the retourprelevement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * retourprelevement, or with status 400 (Bad Request) if the
     * retourprelevement is not valid, or with status 500 (Internal Server
     * Error) if the retourprelevement couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PutMapping("/retourprelevements/{id}")
    public ResponseEntity<RetourPrelevementDTO> updateRetourPrelevement(@PathVariable String id, @Valid @RequestBody RetourPrelevementDTO retourprelevementDTO) throws MethodArgumentNotValidException {
        log.debug("Request to update RetourPrelevement: {}", id);
        retourprelevementDTO.setNumbon(id);
        RetourPrelevementDTO result = retourprelevementService.update(retourprelevementDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET /retourprelevements/{id} : get the "id" retourprelevement.
     *
     * @param id the id of the retourprelevement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * retourprelevement, or with status 404 (Not Found)
     */
    @GetMapping("/retourprelevements/{id}")
    public ResponseEntity<RetourPrelevementDTO> getRetourPrelevement(@PathVariable String id) {
        log.debug("Request to get RetourPrelevement: {}", id);
        RetourPrelevementDTO dto = retourprelevementService.findOne(id);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /retourprelevements : get all the retourprelevements.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @return the ResponseEntity with status 200 (OK) and the list of
     * retourprelevements in body
     */
    @GetMapping("/retourprelevements")
    public List<RetourPrelevementDTO> getAllRetourPrelevements(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "formDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate) {
        log.debug("Request to get all  RetourPrelevements : {}");
        return retourprelevementService.findAll(categ, fromDate, toDate);
    }

    /**
     * DELETE /retourprelevements/{id} : delete the "id" retourprelevement.
     *
     * @param id the id of the retourprelevement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/retourprelevements/{id}")
    public ResponseEntity<Void> deleteRetourPrelevement(@PathVariable String id) {
        log.debug("Request to delete RetourPrelevement: {}", id);
        retourprelevementService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/retourprelevements/edition/{numBon}")
    public ResponseEntity<byte[]> getEditionRetourPrelevement(
            @PathVariable String numBon,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type) throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {
        byte[] bytePdf = retourprelevementService.edition(numBon, type);
        if (type.equalsIgnoreCase("P")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return ResponseEntity.ok().headers(headers).body(bytePdf);
        } else {
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return ResponseEntity.ok().headers(headers).body(bytePdf);
        }
    }

    @GetMapping("/retourprelevements/{id}/details")
    public List<DetailRetourPrelevementDTO> getRetourPrelevementPrDetails(@PathVariable String id) {
        log.debug("Request to get details of retourPrelevement: {}", id);
        return retourprelevementService.findDetailsRetourPrelevementById(id);

    }

     @GetMapping("/retourprelevements/intervalValue")
      public Integer getDefaultValueIntervalRetourPrelevement(){
        return retourprelevementService.getDefaultValueIntervalRetourPrelevement();
     
     
     }
}
