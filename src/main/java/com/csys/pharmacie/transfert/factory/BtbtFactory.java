/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.transfert.domain.Btbt;
import com.csys.pharmacie.transfert.dto.BtbtDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author Administrateur
 */
@Component
public class BtbtFactory {

    public BtbtDTO btbtToBtbtDTO(Btbt btbt) {
        BtbtDTO btbtDTO = new BtbtDTO();
        btbtDTO.setNumBTReturn(btbt.getNumBTReturn());
        return btbtDTO;
    }

    public Btbt btbtDTOToBtbt(BtbtDTO btbtDTO) {
        Btbt btbt = new Btbt();
        btbt.setNumBTReturn(btbtDTO.getNumBTReturn());
        return btbt;
    }

    public List<BtbtDTO> btbtToBtbtDTOs(List<Btbt> btbts) {
        List<BtbtDTO> btbtsDTO = new ArrayList<>();
        btbts.forEach(x -> {
            btbtsDTO.add(btbtToBtbtDTO(x));
        });
        return btbtsDTO;
    }

    public List<Btbt> btbtDTOToBtbts(List<BtbtDTO> btbtDTOs) {
        List<Btbt> btbts = new ArrayList<>();
        btbtDTOs.forEach(x -> {
            btbts.add(btbtDTOToBtbt(x));
        });
        return btbts;
    }
}
