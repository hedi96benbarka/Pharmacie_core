/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.web.rest;

import com.csys.pharmacie.achat.service.ReceivingService;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.transfert.dto.BonTransferAnnulationDTO;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;
import com.csys.pharmacie.transfert.service.BonTransfertAnnulationService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Farouk
 */
@RestController
@RequestMapping("/transfert-annulation")
public class TransfertAnnulationController {

    private final Logger log = LoggerFactory.getLogger(TransfertAnnulationController.class);

    private static final String ENTITY_NAME = "transferAnnulation";

    @Autowired
    private BonTransfertAnnulationService bonTransfertAnnulationService;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody

    ResponseEntity<FactureBTDTO> addBonTransfert(@Valid @RequestBody BonTransferAnnulationDTO dto, BindingResult bindingResult) throws MethodArgumentNotValidException, URISyntaxException {

        if (dto.getCode() != null) {
            bindingResult.addError(new FieldError("ReceivingDTO", "code", "POST method does not accepte " + ENTITY_NAME + " with code"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
        dto.setInterdpot(false);
        FactureBTDTO result = bonTransfertAnnulationService.addBonTransferAnnulation(dto);
        return ResponseEntity.created(new URI("/api/transfert-recup/" + result.getCode())).body(result);
    }

    @GetMapping
    public @ResponseBody
    List<BonTransferAnnulationDTO> findAll(
            @RequestParam(name = "categ", required = false) CategorieDepotEnum categ,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime toDate,
            @RequestParam(name = "depot-id-src", required = false) Integer depotID_src,
            @RequestParam(name = "depot-id-des", required = false) Integer depotID_des,
            @RequestParam(name = "interdepot", required = true, defaultValue = "true") Boolean interdepot,
            @RequestParam(name = "avoirTransfert", required = true, defaultValue = "true") Boolean avoirTransfert,
            @RequestParam(name = "deleted", required = true, defaultValue = "false") Boolean deleted,
            @RequestParam(name = "validated", required = false) Boolean validated,
            @RequestParam(name = "conforme", required = false) Boolean conforme)
            throws ParseException {

        return bonTransfertAnnulationService.findAll(categ, fromDate, toDate, depotID_src, depotID_des ,interdepot, avoirTransfert, deleted, validated, conforme);

    }

    @GetMapping("/{numBon}")
    public ResponseEntity<BonTransferAnnulationDTO> getTransfert(@PathVariable String numBon) {

        BonTransferAnnulationDTO dto = bonTransfertAnnulationService.findOne(numBon);

        return ResponseEntity.ok().body(dto);
    }

}
