package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.stock.domain.DepstoDetailDecoupage;
import com.csys.pharmacie.stock.domain.DetailDecoupage;
import com.csys.pharmacie.stock.dto.DetailDecoupageDTO;
import com.csys.pharmacie.stock.dto.DetailDecoupageEditionDTO;
import com.csys.pharmacie.transfert.domain.DetailMvtStoBE;
import com.csys.pharmacie.vente.quittance.dto.MvtQuittanceDTO;
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
public class DetailDecoupageFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    @Autowired
    MessageSource messages;

    public static DetailDecoupageDTO detaildecoupageToDetailDecoupageDTO(DetailDecoupage detaildecoupage) {
        DetailDecoupageDTO detaildecoupageDTO = new DetailDecoupageDTO();
        detaildecoupageDTO.setArticleID(detaildecoupage.getCodart());
        detaildecoupageDTO.setLot(detaildecoupage.getLotInter());
        detaildecoupageDTO.setExpirationDate(detaildecoupage.getDatePeremption());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            detaildecoupageDTO.setDesart(detaildecoupage.getDesArtSec());
            detaildecoupageDTO.setDesArtSec(detaildecoupage.getDesart());
        } else {
            detaildecoupageDTO.setDesart(detaildecoupage.getDesart());
            detaildecoupageDTO.setDesArtSec(detaildecoupage.getDesArtSec());
        }
        detaildecoupageDTO.setCodeSaisi(detaildecoupage.getCodeSaisi());
        detaildecoupageDTO.setParentID(detaildecoupage.getCodeDecoupage());
        detaildecoupageDTO.setOriginUnit(detaildecoupage.getUniteOrigine());
        detaildecoupageDTO.setFinalUnit(detaildecoupage.getUniteFinal());
        detaildecoupageDTO.setTransformedQuantity(detaildecoupage.getQuantite());
        detaildecoupageDTO.setObtainedQuantity(detaildecoupage.getQuantiteObtenue());
        return detaildecoupageDTO;
    }

    public static DetailDecoupageEditionDTO detaildecoupageToDetailDecoupageEditionDTO(DetailDecoupage detaildecoupage) {
        DetailDecoupageEditionDTO detaildecoupageDTO = new DetailDecoupageEditionDTO();
        detaildecoupageDTO.setArticleID(detaildecoupage.getCodart());
        detaildecoupageDTO.setLot(detaildecoupage.getLotInter());
        detaildecoupageDTO.setExpirationDate(Date.from(detaildecoupage.getDatePeremption().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            detaildecoupageDTO.setDesart(detaildecoupage.getDesArtSec());
            detaildecoupageDTO.setDesArtSec(detaildecoupage.getDesart());
        } else {
            detaildecoupageDTO.setDesart(detaildecoupage.getDesart());
            detaildecoupageDTO.setDesArtSec(detaildecoupage.getDesArtSec());
        }
        detaildecoupageDTO.setCodeSaisi(detaildecoupage.getCodeSaisi());
        detaildecoupageDTO.setParentID(detaildecoupage.getCodeDecoupage());
        detaildecoupageDTO.setOriginUnit(detaildecoupage.getUniteOrigine());
        detaildecoupageDTO.setFinalUnit(detaildecoupage.getUniteFinal());
        detaildecoupageDTO.setTransformedQuantity(detaildecoupage.getQuantite());
        detaildecoupageDTO.setObtainedQuantity(detaildecoupage.getQuantiteObtenue());

        return detaildecoupageDTO;
    }

    public static DetailDecoupage detaildecoupageDTOToDetailDecoupage(DetailDecoupageDTO detaildecoupageDTO) {
        DetailDecoupage detaildecoupage = new DetailDecoupage();
        detaildecoupage.setLotInter(detaildecoupageDTO.getLot());
        detaildecoupage.setDatePeremption(detaildecoupageDTO.getExpirationDate());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            detaildecoupage.setDesart(detaildecoupageDTO.getDesArtSec());
            detaildecoupage.setDesArtSec(detaildecoupageDTO.getDesart());
        } else {
            detaildecoupage.setDesart(detaildecoupageDTO.getDesart());
            detaildecoupage.setDesArtSec(detaildecoupageDTO.getDesArtSec());
        }
        detaildecoupage.setCodart(detaildecoupageDTO.getArticleID());
        detaildecoupage.setCodeSaisi(detaildecoupageDTO.getCodeSaisi());
        detaildecoupage.setUniteOrigine(detaildecoupageDTO.getOriginUnit());
        detaildecoupage.setUniteFinal(detaildecoupageDTO.getFinalUnit());
        detaildecoupage.setQuantite(detaildecoupageDTO.getTransformedQuantity());
        detaildecoupage.setQuantiteObtenue(detaildecoupageDTO.getObtainedQuantity());

        return detaildecoupage;
    }

    public static DetailDecoupage mvtQuittanceDTOToDetailDecoupage(MvtQuittanceDTO mvtQuittanceDTO) {
        DetailDecoupage detaildecoupage = new DetailDecoupage();
        detaildecoupage.setLotInter(mvtQuittanceDTO.getLot());
        detaildecoupage.setDatePeremption(mvtQuittanceDTO.getDatPer());
        detaildecoupage.setDesart(mvtQuittanceDTO.getArticle().getDesignation());
        detaildecoupage.setDesArtSec(mvtQuittanceDTO.getArticle().getDesignationSec());
        detaildecoupage.setCodart(mvtQuittanceDTO.getCodart());
        detaildecoupage.setCodeSaisi(mvtQuittanceDTO.getArticle().getCodeSaisi());
        detaildecoupage.setUniteFinal(mvtQuittanceDTO.getUnite());
        return detaildecoupage;
    }

    public static Collection<DetailDecoupageDTO> detaildecoupageToDetailDecoupageDTOs(Collection<DetailDecoupage> detaildecoupages) {
        List<DetailDecoupageDTO> detaildecoupagesDTO = new ArrayList<>();
        detaildecoupages.forEach(x -> {
            detaildecoupagesDTO.add(detaildecoupageToDetailDecoupageDTO(x));
        });
        return detaildecoupagesDTO;
    }

    public List<Mouvement> toMouvement(DetailDecoupage entity, DepotDTO depot, UniteDTO uniteOrigin, UniteDTO uniteFinal, TypeDateEnum typeDate) {
        if (entity == null) {
            return null;
        }
        Locale loc = LocaleContextHolder.getLocale();
        List<Mouvement> listMouvement = new ArrayList<>();
        Mouvement dto = new Mouvement();
        dto.setDate(Date.from(entity.getDecoupage().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesignation(entity.getDesArtSec());
        } else {
            dto.setDesignation(entity.getDesart());
        }
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setOperation(messages.getMessage("ficheStock.decoupage", null, loc));
        dto.setNumaffiche(entity.getDecoupage().getNumaffiche());
        dto.setNumbon(entity.getDecoupage().getNumbon());
        dto.setLibelle(depot.getDesignation() + " " + depot.getCodeSaisi());
        dto.setCoddep(entity.getDecoupage().getCoddep());

        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie sortie = new EntreSortie();
            sortie.setNumbon(entity.getDecoupage().getNumbon());
            sortie.setEntree(BigDecimal.ZERO);
            sortie.setSortie(entity.getQuantite());
            sortie.setCodeUnite(entity.getUniteOrigine());
            sortie.setDesignationUnite(uniteOrigin.getDesignation());
            sortie.setPrix(BigDecimal.ZERO);
            if (entity.getDatePeremption() != null) {
                sortie.setDatPer(Date.from(entity.getDatePeremption().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            sortie.setLotinter(entity.getLotInter());
            dtoEntreSorties.add(sortie);
            EntreSortie entre = new EntreSortie();
            entre.setNumbon(entity.getDecoupage().getNumbon());
            entre.setEntree(entity.getQuantiteObtenue());
            entre.setSortie(BigDecimal.ZERO);
            entre.setCodeUnite(entity.getUniteFinal());
            entre.setDesignationUnite(uniteFinal.getDesignation());
            entre.setPrix(BigDecimal.ZERO);
            if (entity.getDatePeremption() != null) {
                entre.setDatPer(Date.from(entity.getDatePeremption().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            entre.setLotinter(entity.getLotInter());
            dtoEntreSorties.add(entre);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            EntreSortie sortie = new EntreSortie();
            sortie.setNumbon(entity.getDecoupage().getNumbon());
            sortie.setEntree(BigDecimal.ZERO);
            sortie.setSortie(entity.getQuantite());
            sortie.setCodeUnite(entity.getUniteOrigine());
            sortie.setDesignationUnite(uniteOrigin.getDesignation());
            sortie.setPrix(BigDecimal.ZERO);
            dtoEntreSorties.add(sortie);
            EntreSortie entre = new EntreSortie();
            entre.setNumbon(entity.getDecoupage().getNumbon());
            entre.setEntree(entity.getQuantiteObtenue());
            entre.setSortie(BigDecimal.ZERO);
            entre.setCodeUnite(entity.getUniteFinal());
            entre.setDesignationUnite(uniteFinal.getDesignation());
            entre.setPrix(BigDecimal.ZERO);
            dtoEntreSorties.add(entre);
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            for (DepstoDetailDecoupage depstoDetailDecoupage : entity.getDepstoDetailDecoupageList()) {
                EntreSortie sortie = new EntreSortie();
                sortie.setNumbon(entity.getDecoupage().getNumbon());
                sortie.setEntree(BigDecimal.ZERO);
                sortie.setSortie(depstoDetailDecoupage.getQuantiteDecoupee());
                sortie.setCodeUnite(entity.getUniteOrigine());
                sortie.setDesignationUnite(uniteOrigin.getDesignation());
                sortie.setPrix(BigDecimal.ZERO);
                if (depstoDetailDecoupage.getDepsto().getDatPer() != null) {
                    sortie.setDatPer(Date.from(depstoDetailDecoupage.getDepsto().getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                sortie.setLotinter(depstoDetailDecoupage.getDepsto().getLotInter());
                dtoEntreSorties.add(sortie);
                EntreSortie entre = new EntreSortie();
                entre.setNumbon(entity.getDecoupage().getNumbon());
                entre.setEntree(depstoDetailDecoupage.getQuantiteDecoupee().multiply(entity.getQuantiteObtenue().divide(entity.getQuantite())));
                entre.setSortie(BigDecimal.ZERO);
                entre.setCodeUnite(entity.getUniteFinal());
                entre.setDesignationUnite(uniteFinal.getDesignation());
                entre.setPrix(BigDecimal.ZERO);
                if (depstoDetailDecoupage.getDepsto().getDatPer() != null) {
                    entre.setDatPer(Date.from(depstoDetailDecoupage.getDepsto().getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                entre.setLotinter(depstoDetailDecoupage.getDepsto().getLotInter());
                dtoEntreSorties.add(entre);
            }
            dto.setList(dtoEntreSorties);
            listMouvement.add(dto);
        }
        return listMouvement;

    }
}
