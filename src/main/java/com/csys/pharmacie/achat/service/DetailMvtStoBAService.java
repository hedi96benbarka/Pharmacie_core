package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.DetailMvtStoBA;
import com.csys.pharmacie.achat.domain.DetailMvtStoBAPK;
import com.csys.pharmacie.achat.dto.DetailMvtStoBADTO;
import com.csys.pharmacie.achat.factory.DetailMvtStoBAFactory;
import com.csys.pharmacie.achat.repository.DetailMvtStoBARepository;
import com.google.common.base.Preconditions;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing DetailMvtStoBA.
 */
@Service
@Transactional
public class DetailMvtStoBAService {
  private final Logger log = LoggerFactory.getLogger(DetailMvtStoBAService.class);

  private final DetailMvtStoBARepository detailmvtstobaRepository;

  public DetailMvtStoBAService(DetailMvtStoBARepository detailmvtstobaRepository) {
    this.detailmvtstobaRepository=detailmvtstobaRepository;
  }

  /**
   * Save a detailmvtstobaDTO.
   *
   * @param detailmvtstobaDTO
   * @return the persisted entity
   */
  public DetailMvtStoBADTO save(DetailMvtStoBADTO detailmvtstobaDTO) {
    log.debug("Request to save DetailMvtStoBA: {}",detailmvtstobaDTO);
    DetailMvtStoBA detailmvtstoba = DetailMvtStoBAFactory.detailmvtstobaDTOToDetailMvtStoBA(detailmvtstobaDTO);
    detailmvtstoba = detailmvtstobaRepository.save(detailmvtstoba);
    DetailMvtStoBADTO resultDTO = DetailMvtStoBAFactory.detailmvtstobaToDetailMvtStoBADTO(detailmvtstoba);
    return resultDTO;
  }

  /**
   * Update a detailmvtstobaDTO.
   *
   * @param detailmvtstobaDTO
   * @return the updated entity
   */
  public DetailMvtStoBADTO update(DetailMvtStoBADTO detailmvtstobaDTO) {
    log.debug("Request to update DetailMvtStoBA: {}",detailmvtstobaDTO);
    DetailMvtStoBA inBase= detailmvtstobaRepository.findOne(detailmvtstobaDTO.getPk());
    Preconditions.checkArgument(inBase != null, "detailmvtstoba.NotFound");
    DetailMvtStoBA detailmvtstoba = DetailMvtStoBAFactory.detailmvtstobaDTOToDetailMvtStoBA(detailmvtstobaDTO);
    detailmvtstoba = detailmvtstobaRepository.save(detailmvtstoba);
    DetailMvtStoBADTO resultDTO = DetailMvtStoBAFactory.detailmvtstobaToDetailMvtStoBADTO(detailmvtstoba);
    return resultDTO;
  }

  /**
   * Get one detailmvtstobaDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public DetailMvtStoBADTO findOne(DetailMvtStoBAPK id) {
    log.debug("Request to get DetailMvtStoBA: {}",id);
    DetailMvtStoBA detailmvtstoba= detailmvtstobaRepository.findOne(id);
    DetailMvtStoBADTO dto = DetailMvtStoBAFactory.detailmvtstobaToDetailMvtStoBADTO(detailmvtstoba);
    return dto;
  }

  /**
   * Get one detailmvtstoba by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public DetailMvtStoBA findDetailMvtStoBA(DetailMvtStoBAPK id) {
    log.debug("Request to get DetailMvtStoBA: {}",id);
    DetailMvtStoBA detailmvtstoba= detailmvtstobaRepository.findOne(id);
    return detailmvtstoba;
  }

  /**
   * Get all the detailmvtstobas.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public Collection<DetailMvtStoBADTO> findAll() {
    log.debug("Request to get All DetailMvtStoBAs");
    Collection<DetailMvtStoBA> result= detailmvtstobaRepository.findAll();
    return DetailMvtStoBAFactory.detailmvtstobaToDetailMvtStoBADTOs(result);
  }

  /**
   * Delete detailmvtstoba by id.
   *
   * @param id the id of the entity
   */
  public void delete(DetailMvtStoBAPK id) {
    log.debug("Request to delete DetailMvtStoBA: {}",id);
    detailmvtstobaRepository.delete(id);
  }
}

