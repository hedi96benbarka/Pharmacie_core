package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.TransferOrderState;
import java.lang.Integer;
import javax.validation.constraints.NotNull;

public class EtatDTRDTO {
  @NotNull
  private Integer codedtr;

  @NotNull
  private TransferOrderState etat;

  public Integer getCodedtr() {
    return codedtr;
  }

  public void setCodedtr(Integer codedtr) {
    this.codedtr = codedtr;
  }

  public TransferOrderState getEtat() {
    return etat;
  }

  public void setEtat(TransferOrderState etat) {
    this.etat = etat;
  }
}

