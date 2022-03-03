package com.csys.pharmacie.inventaire.service;

import com.csys.pharmacie.inventaire.domain.DetailInventaire;
import com.csys.pharmacie.inventaire.domain.DetailInventairePK;
import com.csys.pharmacie.inventaire.dto.DetailInventaireDTO;
import com.csys.pharmacie.inventaire.dto.DateInventaire;
import com.csys.pharmacie.inventaire.factory.DetailInventaireFactory;
import com.csys.pharmacie.inventaire.repository.DetailInventaireRepository;
import com.google.common.base.Preconditions;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing DetailInventaire.
 */
@Service
@Transactional
public class DetailInventaireService {
  private final Logger log = LoggerFactory.getLogger(DetailInventaireService.class);

  private final DetailInventaireRepository detailinventaireRepository;

  public DetailInventaireService(DetailInventaireRepository detailinventaireRepository) {
    this.detailinventaireRepository=detailinventaireRepository;
  }

  /**
   * Save a detailinventaireDTO.
   *
   * @param detailinventaireDTO
   * @return the persisted entity
   */
  public DetailInventaireDTO save(DetailInventaireDTO detailinventaireDTO) {
    log.debug("Request to save DetailInventaire: {}",detailinventaireDTO);
    DetailInventaire detailinventaire = DetailInventaireFactory.detailinventaireDTOToDetailInventaire(detailinventaireDTO);
    detailinventaire = detailinventaireRepository.save(detailinventaire);
    DetailInventaireDTO resultDTO = DetailInventaireFactory.detailinventaireToDetailInventaireDTO(detailinventaire);
    return resultDTO;
  }

  /**
   * Update a detailinventaireDTO.
   *
   * @param detailinventaireDTO
   * @return the updated entity
   */
  public DetailInventaireDTO update(DetailInventaireDTO detailinventaireDTO) {
    log.debug("Request to update DetailInventaire: {}",detailinventaireDTO);
    DetailInventaire inBase= detailinventaireRepository.findOne(detailinventaireDTO.getDetailInventairePK());
    Preconditions.checkArgument(inBase != null, "detailinventaire.NotFound");
    DetailInventaire detailinventaire = DetailInventaireFactory.detailinventaireDTOToDetailInventaire(detailinventaireDTO);
    detailinventaire = detailinventaireRepository.save(detailinventaire);
    DetailInventaireDTO resultDTO = DetailInventaireFactory.detailinventaireToDetailInventaireDTO(detailinventaire);
    return resultDTO;
  }

  /**
   * Get one detailinventaireDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public DetailInventaireDTO findOne(DetailInventairePK id) {
    log.debug("Request to get DetailInventaire: {}",id);
    DetailInventaire detailinventaire= detailinventaireRepository.findOne(id);
    DetailInventaireDTO dto = DetailInventaireFactory.detailinventaireToDetailInventaireDTO(detailinventaire);
    return dto;
  }

  /**
   * Get one detailinventaire by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public DetailInventaire findDetailInventaire(DetailInventairePK id) {
    log.debug("Request to get DetailInventaire: {}",id);
    DetailInventaire detailinventaire= detailinventaireRepository.findOne(id);
    return detailinventaire;
  }

  /**
   * Get all the detailinventaires.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public Collection<DetailInventaireDTO> findAll() {
    log.debug("Request to get All DetailInventaires");
    Collection<DetailInventaire> result= detailinventaireRepository.findAll();
    return DetailInventaireFactory.detailinventaireToDetailInventaireDTOs(result);
  }
  
      @Transactional(readOnly = true)
    public List<DateInventaire> findMaxDateInv(Integer coddep, Date fromDate) {
        if (coddep != null) {
            return detailinventaireRepository.findMaxHeureSystemByCoddepAndHeureSystemeGreaterThan(coddep, fromDate);
        } else {
            return detailinventaireRepository.findMaxHeureSystemByHeureSystemeGreaterThan(fromDate);
        }

    }

  /**
   * Delete detailinventaire by id.
   *
   * @param id the id of the entity
   */
  public void delete(DetailInventairePK id) {
    log.debug("Request to delete DetailInventaire: {}",id);
    detailinventaireRepository.delete(id);
  }
}

