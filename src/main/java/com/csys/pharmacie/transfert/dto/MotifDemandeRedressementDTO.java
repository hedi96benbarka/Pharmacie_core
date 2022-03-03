/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.transfert.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author DELL
 */
public class MotifDemandeRedressementDTO {

    @NotNull
    private Integer id;

    @Size(min = 0, max = 25)
    private String description;

    @Size(min = 0, max = 25)
    private String descriptionSec;
    
    private Boolean correctionLot;
    
    private Boolean regenererPMP;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionSec() {
        return descriptionSec;
    }

    public void setDescriptionSec(String descriptionSec) {
        this.descriptionSec = descriptionSec;
    }

    public Boolean getCorrectionLot() {
        return correctionLot;
    }

    public void setCorrectionLot(Boolean correctionLot) {
        this.correctionLot = correctionLot;
    }

    public Boolean getRegenererPMP() {
        return regenererPMP;
    }

    public void setRegenererPMP(Boolean regenererPMP) {
        this.regenererPMP = regenererPMP;
    }

}
