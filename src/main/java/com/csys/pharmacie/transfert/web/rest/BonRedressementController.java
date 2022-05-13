/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.csys.pharmacie.transfert.domain.FactureBE;
import com.csys.pharmacie.transfert.dto.FactureBEDTO;
import com.csys.pharmacie.transfert.dto.MvtStoBEDTO;
import com.csys.pharmacie.transfert.service.FactureBEService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Farouk
 */
@RestController
@RequestMapping("/redressement")
public class BonRedressementController {

    @Autowired
    private FactureBEService factureBEService;

    private static final String ENTITY_NAME = "redressement";

//    @RequestMapping(method = RequestMethod.POST)
//    public @ResponseBody
//    Boolean addBonRedressement(@RequestBody BonRedressementDTO dto) throws ParseException {
//        return bonRedressementService.addBonRedressement(dto);
//
//    }
    @PostMapping
    public @ResponseBody
    ResponseEntity<FactureBEDTO> addRedressement(@Valid @RequestBody FactureBEDTO dto, BindingResult bindingResult) throws MethodArgumentNotValidException, URISyntaxException {

        if (dto.getNumbon() != null) {
            bindingResult.addError(new FieldError("FactureBEDTO", "code", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        FactureBEDTO result = factureBEService.save(dto);

        return ResponseEntity.created(new URI("/api/transfert-recup/" + result.getNumbon())).body(result);
    }

    @GetMapping
    public List<FactureBEDTO> findAll(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "code-depot", required = false) Integer codeDep
    ) {
        FactureBE queriedFacBE = new FactureBE();
        queriedFacBE.setCoddep(codeDep);
        queriedFacBE.setCategDepot(categ);
        return factureBEService.findAll(queriedFacBE, fromDate, toDate, false);
    }

//    @RequestMapping(value = "/findByCoddepAndDatbonBetween", method = RequestMethod.GET)
//    public @ResponseBody
//    List<FactureBEProjection> findByDatbonBetween(@RequestParam("coddep") List<String> coddep,
//            @RequestParam("debut") String deb, @RequestParam("fin") String fin, @RequestParam("stup") boolean stup) throws ParseException {
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date datedeb = formatter.parse(deb);
//        Date datefin = formatter.parse(fin);
//
//        return bonRedressementService.findByCoddepAndDatbonBetween(coddep, datedeb, datefin, stup);
//    }

    @GetMapping("/{numbon}")
    public @ResponseBody
    FactureBEDTO findByNumbon(@PathVariable("numbon") String numbon) {
        return factureBEService.findOne(numbon);
    }

    @RequestMapping(value = "/{numbon}/details", method = RequestMethod.GET)
    public @ResponseBody
    List<MvtStoBEDTO> findDetailBonRedressement(@PathVariable("numbon") String numbon) {
        return factureBEService.findByNumBon(numbon);
    }

    @GetMapping("/BonRedressement/edition/{numBon}")
    public ResponseEntity<byte[]> getEditionReceiving(@PathVariable String numBon) throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {
        byte[] bytePdf = factureBEService.edition(numBon);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }
//    @RequestMapping(value = "/Motif", method = RequestMethod.GET)
//    public @ResponseBody
//    List<MotifRedressement> findDetailBonRedressement() {
//        return bonRedressementService.findAllMotif();
//    }

//    @RequestMapping(value = "/FamilleArticle/{famart}/{coddep}", method = RequestMethod.GET)
//    public @ResponseBody
//    List<QteMouvement> findQuantiteMouvement(@PathVariable("famart") String famart, @PathVariable("coddep") String coddep, @RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//        Date datedeb = new Date(deb);
//        Date datefin = new Date(fin);
//        return bonRedressementService.findQuantiteMouvement(famart, coddep, datedeb, datefin);
//    }
//
//    @RequestMapping(value = "/FamilleArticle/Sortie/{famart}/{coddep}", method = RequestMethod.GET)
//    public @ResponseBody
//    List<QteMouvement> findQuantiteMouvementSortie(@PathVariable("famart") String famart, @PathVariable("coddep") String coddep, @RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//        Date datedeb = new Date(deb);
//        Date datefin = new Date(fin);
//
//        return bonRedressementService.findQuantiteMouvementSortie(famart, coddep, datedeb, datefin);
//    }
//
//    @RequestMapping(value = "/Article/TotalMouvement/{codart}/{coddep}", method = RequestMethod.GET)
//    public Integer findTotalMouvement(@PathVariable("codart") String codart, @PathVariable("coddep") String coddep, @RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//
//        Date datedeb = new Date(deb);
//        Date datefin = new Date(fin);
//
//        return bonRedressementService.findTotalMouvement(codart, coddep, datedeb, datefin);
//    }
//
//    @RequestMapping(value = "/Article/TotalMouvement/Sortie/{codart}/{coddep}", method = RequestMethod.GET)
//    public Integer findTotalMouvementSortie(@PathVariable("codart") String codart, @PathVariable("coddep") String coddep, @RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//
//        Date datedeb = new Date(deb);
//        Date datefin = new Date(fin);
//
//        return bonRedressementService.findTotalMouvementSortie(codart, coddep, datedeb, datefin);
//    }
//
//    @RequestMapping(value = "/MouvementDuJour", method = RequestMethod.GET)
//    public MouvementDuJour findMouvementDuJour(@RequestParam("stup") boolean stup) throws ParseException {
//        return bonRedressementService.findMouvementDuJour(stup);
//    }
}
