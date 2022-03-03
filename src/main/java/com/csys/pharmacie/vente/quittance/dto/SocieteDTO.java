/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrateur
 */
public class SocieteDTO {
    @Basic(optional = false)
    private Integer code;
    @Size(max = 20)
    private String codeSaisie;
    @Size(max = 200)
    private String designation;
    @Size(max = 200)
    private String designationAr;
    @Basic(optional = false)
    @NotNull
    private boolean timbre;
    @Basic(optional = false)
    private boolean facturationSuivie;
    @Basic(optional = false)
    @NotNull
    private int delaisFacturation;
    @Basic(optional = false)
    @NotNull
    private int delaisReglement;
    @Basic(optional = false)
    @NotNull
    private String niveauBordereau;
    @Basic(optional = false)
    @NotNull
    private String calculDifferencePrix;

    private boolean allNatureBordereau;

    private BigDecimal remiseFTP;

    private Integer dureeFTP;
    
    private String calculPrestationMedicale;
    
    @NotNull
    @Size(min = 1, max = 1)
    private String remiseConventionnellePharmacie;

    public SocieteDTO() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCodeSaisie() {
        return codeSaisie;
    }

    public void setCodeSaisie(String codeSaisie) {
        this.codeSaisie = codeSaisie;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDesignationAr() {
        return designationAr;
    }

    public void setDesignationAr(String designationAr) {
        this.designationAr = designationAr;
    }

    public boolean isTimbre() {
        return timbre;
    }

    public void setTimbre(boolean timbre) {
        this.timbre = timbre;
    }

    public boolean isFacturationSuivie() {
        return facturationSuivie;
    }

    public void setFacturationSuivie(boolean facturationSuivie) {
        this.facturationSuivie = facturationSuivie;
    }

    public int getDelaisFacturation() {
        return delaisFacturation;
    }

    public void setDelaisFacturation(int delaisFacturation) {
        this.delaisFacturation = delaisFacturation;
    }

    public int getDelaisReglement() {
        return delaisReglement;
    }

    public void setDelaisReglement(int delaisReglement) {
        this.delaisReglement = delaisReglement;
    }

    public String getNiveauBordereau() {
        return niveauBordereau;
    }

    public void setNiveauBordereau(String niveauBordereau) {
        this.niveauBordereau = niveauBordereau;
    }

    public String getCalculDifferencePrix() {
        return calculDifferencePrix;
    }

    public void setCalculDifferencePrix(String calculDifferencePrix) {
        this.calculDifferencePrix = calculDifferencePrix;
    }

    public boolean isAllNatureBordereau() {
        return allNatureBordereau;
    }

    public void setAllNatureBordereau(boolean allNatureBordereau) {
        this.allNatureBordereau = allNatureBordereau;
    }

    public BigDecimal getRemiseFTP() {
        return remiseFTP;
    }

    public void setRemiseFTP(BigDecimal remiseFTP) {
        this.remiseFTP = remiseFTP;
    }

    public Integer getDureeFTP() {
        return dureeFTP;
    }

    public void setDureeFTP(Integer dureeFTP) {
        this.dureeFTP = dureeFTP;
    }

    public String getCalculPrestationMedicale() {
        return calculPrestationMedicale;
    }

    public void setCalculPrestationMedicale(String calculPrestationMedicale) {
        this.calculPrestationMedicale = calculPrestationMedicale;
    }

    public String getRemiseConventionnellePharmacie() {
        return remiseConventionnellePharmacie;
    }

    public void setRemiseConventionnellePharmacie(String remiseConventionnellePharmacie) {
        this.remiseConventionnellePharmacie = remiseConventionnellePharmacie;
    }

    

}
