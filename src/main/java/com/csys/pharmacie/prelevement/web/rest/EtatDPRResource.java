package com.csys.pharmacie.prelevement.web.rest;

import com.csys.pharmacie.prelevement.dto.EtatDPRDTO;
import com.csys.pharmacie.prelevement.service.EtatDPRService;
import java.lang.Integer;
import java.lang.String;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing EtatDPR.
 */
@RestController
@RequestMapping("/api")
public class EtatDPRResource {

    private static final String ENTITY_NAME = "etatdpr";

    private final EtatDPRService etatdprService;

    private final Logger log = LoggerFactory.getLogger(EtatDPRService.class);

    public EtatDPRResource(EtatDPRService etatdprService) {
        this.etatdprService = etatdprService;
    }

    /**
     *
     * EtatDemandePrelevment by codedpr
     *
     * @param codesDPR
     * @return
     */
    @PostMapping("/dpr-prelevement-state/searches")
    public List<EtatDPRDTO> getEtatPrelevmentDPR(@RequestBody List<Integer> codesDPR) {
        log.debug("Request to get EtatDPR:{}", codesDPR);
        return etatdprService.FindByDPRIn(codesDPR);
    }
}
