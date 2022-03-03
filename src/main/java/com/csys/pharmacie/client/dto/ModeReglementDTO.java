/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.dto;

/**
 *
 * @author bassatine
 */
public class ModeReglementDTO extends BaseDTO {

    public ModeReglementDTO() {
    }

    public ModeReglementDTO(Integer code, String designation, String designationSec, Boolean actif) {
        super(code, designation, designationSec, actif);
    }

}
