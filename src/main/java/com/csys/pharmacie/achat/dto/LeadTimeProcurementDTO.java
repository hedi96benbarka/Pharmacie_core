package com.csys.pharmacie.achat.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LeadTimeProcurementDTO {

    @Size(
            min = 0,
            max = 510
    )
    private String id;

    private Integer differenceDaReception;

    @NotNull
    private Integer codeArticle;

    @NotNull
    @Size(
            min = 1,
            max = 4
    )
    private String categorieArticle;

    @NotNull
    private Integer codeUnite;

    @NotNull
    private Integer codeDa;

    private Date dateValidateDa;

    @Size(
            min = 0,
            max = 14
    )
    private String numAfficheReception;

    private Date dateBonReception;

    private long leadTimeProcurement;

    public LeadTimeProcurementDTO() {
    }

    public LeadTimeProcurementDTO(Integer codeArticle, Integer codeUnite, long leadTimeProcurement) {
        this.codeArticle = codeArticle;
        this.codeUnite = codeUnite;
        this.leadTimeProcurement = leadTimeProcurement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDifferenceDaReception() {
        return differenceDaReception;
    }

    public void setDifferenceDaReception(Integer differenceDaReception) {
        this.differenceDaReception = differenceDaReception;
    }

    public Integer getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(Integer codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getCategorieArticle() {
        return categorieArticle;
    }

    public void setCategorieArticle(String categorieArticle) {
        this.categorieArticle = categorieArticle;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public Integer getCodeDa() {
        return codeDa;
    }

    public void setCodeDa(Integer codeDa) {
        this.codeDa = codeDa;
    }

    public Date getDateValidateDa() {
        return dateValidateDa;
    }

    public void setDateValidateDa(Date dateValidateDa) {
        this.dateValidateDa = dateValidateDa;
    }

    public String getNumAfficheReception() {
        return numAfficheReception;
    }

    public void setNumAfficheReception(String numAfficheReception) {
        this.numAfficheReception = numAfficheReception;
    }

    public Date getDateBonReception() {
        return dateBonReception;
    }

    public void setDateBonReception(Date dateBonReception) {
        this.dateBonReception = dateBonReception;
    }

    public long getLeadTimeProcurement() {
        return leadTimeProcurement;
    }

    public void setLeadTimeProcurement(long leadTimeProcurement) {
        this.leadTimeProcurement = leadTimeProcurement;
    }

}
