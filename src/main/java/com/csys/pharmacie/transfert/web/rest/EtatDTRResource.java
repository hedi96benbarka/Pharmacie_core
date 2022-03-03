package com.csys.pharmacie.transfert.web.rest;

import com.csys.pharmacie.transfert.dto.EtatDTRDTO;
import com.csys.pharmacie.transfert.service.EtatDTRService;
import com.csys.util.RestPreconditions;
import java.lang.Integer;
import java.lang.String;
import java.lang.Void;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
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
 * REST controller for managing EtatDTR.
 */
@RestController
@RequestMapping("/api")
public class EtatDTRResource {
  private static final String ENTITY_NAME = "etatdtr";

  private final EtatDTRService etatdtrService;

  private final Logger log = LoggerFactory.getLogger(EtatDTRService.class);

  public EtatDTRResource(EtatDTRService etatdtrService) {
    this.etatdtrService=etatdtrService;
  }


  
  
 @PostMapping("/dtr-transfer-state/searches")
  public List<EtatDTRDTO>getEtatTransfertDTR(@RequestBody List<Integer>codesDTR){
  log.debug("Request to get EtatDTR:{}",codesDTR);
  return etatdtrService.FindByDTRIn(codesDTR);} 
  
  
  
}

