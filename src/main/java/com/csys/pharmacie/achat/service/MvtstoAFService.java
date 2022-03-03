package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.achat.domain.MvtstoAF;
import com.csys.pharmacie.achat.domain.QMvtstoAF;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoAFDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.factory.MvtstoAFFactory;
import com.csys.pharmacie.achat.repository.MvtstoAFRepository;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MvtstoAF.
 */
@Service
@Transactional
public class MvtstoAFService {

    private final Logger log = LoggerFactory.getLogger(MvtstoAFService.class);

    private final MvtstoAFRepository mvtstoafRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final MvtstoAFFactory mvtstoAFFactory;

    public MvtstoAFService(MvtstoAFRepository mvtstoafRepository, ParamAchatServiceClient paramAchatServiceClient, MvtstoAFFactory mvtstoAFFactory) {
        this.mvtstoafRepository = mvtstoafRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.mvtstoAFFactory = mvtstoAFFactory;
    }

    /**
     * Save a mvtstoafDTO.
     *
     * @param mvtstoafDTO
     * @return the persisted entity
     */
    public MvtstoAFDTO save(MvtstoAFDTO mvtstoafDTO) {
        log.debug("Request to save MvtstoAF: {}", mvtstoafDTO);
        MvtstoAF mvtstoaf = mvtstoAFFactory.mvtstoAFDTOToMvtstoAF(mvtstoafDTO);
        mvtstoaf = mvtstoafRepository.save(mvtstoaf);
        MvtstoAFDTO resultDTO = mvtstoAFFactory.mvtstoAFToMvtstoAFDTO(mvtstoaf);
        return resultDTO;
    }

    /**
     * Update a mvtstoafDTO.
     *
     * @param mvtstoafDTO
     * @return the updated entity
     */
    /**
     * Get one mvtstoafDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    public MvtstoAFDTO findOne(Integer id) {
        log.debug("Request to get MvtstoAF: {}", id);
        MvtstoAF mvtstoaf = mvtstoafRepository.findOne(id);
        MvtstoAFDTO dto = mvtstoAFFactory.mvtstoAFToMvtstoAFDTO(mvtstoaf);
        return dto;
    }

    /**
     * Get one mvtstoaf by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    public MvtstoAF findMvtstoAF(Integer id) {
        log.debug("Request to get MvtstoAF: {}", id);
        MvtstoAF mvtstoaf = mvtstoafRepository.findOne(id);
        return mvtstoaf;
    }

    /**
     * Get all the mvtstoafs.
     *
     * @return the the list of entities
     */
    @Transactional(
            readOnly = true
    )
    public List<MvtstoAFDTO> findAll() {
        log.debug("Request to get All MvtstoAFs");
        List<MvtstoAF> result = mvtstoafRepository.findAll();
        return mvtstoAFFactory.mvtstoAFToMvtstoAFDTOs(result);
    }

    /**
     * Delete mvtstoaf by id.
     *
     * @param id the id of the entity
     */
    public void delete(Integer id) {
        log.debug("Request to delete MvtstoAF: {}", id);
        mvtstoafRepository.delete(id);
    }

    public List<MvtstoAF> findByNumbon(String id) {

        return mvtstoafRepository.findByNumbon(id);

    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddep != null) {
            if (fromDate == null) {
                return mvtstoafRepository.findTotalMouvement(coddep, toDate);
            } else {
                return mvtstoafRepository.findTotalMouvement(coddep, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return mvtstoafRepository.findTotalMouvement(toDate);
            } else {
                return mvtstoafRepository.findTotalMouvement(fromDate, toDate);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return mvtstoafRepository.findTotalMouvement(coddep, codart, toDate);
        } else {
            return mvtstoafRepository.findTotalMouvement(coddep, codart, fromDate, toDate);
        }
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
                    completableFuture[i] = mvtstoafRepository.findTotalMouvement(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = mvtstoafRepository.findTotalMouvement(coddep, codesChunk, fromDate, toDate).whenComplete((articles, exception) -> {
                        lists.addAll(articles);
                    });
                }
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }

    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
        QMvtstoAF _mvtsto = QMvtstoAF.mvtstoAF;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.avoirFournisseur().categDepot.eq(categ))
                .optionalAnd(codart, () -> _mvtsto.codart.eq(codart))
                .optionalAnd(coddep, () -> _mvtsto.avoirFournisseur().coddep.in(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.avoirFournisseur().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.avoirFournisseur().datbon.loe(toDate));
        List<MvtstoAF> list = (List<MvtstoAF>) mvtstoafRepository.findAll(builder);
        List<Mouvement> mouvements = new ArrayList<>();
        List<String> codesFrs = list.stream().map(item -> item.getAvoirFournisseur().getCodeFournisseur()).distinct().collect(Collectors.toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
        List<Integer> codeUnites = list.stream().map(item -> item.getUnite()).distinct().collect(Collectors.toList());
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        list.forEach((mouvement) -> {
            FournisseurDTO fournisseur = fournisseurs.stream().filter(x -> x.getCode().equals(mouvement.getAvoirFournisseur().getCodeFournisseur())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(fournisseur != null, "missing-frs");
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mouvement.getUnite())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            log.debug("mvtsto ba mouvement {}", mouvement);
            mouvements.addAll(mvtstoAFFactory.toMouvement(mouvement, fournisseur, unite, typeDate));
        });
        return mouvements;
    }

}
