package com.csys.pharmacie.inventaire.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MvtStoINVDTO {
  @NotNull
  private Long num;

  @NotNull
 
  private Integer codart;

  @NotNull
  
  private String numbon;

  @NotNull
  private Integer codInventaire;

  private Integer coddep;

  @NotNull
  
  private String typbon;

  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String desart;

  @NotNull
  private BigDecimal quantite;

  @NotNull
  private BigDecimal priuni;
 
  private LocalDate datPer;

  @NotNull 
  private LocalDateTime heureSysteme;

  @Size(
      min = 0,
      max = 50
  )
  private String lotInter;

  @Size(
      min = 0,
      max = 50
  )
  private String numeroAMC;

  @NotNull
  @Size(
      min = 1,
      max = 255
  )
  private String desartSec;

  @NotNull
  @Size(
      min = 1,
      max = 10
  )
  private CategorieDepotEnum categDepot;

  private Boolean isPrixReference;

  @NotNull
  @Size(
      min = 1,
      max = 50
  )
  private String codeSaisi;

  public Long getNum() {
    return num;
  }

  public void setNum(Long num) {
    this.num = num;
  }

  public Integer getCodart() {
    return codart;
  }

  public void setCodart(Integer codart) {
    this.codart = codart;
  }

  public String getNumbon() {
    return numbon;
  }

  public void setNumbon(String numbon) {
    this.numbon = numbon;
  }

  public Integer getCodInventaire() {
    return codInventaire;
  }

  public void setCodInventaire(Integer codInventaire) {
    this.codInventaire = codInventaire;
  }

  public  Integer getCoddep() {
    return coddep;
  }

  public void setCoddep( Integer coddep) {
    this.coddep = coddep;
  }

  public String getTypbon() {
    return typbon;
  }

  public void setTypbon(String typbon) {
    this.typbon = typbon;
  }

  public String getDesart() {
    return desart;
  }

  public void setDesart(String desart) {
    this.desart = desart;
  }

  public BigDecimal getQuantite() {
    return quantite;
  }

  public void setQuantite(BigDecimal quantite) {
    this.quantite = quantite;
  }

  public BigDecimal getPriuni() {
    return priuni;
  }

  public void setPriuni(BigDecimal priuni) {
    this.priuni = priuni;
  }

  public LocalDate getDatPer() {
    return datPer;
  }

  public void setDatPer(LocalDate datPer) {
    this.datPer = datPer;
  }

  public LocalDateTime getHeureSysteme() {
    return heureSysteme;
  }

  public void setHeureSysteme(LocalDateTime heureSysteme) {
    this.heureSysteme = heureSysteme;
  }

  public String getLotInter() {
    return lotInter;
  }

  public void setLotInter(String lotInter) {
    this.lotInter = lotInter;
  }

  public String getNumeroAMC() {
    return numeroAMC;
  }

  public void setNumeroAMC(String numeroAMC) {
    this.numeroAMC = numeroAMC;
  }

  public String getDesartSec() {
    return desartSec;
  }

  public void setDesartSec(String desartSec) {
    this.desartSec = desartSec;
  }

  public CategorieDepotEnum getCategDepot() {
    return categDepot;
  }

  public void setCategDepot(CategorieDepotEnum categDepot) {
    this.categDepot = categDepot;
  }

  public Boolean getIsPrixReference() {
    return isPrixReference;
  }

  public void setIsPrixReference(Boolean isPrixReference) {
    this.isPrixReference = isPrixReference;
  }

  public String getCodeSaisi() {
    return codeSaisi;
  }

  public void setCodeSaisi(String codeSaisi) {
    this.codeSaisi = codeSaisi;
  }
}

