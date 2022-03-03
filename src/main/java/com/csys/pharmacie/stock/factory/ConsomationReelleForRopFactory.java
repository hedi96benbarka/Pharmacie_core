/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.factory;

import com.csys.pharmacie.stock.domain.ConsommationReelleForRop;
import com.csys.pharmacie.stock.dto.ConsommationReelleForRopDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class ConsomationReelleForRopFactory {

    


    public static ConsommationReelleForRopDTO consomationReelleForRopToConsommationReelleForRopDTO(ConsommationReelleForRop entity) {
        ConsommationReelleForRopDTO dto = new ConsommationReelleForRopDTO();
        dto.setCategDepot(entity.getCategDepot());
        dto.setCodart(entity.getCodart());
//        dto.setCodeSaisi(entity.getCodeSaisi());
//        dto.setNumbon(entity.getNumbon());
//        dto.setNumaffiche(entity.getNumaffiche());
//        dto.setCoddep(entity.getCoddep());
        dto.setQuantite(entity.getQuantite());
        dto.setCodeUnite(entity.getCodeUnite());
        dto.setValeur(entity.getValeur());
//        dto.setPriach(entity.getPriach());
//        dto.setCodTvaAch(entity.getCodTvaAch());
//        dto.setTauTvaAch(entity.getTauTvaAch());
//        dto.setTypbon(entity.getTypbon());
        return dto;
    }

    public static List<ConsommationReelleForRopDTO> consomationReelleForRopToConsommationReelleForRopDTOs(List<ConsommationReelleForRop> consommationReelleForRops) {
        List<ConsommationReelleForRopDTO> consommationReelleForRopDTOs = new ArrayList<>();
        consommationReelleForRops.forEach(x -> {
            consommationReelleForRopDTOs.add(consomationReelleForRopToConsommationReelleForRopDTO(x));
        });
        return consommationReelleForRopDTOs;
    }
}
