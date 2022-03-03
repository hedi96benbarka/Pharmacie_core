package com.csys.pharmacie.achat.service;


import com.csys.pharmacie.achat.repository.BaseTvaReceptionTemporaireRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BaseTvaReceptionTemporaireService {
  private final Logger log = LoggerFactory.getLogger(BaseTvaReceptionTemporaireService.class);

  private final BaseTvaReceptionTemporaireRepository basetvareceptiontemporaireRepository;

  public BaseTvaReceptionTemporaireService(BaseTvaReceptionTemporaireRepository basetvareceptiontemporaireRepository) {
    this.basetvareceptiontemporaireRepository=basetvareceptiontemporaireRepository;
  }

//  
 


}

