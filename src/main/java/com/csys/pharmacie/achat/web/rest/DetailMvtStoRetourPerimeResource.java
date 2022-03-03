package com.csys.pharmacie.achat.web.rest;


import com.csys.pharmacie.achat.dto.DetailMvtStoRetourPerimeDTO;
import com.csys.pharmacie.achat.service.DetailMvtStoRetourPerimeService;
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
 * REST controller for managing Detail_MvtStoRetourPerime.
 */
@RestController
@RequestMapping("/api")
public class DetailMvtStoRetourPerimeResource {
  private static final String ENTITY_NAME = "detailMvtstoretourperime";

  private final DetailMvtStoRetourPerimeService detailMvtstoretourperimeService;

  private final Logger log = LoggerFactory.getLogger(DetailMvtStoRetourPerimeService.class);

  public DetailMvtStoRetourPerimeResource(DetailMvtStoRetourPerimeService detail_mvtstoretourperimeService) {
    this.detailMvtstoretourperimeService=detail_mvtstoretourperimeService;
  }

  /**
   * POST  /detail_mvtstoretourperimes : Create a new detail_mvtstoretourperime.
   *
   * @param detail_mvtstoretourperimeDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new detail_mvtstoretourperime, or with status 400 (Bad Request) if the detail_mvtstoretourperime has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/detailMvtstoretourperimes")
  public ResponseEntity<DetailMvtStoRetourPerimeDTO> createDetail_MvtStoRetourPerime(@Valid @RequestBody DetailMvtStoRetourPerimeDTO detail_mvtstoretourperimeDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save Detail_MvtStoRetourPerime : {}", detail_mvtstoretourperimeDTO);
    if ( detail_mvtstoretourperimeDTO.getCode() != null) {
      bindingResult.addError( new FieldError("Detail_MvtStoRetourPerimeDTO","code","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    DetailMvtStoRetourPerimeDTO result = detailMvtstoretourperimeService.save(detail_mvtstoretourperimeDTO);
    return ResponseEntity.created( new URI("/api/detailMvtstoretourperimes/"+ result.getCode())).body(result);
  }

  /**
   * PUT  /detail_mvtstoretourperimes : Updates an existing detail_mvtstoretourperime.
   *
   * @param id
   * @param detail_mvtstoretourperimeDTO the detail_mvtstoretourperime to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated detail_mvtstoretourperime,
   * or with status 400 (Bad Request) if the detail_mvtstoretourperime is not valid,
   * or with status 500 (Internal Server Error) if the detail_mvtstoretourperime couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/detailMvtstoretourperimes/{id}")
  public ResponseEntity<DetailMvtStoRetourPerimeDTO> updateDetail_MvtStoRetourPerime(@PathVariable Integer id, @Valid @RequestBody DetailMvtStoRetourPerimeDTO detail_mvtstoretourperimeDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update Detail_MvtStoRetourPerime: {}",id);
    detail_mvtstoretourperimeDTO.setCode(id);
    DetailMvtStoRetourPerimeDTO result =detailMvtstoretourperimeService.update(detail_mvtstoretourperimeDTO);
    return ResponseEntity.ok().body(result);
  }

  /**
   * GET /detail_mvtstoretourperimes/{id} : get the "id" detail_mvtstoretourperime.
   *
   * @param id the id of the detail_mvtstoretourperime to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of detail_mvtstoretourperime, or with status 404 (Not Found)
   */
  @GetMapping("/detailMvtstoretourperimes/{id}")
  public ResponseEntity<DetailMvtStoRetourPerimeDTO> getDetail_MvtStoRetourPerime(@PathVariable Integer id) {
    log.debug("Request to get Detail_MvtStoRetourPerime: {}",id);
    DetailMvtStoRetourPerimeDTO dto = detailMvtstoretourperimeService.findOne(id);
    RestPreconditions.checkFound(dto, "detailMvtstoretourperime.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /detail_mvtstoretourperimes : get all the detail_mvtstoretourperimes.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of detail_mvtstoretourperimes in body
   */
  @GetMapping("/detailMvtstoretourperimes")
  public Collection<DetailMvtStoRetourPerimeDTO> getAllDetail_MvtStoRetourPerimes() {
    log.debug("Request to get all  Detail_MvtStoRetourPerimes : {}");
    return detailMvtstoretourperimeService.findAll();
  }

  /**
   * DELETE  /detail_mvtstoretourperimes/{id} : delete the "id" detail_mvtstoretourperime.
   *
   * @param id the id of the detail_mvtstoretourperime to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/detailMvtstoretourperimes/{id}")
  public ResponseEntity<Void> deleteDetail_MvtStoRetourPerime(@PathVariable Integer id) {
    log.debug("Request to delete Detail_MvtStoRetourPerime: {}",id);
    detailMvtstoretourperimeService.delete(id);
    return ResponseEntity.ok().build();
  }
}

