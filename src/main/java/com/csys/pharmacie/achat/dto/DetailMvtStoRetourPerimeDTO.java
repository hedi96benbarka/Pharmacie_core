package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.achat.domain.MvtstoRetourPerime;
import com.csys.pharmacie.stock.domain.Depsto;

import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

public class DetailMvtStoRetourPerimeDTO {
//  private Depsto depsto;

  private MvtstoRetourPerime mvtstoRetour_perime;

  private Integer code;

  private Integer codeMvtSto;

  private Integer codeDepsto;

  @NotNull
  private BigDecimal quantiteDisponible;

  @NotNull
  private BigDecimal quantitePrelevee;

  private String numbon;

  private String lotinter;

  private LocalDate datPer;

  @NotNull
  private BigDecimal priuni;

  private Integer unite;

  private LocalDateTime datSYS;

  private BigDecimal stkRel;

//  public Depsto getDepsto() {
//    return depsto;
//  }
//
//  public void setDepsto(Depsto depsto) {
//    this.depsto = depsto;
//  }

  public MvtstoRetourPerime getMvtstoRetour_perime() {
    return mvtstoRetour_perime;
  }

  public void setMvtstoRetour_perime(MvtstoRetourPerime mvtstoRetour_perime) {
    this.mvtstoRetour_perime = mvtstoRetour_perime;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public Integer getCodeMvtSto() {
    return codeMvtSto;
  }

  public void setCodeMvtSto(Integer codeMvtSto) {
    this.codeMvtSto = codeMvtSto;
  }

  public Integer getCodeDepsto() {
    return codeDepsto;
  }

  public void setCodeDepsto(Integer codeDepsto) {
    this.codeDepsto = codeDepsto;
  }

  public BigDecimal getQuantiteDisponible() {
    return quantiteDisponible;
  }

  public void setQuantiteDisponible(BigDecimal quantiteDisponible) {
    this.quantiteDisponible = quantiteDisponible;
  }

  public BigDecimal getQuantitePrelevee() {
    return quantitePrelevee;
  }

  public void setQuantitePrelevee(BigDecimal quantitePrelevee) {
    this.quantitePrelevee = quantitePrelevee;
  }

  public String getNumbon() {
    return numbon;
  }

  public void setNumbon(String numbon) {
    this.numbon = numbon;
  }

  public String getLotinter() {
    return lotinter;
  }

  public void setLotinter(String lotinter) {
    this.lotinter = lotinter;
  }

  public LocalDate getDatPer() {
    return datPer;
  }

  public void setDatPer(LocalDate datPer) {
    this.datPer = datPer;
  }

  public BigDecimal getPriuni() {
    return priuni;
  }

  public void setPriuni(BigDecimal priuni) {
    this.priuni = priuni;
  }

  public Integer getUnite() {
    return unite;
  }

  public void setUnite(Integer unite) {
    this.unite = unite;
  }

  public LocalDateTime getDatSYS() {
    return datSYS;
  }

  public void setDatSYS(LocalDateTime datSYS) {
    this.datSYS = datSYS;
  }

  public BigDecimal getStkRel() {
    return stkRel;
  }

  public void setStkRel(BigDecimal stkRel) {
    this.stkRel = stkRel;
  }
}

