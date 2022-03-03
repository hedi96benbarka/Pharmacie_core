package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.DetailFactureDirecteDTO;
import com.csys.pharmacie.achat.service.DetailFactureDirecteService;
import com.csys.util.RestPreconditions;
import java.lang.Integer;
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
 * REST controller for managing DetailFactureDirecte.
 */
@RestController
@RequestMapping("/api")
public class DetailFactureDirecteResource {
  private static final String ENTITY_NAME = "detailfacturedirecte";

  private final DetailFactureDirecteService detailfacturedirecteService;

  private final Logger log = LoggerFactory.getLogger(DetailFactureDirecteService.class);

  public DetailFactureDirecteResource(DetailFactureDirecteService detailfacturedirecteService) {
    this.detailfacturedirecteService=detailfacturedirecteService;
  }

  /**
   * POST  /detailfacturedirectes : Create a new detailfacturedirecte.
   *
   * @param detailfacturedirecteDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new detailfacturedirecte, or with status 400 (Bad Request) if the detailfacturedirecte has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/detailfacturedirectes")
  public ResponseEntity<DetailFactureDirecteDTO> createDetailFactureDirecte(@Valid @RequestBody DetailFactureDirecteDTO detailfacturedirecteDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save DetailFactureDirecte : {}", detailfacturedirecteDTO);
    if ( detailfacturedirecteDTO.getCode() != null) {
      bindingResult.addError( new FieldError("DetailFactureDirecteDTO","code","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    DetailFactureDirecteDTO result = detailfacturedirecteService.save(detailfacturedirecteDTO);
    return ResponseEntity.created( new URI("/api/detailfacturedirectes/"+ result.getCode())).body(result);
  }

  /**
   * PUT  /detailfacturedirectes : Updates an existing detailfacturedirecte.
   *
   * @param id
   * @param detailfacturedirecteDTO the detailfacturedirecte to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated detailfacturedirecte,
   * or with status 400 (Bad Request) if the detailfacturedirecte is not valid,
   * or with status 500 (Internal Server Error) if the detailfacturedirecte couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/detailfacturedirectes/{id}")
  public ResponseEntity<DetailFactureDirecteDTO> updateDetailFactureDirecte(@PathVariable Integer id, @Valid @RequestBody DetailFactureDirecteDTO detailfacturedirecteDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update DetailFactureDirecte: {}",id);
    detailfacturedirecteDTO.setCode(id);
    DetailFactureDirecteDTO result =detailfacturedirecteService.update(detailfacturedirecteDTO);
    return ResponseEntity.ok().body(result);
  }

  /**
   * GET /detailfacturedirectes/{id} : get the "id" detailfacturedirecte.
   *
   * @param id the id of the detailfacturedirecte to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of detailfacturedirecte, or with status 404 (Not Found)
   */
  @GetMapping("/detailfacturedirectes/{id}")
  public ResponseEntity<DetailFactureDirecteDTO> getDetailFactureDirecte(@PathVariable Integer id) {
    log.debug("Request to get DetailFactureDirecte: {}",id);
    DetailFactureDirecteDTO dto = detailfacturedirecteService.findOne(id);
    RestPreconditions.checkFound(dto, "detailfacturedirecte.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /detailfacturedirectes : get all the detailfacturedirectes.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of detailfacturedirectes in body
   */
  @GetMapping("/detailfacturedirectes")
  public Collection<DetailFactureDirecteDTO> getAllDetailFactureDirectes() {
    log.debug("Request to get all  DetailFactureDirectes : {}");
    return detailfacturedirecteService.findAll();
  }

  /**
   * DELETE  /detailfacturedirectes/{id} : delete the "id" detailfacturedirecte.
   *
   * @param id the id of the detailfacturedirecte to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/detailfacturedirectes/{id}")
  public ResponseEntity<Void> deleteDetailFactureDirecte(@PathVariable Integer id) {
    log.debug("Request to delete DetailFactureDirecte: {}",id);
    detailfacturedirecteService.delete(id);
    return ResponseEntity.ok().build();
  }
}

