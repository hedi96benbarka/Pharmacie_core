//package com.csys.pharmacie.achat.web.rest;
//
//import com.csys.pharmacie.achat.domain.AjustementRetourPerimePK;
//import com.csys.pharmacie.achat.dto.AjustementRetourPerimeDTO;
//import com.csys.pharmacie.achat.service.AjustementRetourPerimeService;
//import com.csys.util.RestPreconditions;
//import java.lang.String;
//import java.lang.Void;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.Collection;
//import javax.validation.Valid;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * REST controller for managing AjustementRetourPerime.
// */
//@RestController
//@RequestMapping("/api")
//public class AjustementRetourPerimeResource {
//  private static final String ENTITY_NAME = "ajustementretourperime";
//
//  private final AjustementRetourPerimeService ajustementretourperimeService;
//
//  private final Logger log = LoggerFactory.getLogger(AjustementRetourPerimeService.class);
//
//  public AjustementRetourPerimeResource(AjustementRetourPerimeService ajustementretourperimeService) {
//    this.ajustementretourperimeService=ajustementretourperimeService;
//  }
//
//  /**
//   * POST  /ajustementretourperimes : Create a new ajustementretourperime.
//   *
//   * @param ajustementretourperimeDTO
//   * @param bindingResult
//   * @return the ResponseEntity with status 201 (Created) and with body the new ajustementretourperime, or with status 400 (Bad Request) if the ajustementretourperime has already an ID
//   * @throws URISyntaxException if the Location URI syntax is incorrect
//   * @throws org.springframework.web.bind.MethodArgumentNotValidException
//   */
//  @PostMapping("/ajustementretourperimes")
//  public ResponseEntity<AjustementRetourPerimeDTO> createAjustementRetourPerime(@Valid @RequestBody AjustementRetourPerimeDTO ajustementretourperimeDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
//    log.debug("REST request to save AjustementRetourPerime : {}", ajustementretourperimeDTO);
//    if ( ajustementretourperimeDTO.getAjustementRetourPerimePK() != null) {
//      bindingResult.addError( new FieldError("AjustementRetourPerimeDTO","ajustementRetourPerimePK","POST method does not accepte "+ENTITY_NAME+" with code"));
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    if (bindingResult.hasErrors()) {
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    AjustementRetourPerimeDTO result = ajustementretourperimeService.save(ajustementretourperimeDTO);
//    return ResponseEntity.created( new URI("/api/ajustementretourperimes/"+ result.getAjustementRetourPerimePK())).body(result);
//  }
//
//  /**
//   * PUT  /ajustementretourperimes : Updates an existing ajustementretourperime.
//   *
//   * @param id
//   * @param ajustementretourperimeDTO the ajustementretourperime to update
//   * @return the ResponseEntity with status 200 (OK) and with body the updated ajustementretourperime,
//   * or with status 400 (Bad Request) if the ajustementretourperime is not valid,
//   * or with status 500 (Internal Server Error) if the ajustementretourperime couldn't be updated
//   * @throws org.springframework.web.bind.MethodArgumentNotValidException
//   */
//  @PutMapping("/ajustementretourperimes/{id}")
//  public ResponseEntity<AjustementRetourPerimeDTO> updateAjustementRetourPerime(@PathVariable AjustementRetourPerimePK id, @Valid @RequestBody AjustementRetourPerimeDTO ajustementretourperimeDTO) throws MethodArgumentNotValidException {
//    log.debug("Request to update AjustementRetourPerime: {}",id);
//    ajustementretourperimeDTO.setAjustementRetourPerimePK(id);
//    AjustementRetourPerimeDTO result =ajustementretourperimeService.update(ajustementretourperimeDTO);
//    return ResponseEntity.ok().body(result);
//  }
//
//  /**
//   * GET /ajustementretourperimes/{id} : get the "id" ajustementretourperime.
//   *
//   * @param id the id of the ajustementretourperime to retrieve
//   * @return the ResponseEntity with status 200 (OK) and with body of ajustementretourperime, or with status 404 (Not Found)
//   */
//  @GetMapping("/ajustementretourperimes/{id}")
//  public ResponseEntity<AjustementRetourPerimeDTO> getAjustementRetourPerime(@PathVariable AjustementRetourPerimePK id) {
//    log.debug("Request to get AjustementRetourPerime: {}",id);
//    AjustementRetourPerimeDTO dto = ajustementretourperimeService.findOne(id);
//    RestPreconditions.checkFound(dto, "ajustementretourperime.NotFound");
//    return ResponseEntity.ok().body(dto);
//  }
//
//  /**
//   * GET /ajustementretourperimes : get all the ajustementretourperimes.
//   *
//   * @return the ResponseEntity with status 200 (OK) and the list of ajustementretourperimes in body
//   */
//  @GetMapping("/ajustementretourperimes")
//  public Collection<AjustementRetourPerimeDTO> getAllAjustementRetourPerimes() {
//    log.debug("Request to get all  AjustementRetourPerimes : {}");
//    return ajustementretourperimeService.findAll();
//  }
//
//  /**
//   * DELETE  /ajustementretourperimes/{id} : delete the "id" ajustementretourperime.
//   *
//   * @param id the id of the ajustementretourperime to delete
//   * @return the ResponseEntity with status 200 (OK)
//   */
//  @DeleteMapping("/ajustementretourperimes/{id}")
//  public ResponseEntity<Void> deleteAjustementRetourPerime(@PathVariable AjustementRetourPerimePK id) {
//    log.debug("Request to delete AjustementRetourPerime: {}",id);
//    ajustementretourperimeService.delete(id);
//    return ResponseEntity.ok().build();
//  }
//}
//
