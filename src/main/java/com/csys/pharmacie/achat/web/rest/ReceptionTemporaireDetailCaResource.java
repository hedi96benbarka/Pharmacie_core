package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCaPK;
import com.csys.pharmacie.achat.dto.ReceptionTemporaireDetailCaDTO;
import com.csys.pharmacie.achat.service.ReceptionTemporaireDetailCaService;
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

@RestController
@RequestMapping("/api")
public class ReceptionTemporaireDetailCaResource {
  private static final String ENTITY_NAME = "receptiontemporairedetailca";

  private final ReceptionTemporaireDetailCaService receptiontemporairedetailcaService;

  private final Logger log = LoggerFactory.getLogger(ReceptionTemporaireDetailCaService.class);

  public ReceptionTemporaireDetailCaResource(ReceptionTemporaireDetailCaService receptiontemporairedetailcaService) {
    this.receptiontemporairedetailcaService=receptiontemporairedetailcaService;
  }

  @PostMapping("/receptiontemporairedetailcas")
  public ResponseEntity<ReceptionTemporaireDetailCaDTO> createReceptionTemporaireDetailCa(@Valid @RequestBody ReceptionTemporaireDetailCaDTO receptiontemporairedetailcaDTO, BindingResult bindingResult) throws URISyntaxException, MethodArgumentNotValidException {
    log.debug("REST request to save ReceptionTemporaireDetailCa : {}", receptiontemporairedetailcaDTO);
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if ( receptiontemporairedetailcaDTO.getReceptionTemporaireDetailCaPK() != null) {
      bindingResult.addError( new FieldError("ReceptionTemporaireDetailCaDTO","receptionTemporaireDetailCaPK","POST method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    ReceptionTemporaireDetailCaDTO result = receptiontemporairedetailcaService.save(receptiontemporairedetailcaDTO);
    return ResponseEntity.created( new URI("/api/receptiontemporairedetailcas/"+ result.getReceptionTemporaireDetailCaPK())).body(result);
  }

  @PutMapping("/receptiontemporairedetailcas")
  public ResponseEntity<ReceptionTemporaireDetailCaDTO> updateReceptionTemporaireDetailCa(@Valid @RequestBody ReceptionTemporaireDetailCaDTO receptiontemporairedetailcaDTO, BindingResult bindingResult) throws MethodArgumentNotValidException {
    log.debug("Request to update ReceptionTemporaireDetailCa: {}",receptiontemporairedetailcaDTO);
    if (bindingResult.hasErrors()) {
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    if ( receptiontemporairedetailcaDTO.getReceptionTemporaireDetailCaPK() == null) {
      bindingResult.addError( new FieldError("ReceptionTemporaireDetailCaDTO","receptionTemporaireDetailCaPK","PUT method does not accepte "+ENTITY_NAME+" with code"));
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
    ReceptionTemporaireDetailCaDTO result =receptiontemporairedetailcaService.update(receptiontemporairedetailcaDTO);
    return ResponseEntity.ok().body(result);
  }

  @GetMapping("/receptiontemporairedetailcas/{id}")
  public ResponseEntity<ReceptionTemporaireDetailCaDTO> getReceptionTemporaireDetailCa(@PathVariable ReceptionTemporaireDetailCaPK id) {
    log.debug("Request to get ReceptionTemporaireDetailCa: {}",id);
    ReceptionTemporaireDetailCaDTO dto = receptiontemporairedetailcaService.findOne(id);
    return ResponseEntity.ok().body(dto);
  }

  public Collection<ReceptionTemporaireDetailCaDTO> getAllReceptionTemporaireDetailCas() {
    log.debug("Request to get all  ReceptionTemporaireDetailCas : {}");
    return receptiontemporairedetailcaService.findAll();
  }

  @DeleteMapping("/receptiontemporairedetailcas/{id}")
  public ResponseEntity<Void> deleteReceptionTemporaireDetailCa(@PathVariable ReceptionTemporaireDetailCaPK id) {
    log.debug("Request to delete ReceptionTemporaireDetailCa: {}",id);
    receptiontemporairedetailcaService.delete(id);
    return ResponseEntity.ok().build();
  }
}

