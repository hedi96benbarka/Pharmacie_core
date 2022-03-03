/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.inventaire.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Farouk
 */
@RestController
@RequestMapping("/Inventaire")
public class InventaireController {

    @Autowired
    com.csys.pharmacie.inventaire.service.InventaireService inventaireService;
 
    @GetMapping("/inventaires/listeDepotOuverte")
    public List<DepotDTO> findListDepotOuvert(
            @RequestParam("categorrie-depot") CategorieDepotEnum categ_depot,
            @RequestParam(name = "annuler", required = false) Boolean annuler
    ) {
        return inventaireService.findListDepotOuvert(categ_depot, annuler);
    }

    @GetMapping("/edition/list-articles-perimes")
    public ResponseEntity<byte[]> editionArticlePerimeApresValidationInventaire(
            @RequestParam Integer codeInventaire,
            @RequestParam Boolean inventaireOuvert,
            @RequestParam(name = "type", required = false, defaultValue = "P") String type)
            throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {
        byte[] bytePdf;
        if (Boolean.TRUE.equals(inventaireOuvert)) {
            bytePdf = inventaireService.editionListArticlePerimeAvantValidationInventaire(codeInventaire, type);
        } else {
            bytePdf = inventaireService.editionListArticlePerimeApresValidationInventaire(codeInventaire, type);
        }
        HttpHeaders headers = new HttpHeaders();
        if (type.equalsIgnoreCase("P")) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else {
            MediaType excelMediaType = new MediaType("application", "vnd.ms-excel");
            headers.setContentType(excelMediaType);
        }
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }

}
