package com.csys.pharmacie.vente.quittance.factory;

import com.csys.pharmacie.vente.quittance.domain.FactureDR;
import com.csys.pharmacie.vente.quittance.domain.MvtstoDR;
import com.csys.pharmacie.vente.quittance.dto.DemandeArticlesDto;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDRDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MvtstoDRFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    public static DemandeArticlesDto mvtstodrToDemandeArticlesDto(MvtstoDR mvtstodr, FactureDR facturedr) {
        DemandeArticlesDto mvtstodrDTO = new DemandeArticlesDto();
        mvtstodrDTO.setCode(mvtstodr.getMvtstoDRPK().getCodart());
        mvtstodrDTO.setNumbon(mvtstodr.getMvtstoDRPK().getNumbon());
        mvtstodrDTO.setNumordre(mvtstodr.getMvtstoDRPK().getNumordre());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstodrDTO.setDesignation(mvtstodr.getDesartSec());
            mvtstodrDTO.setDesignationSec(mvtstodr.getDesart());

        } else {
            mvtstodrDTO.setDesignation(mvtstodr.getDesart());
            mvtstodrDTO.setDesignationSec(mvtstodr.getDesartSec());
        }
        mvtstodrDTO.setCodeSaisi(mvtstodr.getCodeSaisi());
        mvtstodrDTO.setCodeunite(mvtstodr.getCodeUnite());
        mvtstodrDTO.setQuantiteDemande(mvtstodr.getQuantite());
        mvtstodrDTO.setQuantiteRestante(mvtstodr.getQteben());
        mvtstodrDTO.setQuantiteFacture(mvtstodr.getQuantite().subtract(mvtstodr.getQteben()));
        mvtstodrDTO.setPerissable(mvtstodr.getPerissable());
        mvtstodrDTO.setCodeSaisi(mvtstodr.getCodeSaisi());
        mvtstodrDTO.setaRemplacer(mvtstodr.isaRemplacer());
        mvtstodrDTO.setMemoart(mvtstodr.getMemoart());
        mvtstodrDTO.setNumdoss(mvtstodr.getNumdoss());
        mvtstodrDTO.setMedtrait(mvtstodr.getMedtrait());
           mvtstodrDTO.setCodvend(mvtstodr.getFactureDR().getCodvend());
        mvtstodrDTO.setCoddep(mvtstodr.getCoddep());
        return mvtstodrDTO;
    }

    public static List<DemandeArticlesDto> mvtstodrToDemandeArticlesDtos(FactureDR facturedr) {
        List<DemandeArticlesDto> mvtstodrsDTO = new ArrayList<>();
        facturedr.getDetailFactureCollection().forEach(x -> {
            mvtstodrsDTO.add(mvtstodrToDemandeArticlesDto(x, facturedr));
        });
        return mvtstodrsDTO;
    }
    
   public static MvtstoDRDTO mvtstodrToMvtstodrDto(MvtstoDR mvtstodr) {
        MvtstoDRDTO mvtstodrDTO = new MvtstoDRDTO();
        mvtstodrDTO.setNumordre(mvtstodr.getMvtstoDRPK().getNumordre());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstodrDTO.setDesart(mvtstodr.getDesartSec());
            mvtstodrDTO.setDesartSec(mvtstodr.getDesart());

        } else {
            mvtstodrDTO.setDesart(mvtstodr.getDesart());
            mvtstodrDTO.setDesartSec(mvtstodr.getDesartSec());
        }
        mvtstodrDTO.setCodart(mvtstodr.getMvtstoDRPK().getCodart());
        mvtstodrDTO.setCodeSaisi(mvtstodr.getCodeSaisi());
        mvtstodrDTO.setCodeUnite(mvtstodr.getCodeUnite());
        mvtstodrDTO.setQuantite(mvtstodr.getQuantite());
        mvtstodrDTO.setQteben(mvtstodr.getQteben());
        mvtstodrDTO.setPerissable(mvtstodr.getPerissable());
        mvtstodrDTO.setaRemplacer(mvtstodr.isaRemplacer());
        mvtstodrDTO.setMemoart(mvtstodr.getMemoart());
        mvtstodrDTO.setMedtrait(mvtstodr.getMedtrait());
        mvtstodrDTO.setDatbon(mvtstodr.getDatbon());
        mvtstodrDTO.setCodvend(mvtstodr.getFactureDR().getCodvend());
        mvtstodrDTO.setNumaffiche(mvtstodr.getNumaffiche());
        
        return mvtstodrDTO;
    }
    
      public static List<MvtstoDRDTO> mvtstodrsToMvtstodrDtos(List<MvtstoDR> listMvtstodr) {
        List<MvtstoDRDTO> mvtstodrsDTO = new ArrayList<>();
        listMvtstodr.forEach(x -> {
            mvtstodrsDTO.add(mvtstodrToMvtstodrDto(x));
        });
        return mvtstodrsDTO;
    }
}
