package com.csys.pharmacie.stock.web.rest;

import com.csys.pharmacie.stock.domain.DetailDecoupagePK;
import com.csys.pharmacie.stock.dto.DetailDecoupageDTO;
import com.csys.pharmacie.stock.service.DetailDecoupageService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing DetailDecoupage.
 */
@RestController
@RequestMapping("/api")
public class DetailDecoupageResource {
  private static final String ENTITY_NAME = "detaildecoupage";

  private final DetailDecoupageService detaildecoupageService;

  private final Logger log = LoggerFactory.getLogger(DetailDecoupageService.class);

  public DetailDecoupageResource(DetailDecoupageService detaildecoupageService) {
    this.detaildecoupageService=detaildecoupageService;
  }

  /**
   * POST  /detaildecoupages : Create a new detaildecoupage.
   *
   * @param detaildecoupageDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new detaildecoupage, or with status 400 (Bad Request) if the detaildecoupage has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
//  @PostMapping("/detaildecoupages")
//  public ResponseEntity<DetailDecoupageDTO> createDetailDecoupage(@Valid @RequestBody DetailDecoupageDTO detaildecoupageDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
//    log.debug("REST request to save DetailDecoupage : {}", detaildecoupageDTO);
//    if ( detaildecoupageDTO.getDetailDecoupagePK() != null) {
//      bindingResult.addError( new FieldError("DetailDecoupageDTO","detailDecoupagePK","POST method does not accepte "+ENTITY_NAME+" with code"));
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    if (bindingResult.hasErrors()) {
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    DetailDecoupageDTO result = detaildecoupageService.save(detaildecoupageDTO);
//    return ResponseEntity.created( new URI("/api/detaildecoupages/"+ result.getDetailDecoupagePK())).body(result);
//  }

  /**
   * PUT  /detaildecoupages : Updates an existing detaildecoupage.
   *
   * @param id
   * @param detaildecoupageDTO the detaildecoupage to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated detaildecoupage,
   * or with status 400 (Bad Request) if the detaildecoupage is not valid,
   * or with status 500 (Internal Server Error) if the detaildecoupage couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
//  @PutMapping("/detaildecoupages/{id}")
//  public ResponseEntity<DetailDecoupageDTO> updateDetailDecoupage(@PathVariable DetailDecoupagePK id, @Valid @RequestBody DetailDecoupageDTO detaildecoupageDTO) throws MethodArgumentNotValidException {
//    log.debug("Request to update DetailDecoupage: {}",id);
//    detaildecoupageDTO.setDetailDecoupagePK(id);
//    DetailDecoupageDTO result =detaildecoupageService.update(detaildecoupageDTO);
//    return ResponseEntity.ok().body(result);
//  }

  /**
   * GET /detaildecoupages/{id} : get the "id" detaildecoupage.
   *
   * @param id the id of the detaildecoupage to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of detaildecoupage, or with status 404 (Not Found)
   */
  @GetMapping("/detaildecoupages/{id}")
  public ResponseEntity<DetailDecoupageDTO> getDetailDecoupage(@PathVariable DetailDecoupagePK id) {
    log.debug("Request to get DetailDecoupage: {}",id);
    DetailDecoupageDTO dto = detaildecoupageService.findOne(id);
    RestPreconditions.checkFound(dto, "detaildecoupage.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /detaildecoupages : get all the detaildecoupages.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of detaildecoupages in body
   */
  @GetMapping("/detaildecoupages")
  public Collection<DetailDecoupageDTO> getAllDetailDecoupages() {
    log.debug("Request to get all  DetailDecoupages : {}");
    return detaildecoupageService.findAll();
  }

  /**
   * DELETE  /detaildecoupages/{id} : delete the "id" detaildecoupage.
   *
   * @param id the id of the detaildecoupage to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/detaildecoupages/{id}")
  public ResponseEntity<Void> deleteDetailDecoupage(@PathVariable DetailDecoupagePK id) {
    log.debug("Request to delete DetailDecoupage: {}",id);
    detaildecoupageService.delete(id);
    return ResponseEntity.ok().build();
  }
}

