/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.stock.dto;

import java.util.List;
import javax.validation.Valid;

/**
 *
 * @author Administrateur
 */
public class ListDepstoDTOWrapper {
    @Valid
    private List<DepstoDTO> list;
    
    
    
    public List<DepstoDTO> getList() {
        return list;
    }

    public void setList(List<DepstoDTO> list) {
        this.list = list;
    }
    
}
