/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.dto;

/**
 *
 * @author Hamdi
 */
import com.csys.pharmacie.achat.dto.ArticleDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailDemandePrTrDTO {

    @Id
    private Integer code;
    @NotNull
    private Integer codeDemande;

    private ArticleDTO article;

    //   @NotNull
    private Integer quantiteValide;

    private Integer quantiteRestante;

    // @NotNull
    private BigDecimal prixAchatHT;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }

    public Integer getQuantiteValide() {
        return quantiteValide;
    }

    public void setQuantiteValide(Integer quantiteValide) {
        this.quantiteValide = quantiteValide;
    }

    public Integer getQuantiteRestante() {
        return quantiteRestante;
    }

    public void setQuantiteRestante(Integer quantiteRestante) {
        this.quantiteRestante = quantiteRestante;
    }

    public BigDecimal getPrixAchatHT() {
        return prixAchatHT;
    }

    public void setPrixAchatHT(BigDecimal prixAchatHT) {
        this.prixAchatHT = prixAchatHT;
    }

    public Integer getCodeDemande() {
        return codeDemande;
    }

    public void setCodeDemande(Integer codeDemande) {
        this.codeDemande = codeDemande;
    }

}
