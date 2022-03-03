//package com.csys.pharmacie.inventaire.factory;
//
//import com.csys.pharmacie.inventaire.domain.MvtStoINV;
//import com.csys.pharmacie.inventaire.dto.MvtStoINVDTO;
//import com.csys.pharmacie.inventaire.domain.Inventaire;
//import com.csys.pharmacie.stock.dto.DepstoDTO;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Locale;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.MessageSource;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MvtStoINVFactory {
//
//    static String LANGUAGE_SEC;
//
//    @Value("${lang.secondary}")
//    public void setLanguage(String db) {
//        LANGUAGE_SEC = db;
//    }
//    @Autowired
//    MessageSource messages;
//
//    public static MvtStoINVDTO mvtstoinvToMvtStoINVDTO(MvtStoINV mvtstoinv) {
//        MvtStoINVDTO mvtstoinvDTO = new MvtStoINVDTO();
//        mvtstoinvDTO.setNum(mvtstoinv.getNum());
//        mvtstoinvDTO.setCodart(mvtstoinv.getCodart());
//        mvtstoinvDTO.setNumbon(mvtstoinv.getNumbon());
//        mvtstoinvDTO.setCodInventaire(mvtstoinv.getCodInventaire());
//        mvtstoinvDTO.setCoddep(mvtstoinv.getCoddep());
//        mvtstoinvDTO.setTypbon(mvtstoinv.getTypbon());
//        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
//            mvtstoinvDTO.setDesart(mvtstoinv.getDesartSec());
//            mvtstoinvDTO.setDesartSec(mvtstoinv.getDesart());
//        } else {
//            mvtstoinvDTO.setDesart(mvtstoinv.getDesart());
//            mvtstoinvDTO.setDesartSec(mvtstoinv.getDesartSec());
//        }
//        mvtstoinvDTO.setQuantite(mvtstoinv.getQuantite());
//        mvtstoinvDTO.setPriuni(mvtstoinv.getPriuni());
//        mvtstoinvDTO.setDatPer(mvtstoinv.getDatPer());
//        mvtstoinvDTO.setHeureSysteme(mvtstoinv.getHeureSysteme());
//        mvtstoinvDTO.setLotInter(mvtstoinv.getLotInter());
//        mvtstoinvDTO.setNumeroAMC(mvtstoinv.getNumeroAMC());
//
//        mvtstoinvDTO.setCategDepot(mvtstoinv.getCategDepot());
//        mvtstoinvDTO.setIsPrixReference(mvtstoinv.getIsPrixReference());
//        mvtstoinvDTO.setCodeSaisi(mvtstoinv.getCodeSaisi());
//        return mvtstoinvDTO;
//    }
//
//    public static MvtStoINV mvtstoinvDTOToMvtStoINV(MvtStoINVDTO mvtstoinvDTO) {
//        MvtStoINV mvtstoinv = new MvtStoINV();
//        mvtstoinv.setNum(mvtstoinvDTO.getNum());
//        mvtstoinv.setCodart(mvtstoinvDTO.getCodart());
//        mvtstoinv.setNumbon(mvtstoinvDTO.getNumbon());
//        mvtstoinv.setCodInventaire(mvtstoinvDTO.getCodInventaire());
//        mvtstoinv.setCoddep(mvtstoinvDTO.getCoddep());
//        mvtstoinv.setTypbon(mvtstoinvDTO.getTypbon());
//        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
//            mvtstoinv.setDesart(mvtstoinvDTO.getDesartSec());
//            mvtstoinv.setDesartSec(mvtstoinvDTO.getDesart());
//        } else {
//            mvtstoinv.setDesart(mvtstoinvDTO.getDesart());
//            mvtstoinv.setDesartSec(mvtstoinvDTO.getDesartSec());
//        }
//        mvtstoinv.setQuantite(mvtstoinvDTO.getQuantite());
//        mvtstoinv.setPriuni(mvtstoinvDTO.getPriuni());
//        mvtstoinv.setDatPer(mvtstoinvDTO.getDatPer());
//        mvtstoinv.setHeureSysteme(mvtstoinvDTO.getHeureSysteme());
//        mvtstoinv.setLotInter(mvtstoinvDTO.getLotInter());
//        mvtstoinv.setNumeroAMC(mvtstoinvDTO.getNumeroAMC());
//
//        mvtstoinv.setCategDepot(mvtstoinvDTO.getCategDepot());
//        mvtstoinv.setIsPrixReference(mvtstoinvDTO.getIsPrixReference());
//        mvtstoinv.setCodeSaisi(mvtstoinvDTO.getCodeSaisi());
//        return mvtstoinv;
//    }
//
//    public static MvtStoINV depstoDTOToMvtStoINV(DepstoDTO d, Inventaire inventaire) {
//        MvtStoINV mvtstoInv = new MvtStoINV();
//        mvtstoInv.setCodart(d.getArticleID());
//        mvtstoInv.setNumbon(d.getNumBon());
//        mvtstoInv.setCodInventaire(inventaire.getCode());
//        mvtstoInv.setCoddep(d.getCodeDepot());
//        mvtstoInv.setTypbon("IV");
//        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
//            mvtstoInv.setDesart(d.getDesignationSec());
//            mvtstoInv.setDesartSec(d.getDesignation());
//        } else {
//            mvtstoInv.setDesart(d.getDesignation());
//            mvtstoInv.setDesartSec(d.getDesignationSec());
//        }
//        mvtstoInv.setQuantite(d.getQuantiteReel().subtract(d.getQuantiteTheorique()));
//        mvtstoInv.setPriuni(d.getSommeValReel().subtract(d.getSommeValtheorique()));
//        mvtstoInv.setDatPer(d.getPreemptionDate());
//        mvtstoInv.setHeureSysteme(LocalDateTime.ofInstant(inventaire.getDateCloture().toInstant(), ZoneId.systemDefault()));
//        mvtstoInv.setLotInter("");
//        mvtstoInv.setNumeroAMC("");
//
//        mvtstoInv.setCategDepot(d.getCategorieDepot());
//        mvtstoInv.setIsPrixReference(Boolean.TRUE);
//        mvtstoInv.setCodeSaisi(d.getCodeSaisiArticle());
//        mvtstoInv.setUnite(d.getUnityCode());
//        return mvtstoInv;
//    }
//
//    public static Collection<MvtStoINVDTO> mvtstoinvToMvtStoINVDTOs(Collection<MvtStoINV> mvtstoinvs) {
//        List<MvtStoINVDTO> mvtstoinvsDTO = new ArrayList<>();
//        mvtstoinvs.forEach(x -> {
//            mvtstoinvsDTO.add(mvtstoinvToMvtStoINVDTO(x));
//        });
//        return mvtstoinvsDTO;
//    }
//
//}
