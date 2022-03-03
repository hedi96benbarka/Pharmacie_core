/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.helper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 *
 * @author Farouk
 * @param <ENTITY>
 */
@Component
public class BaseTVAFactory {

    public static void toEntity(BaseTva entity, BaseTVADTO dto) {
        entity.setBaseTva(dto.getBaseTva());
        entity.setCodeTva(dto.getCodeTva());
        entity.setMontantTva(dto.getMontantTva());
        entity.setTauxTva(dto.getTauxTva());
    }

    public static BaseTVADTO toDTO(BaseTva entity) {
        BaseTVADTO dto = new BaseTVADTO();
        dto.setBaseTva(entity.getBaseTva());
        dto.setCodeTva(entity.getCodeTva());
        dto.setMontantTva(entity.getMontantTva());
        dto.setTauxTva(entity.getTauxTva());
      
        return dto;
    }

    public static  List<BaseTVADTO> listeEntitiesToListDTos(List entities) {
        List<BaseTVADTO> result = (List<BaseTVADTO>) entities.stream().filter(item -> item instanceof BaseTva).map(entity -> {
            BaseTVADTO dto = toDTO((BaseTva) entity);
            return dto;
        }).collect(Collectors.toList());
        return result;
    }
}
