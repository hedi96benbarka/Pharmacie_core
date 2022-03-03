package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.BaseTvaFactureBonReceptionDTO;
import com.csys.pharmacie.achat.service.BaseTvaFactureBonReceptionService;
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
 * REST controller for managing BaseTvaFactureBonReception.
 */
@RestController
@RequestMapping("/api")
public class BaseTvaFactureBonReceptionResource {
  private static final String ENTITY_NAME = "basetvafacturebonreception";

  private final BaseTvaFactureBonReceptionService basetvafacturebonreceptionService;

  private final Logger log = LoggerFactory.getLogger(BaseTvaFactureBonReceptionService.class);

  public BaseTvaFactureBonReceptionResource(BaseTvaFactureBonReceptionService basetvafacturebonreceptionService) {
    this.basetvafacturebonreceptionService=basetvafacturebonreceptionService;
  }

  /**
   * POST  /basetvafacturebonreceptions : Create a new basetvafacturebonreception.
   *
   * @param basetvafacturebonreceptionDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new basetvafacturebonreception, or with status 400 (Bad Request) if the basetvafacturebonreception has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/basetvafacturebonreceptions")
  public ResponseEntity<BaseTvaFactureBonReceptionDTO> createBaseTvaFactureBonReception(@Valid @RequestBody BaseTvaFactureBonReceptionDTO basetvafacturebonreceptionDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save BaseTvaFactureBonReception : {}", basetvafacturebonreceptionDTO);
    if ( basetvafacturebonreceptionDTO.getCode() != null) {
      bindingResult.addError( new FieldError("BaseTvaFactureBonReceptionDTO","code","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    BaseTvaFactureBonReceptionDTO result = basetvafacturebonreceptionService.save(basetvafacturebonreceptionDTO);
    return ResponseEntity.created( new URI("/api/basetvafacturebonreceptions/"+ result.getCode())).body(result);
  }

  /**
   * PUT  /basetvafacturebonreceptions : Updates an existing basetvafacturebonreception.
   *
   * @param id
   * @param basetvafacturebonreceptionDTO the basetvafacturebonreception to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated basetvafacturebonreception,
   * or with status 400 (Bad Request) if the basetvafacturebonreception is not valid,
   * or with status 500 (Internal Server Error) if the basetvafacturebonreception couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/basetvafacturebonreceptions/{id}")
  public ResponseEntity<BaseTvaFactureBonReceptionDTO> updateBaseTvaFactureBonReception(@PathVariable Long id, @Valid @RequestBody BaseTvaFactureBonReceptionDTO basetvafacturebonreceptionDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update BaseTvaFactureBonReception: {}",id);
    basetvafacturebonreceptionDTO.setCode(id);
    BaseTvaFactureBonReceptionDTO result =basetvafacturebonreceptionService.update(basetvafacturebonreceptionDTO);
    return ResponseEntity.ok().body(result);
  }

  /**
   * GET /basetvafacturebonreceptions/{id} : get the "id" basetvafacturebonreception.
   *
   * @param id the id of the basetvafacturebonreception to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of basetvafacturebonreception, or with status 404 (Not Found)
   */
  @GetMapping("/basetvafacturebonreceptions/{id}")
  public ResponseEntity<BaseTvaFactureBonReceptionDTO> getBaseTvaFactureBonReception(@PathVariable Long id) {
    log.debug("Request to get BaseTvaFactureBonReception: {}",id);
    BaseTvaFactureBonReceptionDTO dto = basetvafacturebonreceptionService.findOne(id);
    RestPreconditions.checkFound(dto, "basetvafacturebonreception.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /basetvafacturebonreceptions : get all the basetvafacturebonreceptions.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of basetvafacturebonreceptions in body
   */
  @GetMapping("/basetvafacturebonreceptions")
  public Collection<BaseTvaFactureBonReceptionDTO> getAllBaseTvaFactureBonReceptions() {
    log.debug("Request to get all  BaseTvaFactureBonReceptions : {}");
    return basetvafacturebonreceptionService.findAll();
  }

  /**
   * DELETE  /basetvafacturebonreceptions/{id} : delete the "id" basetvafacturebonreception.
   *
   * @param id the id of the basetvafacturebonreception to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/basetvafacturebonreceptions/{id}")
  public ResponseEntity<Void> deleteBaseTvaFactureBonReception(@PathVariable Long id) {
    log.debug("Request to delete BaseTvaFactureBonReception: {}",id);
    basetvafacturebonreceptionService.delete(id);
    return ResponseEntity.ok().build();
  }
}

