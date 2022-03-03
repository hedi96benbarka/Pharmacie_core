package com.csys.pharmacie.achat.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;

import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MvtstoRetourPerimeDTO {
//  private List<DetailMvtStoRetourPerimeDTO> detailMvtStoList;

    @Size(min = 0, max = 6)
    private String numordre;

    private Integer codtva;

    private BigDecimal tautva;

//  private FactureRetourPerime FactureRetour_perime;
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
    @NotNull
    private Integer codart;

    private String numbon;

    @Size(min = 1, max = 17)
    @NotNull
    private String lotinter;

    @NotNull
    private LocalDate datPer;
    
    

    private Date datePer;

    @NotNull
    private BigDecimal priuni;

    @NotNull
    private Integer codeUnite;

    private String designationUnite;

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }

    
    
    public String getNumordre() {
        return numordre;
    }

    public void setNumordre(String numordre) {
        this.numordre = numordre;
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

//  public FactureRetourPerime getFactureRetour_perime() {
//    return FactureRetour_perime;
//  }
//
//  public void setFactureRetour_perime(FactureRetourPerime FactureRetour_perime) {
//    this.FactureRetour_perime = FactureRetour_perime;
//  }
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

    public String getLotinter() {
        return lotinter;
    }

    public void setLotinter(String lotinter) {
        this.lotinter = lotinter;
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    public BigDecimal getPriuni() {
        return priuni;
    }

    public void setPriuni(BigDecimal priuni) {
        this.priuni = priuni;
    }

    public Date getDatePer() {
        return datePer;
    }

    public void setDatePer(Date datePer) {
        this.datePer = datePer;
    }


}
