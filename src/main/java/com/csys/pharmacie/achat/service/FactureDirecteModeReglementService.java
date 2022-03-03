package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.FactureDirecte;
import com.csys.pharmacie.achat.domain.FactureDirecteModeReglement;
import com.csys.pharmacie.achat.domain.FactureDirecteModeReglementPK;
import com.csys.pharmacie.achat.dto.FactureDirecteModeReglementDTO;
import com.csys.pharmacie.achat.dto.ListeFactureDirecteModeReglementDTOWrapper;
import com.csys.pharmacie.achat.factory.FactureDirecteModeReglementFactory;
import com.csys.pharmacie.achat.repository.FactureDirecteModeReglementRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing FactureDirecteModeReglement.
 */
@Service
@Transactional
public class FactureDirecteModeReglementService {
    
    private final Logger log = LoggerFactory.getLogger(FactureDirecteModeReglementService.class);
    
    private final FactureDirecteModeReglementRepository factureDirecteModeReglementRepository;
    private final FactureDirecteService factureDirecteService;

    public FactureDirecteModeReglementService(FactureDirecteModeReglementRepository factureDirecteModeReglementRepository, FactureDirecteService factureDirecteService) {
        this.factureDirecteModeReglementRepository = factureDirecteModeReglementRepository;
        this.factureDirecteService = factureDirecteService;
    }
    
   

    /**
     * Save a facturedirectemodereglementDTO.
     *
     * @param factureDirecteModeReglementDTO
     * @return the persisted entity
     */
    public FactureDirecteModeReglementDTO save(FactureDirecteModeReglementDTO factureDirecteModeReglementDTO) {
        log.debug("Request to save FactureDirecteModeReglement: {}", factureDirecteModeReglementDTO);
        
        FactureDirecte factureDirecte = factureDirecteService.findFactureDirecte(factureDirecteModeReglementDTO.getNumBon());
                com.csys.util.Preconditions.checkFound(factureDirecte != null, "factureDirecte.NotFound");
                
        FactureDirecteModeReglement factureDirecteModeReglement = FactureDirecteModeReglementFactory.factureDirecteModeReglementDTOToFactureDirecteModeReglement(factureDirecteModeReglementDTO, factureDirecte);
        factureDirecteModeReglement = factureDirecteModeReglementRepository.save(factureDirecteModeReglement);
        FactureDirecteModeReglementDTO resultDTO = FactureDirecteModeReglementFactory.factureDirecteModeReglementToFactureDirecteModeReglementDTO(factureDirecteModeReglement);
        return resultDTO;
    }
    
    public ListeFactureDirecteModeReglementDTOWrapper save(ListeFactureDirecteModeReglementDTOWrapper listeFactureDirecteModeReglementDTOWrapper) {
        
        Collection<FactureDirecteModeReglementDTO> listeFactureDirecteModeReglementDTOs = listeFactureDirecteModeReglementDTOWrapper.getModeReglementList();
        List<FactureDirecteModeReglementDTO> factureDirecteModeReglementDTOs = new ArrayList<>();
        listeFactureDirecteModeReglementDTOs.forEach(factureDirecteModeReglementDTO -> {
            FactureDirecteModeReglementDTO dto = this.save(factureDirecteModeReglementDTO);
            factureDirecteModeReglementDTOs.add(dto);
        });
        ListeFactureDirecteModeReglementDTOWrapper result = new ListeFactureDirecteModeReglementDTOWrapper();
        result.setModeReglementList(factureDirecteModeReglementDTOs);
        return result;
    }


    /**
     * Get one facturedirectemodereglementDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(readOnly = true)
    public FactureDirecteModeReglementDTO findOne(FactureDirecteModeReglementPK id) {
        log.debug("Request to get FactureDirecteModeReglement: {}", id);
        FactureDirecteModeReglement facturedirectemodereglement = factureDirecteModeReglementRepository.findOne(id);
        FactureDirecteModeReglementDTO dto = FactureDirecteModeReglementFactory.factureDirecteModeReglementToFactureDirecteModeReglementDTO(facturedirectemodereglement);
        return dto;
    }

    /**
     * Get one facturedirectemodereglement by id.
     *findFactureDirecteModeReglementPK
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FactureDirecteModeReglement findFactureDirecteModeReglement(FactureDirecteModeReglementPK id) {
        log.debug("Request to get FactureDirecteModeReglement: {}", id);
        FactureDirecteModeReglement facturedirectemodereglement = factureDirecteModeReglementRepository.findOne(id);
        return facturedirectemodereglement;
    }

    /**
     * Get all the facturedirectemodereglements.
     *
     * @return the the list of entities
     */
    @Transactional(readOnly = true)
    public Collection<FactureDirecteModeReglementDTO> findAll() {
        log.debug("Request to get All FactureDirecteModeReglements");
        Collection<FactureDirecteModeReglement> result = factureDirecteModeReglementRepository.findAll();
        return FactureDirecteModeReglementFactory.facturedirectemodereglementsToFactureDirecteModeReglementDTOs(result);
    }

    /**
     * Delete facturedirectemodereglement by id.
     *
     * @param id the id of the entity
     */
    public void delete(FactureDirecteModeReglementPK id) {
        log.debug("Request to delete FactureDirecteModeReglement: {}", id);
        factureDirecteModeReglementRepository.delete(id);
    }
    
    @Transactional(readOnly = true)
    public Collection<FactureDirecteModeReglementDTO> findFactureDirecteModeReglementByNumBonsIn(List<Integer> numBons) {
        log.debug("Request to get All CommandeAchatModeReglements By numBons OF FD");
        Collection<FactureDirecteModeReglement> result = factureDirecteModeReglementRepository.findByFactureDirecteModeReglementPK_NumBonIn(numBons);
        return FactureDirecteModeReglementFactory.facturedirectemodereglementsToFactureDirecteModeReglementDTOs(result);
    }
}
