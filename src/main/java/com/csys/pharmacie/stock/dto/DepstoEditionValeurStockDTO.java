package com.csys.pharmacie.stock.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepstoEditionValeurStockDTO {

    private Integer coddep;

    private String desdep;

    private Integer codart;

    private String codeSaisi;

    private String desart;

    private BigDecimal pu;

    private Integer codeunite;

    private String designationunite;

    private BigDecimal qte;

    private String lot;

    private Date datPer;

    private String codeSaisiDepot;

    private Integer codtva;

    private BigDecimal tautva;
    
    private BigDecimal qteFixeDepot;

    private String designationCategorieArticle;
    
    public Date getDatPer() {
        return datPer;
    }

    public void setDatPer(Date datPer) {
        this.datPer = datPer;
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

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
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

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getCodeSaisiDepot() {
        return codeSaisiDepot;
    }

    public void setCodeSaisiDepot(String codeSaisiDepot) {
        this.codeSaisiDepot = codeSaisiDepot;
    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public BigDecimal getQteFixeDepot() {
        return qteFixeDepot;
    }

    public void setQteFixeDepot(BigDecimal qteFixeDepot) {
        this.qteFixeDepot = qteFixeDepot;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return "union Select " + coddep + " as coddep,'" + desdep + "' as desdep," + codart + " as codart,'" + codeSaisi + "' as codeSaisi,'" + desart + "' as desart, " + pu + " as pu, " + codeunite + " as codeunite,'" + designationunite + "' as designationunite," + qte + " as qte,'" + lot + "' as lot,'" + df.format(datPer) + "' as datPer";
    }

    public String getDesignationCategorieArticle() {
        return designationCategorieArticle;
    }

    public void setDesignationCategorieArticle(String designationCategorieArticle) {
        this.designationCategorieArticle = designationCategorieArticle;
    }

}
