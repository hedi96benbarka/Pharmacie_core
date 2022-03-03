package com.csys.pharmacie.prelevement.dto;

import java.lang.Integer;
import java.lang.String;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MotifDTO {
  
  private Integer id;


  private String designation;

 
  private String designation_sec;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public String getDesignation_sec() {
    return designation_sec;
  }

  public void setDesignation_sec(String designation_sec) {
    this.designation_sec = designation_sec;
  }

 
}

