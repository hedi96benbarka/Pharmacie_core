package com.csys.pharmacie.inventaire.web.rest;

import com.csys.pharmacie.inventaire.dto.TraceTransfertInventaireDTO;
import com.csys.pharmacie.inventaire.service.TraceTransfertInventaireService;
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
 * REST controller for managing TraceTransfertInventaire.
 */
@RestController
@RequestMapping("/api")
public class TraceTransfertInventaireResource {
  private static final String ENTITY_NAME = "tracetransfertinventaire";

  private final TraceTransfertInventaireService tracetransfertinventaireService;

  private final Logger log = LoggerFactory.getLogger(TraceTransfertInventaireService.class);

  public TraceTransfertInventaireResource(TraceTransfertInventaireService tracetransfertinventaireService) {
    this.tracetransfertinventaireService=tracetransfertinventaireService;
  }

  /**
   * POST  /tracetransfertinventaires : Create a new tracetransfertinventaire.
   *
   * @param tracetransfertinventaireDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new tracetransfertinventaire, or with status 400 (Bad Request) if the tracetransfertinventaire has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/tracetransfertinventaires")
  public ResponseEntity<TraceTransfertInventaireDTO> createTraceTransfertInventaire(@Valid @RequestBody TraceTransfertInventaireDTO tracetransfertinventaireDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save TraceTransfertInventaire : {}", tracetransfertinventaireDTO);
    if ( tracetransfertinventaireDTO.getCode() != null) {
      bindingResult.addError( new FieldError("TraceTransfertInventaireDTO","code","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    TraceTransfertInventaireDTO result = tracetransfertinventaireService.save(tracetransfertinventaireDTO);
    return ResponseEntity.created( new URI("/api/tracetransfertinventaires/"+ result.getCode())).body(result);
  }

  /**
   * PUT  /tracetransfertinventaires : Updates an existing tracetransfertinventaire.
   *
   * @param id
   * @param tracetransfertinventaireDTO the tracetransfertinventaire to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated tracetransfertinventaire,
   * or with status 400 (Bad Request) if the tracetransfertinventaire is not valid,
   * or with status 500 (Internal Server Error) if the tracetransfertinventaire couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */


  /**
   * GET /tracetransfertinventaires/{id} : get the "id" tracetransfertinventaire.
   *
   * @param id the id of the tracetransfertinventaire to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of tracetransfertinventaire, or with status 404 (Not Found)
   */
  @GetMapping("/tracetransfertinventaires/{id}")
  public ResponseEntity<TraceTransfertInventaireDTO> getTraceTransfertInventaire(@PathVariable Long id) {
    log.debug("Request to get TraceTransfertInventaire: {}",id);
    TraceTransfertInventaireDTO dto = tracetransfertinventaireService.findOne(id);
    RestPreconditions.checkFound(dto, "tracetransfertinventaire.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /tracetransfertinventaires : get all the tracetransfertinventaires.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of tracetransfertinventaires in body
   */
  @GetMapping("/tracetransfertinventaires")
  public Collection<TraceTransfertInventaireDTO> getAllTraceTransfertInventaires() {
    log.debug("Request to get all  TraceTransfertInventaires : {}");
    return tracetransfertinventaireService.findAll();
  }

  /**
   * DELETE  /tracetransfertinventaires/{id} : delete the "id" tracetransfertinventaire.
   *
   * @param id the id of the tracetransfertinventaire to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/tracetransfertinventaires/{id}")
  public ResponseEntity<Void> deleteTraceTransfertInventaire(@PathVariable Long id) {
    log.debug("Request to delete TraceTransfertInventaire: {}",id);
    tracetransfertinventaireService.delete(id);
    return ResponseEntity.ok().build();
  }
}

