package com.csys.pharmacie.transfert.dto;

import com.csys.pharmacie.helper.CategorieDepotEnum;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MvtStoBEDTO {

    @NotNull
    private Integer codArt;

    private String numOrdre;

    @NotNull
    @Size(min = 1, max = 50)
    private String lotInter;

  
    //prixAchat article selon unite
    private BigDecimal priuni;

    private LocalDate datPer;

    private String numBon;

    @NotNull
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

    @NotNull
    private Integer codeUnite;

    private String designationUnite;

    private BigDecimal tautva;

    private Integer codtva;


    public MvtStoBEDTO(Integer codArt, String lotInter, LocalDate datPer, CategorieDepotEnum categDepot, Integer codeUnite) {
        this.codArt = codArt;
        this.lotInter = lotInter;
        this.datPer = datPer;
        this.categDepot = categDepot;
        this.codeUnite = codeUnite;
    }

    public MvtStoBEDTO() {
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
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

    public String getNumBon() {
        return numBon;
    }

    public void setNumBon(String numBon) {
        this.numBon = numBon;
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

    public Integer getCodArt() {
        return codArt;
    }

    public void setCodArt(Integer codArt) {
        this.codArt = codArt;
    }

    public String getNumOrdre() {
        return numOrdre;
    }

    public void setNumOrdre(String numOrdre) {
        this.numOrdre = numOrdre;
    }

    public String getDesignationUnite() {
        return designationUnite;
    }

    public void setDesignationUnite(String designationUnite) {
        this.designationUnite = designationUnite;
    }


    @Override
    public String toString() {
        return "MvtStoBEDTO{" + "codArt=" + codArt + ", numOrdre=" + numOrdre + ", lotInter=" + lotInter + ", priuni=" + priuni + ", datPer=" + datPer + ", numBon=" + numBon + ", categDepot=" + categDepot + ", desart=" + desart + ", desArtSec=" + desArtSec + ", codeSaisi=" + codeSaisi + ", quantite=" + quantite + ", codeUnite=" + codeUnite + ", designationUnite=" + designationUnite + ", tautva=" + tautva + ", codtva=" + codtva + '}';
    }

}
