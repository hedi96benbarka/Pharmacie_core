package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TypeBonEnum;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.transfert.domain.DetailMvtStoBT;
import com.csys.pharmacie.transfert.domain.MvtStoBT;
import com.csys.pharmacie.transfert.domain.MvtStoBTPK;
import com.csys.pharmacie.transfert.dto.DetailsTransfertDTO;
import com.csys.pharmacie.transfert.dto.MvtstoBTDTO;
import com.csys.pharmacie.transfert.dto.MvtstoBTEditionDTO;
import com.csys.pharmacie.vente.quittance.domain.Mvtsto;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MvtstoBTFactory {

    static String LANGUAGE_SEC;
    private static String validationTransfertAuto;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    @Value("${validation-transfert-auto}")
    public void setValidation(String validation) {
        validationTransfertAuto = validation;
    }

    @Autowired
    MessageSource messages;
    private final Logger log = LoggerFactory.getLogger(MvtstoBTFactory.class);

    public static MvtstoBTDTO mvtstoBTToMvtstoBTDTO(MvtStoBT mvtstoBT) {
        MvtstoBTDTO mvtstoBTDTO = new MvtstoBTDTO();
        mvtstoBTDTO.setCode(mvtstoBT.getCode());
        mvtstoBTDTO.setArticleID(mvtstoBT.getCodart());
        mvtstoBTDTO.setNumbon(mvtstoBT.getNumbon());
        mvtstoBTDTO.setNumordre(mvtstoBT.getNumordre());
        mvtstoBTDTO.setLotInter(mvtstoBT.getLotinter());
        mvtstoBTDTO.setUnityPrice(mvtstoBT.getPriuni());
        mvtstoBTDTO.setPreemptionDate(mvtstoBT.getDatPer());
        mvtstoBTDTO.setCategDepot(mvtstoBT.getCategDepot());

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstoBTDTO.setDesignation(mvtstoBT.getDesArtSec());
            mvtstoBTDTO.setSecondDesignation(mvtstoBT.getDesart());
        } else {
            mvtstoBTDTO.setDesignation(mvtstoBT.getDesArtSec());
            mvtstoBTDTO.setSecondDesignation(mvtstoBT.getDesart());
        }
        mvtstoBTDTO.setCodeSaisi(mvtstoBT.getCodeSaisi());
        mvtstoBTDTO.setQuantity(mvtstoBT.getQuantite());
        mvtstoBTDTO.setUnityID(mvtstoBT.getUnite());
        mvtstoBTDTO.setQteben(mvtstoBT.getQteben());
        mvtstoBTDTO.setQuantiteDefectueuse(mvtstoBT.getQuantiteDefectueuse());
        mvtstoBTDTO.setQuantiteRecue(mvtstoBT.getQuantiteRecue());
        return mvtstoBTDTO;
    }

    public static MvtstoBTEditionDTO mvtstoBTToMvtstoBTEditionDTO(MvtStoBT mvtstoBT) {
        MvtstoBTEditionDTO mvtstoBTDTO = new MvtstoBTEditionDTO();
        mvtstoBTDTO.setArticleID(mvtstoBT.getCodart());
        mvtstoBTDTO.setNumbon(mvtstoBT.getNumbon());
        mvtstoBTDTO.setNumordre(mvtstoBT.getNumordre());
        mvtstoBTDTO.setLotInter(mvtstoBT.getLotinter());
        if (mvtstoBT.getDatPer() != null) {
            mvtstoBTDTO.setPreemptionDate(Date.from(mvtstoBT.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        mvtstoBTDTO.setUnityPrice(mvtstoBT.getPriuni());
        mvtstoBTDTO.setCategDepot(mvtstoBT.getCategDepot());

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstoBTDTO.setDesignation(mvtstoBT.getDesArtSec());
            mvtstoBTDTO.setSecondDesignation(mvtstoBT.getDesart());
        } else {

            mvtstoBTDTO.setDesignation(mvtstoBT.getDesart());
            mvtstoBTDTO.setSecondDesignation(mvtstoBT.getDesArtSec());
        }
        mvtstoBTDTO.setCodeSaisi(mvtstoBT.getCodeSaisi());
        mvtstoBTDTO.setQuantity(mvtstoBT.getQuantite());
        mvtstoBTDTO.setUnityID(mvtstoBT.getUnite());
        mvtstoBTDTO.setQuantiteDefectueuse(mvtstoBT.getQuantiteDefectueuse());
        mvtstoBTDTO.setQuantiteRecue(mvtstoBT.getQuantiteRecue());
        return mvtstoBTDTO;
    }

    public static MvtStoBT mvtstoBTDTOToMvtstoBT(MvtstoBTDTO mvtstoBTDTO) {
        MvtStoBT mvtstoBT = new MvtStoBT();
        MvtStoBTPK mvtstoBTPK = new MvtStoBTPK();
        mvtstoBTPK.setCodart(mvtstoBTDTO.getArticleID());
        mvtstoBTPK.setNumbon(mvtstoBTDTO.getNumbon());
        mvtstoBT.setCodart(mvtstoBTDTO.getArticleID());
        mvtstoBT.setNumbon(mvtstoBTDTO.getNumbon());
        mvtstoBT.setNumordre(mvtstoBTDTO.getNumordre());
        mvtstoBT.setLotinter(mvtstoBTDTO.getLotInter());
        mvtstoBT.setPriuni(mvtstoBTDTO.getUnityPrice());
        mvtstoBT.setDatPer(mvtstoBTDTO.getPreemptionDate());
        mvtstoBT.setUnite(mvtstoBTDTO.getUnityID());
        mvtstoBT.setCategDepot(mvtstoBTDTO.getCategDepot());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            mvtstoBT.setDesart(mvtstoBTDTO.getSecondDesignation());
            mvtstoBT.setDesArtSec(mvtstoBTDTO.getDesignation());
        } else {
            mvtstoBT.setDesart(mvtstoBTDTO.getDesignation());
            mvtstoBT.setDesArtSec(mvtstoBTDTO.getSecondDesignation());
        }
        mvtstoBT.setCodeSaisi(mvtstoBTDTO.getCodeSaisi());
        mvtstoBT.setQuantite(mvtstoBTDTO.getQuantity());
        mvtstoBT.setQteben(mvtstoBTDTO.getQuantity());
        mvtstoBT.setQuantiteDefectueuse(mvtstoBTDTO.getQuantiteDefectueuse());
        mvtstoBT.setQuantiteRecue(mvtstoBTDTO.getQuantiteRecue());
        if (validationTransfertAuto.equalsIgnoreCase("true")) {
            mvtstoBT.setQuantiteRecue(mvtstoBT.getQuantite());
            mvtstoBT.setQuantiteDefectueuse(BigDecimal.ZERO);
        } else {
            mvtstoBT.setQuantiteDefectueuse(mvtstoBTDTO.getQuantiteDefectueuse());
            mvtstoBT.setQuantiteRecue(mvtstoBTDTO.getQuantiteRecue());
        }
        return mvtstoBT;
    }

    public static MvtStoBT mvtstoToMvtstoBT(MvtStoBA mvtStoBA) {
        MvtStoBT mvtstoBT = new MvtStoBT();
        MvtStoBTPK mvtstoBTPK = new MvtStoBTPK();
        mvtstoBTPK.setCodart(mvtStoBA.getPk().getCodart());
        mvtstoBT.setLotinter(mvtStoBA.getLotInter());
        mvtstoBT.setPriuni(mvtStoBA.getPriuni());
        mvtstoBT.setDatPer(mvtStoBA.getDatPer());
        mvtstoBT.setUnite(mvtStoBA.getCodeUnite());
        mvtstoBT.setCategDepot(mvtStoBA.getCategDepot());
        mvtstoBT.setDesart(mvtStoBA.getDesart());
        mvtstoBT.setDesArtSec(mvtStoBA.getDesArtSec());
        mvtstoBT.setCodeSaisi(mvtStoBA.getCodeSaisi());
        mvtstoBT.setQuantite(mvtStoBA.getQuantite());
        mvtstoBT.setCodeSaisi(mvtStoBA.getCodeSaisi());
        mvtstoBT.setTauxTvaAchat(mvtStoBA.getTautva());
        mvtstoBT.setCodeTvaAchat(mvtStoBA.getCodtva());
        mvtstoBT.setQteben(BigDecimal.ZERO);
        mvtstoBT.setQuantiteRecue(mvtstoBT.getQuantite());
        mvtstoBT.setQuantiteDefectueuse(BigDecimal.ZERO);
        return mvtstoBT;
    }

    public static DetailsTransfertDTO mvtstoBTToDetailsTransfertDTO(MvtStoBT mvt) {
        DetailsTransfertDTO detailTransfertDTO = new DetailsTransfertDTO();
        detailTransfertDTO.setCodeArticle(mvt.getCodart());
        detailTransfertDTO.setNumbon(mvt.getNumbon());
        detailTransfertDTO.setNumordre(mvt.getNumordre());
        detailTransfertDTO.setLotInter(mvt.getLotinter());
        detailTransfertDTO.setPrixUnitaire(mvt.getPriuni());
        detailTransfertDTO.setInterdepot(mvt.getFactureBT().getInterdepot());
        detailTransfertDTO.setAvoirTransfert(mvt.getFactureBT().isAvoirTransfert());
        detailTransfertDTO.setQuantiteRecue(mvt.getQuantiteRecue());
        detailTransfertDTO.setQuantiteDefectueuse(mvt.getQuantiteDefectueuse());
        detailTransfertDTO.setConforme(mvt.getFactureBT().getConforme());
        detailTransfertDTO.setValide(mvt.getFactureBT().getValide());
        detailTransfertDTO.setUserValidate(mvt.getFactureBT().getUserValidate());

        /* deleteTva
        detailTransfertDTO.setTauxTvaA(mvt.getTauxTvaAchat());
        detailTransfertDTO.setCodeTvaA(mvt.getCodeTvaAchat());
         */
        if (mvt.getDatPer() != null) {
            Instant instant = mvt.getDatPer().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            detailTransfertDTO.setDatePeremption(Date.from(instant));
        }
        detailTransfertDTO.setCategDepot(mvt.getCategDepot());

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            detailTransfertDTO.setDesignation(mvt.getDesArtSec());
            detailTransfertDTO.setDesignationSecondaire(mvt.getDesart());
        } else {
            detailTransfertDTO.setDesignation(mvt.getDesart());
            detailTransfertDTO.setDesignationSecondaire(mvt.getDesArtSec());
        }
        detailTransfertDTO.setCodeSaisi(mvt.getCodeSaisi());
        detailTransfertDTO.setQuantite(mvt.getQuantite());
        detailTransfertDTO.setCodeUnite(mvt.getUnite());

        detailTransfertDTO.setNumAffiche(mvt.getFactureBT().getNumaffiche());
        detailTransfertDTO.setCodeDepotSource(mvt.getFactureBT().getCoddep());
        detailTransfertDTO.setCodeDepotDest(mvt.getFactureBT().getDeptr());
        detailTransfertDTO.setDateCreate(java.util.Date.from(mvt.getFactureBT().getDatbon().atZone(ZoneId.systemDefault())
                .toInstant()));

        //            Instant instant1 = matchedFactureBT.getDatbon().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
//            detailTransfertDTO.setDateCreate(Date.from(instant1));
//            detailTransfertDTO.setDateCreate(matchedFactureBT.getDatbon());      
        return detailTransfertDTO;
    }

    public static List<MvtstoBTDTO> mvtstoBTToMvtstoBTDTOs(List<MvtStoBT> mvtstoBTs) {
        List<MvtstoBTDTO> mvtstoBTsDTO = new ArrayList<>();
        mvtstoBTs.forEach(x -> {
            mvtstoBTsDTO.add(mvtstoBTToMvtstoBTDTO(x));
        });
        return mvtstoBTsDTO;
    }

    public static List<MvtstoBTEditionDTO> mvtstoBTToMvtstoBTEditionDTOs(List<MvtStoBT> mvtstoBTs) {
        List<MvtstoBTEditionDTO> mvtstoBTEditionDTO = new ArrayList<>();
        mvtstoBTs.forEach(x -> {
            mvtstoBTEditionDTO.add(mvtstoBTToMvtstoBTEditionDTO(x));
        });
        return mvtstoBTEditionDTO;
    }

    public static List<MvtStoBT> mvtstoBTDTOToMvtstoBTs(List<MvtstoBTDTO> mvtstoBTDTOs) {
        List<MvtStoBT> mvtstoBTs = new ArrayList<>();
        mvtstoBTDTOs.forEach(x -> {
            mvtstoBTs.add(mvtstoBTDTOToMvtstoBT(x));
        });
        return mvtstoBTs;
    }

    public List<Mouvement> toMouvement(MvtStoBT entity, UniteDTO unite, DepotDTO codedep, DepotDTO deptr, Integer coddep, TypeDateEnum typeDate) {
        if (entity == null) {
            return null;
        }
        List<Mouvement> listMouvement = new ArrayList<>();
        Locale loc = LocaleContextHolder.getLocale();
        if (coddep.equals(entity.getFactureBT().getCoddep())) {
            Mouvement dto = new Mouvement();
            dto.setDate(Date.from(entity.getFactureBT().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
            if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
                dto.setDesignation(entity.getDesArtSec());
            } else {
                dto.setDesignation(entity.getDesart());
            }
            if (entity.getFactureBT().getInterdepot()) {
                dto.setOperation(messages.getMessage("ficheStock.transfert", null, loc));
            } else {
                dto.setOperation(messages.getMessage("ficheStock.transfertRecup", null, loc));
            }
            dto.setNumbon(entity.getFactureBT().getNumbon() + "1");
            dto.setNumaffiche(entity.getFactureBT().getNumaffiche());

            if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO) || (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL) && entity.getFactureBT().getAutomatique())) {
                List<EntreSortie> dtoEntreSorties = new ArrayList<>();
                EntreSortie dtoEntreSortie = new EntreSortie();
                dtoEntreSortie.setNumbon(entity.getFactureBT().getNumbon() + "1");
                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                dtoEntreSortie.setCodeUnite(entity.getUnite());
                if (entity.getDatPer() != null) {
                    dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                dtoEntreSortie.setLotinter(entity.getLotinter());
                dto.setLibelle(deptr.getDesignation() + " " + deptr.getCodeSaisi());
                dto.setCoddep(entity.getFactureBT().getCoddep());
                dto.setCodeSaisi(entity.getCodeSaisi());
                dtoEntreSortie.setEntree(BigDecimal.ZERO);
                dtoEntreSortie.setSortie(entity.getQuantite());
                dtoEntreSortie.setPrix(entity.getPriuni());
                dtoEntreSorties.add(dtoEntreSortie);
                dto.setList(dtoEntreSorties);
                listMouvement.add(dto);

            } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
                List<EntreSortie> dtoEntreSorties = new ArrayList<>();
                EntreSortie dtoEntreSortie = new EntreSortie();
                dtoEntreSortie.setNumbon(entity.getFactureBT().getNumbon() + "1");
                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                dtoEntreSortie.setCodeUnite(entity.getUnite());
                dto.setLibelle(deptr.getDesignation() + " " + deptr.getCodeSaisi());
                dto.setCoddep(entity.getFactureBT().getCoddep());
                dto.setCodeSaisi(entity.getCodeSaisi());
                dtoEntreSortie.setEntree(BigDecimal.ZERO);
                dtoEntreSortie.setSortie(entity.getQuantite());
                dtoEntreSortie.setPrix(entity.getPriuni());
                dtoEntreSorties.add(dtoEntreSortie);
                dto.setList(dtoEntreSorties);
                listMouvement.add(dto);
            } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
                dto.setLibelle(deptr.getDesignation() + " " + deptr.getCodeSaisi());
                dto.setCoddep(entity.getFactureBT().getCoddep());
                dto.setCodeSaisi(entity.getCodeSaisi());
                List<EntreSortie> dtoEntreSorties = new ArrayList<>();
                for (DetailMvtStoBT detailMvtStoBT : entity.getDetailMvtStoBTList()) {
                    EntreSortie dtoEntreSortie = new EntreSortie();
                    dtoEntreSortie.setNumbon(entity.getFactureBT().getNumbon() + "1");
                    dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                    dtoEntreSortie.setCodeUnite(entity.getUnite());

                    dtoEntreSortie.setDatPer(Date.from(detailMvtStoBT.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));

                    dtoEntreSortie.setLotinter(detailMvtStoBT.getLotinter());
                    dtoEntreSortie.setEntree(BigDecimal.ZERO);
                    dtoEntreSortie.setSortie(detailMvtStoBT.getQuantitePrelevee());
                    dtoEntreSortie.setPrix(entity.getPriuni());
                    dtoEntreSorties.add(dtoEntreSortie);
                }
                dto.setList(dtoEntreSorties);
                listMouvement.add(dto);
            }
        }
        if (coddep.equals(entity.getFactureBT().getDeptr())) {
            Mouvement dto = new Mouvement();
            dto.setDate(Date.from(entity.getFactureBT().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
            if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
                dto.setDesignation(entity.getDesArtSec());
            } else {
                dto.setDesignation(entity.getDesart());
            }
            if (entity.getFactureBT().getInterdepot()) {
                dto.setOperation(messages.getMessage("ficheStock.transfert", null, loc));
            } else {
                dto.setOperation(messages.getMessage("ficheStock.transfertRecup", null, loc));
            }
            dto.setNumbon(entity.getFactureBT().getNumbon() + "2");
            dto.setNumaffiche(entity.getFactureBT().getNumaffiche());
            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
            if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO) || (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL) && entity.getFactureBT().getAutomatique())) {
                EntreSortie dtoEntreSortie = new EntreSortie();
                dtoEntreSortie.setNumbon(entity.getFactureBT().getNumbon() + "2");
                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                dtoEntreSortie.setCodeUnite(entity.getUnite());
                if (entity.getDatPer() != null) {
                    dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
                dtoEntreSortie.setLotinter(entity.getLotinter());
                dto.setLibelle(codedep.getDesignation() + " " + codedep.getCodeSaisi());
                dto.setCoddep(entity.getFactureBT().getDeptr());
                dto.setCodeSaisi(entity.getCodeSaisi());
                dtoEntreSortie.setEntree(entity.getQuantite());
                dtoEntreSortie.setSortie(BigDecimal.ZERO);
                dtoEntreSortie.setPrix(entity.getPriuni());
                dtoEntreSorties.add(dtoEntreSortie);
                dto.setList(dtoEntreSorties);
                listMouvement.add(dto);

            } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
                EntreSortie dtoEntreSortie = new EntreSortie();
                dtoEntreSortie.setNumbon(entity.getFactureBT().getNumbon() + "2");
                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                dtoEntreSortie.setCodeUnite(entity.getUnite());
                dto.setLibelle(codedep.getDesignation() + " " + codedep.getCodeSaisi());
                dto.setCoddep(entity.getFactureBT().getDeptr());
                dto.setCodeSaisi(entity.getCodeSaisi());
                dtoEntreSortie.setEntree(entity.getQuantite());
                dtoEntreSortie.setSortie(BigDecimal.ZERO);
                dtoEntreSortie.setPrix(entity.getPriuni());
                dtoEntreSorties.add(dtoEntreSortie);
                dto.setList(dtoEntreSorties);
                listMouvement.add(dto);
            } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
                dto.setLibelle(codedep.getDesignation() + " " + codedep.getCodeSaisi());
                dto.setCoddep(entity.getFactureBT().getDeptr());
                dto.setCodeSaisi(entity.getCodeSaisi());
                for (DetailMvtStoBT detailMvtStoBT : entity.getDetailMvtStoBTList()) {
                    EntreSortie dtoEntreSortie = new EntreSortie();
                    dtoEntreSortie.setNumbon(entity.getFactureBT().getNumbon() + "2");
                    dtoEntreSortie.setDesignationUnite(unite.getDesignation());
                    dtoEntreSortie.setCodeUnite(entity.getUnite());
                    if (entity.getDatPer() != null) {
                        dtoEntreSortie.setDatPer(Date.from(detailMvtStoBT.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    }
                    dtoEntreSortie.setLotinter(detailMvtStoBT.getLotinter());
                    dtoEntreSortie.setEntree(detailMvtStoBT.getQuantitePrelevee());
                    dtoEntreSortie.setSortie(BigDecimal.ZERO);
                    dtoEntreSortie.setPrix(entity.getPriuni());
                    dtoEntreSorties.add(dtoEntreSortie);
                }
                dto.setList(dtoEntreSorties);
                listMouvement.add(dto);

            }
        }
        return listMouvement;

    }
}
