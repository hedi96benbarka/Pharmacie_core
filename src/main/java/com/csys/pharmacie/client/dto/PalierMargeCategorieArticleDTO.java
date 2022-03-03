package com.csys.pharmacie.client.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class PalierMargeCategorieArticleDTO {
  private Integer code;

  @NotNull
  private BigDecimal du;

  @NotNull
  private BigDecimal au;

  @NotNull
  private BigDecimal marge;

  private Integer codeMaquetteCategorieArticle;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public BigDecimal getDu() {
    return du;
  }

  public void setDu(BigDecimal du) {
    this.du = du;
  }

  public BigDecimal getAu() {
    return au;
  }

  public void setAu(BigDecimal au) {
    this.au = au;
  }

  public BigDecimal getMarge() {
    return marge;
  }

  public void setMarge(BigDecimal marge) {
    this.marge = marge;
  }

    public Integer getCodemaquetteCategorieArticle() {
        return codeMaquetteCategorieArticle;
    }

    public void setCodemaquetteCategorieArticle(Integer codemaquetteCategorieArticle) {
        this.codeMaquetteCategorieArticle = codemaquetteCategorieArticle;
    }

  
}

