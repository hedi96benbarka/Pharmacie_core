package com.csys.pharmacie.client.dto;


import java.time.LocalDateTime;

public class ClassificationArticleDTO {

  private String code;
  private String designation;
  private String designationSec;
  private String userCreate;
  private LocalDateTime dateCreate;
  private PalierClassificationArticleDTO palierClassificationArticle;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public String getDesignationSec() {
    return designationSec;
  }

  public void setDesignationSec(String designationSec) {
    this.designationSec = designationSec;
  }

  public String getUserCreate() {
    return userCreate;
  }

  public void setUserCreate(String userCreate) {
    this.userCreate = userCreate;
  }

  public LocalDateTime getDateCreate() {
    return dateCreate;
  }

  public void setDateCreate(LocalDateTime dateCreate) {
    this.dateCreate = dateCreate;
  }

  public PalierClassificationArticleDTO getPalierClassificationArticle() {
    return palierClassificationArticle;
  }

}

