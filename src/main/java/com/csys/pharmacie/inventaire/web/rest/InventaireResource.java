package com.csys.pharmacie.inventaire.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.CategorieArticleDTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.inventaire.dto.InitialisationInventaireDTO;
import com.csys.pharmacie.inventaire.dto.InventaireDTO;
import com.csys.pharmacie.inventaire.dto.TypeEnvoieEtatEnum;
import com.csys.pharmacie.inventaire.service.InventaireService;
import com.csys.pharmacie.stock.dto.DepstoDTO;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;
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
 * REST controller for managing Inventaire.
 */
@RestController
@RequestMapping("/api")
public class InventaireResource {

    private static final String ENTITY_NAME = "inventaire";

    private final InventaireService inventaireService;

    private final Logger log = LoggerFactory.getLogger(InventaireService.class);

    public InventaireResource(InventaireService inventaireService) {
        this.inventaireService = inventaireService;
    }

    /**
     * POST /inventaires : Create a new inventaire.
     *
     * @param inventaireDTO
     * @param bindingResult
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new inventaire, or with status 400 (Bad Request) if the inventaire has
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PostMapping("/inventaires")
    public ResponseEntity<InventaireDTO> createInventaire(@Valid @RequestBody InventaireDTO inventaireDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save Inventaire : {}", inventaireDTO);
        if (inventaireDTO.getCode() != null) {
            bindingResult.addError(new FieldError("InventaireDTO", "code", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        InventaireDTO result = inventaireService.save(inventaireDTO);
        return ResponseEntity.created(new URI("/api/inventaires/" + result.getCode())).body(result);
    }

    /**
     * PUT /inventaires : Updates an existing inventaire.
     *
     * @param id
     * @param inventaireDTO the inventaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * inventaire, or with status 400 (Bad Request) if the inventaire is not
     * valid, or with status 500 (Internal Server Error) if the inventaire
     * couldn't be updated
     * @throws org.springframework.web.bind.MethodArgumentNotValidException
     */
    @PutMapping("/inventaires/{id}")
    public ResponseEntity<InventaireDTO> updateInventaire(@PathVariable Integer id, @Valid @RequestBody InventaireDTO inventaireDTO) throws MethodArgumentNotValidException {
        log.debug("Request to update Inventaire: {}", id);
        inventaireDTO.setCode(id);
        InventaireDTO result = inventaireService.update(inventaireDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * GET /inventaires/{id} : get the "id" inventaire.
     *
     * @param id the id of the inventaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * inventaire, or with status 404 (Not Found)
     */
    @GetMapping("/inventaires/{id}")
    public ResponseEntity<InventaireDTO> getInventaire(@PathVariable Integer id) {
        log.debug("Request to get Inventaire: {}", id);
        InventaireDTO dto = inventaireService.findOne(id);
        RestPreconditions.checkFound(dto, "inventaire.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /inventaires : get all the inventaires.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * inventaires in body
     */
    @GetMapping("/inventaires")
    public Collection<InventaireDTO> getAllInventaires() {
        log.debug("Request to get all  Inventaires : {}");
        return inventaireService.findAll();
    }

    /**
     * DELETE /inventaires/{id} : delete the "id" inventaire.
     *
     * @param id the id of the inventaire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/inventaires/{id}")
    public ResponseEntity<Void> deleteInventaire(@PathVariable Integer id) {
        log.debug("Request to delete Inventaire: {}", id);
        inventaireService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/inventaires/list")
    public List<DepstoDTO> inventaireVierge(@RequestParam(name = "coddep", required = true) Integer coddep,
            @RequestParam(name = "categ-depot", required = true) CategorieDepotEnum categ_depot,
            @RequestParam(name = "codCatArticle", required = true) Integer codCatArticle
    ) {
        return inventaireService.getDepStoByCodeDepotAndCategorieArticleInitInventaire(coddep, categ_depot, codCatArticle);
    }

    @GetMapping("/inventaires/edition")
    public ResponseEntity<byte[]> getEditionReceiving(@RequestParam Integer coddep, @RequestParam CategorieDepotEnum categ_depot,
            @RequestParam Integer codCatArticle) throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {
        byte[] bytePdf = inventaireService.edition(coddep, categ_depot, codCatArticle);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    @PostMapping("/inventaires/initialisationInventaire")
    public InventaireDTO initialisationInventaire(@Valid @RequestBody InitialisationInventaireDTO initialisationInventaireDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to save initialisation inventaire : {}", initialisationInventaireDTO);

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        return inventaireService.initialisationInventaire(initialisationInventaireDTO);
    }

    @GetMapping("/inventaires/article")
    public Collection<ArticleDTO> articleByCategArt(@RequestParam Integer categorieArticleID) {
        return inventaireService.articleByCategArt(categorieArticleID);
    }

    @ApiOperation(value = "Check open inventory")
    @GetMapping("/inventaires/is-open")
    public Boolean checkOpenInventaire(@RequestParam Integer coddep, @RequestParam CategorieDepotEnum categ_depot,
            @RequestParam Integer codCatArticle) {
        return inventaireService.checkOpenInventaire(coddep, categ_depot, codCatArticle);
    }

    @GetMapping("/inventaires/categorieArticleOuvert")
    public List<CategorieArticleDTO> getCategorieArticleOuvert(@RequestParam Integer coddep, @RequestParam CategorieDepotEnum categ_depot) {
        return inventaireService.getCategorieArticleOuvert(coddep, categ_depot);
    }

    @GetMapping("/inventaires/inventeryColsedToDay")
    public List<InventaireDTO> findByDateClotureNotNullAndDateCloture(Integer dep) {
        return inventaireService.findByDateClotureNotNullAndDateCloture(dep);
    }

    @ApiOperation("return par default les inventaires non annuleés")
    @GetMapping("/inventaires/listeInventaire")
    public List<InventaireDTO> findListInventaireOuvrte(@RequestParam("code-depot") Integer coddep,
            @RequestParam("categorrie-depot") CategorieDepotEnum categ_depot,
            @RequestParam("ouvert") Boolean ouvert,
            @RequestParam(name = "annuler", required = false) Boolean annuler) {
        return inventaireService.findListInventaire(coddep, categ_depot, ouvert, annuler);
    }

    @GetMapping("/inventaires/editionEtatEcartAvVld")
    public ResponseEntity<byte[]> editionEtatEcartAvVld(@RequestParam Integer coddep,
            @RequestParam CategorieDepotEnum categ_depot,
            @RequestParam Integer codeInventaire,
            @RequestParam TypeEnvoieEtatEnum optionImpression,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "group", required = false, defaultValue = "O") String group,
            @RequestParam(name = "principal", required = false) Boolean withPrincipalUnity) throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {

        byte[] bytePdf = inventaireService.editionEtatEcartAv(coddep, categ_depot, codeInventaire, optionImpression, type, group, withPrincipalUnity);
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    @GetMapping("/inventaires/editionEtatEcartApVld")
    public ResponseEntity<byte[]> editionEtatEcartAp(
            @RequestParam Integer codeInventaire,
            @RequestParam TypeEnvoieEtatEnum optionImpression,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "group", required = false, defaultValue = "O") String group,
            @RequestParam(name = "global", required = false, defaultValue = "O") String global,
            @RequestParam(name = "categorie-depot", required = false) CategorieDepotEnum categDepot,
            @RequestParam(name = "principal", required = false) Boolean withPrincipalUnity) throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {

        byte[] bytePdf = inventaireService.editionEtatEcartAp(codeInventaire, optionImpression, type, group, global, withPrincipalUnity, categDepot);

        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    @GetMapping("/inventaires/editionEtatInventaire")
    public ResponseEntity<byte[]> editionEtatInventaire(
            @RequestParam Integer codeInventaire,
            @RequestParam TypeEnvoieEtatEnum optionImpression,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "group", required = false, defaultValue = "O") String group) throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {
        byte[] bytePdf = inventaireService.editionEtatInventaire(codeInventaire, optionImpression, type, group);
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    @GetMapping("/inventaires/inventairecontrole")
    public Boolean checkCategorieIsInventorie(@RequestParam("categArt") Integer categArt) {
        return inventaireService.checkCategorieIsInventorie(categArt);
    }

}
