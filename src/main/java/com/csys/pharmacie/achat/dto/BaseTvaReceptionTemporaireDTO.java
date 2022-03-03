package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.achat.domain.ReceptionTemporaire;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class BaseTvaReceptionTemporaireDTO {
  @NotNull
  private Integer codeTva;

  private BigDecimal tauxTva;

  @NotNull
  private BigDecimal baseTva;

  @NotNull
  private BigDecimal montantTva;

  private Integer code;

  private BigDecimal montantTvaGratuite;

  private BigDecimal baseTvaGratuite;

  private ReceptionTemporaire reception;
  private String numbon;

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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


  public BigDecimal getMontantTvaGratuite() {
    return montantTvaGratuite;
  }

  public void setMontantTvaGratuite(BigDecimal montantTvaGratuite) {
    this.montantTvaGratuite = montantTvaGratuite;
  }

  public BigDecimal getBaseTvaGratuite() {
    return baseTvaGratuite;
  }

  public void setBaseTvaGratuite(BigDecimal baseTvaGratuite) {
    this.baseTvaGratuite = baseTvaGratuite;
  }

  public ReceptionTemporaire getReception() {
    return reception;
  }

  public void setReception(ReceptionTemporaire reception) {
    this.reception = reception;
  }

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }
  
}

