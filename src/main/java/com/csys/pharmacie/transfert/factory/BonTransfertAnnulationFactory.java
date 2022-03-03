/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.factory;

import com.csys.pharmacie.transfert.domain.FactureBT;
import com.csys.pharmacie.transfert.dto.BonTransferAnnulationDTO;

/**
 *
 * @author bassatine
 */
public class BonTransfertAnnulationFactory {

    public static BonTransferAnnulationDTO factureBTToFactureBTDTO(FactureBT factureBT) {
        BonTransferAnnulationDTO factureBTDTO = new BonTransferAnnulationDTO();
        FactureBTFactory.factureBTToFactureBTDTO(factureBT, factureBTDTO);
        return factureBTDTO;
    }
}
