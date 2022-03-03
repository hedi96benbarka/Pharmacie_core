package com.csys.pharmacie.prelevement.web.rest;

import com.csys.pharmacie.prelevement.domain.TraceDetailRetourPrPK;
import com.csys.pharmacie.prelevement.dto.TraceDetailRetourPrDTO;
import com.csys.pharmacie.prelevement.service.TraceDetailRetourPrService;
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
 * REST controller for managing TraceDetailRetourPr.
 */
@RestController
@RequestMapping("/api")
public class TraceDetailRetourPrResource {
  private static final String ENTITY_NAME = "tracedetailretourpr";

  private final TraceDetailRetourPrService tracedetailretourprService;

  private final Logger log = LoggerFactory.getLogger(TraceDetailRetourPrService.class);

  public TraceDetailRetourPrResource(TraceDetailRetourPrService tracedetailretourprService) {
    this.tracedetailretourprService=tracedetailretourprService;
  }

  /**
   * POST  /tracedetailretourprs : Create a new tracedetailretourpr.
   *
   * @param tracedetailretourprDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new tracedetailretourpr, or with status 400 (Bad Request) if the tracedetailretourpr has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/tracedetailretourprs")
  public ResponseEntity<TraceDetailRetourPrDTO> createTraceDetailRetourPr(@Valid @RequestBody TraceDetailRetourPrDTO tracedetailretourprDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save TraceDetailRetourPr : {}", tracedetailretourprDTO);
    if ( tracedetailretourprDTO.getTraceDetailRetourPrPK() != null) {
      bindingResult.addError( new FieldError("TraceDetailRetourPrDTO","traceDetailRetourPrPK","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    TraceDetailRetourPrDTO result = tracedetailretourprService.save(tracedetailretourprDTO);
    return ResponseEntity.created( new URI("/api/tracedetailretourprs/"+ result.getTraceDetailRetourPrPK())).body(result);
  }

  /**
   * PUT  /tracedetailretourprs : Updates an existing tracedetailretourpr.
   *
   * @param id
   * @param tracedetailretourprDTO the tracedetailretourpr to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated tracedetailretourpr,
   * or with status 400 (Bad Request) if the tracedetailretourpr is not valid,
   * or with status 500 (Internal Server Error) if the tracedetailretourpr couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/tracedetailretourprs/{id}")
  public ResponseEntity<TraceDetailRetourPrDTO> updateTraceDetailRetourPr(@PathVariable TraceDetailRetourPrPK id, @Valid @RequestBody TraceDetailRetourPrDTO tracedetailretourprDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update TraceDetailRetourPr: {}",id);
    tracedetailretourprDTO.setTraceDetailRetourPrPK(id);
    TraceDetailRetourPrDTO result =tracedetailretourprService.update(tracedetailretourprDTO);
    return ResponseEntity.ok().body(result);
  }

  /**
   * GET /tracedetailretourprs/{id} : get the "id" tracedetailretourpr.
   *
   * @param id the id of the tracedetailretourpr to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of tracedetailretourpr, or with status 404 (Not Found)
   */
  @GetMapping("/tracedetailretourprs/{id}")
  public ResponseEntity<TraceDetailRetourPrDTO> getTraceDetailRetourPr(@PathVariable TraceDetailRetourPrPK id) {
    log.debug("Request to get TraceDetailRetourPr: {}",id);
    TraceDetailRetourPrDTO dto = tracedetailretourprService.findOne(id);
    RestPreconditions.checkFound(dto, "tracedetailretourpr.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /tracedetailretourprs : get all the tracedetailretourprs.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of tracedetailretourprs in body
   */
  @GetMapping("/tracedetailretourprs")
  public Collection<TraceDetailRetourPrDTO> getAllTraceDetailRetourPrs() {
    log.debug("Request to get all  TraceDetailRetourPrs : {}");
    return tracedetailretourprService.findAll();
  }

  /**
   * DELETE  /tracedetailretourprs/{id} : delete the "id" tracedetailretourpr.
   *
   * @param id the id of the tracedetailretourpr to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/tracedetailretourprs/{id}")
  public ResponseEntity<Void> deleteTraceDetailRetourPr(@PathVariable TraceDetailRetourPrPK id) {
    log.debug("Request to delete TraceDetailRetourPr: {}",id);
    tracedetailretourprService.delete(id);
    return ResponseEntity.ok().build();
  }
}

