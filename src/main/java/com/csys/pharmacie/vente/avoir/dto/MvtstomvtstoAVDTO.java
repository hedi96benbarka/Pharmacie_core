package com.csys.pharmacie.vente.avoir.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MvtstomvtstoAVDTO {

    @NotNull
    private Integer codart;

    @NotNull
    @Size(min = 1, max = 17)
    private String lotInter;

    @NotNull
    private BigDecimal priuni;

    private BigDecimal montht;
    
    private LocalDate datPer;

    @NotNull
    @Size(min = 0, max = 255)
    private String desart;

    @NotNull
    @Size(min = 0, max = 255)
    private String desArtSec;

    @NotNull
    @Size(min = 0, max = 50)
    private String codeSaisi;

    @NotNull
    private BigDecimal quantite;

    @NotNull
    private Integer unityCode;

    private String unityDesignation;

    public MvtstomvtstoAVDTO() {
    }

    public MvtstomvtstoAVDTO(Integer codart, BigDecimal quantite) {
        this.codart = codart;
        this.quantite = quantite;
    }

    public MvtstomvtstoAVDTO(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public MvtstomvtstoAVDTO(BigDecimal montht, BigDecimal quantite, BigDecimal qteRestante) {
        this.montht = montht;
        this.quantite = quantite;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public BigDecimal getMontht() {
        return montht;
    }

    public void setMontht(BigDecimal montht) {
        this.montht = montht;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesArtSec() {
        return desArtSec;
    }

    public void setDesArtSec(String desArtSec) {
        this.desArtSec = desArtSec;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Integer getUnityCode() {
        return unityCode;
    }

    public void setUnityCode(Integer unityCode) {
        this.unityCode = unityCode;
    }

    public String getUnityDesignation() {
        return unityDesignation;
    }

    public void setUnityDesignation(String unityDesignation) {
        this.unityDesignation = unityDesignation;
    }

    
}
