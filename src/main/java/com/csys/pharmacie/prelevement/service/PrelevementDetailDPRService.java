package com.csys.pharmacie.prelevement.service;

import com.csys.pharmacie.helper.PrelevmentOrderState;
import com.csys.pharmacie.prelevement.domain.PrelevementDetailDPR;
import com.csys.pharmacie.prelevement.domain.PrelevementDetailDPRPK;
import com.csys.pharmacie.prelevement.dto.PrelevementDetailDPRDTO;
import com.csys.pharmacie.prelevement.factory.PrelevementDetailDPRFactory;
import com.csys.pharmacie.prelevement.repository.PrelevementDetailDPRRepository;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing PrelevementDetailDPR.
 */
@Service
@Transactional
public class PrelevementDetailDPRService {

    private final Logger log = LoggerFactory.getLogger(PrelevementDetailDPRService.class);

    private final PrelevementDetailDPRRepository prelevementdetaildprRepository;

    public PrelevementDetailDPRService(PrelevementDetailDPRRepository prelevementdetaildprRepository) {
        this.prelevementdetaildprRepository = prelevementdetaildprRepository;
    }

    /**
     * Save a prelevementdetaildprDTO.
     *
     * @param prelevementdetaildprDTO
     * @return the persisted entity
     */
    public PrelevementDetailDPRDTO save(PrelevementDetailDPRDTO prelevementdetaildprDTO) {
        log.debug("Request to save PrelevementDetailDPR: {}", prelevementdetaildprDTO);
        PrelevementDetailDPR prelevementdetaildpr = PrelevementDetailDPRFactory.prelevementdetaildprDTOToPrelevementDetailDPR(prelevementdetaildprDTO);
        prelevementdetaildpr = prelevementdetaildprRepository.save(prelevementdetaildpr);
        PrelevementDetailDPRDTO resultDTO = PrelevementDetailDPRFactory.prelevementdetaildprToPrelevementDetailDPRDTO(prelevementdetaildpr);
        return resultDTO;
    }

    public List<PrelevementDetailDPR> save(List<PrelevementDetailDPR> prelevment) {
        log.debug("Request to save EtatReceptionCA: {}", prelevment);
        return prelevementdetaildprRepository.save(prelevment);
    }

    /**
     * Get one prelevementdetaildprDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public PrelevementDetailDPRDTO findOne(PrelevementDetailDPRPK id) {
        log.debug("Request to get PrelevementDetailDPR: {}", id);
        PrelevementDetailDPR prelevementdetaildpr = prelevementdetaildprRepository.findOne(id);
        PrelevementDetailDPRDTO dto = PrelevementDetailDPRFactory.prelevementdetaildprToPrelevementDetailDPRDTO(prelevementdetaildpr);
        return dto;
    }

    /**
     * Get one prelevementdetaildpr by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public PrelevementDetailDPR findPrelevementDetailDPR(PrelevementDetailDPRPK id) {
        log.debug("Request to get PrelevementDetailDPR: {}", id);
        PrelevementDetailDPR prelevementdetaildpr = prelevementdetaildprRepository.findOne(id);
        return prelevementdetaildpr;
    }

    /**
     * Get all the prelevementdetaildprs.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public Collection<PrelevementDetailDPRDTO> findAll() {
        log.debug("Request to get All PrelevementDetailDPRs");
        Collection<PrelevementDetailDPR> result = prelevementdetaildprRepository.findAll();
        return PrelevementDetailDPRFactory.prelevementdetaildprToPrelevementDetailDPRDTOs(result);
    }

    /**
     * Delete prelevementdetaildpr by id.
     *
     * @param id the id of the entity
     */
    public void delete(PrelevementDetailDPRPK id) {
        log.debug("Request to delete PrelevementDetailDPR: {}", id);
        prelevementdetaildprRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<PrelevementDetailDPR> findByCodesDPrIn(List<Integer> listCodepr) {
        log.debug("Request to find ReceptionDetailCAs by codes in");
        List<PrelevementDetailDPR> result = prelevementdetaildprRepository.findByCodeDPRIn(listCodepr);
        return result;
    }

    @Transactional(readOnly = true)
    public List<PrelevementDetailDPR> findByCodePrelevement(String numBon) {
        log.debug("Request to find ReceptionDetailCAs by codes in");
        List<PrelevementDetailDPR> result = prelevementdetaildprRepository.findByPk_CodePrelevment(numBon);
        return result;
    }

    @Transactional
    public void deleteList(List<PrelevementDetailDPR> prelevementDetaiDPRs) {
        log.debug("Request to find ReceptionDetailCAs by codes in");
        prelevementdetaildprRepository.deleteInBatch(prelevementDetaiDPRs);
    }

    @Transactional(
            readOnly = true)
    public List<PrelevementDetailDPRDTO> findPrelevmentDetailDPRByCodeDPR(Integer codeDPR) {
        log.debug("Request to get PrelevmentDetailDPR : {}", codeDPR);
        List<PrelevementDetailDPR> prelevmentDetail = prelevementdetaildprRepository.findByCodeDPR(codeDPR);
        Map<Integer, Integer> qtePreleveByArt = prelevmentDetail.stream().collect(Collectors.groupingBy(item -> item.getPk().getCodedetailDPR(), Collectors.summingInt(item -> item.getQuantite_prelevee().intValue())));
        List<PrelevementDetailDPRDTO> dto = new ArrayList();

        qtePreleveByArt.forEach((codDetail, qtepreleve) -> {
            dto.add(new PrelevementDetailDPRDTO(codDetail, BigDecimal.valueOf(qtepreleve), codeDPR));
        });

        return dto;
    }

}
