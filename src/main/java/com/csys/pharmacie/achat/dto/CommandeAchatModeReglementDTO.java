/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csys.pharmacie.achat.dto;

import javax.validation.constraints.NotNull;

/**
 *
 * @author DELL
 */
public class CommandeAchatModeReglementDTO {
    
 private Integer codeCa;

    @NotNull
    private Integer codeReglement;
    
    private String designationReglement;

    private Integer pourcentage;

    @NotNull
    private Integer codeMotifPaiement;
    
    private String designationMotifPaiement;
    
    private Integer delaiPaiement;
    
    private Integer delaiValeurPaiement;

    public CommandeAchatModeReglementDTO() {
    }

    public CommandeAchatModeReglementDTO(Integer codeReglement, Integer pourcentage, Integer codeMotifPaiement, Integer delaiPaiement, Integer delaiValeurPaiement) {
        this.codeReglement = codeReglement;
        this.pourcentage = pourcentage;
        this.codeMotifPaiement = codeMotifPaiement;
        this.delaiPaiement = delaiPaiement;
        this.delaiValeurPaiement = delaiValeurPaiement;
    }
    
    

    public Integer getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Integer pourcentage) {
        this.pourcentage = pourcentage;
    }

    public Integer getCodeCa() {
        return codeCa;
    }

    public void setCodeCa(Integer codeCa) {
        this.codeCa = codeCa;
    }

    public Integer getCodeReglement() {
        return codeReglement;
    }

    public void setCodeReglement(Integer codeReglement) {
        this.codeReglement = codeReglement;
    }

    public Integer getCodeMotifPaiement() {
        return codeMotifPaiement;
    }

    public void setCodeMotifPaiement(Integer codeMotifPaiement) {
        this.codeMotifPaiement = codeMotifPaiement;
    }

    public Integer getDelaiPaiement() {
        return delaiPaiement;
    }

    public void setDelaiPaiement(Integer delaiPaiement) {
        this.delaiPaiement = delaiPaiement;
    }

    public Integer getDelaiValeurPaiement() {
        return delaiValeurPaiement;
    }

    public void setDelaiValeurPaiement(Integer delaiValeurPaiement) {
        this.delaiValeurPaiement = delaiValeurPaiement;
    }

    public String getDesignationReglement() {
        return designationReglement;
    }

    public void setDesignationReglement(String designationReglement) {
        this.designationReglement = designationReglement;
    }

    public String getDesignationMotifPaiement() {
        return designationMotifPaiement;
    }

    public void setDesignationMotifPaiement(String designationMotifPaiement) {
        this.designationMotifPaiement = designationMotifPaiement;
    }
    
}


