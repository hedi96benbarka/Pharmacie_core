package com.csys.pharmacie.prelevement.dto;

import static com.crystaldecisions12.reports.common.RootCauseID.PK;
import com.csys.pharmacie.helper.CategorieDepotEnum;
import com.csys.pharmacie.prelevement.domain.FacturePR;
import com.csys.pharmacie.prelevement.domain.MvtStoPRPK;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.lang.Integer;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MvtStoPRDTO {

    private Integer code;

    @NotNull
    private Integer articleID;
    @NotNull
    private String numbon;
    private String lotinter;

    private LocalDate datPer;

    private BigDecimal priuni;

    private Integer unite;
    private String Designationunite;

    @Enumerated(EnumType.STRING)
    private CategorieDepotEnum categDepot;

    @NotNull
    @Size(min = 0, max = 255)
    private String designation;

    @NotNull
    @Size(min = 0, max = 255)
    private String secondDesignation;

    @NotNull
    @Size(min = 0, max = 50)
    private String codeSaisi;

    private BigDecimal quantite;

    private BigDecimal quantiteNette;
    private BigDecimal quantiteGlobaleNette;
    private Integer codeEmplacement;
    private String designationEmplacement;

    public MvtStoPRDTO() {
    }

    public LocalDate getDatPer() {
        return datPer;
    }

    public void setDatPer(LocalDate datPer) {
        this.datPer = datPer;
    }

    @JsonIgnore
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getArticleID() {
        return articleID;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }

    public String getDesignationunite() {
        return Designationunite;
    }

    public void setDesignationunite(String Designationunite) {
        this.Designationunite = Designationunite;
    }

    @JsonIgnore
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSecondDesignation() {
        return secondDesignation;
    }

    public void setSecondDesignation(String secondDesignation) {
        this.secondDesignation = secondDesignation;
    }

    @JsonIgnore
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

    public CategorieDepotEnum getCategDepot() {
        return categDepot;
    }

    public void setCategDepot(CategorieDepotEnum categDepot) {
        this.categDepot = categDepot;
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

    public BigDecimal getQuantiteNette() {
        return quantiteNette;
    }

    public void setQuantiteNette(BigDecimal quantiteNette) {
        this.quantiteNette = quantiteNette;
    }

    public BigDecimal getQuantiteGlobaleNette() {
        return quantiteGlobaleNette;
    }

    public void setQuantiteGlobaleNette(BigDecimal quantiteGlobaleNette) {
        this.quantiteGlobaleNette = quantiteGlobaleNette;
    }

    public Integer getCodeEmplacement() {
        return codeEmplacement;
    }

    public void setCodeEmplacement(Integer codeEmplacement) {
        this.codeEmplacement = codeEmplacement;
    }

    public String getDesignationEmplacement() {
        return designationEmplacement;
    }

    public void setDesignationEmplacement(String designationEmplacement) {
        this.designationEmplacement = designationEmplacement;
    }

    public MvtStoPRDTO(Integer articleID, String lotinter, Integer unite, CategorieDepotEnum categDepot, BigDecimal quantite) {
        this.articleID = articleID;
        this.lotinter = lotinter;
        this.unite = unite;
        this.categDepot = categDepot;
        this.quantite = quantite;
    }



}
