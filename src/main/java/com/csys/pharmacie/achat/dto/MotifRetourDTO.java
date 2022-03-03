package com.csys.pharmacie.achat.dto;

import java.lang.Integer;
import java.lang.String;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MotifRetourDTO {
    
  private Integer id;

  @NotNull
  @Size(
      min = 0,
      max = 50
  )
  private String description;

  @NotNull
  @Size(
      min = 0,
      max = 50
  )
  private String descriptionSec;

 private List<RetourPerimeDTO> factureRetour_perime;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescriptionSec() {
    return descriptionSec;
  }

  public void setDescriptionSec(String descriptionSec) {
    this.descriptionSec = descriptionSec;
  }

    public List<RetourPerimeDTO> getFactureRetour_perime() {
        return factureRetour_perime;
    }

    public void setFactureRetour_perime(List<RetourPerimeDTO> factureRetour_perime) {
        this.factureRetour_perime = factureRetour_perime;
    }

 
}

