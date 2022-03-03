/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.transfert.domain.Btfe;
import com.csys.pharmacie.transfert.dto.BtfeDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Administrateur
 */
@Component
public class BtfeFactory {

    public BtfeDTO btfeToBtfeDTO(Btfe btfe) {
        BtfeDTO btfeDTO = new BtfeDTO();
        btfeDTO.setNumFE(btfe.getNumFE());
        return btfeDTO;
    }

    public Btfe btfeDTOToBtfe(BtfeDTO btfeDTO) {
        Btfe btfe = new Btfe();
        btfeDTO.setNumFE(btfe.getNumFE());
        return btfe;
    }

    public List<BtfeDTO> btfeToBtfeDTOs(List<Btfe> btfes) {
        List<BtfeDTO> btfesDTO = new ArrayList<>();
        btfes.forEach(x -> {
            btfesDTO.add(btfeToBtfeDTO(x));
        });
        return btfesDTO;
    }

    public List<Btfe> btfeDTOToBtfes(List<BtfeDTO> btfeDTOs) {
        List<Btfe> btfes = new ArrayList<>();
        btfeDTOs.forEach(x -> {
            btfes.add(btfeDTOToBtfe(x));
        });
        return btfes;
    }
}
