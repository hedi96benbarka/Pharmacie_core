package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.dto.FcptfrsPHdto;
import java.math.BigDecimal;
import com.csys.pharmacie.achat.domain.FactureBA;
import com.csys.pharmacie.achat.domain.FcptfrsPH;

public class FcptfrsPHFactory {

    
    
    public static FcptfrsPH FcptfrsPHDTOTOFcptfrsPH(FcptfrsPHdto dto) {

        FcptfrsPH frs = new FcptfrsPH();
        frs.setCodFrs(dto.getCodFrs());
        frs.setLibOpr(dto.getLibOpr());
        frs.setReste(dto.getReste());
        frs.setNumBon(dto.getNumBon());
        frs.setNumBonAff(dto.getNumAffiche());
        frs.setNumFac(dto.getNumFac());
        frs.setNumfacture(dto.getNumFac());
        frs.setSens(dto.getSens());
        frs.setTypBon(dto.getTypBon());
        frs.setCodsup(dto.getCodsup());
        frs.setDebit(dto.getDebit());
        frs.setCredit(dto.getCredit());
        frs.setSolde(dto.getSolde());
        frs.setEtat(dto.getEtat());
        frs.setRetenu(dto.getRetenu());
        frs.setMntOP(dto.getMntOP());
        frs.setRetenuOP(dto.getRetenuOP());      
        frs.setNumReg(dto.getNumReg());
        frs.setNumRegAff(dto.getNumRegAff());
        frs.setDateOpr(dto.getDateOpr());
        frs.setMontantHT(dto.getMontantHT());
        frs.setMontantTVA(dto.getMontantTVA());
        
        return frs;
    }

    public static FcptfrsPHdto FcptfrsPHTOFcptfrsPHDTO(FcptfrsPH fcptFrsPH) {

        FcptfrsPHdto dto = new FcptfrsPHdto();
        dto.setNumOpr(fcptFrsPH.getNumOpr());
        dto.setCodFrs(fcptFrsPH.getCodFrs());
        dto.setLibOpr(fcptFrsPH.getLibOpr());
        dto.setReste(fcptFrsPH.getReste());
        dto.setNumBon(fcptFrsPH.getNumBon());
        dto.setNumAffiche(fcptFrsPH.getNumBonAff());
        dto.setNumFac(fcptFrsPH.getNumFac());
        dto.setNumfacture(fcptFrsPH.getNumFac());
        dto.setSens(fcptFrsPH.getSens());
        dto.setTypBon(fcptFrsPH.getTypBon());
        dto.setCodsup(fcptFrsPH.getCodsup());
        dto.setDebit(fcptFrsPH.getDebit());
        dto.setCredit(fcptFrsPH.getCredit());
        dto.setSolde(fcptFrsPH.getSolde());
        dto.setEtat(fcptFrsPH.getEtat());
        dto.setRetenu(fcptFrsPH.getRetenu());
        dto.setMntOP(fcptFrsPH.getMntOP());
        dto.setRetenuOP(fcptFrsPH.getRetenuOP());
        dto.setDeclar(fcptFrsPH.getDeclar());
        dto.setNumReg(fcptFrsPH.getNumReg());
        dto.setNumRegAff(fcptFrsPH.getNumRegAff());
        dto.setDateOpr(fcptFrsPH.getDateOpr());
        dto.setMontantHT(fcptFrsPH.getMontantHT());
        dto.setMontantTVA(fcptFrsPH.getMontantTVA());
        return dto;
    }

//    public FcptfrsPHdto fcptFrsDtoFromFactureAA(FactureAA facture) {
//        FcptfrsPHdto fcptFrsDto = new FcptfrsPHdto();
//        fcptFrsDto.setCodFrs(facture.getCodfrs());
////        fcptFrsDto.setDateopr(facture.getDatbon());
//        fcptFrsDto.setLibOpr("AVOIR/FN° " + facture.getNumaffiche());
//        fcptFrsDto.setMntttc(facture.getMntbon());
//        fcptFrsDto.setNumAffiche(facture.getNumaffiche());
//        fcptFrsDto.setNumBon(facture.getNumbon());
//        fcptFrsDto.setSens("2");
//        fcptFrsDto.setTypBon(facture.getTypbon());
//        fcptFrsDto.setDebit(facture.getMntbon());
//        fcptFrsDto.setCredit(BigDecimal.ZERO);
//
//        return fcptFrsDto;
//    }

    public static FcptfrsPH fcptFrsDtoFromFactureBA(FactureBA f) {
        FcptfrsPH fcpt = new FcptfrsPH();
        fcpt.setCodFrs(f.getCodfrs());
        fcpt.setLibOpr("BON DE LIVRAISON N° " + f.getNumaffiche());
        fcpt.setReste(f.getMntbon());
        fcpt.setNumBon(f.getNumbon());
        fcpt.setNumBonAff(f.getNumaffiche());
//        fcpt.setDateOpr(f.getDatesys());
        fcpt.setNumFac("");
        fcpt.setNumfacture("");
        fcpt.setSens("2");
        fcpt.setTypBon("BA");
        fcpt.setCodsup("0");
        fcpt.setDebit(BigDecimal.ZERO);
        fcpt.setSolde(BigDecimal.ZERO);
        fcpt.setEtat("0");
        fcpt.setRetenu("0");
        fcpt.setMntOP(BigDecimal.ZERO);
        fcpt.setRetenuOP(BigDecimal.ZERO);
        fcpt.setDeclar(false);
        fcpt.setNumReg("");
        fcpt.setNumRegAff("");
        fcpt.setDebit(BigDecimal.ZERO);
        fcpt.setCredit(f.getMntbon());

        return fcpt;
    }

}
