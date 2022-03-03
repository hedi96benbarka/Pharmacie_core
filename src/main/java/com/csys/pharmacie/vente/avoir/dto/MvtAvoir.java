package com.csys.pharmacie.vente.avoir.dto;

import com.csys.pharmacie.achat.dto.ArticleDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MvtAvoir {

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

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
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

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

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

    @Override
    public String toString() {
        return "MvtAvoir{" + "codart=" + codart + ", lot=" + lot + ", memoart=" + memoart + ", datPer=" + datPer + ", unite=" + unite + ", quantite=" + quantite + ", prixVente=" + prixVente + ", article=" + article + ", tauxTva=" + tauxTva + ", codeTva=" + codeTva + '}';
    }

}
