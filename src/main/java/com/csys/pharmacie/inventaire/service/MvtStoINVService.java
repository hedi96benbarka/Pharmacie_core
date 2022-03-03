//package com.csys.pharmacie.inventaire.service;
//
//import com.csys.pharmacie.achat.dto.UniteDTO;
//import com.csys.pharmacie.inventaire.domain.MvtStoINV;
//import com.csys.pharmacie.inventaire.dto.MvtStoINVDTO;
//import com.csys.pharmacie.inventaire.factory.MvtStoINVFactory;
//import com.csys.pharmacie.inventaire.repository.MvtStoINVRepository;
//import com.csys.pharmacie.client.service.ParamAchatServiceClient;
//import com.csys.pharmacie.helper.CategorieDepotEnum;
//import com.csys.pharmacie.helper.DateInv;
//import com.csys.pharmacie.helper.Mouvement;
//import com.csys.pharmacie.helper.TotalMouvement;
//import com.csys.pharmacie.helper.TypeDateEnum;
//import com.csys.pharmacie.helper.WhereClauseBuilder;
//import com.csys.pharmacie.inventaire.domain.QDepStoHist;
//import com.csys.pharmacie.inventaire.domain.QMvtStoINV;
//import com.csys.pharmacie.inventaire.dto.DateInventaire;
//import com.google.common.base.Preconditions;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Service Implementation for managing MvtStoINV.
// */
//@Service
//@Transactional
//public class MvtStoINVService {
//
//    private final Logger log = LoggerFactory.getLogger(MvtStoINVService.class);
//
//    private final MvtStoINVRepository mvtstoinvRepository;
//    private final ParamAchatServiceClient paramAchatServiceClient;
//    private final MvtStoINVFactory mvtStoINVFactory;
//
//    public MvtStoINVService(MvtStoINVRepository mvtstoinvRepository, ParamAchatServiceClient paramAchatServiceClient, MvtStoINVFactory mvtStoINVFactory) {
//        this.mvtstoinvRepository = mvtstoinvRepository;
//        this.paramAchatServiceClient = paramAchatServiceClient;
//        this.mvtStoINVFactory = mvtStoINVFactory;
//    }
//
//    /**
//     * Save a mvtstoinvDTO.
//     *
//     * @param mvtstoinvDTO
//     * @return the persisted entity
//     */
//    public MvtStoINVDTO save(MvtStoINVDTO mvtstoinvDTO) {
//        log.debug("Request to save MvtStoINV: {}", mvtstoinvDTO);
//        MvtStoINV mvtstoinv = MvtStoINVFactory.mvtstoinvDTOToMvtStoINV(mvtstoinvDTO);
//        mvtstoinv = mvtstoinvRepository.save(mvtstoinv);
//        MvtStoINVDTO resultDTO = MvtStoINVFactory.mvtstoinvToMvtStoINVDTO(mvtstoinv);
//        return resultDTO;
//    }
//
//    /**
//     * Update a mvtstoinvDTO.
//     *
//     * @param mvtstoinvDTO
//     * @return the updated entity
//     */
//    public MvtStoINVDTO update(MvtStoINVDTO mvtstoinvDTO) {
//        log.debug("Request to update MvtStoINV: {}", mvtstoinvDTO);
//        MvtStoINV inBase = mvtstoinvRepository.findOne(mvtstoinvDTO.getNum());
//        Preconditions.checkArgument(inBase != null, "mvtstoinv.NotFound");
//        MvtStoINV mvtstoinv = MvtStoINVFactory.mvtstoinvDTOToMvtStoINV(mvtstoinvDTO);
//        mvtstoinv = mvtstoinvRepository.save(mvtstoinv);
//        MvtStoINVDTO resultDTO = MvtStoINVFactory.mvtstoinvToMvtStoINVDTO(mvtstoinv);
//        return resultDTO;
//    }
//
//    /**
//     * Get one mvtstoinvDTO by id.
//     *
//     * @param id the id of the entity
//     * @return the entity DTO
//     */
//    @Transactional(
//            readOnly = true
//    )
//    public MvtStoINVDTO findOne(Long id) {
//        log.debug("Request to get MvtStoINV: {}", id);
//        MvtStoINV mvtstoinv = mvtstoinvRepository.findOne(id);
//        MvtStoINVDTO dto = MvtStoINVFactory.mvtstoinvToMvtStoINVDTO(mvtstoinv);
//        return dto;
//    }
//
//    /**
//     * Get one mvtstoinv by id.
//     *
//     * @param id the id of the entity
//     * @return the entity
//     */
//    @Transactional(
//            readOnly = true
//    )
//    public MvtStoINV findMvtStoINV(Long id) {
//        log.debug("Request to get MvtStoINV: {}", id);
//        MvtStoINV mvtstoinv = mvtstoinvRepository.findOne(id);
//        return mvtstoinv;
//    }
//
//    /**
//     * Get all the mvtstoinvs.
//     *
//     * @return the the list of entities
//     */
//    @Transactional(
//            readOnly = true
//    )
//    public Collection<MvtStoINVDTO> findAll() {
//        log.debug("Request to get All MvtStoINVs");
//        Collection<MvtStoINV> result = mvtstoinvRepository.findAll();
//        return MvtStoINVFactory.mvtstoinvToMvtStoINVDTOs(result);
//    }
//
//    /**
//     * Delete mvtstoinv by id.
//     *
//     * @param id the id of the entity
//     */
//    public void delete(Long id) {
//        log.debug("Request to delete MvtStoINV: {}", id);
//        mvtstoinvRepository.delete(id);
//    }
//
//    @Transactional(readOnly = true)
//    public LocalDateTime findMaxDateInv(Integer codart, Integer coddep, LocalDateTime fromDate) {
//        return mvtstoinvRepository.findMaxHeureSystemByCodArtAndCoddepAndHeureSystemeGreaterThan(codart, coddep, fromDate);
//    }
//
//    @Transactional(readOnly = true)
//    public List<DateInv> findMaxDateInvByCoddepIn(Integer codart, Integer coddep, LocalDateTime fromDate) {
//        if (coddep != null) {
//            return mvtstoinvRepository.findMaxHeureSystemByCodArtAndHeureSystemeGreaterThan(codart, fromDate);
//        } else {
//            return mvtstoinvRepository.findMaxHeureSystemByCodArtAndCoddepInAndHeureSystemeGreaterThan(codart, coddep, fromDate);
//        }
//    }
//
//
//
//    @Transactional(readOnly = true)
//    public List<TotalMouvement> findTotalMouvementEntree(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
//        if (coddep != null) {
//            if (fromDate == null) {
//                return mvtstoinvRepository.findTotalMouvementEntree(coddep, toDate);
//            } else {
//                return mvtstoinvRepository.findTotalMouvementEntree(coddep, fromDate, toDate);
//            }
//
//        } else {
//            if (fromDate == null) {
//                return mvtstoinvRepository.findTotalMouvementEntree(toDate);
//            } else {
//                return mvtstoinvRepository.findTotalMouvementEntree(fromDate, toDate);
//            }
//        }
//    }
//
//    @Transactional(readOnly = true)
//    public List<TotalMouvement> findTotalMouvementSortie(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
//        if (coddep != null) {
//            if (fromDate == null) {
//                return mvtstoinvRepository.findTotalMouvementSortie(coddep, toDate);
//            } else {
//                return mvtstoinvRepository.findTotalMouvementSortie(coddep, fromDate, toDate);
//            }
//
//        } else {
//            if (fromDate == null) {
//                return mvtstoinvRepository.findTotalMouvementSortie(toDate);
//            } else {
//                return mvtstoinvRepository.findTotalMouvementSortie(fromDate, toDate);
//            }
//        }
//    }
//
//}
