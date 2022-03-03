/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.KafkaConsumerError;
import com.csys.pharmacie.achat.dto.KafkaConsumerErrorDTO;
import com.csys.pharmacie.achat.factory.KafkaConsumerErrorFactory;
import com.csys.util.Preconditions;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrateur
 */
@Service
@Transactional
public class KafkaConsumerErrorService {
  private final Logger log = LoggerFactory.getLogger(KafkaConsumerErrorService.class);

  private final KafkaConsumerErrorRepository kafkaConsumerErrorRepository;

    public KafkaConsumerErrorService(KafkaConsumerErrorRepository kafkaConsumerErrorRepository) {
        this.kafkaConsumerErrorRepository = kafkaConsumerErrorRepository;
    }
  

 
  /**
   * Save a anomaliecentralkafkaDTO.
   *
   * @param kafkaConsumerErrorDTO
   * @return the persisted entity
   */
  public KafkaConsumerErrorDTO save(KafkaConsumerErrorDTO kafkaConsumerErrorDTO) {
    log.debug("Request to save kafkaConsumerErrorDTO:");
    KafkaConsumerError kafkaConsumerError = KafkaConsumerErrorFactory.kafkaConsumerErrorDTOToKafkaConsumerError(kafkaConsumerErrorDTO);
      log.debug("Request to save KafkaConsumerError: {}",kafkaConsumerError);
    kafkaConsumerError = kafkaConsumerErrorRepository.save(kafkaConsumerError);
    KafkaConsumerErrorDTO resultDTO = KafkaConsumerErrorFactory.kafkaConsumerErrorToKafkaConsumerErrorDTO(kafkaConsumerError);
    return resultDTO;
  }

  /**
   * Update a kafkaConsumerErrorDTO.
   *
   * @param kafkaConsumerErrorDTO
   * @return the updated entity
   */
  public KafkaConsumerErrorDTO update(KafkaConsumerErrorDTO kafkaConsumerErrorDTO) {
    log.debug("Request to update KafkaConsumerError: {}",kafkaConsumerErrorDTO);
    KafkaConsumerError inBase= kafkaConsumerErrorRepository.findOne(kafkaConsumerErrorDTO.getCode());
    Preconditions.checkFound(inBase != null, "kafkaConsumerError.NotFound");
    KafkaConsumerError kafkaConsumerError = KafkaConsumerErrorFactory.kafkaConsumerErrorDTOToKafkaConsumerError(kafkaConsumerErrorDTO);
    kafkaConsumerError = kafkaConsumerErrorRepository.save(kafkaConsumerError);
    KafkaConsumerErrorDTO resultDTO = KafkaConsumerErrorFactory.kafkaConsumerErrorToKafkaConsumerErrorDTO(kafkaConsumerError);
    return resultDTO;
  }

  /**
   * Get one kafkaConsumerErrorDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(readOnly = true)
  public KafkaConsumerErrorDTO findOne(Integer id) {
    log.debug("Request to get KafkaConsumerError: {}",id);
    KafkaConsumerError kafkaConsumerError= kafkaConsumerErrorRepository.findOne(id);
    KafkaConsumerErrorDTO dto = KafkaConsumerErrorFactory.kafkaConsumerErrorToKafkaConsumerErrorDTO(kafkaConsumerError);
    return dto;
  }

  /**
   * Get one kafkaConsumerError by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional( readOnly = true )
  public KafkaConsumerError findKafkaConsumerError(Integer id) {
    log.debug("Request to get KafkaConsumerError: {}",id);
    KafkaConsumerError kafkaConsumerError= kafkaConsumerErrorRepository.findOne(id);
    return kafkaConsumerError;
  }

  /**
   * Get all the kafkaConsumerErrors.
   *
   * @return the the list of entities
   */
  @Transactional(readOnly = true)
  public Collection<KafkaConsumerErrorDTO> findAll() {
    log.debug("Request to get All KafkaConsumerErrors");
    Collection<KafkaConsumerError> result= kafkaConsumerErrorRepository.findAll();
    return KafkaConsumerErrorFactory.KafkaConsumerErrorToKafkaConsumerErrorDTOs(result);
  }

  /**
   * Delete kafkaConsumerError by id.
   *
   * @param id the id of the entity
   */
  public void delete(Integer id) {
    log.debug("Request to delete KafkaConsumerError: {}",id);
    kafkaConsumerErrorRepository.delete(id);
  }
}


