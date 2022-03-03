package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.achat.domain.ReceivingCommande;
import com.csys.pharmacie.achat.domain.ReceivingCommandePK;
import com.csys.pharmacie.achat.dto.CommandeAchatDTO;
import com.csys.pharmacie.achat.dto.ReceivingCommandeDTO;
import com.csys.pharmacie.achat.factory.ReceivingCommandeFactory;
import com.csys.pharmacie.achat.repository.ReceivingCommandeRepository;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing ReceivingCommande.
 */
@Service
@Transactional
public class ReceivingCommandeService {

    private final Logger log = LoggerFactory.getLogger(ReceivingCommandeService.class);

    private final ReceivingCommandeRepository receivingcommandeRepository;

    private final DemandeServiceClient demandeServiceClient;

    public ReceivingCommandeService(ReceivingCommandeRepository receivingcommandeRepository, DemandeServiceClient demandeServiceClient) {
        this.receivingcommandeRepository = receivingcommandeRepository;
        this.demandeServiceClient = demandeServiceClient;
    }

    /**
     * Save a receivingcommandeDTO.
     *
     * @param receivingcommandeDTO
     * @return the persisted entity
     */
    public ReceivingCommandeDTO save(ReceivingCommandeDTO receivingcommandeDTO) {
        log.debug("Request to save ReceivingCommande: {}", receivingcommandeDTO);
        ReceivingCommande receivingcommande = ReceivingCommandeFactory.receivingcommandeDTOToReceivingCommande(receivingcommandeDTO);
        receivingcommande = receivingcommandeRepository.save(receivingcommande);
        ReceivingCommandeDTO resultDTO = ReceivingCommandeFactory.receivingcommandeToReceivingCommandeDTO(receivingcommande);
        return resultDTO;
    }

    /**
     * Update a receivingcommandeDTO.
     *
     * @param receivingcommandeDTO
     * @return the updated entity
     */
    public ReceivingCommandeDTO update(ReceivingCommandeDTO receivingcommandeDTO) {
        log.debug("Request to update ReceivingCommande: {}", receivingcommandeDTO);
        ReceivingCommandePK receivingCommandePK = new ReceivingCommandePK();
        receivingCommandePK.setCommandeParamAchat(receivingcommandeDTO.getCommandeAchat().getCode());
        receivingCommandePK.setReciveing(receivingcommandeDTO.getReceivingID());
        ReceivingCommande inBase = receivingcommandeRepository.findOne(receivingCommandePK);
        Preconditions.checkArgument(inBase != null, "receivingcommande.NotFound");
        ReceivingCommande receivingcommande = ReceivingCommandeFactory.receivingcommandeDTOToReceivingCommande(receivingcommandeDTO);
        receivingcommande = receivingcommandeRepository.save(receivingcommande);
        ReceivingCommandeDTO resultDTO = ReceivingCommandeFactory.receivingcommandeToReceivingCommandeDTO(receivingcommande);
        return resultDTO;
    }

    /**
     * Get one receivingcommandeDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public ReceivingCommandeDTO findOne(ReceivingCommandePK id) {
        log.debug("Request to get ReceivingCommande: {}", id);
        ReceivingCommande receivingcommande = receivingcommandeRepository.findOne(id);
        ReceivingCommandeDTO dto = ReceivingCommandeFactory.receivingcommandeToReceivingCommandeDTO(receivingcommande);
        return dto;
    }

    /**
     * Get one receivingcommandeDTO by id.
     *
     * @param codeReceiving
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public List<ReceivingCommandeDTO> findByReceivingCommandePK_Reciveing(Integer codeReceiving) {
        log.debug("Request to get ReceivingCommande: {}", codeReceiving);
        List<ReceivingCommande> receivingcommandeList = receivingcommandeRepository.findByReceivingCommandePK_Reciveing(codeReceiving);
        List<ReceivingCommandeDTO> receivingCommandeDTOs = new ArrayList<>();
        receivingcommandeList.forEach((receivingcommande) -> {
            ReceivingCommandeDTO receivingCommandeDTO = ReceivingCommandeFactory.receivingcommandeToReceivingCommandeDTO(receivingcommande);
            String language = LocaleContextHolder.getLocale().getLanguage();
            CommandeAchatDTO commandeAchatDTO = demandeServiceClient.findCommandeAchat(receivingcommande.getReceivingCommandePK().getCommandeParamAchat(), language);
            receivingCommandeDTO.setCommandeAchat(commandeAchatDTO);
            receivingCommandeDTOs.add(receivingCommandeDTO);
        });
        return receivingCommandeDTOs;
    }
    
    
    

    /**
     * Get one receivingcommande by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public ReceivingCommande findReceivingCommande(ReceivingCommandePK id) {
        log.debug("Request to get ReceivingCommande: {}", id);
        ReceivingCommande receivingcommande = receivingcommandeRepository.findOne(id);
        return receivingcommande;
    }

    /**
     * Get all the receivingcommandes.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public Collection<ReceivingCommandeDTO> findAll() {
        log.debug("Request to get All ReceivingCommandes");
        Collection<ReceivingCommande> result = receivingcommandeRepository.findAll();
        return ReceivingCommandeFactory.receivingcommandeToReceivingCommandeDTOs(result);
    }

    /**
     * Delete receivingcommande by id.
     *
     * @param id the id of the entity
     */
    public void delete(ReceivingCommandePK id) {
        log.debug("Request to delete ReceivingCommande: {}", id);
        receivingcommandeRepository.delete(id);
    }

    public List<ReceivingCommandeDTO> findByExample(Example<ReceivingCommande> exple) {
          Collection<ReceivingCommande> result = receivingcommandeRepository.findAll(exple);
           return (List<ReceivingCommandeDTO>) ReceivingCommandeFactory.receivingcommandeToReceivingCommandeDTOs(result);
    }
}
