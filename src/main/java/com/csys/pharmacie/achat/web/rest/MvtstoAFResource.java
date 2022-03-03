package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.MvtstoAFDTO;
import com.csys.pharmacie.achat.service.MvtstoAFService;
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
 * REST controller for managing MvtstoAF.
 */
@RestController
@RequestMapping("/api")
public class MvtstoAFResource {
  private static final String ENTITY_NAME = "mvtstoaf";

  private final MvtstoAFService mvtstoafService;

  private final Logger log = LoggerFactory.getLogger(MvtstoAFService.class);

  public MvtstoAFResource(MvtstoAFService mvtstoafService) {
    this.mvtstoafService=mvtstoafService;
  }

  /**
   * POST  /mvtstoafs : Create a new mvtstoaf.
   *
   * @param mvtstoafDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new mvtstoaf, or with status 400 (Bad Request) if the mvtstoaf has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */


  /**
   * PUT  /mvtstoafs : Updates an existing mvtstoaf.
   *
   * @param id
   * @param mvtstoafDTO the mvtstoaf to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated mvtstoaf,
   * or with status 400 (Bad Request) if the mvtstoaf is not valid,
   * or with status 500 (Internal Server Error) if the mvtstoaf couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */


  /**
   * GET /mvtstoafs/{id} : get the "id" mvtstoaf.
   *
   * @param id the id of the mvtstoaf to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of mvtstoaf, or with status 404 (Not Found)
   */
  @GetMapping("/mvtstoafs/{id}")
  public ResponseEntity<MvtstoAFDTO> getMvtstoAF(@PathVariable Integer id) {
    log.debug("Request to get MvtstoAF: {}",id);
    MvtstoAFDTO dto = mvtstoafService.findOne(id);
    RestPreconditions.checkFound(dto, "mvtstoaf.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /mvtstoafs : get all the mvtstoafs.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of mvtstoafs in body
   */
  @GetMapping("/mvtstoafs")
  public Collection<MvtstoAFDTO> getAllMvtstoAFs() {
    log.debug("Request to get all  MvtstoAFs : {}");
    return mvtstoafService.findAll();
  }

  /**
   * DELETE  /mvtstoafs/{id} : delete the "id" mvtstoaf.
   *
   * @param id the id of the mvtstoaf to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/mvtstoafs/{id}")
  public ResponseEntity<Void> deleteMvtstoAF(@PathVariable Integer id) {
    log.debug("Request to delete MvtstoAF: {}",id);
    mvtstoafService.delete(id);
    return ResponseEntity.ok().build();
  }
}

