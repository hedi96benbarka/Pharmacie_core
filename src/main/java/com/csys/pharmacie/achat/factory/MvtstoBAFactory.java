/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.MvtStoBA;
import com.csys.pharmacie.achat.domain.MvtStoBAPK;
import com.csys.pharmacie.achat.dto.FactureBADTO;
import com.csys.pharmacie.achat.dto.MvtstoBADTO;
import com.csys.pharmacie.helper.BaseDetailBonFactory;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class MvtstoBAFactory extends BaseDetailBonFactory {

//    @Autowired
//    MessageSource messages;
//    public static MvtStoBA toEntity(MvtstoBADTO dto) {
//        MvtStoBA entity = new MvtStoBA();
//        toEntity(entity, dto);
//        entity.setDatPer(dto.getDatPer());
//        entity.setLotInter(dto.getLotInter());
//        entity.setRemise(dto.getRemise());
//        entity.setPrixVente(dto.getSellingPrice());
//        entity.setBaseTva(dto.getBaseTva());
//        return entity;
//    }
    public static MvtStoBA MvtStoBADTOTOMvtstoBA(MvtstoBADTO mvtstoBADTO, String ordre, FactureBADTO bonRecepDTO, String numbon) {

        MvtStoBA mvtstoBa = new MvtStoBA();
        toEntity(mvtstoBa, mvtstoBADTO);

        MvtStoBAPK pk = new MvtStoBAPK();
        pk.setCodart(mvtstoBADTO.getRefArt());
        pk.setNumbon(numbon);
        pk.setNumordre(ordre);
        mvtstoBa.setPk(pk);

        mvtstoBa.setLotInter(mvtstoBADTO.getLotInter());
        mvtstoBa.setPriuni(mvtstoBADTO.getPriuni());
        mvtstoBa.setRemise(mvtstoBADTO.getRemise());
        mvtstoBa.setMontht(mvtstoBADTO.getMontht());
        mvtstoBa.setTautva(mvtstoBADTO.getTauTVA());
        mvtstoBa.setCodtva(mvtstoBADTO.getCodTVA());
//                log.debug("le fournisseur est exonere :{}",f.getFournisseurExonere());

        mvtstoBa.setCategDepot(bonRecepDTO.getCategDepot());
        mvtstoBa.setCoddep(bonRecepDTO.getCoddep());
        mvtstoBa.setBaseTva(mvtstoBADTO.getBaseTva());

        mvtstoBa.setQtecom(mvtstoBADTO.getQuantite());
        mvtstoBa.setLotInter(mvtstoBADTO.getLotInter());
        mvtstoBa.setIsPrixReference(mvtstoBADTO.getIsPrixRef());
        mvtstoBa.setDatPer(mvtstoBADTO.getDatPer());
        mvtstoBa.setCodeArticleFournisseur(mvtstoBADTO.getCodeArtFrs());

        return mvtstoBa;

    }

    public static MvtstoBADTO toDTO(MvtStoBA entity, MvtstoBADTO dto) {

//        MvtstoBADTO dto = new MvtstoBADTO();
        toDTO(dto, entity);
        dto.setNumbon(entity.getFactureBA().getNumbon());
        dto.setNumAffiche(entity.getFactureBA().getNumaffiche());
        dto.setRefArt(entity.getCodart());
        dto.setDatPer(entity.getDatPer());

        dto.setLotInter(entity.getLotInter());
        dto.setNumOrdre(entity.getPk().getNumordre());
        dto.setRemise(entity.getRemise());
        dto.setPriuni(entity.getPriuni());
        dto.setCodTVA(entity.getCodtva());
        dto.setMontht(entity.getMontht());
        dto.setTauTVA(entity.getTautva());
        dto.setSellingPrice(entity.getPrixVente());
        dto.setBaseTva(entity.getBaseTva());
        dto.setCodeUnite(entity.getCodeUnite());
        if (entity.getPriuni().compareTo(BigDecimal.ZERO) == 0) {
            dto.setFree(true);
        } else if (entity.getPriuni().compareTo(BigDecimal.ZERO) == 1) {
            dto.setFree(false);
        }
//        if (entity.getDatPer() != null) {
//            Instant instant = entity.getDatPer().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
//            dto.setDatePerEdition(Date.from(instant));
//        }

        dto.setDateEdition(java.util.Date.from(entity.getFactureBA().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
        dto.setTypeMouvement(entity.getFactureBA().getTypbon().type());

        dto.setQteReturned(entity.getQuantite().subtract(entity.getQtecom()));
        dto.setIsPrixRef(entity.getIsPrixReference());
        dto.setCodeEmplacement(entity.getCodeEmplacement());
        dto.setQteCom(entity.getQtecom());
        dto.setIsCapitalize(entity.getIsCapitalize());
//        dto.setFactureBA(entity.getFactureBA());
        if (entity.getDatPer() != null) {
            dto.setDatePerEdition(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        return dto;
    }

    public static List<MvtstoBADTO> toDTOs(List<MvtStoBA> mvtstoBAs) {
        List<MvtstoBADTO> dtos = new ArrayList<>();
        mvtstoBAs.forEach(x -> {
            MvtstoBADTO dto = new MvtstoBADTO();
            dtos.add(toDTO(x, dto));
        });
        return dtos;
    }
//      DEPRECATED MEthods $ commented not deleted until cheking methods are 100% unuseful
//    public static MvtstoBADTO toDTOforupdate(MvtstoBADTO entity) {
//
//        MvtstoBADTO dto = new MvtstoBADTO();
//        dto.setRemise(entity.getRemise());
//        dto.setPriuni(entity.getPriuni());
//        dto.setRefArt(entity.getRefArt());
//        dto.setBaseTva(entity.getBaseTva());
//        dto.setCodeArtFrs(entity.getCodeArtFrs());
//        dto.setSellingPrice(entity.getSellingPrice());
//        return dto;
//    }

//    DEPRECATED MEthods $ commented not deleted until cheking methods are 100% unuseful
//    @Deprecated
//    public List<Mouvement> toMouvement(MvtStoBA entity, FournisseurDTO fournisseur, UniteDTO unite, TypeDateEnum typeDate) {
//        if (entity == null) {
//            return null;
//        }
//        Locale loc = LocaleContextHolder.getLocale();
//        List<Mouvement> listMouvement = new ArrayList<>();
//        Mouvement dto = new Mouvement();
//        dto.setDate(Date.from(entity.getFactureBA().getDatbon().atZone(ZoneId.systemDefault()).toInstant()));
//        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
//            dto.setDesignation(entity.getDesArtSec());
//        } else {
//            dto.setDesignation(entity.getDesart());
//        }
//        dto.setCodeSaisi(entity.getCodeSaisi());
//        dto.setNumaffiche(entity.getFactureBA().getNumaffiche());
//        dto.setNumbon(entity.getFactureBA().getNumbon());
//        dto.setCoddep(entity.getFactureBA().getCoddep());
//        dto.setLibelle(fournisseur.getDesignation() + " " + fournisseur.getCode());
//        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO)) {
//            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
//            EntreSortie dtoEntreSortie = new EntreSortie();
//            dtoEntreSortie.setNumbon(entity.getFactureBA().getNumbon());
//            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
//            if (entity.getDatPer() != null) {
//                dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//            }
//            dtoEntreSortie.setLotinter(entity.getLotInter());
//            if (entity.getFactureBA().getTypbon().equals(TypeBonEnum.BA)) {
//                dto.setOperation(messages.getMessage("ficheStock.reception", null, loc));
//                dtoEntreSortie.setEntree(entity.getQuantite());
//                dtoEntreSortie.setSortie(BigDecimal.ZERO);
//            } else {
//                dto.setOperation(messages.getMessage("ficheStock.retour", null, loc));
//                dtoEntreSortie.setEntree(BigDecimal.ZERO);
//                dtoEntreSortie.setSortie(entity.getQuantite());
//            }
//            dtoEntreSortie.setPrix(entity.getPriuni());
//            dtoEntreSortie.setCodeUnite(entity.getCodeUnite());
//            dtoEntreSorties.add(dtoEntreSortie);
//            dto.setList(dtoEntreSorties);
//            listMouvement.add(dto);
//        } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
//            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
//            EntreSortie dtoEntreSortie = new EntreSortie();
//            dtoEntreSortie.setNumbon(entity.getFactureBA().getNumbon());
//            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
//            if (entity.getFactureBA().getTypbon().equals(TypeBonEnum.BA)) {
//                dto.setOperation(messages.getMessage("ficheStock.reception", null, loc));
//                dtoEntreSortie.setEntree(entity.getQuantite());
//                dtoEntreSortie.setSortie(BigDecimal.ZERO);
//            } else {
//                dto.setOperation(messages.getMessage("ficheStock.retour", null, loc));
//                dtoEntreSortie.setEntree(BigDecimal.ZERO);
//                dtoEntreSortie.setSortie(entity.getQuantite());
//            }
//            dtoEntreSortie.setPrix(entity.getPriuni());
//            dtoEntreSortie.setCodeUnite(entity.getCodeUnite());
//            dtoEntreSorties.add(dtoEntreSortie);
//            dto.setList(dtoEntreSorties);
//            listMouvement.add(dto);
//        } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
//            if (entity.getFactureBA().getTypbon().equals(TypeBonEnum.BA)) {
//                List<EntreSortie> dtoEntreSorties = new ArrayList<>();
//                EntreSortie dtoEntreSortie = new EntreSortie();
//                dtoEntreSortie.setNumbon(entity.getFactureBA().getNumbon());
//                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
//                if (entity.getDatPer() != null) {
//                    dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                }
//                dtoEntreSortie.setLotinter(entity.getLotInter());
//                if (entity.getFactureBA().getTypbon().equals(TypeBonEnum.BA)) {
//                    dto.setOperation(messages.getMessage("ficheStock.reception", null, loc));
//                    dtoEntreSortie.setEntree(entity.getQuantite());
//                    dtoEntreSortie.setSortie(BigDecimal.ZERO);
//                } else {
//                    dto.setOperation(messages.getMessage("ficheStock.retour", null, loc));
//                    dtoEntreSortie.setEntree(BigDecimal.ZERO);
//                    dtoEntreSortie.setSortie(entity.getQuantite());
//                }
//                dtoEntreSortie.setPrix(entity.getPriuni());
//                dtoEntreSortie.setCodeUnite(entity.getCodeUnite());
//                dtoEntreSorties.add(dtoEntreSortie);
//                dto.setList(dtoEntreSorties);
//                listMouvement.add(dto);
//            } else {
//                List<EntreSortie> dtoEntreSorties = new ArrayList<>();
//                for (DetailMvtStoBA detailMvtStoBA : entity.getDetailMvtStoBACollection()) {
//                    EntreSortie dtoEntreSortie = new EntreSortie();
//                    dtoEntreSortie.setNumbon(entity.getFactureBA().getNumbon());
//                    dtoEntreSortie.setDesignationUnite(unite.getDesignation());
//                    if (detailMvtStoBA.getDatPer() != null) {
//                        dtoEntreSortie.setDatPer(Date.from(detailMvtStoBA.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                    }
//                    dtoEntreSortie.setLotinter(detailMvtStoBA.getLotinter());
//                    if (entity.getFactureBA().getTypbon().equals(TypeBonEnum.BA)) {
//                        dto.setOperation(messages.getMessage("ficheStock.reception", null, loc));
//                        dtoEntreSortie.setEntree(detailMvtStoBA.getQuantite_retourne());
//                        dtoEntreSortie.setSortie(BigDecimal.ZERO);
//                    } else {
//                        dto.setOperation(messages.getMessage("ficheStock.retour", null, loc));
//                        dtoEntreSortie.setEntree(BigDecimal.ZERO);
//                        dtoEntreSortie.setSortie(detailMvtStoBA.getQuantite_retourne());
//                    }
//                    dtoEntreSortie.setPrix(entity.getPriuni());
//                    dtoEntreSortie.setCodeUnite(entity.getCodeUnite());
//                    dtoEntreSorties.add(dtoEntreSortie);
//                }
//                dto.setList(dtoEntreSorties);
//                listMouvement.add(dto);
//            }
//
//        }
//        return listMouvement;
//
//    }
    //    @Deprecated
//    public List<Mouvement> toMouvementAnnule(MvtStoBA entity, FournisseurDTO fournisseur, UniteDTO unite, TypeDateEnum typeDate) {
//        if (entity == null) {
//            return null;
//        }
//        Locale loc = LocaleContextHolder.getLocale();
//        List<Mouvement> listMouvement = new ArrayList<>();
//        Mouvement dto = new Mouvement();
//        dto.setDate(Date.from(entity.getFactureBA().getDatAnnul().atZone(ZoneId.systemDefault()).toInstant()));
//        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
//            dto.setDesignation(entity.getDesArtSec());
//        } else {
//            dto.setDesignation(entity.getDesart());
//        }
//        dto.setCodeSaisi(entity.getCodeSaisi());
//        dto.setNumaffiche(entity.getFactureBA().getNumaffiche());
//        dto.setNumbon("A" + entity.getFactureBA().getNumbon());
//        dto.setCoddep(entity.getFactureBA().getCoddep());
//        dto.setLibelle(fournisseur.getDesignation() + " " + fournisseur.getCode());
//        if (typeDate.equals(TypeDateEnum.WITH_DATE_MVTSTO)) {
//            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
//            EntreSortie dtoEntreSortie = new EntreSortie();
//            dtoEntreSortie.setNumbon("A" + entity.getFactureBA().getNumbon());
//            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
//            if (entity.getDatPer() != null) {
//                dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//            }
//            dtoEntreSortie.setLotinter(entity.getLotInter());
//            if (entity.getFactureBA().getTypbon().equals(TypeBonEnum.BA)) {
//                dto.setOperation(messages.getMessage("ficheStock.annulationreception", null, loc));
//                dtoEntreSortie.setEntree(BigDecimal.ZERO);
//                dtoEntreSortie.setSortie(entity.getQuantite());
//            } else {
//                dto.setOperation(messages.getMessage("ficheStock.annulationretour", null, loc));
//                dtoEntreSortie.setEntree(entity.getQuantite());
//                dtoEntreSortie.setSortie(BigDecimal.ZERO);
//            }
//            dtoEntreSortie.setPrix(entity.getPriuni());
//            dtoEntreSortie.setCodeUnite(entity.getCodeUnite());
//            dtoEntreSorties.add(dtoEntreSortie);
//            dto.setList(dtoEntreSorties);
//            listMouvement.add(dto);
//        } else if (typeDate.equals(TypeDateEnum.WITHOUT_DATE)) {
//            List<EntreSortie> dtoEntreSorties = new ArrayList<>();
//            EntreSortie dtoEntreSortie = new EntreSortie();
//            dtoEntreSortie.setNumbon("A" + entity.getFactureBA().getNumbon());
//            dtoEntreSortie.setDesignationUnite(unite.getDesignation());
//            if (entity.getFactureBA().getTypbon().equals(TypeBonEnum.BA)) {
//                dto.setOperation(messages.getMessage("ficheStock.annulationreception", null, loc));
//                dtoEntreSortie.setEntree(BigDecimal.ZERO);
//                dtoEntreSortie.setSortie(entity.getQuantite());
//            } else {
//                dto.setOperation(messages.getMessage("ficheStock.annulationretour", null, loc));
//                dtoEntreSortie.setEntree(entity.getQuantite());
//                dtoEntreSortie.setSortie(BigDecimal.ZERO);
//            }
//            dtoEntreSortie.setPrix(entity.getPriuni());
//            dtoEntreSortie.setCodeUnite(entity.getCodeUnite());
//            dtoEntreSorties.add(dtoEntreSortie);
//            dto.setList(dtoEntreSorties);
//            listMouvement.add(dto);
//        } else if (typeDate.equals(TypeDateEnum.WITH_DATE_DETAIL)) {
//            if (entity.getFactureBA().getTypbon().equals(TypeBonEnum.BA)) {
//                List<EntreSortie> dtoEntreSorties = new ArrayList<>();
//                EntreSortie dtoEntreSortie = new EntreSortie();
//                dtoEntreSortie.setNumbon("A" + entity.getFactureBA().getNumbon());
//                dtoEntreSortie.setDesignationUnite(unite.getDesignation());
//                if (entity.getDatPer() != null) {
//                    dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                }
//                dtoEntreSortie.setLotinter(entity.getLotInter());
//                if (entity.getFactureBA().getTypbon().equals(TypeBonEnum.BA)) {
//                    dto.setOperation(messages.getMessage("ficheStock.annulationreception", null, loc));
//                    dtoEntreSortie.setEntree(BigDecimal.ZERO);
//                    dtoEntreSortie.setSortie(entity.getQuantite());
//                } else {
//                    dto.setOperation(messages.getMessage("ficheStock.annulationretour", null, loc));
//                    dtoEntreSortie.setEntree(entity.getQuantite());
//                    dtoEntreSortie.setSortie(BigDecimal.ZERO);
//                }
//                dtoEntreSortie.setPrix(entity.getPriuni());
//                dtoEntreSortie.setCodeUnite(entity.getCodeUnite());
//                dtoEntreSorties.add(dtoEntreSortie);
//                dto.setList(dtoEntreSorties);
//                listMouvement.add(dto);
//            } else {
//                List<EntreSortie> dtoEntreSorties = new ArrayList<>();
//                for (DetailMvtStoBA detailMvtStoBA : entity.getDetailMvtStoBACollection()) {
//                    EntreSortie dtoEntreSortie = new EntreSortie();
//                    dtoEntreSortie.setNumbon("A" + entity.getFactureBA().getNumbon());
//                    dtoEntreSortie.setDesignationUnite(unite.getDesignation());
//                    if (detailMvtStoBA.getDatPer() != null) {
//                        dtoEntreSortie.setDatPer(Date.from(detailMvtStoBA.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                    }
//                    dtoEntreSortie.setLotinter(detailMvtStoBA.getLotinter());
//                    if (entity.getFactureBA().getTypbon().equals(TypeBonEnum.BA)) {
//                        dto.setOperation(messages.getMessage("ficheStock.annulationreception", null, loc));
//                        dtoEntreSortie.setEntree(BigDecimal.ZERO);
//                        dtoEntreSortie.setSortie(detailMvtStoBA.getQuantite_retourne());
//                    } else {
//                        dto.setOperation(messages.getMessage("ficheStock.annulationretour", null, loc));
//                        dtoEntreSortie.setEntree(detailMvtStoBA.getQuantite_retourne());
//                        dtoEntreSortie.setSortie(BigDecimal.ZERO);
//                    }
//                    dtoEntreSortie.setPrix(entity.getPriuni());
//                    dtoEntreSortie.setCodeUnite(entity.getCodeUnite());
//                    dtoEntreSorties.add(dtoEntreSortie);
//                }
//                dto.setList(dtoEntreSorties);
//                listMouvement.add(dto);
//            }
//
//        }
//        return listMouvement;
//
//    }
}
