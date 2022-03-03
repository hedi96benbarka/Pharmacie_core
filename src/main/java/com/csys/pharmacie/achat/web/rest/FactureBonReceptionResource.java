package com.csys.pharmacie.achat.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.DetailReceptionDTO;
import com.csys.pharmacie.achat.dto.FactureBonReceptionDTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.achat.service.FactureBonReceptionService;
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
 * REST controller for managing FactureBonReception.
 */
@RestController
@RequestMapping("/api")
public class FactureBonReceptionResource {

    private static final String ENTITY_NAME = "facturebonreception";

    private final FactureBonReceptionService facturebonreceptionService;

    private final Logger log = LoggerFactory.getLogger(FactureBonReceptionService.class);

    public FactureBonReceptionResource(FactureBonReceptionService facturebonreceptionService) {
        this.facturebonreceptionService = facturebonreceptionService;
    }

    /**
     * POST /facturebonreceptions : Create a new facturebonreception.
     *
     * @param facturebonreceptionDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new facturebonreception, or with status 400 (Bad Request) if the
     * facturebonreception has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/facturebonreceptions")
    public ResponseEntity<FactureBonReceptionDTO> createFactureBonReception(@Valid @RequestBody FactureBonReceptionDTO facturebonreceptionDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save FactureBonReception : {}", facturebonreceptionDTO);
        if (facturebonreceptionDTO.getNumbon() != null) {
            bindingResult.addError(new FieldError("FactureBonReceptionDTO", "numbon", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        FactureBonReceptionDTO result = facturebonreceptionService.save(facturebonreceptionDTO);
        return ResponseEntity.created(new URI("/api/facturebonreceptions/" + result.getNumbon())).body(result);
    }

    /**
     * PUT /facturebonreceptions : Updates an existing facturebonreception.
     *
     * @param id
     * @param facturebonreceptionDTO the facturebonreception to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * facturebonreception, or with status 400 (Bad Request) if the
     * facturebonreception is not valid, or with status 500 (Internal Server
     * Error) if the facturebonreception couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PutMapping("/facturebonreceptions/{id}")
    public ResponseEntity<FactureBonReceptionDTO> updateFactureBonReception(@PathVariable String id, @Valid @RequestBody FactureBonReceptionDTO facturebonreceptionDTO) throws MethodArgumentNotValidException {
        log.debug("Request to update FactureBonReception: {}", id);
        facturebonreceptionDTO.setNumbon(id);
        FactureBonReceptionDTO result = facturebonreceptionService.update(facturebonreceptionDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET /facturebonreceptions/{id} : get the "id" facturebonreception.
     *
     * @param id the id of the facturebonreception to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * facturebonreception, or with status 404 (Not Found)
     */
    @GetMapping("/facturebonreceptions/{id}")
    public ResponseEntity<FactureBonReceptionDTO> getFactureBonReception(@PathVariable String id) {
        log.debug("Request to get FactureBonReception: {}", id);
        FactureBonReceptionDTO dto = facturebonreceptionService.findOne(id);
        RestPreconditions.checkFound(dto, "facturebonreception.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /facturebonreceptions : get all the facturebonreceptions.
     *
     * @param fromDate
     * @param toDate
     * @param categDepot
     * @param deleted
     * @param codeFournisseur
     * @return the ResponseEntity with status 200 (OK) and the list of
     * facturebonreceptions in body
     */
    @GetMapping("/facturebonreceptions")
    public Collection<FactureBonReceptionDTO> getAllFactureBonReceptions(
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categDepot", required = false) CategorieDepotEnum categDepot,
              @RequestParam(name = "deleted", required = false) Boolean deleted,
             @RequestParam(name = "codeFournisseur", required = false) String codeFournisseur){
        log.debug("Request to get all  FactureBonReceptions : {}");
        return facturebonreceptionService.findAll(fromDate, toDate, categDepot, deleted,codeFournisseur);
    }

    @GetMapping("/facturebonreceptions/{id}/details")
    public Collection<MvtstoBADTO> getFactureBonReceptionDetails(@PathVariable String id) {
        log.debug("Request to get details of FactureDirecte: {}", id);

        return facturebonreceptionService.findDetailsFactureById(id);

    }

        @GetMapping("/facturebonreception/edition/{id}")
    public ResponseEntity<byte[]> getEditionFactureBonReception(@PathVariable String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = facturebonreceptionService.editionFactureBonReception(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }
    /**
     * DELETE /facturebonreceptions/{id} : delete the "id" facturebonreception.
     *
     * @param id the id of the facturebonreception to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/facturebonreceptions/{id}")
    public ResponseEntity<Void> deleteFactureBonReception(@PathVariable String id) {
        log.debug("Request to delete FactureBonReception: {}", id);
        facturebonreceptionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
