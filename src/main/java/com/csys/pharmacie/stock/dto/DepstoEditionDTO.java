package com.csys.pharmacie.stock.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepstoEditionDTO {
    private Integer codart;
    
    private Integer coddep;

    private Date datePer;

    private BigDecimal qte;
    
    private BigDecimal PU;
    
    private String lot;
    
    private Integer codunite;
     
    private String desunite;
     
    private String lotInter;

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public Date getDatePer() {
        return datePer;
    }

    public void setDatePer(Date datePer) {
        this.datePer = datePer;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public BigDecimal getPU() {
        return PU;
    }

    public void setPU(BigDecimal PU) {
        this.PU = PU;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public Integer getCodunite() {
        return codunite;
    }

    public void setCodunite(Integer codunite) {
        this.codunite = codunite;
    }

    public String getDesunite() {
        return desunite;
    }

    public void setDesunite(String desunite) {
        this.desunite = desunite;
    }

    public String getLotInter() {
        return lotInter;
    }

    public void setLotInter(String lotInter) {
        this.lotInter = lotInter;
    }
    
   
}
