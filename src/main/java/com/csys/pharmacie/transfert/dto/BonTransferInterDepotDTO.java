/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.achat.dto.DemandeTrDTO;
import java.util.List;


/**
 *
 * @author Farouk
 */
public class BonTransferInterDepotDTO extends FactureBTDTO {

    private List<DemandeTrDTO> listDemandeTrs;

    public List<DemandeTrDTO> getListDemandeTrs() {
        return listDemandeTrs;
    }

    public void setListDemandeTrs(List<DemandeTrDTO> listDemandeTrs) {
        this.listDemandeTrs = listDemandeTrs;
    }

    public BonTransferInterDepotDTO(List<DemandeTrDTO> listDemandeTrs) {
        this.listDemandeTrs = listDemandeTrs;
    }

    public BonTransferInterDepotDTO() {
    }

}
