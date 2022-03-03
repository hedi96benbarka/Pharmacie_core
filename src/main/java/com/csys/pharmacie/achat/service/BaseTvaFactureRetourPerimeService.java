package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.BaseTvaFactureRetourPerime;
import com.csys.pharmacie.achat.dto.BaseTvaFactureRetourPerimeDTO;
import com.csys.pharmacie.achat.factory.BaseTvaFactureRetourPerimeFactory;
import com.csys.pharmacie.achat.repository.BaseTvaFactureRetourPerimeRepository;
import com.google.common.base.Preconditions;
import java.lang.Long;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing BaseTvaFactureRetourPerime.
 */
@Service
@Transactional
public class BaseTvaFactureRetourPerimeService {
  private final Logger log = LoggerFactory.getLogger(BaseTvaFactureRetourPerimeService.class);

  private final BaseTvaFactureRetourPerimeRepository basetvafactureretourperimeRepository;

  public BaseTvaFactureRetourPerimeService(BaseTvaFactureRetourPerimeRepository basetvafactureretourperimeRepository) {
    this.basetvafactureretourperimeRepository=basetvafactureretourperimeRepository;
  }

  /**
   * Save a basetvafactureretourperimeDTO.
   *
   * @param basetvafactureretourperimeDTO
   * @return the persisted entity
   */
  public BaseTvaFactureRetourPerimeDTO save(BaseTvaFactureRetourPerimeDTO basetvafactureretourperimeDTO) {
    log.debug("Request to save BaseTvaFactureRetourPerime: {}",basetvafactureretourperimeDTO);
    BaseTvaFactureRetourPerime basetvafactureretourperime = BaseTvaFactureRetourPerimeFactory.baseTvaFactureRetourPerimeDTOToBaseTvaFactureRetourPerime(basetvafactureretourperimeDTO);
    basetvafactureretourperime = basetvafactureretourperimeRepository.save(basetvafactureretourperime);
    BaseTvaFactureRetourPerimeDTO resultDTO = BaseTvaFactureRetourPerimeFactory.baseTvaFactureRetourPerimeToBaseTvaFactureRetourPerimeDTO(basetvafactureretourperime);
    return resultDTO;
  }

  /**
   * Update a basetvafactureretourperimeDTO.
   *
   * @param basetvafactureretourperimeDTO
   * @return the updated entity
   */
  public BaseTvaFactureRetourPerimeDTO update(BaseTvaFactureRetourPerimeDTO basetvafactureretourperimeDTO) {
    log.debug("Request to update BaseTvaFactureRetourPerime: {}",basetvafactureretourperimeDTO);
    BaseTvaFactureRetourPerime inBase= basetvafactureretourperimeRepository.findOne(basetvafactureretourperimeDTO.getCode());
    Preconditions.checkArgument(inBase != null, "basetvafactureretourperime.NotFound");
    BaseTvaFactureRetourPerime basetvafactureretourperime = BaseTvaFactureRetourPerimeFactory.baseTvaFactureRetourPerimeDTOToBaseTvaFactureRetourPerime(basetvafactureretourperimeDTO);
    basetvafactureretourperime = basetvafactureretourperimeRepository.save(basetvafactureretourperime);
    BaseTvaFactureRetourPerimeDTO resultDTO = BaseTvaFactureRetourPerimeFactory.baseTvaFactureRetourPerimeToBaseTvaFactureRetourPerimeDTO(basetvafactureretourperime);
    return resultDTO;
  }

  /**
   * Get one basetvafactureretourperimeDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public BaseTvaFactureRetourPerimeDTO findOne(Long id) {
    log.debug("Request to get BaseTvaFactureRetourPerime: {}",id);
    BaseTvaFactureRetourPerime basetvafactureretourperime= basetvafactureretourperimeRepository.findOne(id);
    BaseTvaFactureRetourPerimeDTO dto = BaseTvaFactureRetourPerimeFactory.baseTvaFactureRetourPerimeToBaseTvaFactureRetourPerimeDTO(basetvafactureretourperime);
    return dto;
  }

  /**
   * Get one basetvafactureretourperime by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public BaseTvaFactureRetourPerime findBaseTvaFactureRetourPerime(Long id) {
    log.debug("Request to get BaseTvaFactureRetourPerime: {}",id);
    BaseTvaFactureRetourPerime basetvafactureretourperime= basetvafactureretourperimeRepository.findOne(id);
    return basetvafactureretourperime;
  }

  /**
   * Get all the basetvafactureretourperimes.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public Collection<BaseTvaFactureRetourPerimeDTO> findAll() {
    log.debug("Request to get All BaseTvaFactureRetourPerimes");
    Collection<BaseTvaFactureRetourPerime> result= basetvafactureretourperimeRepository.findAll();
    return BaseTvaFactureRetourPerimeFactory.basetvafactureretourperimeToBaseTvaFactureRetourPerimeDTOs(result);
  }

  /**
   * Delete basetvafactureretourperime by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete BaseTvaFactureRetourPerime: {}",id);
    basetvafactureretourperimeRepository.delete(id);
  }
}

