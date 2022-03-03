package com.csys.pharmacie.prelevement.service;

import com.csys.pharmacie.prelevement.domain.Motif;
import com.csys.pharmacie.prelevement.dto.MotifDTO;
import com.csys.pharmacie.prelevement.factory.MotifFactory;
import com.csys.pharmacie.prelevement.repository.MotifRepository;
import com.google.common.base.Preconditions;
import java.lang.Integer;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Motif.
 */
@Service
@Transactional
public class MotifService {
  private final Logger log = LoggerFactory.getLogger(MotifService.class);

  private final MotifRepository motifRepository;

  public MotifService(MotifRepository motifRepository) {
    this.motifRepository=motifRepository;
  }

  /**
   * Save a motifDTO.
   *
   * @param motifDTO
   * @return the persisted entity
   */
  public MotifDTO save(MotifDTO motifDTO) {
    log.debug("Request to save Motif: {}",motifDTO);
    Motif motif = MotifFactory.motifDTOToMotif(motifDTO);
    motif = motifRepository.save(motif);
    MotifDTO resultDTO = MotifFactory.motifToMotifDTO(motif);
    return resultDTO;
  }

  /**
   * Update a motifDTO.
   *
   * @param motifDTO
   * @return the updated entity
   */
  public MotifDTO update(MotifDTO motifDTO) {
    log.debug("Request to update Motif: {}",motifDTO);
    Motif inBase= motifRepository.findOne(motifDTO.getId());
    Preconditions.checkArgument(inBase != null, "motif.NotFound");
    Motif motif = MotifFactory.motifDTOToMotif(motifDTO);
    motif = motifRepository.save(motif);
    MotifDTO resultDTO = MotifFactory.motifToMotifDTO(motif);
    return resultDTO;
  }

  /**
   * Get one motifDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public MotifDTO findOne(Integer id) {
    log.debug("Request to get Motif: {}",id);
    Motif motif= motifRepository.findOne(id);
    MotifDTO dto = MotifFactory.motifToMotifDTO(motif);
    return dto;
  }

  /**
   * Get one motif by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public Motif findMotif(Integer id) {
    log.debug("Request to get Motif: {}",id);
    Motif motif= motifRepository.findOne(id);
    return motif;
  }

  /**
   * Get all the motifs.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public Collection<MotifDTO> findAll() {
    log.debug("Request to get All Motifs");
    Collection<Motif> result= motifRepository.findAll();
    return MotifFactory.motifToMotifDTOs(result);
  }

  /**
   * Delete motif by id.
   *
   * @param id the id of the entity
   */
  public void delete(Integer id) {
    log.debug("Request to delete Motif: {}",id);
    motifRepository.delete(id);
  }
}

