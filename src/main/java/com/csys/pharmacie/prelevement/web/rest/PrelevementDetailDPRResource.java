package com.csys.pharmacie.prelevement.web.rest;

import com.csys.pharmacie.prelevement.dto.PrelevementDetailDPRDTO;
import com.csys.pharmacie.prelevement.service.PrelevementDetailDPRService;
import java.lang.String;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing PrelevementDetailDPR.
 */
@RestController
@RequestMapping("/api")
public class PrelevementDetailDPRResource {
  
  private static final String ENTITY_NAME = "prelevementdetaildpr";

  private final PrelevementDetailDPRService prelevementdetaildprService;

  private final Logger log = LoggerFactory.getLogger(PrelevementDetailDPRService.class);

  public PrelevementDetailDPRResource(PrelevementDetailDPRService prelevementdetaildprService) {
    this.prelevementdetaildprService=prelevementdetaildprService;
  }

 

  @GetMapping("/preleved-dpr-details/{id}")
  public List<PrelevementDetailDPRDTO>getPrelevmentDPR(@PathVariable Integer id){
  log.debug("Request to get PrelevmentDetailDPR:{}",id);
  return prelevementdetaildprService.findPrelevmentDetailDPRByCodeDPR(id);
  }
}

