package com.csys.pharmacie.prelevement.service;

import com.csys.pharmacie.prelevement.domain.TraceDetailRetourPr;
import com.csys.pharmacie.prelevement.domain.TraceDetailRetourPrPK;
import com.csys.pharmacie.prelevement.dto.TraceDetailRetourPrDTO;
import com.csys.pharmacie.prelevement.factory.TraceDetailRetourPrFactory;
import com.csys.pharmacie.prelevement.repository.TraceDetailRetourPrRepository;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing TraceDetailRetourPr.
 */
@Service
@Transactional
public class TraceDetailRetourPrService {
    private final Logger log = LoggerFactory.getLogger(TraceDetailRetourPrService.class);

    private final TraceDetailRetourPrRepository tracedetailretourprRepository;

    public TraceDetailRetourPrService(TraceDetailRetourPrRepository tracedetailretourprRepository) {
        this.tracedetailretourprRepository = tracedetailretourprRepository;
    }

    /**
     * Save a tracedetailretourprDTO.
     *
     * @param tracedetailretourprDTO
     * @return the persisted entity
     */
    public TraceDetailRetourPrDTO save(TraceDetailRetourPrDTO tracedetailretourprDTO) {
    log.debug("Request to save TraceDetailRetourPr: {}",tracedetailretourprDTO);
        TraceDetailRetourPr tracedetailretourpr = TraceDetailRetourPrFactory.tracedetailretourprDTOToTraceDetailRetourPr(tracedetailretourprDTO);
        tracedetailretourpr = tracedetailretourprRepository.save(tracedetailretourpr);
        TraceDetailRetourPrDTO resultDTO = TraceDetailRetourPrFactory.tracedetailretourprToTraceDetailRetourPrDTO(tracedetailretourpr);
        return resultDTO;
    }

    /**
     * Update a tracedetailretourprDTO.
     *
     * @param tracedetailretourprDTO
     * @return the updated entity
     */
    public TraceDetailRetourPrDTO update(TraceDetailRetourPrDTO tracedetailretourprDTO) {
        log.debug("Request to update TraceDetailRetourPr: {}", tracedetailretourprDTO);
        TraceDetailRetourPr inBase = tracedetailretourprRepository.findOne(tracedetailretourprDTO.getTraceDetailRetourPrPK());
        Preconditions.checkArgument(inBase != null, "tracedetailretourpr.NotFound");
        TraceDetailRetourPr tracedetailretourpr = TraceDetailRetourPrFactory.tracedetailretourprDTOToTraceDetailRetourPr(tracedetailretourprDTO);
        tracedetailretourpr = tracedetailretourprRepository.save(tracedetailretourpr);
        TraceDetailRetourPrDTO resultDTO = TraceDetailRetourPrFactory.tracedetailretourprToTraceDetailRetourPrDTO(tracedetailretourpr);
        return resultDTO;
    }

    /**
     * Get one tracedetailretourprDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public TraceDetailRetourPrDTO findOne(TraceDetailRetourPrPK id) {
        log.debug("Request to get TraceDetailRetourPr: {}", id);
        TraceDetailRetourPr tracedetailretourpr = tracedetailretourprRepository.findOne(id);
        TraceDetailRetourPrDTO dto = TraceDetailRetourPrFactory.tracedetailretourprToTraceDetailRetourPrDTO(tracedetailretourpr);
        return dto;
    }

    /**
     * Get one tracedetailretourpr by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public TraceDetailRetourPr findTraceDetailRetourPr(TraceDetailRetourPrPK id) {
        log.debug("Request to get TraceDetailRetourPr: {}", id);
        TraceDetailRetourPr tracedetailretourpr = tracedetailretourprRepository.findOne(id);
        return tracedetailretourpr;
    }

    /**
     * Get all the tracedetailretourprs.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public Collection<TraceDetailRetourPrDTO> findAll() {
        log.debug("Request to get All TraceDetailRetourPrs");
        Collection<TraceDetailRetourPr> result = tracedetailretourprRepository.findAll();
        return TraceDetailRetourPrFactory.tracedetailretourprToTraceDetailRetourPrDTOs(result);
    }

    /**
     * Delete tracedetailretourpr by id.
     *
     * @param id the id of the entity
     */
    public void delete(TraceDetailRetourPrPK id) {
        log.debug("Request to delete TraceDetailRetourPr: {}", id);
        tracedetailretourprRepository.delete(id);
    }

    public List<TraceDetailRetourPr> findByCodeDetailMvtstoprIn(Set<Integer> codes) { 
        return tracedetailretourprRepository.findByTraceDetailRetourPrPK_CodeDetailMvtstoprIn(codes);

    }
}
