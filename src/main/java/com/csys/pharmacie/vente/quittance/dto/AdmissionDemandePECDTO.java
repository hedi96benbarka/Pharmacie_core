package com.csys.pharmacie.vente.quittance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.validation.constraints.Size;

public class AdmissionDemandePECDTO {
    
    @Size(min = 1, max = 12)
    private String code;
    
    private String nomCompletAr;
    
    private String nomCompletEn;
    
    private String nomArMedecinTraitant;
    
    private String nomMedecinTraitant;
    
    private String codeLit;
    
    private Integer codeEtatPatient;
    
    private String designationArEtatPatient;
    
    private String designationEnEtatPatient;
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getNomCompletAr() {
        return nomCompletAr;
    }
    
    public void setNomCompletAr(String nomCompletAr) {
        this.nomCompletAr = nomCompletAr;
    }
    
    public String getNomCompletEn() {
        return nomCompletEn;
    }
    
    public void setNomCompletEn(String nomCompletEn) {
        this.nomCompletEn = nomCompletEn;
    }
    
    public String getNomArMedecinTraitant() {
        return nomArMedecinTraitant;
    }
    
    public void setNomArMedecinTraitant(String nomArMedecinTraitant) {
        this.nomArMedecinTraitant = nomArMedecinTraitant;
    }
    
    public String getNomMedecinTraitant() {
        return nomMedecinTraitant;
    }
    
    public void setNomMedecinTraitant(String nomMedecinTraitant) {
        this.nomMedecinTraitant = nomMedecinTraitant;
    }
    
    public String getCodeLit() {
        return codeLit;
    }
    
    public void setCodeLit(String codeLit) {
        this.codeLit = codeLit;
    }
    
    public Integer getCodeEtatPatient() {
        return codeEtatPatient;
    }
    
    public void setCodeEtatPatient(Integer codeEtatPatient) {
        this.codeEtatPatient = codeEtatPatient;
    }
    
    public String getDesignationArEtatPatient() {
        return designationArEtatPatient;
    }
    
    public void setDesignationArEtatPatient(String designationArEtatPatient) {
        this.designationArEtatPatient = designationArEtatPatient;
    }
    
    public String getDesignationEnEtatPatient() {
        return designationEnEtatPatient;
    }
    
    public void setDesignationEnEtatPatient(String designationEnEtatPatient) {
        this.designationEnEtatPatient = designationEnEtatPatient;
    }
    
    @JsonProperty("medecinTraitant")
    private void unpackNestedMedecinTraitant(Map<String, Object> medecinTraitant) {
        if(medecinTraitant == null){
        this.nomArMedecinTraitant = "not available";
        this.nomMedecinTraitant = "not available";
        }else {
        this.nomArMedecinTraitant = (String) medecinTraitant.get("nomIntervAr");
        this.nomMedecinTraitant = (String) medecinTraitant.get("nomInterv");
        }
    }
    
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("etatPatient")
    private void unpackNestedEtatPatient(Map<String, Object> etatPatient) {
        if (etatPatient != null) {
            this.codeEtatPatient = (Integer) etatPatient.get("code");
            this.designationArEtatPatient = (String) etatPatient.get("designationAr");
            this.designationEnEtatPatient = (String) etatPatient.get("designationEn");
        }
        
    }
    
    @Override
    public String toString() {
        return "AdmissionDemandePECDTO{" + "code=" + code + ", nomCompletAr=" + nomCompletAr + ", nomCompletEn=" + nomCompletEn + ", nomArMedecinTraitant=" + nomArMedecinTraitant + ", nomMedecinTraitant=" + nomMedecinTraitant + ", codeLit=" + codeLit + ", codeEtatPatient=" + codeEtatPatient + ", designationArEtatPatient=" + designationArEtatPatient + ", designationEnEtatPatient=" + designationEnEtatPatient + '}';
    }
    
}
