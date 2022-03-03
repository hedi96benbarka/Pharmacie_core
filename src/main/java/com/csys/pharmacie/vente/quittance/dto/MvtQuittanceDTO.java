package com.csys.pharmacie.vente.quittance.dto;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MvtQuittanceDTO {

    @NotNull
    private Integer codart;
    @Size(
            min = 0,
            max = 17
    )
    private String lot;

    @Size(
            min = 0,
            max = 500
    )
    private String memoart;

    private LocalDate datPer;

    @NotNull
    private Integer unite;

    @NotNull
    @Min(value = 1)
    private BigDecimal quantite;

    private BigDecimal prixVente;

    private ArticleDTO article;

    private BigDecimal tauxTva;

    private Integer codeTva;

    private BigDecimal remise;

    private BigDecimal majoration;

    private BigDecimal tauxCouverture;

    private BigDecimal ajustement;

    private BigDecimal prixAchat;

    private Integer codeTvaAch;

    private BigDecimal valeurTvaAch;

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public Integer getCodeTva() {
        return codeTva;
    }

    public void setCodeTva(Integer codeTva) {
        this.codeTva = codeTva;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getMemoart() {
        return memoart;
    }

    public void setMemoart(String memoart) {
        this.memoart = memoart;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public BigDecimal getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(BigDecimal prixAchat) {
        this.prixAchat = prixAchat;
    }

    public Integer getCodeTvaAch() {
        return codeTvaAch;
    }

    public void setCodeTvaAch(Integer codeTvaAch) {
        this.codeTvaAch = codeTvaAch;
    }

    public BigDecimal getValeurTvaAch() {
        return valeurTvaAch;
    }

    public void setValeurTvaAch(BigDecimal valeurTvaAch) {
        this.valeurTvaAch = valeurTvaAch;
    }

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getMajoration() {
        return majoration;
    }

    public void setMajoration(BigDecimal majoration) {
        this.majoration = majoration;
    }

    public BigDecimal getTauxCouverture() {
        return tauxCouverture;
    }

    public void setTauxCouverture(BigDecimal tauxCouverture) {
        this.tauxCouverture = tauxCouverture;
    }

    public BigDecimal getAjustement() {
        return ajustement;
    }

    public void setAjustement(BigDecimal ajustement) {
        this.ajustement = ajustement;
    }

    @Override
    public String toString() {
        return "MvtQuittanceDTO{" + "codart=" + codart + ", lot=" + lot + ", memoart=" + memoart + ", datPer=" + datPer + ", unite=" + unite + ", quantite=" + quantite + ", article=" + article + '}';
    }

}
