package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.DetailFactureDirecte;
import com.csys.pharmacie.achat.dto.DetailFactureDirecteDTO;
import com.csys.pharmacie.achat.factory.DetailFactureDirecteFactory;
import com.csys.pharmacie.achat.repository.DetailFactureDirecteRepository;
import com.google.common.base.Preconditions;
import java.lang.Integer;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing DetailFactureDirecte.
 */
@Service
@Transactional
public class DetailFactureDirecteService {

    private final Logger log = LoggerFactory.getLogger(DetailFactureDirecteService.class);

    private final DetailFactureDirecteRepository detailfacturedirecteRepository;

    public DetailFactureDirecteService(DetailFactureDirecteRepository detailfacturedirecteRepository) {
        this.detailfacturedirecteRepository = detailfacturedirecteRepository;
    }

    /**
     * Save a detailfacturedirecteDTO.
     *
     * @param detailfacturedirecteDTO
     * @return the persisted entity
     */
    public DetailFactureDirecteDTO save(DetailFactureDirecteDTO detailfacturedirecteDTO) {
        log.debug("Request to save DetailFactureDirecte: {}", detailfacturedirecteDTO);
        DetailFactureDirecte detailfacturedirecte = DetailFactureDirecteFactory.detailfacturedirecteDTOToDetailFactureDirecte(detailfacturedirecteDTO);
        detailfacturedirecte = detailfacturedirecteRepository.save(detailfacturedirecte);
        DetailFactureDirecteDTO resultDTO = DetailFactureDirecteFactory.detailfacturedirecteToDetailFactureDirecteDTO(detailfacturedirecte);
        return resultDTO;
    }

    /**
     * Update a detailfacturedirecteDTO.
     *
     * @param detailfacturedirecteDTO
     * @return the updated entity
     */
    public DetailFactureDirecteDTO update(DetailFactureDirecteDTO detailfacturedirecteDTO) {
        log.debug("Request to update DetailFactureDirecte: {}", detailfacturedirecteDTO);
        DetailFactureDirecte inBase = detailfacturedirecteRepository.findOne(detailfacturedirecteDTO.getCode());
        Preconditions.checkArgument(inBase != null, "detailfacturedirecte.NotFound");
        DetailFactureDirecte detailfacturedirecte = DetailFactureDirecteFactory.detailfacturedirecteDTOToDetailFactureDirecte(detailfacturedirecteDTO);
        detailfacturedirecte = detailfacturedirecteRepository.save(detailfacturedirecte);
        DetailFactureDirecteDTO resultDTO = DetailFactureDirecteFactory.detailfacturedirecteToDetailFactureDirecteDTO(detailfacturedirecte);
        return resultDTO;
    }

    /**
     * Get one detailfacturedirecteDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public DetailFactureDirecteDTO findOne(Integer id) {
        log.debug("Request to get DetailFactureDirecte: {}", id);
        DetailFactureDirecte detailfacturedirecte = detailfacturedirecteRepository.findOne(id);
        DetailFactureDirecteDTO dto = DetailFactureDirecteFactory.detailfacturedirecteToDetailFactureDirecteDTO(detailfacturedirecte);
        return dto;
    }

    /**
     * Get one detailfacturedirecte by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public DetailFactureDirecte findDetailFactureDirecte(Integer id) {
        log.debug("Request to get DetailFactureDirecte: {}", id);
        DetailFactureDirecte detailfacturedirecte = detailfacturedirecteRepository.findOne(id);
        return detailfacturedirecte;
    }

    /**
     * Get all the detailfacturedirectes.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public Collection<DetailFactureDirecteDTO> findAll() {
        log.debug("Request to get All DetailFactureDirectes");
        Collection<DetailFactureDirecte> result = detailfacturedirecteRepository.findAll();
        return DetailFactureDirecteFactory.detailfacturedirecteToDetailFactureDirecteDTOs(result);
    }

    public Collection<DetailFactureDirecte> findByNumBon(String numBon) {
        return detailfacturedirecteRepository.findByNumbon(numBon);
    }

    /**
     * Delete detailfacturedirecte by id.
     *
     * @param id the id of the entity
     */
    public void delete(Integer id) {
        log.debug("Request to delete DetailFactureDirecte: {}", id);
        detailfacturedirecteRepository.delete(id);
    }
    
    
    
    public void deleteList (List<Integer> codes)
    { log.debug("Request to delete liste detailFactureDirecte: {}", codes);
    detailfacturedirecteRepository.deleteByCodeIn(codes);
    }
}
