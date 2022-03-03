package com.csys.pharmacie.prelevement.service;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.MouvementConsomation;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.prelevement.domain.MvtStoPR;
import com.csys.pharmacie.prelevement.domain.QMvtStoPR;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.prelevement.dto.MvtStoPRDTO;
import com.csys.pharmacie.prelevement.factory.MvtStoPRFactory;
import com.csys.pharmacie.prelevement.repository.MvtStoPRRepository;
import com.google.common.base.Preconditions;
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
 * Service Implementation for managing MvtStoPR.
 */
@Service
@Transactional
public class MvtStoPRService {

    private final Logger log = LoggerFactory.getLogger(MvtStoPRService.class);

    private final MvtStoPRRepository mvtstoprRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final MvtStoPRFactory mvtStoPRFactory;

    public MvtStoPRService(MvtStoPRRepository mvtstoprRepository, ParamAchatServiceClient paramAchatServiceClient, MvtStoPRFactory mvtStoPRFactory) {
        this.mvtstoprRepository = mvtstoprRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.mvtStoPRFactory = mvtStoPRFactory;
    }

    /**
     * Save a mvtstoprDTO.
     *
     * @param mvtstoprDTO
     * @return the persisted entity
     */
    public MvtStoPRDTO save(MvtStoPRDTO mvtstoprDTO) {
        log.debug("Request to save MvtStoPR: {}", mvtstoprDTO);
        MvtStoPR mvtstopr = MvtStoPRFactory.mvtstoprDTOToMvtStoPR(mvtstoprDTO);
        mvtstopr = mvtstoprRepository.save(mvtstopr);
        MvtStoPRDTO resultDTO = MvtStoPRFactory.mvtstoprToMvtStoPRDTO(mvtstopr);
        return resultDTO;
    }

    /**
     * Update a mvtstoprDTO.
     *
     * @param mvtstoprDTO
     * @return the updated entity
     */
    public MvtStoPRDTO update(MvtStoPRDTO mvtstoprDTO) {
        log.debug("Request to update MvtStoPR: {}", mvtstoprDTO);
        // MvtStoPR inBase= mvtstoprRepository.findOne(mvtstoprDTO.getPk());
        MvtStoPR inBase = mvtstoprRepository.findOne(mvtstoprDTO.getCode());
        Preconditions.checkArgument(inBase != null, "mvtstopr.NotFound");
        MvtStoPR mvtstopr = MvtStoPRFactory.mvtstoprDTOToMvtStoPR(mvtstoprDTO);
        mvtstopr = mvtstoprRepository.save(mvtstopr);
        MvtStoPRDTO resultDTO = MvtStoPRFactory.mvtstoprToMvtStoPRDTO(mvtstopr);
        return resultDTO;
    }

    /**
     * Get one mvtstoprDTO by id.
     *
     * @param id the id of the entity
     * @return the entity DTO
     */
    @Transactional(
            readOnly = true
    )
    // public MvtStoPRDTO findOne(MvtStoPRPK id) {
    public MvtStoPRDTO findOne(Integer id) {
        log.debug("Request to get MvtStoPR: {}", id);
        MvtStoPR mvtstopr = mvtstoprRepository.findOne(id);

        MvtStoPRDTO dto = MvtStoPRFactory.mvtstoprToMvtStoPRDTO(mvtstopr);
        return dto;
    }

    /**
     * Get one mvtstopr by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(
            readOnly = true
    )
    //public MvtStoPR findMvtStoPR(MvtStoPRPK id) {
    public MvtStoPR findMvtStoPR(Integer id) {
        log.debug("Request to get MvtStoPR: {}", id);
        MvtStoPR mvtstopr = mvtstoprRepository.findOne(id);
        return mvtstopr;
    }

    /**
     * Delete mvtstopr by id.
     *
     * @param id the id of the entity
     */
    // public void delete(MvtStoPRPK id) {
    public void delete(Integer id) {
        log.debug("Request to delete MvtStoPR: {}", id);
        mvtstoprRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
        QMvtStoPR _mvtsto = QMvtStoPR.mvtStoPR;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.facturePR().categDepot.eq(categ))
                .optionalAnd(codart, () -> _mvtsto.codart.eq(codart))
                .optionalAnd(coddep, () -> _mvtsto.facturePR().coddepotSrc.eq(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.facturePR().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.facturePR().datbon.loe(toDate))
                .and(_mvtsto.facturePR().codAnnul.isNull());
        List<MvtStoPR> list = (List<MvtStoPR>) mvtstoprRepository.findAll(builder);
        List<Integer> codeDepartementts = new ArrayList<>();
        List<Integer> codeUnites = new ArrayList<>();
        list.forEach(x -> {
            codeDepartementts.add(x.getFacturePR().getCoddepartDest());
            codeUnites.add(x.getUnite());
        });
        List<DepartementDTO> listDepartementts = paramAchatServiceClient.findListDepartments(codeDepartementts);
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        List<Mouvement> mouvements = new ArrayList<>();
        list.forEach(y -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(y.getUnite())).findFirst().orElse(null);
            DepartementDTO departement = listDepartementts.stream().filter(x -> x.getCode().equals(y.getFacturePR().getCoddepartDest())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            com.csys.util.Preconditions.checkBusinessLogique(departement != null, "missing-depot");
            mouvements.addAll(mvtStoPRFactory.toMouvement(y, unite, departement, typeDate));
        });
        return mouvements;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return mvtstoprRepository.findTotalMouvement(coddep, codart, toDate);
        } else {
            return mvtstoprRepository.findTotalMouvement(coddep, codart, fromDate, toDate);
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
                    completableFuture[i] = mvtstoprRepository.findTotalMouvement(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = mvtstoprRepository.findTotalMouvement(coddep, codesChunk, fromDate, toDate).whenComplete((articles, exception) -> {
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
                return mvtstoprRepository.findTotalMouvement(coddep, toDate);
            } else {
                return mvtstoprRepository.findTotalMouvement(coddep, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return mvtstoprRepository.findTotalMouvement(toDate);
            } else {
                return mvtstoprRepository.findTotalMouvement(fromDate, toDate);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvementSortie(Integer coddepart_desti, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddepart_desti != null) {
            if (fromDate == null) {
                return mvtstoprRepository.findTotalMouvement(coddepart_desti, toDate);
            } else {
                return mvtstoprRepository.findTotalMouvement(coddepart_desti, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return mvtstoprRepository.findTotalMouvement(toDate);
            } else {
                return mvtstoprRepository.findTotalMouvement(fromDate, toDate);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<MouvementConsomation> findConsomationReel(CategorieDepotEnum categ, List<Integer> coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        QMvtStoPR _mvtsto = QMvtStoPR.mvtStoPR;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.facturePR().categDepot.eq(categ))
                .optionalAnd(coddep, () -> _mvtsto.facturePR().coddepartDest.in(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.facturePR().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.facturePR().datbon.loe(toDate))
                .and(_mvtsto.facturePR().codAnnul.isNull());
        List<MvtStoPR> list = (List<MvtStoPR>) mvtstoprRepository.findAll(builder);
        List<Integer> codeUnites = new ArrayList<>();
        List<Integer> codeDepartementts = new ArrayList<>();
        List<Integer> codeArticles = new ArrayList<>();
        list.forEach(mvtsto -> {
            codeDepartementts.add(mvtsto.getFacturePR().getCoddepartDest());
            codeUnites.add(mvtsto.getUnite());
            codeArticles.add(mvtsto.getCodart());
        });
        List<DepartementDTO> listDepartementts = paramAchatServiceClient.findListDepartments(codeDepartementts.stream().distinct().collect(Collectors.toList()));
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites.stream().distinct().collect(Collectors.toList()));
        List<ArticleDTO> listArticles = paramAchatServiceClient.articleFindbyListCode(codeArticles.stream().distinct().collect(Collectors.toList()));
        List<MouvementConsomation> mouvements = new ArrayList<>();
        list.forEach(mvtsto -> {
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mvtsto.getUnite())).findFirst().orElse(null);
            DepartementDTO departement = listDepartementts.stream().filter(x -> x.getCode().equals(mvtsto.getFacturePR().getCoddepartDest())).findFirst().orElse(null);
            ArticleDTO article = listArticles.stream().filter(x -> x.getCode().equals(mvtsto.getCodart())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            com.csys.util.Preconditions.checkBusinessLogique(article != null, "missing-article");
            com.csys.util.Preconditions.checkBusinessLogique(departement != null, "missing-depot");
            mouvements.add(mvtStoPRFactory.toMouvement(mvtsto, unite, departement, article));
        });
        return mouvements;
    }

}
