/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.helper.MouvementConsomation;
import com.csys.pharmacie.stock.domain.ConsomationReels;
import com.csys.pharmacie.stock.dto.ConsommationReelsDTO;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Administrateur
 */
@Component
public class ConsomationReelsFactory {

    @Autowired
    MessageSource messages;

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    public MouvementConsomation toMouvement(ConsomationReels entity) {
        Locale loc = LocaleContextHolder.getLocale();
        MouvementConsomation dto = new MouvementConsomation();
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesart(entity.getDesArtSec());
            dto.setDesArtSec(entity.getDesart());
            dto.setDesignationUnite(entity.getDesignationUniteSec());
            dto.setDesignationDepot(entity.getDesignationDepotSec());
        } else {
            dto.setDesart(entity.getDesart());
            dto.setDesArtSec(entity.getDesArtSec());
            dto.setDesignationUnite(entity.getDesignationUnite());
            dto.setDesignationDepot(entity.getDesignationDepot());
        }
        dto.setCodart(entity.getCodart());
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setNumbon(entity.getNumbon());
        dto.setNumaffiche(entity.getNumaffiche());
        dto.setCoddep(entity.getCoddep());
        dto.setQuantite(entity.getQuantite());
        dto.setCodeUnite(entity.getCodeUnite());
        dto.setPriach(entity.getPriach());
        dto.setCodTvaAch(entity.getCodTvaAch());
        dto.setTauTvaAch(entity.getTauTvaAch());
        dto.setTypbon(entity.getTypbon());
        dto.setDate(java.util.Date.from(entity.getDate().atZone(ZoneId.systemDefault()).toInstant()));
        return dto;

    }

    public List<MouvementConsomation> toMouvement(List<ConsomationReels> consomationReels) {
        List<MouvementConsomation> mouvementConsomations = new ArrayList<>();
        consomationReels.forEach(x -> {
            mouvementConsomations.add(toMouvement(x));
        });
        return mouvementConsomations;
    }

    public ConsommationReelsDTO consommationReelsToConsommationReelsDTO(ConsomationReels entity) {
        ConsommationReelsDTO dto = new ConsommationReelsDTO();
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesart(entity.getDesArtSec());
            dto.setDesArtSec(entity.getDesart());
            dto.setDesignationUnite(entity.getDesignationUniteSec());
            dto.setDesignationDepot(entity.getDesignationDepotSec());
        } else {
            dto.setDesart(entity.getDesart());
            dto.setDesArtSec(entity.getDesArtSec());
            dto.setDesignationUnite(entity.getDesignationUnite());
            dto.setDesignationDepot(entity.getDesignationDepot());
        }
        dto.setCodart(entity.getCodart());
        dto.setCodeSaisi(entity.getCodeSaisi());
        dto.setNumbon(entity.getNumbon());
        dto.setNumaffiche(entity.getNumaffiche());
        dto.setCoddep(entity.getCoddep());
        dto.setQuantite(entity.getQuantite());
        dto.setCodeUnite(entity.getCodeUnite());
        dto.setPriach(entity.getPriach());
        dto.setCodTvaAch(entity.getCodTvaAch());
        dto.setTauTvaAch(entity.getTauTvaAch());
        dto.setTypbon(entity.getTypbon());
        return dto;
    }

    public List<ConsommationReelsDTO> consommationReelsToConsommationReelsDTOs(List<ConsomationReels> consommationReels) {
        List<ConsommationReelsDTO> consommationReelsDTOs = new ArrayList<>();
        consommationReels.forEach(x -> {
            consommationReelsDTOs.add(consommationReelsToConsommationReelsDTO(x));
        });
        return consommationReelsDTOs;
    }
}
