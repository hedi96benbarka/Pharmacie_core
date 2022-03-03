package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.client.dto.ArticleDepotDto;
import java.math.BigDecimal;
import java.util.Collection;

public class ArticleUuDTO extends ArticleDTO {

    private BigDecimal prixVenteFixe;
//    private Boolean prixFixe;
    private BigDecimal prixVente;
    private BigDecimal referencePrice;
    private Collection<ArticleDepotDto> depotArticleCollection;

    public BigDecimal getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(BigDecimal referencePrice) {
        this.referencePrice = referencePrice;
    }

    public BigDecimal getPrixVenteFixe() {
        return prixVenteFixe;
    }

    public void setPrixVenteFixe(BigDecimal prixVenteFixe) {
        this.prixVenteFixe = prixVenteFixe;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public Collection<ArticleDepotDto> getDepotArticleCollection() {
        return depotArticleCollection;
    }

    public void setDepotArticleCollection(Collection<ArticleDepotDto> depotArticleCollection) {
        this.depotArticleCollection = depotArticleCollection;
    }

}
