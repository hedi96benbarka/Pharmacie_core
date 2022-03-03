package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCa;
import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCaPK;
import com.csys.pharmacie.achat.dto.ReceptionTemporaireDetailCaDTO;
import com.csys.pharmacie.achat.factory.ReceptionTemporaireDetailCaFactory;
import com.csys.pharmacie.achat.repository.ReceptionTemporaireDetailCaRepository;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReceptionTemporaireDetailCaService {
  private final Logger log = LoggerFactory.getLogger(ReceptionTemporaireDetailCaService.class);

  private final ReceptionTemporaireDetailCaRepository receptiontemporairedetailcaRepository;

  public ReceptionTemporaireDetailCaService(ReceptionTemporaireDetailCaRepository receptiontemporairedetailcaRepository) {
    this.receptiontemporairedetailcaRepository=receptiontemporairedetailcaRepository;
  }

  public ReceptionTemporaireDetailCaDTO save(ReceptionTemporaireDetailCaDTO receptiontemporairedetailcaDTO) {
    log.debug("Request to save ReceptionTemporaireDetailCa: {}",receptiontemporairedetailcaDTO);
    ReceptionTemporaireDetailCa receptiontemporairedetailca = ReceptionTemporaireDetailCaFactory.receptiontemporairedetailcaDTOToReceptionTemporaireDetailCa(receptiontemporairedetailcaDTO);
    receptiontemporairedetailca = receptiontemporairedetailcaRepository.save(receptiontemporairedetailca);
    ReceptionTemporaireDetailCaDTO resultDTO = ReceptionTemporaireDetailCaFactory.receptiontemporairedetailcaToReceptionTemporaireDetailCaDTO(receptiontemporairedetailca);
    return resultDTO;
  }

  public ReceptionTemporaireDetailCaDTO update(ReceptionTemporaireDetailCaDTO receptiontemporairedetailcaDTO) {
    log.debug("Request to update ReceptionTemporaireDetailCa: {}",receptiontemporairedetailcaDTO);
    ReceptionTemporaireDetailCa inBase= receptiontemporairedetailcaRepository.findOne(receptiontemporairedetailcaDTO.getReceptionTemporaireDetailCaPK());
    Preconditions.checkArgument(inBase != null, "ReceptionTemporaireDetailCa does not exist");
    ReceptionTemporaireDetailCaDTO result= save(receptiontemporairedetailcaDTO);
    return result;
  }

  @Transactional(
      readOnly = true
  )
  public ReceptionTemporaireDetailCaDTO findOne(ReceptionTemporaireDetailCaPK id) {
    log.debug("Request to get ReceptionTemporaireDetailCa: {}",id);
    ReceptionTemporaireDetailCa receptiontemporairedetailca= receptiontemporairedetailcaRepository.findOne(id);
    Preconditions.checkArgument(receptiontemporairedetailca != null, "ReceptionTemporaireDetailCa does not exist");
    ReceptionTemporaireDetailCaDTO dto = ReceptionTemporaireDetailCaFactory.receptiontemporairedetailcaToReceptionTemporaireDetailCaDTO(receptiontemporairedetailca);
    return dto;
  }

  @Transactional(
      readOnly = true
  )
  public ReceptionTemporaireDetailCa findReceptionTemporaireDetailCa(ReceptionTemporaireDetailCaPK id) {
    log.debug("Request to get ReceptionTemporaireDetailCa: {}",id);
    ReceptionTemporaireDetailCa receptiontemporairedetailca= receptiontemporairedetailcaRepository.findOne(id);
    Preconditions.checkArgument(receptiontemporairedetailca != null, "ReceptionTemporaireDetailCa does not exist");
    return receptiontemporairedetailca;
  }

  @Transactional(
      readOnly = true
  )
  public Collection<ReceptionTemporaireDetailCaDTO> findAll() {
    log.debug("Request to get All ReceptionTemporaireDetailCas");
    Collection<ReceptionTemporaireDetailCa> result= receptiontemporairedetailcaRepository.findAll();
    return ReceptionTemporaireDetailCaFactory.receptiontemporairedetailcaToReceptionTemporaireDetailCaDTOs(result);
  }

  public void delete(ReceptionTemporaireDetailCaPK id) {
    log.debug("Request to delete ReceptionTemporaireDetailCa: {}",id);
    receptiontemporairedetailcaRepository.delete(id);
  }
  
  
    @Transactional( readOnly = true )
    public List<ReceptionTemporaireDetailCa> findByCodesCAInAndNotValidated(List<Integer> listCodeCAs) {
        log.debug("Request to find ReceptionTemporaireDetailCa by codes in");
        List<ReceptionTemporaireDetailCa> result = receptiontemporairedetailcaRepository.findByReceptionTemporaireDetailCaPK_CommandeAchatInAndReceptionTemporaire_FactureBAIsNull(listCodeCAs);
        return result;
    }
    @Transactional( readOnly = true )
    public List<ReceptionTemporaireDetailCa> findByCodesCAAndNotValidated(Integer codeCommande) {
        log.debug("Request to find ReceptionTemporaireDetailCa by codes in");
        return receptiontemporairedetailcaRepository.findByReceptionTemporaireDetailCaPK_CommandeAchatAndReceptionTemporaire_FactureBAIsNull(codeCommande);
      
    }
}

