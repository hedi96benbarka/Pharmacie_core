/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.KafkaProducerErrorGec;
import com.csys.pharmacie.achat.dto.KafkaProducerErrorGecDTO;
import com.csys.pharmacie.achat.factory.KafkaProducerErrorGecFactory;
import com.csys.pharmacie.achat.repository.KafkaProducerErrorGecRepository;
import com.csys.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Service
@Transactional
public class KafkaProducerErrorGecService {
  private final Logger log = LoggerFactory.getLogger(KafkaProducerErrorGecService.class);

  private final KafkaProducerErrorGecRepository kafkaProducerErrorGecRepository;

    public KafkaProducerErrorGecService(KafkaProducerErrorGecRepository kafkaProducerErrorGecRepository) {
        this.kafkaProducerErrorGecRepository = kafkaProducerErrorGecRepository;
    }
  

 
  /**
   * Save a anomaliecentralkafkaDTO.
   *
   * @param kafkaProducerErrorDTO
   * @return the persisted entity
   */
  public KafkaProducerErrorGecDTO save(KafkaProducerErrorGecDTO kafkaProducerErrorGecDTO) {
    log.debug("Request to save KafkaProducerError: {}",kafkaProducerErrorGecDTO);
    KafkaProducerErrorGec kafkaProducerErrorGec = KafkaProducerErrorGecFactory.kafkaProducerErrorGecDTOToKafkaProducerErrorGec(kafkaProducerErrorGecDTO);
    kafkaProducerErrorGec = kafkaProducerErrorGecRepository.save(kafkaProducerErrorGec);
    KafkaProducerErrorGecDTO resultDTO = KafkaProducerErrorGecFactory.kafkaProducerErrorGecToKafkaProducerErrorGecDTO(kafkaProducerErrorGec);
    return resultDTO;
  }

  /**
   * Update a kafkaProducerErrorDTO.
   *
   * @param kafkaProducerErrorDTO
   * @return the updated entity
   */
  public KafkaProducerErrorGecDTO update(KafkaProducerErrorGecDTO kafkaProducerErrorGecDTO) {
    log.debug("Request to update KafkaProducerError: {}",kafkaProducerErrorGecDTO);
    KafkaProducerErrorGec inBase= kafkaProducerErrorGecRepository.findOne(kafkaProducerErrorGecDTO.getCode());
    Preconditions.checkFound(inBase != null, "kafkaProducerError.NotFound");
    KafkaProducerErrorGec kafkaProducerErrorGec = KafkaProducerErrorGecFactory.kafkaProducerErrorGecDTOToKafkaProducerErrorGec(kafkaProducerErrorGecDTO);
    kafkaProducerErrorGec = kafkaProducerErrorGecRepository.save(kafkaProducerErrorGec);
    KafkaProducerErrorGecDTO resultDTO = KafkaProducerErrorGecFactory.kafkaProducerErrorGecToKafkaProducerErrorGecDTO(kafkaProducerErrorGec);
    return resultDTO;
  }

  /**
   * Get one kafkaProducerErrorDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(readOnly = true)
  public KafkaProducerErrorGecDTO findOne(Integer id) {
    log.debug("Request to get KafkaProducerError: {}",id);
    KafkaProducerErrorGec kafkaProducerErrorGec= kafkaProducerErrorGecRepository.findOne(id);
    KafkaProducerErrorGecDTO dto = KafkaProducerErrorGecFactory.kafkaProducerErrorGecToKafkaProducerErrorGecDTO(kafkaProducerErrorGec);
    return dto;
  }
}