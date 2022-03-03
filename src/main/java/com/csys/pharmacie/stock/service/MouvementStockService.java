/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.service;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.stock.domain.MouvementStock;
import com.csys.pharmacie.stock.dto.MouvementStockDTO;
import com.csys.pharmacie.stock.dto.MouvementStockEditionDTO;
import com.csys.pharmacie.stock.repository.MouvementStockRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author khouloud
 */
@Service
public class MouvementStockService {

    private final MouvementStockRepository mouvementStockRepository;

    private final Logger log = LoggerFactory.getLogger(MouvementStockService.class);
    public MouvementStockService(MouvementStockRepository mouvementStockRepository) {
        this.mouvementStockRepository = mouvementStockRepository;
    }

// @Transactional(readOnly = true)
//       public List<MouvementStockEditionDTO> findMouvementStockByCategDepotAndCodeDepotAndListArticle(CategorieDepotEnum categDepot, Integer codeDepot, List<Integer> codeArticles, Date fromDate, Date toDate) {
//
//        List<MouvementStockEditionDTO> lists = new ArrayList<>();
//        if (codeArticles != null) {
//            Integer numberOfChunks = (int) Math.ceil((double) codeArticles.size() / 2000);
//            for (int i = 0; i < numberOfChunks; i++) {
//                List<Integer> codesChunk = codeArticles.subList(i * 2000, Math.min(i * 2000 + 2000, codeArticles.size()));
//                    List<MouvementStockEditionDTO> list = mouvementStockRepository.findMouvementStockByListCodeArticle(categDepot, codeDepot, codesChunk, fromDate, toDate);
//                        lists.addAll(list);
//            }
//        } else {
//                return mouvementStockRepository.findMouvementStock(categDepot, codeDepot, fromDate, toDate);
//        }
//
//        return lists;
//
//    }
       
    @Transactional(readOnly = true)
    public List<MouvementStockDTO> findMouvementStockByDateAndCategorieDepot(CategorieDepotEnum categDepot ,LocalDate fromDate, LocalDate toDate) {
      List<MouvementStockDTO> listeMouvements=  mouvementStockRepository.findByCategDepotAndDatbonBetweenGrouppedByCategDepotAndCodeArticleAndCodeUnite(categDepot, java.util.Date.from(fromDate.atStartOfDay().toInstant(ZoneOffset.UTC)),  java.util.Date.from(toDate.atStartOfDay().toInstant(ZoneOffset.UTC)));
        return listeMouvements;
    }

}
