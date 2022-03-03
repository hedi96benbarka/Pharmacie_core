package com.csys.pharmacie.inventaire.web.rest;

import com.csys.pharmacie.inventaire.domain.DetailInventairePK;
import com.csys.pharmacie.inventaire.dto.DetailInventaireDTO;
import com.csys.pharmacie.inventaire.service.DetailInventaireService;
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
 * REST controller for managing DetailInventaire.
 */
@RestController
@RequestMapping("/api")
public class DetailInventaireResource {
  private static final String ENTITY_NAME = "detailinventaire";

  private final DetailInventaireService detailinventaireService;

  private final Logger log = LoggerFactory.getLogger(DetailInventaireService.class);

  public DetailInventaireResource(DetailInventaireService detailinventaireService) {
    this.detailinventaireService=detailinventaireService;
  }

  /**
   * POST  /detailinventaires : Create a new detailinventaire.
   *
   * @param detailinventaireDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new detailinventaire, or with status 400 (Bad Request) if the detailinventaire has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/detailinventaires")
  public ResponseEntity<DetailInventaireDTO> createDetailInventaire(@Valid @RequestBody DetailInventaireDTO detailinventaireDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save DetailInventaire : {}", detailinventaireDTO);
    if ( detailinventaireDTO.getDetailInventairePK() != null) {
      bindingResult.addError( new FieldError("DetailInventaireDTO","detailInventairePK","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    DetailInventaireDTO result = detailinventaireService.save(detailinventaireDTO);
    return ResponseEntity.created( new URI("/api/detailinventaires/"+ result.getDetailInventairePK())).body(result);
  }

  /**
   * PUT  /detailinventaires : Updates an existing detailinventaire.
   *
   * @param id
   * @param detailinventaireDTO the detailinventaire to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated detailinventaire,
   * or with status 400 (Bad Request) if the detailinventaire is not valid,
   * or with status 500 (Internal Server Error) if the detailinventaire couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/detailinventaires/{id}")
  public ResponseEntity<DetailInventaireDTO> updateDetailInventaire(@PathVariable DetailInventairePK id, @Valid @RequestBody DetailInventaireDTO detailinventaireDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update DetailInventaire: {}",id);
    detailinventaireDTO.setDetailInventairePK(id);
    DetailInventaireDTO result =detailinventaireService.update(detailinventaireDTO);
    return ResponseEntity.ok().body(result);
  }

  /**
   * GET /detailinventaires/{id} : get the "id" detailinventaire.
   *
   * @param id the id of the detailinventaire to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of detailinventaire, or with status 404 (Not Found)
   */
  @GetMapping("/detailinventaires/{id}")
  public ResponseEntity<DetailInventaireDTO> getDetailInventaire(@PathVariable DetailInventairePK id) {
    log.debug("Request to get DetailInventaire: {}",id);
    DetailInventaireDTO dto = detailinventaireService.findOne(id);
    RestPreconditions.checkFound(dto, "detailinventaire.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /detailinventaires : get all the detailinventaires.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of detailinventaires in body
   */
  @GetMapping("/detailinventaires")
  public Collection<DetailInventaireDTO> getAllDetailInventaires() {
    log.debug("Request to get all  DetailInventaires : {}");
    return detailinventaireService.findAll();
  }

  /**
   * DELETE  /detailinventaires/{id} : delete the "id" detailinventaire.
   *
   * @param id the id of the detailinventaire to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/detailinventaires/{id}")
  public ResponseEntity<Void> deleteDetailInventaire(@PathVariable DetailInventairePK id) {
    log.debug("Request to delete DetailInventaire: {}",id);
    detailinventaireService.delete(id);
    return ResponseEntity.ok().build();
  }
}

