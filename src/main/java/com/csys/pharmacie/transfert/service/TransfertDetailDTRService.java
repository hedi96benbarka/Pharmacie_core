package com.csys.pharmacie.transfert.service;

import com.csys.pharmacie.transfert.domain.TransfertDetailDTR;
import com.csys.pharmacie.transfert.domain.TransfertDetailDTRPK;
import com.csys.pharmacie.transfert.dto.TransfertDetailDTRDTO;
import com.csys.pharmacie.transfert.factory.TransfertDetailDTRFactory;
import com.csys.pharmacie.transfert.repository.TransfertDetailDTRRepository;
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
 * Service Implementation for managing TransfertDetailDTR.
 */
@Service
@Transactional
public class TransfertDetailDTRService {

    private final Logger log = LoggerFactory.getLogger(TransfertDetailDTRService.class);

    private final TransfertDetailDTRRepository transfertdetaildtrRepository;

    public TransfertDetailDTRService(TransfertDetailDTRRepository transfertdetaildtrRepository) {
        this.transfertdetaildtrRepository = transfertdetaildtrRepository;
    }

    /**
     * Save a transfertdetaildtrDTO.
     *
     * @param transfertdetaildtrDTO
     * @return the persisted entity
     */
    public List<TransfertDetailDTR> save(List<TransfertDetailDTR> transfertdetaildtr) {
        log.debug("Request to save TransfertDetailDTR: {}", transfertdetaildtr);
//    TransfertDetailDTR transfertdetaildtr = TransfertDetailDTRFactory.transfertdetaildtrDTOToTransfertDetailDTR(transfertdetaildtrDTO);
//    transfertdetaildtr = transfertdetaildtrRepository.save(transfertdetaildtr);
//    TransfertDetailDTRDTO resultDTO = TransfertDetailDTRFactory.transfertdetaildtrToTransfertDetailDTRDTO(transfertdetaildtr);
        return transfertdetaildtrRepository.save(transfertdetaildtr);
    }

    /**
     * Get one transfertdetaildtrDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public TransfertDetailDTRDTO findOne(TransfertDetailDTRPK id) {
        log.debug("Request to get TransfertDetailDTR: {}", id);
        TransfertDetailDTR transfertdetaildtr = transfertdetaildtrRepository.findOne(id);
        TransfertDetailDTRDTO dto = TransfertDetailDTRFactory.transfertdetaildtrToTransfertDetailDTRDTO(transfertdetaildtr);
        return dto;
    }

    /**
     * Get one transfertdetaildtr by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public TransfertDetailDTR findTransfertDetailDTR(TransfertDetailDTRPK id) {
        log.debug("Request to get TransfertDetailDTR: {}", id);
        TransfertDetailDTR transfertdetaildtr = transfertdetaildtrRepository.findOne(id);
        return transfertdetaildtr;
    }

    @Transactional(
            readOnly = true
    )
    public List<TransfertDetailDTR> findByCodesDtrIn(List<Integer> listCodetr) {
        log.debug("Request to find ReceptionDetailCAs by codes in");
        List<TransfertDetailDTR> result = transfertdetaildtrRepository.findByCodeDTRIn(listCodetr);
        return result;
    }

    /**
     * Get all the transfertdetaildtrs.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public Collection<TransfertDetailDTRDTO> findAll() {
        log.debug("Request to get All TransfertDetailDTRs");
        Collection<TransfertDetailDTR> result = transfertdetaildtrRepository.findAll();
        return TransfertDetailDTRFactory.transfertdetaildtrToTransfertDetailDTRDTOs(result);
    }

    /**
     * Delete transfertdetaildtr by id.
     *
     * @param id the id of the entity
     */
    public void delete(TransfertDetailDTRPK id) {
        log.debug("Request to delete TransfertDetailDTR: {}", id);
        transfertdetaildtrRepository.delete(id);
    }

    @Transactional(
            readOnly = true)
    public List<TransfertDetailDTRDTO> findTransferDetailDTRByCodeDTR(Integer codeDTR) {
        log.debug("Request to get TransfertDetailDTR: {}", codeDTR);
        List<TransfertDetailDTR> prelevmentDetail = transfertdetaildtrRepository.findByCodeDTR(codeDTR);
        Map<Integer, Integer> qteTransfertByArt = prelevmentDetail.stream().collect(Collectors.groupingBy(item -> item.getPk().getCodedetailDTR(), Collectors.summingInt(item -> item.getQuantiteTransferred().intValue())));
        List<TransfertDetailDTRDTO> dto = new ArrayList();

        qteTransfertByArt.forEach((codDetail, qteTransfert) -> {
            dto.add(new TransfertDetailDTRDTO(codDetail, BigDecimal.valueOf(qteTransfert), codeDTR));
        });

        return dto;
    }

    @Transactional(readOnly = true)
    public List<TransfertDetailDTRDTO> FindByDTRIn(List<Integer> codesDTR) {
        List<TransfertDetailDTRDTO> dto = new ArrayList();
        codesDTR.forEach(codeDTR -> {

            List<TransfertDetailDTR> prelevmentDetail = transfertdetaildtrRepository.findByCodeDTR(codeDTR);
            Map<Integer, Integer> qteTransfertByArt = prelevmentDetail.stream().collect(Collectors.groupingBy(item -> item.getPk().getCodedetailDTR(), Collectors.summingInt(item -> item.getQuantiteTransferred().intValue())));

            qteTransfertByArt.forEach((codDetail, qteTransfert) -> {
                dto.add(new TransfertDetailDTRDTO(codDetail, BigDecimal.valueOf(qteTransfert), codeDTR));
            });
        });
        return dto;
    }

}
