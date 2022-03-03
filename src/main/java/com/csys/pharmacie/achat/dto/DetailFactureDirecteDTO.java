package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DetailFactureDirecteDTO {

    @NotNull
    private Integer codtva;
    @NotNull
    private BigDecimal tautva;

    private CategorieDepotEnum categDepot;

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

    private Integer code;

    private Integer codart;

    private String numbon;
    private String designationUnite;

    @NotNull
    private BigDecimal priuni;

    private Integer unite;

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

    public String getNumbon() {
        return numbon;
    }

    public void setNumbon(String numbon) {
        this.numbon = numbon;
    }

   

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public Integer getUnite() {
        return unite;
    }

    public void setUnite(Integer unite) {
        this.unite = unite;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }
    
}
