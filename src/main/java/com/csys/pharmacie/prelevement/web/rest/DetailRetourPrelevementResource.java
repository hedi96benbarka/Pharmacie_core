package com.csys.pharmacie.prelevement.web.rest;

import com.csys.pharmacie.prelevement.dto.DetailRetourPrelevementDTO;
import com.csys.pharmacie.prelevement.service.DetailRetourPrelevementService;
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
 * REST controller for managing DetailRetourPrelevement.
 */
@RestController
@RequestMapping("/api")
public class DetailRetourPrelevementResource {
  private static final String ENTITY_NAME = "detailretourprelevement";

  private final DetailRetourPrelevementService detailretourprelevementService;

  private final Logger log = LoggerFactory.getLogger(DetailRetourPrelevementService.class);

  public DetailRetourPrelevementResource(DetailRetourPrelevementService detailretourprelevementService) {
    this.detailretourprelevementService=detailretourprelevementService;
  }

  /**
   * POST  /detailretourprelevements : Create a new detailretourprelevement.
   *
   * @param detailretourprelevementDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new detailretourprelevement, or with status 400 (Bad Request) if the detailretourprelevement has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/detailretourprelevements")
  public ResponseEntity<DetailRetourPrelevementDTO> createDetailRetourPrelevement(@Valid @RequestBody DetailRetourPrelevementDTO detailretourprelevementDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save DetailRetourPrelevement : {}", detailretourprelevementDTO);
    if ( detailretourprelevementDTO.getCode() != null) {
      bindingResult.addError( new FieldError("DetailRetourPrelevementDTO","code","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    DetailRetourPrelevementDTO result = detailretourprelevementService.save(detailretourprelevementDTO);
    return ResponseEntity.created( new URI("/api/detailretourprelevements/"+ result.getCode())).body(result);
  }

  /**
   * PUT  /detailretourprelevements : Updates an existing detailretourprelevement.
   *
   * @param id
   * @param detailretourprelevementDTO the detailretourprelevement to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated detailretourprelevement,
   * or with status 400 (Bad Request) if the detailretourprelevement is not valid,
   * or with status 500 (Internal Server Error) if the detailretourprelevement couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/detailretourprelevements/{id}")
  public ResponseEntity<DetailRetourPrelevementDTO> updateDetailRetourPrelevement(@PathVariable Integer id, @Valid @RequestBody DetailRetourPrelevementDTO detailretourprelevementDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update DetailRetourPrelevement: {}",id);
    detailretourprelevementDTO.setCode(id);
    DetailRetourPrelevementDTO result =detailretourprelevementService.update(detailretourprelevementDTO);
    return ResponseEntity.ok().body(result);
  }

  /**
   * GET /detailretourprelevements/{id} : get the "id" detailretourprelevement.
   *
   * @param id the id of the detailretourprelevement to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of detailretourprelevement, or with status 404 (Not Found)
   */
  @GetMapping("/detailretourprelevements/{id}")
  public ResponseEntity<DetailRetourPrelevementDTO> getDetailRetourPrelevement(@PathVariable Integer id) {
    log.debug("Request to get DetailRetourPrelevement: {}",id);
    DetailRetourPrelevementDTO dto = detailretourprelevementService.findOne(id);
    RestPreconditions.checkFound(dto, "detailretourprelevement.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /detailretourprelevements : get all the detailretourprelevements.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of detailretourprelevements in body
   */
  @GetMapping("/detailretourprelevements")
  public Collection<DetailRetourPrelevementDTO> getAllDetailRetourPrelevements() {
    log.debug("Request to get all  DetailRetourPrelevements : {}");
    return detailretourprelevementService.findAll();
  }

  /**
   * DELETE  /detailretourprelevements/{id} : delete the "id" detailretourprelevement.
   *
   * @param id the id of the detailretourprelevement to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/detailretourprelevements/{id}")
  public ResponseEntity<Void> deleteDetailRetourPrelevement(@PathVariable Integer id) {
    log.debug("Request to delete DetailRetourPrelevement: {}",id);
    detailretourprelevementService.delete(id);
    return ResponseEntity.ok().build();
  }
}

