package com.csys.pharmacie.prelevement.dto;

import com.csys.pharmacie.prelevement.domain.TraceDetailRetourPrPK;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class TraceDetailRetourPrDTO {
  private TraceDetailRetourPrPK traceDetailRetourPrPK;

  @NotNull
  private Integer codeMvtstopr;

  @NotNull
  private BigDecimal quantite;

  public TraceDetailRetourPrPK getTraceDetailRetourPrPK() {
    return traceDetailRetourPrPK;
  }

  public void setTraceDetailRetourPrPK(TraceDetailRetourPrPK traceDetailRetourPrPK) {
    this.traceDetailRetourPrPK = traceDetailRetourPrPK;
  }

  public Integer getCodeMvtstopr() {
    return codeMvtstopr;
  }

  public void setCodeMvtstopr(Integer codeMvtstopr) {
    this.codeMvtstopr = codeMvtstopr;
  }

  public BigDecimal getQuantite() {
    return quantite;
  }

  public void setQuantite(BigDecimal quantite) {
    this.quantite = quantite;
  }
}

