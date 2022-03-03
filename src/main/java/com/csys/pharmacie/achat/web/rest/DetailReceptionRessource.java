/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.DetailReceptionDTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.achat.service.MvtStoBAService;
import com.csys.pharmacie.achat.service.ReceptionDetailCAService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author DELL
 */


@RestController
@RequestMapping("/detailReception")
public class DetailReceptionRessource {
    
     private final Logger log = LoggerFactory.getLogger(ReceptionDetailCAService.class);

    private final MvtStoBAService mvtStoBAService;

    public DetailReceptionRessource(MvtStoBAService mvtStoBAService) {
        this.mvtStoBAService = mvtStoBAService;
    }

 @GetMapping("/searches")
  public @ResponseBody
    List<MvtstoBADTO> findReceptionsByCodeCaAndCodeArticle(
            @RequestParam(name = "codeCa", required = true) Integer codeCa,
            @RequestParam(name = "codeArticle", required = true) Integer codeArticle ) {
        return mvtStoBAService.findReceptionsByCodeCaAndCodeArticle(codeCa, codeArticle);
    }
    
}
