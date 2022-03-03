/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

/**
 *
 * @author Administrateur
 */
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DetailCommandeAchatDTO {

    private Integer code;

    

    @NotNull
    private Integer codart;

    private String codeSaisi;
    private String designation;
    private String designationSec;

    @NotNull
    private BigDecimal quantite;

    private BigDecimal quantiteGratuite;
    
    @NotNull
    private Integer codtva;

    private BigDecimal tautva;
    @NotNull
    private Integer delaiLivraison;

    private BigDecimal quantiteRestante;
    
    private BigDecimal quantiteGratuiteRestante;

    private BigDecimal prixUnitaire;
    private Boolean perissable;

    @Size(max = 150)
    private String memo;
    
    private String unitDesignation;
    private Integer codeUnite;

    @NotNull
    private BigDecimal remise;
    private Boolean isCapitalize;
    private Boolean appelOffre;
    
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
    public DetailCommandeAchatDTO(Integer code, Integer codart, BigDecimal quantite) {
        this.code = code;
        this.codart = codart;
        this.quantite = quantite;
    }

    public DetailCommandeAchatDTO(Integer code, Integer codart) {
        this.code = code;
        this.codart = codart;
    }

    
    public DetailCommandeAchatDTO(Integer code, BigDecimal quantite) {
        this.code = code;
        this.quantite = quantite;
    }

    public DetailCommandeAchatDTO() {
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public Integer getCodtva() {
        return codtva;
    }

    public void setCodtva(Integer codtva) {
        this.codtva = codtva;
    }

    public Integer getCodart() {
        return codart;
    }

    public void setCodart(Integer codart) {
        this.codart = codart;
    }

    public Integer getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(Integer delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public BigDecimal getQuantiteRestante() {
        return quantiteRestante;
    }

    public void setQuantiteRestante(BigDecimal quantiteRestante) {
        this.quantiteRestante = quantiteRestante;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getTautva() {
        return tautva;
    }

    public void setTautva(BigDecimal tautva) {
        this.tautva = tautva;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignationSec() {
        return designationSec;
    }

    public void setDesignationSec(String designationSec) {
        this.designationSec = designationSec;
    }

    public Boolean getPerissable() {
        return perissable;
    }

    public void setPerissable(Boolean perissable) {
        this.perissable = perissable;
    }

    public String getUnitDesignation() {
        return unitDesignation;
    }

    public void setUnitDesignation(String unitDesignation) {
        this.unitDesignation = unitDesignation;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getQuantiteGratuiteRestante() {
        return quantiteGratuiteRestante;
    }

    public void setQuantiteGratuiteRestante(BigDecimal quantiteGratuiteRestante) {
        this.quantiteGratuiteRestante = quantiteGratuiteRestante;
    }

    public BigDecimal getQuantiteGratuite() {
        return quantiteGratuite;
    }

    public void setQuantiteGratuite(BigDecimal quantiteGratuite) {
        this.quantiteGratuite = quantiteGratuite;
    }

    public Integer getCodeUnite() {
        return codeUnite;
    }

    public Boolean getIsCapitalize() {
        return isCapitalize;
    }

    public void setIsCapitalize(Boolean isCapitalize) {
        this.isCapitalize = isCapitalize;
    }

    public void setCodeUnite(Integer codeUnite) {
        this.codeUnite = codeUnite;
    }

    public Boolean getAppelOffre() {
        return appelOffre;
    }

    public void setAppelOffre(Boolean appelOffre) {
        this.appelOffre = appelOffre;
    }



    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.code);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DetailCommandeAchatDTO other = (DetailCommandeAchatDTO) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetailCommandeAchatDTO{" + "code=" + code + ", codart=" + codart + ", quantite=" + quantite + ", quantiteGratuite=" + quantiteGratuite + ", quantiteRestante=" + quantiteRestante + ", quantiteGratuiteRestante=" + quantiteGratuiteRestante + ", prixUnitaire=" + prixUnitaire + '}';
    }


    
}
