package com.csys.pharmacie.client.dto;

public class MotifPaiementDTO extends BaseDTO {

    public MotifPaiementDTO() {
    }

    public MotifPaiementDTO(Integer code, String designation, String designationSec, Boolean actif) {
        super(code, designation, designationSec, actif);
    }

}
