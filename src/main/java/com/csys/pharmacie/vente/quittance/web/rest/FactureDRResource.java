package com.csys.pharmacie.vente.quittance.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.SatisfactionEnum;
import com.csys.pharmacie.vente.quittance.domain.FactureDR;
import com.csys.pharmacie.vente.quittance.dto.DemandeArticlesDto;
import com.csys.pharmacie.vente.quittance.dto.DemandeRecuperationDto;
import com.csys.pharmacie.vente.quittance.dto.FactureDRDTO;
import com.csys.pharmacie.vente.quittance.dto.FactureDRDetailDTO;
import com.csys.pharmacie.vente.quittance.dto.ListDemandeRecuperationDto;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDRDTO;
import com.csys.pharmacie.vente.quittance.service.FactureDRService;
import com.csys.util.RestPreconditions;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * REST controller for managing FactureDR.
 */
@RestController
@RequestMapping("/api")
public class FactureDRResource {

    private static final String ENTITY_NAME = "facturedr";

    private final FactureDRService facturedrService;

    private final Logger log = LoggerFactory.getLogger(FactureDRService.class);

    public FactureDRResource(FactureDRService facturedrService) {
        this.facturedrService = facturedrService;
    }

    /**
     * GET /quittances/{id} : get the "id" facture.
     *
     * @param id the id of the facture to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of facture,
     * or with status 404 (Not Found)
     */
    @GetMapping("/facturedrs/{id}")
    public ResponseEntity<DemandeRecuperationDto> getFacture(@PathVariable String id) {
        log.debug("Request to get facturedrs: {}", id);
        DemandeRecuperationDto dto = facturedrService.findOne(id);
        RestPreconditions.checkFound(dto, "facturedrs.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /facturedrs : get all the facturedrs.
     *
     * @param categ
     * @param satisfactions
     * @param fromDate
     * @param toDate
     * @param codeAdmission
     * @param deleted
     * @param depotID
     * @param codeEtage
     * @return the ResponseEntity with status 200 (OK) and the list of
     * facturedrs in body
     */
    @GetMapping("/facturedrs")
    public List<ListDemandeRecuperationDto> getAllFactureDRs(
            @RequestParam(name = "categ-depots", required = false) List<CategorieDepotEnum> categ,
            @RequestParam(name = "satisfactions", required = false) List<SatisfactionEnum> satisfactions,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "code-admission", required = false) String codeAdmission,
            @RequestParam(name = "deleted", required = false) Boolean deleted,
            @RequestParam(name = "depot-id", required = false) Integer depotID,
            @RequestParam(name = "code-etage", required = false) Integer codeEtage) {
        log.debug("Request to get all  FactureDRs : {}");
        return facturedrService.findAll(categ, fromDate, toDate, codeAdmission, depotID, deleted, codeEtage, satisfactions);
    }

    /**
     * PUT /facturedrs/{id} : update the "id" quittance.
     *
     * @param factureDRDTO
     * @return the ResponseEntity with status 200 (OK)
     */
    @PutMapping("/facturedrs")
    public ResponseEntity<DemandeRecuperationDto> updateFacture(@Valid @RequestBody FactureDRDTO factureDRDTO) {
        log.debug("Request to delete quittance: {}", factureDRDTO);
        DemandeRecuperationDto dto = facturedrService.update(factureDRDTO);
        RestPreconditions.checkFound(dto, "facturedrs.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * DELETE /facturedrs/{id} : delete the "id" quittance.
     *
     * @param id the id of the quittance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/facturedrs/{id}")
    public ResponseEntity<Void> deleteFacture(@PathVariable String id) {
        log.debug("Request to delete quittance: {}", id);
        facturedrService.delete(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/facturedrs/edition/{id}")
    public ResponseEntity<byte[]> getEdition(@PathVariable String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = facturedrService.editionFactureDR(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }
    
    @PostMapping("/facturedrs")
    public ResponseEntity<FactureDR> saveFacture(@Valid @RequestBody FactureDRDetailDTO factureDRDTO) {
        log.debug("Request to save quittance: {}", factureDRDTO);
        FactureDR factureDr = facturedrService.save(factureDRDTO);
        RestPreconditions.checkFound(factureDr, "facturedrs.NotFound");
        return ResponseEntity.ok().body(factureDr);
    }
    
   @GetMapping("/facturedrs/details")
      public List<MvtstoDRDTO> getDetailsFactureDRs(
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "codeAdmission", required = true) String codeAdmission,
            @RequestParam(name = "codeDepot", required = true) Integer codeDepot) {
        log.debug("Request to get all  FactureDRs : {}");
        return facturedrService.findDetailsFactureDRs(codeAdmission, codeDepot, fromDate, toDate);
    }
}
