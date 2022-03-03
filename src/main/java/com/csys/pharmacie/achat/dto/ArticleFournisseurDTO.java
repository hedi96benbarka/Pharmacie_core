package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.achat.domain.ArticleFournisseurPK;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ArticleFournisseurDTO {
  private ArticleFournisseurPK articleFournisseurPK;

  @NotNull
  private BigDecimal maxPrixAchat;

  @NotNull
  private BigDecimal minPrixAchat;

  @NotNull
  private BigDecimal prixAchat;

  private Integer delaiLivraison;

  @Size(
      min = 0,
      max = 30
  )
  private String codeArtFrs;

  public ArticleFournisseurPK getArticleFournisseurPK() {
    return articleFournisseurPK;
  }

  public void setArticleFournisseurPK(ArticleFournisseurPK articleFournisseurPK) {
    this.articleFournisseurPK = articleFournisseurPK;
  }

  public BigDecimal getMaxPrixAchat() {
    return maxPrixAchat;
  }

  public void setMaxPrixAchat(BigDecimal maxPrixAchat) {
    this.maxPrixAchat = maxPrixAchat;
  }

  public BigDecimal getMinPrixAchat() {
    return minPrixAchat;
  }

  public void setMinPrixAchat(BigDecimal minPrixAchat) {
    this.minPrixAchat = minPrixAchat;
  }

  public BigDecimal getPrixAchat() {
    return prixAchat;
  }

  public void setPrixAchat(BigDecimal prixAchat) {
    this.prixAchat = prixAchat;
  }

  public Integer getDelaiLivraison() {
    return delaiLivraison;
  }

  public void setDelaiLivraison(Integer delaiLivraison) {
    this.delaiLivraison = delaiLivraison;
  }

  public String getCodeArtFrs() {
    return codeArtFrs;
  }

  public void setCodeArtFrs(String codeArtFrs) {
    this.codeArtFrs = codeArtFrs;
  }
}

