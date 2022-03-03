/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.DetailEditionDTO;
import com.csys.pharmacie.achat.service.FactureDirecteService;
import com.csys.pharmacie.achat.service.ReceivingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.stock.domain.BaseMouvementStock;
import com.csys.pharmacie.stock.service.EditionService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Farouk
 */
@RestController
@RequestMapping("/edition")
public class EditionResource {

    private final Logger log = LoggerFactory.getLogger(ReceivingService.class);

    @Autowired
    private EditionService editionService;
    @Autowired
    private FactureDirecteService factureDirecteService;

    /**
     * GET /edition/article.
     *
     * @param codArt
     * @param det
     * @param categ
     * @param type
     * @return the ResponseEntity with status 200 (OK) and with body of edition,
     * or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/article")
    public ResponseEntity<byte[]> getEditionDetailsStockArticle(@RequestParam("codArt") Integer codArt,
            @RequestParam("det") String det,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ, @RequestParam(name = "type", required = false, defaultValue = "P") String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition details stock article: {}", codArt);
        byte[] bytePdf = editionService.editionDetailsStockArticle(codArt, det, categ, type);
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

    /**
     * GET /edition/article.
     *
     * @param coddep
     * @param categorieArticle
     * @param det
     * @param categ
     * @param type
     * @return the ResponseEntity with status 200 (OK) and with body of edition,
     * or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/valeurStock")
    public ResponseEntity<byte[]> getEditionValeurStock(@RequestParam("coddep") String coddep,
            @RequestParam(name = "categorieArticle", required = false) Integer categorieArticle,
            @RequestParam("det") String det,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ, @RequestParam(name = "type", required = false, defaultValue = "P") String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition details stock depot: {}", coddep);
        List<Integer> coddeps = new ArrayList<>();
        if ("".equals(coddep)) {
            coddeps = null;
        } else {
            String[] parts = coddep.split(",");
            for (String part : parts) {
                coddeps.add(new Integer(part).intValue());
            }
        }
        byte[] bytePdf = editionService.editionValeurStock(coddeps, categorieArticle, det, categ, type);
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

    /**
     * GET /edition/article.
     *
     * @param coddep
     * @param categ
     * @param type
     * @param date
     * @return the ResponseEntity with status 200 (OK) and with body of edition,
     * or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/stockPrime")
    public ResponseEntity<byte[]> getEditionStockPrime(@RequestParam("coddep") String coddep,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ, @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition details stock depot prime: {}", coddep);
        List<Integer> coddeps = new ArrayList<>();
        if ("".equals(coddep)) {
            coddeps = null;
        } else {
            String[] parts = coddep.split(",");
            for (String part : parts) {
                coddeps.add(new Integer(part).intValue());
            }
        }

        byte[] bytePdf = editionService.editionStockPrime(coddeps, categ, type, date);
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

    /**
     * GET /edition/article.
     *
     * @param coddep
     * @param categ
     * @param fromDate
     * @param toDate
     * @return the ResponseEntity with status 200 (OK) and with body of edition,
     * or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/stockProchainementPrime")
    public ResponseEntity<byte[]> getEditionStockProchainementPrime(@RequestParam("coddep") String coddep,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ, @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition details stock depot: {}", coddep);
        List<Integer> coddeps = new ArrayList<>();
        if ("".equals(coddep)) {
            coddeps = null;
        } else {
            String[] parts = coddep.split(",");
            for (String part : parts) {
                coddeps.add(new Integer(part).intValue());
            }
        }

        byte[] bytePdf = editionService.editionStockProchainementPrime(coddeps, categ, type, fromDate, toDate);
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

    /**
     * GET /edition/ficheStock.
     *
     * @param codArt
     * @param coddep
     * @param fromDate
     * @param toDate
     * @param categ
     * @param type
     * @param typeDate
     * @return the ResponseEntity with status 200 (OK) and with body of edition,
     * or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
//    @GetMapping("/ficheStock")
//    public ResponseEntity<byte[]> getEditionDetailsStockArticle(@RequestParam("codArt") Integer codArt,
//            @RequestParam("coddep") Integer coddep,
//            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
//            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
//            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ, @RequestParam(name = "type", required = false, defaultValue = "P") String type,
//            @RequestParam(name = "typeDate", required = true, defaultValue = "WITHOUT_DATE") TypeDateEnum typeDate) throws URISyntaxException, ReportSDKException, IOException, SQLException {
//        log.debug("Request to get edition details stock article: {}", codArt);
//        byte[] bytePdf = editionService.editionFicheStock(categ, type, codArt, coddep, fromDate, toDate, typeDate);
//        if (type.equalsIgnoreCase("P")) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            return ResponseEntity.ok().headers(headers).body(bytePdf);
//        } else {
//            HttpHeaders headers = new HttpHeaders();
//            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
//            headers.setContentType(excelMediaType);
//            return ResponseEntity.ok().headers(headers).body(bytePdf);
//        }
//    }
    /**
     * GET /edition/etatAchats
     *
     * @param codart
     * @param codFrs
     * @param fromDate
     * @param toDate
     * @param categ
     * @param type
     * @param groupby
     * @param categorieArticle
     * @return the ResponseEntity with status 200 (OK) and with body of
     * receiving, or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/etatAchats")
    public ResponseEntity<byte[]> getEditionEtatAchats(
            @RequestParam(name = "codArt", required = false) Integer codart,
            @RequestParam(name = "codFrs", required = false) String codFrs,
            @RequestParam(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam("groupby") String groupby,
            @RequestParam(name = "categorieArticle", required = false) Integer categorieArticle) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        byte[] bytePdf = editionService.editionEtatAchat(codart, codFrs, fromDate, toDate, categ, type, groupby, categorieArticle);
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
      @GetMapping("/suivi-achats")
    public Set<DetailEditionDTO> findlisteAchats(
            @RequestParam(name = "codArt", required = false) Integer codart,
            @RequestParam(name = "codFrs", required = false) String codFrs,
            @RequestParam(name = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
           
            @RequestParam(name = "categorieArticle", required = false) Integer categorieArticle) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        Set<DetailEditionDTO> listeAchats = editionService.listeEtatAchat(codart, codFrs, fromDate, toDate, categ, categorieArticle);
        return listeAchats;
    }
    
    //    @GetMapping("/mouvementStock")
//    public ResponseEntity<byte[]> getEditionMvtStockArticle(@RequestParam("coddep") Integer coddep,
//            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
//            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
//            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ, @RequestParam(name = "type", required = false, defaultValue = "P") String type,
//            @RequestParam(name = "categorieArticle", required = false) Integer categorieArticle) throws URISyntaxException, ReportSDKException, IOException, SQLException {
//        log.debug("Request to get edition mvt stock");
//        byte[] bytePdf = editionService.editionMvtStock(categ, type, coddep, fromDate, toDate, categorieArticle);
//        log.debug(bytePdf.toString());
//        if (type.equalsIgnoreCase("P")) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_PDF);
//            return ResponseEntity.ok().headers(headers).body(bytePdf);
//        } else {
//            HttpHeaders headers = new HttpHeaders();
//            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
//            headers.setContentType(excelMediaType);
//            return ResponseEntity.ok().headers(headers).body(bytePdf);
//        }
//    }
    @GetMapping("/detectionStock")
    public ResponseEntity<byte[]> getEditionDetectionStockArticle(
            @RequestParam("coddep") Integer coddep,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition mvt stock");
        byte[] bytePdf = editionService.editionDetectionStock(categ, type, coddep, fromDate, toDate);

        if (type.equalsIgnoreCase("E")) {
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return ResponseEntity.ok().headers(headers).body(bytePdf);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return ResponseEntity.ok().headers(headers).body(bytePdf);
        }
    }

    @GetMapping("/valeurStockDate")
    public ResponseEntity<byte[]> getEditionStockALaDate(@RequestParam("coddep") String coddep,
            @RequestParam("glob") String glob,
            @RequestParam("detailsPrix") String detailsPrix,
            @RequestParam("datePer") String datePer,
            @RequestParam(name = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ, @RequestParam(name = "type", required = false, defaultValue = "P") String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition details stock article: {}", coddep);
        List<Integer> coddeps = new ArrayList<>();
        if ("".equals(coddep)) {
            coddeps = null;
        } else {
            String[] parts = coddep.split(",");
            for (String part : parts) {
                coddeps.add(new Integer(part).intValue());
            }
        }
        byte[] bytePdf = editionService.editionStockALaDate(coddeps, categ, type, date, glob, detailsPrix, datePer);
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

    @GetMapping("/etatVentes")
    public ResponseEntity<byte[]> getEditionEtatVentes(
            @RequestParam("coddep") String coddep,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
            @RequestParam(name = "categorieArticle", required = true) Integer categorieArticle,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "net", required = true) Boolean net,
            @RequestParam(name = "groupedBycodep", required = true) Boolean groupedBycodep) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition details stock article: {}", coddep);
        List<Integer> coddeps = new ArrayList<>();
        if ("".equals(coddep)) {
            coddeps = null;
        } else {
            String[] parts = coddep.split(",");
            for (String part : parts) {
                coddeps.add(new Integer(part).intValue());
            }
        }
        byte[] bytePdf = editionService.editionEtatVentes(coddeps, fromDate, toDate, categ, categorieArticle, type, net, groupedBycodep);
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

    @GetMapping("/editionConsomationReels")
    public ResponseEntity<byte[]> getEditionConsomationReels(
            @RequestParam(name = "coddep", required = false) String coddep,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
            @RequestParam(name = "glob", required = true) String glob,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type
    ) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition details stock article: {}", coddep);
        List<Integer> coddeps = new ArrayList<>();
        if ("".equals(coddep) || coddep == null) {
            coddeps = null;
        } else {
            String[] parts = coddep.split(",");
            for (String part : parts) {
                coddeps.add(new Integer(part).intValue());
            }
        }
        byte[] bytePdf = editionService.editionConsomationReels(coddeps, fromDate, toDate, categ, glob, type);
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

    @PostMapping("/ConsomationReels")
    public ResponseEntity<List<TotalMouvement>> getConsomationReels(
            @Valid @RequestBody List<Integer> codarts,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ
    ) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        return ResponseEntity.ok().body(editionService.consomationReels(codarts, fromDate, toDate, categ));
    }

    @GetMapping("/stock-mouvement-inspection")
    public List<BaseMouvementStock> getMouvementStock(
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
            @RequestParam(name = "depot-id", required = true) Integer coddep
    ) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        return editionService.findMouvementStock(categ, coddep, fromDate, toDate);
    }

    @GetMapping("/etatEcartStock")
    public ResponseEntity<byte[]> getEtatEcartStock(
            @RequestParam(required = true, value = "categ") CategorieDepotEnum categDep,
            @RequestParam(required = true, value = "coddep") Integer codeDepot,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
//        stockFixParDepot_ar.rpt
        byte[] bytePdf = editionService.editionEcartStock(categDep, codeDepot, type);
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

    @GetMapping("/rotationStock")
    public ResponseEntity<byte[]> getEtatRotationStock(
            @RequestParam(required = true, value = "categ") CategorieDepotEnum categDep,
            @RequestParam(name = "categorieArticle", required = true) Integer categorieArticle,
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        byte[] bytePdf = editionService.editionRotationStock(categDep, categorieArticle, fromDate, toDate, type);
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

    /**
     * GET /edition/ficheStock.
     *
     * @param codArt
     * @param coddep
     * @param fromDate
     * @param toDate
     * @param categ
     * @param type
     * @param typeDate
     * @return the ResponseEntity with status 200 (OK) and with body of edition,
     * or with status 404 (Not Found)
     * @throws java.net.URISyntaxException
     * @throws com.crystaldecisions.sdk.occa.report.lib.ReportSDKException
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @GetMapping("/ficheStock")
    public ResponseEntity<byte[]> getEditionFicheStock(@RequestParam("codArt") Integer codArt,
            @RequestParam("coddep") Integer coddep,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ, @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "typeDate", required = true, defaultValue = "WITHOUT_DATE") TypeDateEnum typeDate) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition details stock article: {}", codArt);
        byte[] bytePdf = editionService.editionFicheStock(categ, type, codArt, coddep, fromDate, toDate, typeDate);
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

    @GetMapping("/mouvements-stock/grouped-by-depot")
    @ResponseBody
    public ResponseEntity<byte[]> getEditionMouvementStockGrouppedByDepot(
            @RequestParam List<Integer> coddep,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categ,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        log.debug("Request to get edition mvt stock");

        log.debug("Request to get edition details stock depot: {}", coddep);

        byte[] bytePdf = editionService.editionMouvementStockGrouppedByDepot(categ, type, coddep, fromDate, toDate);

        if (type.equalsIgnoreCase("E")) {
            HttpHeaders headers = new HttpHeaders();
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
            return ResponseEntity.ok().headers(headers).body(bytePdf);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return ResponseEntity.ok().headers(headers).body(bytePdf);
        }
    }

    @GetMapping("/Price-variance")
    public ResponseEntity<byte[]> getEditionPriceVariance(
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "categ", required = true) CategorieDepotEnum categDepot,
            @RequestParam(name = "categorieArticle", required = false) Integer categorieArticle,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type) throws URISyntaxException, ReportSDKException, IOException, SQLException {

        byte[] bytePdf = editionService.editionPriceVariance(fromDate, toDate, categDepot, categorieArticle, type);
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

    @GetMapping("/receptions-avec-tva")
    public ResponseEntity<byte[]> editionListeBonReceptionAvecTVa(@RequestParam(name = "categ", required = true) CategorieDepotEnum categDepot,
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "order", required = false, defaultValue = "false") Boolean orderDateBon,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "avoir", required = false, defaultValue = "false") Boolean avoir,
            @RequestParam(name = "annule", required = false, defaultValue = "false") List<Boolean> annule
    ) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = editionService.editionListeBonReceptionAvecTVa(fromDate, toDate, categDepot, orderDateBon, type, avoir, annule);
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

    @GetMapping("/facture-directe-avec-cost-centre")
    public ResponseEntity<byte[]> editionListefactureDirecte(
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "type", required = true, defaultValue = "P") String type,
            @RequestParam(name = "annule", required = false) Boolean annule
    )
            throws URISyntaxException, ReportSDKException, IOException, SQLException {

        log.debug("Request to print listefactureDirecte  :");
        byte[] bytePdf = factureDirecteService.findAllFactureDirecteWithCostCenterForEdition(fromDate, toDate, type, annule);
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
    @GetMapping("/bon-receiving")
    public ResponseEntity<byte[]> editionListeBonReceiving(@RequestParam(name = "categ", required = true) CategorieDepotEnum categDepot,
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type,
            @RequestParam(name = "valid", required = false, defaultValue = "true") Boolean valid
    ) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = editionService.editionBonReceiving(categDepot, fromDate, toDate, type, false, valid);
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
    @GetMapping("/bon-redressement")
    public ResponseEntity<byte[]> editionListeBonRedressement(@RequestParam(name = "categ", required = true) CategorieDepotEnum categDepot,
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type
    ) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = editionService.editionBonReception(categDepot, fromDate, toDate, type);
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

    @GetMapping("/articles-gratuit")
    public ResponseEntity<byte[]> editionArticleGratuit(@RequestParam(name = "categ", required = true) CategorieDepotEnum categDepot,
            @RequestParam(name = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type
    ) throws URISyntaxException, ReportSDKException, IOException, SQLException {
        byte[] bytePdf = editionService.editionArticlesGratuit(categDepot, fromDate, toDate, type);
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
}
