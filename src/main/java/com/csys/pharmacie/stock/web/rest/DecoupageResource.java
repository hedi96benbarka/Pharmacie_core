package com.csys.pharmacie.stock.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import static com.csys.pharmacie.helper.CategorieDepotEnum.PH;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.stock.domain.Decoupage;
import com.csys.pharmacie.stock.dto.DecoupageDTO;
import com.csys.pharmacie.stock.dto.DetailDecoupageDTO;
import com.csys.pharmacie.stock.service.DecoupageService;
import com.csys.pharmacie.stock.service.DetailDecoupageService;
import com.csys.util.RestPreconditions;
import java.io.IOException;
import java.lang.String;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Decoupage.
 */
@RestController
@RequestMapping("/api")
public class DecoupageResource {

    private static final String ENTITY_NAME = "decoupage";

    private final DecoupageService decoupageService;
    private final DetailDecoupageService detailDecoupageService;

    private final Logger log = LoggerFactory.getLogger(DecoupageService.class);

    public DecoupageResource(DecoupageService decoupageService, DetailDecoupageService detailDecoupageService) {
        this.decoupageService = decoupageService;
        this.detailDecoupageService = detailDecoupageService;
    }

    

    /**
     * POST /decoupages : Create a new decoupage.
     *
     * @param decoupageDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new decoupage, or with status 400 (Bad Request) if the decoupage has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/decoupages")
    public ResponseEntity<DecoupageDTO> createDecoupage(@Valid @RequestBody DecoupageDTO decoupageDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save Decoupage : {}", decoupageDTO);
        if (decoupageDTO.getNumbon() != null && !decoupageDTO.getNumbon().isEmpty()) {
            bindingResult.addError(new FieldError("DecoupageDTO", "numbon", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        decoupageDTO.setTypbon(TypeBonEnum.DC);
        decoupageDTO.setCategDepot(CategorieDepotEnum.PH);
        DecoupageDTO result = decoupageService.save(decoupageDTO);
        return ResponseEntity.created(new URI("/api/decoupages/" + result.getNumbon())).body(result);
    }

    /**
     * PUT /decoupages : Updates an existing decoupage.
     *
     * @param id
     * @param decoupageDTO the decoupage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * decoupage, or with status 400 (Bad Request) if the decoupage is not
     * valid, or with status 500 (Internal Server Error) if the decoupage
     * couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException //
     */
//  @PutMapping("/decoupages/{id}")
//  public ResponseEntity<DecoupageDTO> updateDecoupage(@PathVariable String id, @Valid @RequestBody DecoupageDTO decoupageDTO) throws MethodArgumentNotValidException {
//    log.debug("Request to update Decoupage: {}",id);
//    decoupageDTO.setNumbon(id);
//    DecoupageDTO result =decoupageService.update(decoupageDTO);
//    return ResponseEntity.ok().body(result);
//  }
    /**
     * GET /decoupages/{id} : get the "id" decoupage.
     *
     * @param id the id of the decoupage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * decoupage, or with status 404 (Not Found)
     */
    @GetMapping("/decoupages/{id}")
    public ResponseEntity<DecoupageDTO> getDecoupage(@PathVariable String id) {
        log.debug("Request to get Decoupage: {}", id);
        DecoupageDTO dto = decoupageService.findOne(id);
        RestPreconditions.checkFound(dto, "decoupage.NotFound");
        return ResponseEntity.ok().body(dto);
    }
    
    
     
    @GetMapping("/decoupages/{id}/details")
    public Collection<DetailDecoupageDTO> getDecoupageDetails(@PathVariable String id) {
        log.debug("Request to get details of  Decoupage: {}", id);
    
        return detailDecoupageService.findDetailDecoupageByCodeDecoupage(id);
    }

    /**
     * GET /decoupages : get all the decoupages.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param codeDep
     * @return the ResponseEntity with status 200 (OK) and the list of
     * decoupages in body
     */
    @GetMapping("/decoupages")
    public List<DecoupageDTO> findAll(
        
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "code-depot", required = false) Integer codeDep
    ) {
        Decoupage queriedDecoupage = new Decoupage();
        queriedDecoupage.setCoddep(codeDep);
        queriedDecoupage.setCategDepot(PH);
        return decoupageService.findAll(queriedDecoupage, fromDate, toDate);
    }

    /**
     * DELETE /decoupages/{id} : delete the "id" decoupage.
     *
     * @param id the id of the decoupage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
//    @DeleteMapping("/decoupages/{id}")
//    public ResponseEntity<Void> deleteDecoupage(@PathVariable String id) {
//        log.debug("Request to delete Decoupage: {}", id);
//        decoupageService.delete(id);
//        return ResponseEntity.ok().build();
//    }
    @GetMapping("/decoupage/edition/{numBon}")
    public ResponseEntity<byte[]> getEditionReceiving(@PathVariable String numBon) throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {
        byte[] bytePdf = decoupageService.edition(numBon);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }
}
