package com.csys.pharmacie.achat.service;


import com.csys.pharmacie.achat.domain.DetailMvtStoRetourPerime;
import com.csys.pharmacie.achat.dto.DetailMvtStoRetourPerimeDTO;
import com.csys.pharmacie.achat.factory.DetailMvtStoRetourPerimeFactory;
import com.csys.pharmacie.achat.repository.DetailMvtStoRetourPerimeRepository;
import com.google.common.base.Preconditions;
import java.lang.Integer;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Detail_MvtStoRetourPerime.
 */
@Service
@Transactional
public class DetailMvtStoRetourPerimeService {
  private final Logger log = LoggerFactory.getLogger(DetailMvtStoRetourPerimeService.class);

  private final DetailMvtStoRetourPerimeRepository detailMvtstoretourperimeRepository;

  public DetailMvtStoRetourPerimeService(DetailMvtStoRetourPerimeRepository detail_mvtstoretourperimeRepository) {
    this.detailMvtstoretourperimeRepository=detail_mvtstoretourperimeRepository;
  }

  /**
   * Save a detail_mvtstoretourperimeDTO.
   *
   * @param detail_mvtstoretourperimeDTO
   * @return the persisted entity
   */
  public DetailMvtStoRetourPerimeDTO save(DetailMvtStoRetourPerimeDTO detail_mvtstoretourperimeDTO) {
    log.debug("Request to save Detail_MvtStoRetourPerime: {}",detail_mvtstoretourperimeDTO);
    DetailMvtStoRetourPerime detail_mvtstoretourperime = DetailMvtStoRetourPerimeFactory.detailMvtstoretourperimeDTOToDetailMvtStoRetourPerime(detail_mvtstoretourperimeDTO);
    detail_mvtstoretourperime = detailMvtstoretourperimeRepository.save(detail_mvtstoretourperime);
    DetailMvtStoRetourPerimeDTO resultDTO = DetailMvtStoRetourPerimeFactory.detailMvtstoretourperimeToDetailMvtStoRetourPerimeDTO(detail_mvtstoretourperime);
    return resultDTO;
  }

  /**
   * Update a detail_mvtstoretourperimeDTO.
   *
   * @param detail_mvtstoretourperimeDTO
   * @return the updated entity
   */
  public DetailMvtStoRetourPerimeDTO update(DetailMvtStoRetourPerimeDTO detail_mvtstoretourperimeDTO) {
    log.debug("Request to update Detail_MvtStoRetourPerime: {}",detail_mvtstoretourperimeDTO);
    DetailMvtStoRetourPerime inBase= detailMvtstoretourperimeRepository.findOne(detail_mvtstoretourperimeDTO.getCode());
    Preconditions.checkArgument(inBase != null, "detail_mvtstoretourperime.NotFound");
    DetailMvtStoRetourPerime detail_mvtstoretourperime = DetailMvtStoRetourPerimeFactory.detailMvtstoretourperimeDTOToDetailMvtStoRetourPerime(detail_mvtstoretourperimeDTO);
    detail_mvtstoretourperime = detailMvtstoretourperimeRepository.save(detail_mvtstoretourperime);
    DetailMvtStoRetourPerimeDTO resultDTO = DetailMvtStoRetourPerimeFactory.detailMvtstoretourperimeToDetailMvtStoRetourPerimeDTO(detail_mvtstoretourperime);
    return resultDTO;
  }

  /**
   * Get one detail_mvtstoretourperimeDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public DetailMvtStoRetourPerimeDTO findOne(Integer id) {
    log.debug("Request to get Detail_MvtStoRetourPerime: {}",id);
    DetailMvtStoRetourPerime detail_mvtstoretourperime= detailMvtstoretourperimeRepository.findOne(id);
    DetailMvtStoRetourPerimeDTO dto = DetailMvtStoRetourPerimeFactory.detailMvtstoretourperimeToDetailMvtStoRetourPerimeDTO(detail_mvtstoretourperime);
    return dto;
  }

  /**
   * Get one detail_mvtstoretourperime by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public DetailMvtStoRetourPerime findDetail_MvtStoRetourPerime(Integer id) {
    log.debug("Request to get Detail_MvtStoRetourPerime: {}",id);
    DetailMvtStoRetourPerime detail_mvtstoretourperime= detailMvtstoretourperimeRepository.findOne(id);
    return detail_mvtstoretourperime;
  }

  /**
   * Get all the detail_mvtstoretourperimes.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public Collection<DetailMvtStoRetourPerimeDTO> findAll() {
    log.debug("Request to get All Detail_MvtStoRetourPerimes");
    Collection<DetailMvtStoRetourPerime> result= detailMvtstoretourperimeRepository.findAll();
    return DetailMvtStoRetourPerimeFactory.detail_mvtstoretourperimeToDetail_MvtStoRetourPerimeDTOs(result);
  }

  /**
   * Delete detail_mvtstoretourperime by id.
   *
   * @param id the id of the entity
   */
  public void delete(Integer id) {
    log.debug("Request to delete Detail_MvtStoRetourPerime: {}",id);
    detailMvtstoretourperimeRepository.delete(id);
  }
}

