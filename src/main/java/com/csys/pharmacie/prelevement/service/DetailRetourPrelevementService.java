package com.csys.pharmacie.prelevement.service;

import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.prelevement.domain.DetailRetourPrelevement;
import com.csys.pharmacie.prelevement.domain.QDetailRetourPrelevement;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.prelevement.dto.DetailRetourPrelevementDTO;
import com.csys.pharmacie.prelevement.factory.DetailRetourPrelevementFactory;
import com.csys.pharmacie.prelevement.repository.DetailRetourPrelevementRepository;
import com.google.common.base.Preconditions;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing DetailRetourPrelevement.
 */
@Service
@Transactional
public class DetailRetourPrelevementService {

    private final Logger log = LoggerFactory.getLogger(DetailRetourPrelevementService.class);

    private final DetailRetourPrelevementRepository detailretourprelevementRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final DetailRetourPrelevementFactory detailRetourPrelevementFactory;

    public DetailRetourPrelevementService(DetailRetourPrelevementRepository detailretourprelevementRepository, ParamAchatServiceClient paramAchatServiceClient, DetailRetourPrelevementFactory detailRetourPrelevementFactory) {
        this.detailretourprelevementRepository = detailretourprelevementRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.detailRetourPrelevementFactory = detailRetourPrelevementFactory;
    }

    

    /**
     * Save a detailretourprelevementDTO.
     *
     * @param detailretourprelevementDTO
     * @return the persisted entity
     */
    public DetailRetourPrelevementDTO save(DetailRetourPrelevementDTO detailretourprelevementDTO) {
        log.debug("Request to save DetailRetourPrelevement: {}", detailretourprelevementDTO);
        DetailRetourPrelevement detailretourprelevement = DetailRetourPrelevementFactory.detailretourprelevementDTOToDetailRetourPrelevement(detailretourprelevementDTO);
        detailretourprelevement = detailretourprelevementRepository.save(detailretourprelevement);
        DetailRetourPrelevementDTO resultDTO = DetailRetourPrelevementFactory.detailretourprelevementToDetailRetourPrelevementDTO(detailretourprelevement);
        return resultDTO;
    }

    /**
     * Update a detailretourprelevementDTO.
     *
     * @param detailretourprelevementDTO
     * @return the updated entity
     */
    public DetailRetourPrelevementDTO update(DetailRetourPrelevementDTO detailretourprelevementDTO) {
        log.debug("Request to update DetailRetourPrelevement: {}", detailretourprelevementDTO);
        DetailRetourPrelevement inBase = detailretourprelevementRepository.findOne(detailretourprelevementDTO.getCode());
        Preconditions.checkArgument(inBase != null, "detailretourprelevement.NotFound");
        DetailRetourPrelevement detailretourprelevement = DetailRetourPrelevementFactory.detailretourprelevementDTOToDetailRetourPrelevement(detailretourprelevementDTO);
        detailretourprelevement = detailretourprelevementRepository.save(detailretourprelevement);
        DetailRetourPrelevementDTO resultDTO = DetailRetourPrelevementFactory.detailretourprelevementToDetailRetourPrelevementDTO(detailretourprelevement);
        return resultDTO;
    }

    /**
     * Get one detailretourprelevementDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public DetailRetourPrelevementDTO findOne(Integer id) {
        log.debug("Request to get DetailRetourPrelevement: {}", id);
        DetailRetourPrelevement detailretourprelevement = detailretourprelevementRepository.findOne(id);
        DetailRetourPrelevementDTO dto = DetailRetourPrelevementFactory.detailretourprelevementToDetailRetourPrelevementDTO(detailretourprelevement);
        return dto;
    }

    /**
     * Get one detailretourprelevement by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public DetailRetourPrelevement findDetailRetourPrelevement(Integer id) {
        log.debug("Request to get DetailRetourPrelevement: {}", id);
        DetailRetourPrelevement detailretourprelevement = detailretourprelevementRepository.findOne(id);
        return detailretourprelevement;
    }

    /**
     * Get all the detailretourprelevements.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<DetailRetourPrelevementDTO> findAll() {
        log.debug("Request to get All DetailRetourPrelevements");
        List<DetailRetourPrelevement> result = detailretourprelevementRepository.findAll();
        return DetailRetourPrelevementFactory.detailretourprelevementToDetailRetourPrelevementDTOs(result);
    }

    /**
     * Delete detailretourprelevement by id.
     *
     * @param id the id of the entity
     */
    public void delete(Integer id) {
        log.debug("Request to delete DetailRetourPrelevement: {}", id);
        detailretourprelevementRepository.delete(id);
    }

    public List<DetailRetourPrelevement> findByNumBon(String numBon) {
        return detailretourprelevementRepository.findByNumbon(numBon);
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return detailretourprelevementRepository.findTotalMouvement(coddep, codart, toDate);
        } else {
            return detailretourprelevementRepository.findTotalMouvement(coddep, codart, fromDate, toDate);
        }
    }

    @Transactional(readOnly = true)
    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
        QDetailRetourPrelevement _mvtsto = QDetailRetourPrelevement.detailRetourPrelevement;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.retourPrelevement().categDepot.eq(categ))
                .optionalAnd(codart, () -> _mvtsto.codart.eq(codart))
                .optionalAnd(coddep, () -> _mvtsto.retourPrelevement().coddepDesti.eq(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.retourPrelevement().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.retourPrelevement().datbon.loe(toDate));
        List<DetailRetourPrelevement> list = (List<DetailRetourPrelevement>) detailretourprelevementRepository.findAll(builder);
        List<Integer> codeDepartementts = new ArrayList<>();
        List<Integer> codeUnites = new ArrayList<>();
        list.forEach(x -> {
            codeDepartementts.add(x.getRetourPrelevement().getCoddepartSrc());
            codeUnites.add(x.getUnite());
        });
        List<DepartementDTO> listDepartementts = paramAchatServiceClient.findListDepartments(codeDepartementts);
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        List<Mouvement> mouvements = new ArrayList<>();
        list.forEach(y -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(y.getUnite())).findFirst().orElse(null);
            DepartementDTO departement = listDepartementts.stream().filter(x -> x.getCode().equals(y.getRetourPrelevement().getCoddepartSrc())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            com.csys.util.Preconditions.checkBusinessLogique(departement != null, "missing-depot");
            mouvements.addAll(detailRetourPrelevementFactory.toMouvement(y, unite, departement, typeDate));
        });
        return mouvements;
    }
    
    
     @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(List<Integer> codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        List<TotalMouvement> lists = new ArrayList<>();
        if (codart != null && codart.size() > 0) {
            Integer numberOfChunks = (int) Math.ceil((double) codart.size() / 2000);
            CompletableFuture[] completableFuture = new CompletableFuture[numberOfChunks];
            for (int i = 0; i < numberOfChunks; i++) {

                List<Integer> codesChunk = codart.subList(i * 2000, Math.min(i * 2000 + 2000, codart.size()));
                if (fromDate == null) {
                    completableFuture[i] = detailretourprelevementRepository.findTotalMouvement(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = detailretourprelevementRepository.findTotalMouvement(coddep, codesChunk, fromDate, toDate).whenComplete((articles, exception) -> {
                        lists.addAll(articles);
                    });
                }
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }
    
     @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddep != null) {
            if (fromDate == null) {
                return detailretourprelevementRepository.findTotalMouvement(coddep, toDate);
            } else {
                return detailretourprelevementRepository.findTotalMouvement(coddep, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return detailretourprelevementRepository.findTotalMouvement(toDate);
            } else {
                return detailretourprelevementRepository.findTotalMouvement(fromDate, toDate);
            }
        }
    }

}
