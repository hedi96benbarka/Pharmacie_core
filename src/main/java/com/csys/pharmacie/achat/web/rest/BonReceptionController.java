package com.csys.pharmacie.achat.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.csys.pharmacie.achat.domain.MvtstoBACodartFrs;
import com.csys.pharmacie.achat.dto.BonRecepDTO;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.achat.dto.PriceVarianceDTO;
import com.csys.pharmacie.achat.dto.ReceptionDetailCADTO;
import com.csys.pharmacie.achat.dto.ReceptionEditionDTO;
import com.csys.pharmacie.achat.service.FactureBAService;
import com.csys.pharmacie.achat.service.MvtStoBAService;
import com.csys.pharmacie.achat.service.ReceptionDetailCAService;
import com.csys.pharmacie.achat.service.ReceptionTemporaireService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.EmailDTO;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.util.RestPreconditions;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/reception")
public class BonReceptionController {

    @Autowired
    private FactureBAService factureBAService;

    @Autowired
    private MvtStoBAService mvtStoBAService;

    @Autowired
    ReceptionDetailCAService receptionDetailCAService;
    @Autowired
    ReceptionTemporaireService receptionTemporaireService;
    private final Logger log = LoggerFactory.getLogger(BonReceptionController.class);

    /**
     * find all receptions based on the passed arguments
     *
     * @param categ-depot
     * @param fromDate the begining date
     * @param toDate the end date
     * @param deleted if true return the deleted receptions
     * @param automatique
     * @param invoiced
     * @param codeFrs supplier id
     * @param codeDep warehouse id
     * @param codeArticle
     * @return list of returns
     */
    @GetMapping
    public List<BonRecepDTO> findAll(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "deleted", required = false) Boolean deleted,
            @RequestParam(name = "automatique", required = false) Boolean automatique,
            @RequestParam(name = "invoiced", required = false) Boolean invoiced,
            @RequestParam(name = "code-frs", required = false) String codeFrs,
            @RequestParam(name = "code-depot", required = false) Integer codeDep,
            @RequestParam(name = "code-article", required = false) Integer codeArticle
    ) {
        FactureBA queriedFacBA = new FactureBA();
        queriedFacBA.setCodfrs(codeFrs);
        queriedFacBA.setCoddep(codeDep);
        queriedFacBA.setTypbon(TypeBonEnum.BA);
        queriedFacBA.setCategDepot(categ);
        queriedFacBA.setAutomatique(automatique);
        return factureBAService.findAllReception(queriedFacBA, fromDate, toDate, deleted, invoiced, codeArticle);
    }

    @GetMapping("/reference-fournisseur")//TODO CHANGE THIS METHOD AND THE ABOVE ONE WITH A SINGLE FINDALL METHODE THAT USES FILTERS
    public @ResponseBody
    Boolean findByrsAndRef(
            @RequestParam("reference") String coddep,
            @RequestParam("codfrs") String codFrs) {
        return factureBAService.checkExistanceRefFournisseur(codFrs, coddep);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BonRecepDTO> getEtatReceptionCA(@PathVariable String id) {
        BonRecepDTO dto = factureBAService.findOneReception(id);
        RestPreconditions.checkFound(dto, "reception.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /reception/edition/{id} : get the "id" receiving.
     *
     * @param id the id of the reception to imprim
     * @return the ResponseEntity with status 200 (OK) and with body of
     * receiving, or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/edition/{id}")
    public ResponseEntity<byte[]> getEditionReception(@PathVariable String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = factureBAService.editionReceptionRetour(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    /**
     * GET /reception/editions : get the edition list of reception.
     *
     * @param categ
     * @param fromDate
     * @param toDate
     * @param order
     * @param type
     * @param avoir
     * @param annule
     * @return the ResponseEntity with status 200 (OK) and with body of
     * receiving, or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/editions")
    public ResponseEntity<byte[]> editionListeBonReception(@RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "order", required = true) String order,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "avoir", required = true, defaultValue = "false") boolean avoir,
            @RequestParam(name = "annule", required = true, defaultValue = "false") List<Boolean> annule
    ) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = factureBAService.editionListeBonReception(fromDate, toDate, categ, order, type, avoir, annule);
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

    @PostMapping("/searches/recived-purchase-orders")
    public @ResponseBody
    List<ReceptionDetailCADTO> findRecivedPurchaseOrders(@ApiParam(value = "List of codes of the purchase orders (commande achat). It must not be null. If empty it will return all the recived purchase orders") @RequestBody(required = true) List<Integer> codes,
            @ApiParam(value = "If \"true\" the api will return the the recived purchase orders that are not in the liste of codes") @RequestParam(name = "notIn", required = false) Boolean notIn) {
        return receptionDetailCAService.searchByCodesCA(codes, notIn);
    }

    @RequestMapping(value = "/{numbon}/details", method = RequestMethod.GET)
    public @ResponseBody
    List<MvtstoBADTO> findListDetailByNumBonRecep(@PathVariable("numbon") String numbonrecep) {
        return mvtStoBAService.getDetailsReception(numbonrecep);
    }

    @RequestMapping(value = "/{numbon}/returned-details", method = RequestMethod.GET)
    public @ResponseBody
    Set<MvtstoBADTO> findReturnedDetailByNumBonRecep(@PathVariable("numbon") String numbonrecep) {
        return factureBAService.findReturnedDetailsOfReception(numbonrecep);
    }

    @PostMapping("/validate-reception-temporaires")
    public ResponseEntity<BonRecepDTO> validateReceptionTemporaire(@Valid @RequestBody BonRecepDTO receptionDTO) throws URISyntaxException, MethodArgumentNotValidException, ParseException {
        log.debug("REST request to save ReceptionTemporaire : {}", receptionDTO);

        /*  log.debug("receptiontemporaireDTO.getNumbon() estv {}",receptionDTO.getNumbon());
        if (receptionDTO.getNumbon() != null ) {
            bindingResult.addError(new FieldError("ReceptionTemporaireDTO", "numbon", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }*/
        BonRecepDTO result = factureBAService.ajoutReeceptionSuiteValidationReceptionTemporaire(receptionDTO);
        return ResponseEntity.created(new URI("/api/validatereceptiontemporaires/" + result.getNumbon())).body(result);
    }

    @PostMapping("/save-reception")
    public @ResponseBody
    ResponseEntity<BonRecepDTO> saveBonReceptionOrReceptionTemporaire(@Valid @RequestBody BonRecepDTO bonReception) throws ParseException, URISyntaxException {
        BonRecepDTO result = factureBAService.saveBonReceptionOrReceptionTemporaire(bonReception);
        return ResponseEntity.created(new URI("/api/save-reception/" + result.getNumbon())).body(result);
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<BonRecepDTO> addBonRecep(@Valid @RequestBody BonRecepDTO b) throws ParseException, URISyntaxException {

        BonRecepDTO result = factureBAService.ajoutBonReception(b);
        return ResponseEntity.created(new URI("/api/reception/" + result.getNumbon())).body(result);
    }

    @PutMapping("/{id}")
    public @ResponseBody
    ResponseEntity<BonRecepDTO> addBonRecep(@PathVariable("id") String id, @Valid @RequestBody BonRecepDTO b) throws ParseException, URISyntaxException {
        b.setNumbon(id);
        BonRecepDTO result = factureBAService.updateBonReception(b);
        return ResponseEntity.created(new URI("/api/reception/" + result.getNumbon())).body(result);
    }

    @ApiOperation("the method returns list of reception voucher on the items passed in the body")
    @RequestMapping(value = "/Article", method = RequestMethod.POST)
    public @ResponseBody
    List<MvtstoBACodartFrs> findRecepByListArt(@RequestBody List<Integer> codarts) {

        return mvtStoBAService.findByCodArtIn(codarts);
    }

    @ApiOperation("the method returns list of all received items by categorie depot")
    @GetMapping("/Article")
    public @ResponseBody
    List<Integer> findReceivedItemsByCategorieDepot(@RequestParam(name = "categorie-depot", required = true) CategorieDepotEnum categDepot) {
        return mvtStoBAService.findReceivedItemsByCategorieDepot(categDepot);
    }

    @ApiOperation("the method returns list of all received items by categorie depot and by articleIDs in ")
    @PostMapping("/ArticleIn")
    public @ResponseBody
    List<Integer> findReceivedItemsByCategorieDepotAndCodesArticleIn(@RequestParam(name = "categorie-depot", required = true) CategorieDepotEnum categDepot, @RequestBody List<Integer> articleIDs) {
        return mvtStoBAService.findReceivedItemsByCategorieDepotAndCodesArticleIn(categDepot, articleIDs);
    }

    @RequestMapping(value = "/Article/getDernierPrixAchat", method = RequestMethod.GET)
    public @ResponseBody
    BigDecimal findRecepByListArt(@RequestParam("codart") String codart) {

        return mvtStoBAService.getDernierPrixAchat(codart);
    }
//
//    @RequestMapping(value = "/MouvementDuJour", method = RequestMethod.GET)
//    public MouvementDuJour findMouvementDuJour(@RequestParam("stup") boolean stup) throws ParseException {
//        return factureBAService.findMouvementDuJour("BA", stup);
//    }

    @PutMapping(value = "/cancel/{id}")
    public BonRecepDTO cancelReception(@PathVariable("id") String numBon) throws ParseException {

        return factureBAService.cancelBonReception(numBon);
    }

    @GetMapping("/edition/bonReceptionNonFactures")
    public ResponseEntity<byte[]> editionListeBonReceptionNonFactures(
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
            @RequestParam(name = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime date,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type)
            throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = factureBAService.editionListeBonReceptionNonFactures(categ, date, type);
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

    @GetMapping("/bonReceptionNonFactures")
    public List<ReceptionEditionDTO> ListeBonReceptionNonFactures(
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
            @RequestParam(name = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime date) {
        log.debug(" rest request to get liste des bons de receptions non facturés a la date {}", date);
        return factureBAService.findListReceptionsNonFactures(categ, date);

    }

    @GetMapping("/Price-variance")
    public List<PriceVarianceDTO> getPriceVariance(
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ-depot", required = true) CategorieDepotEnum categDepot,
            @RequestParam(name = "categ-article", required = false) Integer categorieArticle) {

        return mvtStoBAService.calculPriceVariance(fromDate, toDate, categDepot, categorieArticle);

    }

    @GetMapping("/detail-reception/exists")
    public Boolean checkExistenceByCodartAndIsPrixReference(
            @RequestParam(name = "codeArticle", required = true) Integer codeArticle,
            @RequestParam(name = "isPrixReference", required = false) Boolean isPrixReference) {
        log.debug(" rest request to check existence of detail Reception avec codeArticle {} et prixReference {}", codeArticle, isPrixReference);
        return mvtStoBAService.exitsByCodartAndIsPrixReference(codeArticle, isPrixReference);

    }

    @GetMapping("/receptions-avec-tva")
    public List<ReceptionEditionDTO> getListeReceptionsAvecTva(
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categDepot,
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "orderDateBon", required = true) Boolean orderDateBon,
            @RequestParam(name = "avoir", required = true, defaultValue = "false") Boolean avoir,
            @RequestParam(name = "annule", required = true, defaultValue = "false") List<Boolean> annule
    ) {
        return factureBAService.findListeBonReceptionAvecTVa(fromDate, toDate, categDepot, orderDateBon, avoir, annule);
    }

    @PutMapping("/{numbon}/{codeFrs}")
    public @ResponseBody
    ResponseEntity<BonRecepDTO> updateFournisseurInReception(@PathVariable("numbon") String numbon, @PathVariable("codeFrs") String codeFrs) throws ParseException, URISyntaxException {
        BonRecepDTO result = factureBAService.updateFournisseurBonReception(numbon, codeFrs);
        return ResponseEntity.created(new URI("/api/reception/" + result.getNumbon())).body(result);
    }

    @GetMapping("/type-reception")
    public boolean getTypeReception() {
        return factureBAService.getTypeReception();
    }
        @PostMapping("/notify")
    public ResponseEntity<EmailDTO> notifyTransfert(@RequestBody BonRecepDTO bonReceptDto) {
        log.debug("REST request to notify Transfert Validation : {}", bonReceptDto);
        EmailDTO result = factureBAService.notifyAfterUpdateSupplier(bonReceptDto);
        return ResponseEntity.ok().body(result);
    }
//    @ApiOperation("this method is used to test method managing DetailCa suite validation not for prod")
//    @PostMapping("/managing-reception-detail-ca")
//    public @ResponseBody
//    List<ReceptionDetailCA> managingReceptionDetailCADTOSuiteValidation(@Valid @RequestBody BonRecepDTO bonRecepDTO) {
//        String Numbon = "UUUUAO20000498";
//        return factureBAService.managingPurchaseOrdersForReception(bonRecepDTO, Numbon);
//    }
}
