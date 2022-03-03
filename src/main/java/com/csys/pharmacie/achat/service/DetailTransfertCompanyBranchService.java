package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.DetailTransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.DetailTransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.factory.DetailTransfertCompanyBranchFactory;
import com.csys.pharmacie.achat.repository.DetailTransfertCompanyBranchRepository;
import com.google.common.base.Preconditions;
import java.lang.Long;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DetailTransfertCompanyBranchService {
  private final Logger log = LoggerFactory.getLogger(DetailTransfertCompanyBranchService.class);

  private final DetailTransfertCompanyBranchRepository detailtransfertcompanybranchRepository;

  public DetailTransfertCompanyBranchService(DetailTransfertCompanyBranchRepository detailtransfertcompanybranchRepository) {
    this.detailtransfertcompanybranchRepository=detailtransfertcompanybranchRepository;
  }

  public DetailTransfertCompanyBranchDTO save(DetailTransfertCompanyBranchDTO detailtransfertcompanybranchDTO) {
    log.debug("Request to save DetailTransfertCompanyBranch: {}",detailtransfertcompanybranchDTO);
    DetailTransfertCompanyBranch detailtransfertcompanybranch = DetailTransfertCompanyBranchFactory.detailtransfertcompanybranchDTOToDetailTransfertCompanyBranch(detailtransfertcompanybranchDTO);
    detailtransfertcompanybranch = detailtransfertcompanybranchRepository.save(detailtransfertcompanybranch);
    DetailTransfertCompanyBranchDTO resultDTO = DetailTransfertCompanyBranchFactory.detailtransfertcompanybranchToDetailTransfertCompanyBranchDTO(detailtransfertcompanybranch);
    return resultDTO;
  }

  public DetailTransfertCompanyBranchDTO update(DetailTransfertCompanyBranchDTO detailtransfertcompanybranchDTO) {
    log.debug("Request to update DetailTransfertCompanyBranch: {}",detailtransfertcompanybranchDTO);
    DetailTransfertCompanyBranch inBase= detailtransfertcompanybranchRepository.findOne(detailtransfertcompanybranchDTO.getCode());
    Preconditions.checkArgument(inBase != null, "DetailTransfertCompanyBranch does not exist");
    DetailTransfertCompanyBranchDTO result= save(detailtransfertcompanybranchDTO);
    return result;
  }

  @Transactional(
      readOnly = true
  )
  public DetailTransfertCompanyBranchDTO findOne(Integer id) {
    log.debug("Request to get DetailTransfertCompanyBranch: {}",id);
    DetailTransfertCompanyBranch detailtransfertcompanybranch= detailtransfertcompanybranchRepository.findOne(id);
    Preconditions.checkArgument(detailtransfertcompanybranch != null, "DetailTransfertCompanyBranch does not exist");
    DetailTransfertCompanyBranchDTO dto = DetailTransfertCompanyBranchFactory.detailtransfertcompanybranchToDetailTransfertCompanyBranchDTO(detailtransfertcompanybranch);
    return dto;
  }

  @Transactional(
      readOnly = true
  )
  public DetailTransfertCompanyBranch findDetailTransfertCompanyBranch(Integer id) {
    log.debug("Request to get DetailTransfertCompanyBranch: {}",id);
    DetailTransfertCompanyBranch detailtransfertcompanybranch= detailtransfertcompanybranchRepository.findOne(id);
    Preconditions.checkArgument(detailtransfertcompanybranch != null, "DetailTransfertCompanyBranch does not exist");
    return detailtransfertcompanybranch;
  }

  @Transactional(
      readOnly = true
  )
  public Collection<DetailTransfertCompanyBranchDTO> findAll() {
    log.debug("Request to get All DetailTransfertCompanyBranchs");
    Collection<DetailTransfertCompanyBranch> result= detailtransfertcompanybranchRepository.findAll();
    return DetailTransfertCompanyBranchFactory.detailtransfertcompanybranchToDetailTransfertCompanyBranchDTOs(result);
  }

  public void delete(Integer id) {
    log.debug("Request to delete DetailTransfertCompanyBranch: {}",id);
    detailtransfertcompanybranchRepository.delete(id);
  }
}

