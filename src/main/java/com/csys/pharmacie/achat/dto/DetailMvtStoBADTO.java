package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.achat.domain.DetailMvtStoBAPK;
import com.csys.pharmacie.stock.domain.Depsto;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

public class DetailMvtStoBADTO {    
  private DetailMvtStoBAPK pk;

  @NotNull
  private BigDecimal quantiteDisponible;

  @NotNull
  private BigDecimal quantite_retourne;

  private String numbonDepsto;

  private String lotinter;

  private LocalDate datPer;

  @NotNull
  private BigDecimal priuni;

  private Integer unite;

  private LocalDateTime datSYS;

  private BigDecimal stkRel;

  private Depsto depsto;

  public DetailMvtStoBAPK getPk() {
    return pk;
  }

  public void setPk(DetailMvtStoBAPK pk) {
    this.pk = pk;
  }

  public BigDecimal getQuantiteDisponible() {
    return quantiteDisponible;
  }

  public void setQuantiteDisponible(BigDecimal quantiteDisponible) {
    this.quantiteDisponible = quantiteDisponible;
  }

  public BigDecimal getQuantite_retourne() {
    return quantite_retourne;
  }

  public void setQuantite_retourne(BigDecimal quantite_retourne) {
    this.quantite_retourne = quantite_retourne;
  }

  public String getNumbonDepsto() {
    return numbonDepsto;
  }

  public void setNumbonDepsto(String numbonDepsto) {
    this.numbonDepsto = numbonDepsto;
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

  public Depsto getDepsto() {
    return depsto;
  }

  public void setDepsto(Depsto depsto) {
    this.depsto = depsto;
  }
}

