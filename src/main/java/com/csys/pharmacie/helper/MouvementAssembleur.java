/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
public class MouvementAssembleur {

    @Autowired
    MessageSource messages;

    static String LANGUAGE_SEC;

    @Value("${lang.secondary}")
    public void setLanguage(String db) {
        LANGUAGE_SEC = db;
    }

    public Mouvement mouvementAssembleur(List<TotalMouvement> totalMouvements, Date date, ArticleDTO article,DepotDTO depot) {
        Locale loc = LocaleContextHolder.getLocale();
        Mouvement mouvement = new Mouvement();
        mouvement.setId("INVSOLDEDEPART");
        mouvement.setDate(date);
        mouvement.setDesignation(article.getDesignation());
        mouvement.setCodeSaisi(article.getCodeSaisi());
        mouvement.setOperation("");
        mouvement.setNumbon(depot.getCode().toString());
        mouvement.setLibelle(messages.getMessage("ficheStock.soldeDepart", null, loc));
        mouvement.setCoddep(depot.getCode());
        mouvement.setDesignationDepot(depot.getDesignation());
        List<EntreSortie> listEntreSortie = new ArrayList<>();
        totalMouvements.forEach(x -> {
            EntreSortie dtoEntreSortie = new EntreSortie();
            dtoEntreSortie.setNumbon(depot.getCode().toString());
            dtoEntreSortie.setEntree(x.getQuantite());
            dtoEntreSortie.setPrix(BigDecimal.ZERO);
            dtoEntreSortie.setSortie(BigDecimal.ZERO);
            dtoEntreSortie.setCodeUnite(x.getCodeUnite());
            listEntreSortie.add(dtoEntreSortie);
        });
        mouvement.setList(listEntreSortie);
        return mouvement;
    }

}
