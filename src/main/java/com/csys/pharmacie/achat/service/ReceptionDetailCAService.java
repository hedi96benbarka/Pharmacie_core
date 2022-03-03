package com.csys.pharmacie.achat.service;

//import com.csys.pharmacie.achat.domain.QReceptionDetailCA;
import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.ReceptionDetailCA;
import com.csys.pharmacie.achat.domain.ReceptionDetailCAPK;
import com.csys.pharmacie.achat.domain.ReceptionTemporaireDetailCa;
import com.csys.pharmacie.achat.dto.ReceptionDetailCADTO;
import com.csys.pharmacie.achat.factory.ReceptionDetailCAFactory;
import com.csys.pharmacie.achat.repository.ReceptionDetailCARepository;
import com.google.common.base.Preconditions;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing ReceptionDetailCA.
 */
@Service
@Transactional
public class ReceptionDetailCAService {

    private final Logger log = LoggerFactory.getLogger(ReceptionDetailCAService.class);

    private final ReceptionDetailCARepository receptiondetailcaRepository;
    private final ReceptionTemporaireDetailCaService receptionTemporaireDetailCaService;

    public ReceptionDetailCAService(ReceptionDetailCARepository receptiondetailcaRepository, ReceptionTemporaireDetailCaService receptionTemporaireDetailCaService) {
        this.receptiondetailcaRepository = receptiondetailcaRepository;
        this.receptionTemporaireDetailCaService = receptionTemporaireDetailCaService;
    }

    /**
     * Save a receptiondetailcaDTO.
     *
     * @param receptiondetailcaDTO
     * @return the persisted entity converted to DTO
     */
    public ReceptionDetailCADTO save(ReceptionDetailCADTO receptiondetailcaDTO) {
        log.debug("Request to save ReceptionDetailCA: {}", receptiondetailcaDTO);
        ReceptionDetailCA receptiondetailca = ReceptionDetailCAFactory.receptiondetailcaDTOToReceptionDetailCA(receptiondetailcaDTO);
        receptiondetailca = receptiondetailcaRepository.save(receptiondetailca);
        ReceptionDetailCADTO resultDTO = ReceptionDetailCAFactory.receptiondetailcaToReceptionDetailCADTO(receptiondetailca);
        return resultDTO;
    }

    /**
     * Save a receptiondetailca.
     *
     * @param receptiondetailca
     * @return the persisted entity
     */
    public ReceptionDetailCA save(ReceptionDetailCA receptiondetailca) {
        log.debug("Request to save ReceptionDetailCA: {}", receptiondetailca);
        receptiondetailca = receptiondetailcaRepository.save(receptiondetailca);
        return receptiondetailca;
    }

    /**
     * Save a receptiondetailca.
     *
     * @param receptiondetailca
     * @return the persisted entity
     */
    public Iterable<ReceptionDetailCA> save(Iterable receptiondetailca) {
        log.debug("Request to save ReceptionDetailCA: {}", receptiondetailca);
        receptiondetailca = receptiondetailcaRepository.save(receptiondetailca);
        return receptiondetailca;
    }

    /**
     * Update a receptiondetailcaDTO.
     *
     * @param receptiondetailcaDTO
     * @return the updated entity
     */
    public ReceptionDetailCADTO update(ReceptionDetailCADTO receptiondetailcaDTO) {
        log.debug("Request to update ReceptionDetailCA: {}", receptiondetailcaDTO);
        ReceptionDetailCA inBase = receptiondetailcaRepository.findOne(new ReceptionDetailCAPK(receptiondetailcaDTO.getReception(), receptiondetailcaDTO.getPurchaseOrder(), receptiondetailcaDTO.getArticle()));
        Preconditions.checkArgument(inBase != null, "receptiondetailca.NotFound");
        ReceptionDetailCA receptiondetailca = ReceptionDetailCAFactory.receptiondetailcaDTOToReceptionDetailCA(receptiondetailcaDTO);
        receptiondetailca = receptiondetailcaRepository.save(receptiondetailca);
        ReceptionDetailCADTO resultDTO = ReceptionDetailCAFactory.receptiondetailcaToReceptionDetailCADTO(receptiondetailca);
        return resultDTO;
    }

    /**
     * Get one receptiondetailcaDTO by id.
     *
     * @param codeCommande the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public List<ReceptionDetailCADTO> findRecivedDetailCAByCommandeAchat(Integer codeCommande) {
        log.debug("Request to get ReceptionDetailCA: {}", codeCommande);
        List<ReceptionDetailCA> listeReceptiondetailcas = receptiondetailcaRepository.findByPkCommandeAchat(codeCommande);
        List<ReceptionTemporaireDetailCa> recivedTempDetailCAs = receptionTemporaireDetailCaService.findByCodesCAAndNotValidated(codeCommande);// find Recepetion temp detail ca by code in and non receptionnes

        Map<Integer, List<ReceptionDetailCA>> listeReceptionDetailsCaGroupedBycodart = listeReceptiondetailcas
                .stream()
                .collect(Collectors.groupingBy(item -> item.getPk().getArticle()));

        List<ReceptionDetailCADTO> dtos = new ArrayList();

        for (Map.Entry<Integer, List<ReceptionDetailCA>> receptionDetailsCa : listeReceptionDetailsCaGroupedBycodart.entrySet()) {
            BigDecimal totalReceivedQuantity = BigDecimal.ZERO;
            BigDecimal totalFreereceivedQuantity = BigDecimal.ZERO;
            for (ReceptionDetailCA receptionDetailCa : receptionDetailsCa.getValue()) {

                totalReceivedQuantity = totalReceivedQuantity.add(receptionDetailCa.getQuantiteReceptione());
                totalFreereceivedQuantity = totalFreereceivedQuantity.add(receptionDetailCa.getQuantiteGratuite());

            }

            dtos.add(new ReceptionDetailCADTO(codeCommande, receptionDetailsCa.getKey(), totalReceivedQuantity, totalFreereceivedQuantity));

        }
        log.debug("commande from reception: {}", dtos);

        Map<Integer, List<ReceptionTemporaireDetailCa>> listeReceptionDetailsTemporaireCaGroupedBycodart = recivedTempDetailCAs
                .stream()
                .collect(Collectors.groupingBy(item -> item.getReceptionTemporaireDetailCaPK().getArticle()));

        for (Map.Entry<Integer, List<ReceptionTemporaireDetailCa>> receptionTempDetailsCa : listeReceptionDetailsTemporaireCaGroupedBycodart.entrySet()) {
            BigDecimal totalTempReceivedQuantity = BigDecimal.ZERO;
            BigDecimal totalTempFreeReceivedQuantity = BigDecimal.ZERO;
            log.debug("***********************************iteration************************");
            for (ReceptionTemporaireDetailCa receptionTempDetailCa : receptionTempDetailsCa.getValue()) {

                totalTempReceivedQuantity = totalTempReceivedQuantity.add(receptionTempDetailCa.getQuantiteReceptione());
                totalTempFreeReceivedQuantity = totalTempFreeReceivedQuantity.add(receptionTempDetailCa.getQuantiteGratuite());

                log.debug("totalTempReceivedQuantity for item {} est {}", receptionTempDetailsCa.getKey(), totalTempReceivedQuantity);
            }

            Optional<ReceptionDetailCADTO> matchingReceptionDetailCADTO = dtos.stream()
                    .filter(elt -> elt.getArticle().equals(receptionTempDetailsCa.getKey()))
                    .findFirst();

            log.debug("matchingReceptionDetailCADTO est {}", matchingReceptionDetailCADTO);
            if (matchingReceptionDetailCADTO.isPresent()) {

                matchingReceptionDetailCADTO.get().setReceivedQuantity(matchingReceptionDetailCADTO.get().getReceivedQuantity().add(totalTempReceivedQuantity));
                matchingReceptionDetailCADTO.get().setFreeReceivedQuantity(matchingReceptionDetailCADTO.get().getFreeReceivedQuantity().add(totalTempFreeReceivedQuantity));
                log.debug("matchingReceptionDetailCADTO while present est {}", matchingReceptionDetailCADTO);

            } else {
                dtos.add(new ReceptionDetailCADTO(codeCommande, receptionTempDetailsCa.getKey(), totalTempReceivedQuantity, totalTempFreeReceivedQuantity));
            }

        }
        log.debug("commande from receptionTemporaire: {}", dtos);

        return dtos;
    }

    /**
     * findRecivedDetailCAByPKs .
     *
     * @param receptionDetailsCAs
     * @return the list of DTOs
     */
    @Transactional(readOnly = true)
    public List<ReceptionDetailCADTO> findRecivedDetailCAByCAsAndArticleIds(List<ReceptionDetailCADTO> receptionDetailsCAs) {
        log.debug("Request to get ReceptionDetailCA: {}", receptionDetailsCAs);
        List<Integer> pks = receptionDetailsCAs.stream().map(ReceptionDetailCADTO::getPurchaseOrder).collect(toList());

        List<ReceptionDetailCA> receptiondetailca = new ArrayList();
        Integer numberOfChunks = (int) Math.ceil((double) pks.size() / 2000);
        for (int i = 0; i < numberOfChunks; i++) {
            List<Integer> pksChunk = pks.subList(i * 2000, Math.min(i * 2000 + 2000, pks.size()));
            receptiondetailca.addAll(receptiondetailcaRepository.findByPkCommandeAchatIn(pksChunk));
        }

        receptionDetailsCAs.stream().forEach(item -> {
            BigDecimal qteRecept = receptiondetailca.stream()
                    .filter(elt -> elt.getPk().getArticle().equals(item.getArticle()) && elt.getPk().getCommandeAchat().equals(item.getPurchaseOrder()))
                    .collect(reducing(BigDecimal.ZERO, ReceptionDetailCA::getQuantiteReceptione, (a, b) -> a.add(b)));
            item.setReceivedQuantity(qteRecept);
        });
        
        
        log.debug("resultfindRecivedDetailCAByCAsAndArticleIds : {}", receptionDetailsCAs);
        return receptionDetailsCAs;
    }

    /**
     * Get one receptiondetailcaDTO by id.
     *
     * @param receptionIDs
     * @return the entity DTO
     */
    @Transactional(readOnly = true)
    public List<ReceptionDetailCA> findRecivedDetailCAByReceptionIn(List<String> receptionIDs) {
        log.debug("Request to find receivedDetailCa by reception in: {}", receptionIDs);
        List<ReceptionDetailCA> receptiondetailca = receptiondetailcaRepository.findByPkReceptionIn(receptionIDs);

        return receptiondetailca;
    }

    /**
     * Get one receptiondetailca by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public ReceptionDetailCA findReceptionDetailCA(ReceptionDetailCAPK id) {
        log.debug("Request to get ReceptionDetailCA: {}", id);
        ReceptionDetailCA receptiondetailca = receptiondetailcaRepository.findOne(id);
        return receptiondetailca;
    }

    /**
     * Get all the receptiondetailcas.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<ReceptionDetailCADTO> findAll() {
        log.debug("Request to get All ReceptionDetailCAs");
        Collection<ReceptionDetailCA> result = receptiondetailcaRepository.findAll();
        return ReceptionDetailCAFactory.receptiondetailcaToReceptionDetailCADTOs(result);
    }

    /**
     * Get all the receptiondetailcas.
     *
     * @param listCodeCAs codes of commandes achat.
     * @return the the list of ReceptionDetails which codes are in or not in the
     * passed list.
     */
    @Transactional(
            readOnly = true
    )
    public List<ReceptionDetailCA> findByCodesCAIn(List<Integer> listCodeCAs) {
        log.debug("Request to find ReceptionDetailCAs by codes in");
        List<ReceptionDetailCA> result = receptiondetailcaRepository.findByPkCommandeAchatIn(listCodeCAs);
        return result;
    }

    /**
     * Get all the receptiondetailcas.
     *
     * @param listCodeCAs codes of commandes achat.
     * @param notIn if true it will the recived purchase orders that are not in
     * the list of codes.
     * @return the the list of ReceptionDetails which codes are in or not in the
     * passed list.
     */
    @Transactional(
            readOnly = true
    )
    public List<ReceptionDetailCADTO> searchByCodesCA(List<Integer> listCodeCAs, Boolean notIn) {
        log.debug("Request to search ReceptionDetailCAs");

        if (listCodeCAs.isEmpty()) {
            return findAll();
        } else {
            List<ReceptionDetailCA> result = notIn ? receptiondetailcaRepository.findByPkCommandeAchatNotIn(listCodeCAs) : receptiondetailcaRepository.findByPkCommandeAchatIn(listCodeCAs);

            return ReceptionDetailCAFactory.receptiondetailcaToReceptionDetailCADTOs(result);
        }
    }

    /**
     * Delete receptiondetailca by id.
     *
     * @param id the id of the entity
     */
    public void delete(ReceptionDetailCAPK id) {
        log.debug("Request to delete ReceptionDetailCA: {}", id);
        receptiondetailcaRepository.delete(id);
    }
}
