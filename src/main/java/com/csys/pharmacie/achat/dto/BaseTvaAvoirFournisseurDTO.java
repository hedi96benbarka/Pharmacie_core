package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.achat.domain.AvoirFournisseur;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class BaseTvaAvoirFournisseurDTO {
  private BigDecimal mntTvaGrtauite;

  private BigDecimal baseTvaGratuite;

  private Integer code;

  private Integer codeTva;

  private BigDecimal tauxTva;

  private BigDecimal baseTva;

  private BigDecimal montantTva;

  public BigDecimal getMntTvaGrtauite() {
    return mntTvaGrtauite;
  }

  public void setMntTvaGrtauite(BigDecimal mntTvaGrtauite) {
    this.mntTvaGrtauite = mntTvaGrtauite;
  }

  public BigDecimal getBaseTvaGratuite() {
    return baseTvaGratuite;
  }

  public void setBaseTvaGratuite(BigDecimal baseTvaGratuite) {
    this.baseTvaGratuite = baseTvaGratuite;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public Integer getCodeTva() {
    return codeTva;
  }

  public void setCodeTva(Integer codeTva) {
    this.codeTva = codeTva;
  }

  public BigDecimal getTauxTva() {
    return tauxTva;
  }

  public void setTauxTva(BigDecimal tauxTva) {
    this.tauxTva = tauxTva;
  }

  public BigDecimal getBaseTva() {
    return baseTva;
  }

  public void setBaseTva(BigDecimal baseTva) {
    this.baseTva = baseTva;
  }

  public BigDecimal getMontantTva() {
    return montantTva;
  }

  public void setMontantTva(BigDecimal montantTva) {
    this.montantTva = montantTva;
  }

    @Override
    public String toString() {
        return "BaseTvaAvoirFournisseurDTO{" + "mntTvaGrtauite=" + mntTvaGrtauite + ", baseTvaGratuite=" + baseTvaGratuite + ", code=" + code + ", codeTva=" + codeTva + ", tauxTva=" + tauxTva + ", baseTva=" + baseTva + ", montantTva=" + montantTva + '}';
    }
  
  
}

