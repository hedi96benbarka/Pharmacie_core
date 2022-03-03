//package com.csys.pharmacie.achat.web.rest;
//
//import com.csys.pharmacie.achat.dto.BaseTvaFactureRetourPerimeDTO;
//import com.csys.pharmacie.achat.service.BaseTvaFactureRetourPerimeService;
//import com.csys.util.RestPreconditions;
//import java.lang.Long;
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
// * REST controller for managing BaseTvaFactureRetourPerime.
// */
//@RestController
//@RequestMapping("/api")
//public class BaseTvaFactureRetourPerimeResource {
//  private static final String ENTITY_NAME = "basetvafactureretourperime";
//
//  private final BaseTvaFactureRetourPerimeService basetvafactureretourperimeService;
//
//  private final Logger log = LoggerFactory.getLogger(BaseTvaFactureRetourPerimeService.class);
//
//  public BaseTvaFactureRetourPerimeResource(BaseTvaFactureRetourPerimeService basetvafactureretourperimeService) {
//    this.basetvafactureretourperimeService=basetvafactureretourperimeService;
//  }
//
//  /**
//   * POST  /basetvafactureretourperimes : Create a new basetvafactureretourperime.
//   *
//   * @param basetvafactureretourperimeDTO
//   * @param bindingResult
//   * @return the ResponseEntity with status 201 (Created) and with body the new basetvafactureretourperime, or with status 400 (Bad Request) if the basetvafactureretourperime has already an ID
//   * @throws URISyntaxException if the Location URI syntax is incorrect
//   * @throws org.springframework.web.bind.MethodArgumentNotValidException
//   */
//  @PostMapping("/basetvafactureretourperimes")
//  public ResponseEntity<BaseTvaFactureRetourPerimeDTO> createBaseTvaFactureRetourPerime(@Valid @RequestBody BaseTvaFactureRetourPerimeDTO basetvafactureretourperimeDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
//    log.debug("REST request to save BaseTvaFactureRetourPerime : {}", basetvafactureretourperimeDTO);
//    if ( basetvafactureretourperimeDTO.getCode() != null) {
//      bindingResult.addError( new FieldError("BaseTvaFactureRetourPerimeDTO","code","POST method does not accepte "+ENTITY_NAME+" with code"));
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    if (bindingResult.hasErrors()) {
//      throw new MethodArgumentNotValidException(null, bindingResult);
//    }
//    BaseTvaFactureRetourPerimeDTO result = basetvafactureretourperimeService.save(basetvafactureretourperimeDTO);
//    return ResponseEntity.created( new URI("/api/basetvafactureretourperimes/"+ result.getCode())).body(result);
//  }
//
//  /**
//   * PUT  /basetvafactureretourperimes : Updates an existing basetvafactureretourperime.
//   *
//   * @param id
//   * @param basetvafactureretourperimeDTO the basetvafactureretourperime to update
//   * @return the ResponseEntity with status 200 (OK) and with body the updated basetvafactureretourperime,
//   * or with status 400 (Bad Request) if the basetvafactureretourperime is not valid,
//   * or with status 500 (Internal Server Error) if the basetvafactureretourperime couldn't be updated
//   * @throws org.springframework.web.bind.MethodArgumentNotValidException
//   */
//  @PutMapping("/basetvafactureretourperimes/{id}")
//  public ResponseEntity<BaseTvaFactureRetourPerimeDTO> updateBaseTvaFactureRetourPerime(@PathVariable Long id, @Valid @RequestBody BaseTvaFactureRetourPerimeDTO basetvafactureretourperimeDTO) throws MethodArgumentNotValidException {
//    log.debug("Request to update BaseTvaFactureRetourPerime: {}",id);
//    basetvafactureretourperimeDTO.setCode(id);
//    BaseTvaFactureRetourPerimeDTO result =basetvafactureretourperimeService.update(basetvafactureretourperimeDTO);
//    return ResponseEntity.ok().body(result);
//  }
//
//  /**
//   * GET /basetvafactureretourperimes/{id} : get the "id" basetvafactureretourperime.
//   *
//   * @param id the id of the basetvafactureretourperime to retrieve
//   * @return the ResponseEntity with status 200 (OK) and with body of basetvafactureretourperime, or with status 404 (Not Found)
//   */
//  @GetMapping("/basetvafactureretourperimes/{id}")
//  public ResponseEntity<BaseTvaFactureRetourPerimeDTO> getBaseTvaFactureRetourPerime(@PathVariable Long id) {
//    log.debug("Request to get BaseTvaFactureRetourPerime: {}",id);
//    BaseTvaFactureRetourPerimeDTO dto = basetvafactureretourperimeService.findOne(id);
//    RestPreconditions.checkFound(dto, "basetvafactureretourperime.NotFound");
//    return ResponseEntity.ok().body(dto);
//  }
//
//  /**
//   * GET /basetvafactureretourperimes : get all the basetvafactureretourperimes.
//   *
//   * @return the ResponseEntity with status 200 (OK) and the list of basetvafactureretourperimes in body
//   */
//  @GetMapping("/basetvafactureretourperimes")
//  public Collection<BaseTvaFactureRetourPerimeDTO> getAllBaseTvaFactureRetourPerimes() {
//    log.debug("Request to get all  BaseTvaFactureRetourPerimes : {}");
//    return basetvafactureretourperimeService.findAll();
//  }
//
//  /**
//   * DELETE  /basetvafactureretourperimes/{id} : delete the "id" basetvafactureretourperime.
//   *
//   * @param id the id of the basetvafactureretourperime to delete
//   * @return the ResponseEntity with status 200 (OK)
//   */
//  @DeleteMapping("/basetvafactureretourperimes/{id}")
//  public ResponseEntity<Void> deleteBaseTvaFactureRetourPerime(@PathVariable Long id) {
//    log.debug("Request to delete BaseTvaFactureRetourPerime: {}",id);
//    basetvafactureretourperimeService.delete(id);
//    return ResponseEntity.ok().build();
//  }
//}
//
