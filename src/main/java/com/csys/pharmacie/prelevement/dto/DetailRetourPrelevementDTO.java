package com.csys.pharmacie.prelevement.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DetailRetourPrelevementDTO {

  
    private Integer code;

    @NotNull
    private Integer codart;
   
    private String desart;
  
    private String desartSec;

    private String codeSaisi;

    private BigDecimal quantite;

    @Size(min = 0, max = 17)
    private String lotInter;

    @Enumerated(EnumType.STRING)
    private CategorieDepotEnum categDepot;

    private BigDecimal priuni;

    private LocalDate datPer;
    
    @JsonIgnore
    private Date datPerEdition;

    private Integer unite;

    private String DesignationUnite;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getDesartSec() {
        return desartSec;
    }

    public void setDesartSec(String desartSec) {
        this.desartSec = desartSec;
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

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public String getDesignationunite() {
        return DesignationUnite;
    }

    public void setDesignationunite(String Designationunite) {
        this.DesignationUnite = Designationunite;
    }

    public Date getDatPerEdition() {
        return datPerEdition;
    }

    public void setDatPerEdition(Date datPerEdition) {
        this.datPerEdition = datPerEdition;
    }

    public String getDesignationUnite() {
        return DesignationUnite;
    }

    public void setDesignationUnite(String DesignationUnite) {
        this.DesignationUnite = DesignationUnite;
    }

}
