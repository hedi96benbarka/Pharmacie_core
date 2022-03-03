package com.csys.pharmacie.achat.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.dto.BonRetourDTO; 
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csys.pharmacie.achat.service.FactureBAService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.util.RestPreconditions;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/BonRetour")
public class BonRetourController {

//    @Autowired
//    private MvtStoBAService MvtStoBAService;
    @Autowired
    private FactureBAService factureBAService;
//    @Autowired
//    private FactureBAdao factureBAdao;

//   	@RequestMapping(value = "/FamilleArticle/{famart}/{coddep}", method = RequestMethod.GET)
//   	public @ResponseBody List<QteMouvement> findQuantiteMouvement(@PathVariable("famart") String famart,@PathVariable("coddep") String coddep,@RequestParam("debut") Long deb, @RequestParam("fin") Long fin) throws ParseException {
//   		Date datedeb = new Date(deb);
//		Date datefin = new Date(fin);		
//		return MvtStoBAService.findQuantiteMouvementRetour(famart,coddep,datedeb,datefin);
//	}TODO qte mouvementée par famille
    /**    
     * find all returns based on the passed arguments
     *
     * @param categ-depot
     * @param fromDate the begining date
     * @param toDate the end date
     * @param codeArticle
     * @return list of returns
     */
    @GetMapping
    public List<BonRetourDTO> findAll(
            @RequestParam(name = "categ-depot", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "code-article", required = false) Integer codeArticle) {
        
        FactureBA queriedFacBA = new FactureBA();
        queriedFacBA.setTypbon(TypeBonEnum.RT);
        queriedFacBA.setCategDepot(categ);
        return factureBAService.findAllReturns(queriedFacBA, fromDate, toDate,codeArticle);
    }

    
    /**
     * GET /Retour/edition/{id} : get the "id" Retour.
     *
     * @param id the id of the Retour to imprim
     * @return the ResponseEntity with status 200 (OK) and with body of
     * receiving, or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/edition/{id}")
    public ResponseEntity<byte[]> getEditionRetour(@PathVariable String id) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = factureBAService.editionReceptionRetour(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }
    /**
     *
     * @param bonretourDTO
     * @return
     */
    @PostMapping
    public @ResponseBody
    ResponseEntity<BonRetourDTO> ajoutbonlivraisonPH(@RequestBody BonRetourDTO bonretourDTO) throws URISyntaxException, ParseException {
        BonRetourDTO result = factureBAService.ajoutBonRetour(bonretourDTO);
        return ResponseEntity.created(new URI("/api/receptiondetailcas/" + bonretourDTO.getNumbon())).body(result);
    }

//    @RequestMapping(value = "/MouvementDuJour", method = RequestMethod.GET)
//    public MouvementDuJour findMouvementDuJour(@RequestParam("stup") boolean stup) throws ParseException {
//        return factureBAService.findMouvementDuJour("RT", stup);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<BonRetourDTO> findOne(@PathVariable String id) {
        BonRetourDTO dto = factureBAService.findOneRetour(id);
        RestPreconditions.checkFound(dto, "reception.NotFound");
        return ResponseEntity.ok().body(dto);
    }
}
