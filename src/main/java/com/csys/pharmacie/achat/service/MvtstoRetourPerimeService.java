package com.csys.pharmacie.achat.service;

import com.csys.pharmacie.client.service.ParamAchatServiceClient;
import com.csys.pharmacie.achat.domain.MvtstoRetourPerime;
import com.csys.pharmacie.achat.domain.QMvtstoRetourPerime;
import com.csys.pharmacie.achat.dto.DetailMvtStoRetourPerimeDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoRetourPerimeDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.achat.factory.MvtstoRetourPerimeFactory;
import com.csys.pharmacie.achat.repository.MvtstoRetourPerimeRepository;
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
 * Service Implementation for managing MvtstoRetour_perime.
 */
@Service
@Transactional
public class MvtstoRetourPerimeService {

    private final Logger log = LoggerFactory.getLogger(MvtstoRetourPerimeService.class);

    private final MvtstoRetourPerimeRepository mvtstoretourPerimeRepository;
    private final ParamAchatServiceClient paramAchatServiceClient;
    private final MvtstoRetourPerimeFactory mvtstoRetourPerimeFactory;

    public MvtstoRetourPerimeService(MvtstoRetourPerimeRepository mvtstoretourPerimeRepository, ParamAchatServiceClient paramAchatServiceClient, MvtstoRetourPerimeFactory mvtstoRetourPerimeFactory) {
        this.mvtstoretourPerimeRepository = mvtstoretourPerimeRepository;
        this.paramAchatServiceClient = paramAchatServiceClient;
        this.mvtstoRetourPerimeFactory = mvtstoRetourPerimeFactory;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return mvtstoretourPerimeRepository.findTotalMouvement(coddep, codart, toDate);
        } else {
            return mvtstoretourPerimeRepository.findTotalMouvement(coddep, codart, fromDate, toDate);
        }
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (coddep != null) {
            if (fromDate == null) {
                return mvtstoretourPerimeRepository.findTotalMouvement(coddep, toDate);
            } else {
                return mvtstoretourPerimeRepository.findTotalMouvement(coddep, fromDate, toDate);
            }

        } else {
            if (fromDate == null) {
                return mvtstoretourPerimeRepository.findTotalMouvement(toDate);
            } else {
                return mvtstoretourPerimeRepository.findTotalMouvement(fromDate, toDate);
            }
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
                    completableFuture[i] = mvtstoretourPerimeRepository.findTotalMouvement(coddep, codesChunk, toDate).whenComplete((list, exception) -> {
                        lists.addAll(list);
                    });
                } else {
                    completableFuture[i] = mvtstoretourPerimeRepository.findTotalMouvement(coddep, codesChunk, fromDate, toDate).whenComplete((articles, exception) -> {
                        lists.addAll(articles);
                    });
                }
            }
            CompletableFuture.allOf(completableFuture).join();
        }
        return lists;
    }

    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate) {
        QMvtstoRetourPerime _mvtsto = QMvtstoRetourPerime.mvtstoRetourPerime;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_mvtsto.factureRetourPerime().categDepot.eq(categ))
                .optionalAnd(codart, () -> _mvtsto.codart.eq(codart))
                .optionalAnd(coddep, () -> _mvtsto.factureRetourPerime().coddep.in(coddep))
                .optionalAnd(fromDate, () -> _mvtsto.factureRetourPerime().datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _mvtsto.factureRetourPerime().datbon.loe(toDate));
        List<MvtstoRetourPerime> list = (List<MvtstoRetourPerime>) mvtstoretourPerimeRepository.findAll(builder);
        List<Mouvement> mouvements = new ArrayList<>();
        List<String> codesFrs = list.stream().map(item -> item.getFactureRetourPerime().getCodFrs()).distinct().collect(Collectors.toList());
        List<FournisseurDTO> fournisseurs = paramAchatServiceClient.findFournisseurByListCode(codesFrs);
        List<Integer> codeUnites = list.stream().map(item -> item.getUnite()).distinct().collect(Collectors.toList());
        List<UniteDTO> listUnite = paramAchatServiceClient.findUnitsByCodes(codeUnites);
        list.forEach((mouvement) -> {
            FournisseurDTO fournisseur = fournisseurs.stream().filter(x -> x.getCode().equals(mouvement.getFactureRetourPerime().getCodFrs())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(fournisseur != null, "missing-frs");
            UniteDTO unite = listUnite.stream().filter(x -> x.getCode().equals(mouvement.getUnite())).findFirst().orElse(null);
            com.csys.util.Preconditions.checkBusinessLogique(unite != null, "missing-unity");
            log.debug("mvtsto ba mouvement {}", mouvement);
            mouvements.addAll(mvtstoRetourPerimeFactory.toMouvement(mouvement, fournisseur, unite, typeDate));
        });
        return mouvements;
    }

        public List<MvtstoRetourPerimeDTO> getDetailsRetourPerime(List<String> numBonRetourPerimes) {
        List<MvtstoRetourPerime> mvtstoRetourPerimes = mvtstoretourPerimeRepository.findByNumbonIn(numBonRetourPerimes);
        return MvtstoRetourPerimeFactory.mvtstoRetourPerimesToMvtstoRetourPerimeDTOs(mvtstoRetourPerimes);
    }
}
