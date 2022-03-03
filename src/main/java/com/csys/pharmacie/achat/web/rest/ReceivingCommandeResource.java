package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.domain.ReceivingCommande;
import com.csys.pharmacie.achat.dto.ReceivingCommandeDTO;
import com.csys.pharmacie.achat.service.ReceivingCommandeService;
import com.csys.util.RestPreconditions;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing ReceivingCommande.
 */
@RestController
@RequestMapping("/api")
public class ReceivingCommandeResource {

    private static final String ENTITY_NAME = "receivingcommande";

    private final ReceivingCommandeService receivingcommandeService;

    private final Logger log = LoggerFactory.getLogger(ReceivingCommandeService.class);

    public ReceivingCommandeResource(ReceivingCommandeService receivingcommandeService) {
        this.receivingcommandeService = receivingcommandeService;
    }

    /**
     *  
     *
     * @param code
     * @param purchaseOrdrId
     * @return the ResponseEntity with status 200 (OK) and with body of
     * receivingcommande, or with status 404 (Not Found)
     */
    @GetMapping("/receivingcommandes")
    public List<ReceivingCommandeDTO> getReceivingCommande(@RequestParam(required = false ,name = "receiving-id") Integer code,@RequestParam(required = false,name = "purchase-order-id") Integer purchaseOrdrId) {
        log.debug("Request to get ReceivingCommande: {}", code);
        ReceivingCommande exple=new ReceivingCommande(code,purchaseOrdrId);
        List<ReceivingCommandeDTO> dto = receivingcommandeService.findByExample(Example.of(exple));
        RestPreconditions.checkFound(dto, "receivingcommande.NotFound");
        return dto;
    }
}
