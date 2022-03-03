package com.csys.pharmacie.prelevement.dto;

import com.csys.pharmacie.helper.PrelevmentOrderState;
import java.lang.Integer;
import java.lang.String;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EtatDPRDTO {
  @NotNull
  private Integer codedpr;

  @Size(
      min = 0,
      max = 20
  )
  private  PrelevmentOrderState etat;

  public Integer getCodedpr() {
    return codedpr;
  }

  public void setCodedpr(Integer codedpr) {
    this.codedpr = codedpr;
  }

    public PrelevmentOrderState getEtat() {
        return etat;
    }

    public void setEtat(PrelevmentOrderState etat) {
        this.etat = etat;
    }


}

