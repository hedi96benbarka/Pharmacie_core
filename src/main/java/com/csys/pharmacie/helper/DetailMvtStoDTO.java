package com.csys.pharmacie.helper;

import com.csys.pharmacie.stock.domain.Depsto;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

public class DetailMvtStoDTO {
  @NotNull
  private Integer codemvt;

  @NotNull
  private Integer codeDepsto;

  private Depsto depsto;

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

  public Integer getCodemvt() {
    return codemvt;
  }

  public void setCodemvt(Integer codemvt) {
    this.codemvt = codemvt;
  }

  public Integer getCodeDepsto() {
    return codeDepsto;
  }

  public void setCodeDepsto(Integer codeDepsto) {
    this.codeDepsto = codeDepsto;
  }

  public Depsto getDepsto() {
    return depsto;
  }   

  public void setDepsto(Depsto depsto) {
    this.depsto = depsto;
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

