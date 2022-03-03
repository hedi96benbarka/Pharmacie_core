/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.client.dto;

/**
 *
 * @author admin
 */
public class AdmissionFacturationDTO {
    
    private String code;

    private String codePatient;

    private String nomCompletAr;

    private String nomCompletEn;

    private NatureAdmissionDTO natureAdmission;

    private EtatPatientDTO etatPatient;


    public AdmissionFacturationDTO() {
    }

 
    public String getCodePatient() {
        return codePatient;
    }

    public void setCodePatient(String codePatient) {
        this.codePatient = codePatient;
    }

    public NatureAdmissionDTO getNatureAdmission() {
        return natureAdmission;
    }

    public void setNatureAdmission(NatureAdmissionDTO natureAdmission) {
        this.natureAdmission = natureAdmission;
    }

    public EtatPatientDTO getEtatPatient() {
        return etatPatient;
    }

    public void setEtatPatient(EtatPatientDTO etatPatient) {
        this.etatPatient = etatPatient;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
