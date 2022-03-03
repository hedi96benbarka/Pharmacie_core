package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.DetailMvtStoAF;
import com.csys.pharmacie.achat.domain.MvtstoAF;
import com.csys.pharmacie.achat.dto.DetailEditionDTO;
import com.csys.pharmacie.achat.dto.FournisseurDTO;
import com.csys.pharmacie.achat.dto.MvtstoAFDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class MvtstoAFFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }
    @Autowired
    MessageSource messages;

    public MvtstoAFDTO mvtstoAFToMvtstoAFDTO(MvtstoAF mvtstoaf) {
        MvtstoAFDTO mvtstoafDTO = new MvtstoAFDTO();
        mvtstoafDTO.setPriuni(mvtstoaf.getPriuni());
        mvtstoafDTO.setMontht(mvtstoaf.getMontht());
        mvtstoafDTO.setTautva(mvtstoaf.getTautva());
        mvtstoafDTO.setCodtva(mvtstoaf.getCodtva());
        mvtstoafDTO.setNumBonReception(mvtstoaf.getNumbonReception());

        mvtstoafDTO.setCategDepot(mvtstoaf.getCategDepot());
        mvtstoafDTO.setDesart(mvtstoaf.getDesart());
        mvtstoafDTO.setDesArtSec(mvtstoaf.getDesArtSec());
        mvtstoafDTO.setCodeSaisi(mvtstoaf.getCodeSaisi());
        mvtstoafDTO.setQuantite(mvtstoaf.getQuantite());
        mvtstoafDTO.setCode(mvtstoaf.getCode());
        mvtstoafDTO.setCodart(mvtstoaf.getCodart());
        mvtstoafDTO.setNumbon(mvtstoaf.getNumbon());
        mvtstoafDTO.setLotinter(mvtstoaf.getLotinter());
        mvtstoafDTO.setDatPer(mvtstoaf.getDatPer());
        mvtstoafDTO.setPriuni(mvtstoaf.getPriuni());
        mvtstoafDTO.setUnite(mvtstoaf.getUnite());
        mvtstoafDTO.setRemise(mvtstoaf.getRemise());
        mvtstoafDTO.setBaseTva(mvtstoaf.getBaseTva());
        if (mvtstoaf.getPriuni().compareTo(BigDecimal.ZERO) == 0) {
            mvtstoafDTO.setFree(true);
        } else if (mvtstoaf.getPriuni().compareTo(BigDecimal.ZERO) == 1) {
            mvtstoafDTO.setFree(false);
        }
        return mvtstoafDTO;
    }

    public MvtstoAF mvtstoAFDTOToMvtstoAF(MvtstoAFDTO mvtstoafDTO) {
        MvtstoAF mvtstoaf = new MvtstoAF();
        mvtstoaf.setPriuni(mvtstoafDTO.getPriuni());
        mvtstoaf.setMontht(mvtstoafDTO.getMontht());
        mvtstoaf.setTautva(mvtstoafDTO.getTautva());
        mvtstoaf.setCodtva(mvtstoafDTO.getCodtva());
        mvtstoaf.setNumbonReception(mvtstoafDTO.getNumBonReception());
        mvtstoaf.setCategDepot(mvtstoafDTO.getCategDepot());
        mvtstoaf.setDesart(mvtstoafDTO.getDesart());
        mvtstoaf.setDesArtSec(mvtstoafDTO.getDesArtSec());
        mvtstoaf.setCodeSaisi(mvtstoafDTO.getCodeSaisi());
        mvtstoaf.setQuantite(mvtstoafDTO.getQuantite());
        mvtstoaf.setCode(mvtstoafDTO.getCode());
        mvtstoaf.setCodart(mvtstoafDTO.getCodart());
        mvtstoaf.setNumbon(mvtstoafDTO.getNumbon());
        mvtstoaf.setLotinter(mvtstoafDTO.getLotinter());
        mvtstoaf.setDatPer(mvtstoafDTO.getDatPer());
        mvtstoaf.setPriuni(mvtstoafDTO.getPriuni());
        mvtstoaf.setUnite(mvtstoafDTO.getUnite());
        mvtstoaf.setRemise(mvtstoafDTO.getRemise());
        mvtstoaf.setBaseTva(mvtstoafDTO.getBaseTva());

        return mvtstoaf;
    }

    public List<MvtstoAFDTO> mvtstoAFToMvtstoAFDTOs(List<MvtstoAF> mvtstoafs) {
        List<MvtstoAFDTO> mvtstoafsDTO = new ArrayList<>();
        mvtstoafs.forEach(x -> {
            mvtstoafsDTO.add(mvtstoAFToMvtstoAFDTO(x));
        });
        return mvtstoafsDTO;
    }

    public DetailEditionDTO toEditionDTO(MvtstoAF entity) {
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
        dto.setNumbon(entity.getAvoirFournisseur().getNumbon());
        dto.setRemise(entity.getRemise());
        dto.setPriuni(entity.getPriuni());
        dto.setCodTVA(entity.getCodtva());
        dto.setTauTVA(entity.getTautva());
        dto.setMontht(entity.getMontht());
        dto.setCodeUnite(entity.getUnite());

        BigDecimal baseTvaGratuite = BigDecimal.ZERO;
        if (entity.getPriuni().compareTo(BigDecimal.ZERO) == 0 && entity.getBaseTva().compareTo(BigDecimal.ZERO) == 1) {


            baseTvaGratuite = entity.getBaseTva().multiply(entity.getQuantite()).setScale(7, RoundingMode.HALF_UP);
        }

        dto.setMontantTvaGratuite(baseTvaGratuite.multiply(entity.getTautva()).divide(new BigDecimal(100)).setScale(7, RoundingMode.HALF_UP));
        return dto;

    }

    public List<DetailEditionDTO> toEditionDTO(List<MvtstoAF> mvtstoAFs) {
        List<DetailEditionDTO> entitys = new ArrayList<>();
        mvtstoAFs.forEach(x -> {
            entitys.add(toEditionDTO(x));
        });
        return entitys;
    }

    public List<Mouvement> toMouvement(MvtstoAF entity, FournisseurDTO fournisseur, UniteDTO unite, TypeDateEnum typeDate) {
        if (entity == null) {
            return null;
        }
        Locale loc = LocaleContextHolder.getLocale();
        List<Mouvement> listMouvement = new ArrayList<>();
        Mouvement dto = new Mouvement();
        dto.setDate(Date.from(entity.getAvoirFournisseur().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesignation(entity.getDesArtSec());
        } else {
            dto.setDesignation(entity.getDesart());
        }
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setNumaffiche(entity.getAvoirFournisseur().getNumaffiche());
        dto.setNumbon(entity.getAvoirFournisseur().getNumbon());
        dto.setLibelle(entity.getAvoirFournisseur().getCodeFournisseur());
        dto.setCoddep(entity.getAvoirFournisseur().getCoddep());
        dto.setLibelle(fournisseur.getDesignation() + " " + fournisseur.getCode());
        dto.setOperation(messages.getMessage("ficheStock.avoirFournisseur", null, loc));

        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            if (entity.getDatPer() != null) {
                dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            dtoEntreSortie.setLotinter(entity.getLotinter());
            dtoEntreSortie.setNumbon(entity.getAvoirFournisseur().getNumbon());
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
            dtoEntreSortie.setNumbon(entity.getAvoirFournisseur().getNumbon());
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
            for (DetailMvtStoAF detailMvtStoAF : entity.getDetailMvtStoAFList()) {
                EntreSortie dtoEntreSortie = new EntreSortie();
                if (detailMvtStoAF.getDepsto().getDatPer() != null) {
                    dtoEntreSortie.setDatPer(Date.from(detailMvtStoAF.getDepsto().getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                dtoEntreSortie.setLotinter(detailMvtStoAF.getDepsto().getLotInter());
                dtoEntreSortie.setNumbon(entity.getAvoirFournisseur().getNumbon());
                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                dtoEntreSortie.setEntree(BigDecimal.ZERO);
                dtoEntreSortie.setSortie(detailMvtStoAF.getQuantitePrelevee());
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
