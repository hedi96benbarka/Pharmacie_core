package com.csys.pharmacie.inventaire.dto;


import java.lang.Long;
import java.lang.String;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TraceTransfertInventaireDTO {
  @NotNull
  private Long code;

  @NotNull
  @Size(
      min = 1,
      max = 20
  )
  private String user;

  private LocalDateTime date;



  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

}

