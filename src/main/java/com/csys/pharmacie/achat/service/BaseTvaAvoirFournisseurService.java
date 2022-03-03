package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.BaseTvaAvoirFournisseur;
import com.csys.pharmacie.achat.dto.BaseTvaAvoirFournisseurDTO;
import com.csys.pharmacie.achat.factory.BaseTvaAvoirFournisseurFactory;
import com.csys.pharmacie.achat.repository.BaseTvaAvoirFournisseurRepository;
import com.google.common.base.Preconditions;
import java.lang.Integer;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing BaseTvaAvoirFournisseur.
 */
@Service
@Transactional
public class BaseTvaAvoirFournisseurService {
  private final Logger log = LoggerFactory.getLogger(BaseTvaAvoirFournisseurService.class);

  private final BaseTvaAvoirFournisseurRepository basetvaavoirfournisseurRepository;

  public BaseTvaAvoirFournisseurService(BaseTvaAvoirFournisseurRepository basetvaavoirfournisseurRepository) {
    this.basetvaavoirfournisseurRepository=basetvaavoirfournisseurRepository;
  }

  /**
   * Save a basetvaavoirfournisseurDTO.
   *
   * @param basetvaavoirfournisseurDTO
   * @return the persisted entity
   */
  public BaseTvaAvoirFournisseurDTO save(BaseTvaAvoirFournisseurDTO basetvaavoirfournisseurDTO) {
    log.debug("Request to save BaseTvaAvoirFournisseur: {}",basetvaavoirfournisseurDTO);
    BaseTvaAvoirFournisseur basetvaavoirfournisseur = BaseTvaAvoirFournisseurFactory.basetvaavoirfournisseurDTOToBaseTvaAvoirFournisseur(basetvaavoirfournisseurDTO);
    basetvaavoirfournisseur = basetvaavoirfournisseurRepository.save(basetvaavoirfournisseur);
    BaseTvaAvoirFournisseurDTO resultDTO = BaseTvaAvoirFournisseurFactory.basetvaavoirfournisseurToBaseTvaAvoirFournisseurDTO(basetvaavoirfournisseur);
    return resultDTO;
  }

  /**
   * Update a basetvaavoirfournisseurDTO.
   *
   * @param basetvaavoirfournisseurDTO
   * @return the updated entity
   */
  public BaseTvaAvoirFournisseurDTO update(BaseTvaAvoirFournisseurDTO basetvaavoirfournisseurDTO) {
    log.debug("Request to update BaseTvaAvoirFournisseur: {}",basetvaavoirfournisseurDTO);
    BaseTvaAvoirFournisseur inBase= basetvaavoirfournisseurRepository.findOne(basetvaavoirfournisseurDTO.getCode());
    Preconditions.checkArgument(inBase != null, "basetvaavoirfournisseur.NotFound");
    BaseTvaAvoirFournisseur basetvaavoirfournisseur = BaseTvaAvoirFournisseurFactory.basetvaavoirfournisseurDTOToBaseTvaAvoirFournisseur(basetvaavoirfournisseurDTO);
    basetvaavoirfournisseur = basetvaavoirfournisseurRepository.save(basetvaavoirfournisseur);
    BaseTvaAvoirFournisseurDTO resultDTO = BaseTvaAvoirFournisseurFactory.basetvaavoirfournisseurToBaseTvaAvoirFournisseurDTO(basetvaavoirfournisseur);
    return resultDTO;
  }

  /**
   * Get one basetvaavoirfournisseurDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public BaseTvaAvoirFournisseurDTO findOne(Integer id) {
    log.debug("Request to get BaseTvaAvoirFournisseur: {}",id);
    BaseTvaAvoirFournisseur basetvaavoirfournisseur= basetvaavoirfournisseurRepository.findOne(id);
    BaseTvaAvoirFournisseurDTO dto = BaseTvaAvoirFournisseurFactory.basetvaavoirfournisseurToBaseTvaAvoirFournisseurDTO(basetvaavoirfournisseur);
    return dto;
  }

  /**
   * Get one basetvaavoirfournisseur by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public BaseTvaAvoirFournisseur findBaseTvaAvoirFournisseur(Integer id) {
    log.debug("Request to get BaseTvaAvoirFournisseur: {}",id);
    BaseTvaAvoirFournisseur basetvaavoirfournisseur= basetvaavoirfournisseurRepository.findOne(id);
    return basetvaavoirfournisseur;
  }

  /**
   * Get all the basetvaavoirfournisseurs.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public List<BaseTvaAvoirFournisseurDTO> findAll() {
    log.debug("Request to get All BaseTvaAvoirFournisseurs");
    List<BaseTvaAvoirFournisseur> result= basetvaavoirfournisseurRepository.findAll();
    return BaseTvaAvoirFournisseurFactory.basetvaavoirfournisseurToBaseTvaAvoirFournisseurDTOs(result);
  }

  /**
   * Delete basetvaavoirfournisseur by id.
   *
   * @param id the id of the entity
   */
  public void delete(Integer id) {
    log.debug("Request to delete BaseTvaAvoirFournisseur: {}",id);
    basetvaavoirfournisseurRepository.delete(id);
  }
}

