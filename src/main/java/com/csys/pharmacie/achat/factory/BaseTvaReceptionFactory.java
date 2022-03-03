/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.factory;

import com.csys.pharmacie.achat.domain.BaseTvaReception;
import com.csys.pharmacie.achat.dto.BaseTvaReceptionDTO;
import com.csys.pharmacie.helper.BaseTVAFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DELL
 */
public class BaseTvaReceptionFactory extends BaseTVAFactory {

    public static BaseTvaReceptionDTO baseTvaReceptionToBaseTvaReceptionDTO(BaseTvaReception baseTvaReception) {
        if (baseTvaReception == null) {
            return null;
        }
        BaseTvaReceptionDTO baseTvaReceptionDTO = new BaseTvaReceptionDTO();

        baseTvaReceptionDTO.setBaseTva(baseTvaReception.getBaseTva());
        baseTvaReceptionDTO.setCodeTva(baseTvaReception.getCodeTva());
        baseTvaReceptionDTO.setMontantTva(baseTvaReception.getMontantTva());
        baseTvaReceptionDTO.setTauxTva(baseTvaReception.getTauxTva());
        baseTvaReceptionDTO.setBaseTvaGratuite(baseTvaReception.getBaseTvaGratuite());
        baseTvaReceptionDTO.setMontantTvaGratuite(baseTvaReception.getMontantTvaGratuite());
        baseTvaReceptionDTO.setNumbon(baseTvaReception.getFactureBA().getNumbon());
        return baseTvaReceptionDTO;

    }
        /*public static BaseTvaReception baseTvaReceptionDTOToBaseTvaReception(BaseTvaReceptionDTO baseTvaReceptionDto) {
        if (baseTvaReceptionDto == null) {
            return null;
        }
        BaseTvaReception baseTvaReception = new BaseTvaReception();

        baseTvaReception.setBaseTva(baseTvaReceptionDto.getBaseTva());
        baseTvaReception.setCodeTva(baseTvaReceptionDto.getCodeTva());
        baseTvaReception.setMontantTva(baseTvaReceptionDto.getMontantTva());
        baseTvaReception.setTauxTva(baseTvaReceptionDto.getTauxTva());
        baseTvaReception.setBaseTvaGratuite(baseTvaReceptionDto.getBaseTvaGratuite());
        baseTvaReception.setMontantTvaGratuite(baseTvaReceptionDto.getMontantTvaGratuite());
     
        return baseTvaReception;

    }*/
    
  public static List<BaseTvaReceptionDTO> baseTvaReceptionsToBaseTvaReception(List<BaseTvaReception> listeBaseTvaReception) {
        List<BaseTvaReceptionDTO> listeBaseTvaReceptionDTO = new ArrayList<>();
        listeBaseTvaReception.forEach(x -> {
            listeBaseTvaReceptionDTO.add(baseTvaReceptionToBaseTvaReceptionDTO(x));
        });
        return listeBaseTvaReceptionDTO;
    }
/*
  public static List<BaseTvaReception> baseTvaReceptionsToBaseTvaReceptionDto(List<BaseTvaReceptionDTO> listeBaseTvaReceptionDto) {
        List<BaseTvaReception> listeBaseTvaReception = new ArrayList<>();
        listeBaseTvaReceptionDto.forEach(x -> {
            listeBaseTvaReception.add(baseTvaReceptionDTOToBaseTvaReception(x));
        });
        return listeBaseTvaReception;
    }*/
    }
