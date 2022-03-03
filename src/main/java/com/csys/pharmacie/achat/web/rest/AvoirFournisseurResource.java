package com.csys.pharmacie.achat.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.AvoirFournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoAFDTO;
import com.csys.pharmacie.achat.service.AvoirFournisseurService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.io.IOException;
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
 * REST controller for managing AvoirFournisseur.
 */
@RestController
@RequestMapping("/api")
public class AvoirFournisseurResource {

    private static final String ENTITY_NAME = "avoirfournisseur";

    private final AvoirFournisseurService avoirfournisseurService;

    private final Logger log = LoggerFactory.getLogger(AvoirFournisseurService.class);

    public AvoirFournisseurResource(AvoirFournisseurService avoirfournisseurService) {
        this.avoirfournisseurService = avoirfournisseurService;
    }

    /**
     * POST /avoirfournisseurs : Create a new avoirfournisseur.
     *
     * @param avoirfournisseurDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new avoirfournisseur, or with status 400 (Bad Request) if the
     * avoirfournisseur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/avoirfournisseurs")
    public ResponseEntity<AvoirFournisseurDTO> createAvoirFournisseur(@Valid @RequestBody AvoirFournisseurDTO avoirfournisseurDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save AvoirFournisseur : {}", avoirfournisseurDTO);
        if (avoirfournisseurDTO.getNumbon() != null) {
            bindingResult.addError(new FieldError("AvoirFournisseurDTO", "numbon", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        AvoirFournisseurDTO result = avoirfournisseurService.save(avoirfournisseurDTO);
        return ResponseEntity.created(new URI("/api/avoirfournisseurs/" + result.getNumbon())).body(result);
    }

    /**
     * PUT /avoirfournisseurs : Updates an existing avoirfournisseur.
     *
     * @param id
     * @param avoirfournisseurDTO the avoirfournisseur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * avoirfournisseur, or with status 400 (Bad Request) if the
     * avoirfournisseur is not valid, or with status 500 (Internal Server Error)
     * if the avoirfournisseur couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PutMapping("/avoirfournisseurs/{id}")
    public ResponseEntity<AvoirFournisseurDTO> updateAvoirFournisseur(@PathVariable String id, @Valid @RequestBody AvoirFournisseurDTO avoirfournisseurDTO) throws MethodArgumentNotValidException {
        log.debug("Request to update AvoirFournisseur: {}", id);
        avoirfournisseurDTO.setNumbon(id);
        AvoirFournisseurDTO result = avoirfournisseurService.update(avoirfournisseurDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET /avoirfournisseurs/{id} : get the "id" avoirfournisseur.
     *
     * @param id the id of the avoirfournisseur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * avoirfournisseur, or with status 404 (Not Found)
     */
    @GetMapping("/avoirfournisseurs/{id}")
    public AvoirFournisseurDTO getAvoirFournisseur(@PathVariable String id) {
        log.debug("Request to get AvoirFournisseur: {}", id);
        return avoirfournisseurService.findOne(id);
     
    }

    @GetMapping("/avoirfournisseurs/{numBon}/details")
    public List<MvtstoAFDTO> getAvoiFournisseursDetails(@PathVariable String numBon) {
        log.debug("Request to get details of AvoirFournisseur: {}", numBon);

        return avoirfournisseurService.findDetailsAvoirById(numBon);

    }

    @GetMapping("/avoirfournisseurs/edition/{id}")
    public ResponseEntity<byte[]> getEditionAvoirFournisseur(@PathVariable String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = avoirfournisseurService.editionAvoirFournisseur(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    /**
     * GET /avoirfournisseurs : get all the avoirfournisseurs.
     *
     * @param fromDate
     * @param toDate
     * @param deleted
     * @param categDepot
     * @return the ResponseEntity with status 200 (OK) and the list of
     * avoirfournisseurs in body
     */
    @GetMapping("/avoirfournisseurs")
    public Collection<AvoirFournisseurDTO> getAllAvoirFournisseurs(
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "deleted", required = false) Boolean deleted,
            @RequestParam(name = "categDepot", required = false) CategorieDepotEnum categDepot) {
        log.debug("Request to get all  AvoirFournisseurs : {}");
        return avoirfournisseurService.findAll(fromDate, toDate, deleted,categDepot);
    }

    /**
     * DELETE /avoirfournisseurs/{id} : delete the "id" avoirfournisseur.
     *
     * @param id the id of the avoirfournisseur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/avoirfournisseurs/{id}")
    public ResponseEntity<Void> deleteAvoirFournisseur(@PathVariable String id) {
        log.debug("Request to delete AvoirFournisseur: {}", id);
        avoirfournisseurService.delete(id);
        return ResponseEntity.ok().build();
    }
}
