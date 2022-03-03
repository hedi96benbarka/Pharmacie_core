package com.csys.pharmacie.stock.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValeurStockDTO {

    private Integer code;
    @NotNull
    private Integer codart;

    private String codeSaisi;

    private String desart;

    private String desartSec;

    private BigDecimal pu;

    private BigDecimal pmp;

    @NotNull
    private Integer codeunite;

    private String designationunite;

    private Date preemptionDate;

    private String Lot;
    @NotNull
    private BigDecimal qte;

    private Integer coddep;

    private String desdep;

    private String codeSaisiDepot;

    private BigDecimal tautva;

    public ValeurStockDTO() {
    }

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

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
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

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public BigDecimal getPmp() {
        return pmp;
    }

    public void setPmp(BigDecimal pmp) {
        this.pmp = pmp;
    }

    public Integer getCodeunite() {
        return codeunite;
    }

    public void setCodeunite(Integer codeunite) {
        this.codeunite = codeunite;
    }

    public String getDesignationunite() {
        return designationunite;
    }

    public void setDesignationunite(String designationunite) {
        this.designationunite = designationunite;
    }

    public Date getPreemptionDate() {
        return preemptionDate;
    }

    public void setPreemptionDate(Date preemptionDate) {
        this.preemptionDate = preemptionDate;
    }

    public String getLot() {
        return Lot;
    }

    public void setLot(String Lot) {
        this.Lot = Lot;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public Integer getCoddep() {
        return coddep;
    }

    public void setCoddep(Integer coddep) {
        this.coddep = coddep;
    }

    public String getDesdep() {
        return desdep;
    }

    public void setDesdep(String desdep) {
        this.desdep = desdep;
    }

    public String getCodeSaisiDepot() {
        return codeSaisiDepot;
    }

    public void setCodeSaisiDepot(String codeSaisiDepot) {
        this.codeSaisiDepot = codeSaisiDepot;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    @Override
    public String toString() {
        return "ValeurStockDTO{" + "code=" + code + ", codart=" + codart + ", codeSaisi=" + codeSaisi + ", desart=" + desart + ", desartSec=" + desartSec + ", PU=" + pu + ", codeunite=" + codeunite + ", designationunite=" + designationunite + ", preemptionDate=" + preemptionDate + ", Lot=" + Lot + ", qte=" + qte + ", coddep=" + coddep + ", desdep=" + desdep + '}';
    }

}
