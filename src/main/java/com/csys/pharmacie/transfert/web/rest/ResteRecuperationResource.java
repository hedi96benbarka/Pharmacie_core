package com.csys.pharmacie.transfert.web.rest;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.transfert.domain.ResteRecuperationPK;
import com.csys.pharmacie.transfert.dto.ResteRecuperationDTO;
import com.csys.pharmacie.transfert.service.ResteRecuperationService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing ResteRecuperation.
 */
@RestController
@RequestMapping("/api")
public class ResteRecuperationResource {

    private static final String ENTITY_NAME = "resterecuperation";

    private final ResteRecuperationService resterecuperationService;

    private final Logger log = LoggerFactory.getLogger(ResteRecuperationService.class);

    public ResteRecuperationResource(ResteRecuperationService resterecuperationService) {
        this.resterecuperationService = resterecuperationService;
    }

//  /**
//   * POST  /resterecuperations : Create a new resterecuperation.
//   *
//   * @param resterecuperationDTO
//   * @param bindingResult
//   * @return the ResponseEntity with status 201 (Created) and with body the new resterecuperation, or with status 400 (Bad Request) if the resterecuperation has already an ID
//   * @throws URISyntaxException if the Location URI syntax is incorrect
//   * @throws org.springframework.web.bind.MethodArgumentNotValidException
//   */
//  @PostMapping("/reste-recuperations")
//  public ResponseEntity<ResteRecuperationDTO> createResteRecuperation(@Valid @RequestBody ResteRecuperationDTO resterecuperationDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
//    log.debug("REST request to save ResteRecuperation : {}", resterecuperationDTO);
//    if ( resterecuperationDTO.getResteRecuperationPK() != null) {
//      bindingResult.addError( new FieldError("ResteRecuperationDTO","resteRecuperationPK","POST method does not accepte "+ENTITY_NAME+" with code"));
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    if (bindingResult.hasErrors()) {
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    ResteRecuperationDTO result = resterecuperationService.save(resterecuperationDTO);
//    return ResponseEntity.created( new URI("/api/resterecuperations/"+ result.getResteRecuperationPK())).body(result);
//  }
//
//  /**
//   * PUT  /resterecuperations : Updates an existing resterecuperation.
//   *
//   * @param id
//   * @param resterecuperationDTO the resterecuperation to update
//   * @return the ResponseEntity with status 200 (OK) and with body the updated resterecuperation,
//   * or with status 400 (Bad Request) if the resterecuperation is not valid,
//   * or with status 500 (Internal Server Error) if the resterecuperation couldn't be updated
//   * @throws org.springframework.web.bind.MethodArgumentNotValidException
//   */
//  @PutMapping("/reste-recuperations/{id}")
//  public ResponseEntity<ResteRecuperationDTO> updateResteRecuperation(@PathVariable ResteRecuperationPK id, @Valid @RequestBody ResteRecuperationDTO resterecuperationDTO) throws MethodArgumentNotValidException {
//    log.debug("Request to update ResteRecuperation: {}",id);
//    resterecuperationDTO.setResteRecuperationPK(id);
//    ResteRecuperationDTO result =resterecuperationService.update(resterecuperationDTO);
//    return ResponseEntity.ok().body(result);
//  }
    /**
     * GET /resterecuperations/{id} : get the "id" resterecuperation.
     *
     * @param depotID
     * @param categDepot
     * @param onlyMyArticles
     * @return the ResponseEntity with status 200 (OK) and with body of
     * resterecuperation, or with status 404 (Not Found)
     */
    @GetMapping("/reste-recuperations/{depot-id}")
    public Collection<ResteRecuperationDTO> getResteRecuperation(
            @PathVariable("depot-id") Integer depotID, 
            @RequestParam(name = "categDepot", required = false) CategorieDepotEnum categDepot,
            @RequestParam(required = false, value = "only-my-articles") boolean onlyMyArticles) {
        log.debug("Request to get ResteRecuperation: {}", depotID);

        return resterecuperationService.findByCodeDep(depotID, categDepot, onlyMyArticles);
    }

    
     
    @RequestMapping(value = "/reste-recuperations/reinit", method = RequestMethod.PUT)
    @ResponseBody
    public void reinitialiserRecup(@RequestParam(name = "categDepot", required = true) CategorieDepotEnum categ,
           @RequestParam(required = true, value = "depot-id") Integer codeDepot ) {
        resterecuperationService.reinitialiserResteRecup(categ,codeDepot);

    }   
//  /**
//   * GET /resterecuperations : get all the resterecuperations.
//   *
//   * @return the ResponseEntity with status 200 (OK) and the list of resterecuperations in body
//   */
//  @GetMapping("/reste-recuperations")
//  public Collection<ResteRecuperationDTO> getAllResteRecuperations() {
//    log.debug("Request to get all  ResteRecuperations : {}");
//    return resterecuperationService.findAll();
//  }
//  /**
//   * DELETE  /resterecuperations/{id} : delete the "id" resterecuperation.
//   *
//   * @param id the id of the resterecuperation to delete
//   * @return the ResponseEntity with status 200 (OK)
//   */
//  @DeleteMapping("/reste-recuperations/{id}")
//  public ResponseEntity<Void> deleteResteRecuperation(@PathVariable ResteRecuperationPK id) {
//    log.debug("Request to delete ResteRecuperation: {}",id);
//    resterecuperationService.delete(id);
//    return ResponseEntity.ok().build();
//  }
}
