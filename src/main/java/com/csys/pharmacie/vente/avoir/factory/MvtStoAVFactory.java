package com.csys.pharmacie.vente.avoir.factory;

import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.vente.avoir.domain.DetailMvtstoAV;
import com.csys.pharmacie.vente.avoir.domain.MvtStoAV;
import com.csys.pharmacie.vente.avoir.domain.MvtStoAVPK;
import com.csys.pharmacie.vente.avoir.dto.MvtStoAVDTO;
import com.csys.pharmacie.vente.quittance.domain.DetailMvtsto;
import com.csys.pharmacie.vente.quittance.dto.AdmissionDemandePECDTO;
import com.csys.pharmacie.vente.quittance.dto.MvtstoDTO;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MvtStoAVFactory {
    
    @Autowired
    MessageSource messages;
    
    static String LANGUAGE_SEC;
    
    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }
    
    public static MvtStoAVDTO mvtstoavToMvtStoAVDTO(MvtStoAV mvtstoav) {
        MvtStoAVDTO mvtstoavDTO = new MvtStoAVDTO();
        mvtstoavDTO.setNumbon(mvtstoav.getMvtStoAVPK().getNumbon());
        mvtstoavDTO.setNumordre(mvtstoav.getMvtStoAVPK().getNumordre());
        mvtstoavDTO.setCodart(mvtstoav.getMvtStoAVPK().getCodart());
        mvtstoavDTO.setTypbon(mvtstoav.getTypbon());
        mvtstoavDTO.setPriuni(mvtstoav.getPriuni());
        mvtstoavDTO.setMontht(mvtstoav.getMontht());
        mvtstoavDTO.setTautva(mvtstoav.getTautva());
        mvtstoavDTO.setPriach(mvtstoav.getPriach());
        mvtstoavDTO.setMemoart(mvtstoav.getMemoart());
        mvtstoavDTO.setCodtva(mvtstoav.getCodtva());
        mvtstoavDTO.setDateSysteme(mvtstoav.getDateSysteme());
        mvtstoavDTO.setHeureSysteme(mvtstoav.getHeureSysteme());
        mvtstoavDTO.setUnityCode(mvtstoav.getUnite());
        mvtstoavDTO.setLot(mvtstoav.getLotInter());
        mvtstoavDTO.setDatPer(mvtstoav.getDatPer());
        mvtstoavDTO.setCodeSociete(mvtstoav.getFactureAV().getCodeSociete());
        mvtstoavDTO.setRemiseConventionnellePharmacie(mvtstoav.getFactureAV().getRemiseConventionnellePharmacie());
        if (mvtstoav.getDatPer() != null) {
            mvtstoavDTO.setDatPerDate(Date.from(mvtstoav.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        mvtstoavDTO.setCategDepot(mvtstoav.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstoavDTO.setDesart(mvtstoav.getDesArtSec());
            mvtstoavDTO.setDesArtSec(mvtstoav.getDesart());
        } else {
            mvtstoavDTO.setDesart(mvtstoav.getDesart());
            mvtstoavDTO.setDesArtSec(mvtstoav.getDesArtSec());
        }
        mvtstoavDTO.setCodeSaisi(mvtstoav.getCodeSaisi());
        mvtstoavDTO.setQuantite(mvtstoav.getQuantite());
        if (mvtstoav.getFactureAV() != null) {
            mvtstoavDTO.setNumaffiche(mvtstoav.getFactureAV().getNumaffiche());
            mvtstoavDTO.setDatbon(mvtstoav.getFactureAV().getDatbon());
            mvtstoavDTO.setCodvend(mvtstoav.getFactureAV().getCodvend());
        }
        
        mvtstoavDTO.setAjustement(mvtstoav.getAjustement());
        mvtstoavDTO.setPrixBrute(mvtstoav.getPrixBrute());
        mvtstoavDTO.setTauxCouverture(mvtstoav.getTauxCouverture());
        
        return mvtstoavDTO;
    }
    
    public static MvtStoAV mvtstoavDTOToMvtStoAV(MvtStoAVDTO mvtstoavDTO) {
        MvtStoAV mvtstoav = new MvtStoAV();
        MvtStoAVPK mvtStoAVPK = new MvtStoAVPK();
        mvtStoAVPK.setNumbon(mvtstoavDTO.getNumbon());
        mvtStoAVPK.setNumordre(mvtstoavDTO.getNumordre());
        mvtStoAVPK.setCodart(mvtstoavDTO.getCodart());
        mvtstoav.setMvtStoAVPK(mvtStoAVPK);
        mvtstoav.setTypbon(mvtstoavDTO.getTypbon());
        mvtstoav.setPriuni(mvtstoavDTO.getPriuni());
        mvtstoav.setMontht(mvtstoavDTO.getMontht());
        mvtstoav.setTautva(mvtstoavDTO.getTautva());
        mvtstoav.setPriach(mvtstoavDTO.getPriach());
        mvtstoav.setLotInter(mvtstoavDTO.getLot());
        mvtstoav.setDatPer(mvtstoavDTO.getDatPer());
        mvtstoav.setMemoart(mvtstoavDTO.getMemoart());
        mvtstoav.setCodtva(mvtstoavDTO.getCodtva());
        mvtstoav.setDateSysteme(mvtstoavDTO.getDateSysteme());
        mvtstoav.setHeureSysteme(mvtstoavDTO.getHeureSysteme());
        mvtstoav.setUnite(mvtstoavDTO.getUnityCode());
        mvtstoav.setCategDepot(mvtstoavDTO.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstoav.setDesart(mvtstoavDTO.getDesArtSec());
            mvtstoav.setDesArtSec(mvtstoavDTO.getDesart());
        } else {
            mvtstoav.setDesart(mvtstoavDTO.getDesart());
            mvtstoav.setDesArtSec(mvtstoavDTO.getDesArtSec());
        }
        mvtstoav.setCodeSaisi(mvtstoavDTO.getCodeSaisi());
        mvtstoav.setQuantite(mvtstoavDTO.getQuantite());
        mvtstoavDTO.setTauxCouverture(mvtstoav.getTauxCouverture());
        return mvtstoav;
    }
    
    public static List<MvtStoAVDTO> mvtstoavToMvtStoAVDTOs(List<MvtStoAV> mvtstoavs) {
        List<MvtStoAVDTO> mvtstoavsDTO = new ArrayList<>();
        mvtstoavs.forEach(x -> {
            mvtstoavsDTO.add(mvtstoavToMvtStoAVDTO(x));
        });
        return mvtstoavsDTO;
    }
    
    public List<Mouvement> toMouvement(MvtStoAV entity, UniteDTO unite, AdmissionDemandePECDTO client, TypeDateEnum typeDate) {
        if (entity == null) {
            return null;
        }
        Locale loc = LocaleContextHolder.getLocale();
        List<Mouvement> listMouvement = new ArrayList<>();
        Mouvement dto = new Mouvement();
        dto.setDate(Date.from(entity.getFactureAV().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesignation(entity.getDesArtSec());
        } else {
            dto.setDesignation(entity.getDesart());
        }
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setOperation(messages.getMessage("ficheStock.avoirClient", null, loc));
        dto.setNumbon(entity.getFactureAV().getNumbon());
        dto.setNumaffiche(entity.getFactureAV().getNumaffiche());
        dto.setLibelle(client.getCode() + " " + client.getNomCompletAr());
        dto.setCoddep(entity.getFactureAV().getCoddep());
        
        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            if (entity.getDatPer() != null) {
                dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            dtoEntreSortie.setLotinter(entity.getLotInter());
            dtoEntreSortie.setNumbon(entity.getFactureAV().getNumbon());
            dtoEntreSortie.setEntree(entity.getQuantite());
            dtoEntreSortie.setSortie(BigDecimal.ZERO);
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            dtoEntreSortie.setNumbon(entity.getFactureAV().getNumbon());
            dtoEntreSortie.setEntree(entity.getQuantite());
            dtoEntreSortie.setSortie(BigDecimal.ZERO);
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            for (DetailMvtstoAV detailMvtstoAV : entity.getDetailMvtstoAVCollection()) {
                EntreSortie dtoEntreSortie = new EntreSortie();
                if (detailMvtstoAV.getDepsto().getDatPer() != null) {
                    dtoEntreSortie.setDatPer(Date.from(detailMvtstoAV.getDepsto().getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                dtoEntreSortie.setLotinter(detailMvtstoAV.getDepsto().getLotInter());
                dtoEntreSortie.setNumbon(entity.getFactureAV().getNumbon());
                dtoEntreSortie.setEntree(detailMvtstoAV.getQte());
                dtoEntreSortie.setSortie(BigDecimal.ZERO);
                dtoEntreSortie.setPrix(entity.getPriuni());
                dtoEntreSortie.setCodeUnite(entity.getUnite());
                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                dtoEntreSorties.add(dtoEntreSortie);
            }
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        }
        return listMouvement;
        
    }
    
    public static MvtstoDTO mvtstoAVToMvtstoDTO(MvtStoAV mvtsto) {
        MvtstoDTO mvtstoDTO = new MvtstoDTO();
        mvtstoDTO.setCodart(mvtsto.getMvtStoAVPK().getCodart());
        mvtstoDTO.setNumbon(mvtsto.getMvtStoAVPK().getNumbon());
        mvtstoDTO.setNumordre(mvtsto.getMvtStoAVPK().getNumordre());
        mvtstoDTO.setNumdoss(mvtsto.getFactureAV().getNumdoss());
        mvtstoDTO.setPriuni(mvtsto.getPriuni());
        mvtstoDTO.setRemise(mvtsto.getRemise());
        mvtstoDTO.setMajoration(mvtsto.getMajoration());
        mvtstoDTO.setMontht(mvtsto.getMontht());
        mvtstoDTO.setTautva(mvtsto.getTautva());
        mvtstoDTO.setPriach(mvtsto.getPriach());
        mvtstoDTO.setUnityCode(mvtsto.getUnite());
        mvtstoDTO.setCategDepot(mvtsto.getCategDepot());
        mvtstoDTO.setLotInter(mvtsto.getLotInter());
        mvtstoDTO.setCodeSociete(mvtsto.getFactureAV().getCodeSociete());
        mvtstoDTO.setRemiseConventionnellePharmacie(mvtsto.getFactureAV().getRemiseConventionnellePharmacie());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            
            mvtstoDTO.setDesart(mvtsto.getDesArtSec());
            mvtstoDTO.setDesArtSec(mvtsto.getDesart());
            
        } else {
            mvtstoDTO.setDesart(mvtsto.getDesart());
            mvtstoDTO.setDesArtSec(mvtsto.getDesArtSec());
        }
        
        mvtstoDTO.setCodeSaisi(mvtsto.getCodeSaisi());
        mvtstoDTO.setQuantite(mvtsto.getQuantite());
        mvtstoDTO.setCodtva(mvtsto.getCodtva());
        mvtstoDTO.setNumaffiche(mvtsto.getFactureAV().getNumaffiche());
        mvtstoDTO.setDatbon(mvtsto.getFactureAV().getDatbon());
        mvtstoDTO.setCodvend(mvtsto.getFactureAV().getCodvend());
        
        mvtstoDTO.setAjustement(mvtsto.getAjustement());
        mvtstoDTO.setPrixBrute(mvtsto.getPrixBrute());
        mvtstoDTO.setTauxCouverture(mvtsto.getTauxCouverture());
        mvtstoDTO.setTypbon(mvtsto.getFactureAV().getTypbon());
        return mvtstoDTO;
    }
    
    public static List<MvtstoDTO> mvtstoAVToMvtstoDTOs(List<MvtStoAV> mvtstos) {
        List<MvtstoDTO> mvtstosDTO = new ArrayList<>();
        mvtstos.forEach(x -> {
            mvtstosDTO.add(mvtstoAVToMvtstoDTO(x));
        });
        return mvtstosDTO;
    }
}
