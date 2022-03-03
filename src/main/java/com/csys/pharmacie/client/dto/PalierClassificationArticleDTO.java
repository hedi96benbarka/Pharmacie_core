package com.csys.pharmacie.client.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class PalierClassificationArticleDTO {
  private Long code;


  private String classificationArticle;

  private BigDecimal du;

  private BigDecimal au;

  private Integer safetyStockPerDay;

  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  public String getClassificationArticle() {
    return classificationArticle;
  }

  public void setClassificationArticle(String classificationArticle) {
    this.classificationArticle = classificationArticle;
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

  public Integer getSafetyStockPerDay() {
    return safetyStockPerDay;
  }

  public void setSafetyStockPerDay(Integer safetyStockPerDay) {
    this.safetyStockPerDay = safetyStockPerDay;
  }
}

