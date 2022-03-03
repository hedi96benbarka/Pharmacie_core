/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import static com.csys.pharmacie.helper.MouvementAssembleur.LANGUAGE_SEC;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Farouk
 */
@Component
public class BaseDetailBonFactory {

//    static String LANGUAGE_SEC;
//
//    @Value("${lang.secondary}")
//    public void setLanguage(String db) {
//        LANGUAGE_SEC = db;
//    }
//    public DetailBon detailBonToDTO() {
//
//    }
    public static void toEntity(BaseDetailBon entity, BaseDetailBonDTO dto) {
        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            entity.setDesart(dto.getDesartSec());
            entity.setDesArtSec(dto.getDesart());
        } else {
            entity.setDesart(dto.getDesart());
            entity.setDesArtSec(dto.getDesartSec());
        }
        entity.setQuantite(dto.getQuantite());
        entity.setCodeSaisi(dto.getCodeSaisi());
    }

    public static void toDTO(BaseDetailBonDTO dto, BaseDetailBon entity) {

        if (LocaleContextHolder.getLocale().getLanguage().equals(new Locale(LANGUAGE_SEC).getLanguage())) {
            dto.setDesart(entity.getDesArtSec());
            dto.setDesartSec(entity.getDesart());
        } else {
            dto.setDesart(entity.getDesart());
            dto.setDesartSec(entity.getDesArtSec());
        }
        dto.setQuantite(entity.getQuantite());
        dto.setCodeSaisi(entity.getCodeSaisi());
    }

}
