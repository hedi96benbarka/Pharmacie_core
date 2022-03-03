package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.DetailMvtStoRetourPerime;
import com.csys.pharmacie.achat.domain.MvtstoRetourPerime;
import com.csys.pharmacie.achat.dto.DetailEditionDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoRetourPerimeDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MvtstoRetourPerimeFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }
    @Autowired
    MessageSource messages;

    public static MvtstoRetourPerimeDTO mvtStoretourPerimeToMvtStoRetourPerimeDTO(MvtstoRetourPerime mvtstoRetourPerime) {
        MvtstoRetourPerimeDTO mvtstoRetourPerimeDTO = new MvtstoRetourPerimeDTO();

        mvtstoRetourPerimeDTO.setNumordre(mvtstoRetourPerime.getNumordre());
        mvtstoRetourPerimeDTO.setCodtva(mvtstoRetourPerime.getCodtva());
        mvtstoRetourPerimeDTO.setTautva(mvtstoRetourPerime.getTautva());
        mvtstoRetourPerimeDTO.setDatePer(Date.from(mvtstoRetourPerime.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        mvtstoRetourPerimeDTO.setCategDepot(mvtstoRetourPerime.getCategDepot());

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstoRetourPerimeDTO.setDesart(mvtstoRetourPerime.getDesArtSec());
            mvtstoRetourPerimeDTO.setDesArtSec(mvtstoRetourPerime.getDesart());
        } else {
            mvtstoRetourPerimeDTO.setDesart(mvtstoRetourPerime.getDesart());
            mvtstoRetourPerimeDTO.setDesArtSec(mvtstoRetourPerime.getDesArtSec());
        }
        mvtstoRetourPerimeDTO.setCodeSaisi(mvtstoRetourPerime.getCodeSaisi());
        mvtstoRetourPerimeDTO.setQuantite(mvtstoRetourPerime.getQuantite());
        mvtstoRetourPerimeDTO.setCode(mvtstoRetourPerime.getCode());
        mvtstoRetourPerimeDTO.setCodart(mvtstoRetourPerime.getCodart());
        mvtstoRetourPerimeDTO.setNumbon(mvtstoRetourPerime.getNumbon());
        mvtstoRetourPerimeDTO.setLotinter(mvtstoRetourPerime.getLotinter());
        mvtstoRetourPerimeDTO.setDatPer(mvtstoRetourPerime.getDatPer());
        mvtstoRetourPerimeDTO.setPriuni(mvtstoRetourPerime.getPriuni());
        mvtstoRetourPerimeDTO.setCodeUnite(mvtstoRetourPerime.getUnite());
        return mvtstoRetourPerimeDTO;
    }

    public static MvtstoRetourPerime mvtStoretourPerimeDTOToMvtStoRetourPerime(MvtstoRetourPerimeDTO mvtstoDTO) {
        MvtstoRetourPerime mvtsto = new MvtstoRetourPerime();
        mvtsto.setLotinter(mvtstoDTO.getLotinter());
        mvtsto.setPriuni(mvtstoDTO.getPriuni());
        mvtsto.setDatPer(mvtstoDTO.getDatPer());
        mvtsto.setCategDepot(mvtstoDTO.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtsto.setDesart(mvtstoDTO.getDesArtSec());
            mvtsto.setDesArtSec(mvtstoDTO.getDesart());

        } else {
            mvtsto.setDesart(mvtstoDTO.getDesart());
            mvtsto.setDesArtSec(mvtstoDTO.getDesArtSec());
        }
        mvtsto.setCodtva(mvtstoDTO.getCodtva());
        mvtsto.setTautva(mvtstoDTO.getTautva());
        mvtsto.setCodeSaisi(mvtstoDTO.getCodeSaisi());
        mvtsto.setQuantite(mvtstoDTO.getQuantite());
        mvtsto.setUnite(mvtstoDTO.getCodeUnite());
        return mvtsto;
    }

    public static List<MvtstoRetourPerimeDTO> mvtstoRetourPerimesToMvtstoRetourPerimeDTOs(Collection<MvtstoRetourPerime> mvtstoretour_perimes) {
        List<MvtstoRetourPerimeDTO> mvtstoretour_perimesDTO = new ArrayList<>();
        mvtstoretour_perimes.forEach(x -> {
            mvtstoretour_perimesDTO.add(mvtStoretourPerimeToMvtStoRetourPerimeDTO(x));
        });
        return mvtstoretour_perimesDTO;
    }

    public static DetailEditionDTO toEditionDTO(MvtstoRetourPerime entity) {
        if (entity == null) {
            return null;
        }
        DetailEditionDTO dto = new DetailEditionDTO();
        dto.setNumbon(dto.getNumbon());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesart(entity.getDesArtSec());
            dto.setDesartSec(entity.getDesart());
        } else {
            dto.setDesart(entity.getDesart());
            dto.setDesartSec(entity.getDesArtSec());
        }
        dto.setQuantite(entity.getQuantite());
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setRefArt(entity.getCodart());
        if (entity.getDatPer() != null) {
            dto.setDatePerEdition(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
//        dto.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dto.setLotInter(entity.getLotinter());
        dto.setNumOrdre(entity.getNumordre());
        dto.setNumbon(entity.getFactureRetourPerime().getNumbon());
        dto.setRemise(BigDecimal.ZERO);
        dto.setMontantTvaGratuite(BigDecimal.ZERO);
        dto.setPriuni(entity.getPriuni());
        dto.setCodTVA(entity.getCodtva());
        dto.setTauTVA(entity.getTautva());
        dto.setMontht(entity.getPriuni().multiply(entity.getQuantite()));
        dto.setCodeUnite(entity.getUnite());
        return dto;

    }

    public static List<DetailEditionDTO> toEditionDTO(List<MvtstoRetourPerime> mvtstoretour_perimes) {
        List<DetailEditionDTO> mvtstoretour_perimesDTO = new ArrayList<>();
        mvtstoretour_perimes.forEach(x -> {
            mvtstoretour_perimesDTO.add(toEditionDTO(x));
        });
        return mvtstoretour_perimesDTO;
    }

    public List<Mouvement> toMouvement(MvtstoRetourPerime entity, FournisseurDTO fournisseur, UniteDTO unite, TypeDateEnum typeDate) {
        if (entity == null) {
            return null;
        }
        Locale loc = LocaleContextHolder.getLocale();
        List<Mouvement> listMouvement = new ArrayList<>();
        Mouvement dto = new Mouvement();
        dto.setDate(Date.from(entity.getFactureRetourPerime().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesignation(entity.getDesArtSec());
        } else {
            dto.setDesignation(entity.getDesart());
        }
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setNumaffiche(entity.getFactureRetourPerime().getNumaffiche());
        dto.setNumbon(entity.getFactureRetourPerime().getNumbon());
        dto.setLibelle(entity.getFactureRetourPerime().getCodFrs());
        dto.setCoddep(entity.getFactureRetourPerime().getCoddep());
        dto.setLibelle(fournisseur.getDesignation() + " " + fournisseur.getCode());
        dto.setOperation(messages.getMessage("ficheStock.retourPerime", null, loc));

        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            if (entity.getDatPer() != null) {
                dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            dtoEntreSortie.setLotinter(entity.getLotinter());
            dtoEntreSortie.setNumbon(entity.getFactureRetourPerime().getNumbon());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSortie.setEntree(BigDecimal.ZERO);
            dtoEntreSortie.setSortie(entity.getQuantite());
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            dtoEntreSortie.setNumbon(entity.getFactureRetourPerime().getNumbon());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSortie.setEntree(BigDecimal.ZERO);
            dtoEntreSortie.setSortie(entity.getQuantite());
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            for (DetailMvtStoRetourPerime detailMvtStoRetourPerime : entity.getDetailMvtStoList()) {
                EntreSortie dtoEntreSortie = new EntreSortie();
                if (detailMvtStoRetourPerime.getDepsto().getDatPer() != null) {
                    dtoEntreSortie.setDatPer(Date.from(detailMvtStoRetourPerime.getDepsto().getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                dtoEntreSortie.setLotinter(detailMvtStoRetourPerime.getDepsto().getLotInter());
                dtoEntreSortie.setNumbon(entity.getFactureRetourPerime().getNumbon());
                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                dtoEntreSortie.setEntree(BigDecimal.ZERO);
                dtoEntreSortie.setSortie(detailMvtStoRetourPerime.getQuantitePrelevee());
                dtoEntreSortie.setPrix(entity.getPriuni());
                dtoEntreSortie.setCodeUnite(entity.getUnite());
                dtoEntreSorties.add(dtoEntreSortie);
            }
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        }
        return listMouvement;

    }

}
