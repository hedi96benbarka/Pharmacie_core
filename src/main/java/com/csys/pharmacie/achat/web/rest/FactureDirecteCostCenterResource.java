package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.domain.FactureDirecteCostCenterPK;
import com.csys.pharmacie.achat.dto.FactureDirecteCostCenterDTO;
import com.csys.pharmacie.achat.service.FactureDirecteCostCenterService;
import com.csys.util.RestPreconditions;
import java.lang.String;
import java.lang.Void;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing FactureDirecteCostCenter.
 */
@RestController
@RequestMapping("/api")
public class FactureDirecteCostCenterResource {

    private static final String ENTITY_NAME = "facturedirectecostcenter";

    private final FactureDirecteCostCenterService facturedirectecostcenterService;

    private final Logger log = LoggerFactory.getLogger(FactureDirecteCostCenterService.class);

    public FactureDirecteCostCenterResource(FactureDirecteCostCenterService facturedirectecostcenterService) {
        this.facturedirectecostcenterService = facturedirectecostcenterService;
    }

//  /**
//   * POST  /facturedirectecostcenters : Create a new facturedirectecostcenter.
//   *
//   * @param facturedirectecostcenterDTO
//   * @param bindingResult
//   * @return the ResponseEntity with status 201 (Created) and with body the new facturedirectecostcenter, or with status 400 (Bad Request) if the facturedirectecostcenter has already an ID
//   * @throws URISyntaxException if the Location URI syntax is incorrect
//   * @throws org.springframework.web.bind.MethodArgumentNotValidException
//   */
//  @PostMapping("/facturedirectecostcenters")
//  public ResponseEntity<FactureDirecteCostCenterDTO> createFactureDirecteCostCenter(@Valid @RequestBody FactureDirecteCostCenterDTO facturedirectecostcenterDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
//    log.debug("REST request to save FactureDirecteCostCenter : {}", facturedirectecostcenterDTO);
//    if ( facturedirectecostcenterDTO.getFactureDirectCostCenterPK() != null) {
//      bindingResult.addError( new FieldError("FactureDirecteCostCenterDTO","factureDirectCostCenterPK","POST method does not accepte "+ENTITY_NAME+" with code"));
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    if (bindingResult.hasErrors()) {
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    FactureDirecteCostCenterDTO result = facturedirectecostcenterService.save(facturedirectecostcenterDTO);
//    return ResponseEntity.created( new URI("/api/facturedirectecostcenters/"+ result.getFactureDirectCostCenterPK())).body(result);
//  }
//  /**
//   * PUT  /facturedirectecostcenters : Updates an existing facturedirectecostcenter.
//   *
//   * @param id
//   * @param facturedirectecostcenterDTO the facturedirectecostcenter to update
//   * @return the ResponseEntity with status 200 (OK) and with body the updated facturedirectecostcenter,
//   * or with status 400 (Bad Request) if the facturedirectecostcenter is not valid,
//   * or with status 500 (Internal Server Error) if the facturedirectecostcenter couldn't be updated
//   * @throws org.springframework.web.bind.MethodArgumentNotValidException
//   */
//  @PutMapping("/facturedirectecostcenters/{id}")
//  public ResponseEntity<FactureDirecteCostCenterDTO> updateFactureDirecteCostCenter(@PathVariable FactureDirecteCostCenterPK id, @Valid @RequestBody FactureDirecteCostCenterDTO facturedirectecostcenterDTO) throws MethodArgumentNotValidException {
//    log.debug("Request to update FactureDirecteCostCenter: {}",id);
//    facturedirectecostcenterDTO.setFactureDirectCostCenterPK(id);
//    FactureDirecteCostCenterDTO result =facturedirectecostcenterService.update(facturedirectecostcenterDTO);
//    return ResponseEntity.ok().body(result);
//  }
//  /**
//   * GET /facturedirectecostcenters/{id} : get the "id" facturedirectecostcenter.
//   *
//   * @param id the id of the facturedirectecostcenter to retrieve
//   * @return the ResponseEntity with status 200 (OK) and with body of facturedirectecostcenter, or with status 404 (Not Found)
//   */
//  @GetMapping("/facturedirectecostcenters/{id}")
//  public ResponseEntity<FactureDirecteCostCenterDTO> getFactureDirecteCostCenter(@PathVariable FactureDirecteCostCenterPK id) {
//    log.debug("Request to get FactureDirecteCostCenter: {}",id);
//    FactureDirecteCostCenterDTO dto = facturedirectecostcenterService.findOne(id);
//    RestPreconditions.checkFound(dto, "facturedirectecostcenter.NotFound");
//    return ResponseEntity.ok().body(dto);
//  }
    /**
     * GET /facturedirectecostcenters : get all the facturedirectecostcenters.
     *
     * @param costCenterID
     * @param numFactureDirecte
     * @return the ResponseEntity with status 200 (OK) and the list of
     * facturedirectecostcenters in body
     */
    @GetMapping("/facturedirectecostcenters")
    public Collection<FactureDirecteCostCenterDTO> getAllFactureDirecteCostCenters(@RequestParam("cost-center-id") Integer costCenterID,
            @RequestParam("num-facture-directe") String numFactureDirecte) {
        log.debug("Request to get all  FactureDirecteCostCenters : costCenterID{} , numFactureDirecte{} ", costCenterID, numFactureDirecte);
        return facturedirectecostcenterService.findAll(costCenterID, numFactureDirecte);
    }

//  /**
//   * DELETE  /facturedirectecostcenters/{id} : delete the "id" facturedirectecostcenter.
//   *
//   * @param id the id of the facturedirectecostcenter to delete
//   * @return the ResponseEntity with status 200 (OK)
//   */
//  @DeleteMapping("/facturedirectecostcenters/{id}")
//  public ResponseEntity<Void> deleteFactureDirecteCostCenter(@PathVariable FactureDirecteCostCenterPK id) {
//    log.debug("Request to delete FactureDirecteCostCenter: {}",id);
//    facturedirectecostcenterService.delete(id);
//    return ResponseEntity.ok().build();
//  }
}
