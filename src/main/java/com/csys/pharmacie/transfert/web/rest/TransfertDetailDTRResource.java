package com.csys.pharmacie.transfert.web.rest;

import com.csys.pharmacie.transfert.dto.TransfertDetailDTRDTO;
import com.csys.pharmacie.transfert.service.TransfertDetailDTRService;
import java.lang.String;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing TransfertDetailDTR.
 */
@RestController
@RequestMapping("/api")
public class TransfertDetailDTRResource {

    private static final String ENTITY_NAME = "transfertdetaildtr";

    private final TransfertDetailDTRService transfertdetaildtrService;

    private final Logger log = LoggerFactory.getLogger(TransfertDetailDTRService.class);

    public TransfertDetailDTRResource(TransfertDetailDTRService transfertdetaildtrService) {
        this.transfertdetaildtrService = transfertdetaildtrService;
    }

    @GetMapping("/transferred-dtr-details/{id}")
    public List<TransfertDetailDTRDTO> getTransfertDTR(@PathVariable Integer id) {
        log.debug("Request to get TransfertDetailDTR:{}", id);
        return transfertdetaildtrService.findTransferDetailDTRByCodeDTR(id);
    }

    @PostMapping("/transferred-dtr-details/searches")
    public List<TransfertDetailDTRDTO> getEtatTransfertDTR(@RequestBody List<Integer> codesDTR) {
        log.debug("Request to get DTR:{}", codesDTR);
        return transfertdetaildtrService.FindByDTRIn(codesDTR);
    }


    }       
    
    
   
    
    
    
    


