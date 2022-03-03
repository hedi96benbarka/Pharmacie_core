package com.csys.pharmacie.achat.web.rest;


import com.csys.pharmacie.achat.dto.MotifRetourDTO;
import com.csys.pharmacie.achat.service.MotifRetourService;
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
 * REST controller for managing MotifRetour.
 */
@RestController
@RequestMapping("/api")
public class MotifRetourResource {
  private static final String ENTITY_NAME = "motifretour";

  private final MotifRetourService motifretourService;

  private final Logger log = LoggerFactory.getLogger(MotifRetourService.class);

  public MotifRetourResource(MotifRetourService motifretourService) {
    this.motifretourService=motifretourService;
  }

  /**
   * POST  /motifretours : Create a new motifretour.
   *
   * @param motifretourDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new motifretour, or with status 400 (Bad Request) if the motifretour has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/motifretours")
  public ResponseEntity<MotifRetourDTO> createMotifRetour(@Valid @RequestBody MotifRetourDTO motifretourDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save MotifRetour : {}", motifretourDTO);
    if ( motifretourDTO.getId() != null) {
      bindingResult.addError( new FieldError("MotifRetourDTO","id","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    MotifRetourDTO result = motifretourService.save(motifretourDTO);
    return ResponseEntity.created( new URI("/api/motifretours/"+ result.getId())).body(result);
  }

  /**
   * PUT  /motifretours : Updates an existing motifretour.
   *
   * @param id
   * @param motifretourDTO the motifretour to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated motifretour,
   * or with status 400 (Bad Request) if the motifretour is not valid,
   * or with status 500 (Internal Server Error) if the motifretour couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/motifretours/{id}")
  public ResponseEntity<MotifRetourDTO> updateMotifRetour(@PathVariable Integer id, @Valid @RequestBody MotifRetourDTO motifretourDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update MotifRetour: {}",id);
    motifretourDTO.setId(id);
    MotifRetourDTO result =motifretourService.update(motifretourDTO);
    return ResponseEntity.ok().body(result);
  }     

  /**
   * GET /motifretours/{id} : get the "id" motifretour.
   *
   * @param id the id of the motifretour to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of motifretour, or with status 404 (Not Found)
   */
  @GetMapping("/motifretours/{id}")
  public ResponseEntity<MotifRetourDTO> getMotifRetour(@PathVariable Integer id) {
    log.debug("Request to get MotifRetour: {}",id);
    MotifRetourDTO dto = motifretourService.findOne(id);
    RestPreconditions.checkFound(dto, "motifretour.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /motifretours : get all the motifretours.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of motifretours in body
   */
  @GetMapping("/motifretours")
  public Collection<MotifRetourDTO> getAllMotifRetours() {
    log.debug("Request to get all  MotifRetours : {}");
    return motifretourService.findAll();
  }

  /**
   * DELETE  /motifretours/{id} : delete the "id" motifretour.
   *
   * @param id the id of the motifretour to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/motifretours/{id}")
  public ResponseEntity<Void> deleteMotifRetour(@PathVariable Integer id) {
    log.debug("Request to delete MotifRetour: {}",id);
    motifretourService.delete(id);
    return ResponseEntity.ok().build();
  }
}

