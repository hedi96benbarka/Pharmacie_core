package com.csys.pharmacie.inventaire.web.rest;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.inventaire.dto.DepStoHistDTO;
import com.csys.pharmacie.inventaire.dto.TypeEnvoieEtatEnum;
import com.csys.pharmacie.inventaire.factory.DepStoHistFactory;
import com.csys.pharmacie.inventaire.service.DepStoHistService;
import com.csys.util.RestPreconditions;
import java.io.IOException;
import java.lang.Long;
import java.lang.String;
import java.lang.Void;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing DepStoHist.
 */
@RestController
@RequestMapping("/api")
public class DepStoHistResource {
  private static final String ENTITY_NAME = "depstohist";

  private final DepStoHistService depstohistService;

  private final Logger log = LoggerFactory.getLogger(DepStoHistService.class);

  public DepStoHistResource(DepStoHistService depstohistService) {
    this.depstohistService=depstohistService;
  }

  /**
   * POST  /depstohists : Create a new depstohist.
   *
   * @param depstohistDTO
   * @param bindingResult
   * @return the ResponseEntity with status 201 (Created) and with body the new depstohist, or with status 400 (Bad Request) if the depstohist has already an ID
   * @throws URISyntaxException if the Location URI syntax is incorrect
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PostMapping("/depstohists")
  public ResponseEntity<DepStoHistDTO> createDepStoHist(@Valid @RequestBody DepStoHistDTO depstohistDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save DepStoHist : {}", depstohistDTO);
    if ( depstohistDTO.getNum() != null) {
      bindingResult.addError( new FieldError("DepStoHistDTO","num","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    DepStoHistDTO result = depstohistService.save(depstohistDTO);
    return ResponseEntity.created( new URI("/api/depstohists/"+ result.getNum())).body(result);
  }

  /**
   * PUT  /depstohists : Updates an existing depstohist.
   *
   * @param id
   * @param depstohistDTO the depstohist to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated depstohist,
   * or with status 400 (Bad Request) if the depstohist is not valid,
   * or with status 500 (Internal Server Error) if the depstohist couldn't be updated
   * @throws org.springframework.web.bind.MethodArgumentNotValidException
   */
  @PutMapping("/depstohists/{id}")
  public ResponseEntity<DepStoHistDTO> updateDepStoHist(@PathVariable Integer id, @Valid @RequestBody DepStoHistDTO depstohistDTO) throws MethodArgumentNotValidException {
    log.debug("Request to update DepStoHist: {}",id);
    depstohistDTO.setNum(id);
    DepStoHistDTO result =depstohistService.update(depstohistDTO);
    return ResponseEntity.ok().body(result);
  }

  /**
   * GET /depstohists/{id} : get the "id" depstohist.
   *
   * @param id the id of the depstohist to retrieve
   * @return the ResponseEntity with status 200 (OK) and with body of depstohist, or with status 404 (Not Found)
   */
  @GetMapping("/depstohists/{id}")
  public ResponseEntity<DepStoHistDTO> getDepStoHist(@PathVariable Integer id) {
    log.debug("Request to get DepStoHist: {}",id);
    DepStoHistDTO dto = depstohistService.findOne(id);
    RestPreconditions.checkFound(dto, "depstohist.NotFound");
    return ResponseEntity.ok().body(dto);
  }

  /**
   * GET /depstohists : get all the depstohists.
   *
   * @return the ResponseEntity with status 200 (OK) and the list of depstohists in body
   */
  @GetMapping("/depstohists")
  public Collection<DepStoHistDTO> getAllDepStoHists() {
    log.debug("Request to get all  DepStoHists : {}");
    return depstohistService.findAll();
  }

  /**
   * DELETE  /depstohists/{id} : delete the "id" depstohist.
   *
   * @param id the id of the depstohist to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/depstohists/{id}")
  public ResponseEntity<Void> deleteDepStoHist(@PathVariable Integer id) {
    log.debug("Request to delete DepStoHist: {}",id);
    depstohistService.delete(id);
    return ResponseEntity.ok().build();
  }
    @GetMapping("/depstohists/byCodeInventaire")
  public List<DepStoHistDTO> findByInventaire_Code(@RequestParam(required = true,name = "numInventaire") Integer numInventaire){
      return depstohistService.findByInventaire_Code(numInventaire);
  }
  
     @GetMapping("/depstohists/editionEtatInv")
    public ResponseEntity<byte[]> editionEtatEcartAvVld(@RequestParam Integer numInventaire) throws URISyntaxException, ReportSDKException, IOException, SQLException, ReportSDKException, com.crystaldecisions.sdk.occa.report.lib.ReportSDKException, com.crystaldecisions12.sdk.occa.report.lib.ReportSDKException {
        byte[] bytePdf = depstohistService.editionEtatEcartAv(numInventaire);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(bytePdf);
    }
    
}

