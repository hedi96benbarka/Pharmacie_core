package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.BaseTvaFactureDirecte;
import com.csys.pharmacie.achat.dto.BaseTvaFactureDirecteDTO;
import com.csys.pharmacie.achat.factory.BaseTvaFactureDirecteFactory;
import com.csys.pharmacie.achat.repository.BaseTvaFactureDirecteRepository;
import com.google.common.base.Preconditions;
import java.lang.Long;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing BaseTvaFactureDirecte.
 */
@Service
@Transactional
public class BaseTvaFactureDirecteService {
  private final Logger log = LoggerFactory.getLogger(BaseTvaFactureDirecteService.class);

  private final BaseTvaFactureDirecteRepository basetvafacturedirecteRepository;

  public BaseTvaFactureDirecteService(BaseTvaFactureDirecteRepository basetvafacturedirecteRepository) {
    this.basetvafacturedirecteRepository=basetvafacturedirecteRepository;
  }

  /**
   * Save a basetvafacturedirecteDTO.
   *
   * @param basetvafacturedirecteDTO
   * @return the persisted entity
   */
  public BaseTvaFactureDirecteDTO save(BaseTvaFactureDirecteDTO basetvafacturedirecteDTO) {
    log.debug("Request to save BaseTvaFactureDirecte: {}",basetvafacturedirecteDTO);
    BaseTvaFactureDirecte basetvafacturedirecte = BaseTvaFactureDirecteFactory.basetvafacturedirecteDTOToBaseTvaFactureDirecte(basetvafacturedirecteDTO);
    basetvafacturedirecte = basetvafacturedirecteRepository.save(basetvafacturedirecte);
    BaseTvaFactureDirecteDTO resultDTO = BaseTvaFactureDirecteFactory.basetvafacturedirecteToBaseTvaFactureDirecteDTO(basetvafacturedirecte);
    return resultDTO;
  }

  /**
   * Update a basetvafacturedirecteDTO.
   *
   * @param basetvafacturedirecteDTO
   * @return the updated entity
   */
  public BaseTvaFactureDirecteDTO update(BaseTvaFactureDirecteDTO basetvafacturedirecteDTO) {
    log.debug("Request to update BaseTvaFactureDirecte: {}",basetvafacturedirecteDTO);
    BaseTvaFactureDirecte inBase= basetvafacturedirecteRepository.findOne(basetvafacturedirecteDTO.getCode());
    Preconditions.checkArgument(inBase != null, "basetvafacturedirecte.NotFound");
    BaseTvaFactureDirecte basetvafacturedirecte = BaseTvaFactureDirecteFactory.basetvafacturedirecteDTOToBaseTvaFactureDirecte(basetvafacturedirecteDTO);
    basetvafacturedirecte = basetvafacturedirecteRepository.save(basetvafacturedirecte);
    BaseTvaFactureDirecteDTO resultDTO = BaseTvaFactureDirecteFactory.basetvafacturedirecteToBaseTvaFactureDirecteDTO(basetvafacturedirecte);
    return resultDTO;
  }

  /**
   * Get one basetvafacturedirecteDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public BaseTvaFactureDirecteDTO findOne(Long id) {
    log.debug("Request to get BaseTvaFactureDirecte: {}",id);
    BaseTvaFactureDirecte basetvafacturedirecte= basetvafacturedirecteRepository.findOne(id);
    BaseTvaFactureDirecteDTO dto = BaseTvaFactureDirecteFactory.basetvafacturedirecteToBaseTvaFactureDirecteDTO(basetvafacturedirecte);
    return dto;
  }

  /**
   * Get one basetvafacturedirecte by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public BaseTvaFactureDirecte findBaseTvaFactureDirecte(Long id) {
    log.debug("Request to get BaseTvaFactureDirecte: {}",id);
    BaseTvaFactureDirecte basetvafacturedirecte= basetvafacturedirecteRepository.findOne(id);
    return basetvafacturedirecte;
  }

  /**
   * Get all the basetvafacturedirectes.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public Collection<BaseTvaFactureDirecteDTO> findAll() {
    log.debug("Request to get All BaseTvaFactureDirectes");
    Collection<BaseTvaFactureDirecte> result= basetvafacturedirecteRepository.findAll();
    return BaseTvaFactureDirecteFactory.basetvafacturedirecteToBaseTvaFactureDirecteDTOs(result);
  }

  /**
   * Delete basetvafacturedirecte by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete BaseTvaFactureDirecte: {}",id);
    basetvafacturedirecteRepository.delete(id);
  }
  
}

