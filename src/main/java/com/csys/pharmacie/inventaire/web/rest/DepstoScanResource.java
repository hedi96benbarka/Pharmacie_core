package com.csys.pharmacie.inventaire.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.inventaire.dto.DepstoScanDTO;
import com.csys.pharmacie.inventaire.service.DepstoScanService;
import com.csys.util.RestPreconditions;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing DepstoScan.
 */
@RestController
@RequestMapping("/api")
public class DepstoScanResource {

    private static final String ENTITY_NAME = "depstoscan";

    private final DepstoScanService depstoscanService;

    private final Logger log = LoggerFactory.getLogger(DepstoScanService.class);

    public DepstoScanResource(DepstoScanService depstoscanService) {
        this.depstoscanService = depstoscanService;
    }

    /**
     * POST /depstoscans : Create a new depstoscan.
     *
     * @param depstoscanDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new depstoscan, or with status 400 (Bad Request) if the depstoscan has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/depstoscans")
    @ApiOperation("post article in depsto_scan par application android")
    public ResponseEntity<DepstoScanDTO> createDepstoScan(@Valid @RequestBody DepstoScanDTO depstoscanDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save DepstoScan : {}", depstoscanDTO);
        if (depstoscanDTO.getNum() != null) {
            bindingResult.addError(new FieldError("DepstoScanDTO", "num", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        DepstoScanDTO result = depstoscanService.save(depstoscanDTO);
        return ResponseEntity.created(new URI("/api/depstoscans/" + result.getNum())).body(result);
    }

    /**
     * PUT /depstoscans : Updates an existing depstoscan.
     *
     * @param id
     * @param depstoscanDTO the depstoscan to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * depstoscan, or with status 400 (Bad Request) if the depstoscan is not
     * valid, or with status 500 (Internal Server Error) if the depstoscan
     * couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PutMapping("/depstoscans/{id}")
    public ResponseEntity<DepstoScanDTO> updateDepstoScan(@PathVariable Long id, @Valid @RequestBody DepstoScanDTO depstoscanDTO) throws MethodArgumentNotValidException {
        log.debug("Request to update DepstoScan: {}", id);
        depstoscanDTO.setNum(id);
        DepstoScanDTO result = depstoscanService.update(depstoscanDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET /depstoscans/{id} : get the "id" depstoscan.
     *
     * @param id the id of the depstoscan to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * depstoscan, or with status 404 (Not Found)
     */
    @GetMapping("/depstoscans/{id}")
    public ResponseEntity<DepstoScanDTO> getDepstoScan(@PathVariable Long id) {
        log.debug("Request to get DepstoScan: {}", id);
        DepstoScanDTO dto = depstoscanService.findOne(id);
        RestPreconditions.checkFound(dto, "depstoscan.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/inventaires/listeDejaSaisie")
    public List<DepstoScanDTO> findByCodartAndUniteAndCategDepotAndCoddepAndInventerier(
            @RequestParam(required = false, value = "codArt") Integer codArt,
            @RequestParam(required = false, value = "unite") Integer unite,
            @RequestParam(value = "catgorie-depot") CategorieDepotEnum categDepot,
            @RequestParam("coddep") Integer coddep,
            @RequestParam("invnterier") boolean invnterier,
            @RequestParam(required = false, value = "userName") String userName,
            @RequestParam(required = false, value = "importer") Boolean importer
    ) {
        return depstoscanService.findByCodartAndUniteAndCategDepotAndCoddepAndInventerier(codArt, unite, categDepot, coddep, invnterier, userName, importer);
    }

    /**
     * GET /depstoscans : get all the depstoscans.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * depstoscans in body
     */
    @GetMapping("/depstoscans")
    public Collection<DepstoScanDTO> getAllDepstoScans() {
        log.debug("Request to get all  DepstoScans : {}");
        return depstoscanService.findAll();
    }

    /**
     * DELETE /depstoscans/{id} : delete the "id" depstoscan.
     *
     * @param id the id of the depstoscan to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/depstoscans")
    public ResponseEntity<Void> deleteDepstoScan(@RequestBody List<Long> id) {
        log.debug("Request to delete DepstoScan: {}", id);
        depstoscanService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nombreRowForSelectDepot")
    public Integer getAllDepstoScans(
            @RequestParam(value = "catgorie-depot") CategorieDepotEnum categDepot,
            @RequestParam("coddep") Integer coddep) {

        return depstoscanService.nombreRowForSelectDepot(categDepot, coddep);
    }

    @GetMapping("/depstoscans/editionAvImporter")
    public ResponseEntity<byte[]> editionEtatAvImporter(
            @RequestParam CategorieDepotEnum categ_depot,
            @RequestParam Integer coddep,
            @RequestParam boolean inventerier,
            @RequestParam boolean importer,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type)
            throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {
        byte[] bytePdf = depstoscanService.editionEtatAvImporter(categ_depot, coddep, inventerier, importer, type);
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    @GetMapping("/depstoscans/editionArtPerim")
    public ResponseEntity<byte[]> editionEtatArtPerim(
            @RequestParam Integer codeInventaire,
            @RequestParam Boolean ouvert,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type)
            throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {
        byte[] bytePdf = depstoscanService.editionEtatArtPerim(codeInventaire, ouvert, type);
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    @PostMapping("/depstoscans/articleInDepScan")
    public List<DepstoScanDTO> findByCodartInAndCategDepotAndInventerierAndImporter(
            @RequestBody List<Integer> codeArticles,
            @RequestParam(value = "categorie-depot") CategorieDepotEnum categDepot
    ) {
        return depstoscanService.findByCodartInAndCategDepotAndInventerierAndImporter(codeArticles, categDepot);
    }

}
