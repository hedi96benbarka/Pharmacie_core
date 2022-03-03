package com.csys.pharmacie.achat.dto;

import java.lang.Long;
import java.lang.String;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PieceJointeReceptionDTO {

   
    private Long code;

    @NotNull
    @Size(
            min = 1,
            max = 2147483647
    )
    private String value;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
