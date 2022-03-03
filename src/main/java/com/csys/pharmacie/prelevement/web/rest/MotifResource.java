package com.csys.pharmacie.prelevement.web.rest;

import com.csys.pharmacie.prelevement.dto.MotifDTO;
import com.csys.pharmacie.prelevement.service.MotifService;
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
 * REST controller for managing Motif.
 */
@RestController
@RequestMapping("/api")
public class MotifResource {
  private static final String ENTITY_NAME = "motif";

  private final MotifService motifService;

  private final Logger log = LoggerFactory.getLogger(MotifService.class);

  public MotifResource(MotifService motifService) {
    this.motifService=motifService;
  }

  /**
   * POST  /motifs : Create a new motif.
   *
   * @param motifDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new motif, or with status 400 (Bad Request) if the motif has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/motifs")
  public ResponseEntity<MotifDTO> createMotif(@Valid @RequestBody MotifDTO motifDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save Motif : {}", motifDTO);
    if ( motifDTO.getId() != null) {
      bindingResult.addError( new FieldError("MotifDTO","id","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    MotifDTO result = motifService.save(motifDTO);
    return ResponseEntity.created( new URI("/api/motifs/"+ result.getId())).body(result);
  }

  /**
   * PUT  /motifs : Updates an existing motif.
   *
   * @param id
   * @param motifDTO the motif to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated motif,
   * or with status 400 (Bad Request) if the motif is not valid,
   * or with status 500 (Internal Server Error) if the motif couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/motifs/{id}")
  public ResponseEntity<MotifDTO> updateMotif(@PathVariable Integer id, @Valid @RequestBody MotifDTO motifDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update Motif: {}",id);
    motifDTO.setId(id);
    MotifDTO result =motifService.update(motifDTO);
    return ResponseEntity.ok().body(result);
  }

  /**
   * GET /motifs/{id} : get the "id" motif.
   *
   * @param id the id of the motif to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of motif, or with status 404 (Not Found)
   */
  @GetMapping("/motifs/{id}")
  public ResponseEntity<MotifDTO> getMotif(@PathVariable Integer id) {
    log.debug("Request to get Motif: {}",id);
    MotifDTO dto = motifService.findOne(id);
    RestPreconditions.checkFound(dto, "motif.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /motifs : get all the motifs.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of motifs in body
   */
  @GetMapping("/motifs")
  public Collection<MotifDTO> getAllMotifs() {
    log.debug("Request to get all  Motifs : {}");
    return motifService.findAll();
  }

  /**
   * DELETE  /motifs/{id} : delete the "id" motif.
   *
   * @param id the id of the motif to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/motifs/{id}")
  public ResponseEntity<Void> deleteMotif(@PathVariable Integer id) {
    log.debug("Request to delete Motif: {}",id);
    motifService.delete(id);
    return ResponseEntity.ok().build();
  }
}

