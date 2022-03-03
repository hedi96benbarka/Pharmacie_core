package com.csys.pharmacie.stock.service;

import com.csys.exception.IllegalBusinessLogiqueException;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.stock.domain.Decoupage;
import com.csys.pharmacie.stock.domain.DetailDecoupage;
import com.csys.pharmacie.stock.domain.DetailDecoupagePK;
import com.csys.pharmacie.stock.domain.QDetailDecoupage;
import com.csys.pharmacie.stock.dto.DetailDecoupageDTO;
import com.csys.pharmacie.stock.factory.DetailDecoupageFactory;
import com.csys.pharmacie.stock.repository.DetailDecoupageRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
 * Service Implementation for managing DetailDecoupage.
 */
@Service
@Transactional
public class DetailDecoupageService {

    private final Logger log = LoggerFactory.getLogger(DetailDecoupageService.class);

    private final DetailDecoupageRepository detaildecoupageRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final DetailDecoupageFactory detailDecoupageFactory;

    public DetailDecoupageService(DetailDecoupageRepository detaildecoupageRepository, ParamAchatServiceClient paramAchatServiceClient, DetailDecoupageFactory detailDecoupageFactory) {
        this.detaildecoupageRepository = detaildecoupageRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.detailDecoupageFactory = detailDecoupageFactory;
    }

    /**
     * Save a detaildecoupageDTO.
     *
     * @param detaildecoupageDTO
     * @return the persisted entity
     */
    public DetailDecoupageDTO save(DetailDecoupageDTO detaildecoupageDTO) {
        log.debug("Request to save DetailDecoupage: {}", detaildecoupageDTO);
        DetailDecoupage detaildecoupage = DetailDecoupageFactory.detaildecoupageDTOToDetailDecoupage(detaildecoupageDTO);
        detaildecoupage = detaildecoupageRepository.save(detaildecoupage);
        DetailDecoupageDTO resultDTO = DetailDecoupageFactory.detaildecoupageToDetailDecoupageDTO(detaildecoupage);
        return resultDTO;
    }

    /**
     * Update a detaildecoupageDTO.
     *
     * @param detaildecoupageDTO
     * @return the updated entity
     */
//  public DetailDecoupageDTO update(DetailDecoupageDTO detaildecoupageDTO) {
//    log.debug("Request to update DetailDecoupage: {}",detaildecoupageDTO);
//    DetailDecoupage inBase= detaildecoupageRepository.findOne(detaildecoupageDTO.getDetailDecoupagePK());
//    Preconditions.checkArgument(inBase != null, "detaildecoupage.NotFound");
//    DetailDecoupage detaildecoupage = DetailDecoupageFactory.detaildecoupageDTOToDetailDecoupage(detaildecoupageDTO);
//    detaildecoupage = detaildecoupageRepository.save(detaildecoupage);
//    DetailDecoupageDTO resultDTO = DetailDecoupageFactory.detaildecoupageToDetailDecoupageDTO(detaildecoupage);
//    return resultDTO;
//  }
    /**
     * Get one detaildecoupageDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public DetailDecoupageDTO findOne(DetailDecoupagePK id) {
        log.debug("Request to get DetailDecoupage: {}", id);
        DetailDecoupage detaildecoupage = detaildecoupageRepository.findOne(id);
        DetailDecoupageDTO dto = DetailDecoupageFactory.detaildecoupageToDetailDecoupageDTO(detaildecoupage);
        return dto;
    }

    /**
     * Get one detaildecoupage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public DetailDecoupage findDetailDecoupage(DetailDecoupagePK id) {
        log.debug("Request to get DetailDecoupage: {}", id);
        DetailDecoupage detaildecoupage = detaildecoupageRepository.findOne(id);
        return detaildecoupage;
    }

    @Transactional(
            readOnly = true
    )
    public List<DetailDecoupageDTO> findDetailDecoupageByCodeDecoupage(String decoupageID) {
        log.debug("Request to get DetailDecoupage: {}", decoupageID);
        List<DetailDecoupage> detaildecoupages = detaildecoupageRepository.findByCodeDecoupage(decoupageID);
        Set<Integer> unitIds = new HashSet();
        detaildecoupages.forEach(item -> {
            unitIds.add(item.getUniteFinal());
            unitIds.add(item.getUniteOrigine());

        });
        List<UniteDTO> unities = paramAchatServiceClient.findUnitsByCodes(unitIds);
        List<DetailDecoupageDTO> detailsDecoupageDTO = detaildecoupages.stream().map(item -> {
            DetailDecoupageDTO detailDecoupageDTO = DetailDecoupageFactory.detaildecoupageToDetailDecoupageDTO(item);
            UniteDTO originUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUniteOrigine())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit"));
            UniteDTO finalUnit = unities.stream().filter(unit -> unit.getCode().equals(item.getUniteFinal())).findFirst().orElseThrow(() -> new IllegalBusinessLogiqueException("missing-unit"));
            detailDecoupageDTO.setOriginUnitDesignation(originUnit.getDesignation());
            detailDecoupageDTO.setFinalUnitDesignation(finalUnit.getDesignation());
            return detailDecoupageDTO;
        }).collect(toList());

        return detailsDecoupageDTO;

    }

    /**
     * Get all the detaildecoupages.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public Collection<DetailDecoupageDTO> findAll() {
        log.debug("Request to get All DetailDecoupages");
        Collection<DetailDecoupage> result = detaildecoupageRepository.findAll();
        return DetailDecoupageFactory.detaildecoupageToDetailDecoupageDTOs(result);
    }

    /**
     * Delete detaildecoupage by id.
     *
     * @param id the id of the entity
     */
    public void delete(DetailDecoupagePK id) {
        log.debug("Request to delete DetailDecoupage: {}", id);
        detaildecoupageRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
        QDetailDecoupage _detailDecoupage = QDetailDecoupage.detailDecoupage;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_detailDecoupage.decoupage().categDepot.eq(categ))
                .optionalAnd(codart, () -> _detailDecoupage.codart.eq(codart))
                .optionalAnd(coddep, () -> _detailDecoupage.decoupage().coddep.eq(coddep))
                .optionalAnd(fromDate, () -> _detailDecoupage.decoupage().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _detailDecoupage.decoupage().datbon.loe(toDate));
        List<DetailDecoupage> list = (List<DetailDecoupage>) detaildecoupageRepository.findAll(builder);
        List<Mouvement> mouvements = new ArrayList<>();
        Set<Integer> codeDepots = list.stream().map(item -> item.getDecoupage().getCoddep()).collect(toSet());
        List<DepotDTO> listDepot = paramAchatServiceClient.findDepotsByCodes(codeDepots);
        List<Integer> codeUnites = new ArrayList<>();
        list.forEach(x -> {
            codeUnites.add(x.getUniteFinal());
            codeUnites.add(x.getUniteOrigine());
        });
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        list.forEach((mouvement) -> {
            DepotDTO depot = listDepot.stream().filter(x -> x.getCode().equals(mouvement.getDecoupage().getCoddep())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(depot != null, "missing-depot");
            UniteDTO uniteFinal = listUnite.stream().filter(x -> x.getCode().equals(mouvement.getUniteFinal())).findFirst().orElse(null);
            UniteDTO uniteOrigin = listUnite.stream().filter(x -> x.getCode().equals(mouvement.getUniteOrigine())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(uniteFinal != null, "missing-unity");
            com.csys.util.Preconditions.checkBusinessLogique(uniteOrigin != null, "missing-unity");
            mouvements.addAll(detailDecoupageFactory.toMouvement(mouvement, depot, uniteOrigin, uniteFinal, typeDate));
        });
        return mouvements;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementSortie(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return detaildecoupageRepository.findTotalMouvementSortie(coddep, codart, toDate);
        } else {
            return detaildecoupageRepository.findTotalMouvementSortie(coddep, codart, fromDate, toDate);
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementEntree(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return detaildecoupageRepository.findTotalMouvementEntree(coddep, codart, toDate);
        } else {
            return detaildecoupageRepository.findTotalMouvementEntree(coddep, codart, fromDate, toDate);
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
                    completableFuture[i] = detaildecoupageRepository.findTotalMouvementEntree(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = detaildecoupageRepository.findTotalMouvementEntree(coddep, codesChunk, fromDate, toDate).whenComplete((list, exception) -> {
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
                    completableFuture[i] = detaildecoupageRepository.findTotalMouvementSortie(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = detaildecoupageRepository.findTotalMouvementSortie(coddep, codesChunk, fromDate, toDate).whenComplete((list, exception) -> {
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
                return detaildecoupageRepository.findTotalMouvementEntree(coddep, toDate);
            } else {
                return detaildecoupageRepository.findTotalMouvementEntree(coddep, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return detaildecoupageRepository.findTotalMouvementEntree(toDate);
            } else {
                return detaildecoupageRepository.findTotalMouvementEntree(fromDate, toDate);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddep != null) {
            if (fromDate == null) {
                return detaildecoupageRepository.findTotalMouvementSortie(coddep, toDate);
            } else {
                return detaildecoupageRepository.findTotalMouvementSortie(coddep, fromDate, toDate);
            }
        } else {
            if (fromDate == null) {
                return detaildecoupageRepository.findTotalMouvementSortie(toDate);
            } else {
                return detaildecoupageRepository.findTotalMouvementSortie(fromDate, toDate);
            }

        }
    }
}
