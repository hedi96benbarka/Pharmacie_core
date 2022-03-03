/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.parametrage.controller;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.TypeBonEnum;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.csys.pharmacie.parametrage.entity.OptionVersionPharmacie;
import com.csys.pharmacie.parametrage.entity.Paramph;
import com.csys.pharmacie.parametrage.repository.ParamService;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Farouk
 */
@RestController
@RequestMapping("/Parametrage")
public class ParametrageController {

    @Autowired
    ParamService paramService;

    @GetMapping(value = "/counter")
    public @ResponseBody
    String checkCompteurByCodeDepotTypebon(
            @RequestParam("categ-depot") CategorieDepotEnum categDepot,
            @RequestParam("typebon") TypeBonEnum typebon) {
        return paramService.getcompteur(categDepot, typebon);
    }

//    @RequestMapping(value = "/numBonByTypeBon/{typeBon}", method = RequestMethod.GET)
//    public @ResponseBody
//    String getNumBonByTypeBon(@PathVariable("typeBon") String t) {
//        CompteurPharmacie c = paramService.findcompteurbycode("PH",t);
//        return c.getP1() + c.getP2();
//    }
//    @RequestMapping(value = "/findAllModeReg", method = RequestMethod.GET)
//    public @ResponseBody
//    List<ModReg> findAllModeReg() {
//        return paramService.findAllModeReg();
//    }

    @RequestMapping(value = "/findSuffixRptBC", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> findSuffixRptBC() {
        return Collections.singletonMap("Suffix", paramService.findSuffixRptBC());
    }

//    @Deprecated
//    @RequestMapping(value = "/findAllTva", method = RequestMethod.GET)
//    public @ResponseBody
//    List<Tva> findAllTva() {
//        return paramService.findAllTva();
//    }

    @RequestMapping(value = "/findParamByCode/{code}", method = RequestMethod.GET)
    public @ResponseBody
    Paramph findparambycode(@PathVariable("code") String code) {
        return paramService.findparambycode(code);

    }

//    @Deprecated
//    @RequestMapping(value = "/findCliniqueConfig", method = RequestMethod.GET)
//    public @ResponseBody
//    Clinique findCliniqueConfig() {
//        return paramService.findCliniqueConfig();
//
//    }

    @RequestMapping(value = "/OptionVersionPharmacie/{ID}", method = RequestMethod.GET)
    public @ResponseBody
    String findValeurOptionVersionPharmacieByID(@PathVariable("ID") String ID) {
        return paramService.findValeurOptionVersionPharmacieByID(ID);

    }

 

    @RequestMapping(value = "/findConfigTransfRecup", method = RequestMethod.GET)
    public @ResponseBody
    List<OptionVersionPharmacie> findConfigTransfRecup() {
        return paramService.findConfigTransfRecup();
    }

    @RequestMapping(value = "/findConfigPrmArt", method = RequestMethod.GET)
    public @ResponseBody
    List<OptionVersionPharmacie> findConfigPrmArt() {
        return paramService.findConfigPrmArticle();
    }

    @RequestMapping(value = "/Param/{codarts}", method = RequestMethod.GET)
    public @ResponseBody
    List<Paramph> findConfigTransfRecup(@PathVariable("codarts") List<String> listCodes) {
        return paramService.getParamsByCodeIn(listCodes);
    }

//    @RequestMapping(value = "/Param", method = RequestMethod.PUT)
//    public @ResponseBody
//    boolean updateParam(@RequestBody ParamDTO p) {
//        return paramService.updateParam(p);
//    }

    @RequestMapping(value = "/DateServeur", method = RequestMethod.GET)
    public @ResponseBody
    LocalDate updateParam() {
        return LocalDate.now();
    }

    @RequestMapping(value = "/checkMdpByCode", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkMdpByCode(@RequestParam("codeMdp") String codeMdp, @RequestParam("mdp") String mdp) {
        return paramService.checkMdpByCode(codeMdp, mdp);
    }
}
