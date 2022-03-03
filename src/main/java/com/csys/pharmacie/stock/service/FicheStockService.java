package com.csys.pharmacie.stock.service;

import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TotalMouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.helper.WhereClauseBuilder;
import com.csys.pharmacie.stock.domain.FicheStock;
import com.csys.pharmacie.stock.domain.QFicheStock;
import com.csys.pharmacie.stock.factory.FicheStockFactory;
import org.springframework.stereotype.Service;
import com.csys.pharmacie.stock.repository.FicheStockRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service("FicheStockService")
public class FicheStockService {

    private final Logger log = LoggerFactory.getLogger(FicheStockService.class);

    private final FicheStockRepository ficheStockRepository;

    public FicheStockService(FicheStockRepository ficheStockRepository) {
        this.ficheStockRepository = ficheStockRepository;
    }

    @Transactional(readOnly = true)
    public List<TotalMouvement> findTotalMouvement(Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null) {
            return ficheStockRepository.findTotalMouvement(coddep, codart, toDate);
        } else {
            return ficheStockRepository.findTotalMouvement(coddep, codart, fromDate, toDate);
        }
    }

    @Transactional(readOnly = true)
    public List<Mouvement> findListMouvement(CategorieDepotEnum categ, Integer codart, Integer coddep, LocalDateTime fromDate, LocalDateTime toDate, TypeDateEnum typeDate, DepotDTO depot) {
        QFicheStock _ficheStock = QFicheStock.ficheStock;
        WhereClauseBuilder builder = new WhereClauseBuilder().and(_ficheStock.categDepot.eq(categ))
                .optionalAnd(codart, () -> _ficheStock.codart.eq(codart))
                .optionalAnd(coddep, () -> _ficheStock.coddep.eq(coddep))
                .optionalAnd(fromDate, () -> _ficheStock.datbon.goe(fromDate))
                .optionalAnd(toDate, () -> _ficheStock.datbon.loe(toDate));
        List<FicheStock> list = (List<FicheStock>) ficheStockRepository.findAll(builder);
        List<Mouvement> mouvements = new ArrayList<>();
        list.forEach((mouvement) -> {
            mouvements.add(FicheStockFactory.toMouvement(mouvement, typeDate, depot));
        });
        return mouvements;
    }

}
