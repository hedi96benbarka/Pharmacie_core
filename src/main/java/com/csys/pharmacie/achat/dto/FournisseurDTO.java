package com.csys.pharmacie.achat.dto;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Lenovo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FournisseurDTO {

    @Size(max = 10)
    private String code;

    @Size(max = 255)
    private String designation;

    @Size(max = 255)
    private String designationSec;

    @Size(max = 50)
    private String registreCommercial;

    @Size(max = 50)
    private String designationRegion;
    @NotNull
    private Integer delaiPaiement;
    @NotNull
    private Integer delaiLivraison;

    private String designationReglement;
    @NotNull
    private Integer codeModeReglement;

    private Boolean exonere;

    private Date dateDebutExenoration;

    private Date dateFinExenoration;

    private Boolean stopped;

    private Boolean annule;

    public FournisseurDTO(String code, String designation) {
        this.code = code;
        this.designation = designation;
    }

    public FournisseurDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getRegistreCommercial() {
        return registreCommercial;
    }

    public void setRegistreCommercial(String registreCommercial) {
        this.registreCommercial = registreCommercial;
    }

    public String getDesignationRegion() {
        return designationRegion;
    }

    public void setDesignationRegion(String designationRegion) {
        this.designationRegion = designationRegion;
    }

    public Integer getDelaiPaiement() {
        return delaiPaiement;
    }

    public void setDelaiPaiement(Integer delaiPaiement) {
        this.delaiPaiement = delaiPaiement;
    }

    public Integer getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(Integer delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public String getDesignationReglement() {
        return designationReglement;
    }

    public void setDesignationReglement(String designationReglement) {
        this.designationReglement = designationReglement;
    }

    public Integer getCodeModeReglement() {
        return codeModeReglement;
    }

    public void setCodeModeReglement(Integer codeModeReglement) {
        this.codeModeReglement = codeModeReglement;
    }

    public Boolean getExonere() {
        return exonere;
    }

    public void setExonere(Boolean exonere) {
        this.exonere = exonere;
    }

    public Date getDateDebutExenoration() {
        return dateDebutExenoration;
    }

    public void setDateDebutExenoration(Date dateDebutExenoration) {
        this.dateDebutExenoration = dateDebutExenoration;
    }

    public Date getDateFinExenoration() {
        return dateFinExenoration;
    }

    public void setDateFinExenoration(Date dateFinExenoration) {
        this.dateFinExenoration = dateFinExenoration;
    }

    public void setStopped(Boolean stopped) {
        this.stopped = stopped;
    }

    public void setAnnule(Boolean annule) {
        this.annule = annule;
    }

    public Boolean getStopped() {
        return stopped;
    }

    public Boolean getAnnule() {
        return annule;
    }

    @Override
    public String toString() {
        return "FournisseurDTO{" + "code=" + code + ", designation=" + designation + ", exonere=" + exonere + ", dateDebutExenoration=" + dateDebutExenoration + ", dateFinExenoration=" + dateFinExenoration + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.code);
        hash = 79 * hash + Objects.hashCode(this.designation);
        hash = 79 * hash + Objects.hashCode(this.registreCommercial);
        hash = 79 * hash + Objects.hashCode(this.designationRegion);
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
        final FournisseurDTO other = (FournisseurDTO) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        if (!Objects.equals(this.designation, other.designation)) {
            return false;
        }
        if (!Objects.equals(this.registreCommercial, other.registreCommercial)) {
            return false;
        }
        if (!Objects.equals(this.designationRegion, other.designationRegion)) {
            return false;
        }
        return true;
    }

}
