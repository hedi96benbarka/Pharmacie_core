package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.FactureDirecteCostCenter;
import com.csys.pharmacie.achat.domain.FactureDirecteCostCenterPK; 
import com.csys.pharmacie.achat.domain.QFactureDirecteCostCenter;
import com.csys.pharmacie.achat.dto.FactureDirecteCostCenterDTO;
import com.csys.pharmacie.achat.factory.FactureDirecteCostCenterFactory;
import com.csys.pharmacie.achat.repository.FactureDirecteCostCenterRepository;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing FactureDirecteCostCenter.
 */
@Service
@Transactional
public class FactureDirecteCostCenterService {

    private final Logger log = LoggerFactory.getLogger(FactureDirecteCostCenterService.class);

    private final FactureDirecteCostCenterRepository facturedirectecostcenterRepository;

    public FactureDirecteCostCenterService(FactureDirecteCostCenterRepository facturedirectecostcenterRepository) {
        this.facturedirectecostcenterRepository = facturedirectecostcenterRepository;
    }

    /**
     * Save a facturedirectecostcenterDTO.
     *
     * @param facturedirectecostcenterDTO
     * @return the persisted entity
     */
    public FactureDirecteCostCenterDTO save(FactureDirecteCostCenterDTO facturedirectecostcenterDTO) {
        log.debug("Request to save FactureDirecteCostCenter: {}", facturedirectecostcenterDTO);
        FactureDirecteCostCenter facturedirectecostcenter = FactureDirecteCostCenterFactory.facturedirectecostcenterDTOToFactureDirecteCostCenter(facturedirectecostcenterDTO);
        facturedirectecostcenter = facturedirectecostcenterRepository.save(facturedirectecostcenter);
        FactureDirecteCostCenterDTO resultDTO = FactureDirecteCostCenterFactory.facturedirectecostcenterToFactureDirecteCostCenterDTO(facturedirectecostcenter);
        return resultDTO;
    }

//    /**
//     * Update a facturedirectecostcenterDTO.
//     *
//     * @param facturedirectecostcenterDTO
//     * @return the updated entity
//     */
//    public FactureDirecteCostCenterDTO update(FactureDirecteCostCenterDTO facturedirectecostcenterDTO) {
//        log.debug("Request to update FactureDirecteCostCenter: {}", facturedirectecostcenterDTO);
//        FactureDirecteCostCenter inBase = facturedirectecostcenterRepository.findOne(facturedirectecostcenterDTO.getFactureDirectCostCenterPK());
//        Preconditions.checkArgument(inBase != null, "facturedirectecostcenter.NotFound");
//        FactureDirecteCostCenter facturedirectecostcenter = FactureDirecteCostCenterFactory.facturedirectecostcenterDTOToFactureDirecteCostCenter(facturedirectecostcenterDTO);
//        facturedirectecostcenter = facturedirectecostcenterRepository.save(facturedirectecostcenter);
//        FactureDirecteCostCenterDTO resultDTO = FactureDirecteCostCenterFactory.facturedirectecostcenterToFactureDirecteCostCenterDTO(facturedirectecostcenter);
//        return resultDTO;
//    }
    /**
     * Get one facturedirectecostcenterDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public FactureDirecteCostCenterDTO findOne(FactureDirecteCostCenterPK id) {
        log.debug("Request to get FactureDirecteCostCenter: {}", id);
        FactureDirecteCostCenter facturedirectecostcenter = facturedirectecostcenterRepository.findOne(id);
        FactureDirecteCostCenterDTO dto = FactureDirecteCostCenterFactory.facturedirectecostcenterToFactureDirecteCostCenterDTO(facturedirectecostcenter);
        return dto;
    }

    /**
     * Get one facturedirectecostcenter by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public FactureDirecteCostCenter findFactureDirecteCostCenter(FactureDirecteCostCenterPK id) {
        log.debug("Request to get FactureDirecteCostCenter: {}", id);
        FactureDirecteCostCenter facturedirectecostcenter = facturedirectecostcenterRepository.findOne(id);
        return facturedirectecostcenter;
    }

    /**
     * Get all the facturedirectecostcenters.
     *
     * @param costCenter
     * @param factureDirecte
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public Collection<FactureDirecteCostCenterDTO> findAll(Integer costCenter, String factureDirecte) {
        log.debug("Request to get All FactureDirecteCostCenters");
        QFactureDirecteCostCenter factureDirecteCostCenter = QFactureDirecteCostCenter.factureDirecteCostCenter;
        WhereClauseBuilder builder = new WhereClauseBuilder()
                .optionalAnd(costCenter, () -> factureDirecteCostCenter.pk().codeCostCenter.eq(costCenter))
                .optionalAnd(factureDirecte, () -> factureDirecteCostCenter.pk().numeroFactureDirecte.eq(factureDirecte));
        Set<FactureDirecteCostCenter> result = new HashSet((Collection) facturedirectecostcenterRepository.findAll(builder));
        return FactureDirecteCostCenterFactory.facturedirectecostcenterToFactureDirecteCostCenterDTOs(result);
    }

    /**
     * Delete facturedirectecostcenter by id.
     *
     * @param id the id of the entity
     */
    public void delete(FactureDirecteCostCenterPK id) {
        log.debug("Request to delete FactureDirecteCostCenter: {}", id);
        facturedirectecostcenterRepository.delete(id);
    }
}
