package com.csys.pharmacie.transfert.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.transfert.domain.MvtStoBE;
import com.csys.pharmacie.transfert.domain.MvtStoBEPK;
import com.csys.pharmacie.transfert.domain.QMvtStoBE;
import com.csys.pharmacie.transfert.dto.MvtStoBEDTO;
import com.csys.pharmacie.transfert.factory.MvtStoBEFactory;
import com.csys.pharmacie.transfert.repository.MvtstoBERepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MvtStoBE.
 */
@Service
@Transactional
public class MvtStoBEService {

    private final Logger log = LoggerFactory.getLogger(MvtStoBEService.class);

    private final MvtstoBERepository mvtstobeRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final MvtStoBEFactory mvtStoBEFactory;

    public MvtStoBEService(MvtstoBERepository mvtstobeRepository, ParamAchatServiceClient paramAchatServiceClient, MvtStoBEFactory mvtStoBEFactory) {
        this.mvtstobeRepository = mvtstobeRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.mvtStoBEFactory = mvtStoBEFactory;
    }

    /**
     * Save a mvtstobeDTO.
     *
     * @param mvtstobeDTO
     * @return the persisted entity
     */
    public MvtStoBEDTO save(MvtStoBEDTO mvtstobeDTO) {
        log.debug("Request to save MvtStoBE: {}", mvtstobeDTO);
        MvtStoBE mvtstobe = MvtStoBEFactory.mvtstobeDTOToMvtStoBE(mvtstobeDTO);
        mvtstobe = mvtstobeRepository.save(mvtstobe);
        MvtStoBEDTO resultDTO = MvtStoBEFactory.mvtstobeToMvtStoBEDTO(mvtstobe);
        return resultDTO;
    }

    /**
     * Get one mvtstobeDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public MvtStoBEDTO findOne(MvtStoBEPK id) {
        log.debug("Request to get MvtStoBE: {}", id);
        MvtStoBE mvtstobe = mvtstobeRepository.findOne(id);

        MvtStoBEDTO dto = MvtStoBEFactory.mvtstobeToMvtStoBEDTO(mvtstobe);
        return dto;
    }

    /**
     * Get one mvtstobe by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public MvtStoBE findMvtStoBE(MvtStoBEPK id) {
        log.debug("Request to get MvtStoBE: {}", id);
        MvtStoBE mvtstobe = mvtstobeRepository.findOne(id);
        return mvtstobe;
    }

    /**
     * Get all the mvtstobes by numbon.
     *
     * @param numBon
     * @return the the list of entities of the given numbon
     */
    @Transactional(
            readOnly = true
    )
    public List<MvtStoBEDTO> findByNumBon(String numBon) {
        log.debug("Request to get All MvtStoBEs");
        List<MvtStoBE> result = mvtstobeRepository.findByNumbon(numBon);
        Set<Integer> unitIds = result.stream().map(MvtStoBE::getUnite).collect(toSet());
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unitIds);

        return result.stream().map(item -> {
            MvtStoBEDTO mvtstoBEDTO = MvtStoBEFactory.mvtstobeToMvtStoBEDTO(item);
            UniteDTO matchedUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUnite())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit"));
            mvtstoBEDTO.setDesignationUnite(matchedUnit.getDesignation());
            return mvtstoBEDTO;
        }).collect(toList());
    }

    /**
     * Delete mvtstobe by id.
     *
     * @param id the id of the entity
     */
    public void delete(MvtStoBEPK id) {
        log.debug("Request to delete MvtStoBE: {}", id);
        mvtstobeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
        QMvtStoBE _mvtsto = QMvtStoBE.mvtStoBE;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.factureBE().categDepot.eq(categ))
                .optionalAnd(codart, () -> _mvtsto.codart.eq(codart))
                .optionalAnd(coddep, () -> _mvtsto.factureBE().coddep.eq(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.factureBE().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.factureBE().datbon.loe(toDate));
        List<MvtStoBE> list = (List<MvtStoBE>) mvtstobeRepository.findAll(builder);
        List<Integer> codeDepots = new ArrayList<>();
        List<Integer> codeUnites = new ArrayList<>();
        list.forEach(x -> {
            codeDepots.add(x.getFactureBE().getCoddep());
            codeUnites.add(x.getUnite());
        });
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots);
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        List<Mouvement> mouvements = new ArrayList<>();
        list.forEach(y -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(y.getUnite())).findFirst().orElse(null);
            DepotDTO codedep = listDepot.stream().filter(x -> x.getCode().equals(y.getFactureBE().getCoddep())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            com.csys.util.Preconditions.checkBusinessLogique(codedep != null, "missing-depot");
            mouvements.addAll(mvtStoBEFactory.toMouvement(y, unite, codedep, typeDate));
        });
        return mouvements;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementEntree(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return mvtstobeRepository.findTotalMouvementEntree(coddep, codart, toDate);
        } else {
            return mvtstobeRepository.findTotalMouvementEntree(coddep, codart, fromDate, toDate);
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementSortie(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return mvtstobeRepository.findTotalMouvementSortie(coddep, codart, toDate);
        } else {
            return mvtstobeRepository.findTotalMouvementSortie(coddep, codart, fromDate, toDate);
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementEntree(List<Integer> codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        List<TotalMouvement> lists = new ArrayList<>();
        if (codart != null && codart.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codart.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {

                List<Integer> codesChunk = codart.subList(i * 2000, Math.min(i * 2000 + 2000, codart.size()));
                if (fromDate == null) {
                    completableFuture[i] = mvtstobeRepository.findTotalMouvementEntree(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = mvtstobeRepository.findTotalMouvementEntree(coddep, codesChunk, fromDate, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                }
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementSortie(List<Integer> codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        List<TotalMouvement> lists = new ArrayList<>();
        if (codart != null && codart.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codart.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {

                List<Integer> codesChunk = codart.subList(i * 2000, Math.min(i * 2000 + 2000, codart.size()));
                if (fromDate == null) {
                    completableFuture[i] = mvtstobeRepository.findTotalMouvementSortie(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = mvtstobeRepository.findTotalMouvementSortie(coddep, codesChunk, fromDate, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                }
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddep != null) {
            if (fromDate == null) {
                return mvtstobeRepository.findTotalMouvementEntree(coddep, toDate);
            } else {
                return mvtstobeRepository.findTotalMouvementEntree(coddep, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return mvtstobeRepository.findTotalMouvementEntree(toDate);
            } else {
                return mvtstobeRepository.findTotalMouvementEntree(fromDate, toDate);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddep != null) {
            if (fromDate == null) {
                return mvtstobeRepository.findTotalMouvementSortie(coddep, toDate);
            } else {
                return mvtstobeRepository.findTotalMouvementSortie(coddep, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return mvtstobeRepository.findTotalMouvementSortie(toDate);
            } else {
                return mvtstobeRepository.findTotalMouvementSortie(fromDate, toDate);
            }
        }
    }
    
    
    public List<MvtStoBE> findByCodeArticleInAndDatBonAfterAndQuantityGreaterThan(Set<Integer> codeArticles, LocalDateTime datbon,BigDecimal quantite){
      log.debug("find derniers MVtstoBe avec les codarts suivants {}", codeArticles);
        return mvtstobeRepository.findByCodartInAndFactureBE_DatbonAfterAndQuantiteGreaterThan(codeArticles, datbon,quantite);
 }
}
