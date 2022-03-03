/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.dto.DetailEditionDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Farouk
 */
//@Component
public class DetailReceptionFactory extends MvtstoBAFactory {

//    static String LANGUAGE_SEC;
//    @Value("${lang.secondary}")
//    public void setLanguage(String db) {
//        LANGUAGE_SEC = db;
//    }
//    public static DetailReceptionDTO toDTO(MvtStoBA entity) {
//
//        DetailReceptionDTO dto = new DetailReceptionDTO();
//        toDTO(entity, dto); // FACtory of MvtstoBaDTO;
//        dto.setQteReturned(entity.getQuantite().subtract(entity.getQtecom()));
//        dto.setIsPrixRef(entity.getIsPrixReference());
//        dto.setCodeEmplacement(entity.getCodeEmplacement());
//        dto.setQteCom(entity.getQtecom());
//        dto.setIsCapitalize(entity.getIsCapitalize());
//
//        return dto;
//    }
    public static DetailEditionDTO toEditionDTO(MvtStoBA entity) {
//        if (entity == null) {
//            return null;
//        }
        DetailEditionDTO dto = new DetailEditionDTO();
        toDTO(dto, entity);
        dto.setNumbon(entity.getFactureBA().getNumbon());

        dto.setRefArt(entity.getCodart());

        dto.setLotInter(entity.getLotInter());
        dto.setNumOrdre(entity.getPk().getNumordre());

        dto.setRemise(entity.getRemise());
        dto.setPriuni(entity.getPriuni());
        dto.setCodTVA(entity.getCodtva());
        dto.setTauTVA(entity.getTautva());
        dto.setMontht(entity.getMontht());

        dto.setQteReturned(entity.getQuantite().subtract(entity.getQtecom()));

        dto.setIsPrixRef(entity.getIsPrixReference());
        dto.setCodeUnite(entity.getCodeUnite());
        dto.setSellingPrice(entity.getPrixVente());
        dto.setBaseTva(entity.getBaseTva());

        BigDecimal baseTvaGratuite = BigDecimal.ZERO;
        if (entity.getPriuni().compareTo(BigDecimal.ZERO) == 0) {

            dto.setFree(true);
            if (entity.getBaseTva().compareTo(BigDecimal.ZERO) == 1) {

                baseTvaGratuite = entity.getBaseTva().multiply(entity.getQuantite()).setScale(7, RoundingMode.HALF_UP);
            }
        } else if (entity.getPriuni().compareTo(BigDecimal.ZERO) == 1) {

            dto.setFree(false);
        }
        if (entity.getDatPer() != null) {
            dto.setDatePerEdition(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        dto.setMontantTvaGratuite(baseTvaGratuite.multiply(entity.getTautva()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
        return dto;

    }

}
