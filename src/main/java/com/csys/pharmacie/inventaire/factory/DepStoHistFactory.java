package com.csys.pharmacie.inventaire.factory;

import com.csys.pharmacie.helper.EntreSortie;
import com.csys.pharmacie.helper.Mouvement;
import com.csys.pharmacie.helper.TypeDateEnum;
import com.csys.pharmacie.inventaire.domain.DepStoHist;
import com.csys.pharmacie.inventaire.domain.Inventaire;
import com.csys.pharmacie.inventaire.dto.DepStoHistDTO;
import com.csys.pharmacie.stock.dto.DepstoDTO;
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
public class DepStoHistFactory {

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }
    @Autowired
    MessageSource messages;

    public static DepStoHistDTO depstohistToDepStoHistDTO(DepStoHist depstohist) {
        DepStoHistDTO depstohistDTO = new DepStoHistDTO();
        depstohistDTO.setCodeArticle(depstohist.getCodeArticle());
        depstohistDTO.setArticledesignationAr(depstohist.getArticledesignationAr());
        depstohistDTO.setArticleDesignation(depstohist.getArticleDesignation());
        depstohistDTO.setCodeCategorieArticle(depstohist.getCodeCategorieArticle());
        depstohistDTO.setCodeUnite(depstohist.getCodeUnite());
        depstohistDTO.setNum(depstohist.getNum());
        depstohistDTO.setLot(depstohist.getLot());
        depstohistDTO.setStkDep(depstohist.getStkDep());
        depstohistDTO.setQte0(depstohist.getQte0());
        depstohistDTO.setPu(depstohist.getPu());
        depstohistDTO.setPuTotReel(depstohist.getPuTotReel());
        depstohistDTO.setPuTotTheorique(depstohist.getPuTotTheorique());
        depstohistDTO.setDatPer(depstohist.getDatPer());
        depstohistDTO.setDatPerEdition(java.util.Date.from(depstohist.getDatPer().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()));
        depstohistDTO.setNumInventaire(depstohist.getInventaire().getCode());
        depstohistDTO.setCodeSaisie(depstohist.getCodeSaisie());
        depstohistDTO.setCodeTva(depstohist.getCodeTva());
        depstohistDTO.setTauxTva(depstohist.getTauxTva());
        return depstohistDTO;
    }

    public static DepStoHist depstohistDTOToDepStoHist(DepStoHistDTO depstohistDTO) {
        DepStoHist depstohist = new DepStoHist();
        depstohist.setCodeArticle(depstohistDTO.getCodeArticle());
        depstohist.setArticledesignationAr(depstohistDTO.getArticledesignationAr());
        depstohist.setArticleDesignation(depstohistDTO.getArticleDesignation());
        depstohist.setCodeCategorieArticle(depstohistDTO.getCodeCategorieArticle());
        depstohist.setCodeUnite(depstohistDTO.getCodeUnite());
        depstohist.setNum(depstohistDTO.getNum());
        depstohist.setLot(depstohistDTO.getLot());
        depstohist.setStkDep(depstohistDTO.getStkDep());
        depstohist.setQte0(depstohistDTO.getQte0());
        depstohist.setPu(depstohistDTO.getPu());
        depstohist.setPuTotReel(depstohistDTO.getPuTotReel());
        depstohist.setPuTotTheorique(depstohistDTO.getPuTotTheorique());
        depstohist.setDatPer(depstohistDTO.getDatPer());
        depstohist.setCodeSaisie(depstohistDTO.getCodeSaisie());
        depstohist.setCodeTva(depstohistDTO.getCodeTva());
        depstohist.setTauxTva(depstohistDTO.getTauxTva());
        return depstohist;
    }

    public static DepStoHist depstoDTOToDepstoHist(DepstoDTO d, Inventaire inventaire) {
        DepStoHist depstoHist = new DepStoHist();
        depstoHist.setInventaire(inventaire);
        depstoHist.setCodeArticle(d.getArticleID());
        depstoHist.setArticleDesignation(d.getDesignation());
        depstoHist.setArticledesignationAr(d.getDesignationSec());
        depstoHist.setCodeCategorieArticle(Integer.parseInt(d.getCategorieArticle()));
        depstoHist.setStkDep(d.getQuantiteReel());
        depstoHist.setQte0(d.getQuantiteTheorique());
        depstoHist.setPu(d.getPrixAchat());
        depstoHist.setCodeSaisie(d.getCodeSaisiArticle());
        if (d.getLotInter() != null) {
            depstoHist.setLot(d.getLotInter());
        } else {
            depstoHist.setLot("");
        }
        depstoHist.setCodeUnite(d.getUnityCode());
        depstoHist.setPuTotReel(d.getQuantiteReel().multiply(d.getPrixAchat()));
        depstoHist.setPuTotTheorique(d.getQuantiteTheorique().multiply(d.getPrixAchat()));
        depstoHist.setDatPer(d.getPreemptionDate());
        depstoHist.setCodeTva(d.getCodeTva());
        depstoHist.setTauxTva(d.getTauxTva());
        return depstoHist;
    }

    public static List<DepStoHistDTO> depstohistToDepStoHistDTOs(List<DepStoHist> depstohists) {
        List<DepStoHistDTO> depstohistsDTO = new ArrayList<>();
        depstohists.forEach(x -> {
            depstohistsDTO.add(depstohistToDepStoHistDTO(x));
        });
        return depstohistsDTO;
    }

    public Mouvement toMouvement(DepStoHist entity, TypeDateEnum typeDate) {
        if (entity == null) {
            return null;
        }
        Locale loc = LocaleContextHolder.getLocale();
        Mouvement dto = new Mouvement();
        dto.setDate(entity.getInventaire().getDateCloture());
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesignation(entity.getArticledesignationAr());
        } else {
            dto.setDesignation(entity.getArticleDesignation());
        }
        dto.setCodeSaisi(entity.getCodeSaisie());
        dto.setNumaffiche(entity.getInventaire().getCodeSaisie());
        dto.setOperation(messages.getMessage("ficheStock.inventaire", null, loc));
        dto.setNumbon(entity.getInventaire().getCodeSaisie());
        dto.setLibelle(entity.getInventaire().getCodeSaisie());
        dto.setCoddep(entity.getInventaire().getDepot());
        List<EntreSortie> dtoEntreSorties = new ArrayList<>();
        EntreSortie dtoEntreSortie = new EntreSortie();
        dtoEntreSortie.setNumbon(entity.getInventaire().getCodeSaisie());
        dtoEntreSortie.setEntree(entity.getStkDep());
        dtoEntreSortie.setSortie(entity.getQte0());
        dtoEntreSortie.setDatPer(Date.from(entity.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dtoEntreSortie.setLotinter(entity.getLot());
        dtoEntreSortie.setPrix(entity.getPu());
        dtoEntreSortie.setCodeUnite(entity.getCodeUnite());
        dtoEntreSorties.add(dtoEntreSortie);
        dto.setList(dtoEntreSorties);

        return dto;

    }

    public List<Mouvement> toMouvements(List<DepStoHist> entitys, TypeDateEnum typeDate) {
        List<Mouvement> mouvements = new ArrayList<>();
        entitys.forEach(x -> {
            mouvements.add(toMouvement(x, typeDate));
        });
        return mouvements;
    }
}
