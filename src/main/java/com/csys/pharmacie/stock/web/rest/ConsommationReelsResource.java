/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.web.rest;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.stock.domain.ConsomationReels;
import com.csys.pharmacie.stock.dto.MouvementStockDTO;
import com.csys.pharmacie.stock.service.ConsommationReelsService;
import com.csys.pharmacie.stock.service.MouvementStockService;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Administrateur
 */
@RestController
@RequestMapping("/api")

public class ConsommationReelsResource {

    private final ConsommationReelsService consommationReelsService;

    private final Logger log = LoggerFactory.getLogger(ConsommationReelsResource.class);

    public ConsommationReelsResource(ConsommationReelsService consommationReelsService) {
        this.consommationReelsService = consommationReelsService;
    }

    @ApiOperation("get consommations reelles groupped by article and unite")
    @GetMapping("/consommation-reelle")
    public Collection<TotalMouvement> getConsommationReellesGroupped(
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "catgorieDepot", required = true) CategorieDepotEnum categ) {
        log.debug("Request to get all  mouvenment de stock : {}");

        return consommationReelsService.findConsommationReellesGrouppedByCodeAticleAndCodeUnite(fromDate, toDate, categ);

    }
}
