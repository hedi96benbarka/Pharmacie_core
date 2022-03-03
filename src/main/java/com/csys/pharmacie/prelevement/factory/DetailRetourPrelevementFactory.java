package com.csys.pharmacie.prelevement.factory;

import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.prelevement.domain.DetailRetourPrelevement;
import com.csys.pharmacie.prelevement.domain.TraceDetailRetourPr;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.prelevement.dto.DetailRetourPrelevementDTO;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;


@Component
public class DetailRetourPrelevementFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }
    @Autowired
    MessageSource messages;

    public static DetailRetourPrelevementDTO detailretourprelevementToDetailRetourPrelevementDTO(DetailRetourPrelevement detailretourprelevement) {
        DetailRetourPrelevementDTO detailretourprelevementDTO = new DetailRetourPrelevementDTO();
        detailretourprelevementDTO.setCode(detailretourprelevement.getCode());
        detailretourprelevementDTO.setCodart(detailretourprelevement.getCodart());
        detailretourprelevementDTO.setCategDepot(detailretourprelevement.getCategDepot());
        detailretourprelevementDTO.setDesart(detailretourprelevement.getDesart());
        detailretourprelevementDTO.setDesartSec(detailretourprelevement.getDesArtSec());
        detailretourprelevementDTO.setCodeSaisi(detailretourprelevement.getCodeSaisi());
        detailretourprelevementDTO.setQuantite(detailretourprelevement.getQuantite());
        detailretourprelevementDTO.setLotInter(detailretourprelevement.getLotinter());
        detailretourprelevementDTO.setPriuni(detailretourprelevement.getPriuni());
        detailretourprelevementDTO.setDatPer(detailretourprelevement.getDatPer());
        detailretourprelevementDTO.setDatPerEdition(Date.from(detailretourprelevement.getDatPer().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        detailretourprelevementDTO.setUnite(detailretourprelevement.getUnite());
        return detailretourprelevementDTO;
    }

    public static DetailRetourPrelevement detailretourprelevementDTOToDetailRetourPrelevement(DetailRetourPrelevementDTO detailretourprelevementDTO) {
        DetailRetourPrelevement detailretourprelevement = new DetailRetourPrelevement();
        detailretourprelevement.setCodart(detailretourprelevementDTO.getCodart());
        detailretourprelevement.setCategDepot(detailretourprelevementDTO.getCategDepot());
        detailretourprelevement.setDesart(detailretourprelevementDTO.getDesart());
        detailretourprelevement.setDesArtSec(detailretourprelevementDTO.getDesartSec());
        detailretourprelevement.setCodeSaisi(detailretourprelevementDTO.getCodeSaisi());
        detailretourprelevement.setQuantite(detailretourprelevementDTO.getQuantite());
        detailretourprelevement.setLotinter(detailretourprelevementDTO.getLotInter());
        detailretourprelevement.setPriuni(detailretourprelevementDTO.getPriuni());
        detailretourprelevement.setDatPer(detailretourprelevementDTO.getDatPer());
        detailretourprelevement.setUnite(detailretourprelevementDTO.getUnite());
        return detailretourprelevement;
    }

    public static List<DetailRetourPrelevementDTO> detailretourprelevementToDetailRetourPrelevementDTOs(List<DetailRetourPrelevement> detailretourprelevements) {
        List<DetailRetourPrelevementDTO> detailretourprelevementsDTO = new ArrayList<>();
        detailretourprelevements.forEach(x -> {
            detailretourprelevementsDTO.add(detailretourprelevementToDetailRetourPrelevementDTO(x));
        });
        return detailretourprelevementsDTO;
    }

    public static List<DetailRetourPrelevement> detailretourprelevementDTOsToDetailRetourPrelevements(List<DetailRetourPrelevementDTO> detailretourprelevementDTOs) {
        List<DetailRetourPrelevement> detailretourprelevements = new ArrayList<>();
        detailretourprelevementDTOs.forEach(x -> {

            detailretourprelevements.add(detailretourprelevementDTOToDetailRetourPrelevement(x));
        });
        return detailretourprelevements;
    }

    public List<Mouvement> toMouvement(DetailRetourPrelevement entity, UniteDTO unite, DepartementDTO departement, TypeDateEnum typeDate) {
        if (entity == null) {
            return null;
        }
        Locale loc = LocaleContextHolder.getLocale();
        List<Mouvement> listMouvement = new ArrayList<>();
        Mouvement dto = new Mouvement();
        dto.setDate(Date.from(entity.getRetourPrelevement().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesignation(entity.getDesArtSec());
        } else {
            dto.setDesignation(entity.getDesart());
        }
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setOperation(messages.getMessage("ficheStock.retouPrelevement", null, loc));
        dto.setNumbon(entity.getRetourPrelevement().getNumbon());
        dto.setNumaffiche(entity.getRetourPrelevement().getNumaffiche());
        dto.setLibelle(departement.getDesignation() + " " + departement.getCodeSaisi());
        dto.setCoddep(entity.getRetourPrelevement().getCoddepDesti());
        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            if (entity.getDatPer() != null) {
                dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            dtoEntreSortie.setLotinter(entity.getLotinter());
            dtoEntreSortie.setNumbon(entity.getRetourPrelevement().getNumbon());
            dtoEntreSortie.setEntree(entity.getQuantite());
            dtoEntreSortie.setSortie(BigDecimal.ZERO);
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            dtoEntreSortie.setNumbon(entity.getRetourPrelevement().getNumbon());
            dtoEntreSortie.setEntree(entity.getQuantite());
            dtoEntreSortie.setSortie(BigDecimal.ZERO);
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            for (TraceDetailRetourPr traceDetailRetourPr : entity.getTraceDetailRetourPr()) {
                EntreSortie dtoEntreSortie = new EntreSortie();
                if (traceDetailRetourPr.getDetailMvtStoPR().getDepsto().getDatPer() != null) {
                    dtoEntreSortie.setDatPer(Date.from(traceDetailRetourPr.getDetailMvtStoPR().getDepsto().getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                dtoEntreSortie.setLotinter(traceDetailRetourPr.getDetailMvtStoPR().getDepsto().getLotInter());
                dtoEntreSortie.setNumbon(entity.getRetourPrelevement().getNumbon());
                dtoEntreSortie.setEntree(traceDetailRetourPr.getQuantite());
                dtoEntreSortie.setSortie(BigDecimal.ZERO);
                dtoEntreSortie.setCodeUnite(entity.getUnite());
                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                dtoEntreSortie.setPrix(entity.getPriuni());
                dtoEntreSorties.add(dtoEntreSortie);
            }
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        }
        return listMouvement;

    }
}
