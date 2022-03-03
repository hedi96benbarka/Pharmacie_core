package com.csys.pharmacie.achat.web.rest;

import com.csys.pharmacie.achat.dto.BaseTvaReceptionTemporaireDTO;
import com.csys.pharmacie.achat.service.BaseTvaReceptionTemporaireService;
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

@RestController
@RequestMapping("/api")
public class BaseTvaReceptionTemporaireResource {
  private static final String ENTITY_NAME = "basetvareceptiontemporaire";

  private final BaseTvaReceptionTemporaireService basetvareceptiontemporaireService;

  private final Logger log = LoggerFactory.getLogger(BaseTvaReceptionTemporaireService.class);

  public BaseTvaReceptionTemporaireResource(BaseTvaReceptionTemporaireService basetvareceptiontemporaireService) {
    this.basetvareceptiontemporaireService=basetvareceptiontemporaireService;
  }



//  @GetMapping("/basetvareceptiontemporaires/{id}")
//  public ResponseEntity<BaseTvaReceptionTemporaireDTO> getBaseTvaReceptionTemporaire(@PathVariable Long id) {
//    log.debug("Request to get BaseTvaReceptionTemporaire: {}",id);
//    BaseTvaReceptionTemporaireDTO dto = basetvareceptiontemporaireService.findOne(id);
//    return ResponseEntity.ok().body(dto);
//  }
//
//  public Collection<BaseTvaReceptionTemporaireDTO> getAllBaseTvaReceptionTemporaires() {
//    log.debug("Request to get all  BaseTvaReceptionTemporaires : {}");
//    return basetvareceptiontemporaireService.findAll();
//  }


}

