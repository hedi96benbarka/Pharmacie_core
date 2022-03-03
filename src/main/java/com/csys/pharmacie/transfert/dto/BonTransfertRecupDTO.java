/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.vente.quittance.dto.FactureDTO;
import java.util.List;
import javax.validation.Valid;

/**
 *
 * @author bassatine
 */
public class BonTransfertRecupDTO extends FactureBTDTO { 
    
    
     @Valid
    private List<ResteRecuperationDTO> remainToRecover;

    private List<FactureDTO> listQuittances;  
    
    
    

    public List<ResteRecuperationDTO> getRemainToRecover() {
        return remainToRecover;
    }

    public void setRemainToRecover(List<ResteRecuperationDTO> remainToRecover) {
        this.remainToRecover = remainToRecover;
    }

    public List<FactureDTO> getListQuittances() {
        return listQuittances;
    }

    public void setListQuittances(List<FactureDTO> listQuittances) {
        this.listQuittances = listQuittances;
    }

    public BonTransfertRecupDTO() {
    }

    public BonTransfertRecupDTO(List<ResteRecuperationDTO> remainToRecover, List<FactureDTO> listQuittances) {
        this.remainToRecover = remainToRecover;
        this.listQuittances = listQuittances;
    }
    
    
}
