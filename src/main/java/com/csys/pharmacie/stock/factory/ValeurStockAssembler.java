/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.csys.pharmacie.achat.dto.DepotDTO;
import com.csys.pharmacie.achat.dto.UniteDTO;
import static com.csys.pharmacie.stock.domain.QDepsto.depsto;
import com.csys.pharmacie.stock.domain.ValeurStock;
import com.csys.pharmacie.stock.dto.ValeurStockDTO;
import java.time.ZoneId;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Administrateur
 */
public class ValeurStockAssembler {
    
    private final static Logger log = LoggerFactory.getLogger(ValeurStockAssembler.class);
    
    public static ValeurStockDTO assembleDepstoEditionValeurStockDTO(ValeurStock valeurStock, ArticleDTO article, UniteDTO unite, DepotDTO depot) {
        ValeurStockDTO valeurStockDTO = new ValeurStockDTO();
        if (valeurStock != null) {
            valeurStockDTO.setPreemptionDate(Date.from(valeurStock.getDatPer().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            valeurStockDTO.setQte(valeurStock.getQte());
            valeurStockDTO.setPu(valeurStock.getPu());
            valeurStockDTO.setPmp(valeurStock.getPmp());
            valeurStockDTO.setLot(valeurStock.getLotInter());
            valeurStockDTO.setCoddep(valeurStock.getCoddep());
            valeurStockDTO.setCodart(valeurStock.getCodart());
            valeurStockDTO.setCodeunite(valeurStock.getUnite());
            valeurStockDTO.setTautva(valeurStock.getTauxTvaAchat());
        }
        
        if (article != null) {
            valeurStockDTO.setCodeSaisi(article.getCodeSaisi());
            valeurStockDTO.setDesart(article.getDesignation());
        }
        if (unite != null) {
            valeurStockDTO.setDesignationunite(unite.getDesignation());
        }
        if (depot != null) {
            valeurStockDTO.setDesdep(depot.getDesignation());
            valeurStockDTO.setCodeSaisiDepot(depot.getCodeSaisi());
        }
        
        return valeurStockDTO;
    }
    
}
