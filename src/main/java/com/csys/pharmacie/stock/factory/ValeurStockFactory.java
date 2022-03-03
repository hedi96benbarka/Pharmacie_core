/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.stock.domain.ValeurStock;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author khouloud
 */
@Component
public class ValeurStockFactory {

    @Autowired
     MessageSource messages;


    public Mouvement toMouvement(ValeurStock entity, DepotDTO depot, ArticleDTO article, UniteDTO unite) {   
    Locale loc = LocaleContextHolder.getLocale();
        if (entity == null) {
            return null;
        }
        Mouvement dto = new Mouvement();
        dto.setId(entity.getValeurStockPK().toString());
        dto.setDate(Date.from(entity.getValeurStockPK().getDatesys().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        dto.setDesignation(article.getDesignation());
        dto.setCodeSaisi(article.getCodeSaisi());
        dto.setNumaffiche("");
        dto.setCoddep(entity.getCoddep());
        dto.setDesignationDepot(depot.getDesignation());
        dto.setLibelle("");
        dto.setOperation(messages.getMessage("solde-depart", null, loc));
        dto.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dto.setEntree(entity.getQte());
        dto.setSortie(BigDecimal.ZERO);
        dto.setLotinter(entity.getLotInter());
        dto.setPrix(entity.getPu());
        
                                                                            
        dto.setValeur((entity.getPu().multiply(entity.getTauxTvaAchat().add(new BigDecimal(100))).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP))
                                                                    .multiply(entity.getQte()));
        dto.setCodeUnite(entity.getUnite());
        dto.setDesignationUnite(unite.getDesignation());

        return dto;

    }
}
