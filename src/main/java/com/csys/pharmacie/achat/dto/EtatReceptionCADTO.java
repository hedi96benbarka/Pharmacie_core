package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.PurchaseOrderReceptionState;
import java.lang.Integer;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EtatReceptionCADTO {
  @NotNull
  private Integer commandeAchat;

  @Size(
      min = 0,
      max = 50
  )
  private PurchaseOrderReceptionState etatReception;

  public Integer getCommandeAchat() {
    return commandeAchat;
  }

  public void setCommandeAchat(Integer commandeAchat) {
    this.commandeAchat = commandeAchat;
  }

  public PurchaseOrderReceptionState getEtatReception() {
    return etatReception;
  }

  public void setEtatReception(PurchaseOrderReceptionState etatReception) {
    this.etatReception = etatReception;
  }
}

