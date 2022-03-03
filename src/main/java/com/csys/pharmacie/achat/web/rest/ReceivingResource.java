package com.csys.pharmacie.achat.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.domain.ReceivingDetails;
import com.csys.pharmacie.achat.dto.ReceivingCommandeDTO;
import com.csys.util.RestPreconditions;
import com.csys.pharmacie.achat.dto.ReceivingDTO;
import com.csys.pharmacie.achat.dto.ReceivingDetailsDTO;
import com.csys.pharmacie.achat.service.ReceivingCommandeService;
import com.csys.pharmacie.achat.service.ReceivingService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
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
 * REST controller for managing Receiving.
 */
@RestController
@RequestMapping("/api")
public class ReceivingResource {

    private static final String ENTITY_NAME = "receiving";

    private final ReceivingService receivingService;

    private final ReceivingCommandeService receivingcommandeService;

    private final Logger log = LoggerFactory.getLogger(ReceivingService.class);

    public ReceivingResource(ReceivingService receivingService, ReceivingCommandeService receivingcommandeService) {
        this.receivingService = receivingService;
        this.receivingcommandeService = receivingcommandeService;
    }

    /**
     * POST /receivings : Create a new receiving.
     *
     * @param receivingDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new receiving, or with status 400 (Bad Request) if the receiving has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/receivings")
    public ResponseEntity<ReceivingDTO> createReceiving(@Valid @RequestBody ReceivingDTO receivingDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save Receiving : {}", receivingDTO);
        if (receivingDTO.getCode() != null) {
            bindingResult.addError(new FieldError("ReceivingDTO", "code", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        ReceivingDTO result = receivingService.save(receivingDTO);
        return ResponseEntity.created(new URI("/api/receivings/" + result.getCode())).body(result);
    }
    
    @PutMapping("/validate-receivings")
    public ResponseEntity<ReceivingDTO> validateReceiving(@RequestBody ReceivingDTO receivingDTO) {
        log.debug("REST request to save Receiving : {}", receivingDTO);
        List<ReceivingDetails> result = receivingService.validateReceiving(receivingDTO);
        return ResponseEntity.ok().body(receivingDTO);
    }

    /**
     * POST /receivings : Create a new receiving.
     *
     * @param id
     * @param memo
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new receiving, or with status 400 (Bad Request) if the receiving has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PutMapping("/receivings/{id}")
    public ResponseEntity<ReceivingDTO> addMemoReceiving(@PathVariable Integer id, @RequestBody String memo, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to add memo Receiving : {}", id);
        ReceivingDTO result = receivingService.addMemo(id, memo);
        return ResponseEntity.created(new URI("/api/receivings/" + result.getCode())).body(result);
    }

    /**
     * GET /receivings/{id} : get the "id" receiving.
     *
     * @param id the id of the receiving to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * receiving, or with status 404 (Not Found)
     */
    @GetMapping("/receivings/{id}")
    public ResponseEntity<ReceivingDTO> getReceiving(@PathVariable Integer id) {
        log.debug("Request to get Receiving: {}", id);
        ReceivingDTO dto = receivingService.findOne(id);
        RestPreconditions.checkFound(dto, "receiving.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /receivings/{id} : get the "id" receiving.
     *
     * @param id the id of the receiving to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * receiving, or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/receivings/edition/{id}")
    public ResponseEntity<byte[]> getEditionReceiving(@PathVariable Integer id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get Receiving: {}", id);
        byte[] bytePdf = receivingService.edition(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    /**
     * GET /receivings/details : get the "id" receiving.
     *
     * @param code
     * @return the ResponseEntity with status 200 (OK) and with body of
     * receiving, or with status 404 (Not Found)
     */
    @GetMapping("/receivings/details-receiving-commande")
    public ResponseEntity<List<ReceivingCommandeDTO>> getReceivingCommande(@RequestParam(name = "codeReceiving") Integer code) {
        log.debug("Request to get ReceivingCommande: {}", code);
        List<ReceivingCommandeDTO> dto = receivingcommandeService.findByReceivingCommandePK_Reciveing(code);
        RestPreconditions.checkFound(dto, "receivingcommande.NotFound");
        return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/receivings/details/{id}")
    public ResponseEntity<List<ReceivingDetailsDTO>> getDetailReceiving(@PathVariable Integer id) {
        log.debug("Request to get Receiving: {}", id);
        List<ReceivingDetailsDTO> result = receivingService.findDetailsBycodeReceiving(id);
        return ResponseEntity.ok().body(result);
    }
    /**
     * GET /receivings : get all the receivings.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param deleted
     * @param valid
     * @param imprime
     * @param hasMemo
     * @param codeFrs
     * @param validated
     * @param codeSite
     * @return the ResponseEntity with status 200 (OK) and the list of
     * receivings in body
     * @throws java.text.ParseException
     */
    @GetMapping("/receivings")
    public Collection<ReceivingDTO> getAllReceivings(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "deleted", required = false) Boolean deleted,
            @RequestParam(name = "valid", required = false) Boolean valid,
            @RequestParam(name = "imprime", required = false) Boolean imprime,
            @RequestParam(name = "hasMemo", required = false) Boolean hasMemo,
            @RequestParam(name = "code-frs", required = false) String codeFrs,
            @RequestParam(name = "validated", required = false) Boolean validated,
            @RequestParam(name = "codeSite", required = false) Integer codeSite) throws ParseException {
        log.debug("Request to get all  Receivings ");
   
        return receivingService.findAll(categ, fromDate, toDate, imprime, hasMemo, deleted, valid, codeFrs,validated,codeSite);
    }

    /**
     * DELETE /receivings/{id} : delete the "id" receiving.
     *
     * @param id the id of the receiving to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/receivings/{id}")
    public ResponseEntity<Void> deleteReceiving(@PathVariable Integer id) {
        log.debug("Request to delete Receiving: {}", id);
        receivingService.delete(id);
        return ResponseEntity.ok().build();
    }
}
