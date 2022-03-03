/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
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

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.SatisfactionEnum;
import com.csys.pharmacie.transfert.domain.FactureBT;
import com.csys.pharmacie.transfert.service.BonTransfertInterDepotService;

import com.csys.pharmacie.transfert.domain.MvtStoBT;
import com.csys.pharmacie.transfert.dto.BonTransferInterDepotDTO;
import com.csys.pharmacie.transfert.dto.DetailsTransfertDTO;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;
import com.csys.pharmacie.transfert.dto.MvtstoBTDTO;
import com.csys.pharmacie.transfert.service.BonTransfertRecupService;
import com.csys.pharmacie.transfert.service.BonTransfertService;
import com.csys.util.RestPreconditions;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.PutMapping;

/**
 *
 * @author Farouk
 */
@RestController
@RequestMapping("/transfert-inter-depot")
public class TransfertInterDepController {

    @Autowired
    private BonTransfertInterDepotService bonTIDService;
    @Autowired
    private BonTransfertInterDepotService bonTransfertInterDepotService;

    @Autowired
    private BonTransfertService bonTransfertService;

    private static final String ENTITY_NAME = "transferRecup";

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<FactureBTDTO> addBonTransfert(@Valid @RequestBody FactureBTDTO dto, BindingResult bindingResult) throws MethodArgumentNotValidException, URISyntaxException {
        if (dto.getCode() != null) {
            bindingResult.addError(new FieldError("ReceivingDTO", "code", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        dto.setInterdpot(true);
        FactureBTDTO result = bonTIDService.addBonTransferInterDepot(dto);
        return ResponseEntity.created(new URI("/api/transfert-recup/" + result.getCode())).body(result);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public @ResponseBody
//    List<FactureBT> findByDatbonBetweens(@RequestParam("du") String du,
//            @RequestParam("au") String au, @RequestParam("stup") boolean stup) throws ParseException {
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datedeb = formatter.parse(du);
//        Date datefin = formatter.parse(au);
//        return bonTIDService.findByDatbonBetween("BT", datedeb, datefin, true, stup);
//    }
    @RequestMapping(value = "/{numBon}/details", method = RequestMethod.GET)
    public @ResponseBody
    List<MvtstoBTDTO> findDetails(@PathVariable("numBon") String numBon) {
        return bonTransfertService.findDetails(numBon);
    }

//    @RequestMapping(value = "/{numBon}", method = RequestMethod.GET)
//    public @ResponseBody
//    BonTransfertInterdepotConsultationDTO<CommandeInterneProjection> findByNumBon(@PathVariable("numBon") String numBon) {
//        return bonTIDService.consulterBonTID(numBon);
//    }
//    @RequestMapping(value = "/FamilleArticle/{famart}/{coddep}", method = RequestMethod.GET)
//    public @ResponseBody
//    List<QteMouvement> findQuantiteMouvement(@PathVariable("famart") String famart, @PathVariable("coddep") String coddep, @RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//        Date datedeb = new Date(deb);
//        Date datefin = new Date(fin);
//        return bonTIDService.findQuantiteMouvement(famart, coddep, datedeb, datefin);
//    }
//    @RequestMapping(value = "/FamilleArticle/Sortie/{famart}/{coddep}", method = RequestMethod.GET)
//    public @ResponseBody
//    List<QteMouvement> findQuantiteMouvementSortie(@PathVariable("famart") String famart, @PathVariable("coddep") String coddep, @RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//        Date datedeb = new Date(deb);
//        Date datefin = new Date(fin);
//        return bonTIDService.findQuantiteMouvementSortie(famart, coddep, datedeb, datefin);
//    }
//    @RequestMapping(value = "/Article/TotalMouvement/{codart}/{coddep}", method = RequestMethod.GET)
//    public Integer findTotalMouvement(@PathVariable("codart") String codart, @PathVariable("coddep") String coddep, @RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//
//        Date datedeb = new Date(deb);
//        Date datefin = new Date(fin);
//
//        return bonTIDService.findTotalMouvement(codart, coddep, datedeb, datefin);
//    }
//
//    @RequestMapping(value = "/Article/TotalMouvement/Sortie/{codart}/{coddep}", method = RequestMethod.GET)
//    public Integer findTotalMouvementSortie(@PathVariable("codart") String codart, @PathVariable("coddep") String coddep, @RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//
//        Date datedeb = new Date(deb);
//        Date datefin = new Date(fin);
//
//        return bonTIDService.findTotalMouvementSortie(codart, coddep, datedeb, datefin);
//    }
//    
//     @RequestMapping(value = "findAll", method = RequestMethod.GET)
    @GetMapping
    public @ResponseBody
    List<BonTransferInterDepotDTO> findAll(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "satisfactions", required = false) List<SatisfactionEnum> satisfactions,
            @RequestParam(name = "depot-id-src", required = false) Integer depotID_src,
            @RequestParam(name = "depot-id-des", required = false) Integer depotID_des,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "interdepot", required = true, defaultValue = "true") Boolean interdepot,
            @RequestParam(name = "avoirTransfert", required = true, defaultValue = "false") Boolean avoirTransfert,
            @RequestParam(name = "deleted", required = true, defaultValue = "false") Boolean deleted,
            @RequestParam(name = "perime", required = false) Boolean perime,
            @RequestParam(name = "validated", required = false) Boolean validated,
            @RequestParam(name = "conforme", required = false) Boolean conforme)
            throws ParseException {

        return bonTransfertInterDepotService.findAll(categ, fromDate, toDate, interdepot, avoirTransfert, satisfactions, depotID_src, depotID_des, deleted, perime, validated,conforme);

    }

    @GetMapping("/{numBon}")
    public ResponseEntity<BonTransferInterDepotDTO> getTransfert(@PathVariable String numBon) {

        BonTransferInterDepotDTO dto = bonTransfertInterDepotService.findOne(numBon);
        RestPreconditions.checkFound(dto, "factureBT.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/edition")
    public ResponseEntity<byte[]> editionInterTransfert(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "depotSrcId", required = false) Integer depotID_src,
            @RequestParam(name = "depotDestId", required = false) List<Integer> depotIDS_dest,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "groupByArticle", required = false) Boolean groupByArticle,
            @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "conforme", required = false) Boolean conforme,
            @RequestParam(name = "detailed", required = false) Boolean detailed) throws URISyntaxException, ReportSDKException, IOException, SQLException {
//            @RequestParam(name = "interdepot", required = true, defaultValue = "true") Boolean interdepot) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        byte[] bytePdf = bonTransfertService.editionDetailsTransfert(categ, depotID_src, depotIDS_dest, fromDate, toDate, type, groupByArticle,filter,conforme,detailed);
//              byte[] bytePdf  = bonTransfertService.editionDetailsTransfert(categ,depotID_src, depotIDS_dest, fromDate , toDate,type);
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

    @GetMapping(value = "/findInterTransfert")
    public ResponseEntity<List<DetailsTransfertDTO>> findInterTransfert(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "depotSrcId", required = false) Integer depotID_src,
            @RequestParam(name = "depotDestId", required = false) List<Integer> depotIDS_dest,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "groupByArticle", required = false) Boolean groupByArticle,
              @RequestParam(name = "filter", required = false) String filter,
            @RequestParam(name = "conforme", required = false) Boolean conforme,
            @RequestParam(name = "detailed", required = false) Boolean detailed) throws URISyntaxException, ReportSDKException, IOException, SQLException {
//            @RequestParam(name = "interdepot", required = true, defaultValue = "true") Boolean interdepot) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        List<DetailsTransfertDTO> dto = bonTransfertService.findDetailTransfert(categ, depotID_src, depotIDS_dest, fromDate, toDate, groupByArticle,filter,conforme, detailed);

        return ResponseEntity.ok().body(dto);

    }

}
