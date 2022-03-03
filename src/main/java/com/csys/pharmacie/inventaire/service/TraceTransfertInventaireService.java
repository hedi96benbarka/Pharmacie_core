package com.csys.pharmacie.inventaire.service;

import com.csys.pharmacie.inventaire.domain.TraceTransfertInventaire;
import com.csys.pharmacie.inventaire.dto.TraceTransfertInventaireDTO;
import com.csys.pharmacie.inventaire.factory.TraceTransfertInventaireFactory;
import com.csys.pharmacie.inventaire.repository.TraceTransfertInventaireRepository;
import com.google.common.base.Preconditions;
import java.lang.Long;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing TraceTransfertInventaire.
 */
@Service
@Transactional
public class TraceTransfertInventaireService {

    private final Logger log = LoggerFactory.getLogger(TraceTransfertInventaireService.class);

    private final TraceTransfertInventaireRepository tracetransfertinventaireRepository;

    public TraceTransfertInventaireService(TraceTransfertInventaireRepository tracetransfertinventaireRepository) {
        this.tracetransfertinventaireRepository = tracetransfertinventaireRepository;
    }

    /**
     * Save a tracetransfertinventaireDTO.
     *
     * @param tracetransfertinventaireDTO
     * @return the persisted entity
     */
    public TraceTransfertInventaireDTO save(TraceTransfertInventaireDTO tracetransfertinventaireDTO) {
        log.debug("Request to save TraceTransfertInventaire: {}", tracetransfertinventaireDTO);
        TraceTransfertInventaire tracetransfertinventaire = TraceTransfertInventaireFactory.tracetransfertinventaireDTOToTraceTransfertInventaire(tracetransfertinventaireDTO);
        tracetransfertinventaire = tracetransfertinventaireRepository.save(tracetransfertinventaire);
        TraceTransfertInventaireDTO resultDTO = TraceTransfertInventaireFactory.tracetransfertinventaireToTraceTransfertInventaireDTO(tracetransfertinventaire);
        return resultDTO;
    }


    public void saveAll(List<TraceTransfertInventaire> tracetransfertinventaires) {
        log.debug("Request to save TraceTransfertInventaire: {}", tracetransfertinventaires);
        tracetransfertinventaireRepository.save(tracetransfertinventaires);

    }
@Transactional(readOnly = true )
    public List<TraceTransfertInventaire>  findByCodeInventaire(Integer codeInventaire) {
        log.debug("Request to get findByCodeInventaire: {}", codeInventaire);
       return  tracetransfertinventaireRepository.findByInventaire_Code(codeInventaire);
     
    }
    /**
     * Update a tracetransfertinventaireDTO.
     *
     * @param tracetransfertinventaireDTO
     * @return the updated entity
     */

    /**
     * Get one tracetransfertinventaireDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public TraceTransfertInventaireDTO findOne(Long id) {
        log.debug("Request to get TraceTransfertInventaire: {}", id);
        TraceTransfertInventaire tracetransfertinventaire = tracetransfertinventaireRepository.findOne(id);
        TraceTransfertInventaireDTO dto = TraceTransfertInventaireFactory.tracetransfertinventaireToTraceTransfertInventaireDTO(tracetransfertinventaire);
        return dto;
    }

    /**
     * Get one tracetransfertinventaire by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public TraceTransfertInventaire findTraceTransfertInventaire(Long id) {
        log.debug("Request to get TraceTransfertInventaire: {}", id);
        TraceTransfertInventaire tracetransfertinventaire = tracetransfertinventaireRepository.findOne(id);
        return tracetransfertinventaire;
    }

    /**
     * Get all the tracetransfertinventaires.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<TraceTransfertInventaireDTO> findAll() {
        log.debug("Request to get All TraceTransfertInventaires");
        List<TraceTransfertInventaire> result = tracetransfertinventaireRepository.findAll();
        return TraceTransfertInventaireFactory.traceTransfertInventaireToTraceTransfertInventaireDTOs(result);
    }

    /**
     * Delete tracetransfertinventaire by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TraceTransfertInventaire: {}", id);
        tracetransfertinventaireRepository.delete(id);
    }
}
