/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.vente.quittance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author Administrateur
 */
public class LitDTO {

    private String numLit;

    private BigDecimal coefficient;

    private boolean actif;

    private Integer codeChambre;

    private String numeroChambre;

    private Integer codeEtage;

    private String designationArEtage;

    private String designationEtage;

    private Integer codeCostCenterAnalytique;

    public LitDTO() {
    }

    public String getNumLit() {
        return numLit;
    }

    public void setNumLit(String numLit) {
        this.numLit = numLit;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Integer getCodeChambre() {
        return codeChambre;
    }

    public void setCodeChambre(Integer codeChambre) {
        this.codeChambre = codeChambre;
    }

    public String getNumeroChambre() {
        return numeroChambre;
    }

    public void setNumeroChambre(String numeroChambre) {
        this.numeroChambre = numeroChambre;
    }

    public Integer getCodeEtage() {
        return codeEtage;
    }

    public void setCodeEtage(Integer codeEtage) {
        this.codeEtage = codeEtage;
    }

    public String getDesignationArEtage() {
        return designationArEtage;
    }

    public void setDesignationArEtage(String designationArEtage) {
        this.designationArEtage = designationArEtage;
    }

    public String getDesignationEtage() {
        return designationEtage;
    }

    public void setDesignationEtage(String designationEtage) {
        this.designationEtage = designationEtage;
    }

    public Integer getCodeCostCenterAnalytique() {
        return codeCostCenterAnalytique;
    }

    public void setCodeCostCenterAnalytique(Integer codeCostCenterAnalytique) {
        this.codeCostCenterAnalytique = codeCostCenterAnalytique;
    }

    @JsonProperty("numChambre")
    private void unpackNestedChambre(Map<String, Object> numChambre) {
        this.codeChambre = (Integer) numChambre.get("code");
        this.numeroChambre = (String) numChambre.get("numeroChambre");
        Map<String, Object> codeEtage = (Map<String, Object>) numChambre.get("codeEtage");
        this.codeEtage = (Integer) codeEtage.get("code");
        this.designationArEtage = (String) codeEtage.get("designationAr");
        this.designationEtage = (String) codeEtage.get("designation");

        Map<String, Object> departement = (Map<String, Object>) numChambre.get("departement");
        this.codeCostCenterAnalytique = (Integer) departement.get("codeCostCenter");
    }

    @Override
    public String toString() {
        return "LitDTO{" + "numLit=" + numLit + ", coefficient=" + coefficient + ", actif=" + actif + ", codeChambre=" + codeChambre + ", numeroChambre=" + numeroChambre + ", codeEtage=" + codeEtage + ", designationArEtage=" + designationArEtage + ", designationEtage=" + designationEtage + '}';
    }

}
