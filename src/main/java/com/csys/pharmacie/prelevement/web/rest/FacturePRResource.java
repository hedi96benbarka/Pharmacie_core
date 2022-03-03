package com.csys.pharmacie.prelevement.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.client.dto.ListeArticleNonMvtDTOWrapper;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.prelevement.domain.FacturePR;
import com.csys.pharmacie.prelevement.dto.FacturePRDTO;
import com.csys.pharmacie.prelevement.dto.ListeFacturePRDTOWrapper;
import com.csys.pharmacie.prelevement.dto.MvtStoPRDTO;
import com.csys.pharmacie.prelevement.factory.FacturePRFactory;
import com.csys.pharmacie.prelevement.service.FacturePRService;
import com.csys.util.RestPreconditions;
import java.io.IOException;
import java.net.URI;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing FacturePR.
 */
@RestController
@RequestMapping("/api")
public class FacturePRResource {

    private static final String ENTITY_NAME = "facturepr";

    private final FacturePRService factureprService;
    private final ParamAchatServiceClient paramAchatServiceClient;

    private final Logger log = LoggerFactory.getLogger(FacturePRService.class);

    public FacturePRResource(FacturePRService factureprService, ParamAchatServiceClient paramAchatServiceClient) {
        this.factureprService = factureprService;
        this.paramAchatServiceClient = paramAchatServiceClient;
    }


    /**
     * POST /factureprs : Create a new facturepr.
     *
     * @param factureprDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new facturepr, or with status 400 (Bad Request) if the facturepr has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/liste-factureprs")
    public ResponseEntity<ListeFacturePRDTOWrapper> createListeFacturePR(@Valid @RequestBody ListeFacturePRDTOWrapper listeFacturePRDTOWrapper, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save ListeFacturePRDTOWrapper : {}", listeFacturePRDTOWrapper);

        ListeFacturePRDTOWrapper result = factureprService.saveListeFacturePR(listeFacturePRDTOWrapper);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/factureprs")
    public ResponseEntity<FacturePRDTO> createFacturePR(@Valid @RequestBody FacturePRDTO factureprDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save FacturePR : {}", factureprDTO);
        FacturePRDTO result = factureprService.save(factureprDTO);
        ListeArticleNonMvtDTOWrapper facturePRDTOWrapper = FacturePRFactory.facturePrDTOToListeArticleNonMvtDTOWrapper(result);
        log.debug("facturePRDTOWrapper to send: {}", facturePRDTOWrapper);
               paramAchatServiceClient.updateListArticleNonMvt(facturePRDTOWrapper); 
        return ResponseEntity.created(new URI("/api/factureprs/" + result.getNumbon())).body(result);
    }

    /**
     * GET /factureprs/{id} : get the "id" facturepr.
     *
     * @param id the id of the facturepr to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * facturepr, or with status 404 (Not Found)
     */
    @GetMapping("/factureprs/{id}/details")
    public List<MvtStoPRDTO> getmvtFacturePR(@PathVariable String id) {
        log.debug("Request to get FacturePR: {}", id);
        List<MvtStoPRDTO> dto = factureprService.findDetailFactureById(id);

        RestPreconditions.checkFound(dto, "facturepr.NotFound");
        return dto;
    }

    @GetMapping("/factureprs/{id}")
    public ResponseEntity<FacturePRDTO> getFacturePR(@PathVariable String id) {
        log.debug("Request to get FacturePR: {}", id);
        FacturePRDTO dto = factureprService.findOne(id);
        return ResponseEntity.ok().body(dto);

    }

    /**
     * GET /factureprs : get all the factureprs.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param codeDep
     * @param deleted
     * @return the ResponseEntity with status 200 (OK) and the list of
     * factureprs in body
     */
    @GetMapping("/factureprs")
    public List<FacturePRDTO> getAllFacturePRs(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "formDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "code-depot", required = false) Integer codeDep,
            @RequestParam(name = "deleted", required = false) Boolean deleted
    ) {
        FacturePR quiredFacPR = new FacturePR();
        quiredFacPR.setCoddepotSrc(codeDep);
        quiredFacPR.setCategDepot(categ);

        return factureprService.findAll(quiredFacPR, fromDate, toDate, deleted);
    }

    @GetMapping("/factureprs/edition/{numBon}")
    public ResponseEntity<byte[]> getEditionReceiving(@PathVariable String numBon) throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {
        byte[] bytePdf = factureprService.edition(numBon);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    @DeleteMapping("/facturepr/prelevment/{numBon}")
    public FacturePRDTO deleteEtatReceptionCA(@PathVariable String numBon) {
        log.debug("Request to delete : {}", numBon);
        FacturePRDTO facturepr = factureprService.delete(numBon);
        return facturepr;

    }

    @GetMapping("/facturepr/details")
    public List<MvtStoPRDTO> findListDetails(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "formDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "wharehouse-src-id", required = false) Integer whareHouseSrcId,
            @RequestParam(name = "departemenet-dest-id", required = false) Integer departementDestId
    ) {

        return factureprService.findListDetailsDTOGrouped(categ, fromDate, toDate, whareHouseSrcId, departementDestId);
    }

    @GetMapping("/facturepr/{qr-code:.+}")
    public MvtStoPRDTO SearchOne(
            @PathVariable("qr-code") String QRCode,
            @RequestParam(name = "wharehouse-src-id", required = false) Integer whareHouseSrcId,
            @RequestParam(name = "departemenet-dest-id", required = false) Integer departementDestId,
            @RequestParam(name = "formDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate) {
        return factureprService.findByQrCodeAndUnity(QRCode, whareHouseSrcId, departementDestId, fromDate, toDate);
    }
     @GetMapping("/facturepr/lastFacturePrByCodart")
    public List<FacturePRDTO> findTopFacturePRByCodart(
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categDepot,                                                                                                                                              
            @RequestParam(name = "wharehouse-dest-Id", required = true) Integer codeDepot,
            @RequestParam(name = "item-id", required = true) Integer codeArticle 
            
    ) {

        return factureprService.findLastFacturePRByCodart(categDepot, codeDepot, codeArticle);
    }

}
