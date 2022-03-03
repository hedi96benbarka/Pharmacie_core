package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.domain.DetailMvtStoBAPK;
import com.csys.pharmacie.achat.dto.DetailMvtStoBADTO;
import com.csys.pharmacie.achat.service.DetailMvtStoBAService;
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
 * REST controller for managing DetailMvtStoBA.
 */
@RestController
@RequestMapping("/api")
public class DetailMvtStoBAResource {
  private static final String ENTITY_NAME = "detailmvtstoba";

  private final DetailMvtStoBAService detailmvtstobaService;

  private final Logger log = LoggerFactory.getLogger(DetailMvtStoBAService.class);

  public DetailMvtStoBAResource(DetailMvtStoBAService detailmvtstobaService) {
    this.detailmvtstobaService=detailmvtstobaService;
  }

  /**
   * POST  /detailmvtstobas : Create a new detailmvtstoba.
   *
   * @param detailmvtstobaDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new detailmvtstoba, or with status 400 (Bad Request) if the detailmvtstoba has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/detailmvtstobas")
  public ResponseEntity<DetailMvtStoBADTO> createDetailMvtStoBA(@Valid @RequestBody DetailMvtStoBADTO detailmvtstobaDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save DetailMvtStoBA : {}", detailmvtstobaDTO);
    if ( detailmvtstobaDTO.getPk() != null) {
      bindingResult.addError( new FieldError("DetailMvtStoBADTO","pk","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    DetailMvtStoBADTO result = detailmvtstobaService.save(detailmvtstobaDTO);
    return ResponseEntity.created( new URI("/api/detailmvtstobas/"+ result.getPk())).body(result);
  }

  /**
   * PUT  /detailmvtstobas : Updates an existing detailmvtstoba.
   *
   * @param id
   * @param detailmvtstobaDTO the detailmvtstoba to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated detailmvtstoba,
   * or with status 400 (Bad Request) if the detailmvtstoba is not valid,
   * or with status 500 (Internal Server Error) if the detailmvtstoba couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/detailmvtstobas/{id}")
  public ResponseEntity<DetailMvtStoBADTO> updateDetailMvtStoBA(@PathVariable DetailMvtStoBAPK id, @Valid @RequestBody DetailMvtStoBADTO detailmvtstobaDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update DetailMvtStoBA: {}",id);
    detailmvtstobaDTO.setPk(id);
    DetailMvtStoBADTO result =detailmvtstobaService.update(detailmvtstobaDTO);
    return ResponseEntity.ok().body(result);
  }

  /**
   * GET /detailmvtstobas/{id} : get the "id" detailmvtstoba.
   *
   * @param id the id of the detailmvtstoba to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of detailmvtstoba, or with status 404 (Not Found)
   */
  @GetMapping("/detailmvtstobas/{id}")
  public ResponseEntity<DetailMvtStoBADTO> getDetailMvtStoBA(@PathVariable DetailMvtStoBAPK id) {
    log.debug("Request to get DetailMvtStoBA: {}",id);
    DetailMvtStoBADTO dto = detailmvtstobaService.findOne(id);
    RestPreconditions.checkFound(dto, "detailmvtstoba.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /detailmvtstobas : get all the detailmvtstobas.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of detailmvtstobas in body
   */
  @GetMapping("/detailmvtstobas")
  public Collection<DetailMvtStoBADTO> getAllDetailMvtStoBAs() {
    log.debug("Request to get all  DetailMvtStoBAs : {}");
    return detailmvtstobaService.findAll();
  }

  /**
   * DELETE  /detailmvtstobas/{id} : delete the "id" detailmvtstoba.
   *
   * @param id the id of the detailmvtstoba to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/detailmvtstobas/{id}")
  public ResponseEntity<Void> deleteDetailMvtStoBA(@PathVariable DetailMvtStoBAPK id) {
    log.debug("Request to delete DetailMvtStoBA: {}",id);
    detailmvtstobaService.delete(id);
    return ResponseEntity.ok().build();
  }
}

