package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.BaseTvaFactureBonReception;
import com.csys.pharmacie.achat.dto.BaseTvaFactureBonReceptionDTO;
import com.csys.pharmacie.achat.factory.BaseTvaFactureBonReceptionFactory;
import com.csys.pharmacie.achat.repository.BaseTvaFactureBonReceptionRepository;
import com.google.common.base.Preconditions;
import java.lang.Long;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing BaseTvaFactureBonReception.
 */
@Service
@Transactional
public class BaseTvaFactureBonReceptionService {
  private final Logger log = LoggerFactory.getLogger(BaseTvaFactureBonReceptionService.class);

  private final BaseTvaFactureBonReceptionRepository basetvafacturebonreceptionRepository;

  public BaseTvaFactureBonReceptionService(BaseTvaFactureBonReceptionRepository basetvafacturebonreceptionRepository) {
    this.basetvafacturebonreceptionRepository=basetvafacturebonreceptionRepository;
  }

  /**
   * Save a basetvafacturebonreceptionDTO.
   *
   * @param basetvafacturebonreceptionDTO
   * @return the persisted entity
   */
  public BaseTvaFactureBonReceptionDTO save(BaseTvaFactureBonReceptionDTO basetvafacturebonreceptionDTO) {
    log.debug("Request to save BaseTvaFactureBonReception: {}",basetvafacturebonreceptionDTO);
    BaseTvaFactureBonReception basetvafacturebonreception = BaseTvaFactureBonReceptionFactory.basetvafacturebonreceptionDTOToBaseTvaFactureBonReception(basetvafacturebonreceptionDTO);
    basetvafacturebonreception = basetvafacturebonreceptionRepository.save(basetvafacturebonreception);
    BaseTvaFactureBonReceptionDTO resultDTO = BaseTvaFactureBonReceptionFactory.basetvafacturebonreceptionToBaseTvaFactureBonReceptionDTO(basetvafacturebonreception);
    return resultDTO;
  }

  /**
   * Update a basetvafacturebonreceptionDTO.
   *
   * @param basetvafacturebonreceptionDTO
   * @return the updated entity
   */
  public BaseTvaFactureBonReceptionDTO update(BaseTvaFactureBonReceptionDTO basetvafacturebonreceptionDTO) {
    log.debug("Request to update BaseTvaFactureBonReception: {}",basetvafacturebonreceptionDTO);
    BaseTvaFactureBonReception inBase= basetvafacturebonreceptionRepository.findOne(basetvafacturebonreceptionDTO.getCode());
    Preconditions.checkArgument(inBase != null, "basetvafacturebonreception.NotFound");
    BaseTvaFactureBonReception basetvafacturebonreception = BaseTvaFactureBonReceptionFactory.basetvafacturebonreceptionDTOToBaseTvaFactureBonReception(basetvafacturebonreceptionDTO);
    basetvafacturebonreception = basetvafacturebonreceptionRepository.save(basetvafacturebonreception);
    BaseTvaFactureBonReceptionDTO resultDTO = BaseTvaFactureBonReceptionFactory.basetvafacturebonreceptionToBaseTvaFactureBonReceptionDTO(basetvafacturebonreception);
    return resultDTO;
  }

  /**
   * Get one basetvafacturebonreceptionDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public BaseTvaFactureBonReceptionDTO findOne(Long id) {
    log.debug("Request to get BaseTvaFactureBonReception: {}",id);
    BaseTvaFactureBonReception basetvafacturebonreception= basetvafacturebonreceptionRepository.findOne(id);
    BaseTvaFactureBonReceptionDTO dto = BaseTvaFactureBonReceptionFactory.basetvafacturebonreceptionToBaseTvaFactureBonReceptionDTO(basetvafacturebonreception);
    return dto;
  }

  /**
   * Get one basetvafacturebonreception by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public BaseTvaFactureBonReception findBaseTvaFactureBonReception(Long id) {
    log.debug("Request to get BaseTvaFactureBonReception: {}",id);
    BaseTvaFactureBonReception basetvafacturebonreception= basetvafacturebonreceptionRepository.findOne(id);
    return basetvafacturebonreception;
  }

  /**
   * Get all the basetvafacturebonreceptions.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public Collection<BaseTvaFactureBonReceptionDTO> findAll() {
    log.debug("Request to get All BaseTvaFactureBonReceptions");
    Collection<BaseTvaFactureBonReception> result= basetvafacturebonreceptionRepository.findAll();
    return BaseTvaFactureBonReceptionFactory.basetvafacturebonreceptionToBaseTvaFactureBonReceptionDTOs(result);
  }

  /**
   * Delete basetvafacturebonreception by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete BaseTvaFactureBonReception: {}",id);
    basetvafacturebonreceptionRepository.delete(id);
  }
}

