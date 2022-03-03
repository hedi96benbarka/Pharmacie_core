/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.service.FactureBAService;
import com.csys.pharmacie.achat.service.ReceivingService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.EmailDTO;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.csys.pharmacie.transfert.dto.MvtstoBTDTO;
import com.csys.pharmacie.transfert.service.BonTransfertInterDepotService;
import com.csys.pharmacie.transfert.service.BonTransfertRecupService;
import com.csys.pharmacie.transfert.service.BonTransfertService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Farouk
 */
@RestController
@RequestMapping("/Transfert")

public class TransfertController {

    private final Logger log = LoggerFactory.getLogger(ReceivingService.class);

    @Autowired
    private BonTransfertService bonTransfertService;
    @Autowired
    private FactureBAService facture;
//    @Autowired
//    private BonTransfertInterDepotService bonTransfertInterDepotService;
//
//    @Autowired
//    private BonTransfertRecupService BonTransfertRecupService;

//    @RequestMapping(method = RequestMethod.GET)
//    public @ResponseBody
//     List<? extends FactureBTDTO> findAll(
//            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
//            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
//            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
//            @RequestParam(name = "interdepot", required = true, defaultValue = "false") Boolean interdepot)
//            throws ParseException {
//        if (interdepot) {
//            return bonTransfertInterDepotService.findAll(categ, fromDate, toDate, interdepot);
//        } else {
//            return BonTransfertRecupService.findAll(categ, fromDate, toDate, interdepot);
//        }
//
//    }
    @GetMapping
    public @ResponseBody
    List<FactureBTDTO> findAll(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "interdepot", required = false) Boolean interdepot,
            @RequestParam(name = "avoirTransfert", required = false) Boolean avoirTransfert,
            @RequestParam(name = "deleted", required = true, defaultValue = "false") Boolean deleted,
            @RequestParam(name = "validated", required = false) Boolean validated,
            @RequestParam(name = "conforme", required = false) Boolean conforme,
            @RequestParam(name = "depotDest", required = false) Integer depotDest,
           @RequestParam(name = "depotSrc", required = false) Integer depotSrc )
            throws ParseException {

        return bonTransfertService.findAll(categ, fromDate, toDate, interdepot, avoirTransfert, deleted, validated, conforme, depotDest,depotSrc);

    }

    @RequestMapping(value = "/not-validated", method = RequestMethod.GET)
    public @ResponseBody
    List<FactureBTDTO> findNotValidatedTransfertsForInventory(
            @RequestParam(name = "depot", required = false) Integer depot,
            @RequestParam(name = "categorie-depots", required = false) CategorieDepotEnum categ) throws ParseException {

        return bonTransfertService.findNotValidatedTransfertsForInventory(depot, categ);

    }

    /**
     * GET /factures/{numBon}/details : get the "numBon" details bon transfert.
     *
     * @param numBon
     * @return the ResponseEntity with status 200 (OK) and with body of facture,
     * or with status 404 (Not Found)
     */
    @RequestMapping(value = "/{numBon}/details", method = RequestMethod.GET)
    public @ResponseBody
    List<MvtstoBTDTO> findDetailsTransfert(@PathVariable("numBon") String numBon) {
        return bonTransfertService.findDetails(numBon);
    }

    /**
     * GET /factures/{numBon}/edition : get the "numBon" edition bon transfert.
     *
     * @param numBon
     * @param simpleHeader
     * @param detailed
     * @return the ResponseEntity with status 200 (OK) and with body of facture,
     * or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @RequestMapping(value = "/{numBon}/edition", method = RequestMethod.GET)
    public ResponseEntity<byte[]> editionTransfert(@PathVariable("numBon") String numBon,
            @RequestParam(name = "simple-header", defaultValue = "false", required = false) Boolean simpleHeader,
            @RequestParam(name = "detailed", defaultValue = "false", required = false) Boolean detailed) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition transfert: {}", numBon);
        byte[] bytePdf = bonTransfertService.edition(numBon, simpleHeader, detailed);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

    @PutMapping(value = "/{numBon}/validate")
    public ResponseEntity<FactureBTDTO> validateTransfert(@PathVariable("numBon") String numBon, @RequestBody FactureBTDTO factureBTDTO) throws ParseException {

        FactureBTDTO result = bonTransfertService.validateTransfer(factureBTDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/Notify")
    public ResponseEntity<EmailDTO> notifyTransfert(@Valid @RequestBody String id, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
        log.debug("REST request to notify Transfert Validation : {}", id);
        EmailDTO result = bonTransfertService.notify(id);
        return ResponseEntity.ok().body(result);
    }
//       @PostMapping("/NotifyBa")
//    public ResponseEntity<EmailDTO> notifyTransfertBA() throws URISyntaxException, MethodArgumentNotValidException {
//        //log.debug("REST request to notifyAfterUpdateSupplier Transfert Validation : {}", id);
//        List exp=new ArrayList<String>();
//           List exp2=new ArrayList<String>();
//        exp.add("chourouk.clinisys");
//        exp2.add("12260");
//        EmailDTO result =facture.notifyAfterUpdateSupplier(exp, "Bi000019",exp2,"PHPHAO20000181") ;
//        return ResponseEntity.ok().body(result);
//    }
    /**
     * GET /factures/{numBon} : get the "numBon" bon transfert.
     *
     * @param numBon
     * @return the ResponseEntity with status 200 (OK) and with body of facture,
     * or with status 404 (Not Found)
     */
//    @GetMapping("/{numBon}")
//    public ResponseEntity<? extends FactureBTDTO> getTransfert(@PathVariable String numBon) {
//        log.debug("Request to get Facture: {}", numBon);
//        FactureBTDTO dto = bonTransfertService.findOne(numBon);
//        RestPreconditions.checkFound(dto, "factureBT.NotFound");
//        return ResponseEntity.ok().body(dto);
//    }
     @GetMapping("/facturepr/lastFactureBTByCodart")
    public List<FactureBTDTO> findTopFactureBTByCodart(
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categDepot,                                                                                                                                              
            @RequestParam(name = "wharehouse-dest-Id", required = true) Integer codeDepot,
            @RequestParam(name = "item-id", required = true) Integer codeArticle 
            
    ) {

        return bonTransfertService.findLastFactureBTByCodart(categDepot, codeDepot, codeArticle);
    }
}
