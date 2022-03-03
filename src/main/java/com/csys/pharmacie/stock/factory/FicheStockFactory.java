/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.helper.BaseDetailBonFactory;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.stock.domain.FicheStock;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Administrateur
 */
@Component
public class FicheStockFactory extends BaseDetailBonFactory {

    public static Mouvement toMouvement(FicheStock entity, TypeDateEnum typeDate,DepotDTO depot ) {
        if (entity == null) {
            return null;
        }
        Mouvement dto = new Mouvement();
        dto.setId(entity.getId());
        dto.setDate(Date.from(entity.getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        dto.setDesignation(entity.getDesart());
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setNumaffiche(entity.getNumaffiche());
        dto.setNumbon(entity.getNumbon());
        dto.setCoddep(entity.getCoddep());
        dto.setLibelle(entity.getLibelle());
        dto.setOperation(entity.getDesignationMvt());
        dto.setTypbon(entity.getTypbon());
        dto.setDesignationDepot(depot.getDesignation()); 
        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO)) {
            dto.setDesignationUnite(entity.getDesUnite());
            if (entity.getDatperMvt() != null) {
                dto.setDatPer(Date.from(entity.getDatperMvt().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            if (entity.getQuantite().compareTo(BigDecimal.ZERO) > 0) {
                dto.setEntree(entity.getQuantite());
                dto.setSortie(BigDecimal.ZERO);
            } else {
                dto.setEntree(BigDecimal.ZERO);
                dto.setSortie(entity.getQuantite().negate());
            }
            dto.setLotinter(entity.getLotMvt());
            dto.setPrix(entity.getPriuni());
            dto.setValeur(entity.getValeur());
            dto.setCodeUnite(entity.getCodeUnite());
        } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
            dto.setDesignationUnite(entity.getDesUnite());
            if (entity.getQuantite().compareTo(BigDecimal.ZERO) > 0) {
                dto.setEntree(entity.getQuantite());
                dto.setSortie(BigDecimal.ZERO);
            } else {
                dto.setEntree(BigDecimal.ZERO);
                dto.setSortie(entity.getQuantite().negate());
            }
            dto.setPrix(entity.getPriuni());
            dto.setValeur(entity.getValeur());
            dto.setCodeUnite(entity.getCodeUnite());
        } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
            dto.setDesignationUnite(entity.getDesUnite());
            if (entity.getDatdepsto() != null) {
                dto.setDatPer(Date.from(entity.getDatdepsto().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            dto.setLotinter(entity.getLotdepsto());
            if (entity.getQuantite().compareTo(BigDecimal.ZERO) > 0) {
                dto.setEntree(entity.getQuantite());
                dto.setSortie(BigDecimal.ZERO);
            } else {
                dto.setEntree(BigDecimal.ZERO);
                dto.setSortie(entity.getQuantite().negate());
            }
            dto.setPrix(entity.getPriuni());
            dto.setValeur(entity.getValeur());
            dto.setCodeUnite(entity.getCodeUnite());

        }
        return dto;

    }

}
