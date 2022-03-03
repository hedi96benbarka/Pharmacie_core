/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.service;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.stock.domain.QValeurStock;
import com.csys.pharmacie.stock.domain.ValeurStock;
import com.csys.pharmacie.stock.repository.ValeurStockRepository;
import edu.emory.mathcs.backport.java.util.Arrays;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
public class ValeurStockService {

    private final Logger log = LoggerFactory.getLogger(ValeurStockService.class);

    private final ValeurStockRepository valeurStockRepository;

    public ValeurStockService(ValeurStockRepository valeurStockRepository) {
        this.valeurStockRepository = valeurStockRepository;
    }

    @Transactional(readOnly = true)
    public List<ValeurStock> findAll(CategorieDepotEnum categ, LocalDate date, List<Integer> codarts) {
        log.debug("findAll ValeurStock size codarts: {} ", codarts.size());
        List<ValeurStock> result = new ArrayList<>();

        if (codarts != null && !codarts.isEmpty()) {
            Integer numberOfChunks = (int) Math.ceil((double) codarts.size() / 2000);
            for (int i = 0; i < numberOfChunks; i++) {
                List<Integer> codesChunk = codarts.subList(i * 2000, Math.min(i * 2000 + 2000, codarts.size()));
                QValeurStock _valeurStock = QValeurStock.valeurStock;
                WhereClauseBuilder builder = new WhereClauseBuilder()
                        .and(_valeurStock.categDepot.eq(categ))
                        .booleanAnd(codesChunk != null, () -> _valeurStock.codart.in(codesChunk))
                        .and(_valeurStock.qte.gt(BigDecimal.ZERO))
                        .and(_valeurStock.valeurStockPK().datesys.eq(date));
                log.error("codesChunk ValeurStock :{}", codesChunk);
                List<ValeurStock> listValeurStock = (List<ValeurStock>) valeurStockRepository.findAll(builder);
                result.addAll(listValeurStock);
            }
        } else {
            QValeurStock _valeurStock = QValeurStock.valeurStock;
            WhereClauseBuilder builder = new WhereClauseBuilder()
                    .and(_valeurStock.categDepot.eq(categ))
                    .and(_valeurStock.qte.gt(BigDecimal.ZERO))
                    .and(_valeurStock.valeurStockPK().datesys.eq(date));
            result = (List<ValeurStock>) valeurStockRepository.findAll(builder);
        }
        return result;
    }

}
