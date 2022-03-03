package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class AjustementRetourFournisseurDTO {
//  private AjustementRetourFournisseurPK ajustementRetourFournisseurPK;

  @NotNull
  private long codeDepot;

  @NotNull
  private BigDecimal diffMntHt;

  @NotNull
  private BigDecimal diffMntTtc;

 

  public long getCodeDepot() {
    return codeDepot;
  }

  public void setCodeDepot(long codeDepot) {
    this.codeDepot = codeDepot;
  }

  public BigDecimal getDiffMntHt() {
    return diffMntHt;
  }

  public void setDiffMntHt(BigDecimal diffMntHt) {
    this.diffMntHt = diffMntHt;
  }

  public BigDecimal getDiffMntTtc() {
    return diffMntTtc;
  }

  public void setDiffMntTtc(BigDecimal diffMntTtc) {
    this.diffMntTtc = diffMntTtc;
  }
}

