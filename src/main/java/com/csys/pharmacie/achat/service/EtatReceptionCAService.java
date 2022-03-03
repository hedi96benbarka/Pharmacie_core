package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.EtatReceptionCA;
import com.csys.pharmacie.achat.dto.EtatReceptionCADTO;
import com.csys.pharmacie.achat.factory.EtatReceptionCAFactory;
import com.csys.pharmacie.achat.repository.EtatReceptionCARepository;
import static com.csys.pharmacie.helper.PurchaseOrderReceptionState.NOT_RECEIVED;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing EtatReceptionCA.
 */
@Service
@Transactional
public class EtatReceptionCAService {

    private final Logger log = LoggerFactory.getLogger(EtatReceptionCAService.class);

    private final EtatReceptionCARepository etatreceptioncaRepository;

    public EtatReceptionCAService(EtatReceptionCARepository etatreceptioncaRepository) {
        this.etatreceptioncaRepository = etatreceptioncaRepository;
    }

    /**
     * Save a etatreceptioncaDTO.
     *
     * @param etatreceptioncaDTO
     * @return the persisted entity
     */
    public EtatReceptionCADTO save(EtatReceptionCADTO etatreceptioncaDTO) {
        log.debug("Request to save EtatReceptionCA: {}", etatreceptioncaDTO);
        EtatReceptionCA etatreceptionca = EtatReceptionCAFactory.etatreceptioncaDTOToEtatReceptionCA(etatreceptioncaDTO);
        etatreceptionca = etatreceptioncaRepository.save(etatreceptionca);
        EtatReceptionCADTO resultDTO = EtatReceptionCAFactory.etatreceptioncaToEtatReceptionCADTO(etatreceptionca);
        return resultDTO;
    }

    /**
     * Save a etatreceptioncaDTO.
     *
     * @param purchaseOrdersStates
     * @return the persisted entity
     */
    public List<EtatReceptionCA> save(List<EtatReceptionCA> purchaseOrdersStates) {
        log.debug("Request to save EtatReceptionCA: {}", purchaseOrdersStates);
        return etatreceptioncaRepository.save(purchaseOrdersStates);
    }

    /**
     * Save a etatreceptioncaDTO.
     *
     * @param purchaseOrdersStates
     * @return the persisted entity
     */
    public EtatReceptionCA save(EtatReceptionCA purchaseOrdersStates) {
        log.debug("Request to save EtatReceptionCA: {}", purchaseOrdersStates);
        return etatreceptioncaRepository.save(purchaseOrdersStates);
    }

    /**
     * Update a etatreceptioncaDTO.
     *
     * @param etatreceptioncaDTO
     * @return the updated entity
     */
    public EtatReceptionCADTO update(EtatReceptionCADTO etatreceptioncaDTO) {
        log.debug("Request to update EtatReceptionCA: {}", etatreceptioncaDTO);
        EtatReceptionCA inBase = etatreceptioncaRepository.findOne(etatreceptioncaDTO.getCommandeAchat());
        Preconditions.checkArgument(inBase != null, "etatreceptionca.NotFound");
        EtatReceptionCA etatreceptionca = EtatReceptionCAFactory.etatreceptioncaDTOToEtatReceptionCA(etatreceptioncaDTO);
        etatreceptionca = etatreceptioncaRepository.save(etatreceptionca);
        EtatReceptionCADTO resultDTO = EtatReceptionCAFactory.etatreceptioncaToEtatReceptionCADTO(etatreceptionca);
        return resultDTO;
    }

    /**
     * Get one etatreceptioncaDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public EtatReceptionCADTO findOne(Integer id) {
        log.debug("Request to get EtatReceptionCA: {}", id);
        EtatReceptionCA etatreceptionca = etatreceptioncaRepository.findOne(id);
        EtatReceptionCADTO dto = EtatReceptionCAFactory.etatreceptioncaToEtatReceptionCADTO(etatreceptionca);
        return dto;
    }

    /**
     * Get one etatreceptionca by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public EtatReceptionCA findEtatReceptionCA(Integer id) {
        log.debug("Request to get EtatReceptionCA: {}", id);
        EtatReceptionCA etatreceptionca = etatreceptioncaRepository.findOne(id);
        return etatreceptionca;
    }

    /**
     * Get all the etatreceptioncas.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public Collection<EtatReceptionCADTO> findAll() {
        log.debug("Request to get All EtatReceptionCAs");
        Collection<EtatReceptionCA> result = etatreceptioncaRepository.findAll();
        return EtatReceptionCAFactory.etatreceptioncaToEtatReceptionCADTOs(result);
    }

    /**
     * find etatreceptioncas by purchase orders codes.
     *
     * @param codesCA purchace orders codes
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<EtatReceptionCADTO> findByCommandeAchatIn(List<Integer> codesCA) {
        log.debug("Request to get All EtatReceptionCAs");
        List<EtatReceptionCA> result = etatreceptioncaRepository.findByCommandeAchatIn(codesCA);
        codesCA.forEach(codeCA -> {
            Optional<EtatReceptionCA> etatReception = result.stream().filter(elt -> elt.getCommandeAchat().equals(codeCA)).findFirst();
            if (!etatReception.isPresent()) {
                result.add(new EtatReceptionCA(codeCA, NOT_RECEIVED));
            }
        });
        return EtatReceptionCAFactory.etatreceptioncaToEtatReceptionCADTOs(result);
    }

    /**
     * Delete etatreceptionca by id.
     *
     * @param id the id of the entity
     */
    public void delete(Integer id) {
        log.debug("Request to delete EtatReceptionCA: {}", id);
        etatreceptioncaRepository.delete(id);
    }

    public void deleteByCommandeAchatIn(List<Integer> id) {
        log.debug("Request to delete EtatReceptionCA: {}", id);
        etatreceptioncaRepository.deleteByCommandeAchatIn(id);
    }
}
