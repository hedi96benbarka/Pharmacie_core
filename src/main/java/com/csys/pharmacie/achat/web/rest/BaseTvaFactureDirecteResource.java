package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.BaseTvaFactureDirecteDTO;
import com.csys.pharmacie.achat.service.BaseTvaFactureDirecteService;
import com.csys.util.RestPreconditions;
import java.lang.Long;
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
 * REST controller for managing BaseTvaFactureDirecte.
 */
@RestController
@RequestMapping("/api")
public class BaseTvaFactureDirecteResource {
  private static final String ENTITY_NAME = "basetvafacturedirecte";

  private final BaseTvaFactureDirecteService basetvafacturedirecteService;

  private final Logger log = LoggerFactory.getLogger(BaseTvaFactureDirecteService.class);

  public BaseTvaFactureDirecteResource(BaseTvaFactureDirecteService basetvafacturedirecteService) {
    this.basetvafacturedirecteService=basetvafacturedirecteService;
  }

  /**
   * POST  /basetvafacturedirectes : Create a new basetvafacturedirecte.
   *
   * @param basetvafacturedirecteDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new basetvafacturedirecte, or with status 400 (Bad Request) if the basetvafacturedirecte has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/basetvafacturedirectes")
  public ResponseEntity<BaseTvaFactureDirecteDTO> createBaseTvaFactureDirecte(@Valid @RequestBody BaseTvaFactureDirecteDTO basetvafacturedirecteDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save BaseTvaFactureDirecte : {}", basetvafacturedirecteDTO);
    if ( basetvafacturedirecteDTO.getCode() != null) {
      bindingResult.addError( new FieldError("BaseTvaFactureDirecteDTO","code","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    BaseTvaFactureDirecteDTO result = basetvafacturedirecteService.save(basetvafacturedirecteDTO);
    return ResponseEntity.created( new URI("/api/basetvafacturedirectes/"+ result.getCode())).body(result);
  }

  /**
   * PUT  /basetvafacturedirectes : Updates an existing basetvafacturedirecte.
   *
   * @param id
   * @param basetvafacturedirecteDTO the basetvafacturedirecte to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated basetvafacturedirecte,
   * or with status 400 (Bad Request) if the basetvafacturedirecte is not valid,
   * or with status 500 (Internal Server Error) if the basetvafacturedirecte couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/basetvafacturedirectes/{id}")
  public ResponseEntity<BaseTvaFactureDirecteDTO> updateBaseTvaFactureDirecte(@PathVariable Long id, @Valid @RequestBody BaseTvaFactureDirecteDTO basetvafacturedirecteDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update BaseTvaFactureDirecte: {}",id);
    basetvafacturedirecteDTO.setCode(id);
    BaseTvaFactureDirecteDTO result =basetvafacturedirecteService.update(basetvafacturedirecteDTO);
    return ResponseEntity.ok().body(result);
  }

  /**
   * GET /basetvafacturedirectes/{id} : get the "id" basetvafacturedirecte.
   *
   * @param id the id of the basetvafacturedirecte to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of basetvafacturedirecte, or with status 404 (Not Found)
   */
  @GetMapping("/basetvafacturedirectes/{id}")
  public ResponseEntity<BaseTvaFactureDirecteDTO> getBaseTvaFactureDirecte(@PathVariable Long id) {
    log.debug("Request to get BaseTvaFactureDirecte: {}",id);
    BaseTvaFactureDirecteDTO dto = basetvafacturedirecteService.findOne(id);
    RestPreconditions.checkFound(dto, "basetvafacturedirecte.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /basetvafacturedirectes : get all the basetvafacturedirectes.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of basetvafacturedirectes in body
   */
  @GetMapping("/basetvafacturedirectes")
  public Collection<BaseTvaFactureDirecteDTO> getAllBaseTvaFactureDirectes() {
    log.debug("Request to get all  BaseTvaFactureDirectes : {}");
    return basetvafacturedirecteService.findAll();
  }

  /**
   * DELETE  /basetvafacturedirectes/{id} : delete the "id" basetvafacturedirecte.
   *
   * @param id the id of the basetvafacturedirecte to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/basetvafacturedirectes/{id}")
  public ResponseEntity<Void> deleteBaseTvaFactureDirecte(@PathVariable Long id) {
    log.debug("Request to delete BaseTvaFactureDirecte: {}",id);
    basetvafacturedirecteService.delete(id);
    return ResponseEntity.ok().build();
  }
}

