/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.TransfertCompanyBranch;
import com.csys.pharmacie.achat.dto.TransfertCompanyBranchDTO;
import com.csys.pharmacie.achat.factory.TransfertCompanyBranchFactory;
import com.csys.pharmacie.achat.repository.TransfertCompanyBranchRepository;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.parametrage.repository.ParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrateur
 */
@Service
@Transactional
public class TransfertCompanyService {

    protected final Logger log = LoggerFactory.getLogger(TransfertCompanyService.class);

    protected final TransfertCompanyBranchRepository transfertcompanybranchRepository;

//    protected final TopicPartitionOffsetService topicPartitionOffsetService;
//
//    protected final KafkaConsumerErrorService kafkaConsumerErrorService;
    protected final ParamService paramService;

//    protected final ParamAchatServiceClient paramAchatServiceClient;
//    protected final Environment env;
//    protected final ParamServiceClient paramServiceClient;
    public TransfertCompanyService(TransfertCompanyBranchRepository transfertcompanybranchRepository, ParamService paramService) {
        this.transfertcompanybranchRepository = transfertcompanybranchRepository;
        this.paramService = paramService;
    }

    public TransfertCompanyBranchDTO saveTransfertCompany(TransfertCompanyBranchDTO transfertCompanyBranchDTO) {
        log.debug("Request to save TransfertCompanyBranch: {}", transfertCompanyBranchDTO);
        TransfertCompanyBranch transfertCompanyBranch = TransfertCompanyBranchFactory.transfertCompanyBranchDTOToTransfertCompanyBranch(transfertCompanyBranchDTO);
        transfertCompanyBranch.setReplicated(Boolean.FALSE);
        transfertCompanyBranch.setIntegrer(Boolean.FALSE);
        transfertCompanyBranch.setCodeIntegration("");
        transfertcompanybranchRepository.save(transfertCompanyBranch);
        paramService.updateCompteurPharmacie(transfertCompanyBranchDTO.getCategDepot(), TypeBonEnum.TCB);
//        TransfertCompanyBranchDTO resultDTO = TransfertCompanyBranchFactory.transfertCompanyBranchToTransfertCompanyBranchDTO(transfertCompanyBranch);

        return transfertCompanyBranchDTO;
    }

    public void saveTransfertCompany(TransfertCompanyBranch transfertCompanyBranch) {

        transfertcompanybranchRepository.save(transfertCompanyBranch);

    }
}
