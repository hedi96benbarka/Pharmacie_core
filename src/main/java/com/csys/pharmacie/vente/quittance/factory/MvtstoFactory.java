package com.csys.pharmacie.vente.quittance.factory;

import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.MouvementConsomation;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.vente.quittance.domain.DetailMvtsto;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import com.csys.pharmacie.vente.quittance.domain.MvtstoPK;
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
public class MvtstoFactory {

    @Autowired
    MessageSource messages;

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    public static MvtstoDTO mvtstoToMvtstoDTO(Mvtsto mvtsto) {
        MvtstoDTO mvtstoDTO = new MvtstoDTO();
        mvtstoDTO.setCodart(mvtsto.getMvtstoPK().getCodart());
        mvtstoDTO.setNumbon(mvtsto.getMvtstoPK().getNumbon());
        mvtstoDTO.setNumordre(mvtsto.getMvtstoPK().getNumordre());
        mvtstoDTO.setLotInter(mvtsto.getLotInter());
        mvtstoDTO.setPriuni(mvtsto.getPriuni());
        mvtstoDTO.setRemise(mvtsto.getRemise());
        mvtstoDTO.setMajoration(mvtsto.getMajoration());
        mvtstoDTO.setMontht(mvtsto.getMontht());
        mvtstoDTO.setTautva(mvtsto.getTautva());
        mvtstoDTO.setPriach(mvtsto.getPriach());
        mvtstoDTO.setDatPer(mvtsto.getDatPer());
        mvtstoDTO.setUnityCode(mvtsto.getUnite());
        mvtstoDTO.setCategDepot(mvtsto.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {

            mvtstoDTO.setDesart(mvtsto.getDesArtSec());
            mvtstoDTO.setDesArtSec(mvtsto.getDesart());

        } else {
            mvtstoDTO.setDesart(mvtsto.getDesart());
            mvtstoDTO.setDesArtSec(mvtsto.getDesArtSec());
        }

        mvtstoDTO.setCodeSaisi(mvtsto.getCodeSaisi());
        mvtstoDTO.setQuantite(mvtsto.getQuantite());
        mvtstoDTO.setQteRestante(mvtsto.getQteben());
        mvtstoDTO.setCodtva(mvtsto.getCodtva());
        mvtstoDTO.setNumaffiche(mvtsto.getFacture().getNumaffiche());
        mvtstoDTO.setDatbon(mvtsto.getFacture().getDatbon());
        mvtstoDTO.setNumdoss(mvtsto.getFacture().getNumdoss());
        mvtstoDTO.setCodvend(mvtsto.getFacture().getCodvend());
        mvtstoDTO.setCodeSociete(mvtsto.getFacture().getCodeSociete());
        mvtstoDTO.setRemiseConventionnellePharmacie(mvtsto.getFacture().getRemiseConventionnellePharmacie());
        mvtstoDTO.setAjustement(mvtsto.getAjustement());
        mvtstoDTO.setPrixBrute(mvtsto.getPrixBrute());
        mvtstoDTO.setTauxCouverture(mvtsto.getTauxCouverture());
        mvtstoDTO.setTypbon(mvtsto.getFacture().getTypbon());
        mvtstoDTO.setCodTvaAch(mvtsto.getCodTvaAch());
        mvtstoDTO.setTauTvaAch(mvtsto.getTauTvaAch());

        return mvtstoDTO;
    }

    public static Mvtsto mvtstoDTOToMvtsto(MvtstoDTO mvtstoDTO) {
        Mvtsto mvtsto = new Mvtsto();
        MvtstoPK mvtstoPK = new MvtstoPK();
        mvtstoPK.setCodart(mvtstoDTO.getCodart());
        mvtstoPK.setNumbon(mvtstoDTO.getNumbon());
        mvtstoPK.setNumbon(mvtstoDTO.getNumordre());
        mvtstoPK.setCodart(mvtstoDTO.getCodart());
        mvtsto.setMvtstoPK(mvtstoPK);
        mvtsto.setCodTvaAch(mvtstoDTO.getCodTvaAch());
        mvtsto.setTauTvaAch(mvtstoDTO.getTauTvaAch());
        mvtsto.setTypbon(mvtstoDTO.getTypbon());
        mvtsto.setPriuni(mvtstoDTO.getPriuni());
        mvtsto.setMontht(mvtstoDTO.getMontht());
        mvtsto.setTautva(mvtstoDTO.getTautva());
        mvtsto.setPriach(mvtstoDTO.getPriach());
        mvtsto.setMemoart(mvtstoDTO.getMemoart());
        mvtsto.setDatPer(mvtstoDTO.getDatPer());
        mvtsto.setHeureSysteme(mvtstoDTO.getHeureSysteme());
        mvtsto.setUnite(mvtstoDTO.getUnityCode());
        mvtsto.setLotInter(mvtstoDTO.getLotInter());
        mvtsto.setCategDepot(mvtstoDTO.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {

            mvtsto.setDesart(mvtstoDTO.getDesArtSec());
            mvtsto.setDesArtSec(mvtstoDTO.getDesart());

        } else {

            mvtsto.setDesart(mvtstoDTO.getDesart());
            mvtsto.setDesArtSec(mvtstoDTO.getDesArtSec());
        }
        mvtsto.setCodeSaisi(mvtstoDTO.getCodeSaisi());
        mvtsto.setQuantite(mvtstoDTO.getQuantite());
        mvtsto.setQteben(mvtstoDTO.getQteRestante());
        mvtsto.setCodtva(mvtstoDTO.getCodtva());
        mvtsto.setTauxCouverture(mvtstoDTO.getTauxCouverture());
        return mvtsto;
    }

    public static List<MvtstoDTO> mvtstoToMvtstoDTOs(List<Mvtsto> mvtstos) {
        List<MvtstoDTO> mvtstosDTO = new ArrayList<>();
        mvtstos.forEach(x -> {
            mvtstosDTO.add(mvtstoToMvtstoDTO(x));
        });
        return mvtstosDTO;
    }

    public static List<Mvtsto> mvtstoDTOToMvtstos(List<MvtstoDTO> mvtstoDTOs) {
        List<Mvtsto> mvtstos = new ArrayList<>();
        mvtstoDTOs.forEach(x -> {
            mvtstos.add(mvtstoDTOToMvtsto(x));
        });
        return mvtstos;
    }

    public List<Mouvement> toMouvement(Mvtsto entity, UniteDTO unite, AdmissionDemandePECDTO client, TypeDateEnum typeDate) {
        if (entity == null) {
            return null;
        }
        Locale loc = LocaleContextHolder.getLocale();
        List<Mouvement> listMouvement = new ArrayList<>();
        Mouvement dto = new Mouvement();
        dto.setDate(Date.from(entity.getFacture().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesignation(entity.getDesArtSec());
        } else {
            dto.setDesignation(entity.getDesart());
        }
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setOperation(messages.getMessage("ficheStock.quittance", null, loc));
        dto.setNumbon(entity.getFacture().getNumbon());
        dto.setNumaffiche(entity.getFacture().getNumaffiche());
        dto.setLibelle(client.getCode() + " " + client.getNomCompletAr());
        dto.setCoddep(entity.getFacture().getCoddep());

        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO) || (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL) && entity.getFacture().getNumbonTransfert() != null)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            if (entity.getDatPer() != null) {
                dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            dtoEntreSortie.setLotinter(entity.getLotInter());
            dtoEntreSortie.setNumbon(entity.getFacture().getNumbon());
            dtoEntreSortie.setEntree(BigDecimal.ZERO);
            dtoEntreSortie.setSortie(entity.getQuantite());
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            dtoEntreSortie.setNumbon(entity.getFacture().getNumbon());
            dtoEntreSortie.setEntree(BigDecimal.ZERO);
            dtoEntreSortie.setSortie(entity.getQuantite());
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            for (DetailMvtsto detailMvtsto : entity.getDetailMvtstoCollection()) {
                EntreSortie dtoEntreSortie = new EntreSortie();
                if (detailMvtsto.getDepsto().getDatPer() != null) {
                    dtoEntreSortie.setDatPer(Date.from(detailMvtsto.getDepsto().getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                dtoEntreSortie.setLotinter(detailMvtsto.getDepsto().getLotInter());
                dtoEntreSortie.setNumbon(entity.getFacture().getNumbon());
                dtoEntreSortie.setEntree(BigDecimal.ZERO);
                dtoEntreSortie.setSortie(detailMvtsto.getQte());
                dtoEntreSortie.setCodeUnite(entity.getUnite());
                dtoEntreSortie.setPrix(entity.getPriuni());
                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                dtoEntreSorties.add(dtoEntreSortie);
            }
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        }
        return listMouvement;
    }

    public MouvementConsomation toMouvement(Mvtsto entity, UniteDTO unite, DepotDTO depot) {
        if (entity == null) {
            return null;
        }
        Locale loc = LocaleContextHolder.getLocale();
        MouvementConsomation dto = new MouvementConsomation();
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesart(entity.getDesArtSec());
            dto.setDesArtSec(entity.getDesart());
        } else {
            dto.setDesart(entity.getDesart());
            dto.setDesArtSec(entity.getDesArtSec());
        }
        dto.setCodart(entity.getMvtstoPK().getCodart());
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setNumbon(entity.getFacture().getNumbon());
        dto.setNumaffiche(entity.getFacture().getNumaffiche());
        dto.setCoddep(entity.getFacture().getCoddep());
        dto.setNumbon(entity.getFacture().getNumbon());
        dto.setQuantite(entity.getQteben());
        dto.setCodeUnite(entity.getUnite());
        dto.setPriach(entity.getPriach());
        dto.setCodTvaAch(entity.getCodTvaAch());
        dto.setTauTvaAch(entity.getTauTvaAch());
        dto.setTypbon(entity.getFacture().getTypbon().type());
        dto.setDesignationUnite(unite.getDesignation());
        dto.setDesignationDepot(depot.getDesignation());
        return dto;

    }

}
