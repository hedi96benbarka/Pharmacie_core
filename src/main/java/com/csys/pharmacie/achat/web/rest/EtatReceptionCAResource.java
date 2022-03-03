package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.EtatReceptionCADTO;
import com.csys.pharmacie.achat.service.EtatReceptionCAService;
import com.csys.util.RestPreconditions;
import java.lang.Integer;
import java.lang.String;
import java.lang.Void;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing EtatReceptionCA.
 */
@RestController
@RequestMapping("/api")
public class EtatReceptionCAResource {

    private static final String ENTITY_NAME = "etatreceptionca";

    private final EtatReceptionCAService etatreceptioncaService;

    private final Logger log = LoggerFactory.getLogger(EtatReceptionCAService.class);

    public EtatReceptionCAResource(EtatReceptionCAService etatreceptioncaService) {
        this.etatreceptioncaService = etatreceptioncaService;
    }

//  /**
//   * POST  /purchase-orders-reception-state : Create a new etatreceptionca.
//   *
//   * @param etatreceptioncaDTO
//   * @param bindingResult
//   * @return the ResponseEntity with status 201 (Created) and with body the new etatreceptionca, or with status 400 (Bad Request) if the etatreceptionca has already an ID
//   * @throws URISyntaxException if the Location URI syntax is incorrect
//   * @throws org.springframework.web.bind.MethodArgumentNotValidException
//   */
//  @PostMapping("/purchase-orders-reception-state")
//  public ResponseEntity<EtatReceptionCADTO> createEtatReceptionCA(@Valid @RequestBody EtatReceptionCADTO etatreceptioncaDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
//    log.debug("REST request to save EtatReceptionCA : {}", etatreceptioncaDTO);
//    if ( etatreceptioncaDTO.getCommandeAchat() != null) {
//      bindingResult.addError( new FieldError("EtatReceptionCADTO","commandeAchat","POST method does not accepte "+ENTITY_NAME+" with code"));
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    if (bindingResult.hasErrors()) {
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    EtatReceptionCADTO result = etatreceptioncaService.save(etatreceptioncaDTO);
//    return ResponseEntity.created( new URI("/api/purchase-orders-reception-state/"+ result.getCommandeAchat())).body(result);
//  }
//  /**
//   * PUT  /purchase-orders-reception-state : Updates an existing etatreceptionca.
//   *
//   * @param id
//   * @param etatreceptioncaDTO the etatreceptionca to update
//   * @return the ResponseEntity with status 200 (OK) and with body the updated etatreceptionca,
//   * or with status 400 (Bad Request) if the etatreceptionca is not valid,
//   * or with status 500 (Internal Server Error) if the etatreceptionca couldn't be updated
//   * @throws org.springframework.web.bind.MethodArgumentNotValidException
//   */
//  @PutMapping("/purchase-orders-reception-state/{id}")
//  public ResponseEntity<EtatReceptionCADTO> updateEtatReceptionCA(@PathVariable Integer id, @Valid @RequestBody EtatReceptionCADTO etatreceptioncaDTO) throws MethodArgumentNotValidException {
//    log.debug("Request to update EtatReceptionCA: {}",id);
//    etatreceptioncaDTO.setCommandeAchat(id);
//    EtatReceptionCADTO result =etatreceptioncaService.update(etatreceptioncaDTO);
//    return ResponseEntity.ok().body(result);
//  }
    /**
     * GET /purchase-orders-reception-state/{id} : get the "id" etatreceptionca.
     *
     * @param id the id of the etatreceptionca to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body of
     * etatreceptionca, or with status 404 (Not Found)
     */
    @GetMapping("/purchase-orders-reception-state/{id}")
    public ResponseEntity<EtatReceptionCADTO> getEtatReceptionCA(@PathVariable Integer id) {
        log.debug("Request to get EtatReceptionCA: {}", id);
        EtatReceptionCADTO dto = etatreceptioncaService.findOne(id);
        RestPreconditions.checkFound(dto, "etatreceptionca.NotFound");
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET /purchase-orders-reception-state : get all the
     * purchase-orders-reception-state.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of
     * purchase-orders-reception-state in body
     */
    @GetMapping("/purchase-orders-reception-state")
    public Collection<EtatReceptionCADTO> getAllEtatReceptionCAs() {
        log.debug("Request to get all  EtatReceptionCAs : {}");
        return etatreceptioncaService.findAll();
    }

    /**
     * POST /purchase-orders-reception-state/searches : search the
     * etatreceptionca by purchase orders codes.
     *
     * @param codesCA list of purchase orders codes
     * @return the ResponseEntity with status 200 (OK) and with body of
     * etatreceptionca
     */
    @PostMapping("/purchase-orders-reception-state/searches")
    public List<EtatReceptionCADTO> getEtatReceptionCA(@RequestBody List<Integer> codesCA) {
        log.debug("Request to get EtatReceptionCA: {}", codesCA);
        return etatreceptioncaService.findByCommandeAchatIn(codesCA);
    }

    /**
     * DELETE /purchase-orders-reception-state/{id} : delete the "id"
     * etatreceptionca.
     *
     * @param id the id of the etatreceptionca to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchase-orders-reception-state/{id}")
    public ResponseEntity<Void> deleteEtatReceptionCA(@PathVariable Integer id) {
        log.debug("Request to delete EtatReceptionCA: {}", id);
        etatreceptioncaService.delete(id);
        return ResponseEntity.ok().build();
    }
}
