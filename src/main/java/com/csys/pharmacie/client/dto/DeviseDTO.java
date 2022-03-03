/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.dto;


import java.lang.Boolean;
import java.lang.String;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DeviseDTO {
  @NotNull
  private String code;

  @NotNull
  private String designationAr;

  @NotNull
  private String designationEn;

  @NotNull
  private BigDecimal taux;

  private Boolean isPrincipal;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDesignationAr() {
    return designationAr;
  }

  public void setDesignationAr(String designationAr) {
    this.designationAr = designationAr;
  }

  public String getDesignationEn() {
    return designationEn;
  }

  public void setDesignationEn(String designationEn) {
    this.designationEn = designationEn;
  }

  public BigDecimal getTaux() {
    return taux;
  }

  public void setTaux(BigDecimal taux) {
    this.taux = taux;
  }

  public Boolean getIsPrincipal() {
    return isPrincipal;
  }

  public void setIsPrincipal(Boolean isPrincipal) {
    this.isPrincipal = isPrincipal;
  }
}


