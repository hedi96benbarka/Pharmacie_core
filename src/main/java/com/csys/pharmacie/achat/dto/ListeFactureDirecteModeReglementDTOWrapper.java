/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import java.util.Collection;

/**
 *
 * @author Administrateur
 */
public class ListeFactureDirecteModeReglementDTOWrapper {
    private  Collection<FactureDirecteModeReglementDTO> modeReglementList;

    public Collection<FactureDirecteModeReglementDTO> getModeReglementList() {
        return modeReglementList;
    }

    public void setModeReglementList(Collection<FactureDirecteModeReglementDTO> modeReglementList) {
        this.modeReglementList = modeReglementList;
    }
    
}
