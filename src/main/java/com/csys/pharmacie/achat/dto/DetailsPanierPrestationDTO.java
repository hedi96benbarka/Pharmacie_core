package com.csys.pharmacie.achat.dto;

import javax.validation.constraints.NotNull;

public class DetailsPanierPrestationDTO {

    private Integer codePrestation;
    private ArticleDTO codearticle;
    @NotNull
    private long qte;

    private String codeCategorie;

    public ArticleDTO getCodearticle() {
        return codearticle;
    }

    public void setCodearticle(ArticleDTO codearticle) {
        this.codearticle = codearticle;
    }

    public long getQte() {
        return qte;
    }

    public void setQte(long qte) {
        this.qte = qte;
    }

    public Integer getCodePrestation() {
        return codePrestation;
    }

    public void setCodePrestation(Integer codePrestation) {
        this.codePrestation = codePrestation;
    }

    public String getCodeCategorie() {
        return codeCategorie;
    }

    public void setCodeCategorie(String codeCategorie) {
        this.codeCategorie = codeCategorie;
    }

}
