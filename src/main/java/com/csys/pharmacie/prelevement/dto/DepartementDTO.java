/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.prelevement.dto;

import java.util.Collection;

/**
 *
 * @author bassatine
 */
public class DepartementDTO {
   private Boolean actif ;
  
   private Collection<String> administrateurs;
   
    private Integer code;

  
    private String designation;
  
    private String[] categDepots;
    
    private boolean isPos;
    
   private String codeSaisi ;
   
   private Integer codeCostCenter;
     private Integer codeService;
  private String desService;
  private String desCostCenter;

    public DepartementDTO() {
    }

    public DepartementDTO(Boolean actif, Collection<String> administrateurs, Integer code, String designation, String[] categDepots, boolean isPos) {
        this.actif = actif;
        this.administrateurs = administrateurs;
        this.code = code;
        this.designation = designation;
        this.categDepots = categDepots;
        this.isPos = isPos;
    }


    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Collection<String> getAdministrateurs() {
        return administrateurs;
    }

    public void setAdministrateurs(Collection<String> administrateurs) {
        this.administrateurs = administrateurs;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public boolean isIsPos() {
        return isPos;
    }

    public void setIsPos(boolean isPos) {
        this.isPos = isPos;
    }

    public String getCodeSaisi() {
        return codeSaisi;
    }

    public void setCodeSaisi(String codeSaisi) {
        this.codeSaisi = codeSaisi;
    }

    public String[] getCategDepots() {
        return categDepots;
    }

    public void setCategDepots(String[] categDepots) {
        this.categDepots = categDepots;
    }

    public Integer getCodeCostCenter() {
        return codeCostCenter;
    }

    public void setCodeCostCenter(Integer codeCostCenter) {
        this.codeCostCenter = codeCostCenter;
    }

    public Integer getCodeService() {
        return codeService;
    }

    public void setCodeService(Integer codeService) {
        this.codeService = codeService;
    }

    public String getDesService() {
        return desService;
    }

    public void setDesService(String desService) {
        this.desService = desService;
    }

    public String getDesCostCenter() {
        return desCostCenter;
    }

    public void setDesCostCenter(String desCostCenter) {
        this.desCostCenter = desCostCenter;
    }

    
   
    
}
