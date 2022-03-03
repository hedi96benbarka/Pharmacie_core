package com.csys.pharmacie.stock.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ArticleReorderPointDTO {

    private Integer code;
    
    @NotNull
    private CategorieDepotEnum categDepot;

    private String codeSaisie;
    
    private LocalDateTime dateCreate;
    
    @NotNull
    private LocalDateTime dateDuReference;
    @NotNull
    private LocalDateTime dateAuReference;
    
    private String userCreate;

    @NotNull 
    @Min(value = 1)
    @Max(value = 360)
    private BigDecimal consummingDays;
    
    @JsonIgnore
    private Date dateDuReferenceEdition;
    @JsonIgnore
    private Date dateAuReferenceEdition;
    @JsonIgnore
    private Date dateCreateEdition;
    
    private Integer codePlanning;


    private Collection<DetailArticleReorderPointDTO> detailArticleReorderPointCollection;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCodeSaisie() {
        return codeSaisie;
    }

    public void setCodeSaisie(String codeSaisie) {
        this.codeSaisie = codeSaisie;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDateTime getDateDuReference() {
        return dateDuReference;
    }

    public void setDateDuReference(LocalDateTime dateDuReference) {
        this.dateDuReference = dateDuReference;
    }

    public LocalDateTime getDateAuReference() {
        return dateAuReference;
    }

    public void setDateAuReference(LocalDateTime dateAuReference) {
        this.dateAuReference = dateAuReference;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public BigDecimal getConsummingDays() {
        return consummingDays;
    }

    public void setConsummingDays(BigDecimal consummingDays) {
        this.consummingDays = consummingDays;
    }
    
    public Collection<DetailArticleReorderPointDTO> getDetailArticleReorderPointCollection() {
        return detailArticleReorderPointCollection;
    }

    public void setDetailArticleReorderPointCollection(Collection<DetailArticleReorderPointDTO> detailArticleReorderPointCollection) {
        this.detailArticleReorderPointCollection = detailArticleReorderPointCollection;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public Date getDateDuReferenceEdition() {
        return dateDuReferenceEdition;
    }

    public void setDateDuReferenceEdition(Date dateDuReferenceEdition) {
        this.dateDuReferenceEdition = dateDuReferenceEdition;
    }

    public Date getDateAuReferenceEdition() {
        return dateAuReferenceEdition;
    }

    public void setDateAuReferenceEdition(Date dateAuReferenceEdition) {
        this.dateAuReferenceEdition = dateAuReferenceEdition;
    }

    public Date getDateCreateEdition() {
        return dateCreateEdition;
    }

    public void setDateCreateEdition(Date dateCreateEdition) {
        this.dateCreateEdition = dateCreateEdition;
    }

    public Integer getCodePlanning() {
        return codePlanning;
    }

    public void setCodePlanning(Integer codePlanning) {
        this.codePlanning = codePlanning;
    }
    
    @Override
    public String toString() {
        return "ArticleReorderPointDTO{" + "code=" + code + ", categDepot=" + categDepot + ", codeSaisie=" + codeSaisie + ", dateCreate=" + dateCreate + ", dateDuReference=" + dateDuReference + ", dateAuReference=" + dateAuReference + ", userCreate=" + userCreate + ", consummingDays=" + consummingDays + ", dateDuReferenceEdition=" + dateDuReferenceEdition + ", dateAuReferenceEdition=" + dateAuReferenceEdition + ", dateCreateEdition=" + dateCreateEdition + '}';
    }

}
