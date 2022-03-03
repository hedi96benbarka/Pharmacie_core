package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.domain.AjustementRetourFournisseur;
import com.csys.pharmacie.achat.service.AjustementRetourFournisseurService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing AjustementRetourFournisseur.
 */
@RestController
@RequestMapping("/api")
public class AjustementRetourFournisseurResource {



    private final AjustementRetourFournisseurService ajustementretourfournisseurService;

    private final Logger log = LoggerFactory.getLogger(AjustementRetourFournisseurService.class);

    public AjustementRetourFournisseurResource(AjustementRetourFournisseurService ajustementretourfournisseurService) {
        this.ajustementretourfournisseurService = ajustementretourfournisseurService;
    }

    @Deprecated
    @ApiOperation("only for test use do not implement it  ")
    @GetMapping("/tajustementretourfournisseurs")
    public void testAjustementRetourFournisseur(String numBonRetour) {
        log.debug("REST request to save numBonRetour : {}", numBonRetour);
//    if ( ajustementretourfournisseurDTO.getAjustementRetourFournisseurPK() != null) {
//      bindingResult.addError( new FieldError("AjustementRetourFournisseurDTO","ajustementRetourFournisseurPK","POST method does not accepte "+ENTITY_NAME+" with code"));
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    if (bindingResult.hasErrors()) {
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
        List<AjustementRetourFournisseur> result = ajustementretourfournisseurService.testAjustementRetour(numBonRetour);
//    return ResponseEntity.created( new URI("/api/ajustementretourfournisseurs/"+ result.getAjustementRetourFournisseurPK())).body(result);
    }

//  /**
//   * GET /ajustementretourfournisseurs : get all the ajustementretourfournisseurs.
//   *
//   * @return the ResponseEntity with status 200 (OK) and the list of ajustementretourfournisseurs in body
//   */
//  @GetMapping("/ajustementretourfournisseurs")
//  public Collection<AjustementRetourFournisseurDTO> getAllAjustementRetourFournisseurs() {
//    log.debug("Request to get all  AjustementRetourFournisseurs : {}");
//    return ajustementretourfournisseurService.findAll();
//  }
}
