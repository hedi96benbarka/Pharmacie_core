package com.csys.pharmacie.inventaire.dto;

import com.csys.pharmacie.inventaire.domain.DetailInventairePK;

public class DetailInventaireDTO {
  private DetailInventairePK detailInventairePK;

//  private Inventaire inventaire1;

  public DetailInventairePK getDetailInventairePK() {
    return detailInventairePK;
  }

  public void setDetailInventairePK(DetailInventairePK detailInventairePK) {
    this.detailInventairePK = detailInventairePK;
  }

//  public Inventaire getInventaire1() {
//    return inventaire1;
//  }
//
//  public void setInventaire1(Inventaire inventaire1) {
//    this.inventaire1 = inventaire1;
//  }
}

