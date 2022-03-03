package com.csys.pharmacie.achat.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;


@Deprecated
public class AjustementTransfertBranchCompanyDTO {
  

  @NotNull
  private Integer codeDepot;

  @NotNull
  private BigDecimal diffMntHt;

  @NotNull
  private BigDecimal diffMntTtc;

  private Boolean integrer;

  private String codeIntegration;

 

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

  public Boolean getIntegrer() {
    return integrer;
  }

  public void setIntegrer(Boolean integrer) {
    this.integrer = integrer;
  }

  public String getCodeIntegration() {
    return codeIntegration;
  }

  public void setCodeIntegration(String codeIntegration) {
    this.codeIntegration = codeIntegration;
  }
}

