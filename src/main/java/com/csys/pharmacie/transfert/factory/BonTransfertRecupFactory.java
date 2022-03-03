/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.transfert.domain.FactureBT;
import com.csys.pharmacie.transfert.dto.BonTransfertRecupDTO;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;

/**
 *
 * @author bassatine
 */
public class BonTransfertRecupFactory {

    public static BonTransfertRecupDTO factureBTToFactureBTDTO(FactureBT factureBT) {
        BonTransfertRecupDTO factureBTDTO = new BonTransfertRecupDTO();
        FactureBTFactory.factureBTToFactureBTDTO(factureBT, factureBTDTO);
        return factureBTDTO;
    }
}
