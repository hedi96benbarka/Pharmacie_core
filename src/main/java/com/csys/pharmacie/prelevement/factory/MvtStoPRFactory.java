package com.csys.pharmacie.prelevement.factory;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.MouvementConsomation;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.prelevement.domain.DetailMvtStoPR;
import com.csys.pharmacie.prelevement.domain.MvtStoPR;
import com.csys.pharmacie.prelevement.dto.DepartementDTO;
import com.csys.pharmacie.prelevement.dto.MvtStoPRDTO;
import com.csys.pharmacie.prelevement.dto.MvtStoPREditionDTO;
import com.csys.pharmacie.vente.avoir.domain.DetailMvtstoAV;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
public class MvtStoPRFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }
    @Autowired
    MessageSource messages;

    public static MvtStoPRDTO mvtstoprToMvtStoPRDTO(MvtStoPR mvtstopr) {
        MvtStoPRDTO mvtstoprDTO = new MvtStoPRDTO();

        mvtstoprDTO.setPriuni(mvtstopr.getPriuni());
        mvtstoprDTO.setUnite(mvtstopr.getUnite());
        mvtstoprDTO.setArticleID(mvtstopr.getCodart());
        mvtstoprDTO.setDatPer(mvtstopr.getDatPer());

        mvtstoprDTO.setLotinter(mvtstopr.getLotinter());

        mvtstoprDTO.setCategDepot(mvtstopr.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstoprDTO.setDesignation(mvtstopr.getDesArtSec());
            mvtstoprDTO.setSecondDesignation(mvtstopr.getDesart());
        } else {

            mvtstoprDTO.setDesignation(mvtstopr.getDesart());
            mvtstoprDTO.setSecondDesignation(mvtstopr.getDesArtSec());
        }
        mvtstoprDTO.setCodeSaisi(mvtstopr.getCodeSaisi());
        mvtstoprDTO.setQuantite(mvtstopr.getQuantite());
        mvtstoprDTO.setQuantiteNette(mvtstopr.getQtecom());
        mvtstoprDTO.setCodeEmplacement(mvtstopr.getCodeEmplacement());
        return mvtstoprDTO;
    }

    public static MvtStoPREditionDTO mvtstoprToMvtStoPREditionDTO(MvtStoPR mvtstopr) {
        MvtStoPREditionDTO mvtstoprDTO = new MvtStoPREditionDTO();

        mvtstoprDTO.setPriuni(mvtstopr.getPriuni());
        mvtstoprDTO.setUnite(mvtstopr.getUnite());
        mvtstoprDTO.setArticleID(mvtstopr.getCodart());
        if (mvtstopr.getDatPer() != null) {
            mvtstoprDTO.setDatPer(Date.from(mvtstopr.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        mvtstoprDTO.setLotinter(mvtstopr.getLotinter());

        mvtstoprDTO.setCategDepot(mvtstopr.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {

            mvtstoprDTO.setDesignation(mvtstopr.getDesArtSec());
            mvtstoprDTO.setSecondDesignation(mvtstopr.getDesart());
        } else {
            mvtstoprDTO.setDesignation(mvtstopr.getDesart());
            mvtstoprDTO.setSecondDesignation(mvtstopr.getDesArtSec());
        }

        mvtstoprDTO.setCodeSaisi(mvtstopr.getCodeSaisi());
        mvtstoprDTO.setQuantite(mvtstopr.getQuantite());
        mvtstoprDTO.setCodeEmplacement(mvtstopr.getCodeEmplacement());
        return mvtstoprDTO;
    }

    public static MvtStoPR mvtstoprDTOToMvtStoPR(MvtStoPRDTO mvtstoprDTO) {
        MvtStoPR mvtstopr = new MvtStoPR();

        mvtstopr.setCodart(mvtstoprDTO.getArticleID());
        mvtstopr.setNumbon(mvtstoprDTO.getNumbon());

        mvtstopr.setDatPer(mvtstoprDTO.getDatPer());
        mvtstopr.setLotinter(mvtstoprDTO.getLotinter());

        mvtstopr.setUnite(mvtstoprDTO.getUnite());

        mvtstopr.setCategDepot(mvtstoprDTO.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstopr.setDesart(mvtstoprDTO.getSecondDesignation());
            mvtstopr.setDesArtSec(mvtstoprDTO.getDesignation());
        } else {
            mvtstopr.setDesart(mvtstoprDTO.getDesignation());
            mvtstopr.setDesArtSec(mvtstoprDTO.getSecondDesignation());
        }
        mvtstopr.setCodeSaisi(mvtstoprDTO.getCodeSaisi());
        mvtstopr.setQuantite(mvtstoprDTO.getQuantite());
        mvtstopr.setCodeEmplacement(mvtstoprDTO.getCodeEmplacement());
        return mvtstopr;
    }

    public static List<MvtStoPRDTO> mvtstoprToMvtStoPRDTOs(List<MvtStoPR> mvtstoprs) {
        List<MvtStoPRDTO> mvtstoprsDTO = new ArrayList<>();
        mvtstoprs.forEach(x -> {
            mvtstoprsDTO.add(mvtstoprToMvtStoPRDTO(x));
        });
        return mvtstoprsDTO;
    }

    public static List<MvtStoPREditionDTO> mvtstoprToMvtStoPREditionDTOs(List<MvtStoPR> mvtstoprs) {
        List<MvtStoPREditionDTO> mvtstoprsDTO = new ArrayList<>();
        mvtstoprs.forEach(x -> {
            mvtstoprsDTO.add(mvtstoprToMvtStoPREditionDTO(x));
        });
        return mvtstoprsDTO;
    }

    public static Collection<MvtStoPR> MvtStoPRDTOsTomvtstopr(Collection<MvtStoPRDTO> mvtstoprsdto) {
        List<MvtStoPR> mvtstoprs = new ArrayList<>();
        mvtstoprsdto.forEach(x
                -> {
            mvtstoprs.add(mvtstoprDTOToMvtStoPR(x));
        });

        return mvtstoprs;
    }

    public List<Mouvement> toMouvement(MvtStoPR entity, UniteDTO unite, DepartementDTO departement, TypeDateEnum typeDate) {
        if (entity == null) {
            return null;
        }
        Locale loc = LocaleContextHolder.getLocale();
        List<Mouvement> listMouvement = new ArrayList<>();
        Mouvement dto = new Mouvement();
        dto.setDate(Date.from(entity.getFacturePR().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesignation(entity.getDesArtSec());
        } else {
            dto.setDesignation(entity.getDesart());
        }
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setOperation(messages.getMessage("ficheStock.prelevement", null, loc));
        dto.setNumbon(entity.getFacturePR().getNumbon());
        dto.setNumaffiche(entity.getFacturePR().getNumaffiche());
        dto.setLibelle(departement.getDesignation() + " " + departement.getCodeSaisi());
        dto.setCoddep(entity.getFacturePR().getCoddepotSrc());
        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            if (entity.getDatPer() != null) {
                dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            dtoEntreSortie.setLotinter(entity.getLotinter());
            dtoEntreSortie.setNumbon(entity.getFacturePR().getNumbon());
            dtoEntreSortie.setEntree(BigDecimal.ZERO);
            dtoEntreSortie.setSortie(entity.getQuantite());
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie dtoEntreSortie = new EntreSortie();
            dtoEntreSortie.setNumbon(entity.getFacturePR().getNumbon());
            dtoEntreSortie.setEntree(BigDecimal.ZERO);
            dtoEntreSortie.setSortie(entity.getQuantite());
            dtoEntreSortie.setCodeUnite(entity.getUnite());
            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
            dtoEntreSortie.setPrix(entity.getPriuni());
            dtoEntreSorties.add(dtoEntreSortie);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            for (DetailMvtStoPR detailMvtStoPR : entity.getDetailMvtStoPRList()) {
                EntreSortie dtoEntreSortie = new EntreSortie();
                if (detailMvtStoPR.getDepsto().getDatPer() != null) {
                    dtoEntreSortie.setDatPer(Date.from(detailMvtStoPR.getDepsto().getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                dtoEntreSortie.setLotinter(detailMvtStoPR.getDepsto().getLotInter());
                dtoEntreSortie.setNumbon(entity.getFacturePR().getNumbon());
                dtoEntreSortie.setEntree(BigDecimal.ZERO);
                dtoEntreSortie.setSortie(detailMvtStoPR.getQuantitePrelevee());
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

    public MouvementConsomation toMouvement(MvtStoPR entity, UniteDTO unite, DepartementDTO departement, ArticleDTO article) {
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
        dto.setCodart(entity.getCodart());
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setNumbon(entity.getFacturePR().getNumbon());
        dto.setNumaffiche(entity.getFacturePR().getNumaffiche());
        dto.setCoddep(entity.getFacturePR().getCoddepartDest());
        dto.setNumbon(entity.getFacturePR().getNumbon());
        dto.setQuantite(entity.getQuantite());
        dto.setCodeUnite(entity.getUnite());
        dto.setPriach(entity.getPriuni());
        dto.setCodTvaAch(article.getCodeTvaVente());
        dto.setTauTvaAch(article.getValeurTvaVente());
        dto.setTypbon(entity.getFacturePR().getTypbon().type());
        dto.setDesignationUnite(unite.getDesignation());
        dto.setDesignationDepot(departement.getDesignation());
        return dto;

    }

}
