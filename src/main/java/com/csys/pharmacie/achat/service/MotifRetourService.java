package com.csys.pharmacie.achat.service;


import com.csys.pharmacie.achat.domain.MotifRetour;
import com.csys.pharmacie.achat.dto.MotifRetourDTO;
import com.csys.pharmacie.achat.factory.MotifRetourFactory;
import com.csys.pharmacie.achat.repository.MotifRetourRepository;
import com.google.common.base.Preconditions;
import java.lang.Integer;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MotifRetour.
 */
@Service
@Transactional
public class MotifRetourService {
  private final Logger log = LoggerFactory.getLogger(MotifRetourService.class);

  private final MotifRetourRepository motifretourRepository;

  public MotifRetourService(MotifRetourRepository motifretourRepository) {
    this.motifretourRepository=motifretourRepository;
  }

  /**
   * Save a motifretourDTO.
   *
   * @param motifretourDTO
   * @return the persisted entity
   */
  public MotifRetourDTO save(MotifRetourDTO motifretourDTO) {
    log.debug("Request to save MotifRetour: {}",motifretourDTO);
    MotifRetour motifretour = MotifRetourFactory.motifretourDTOToMotifRetour(motifretourDTO);
    motifretour = motifretourRepository.save(motifretour);
    MotifRetourDTO resultDTO = MotifRetourFactory.motifretourToMotifRetourDTO(motifretour);
    return resultDTO;
  }

  /**
   * Update a motifretourDTO.
   *
   * @param motifretourDTO
   * @return the updated entity
   */
  public MotifRetourDTO update(MotifRetourDTO motifretourDTO) {
    log.debug("Request to update MotifRetour: {}",motifretourDTO);
    MotifRetour inBase= motifretourRepository.findOne(motifretourDTO.getId());
    Preconditions.checkArgument(inBase != null, "motifretour.NotFound");
    MotifRetour motifretour = MotifRetourFactory.motifretourDTOToMotifRetour(motifretourDTO);
    motifretour = motifretourRepository.save(motifretour);
    MotifRetourDTO resultDTO = MotifRetourFactory.motifretourToMotifRetourDTO(motifretour);
    return resultDTO;
  }

  /**
   * Get one motifretourDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public MotifRetourDTO findOne(Integer id) {
    log.debug("Request to get MotifRetour: {}",id);
    MotifRetour motifretour= motifretourRepository.findOne(id);
    MotifRetourDTO dto = MotifRetourFactory.motifretourToMotifRetourDTO(motifretour);
    return dto;
  }

  /**
   * Get one motifretour by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public MotifRetour findMotifRetour(Integer id) {
    log.debug("Request to get MotifRetour: {}",id);
    MotifRetour motifretour= motifretourRepository.findOne(id);
    return motifretour;
  }

  /**
   * Get all the motifretours.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public Collection<MotifRetourDTO> findAll() {
    log.debug("Request to get All MotifRetours");
    Collection<MotifRetour> result= motifretourRepository.findAll();
    return MotifRetourFactory.motifretourToMotifRetourDTOs(result);
  }

  /**
   * Delete motifretour by id.
   *
   * @param id the id of the entity
   */
  public void delete(Integer id) {
    log.debug("Request to delete MotifRetour: {}",id);
    motifretourRepository.delete(id);
  }
}

