package com.csys.pharmacie.prelevement.service;

import static com.csys.pharmacie.helper.PrelevmentOrderState.NOT_PRELEVE;
import com.csys.pharmacie.prelevement.domain.EtatDPR;
import com.csys.pharmacie.prelevement.dto.EtatDPRDTO;
import com.csys.pharmacie.prelevement.factory.EtatDPRFactory;
import com.csys.pharmacie.prelevement.repository.EtatDPRRepository;
import com.google.common.base.Preconditions;
import java.lang.Integer;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing EtatDPR.
 */
@Service
@Transactional
public class EtatDPRService {
  private final Logger log = LoggerFactory.getLogger(EtatDPRService.class);

  private final EtatDPRRepository etatdprRepository;

  public EtatDPRService(EtatDPRRepository etatdprRepository) {
    this.etatdprRepository=etatdprRepository;
  }

  /**
   * Save a etatdprDTO.
   *
   * @param etatdprDTO
   * @return the persisted entity
   */
  public EtatDPRDTO save(EtatDPRDTO etatdprDTO) {
    log.debug("Request to save EtatDPR: {}",etatdprDTO);
    EtatDPR etatdpr = EtatDPRFactory.etatdprDTOToEtatDPR(etatdprDTO);
    etatdpr = etatdprRepository.save(etatdpr);
    EtatDPRDTO resultDTO = EtatDPRFactory.etatdprToEtatDPRDTO(etatdpr);
    return resultDTO;
  }

  
   public List<EtatDPR> save(List<EtatDPR> purchaseOrdersStates) {
        log.debug("Request to save EtatReceptionDPR: {}", purchaseOrdersStates);
        return etatdprRepository.save(purchaseOrdersStates);
    }
  
  /**
   * Update a etatdprDTO.
   *
   * @param etatdprDTO
   * @return the updated entity
   */
  public EtatDPRDTO update(EtatDPRDTO etatdprDTO) {
    log.debug("Request to update EtatDPR: {}",etatdprDTO);
    EtatDPR inBase= etatdprRepository.findOne(etatdprDTO.getCodedpr());
    Preconditions.checkArgument(inBase != null, "etatdpr.NotFound");
    EtatDPR etatdpr = EtatDPRFactory.etatdprDTOToEtatDPR(etatdprDTO);
    etatdpr = etatdprRepository.save(etatdpr);
    EtatDPRDTO resultDTO = EtatDPRFactory.etatdprToEtatDPRDTO(etatdpr);
    return resultDTO;
  }

  /**
   * Get one etatdprDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public EtatDPRDTO findOne(Integer id) {
    log.debug("Request to get EtatDPR: {}",id);
    EtatDPR etatdpr= etatdprRepository.findOne(id);
    EtatDPRDTO dto = EtatDPRFactory.etatdprToEtatDPRDTO(etatdpr);
    return dto;
  }

  /**
   * Get one etatdpr by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public EtatDPR findEtatDPR(Integer id) {
    log.debug("Request to get EtatDPR: {}",id);
    EtatDPR etatdpr= etatdprRepository.findOne(id);
    return etatdpr;
  }

  /**
   * Get all the etatdprs.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public List<EtatDPRDTO> findAll() {
    log.debug("Request to get All EtatDPRs");
    List<EtatDPR> result= etatdprRepository.findAll();
    return EtatDPRFactory.etatdprToEtatDPRDTOs(result);
  }

  /**
   * Delete etatdpr by id.
   *
   * @param id the id of the entity
   */
  public void delete(Integer id) {
    log.debug("Request to delete EtatDPR: {}",id);
    etatdprRepository.delete(id);
  }
  
  @Transactional( readOnly = true)
  public List<EtatDPRDTO>FindByDPRIn (List<Integer> codesDPR){
  log.debug("Request to get All EratDPR");
  List<EtatDPR> result = etatdprRepository.findByCodedprIn(codesDPR);
  codesDPR.forEach(codeDPR-> { 
  Optional<EtatDPR>etatPrelevment = result.stream().filter(elt -> elt.getCodedpr().equals(codeDPR)).findFirst();
  if(!etatPrelevment.isPresent()){
  result.add(new EtatDPR(codeDPR,NOT_PRELEVE));
  }
  });
  
  return EtatDPRFactory.etatdprToEtatDPRDTOs(result);
  }
  
   public void deleteByCodedprIn(List<Integer> id) {
        log.debug("Request to delete EtatDPR: {}", id);
        etatdprRepository.deleteByCodedprIn(id);
    }
  
}

