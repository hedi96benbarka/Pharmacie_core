package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.achat.domain.AjustementRetourPerimePK;
import com.csys.pharmacie.achat.domain.RetourPerime;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class AjustementRetourPerimeDTO {


  @NotNull
  private Integer codeDepot;

  @NotNull
  private BigDecimal diffMntHt;

  @NotNull
  private BigDecimal diffMntTtc;

  private RetourPerime retourPerime;

 

  public Integer getCodeDepot() {
    return codeDepot;
  }

  public void setCodeDepot(Integer codeDepot) {
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

  public RetourPerime getRetourPerime() {
    return retourPerime;
  }

  public void setRetourPerime(RetourPerime retourPerime) {
    this.retourPerime = retourPerime;
  }
}

