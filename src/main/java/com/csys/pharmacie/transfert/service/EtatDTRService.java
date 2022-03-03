package com.csys.pharmacie.transfert.service;

import static com.csys.pharmacie.helper.TransferOrderState.NOT_TRANSFERRED;
import com.csys.pharmacie.transfert.domain.EtatDTR;
import com.csys.pharmacie.transfert.dto.EtatDTRDTO;
import com.csys.pharmacie.transfert.factory.EtatDTRFactory;
import com.csys.pharmacie.transfert.repository.EtatDTRRepository;
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
 * Service Implementation for managing EtatDTR.
 */
@Service
@Transactional
public class EtatDTRService {
  private final Logger log = LoggerFactory.getLogger(EtatDTRService.class);

  private final EtatDTRRepository etatdtrRepository;

  public EtatDTRService(EtatDTRRepository etatdtrRepository) {
    this.etatdtrRepository=etatdtrRepository;
  }

  /**
   * Save a etatdtrDTO.
   *
   * @param etatdtrDTO
   * @return the persisted entity
   */
  public EtatDTRDTO save(EtatDTRDTO etatdtrDTO) {
    log.debug("Request to save EtatDTR: {}",etatdtrDTO);
    EtatDTR etatdtr = EtatDTRFactory.etatdtrDTOToEtatDTR(etatdtrDTO);   
    etatdtr = etatdtrRepository.save(etatdtr);
    EtatDTRDTO resultDTO = EtatDTRFactory.etatdtrToEtatDTRDTO(etatdtr);
    return resultDTO;
  }
 public List<EtatDTR> save(List<EtatDTR> purchaseOrdersStates) {
        log.debug("Request to save EtatReceptionBT: {}", purchaseOrdersStates);
        return etatdtrRepository.save(purchaseOrdersStates);
    }
  
  /**
   * Update a etatdtrDTO.
   *
   * @param etatdtrDTO
   * @return the updated entity
   */
  public EtatDTRDTO update(EtatDTRDTO etatdtrDTO) {
    log.debug("Request to update EtatDTR: {}",etatdtrDTO);
    EtatDTR inBase= etatdtrRepository.findOne(etatdtrDTO.getCodedtr());
    Preconditions.checkArgument(inBase != null, "etatdtr.NotFound");
    EtatDTR etatdtr = EtatDTRFactory.etatdtrDTOToEtatDTR(etatdtrDTO);      
    etatdtr = etatdtrRepository.save(etatdtr);
    EtatDTRDTO resultDTO = EtatDTRFactory.etatdtrToEtatDTRDTO(etatdtr);
    return resultDTO;
  }

  /**
   * Get one etatdtrDTO by id.
   *
   * @param id the id of the entity
   * @return the entity DTO
   */
  @Transactional(
      readOnly = true
  )
  public EtatDTRDTO findOne(Integer id) {
    log.debug("Request to get EtatDTR: {}",id);
    EtatDTR etatdtr= etatdtrRepository.findOne(id);
    EtatDTRDTO dto = EtatDTRFactory.etatdtrToEtatDTRDTO(etatdtr);
    return dto;
  }

  /**
   * Get one etatdtr by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(
      readOnly = true
  )
  public EtatDTR findEtatDTR(Integer id) {
    log.debug("Request to get EtatDTR: {}",id);
    EtatDTR etatdtr= etatdtrRepository.findOne(id);
    return etatdtr;
  }

  /**
   * Get all the etatdtrs.
   *
   * @return the the list of entities
   */
  @Transactional(
      readOnly = true
  )
  public Collection<EtatDTRDTO> findAll() {
    log.debug("Request to get All EtatDTRs");
    Collection<EtatDTR> result= etatdtrRepository.findAll();
    return EtatDTRFactory.etatdtrToEtatDTRDTOs(result);
  }

  /**
   * Delete etatdtr by id.
   *
   * @param id the id of the entity
   */
  public void delete(Integer id) {
    log.debug("Request to delete EtatDTR: {}",id);
    etatdtrRepository.delete(id);
  }   
  
   @Transactional( readOnly = true)
  public List<EtatDTRDTO>FindByDTRIn (List<Integer> codesDTR){
  log.debug("Request to get All EtatDTR");
  List<EtatDTR> result = etatdtrRepository.findByCodedtrIn(codesDTR);
  codesDTR.forEach(codeDTR-> { 
  Optional<EtatDTR>etatTransfert = result.stream().filter(elt -> elt.getCodedtr().equals(codeDTR)).findFirst();
  if(!etatTransfert.isPresent()){
  result.add(new EtatDTR(codeDTR,NOT_TRANSFERRED));
  }
  });
  
    return EtatDTRFactory.etatdTrToEtatDTRDTOsLIST(result);  
  }
}

