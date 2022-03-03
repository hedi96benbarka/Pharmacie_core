/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.transfert.domain.FactureBT;
import com.csys.pharmacie.transfert.dto.BonTransferInterDepotDTO;
import com.csys.pharmacie.transfert.dto.BonTransfertRecupDTO;
import com.csys.pharmacie.transfert.dto.FactureBTDTO;

/**
 *
 * @author bassatine
 */
public class BonTransfertInterDepotFactory {

    public static BonTransferInterDepotDTO factureBTToFactureBTDTO(FactureBT factureBT) {
        BonTransferInterDepotDTO factureBTDTO = new BonTransferInterDepotDTO();
        FactureBTFactory.factureBTToFactureBTDTO(factureBT, factureBTDTO);
        return factureBTDTO;
    }
}
